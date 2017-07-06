package com.bohai.subAccount.swt.risk;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_D_Buy;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_D_Sell;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_LimitPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CloseRuleService;
import com.bohai.subAccount.service.GroupRuleService;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.service.SubTradingaccountService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.swt.risk.helper.RiskMainMarketReceiveThread;
import com.bohai.subAccount.swt.risk.helper.RiskMainTradeReceiveThread;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.utils.Datecalculate;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserAccountVO;
import com.bohai.subAccount.vo.UserPositionVO;

import swing2swt.layout.BorderLayout;

public class RiskManageView {
	
    private static Logger logger = Logger.getLogger(RiskManageView.class);
    
	//static final String MARKET_IP = "10.0.0.204";
	static final String MARKET_IP = ApplicationConfig.getProperty("marketAddr");
	

	//static final String TRADE_IP = "10.0.0.202";
	static final String TRADE_IP = ApplicationConfig.getProperty("tradeAddr");
	
	protected Shell shell;
	private Table mainAccountTable;
	private Table subAccountTable;
	private MainAccountService mainAccountService;
	private UserInfoService userInfoService;
	private InvestorPositionService investorPositionService;
	private UserContractService userContractService;
	private SubTradingaccountService subTradingaccountService;
	private GroupRuleService groupRuleService;
	private CloseRuleService closeRuleService;
    private Socket socket;
    private Socket tradeSocket;
    private Datecalculate dateCalcuate = new Datecalculate();
    private HashMap<Object, Object> investorPositionsInfos;
    private List<UserContract> userContracts = new ArrayList<UserContract>();    
    //组规则信息
    private GroupRule groupRule = new GroupRule();
    //平仓规则表
    private List<CloseRule> closeRule;

