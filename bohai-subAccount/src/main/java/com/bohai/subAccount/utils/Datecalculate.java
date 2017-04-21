package com.bohai.subAccount.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.TableItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.utils.CommonUtil;
import com.bohai.subAccount.vo.UserPositionVO;

public class Datecalculate {
	
	static Logger logger = Logger.getLogger(Datecalculate.class);
	
	private static CommonUtil commonUtil = new CommonUtil();
    private Socket tradeSocket;
	//主画面
	public String getMainCapital(List<InvestorPosition> investorPositionsInfos, List<UserContract> userContractInfo,
			List<CloseRule> closeRule, JSONObject json) {

		String strCapital = "";
		// 持仓盈亏
		BigDecimal bigOpsition = new BigDecimal(0);
		// 合约单位
		Integer intContract = 0;
		StringBuffer stringBuffer;
		for (UserContract usercontract : userContractInfo) {
			if (json.getString("instrumentID").equals(usercontract.getContractNo().toString())) {
				intContract = usercontract.getContractUnit();
				break;
			}

		}
		
		for (InvestorPosition each : investorPositionsInfos) {

			if (each.getInstrumentid().equals(json.getString("instrumentID"))) {
				// 最小跳动
				BigDecimal tick = new BigDecimal("0");
				// 跳数
				BigDecimal hop = new BigDecimal("0");
				//持仓盈亏
				BigDecimal win = new BigDecimal("0");
				//跳数*最小跳到单位
				BigDecimal bigproduct = new BigDecimal("0");
				boolean booTick = false;
				boolean booHop = false;
				// 最小跳动单位取得
				for (UserContract usercontract : userContractInfo) {
					if (json.getString("instrumentID").equals(usercontract.getContractNo().toString())) {
						tick = usercontract.getTickSize();
						booTick = true;
						break;
					}
				}

				for (CloseRule closerule : closeRule) {
					if (json.getString("instrumentID").equals(closerule.getContractNo().toString())) {
						hop = new BigDecimal(closerule.getHop());
						booHop = true;
						break;
					}
				}
				
				//跳数*最小跳到单位
				bigproduct = tick.multiply(hop).multiply(new BigDecimal(intContract)).multiply(new BigDecimal(each.getPosition()));
				if (!each.getPosidirection().equals("0")) {

					// 持仓盈亏 = (开-最新)*合约单位*今日持仓
					win = (commonUtil.isNull(each.getOpenamount())
							.subtract(commonUtil.isNull(new BigDecimal(json.getString("lastPrice")))))
									.multiply(commonUtil.isNull(new BigDecimal(each.getPosition())))
									.multiply(new BigDecimal(intContract));

					logger.debug("卖开持仓盈亏：" + win);

					bigOpsition = bigOpsition.add(win);


				} else {

					// 持仓盈亏 = (最新-开)*合约单位*今日持仓
					win = (commonUtil.isNull(new BigDecimal(json.getString("lastPrice")))
							.subtract(commonUtil.isNull(each.getOpenamount())))
									.multiply(commonUtil.isNull(new BigDecimal(each.getPosition())))
									.multiply(new BigDecimal(intContract));
					bigOpsition = bigOpsition.add(win);
				}
				
				if (win.add(bigproduct).compareTo(new BigDecimal("0")) < 0 && booHop && booTick) {
					try {
						getTradeSocket();
		                PrintWriter out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
						stringBuffer = new StringBuffer();
		            	stringBuffer.append("risk|");
		                stringBuffer.append(each.getSubuserid() + "|");
		                stringBuffer.append("TPXZ" + json.getString("instrumentID"));
		                stringBuffer.append("|" + each.getPosidirection());
		                logger.info("跳平限制参数：" + stringBuffer.toString());
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

		strCapital = bigOpsition.toString();

		return strCapital;
	}
	
	//子画面
	public String[] getSubmainCapital(InvestorPosition2 investorPosition, List<UserContract> userContractInfo, JSONObject json) {
		
		String strRet[] = new String[2];
		//现价
		BigDecimal bigLastprice = new BigDecimal(0);
		//持仓盈亏
		BigDecimal bigOpsition = new BigDecimal(0);
		//合约单位
		Integer intContract = 0;
		

		for (UserContract usercontract : userContractInfo) {
			if (json.getString("instrumentID").equals(usercontract.getContractNo().toString())) {
				intContract = usercontract.getContractUnit();
				break;
			}
		}
		
		if (investorPosition.getInstrumentid().equals(json.getString("instrumentID"))) {
			//现价
			bigLastprice = new BigDecimal(json.getString("lastPrice"));
			if (!investorPosition.getPosidirection().equals("0")) {
				//持仓盈亏 = (开-最新)*合约单位*今日持仓
				bigOpsition = bigOpsition.add((commonUtil.isNull(investorPosition.getOpenamount())
						.subtract(commonUtil.isNull(new BigDecimal(json.getString("lastPrice")))))
								.multiply(commonUtil.isNull(investorPosition.getPosition()))
								.multiply(new BigDecimal(intContract)));		
				
			} else {
				//持仓盈亏 = (最新-开)*合约单位*今日持仓
				bigOpsition = bigOpsition.add((commonUtil.isNull(new BigDecimal(json.getString("lastPrice")))
						.subtract(commonUtil.isNull(investorPosition.getOpenamount())))
								.multiply(commonUtil.isNull(investorPosition.getPosition()))
								.multiply(new BigDecimal(intContract)));		
				
			}

		} 

		//持仓盈亏
		strRet[0] = bigOpsition.toString();
		//现价
		strRet[1] = bigLastprice.toString();
		
		return strRet;
	}
	
	//子画面(只有平仓盈亏)
	public String getSubmainCapital2(InvestorPosition2 investorPosition2, List<UserContract> userContractInfo, JSONObject json) {
		
		String strRet = "";
		//持仓盈亏
		BigDecimal bigOpsition = new BigDecimal(0);
		//合约单位
		Integer intContract = 0;

		for (UserContract usercontract : userContractInfo) {
			if (json.getString("instrumentID").equals(usercontract.getContractNo().toString())) {
				intContract = usercontract.getContractUnit();
				break;
			}
		}
		if (investorPosition2.getInstrumentid().equals(json.getString("instrumentID"))) {

			if (!investorPosition2.getPosidirection().equals("0")) {
				//持仓盈亏 = (开-最新)*合约单位*今日持仓
				bigOpsition = bigOpsition.add((commonUtil.isNull(investorPosition2.getOpenamount())
						.subtract(commonUtil.isNull(new BigDecimal(json.getString("lastPrice")))))
								.multiply(commonUtil.isNull(investorPosition2.getPosition()))
								.multiply(new BigDecimal(intContract)));	
				
			} else {
				//持仓盈亏 = (最新-开)*合约单位*今日持仓
				bigOpsition = bigOpsition.add((commonUtil.isNull(new BigDecimal(json.getString("lastPrice")))
						.subtract(commonUtil.isNull(investorPosition2.getOpenamount())))
								.multiply(commonUtil.isNull(investorPosition2.getPosition()))
								.multiply(new BigDecimal(intContract)));	
			}

		} 
		//持仓盈亏
		strRet = bigOpsition.toString();
		
		return strRet;
	}
	
	//初期化的计算
	public String[] getCapitalinit(List<InvestorPosition> investorPositionsInfos, SubTradingaccount subTradingaccount) {
		
		String[] strCapital = new String[4];
		//动态权益
		BigDecimal bigInterest = new BigDecimal(0);
		//可用资金
		BigDecimal bigFunds =  new BigDecimal(0);
		//持仓盈亏
		BigDecimal bigOpsition = new BigDecimal(0);
		//平仓盈亏
		BigDecimal bigClose = new BigDecimal(0);
		
		//先从子账户资金取出
		//可用资金
		bigFunds = commonUtil.isNull(subTradingaccount.getAvailable());
		//动态权益 = 平仓盈亏 +浮动盈亏 - 累计手续费
		bigInterest = commonUtil.isNull(subTradingaccount.getCloseprofit())
				.add(commonUtil.isNull(subTradingaccount.getPositionprofit()))
				.subtract(commonUtil.isNull(subTradingaccount.getCommission()));
		for (InvestorPosition investorPositionsInfo : investorPositionsInfos) {

			//持仓盈亏
			bigOpsition = bigOpsition.add(commonUtil.isNull(investorPositionsInfo.getPositionprofit()));
			//平仓盈亏
			bigClose = bigClose.add(commonUtil.isNull(investorPositionsInfo.getCloseprofit()));
			
		}
		strCapital[0] = String.valueOf(bigInterest);
		strCapital[1] = String.valueOf(bigFunds);
		strCapital[2] = String.valueOf(bigOpsition);
		strCapital[3] = String.valueOf(bigClose);

		return strCapital;
	}
	
	/**
	 * @author caojia
	 * 初始化主界面可用资金
	 * @param item 表格行对象
	 */
	public void initCapital(TableItem item){
		UserPositionVO userPositionVO = (UserPositionVO) item.getData();
		SubTradingaccount subTradingaccount = userPositionVO.getSubTradingaccount();
		//用户名
		item.setText(0,userPositionVO.getUserName());
		//动态权益
		item.setText(1, "0");
		//可用资金
		item.setText(2, subTradingaccount.getAvailable().toString());
		//持仓盈亏
		item.setText(3, "0");
		//平仓盈亏
		item.setText(4, "0");
	}
	
	/**
	 * @author caojia
	 * 持仓盈亏计算
	 * @param investorPositionsInfos
	 */
	public BigDecimal calcPositionWin(InvestorPosition investorPosition,UserContract contract,JSONObject market){
		
		BigDecimal positionWin = new BigDecimal("0");
		//买开的持仓盈亏 = (最新-开)*合约单位*今日持仓
		if(investorPosition.getPosidirection().equals("0")){
			positionWin = (commonUtil.isNull(new BigDecimal(market.getString("lastPrice")))
					.subtract(commonUtil.isNull(investorPosition.getOpenamount())))
							.multiply(commonUtil.isNull(new BigDecimal(investorPosition.getPosition())))
							.multiply(new BigDecimal(contract.getContractUnit())).setScale(2, RoundingMode.HALF_UP);
			logger.debug("合约："+contract.getContractNo()+"买开的持仓盈亏为："+positionWin.toString());
		}else{
		// 买开持仓盈亏 = (开-最新)*合约单位*今日持仓
			positionWin = (commonUtil.isNull(investorPosition.getOpenamount())
								.subtract(commonUtil.isNull(new BigDecimal(market.getString("lastPrice")))))
										.multiply(commonUtil.isNull(new BigDecimal(investorPosition.getPosition())))
										.multiply(new BigDecimal(contract.getContractUnit())).setScale(2, RoundingMode.HALF_UP);
			logger.debug("合约："+contract.getContractNo()+"卖开的持仓盈亏为："+positionWin.toString());
						
		}
		return positionWin;
	}
	
    public void getTradeSocket(){
        //如果socket为空 或者断开连接则创建一个socket
        if(tradeSocket == null || !(tradeSocket.isConnected() == true && tradeSocket.isClosed() == false)){
            try {
            	logger.info("=====================开始与交易服务器建立连接==============");
                tradeSocket = new Socket("10.0.0.202",3394);
                tradeSocket.setKeepAlive(false);
            } catch (Exception e) {
            	logger.error("与交易服务器通信失败",e);
            }
        }
    }

}
