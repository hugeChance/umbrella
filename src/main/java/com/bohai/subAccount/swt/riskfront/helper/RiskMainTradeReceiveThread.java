package com.bohai.subAccount.swt.riskfront.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.entity.InvestorPosition;
//import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.swt.riskfront.RiskFrontView;
import com.bohai.subAccount.vo.UserPositionVO;

public class RiskMainTradeReceiveThread implements Runnable {
	
	static Logger logger = Logger.getLogger(RiskMainTradeReceiveThread.class);
	
	private RiskFrontView riskManageView;
	
	private Table table;
	
	private InvestorPositionService investorPositionService;
	
	public RiskMainTradeReceiveThread(RiskFrontView riskManageView, Table table, InvestorPositionService investorPositionService) {
		this.riskManageView = riskManageView;
		this.investorPositionService = investorPositionService;
		this.table = table;
	}



	@Override
	public void run() {
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(riskManageView.getTradeSocket().getInputStream(),"UTF-8"));
			
		} catch (Exception e) {
			logger.error("获取交易socket输入流失败",e);
		}
		while(true){
			try {
				String result = in.readLine();
				logger.info("收到服务器返回信息"+result);
				
				String[] params = result.split("\\|");

				//成交回报 params[1]
                if(params[0].equals("riskKYZJ")) {//成交回报
                	String userName = params[1];
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							TableItem[] tableItems = table.getItems();
							for (TableItem item : tableItems) {
								if (userName.equals(item.getText(0))) {
									//结构体取得
			                        String save = params[4];
			                        JSONObject jo = JSON.parseObject(save);
									//可用资金
									String available = params[3];
									//动态权益 = 可用资金+保证金
									BigDecimal bigInterest = new BigDecimal("0");
									bigInterest = new BigDecimal(available).add(new BigDecimal(jo.getString("margin"))).setScale(2, RoundingMode.HALF_UP);										
									//动态权益
									item.setText(1, bigInterest.toString());
									//可用资金
									item.setText(2, new BigDecimal(available).setScale(2, RoundingMode.HALF_UP).toString());
									//平仓盈亏
									BigDecimal closeWin = jo.getBigDecimal("closeWin").setScale(2, RoundingMode.HALF_UP);
									item.setText(4, closeWin.toString());
									//持仓盈亏
									BigDecimal positionWin = jo.getBigDecimal("positionWin").setScale(2, RoundingMode.HALF_UP);
									
									
									//自有资金强平
									TableItem[] items =  riskManageView.getSubAccountTable().getItems();
//									for(TableItem tableItem : items){
//										
//										UserPositionVO positionVO = (UserPositionVO) tableItem.getData();
//										if(positionVO.getUserName().equals(userName)){
//											
//											BigDecimal limit = positionVO.getSubTradingaccount().getUSER_CAPITAL().subtract(
//													positionVO.getSubTradingaccount().getUSER_CAPITAL_YESTORDAY()==null?new BigDecimal("0"):positionVO.getSubTradingaccount().getUSER_CAPITAL_YESTORDAY());
//											
//											//已亏损金额
//											limit = limit.add(closeWin).add(positionWin);
//											
//											//强平比例
//											String closeRate = tableItem.getText(5);
//											if(!StringUtils.isEmpty(closeRate)){
//												BigDecimal closeRateBig = new BigDecimal(closeRate);
//												//允许亏损的最大值 = 自有资金*强平比例
//												BigDecimal closeAmountBig = closeRateBig.multiply(positionVO.getSubTradingaccount().getUSER_CAPITAL());
//												
//												if(closeAmountBig.compareTo(limit) <= 0){
//													riskManageView.forceCloseByUserName(userName);
//												}
//											}
//											//强平金额
//											String closeAmount = tableItem.getText(6);
//											if(!StringUtils.isEmpty(closeAmount)){
//												BigDecimal closeAmountBig = new BigDecimal(closeAmount);
//												
//												//允许亏损的最大金额 <= 已亏损金额 ，则强平 
//												if( closeAmountBig.compareTo(limit) <= 0){
//													riskManageView.forceCloseByUserName(userName);
//												}
//											}
//											break;
//										}
//										
//									}
										
										
									    
								}
							}
							
						}
					});
                	
                } else if(params[0].equals("onPosition")) {
                	
                	String userName = params[1];
                	if(params[2].equals("0")){//持仓为空
                        //刷新持仓表
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                            	if(riskManageView.getSubAccountTable().getItemCount()>0){
                            		TableItem[] items = riskManageView.getSubAccountTable().getItems();
                            		for(TableItem item :items){
                            			if(item.getText(0).equals(userName)){
                            				UserPositionVO userPositionVO = (UserPositionVO) item.getData();
                            				userPositionVO.setInvestorPositions(null);
                            				//将用户持仓信息设为空
                            				item.setData(userPositionVO);
                            			}
                            		}
                            	}
                            }
                        });
                        continue;
                    }
                    
                    if(params[2].equals("1")){//如果是重新推送则清空持仓表
                        //刷新持仓表
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                            	if(riskManageView.getSubAccountTable().getItemCount()>0){
                            		TableItem[] items = riskManageView.getSubAccountTable().getItems();
                            		for(TableItem item :items){
                            			if(item.getText(0).equals(userName)){
                            				UserPositionVO userPositionVO = (UserPositionVO) item.getData();
                            				
                            				//查询持仓表
                            				try {
                            					List<InvestorPosition> investorPositions = investorPositionService.getUserUnClosePostion(userName);
                            					userPositionVO.setInvestorPositions(investorPositions);
                        					} catch (FutureException e) {
                        						// TODO Auto-generated catch block
                        						e.printStackTrace();
                        					}
                            				item.setData(userPositionVO);
                            			}
                            		}
                            	}
                            }
                        });
                    }
                	
                	/*List<InvestorPosition> investorPositions = null;
                	//List<InvestorPosition2> investorPositions2 = null;
					try {
						investorPositions = investorPositionService.getUserByUserName(userName);
						//investorPositions2 = investorPositionService.getUserByUserName2(userName);
					} catch (FutureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					riskManageView.savePosition(userName, investorPositions);*/
					//riskManageView.savePosition(params[1] + "close", investorPositions2);
                	
                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
