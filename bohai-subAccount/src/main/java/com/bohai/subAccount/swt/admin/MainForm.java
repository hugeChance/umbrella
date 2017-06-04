package com.bohai.subAccount.swt.admin;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
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
import org.eclipse.swt.widgets.Composite;
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

import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.dao.UseravailableindbMapper;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
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
import com.bohai.subAccount.swt.admin.AdminViewMain.CloseRuleAddSelection;
import com.bohai.subAccount.swt.admin.AdminViewMain.CloseRuleRemoveSelection;
import com.bohai.subAccount.swt.admin.AdminViewMain.GroupRuleAddSelection;
import com.bohai.subAccount.swt.admin.AdminViewMain.RuleAddSelection;
import com.bohai.subAccount.swt.admin.AdminViewMain.RuleRemoveSelection;
import com.bohai.subAccount.utils.DateFormatterUtil;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserContractTradeRule;

public class MainForm {
    
    static Logger logger = Logger.getLogger(MainForm.class);
	
	TabItem tabItem_4;

	private Table table;
	private Table instrumentTable;
	private Table riskGroupRuleTable;
	private Table riskInstrumentTable;
	private Table mainAccountTable;

	private TabFolder tabFolder;
	private ExpandBar expandBar;
	private ToolBar toolBar;
	
	private  Tree userTree;
	private  Tree instrumentUserTree;
	private  Tree riskUserTree;

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
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shell = new Shell();

		shell.setSize(800, 600);
		shell.setMinimumSize(800, 600);
		shell.setText("SWT Application");

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

		createSubMenu(userTree);

		Listener treeSelection = new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = userTree.getSelection();
				for (int i = 0; i < selection.length; i++) {
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
							}
							
							return;
						}
					}
