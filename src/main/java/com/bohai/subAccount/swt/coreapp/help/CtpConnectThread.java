package com.bohai.subAccount.swt.coreapp.help;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoConfirmField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradingAccountField;

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
                        
                        JSONObject json1 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(userLoginField, json);
                        BeanUtils.copyProperties(pRspInfo, json1);
                        
                        coreappView.onRspUserLogin(userLoginField, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                    }else if (params[0].equals("onRtnOrder")) {
                        JSONObject json = JSON.parseObject(params[1]);
                        CThostFtdcOrderField pOrder = new CThostFtdcOrderField();
                        
                        BeanUtils.copyProperties(pOrder, json);
                        
                        pOrder.setStatusMsg(json.getString("statusMsg"));
                        
                        
                        
                        coreappView.onRtnOrder(pOrder);
                    }else if (params[0].equals("onRtnTrade")) {
                        JSONObject json = JSON.parseObject(params[1]);
                        CThostFtdcTradeField pTrade = new CThostFtdcTradeField();
                        
                        BeanUtils.copyProperties(pTrade, json);
                        
                        coreappView.onRtnTrade(pTrade);
                    }else if (params[0].equals("onRspOrderInsert")) {//报单录入请求响应
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcInputOrderField pInputOrder = new CThostFtdcInputOrderField();
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(pInputOrder, json1);
                        BeanUtils.copyProperties(pRspInfo, json2);
                        //报单录入请求响应回调
                        coreappView.onRspOrderInsert(pInputOrder, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                    }else if (params[0].equals("onRspOrderAction")) {//报单操作请求响应
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
                        
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(pInputOrderAction, json1);
                        BeanUtils.copyProperties(pRspInfo, json2);
                        
                        //报单操作请求响应回调
                        coreappView.onRspOrderAction(pInputOrderAction, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                    }else if (params[0].equals("onRspQryInvestorPositionDetail")) {//请求查询投资者持仓明细响应
                        
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcInvestorPositionDetailField pInvestorPositionDetail = new CThostFtdcInvestorPositionDetailField();
                        
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(pInvestorPositionDetail, json1);
                        BeanUtils.copyProperties(pRspInfo, json2);
                        
                        coreappView.onRspQryInvestorPositionDetail(pInvestorPositionDetail, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                        
                    }else if (params[0].equals("onRspQryInvestorPosition")) {
                        
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcInvestorPositionField pInvestorPosition = new CThostFtdcInvestorPositionField();
                        
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(pInvestorPosition, json1);
                        BeanUtils.copyProperties(pRspInfo, json2);
                        
                        coreappView.onRspQryInvestorPosition(pInvestorPosition, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                        
                    }else if (params[0].equals("onRspSettlementInfoConfirm")) {//投资者结算结果确认响应
                        
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm = new CThostFtdcSettlementInfoConfirmField();
                        
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(pSettlementInfoConfirm, json1);
                        BeanUtils.copyProperties(pRspInfo, json2);
                        
                        coreappView.onRspSettlementInfoConfirm(pSettlementInfoConfirm, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                        
                    }else if (params[0].equals("onRspError")) {
                        JSONObject json1 = JSON.parseObject(params[1]);
                        
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        BeanUtils.copyProperties(pRspInfo, json1);
                        
                        coreappView.onRspError(pRspInfo, Integer.parseInt(params[2]), Boolean.parseBoolean(params[3]));
                        
                    }else if (params[0].equals("onErrRtnOrderInsert")) {//报单录入错误回报
                        
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcInputOrderField pInputOrder = new CThostFtdcInputOrderField();
                        
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
//                        BeanUtils.copyProperties(pInputOrder, json1);
                        
                        BeanUtils.copyProperties(pRspInfo, json2);
                        
                        pInputOrder.setBrokerID(json1.getString("brokerID"));
                        pInputOrder.setBusinessUnit(json1.getString("businessUnit"));
                        pInputOrder.setCombHedgeFlag(json1.getString("combHedgeFlag"));
                        pInputOrder.setCombOffsetFlag(json1.getString("combOffsetFlag"));
//                        pInputOrder.setContingentCondition(json1.getString("contingentCondition"));
                        pInputOrder.setDirection(json1.getString("direction").charAt(0));
                        pInputOrder.setForceCloseReason(json1.getString("forceCloseReason").toCharArray()[0]);
                        pInputOrder.setGTDDate(json1.getString("gTDDate"));
                        pInputOrder.setInstrumentID(json1.getString("instrumentID"));
                        pInputOrder.setInvestorID(json1.getString("investorID"));
//                        pInputOrder.setIsAutoSuspend(json1.getString("isAutoSuspend"));
//                        pInputOrder.setIsSwapOrder(json1.getString("isSwapOrder"));
//                        pInputOrder.setLimitPrice(json1.getString("limitPrice"));
//                        pInputOrder.setMinVolume(json1.getString("minVolume"));
                        pInputOrder.setOrderPriceType(json1.getString("orderPriceType").toCharArray()[0]);
                        pInputOrder.setOrderRef(json1.getString("orderRef"));
                        pInputOrder.setRequestID(Integer.valueOf(json1.getString("requestID")));
                        pInputOrder.setStopPrice(Double.valueOf(json1.getString("stopPrice")));
                        pInputOrder.setTimeCondition(json1.getString("timeCondition").toCharArray()[0]);
                        pInputOrder.setUserForceClose(Integer.valueOf(json1.getString("userForceClose")));
                        pInputOrder.setUserID(json1.getString("userID"));
                        pInputOrder.setVolumeCondition(json1.getString("volumeCondition").toCharArray()[0]);
                        pInputOrder.setVolumeTotalOriginal(Integer.valueOf(json1.getString("volumeTotalOriginal")));
                        
                       
                        coreappView.onErrRtnOrderInsert(pInputOrder, pRspInfo);
                        
                    }else if (params[0].equals("onRspQryTradingAccount")) {//请求查询资金账户响应
                        JSONObject json1 = JSON.parseObject(params[1]);
                        CThostFtdcTradingAccountField pTradingAccount = new CThostFtdcTradingAccountField();
                        
                        JSONObject json2 = JSON.parseObject(params[2]);
                        CThostFtdcRspInfoField pRspInfo = new CThostFtdcRspInfoField();
                        
                        BeanUtils.copyProperties(pTradingAccount, json1);
                        BeanUtils.copyProperties(pRspInfo, json2);
                        
                        coreappView.onRspQryTradingAccount(pTradingAccount, pRspInfo, Integer.parseInt(params[3]), Boolean.parseBoolean(params[4]));
                        
                    }
                } catch (Exception e) {
                    logger.error("解析响应失败",e);
                    break;
                }
                
                    
        }
    }

}
