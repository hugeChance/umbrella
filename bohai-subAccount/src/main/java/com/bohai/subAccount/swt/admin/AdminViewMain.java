package com.bohai.subAccount.swt.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.dao.UseravailableindbMapper;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.FutureMarket;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.Useravailableindb;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.ClearService;
import com.bohai.subAccount.service.CloseRuleService;
import com.bohai.subAccount.service.FutureMarketService;
import com.bohai.subAccount.service.GroupInfoService;
import com.bohai.subAccount.service.GroupRuleService;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.utils.DateFormatterUtil;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.SettlemenetPart1Body;
import com.bohai.subAccount.vo.SettlemenetPart1Head;
import com.bohai.subAccount.vo.SettlemenetPart2Body;
import com.bohai.subAccount.vo.SettlemenetPart2Head;
import com.bohai.subAccount.vo.SettlemenetPart3Body;
import com.bohai.subAccount.vo.SettlemenetPart3Head;
import com.bohai.subAccount.vo.SettlemenetTitleVO;
import com.bohai.subAccount.vo.UserContractTradeRule;

import swing2swt.layout.BorderLayout;

public class AdminViewMain {

    static Logger logger = Logger.getLogger(AdminViewMain.class);
    
    public static Shell shell;
    private Table mainAccountTable;
    private Table ruleTable;
    private Table closeTable;
    private Table userRuleTable;
    private Tree tree;
    
    private MainAccountService mainAccountService;
    private GroupInfoService groupInfoService;
    private UserInfoService userInfoService;
    private TradeRuleService tradeRuleService;
    private CloseRuleService closeRuleService;
    private InvestorPositionService investorPositionService;
    private GroupRuleService groupRuleService;
    private UseravailableindbMapper useravailableindbMapper;
	private TradeService tradeService;
	private UserContractService userContractService;
	private FutureMarketService futureMarketService;
	
	private Map<String,UserContract> mapUserContractMemorySave;
    
    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            AdminViewMain window = new AdminViewMain();
            window.loadSpringContext();
            window.setMemory();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setMemory(){
    	mapUserContractMemorySave = new HashMap<String,UserContract>();
    	
    	List<UserContract> listUserContract;
		try {
			listUserContract = userContractService.queryUserContractByAll();
			for (UserContract userContract2 : listUserContract) {
				logger.info("setMemory="+JSON.toJSONString(userContract2));
				
//				listUserContractMemorySave.set
				mapUserContractMemorySave.put(userContract2.getUserNo() + userContract2.getContractNo(), userContract2);
				
				
			}
		} catch (FutureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        
        
        
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
    
    public void loadSpringContext(){
        logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        logger.info("===================加载成功 !==================");
        
        groupInfoService = (GroupInfoService) SpringContextUtil.getBean("groupInfoService");
        mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
        userInfoService = (UserInfoService) SpringContextUtil.getBean("userInfoService");
        tradeRuleService = (TradeRuleService) SpringContextUtil.getBean("tradeRuleService");
        investorPositionService = (InvestorPositionService) SpringContextUtil.getBean("investorPositionService");
        closeRuleService = (CloseRuleService) SpringContextUtil.getBean("closeRuleService");
        groupRuleService = (GroupRuleService) SpringContextUtil.getBean("groupRuleService");
        useravailableindbMapper = (UseravailableindbMapper) SpringContextUtil.getBean("useravailableindbMapper");
        tradeService = (TradeService) SpringContextUtil.getBean("tradeService");
        futureMarketService = (FutureMarketService) SpringContextUtil.getBean("futureMarketService");
        userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(898, 540);
        shell.setText("管理员");
        shell.setLayout(new BorderLayout(0, 0));
        
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        
        MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
        menuItem.setText("账户");
        
        Menu accountMenu = new Menu(menuItem);
        menuItem.setMenu(accountMenu);
        
        MenuItem accountAddMenu = new MenuItem(accountMenu, SWT.NONE);
        accountAddMenu.addSelectionListener(new MainAccountAddSelection());
        accountAddMenu.setText("添加账户");
        
        CTabFolder treeTabFolder = new CTabFolder(shell, SWT.BORDER);
        treeTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        treeTabFolder.setLayoutData(BorderLayout.WEST);
        
        CTabItem leftItemAccount = new CTabItem(treeTabFolder, SWT.NONE);
        leftItemAccount.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        leftItemAccount.setText("用户组");
        
        tree = new Tree(treeTabFolder, SWT.BORDER);
        
        tree.addMouseListener(new MouseAdapter() {
            //左侧导航树鼠标点击事件
            @Override
            public void mouseDown(MouseEvent e) {
                TreeItem selected=tree.getItem(new Point(e.x,e.y));  //取节点控件
                Menu rightClickMenu = new Menu(tree);
                //鼠标右键菜单
                tree.setMenu(rightClickMenu);
                
                if(e.button == 1 && selected == null){//左键空白
                    return;
                }else if (e.button == 1 && selected != null) {//左键树节点
                    logger.debug("鼠标左键点击用户组树节点："+selected.getText());
                    refreshTradeRule(selected);
                }else if(selected == null && e.button == 3){//右键空白
                    
                    MenuItem addGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    addGroupMenuItem.setText("添加用户组");
                    addGroupMenuItem.addSelectionListener(new GroupAddSelection());
                    
                }else if(selected.getParentItem() == null && e.button == 3){//右键用户组
                    
                    MenuItem addSubAccountMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    addSubAccountMenuItem.setText("添加子账户");
                    addSubAccountMenuItem.addSelectionListener(new SubAccountAddSelection(selected));
                    
                    MenuItem editGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    editGroupMenuItem.setText("修改用户组");
                    
                    MenuItem removeGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    removeGroupMenuItem.setText("删除用户组");
                    removeGroupMenuItem.addSelectionListener(new GroupRemoveSelection(selected));
                    
                    /*MenuItem addRuleMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    addRuleMenuItem.setText("添加交易数量组规则");
                    addRuleMenuItem.addSelectionListener(new RuleAddSelection());
                    
                    MenuItem addCloseRuleMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    addCloseRuleMenuItem.setText("添加开平仓组规则");
                    addCloseRuleMenuItem.addSelectionListener(new CloseRuleAddSelection());*/
                    
                }else if(selected.getParentItem() != null && e.button == 3){//右键用户
                    
                    MenuItem editGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    editGroupMenuItem.setText("修改子账户");
                    editGroupMenuItem.addSelectionListener(new SubAccountEditSelection(selected));
                    
                    MenuItem removeUserMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    removeUserMenuItem.setText("删除子账户");
                    removeUserMenuItem.addSelectionListener(new SubAccountRemoveSelection(selected));
                }
            }
        });
        leftItemAccount.setControl(tree);
        
        //查询用户树结构
        refreshUserTree();
        
        CTabFolder dataTabFolder = new CTabFolder(shell, SWT.BORDER|SWT.CLOSE);
        dataTabFolder.setMaximizeVisible(true);
        dataTabFolder.setMinimizeVisible(true);
        dataTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        dataTabFolder.setLayoutData(BorderLayout.CENTER);
        
        /*CTabItem tabItem_2 = new CTabItem(dataTabFolder, SWT.BORDER);
        tabItem_2.setText("主界面");*/
        
        CTabItem ruleTabItem = new CTabItem(dataTabFolder, SWT.NONE);
        ruleTabItem.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        ruleTabItem.setText("交易组规则");
        //选中组规则标签页
        dataTabFolder.setSelection(ruleTabItem);
        
        createRuleTable(dataTabFolder);
        ruleTabItem.setControl(ruleTable);
        ruleTable.setHeaderVisible(true);
        ruleTable.setLinesVisible(true);
        
        /*logger.debug("初始化加载所有组规则");
        refreshTradeRule(null);*/
        
        CTabItem tabItem = new CTabItem(dataTabFolder, SWT.NONE);
        tabItem.setText("平仓组规则");
        
        createCloseRuleTable(dataTabFolder);
        tabItem.setControl(closeTable);
        closeTable.setHeaderVisible(true);
        closeTable.setLinesVisible(true);
        
        CTabItem tabItem_1 = new CTabItem(dataTabFolder, SWT.NONE);
        tabItem_1.setText("用户组规则");
        
        createUserRuleTable(dataTabFolder);
        userRuleTable.setHeaderVisible(true);
        userRuleTable.setLinesVisible(true);
        tabItem_1.setControl(userRuleTable);
        
        
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(BorderLayout.SOUTH);
        composite.setSize(100, 200);
        
        Label label = new Label(composite, SWT.NONE);
        label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        label.setBounds(145, 24, 61, 27);
        label.setText("状态：");
        
        Label lblNewLabel = new Label(composite, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        lblNewLabel.setBounds(238, 24, 90, 27);
        lblNewLabel.setText("");
        
        Button btnNewButton = new Button(composite, SWT.NONE);
        btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(!lblNewLabel.getText().equals("结算状态")){
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("请先结算");
                    box.setText(CommonConstant.MESSAGE_BOX_WARN);
                    box.open();
                    return;
                }
                
                ClearService clearService = (ClearService) SpringContextUtil.getBean("clearService");
                clearService.backUp();
                clearService.init();
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("初始化完成");
                box.setText(CommonConstant.MESSAGE_BOX_WARN);
                box.open();
                lblNewLabel.setText("初始化状态");
            }
        });
        btnNewButton.setBounds(367, 21, 80, 27);
        btnNewButton.setText("初始化");
        
        Button clearButton = new Button(composite, SWT.NONE);
        clearButton.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        clearButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	
            	Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							Socket socket = new Socket(ApplicationConfig.getProperty("tradeAddr"),3394);
							PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
							out.println("admin|closeAccount");
							out.flush();
							
						} catch (Exception e) {
							logger.error("建立socket通讯失败");
						}
						
					}
				});
            	
            	thread.start();
            	
            	try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	//生成结算数据
            	if (settlement() == 0 ){
            		//结算正常
            		MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("结算完成");
                    box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
                    box.open();
            		lblNewLabel.setText("结算状态");
            		
            	} else {
            		//结算异常
            		
            	}
                
