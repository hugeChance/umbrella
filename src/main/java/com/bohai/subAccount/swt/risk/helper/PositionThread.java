package com.bohai.subAccount.swt.risk.helper;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.swt.risk.RiskControlDialog;
import com.bohai.subAccount.swt.risk.RiskManageView;
import com.bohai.subAccount.vo.UserPositionVO;

/**
 * 刷新子画面持仓信息
 * @author BHQH-CXYWB
 */
public class PositionThread implements Runnable {
	
	static Logger logger = Logger.getLogger(PositionThread.class);
	
	private RiskControlDialog dialog;
	
	private String userName;
	
	private RiskManageView manageView;
	
	public PositionThread(String userName, RiskControlDialog dialog, RiskManageView manageView) {
		this.userName = userName;
		this.dialog = dialog;
		this.manageView = manageView;
	}
	
	@Override
	public void run() {

		
		while(true){
			
			if(dialog.shell.isDisposed()){
				break;
			}
			Display.getDefault().syncExec(new Runnable() {
				
				@Override
				public void run() {
					try {
						TableItem[] items = manageView.getSubAccountTable().getItems();
						
						for(TableItem item :items){
							
							if(item.getText().equals(userName)){
								
								UserPositionVO positionVO = (UserPositionVO) item.getData();
								
								//总平仓盈亏
								dialog.getCloseWinLab().setText(item.getText(4));
								//总持仓盈亏
								dialog.getPositionWinLab().setText(item.getText(3));
								//动态权益
								dialog.getRightValue().setText(item.getText(1));
								//可用资金
								dialog.getAvailableLab().setText(item.getText(2));
								
								if(positionVO.getInvestorPositions() ==null || positionVO.getInvestorPositions().size() <1){
									dialog.getPositionTable().removeAll();
									
								}else {
									dialog.getPositionTable().removeAll();
									for(InvestorPosition position : positionVO.getInvestorPositions()){
										//for(TableItem dialogItem :)
										TableItem dialogItem = new TableItem(dialog.getPositionTable(), SWT.NONE);
										//合约
										dialogItem.setText(0,position.getInstrumentid());
										//买卖数量
										String direction = position.getPosidirection().equals("0")?"买":"卖";
										dialogItem.setText(1,direction+position.getPosition().toString());
										//持仓均价
										dialogItem.setText(2,position.getOpenamount().toString());
										//现价
										dialogItem.setText(3, StringUtils.isEmpty(position.getLastPrice())?"0":position.getLastPrice().toString());
										//持仓盈亏
										dialogItem.setText(4, StringUtils.isEmpty(position.getPositionWin())?"0":position.getPositionWin().toString());
										//可平量
										dialogItem.setText(5,position.getPosition().toString());
									}
								}
								break;
							}
						}
					} catch (Exception e) {
						logger.error("线程异常",e);
					}
					
				}
			});
			
			
			try {
				Thread.sleep(333);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
