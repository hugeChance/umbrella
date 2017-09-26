package com.bohai.subAccount.swt.riskback.helper;

import org.apache.log4j.Logger;

import com.bohai.subAccount.swt.risk.RiskManageView;
import com.bohai.subAccount.utils.DateFormatterUtil;

public class HeartBeatThread implements Runnable{
	
	static Logger logger = Logger.getLogger(HeartBeatThread.class);
	
	private RiskManageView riskView;
	

	public HeartBeatThread(RiskManageView riskView){
		this.riskView = riskView;
	}

	public void run() {
		
		logger.info("==============开始与行情服务器建立心跳=============");
		while(true){

			try {
				riskView.getSocket().sendUrgentData(0xFF);
				logger.debug("send heart beat data package ! "+DateFormatterUtil.getCurrentDateStr());
			} catch (NullPointerException e) {
				//两秒重连
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					logger.error(e1);
				}
				riskView.getMarketSocket();
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