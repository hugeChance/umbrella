package com.bohai.subAccount.swt.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.dao.CapitalRateMapper;
import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.dao.UseravailableindbMapper;
import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.FutureMarket;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.entity.SellDetail;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.Useravailableindb;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuyDetailService;
import com.bohai.subAccount.service.ClearService;
import com.bohai.subAccount.service.CloseRuleService;
import com.bohai.subAccount.service.FutureMarketService;
import com.bohai.subAccount.service.GroupInfoService;
import com.bohai.subAccount.service.GroupRuleService;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.service.PositionsDetailService;
import com.bohai.subAccount.service.SellDetailService;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.service.UserAvailableInDbService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.SettlemenetPart1Body;
import com.bohai.subAccount.vo.SettlemenetPart1Head;
import com.bohai.subAccount.vo.SettlemenetPart2Body;
import com.bohai.subAccount.vo.SettlemenetPart2Head;
import com.bohai.subAccount.vo.SettlemenetPart3Body;
import com.bohai.subAccount.vo.SettlemenetPart3Head;
import com.bohai.subAccount.vo.SettlemenetPart4Body;
import com.bohai.subAccount.vo.SettlemenetPart4Head;
import com.bohai.subAccount.vo.SettlemenetTitleVO;
import com.bohai.subAccount.vo.UserContractTradeRule;


public class MainForm {
    
    static Logger logger = Logger.getLogger(MainForm.class);
	
	TabItem tabItem_4;
	TabItem tabItem2;
	
	TabItem riskTabItem;

	//子账户表
	private Table subaccountTable;
	//用户合约信息表
	private Table instrumentTable;
	private Table riskGroupRuleTable;
	private Table riskInstrumentTable;
	private Table mainAccountTable;
	private Table tradeRuleTable;
	private Table allUserInstrumentTable;
	private Table instrumentUserTable;
	//风控用户表
	private Table riskUserTable;
	//所有风控规则列表
	private Table allRiskTable;
	
	//add COMPOSITE组件
	private Composite composite;
	private Button buttonComposite;
	private TabFolder tabFolder;
	private ExpandBar expandBar;
	private ToolBar toolBar;
	
	public  Tree userTree;
	public  Tree instrumentUserTree;
	public  Tree riskUserTree;
	public  Tree systemTree;

	private Shell shell;
	
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
    private UserInfoMapper userInfoMapper;
    
    private BuyDetailService buyDetailService;
	private PositionsDetailService positionsDetailService;
	private SellDetailService sellDetailService;
	private UserAvailableInDbService userAvailableInDbService;
	
	private Map<String,UserContract> mapUserContractMemorySave;
	
	private Composite composite_button;
	


	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainForm window = new MainForm();
			window.loadSpringContext();
		    
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setMemory(){
    	mapUserContractMemorySave = new HashMap<String,UserContract>();
    	
    	List<UserContract> listUserContract;
    	String userName = "";
		try {
			listUserContract = userContractService.queryUserContractByAll();
			for (UserContract userContract2 : listUserContract) {
				logger.info("setMemory="+JSON.toJSONString(userContract2));
				userName = userInfoMapper.getUserName(userContract2.getUserNo());
//				listUserContractMemorySave.set
				mapUserContractMemorySave.put(userName + userContract2.getContractNo(), userContract2);
				
				
			}
		} catch (FutureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        userInfoMapper = (UserInfoMapper)SpringContextUtil.getBean("userInfoMapper");
        
        sellDetailService = (SellDetailService) SpringContextUtil.getBean("sellDetailService");
        buyDetailService = (BuyDetailService) SpringContextUtil.getBean("buyDetailService");
        positionsDetailService = (PositionsDetailService) SpringContextUtil.getBean("positionsDetailService");
        
        setMemory();
    }

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shell = new Shell();

		shell.setSize(800, 600);
		shell.setMinimumSize(800, 600);
		shell.setText("交易资管");

		Rectangle bounds = Display.getDefault().getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		createMenu();

		final SashForm divider = new SashForm(shell, SWT.BORDER | SWT.VERTICAL);
		divider.setLayout(new FillLayout(SWT.HORIZONTAL));

		toolBar = new ToolBar(divider, SWT.FLAT | SWT.RIGHT | SWT.SHADOW_OUT);
		createToolItem();

		final SashForm bottom = new SashForm(divider, SWT.HORIZONTAL);
		bottom.setLayout(new FillLayout(SWT.HORIZONTAL));

		expandBar = new ExpandBar(bottom, SWT.BORDER);
		createExpd();

		tabFolder = new TabFolder(bottom, SWT.NONE);
		createTabItem();
		
		//caoxx add Composite start
//		composite = new TabFolder(bottom, SWT.NONE);
		
//	    Button btn = new Button(bottom, SWT.NONE);
        composite_button = new Composite(tabFolder, SWT.NONE);
        createButtonsViewer(composite_button);
		//caoxx add Composite end

		bottom.setWeights(new int[] { 165, 612 });
		divider.setWeights(new int[] { 37, 501 });

		bottom.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent e) {
				Point p = bottom.getSize();
				if (p.y > 500) {
					divider.setWeights(new int[] { 37, 501 });
				}
			}