//                DirectoryDialog dialog = new DirectoryDialog(shell);
//                dialog.setText("目录选择");
//                dialog.setMessage("结算文件存放目录");
//                String saveFile=dialog.open();  
//                if(saveFile!=null){  
//                    File directiory=new File(saveFile);
//                    logger.info(directiory.getPath());
//                    List<UserInfo> userList;
//                    try {
//                        userList = userInfoService.getUsersByGroup();
//                    } catch (FutureException e1) {
//                        return;
//                    }
//                    if(userList == null || userList.size() < 1 ){
//                        return;
//                    }
//                    for(UserInfo userInfo :userList){
//                        try {
//                            List<InvestorPosition> position = investorPositionService.getUserClosePostion(userInfo.getUserName());
//                            logger.info("查询用户："+userInfo.getUserName()+"的已平仓信息："+JSON.toJSONString(position));
//                            if(position == null || position.size() < 1 ){
//                                continue;
//                            }
//                            Settlement settlement = new Settlement();
//                            try {
//                                settlement.clear(directiory.getPath(), userInfo.getUserName(), position);
//                            } catch (Exception e1) {
//                                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
//                                box.setMessage("结算失败:");
//                                box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
//                                box.open();
//                                return;
//                            }
//                        } catch (FutureException e1) {
//                            return;
//                        }
//                        
//                    }
//                    
//                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
//                    box.setMessage("结算完成");
//                    box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
//                    box.open();
//                    
//                    lblNewLabel.setText("结算状态");
//                }
            }
        });
        clearButton.setText("结算");
        clearButton.setBounds(503, 21, 80, 27);
        
        Label lblNewLabel_1 = new Label(composite, SWT.NONE);
        lblNewLabel_1.setBounds(650, 41, 61, 17);
        
        CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
        tabFolder.setLayoutData(BorderLayout.NORTH);
        tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        
        CTabItem mainAccountTabItem = new CTabItem(tabFolder, SWT.NONE);
        mainAccountTabItem.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        mainAccountTabItem.setText("主账户");
        //选中账户标签页
        tabFolder.setSelection(mainAccountTabItem);
        
        
        Composite mainAccountComp = new Composite(tabFolder, SWT.NONE);
        mainAccountTabItem.setControl(mainAccountComp);
        mainAccountComp.setLayout(new BorderLayout(0, 0));
        
        createTableViewer(mainAccountComp);
        
    }
    
    public int settlement(){
    	//生成结算
    	DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setText("目录选择");
        dialog.setMessage("结算文件存放目录");
        String saveFile=dialog.open();  
        String tmpStr = "";
        
        if(saveFile!=null){
        	File directiory=new File(saveFile);
            logger.info(directiory.getPath());
            List<Useravailableindb> listUseravailableindb = new ArrayList<Useravailableindb>();
            listUseravailableindb = useravailableindbMapper.selectAll();
            
            //循环按每个客户出文件
            for (Useravailableindb useravailableindb : listUseravailableindb) {
            	StringBuffer strB = new StringBuffer();
            	SettlemenetTitleVO settlemenetTitleVO = new SettlemenetTitleVO();
            	settlemenetTitleVO.setCompanyName("赫城软件");
            	settlemenetTitleVO.setUserName(useravailableindb.getUsername());
            	settlemenetTitleVO.setTodayDate(String.valueOf(new Date()));
            	//期初结存
            	try {
					tmpStr = userInfoService.getUserInfoCapital(useravailableindb.getUsername());
				} catch (FutureException e) {
					logger.info("期初结存取得出错！！");
					e.printStackTrace();
				}
            	settlemenetTitleVO.setBalance_Start(tmpStr);
            	//期末结存 计算
            	//客户权益=上日结存+出入金-手续费+平仓盈亏（逐日盯市）+持仓盈亏（逐日盯市）；
            	BigDecimal balanceBDec = useravailableindb.getAvailable().add(useravailableindb.getFrozenavailable()).add(useravailableindb.getMargin()) ;
            	settlemenetTitleVO.setBalance_End(String.valueOf(balanceBDec));
            	settlemenetTitleVO.setClient_Equity(String.valueOf(balanceBDec));
            	settlemenetTitleVO.setCommission(String.valueOf(useravailableindb.getCommission()));
            	settlemenetTitleVO.setDeposit(String.valueOf(useravailableindb.getInoutmoney()));
            	//可用资金
            	BigDecimal fund_availBDec = useravailableindb.getAvailable().add(useravailableindb.getFrozenavailable());
            	settlemenetTitleVO.setFund_availible(String.valueOf(fund_availBDec));
            	settlemenetTitleVO.setMargin(String.valueOf(useravailableindb.getMargin()));
            	settlemenetTitleVO.setMTM(String.valueOf(useravailableindb.getPositionwin()));
            	settlemenetTitleVO.setRealized(String.valueOf(useravailableindb.getClosewin()));
            	//风险度=客户保证金占用/客户权益*100%；
            	BigDecimal risk_DegreeBDec = useravailableindb.getMargin().divide(balanceBDec);
            	settlemenetTitleVO.setRisk_Degree(String.valueOf(risk_DegreeBDec));
            	//应追加资金
            	BigDecimal margin_CallDBec = useravailableindb.getAvailable().add(useravailableindb.getFrozenavailable());
            	
            	if (margin_CallDBec.compareTo(new BigDecimal(0))<0){
            		settlemenetTitleVO.setMargin_Call(String.valueOf(margin_CallDBec));
            	} else {
            		settlemenetTitleVO.setMargin_Call("0");
            	}
				//账单头部
            	strB.append(settlemenetTitleVO.getRetStr());
            	strB.append("\r\n");
            	strB.append("\r\n");
//            	fileLineWrite();
//            	fileLineWrite("");
//            	fileLineWrite("");
            	//格式化输出明细
            	SettlemenetPart1Head settlemenetPart1Head = new SettlemenetPart1Head();
            	SettlemenetPart2Head settlemenetPart2Head = new SettlemenetPart2Head();
            	SettlemenetPart3Head settlemenetPart3Head = new SettlemenetPart3Head();
            	SettlemenetPart1Body settlemenetPart1Body = new SettlemenetPart1Body();
            	SettlemenetPart2Body settlemenetPart2Body = new SettlemenetPart2Body();
            	SettlemenetPart3Body settlemenetPart3Body = new SettlemenetPart3Body();
            	
            	//查询成交表
            	try {
					List<Trade> listTrade = tradeService.getUserByUserName2(useravailableindb.getUsername());
					UserContract userContract = new UserContract();
					
					if (listTrade.size() > 0) {
						//有成交则出明细
//						fileLineWrite(settlemenetPart1Head.getRetPart1Head1());
						strB.append(settlemenetPart1Head.getRetPart1Head1());
						strB.append("\r\n");
						for (Trade trade : listTrade) {
							//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
							//|成交日期| 交易所 |       品种       |      合约      |买/卖|   投/保    |  成交价  | 手数 |   成交额   |       开平       |  手续费  |  平仓盈亏  |     权利金收支      |  成交序号  |
							//|  Date  |Exchange|     Product      |   Instrument   | B/S |    S/H     |   Price  | Lots |  Turnover  |       O/C        |   Fee    |Realized P/L|Premium Received/Paid|  Trans.No. |
							//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
							//|20170512|上期所  |铝                |     al1706     |买   |投          | 13840.000|     1|    69200.00|开                |      3.00|        0.00|                 0.00|98274       |
							userContract = mapUserContractMemorySave.get(useravailableindb.getUsername() + trade.getInstrumentid());
							settlemenetPart1Body.setDate(settlemenetTitleVO.getTodayDate());
							settlemenetPart1Body.setExchange(trade.getExchangeid());
							settlemenetPart1Body.setProduct(trade.getInstrumentid());
							settlemenetPart1Body.setInstrument(trade.getInstrumentid());
							settlemenetPart1Body.setBS(trade.getDirection());
							settlemenetPart1Body.setSH("投");
							settlemenetPart1Body.setPrice(trade.getPrice().toString());
							settlemenetPart1Body.setLots(trade.getVolume().toString());
							//成交额
							BigDecimal turnover = new BigDecimal(0);
							turnover =trade.getPrice().multiply(new BigDecimal(trade.getVolume())).multiply(new BigDecimal(userContract.getContractUnit()));
							settlemenetPart1Body.setTurnover(turnover.toString());
							//
							if(trade.getOffsetflag().equals("0")){
								settlemenetPart1Body.setOC("开");
							} else {
								settlemenetPart1Body.setOC("平");
							}
							//手续费
							double commission = 0;
							//手数
							int shoushu = Integer.valueOf(trade.getVolume().toString());
							//手续费
							if(trade.getOffsetflag().equals("0")){
								//开仓手续费 = 手数 * 开仓手续费 + 价格 * 合约单位* 手数  * 开仓手续费比例  
								commission = shoushu * userContract.getOpenCharge().doubleValue() +    trade.getPrice().doubleValue()  * userContract.getContractUnit().doubleValue() * userContract.getOpenChargeRate().doubleValue()*shoushu ;
								logger.info("开仓手续费手数 ="+shoushu);
								logger.info("开仓开仓手续费  ="+ userContract.getOpenCharge().doubleValue() );
								logger.info("开仓手续费价格 ="+trade.getPrice());
								logger.info("开仓合约单位 ="+userContract.getContractUnit().doubleValue() );
								logger.info("开仓手开仓手续费比例 ="+userContract.getOpenChargeRate().doubleValue());
								
								
								
								
							} else {
								//平仓手续费 = 手数 * 平仓手续费 + 价格 * 合约单位* 手数  * 平仓手续费比例  
								commission = shoushu * userContract.getCloseCurrCharge().doubleValue() +    trade.getPrice().doubleValue()  * userContract.getContractUnit().doubleValue() * userContract.getCloseCurrChargeRate().doubleValue()*shoushu ;
							}
							
							
							
							settlemenetPart1Body.setFee(String.valueOf(commission));
							
							settlemenetPart1Body.setPremiumReceived("0.00");
							settlemenetPart1Body.setTransNo(trade.getOrdersysid());
//							fileLineWrite(settlemenetPart1Body.getRetStr());
							strB.append(settlemenetPart1Body.getRetStr());
							strB.append("\r\n");
							
						}
						
					}
				} catch (FutureException e) {
					logger.info("结算单查询成交表！！");
					e.printStackTrace();
				}
            	
            	//查询平仓表
            	// TODO 由COREAPP 增加新功能当平仓时写入平仓表
            	
            	
            	//查询持仓明细表
            	// TODO 由COREAPP 增加新功能当平仓时写入平仓表
            	
            	//查询持仓汇总表
            	List<InvestorPosition> listInvestorPosition = new ArrayList<InvestorPosition>();
            	try {
            		listInvestorPosition = investorPositionService.getUserUnClosePostion(useravailableindb.getUsername());
            		if (listInvestorPosition.size() > 0) {
						//有成交则出明细
//						fileLineWrite(settlemenetPart3Head.getRetPart3Head1());	
						strB.append(settlemenetPart3Head.getRetPart3Head1());
						strB.append("\r\n");
	            		for (InvestorPosition investorPosition : listInvestorPosition) {
		            			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		            			//|       品种       |      合约      |    买持     |    买均价   |     卖持     |    卖均价    |  昨结算  |  今结算  |持仓盯市盈亏|  保证金占用   |  投/保     |   多头期权市值   |   空头期权市值    |
		            			//|      Product     |   Instrument   |  Long Pos.  |Avg Buy Price|  Short Pos.  |Avg Sell Price|Prev. Sttl|Sttl Today|  MTM P/L   |Margin Occupied|    S/H     |Market Value(Long)|Market Value(Short)|
		            			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		            			//|        铝        |     al1706     |            1|    13840.000|             0|         0.000| 13765.000| 13790.000|     -250.00|        6895.00|投          |              0.00|               0.00|
	            			settlemenetPart3Body.setProduct(investorPosition.getInstrumentid());
	            			settlemenetPart3Body.setInstrument(investorPosition.getInstrumentid());
	            			if(investorPosition.getPosidirection().equals("0"))
	            			{
	            				//买
	            				settlemenetPart3Body.setLongPos(investorPosition.getPosition().toString());
		            			settlemenetPart3Body.setAvgBuyPrice(investorPosition.getPositioncost().toString());
		            			settlemenetPart3Body.setShortPos("0");
		            			settlemenetPart3Body.setAvgSellPrice("0");

	            			} else {
	            				//卖
	            				settlemenetPart3Body.setLongPos("0");
		            			settlemenetPart3Body.setAvgBuyPrice("0");
		            			settlemenetPart3Body.setShortPos(investorPosition.getPosition().toString());
		            			settlemenetPart3Body.setAvgSellPrice(investorPosition.getPositioncost().toString());
		            			
	            			}
	            			
	            			FutureMarket futureMarket = this.futureMarketService.queryFutureMarketByInstrument(investorPosition.getInstrumentid());
	            			
	            			settlemenetPart3Body.setPrev(futureMarket.getPreSettlementPrice().toString());
	            			settlemenetPart3Body.setSttlToday(futureMarket.getSettlementPrice());
	            			double mTM = 0;
	            			mTM = investorPosition.getPositioncost().doubleValue() - Double.valueOf(futureMarket.getSettlementPrice()) ;
	            			mTM = mTM * investorPosition.getPosition().doubleValue();
	            			settlemenetPart3Body.setMTM(String.valueOf(mTM));
	            			settlemenetPart3Body.setMarginOccupied(investorPosition.getUsemargin().toString());
	            			settlemenetPart3Body.setSH("投");
	            			settlemenetPart3Body.setMarketValueLong("0.00");
	            			settlemenetPart3Body.setMarketValueShort("0.00");
//	            			fileLineWrite(directiory.getPath(),useravailableindb.getUsername(),settlemenetPart3Body.getRetStr());
	            			strB.append(settlemenetPart3Body.getRetStr());
							strB.append("\r\n");
							}
            		}
				} catch (FutureException e) {
					logger.error("结算单查询持仓汇总表失败！！",e);
					return 1;
				}
            	
            	fileLineWrite(directiory.getPath(),useravailableindb.getUsername(),strB.toString());
			}
            
        	
    	  
        }
    	
    	return 0;
    }
    
    /**
     * 创建主账户表格
     * @param parent
     */
    public int fileLineWrite(String path, String username, String writeLine){
        
        path += "\\"+username+".txt";
    	
        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
            fw.write(writeLine);  
            
        } catch (IOException e) {
            
            logger.error("创建文件失败");
            return 0;
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                logger.error("关闭写入流失败");
            }
        }
    	return 1;
    }
    
    /**
     * 创建主账户表格
     * @param parent
     */
    public void createTableViewer(Composite parent){
        
        mainAccountTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        mainAccountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                //鼠标双击事件
                TableItem item = mainAccountTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && item != null){
                    MainAccount mainAccount = (MainAccount) item.getData();
                    AccountEditDialog editDialog = new AccountEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, mainAccount, AdminViewMain.this);
                    editDialog.open();
                }
            }
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = mainAccountTable.getItem(new Point(e.x, e.y));
                Menu menu = new Menu(mainAccountTable);
                mainAccountTable.setMenu(menu);
                //鼠标右键数据项
                if(e.button ==3 && selected != null){
                    MenuItem menuItem = new MenuItem(menu, SWT.None);
                    menuItem.setText("删除");
                    menuItem.addSelectionListener(new MainAccountRemoveSelection(selected));
                }
            }
        });
        mainAccountTable.setLayoutData(BorderLayout.CENTER);
        
        
        mainAccountTable.setHeaderVisible(true);//设置表头
        mainAccountTable.setLinesVisible(true);//显示表格线
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        mainAccountTable.setLayout(tLayout);


        /* 第三步:建立TableViewer中的列
           */
        tLayout.addColumnData(new ColumnWeightData(15));//这个是设置ID列的列宽为40像素
        new TableColumn(mainAccountTable, SWT.NONE).setText("交易账号");
          
        tLayout.addColumnData(new ColumnWeightData(15));//这个是设置ID列的列宽为10像素
        new TableColumn(mainAccountTable, SWT.NONE).setText("期货公司代码");
        
        tLayout.addColumnData(new ColumnWeightData(30));//这个是设置ID列的列宽为10像素
        new TableColumn(mainAccountTable, SWT.NONE).setText("账户类型");
        
        tLayout.addColumnData(new ColumnWeightData(20));//这个是设置ID列的列宽为70像素
        new TableColumn(mainAccountTable, SWT.NONE).setText("记录建立时间");
        
        tLayout.addColumnData(new ColumnWeightData(20));//这个是设置ID列的列宽为10像素
        new TableColumn(mainAccountTable, SWT.NONE).setText("记录更新时间");
        //初始化主账户数据
        refreshMainAccount();
    }
    
    //交易规则表格
    public void createRuleTable(Composite parent){
        ruleTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        ruleTable.addMouseListener(new MouseAdapter() {
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = ruleTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    TradeRuleEditDialog dialog = new TradeRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, selected ,AdminViewMain.this ,tree.getSelection()[0]);
                    dialog.open();
                }
            }
            
            //单击数据项事件
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = ruleTable.getItem(new Point(e.x, e.y));
                
                //创建菜单
                Menu menu = new  Menu(ruleTable);
                ruleTable.setMenu(menu);
                
                
                if(selected != null && e.button == 3){//鼠标右键数据项
                    logger.debug("鼠标右键数据项："+selected.getText());
                    
                    MenuItem removeItem = new MenuItem(menu, SWT.NONE);
                    removeItem.setText("删除");
                    removeItem.addSelectionListener(new RuleRemoveSelection(selected));
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("添加合约");
                    addItem.addSelectionListener(new RuleAddSelection());
                }else if (selected == null && e.button == 3) {//鼠标右键空白

                    logger.debug("鼠标右键规则表空白区域");
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("添加合约");
                    addItem.addSelectionListener(new RuleAddSelection());
                    
                }
            }
        });
        
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        ruleTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(ruleTable, SWT.NONE).setText("合约");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(ruleTable, SWT.NONE).setText("撤单数");
          
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(ruleTable, SWT.NONE).setText("委托数");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(ruleTable, SWT.NONE).setText("开仓数");
        
        tLayout.addColumnData(new ColumnWeightData(45));
        new TableColumn(ruleTable, SWT.NONE).setText("开仓手续费值");
        
        tLayout.addColumnData(new ColumnWeightData(45));
        new TableColumn(ruleTable, SWT.NONE).setText("开仓手续费%");
        
        tLayout.addColumnData(new ColumnWeightData(45));
        new TableColumn(ruleTable, SWT.NONE).setText("平今手续费值");
        
        tLayout.addColumnData(new ColumnWeightData(45));
        new TableColumn(ruleTable, SWT.NONE).setText("平今手续费%");
        
        tLayout.addColumnData(new ColumnWeightData(40));
        new TableColumn(ruleTable, SWT.NONE).setText("保证金比例");
        
        tLayout.addColumnData(new ColumnWeightData(40));
        new TableColumn(ruleTable, SWT.NONE).setText("合约单位");
        
        tLayout.addColumnData(new ColumnWeightData(45));
        new TableColumn(ruleTable, SWT.NONE).setText("最小跳动单位");
        
    }
    
    //平仓组规则表格
    public void createCloseRuleTable(Composite parent){
        
        closeTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        //鼠标单击事件
        closeTable.addMouseListener(new MouseAdapter() {
            
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = closeTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    TreeItem treeitem = tree.getSelection().length <1 ? tree.getTopItem() : tree.getSelection()[0];
                    CloseRuleEditDialog dialog = new CloseRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, AdminViewMain.this, treeitem, selected);
                    dialog.open();
                }
            }
            
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = closeTable.getItem(new Point(e.x, e.y));
                //创建菜单
                Menu menu = new  Menu(closeTable);
                closeTable.setMenu(menu);
                
                MenuItem addItem = new MenuItem(menu, SWT.NONE);
                addItem.setText("添加平仓组规则");
                addItem.addSelectionListener(new CloseRuleAddSelection());
                
                //右键某条数据
                if(e.button == 3 && selected != null){
                    logger.debug("鼠标右键数据项："+selected.getText());
                    MenuItem removeItem = new MenuItem(menu, SWT.NONE);
                    removeItem.setText("删除");
                    removeItem.addSelectionListener(new CloseRuleRemoveSelection(selected));
                }
                
            }
        });
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        closeTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(closeTable, SWT.NONE).setText("合约");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(closeTable, SWT.NONE).setText("最小变动单位");
          
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(closeTable, SWT.NONE).setText("跳数");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(closeTable, SWT.NONE).setText("强平比例");
    }
    
    
    //用户组规则表格
    public void createUserRuleTable(Composite parent){
        
        userRuleTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        userRuleTable.addMouseListener(new MouseAdapter() {
        	
            @Override
            public void mouseDown(MouseEvent e) {
                
                Menu menu = new Menu(userRuleTable);
                
                TableItem selected = userRuleTable.getItem(new Point(e.x, e.y));
                
                if(selected != null){
                    MenuItem item = new MenuItem(menu, SWT.None);
                    item.setText("删除");
                    item.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            GroupRule rule = (GroupRule) selected.getData();
                            try {
                                groupRuleService.removeGroupRule(rule.getId());
                                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                                box.setMessage("删除成功");
                                box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
                                box.open();
                                refreshGroupRuleTable();
                            } catch (FutureException e1) {
                                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                                box.setMessage("删除失败");
                                box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                                box.open();
                            }
                        }
                    });
                }
                if(e.button == 3){
                    
                    userRuleTable.setMenu(menu);
                    
                    MenuItem item = new MenuItem(menu, SWT.None);
                    item.setText("添加组规则");
                    item.addSelectionListener(new GroupRuleAddSelection());
                }
            }
            //双击数据更新
        	@Override
        	public void mouseDoubleClick(MouseEvent e) {
        		
        		TableItem selected = userRuleTable.getItem(new Point(e.x, e.y));
        		if(selected == null){
        			return;
        		}
        		
        		GroupInfo groupInfo = null;
        		if(tree.getItemCount()>0){
        			groupInfo = (GroupInfo) tree.getTopItem().getData();
        		}
        		GroupRule groupRule = (GroupRule) selected.getData();
        		GroupRuleEditDialog editDialog = new GroupRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo, AdminViewMain.this, groupRule);
        		editDialog.open();
        	}
        });
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        userRuleTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(userRuleTable, SWT.NONE).setText("用户组");
        
        /*tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(userRuleTable, SWT.NONE).setText("交易账号");*/
          
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(userRuleTable, SWT.NONE).setText("强平比例");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(userRuleTable, SWT.NONE).setText("开仓时间");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(userRuleTable, SWT.NONE).setText("平仓时间");
        
        refreshGroupRuleTable();
    }
    
    public void refreshMainAccount(){
        //清空主账户列表
        mainAccountTable.removeAll();
        
        List<MainAccount> list = null;
        
        try {
            list = mainAccountService.getMainAccount();
        } catch (FutureException e) {
            return;
        }
        
        if(list != null && list.size()>0){
            for (MainAccount mainAccount : list) {
                TableItem item = new TableItem(mainAccountTable, SWT.NULL);
                item.setData(mainAccount);
                item.setText(0,mainAccount.getAccountNo());
                item.setText(1,mainAccount.getBrokerId());
                if(!StringUtils.isEmpty(mainAccount.getAccountType())){
                    item.setText(2,mainAccount.getAccountType().equals("1") ? "账户主" : "账户备");
                }
                item.setText(3, mainAccount.getCreateTime()==null?"":DateFormatterUtil.getDateStr(mainAccount.getCreateTime()));
                item.setText(4, mainAccount.getUpdateTime()==null?"":DateFormatterUtil.getDateStr(mainAccount.getUpdateTime()));
            }
        }
        
    }
    
    public void refreshGroupRuleTable(){
    	//清空组规则列表
    	userRuleTable.removeAll();
    	
    	try {
			GroupRule groupRule = this.groupRuleService.getGroupRule();
			if(groupRule == null){
				return;
			}
			
			GroupInfo group = this.groupInfoService.getGroupInfoById(groupRule.getGroupId());
			
			TableItem item = new TableItem(userRuleTable, SWT.NULL);
			item.setData(groupRule);
			//用户组
			item.setText(0, group.getGroupName()==null?"":group.getGroupName());
			//强平比例
			item.setText(1, groupRule.getForceCloseRate()==null?"":groupRule.getForceCloseRate().toString());
			//开仓时间
			item.setText(2, groupRule.getOpenTime()==null?"":groupRule.getOpenTime());
			//强平时间
			item.setText(3, groupRule.getCloseTime()==null?"":groupRule.getCloseTime());
		} catch (FutureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 用户组树结构
     * @param tree
     */
    public void refreshUserTree(){
        //清空tree
        tree.removeAll();
        
        //查询用户组
        List<GroupInfo> groupInfos = null;
        try {
            groupInfos = groupInfoService.getGroups();
        } catch (FutureException e1) {
            logger.error(e1.getMessage());
        }
        
        if(groupInfos != null){
            
            for (GroupInfo groupInfo : groupInfos) {
                TreeItem groupTreeItem = new TreeItem(tree, SWT.NONE);
                groupTreeItem.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
                groupTreeItem.setText(groupInfo.getGroupName());
                groupTreeItem.setData(groupInfo);
                //查询组用户
                try {
                    List<UserInfo> userInfos = this.userInfoService.getUsersByGroupId(groupInfo.getId());
                    if(userInfos != null){
                        for (UserInfo userInfo : userInfos) {
                            TreeItem userTreeItem = new TreeItem(groupTreeItem, SWT.NONE);
                            userTreeItem.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
                            userTreeItem.setText(userInfo.getUserName());
                            userTreeItem.setData(userInfo);
                            userTreeItem.setExpanded(true);
                        }
                    }
                } catch (FutureException e1) {
                    logger.error(e1.getMessage());
                }
                //展开树节点
                groupTreeItem.setExpanded(true);
            }
        }
    }
    
    /**
     * 刷新交易组规则
     * @param treeItem
     */
    public void refreshTradeRule(TreeItem treeItem){
        //查询所有用户组交易规则
        if(treeItem == null){
            List<UserContractTradeRule> list = null;
            try {
                list = tradeRuleService.getTradeRulesByGroupId(null);
                ruleTable.removeAll();
                if(list != null && list.size() >0){
                    for (UserContractTradeRule rule : list) {
                        TableItem item= new TableItem(ruleTable, SWT.NONE);
                        item.setData(rule);
                        item.setText(0, rule.getContractNo());//合约
                        item.setText(1, StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());//撤单数
                        item.setText(2, StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());//委托数
                        item.setText(3, StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());//开仓数
                        item.setText(4, StringUtils.isEmpty(rule.getOpenCharge())?"":rule.getOpenCharge().toString());//开仓手续费固定值
                        item.setText(5, StringUtils.isEmpty(rule.getOpenChargeRate())?"":rule.getOpenChargeRate().toString());//开仓手续费比例
                        item.setText(6, StringUtils.isEmpty(rule.getCloseCurrCharge())?"":rule.getCloseCurrCharge().toString());//平今手续费固定值
                        item.setText(7, StringUtils.isEmpty(rule.getCloseCurrChargeRate())?"":rule.getCloseCurrChargeRate().toString());//平今手续费比例
                        item.setText(8, StringUtils.isEmpty(rule.getMargin())?"":rule.getMargin().toString());//保证金比例
                        item.setText(9, StringUtils.isEmpty(rule.getContractUnit())?"":rule.getContractUnit().toString());//合约单位
                        item.setText(10, StringUtils.isEmpty(rule.getTickSize())?"":rule.getTickSize().toString());//最小跳动单位
                    }
                }
            } catch (FutureException e) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e.getMessage());
                box.setText("警告");
                box.open();
            }
            //查询所有平仓规则
            List<CloseRule> closeRules = null;
            try {
                closeRules = closeRuleService.getAllCloseRule();
            } catch (FutureException e) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e.getMessage());
                box.setText("警告");
                box.open();
            }
            
            closeTable.removeAll();
            if(closeRules == null || closeRules.size() <1){
                return;
            }
            
            for(CloseRule closeRule :closeRules){
                TableItem item= new TableItem(closeTable, SWT.NONE);
                item.setData(closeRule);
                item.setText(0, closeRule.getContractNo());//合约
                item.setText(1, StringUtils.isEmpty(closeRule.getTickSize())?"":closeRule.getTickSize().toString());//最小跳动单位
                item.setText(2, StringUtils.isEmpty(closeRule.getHop())?"":closeRule.getHop().toString());//跳数
                item.setText(3, StringUtils.isEmpty(closeRule.getForceCloseRate())?"":closeRule.getForceCloseRate().toString());
            }
            
        }else if(treeItem.getData() instanceof GroupInfo){
            GroupInfo groupInfo = (GroupInfo) treeItem.getData();
            logger.debug("查询用户组id为："+groupInfo.getId()+"的交易规则");
            List<UserContractTradeRule> list = null;
            try {
                list = tradeRuleService.getTradeRulesByGroupId(groupInfo.getId());
                ruleTable.removeAll();
                if(list != null && list.size() >0){
                    for (UserContractTradeRule rule : list) {
                        TableItem item= new TableItem(ruleTable, SWT.NONE);
                        rule.setGroupName(groupInfo.getGroupName());
                        rule.setGroupId(groupInfo.getId());
                        item.setData(rule);
                        item.setText(0, rule.getContractNo());//合约
                        item.setText(1, StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());//撤单数
                        item.setText(2, StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());//委托数
                        item.setText(3, StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());//开仓数
                        item.setText(4, StringUtils.isEmpty(rule.getOpenCharge())?"":rule.getOpenCharge().toString());//开仓手续费固定值
                        item.setText(5, StringUtils.isEmpty(rule.getOpenChargeRate())?"":rule.getOpenChargeRate().toString());//开仓手续费比例
                        item.setText(6, StringUtils.isEmpty(rule.getCloseCurrCharge())?"":rule.getCloseCurrCharge().toString());//平今手续费固定值
                        item.setText(7, StringUtils.isEmpty(rule.getCloseCurrChargeRate())?"":rule.getCloseCurrChargeRate().toString());//平今手续费比例
                        item.setText(8, StringUtils.isEmpty(rule.getMargin())?"":rule.getMargin().toString());//保证金比例
                        item.setText(9, StringUtils.isEmpty(rule.getContractUnit())?"":rule.getContractUnit().toString());//合约单位
                        item.setText(10, StringUtils.isEmpty(rule.getTickSize())?"":rule.getTickSize().toString());//最小跳动单位
                    }
                }
            } catch (FutureException e) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e.getMessage());
                box.setText("警告");
                box.open();
            }
            
            //查询所有平仓规则
            List<CloseRule> closeRules = null;
            try {
                closeRules = closeRuleService.getAllCloseRule();
            } catch (FutureException e) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e.getMessage());
                box.setText("警告");
                box.open();
            }
            closeTable.removeAll();
            if(closeRules == null || closeRules.size() <1){
                return;
            }
            
            for(CloseRule closeRule :closeRules){
                TableItem item= new TableItem(closeTable, SWT.NONE);
                item.setData(closeRule);
                item.setText(0, closeRule.getContractNo());//合约
                item.setText(1, StringUtils.isEmpty(closeRule.getTickSize())?"":closeRule.getTickSize().toString());//最小跳动单位
                item.setText(2, StringUtils.isEmpty(closeRule.getHop())?"":closeRule.getHop().toString());//跳数
                item.setText(3, StringUtils.isEmpty(closeRule.getForceCloseRate())?"":closeRule.getForceCloseRate().toString());
            }
            
        }else if (treeItem.getData() instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) treeItem.getData();
            GroupInfo groupInfo = (GroupInfo) treeItem.getParentItem().getData();
            List<UserContractTradeRule> list = null;
            try {
                list = tradeRuleService.getTradeRulesByUserNo(userInfo.getUserNo());
                ruleTable.removeAll();
                if(list != null && list.size() >0){
                    for (UserContractTradeRule rule : list) {
                        TableItem item= new TableItem(ruleTable, SWT.NONE);
                        rule.setGroupId(groupInfo.getId());
                        rule.setGroupName(groupInfo.getGroupName());
                        item.setData(rule);
                        item.setText(0, rule.getContractNo());//合约
                        item.setText(1, StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());//撤单数
                        item.setText(2, StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());//委托数
                        item.setText(3, StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());//开仓数
                        item.setText(4, StringUtils.isEmpty(rule.getOpenCharge())?"":rule.getOpenCharge().toString());//开仓手续费固定值
                        item.setText(5, StringUtils.isEmpty(rule.getOpenChargeRate())?"":rule.getOpenChargeRate().toString());//开仓手续费比例
                        item.setText(6, StringUtils.isEmpty(rule.getCloseCurrCharge())?"":rule.getCloseCurrCharge().toString());//平今手续费固定值
                        item.setText(7, StringUtils.isEmpty(rule.getCloseCurrChargeRate())?"":rule.getCloseCurrChargeRate().toString());//平今手续费比例
                        item.setText(8, StringUtils.isEmpty(rule.getMargin())?"":rule.getMargin().toString());//保证金比例
                        item.setText(9, StringUtils.isEmpty(rule.getContractUnit())?"":rule.getContractUnit().toString());//合约单位
                        item.setText(10, StringUtils.isEmpty(rule.getTickSize())?"":rule.getTickSize().toString());//最小跳动单位
                    }
                }
            } catch (FutureException e) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e.getMessage());
                box.setText("警告");
                box.open();
            }
            
            //查询该用户所有平仓规则
            List<CloseRule> closeRules = null;
            try {
                closeRules = closeRuleService.getCloseRuleByUserNo(userInfo.getUserNo());
            } catch (FutureException e) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e.getMessage());
                box.setText("警告");
                box.open();
            }
            closeTable.removeAll();
            if(closeRules == null || closeRules.size() <1){
                return;
            }
            for(CloseRule closeRule :closeRules){
                TableItem item= new TableItem(closeTable, SWT.NONE);
                item.setData(closeRule);
                item.setText(0, closeRule.getContractNo());//合约
                item.setText(1, StringUtils.isEmpty(closeRule.getTickSize())?"":closeRule.getTickSize().toString());//最小跳动单位
                item.setText(2, StringUtils.isEmpty(closeRule.getHop())?"":closeRule.getHop().toString());//跳数
                item.setText(3, StringUtils.isEmpty(closeRule.getForceCloseRate())?"":closeRule.getForceCloseRate().toString());
            }
        }
    }
    
    /**
     * 添加主账号
     * @author BHQH-CXYWB
     *
     */
    public class MainAccountAddSelection extends SelectionAdapter{
        @Override
        public void widgetSelected(SelectionEvent e) {
            
            AccountAddDialog accountAddDialog = new AccountAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, AdminViewMain.this);
            accountAddDialog.open();
        }
    }
    
    /**
     * 删除主账户事件
     * @author BHQH-CXYWB
     *
     */
    public class MainAccountRemoveSelection extends SelectionAdapter {
        
        private TableItem item;
        
        public MainAccountRemoveSelection(TableItem item) {
            this.item = item;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            MainAccount account = (MainAccount) this.item.getData();
            try {
                mainAccountService.removeMainAccount(account.getId());
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功");
                box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
                box.open();
                refreshMainAccount();
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                box.open();
            }
        }
    }
    
    /**
     * 添加用户组事件
     * @author caojia
     *
     */
    public class GroupAddSelection extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(mainAccountTable.getItemCount()<1){
                logger.warn("请先添加主账户");
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加主账户");
                box.setText("警告");
                box.open();
                return;
            }
            MainAccount mainAccount = (MainAccount) mainAccountTable.getItem(0).getData();
            try {
                List<GroupInfo> list = groupInfoService.getGroups();
                
                if(list !=null && list.size()>0){
                    logger.warn("最多只能创建一个用户组");
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("最多只能创建一个用户组");
                    box.setText("警告");
                    box.open();
                    return;
                }
            } catch (FutureException e1) {
                return;
            }
            GroupAddDialog groupAddDialog = new GroupAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, mainAccount,AdminViewMain.this);
            groupAddDialog.open();
        }
    }
    
    /**
     * 添加用户组规则
     * @author caojia
     */
    public class GroupRuleAddSelection extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(tree.getItemCount()<1){
                logger.warn("请先添加用户组");
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组");
                box.setText("警告");
                box.open();
                return;
            }
            
            //MainAccount mainAccount = (MainAccount) mainAccountTable.getItem(0).getData();
            
            GroupInfo groupInfo = (GroupInfo) tree.getTopItem().getData();
            
            GroupRuleAddDialog groupEditDialog = new GroupRuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo,AdminViewMain.this);
            groupEditDialog.open();
        }
    }
    
    
    /**
     * 删除用户组
     * @author BHQH-CXYWB
     *
     */
    public class GroupRemoveSelection extends SelectionAdapter {
        
        private TreeItem treeItem;
        
        public GroupRemoveSelection(TreeItem treeItem) {
            this.treeItem = treeItem;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            GroupInfo groupInfo = (GroupInfo) treeItem.getData();
            try {
                groupInfoService.daleteGroupInfo(groupInfo.getId());
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功！");
                box.setText("提示");
                box.open();
                //刷新用户组树
                refreshUserTree();
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("警告");
                box.open();
            }
        }
    }
    
    /**
     * 添加子账户事件
     * @author caojia
     *
     */
    public class SubAccountAddSelection extends SelectionAdapter {
        
        private TreeItem treeItem;
        
        public SubAccountAddSelection(TreeItem treeItem) {
            this.treeItem = treeItem;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            GroupInfo groupInfo = (GroupInfo) treeItem.getData();
            SubAccountAddDialog subAccountAddDialog = new SubAccountAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo, AdminViewMain.this);
            subAccountAddDialog.open();
        }
    }
    
    /**
     * 修改子账户事件
     * @author caojia
     */
    public class SubAccountEditSelection extends SelectionAdapter {
        
        private TreeItem treeItem;
        
        public SubAccountEditSelection(TreeItem treeItem) {
            this.treeItem = treeItem;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            UserInfo userInfo = (UserInfo) treeItem.getData();
            GroupInfo groupInfo = (GroupInfo) treeItem.getParentItem().getData();
            SubAccountEditDialog subAccountEditDialog = new SubAccountEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL,
                    userInfo, groupInfo, AdminViewMain.this);
            subAccountEditDialog.open();
            
        }
        
    }
        
        
        
    
    /**
     * 删除子账户事件
     * @author caojia
     */
    public class SubAccountRemoveSelection extends SelectionAdapter {
        
        private TreeItem treeItem;
        
        public SubAccountRemoveSelection(TreeItem treeItem){
            this.treeItem = treeItem;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e){
            UserInfo userInfo = (UserInfo) treeItem.getData();
            try {
                MessageBox box1 = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK|SWT.CANCEL);
                box1.setMessage("删除子账户会将用户下所有合约规则删除，确定要删除子账户"+userInfo.getUserName()+"吗？");
                box1.setText("提示");
                if(box1.open() == SWT.CANCEL){
                    return;
                }
                
                AdminViewMain.this.userInfoService.deleteUser(userInfo);
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功！");
                box.setText("提示");
                box.open();
                //刷新用户组树
                refreshUserTree();
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("警告");
                box.open();
            }
        }
        
    }
    
    /**
     * 交易组规则查询
     * @author caojia
     */
    public class tradeRuleQuerySelection extends SelectionAdapter {
        
        private TreeItem treeItem;
        
        public tradeRuleQuerySelection(TreeItem treeItem) {
            this.treeItem = treeItem;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            refreshTradeRule(treeItem);
        }
    }
    
    
    /**
     * 添加组规则事件
     * @author caojia
     *
     */
    public class RuleAddSelection extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(tree.getItemCount()<1){
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组！");
                box.setText("警告");
                box.open();
                return ;
            }
            TreeItem item = tree.getSelection().length <1 ? tree.getTopItem() : tree.getSelection()[0];
            RuleAddDialog ruleAddDialog = new RuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, AdminViewMain.this ,item);
            ruleAddDialog.open();
        }
    }
    
    /**
     * 删除交易规则
     * @author caojia
     *
     */
    public class RuleRemoveSelection extends SelectionAdapter {
        
        private TableItem item;
        
        public RuleRemoveSelection(TableItem item) {
            this.item = item;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            UserContractTradeRule rule = (UserContractTradeRule) item.getData();
            logger.debug("删除ID为："+rule.getTradeRuleId()+"的交易规则,合约编号："+rule.getContractNo());
            try {
                tradeRuleService.removeUserTradeRule(rule.getTradeRuleId(),rule.getId());
                //删除合约对应的平仓规则
                closeRuleService.removeCloseRuleByContractNo(rule.getContractNo());
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功");
                box.setText("提示");
                box.open();
                refreshTradeRule(tree.getSelection()[0]);
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("错误");
                box.open();
            } 
        }
    }
    
    /**
     * 添加平仓组规则
     * @author caojia
     *
     */
    public class CloseRuleAddSelection extends SelectionAdapter{
        @Override
        public void widgetSelected(SelectionEvent e) {
            
            if(tree.getItemCount()<1){
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组！");
                box.setText(CommonConstant.MESSAGE_BOX_WARN);
                box.open();
                return ;
            }
            TreeItem item = tree.getSelection().length <1 ? tree.getTopItem() : tree.getSelection()[0];
            CloseRuleAddDialog closeRuleAddDialog = new CloseRuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, AdminViewMain.this ,item);
            closeRuleAddDialog.open();
        }
    }
    
    /**
     * 删除平仓组规则
     * @author caojia
     */
    public class CloseRuleRemoveSelection extends SelectionAdapter{
        
        private TableItem item;
        
        public CloseRuleRemoveSelection(TableItem item) {
            this.item = item;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            
            CloseRule closeRule = (CloseRule) item.getData();
            try {
                closeRuleService.removeCloseRuleById(closeRule.getId());
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功");
                box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
                box.open();
                refreshTradeRule(tree.getSelection()==null?null:tree.getSelection()[0]);
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                box.open();
            }
            
        }
    }
    
}
