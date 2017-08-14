package com.bohai.subAccount.swt.trader.helper;

import org.apache.log4j.Logger;

import com.bohai.subAccount.swt.trader.TraderView;
import com.bohai.subAccount.utils.DateFormatterUtil;

public class HeartBeatThread implements Runnable{
	
	static Logger logger = Logger.getLogger(HeartBeatThread.class);
	
	private TraderView tradeView;
	

	public HeartBeatThread(TraderView tradeView){
		this.tradeView = tradeView;
	}

	public void run() {
		
		logger.info("==============开始与行情服务器建立心跳=============");
		while(true){

			try {
				tradeView.getSocket().sendUrgentData(0xFF);
				logger.debug("send heart beat data package ! "+DateFormatterUtil.getCurrentDateStr());
			} catch (NullPointerException e) {
				//两秒重连
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					logger.error(e1);
				}
				tradeView.initMarketSocket();
				continue;
			} catch (Exception e) {
			logger.debug("Connection refused ! "+DateFormatterUtil.getCurrentDateStr());
			logger.error("建立心跳失败",e);
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}
}