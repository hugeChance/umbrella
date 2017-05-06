package com.bohai.subAccount.swt.coreapp.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.swt.coreapp.CoreappView;

public class CtpConnectThread implements Runnable{

    static Logger logger = Logger.getLogger(CtpConnectThread.class);
    
    private CoreappView coreappView;
    
    private Socket socket;
    
    public CtpConnectThread(CoreappView coreappView, Socket socket) {
        this.coreappView = coreappView;
        this.socket = socket;
    }
    
    @Override
    public void run() {
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            
        } catch (Exception e) {
            logger.error("获取交易socket输入流失败",e);
        }
        while(true){
        
                try {
                    String result = in.readLine();
                    logger.info("收到服务器返回信息"+result);
                    
                    String[] params = result.split("\\|");
                    if(params[0].equals("onRspUserLogin")){
                        
                        JSONObject json = JSON.parseObject(params[1]);
                        CThostFtdcRspUserLoginField userLoginField = new CThostFtdcRspUserLoginField();
                        /*userLoginField.setBrokerID(json.getString("brokerID"));
                        userLoginField.setCZCETime(json.getString("cZCETime"));
                        userLoginField.setDCETime(json.getString("dCETime"));
                        userLoginField.setFFEXTime(json.getString("fFEXTime"));
                        userLoginField.setFrontID(json.getIntValue("frontID"));
                        userLoginField.setLoginTime(json.getString("loginTime"));
                        userLoginField.setMaxOrderRef(json.getString("maxOrderRef"));
                        userLoginField.setSessionID(json.getIntValue("sessionID"));
                        userLoginField.setSHFETime(json.getString("sHFETime"));
                        userLoginField.setSystemName(json.getString("systemName"));
                        userLoginField.setTradingDay(json.getString("tradingDay"));
                        userLoginField.setUserID(json.getString("userID"));*/
                        
                        JSONObject json1 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        /*pRspInfo.setErrorID(json1.getIntValue("errorID"));
                        pRspInfo.setErrorMsg(json1.getString("errorMsg"));*/
                        
                        try {
                            BeanUtils.copyProperties(userLoginField, json);
                            BeanUtils.copyProperties(pRspInfo, json1);
                        } catch (Exception e) {
                            logger.error("复制属性失败");
                            continue;
                        }
                        
                        coreappView.onRspUserLogin(userLoginField, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                    }else if (params[0].equals("onRtnOrder")) {
                        JSONObject json = JSON.parseObject(params[1]);
                        CThostFtdcOrderField pOrder = new CThostFtdcOrderField();
                        try {
                            BeanUtils.copyProperties(pOrder, json);
                        } catch (Exception e) {
                            logger.error("复制属性失败");
                            continue;
                        }
                        coreappView.onRtnOrder(pOrder);
                    }else if (params[0].equals("onRtnTrade")) {
                        JSONObject json = JSON.parseObject(params[1]);
                        CThostFtdcTradeField pTrade = new CThostFtdcTradeField();
                        try {
                            BeanUtils.copyProperties(pTrade, json);
                        } catch (Exception e) {
                            logger.error("复制属性失败");
                            continue;
                        }
                        coreappView.onRtnTrade(pTrade);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                    
        }
    }

}
