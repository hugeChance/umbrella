package com.bohai.subAccount.swt.trader.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.swt.loginMain;
import com.bohai.subAccount.swt.trader.TraderView;
import com.bohai.subAccount.utils.MediaPlay;
import com.bohai.subAccount.vo.UserAvailableMemorySave;

public class TradeReceiveThread implements Runnable {
    
    static Logger logger = Logger.getLogger(TradeReceiveThread.class);
    
    private TraderView tradreView;
    
    public TradeReceiveThread(TraderView tradeView) {
        this.tradreView = tradeView;
    }

    @Override
    public void run() {
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(tradreView.getTradeSocket().getInputStream(),"UTF-8"));
            
        } catch (Exception e) {
            logger.error("获取交易socket输入流失败",e);
        }
        while(true){
            try {
                String result = in.readLine();
                logger.info("收到服务器返回信息"+result);
                
                String[] params = result.split("\\|");
                if(params[1].equals(tradreView.getUserName())){
                    
                    //成交回报
                    if(params[0].equals("onRtnTrade")){//成交回报
                    	
                        
                        String tradeStr = params[3];
                        JSONObject jo = JSON.parseObject(tradeStr);
                        Trade trade = new Trade();
                        //合约
                        trade.setInstrumentid(jo.getString("instrumentid"));
                        //买卖
                        trade.setDirection(jo.getString("direction"));
                        //开平
                        trade.setOffsetflag(jo.getString("offsetflag"));
                        //成交数量
                        trade.setVolume(jo.getLong("volume"));
                        //成交价格
                        trade.setPrice(jo.getBigDecimal("price"));
                        //成交时间
                        trade.setTradetime(jo.getString("tradetime"));
                        //sessionID
                        trade.setSessionid(jo.getBigDecimal("sessionid"));
                        //frontID
                        trade.setFrontid(jo.getBigDecimal("frontid"));
                        //orderRef
                        trade.setOrderref(jo.getString("orderref"));
                        
                        logger.info("成交回报："+JSON.toJSONString(trade));
                        
                        Display.getDefault().syncExec(new Runnable() {
                            
                            @Override
                            public void run() {
                                
                            	try {
        							if(tradreView.dealMenuItem.getSelection()){
        								MediaPlay.play();
        							}
        						} catch (Exception e) {
        							logger.error("播放音频失败",e);
        						}
                            	
                                //更新成交单列表
                                Table dealTable = tradreView.getDealTable();
                                TableItem tableItem= new TableItem(dealTable, SWT.NONE, 0);
                                tableItem.setData(trade);
                                tableItem.setText(0, trade.getInstrumentid());
                                tableItem.setText(1, trade.getDirection().equals("0")? "买":"卖");
                                String offsetFlagStr = "";
                                if(trade.getOffsetflag().equals("0")){
                                    offsetFlagStr = "开";
                                }else if (trade.getOffsetflag().equals("1")) {
                                    offsetFlagStr = "平";
                                }else if (trade.getOffsetflag().equals("3")) {
                                    offsetFlagStr = "平今";
                                }else if (trade.getOffsetflag().equals("4")) {
                                    offsetFlagStr = "平昨";
                                }
                                tableItem.setText(2, offsetFlagStr);
                                tableItem.setText(3, trade.getVolume().toString());
                                tableItem.setText(4, trade.getPrice().toString());
                                tableItem.setText(5, StringUtils.isEmpty(trade.getTradetime())?"":trade.getTradetime());
                                
                                //更新总成交手数
                                Integer handCount = Integer.parseInt(tradreView.getHand().getText());
                                handCount = handCount + trade.getVolume().intValue();
                                tradreView.getHand().setText(handCount.toString());
                            }
                        });
                    }else if (params[0].equals("onRtnOrder")) {//报单回报
                        
                        if(params[2].equals("error")){
                            Display.getDefault().syncExec(new Runnable() {
                                
                                @Override
                                public void run() {
                                    logger.warn(params[3]);
                                    MessageBox box = new MessageBox(tradreView.shell, SWT.APPLICATION_MODAL | SWT.YES);
                                    box.setMessage(params[3]);
                                    box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
                                    box.open();
                                }
                            });
                            continue;
                        }
                        
                        String tradeStr = params[3];
                        JSONObject jo = JSON.parseObject(tradeStr);
                        Order order = new Order();
                        //合约
                        order.setInstrumentid(jo.getString("instrumentid"));
                        //挂单状态
                        order.setStatusmsg(jo.getString("statusmsg"));
                        order.setOrderstatus(jo.getString("orderstatus"));
                        //买卖
                        order.setDirection(jo.getString("direction"));
                        //开平
                        order.setComboffsetflag(jo.getString("comboffsetflag"));
                        //数量
                        order.setVolumetotaloriginal(jo.getLong("volumetotaloriginal"));
                        //价格
                        order.setLimitprice(jo.getBigDecimal("limitprice"));
                        //成交手数
                        order.setVolumetraded(jo.getLong("volumetraded"));
                        //报单时间
                        order.setInserttime(jo.getString("inserttime"));
                        //sessionID
                        order.setSessionid(jo.getBigDecimal("sessionid"));
                        //frontID
                        order.setFrontid(jo.getBigDecimal("frontid"));
                        //orderRef
                        order.setOrderref(jo.getString("orderref"));
                        //orderSysID
                        order.setOrdersysid(jo.getString("ordersysid"));
                        //exchangeID
                        order.setExchangeid(jo.getString("exchangeid"));
                        
                        logger.info("报单回报："+JSON.toJSONString(order));
                        Display.getDefault().syncExec(new Runnable() {
                            
                            @Override
                            public void run() {
                                
                                TableItem[] entrustItems = tradreView.getEntrustTable().getItems();
                                if(entrustItems != null && entrustItems.length >0){
                                    
                                    for(TableItem item : entrustItems){
                                        Order originalOrder = (Order) item.getData();
                                        /*
                                        logger.debug("判断表格中是否已经存在");
                                        logger.debug(originalOrder.getSessionid()+" : "+order.getSessionid());
                                        logger.debug(originalOrder.getFrontid()+" : "+order.getFrontid());
                                        logger.debug(originalOrder.getOrderref()+" : "+order.getOrderref());
                                        
                                        logger.debug(originalOrder.getSessionid() == order.getSessionid() 
                                                && originalOrder.getFrontid() == order.getFrontid() 
                                                && originalOrder.getOrderref().equals(order.getOrderref()));*/
                                        
                                        if(originalOrder.getSessionid().equals(order.getSessionid()) 
                                                && originalOrder.getFrontid().equals(order.getFrontid()) 
                                                && originalOrder.getOrderref().equals(order.getOrderref())){
                                            
                                            item.setData(order);
                                            item.setText(0, order.getInstrumentid());
                                            item.setText(1, order.getDirection().equals("0")? "买":"卖");
                                            
                                            String offsetFlagStr = "";
                                            if(order.getComboffsetflag().equals("0")){
                                                offsetFlagStr = "开";
                                            }else if (order.getComboffsetflag().equals("1")) {
                                                offsetFlagStr = "平";
                                            }else if (order.getComboffsetflag().equals("3")) {
                                                offsetFlagStr = "平今";
                                            }else if (order.getComboffsetflag().equals("4")) {
                                                offsetFlagStr = "平昨";
                                            }
                                            
                                            item.setText(2, offsetFlagStr);
                                            item.setText(3, order.getVolumetotaloriginal().toString());
                                            item.setText(4, order.getLimitprice().toString());
                                            item.setText(5, order.getVolumetraded().toString());
                                            item.setText(6, order.getStatusmsg());
                                            item.setText(7, order.getInserttime());
                                            
                                            //不成交即撤单
                                            if(tradreView.getKillButton().getSelection()){
                                                logger.debug("不成交即撤单");
                                                if(order.getOrderstatus().equals("3")){
                                                    tradreView.cancelOrder(order);
                                                }
                                            }
                                            
                                            return;
                                        }
                                    }
                                }
                                
                                //更新委托单列表
                                Table dealTable = tradreView.getEntrustTable();
                                TableItem tableItem= new TableItem(dealTable, SWT.NONE, 0);
                                tableItem.setData(order);
                                tableItem.setText(0, order.getInstrumentid());
                                tableItem.setText(1, order.getDirection().equals("0")? "买":"卖");
                                tableItem.setText(2, order.getComboffsetflag().equals("0")? "开":"平");
                                tableItem.setText(3, order.getVolumetotaloriginal().toString());
                                tableItem.setText(4, order.getLimitprice().toString());
                                tableItem.setText(5, order.getVolumetraded().toString());
                                tableItem.setText(6, order.getStatusmsg());
                                tableItem.setText(7, order.getInserttime());

                            }
                        });
                        
                    }else if (params[0].equals("onPosition")) {//持仓
                        
                        if(params[2].equals("0")){//持仓为空
                            //刷新持仓表
                            Display.getDefault().syncExec(new Runnable() {
                                @Override
                                public void run() {
                                    tradreView.getPositionTable().removeAll();
                                }
                            });
                            continue;
                        }
                        
                        if(params[2].equals("1")){//如果是重新推送则清空持仓表
                            //刷新持仓表
                            Display.getDefault().syncExec(new Runnable() {
                                @Override
                                public void run() {
                                    tradreView.getPositionTable().removeAll();
                                }
                            });
                        }
                        
                        String positionStr = params[4];
                        JSONObject jo = JSON.parseObject(positionStr);
                        
                        String oldPosition = params[5];
                        
                        InvestorPosition position = new InvestorPosition();
                        position.setInstrumentid(jo.getString("instrumentid"));
                        position.setPosidirection(jo.getString("posidirection"));
                        position.setPosition(jo.getLong("position"));
                        position.setOpenamount(jo.getBigDecimal("openamount"));
                        position.setYdposition(Long.parseLong(oldPosition));//昨仓
                        
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                                TableItem tableItem = new TableItem(tradreView.getPositionTable(), SWT.NONE);
                                tableItem.setData(position);
                                tableItem.setText(0, jo.getString("instrumentid"));
                                tableItem.setText(1, jo.getString("posidirection").equals("0")?"买":"卖");
                                tableItem.setText(2, jo.getString("position"));
                                if(tradreView.checkSHPosition(position.getInstrumentid())){
                                    //昨仓
                                    tableItem.setText(3, position.getYdposition().toString());
                                    //今仓
                                    Long todayPosition = position.getPosition()-position.getYdposition();
                                    tableItem.setText(4, todayPosition.toString());
                                }
                                tableItem.setText(5, jo.getString("openamount"));
                                //tradreView.shell.layout();
                            }
                        });
                    }else if (params[0].equals("riskKYZJ")) {//可用资金
                    
                        Display.getDefault().syncExec(new Runnable() {
                            
                            @Override
                            public void run() {
                                String available = StringUtils.isEmpty(params[3])?"":params[3];
                                
                                BigDecimal b = new BigDecimal(available).setScale(2, RoundingMode.HALF_UP);
                                
                                //tradreView.availableLabel.setText(b.toString());
                                //资金栏目
                                String jsonStr = params[4];
                                UserAvailableMemorySave obj = JSON.parseObject(jsonStr, UserAvailableMemorySave.class);
                                
                                
                                TableItem[] items = tradreView.getTable().getItems();
                                if(items == null ||items.length < 1){
                                    TableItem newItem = new TableItem(tradreView.getTable(), SWT.NONE);
                                    newItem.setText(0, obj.getAvailable()==null ? "0.00" : new BigDecimal(obj.getAvailable()).setScale(2, RoundingMode.HALF_UP).toString());//静态资金
                                    newItem.setText(1, obj.getCloseWin()==null ? "0.00" : new BigDecimal(obj.getCloseWin()).setScale(2, RoundingMode.HALF_UP).toString());//平仓盈亏
                                    newItem.setText(2, obj.getPositionWin()==null ? "0.00" : new BigDecimal(obj.getPositionWin()).setScale(2, RoundingMode.HALF_UP).toString());//持仓盈亏
                                    newItem.setText(3, b.toString());   //可用资金
                                    newItem.setText(4, obj.getFrozenAvailable()==null ? "0.00" : new BigDecimal(obj.getFrozenAvailable()).setScale(2, RoundingMode.HALF_UP).toString());//冻结资金
                                    newItem.setText(5, obj.getMargin()==null ? "0.00" : new BigDecimal(obj.getMargin()).setScale(2, RoundingMode.HALF_UP).toString());//占用保证金
                                    newItem.setText(6, obj.getCommission()==null ? "0.00" : new BigDecimal(obj.getCommission()).setScale(2, RoundingMode.HALF_UP).toString());//手续费
                                    newItem.setText(7, obj.getInOutMoney()==null? "0.00":new BigDecimal(obj.getInOutMoney()).setScale(2, RoundingMode.HALF_UP).toString());//出入金
                                }else {
                                    TableItem item = items[0];
                                    item.setText(0, obj.getAvailable()==null ? "0.00" : new BigDecimal(obj.getAvailable()).setScale(2, RoundingMode.HALF_UP).toString());//静态资金
                                    item.setText(1, obj.getCloseWin()==null ? "0.00" : new BigDecimal(obj.getCloseWin()).setScale(2, RoundingMode.HALF_UP).toString());//平仓盈亏
                                    item.setText(2, obj.getPositionWin()==null ? "0.00" : new BigDecimal(obj.getPositionWin()).setScale(2, RoundingMode.HALF_UP).toString());//持仓盈亏
                                    item.setText(3, b.toString());   //可用资金
                                    item.setText(4, obj.getFrozenAvailable()==null ? "0.00" : new BigDecimal(obj.getFrozenAvailable()).setScale(2, RoundingMode.HALF_UP).toString());//冻结资金
                                    item.setText(5, obj.getMargin()==null ? "0.00" : new BigDecimal(obj.getMargin()).setScale(2, RoundingMode.HALF_UP).toString());//占用保证金
                                    item.setText(6, obj.getCommission()==null ? "0.00" : new BigDecimal(obj.getCommission()).setScale(2, RoundingMode.HALF_UP).toString());//手续费
                                    item.setText(7, obj.getInOutMoney()==null? "0.00":new BigDecimal(obj.getInOutMoney()).setScale(2, RoundingMode.HALF_UP).toString());//出入金
                                }
                                
                                String price = tradreView.getPriceText().getText();
                                if(StringUtils.isEmpty(price)){
                                    return;
                                }
                                
                                String contract = tradreView.getCombo().getText();
                                if(StringUtils.isEmpty(contract)){
                                	return;
                                }
                                
                                try {
                                    if(new BigDecimal(available).compareTo(new BigDecimal("0")) <=0 ){
                                        tradreView.getVolumePermit().setText("0");
                                    }else {
                                    	
                                    	UserContract userContract = tradreView.getContractByContractNo(contract);
                                    	
                                        //可开手数 = 可用资金/（指定价*合约单位*保证金比例）
                                        BigDecimal volumePermit = new BigDecimal(available).divide(new BigDecimal(price).multiply(userContract.getMargin()).multiply(new BigDecimal(userContract.getContractUnit())),RoundingMode.DOWN);
                                        tradreView.getVolumePermit().setText("<="+volumePermit.intValue());
                                    }
                                } catch (Exception e) {
                                    logger.error("计算可开手数异常：",e);
                                }
                                
                            }
                        });
                        
                    }
                }
            } catch (IOException e) {
                logger.error("连接交易服务器失败",e);
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("重新登录");
                        loginMain loginMain = new loginMain();
                        tradreView.shell.dispose();
                        loginMain.open();
                    }
                });
            }
        }
        
    }

}
