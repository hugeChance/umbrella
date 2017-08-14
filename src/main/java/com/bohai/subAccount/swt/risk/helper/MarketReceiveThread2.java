/*package com.bohai.subAccount.swt.risk.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.swt.risk.RiskControlDialog;
import com.bohai.subAccount.utils.CommonUtil;
import com.bohai.subAccount.utils.Datecalculate;

public class MarketReceiveThread2 implements Runnable {
	
	static Logger logger = Logger.getLogger(MarketReceiveThread2.class);

	private Table positionTable;
	
	private RiskControlDialog riskDialogView;
	private String useName;
	
    private Datecalculate dateCalcuate = new Datecalculate();
    
    private List<InvestorPosition2> investorPositions = null;
    
    private List<InvestorPosition2> investorPositions2 = null;
    private static CommonUtil commonUtil = new CommonUtil();
    
	//合约信息
	private List<UserContract> userContractInfos;
	
	public MarketReceiveThread2(RiskControlDialog riskControlDialog, Table positionTable,
			 String useName,
			List<UserContract> userContractInfos) {
		// TODO Auto-generated constructor stub
		this.riskDialogView = riskControlDialog;
		this.positionTable = positionTable;
		this.useName = useName;
		this.userContractInfos = userContractInfos;
	}

	@Override
	public void run() {
		
		while(true){
			
//			if(riskDialogView.shell.isDisposed()){
//				continue;
//			}
			if(riskDialogView.getSocket() == null){
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
				in = new BufferedReader(new InputStreamReader(riskDialogView.getSocket().getInputStream(),"UTF-8"));
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
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(riskDialogView.getSocket().getOutputStream(),"UTF-8"));
                        out.println("test");
                        out.flush();
                        continue;
                    }
					
					JSONObject json = JSON.parseObject(market);
					
					if(riskDialogView.shell.isDisposed()){
						continue;
					}
					Display.getDefault().syncExec(new Runnable() {
						//@SuppressWarnings("unchecked")
						@Override
						public void run() {
							investorPositions = riskDialogView.getInvestorPositions();
							investorPositions2 = riskDialogView.getInvestorPositions2();

							// TODO Auto-generated method stub
							TableItem[] tableItems = positionTable.getItems();
							//持仓盈亏
							BigDecimal bigOpsition = new BigDecimal(0);
							StringBuffer stringBuffer;
							//持仓+平仓计算返回结果
							String retResult[];
							//平仓盈亏
							String retResult2;
							if(tableItems.length != 0) {

								int i = 0;
								InvestorPosition2 investorPosition;
								
								for (TableItem tableItem : tableItems) {
									investorPosition = investorPositions.get(i);
									if (investorPosition.getInstrumentid().equals(json.getString("instrumentID"))) {
										retResult = dateCalcuate.getSubmainCapital(investorPosition,
												 userContractInfos, json);
										//现价
										tableItem.setText(3, retResult[1]);
										//持仓盈亏
										tableItem.setText(4, retResult[0]);
  									    //head持仓盈亏
										bigOpsition = bigOpsition.add(commonUtil.isNull(new BigDecimal(retResult[0])));
										riskDialogView.getPositionWinLab().setText(bigOpsition.toString());
									}
									try {
										PrintWriter out = new PrintWriter(new OutputStreamWriter(riskDialogView.getTradeSocket().getOutputStream(),"UTF-8"));
										stringBuffer = new StringBuffer();
						            	stringBuffer.append("risk|");
						                stringBuffer.append(useName.trim() + "|");
						                stringBuffer.append("CCYK|" + bigOpsition);
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
									i++;

								}
							}else {//空持仓持仓盈亏=0
								riskDialogView.getPositionWinLab().setText("0");
								PrintWriter out;
								try {
									out = new PrintWriter(new OutputStreamWriter(riskDialogView.getTradeSocket().getOutputStream(),"UTF-8"));
									stringBuffer = new StringBuffer();
					            	stringBuffer.append("risk|");
					                stringBuffer.append(useName.trim() + "|");
					                stringBuffer.append("CCYK|" + riskDialogView.getPositionWinLab().getText());
					                logger.info("持仓盈亏参数：" + stringBuffer.toString());
									out.println(stringBuffer.toString());
					                out.flush();
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							} else if (investorPositions2.size() > 0) {

								for (InvestorPosition2 each : investorPositions2) {
									if (each.getInstrumentid().equals(json.getString("instrumentID"))) {
										//retResult2 = new String[3];

										retResult2 = dateCalcuate.getSubmainCapital2(each, userContractInfos, json);

										//head持仓盈亏
										bigOpsition = bigOpsition.add(commonUtil.isNull(new BigDecimal(retResult2)));
										riskDialogView.getLblNewLabel_1().setText(bigOpsition.toString());
										try {
											PrintWriter out = new PrintWriter(new OutputStreamWriter(
													riskDialogView.getTradeSocket().getOutputStream(), "UTF-8"));
											stringBuffer = new StringBuffer();
											stringBuffer.append("risk|");
											stringBuffer.append(useName.trim() + "|");
											stringBuffer.append("CCYK|" + riskDialogView.getLblNewLabel_1().getText());
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
					});
					
				} catch (SocketException e) {
					logger.error("获取socket输入流失败",e);
					logger.debug("重新创建socket连接");
					riskDialogView.recreateSocket();
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

}
*/