    /**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RiskManageView window = new RiskManageView();
			window.loadSpringContext();
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
		//主账户和子账户的取得
		createContents();
		shell.open();
		shell.layout();
        //初始化行情socket
        getMarketSocket();
        //初始化交易socket
        getTradeSocket1();
        
        //行情接收线程
		Thread thread = new Thread(
				new RiskMainMarketReceiveThread(this, subAccountTable, userContracts, closeRule));
		thread.setDaemon(true);
		thread.start();
   
        //交易接收线程
		Thread tradethread = new Thread(
				new RiskMainTradeReceiveThread(this, subAccountTable, investorPositionService));
		tradethread.setDaemon(true);
		tradethread.start();
        
        //定时任务
//		if (null != groupRule && groupRule.getCloseTime() != null) {
//			String[] closeTime = groupRule.getCloseTime().split(",");
//			Thread quartz = new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					CronTriggerExample example = new CronTriggerExample(closeTime, RiskManageView.this);
//			        try {
//						example.run();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});
//			quartz.setDaemon(true);
//			quartz.start();
//		}
		
		logger.info("===========================定时任务已开启=======================");
		
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				display.sleep();
//			}
//		}
	}
	
	public void loadSpringContext(){
		logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        logger.info("===================加载成功 !==================");
        mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
        userInfoService = (UserInfoService) SpringContextUtil.getBean("userInfoService");
        investorPositionService = (InvestorPositionService) SpringContextUtil.getBean("investorPositionService");
        userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
        subTradingaccountService = (SubTradingaccountService) SpringContextUtil.getBean("subTradingaccountService");
        groupRuleService = (GroupRuleService) SpringContextUtil.getBean("groupRuleService");
        closeRuleService = (CloseRuleService) SpringContextUtil.getBean("closeRuleService");
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				SchedulerFactory sf = new StdSchedulerFactory();
				try {
					Scheduler sched = sf.getScheduler();
					sched.shutdown();
					logger.debug("关闭定时任务");
				} catch (SchedulerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				logger.info("退出");
			}
		});
		
		shell.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION
                        | SWT.YES | SWT.NO);
                messagebox.setText("提示");
                messagebox.setMessage("您确定要退出吗?") ;
                int message = messagebox.open();
                e.doit = message == SWT.YES;
            }
        });
		
		shell.setSize(548, 305);
		shell.setText("风控");
		shell.setLayout(new BorderLayout(0, 0));
		
		createMainAccountTable(shell);

		//主账户信息取得
		getMainAccountInfo();
		
		createSubAccoutTable(shell);
		
		//子账户信息取得
		getUserInfo();
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.SOUTH);
		
		Button button = new Button(composite, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//一键撤单
				cancelLation();
			}
		});
		button.setBounds(155, 10, 88, 38);
		button.setText("一键撤单");
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openLimit();
			}
		});
		btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		btnNewButton.setBounds(283, 10, 88, 38);
		btnNewButton.setText("限制开仓");
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    // 强平限制操作
				getKyohei();
			}
		});
		btnNewButton_1.setBounds(21, 10, 88, 38);
		btnNewButton_1.setText("强平限制");
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deRestrict();
			}
		});
		button_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button_1.setBounds(419, 10, 80, 38);
		button_1.setText("取消限制");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(471, 50, 61, 11);
		

	}
	
	//主账户信息取得
	public void getMainAccountInfo() {
		mainAccountTable.setLayoutData(BorderLayout.NORTH);
		mainAccountTable.setHeaderVisible(true);
		mainAccountTable.setLinesVisible(true);
		
		//查询用户组
		List<MainAccount> mainAccountInfos = null;
		try {
			mainAccountInfos = mainAccountService.getMainAccount();
		} catch (FutureException e1) {
			logger.error(e1.getMessage());
		}
		if(mainAccountInfos != null){
			
			for (MainAccount mainAccount : mainAccountInfos) {
				TableItem mainAccountTableItem = new TableItem(mainAccountTable, SWT.NONE);
				mainAccountTableItem.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
				mainAccountTableItem.setText(0, mainAccount.getAccountNo());
			}
		}

	}
	
	//子账户信息取得
	//@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getUserInfo() {
		
		subAccountTable.setLayoutData(BorderLayout.CENTER);
		subAccountTable.setHeaderVisible(true);
		subAccountTable.setLinesVisible(true);
		
		//查询用户组
		List<UserInfo> userInfos = null;

		try {
			userInfos = userInfoService.getUsersByGroup();
			//合约信息
			userContracts = userContractService.queryUserContractByAll();
			//group信息
			groupRule = groupRuleService.getGroupRule();
			//平仓规则表
			closeRule = closeRuleService.getAllCloseRule();
		

		} catch (FutureException e1) {
			logger.error(e1.getMessage());
		}
		if(userInfos != null){
			String strUsername = "";
			investorPositionsInfos = new HashMap<Object, Object>();

			for (UserInfo userInfo : userInfos) {
				//CTP成交信息取得
				//List<Trade> trades = null;
				//投资者持仓信息取得
				List<InvestorPosition> investorPositions = new ArrayList<InvestorPosition>();
				UserAccountVO subTradingaccount = new UserAccountVO();

				//计算返回结果
				String[] retResult;
				TableItem userInfoTableItem = new TableItem(subAccountTable, SWT.NONE);
				userInfoTableItem.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
				//子账户名
				strUsername = userInfo.getUserName();
				userInfoTableItem.setText(0, strUsername);
				
				try {
					//CTP成交信息取得
					//trades = tradeService.getUserByUserName(strUsername);
					//投资者持仓信息取得
					investorPositions = investorPositionService.getUserUnClosePostion(strUsername);
					//子账户资金信息
					subTradingaccount = subTradingaccountService.getUserByUserName10(strUsername);
					
					UserPositionVO userPositionVO = new UserPositionVO();
					userPositionVO.setUserName(strUsername);
					userPositionVO.setSubTradingaccount(subTradingaccount);
					userPositionVO.setInvestorPositions(investorPositions);
					//保存用户持仓信息到表格内
					userInfoTableItem.setData(userPositionVO);

					retResult = dateCalcuate.getCapitalinit(investorPositions, subTradingaccount);
					userInfoTableItem.setText(1, retResult[0]);
					userInfoTableItem.setText(2, retResult[1]);
					userInfoTableItem.setText(3, retResult[2]);
					userInfoTableItem.setText(4, retResult[3]);
					//userInfoTableItem.setText(4, retResult[3]);
					//强平比例
					userInfoTableItem.setText(5, StringUtils.isEmpty(userInfo.getForceRate())?"":userInfo.getForceRate());
					//强平金额
					userInfoTableItem.setText(6, StringUtils.isEmpty(userInfo.getForceLimit())?"":userInfo.getForceLimit());

				} catch (FutureException e) {
					logger.error("查询用户持仓失败",e);
				}

				//投资者持仓信息取得
				investorPositionsInfos.put(strUsername, investorPositions);

			}
		}

	}
	
	public void createMainAccountTable(Composite parent){
		
		mainAccountTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		mainAccountTable.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		TableLayout tLayout = new TableLayout();//专用于表格的布局
		mainAccountTable.setLayout(tLayout);
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(mainAccountTable, SWT.NONE).setText("主账号");
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(mainAccountTable, SWT.NONE).setText("动态权益");
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(mainAccountTable, SWT.NONE).setText("可用资金");
		
	}
	
	public void createSubAccoutTable(Composite parent){
		
		subAccountTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		subAccountTable.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		subAccountTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem item = subAccountTable.getItem(new Point(e.x, e.y));
				if(item == null){
					return;
				}
				RiskControlDialog riskControlDialog = new RiskControlDialog(shell, SWT.CLOSE, item,RiskManageView.this,userContracts);
				riskControlDialog.open();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			    
			    TableItem item = subAccountTable.getItem(new Point(e.x, e.y));
                if(item == null){
                    return;
                }
                
			    //创建菜单
                Menu menu = new  Menu(subAccountTable);
                subAccountTable.setMenu(menu);
			    
			    
				if(e.button == 3) {
				    
				    MenuItem removeItem = new MenuItem(menu, SWT.NONE);
                    removeItem.setText("调整平仓比例");
                    removeItem.addSelectionListener(new SelectionAdapter() {
                        
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            ForceCloseDialog closeDialog = new ForceCloseDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, item, RiskManageView.this);
                            closeDialog.open();
                        }
                        
                    });
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("出入金");
                    addItem.addSelectionListener(new SelectionAdapter() {
                        
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            
                            RiskCapitalRateDialog riskCapitalRateDialog = new RiskCapitalRateDialog(shell, SWT.CLOSE, item,RiskManageView.this);
                            riskCapitalRateDialog.open();
                        }
                        
                    });
                    
				}
			}
			
		});
		TableLayout tLayout = new TableLayout();//专用于表格的布局
		subAccountTable.setLayout(tLayout);
		
		tLayout.addColumnData(new ColumnWeightData(60));
		TableColumn accountTabCol = new TableColumn(subAccountTable, SWT.NONE);
		accountTabCol.setText("子账号");
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(subAccountTable, SWT.NONE).setText("动态权益");
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(subAccountTable, SWT.NONE).setText("可用资金");
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(subAccountTable, SWT.NONE).setText("持仓盈亏");
		
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(subAccountTable, SWT.NONE).setText("平仓盈亏");
		
		tLayout.addColumnData(new ColumnWeightData(60));
        new TableColumn(subAccountTable, SWT.NONE).setText("强平比例");
        
        tLayout.addColumnData(new ColumnWeightData(60));
        new TableColumn(subAccountTable, SWT.NONE).setText("强平金额");
		
	}
	
    public synchronized void getMarketSocket(){
    	//如果socket为空 或者断开连接则创建一个socket
    	if(socket == null || !(socket.isConnected() == true && socket.isClosed() == false)){
			try {
				logger.info("=====================开始与行情服务器建立连接==============");
				socket = new Socket(MARKET_IP,3393);
			} catch (Exception e) {
				logger.error("与行情服务器通信失败",e);
			}
		}
    }
    
    public void getTradeSocket1(){
        //如果socket为空 或者断开连接则创建一个socket
        if(tradeSocket == null || !(tradeSocket.isConnected() == true && tradeSocket.isClosed() == false)){
            try {
                logger.info("=====================开始与交易服务器建立连接==============");
                tradeSocket = new Socket(TRADE_IP,3394);
                tradeSocket.setKeepAlive(false);
            } catch (Exception e) {
            	MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
				box.setMessage("与交易服务器通信失败");
				box.setText(CommonConstant.MESSAGE_BOX_ERROR);
				box.open();
                logger.error("与交易服务器通信失败",e);
            }
        }
    }
    
    // 强平限制操作
    private void getKyohei() {
		boolean retResut = MessageDialog.openQuestion(shell, CommonConstant.MESSGAE_BOX_CONF, CommonConstant.SX001C);
		if (retResut) {
			setStrade("QPXZ");
			
			try {
				Thread.sleep(80);
			} catch (InterruptedException e1) {
				logger.error("线程睡眠失败",e1);
			}
			
			for(TableItem item :subAccountTable.getItems()){
				UserPositionVO userPositionVO = (UserPositionVO) item.getData();
				List<InvestorPosition> list = userPositionVO.getInvestorPositions();
				if(list != null && list.size() > 0){
					
					for(InvestorPosition position : list){
						
						try {
							StringBuffer sb = new StringBuffer();
							sb.append("risk");
							String userName = item.getText(0).trim();
							sb.append("|"+userName);
							sb.append("|FKQP");
							//报单入参
							CThostFtdcInputOrderField inputOrderField = new CThostFtdcInputOrderField();
							//合约代码
							String instrumentid = position.getInstrumentid();
							inputOrderField.setInstrumentID(instrumentid);
							//数量
							inputOrderField.setVolumeTotalOriginal(position.getPosition().intValue());
							//平今仓
							inputOrderField.setCombOffsetFlag("3");
							//投资者代码
							inputOrderField.setInvestorID(userName);
							// 用户代码
							inputOrderField.setUserID(userName);
							// 报单价格条件
							inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
							// 组合投机套保标志
							inputOrderField.setCombHedgeFlag("1");
							// 有效期类型
							inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
							// GTD日期
							inputOrderField.setGTDDate("");
							// 成交量类型
							inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
							// 最小成交量
							inputOrderField.setMinVolume(0);
							// 触发条件
							inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
							// 止损价
							inputOrderField.setStopPrice(0);
							// 强平原因
							inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
							// 自动挂起标志
							inputOrderField.setIsAutoSuspend(0);
							//合约属性
							UserContract contract = this.getContractByContractNo(instrumentid);
							if(position.getPosidirection().equals("0")){
								logger.debug("买开强平持仓信息："+JSON.toJSONString(position));
								//买开强平 ---> 卖平
								inputOrderField.setDirection(THOST_FTDC_D_Sell);
								//价格 = 对手价（买价） - 10跳
								BigDecimal price = position.getBidPrice1().subtract(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
								//跌停价
								BigDecimal lowerLimitPrice = position.getLowerLimitPrice();
								if(price.compareTo(lowerLimitPrice)<0){
									//如果价格小于跌停价，就用跌停价
									price = lowerLimitPrice;
								}
								logger.debug("强平价格："+price);
								inputOrderField.setLimitPrice(price.doubleValue());
								
							}else {
								logger.debug("卖开强平持仓信息："+JSON.toJSONString(position));
								//卖开强平 ---> 买平
								inputOrderField.setDirection(THOST_FTDC_D_Buy);
								//价格 = 对手价（卖价） + 10跳
								BigDecimal price = position.getAskPrice1().add(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
								//涨停价
								BigDecimal upperLimitPrice = position.getUpperLimitPrice();
								if(price.compareTo(upperLimitPrice)>0){
									//如果价格大于涨停价，就用涨停价
									price = upperLimitPrice;
								}
								logger.debug("强平价格："+price);
								inputOrderField.setLimitPrice(price.doubleValue());
							}
							sb.append("|"+JSON.toJSONString(inputOrderField));
							logger.info("强平指令："+sb.toString());
							PrintWriter out = new PrintWriter(new OutputStreamWriter(this.tradeSocket.getOutputStream(),"UTF-8"));
							out.println(sb.toString());
							out.flush();
						} catch (Exception e) {
							logger.error("强平异常",e);
						}
						
					}
				}
			}
			
		} else {
			return;
		}
    	
    }
    
    
    /**
     *  定时强平限制操作，由quartz定时调度
     */
    public void closeJob() {
    	
    	logger.debug("--------------进入定时强平方法---------------");
			try {
				setStrade("QPXZ");
				
				Thread.sleep(100);
				
				for(TableItem item :subAccountTable.getItems()){
					UserPositionVO userPositionVO = (UserPositionVO) item.getData();
					List<InvestorPosition> list = userPositionVO.getInvestorPositions();
					if(list != null && list.size() > 0){
						
						for(InvestorPosition position : list){
							
							try {
								StringBuffer sb = new StringBuffer();
								sb.append("risk");
								String userName = item.getText(0).trim();
								sb.append("|"+userName);
								sb.append("|FKQP");
								//报单入参
								CThostFtdcInputOrderField inputOrderField = new CThostFtdcInputOrderField();
								//合约代码
								String instrumentid = position.getInstrumentid();
								inputOrderField.setInstrumentID(instrumentid);
								//数量
								inputOrderField.setVolumeTotalOriginal(position.getPosition().intValue());
								//平今仓
								inputOrderField.setCombOffsetFlag("3");
								//投资者代码
								inputOrderField.setInvestorID(userName);
								// 用户代码
								inputOrderField.setUserID(userName);
								// 报单价格条件
								inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
								// 组合投机套保标志
								inputOrderField.setCombHedgeFlag("1");
								// 有效期类型
								inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
								// GTD日期
								inputOrderField.setGTDDate("");
								// 成交量类型
								inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
								// 最小成交量
								inputOrderField.setMinVolume(0);
								// 触发条件
								inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
								// 止损价
								inputOrderField.setStopPrice(0);
								// 强平原因
								inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
								// 自动挂起标志
								inputOrderField.setIsAutoSuspend(0);
								//合约属性
								UserContract contract = this.getContractByContractNo(instrumentid);
								if(position.getPosidirection().equals("0")){
									logger.debug("买开强平持仓信息："+JSON.toJSONString(position));
									//买开强平 ---> 卖平
									inputOrderField.setDirection(THOST_FTDC_D_Sell);
									//价格 = 对手价（买价） - 10跳
									BigDecimal price = position.getBidPrice1().subtract(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
									//跌停价
									BigDecimal lowerLimitPrice = position.getLowerLimitPrice();
									if(price.compareTo(lowerLimitPrice)<0){
										//如果价格小于跌停价，就用跌停价
										price = lowerLimitPrice;
									}
									logger.debug("强平价格："+price);
									inputOrderField.setLimitPrice(price.doubleValue());
									
								}else {
									logger.debug("卖开强平持仓信息："+JSON.toJSONString(position));
									//卖开强平 ---> 买平
									inputOrderField.setDirection(THOST_FTDC_D_Buy);
									//价格 = 对手价（卖价） + 10跳
									BigDecimal price = position.getAskPrice1().add(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
									//涨停价
									BigDecimal upperLimitPrice = position.getUpperLimitPrice();
									if(price.compareTo(upperLimitPrice)>0){
										//如果价格大于涨停价，就用涨停价
										price = upperLimitPrice;
									}
									logger.debug("强平价格："+price);
									inputOrderField.setLimitPrice(price.doubleValue());
								}
								sb.append("|"+JSON.toJSONString(inputOrderField));
								logger.info("强平指令："+sb.toString());
								PrintWriter out = new PrintWriter(new OutputStreamWriter(this.tradeSocket.getOutputStream(),"UTF-8"));
								out.println(sb.toString());
								out.flush();
							} catch (Exception e) {
								logger.error("强平异常",e);
							}
							
						}
					}
				}
			} catch (Exception e) {
				logger.error("定时强平失败",e);
			}
    }
    
    /**
     * 止损强平
     * @param userName
     */
    public void forceCloseByUserName(String userName){
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(getTradeSocket().getOutputStream(),"UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();;
            stringBuffer.append("risk|");
            stringBuffer.append(userName + "|");
            stringBuffer.append("QPXZ|");
            logger.info("强平指令：" + stringBuffer.toString());
            out.println(stringBuffer.toString());
            out.flush();
            Thread.sleep(200);
            
            for(TableItem item :subAccountTable.getItems()){
                if(!userName.equals(item.getText(0))){
                    continue;
                }
                
                UserPositionVO userPositionVO = (UserPositionVO) item.getData();
                List<InvestorPosition> list = userPositionVO.getInvestorPositions();
                if(list != null && list.size() > 0){
                    
                    for(InvestorPosition position : list){
                        
                        StringBuffer sb = new StringBuffer();
                        sb.append("risk");
                        sb.append("|"+userName);
                        sb.append("|FKQP");
                        //报单入参
                        CThostFtdcInputOrderField inputOrderField = new CThostFtdcInputOrderField();
                        //合约代码
                        String instrumentid = position.getInstrumentid();
                        inputOrderField.setInstrumentID(instrumentid);
                        //数量
                        inputOrderField.setVolumeTotalOriginal(position.getPosition().intValue());
                        //平今仓
                        inputOrderField.setCombOffsetFlag("3");
                        //投资者代码
                        inputOrderField.setInvestorID(userName);
                        // 用户代码
                        inputOrderField.setUserID(userName);
                        // 报单价格条件
                        inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
                        // 组合投机套保标志
                        inputOrderField.setCombHedgeFlag("1");
                        // 有效期类型
                        inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
                        // GTD日期
                        inputOrderField.setGTDDate("");
                        // 成交量类型
                        inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
                        // 最小成交量
                        inputOrderField.setMinVolume(0);
                        // 触发条件
                        inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
                        // 止损价
                        inputOrderField.setStopPrice(0);
                        // 强平原因
                        inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
                        // 自动挂起标志
                        inputOrderField.setIsAutoSuspend(0);
                        //合约属性
                        UserContract contract = this.getContractByContractNo(instrumentid);
                        if(position.getPosidirection().equals("0")){
                            logger.debug("买开强平持仓信息："+JSON.toJSONString(position));
                            //买开强平 ---> 卖平
                            inputOrderField.setDirection(THOST_FTDC_D_Sell);
                            //价格 = 对手价（买价） - 10跳
                            BigDecimal price = position.getBidPrice1().subtract(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
                            //跌停价
                            BigDecimal lowerLimitPrice = position.getLowerLimitPrice();
                            if(price.compareTo(lowerLimitPrice)<0){
                                //如果价格小于跌停价，就用跌停价
                                price = lowerLimitPrice;
                            }
                            logger.debug("强平价格："+price);
                            inputOrderField.setLimitPrice(price.doubleValue());
                            
                        }else {
                            logger.debug("卖开强平持仓信息："+JSON.toJSONString(position));
                            //卖开强平 ---> 买平
                            inputOrderField.setDirection(THOST_FTDC_D_Buy);
                            //价格 = 对手价（卖价） + 10跳
                            BigDecimal price = position.getAskPrice1().add(contract.getTickSize().multiply(new BigDecimal("10"))).setScale(2, RoundingMode.HALF_UP);
                            //涨停价
                            BigDecimal upperLimitPrice = position.getUpperLimitPrice();
                            if(price.compareTo(upperLimitPrice)>0){
                                //如果价格大于涨停价，就用涨停价
                                price = upperLimitPrice;
                            }
                            logger.debug("强平价格："+price);
                            inputOrderField.setLimitPrice(price.doubleValue());
                        }
                        sb.append("|"+JSON.toJSONString(inputOrderField));
                        logger.info("风控强平指令："+sb.toString());
                        
                        out.println(sb.toString());
                        out.flush();
                        
                    }
                }
            }
            
            
        } catch (Exception e) {
            logger.error("强平失败",e);
        }
        
    }
    
    // 一键撤单操作
    private void cancelLation() {
    	boolean retResut = MessageDialog.openQuestion(shell, CommonConstant.MESSGAE_BOX_CONF, CommonConstant.SX002C);
		if (retResut) {
			setStrade("YJCD");
		} else {
			return;
		}
    	
    }
    
    // 限制开仓
    private void openLimit() {
    	boolean retResut = MessageDialog.openQuestion(shell, CommonConstant.MESSGAE_BOX_CONF, CommonConstant.SX003C);
		if (retResut) {
			setStrade("XZKC");
		} else {
			return;
		}
    	
    }
    
    // 取消限制
    private void deRestrict() {
    	boolean retResut = MessageDialog.openQuestion(shell, CommonConstant.MESSGAE_BOX_CONF, CommonConstant.SX004C);
		if (retResut) {
			setStrade("QXXZ");
		} else {
			return;
		}
    }
    
    public void recreateSocket(){
    	try {
			this.socket = new Socket(MARKET_IP,3393);
		} catch (Exception e) {
			logger.error("与行情服务器通信失败",e);
		}
    }

    
    private void setStrade(String strFlg) {
    	try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(getTradeSocket().getOutputStream(),"UTF-8"));
            StringBuffer stringBuffer;
            String strlogInfo = "";
            if (strFlg.equals("QPXZ")) {
            	strlogInfo = "强平限制参数：";
            } else if (strFlg.equals("YJCD")) {
            	strlogInfo = "一键撤单参数：";
            } else if (strFlg.equals("XZKC")) {
            	strlogInfo = "限制开仓参数：";
            } else if (strFlg.equals("QXXZ")) {
            	strlogInfo = "取消限制参数：";
            }
            for (TableItem item : subAccountTable.getItems()) {
            	stringBuffer = new StringBuffer();
            	stringBuffer.append("risk|");
                stringBuffer.append(item.getText(0).trim() + "|");
                stringBuffer.append(strFlg.trim() + "|");
                logger.info(strlogInfo + stringBuffer.toString());
                out.println(stringBuffer.toString());
                out.flush();
            }

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void inoutCapital(String str) throws FutureException{
    	try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(getTradeSocket().getOutputStream(),"UTF-8"));
			out.println(str);
			out.flush();

		} catch (Exception e) {
			logger.error("请求出入金失败");
			throw new FutureException("", "请求出入金失败");
		}
    }
    
	/**
     * 根据合约编号查询合约属性
     * @param contractNo
     * @return
     */
    public UserContract getContractByContractNo(String contractNo){
        UserContract userContract = null;
        
        if(this.userContracts != null){
            for(UserContract contract : userContracts){
                if(contractNo.equals(contract.getContractNo())){
                    userContract = contract;
                    break;
                }
            }
        }
        return userContract;
    }
    
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Socket getTradeSocket() {
		return tradeSocket;
	}

	public void setTradeSocket(Socket tradeSocket) {
		this.tradeSocket = tradeSocket;
	}
	
	
	
	public HashMap<Object, Object> getInvestorPositionsInfos() {
		return investorPositionsInfos;
	}

	public void setInvestorPositionsInfos(HashMap<Object, Object> investorPositionsInfos) {
		this.investorPositionsInfos = investorPositionsInfos;
	}
	

	public Table getSubAccountTable() {
		return subAccountTable;
	}

	public void setSubAccountTable(Table subAccountTable) {
		this.subAccountTable = subAccountTable;
	}

	/**
	 * 更新持仓
	 * @param userName
	 * @param list
	 */
	public void savePosition(String userName, Object object){
		this.investorPositionsInfos.put(userName, object);
	}

}
