package com.bohai.subAccount.swt.trader.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketException;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.swt.trader.TraderView;

public class MarketReceiveThread implements Runnable {
    
    static Logger logger = Logger.getLogger(MarketReceiveThread.class);

    private Table table;
    
    private TraderView traderView;
    
    public MarketReceiveThread(TraderView traderView,Table table) {
        this.traderView = traderView;
        this.table = table;
    }
    
    @Override
    public void run() {
        
        while(true){
            
            if(traderView.getSocket() == null){
                traderView.recreateSocket();
                try {
                    //若果连不到行情服务器，每隔两秒尝试一次
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
                continue;
            }
            
            BufferedReader in = null;
            try {
                logger.debug("获取socket输入流");
                in = new BufferedReader(new InputStreamReader(traderView.getSocket().getInputStream(),"UTF-8"));
            } catch (Exception e) {
                logger.error("获取socket输入流失败",e);
                try {
                    Thread.sleep(1000);
                    traderView.recreateSocket();
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
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(traderView.getSocket().getOutputStream(),"UTF-8"));
                        out.println("test");
                        out.flush();
                        continue;
                    }
                    
                    //FutureMarket futureMarket = (FutureMarket) JSON.parse(market);
                    
                    JSONObject json = JSON.parseObject(market);
                    
                    //更新行情
                    Display.getDefault().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            TableItem[] tableItems = table.getItems();
                            if(tableItems.length  > 0){
                                for (TableItem tableItem : tableItems) {
                                    if(tableItem.getText(0).equals(json.getString("instrumentID"))){
                                        //最新价
                                        tableItem.setText(1, json.getBigDecimal("lastPrice").setScale(1, RoundingMode.HALF_UP).toString());
                                        
                                        //买量
                                        Integer bidVolume1 = json.getInteger("bidVolume1");
                                        tableItem.setText(2, bidVolume1.toString());
                                        tableItem.setForeground(2, SWTResourceManager.getColor(242,14,14));
                                        //买价
                                        Double buyPrice = json.getDouble("bidPrice1");
                                        if(buyPrice.equals(Double.MAX_VALUE)||buyPrice.equals(Double.MIN_VALUE)){
                                        	//如果是极限值 买价等于跌停价
                                        	buyPrice = json.getDouble("lowerLimitPrice");
                                        }
                                        tableItem.setText(3, new BigDecimal(buyPrice).setScale(1, RoundingMode.HALF_UP).toString());
                                        tableItem.setForeground(3, SWTResourceManager.getColor(242,14,14));
                                        
                                        //卖价
                                        Double sellPrice = json.getDouble("askPrice1");
                                        if(sellPrice.equals(Double.MAX_VALUE)||sellPrice.equals(Double.MIN_VALUE)){
                                        	//如果是极限值   卖价就是涨停价
                                        	sellPrice = json.getDouble("upperLimitPrice");
                                        }
                                        tableItem.setText(4, new BigDecimal(sellPrice).setScale(1, RoundingMode.HALF_UP).toString());
                                        tableItem.setForeground(4, SWTResourceManager.getColor(78,178,88));
                                        //卖量
                                        Integer askVolume1 = json.getInteger("askVolume1");
                                        tableItem.setText(5, askVolume1.toString());
                                        tableItem.setForeground(5, SWTResourceManager.getColor(78,178,88));
                                        //涨跌 = 最新价 - 昨收盘
                                        BigDecimal change= new BigDecimal(json.getString("lastPrice")).subtract(new BigDecimal(json.getString("preSettlementPrice"))).setScale(1, RoundingMode.HALF_UP);
                                        tableItem.setText(6, String.valueOf(change));
                                        if(change.compareTo(new BigDecimal("0"))>0){
                                        	//红色
                                            tableItem.setForeground(6, SWTResourceManager.getColor(242,14,14));
                                            tableItem.setForeground(7, SWTResourceManager.getColor(242,14,14));
                                        }else if(change.compareTo(new BigDecimal("0")) < 0){
                                        	//绿色
                                            tableItem.setForeground(6, SWTResourceManager.getColor(78,178,88));
                                            tableItem.setForeground(7, SWTResourceManager.getColor(78,178,88));
                                        }
                                        //涨跌幅
                                        BigDecimal changeRate = change.divide(new BigDecimal(json.getString("preSettlementPrice")), 4, RoundingMode.HALF_UP);
                                        changeRate = changeRate.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);;
                                        tableItem.setText(7, changeRate+"%");
                                        //涨停价
                                        BigDecimal upperLimitPrice = json.getBigDecimal("upperLimitPrice").setScale(1, RoundingMode.HALF_UP);
                                        tableItem.setText(8, upperLimitPrice.toString());
                                        //跌停价
                                        BigDecimal lowerLimitPrice = json.getBigDecimal("lowerLimitPrice").setScale(1, RoundingMode.HALF_UP);
                                        tableItem.setText(9, lowerLimitPrice.toString());
                                        
                                        //最高价
                                        BigDecimal highestPrice = json.getBigDecimal("highestPrice").setScale(1, RoundingMode.HALF_UP);
                                        tableItem.setText(10, highestPrice.toString());
                                        //最低价
                                        BigDecimal lowestPrice = json.getBigDecimal("lowestPrice").setScale(1, RoundingMode.HALF_UP);
                                        tableItem.setText(11, lowestPrice.toString());
                                        //成交量
                                        Integer volume = json.getInteger("volume");
                                        tableItem.setText(12, volume.toString());
                                        //持仓量
                                        BigDecimal openInterest = json.getBigDecimal("openInterest").setScale(0);
                                        tableItem.setText(13, openInterest.toString());
                                    }
                                }
                            }
                            
                            String contract = traderView.getCombo().getText();
                            if(!StringUtils.isEmpty(contract) && contract.equals(json.getString("instrumentID"))){
                                //更新买卖价格
                                traderView.sellLabel.setText("卖  "+json.getBigDecimal("askPrice1").setScale(2,RoundingMode.HALF_UP).toString()+"   /   "+json.getInteger("askVolume1"));
                                traderView.buyLabel.setText("买  "+json.getBigDecimal("bidPrice1").setScale(2,RoundingMode.HALF_UP).toString()+"   /   "+json.getInteger("bidVolume1"));
                            }
                            
                        }
                    });
                    
                    
                    //更新持仓
                    Display.getDefault().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            
                            try {
                                TableItem[] items = traderView.getPositionTable().getItems();
                                if(items == null || items.length < 1){
                                    return;
                                }
                                for(TableItem item : items){
                                    if(item.getText(0).equals(json.getString("instrumentID"))){
                                        //最新价
                                        item.setText(6,json.getBigDecimal("lastPrice").setScale(1, RoundingMode.HALF_UP).toString());
                                        UserContract contract = traderView.getContractByContractNo(json.getString("instrumentID"));
                                        
                                        //合约单位
                                        Integer contractUnit = contract.getContractUnit();
                                        //买开  持仓盈亏  = （最新价-持仓价）*合约单位*手数 
                                        
                                        BigDecimal d = new BigDecimal("0");
                                        String lastPrice = json.getString("lastPrice");
                                        //logger.debug("最新价："+lastPrice);
                                        String positionPrice = item.getText(5);
                                        //logger.debug("持仓价："+positionPrice);
                                        //logger.debug("合约单位："+contractUnit.toString());
                                        String volumn = item.getText(2);
                                        //logger.debug("手数："+volumn);
                                        
                                        d = (new BigDecimal(lastPrice).subtract(new BigDecimal(positionPrice)))
                                        		.multiply(new BigDecimal(contractUnit.toString())).multiply(new BigDecimal(volumn)).setScale(0, RoundingMode.HALF_UP);
                                        if(item.getText(1).equals("买")){
                                            //logger.debug("买入开仓盈亏 = （最新价-持仓价）*合约单位*手数 ");
                                            //logger.debug("持仓盈亏："+d.toString());
                                            item.setText(7, d.toString());
                                        }else {
                                            //logger.debug("卖出开仓盈亏 = （持仓价-最新价）*合约单位*手数 ");
                                            d = d.multiply(new BigDecimal("-1"));
                                            //logger.debug("持仓盈亏："+ d.toString());
                                            item.setText(7, d.toString());
                                        }
                                        
                                        if(d.compareTo(new BigDecimal("0"))>0){
                                            //item.setBackground(5, SWTResourceManager.getColor(249,204,226));
                                            item.setForeground(7, SWTResourceManager.getColor(242,14,14));
                                        }else {
                                            //item.setBackground(5, SWTResourceManager.getColor(117,243,83));
                                            item.setForeground(7, SWTResourceManager.getColor(78,178,88));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("更新持仓盈亏失败",e);
                            }
                            
                        }
                    });
                    
                    
                } catch (SocketException e) {
                    logger.error("获取socket输入流失败",e);
                    logger.debug("重新创建socket连接");
                    traderView.recreateSocket();
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
