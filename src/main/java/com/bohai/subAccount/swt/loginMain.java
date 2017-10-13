package com.bohai.subAccount.swt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.swt.admin.oldAdminViewMain;
import com.bohai.subAccount.swt.admin.MainForm;
import com.bohai.subAccount.swt.risk.RiskManageView;
import com.bohai.subAccount.swt.trader.TraderView;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.utils.UserConfig;
import com.bohai.subAccount.vo.UserVO;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class loginMain {
    
    static Logger logger = Logger.getLogger(loginMain.class);
    //static final String TRADE_IP = "10.0.0.202";
    static final String TRADE_IP = ApplicationConfig.getProperty("tradeAddr");
    static final int TRADE_PORT = 3394;
    protected Shell shlSubaccount;
    //private Text username;
    private Combo username;
    private Text passwd;
    private Combo combo;
    private Button rememberMe;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            loginMain window = new loginMain();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shlSubaccount.open();
        shlSubaccount.layout();
        while (!shlSubaccount.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlSubaccount = new Shell();
        shlSubaccount.setSize(327, 443);
        shlSubaccount.setText("登录");
        shlSubaccount.setLayout(new FormLayout());
        
        Composite composite = new Composite(shlSubaccount, SWT.NONE);
        composite.setLayout(new FormLayout());
        FormData fd_composite = new FormData();
        fd_composite.bottom = new FormAttachment(100, -32);
        fd_composite.left = new FormAttachment(0, 20);
        fd_composite.top = new FormAttachment(0, 62);
        fd_composite.right = new FormAttachment(100, -28);
        composite.setLayoutData(fd_composite);
        
        
        //username = new Text(composite, SWT.BORDER);
        //add by caojia   20170316 记住密码
        rememberMe = new Button(composite, SWT.CHECK);
        FormData fd_button_1 = new FormData();
        fd_button_1.right = new FormAttachment(0, 185);
        rememberMe.setLayoutData(fd_button_1);
        rememberMe.setText("记住密码");
        if(!StringUtils.isEmpty(UserConfig.getPropertyByKey("rememberMe"))&&
        		UserConfig.getPropertyByKey("rememberMe").equals("true")){
        	rememberMe.setSelection(true);
        }
        
        username = new Combo(composite, SWT.DROP_DOWN);
        fd_button_1.left = new FormAttachment(username, 0, SWT.LEFT);
        username.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent e) {
        		
        		String name = username.getText();
        		System.out.println(name);
        		if(rememberMe.getSelection()){
            		passwd.setText(StringUtils.isEmpty(UserConfig.getPropertyByKey(name))?"":UserConfig.getPropertyByKey(name));
            	}
        	}
        });
        FormData fd_username = new FormData();
        fd_username.top = new FormAttachment(0, 44);
        fd_username.right = new FormAttachment(100, -49);
        username.setLayoutData(fd_username);
        
        
        Label userLabel = new Label(composite, SWT.NONE);
        fd_username.left = new FormAttachment(0, 116);
        FormData fd_userLabel = new FormData();
        fd_userLabel.top = new FormAttachment(0, 47);
        fd_userLabel.left = new FormAttachment(0, 48);
        fd_userLabel.right = new FormAttachment(username, -20);
        userLabel.setLayoutData(fd_userLabel);
        userLabel.setText("用户名：");
        
        Label passwdLabel = new Label(composite, SWT.NONE);
        passwdLabel.setText("密码：");
        FormData fd_passwdLabel = new FormData();
        fd_passwdLabel.top = new FormAttachment(userLabel, 31);
        fd_passwdLabel.left = new FormAttachment(0, 48);
        passwdLabel.setLayoutData(fd_passwdLabel);
        
        passwd = new Text(composite, SWT.BORDER|SWT.PASSWORD);
        fd_passwdLabel.right = new FormAttachment(passwd, -32);
        FormData fd_passwd = new FormData();
        fd_passwd.left = new FormAttachment(username, 0, SWT.LEFT);
        fd_passwd.top = new FormAttachment(username, 25);
        fd_passwd.right = new FormAttachment(100, -49);
        passwd.setLayoutData(fd_passwd);
        
        //用户名从配置文件读取
        List<UserVO> userVOs = UserConfig.getAllProperties();
        if(userVOs != null && userVOs.size() >0){
        	for (UserVO userVO : userVOs) {
        		username.add(userVO.getUserName());
			}
        	String name = username.getItem(0);
        	
        	username.setText(name);
        	
        	String rememberMe = UserConfig.getPropertyByKey("rememberMe");
        	if(!StringUtils.isEmpty(rememberMe)&&rememberMe.equals("true")){
        		
        		passwd.setText(StringUtils.isEmpty(UserConfig.getPropertyByKey(name))?"":UserConfig.getPropertyByKey(name));
        	}
        }
        
        
        
        
        Button loginBt = new Button(composite, SWT.NONE);
        loginBt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if("管理员".equals(combo.getText())){
                    if("admin".equals(username.getText()) && "admin".equals(passwd.getText())){
                    	
                    	//判断是否记住密码
                    	String userName = username.getText();
                    	String pass = "";
                    	if(rememberMe.getSelection()){
                    		UserConfig.setProperty("rememberMe", "true");
                    		pass = passwd.getText();
                    	}else {
                    		UserConfig.setProperty("rememberMe", "false");
						}
                    	//记住用户名
                    	UserConfig.setProperty(userName, pass);
                    	
                    	
                        /*AdminViewMain main = new AdminViewMain();
                        main.loadSpringContext();*/
                    	MainForm window = new MainForm();
                        window.loadSpringContext();
                        shlSubaccount.dispose();
                        window.open();
                    }else {
                        MessageBox box = new MessageBox(shlSubaccount, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage("用户名或密码错误");
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                    }
                }else if ("风控员".equals(combo.getText())) {
                	if("risk".equals(username.getText()) && "risk".equals(passwd.getText())){
                		//判断是否记住密码
                    	String userName = username.getText();
                    	String pass = "";
                    	if(rememberMe.getSelection()){
                    		UserConfig.setProperty("rememberMe", "true");
                    		pass = passwd.getText();
                    	}else {
                    		UserConfig.setProperty("rememberMe", "false");
						}
                    	//记住用户名
                    	UserConfig.setProperty(userName, pass);
                		try {
                			RiskManageView window = new RiskManageView();
                			window.loadSpringContext();
                			shlSubaccount.dispose();
                			window.open();
                		} catch (Exception e1) {
                			e1.printStackTrace();
                		}
                	}
				}else if ("交易员".equals(combo.getText())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("hello");
                    sb.append("|"+ (StringUtils.isEmpty(username.getText())?" ":username.getText()));
                    sb.append("|"+(StringUtils.isEmpty(passwd.getText())?" ":passwd.getText()));
                    
                    try {
                    	logger.debug("连接服务器："+TRADE_IP+",端口号："+TRADE_PORT);
                        Socket traderSocket = new Socket(TRADE_IP,TRADE_PORT);
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(traderSocket.getOutputStream(),"UTF-8"));
                        out.println(sb.toString());
                        logger.info("登录请求："+sb.toString());
                        out.flush();
                        BufferedReader in = new BufferedReader(new InputStreamReader(traderSocket.getInputStream(),"UTF-8"));
                        List<UserContract> contracts = new ArrayList<UserContract>();
                        List<Order> orders = new ArrayList<Order>();
                        List<Trade> trades = new ArrayList<Trade>();
                        List<InvestorPosition> investorPositions = new ArrayList<InvestorPosition>();
                        String available = "";
                        Integer handCount = 0;
                        while(true){
                            String s = in.readLine();
                            logger.info("登录响应："+s);
                            String[] paramList = s.split("\\|");
                            if((StringUtils.isEmpty(username.getText())?" ":username.getText()).equals(paramList[1])){
                                
                                
                                if(paramList[2].equals("login")){
                                    if("END".equals(paramList[3])){
                                    	
                                    	//判断是否记住密码
                                    	String userName = username.getText();
                                    	String pass = "";
                                    	if(rememberMe.getSelection()){
                                    		UserConfig.setProperty("rememberMe", "true");
                                    		pass = passwd.getText();
                                    	}else {
                                    		UserConfig.setProperty("rememberMe", "false");
										}
                                    	
                                    	//记住用户名
                                    	UserConfig.setProperty(userName, pass);
                                    	
                                        TraderView window = new TraderView();
                                        window.setTradeSocket(traderSocket);
                                        //初始化订阅
                                        window.setContracts(contracts);
                                        //初始化委托
                                        window.setOrders(orders);
                                        //初始化成交
                                        window.setTrades(trades);
                                        window.handCount = handCount;
                                        //初始化持仓
                                        window.setPositions(investorPositions);
                                        window.setUserName(username.getText());
                                        window.setPasswd(passwd.getText());
                                        //可用资金
                                        window.setAvailable(available);
                                        
                                        shlSubaccount.dispose();
                                        window.open();
                                        break;
                                    }else if (paramList[3].indexOf("ERROR") > -1) {
                                        MessageBox box = new MessageBox(shlSubaccount, SWT.APPLICATION_MODAL | SWT.YES);
                                        box.setMessage(paramList[3]+paramList[4]);
                                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                                        box.open();
                                        break;
                                    }
                                }else if (paramList[2].equals("subTradingaccount")) {
                                	 String availableStr = paramList[3];
                                     if(!StringUtils.isEmpty(availableStr)){
                                    	 JSONObject jo = JSON.parseObject(availableStr);
                                    	 
                                    	 available = jo.getBigDecimal("available").setScale(2, RoundingMode.HALF_UP).toString();
                                     }
								}else if(paramList[2].equals("listUserContract")){//订阅合约信息
                                    
                                    String contractStr = paramList[3];
                                    if(!StringUtils.isEmpty(contractStr)){
                                        JSONObject jo = JSON.parseObject(contractStr);
                                        
                                        UserContract contract = new UserContract();
                                        contract.setContractNo(jo.getString("contractNo"));
                                        contract.setMargin(jo.getBigDecimal("margin"));//手续费
                                        contract.setTickSize(jo.getBigDecimal("tickSize"));//每跳单位
                                        contract.setOpenCharge(jo.getBigDecimal("openCharge"));//开仓手续费
                                        contract.setOpenChargeRate(jo.getBigDecimal("openChargeRate"));//开仓手续费比例
                                        contract.setCloseCurrCharge(jo.getBigDecimal("closeCurrCharge"));
                                        contract.setCloseCurrChargeRate(jo.getBigDecimal("closeCurrChargeRate"));
                                        contract.setContractUnit(jo.getInteger("contractUnit"));
                                        contracts.add(contract);
                                    }
                                    
                                }else if(paramList[2].equals("order")){//报单信息

                                    String inputOrderStr = paramList[3];
                                    if(!StringUtils.isEmpty(inputOrderStr)){
                                        JSONObject jo = JSON.parseObject(inputOrderStr);
                                        if(jo != null && !StringUtils.isEmpty(jo.getString("instrumentid"))){
                                            Order Order = new Order();
                                            //合约
                                            Order.setInstrumentid(jo.getString("instrumentid"));
                                            //状态
                                            Order.setStatusmsg(jo.getString("statusmsg"));
                                            Order.setOrderstatus(jo.getString("orderstatus"));
                                            //买卖
                                            Order.setDirection(jo.getString("direction"));
                                            //开平
                                            Order.setComboffsetflag(jo.getString("comboffsetflag"));
                                            //数量
                                            Order.setVolumetotaloriginal(jo.getLong("volumetotaloriginal"));
                                            //价格
                                            Order.setLimitprice(jo.getBigDecimal("limitprice"));
                                            //成交手数
                                            Order.setVolumetraded(jo.getLong("volumetraded"));
                                            //报单时间
                                            Order.setInserttime(jo.getString("inserttime"));
                                            //sessionID
                                            Order.setSessionid(jo.getBigDecimal("sessionid"));
                                            //frontID
                                            Order.setFrontid(jo.getBigDecimal("frontid"));
                                            //orderRef
                                            Order.setOrderref(jo.getString("orderref"));
                                            orders.add(Order);
                                        }
                                    }
                                }else if(paramList[2].equals("trade")){
                                    String tradeStr = paramList[3];
                                    if(!StringUtils.isEmpty(tradeStr)){
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
                                        trades.add(trade);
                                        
                                        handCount += trade.getVolume().intValue();
                                    }
                                }else if (paramList[2].equals("onPosition")) {//持仓
                                	String position = paramList[3];
                                	if(!StringUtils.isEmpty(position)){
                                		JSONObject jo = JSON.parseObject(position);
                                		InvestorPosition investorPosition = new InvestorPosition();
                                		investorPosition.setInstrumentid(jo.getString("instrumentid"));
                                		investorPosition.setPosidirection(jo.getString("posidirection"));
                                		investorPosition.setPosition(jo.getLong("position"));
                                		investorPosition.setOpenamount(jo.getBigDecimal("openamount"));
                                		investorPositions.add(investorPosition);
                                	}
								}
                            }
                        }
                    } catch (Exception e1) {
                        logger.error("登录失败",e1);
                        MessageBox box = new MessageBox(shlSubaccount, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage("与服务器通讯失败");
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                    }
                }
                
            }
        });
        FormData fd_loginBt = new FormData();
        fd_loginBt.left = new FormAttachment(userLabel, 0, SWT.LEFT);
        loginBt.setLayoutData(fd_loginBt);
        loginBt.setText("登录");
        
        Button button = new Button(composite, SWT.NONE);
        fd_loginBt.top = new FormAttachment(button, 0, SWT.TOP);
        fd_loginBt.right = new FormAttachment(button, -21);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shlSubaccount.dispose();
            }
        });
        button.setText("退出");
        FormData fd_button = new FormData();
        fd_button.left = new FormAttachment(0, 141);
        fd_button.right = new FormAttachment(100, -49);
        button.setLayoutData(fd_button);
        
        combo = new Combo(composite, SWT.DROP_DOWN|SWT.READ_ONLY);
        fd_button_1.bottom = new FormAttachment(combo, -6);
        fd_button.top = new FormAttachment(combo, 49);
        FormData fd_combo = new FormData();
        fd_combo.right = new FormAttachment(username, 0, SWT.RIGHT);
        fd_combo.left = new FormAttachment(username, 0, SWT.LEFT);
        combo.setLayoutData(fd_combo);
        combo.add("交易员");
        combo.add("风控员");
        combo.add("管理员");
        combo.setText(combo.getItem(0));
        
        Label label = new Label(composite, SWT.NONE);
        fd_combo.top = new FormAttachment(label, -3, SWT.TOP);
        FormData fd_label = new FormData();
        fd_label.top = new FormAttachment(passwdLabel, 34);
        fd_label.left = new FormAttachment(userLabel, 0, SWT.LEFT);
        label.setLayoutData(fd_label);
        label.setText("角色：");
        
    }
}