//					tabFolder.setSelection(0);
					if (ctab != table) {
						tabItem_4.setText("子账号明细");
						tabItem_4.setControl(table);
					}

				}
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
		refreshUserTree(userTree);
		
		expdItem1.setHeight(150);

		ExpandItem expdItem2 = new ExpandItem(expandBar, SWT.NONE);
		expdItem2.setExpanded(true);
		expdItem2.setText("合约设置"); // 合约设置

		instrumentUserTree = new Tree(expandBar, SWT.NONE);
		expdItem2.setControl(instrumentUserTree);
		createSubMenu(instrumentUserTree);

		Listener tree1Selection = new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = instrumentUserTree.getSelection();
				Table ctab = (Table)tabItem_4.getControl();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					System.out.println("Selection={" + string + "}");
					if (ctab != instrumentTable) {
						tabItem_4.setText("合约明细");
						tabItem_4.setControl(instrumentTable);
						refreshTradeRule(instrumentUserTree.getSelection()[0]);
					}
					//tabFolder.setSelection(1); // 合约明细
				}
			}
		};

		instrumentUserTree.addListener(SWT.Selection, tree1Selection);
		//合约用户树
		refreshUserTree(instrumentUserTree);
		
		expdItem2.setHeight(150);
		

		ExpandItem expdItem3 = new ExpandItem(expandBar, SWT.NONE);
		expdItem3.setHeight(150);
		expdItem3.setText("风控设置"); // 风控设置

		riskUserTree = new Tree(expandBar, SWT.NONE);
		expdItem3.setControl(riskUserTree);

		createSubMenu(riskUserTree);

		Listener tree2Selection = new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = riskUserTree.getSelection();
				Table ctab = (Table)tabItem_4.getControl();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					System.out.println("Selection={" + string + "}");
					for (TreeItem item : riskUserTree.getItems()) {
						if (item.getText().equals(selection[i].getText())) {
							if (ctab != riskGroupRuleTable) {
								tabItem_4.setText("风控组规则明细");
								tabItem_4.setControl(riskGroupRuleTable);
								refreshGroupRuleTable();
							}
							//tabFolder.setSelection(2); // 风控组
							return;
						}
					}
					
					if (ctab != riskInstrumentTable) {
						tabItem_4.setText("风控合约规则明细");
						tabItem_4.setControl(riskInstrumentTable);
						refreshRiskClose(selection[0]);
					}
					//tabFolder.setSelection(3); // 风控明细
				}
			}
		};

		riskUserTree.addListener(SWT.Selection, tree2Selection);
		refreshUserTree(riskUserTree);
		

		/*TreeItem treeItem4 = new TreeItem(tree_2, SWT.NONE);
		treeItem4.setText("风控组");

		TreeItem treeSubItem4_1 = new TreeItem(treeItem4, SWT.NONE);
		treeSubItem4_1.setText("风控明细");
		treeItem4.setExpanded(true);*/
		

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
		//TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		//tabItem.setText("\u5B50\u8D26\u53F7\u660E\u7EC6");

		table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		//tabItem.setControl(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("\u52A8\u6001\u6743\u76CA");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("\u53EF\u7528\u8D44\u91D1");

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("\u6301\u4ED3\u76C8\u4E8F");

		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("\u5E73\u4ED3\u76C8\u4E8F");

		//TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		//tabItem_1.setText("\u5408\u7EA6\u660E\u7EC6");


		//创建合约属性表
		createRuleTable(tabFolder);
		
		//创建风控组规则表格
		createUserRuleTable(tabFolder);
		
		//创建风控规则表格
		createCloseRuleTable(tabFolder);
		

		tabItem_4 = new TabItem(tabFolder, SWT.NONE);
		tabItem_4.setText("\u4E3B\u8D26\u53F7\u660E\u7EC6");

		mainAccountTable = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabItem_4.setControl(mainAccountTable);
		mainAccountTable.setHeaderVisible(true);
		mainAccountTable.setLinesVisible(true);

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
		
		TableItem item = null;

		
		for (int row = 0; row < 2; row++) {
			item = new TableItem(table, SWT.NONE);
			for (int col = 0; col < table.getColumnCount(); col++) {
				item.setText(col, "item" + (row + 1) + "-" + (col + 1));
			}
		}
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
				box.setMessage("版本号版本号版本号版本号");
				box.open();
			}
		});

		MenuItem menuItem4_2 = new MenuItem(menu_4, SWT.NONE);
		menuItem4_2.setText("联系电话");
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
    public void refreshUserTree(Tree tree){
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
    
    /**
     * 刷新交易组规则
     * @param treeItem
     */
    public void refreshTradeRule(TreeItem treeItem){
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
            //用户组
            item.setText(0, group.getGroupName()==null?"":group.getGroupName());
            /*//强平比例
            item.setText(1, groupRule.getForceCloseRate()==null?"":groupRule.getForceCloseRate().toString());
            //开仓时间
            item.setText(2, groupRule.getOpenTime()==null?"":groupRule.getOpenTime());*/
            //强平时间
            item.setText(1, groupRule.getCloseTime()==null?"":groupRule.getCloseTime());
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    /**
     * 查询风控平仓规则
     */
    public void refreshRiskClose(TreeItem treeItem){
        
        UserInfo userInfo = (UserInfo) treeItem.getData();
        
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
        riskInstrumentTable.removeAll();
        if(closeRules == null || closeRules.size() <1){
            return;
        }
        for(CloseRule closeRule :closeRules){
            TableItem item= new TableItem(riskInstrumentTable, SWT.NONE);
            item.setData(closeRule);
            item.setText(0, closeRule.getContractNo());//合约
            item.setText(1, StringUtils.isEmpty(closeRule.getTickSize())?"":closeRule.getTickSize().toString());//最小跳动单位
            item.setText(2, StringUtils.isEmpty(closeRule.getHop())?"":closeRule.getHop().toString());//跳数
            item.setText(3, StringUtils.isEmpty(closeRule.getForceCloseRate())?"":closeRule.getForceCloseRate().toString());
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
                GroupRuleEditDialog editDialog = new GroupRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, groupInfo, MainForm.this, groupRule);
                editDialog.open();
            }
        });
        
        riskGroupRuleTable.setHeaderVisible(true);
        riskGroupRuleTable.setLinesVisible(true);
        
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
    
    
    //合约属性表格
    public void createRuleTable(Composite parent){
        instrumentTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        instrumentTable.addMouseListener(new MouseAdapter() {
            //双击数据项事件
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem selected = instrumentTable.getItem(new Point(e.x, e.y));
                if(e.button ==1 && selected != null){//左键双击数据项
                    TradeRuleEditDialog dialog = new TradeRuleEditDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, selected ,MainForm.this ,instrumentUserTree.getSelection()[0]);
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
        
        instrumentTable.setHeaderVisible(true);
        instrumentTable.setLinesVisible(true);
        
        TableColumn tableColumn0 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn0.setText("合约");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn1.setText("撤单数");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn2.setText("委托数");
        tableColumn2.setWidth(100);
        
        TableColumn tableColumn3 = new TableColumn(instrumentTable, SWT.NONE);
        tableColumn3.setText("开仓数");
        tableColumn3.setWidth(100);
        
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
        tableColumn0.setText("合约");
        tableColumn0.setWidth(100);
        
        TableColumn tableColumn1 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn1.setText("最小变动单位");
        tableColumn1.setWidth(100);
          
        TableColumn tableColumn2 = new TableColumn(riskInstrumentTable, SWT.NONE);
        tableColumn2.setText("跳数");
        tableColumn2.setWidth(100);
        
        
        
        /*tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(riskInstrumentTable, SWT.NONE).setText("强平比例");*/
        //refreshRiskClose();
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
                refreshTradeRule(instrumentUserTree.getSelection()[0]);
            } catch (FutureException e1) {
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
            RuleAddDialog ruleAddDialog = new RuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this ,item);
            ruleAddDialog.open();
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
            CloseRuleAddDialog closeRuleAddDialog = new CloseRuleAddDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, MainForm.this,riskUserTree.getSelection()[0]);
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
                refreshTradeRule(riskUserTree.getSelection()[0]);
            } catch (FutureException e1) {
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage(e1.getMessage());
                box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                box.open();
            }
            
        }
    }
    

}
