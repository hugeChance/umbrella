package com.bohai.subAccount.swt.riskfront.helper;

import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import com.bohai.subAccount.entity.Trade2;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.swt.riskfront.RiskControlDialog;

public class TradeReceiveThread implements Runnable {
	
	static Logger logger = Logger.getLogger(TradeReceiveThread.class);

	private RiskControlDialog riskDialogView;
	private TradeService tradeService;
	
	public TradeReceiveThread(RiskControlDialog riskControlDialog, TradeService tradeService) {
		// TODO Auto-generated constructor stub
		this.riskDialogView = riskControlDialog;
		this.tradeService = tradeService;

	}

	@Override
	public void run() {

		while (true) {
			
			if(riskDialogView.shell.isDisposed()){
				break;
			}

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						List<Trade2> trade2 = null;
						if(riskDialogView.shell.isDisposed()){
							return;
						}
						trade2 = tradeService.getVolumes(riskDialogView.getAccountValue().getText());
						if (trade2.get(0) != null) {
							riskDialogView.getLabel().setText(trade2.get(0).getVolume().toString());
						} else {
							riskDialogView.getLabel().setText("0");
						}
					} catch (FutureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			try {
				// 若果连不到行情服务器，每隔两秒尝试一次
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}

	}

}