			@Override
			public void controlMoved(ControlEvent paramControlEvent) {}
		});

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	

	private void createToolItem() {
		
		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.setImage(SWTResourceManager.getImage(MainForm.class,"add.png"));
		tltmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditDailog editDailog = new EditDailog(shell, SWT.DIALOG_TRIM
						| SWT.SYSTEM_MODAL);
				editDailog.open();
			}
		});
		tltmNewItem.setText("添加");

		new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.setText("修改");
		toolItem_1.setImage(SWTResourceManager.getImage(MainForm.class,"edit.png"));
		toolItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});

		new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem tltmNewItem_1 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_1.setText("删除");
		tltmNewItem_1.setImage(SWTResourceManager.getImage(MainForm.class,"del.png"));
		tltmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});

		new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem toolItem_2 = new ToolItem(toolBar, SWT.NONE);
		toolItem_2.setText("刷新");
		toolItem_2.setImage(SWTResourceManager.getImage(MainForm.class,"ref.png"));
		toolItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	private void createExpd() {

		ExpandItem expdItem1 = new ExpandItem(expandBar, SWT.NONE);
		expdItem1.setExpanded(true);
		expdItem1.setText("帐号设置"); // 帐号设置

		userTree = new Tree(expandBar, SWT.NONE);
		expdItem1.setControl(userTree);
		
		userTree.addMouseListener(new MouseAdapter() {
            //左侧导航树鼠标点击事件
            @Override
            public void mouseDown(MouseEvent e) {
                TreeItem selected=userTree.getItem(new Point(e.x,e.y));  //取节点控件
                Menu rightClickMenu = new Menu(userTree);
                //鼠标右键菜单
                userTree.setMenu(rightClickMenu);
                
                if(e.button == 1 && selected == null){//左键空白
                    return;
                }else if (e.button == 1 && selected != null) {//左键树节点
                    //logger.debug("鼠标左键点击用户组树节点："+selected.getText());
                    
                    //refreshTradeRule(selected);
                }else if(selected == null && e.button == 3){//右键空白
                    
                    MenuItem addGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    addGroupMenuItem.setText("添加用户组");
                    addGroupMenuItem.addSelectionListener(new GroupAddSelection());
                    
                }else if(selected.getData() != null && selected.getData() instanceof GroupInfo && e.button == 3){//右键用户组
                    
                    MenuItem addSubAccountMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    addSubAccountMenuItem.setText("添加子账户");
                    addSubAccountMenuItem.addSelectionListener(new SubAccountAddSelection(selected));
                    
                    MenuItem editGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    editGroupMenuItem.setText("修改用户组");
                    
                    MenuItem removeGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    removeGroupMenuItem.setText("删除用户组");
                    removeGroupMenuItem.addSelectionListener(new GroupRemoveSelection(selected));
                    
                    
                }else if(selected.getData() != null && selected.getData() instanceof UserInfo && e.button == 3){//右键用户
                    
                    MenuItem editGroupMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    editGroupMenuItem.setText("修改子账户");
                    //editGroupMenuItem.addSelectionListener(new SubAccountEditSelection(selected));
                    
                    MenuItem removeUserMenuItem = new MenuItem(rightClickMenu, SWT.NONE);
                    removeUserMenuItem.setText("删除子账户");
                    //removeUserMenuItem.addSelectionListener(new SubAccountRemoveSelection(selected));
                }
            }
        });


		//createSubMenu(userTree);

		Listener treeSelection = new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = userTree.getSelection();
				
				TreeItem item = selection[0];
				Table ctab = (Table)tabItem_4.getControl();
				
				if("主账号".equals(item.getText())){
				    if(ctab != mainAccountTable){
				        tabItem_4.setText("主账号明细");
				        tabItem_4.setControl(mainAccountTable);
				    }
                    refreshMainAccount();
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
                    if(riskTabItem != null && !riskTabItem.isDisposed()){
                        riskTabItem.dispose();
                    }
				}else if ("用户组".equals(item.getText())) {
				    /*if(ctab != subaccountTable){
				        tabItem_4.setText("子账号明细");
				        tabItem_4.setControl(subaccountTable);
				    }
                    refreshSubaccountByGroupId(null);
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }*/
                }else if (item.getData() != null && item.getData() instanceof GroupInfo) {
                    if(ctab != subaccountTable){
                        tabItem_4.setText("子账号明细");
                        tabItem_4.setControl(subaccountTable);
                    }
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
                    if(riskTabItem != null && !riskTabItem.isDisposed()){
                        riskTabItem.dispose();
                    }
                    refreshSubaccountByGroupId(((GroupInfo)item.getData()).getId());
                }
				
				
				/*for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					System.out.println("Selection={" + string + "}");
					Table ctab = (Table)tabItem_4.getControl();
					for (TreeItem item : userTree.getItems()) {

						if (item.getText().equals(selection[i].getText())) {
//							tabFolder.setSelection(4);
							
							
							
							if (ctab != mainAccountTable) {
								tabItem_4.setText("主账号明细");
								tabItem_4.setControl(mainAccountTable);
								refreshMainAccount();
								
								tabItem2 = new TabItem(tabFolder, SWT.NONE);
						        tabItem2.setText("tab2");
							}
							
							return;
						}
					}
//					tabFolder.setSelection(0);
					if (ctab != subaccountTable) {
						tabItem_4.setText("子账号明细");
						tabItem_4.setControl(subaccountTable);
						
						if(tabItem2 != null){
						    tabItem2.dispose();
						}
					}
					refreshSubaccount(selection[0]);

				}*/
				/*tabFolder.setSelection(4);
				if(selection[0].getData() instanceof GroupInfo){
				    tabItem_4.setText("主账号明细");
                    tabItem_4.setControl(mainAccountTable);
                    refreshMainAccount();
                    return;
				}else if (selection[0].getData() instanceof UserInfo) {
				    tabItem_4.setText("子账号明细");
                    tabItem_4.setControl(subaccountTable);
                    refreshSubaccount(selection[0]);
                }*/
				
			}
		};

		userTree.addListener(SWT.Selection, treeSelection);

		/*TreeItem treeItem1 = new TreeItem(userTree, SWT.NONE);
		treeItem1.setText("\u8D6B\u57CE\u8F6F\u4EF6"); // 赫城软件

		// treeItem1.addListener(eventType, listener);

		TreeItem treeSubItem1 = new TreeItem(treeItem1, SWT.NONE);
		treeSubItem1.setText("子账号1");

		TreeItem treeItem2 = new TreeItem(userTree, SWT.NONE);
		treeItem2.setText("\u5F6D\u535A"); // 彭博
		
		TreeItem treeItem = new TreeItem(userTree, SWT.NONE);
		treeItem.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		treeItem.setText("New TreeItem");*/
		refreshUserTree(userTree,"1");
		
		expdItem1.setHeight(150);

		ExpandItem expdItem2 = new ExpandItem(expandBar, SWT.NONE);
		expdItem2.setText("合约设置"); // 合约设置

		instrumentUserTree = new Tree(expandBar, SWT.NONE);
		expdItem2.setControl(instrumentUserTree);
		//createSubMenu(instrumentUserTree);

		Listener tree1Selection = new Listener() {
			/*public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = instrumentUserTree.getSelection();
				Table ctab = (Table)tabItem_4.getControl();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					System.out.println("Selection={" + string + "}");
					if (ctab != instrumentTable) {
						tabItem_4.setText("合约明细");
						tabItem_4.setControl(instrumentTable);
					}
					refreshContract(instrumentUserTree.getSelection()[0]);
					//tabFolder.setSelection(1); // 合约明细
				}
			}*/
		    
		    public void handleEvent(Event e) {
                String string = "";
                TreeItem selection = instrumentUserTree.getSelection()[0];
                Table ctab = (Table)tabItem_4.getControl();
                
                if("合约交易规则".equals(selection.getText())){
                    if(ctab != tradeRuleTable){
                        tabItem_4.setText("合约交易规则");
                        tabItem_4.setControl(tradeRuleTable);
                    }
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
                    if(riskTabItem != null && !riskTabItem.isDisposed()){
                        riskTabItem.dispose();
                    }
                    refreshContractTradeRule();
                    
                }else if ("所有合约".equals(selection.getText())) {
                    if(ctab != allUserInstrumentTable){
                        tabItem_4.setText("所有合约");
                        tabItem_4.setControl(allUserInstrumentTable);
                    }
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
                    if(riskTabItem != null && !riskTabItem.isDisposed()){
                        riskTabItem.dispose();
                    }
                    refreshAllUserInstruments();
                }else if ("按用户查找合约".equals(selection.getText())) {
                    if(ctab != instrumentUserTable){
                        tabItem_4.setText("按用户查询");
                        tabItem_4.setControl(instrumentUserTable);
                    }
                    if(riskTabItem != null && !riskTabItem.isDisposed()){
                        riskTabItem.dispose();
                    }
                    refreshInstrumentUserTable();
                }
                
                
                /*for (int i = 0; i < selection.length; i++) {
                    
                    string += selection[i] + " ";
                    System.out.println("Selection={" + string + "}");
                    for (TreeItem item : instrumentUserTree.getItems()) {

                        if (item.getText().equals(selection[i].getText())) {
                            
                            if (ctab != tradeRuleTable) {
                                tabItem_4.setText("合约交易规则");
                                tabItem_4.setControl(tradeRuleTable);
                                refreshContractTradeRule();
                            }
                            
                            return;
                        }
                    }

                    if (ctab != instrumentTable) {
                        tabItem_4.setText("合约明细");
                        tabItem_4.setControl(instrumentTable);
                    }
                    refreshContract(instrumentUserTree.getSelection()[0]);
                    //tabFolder.setSelection(1); // 合约明细
                }*/
            }
		};

		instrumentUserTree.addListener(SWT.Selection, tree1Selection);
		//合约用户树
		//refreshUserTree(instrumentUserTree);
		TreeItem treeItem = new TreeItem(instrumentUserTree, SWT.NONE);
		treeItem.setText("合约交易规则");
		TreeItem treeItem1 = new TreeItem(instrumentUserTree, SWT.NONE);
		treeItem1.setText("所有合约");
		TreeItem treeItem2 = new TreeItem(instrumentUserTree, SWT.NONE);
		treeItem2.setText("按用户查找合约");
		
		expdItem2.setHeight(150);
		

		ExpandItem expdItem3 = new ExpandItem(expandBar, SWT.NONE);
		expdItem3.setHeight(150);
		expdItem3.setText("风控设置"); // 风控设置

		riskUserTree = new Tree(expandBar, SWT.NONE);
		expdItem3.setControl(riskUserTree);

		//createSubMenu(riskUserTree);

		Listener tree2Selection = new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem selection = riskUserTree.getSelection()[0];
				Table ctab = (Table)tabItem_4.getControl();
				
				if("风控组规则".equals(selection.getText())){
				    if (ctab != riskGroupRuleTable) {
                        tabItem_4.setText("风控组规则明细");
                        tabItem_4.setControl(riskGroupRuleTable);
                        if(tabItem2 != null && !tabItem2.isDisposed()){
                            tabItem2.dispose();
                        }
                        
                        if(riskTabItem != null && !riskTabItem.isDisposed()){
                            riskTabItem.dispose();
                        }
                        refreshGroupRuleTable();
                    }
				}else if ("所有风控明细".equals(selection.getText())) {
				    if (ctab != allRiskTable) {
                        tabItem_4.setText("风控合约规则明细");
                        tabItem_4.setControl(allRiskTable);
                    }
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
                    if(riskTabItem != null && !riskTabItem.isDisposed()){
                        riskTabItem.dispose();
                    }
                    refreshAllRiskTable();
                }else if ("按用户查找风控明细".equals(selection.getText())) {
                    if (ctab != riskUserTable) {
                        tabItem_4.setText("按用户查询风控");
                        tabItem_4.setControl(riskUserTable);
                    }
                    if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
                    refreshRiskUserTable();
                }
				
				
				/*for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					System.out.println("Selection={" + string + "}");
					for (TreeItem item : riskUserTree.getItems()) {
						if (item.getText().equals(selection[i].getText())) {
							if (ctab != riskGroupRuleTable) {
								tabItem_4.setText("风控组规则明细");
								tabItem_4.setControl(riskGroupRuleTable);
								if(tabItem2 != null && !tabItem2.isDisposed()){
			                        tabItem2.dispose();
			                    }
								refreshGroupRuleTable();
							}
							//tabFolder.setSelection(2); // 风控组
							return;
						}
					}
					
					if (ctab != riskInstrumentTable) {
						tabItem_4.setText("风控合约规则明细");
						tabItem_4.setControl(riskInstrumentTable);
					}
					if(tabItem2 != null && !tabItem2.isDisposed()){
                        tabItem2.dispose();
                    }
					refreshRiskClose(selection[0]);
					//tabFolder.setSelection(3); // 风控明细
				}*/
			}
		};
		
		//caoxx add
		Listener systemTreeSelection = new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem selection = systemTree.getSelection()[0];
				 if ("日初设置".equals(selection.getText())) {
                    
                    tabItem_4.setText("日初设置");
                 
                    tabItem_4.setControl(null);
                
				 }
		
				
				if("日初设置".equals(selection.getText())){
					MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION
							| SWT.OK | SWT.CANCEL);
					box.setMessage("日初设置是否开始？");
