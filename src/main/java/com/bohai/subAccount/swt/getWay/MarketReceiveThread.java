package com.bohai.subAccount.swt.getWay;

import java.io.BufferedReader;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Text;
import org.hraink.futures.jctp.md.JCTPMdApi;

import com.bohai.subAccount.service.FutureMarketService;
import com.bohai.subAccount.utils.ApplicationConfig;

public class MarketReceiveThread implements Runnable{
	
	static Logger logger = Logger.getLogger(MarketReceiveThread.class);
	
	//实盘行情
	//public static String frontAddr = "tcp://180.169.116.119:41213";
	public static String frontAddr = ApplicationConfig.getProperty("marketFrontAddr");
	//测试行情
	//public static String frontAddr = "tcp://180.169.116.120:41213";
	/** 行情API **/
	public static JCTPMdApi mdApi;
	public static MyMdSpi mdSpi;
	
	private GetWay getWay;
	private FutureMarketService marketService;
	
	public MarketReceiveThread(GetWay getWay,FutureMarketService marketService) {
		this.getWay = getWay;
		this.marketService = marketService;
	}

	@Override
	public void run() {
		
		mdApi = JCTPMdApi.createFtdcTraderApi();
		
		//行情service
		
		
		mdSpi = new MyMdSpi(mdApi,getWay,marketService);
		//注册spi
		mdApi.registerSpi(mdSpi);
		//注册前置机地址
		mdApi.registerFront(frontAddr);
		mdApi.Init();
		
		mdApi.Join();
		
//		TimeUnit.SECONDS.sleep(5);
		mdApi.Release();
		
	}

}
