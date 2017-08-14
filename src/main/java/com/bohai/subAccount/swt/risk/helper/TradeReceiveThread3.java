/*package com.bohai.subAccount.swt.risk.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.swt.risk.RiskControlDialog;

public class TradeReceiveThread3 implements Runnable {
	
	static Logger logger = Logger.getLogger(TradeReceiveThread3.class);
	
	private RiskControlDialog riskDialog;
	
	private String userName;
	
	private InvestorPositionService investorPositionService;
	
	public TradeReceiveThread3(RiskControlDialog riskDialog, InvestorPositionService investorPositionService, String userName) {
		this.riskDialog = riskDialog;
		this.investorPositionService = investorPositionService;
		this.userName = userName;
	}

	@Override
	public void run() {
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(riskDialog.getTradeSocket().getInputStream(),"UTF-8"));
			
		} catch (Exception e) {
			logger.error("获取交易socket输入流失败",e);
		}
		while(true){
			
			try {
				String result = in.readLine();
				logger.info("收到服务器返回信息"+result);
				
				String[] params = result.split("\\|");
				
				if(!userName.equals(params[1])){
					continue;
				}
				
				//成交回报 params[1]
                if(params[0].equals("riskKYZJ")) {//成交回报
                	
        			if(riskDialog.shell.isDisposed()){
        				continue;
        			}
					Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (userName.equals(riskDialog.getAccountValue().getText())) {
								//结构体取得
		                        String save = params[4];
		                        JSONObject jo = JSON.parseObject(save);
								//可用资金
								String strbigFunds = params[3];
								//动态权益
								BigDecimal bigInterest = new BigDecimal("0");
								bigInterest = new BigDecimal(strbigFunds).add(new BigDecimal(jo.getString("margin")));										

								//head平仓盈亏
								riskDialog.getCloseWinLab().setText(jo.getString("closeWin"));
								//动态权益
								riskDialog.getRightValue().setText(bigInterest.toString());
								//可用资金
								riskDialog.getAvailableLab().setText(strbigFunds);
							}
							
						}
					});
                	
                } else if (params[0].equals("onPosition")) {
                	
                	if(params[2].equals("0")){//持仓为空
                        //刷新持仓表
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                            	riskDialog.getPositionTable().removeAll();
                            }
                        });
                        continue;
                    }
                    
                    if(params[2].equals("1")){//如果是重新推送则清空持仓表
                    	
                        //刷新持仓表
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                            	riskDialog.getPositionTable().removeAll();
                            	List<InvestorPosition2> investorPositions = new ArrayList<InvestorPosition2>();
                            	try {
            						investorPositions = investorPositionService.getUserByUserName2(userName);
            					} catch (FutureException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            					riskDialog.setInvestorPositions(investorPositions);
                            	for(InvestorPosition2 position2:investorPositions){
                            		TableItem item = new TableItem(riskDialog.getPositionTable(), SWT.NONE);
                            		item.setData(position2);
                            		//合约代号
                            		item.setText(0, position2.getInstrumentid());
                    				//买卖数量
                            		item.setText(1, position2.getTradingvolume().toString());
                    				//持仓均价
                            		item.setText(2, position2.getAverageposition().toString());
                    				//现价
                            		item.setText(3, "0");
                    				//持仓盈亏
                            		item.setText(4, "0");
                    				//可平量
                            		item.setText(5, position2.getAdjustablequantity().toString());
                            	}
                            }
                        });
                    }
                    
                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
*/