package com.bohai.subAccount.swt.riskback.helper;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_D_Buy;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_D_Sell;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_LimitPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.swt.riskback.RiskManageView;
import com.bohai.subAccount.utils.Datecalculate;
import com.bohai.subAccount.vo.UserPositionVO;

public class RiskMainMarketReceiveThread implements Runnable {
	
	static Logger logger = Logger.getLogger(RiskMainMarketReceiveThread.class);

	private Table table;
	
	private RiskManageView riskView;
	
    private Datecalculate dateCalcuate = new Datecalculate();
	
	//持仓信息
	@SuppressWarnings("rawtypes")
	private HashMap hsInvestorPositionsInfos;
	
	private List<CloseRule> closeRules;
	
	//合约信息
	private List<UserContract> userContractInfos;
	
	public RiskMainMarketReceiveThread(RiskManageView riskView, Table table, List<UserContract> userContractInfos,
			List<CloseRule> closeRules) {
		this.riskView = riskView;
		this.table = table;
		this.userContractInfos = userContractInfos;
		this.closeRules = closeRules;
	}
	
	@Override
	public void run() {
		
		while(true){
			
			if(riskView.getSocket() == null) {
				try {
					//若果连不到行情服务器，每隔两秒尝试一次
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error(e);
				}
				continue;
			}
			
			BufferedReader in = null;
			try {
				logger.debug("获取socket输入流");
				in = new BufferedReader(new InputStreamReader(riskView.getSocket().getInputStream(),"UTF-8"));
			} catch (Exception e) {
				logger.error("获取socket输入流失败",e);
				try {
					Thread.sleep(5000);
					continue;
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			while(true){
				try {
					String market = in.readLine();
					
					//心跳测试
                    if(market.equals("test")){
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(riskView.getSocket().getOutputStream(),"UTF-8"));
                        out.println("test");
                        out.flush();
                        continue;
                    }
					
					JSONObject json = JSON.parseObject(market);
					//合约编号
					String instrumentID = json.getString("instrumentID");
					
					//合约信息
					UserContract contract = this.getContractByContractNo(instrumentID);
					
					//用户名
					String userName = contract.getUserName();
					
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							
							if(table.getItemCount() < 1){
								return;
							}
							
							BigDecimal totalPositionWin = new BigDecimal("0");
							//循环子账户表格，找到当前合约对应的用户
							for (TableItem item : table.getItems()) {
								if(userName.equals(item.getText())){
									
									UserPositionVO positionVO = (UserPositionVO) item.getData();
									//有持仓的情况下计算总持仓盈亏
									if(positionVO.getInvestorPositions() != null && positionVO.getInvestorPositions().size() > 0){
										
										for(InvestorPosition investorPosition : positionVO.getInvestorPositions()){
											//如果是当前行情推送合约，则计算最新浮动盈亏
											if(investorPosition.getInstrumentid().equals(instrumentID)){
												BigDecimal positionWin = dateCalcuate.calcPositionWin(investorPosition, contract, json);
												
												//缓存买一价
												investorPosition.setBidPrice1(json.getBigDecimal("bidPrice1"));
												//缓存卖一价
												investorPosition.setAskPrice1(json.getBigDecimal("askPrice1"));
												//缓存涨停价
												investorPosition.setUpperLimitPrice(json.getBigDecimal("upperLimitPrice"));
												//缓存跌停价
												investorPosition.setLowerLimitPrice(json.getBigDecimal("lowerLimitPrice"));
												//缓存最新价
												investorPosition.setLastPrice(json.getBigDecimal("lastPrice"));
												//强平判断
												riskForceClose(investorPosition, contract, positionWin);
												
												//缓存此次计算的浮动盈亏
												investorPosition.setPositionWin(positionWin);
												
												totalPositionWin = totalPositionWin.add(positionWin);
											}else {
												//不是当前推送的合约，取上次计算过的浮动盈亏
												BigDecimal positionWin = investorPosition.getPositionWin()==null?new BigDecimal("0"):investorPosition.getPositionWin();
												totalPositionWin = totalPositionWin.add(positionWin);
											}
										}
									}
									
									//持仓盈亏添加到表单
									item.setText(3,totalPositionWin.toString());
									break;
								}
								
							}
							
							try {
								PrintWriter out = new PrintWriter(new OutputStreamWriter(riskView.getTradeSocket().getOutputStream(),"UTF-8"));
								StringBuffer sb = new StringBuffer();
								sb.append("risk|");
								sb.append(userName.trim() + "|");
								sb.append("CCYK|" + totalPositionWin);
				                logger.info("持仓盈亏参数：" + sb.toString());
								out.println(sb.toString());
				                out.flush();
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
					
					
					
					/*Display.getDefault().syncExec(new Runnable() {
						@SuppressWarnings("unchecked")
						@Override
						public void run() {
							// TODO Auto-generated method stub
							hsInvestorPositionsInfos = riskView.getInvestorPositionsInfos();
							
							logger.debug("参数打印："+JSON.toJSONString(hsInvestorPositionsInfos));
							
							TableItem[] tableItems = table.getItems();
							if(tableItems.length != 0) {
								
								//计算返回结果
								String retResult;
 
								StringBuffer stringBuffer;
								for (TableItem tableItem : tableItems) {
									//子账户名
									String useName = tableItem.getText(0);

									//子账户持仓信息
									List<InvestorPosition> arrInvestorPositionsInfo = null;
									//List<InvestorPosition2> arrInvestorPositionsInfo2 = null;
									arrInvestorPositionsInfo = (List<InvestorPosition>) hsInvestorPositionsInfos.get(useName);
									//arrInvestorPositionsInfo2 = (List<InvestorPosition2>) hsInvestorPositionsInfos.get(useName + "close");								
									if (null != arrInvestorPositionsInfo && arrInvestorPositionsInfo.size() > 0) {
										//持仓信息和行情合约不匹配的时候不进入
										for (InvestorPosition each : arrInvestorPositionsInfo) {
											if (each.getInstrumentid().equals(json.getString("instrumentID"))) {
												retResult = dateCalcuate.getMainCapital(arrInvestorPositionsInfo,
														userContractInfos, closeRules, json);
												tableItem.setText(3, retResult);
												try {
									                PrintWriter out = new PrintWriter(new OutputStreamWriter(riskView.getTradeSocket().getOutputStream(),"UTF-8"));
													stringBuffer = new StringBuffer();
									            	stringBuffer.append("risk|");
									                stringBuffer.append(useName.trim() + "|");
									                stringBuffer.append("CCYK|" + tableItem.getText(3));
									                logger.info("持仓盈亏参数：" + stringBuffer.toString());
													out.println(stringBuffer.toString());
									                out.flush();
												} catch (UnsupportedEncodingException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												};
											}
											
										}

									}
				            
								}
							} 
						}
					});*/
					
				} catch (SocketException e) {
					logger.error("获取socket输入流失败",e);
					logger.debug("重新创建socket连接");
					riskView.recreateSocket();
					break;
				}catch (Exception e1){
					logger.error("系统异常",e1);
					break;
				}
			}
			//睡眠5秒
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	/**
     * 根据合约编号查询合约属性
     * @param contractNo
     * @return
     */
    public UserContract getContractByContractNo(String contractNo){
        
        //logger.debug("查询"+contractNo+"合约属性");
        
        UserContract userContract = null;
        
        if(this.userContractInfos != null){
            for(UserContract contract : userContractInfos){
                if(contractNo.equals(contract.getContractNo())){
                    userContract = contract;
                    break;
                }
            }
        }
        //logger.debug("返回"+contractNo+"合约属性"+JSON.toJSONString(userContract));
        return userContract;
    }
    
    /**
     * 风控强平
     * @param instrumentID
     */
    public void riskForceClose(InvestorPosition investorPosition,UserContract contract,BigDecimal positionWin){
    	
    	if(closeRules == null || closeRules.size() <1 ){
    		return ;
    	}
    	
    	for(CloseRule closeRule:closeRules){
    		if(investorPosition.getInstrumentid().equals(closeRule.getContractNo()) && contract.getUserName().equals(closeRule.getUserName())){
    			
    			if(closeRule.getHop() == null){
    				return;
    			}else {
    				//亏损金额极限 = 跳数*最小跳动单位*手数*合约单位
    				BigDecimal limit = contract.getTickSize().multiply(new BigDecimal(closeRule.getHop()))
    						.multiply(new BigDecimal(investorPosition.getPosition())).multiply(new BigDecimal(contract.getContractUnit()));
    				logger.info("允许亏损的极限值："+limit);
    				if(limit.add(positionWin).compareTo(new BigDecimal("0")) <= 0){
    					logger.info("触发强平，持仓价："+investorPosition.getOpenamount()+",已亏损："+positionWin);
    					PrintWriter out;
						try {
							out = new PrintWriter(new OutputStreamWriter(riskView.getTradeSocket().getOutputStream(),"UTF-8"));
							StringBuffer sb = new StringBuffer();
	    					sb.append("risk|");
	    					sb.append(investorPosition.getSubuserid());
	    					sb.append("|FKQP");
	    					
	    					//报单入参
							CThostFtdcInputOrderField inputOrderField = new CThostFtdcInputOrderField();
							//合约代码
							String instrumentid = investorPosition.getInstrumentid();
							inputOrderField.setInstrumentID(instrumentid);
							//数量
							inputOrderField.setVolumeTotalOriginal(investorPosition.getPosition().intValue());
							//平今仓
							inputOrderField.setCombOffsetFlag("3");
							//投资者代码
							inputOrderField.setInvestorID(contract.getUserName());
							// 用户代码
							inputOrderField.setUserID(contract.getUserName());
							// 报单价格条件
							inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
							// 组合投机套保标志
							inputOrderField.setCombHedgeFlag("1");
							// 有效期类型
							inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
							// GTD日期
							inputOrderField.setGTDDate("");
							// 成交量类型
							inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
							// 最小成交量
							inputOrderField.setMinVolume(0);
							// 触发条件
							inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
							// 止损价
							inputOrderField.setStopPrice(0);
							// 强平原因
							inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
							// 自动挂起标志
							inputOrderField.setIsAutoSuspend(0);
							//合约属性
							if(investorPosition.getPosidirection().equals("0")){
								logger.debug("买开强平持仓信息："+JSON.toJSONString(investorPosition));
								//买开强平 ---> 卖平
								inputOrderField.setDirection(THOST_FTDC_D_Sell);
								//价格 = 对手价（买价） - 10跳
								BigDecimal price = investorPosition.getBidPrice1().subtract(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
								//跌停价
								BigDecimal lowerLimitPrice = investorPosition.getLowerLimitPrice();
								if(price.compareTo(lowerLimitPrice)<0){
									//如果价格小于跌停价，就用跌停价
									price = lowerLimitPrice;
								}
								logger.debug("强平价格："+price);
								inputOrderField.setLimitPrice(price.doubleValue());
								
							}else {
								logger.debug("卖开强平持仓信息："+JSON.toJSONString(investorPosition));
								//卖开强平 ---> 买平
								inputOrderField.setDirection(THOST_FTDC_D_Buy);
								//价格 = 对手价（卖价） + 10跳
								BigDecimal price = investorPosition.getAskPrice1().add(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
								//涨停价
								BigDecimal upperLimitPrice = investorPosition.getUpperLimitPrice();
								if(price.compareTo(upperLimitPrice)>0){
									//如果价格大于涨停价，就用涨停价
									price = upperLimitPrice;
								}
								logger.debug("强平价格："+price);
								inputOrderField.setLimitPrice(price.doubleValue());
							}
							sb.append("|"+JSON.toJSONString(inputOrderField));
	    					
			                logger.info("止损强平参数：" + sb.toString());
							out.println(sb.toString());
			                out.flush();
						} catch (Exception e) {
							logger.error("止损强平异常",e);
						}
    					
    				}
				}
    			break;
    			
    		}
    	}
    	
    }

}
