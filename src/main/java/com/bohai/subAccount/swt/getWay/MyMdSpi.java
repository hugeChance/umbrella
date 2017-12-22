package com.bohai.subAccount.swt.getWay;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.hraink.futures.ctp.socket.JCTPCallBack;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;
import org.hraink.futures.jctp.util.FileUtil;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.FutureMarketService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.utils.SpringContextUtil;

public class MyMdSpi extends JCTPMdSpi {
	
	static Logger logger = Logger.getLogger(MyMdSpi.class); 
	
	private JCTPMdApi mdApi;
	
	private JCTPCallBack callBack;
	
	private FutureMarketService futureMarketService;
	
	private GetWay getWay;
	
	public MyMdSpi(JCTPMdApi mdApi) {
		this.mdApi = mdApi;
	}
	
	public MyMdSpi(JCTPMdApi mdApi, GetWay getWay,FutureMarketService futureMarketService) {
		this.mdApi = mdApi;
		this.getWay = getWay;
		this.futureMarketService = futureMarketService;
	}
	
	public MyMdSpi(JCTPMdApi mdApi,FutureMarketService futureMarketService) {
		this.mdApi = mdApi;
		this.futureMarketService = futureMarketService;
	}
	
	public MyMdSpi(JCTPMdApi mdApi,JCTPCallBack callBack) {
		this.mdApi = mdApi;
		this.callBack = callBack;
	}
	
	@Override
	public void onFrontConnected() {
		System.out.println("准备登陆");
		//登陆
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
//		userLoginField.setBrokerID("6000");
//		userLoginField.setUserID("80509729");
//		userLoginField.setPassword("80509729");
		logger.info("准备登陆，账户信息"+JSON.toJSONString(getWay.getMainAccount()));
		/*userLoginField.setBrokerID("4080");
		userLoginField.setUserID("86001017");
		userLoginField.setPassword("074014");*/
		
		userLoginField.setBrokerID(getWay.getMainAccount().getBrokerId());
		userLoginField.setUserID(getWay.getMainAccount().getAccountNo());
		userLoginField.setPassword(getWay.getMainAccount().getPasswd());
		
		mdApi.reqUserLogin(userLoginField, 112);
		System.out.println("登陆完成");
	}
	
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("登录回调");
		System.out.println(pRspUserLogin.getLoginTime());
		//订阅
		int subResult = -1;
		
		UserContractService userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
		List<UserContract> list = null;
		try {
			list =userContractService.queryUserContractByAll();
		} catch (FutureException e) {
			Display.getDefault().syncExec(new Runnable() {
				
				@Override
				public void run() {
					MessageBox box = new MessageBox(getWay.shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("订阅行情失败");
					box.setText("警告");
					box.open();
				}
			});
		}
		
		String[] contractNo = new String[list.size()];
		if(list != null){
			for(int i = 0 ; i< list.size() ;i++){
				contractNo[i] = list.get(i).getContractNo();
			}
		}
		
		logger.debug("订阅合约信息:"+JSON.toJSONString(contractNo));
		
		try {
            String s = FileUtil.read("/合约.txt");
            String s1 = s.replaceAll("\r\n", "");
            String[] ss = s1.split(",");
            if(ss != null && ss.length >0){
                contractNo = Arrays.copyOf(contractNo, contractNo.length+ss.length);//数组扩容
                System.arraycopy(ss, 0, contractNo, contractNo.length, ss.length);
                
                logger.debug("订阅合约信息:"+JSON.toJSONString(contractNo));
            }
        } catch (Exception e) {
            logger.error("读取文件失败");
        }
		//logger.debug("读取文件合约信息："+JSON.toJSONString(ss));
		
		
		subResult = mdApi.subscribeMarketData(contractNo);
		System.out.println(subResult == 0 ? "订阅成功" : "订阅失败");
	}

	@Override
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		/*System.out.print(pDepthMarketData.getUpdateTime() + " " + pDepthMarketData.getUpdateMillisec() + "   ");
		System.out.println(pDepthMarketData.getInstrumentID()+": "+JSON.toJSONString(pDepthMarketData));*/
		//回调转发socket
		if(callBack != null){
			callBack.execute(pDepthMarketData);
		}
		//保存行情
		if(futureMarketService != null){
			System.out.println(JSON.toJSONString(pDepthMarketData));
			this.futureMarketService.saveFutureMarket(pDepthMarketData);
		}
		if(getWay != null){
			logger.debug(JSON.toJSONString(pDepthMarketData));
			getWay.printOut(JSON.toJSONString(pDepthMarketData));
		}
	}
//	
	@Override
	public void onRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		
		System.out.println("订阅回报:" + bIsLast +" : "+ pRspInfo.getErrorID()+":"+pRspInfo.getErrorMsg());
		System.out.println("InstrumentID:" + pSpecificInstrument.getInstrumentID());
	}
	
	@Override
	public void onHeartBeatWarning(int nTimeLapse) {
	}
	
	@Override
	public void onFrontDisconnected(int nReason) {
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onRspUnSubMarketData(
			CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		// TODO Auto-generated method stub
	}

	public JCTPCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(JCTPCallBack callBack) {
		this.callBack = callBack;
	}

	public FutureMarketService getFutureMarketService() {
		return futureMarketService;
	}

	public void setFutureMarketService(FutureMarketService futureMarketService) {
		this.futureMarketService = futureMarketService;
	}
	

}