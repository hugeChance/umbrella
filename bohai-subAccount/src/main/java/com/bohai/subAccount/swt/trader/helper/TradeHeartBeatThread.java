package com.bohai.subAccount.swt.trader.helper;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.bohai.subAccount.swt.trader.TraderView;

/**
 * 与交易服务器心跳
 * @author BHQH-CXYWB
 */
public class TradeHeartBeatThread implements Runnable {
	
	private Logger logger = Logger.getLogger(TradeHeartBeatThread.class);
	
	private TraderView traderView;
	
	public TradeHeartBeatThread(TraderView traderView) {
		this.traderView = traderView;
	}

	@Override
	public void run() {
		
		logger.info("----------------与交易服务器建立心跳-----------------");
		while(true){
			
			try {
				traderView.getTradeSocket().sendUrgentData(0xFF);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