//					box.open();
					if(box.open() == SWT.OK){
						
						MessageBox boxrepeat = new MessageBox(shell, SWT.ICON_INFORMATION
								| SWT.OK | SWT.CANCEL);
						boxrepeat.setMessage("日初设置会清除当日数据是否开始？");
						
						if(boxrepeat.open() == SWT.OK){
							System.out.println("日初开始");
							
							ClearService clearService = (ClearService) SpringContextUtil.getBean("clearService");
			                clearService.init();
			                
			                
			                MessageBox box2 = new MessageBox(shell, SWT.ICON_INFORMATION
									| SWT.OK );
							box2.setMessage("日初设置完成！");
						}
					}
				}else if ("结算设置".equals(selection.getText())) {
					if ("结算设置".equals(selection.getText())) {
	                    
	                    tabItem_4.setText("结算设置");
	                    tabItem_4.setControl(null);
	                
					 }
					MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION
							| SWT.OK | SWT.CANCEL);
					box.setMessage("结算设置是否开始？");
					if(box.open() == SWT.OK){
						MessageBox boxrepeat = new MessageBox(shell, SWT.ICON_INFORMATION
								| SWT.OK | SWT.CANCEL);
						boxrepeat.setMessage("结算设置开始再次确认？请耐心等待10秒钟！");
						if(boxrepeat.open() == SWT.OK){
							System.out.println("结算开始");
							
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
								Thread.sleep(10000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            	
			            	//生成结算单
			            	settlement();
			            	
			            	ClearService clearService = (ClearService) SpringContextUtil.getBean("clearService");
//			                //原系统备份和删除 
							clearService.backUp();
							
							MessageBox box3 = new MessageBox(shell, SWT.ICON_INFORMATION
									| SWT.OK | SWT.CANCEL);
							box3.setMessage("结算设置完成");
						}
						
					}
                }
				
				
			}
		};
		
		
		//caoxx add end
		
		
		ExpandItem expdItem4 = new ExpandItem(expandBar, SWT.NONE);
		expdItem4.setExpanded(true);
		expdItem4.setHeight(150);
		expdItem4.setText("系统设置"); // 系统设置
		
		systemTree = new Tree(expandBar, SWT.NONE);
		expdItem4.setControl(systemTree);
		
		systemTree.addListener(SWT.Selection, systemTreeSelection);
		TreeItem treeSysItem1 = new TreeItem(systemTree, SWT.NONE);
		treeSysItem1.setText("日初设置");
		treeSysItem1.setExpanded(true);

		TreeItem treeSysItem2 = new TreeItem(systemTree, SWT.NONE);
		treeSysItem2.setText("结算设置");
		
		
		
		

		riskUserTree.addListener(SWT.Selection, tree2Selection);
		//refreshUserTree(riskUserTree);
		TreeItem tree = new TreeItem(riskUserTree, SWT.NONE);
		tree.setText("风控组规则");
		
		TreeItem tree1 = new TreeItem(riskUserTree, SWT.NONE);
		tree1.setText("所有风控明细");
		
		TreeItem tree2 = new TreeItem(riskUserTree, SWT.NONE);
		tree2.setText("按用户查找风控明细");

		/*TreeItem treeItem4 = new TreeItem(tree_2, SWT.NONE);
		treeItem4.setText("风控组");

		TreeItem treeSubItem4_1 = new TreeItem(treeItem4, SWT.NONE);
		treeSubItem4_1.setText("风控明细");
		treeItem4.setExpanded(true);*/

	}
	
	public int settlement(){
    	//生成结算
    	DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setText("目录选择");
        dialog.setMessage("结算文件存放目录");
        String saveFile=dialog.open();  
        String tmpStr = "";
        
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        
        if(saveFile!=null){
        	File directiory=new File(saveFile);
            logger.info(directiory.getPath());
            List<Useravailableindb> listUseravailableindb = new ArrayList<Useravailableindb>();
            listUseravailableindb = useravailableindbMapper.selectAll();
            
            //循环按每个客户出文件
            for (Useravailableindb useravailableindb : listUseravailableindb) {
            	StringBuffer strB = new StringBuffer();
            	SettlemenetTitleVO settlemenetTitleVO = new SettlemenetTitleVO();
            	settlemenetTitleVO.setCompanyName("交易软件");
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
//            	BigDecimal balanceBDec = useravailableindb.getAvailable().add(useravailableindb.getFrozenavailable()).add(useravailableindb.getMargin()) ;
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
//            	BigDecimal risk_DegreeBDec = useravailableindb.getMargin().divide(balanceBDec);
            	double dbltmp = useravailableindb.getMargin().doubleValue() / balanceBDec.doubleValue();
            	dbltmp = Math.round(dbltmp);
            	settlemenetTitleVO.setRisk_Degree(String.valueOf(dbltmp));
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
            	SettlemenetPart4Head settlemenetPart4Head = new SettlemenetPart4Head();
            	SettlemenetPart1Body settlemenetPart1Body = new SettlemenetPart1Body();
            	SettlemenetPart2Body settlemenetPart2Body = new SettlemenetPart2Body();
            	SettlemenetPart3Body settlemenetPart3Body = new SettlemenetPart3Body();
            	SettlemenetPart4Body settlemenetPart4Body = new SettlemenetPart4Body();
            	
            
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
            	//由COREAPP 增加新功能当平仓时写入平仓表
            	//select COMBOKEY from T_SELL_DETAIL where SUBUSERID = '11111' and TRADEDATE = '20170611'
            	//先查询平仓明细表
        
            	try {
					List<SellDetail> listSellDetail = sellDetailService.getSellDetailForUser(useravailableindb.getUsername(),dateString);
					
					if(listSellDetail.size() > 0){
						strB.append(settlemenetPart2Head.getRetPart2Head1());
						strB.append("\r\n");
					
					}
						
					
					for (SellDetail sellDetail : listSellDetail) {
						//拿着平仓COMBOKEY去查开仓
						List<BuyDetail> listBuyDetail = buyDetailService.getBuyDetailForComboKey2(sellDetail.getCombokey());
						
						
						for (BuyDetail buyDetail : listBuyDetail) {
							//输出平仓明细
							settlemenetPart2Body.setCloseDate(sellDetail.getTradedate());
							settlemenetPart2Body.setExchange(buyDetail.getExchangeid());
							settlemenetPart2Body.setProduct(buyDetail.getInstrumentid());
							settlemenetPart2Body.setInstrument(buyDetail.getInstrumentid());
							settlemenetPart2Body.setOpenDate(buyDetail.getTradedate());
							if(buyDetail.getDirection().equals("0")){
								settlemenetPart2Body.setBS("买");
							} else {
								settlemenetPart2Body.setBS("卖");
							}
							if(sellDetail.getVolume() > buyDetail.getVolume()){
								settlemenetPart2Body.setLots(String.valueOf(buyDetail.getVolume()));
							} else {
								settlemenetPart2Body.setLots(String.valueOf(sellDetail.getVolume()));
							}
							settlemenetPart2Body.setPos(buyDetail.getPrice().toString());
							//昨结算 要实装 T_FUTURE_MARKET表PRE_SETTLEMENT_PRICE
							settlemenetPart2Body.setPrev("");
							settlemenetPart2Body.setTrans(sellDetail.getPrice().toString());
							settlemenetPart2Body.setRealized(sellDetail.getPrice().subtract(buyDetail.getPrice()).toString());
							settlemenetPart2Body.setPremium("0.00");
							
							strB.append(settlemenetPart2Body.getRetStr());
							strB.append("\r\n");
							
						}
					}
					
				} catch (FutureException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	
            	
            	//查询持仓明细表
            	//由COREAPP 增加新功能当平仓时写入平仓表
            	//select * from T_POSITIONS_DETAIL where SUBUSERID = '1111111'
            	//取得最新客户持仓表的信息
            
            	try {
					List<PositionsDetail> listPositionsDetail = positionsDetailService.getPositionsForUser(useravailableindb.getUsername(),dateString);
					
					if(listPositionsDetail.size() > 0){
						strB.append(settlemenetPart4Head.getRetPart4Head1());
						strB.append("\r\n");
						
					}
					for (PositionsDetail positionsDetail : listPositionsDetail) {
						
						List<BuyDetail> listBuyDetail = buyDetailService.getBuyDetailForComboKey(positionsDetail.getCombokey());
						
						
						for (BuyDetail buyDetail : listBuyDetail) {
							//输出平仓明细
							settlemenetPart4Body.setExchange(buyDetail.getExchangeid());
							settlemenetPart4Body.setProduct(buyDetail.getInstrumentid());
							settlemenetPart4Body.setInstrument(buyDetail.getInstrumentid());
							settlemenetPart4Body.setOpenDate(buyDetail.getTradedate());
							settlemenetPart4Body.setSH("投");
							if(buyDetail.getDirection().equals("0")){
								settlemenetPart4Body.setBS("买");
							} else {
								settlemenetPart4Body.setBS("卖");
							}
							settlemenetPart4Body.setPositon(String.valueOf(positionsDetail.getVolume()));
							settlemenetPart4Body.setPrice(buyDetail.getPrice().toString());
							//昨结算 需要实装
							BigDecimal prev = new BigDecimal(0) ;
							settlemenetPart4Body.setPrev(prev.toString());
							
							//结算价 需要实装
							BigDecimal settlement =new BigDecimal(0) ;
							settlemenetPart4Body.setSettlement(prev.toString());
							
							//Accum 浮动盈亏
							settlemenetPart4Body.setAccum("0");
							
							//盯市盈亏
							settlemenetPart4Body.setMTM(buyDetail.getPrice().subtract(settlement).toString());
							
							//保证金
							settlemenetPart4Body.setMargin("");
							
							//期权市值
							settlemenetPart4Body.setMarketValue("0.00");
							
							
							
							
							strB.append(settlemenetPart4Body.getRetStr());
							strB.append("\r\n");
							
						}
						
					}
				} catch (FutureException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            
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
		            			settlemenetPart3Body.setAvgBuyPrice(investorPosition.getPositioncost() ==null? "":investorPosition.getPositioncost().toString());
		            			settlemenetPart3Body.setShortPos("0");
		            			settlemenetPart3Body.setAvgSellPrice("0");

	            			} else {
	            				//卖
	            				settlemenetPart3Body.setLongPos("0");
		            			settlemenetPart3Body.setAvgBuyPrice("0");
		            			settlemenetPart3Body.setShortPos(investorPosition.getPosition().toString());
		            			settlemenetPart3Body.setAvgSellPrice(investorPosition.getPositioncost() ==null? "":investorPosition.getPositioncost().toString());
		            			
	            			}
	            			//caoxx tempupdate start
//	            			FutureMarket futureMarket = this.futureMarketService.queryFutureMarketByInstrument(investorPosition.getInstrumentid());
//	            			
//	            			settlemenetPart3Body.setPrev(futureMarket.getPreSettlementPrice().toString());
//	            			settlemenetPart3Body.setSttlToday(futureMarket.getSettlementPrice());
//	            			double mTM = 0;
//	            			if(investorPosition.getPositioncost() == null){
//	            				mTM = Double.valueOf(futureMarket.getSettlementPrice()) ;
//	            			} else {
//	            				mTM = investorPosition.getPositioncost().doubleValue() - Double.valueOf(futureMarket.getSettlementPrice()) ;
//	            			}
//	            			
//	            			mTM = mTM * investorPosition.getPosition().doubleValue();
//	            			settlemenetPart3Body.setMTM(String.valueOf(mTM));
//	            			settlemenetPart3Body.setMarginOccupied(investorPosition.getUsemargin().toString());
//	            			settlemenetPart3Body.setSH("投");
//	            			settlemenetPart3Body.setMarketValueLong("0.00");
//	            			settlemenetPart3Body.setMarketValueShort("0.00");
////	            			fileLineWrite(directiory.getPath(),useravailableindb.getUsername(),settlemenetPart3Body.getRetStr());
//	            			strB.append(settlemenetPart3Body.getRetStr());
	            			//caoxx tempupdate
	            			
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

	private void createSubMenu(Tree tree) {
	    
		Menu menu = new Menu(tree);
		
		MenuItem menuItemAdd = new MenuItem(menu, SWT.PUSH);
		menuItemAdd.setText("添加");
		menuItemAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("====================添加");
				EditDailog editDailog = new EditDailog(shell, SWT.DIALOG_TRIM
						| SWT.SYSTEM_MODAL);
				editDailog.open();
			}
		});

		MenuItem menuItemEdit = new MenuItem(menu, SWT.PUSH);
		menuItemEdit.setText("修改");
		menuItemEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("====================修改");
			}
		});

		MenuItem menuItemDel = new MenuItem(menu, SWT.PUSH);
		menuItemDel.setText("删除");

		menuItemDel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("====================删除");
			}
		});

		tree.setMenu(menu);
	}

	private void createTabItem() {
		
		
		//创建主账号表
		createMainAccount(tabFolder);
		
		//创建子账户表
		createSubaccountTable(tabFolder);

		//创建合约属性表
		createRuleTable(tabFolder);
		
		//创建合约交易规则表
		createContractTradeRuleTable(tabFolder);
		
		//创建风控组规则表格
		createUserRuleTable(tabFolder);
		
		//创建风控规则表格
		createCloseRuleTable(tabFolder);
		
		//创建所有用户的合约
		createAllUserInstrumentsTable(tabFolder);
		
		//创建用户拥有的合约数量
		createInstrumentUserTable(tabFolder);
		
		//
		createAllRiskTable(tabFolder);
		
		createRiskUserTable(tabFolder);
		
	}

	private void createMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem subMenu1 = new MenuItem(menu, SWT.CASCADE);
		subMenu1.setText("系统");

		Menu menu_1 = new Menu(subMenu1);
		subMenu1.setMenu(menu_1);

		MenuItem menuItem1_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem1_1.setText("关闭");

		menuItem1_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//MenuItem selected = (MenuItem) event.widget;
				shell.close();
			}
		});

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem subMenu2 = new MenuItem(menu, SWT.CASCADE);
		subMenu2.setText("业务管理");

		Menu menu_2 = new Menu(subMenu2);
		subMenu2.setMenu(menu_2);

		MenuItem menuItem2_1 = new MenuItem(menu_2, SWT.NONE);
		menuItem2_1.setText("账号");
		menuItem2_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				expandBar.getItem(0).setExpanded(true);
			}
		});

		MenuItem menuItem2_3 = new MenuItem(menu_2, SWT.NONE);
		menuItem2_3.setText("合约");
		menuItem2_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				expandBar.getItem(1).setExpanded(true);
			}
		});

		MenuItem menuItem = new MenuItem(menu_2, SWT.NONE);
		menuItem.setText("风控");
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				expandBar.getItem(2).setExpanded(true);
			}
		});

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem subMenu3 = new MenuItem(menu, SWT.CASCADE);
		subMenu3.setText("操作");

		Menu menu_3 = new Menu(subMenu3);
		subMenu3.setMenu(menu_3);

		MenuItem menuItem3_1 = new MenuItem(menu_3, SWT.NONE);
		menuItem3_1.setText("添加");
		menuItem3_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				EditDailog editDailog = new EditDailog(shell, SWT.DIALOG_TRIM
						| SWT.SYSTEM_MODAL);
				editDailog.open();
			}
		});

		MenuItem menuItem3_2 = new MenuItem(menu_3, SWT.NONE);
		menuItem3_2.setText("修改");

		MenuItem menuItem3_3 = new MenuItem(menu_3, SWT.NONE);
		menuItem3_3.setText("删除");

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem subMenu4 = new MenuItem(menu, SWT.CASCADE);
		subMenu4.setText("帮助");

		Menu menu_4 = new Menu(subMenu4);
		subMenu4.setMenu(menu_4);

		MenuItem menuItem4_1 = new MenuItem(menu_4, SWT.NONE);
		menuItem4_1.setText("版本号");
		menuItem4_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// MessageDialog.openInformation(shell, "test", "test");

				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION
						| SWT.OK);
				box.setMessage("交易资管系统 V2.0");
				box.open();
			}
		});

		MenuItem menuItem4_2 = new MenuItem(menu_4, SWT.NONE);
		menuItem4_2.setText("联系电话：13916702735");
		menuItem4_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// MessageDialog.openInformation(shell, "test", "test");

				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION
						| SWT.OK);
				box.setMessage("联系电话联系电话联系电话联系电话");
				box.open();
			}
		});
	}
	
	/**
     * 用户组树结构
     * @param tree
     */
    public void refreshUserTree(Tree tree,String ... strings){
        //清空tree
        tree.removeAll();
        
        if(strings != null && strings.length >0){
            
            TreeItem mainAccountTreeItem = new TreeItem(tree, SWT.NONE);
            mainAccountTreeItem.setText("主账号");
            
            TreeItem groupTopTreeItem = new TreeItem(tree, SWT.NONE);
            groupTopTreeItem.setText("用户组");
          //查询用户组
            List<GroupInfo> groupInfos = null;
            try {
                groupInfos = groupInfoService.getGroups();
            } catch (FutureException e1) {
                logger.error(e1.getMessage());
            }
            
            if(groupInfos != null){
                
                for (GroupInfo groupInfo : groupInfos) {
                    TreeItem groupTreeItem = new TreeItem(groupTopTreeItem, SWT.NONE);
                    groupTreeItem.setText(groupInfo.getGroupName());
                    groupTreeItem.setData(groupInfo);
                    //查询组用户
                    /*try {
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
                    }*/
                    //展开树节点
                    groupTreeItem.setExpanded(true);
                }
            }
            
        }else {
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
                    groupTreeItem.setText(groupInfo.getGroupName());
                    groupTreeItem.setData(groupInfo);
                    //查询组用户
                    try {
                        List<UserInfo> userInfos = this.userInfoService.getUsersByGroupId(groupInfo.getId());
                        if(userInfos != null){
                            for (UserInfo userInfo : userInfos) {
                                TreeItem userTreeItem = new TreeItem(groupTreeItem, SWT.NONE);
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
            int i = 0;
            for (MainAccount mainAccount : list) {
                TableItem item = new TableItem(mainAccountTable, SWT.NULL);
                item.setData(mainAccount);
                item.setText(0,++i +"");
                item.setText(1,mainAccount.getBrokerId());
                item.setText(2,mainAccount.getAccountNo());
                item.setText(3, mainAccount.getPasswd());
                if(!StringUtils.isEmpty(mainAccount.getAccountType())){
                    item.setText(4,mainAccount.getAccountType().equals("1") ? "账户主" : "账户备");
                }
                //item.setText(4, mainAccount.getCreateTime()==null?"":DateFormatterUtil.getDateStr(mainAccount.getCreateTime()));
                //item.setText(5, mainAccount.getUpdateTime()==null?"":DateFormatterUtil.getDateStr(mainAccount.getUpdateTime()));
            }
        }
        
    }
    
    public void refreshSubaccountByGroupId(String groupId){
      //清空子账户表
        subaccountTable.removeAll();
        UserInfo info = new UserInfo();
        info.setGroupId(groupId);
        List<Map<String,Object>> list = userInfoMapper.selectUserInfoByGroupId(info);
        
        if(list != null && list.size() > 0){
            int i = 0;
            for (Map<String, Object> map : list) {
                TableItem item = new TableItem(subaccountTable, SWT.NULL);
                
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName((String) map.get("USER_NAME"));
                userInfo.setUserNo((String) map.get("USER_NO"));
                userInfo.setId((String) map.get("ID"));
                userInfo.setGroupId((String) map.get("GROUP_ID"));
                userInfo.setGroupName((String) map.get("GROUP_NAME"));
                userInfo.setUserPwd((String) map.get("USER_PWD"));
                userInfo.setCapital((BigDecimal) map.get("CAPITAL"));
                userInfo.setForceRate((String) map.get("FORCE_RATE"));
                userInfo.setForceLimit((String) map.get("FORCE_LIMIT"));
                item.setData(userInfo);
                
                item.setText(0, ++i +"");
                //用户名
                item.setText(1, (String) map.get("USER_NAME"));
                //密码
                item.setText(2, (String) map.get("USER_PWD"));
                //初始资金
                BigDecimal capital = (BigDecimal) map.get("CAPITAL");
                if(capital != null){
                    item.setText(3, capital.toString());
                }
                
                if(map.get("HOST_CAPITAL1") != null){
                    //资金调入金额
                    item.setText(4, ((BigDecimal) map.get("HOST_CAPITAL1")).toString());
                }
                
                //强平比例
                item.setText(5, StringUtils.isEmpty(userInfo.getForceRate())?"":userInfo.getForceRate());
                
                //强平金额
                item.setText(6, StringUtils.isEmpty(userInfo.getForceLimit())?"":userInfo.getForceLimit());
                
                //动态权益
                item.setText(7, ((BigDecimal)map.get("RIGHTS")).toString());
                //可用资金
                item.setText(8, ((BigDecimal)map.get("AVAILABLE")).toString());
                //持仓盈亏
                item.setText(9, ((BigDecimal)map.get("POSITIONWIN")).toString());
                //平仓盈亏
                item.setText(10, ((BigDecimal)map.get("CLOSEWIN")).toString());
                //占用保证金
                item.setText(11, ((BigDecimal)map.get("MARGIN")).toString());
                
            }
        }
    }
    
    public void refreshSubaccount(TreeItem treeItem){
        
        UserInfo userInfo = (UserInfo) treeItem.getData();
        //清空子账户表
        subaccountTable.removeAll();
        
        Useravailableindb useravailableindb = useravailableindbMapper.selectByUserName(userInfo.getUserName());
        
        TableItem item = new TableItem(subaccountTable, SWT.NULL);
        if(useravailableindb != null){
            //动态权益
            item.setText(0, useravailableindb.getAvailable().add(useravailableindb.getMargin()).toString());
            //可用资金
            item.setText(1, useravailableindb.getAvailable().toString());
            //持仓盈亏
            item.setText(2, useravailableindb.getPositionwin().toString());
            //平仓盈亏
            item.setText(3, useravailableindb.getClosewin().toString());
        }else {
            //动态权益
            item.setText(0, userInfo.getCapital().toString());
            //可用资金
            item.setText(1, userInfo.getCapital().toString());
            //持仓盈亏
            item.setText(2, "0");
            //平仓盈亏
            item.setText(3, "0");
        }
        
    }
    
    /**
     * 刷新合约
     * @param treeItem
     */
    public void refreshContract(TreeItem treeItem){
        //查询所有用户组交易规则
        if(treeItem == null){
            
        }else if(treeItem.getData() instanceof GroupInfo){
            
        }else if (treeItem.getData() instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) treeItem.getData();
            GroupInfo groupInfo = (GroupInfo) treeItem.getParentItem().getData();
            List<UserContractTradeRule> list = null;
            try {
                list = tradeRuleService.getTradeRulesByUserNo(userInfo.getUserNo());
                instrumentTable.removeAll();
                if(list != null && list.size() >0){
                    for (UserContractTradeRule rule : list) {
                        TableItem item= new TableItem(instrumentTable, SWT.NONE);
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
            /*List<CloseRule> closeRules = null;
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
            }*/
        }
    }
    
    /**
     * 刷新合约
     * @param treeItem
     */
    public void refreshUserContract(String userNo){
        
        try {
            List<UserContractTradeRule> list = tradeRuleService.getTradeRulesByUserNo(userNo);
            instrumentTable.removeAll();
            if(list != null && list.size() >0){
                int i =0 ;
                for (UserContractTradeRule rule : list) {
                    TableItem item= new TableItem(instrumentTable, SWT.NONE);
                    item.setData(rule);
                    item.setText(0, ++i +"");
                    item.setText(1, rule.getUserName());//用户名
                    item.setText(2, rule.getContractNo());//合约
                    /*item.setText(3, StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());//撤单数
                    item.setText(4, StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());//委托数
                    item.setText(5, StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());//开仓数
*/                    item.setText(3, StringUtils.isEmpty(rule.getOpenCharge())?"":rule.getOpenCharge().toString());//开仓手续费固定值
                    item.setText(4, StringUtils.isEmpty(rule.getOpenChargeRate())?"":rule.getOpenChargeRate().toString());//开仓手续费比例
                    item.setText(5, StringUtils.isEmpty(rule.getCloseCurrCharge())?"":rule.getCloseCurrCharge().toString());//平今手续费固定值
                    item.setText(6, StringUtils.isEmpty(rule.getCloseCurrChargeRate())?"":rule.getCloseCurrChargeRate().toString());//平今手续费比例
                    item.setText(7, StringUtils.isEmpty(rule.getMargin())?"":rule.getMargin().toString());//保证金比例
                    item.setText(8, StringUtils.isEmpty(rule.getContractUnit())?"":rule.getContractUnit().toString());//合约单位
                    item.setText(9, StringUtils.isEmpty(rule.getTickSize())?"":rule.getTickSize().toString());//最小跳动单位
                }
            }
        } catch (FutureException e) {
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
    }
    
    /**
     * 查询合约交易规则
     */
    public void refreshContractTradeRule(){
        
        tradeRuleTable.removeAll();
        
        List<TradeRule> rules = null;
        
        try {
            rules = this.tradeRuleService.getTradeRulesByAll();
            
            if(rules != null){
                int i = 0;
                for(TradeRule rule :rules){
                    TableItem item= new TableItem(tradeRuleTable, SWT.NONE);
                    item.setData(rule);
                    item.setText(0, ++i +"");
                    item.setText(1, rule.getContract());//合约
                    item.setText(2, StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());//撤单数
                    item.setText(3, StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());//委托数
                    item.setText(4, StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());//开仓数
                }
            }
        } catch (FutureException e) {
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
        
    }
    
    public void refreshAllUserInstruments(){
        allUserInstrumentTable.removeAll();
        
        try {
            List<UserContract> list = userContractService.queryUserContractByAll();
            if(list != null && list.size() >0){
                int i = 0;
                for (UserContract userContract : list) {
                    TableItem item= new TableItem(allUserInstrumentTable, SWT.NONE);
                    item.setData(userContract);
                    item.setText(0, ++i +"");
                    item.setText(1, userContract.getUserName());
                    item.setText(2, userContract.getContractNo());
                    item.setText(3, StringUtils.isEmpty(userContract.getOpenCharge())?"":userContract.getOpenCharge().toString());//开仓手续费固定值
                    item.setText(4, StringUtils.isEmpty(userContract.getOpenChargeRate())?"":userContract.getOpenChargeRate().toString());//开仓手续费比例
                    item.setText(5, StringUtils.isEmpty(userContract.getCloseCurrCharge())?"":userContract.getCloseCurrCharge().toString());//平今手续费固定值
                    item.setText(6, StringUtils.isEmpty(userContract.getCloseCurrChargeRate())?"":userContract.getCloseCurrChargeRate().toString());//平今手续费比例
                    item.setText(7, StringUtils.isEmpty(userContract.getMargin())?"":userContract.getMargin().toString());//保证金比例
                    item.setText(8, StringUtils.isEmpty(userContract.getContractUnit())?"":userContract.getContractUnit().toString());//合约单位
                    item.setText(9, StringUtils.isEmpty(userContract.getTickSize())?"":userContract.getTickSize().toString());//最小跳动单位
                }
            }
        } catch (FutureException e) {
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
    }
    
    /**
     * 刷新所有平仓规则
     */
    public void refreshAllRiskTable(){
        allRiskTable.removeAll();
        try {
            List<CloseRule> list = this.closeRuleService.getAllCloseRule();
            if(list != null && list.size() >0){
                int i = 0;
                for (CloseRule closeRule : list) {
                    
                    TableItem item= new TableItem(allRiskTable, SWT.NONE);
                    item.setData(closeRule);
                    item.setText(0, ++i+"");
                    item.setText(1, closeRule.getUserName());//用户名
                    item.setText(2, closeRule.getContractNo());//合约
                    item.setText(3, StringUtils.isEmpty(closeRule.getTickSize())?"":closeRule.getTickSize().toString());//最小跳动单位
                    item.setText(4, StringUtils.isEmpty(closeRule.getHop())?"":closeRule.getHop().toString());//跳数
                    item.setText(5, StringUtils.isEmpty(closeRule.getForceCloseRate())?"":closeRule.getForceCloseRate().toString());
                }
            }
        } catch (FutureException e) {
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
        
    }
    
    /**
     * 刷新用户合约信息
     */
    public void refreshInstrumentUserTable(){
        instrumentUserTable.removeAll();
        
        try {
            List<Map<String,Object>> list = this.userInfoMapper.selectUserInstrumentCount();
            if(list != null && list.size() >0){
                int i = 0;
                for (Map<String,Object> map : list) {
                    TableItem item= new TableItem(instrumentUserTable, SWT.NONE);
                    item.setData(map);
                    item.setText(0, ++i +"");
                    item.setText(1, (String) map.get("USER_NAME"));
                    item.setText(2, ((BigDecimal) map.get("COUNT")).toString());
                }
            }
        } catch (Exception e) {
            logger.error("查询用户拥有的合约数量失败",e);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
    }
    
    /**
     * 刷新用户平仓规则信息
     */
    public void refreshRiskUserTable(){
        riskUserTable.removeAll();
        try {
            List<Map<String,Object>> list = this.userInfoMapper.selectUserRiskCount();
            if(list != null && list.size() >0){
                int i = 0;
                for (Map<String,Object> map : list) {
                    TableItem item= new TableItem(riskUserTable, SWT.NONE);
                    item.setData(map);
                    item.setText(0, ++i +"");
                    item.setText(1, (String) map.get("USER_NAME"));
                    item.setText(2, ((BigDecimal) map.get("COUNT")).toString());
                }
            }
        } catch (Exception e) {
            logger.error("查询用户拥有的风控规则失败",e);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
        
    }
    
    /**
     * 加载风控组规则
     */
    public void refreshGroupRuleTable(){
        //清空组规则列表
        riskGroupRuleTable.removeAll();
        
        try {
            GroupRule groupRule = this.groupRuleService.getGroupRule();
            if(groupRule == null){
                return;
            }
            
            GroupInfo group = this.groupInfoService.getGroupInfoById(groupRule.getGroupId());
            
            TableItem item = new TableItem(riskGroupRuleTable, SWT.NULL);
            item.setData(groupRule);
            item.setText(0, "1");
            //用户组
            item.setText(1, group.getGroupName()==null?"":group.getGroupName());
            /*//强平比例
            item.setText(1, groupRule.getForceCloseRate()==null?"":groupRule.getForceCloseRate().toString());
            //开仓时间
            item.setText(2, groupRule.getOpenTime()==null?"":groupRule.getOpenTime());*/
            //强平时间
            item.setText(2, groupRule.getCloseTime()==null?"":groupRule.getCloseTime());
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    /**
     * 查询风控平仓规则
     */
    public void refreshRiskClose(String userName){
        
        //UserInfo userInfo = (UserInfo) treeItem.getData();
        
      //查询该用户所有平仓规则
        List<CloseRule> closeRules = null;
        try {
            //closeRules = closeRuleService.getCloseRuleByUserNo(userInfo.getUserNo());
            closeRules = closeRuleService.getCloseRuleByUserName(userName);
        } catch (FutureException e) {
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText("警告");
            box.open();
        }
        riskInstrumentTable.removeAll();
        if(closeRules == null || closeRules.size() <1){
            return;
        }
        int i = 0;
        for(CloseRule closeRule :closeRules){
            TableItem item= new TableItem(riskInstrumentTable, SWT.NONE);
            item.setData(closeRule);
            item.setText(0, ++i+"");
            item.setText(1, closeRule.getUserName());
            item.setText(2, closeRule.getContractNo());//合约
            item.setText(3, StringUtils.isEmpty(closeRule.getTickSize())?"":closeRule.getTickSize().toString());//最小跳动单位
            item.setText(4, StringUtils.isEmpty(closeRule.getHop())?"":closeRule.getHop().toString());//跳数
            item.setText(5, StringUtils.isEmpty(closeRule.getForceCloseRate())?"":closeRule.getForceCloseRate().toString());
        }
    }
    
    
    //风控组规则表格
    public void createUserRuleTable(Composite parent){
        
        riskGroupRuleTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        riskGroupRuleTable.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseDown(MouseEvent e) {
                
                Menu menu = new Menu(riskGroupRuleTable);
                
                TableItem selected = riskGroupRuleTable.getItem(new Point(e.x, e.y));
                
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
                    
                    riskGroupRuleTable.setMenu(menu);
                    
                    MenuItem item = new MenuItem(menu, SWT.None);
                    item.setText("添加组规则");
                    item.addSelectionListener(new GroupRuleAddSelection());
                }
            }
            //双击数据更新
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                
                TableItem selected = riskGroupRuleTable.getItem(new Point(e.x, e.y));
                if(selected == null){
                    return;
                }
                
                GroupInfo groupInfo = null;
                /*if(tree.getItemCount()>0){
                    groupInfo = (GroupInfo) tree.getTopItem().getData();
                }*/
                GroupRule groupRule = (GroupRule) selected.getData();
                GroupRuleEditDialog editDialog = new GroupRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo, MainForm.this, selected);
                editDialog.open();
            }
        });
        
        riskGroupRuleTable.setHeaderVisible(true);
        riskGroupRuleTable.setLinesVisible(true);
        
        TableColumn tableColumn = new TableColumn(riskGroupRuleTable, SWT.NONE);
        tableColumn.setText("序号");
        tableColumn.setWidth(100);
        
        TableColumn tableColumn0 = new TableColumn(riskGroupRuleTable, SWT.NONE);
        tableColumn0.setText("用户组");
        tableColumn0.setWidth(100);
        
        /*tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(userRuleTable, SWT.NONE).setText("交易账号");*/
          
        /*tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(riskGroupRuleTable, SWT.NONE).setText("强平比例");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(riskGroupRuleTable, SWT.NONE).setText("开仓时间");*/
        
        TableColumn tableColumn1 = new TableColumn(riskGroupRuleTable, SWT.NONE);
        tableColumn1.setText("平仓时间");
        tableColumn1.setWidth(100);
        
        refreshGroupRuleTable();
    }
    
    private void createButtonsViewer(Composite parent)
	 {
    	tabItem_4 = new TabItem(tabFolder, SWT.NONE);
        tabItem_4.setText("日初设置");
        buttonComposite = new Button(parent, SWT.BORDER | SWT.FULL_SELECTION);
        buttonComposite.setText("XXXXXXXXXXXX");
	 }
    
    public void createMainAccount(Composite parent){
        
        tabItem_4 = new TabItem(tabFolder, SWT.NONE);
        tabItem_4.setText("账号设置");

        mainAccountTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        mainAccountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                
                //选中的数据项
                TableItem selected = mainAccountTable.getItem(new Point(e.x, e.y));
                
                //创建菜单
                Menu menu = new  Menu(mainAccountTable);
                mainAccountTable.setMenu(menu);
                
                if(selected != null && e.button == 3){//鼠标右键数据项
                    logger.debug("鼠标右键数据项："+selected.getText());
                    
                    MenuItem removeItem = new MenuItem(menu, SWT.NONE);
                    removeItem.setText("删除");
                    removeItem.addSelectionListener(new MainAccountRemoveSelection(selected));
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("添加主账号");
                    addItem.addSelectionListener(new MainAccountAddSelection());
                }else if (selected == null && e.button == 3) {//鼠标右键空白

                    logger.debug("鼠标右键规则表空白区域");
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("添加主账号");
                    addItem.addSelectionListener(new MainAccountAddSelection());
                    
                }
            }
            
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                //鼠标双击事件
                TableItem item = mainAccountTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && item != null){
                    MainAccount mainAccount = (MainAccount) item.getData();
                    AccountEditDialog editDialog = new AccountEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, mainAccount, MainForm.this);
                    editDialog.open();
                }
            }
        });
        tabItem_4.setControl(mainAccountTable);
        mainAccountTable.setHeaderVisible(true);
        mainAccountTable.setLinesVisible(true);
        
        TableColumn tableColumn = new TableColumn(mainAccountTable, SWT.NONE);
        tableColumn.setWidth(100);
        tableColumn.setText("序号");

        TableColumn tableColumn_7 = new TableColumn(mainAccountTable, SWT.NONE);
        tableColumn_7.setWidth(100);
        tableColumn_7.setText("期货公司代码");

        TableColumn tableColumn_8 = new TableColumn(mainAccountTable, SWT.NONE);
        tableColumn_8.setWidth(100);
        tableColumn_8.setText("投资者代码");

        TableColumn tableColumn_9 = new TableColumn(mainAccountTable, SWT.NONE);
        tableColumn_9.setWidth(100);
        tableColumn_9.setText("密码");

        TableColumn tableColumn_10 = new TableColumn(mainAccountTable, SWT.NONE);
        tableColumn_10.setWidth(100);
        tableColumn_10.setText("账户类型");
        
        //加载主账户信息
        refreshMainAccount();
    }
    
    public void createSubaccountTable(Composite parent){
        subaccountTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        //tabItem.setControl(table);
        subaccountTable.setHeaderVisible(true);
        subaccountTable.setLinesVisible(true);
        
        TableColumn col0 = new TableColumn(subaccountTable, SWT.NONE);
        col0.setWidth(100);
        col0.setText("序号");
        
        TableColumn col = new TableColumn(subaccountTable, SWT.NONE);
        col.setWidth(100);
        col.setText("用户名");
        
        TableColumn col1 = new TableColumn(subaccountTable, SWT.NONE);
        col1.setWidth(100);
        col1.setText("密码");
        
        TableColumn col2 = new TableColumn(subaccountTable, SWT.NONE);
        col2.setWidth(100);
        col2.setText("初始资金");
        
        TableColumn col3 = new TableColumn(subaccountTable, SWT.NONE);
        col3.setWidth(100);
        col3.setText("调入金额");
        
        TableColumn forceCloseCol = new TableColumn(subaccountTable, SWT.NONE);
        forceCloseCol.setWidth(100);
        forceCloseCol.setText("强平比例");
        
        TableColumn forceLimitCol = new TableColumn(subaccountTable, SWT.NONE);
        forceLimitCol.setWidth(100);
        forceLimitCol.setText("强平金额");

        TableColumn tblclmnNewColumn = new TableColumn(subaccountTable, SWT.NONE);
        tblclmnNewColumn.setWidth(100);
        tblclmnNewColumn.setText("动态权益");

        TableColumn tblclmnNewColumn_1 = new TableColumn(subaccountTable, SWT.NONE);
        tblclmnNewColumn_1.setWidth(100);
        tblclmnNewColumn_1.setText("可用资金");

        TableColumn tblclmnNewColumn_2 = new TableColumn(subaccountTable, SWT.NONE);
        tblclmnNewColumn_2.setWidth(100);
        tblclmnNewColumn_2.setText("持仓盈亏");

        TableColumn tblclmnNewColumn_3 = new TableColumn(subaccountTable, SWT.NONE);
        tblclmnNewColumn_3.setWidth(100);
        tblclmnNewColumn_3.setText("平仓盈亏");
        
        TableColumn tblclmnNewColumn_4 = new TableColumn(subaccountTable, SWT.NONE);
        tblclmnNewColumn_4.setWidth(100);
        tblclmnNewColumn_4.setText("占用保证金");
        
        subaccountTable.addMouseListener(new MouseAdapter() {
            
          //单击数据项事件
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = subaccountTable.getItem(new Point(e.x, e.y));
                
                //创建菜单
                Menu menu = new  Menu(subaccountTable);
                subaccountTable.setMenu(menu);
                
                
                if(selected != null && e.button == 3){//鼠标右键数据项
                    logger.debug("鼠标右键数据项："+selected.getText());
                    
                    MenuItem editGroupMenuItem = new MenuItem(menu, SWT.NONE);
                    editGroupMenuItem.setText("修改子账户");
                    editGroupMenuItem.addSelectionListener(new SubAccountEditSelection(selected));
                    
                    MenuItem removeUserMenuItem = new MenuItem(menu, SWT.NONE);
                    removeUserMenuItem.setText("删除子账户");
                    removeUserMenuItem.addSelectionListener(new SubAccountRemoveSelection(selected));
                }else if (selected == null && e.button == 3) {//鼠标右键空白

                    logger.debug("鼠标右键规则表空白区域");
                    
                    
                }
            }
            
           
        });
    }
    
    
    //合约属性表格
    public void createRuleTable(Composite parent){
        instrumentTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        instrumentTable.addMouseListener(new MouseAdapter() {
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = instrumentTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    TradeRuleEditDialog dialog = new TradeRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, selected ,MainForm.this ,null);
                    dialog.open();
                }
            }
            
            //单击数据项事件
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = instrumentTable.getItem(new Point(e.x, e.y));
                
                //创建菜单
                Menu menu = new  Menu(instrumentTable);
                instrumentTable.setMenu(menu);
                
                
                if(selected != null && e.button == 3){//鼠标右键数据项
                    logger.debug("鼠标右键数据项："+selected.getText());
                    
                    MenuItem removeItem = new MenuItem(menu, SWT.NONE);
                    removeItem.setText("删除");
                    removeItem.addSelectionListener(new UserContractRemoveSelection(selected));
                    
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
        
        instrumentTable.setHeaderVisible(true);
        instrumentTable.setLinesVisible(true);
        
        TableColumn tableColumn111 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn111.setText("序号");
        tableColumn111.setWidth(100);
        
        TableColumn tableColumn = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn.setText("用户名");
        tableColumn.setWidth(100);
        
        TableColumn tableColumn0 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn0.setText("合约");
        tableColumn0.setWidth(100);
        
        /*TableColumn tableColumn1 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn1.setText("撤单数");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn2.setText("委托数");
        tableColumn2.setWidth(100);
        
        TableColumn tableColumn3 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn3.setText("开仓数");
        tableColumn3.setWidth(100);*/
        
        TableColumn tableColumn4 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn4.setText("开仓手续费值");
        tableColumn4.setWidth(100);
        
        TableColumn tableColumn5 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn5.setText("开仓手续费%");
        tableColumn5.setWidth(100);
        
        TableColumn tableColumn6 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn6.setText("平今手续费值");
        tableColumn6.setWidth(100);
        
        TableColumn tableColumn7 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn7.setText("平今手续费%");
        tableColumn7.setWidth(100);
        
        TableColumn tableColumn8 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn8.setText("保证金比例");
        tableColumn8.setWidth(100);
        
        TableColumn tableColumn9 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn9.setText("合约单位");
        tableColumn9.setWidth(100);
        
        TableColumn tableColumn10 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn10.setText("最小跳动单位");
        tableColumn10.setWidth(100);
        
    }
    
    /**
     * 创建合约交易规则表
     * @param parent
     */
    public void createContractTradeRuleTable(Composite parent){
        
        tradeRuleTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        tradeRuleTable.addMouseListener(new MouseAdapter() {
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = tradeRuleTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    ContractTradeRuleEditDialog dialog = new ContractTradeRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL ,MainForm.this ,selected);
                    dialog.open();
                }
            }
            
            //单击数据项事件
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = tradeRuleTable.getItem(new Point(e.x, e.y));
                
                //创建菜单
                Menu menu = new  Menu(tradeRuleTable);
                tradeRuleTable.setMenu(menu);
                
                
                if(selected != null && e.button == 3){//鼠标右键数据项
                    logger.debug("鼠标右键数据项："+selected.getText());
                    
                    MenuItem removeItem = new MenuItem(menu, SWT.NONE);
                    removeItem.setText("删除");
                    removeItem.addSelectionListener(new TradeRuleRemoveSelection(selected));
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("添加合约交易规则");
                    addItem.addSelectionListener(new TradeRuleAddSelection());
                }else if (selected == null && e.button == 3) {//鼠标右键空白

                    logger.debug("鼠标右键规则表空白区域");
                    
                    MenuItem addItem = new MenuItem(menu, SWT.NONE);
                    addItem.setText("添加合约交易规则");
                    addItem.addSelectionListener(new TradeRuleAddSelection());
                    
                }
            }
        });
        
        tradeRuleTable.setHeaderVisible(true);
        tradeRuleTable.setLinesVisible(true);
        
        TableColumn tableColumn = new TableColumn(tradeRuleTable, SWT.NONE);
        tableColumn.setText("序号");
        tableColumn.setWidth(100);
        
        TableColumn tableColumn0 = new TableColumn(tradeRuleTable, SWT.NONE);
        tableColumn0.setText("合约");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(tradeRuleTable, SWT.NONE);
        tableColumn1.setText("撤单数");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(tradeRuleTable, SWT.NONE);
        tableColumn2.setText("委托数");
        tableColumn2.setWidth(100);
        
        TableColumn tableColumn3 = new TableColumn(tradeRuleTable, SWT.NONE);
        tableColumn3.setText("开仓数");
        tableColumn3.setWidth(100);
        
        refreshContractTradeRule();
    }
    
    /**
     * 创建所有合约表
     * @param parent
     */
    public void createAllUserInstrumentsTable(Composite parent){
        allUserInstrumentTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        
        allUserInstrumentTable.setHeaderVisible(true);
        allUserInstrumentTable.setLinesVisible(true);
        
        TableColumn tableColumn00 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn00.setText("序号");
        tableColumn00.setWidth(100);
        
        TableColumn tableColumn = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn.setText("用户名");
        tableColumn.setWidth(100);
        
        TableColumn tableColumn0 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn0.setText("合约");
        tableColumn0.setWidth(100);
        
        /*TableColumn tableColumn1 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn1.setText("撤单数");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn2.setText("委托数");
        tableColumn2.setWidth(100);
        
        TableColumn tableColumn3 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn3.setText("开仓数");
        tableColumn3.setWidth(100);*/
        
        TableColumn tableColumn4 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn4.setText("开仓手续费值");
        tableColumn4.setWidth(100);
        
        TableColumn tableColumn5 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn5.setText("开仓手续费%");
        tableColumn5.setWidth(100);
        
        TableColumn tableColumn6 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn6.setText("平今手续费值");
        tableColumn6.setWidth(100);
        
        TableColumn tableColumn7 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn7.setText("平今手续费%");
        tableColumn7.setWidth(100);
        
        TableColumn tableColumn8 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn8.setText("保证金比例");
        tableColumn8.setWidth(100);
        
        TableColumn tableColumn9 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn9.setText("合约单位");
        tableColumn9.setWidth(100);
        
        TableColumn tableColumn10 = new TableColumn(allUserInstrumentTable, SWT.NONE);
        tableColumn10.setText("最小跳动单位");
        tableColumn10.setWidth(100);
        
    }
    
    /**
     * 创建用户拥有的合约数量
     * @param parent
     */
    public void createInstrumentUserTable(Composite parent){
        
        instrumentUserTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        
        instrumentUserTable.addMouseListener(new MouseAdapter() {
        
            
          //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = instrumentUserTable.getItem(new Point(e.x, e.y));
                
                
                
                if(selected != null ){
                    if(tabItem2 == null || tabItem2.isDisposed()){
                        
                        tabItem2 = new TabItem(tabFolder, SWT.NONE);
                        tabItem2.setText("用户合约信息");
                    }
                    
                    //用户所拥有的合约信息
                    tabItem2.setControl(instrumentTable);
                    
                    Map<String, Object> map = (Map<String, Object>) selected.getData();
                    refreshUserContract((String) map.get("USER_NO"));
                    tabFolder.setSelection(tabItem2);
                }
                
            }

        });
        
        instrumentUserTable.setHeaderVisible(true);
        instrumentUserTable.setLinesVisible(true);
        
        TableColumn tableColumn = new TableColumn(instrumentUserTable, SWT.NONE);
        tableColumn.setText("序号");
        tableColumn.setWidth(100);
        
        TableColumn tableColumn0 = new TableColumn(instrumentUserTable, SWT.NONE);
        tableColumn0.setText("用户名");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(instrumentUserTable, SWT.NONE);
        tableColumn1.setText("合约数");
        tableColumn1.setWidth(100);
    }
    
    /**
     * 创建所有风控表格
     * @param parent
     */
    public void createAllRiskTable(Composite parent){
        
        allRiskTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        
        allRiskTable.setHeaderVisible(true);
        allRiskTable.setLinesVisible(true);
        
        allRiskTable.addMouseListener(new MouseAdapter() {
            
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = allRiskTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    CloseRuleEditDialog dialog = new CloseRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this, selected);
                    dialog.open();
                }
            }
        });
        
        TableColumn tableColumn = new TableColumn(allRiskTable, SWT.NONE);
        tableColumn.setText("序号");
        tableColumn.setWidth(100);
        
        TableColumn tableColumn00 = new TableColumn(allRiskTable, SWT.NONE);
        tableColumn00.setText("用户名");
        tableColumn00.setWidth(100);
        
        TableColumn tableColumn0 = new TableColumn(allRiskTable, SWT.NONE);
        tableColumn0.setText("合约");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(allRiskTable, SWT.NONE);
        tableColumn1.setText("最小变动单位");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(allRiskTable, SWT.NONE);
        tableColumn2.setText("跳数");
        tableColumn2.setWidth(100);
        
    }
    
    //创建风控用户表格
    public void createRiskUserTable(Composite parent){
        
        riskUserTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        
        riskUserTable.setHeaderVisible(true);
        riskUserTable.setLinesVisible(true);
        
        TableColumn tableColumn0 = new TableColumn(riskUserTable, SWT.NONE);
        tableColumn0.setText("序号");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(riskUserTable, SWT.NONE);
        tableColumn1.setText("用户名");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(riskUserTable, SWT.NONE);
        tableColumn2.setText("风控规则数量");
        tableColumn2.setWidth(100);
        
        
        riskUserTable.addMouseListener(new MouseAdapter() {
            
            
            //双击数据项事件
              @Override
              public void mouseDoubleClick(MouseEvent e) {
                  TableItem selected = riskUserTable.getItem(new Point(e.x, e.y));
                  
                  
                  
                  if(selected != null ){
                      if(riskTabItem == null || riskTabItem.isDisposed()){
                          
                          riskTabItem = new TabItem(tabFolder, SWT.NONE);
                          riskTabItem.setText("用户风控信息");
                      }
                      
                      //用户拥有的合约信息
                      riskTabItem.setControl(riskInstrumentTable);
                      //123
                      Map<String, Object> map = (Map<String, Object>) selected.getData();
                      refreshRiskClose((String) map.get("USER_NAME"));
                      tabFolder.setSelection(riskTabItem);
                  }
                  
              }

          });
    }
        
    
    //添加风控平仓规则表格
    public void createCloseRuleTable(Composite parent){
        
        riskInstrumentTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        //鼠标单击事件
        riskInstrumentTable.addMouseListener(new MouseAdapter() {
            
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = riskInstrumentTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    CloseRuleEditDialog dialog = new CloseRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this, selected);
                    dialog.open();
                }
            }
            
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = riskInstrumentTable.getItem(new Point(e.x, e.y));
                //创建菜单
                Menu menu = new  Menu(riskInstrumentTable);
                riskInstrumentTable.setMenu(menu);
                
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
        
        riskInstrumentTable.setHeaderVisible(true);
        riskInstrumentTable.setLinesVisible(true);
        
        TableColumn tableColumn0 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn0.setText("序号");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn1.setText("用户名");
        tableColumn1.setWidth(100);
        
        TableColumn tableColumn2 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn2.setText("合约");
        tableColumn2.setWidth(100);
        
        TableColumn tableColumn3 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn3.setText("最小变动单位");
        tableColumn3.setWidth(100);
          
        TableColumn tableColumn4 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn4.setText("跳数");
        tableColumn4.setWidth(100);
        
        
        
        /*tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(riskInstrumentTable, SWT.NONE).setText("强平比例");*/
        //refreshRiskClose();
    }
    
    /**
     * 删除合约
     * @author caojia
     *
     */
    public class UserContractRemoveSelection extends SelectionAdapter {
        
        private TableItem item;
        
        public UserContractRemoveSelection(TableItem item) {
            this.item = item;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            UserContractTradeRule rule = (UserContractTradeRule) item.getData();
            logger.debug("删除ID为："+rule.getTradeRuleId()+"的交易规则,合约编号："+rule.getContractNo());
            try {
                tradeRuleService.removeUserTradeRule(rule.getTradeRuleId(),rule.getId());
                //删除合约对应的平仓规则
                String userNo = rule.getUserNo();
                //closeRuleService.removeCloseRuleByContractNo(rule.getContractNo());
                closeRuleService.removeCloseRuleByContractNoAndUserNo(rule.getContractNo(), userNo);
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功");
                box.setText("提示");
                box.open();
                refreshUserContract(rule.getUserNo());
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("错误");
                box.open();
            } 
        }
    }
    
    /**
     * 删除交易规则信息
     * @author caojia
     *
     */
    public class TradeRuleRemoveSelection extends SelectionAdapter {
        
        private TableItem item;
        
        public TradeRuleRemoveSelection(TableItem item) {
            this.item = item;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            TradeRule rule = (TradeRule) item.getData();
            logger.debug("删除ID为："+rule.getId()+"的交易规则,合约编号："+rule.getContractId());
            
            try {
                tradeRuleService.removeTradeRule(rule.getId());
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功");
                box.setText("提示");
                box.open();
                refreshContractTradeRule();
                
            } catch (Exception e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("错误");
                box.open();
            } 
        }
    }
    
    /**
     * 添加合约事件
     * @author caojia
     *
     */
    public class RuleAddSelection extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(instrumentUserTree.getItemCount()<1){
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组！");
                box.setText("警告");
                box.open();
                return ;
            }
            TreeItem item = instrumentUserTree.getSelection().length <1 ? instrumentUserTree.getTopItem() : instrumentUserTree.getSelection()[0];
            
            
            //RuleAddDialog ruleAddDialog = new RuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this ,item);
            RuleAddDialog ruleAddDialog = new RuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this ,instrumentUserTable.getSelection()[0]);
            ruleAddDialog.open();
        }
    }
    
    
    public class TradeRuleAddSelection extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(instrumentUserTree.getItemCount()<1){
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组！");
                box.setText("警告");
                box.open();
                return ;
            }
            TreeItem item = instrumentUserTree.getSelection().length <1 ? instrumentUserTree.getTopItem() : instrumentUserTree.getSelection()[0];
            ContractTradeRuleAddDialog dialog = new ContractTradeRuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this);
            dialog.open();
        }
    }
    
    
    /**
     * 添加风控平仓规则
     * @author caojia
     *
     */
    public class CloseRuleAddSelection extends SelectionAdapter{
        @Override
        public void widgetSelected(SelectionEvent e) {
            
            if(riskUserTree.getItemCount()<1){
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组！");
                box.setText(CommonConstant.MESSAGE_BOX_WARN);
                box.open();
                return ;
            }
            CloseRuleAddDialog closeRuleAddDialog = new CloseRuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this,riskUserTable.getSelection()[0]);
            closeRuleAddDialog.open();
        }
    }
    
    /**
     * 添加风控组规则
     * @author caojia
     */
    public class GroupRuleAddSelection extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if(userTree.getItemCount()<1){
                logger.warn("请先添加用户组");
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组");
                box.setText("警告");
                box.open();
                return;
            }
            
            //MainAccount mainAccount = (MainAccount) mainAccountTable.getItem(0).getData();
            
            GroupInfo groupInfo = (GroupInfo) userTree.getTopItem().getData();
            
            GroupRuleAddDialog groupEditDialog = new GroupRuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo,MainForm.this);
            groupEditDialog.open();
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
                //TODO
                //refreshTradeRule(tree.getSelection()==null?null:tree.getSelection()[0]);
                //refreshRiskClose(riskUserTree.getSelection()[0]);
                refreshRiskClose(closeRule.getUserName());
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                box.open();
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
            
            AccountAddDialog accountAddDialog = new AccountAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this);
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
            GroupAddDialog groupAddDialog = new GroupAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, mainAccount,MainForm.this);
            groupAddDialog.open();
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
            SubAccountAddDialog subAccountAddDialog = new SubAccountAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo, MainForm.this);
            subAccountAddDialog.open();
        }
    }
    
    /**
     * 修改子账户事件
     * @author caojia
     */
    public class SubAccountEditSelection extends SelectionAdapter {
        
        private TableItem tableItem;
        
        public SubAccountEditSelection(TableItem tableItem) {
            this.tableItem = tableItem;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            UserInfo userInfo = (UserInfo) tableItem.getData();
            
            SubAccountEditDialog subAccountEditDialog = new SubAccountEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL,
                    userInfo, MainForm.this,tableItem.getText(4));
            subAccountEditDialog.open();
            
        }
        
    }
        
        
        
    
    /**
     * 删除子账户事件
     * @author caojia
     */
    public class SubAccountRemoveSelection extends SelectionAdapter {
        
        private TableItem tableItem;
        
        public SubAccountRemoveSelection(TableItem tableItem){
            this.tableItem = tableItem;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e){
            UserInfo userInfo = (UserInfo) tableItem.getData();
            try {
                MessageBox box1 = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK|SWT.CANCEL);
                box1.setMessage("删除子账户会将用户下所有合约规则删除，确定要删除子账户"+userInfo.getUserName()+"吗？");
                box1.setText("提示");
                if(box1.open() == SWT.CANCEL){
                    return;
                }
                
                userInfoService.deleteUser(userInfo);
                CapitalRateMapper capitalRateMapper = (CapitalRateMapper) SpringContextUtil.getBean("capitalRateMapper");
                capitalRateMapper.deleteByPrimaryKey(userInfo.getUserName());
                
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("删除成功！");
                box.setText("提示");
                box.open();
                //刷新用户组树
                refreshSubaccountByGroupId(userInfo.getGroupId());
                //refreshUserTree(instrumentUserTree);
                //refreshUserTree(riskUserTree);
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("警告");
                box.open();
            }
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
                refreshUserTree(userTree,"1");
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText("警告");
                box.open();
            }
        }
    }
}
