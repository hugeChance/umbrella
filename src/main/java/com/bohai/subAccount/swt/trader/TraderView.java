package com.bohai.subAccount.swt.trader;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_AF_Delete;
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
import java.math.BigDecimal;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.swt.trader.helper.MarketReceiveThread;
import com.bohai.subAccount.swt.trader.helper.TradeReceiveThread;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.vo.UserAvailableMemorySave;

import swing2swt.layout.BorderLayout;

public class TraderView {

    static Logger logger = Logger.getLogger(TraderView.class);
    
    //static final String MARKET_IP = "10.0.0.204";
    static final String MARKET_IP = ApplicationConfig.getProperty("marketAddr");
    static final int MARKET_PORT = 3393;
    
    //static final String TRADE_IP = "10.0.0.202";
    static final String TRADE_IP = ApplicationConfig.getProperty("tradeAddr");
    static final int TRADE_PORT = 3394;
    
    private String userName;
    private String passwd;
    
    public Shell shell;
    private Table marketTable; 
    private Table entrustTable;
    private Table dealTable;
    private Table positionTable;
    private Text volume;
    private Text priceText;
    
    private Combo combo;
    private Button buyButton;
    private Button sellButton;
    private Button openButton;
    private Button closeButton;
    private Button closeTodayButton;
    private Combo mode;
    //public Label availableLabel;
    private Label hand;
    public Integer handCount;
    private Label volumePermit;
    
    private Button killButton;
    
    private CTabFolder tabFolder;
	private Composite composite;
	private CTabFolder centerFolder;
	private CTabFolder southFolder;
    
    //成交提示声音选项
    public MenuItem dealMenuItem;
    
    //订阅合约
    private List<UserContract> contracts;
    
    //委托
    private List<Order> orders;
    
    //成交
    private List<Trade> trades;
    
    //持仓
    private List<InvestorPosition> positions;
    
    private String available;
    
    private Socket marketSocket;
    
    private Socket tradeSocket;
    private Table table;
    
    public Label sellLabel;
    
    public Label buyLabel;
    
    private UserAvailableMemorySave initAvailable;
    
    static List<String> SHINST ;
    
    static {
        SHINST = new ArrayList<String>();
        SHINST.add("cu");
        SHINST.add("al");
        SHINST.add("zn");
        SHINST.add("pb");
        SHINST.add("ru");
        SHINST.add("fu");
        SHINST.add("rb");
        SHINST.add("wr");
        SHINST.add("au");
        SHINST.add("ag");
        SHINST.add("bu");
        SHINST.add("hc");
        SHINST.add("ni");
        SHINST.add("sn");
    }
    
    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            TraderView window = new TraderView();
            //window.loadSpringContext();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public TraderView() {

    }
    
    /*public void loadSpringContext(){
        logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        System.out.println("===================加载成功 !==================");
    }*/

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        
        //初始化行情socket
        initMarketSocket();
        
        /*Thread heartBeatThread = new Thread(new HeartBeatThread(this));
        heartBeatThread.setDaemon(true);
        heartBeatThread.start();*/
        
        //行情接收线程
        Thread thread = new Thread(new MarketReceiveThread(this, marketTable));
        
        
        thread.setDaemon(true);
        thread.start();
        
        //初始化交易socket
        initTradeSocket();
        
        Thread tradeThread = new Thread(new TradeReceiveThread(this));
        tradeThread.setDaemon(true);
        tradeThread.start();
        
        SashForm sashForm = new SashForm(shell, SWT.NONE);
        sashForm.setLayoutData(BorderLayout.NORTH);
        
        table = new Table(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
        tblclmnNewColumn.setWidth(100);
        tblclmnNewColumn.setText("静态权益");
        
        TableColumn tableColumn_3 = new TableColumn(table, SWT.CENTER);
        tableColumn_3.setWidth(100);
        tableColumn_3.setText("平仓盈亏");
        
        TableColumn tableColumn_4 = new TableColumn(table, SWT.CENTER);
        tableColumn_4.setWidth(100);
        tableColumn_4.setText("持仓盈亏");
        
        TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
        tableColumn.setWidth(100);
        tableColumn.setText("可用资金");
        
        TableColumn tableColumn_5 = new TableColumn(table, SWT.CENTER);
        tableColumn_5.setWidth(100);
        tableColumn_5.setText("冻结资金");
        
        TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
        tableColumn_2.setWidth(100);
        tableColumn_2.setText("占用保证金");
        
        TableColumn tableColumn_1 = new TableColumn(table, SWT.CENTER);
        tableColumn_1.setWidth(100);
        tableColumn_1.setText("手续费");
        sashForm.setWeights(new int[] {1});
        
        if(initAvailable != null){
            TableItem newItem = new TableItem(table, SWT.NONE);
            newItem.setText(0, initAvailable.getAvailable());//静态资金
            newItem.setText(1, initAvailable.getCloseWin());//平仓盈亏
            newItem.setText(2, initAvailable.getPositionWin());//持仓盈亏
            newItem.setText(3, available);   //可用资金
            newItem.setText(4, initAvailable.getFrozenAvailable());//冻结资金
            newItem.setText(5, initAvailable.getMargin());//占用保证金
            newItem.setText(6, initAvailable.getCommission());//手续费
        }
        
        shell.layout();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
//        shell.setSize(800, 600);
//		shell.setMinimumSize(800, 600);
//		shell.setText("SWT Application");
//
//		Rectangle bounds = Display.getDefault().getPrimaryMonitor().getBounds();
//		Rectangle rect = shell.getBounds();
//		int x = bounds.x + (bounds.width - rect.width) / 2;
//		int y = bounds.y + (bounds.height - rect.height) / 2;
//		shell.setLocation(x, y);

//		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        shell.addDisposeListener(new DisposeListener() {
        	
            public void widgetDisposed(DisposeEvent e) {
            	
                	  logger.info("退出系统");
                      try {
                          PrintWriter out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
                          out.println("bye|"+userName+"|"+passwd);
                          out.flush();
                          out.close();
                          tradeSocket.close();
                      } catch (Exception e1) {
                          logger.error("获取交易输出流失败");
                      }
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
        
        shell.setSize(908, 693);
        shell.setText("交易员："+userName);
        shell.setLayout(new BorderLayout(0, 0));
        
        //创建菜单栏  begin
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        
        MenuItem explainMenuItem = new MenuItem(menu, SWT.CASCADE);
        explainMenuItem.setText("说明");
        
        Menu menu_1 = new Menu(explainMenuItem);
        explainMenuItem.setMenu(menu_1);
        
        MenuItem quickOrderMenuItem = new MenuItem(menu_1, SWT.CASCADE);
        quickOrderMenuItem.setText("快捷键下单");
        
        Menu menu_2 = new Menu(quickOrderMenuItem);
        quickOrderMenuItem.setMenu(menu_2);
        
        MenuItem modeOne = new MenuItem(menu_2, SWT.NONE);
        modeOne.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		QuickOrderModeDialog dialog = new QuickOrderModeDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, "模式一");
        		dialog.open();
        	}
        });
        modeOne.setText("模式一");
        
        MenuItem modeTwo = new MenuItem(menu_2, SWT.NONE);
        modeTwo.setText("模式二");
        modeTwo.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		QuickOrderModeDialog dialog = new QuickOrderModeDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, "模式二");
        		dialog.open();
        	}
        });
        
        MenuItem modeThree = new MenuItem(menu_2, SWT.NONE);
        modeThree.setText("模式三");
        modeThree.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		QuickOrderModeDialog dialog = new QuickOrderModeDialog(shell, SWT.CLOSE|SWT.APPLICATION_MODAL, "模式三");
        		dialog.open();
        	}
        });
        
        MenuItem soundMenuItem = new MenuItem(menu, SWT.CASCADE);
        soundMenuItem.setText("声音");
        
        Menu soundMenu = new Menu(soundMenuItem);
        soundMenuItem.setMenu(soundMenu);
        
        dealMenuItem = new MenuItem(soundMenu, SWT.CHECK);
        dealMenuItem.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		if(dealMenuItem.getSelection()){
        			ApplicationConfig.setProperty("media", "true");
        		}else {
        			ApplicationConfig.setProperty("media", "false");
				}
        	}
        });
        dealMenuItem.setText("成交提示");
        if(!StringUtils.isEmpty(ApplicationConfig.getProperty("media")) && ApplicationConfig.getProperty("media").equals("true")){
        	dealMenuItem.setSelection(true);
        }
        
        
      //创建菜单栏  end
        
        SashForm sashform = new SashForm(shell, SWT.HORIZONTAL);
        sashform.setLayout(new FillLayout());
        
        final SashForm headForm = new SashForm(shell, SWT.BORDER | SWT.VERTICAL);
        headForm.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        tabFolder = new CTabFolder(headForm, SWT.FLAT | SWT.RIGHT | SWT.SHADOW_OUT);
		createHeadForm();
		
		final SashForm centerForm = new SashForm(headForm, SWT.HORIZONTAL);
		centerForm.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		composite = new Composite(centerForm, SWT.NONE);
		createComposite();

		centerFolder = new CTabFolder(centerForm, SWT.NONE);
		centerForm.setWeights(new int[] {112, 773});
		createCenterFolder();
		
		final SashForm bottomForm = new SashForm(headForm, SWT.HORIZONTAL);
		bottomForm.setLayout(new FillLayout(SWT.HORIZONTAL));
        
		southFolder = new CTabFolder(bottomForm, SWT.NONE);
		headForm.setWeights(new int[] {143, 312, 117});
		createSouthFolder();
        
        
        
       
        
    }
    
    /**
     * 快捷下单注册快捷键
     */
    public void regeditQuickOrder(Listener listener){
        
        if(StringUtils.isEmpty(mode.getText())){
            return;
        }
        
        logger.debug("启动快捷键");
        shell.getDisplay().addFilter(SWT.KeyDown, listener);
    }
    
    
    
    
    public void createMarketTableViewer(Composite parent){
        
        marketTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
//        marketTable.addMouseListener(new MouseAdapter() {
//        	@Override
//        	public void mouseDown(MouseEvent e) {
//        		TableItem row = marketTable.getItem(new Point(e.x, e.y));
//        		if(row != null){
//                    //所在行
//                    combo.setText(row.getText());
//                    
//                }
//        	}
//        });
        
        TableCursor tableCursor = new TableCursor(marketTable, SWT.NONE);
        //选中事件
        tableCursor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem row = tableCursor.getRow();
                if(row != null){
                    //所在行
                    combo.setText(row.getText());
                    //所在列
                    int column = tableCursor.getColumn();
                    
                    if(column == 3){
                        sellButton.setSelection(true);
                        buyButton.setSelection(false);
                        priceText.setText(row.getText(4));
                    }else if(column == 4) {
                        buyButton.setSelection(true);
                        sellButton.setSelection(false);
                        priceText.setText(row.getText(3));
                    } else {
                    	priceText.setText(row.getText(1));
                    }
                    
                    
                    
                }
            }
        });
        
        
        marketTable.setHeaderVisible(true);//设置表头
        marketTable.setLinesVisible(true);//显示表格线
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        marketTable.setLayout(tLayout);


        /* 第三步:建立TableViewer中的列
           */
        tLayout.addColumnData(new ColumnWeightData(30));//这个是设置ID列的列宽为10像素
        new TableColumn(marketTable, SWT.NONE).setText("合约");
        
        tLayout.addColumnData(new ColumnWeightData(30));//这个是设置ID列的列宽为40像素
        new TableColumn(marketTable, SWT.NONE).setText("最新价");
          
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(marketTable, SWT.NONE).setText("买量");
        
        tLayout.addColumnData(new ColumnWeightData(35));//这个是设置ID列的列宽为10像素
        new TableColumn(marketTable, SWT.NONE).setText("买价");
        
        tLayout.addColumnData(new ColumnWeightData(35));//这个是设置ID列的列宽为70像素
        new TableColumn(marketTable, SWT.NONE).setText("卖价");

        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(marketTable, SWT.NONE).setText("卖量");
        
        tLayout.addColumnData(new ColumnWeightData(30));//这个是设置ID列的列宽为10像素
        new TableColumn(marketTable, SWT.NONE).setText("涨跌");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(marketTable, SWT.NONE).setText("涨跌幅");
        
        tLayout.addColumnData(new ColumnWeightData(35));
        new TableColumn(marketTable, SWT.NONE).setText("涨停价");
        
        tLayout.addColumnData(new ColumnWeightData(35));
        new TableColumn(marketTable, SWT.NONE).setText("跌停价");
        
        tLayout.addColumnData(new ColumnWeightData(35));
        new TableColumn(marketTable, SWT.NONE).setText("最高价");
        
        tLayout.addColumnData(new ColumnWeightData(35));
        new TableColumn(marketTable, SWT.NONE).setText("最低价");
        
        tLayout.addColumnData(new ColumnWeightData(40));
        new TableColumn(marketTable, SWT.NONE).setText("成交量");
        
        tLayout.addColumnData(new ColumnWeightData(40));
        new TableColumn(marketTable, SWT.NONE).setText("持仓量");
        
    }
    
    private void createHeadForm() {
//    	CTabFolder tabFolder = new CTabFolder(tabFolderN, SWT.NONE);
    	tabFolder.setLayoutData(BorderLayout.NORTH);
    	tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        
        CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
        tbtmNewItem.setFont(SWTResourceManager.getFont("微软雅黑", 7, SWT.NORMAL));
        tbtmNewItem.setText(" 自 选 ");
        
        createMarketTableViewer(tabFolder);
        tbtmNewItem.setControl(marketTable);
        //初始化用户订阅合约
        if(contracts !=null && contracts.size() >0){
            for (UserContract userContract : contracts) {
                TableItem item = new TableItem(marketTable, SWT.NONE);
                item.setData(userContract);
                item.setText(0, userContract.getContractNo());
                
                //涨停价红色
                item.setForeground(8, SWTResourceManager.getColor(242,14,14));
                //最高价红色
                item.setForeground(10, SWTResourceManager.getColor(242,14,14));
                //跌停价绿色
                item.setForeground(9, SWTResourceManager.getColor(78,178,88));
                //最低价绿色
                item.setForeground(11, SWTResourceManager.getColor(78,178,88));
            }
        }
    }
    
    private void createComposite(){
//    	Composite composite = new Composite(compositeN, SWT.NONE);
        composite.setLayoutData(BorderLayout.WEST);
        composite.setLayout(null);
        
        Label lblNewLabel = new Label(composite, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        lblNewLabel.setBounds(10, 17, 55, 22);
        lblNewLabel.setText("合约");
        
        combo = new Combo(composite, SWT.NONE);
        combo.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        combo.setEnabled(false);
        combo.setBounds(71, 14, 95, 26);
        
        Label label = new Label(composite, SWT.NONE);
        label.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        label.setBounds(10, 48, 55, 27);
        label.setText("买卖");
        
        Composite buySellComp = new Composite(composite, SWT.NONE);
        buySellComp.setBounds(71, 50, 167, 25);
        
        buyButton = new Button(buySellComp, SWT.RADIO);
        buyButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        buyButton.setLocation(0, 0);
        buyButton.setSize(53, 25);
        buyButton.setText("买入");
        
        sellButton = new Button(buySellComp, SWT.RADIO);
        sellButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        sellButton.setLocation(59, 0);
        sellButton.setSize(53, 25);
        sellButton.setText("卖出");
        
        Label label_1 = new Label(composite, SWT.NONE);
        label_1.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        label_1.setBounds(10, 81, 55, 27);
        label_1.setText("开平");
        
        Composite openCloseComp = new Composite(composite, SWT.NONE);
        openCloseComp.setBounds(71, 81, 190, 27);
        
        openButton = new Button(openCloseComp, SWT.RADIO);
        openButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        openButton.setBounds(0, 0, 53, 27);
        openButton.setText("开仓");
        
        closeButton = new Button(openCloseComp, SWT.RADIO);
        closeButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        closeButton.setBounds(59, 0, 53, 26);
        closeButton.setText("平仓");
        
        closeTodayButton = new Button(openCloseComp, SWT.RADIO);
        closeTodayButton.setText("平今");
        closeTodayButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        closeTodayButton.setBounds(118, 0, 59, 26);
        
        Label label_2 = new Label(composite, SWT.NONE);
        label_2.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        label_2.setBounds(10, 119, 55, 27);
        label_2.setText("手数");
        
        volume = new Text(composite, SWT.BORDER);
        volume.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent e) {
        		if(!StringUtils.isEmpty(volume.getText())){
        			ApplicationConfig.setProperty("volume", volume.getText());
        		}
        	}
        });
        volume.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        volume.setBounds(71, 116, 95, 27);
        
        String volumeStr = ApplicationConfig.getProperty("volume");
        if(!StringUtils.isEmpty(volumeStr)){
        	volume.setText(volumeStr);;
        }
        
        Label label_3 = new Label(composite, SWT.NONE);
        label_3.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        label_3.setBounds(10, 162, 55, 24);
        label_3.setText("指定价");
        
        priceText = new Text(composite, SWT.BORDER);
        priceText.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        priceText.setBounds(71, 159, 95, 27);
        
        /*availableLabel = new Label(composite, SWT.NONE);
        availableLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        availableLabel.setBounds(190, 149, 83, 27);
        availableLabel.setText(available);*/
        
        volumePermit = new Label(composite, SWT.NONE);
        volumePermit.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        volumePermit.setBounds(190, 119, 71, 27);
        volumePermit.setText("可开数量");
        
        Button btnNewButton = new Button(composite, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                //校验参数
                //下单入参
                CThostFtdcInputOrderField inputOrderField;
                try {
                    inputOrderField = new CThostFtdcInputOrderField();
                    // 买卖方向
                    if(buyButton.getSelection()){
                        inputOrderField.setDirection(THOST_FTDC_D_Buy);
                    }else if(sellButton.getSelection()){
                        inputOrderField.setDirection(THOST_FTDC_D_Sell);
                    }else {
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(CommonConstant.DIRECTION_MISS);
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                        return;
                    }
                    
                    // 合约代码
                    String instrumentID = combo.getText();
                    
                    if(StringUtils.isEmpty(instrumentID)){
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(CommonConstant.CONTRACT_MISS);
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                        return;
                    }
                    
                    
                    inputOrderField.setInstrumentID(instrumentID);
                    
                    // 价格
                    if(StringUtils.isEmpty(priceText.getText())){
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(CommonConstant.PRICE_MISS);
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                        return;
                    }
                    inputOrderField.setLimitPrice(Double.valueOf(priceText.getText()));
                    
                    // 数量
                    if(StringUtils.isEmpty(volume.getText())){
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(CommonConstant.VOLUME_MISS);
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                        return;
                    }
                    //手数必须大于0
                    Integer vol = Integer.parseInt(volume.getText());
                    if(vol < 1){
                        
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(CommonConstant.VOLUME_LIMIT);
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                        return;
                    }
                    
                    inputOrderField.setVolumeTotalOriginal(Integer.parseInt(volume.getText()));
                    
                    //开平方向
                    if(openButton.getSelection()){
                        // 组合开平标志     开仓
                        inputOrderField.setCombOffsetFlag("0");
                    }else if (closeButton.getSelection()) {
                        //平仓
                        if(checkSHPosition(instrumentID)){
                            inputOrderField.setCombOffsetFlag("4");
                        }else {
                            inputOrderField.setCombOffsetFlag("1");
                        }
                    }else if (closeTodayButton.getSelection()) {
                        //平今
                        if(checkSHPosition(instrumentID)){
                            inputOrderField.setCombOffsetFlag("3");
                        }else {
                            inputOrderField.setCombOffsetFlag("1");
                        }
                    }else {
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(CommonConstant.OFFSET_MISS);
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                        return;
                    }
                    int checkflg = 0;
                    if (closeButton.getSelection()) {
                    	
                    	TableItem[] items = positionTable.getItems();
                        //轮询持仓表，查看是否有反方向的持仓，如果有则判断数量，数量小于等于持仓数量就平仓
                        if(items != null && items.length >0){
                            for(TableItem item : items){
                                if(StringUtils.isEmpty(item.getText())){
                                    continue;
                                }
                                InvestorPosition position = (InvestorPosition) item.getData();
                                
                                
                                if(position.getInstrumentid().equals(combo.getText())){
                            		if(buyButton.getSelection()){
                            			//卖开要买平
                            			if(position.getPosidirection().equals("1")){
                            				if(Integer.parseInt(position.getPosition().toString()) >= Integer.parseInt(volume.getText())) {
                            					checkflg = 1;
                            				}
                            			}
                            		}
                            		
                            		if(sellButton.getSelection()){
                            			//买开要卖平
                            			if(position.getPosidirection().equals("0")){
                            				if(Integer.parseInt(position.getPosition().toString()) >= Integer.parseInt(volume.getText())) {
                            					checkflg = 1;
                            				}
                            			}
                            		}
                            		
                            		
                            	}
                            }
                        }
                    	
                        if(checkflg == 0){
                        	MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                            box.setMessage("平仓数量请确认！");
                            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                            box.open();
                            return;
                        }
                    }
                    
                    
                    
                    
                } catch (NumberFormatException e2) {
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("下单失败");
                    box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                    box.open();
                    return;
                }
                
                
                
                Thread thread = new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                        try {
                            PrintWriter out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("order");
                            stringBuffer.append("|"+userName);
                            
                            //投资者代码
                            inputOrderField.setInvestorID(userName);
                            
                            ///报单引用
                            //inputOrderField.setOrderRef("000000000001");
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
                            
                            stringBuffer.append("|"+JSON.toJSONString(inputOrderField));
                            logger.info("下单请求参数："+stringBuffer.toString());
                            out.println(stringBuffer.toString());
                            out.flush();
                        } catch (Exception e1) {
                            logger.error("获取交易socket输出流失败",e1);
                            Display.getDefault().syncExec(new Runnable() {
                                @Override
                                public void run() {
                                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                                    box.setMessage("下单失败");
                                    box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                                    box.open();
                                }
                            });
                            
                        }
                        
                    }
                });
                thread.start();
            }
        });
        btnNewButton.setBounds(10, 267, 156, 34);
        btnNewButton.setText("下单");
        
        Listener listener = new quickKeyListener();
        
        Button btnCheckButton = new Button(composite, SWT.CHECK);
        btnCheckButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        btnCheckButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //开启一键下单
                if(btnCheckButton.getSelection()){
                    combo.setEnabled(false);
                    buyButton.setEnabled(false);
                    sellButton.setEnabled(false);
                    openButton.setEnabled(false);
                    closeButton.setEnabled(false);
                    volume.setEnabled(false);
                    priceText.setEnabled(false);
                    mode.setEnabled(true);
                    btnNewButton.setEnabled(false);
                    regeditQuickOrder(listener);
                }else {
                    combo.setEnabled(true);
                    buyButton.setEnabled(true);
                    sellButton.setEnabled(true);
                    openButton.setEnabled(true);
                    closeButton.setEnabled(true);
                    volume.setEnabled(true);
                    priceText.setEnabled(true);
                    mode.setEnabled(false);
                    btnNewButton.setEnabled(true);
                    logger.debug("注销快捷键");
                    shell.getDisplay().removeFilter(SWT.KeyDown, listener);
                }
            }
        });
        btnCheckButton.setBounds(10, 198, 83, 17);
        btnCheckButton.setText("一键下单");
        
        killButton = new Button(composite, SWT.CHECK);
        killButton.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        killButton.setBounds(99, 198, 105, 17);
        killButton.setText("不成交即撤单");
        
        Label label_6 = new Label(composite, SWT.NONE);
        label_6.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        label_6.setBounds(10, 233, 99, 28);
        label_6.setText("一键模式选择");
        
        mode = new Combo(composite, SWT.DROP_DOWN|SWT.READ_ONLY);
        mode.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        mode.setBounds(115, 230, 71, 22);
        mode.add("模式一");
        mode.add("模式二");
        mode.add("模式三");
        mode.setText(mode.getItem(0));
        mode.setEnabled(false);
        
        hand = new Label(composite, SWT.NONE);
        hand.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        hand.setBounds(261, 17, 83, 22);
        hand.setText(handCount.toString());
        
        Label label_4 = new Label(composite, SWT.NONE);
        label_4.setAlignment(SWT.RIGHT);
        label_4.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
        label_4.setText("总开平：");
        label_4.setBounds(190, 17, 65, 22);
        
        sellLabel = new Label(composite, SWT.NONE);
        sellLabel.setBounds(190, 145, 126, 17);
        sellLabel.setText(" ");
        
        buyLabel = new Label(composite, SWT.NONE);
        buyLabel.setBounds(190, 168, 126, 17);
        buyLabel.setText(" ");
        
        Button btnNewButton_1 = new Button(composite, SWT.NONE);
        btnNewButton_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                //市价反手
                
                TableItem[] items = positionTable.getSelection();
                if(items == null || items.length <1){
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("请先选择持仓");
                    box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                    box.open();
                }else{
                    if(items[0].getData() != null){
                        InvestorPosition position = (InvestorPosition) items[0].getData();
                        //下反手单
                        inverseOrder(position);
                    }else{
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage("请先选择持仓");
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                    }
                }
                
            }
        });
        btnNewButton_1.setBounds(188, 267, 156, 34);
        btnNewButton_1.setText("市价反手");
    }
    
    private void createCenterFolder(){
//    	CTabFolder centerFolder = new CTabFolder(centerFolderN, SWT.BORDER);
        centerFolder.setLayoutData(BorderLayout.CENTER);
        
        centerFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        
        CTabItem tabItem = new CTabItem(centerFolder, SWT.NONE);
        tabItem.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
        
        centerFolder.setSelection(tabItem);
        tabItem.setText(" 委 托 单 ");
        //创建委托单表格
        createEntrustTable(centerFolder);
        
        tabItem.setControl(entrustTable);
        entrustTable.setHeaderVisible(true);
        entrustTable.setLinesVisible(true);
        
        //初始化委托表格
        if(orders != null && orders.size() >0){
            for(Order order:orders){
                TableItem item = new TableItem(entrustTable, SWT.NONE);
                item.setData(order);
                item.setText(0, order.getInstrumentid());
                item.setText(1, order.getDirection().equals("0")? "买":"卖");
                item.setText(2, order.getComboffsetflag().equals("0")? "开":"平");
                item.setText(3, order.getVolumetotaloriginal().toString());
                item.setText(4, order.getLimitprice().toString());
                item.setText(5, order.getVolumetraded()==null?"0":order.getVolumetraded().toString());
                item.setText(6, order.getStatusmsg());
                item.setText(7, StringUtils.isEmpty(order.getInserttime())?"":order.getInserttime());
            }
            //根据时间冒泡排序
            for(int i=0 ; i < orders.size()-1 ;i++){
                for(int j = 0 ; j < orders.size()-1-i ; j++){
                    TableItem item = entrustTable.getItem(j);
                    TableItem item1 = entrustTable.getItem(j+1);
                    
                    String time = item.getText(7);
                    String time1 = item1.getText(7);
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    try {
                        Date d = sdf.parse(time);
                        Date d1 = sdf.parse(time1);
                        if(d1.after(d)){
                            Order order = (Order) item.getData();
                            item.setText(1, item1.getText(1));
                            item.setText(2, item1.getText(2));
                            item.setText(3, item1.getText(3));
                            item.setText(4, item1.getText(4));
                            item.setText(5, item1.getText(5));
                            item.setText(6, item1.getText(6));
                            item.setText(7, item1.getText(7));
                            item.setData(item1.getData());
                            
                            item1.setData(order);
                            item1.setText(0, order.getInstrumentid());
                            item1.setText(1, order.getDirection().equals("0")? "买":"卖");
                            item1.setText(2, order.getComboffsetflag().equals("0")? "开":"平");
                            item1.setText(3, order.getVolumetotaloriginal().toString());
                            item1.setText(4, order.getLimitprice().toString());
                            item1.setText(5, order.getVolumetraded()==null?"0":order.getVolumetraded().toString());
                            item1.setText(6, order.getStatusmsg());
                            item1.setText(7, StringUtils.isEmpty(order.getInserttime())?"":order.getInserttime());
                        }
                        
                    } catch (ParseException e1) {
                        logger.error("解析时间失败",e1);
                    }
                    
                }
            }
        }
        
        CTabItem tabItem_1 = new CTabItem(centerFolder, SWT.NONE);
        tabItem_1.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
        tabItem_1.setText(" 成 交 单 ");
        //创建成交单表格
        createDealTable(centerFolder);
        
        tabItem_1.setControl(dealTable);
        dealTable.setHeaderVisible(true);
        dealTable.setLinesVisible(true);
        
        //初始化成交表格
        if(trades != null && trades.size() >0){
            for(Trade trade : trades){
                
                TableItem item = new TableItem(dealTable, SWT.NONE);
                item.setData(trade);
                item.setText(0, trade.getInstrumentid());
                item.setText(1, trade.getDirection().equals("0")? "买":"卖");
                item.setText(2, trade.getOffsetflag().equals("0")? "开":"平");
                item.setText(3, trade.getVolume().toString());
                item.setText(4, trade.getPrice().toString());
                item.setText(5, StringUtils.isEmpty(trade.getTradetime())?"":trade.getTradetime());
            }
            
          //根据时间冒泡排序
            for(int i=0 ; i < trades.size()-1 ;i++){
                for(int j = 0 ; j < trades.size()-1-i ; j++){
                    TableItem item = dealTable.getItem(j);
                    TableItem item1 = dealTable.getItem(j+1);
                    
                    String time = item.getText(5);
                    String time1 = item1.getText(5);
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    try {
                        Date d = sdf.parse(time);
                        Date d1 = sdf.parse(time1);
                        if(d1.after(d)){
                            Trade trade = (Trade) item.getData();
                            item.setText(1, item1.getText(1));
                            item.setText(2, item1.getText(2));
                            item.setText(3, item1.getText(3));
                            item.setText(4, item1.getText(4));
                            item.setText(5, item1.getText(5));
                            item.setData(item1.getData());
                            
                            item1.setData(trade);
                            item1.setText(0, trade.getInstrumentid());
                            item1.setText(1, trade.getDirection().equals("0")? "买":"卖");
                            item1.setText(2, trade.getOffsetflag().equals("0")? "开":"平");
                            item1.setText(3, trade.getVolume().toString());
                            item1.setText(4, trade.getPrice().toString());
                            item1.setText(5, StringUtils.isEmpty(trade.getTradetime())?"":trade.getTradetime());
                        }
                        
                    } catch (ParseException e1) {
                        logger.error("解析时间失败",e1);
                    }
                }
            }
        }
        
    	
    }
    
    
    private void createSouthFolder(){
//    	 CTabFolder southFolder = new CTabFolder(southFolderN, SWT.BORDER);
         southFolder.setLayoutData(BorderLayout.SOUTH);
         southFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
         
         CTabItem positionTabItem = new CTabItem(southFolder, SWT.NONE);
         positionTabItem.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
         southFolder.setSelection(positionTabItem);
         positionTabItem.setText("  持  仓  ");
         
         createPositionTable(southFolder);
         positionTabItem.setControl(positionTable);
         positionTable.setHeaderVisible(true);
         positionTable.setLinesVisible(true);
         
         if(positions !=null && positions.size() >0){
             for(InvestorPosition position :positions){
                 TableItem tableItem= new TableItem(positionTable, SWT.NONE);
                 tableItem.setData(position);
                 tableItem.setText(0, position.getInstrumentid());
                 String s = position.getPosidirection().equals("0")?"买":"卖";
                 tableItem.setText(1, s);//买卖数量
                 tableItem.setText(2, position.getPosition().toString());//数量
                 tableItem.setText(3, position.getOpenamount().toString());//持仓均价
             }
         }
         
         //持仓大小初始化
         int r = 2 - (StringUtils.isEmpty(positions) ? 0:positions.size());
         if(r>0){
             for(int i = 0; i<r; i++){
                 TableItem tableItem= new TableItem(positionTable, SWT.NONE);
                 tableItem.setText("");
             }
         }
    }
    
    public void createEntrustTable(Composite parent){
        
        entrustTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        entrustTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                TableItem selected = entrustTable.getItem(new Point(e.x, e.y));
                if(e.button == 3){//鼠标右键
                    Menu menu = new Menu(entrustTable);
                    entrustTable.setMenu(menu);
                    
                    if(selected != null ){
                        
                        Order order = (Order) selected.getData();
                        if(!order.getOrderstatus().equals("5") && !order.getOrderstatus().equals("0")
                                &&!order.getOrderstatus().equals("2") && !order.getOrderstatus().equals("4")){
                            MenuItem item1 = new MenuItem(menu, SWT.None);
                            item1.setText("撤单");
                            item1.addSelectionListener(new CancelOrderSelection(order));
                        }
                    }
                    
                    if(entrustTable.getItemCount()>0){
                        MenuItem item = new MenuItem(menu, SWT.None);
                        item.setText("全部撤单");
                        item.addSelectionListener(new CancelAllOrderSelection(entrustTable.getItems()));
                    }
                    
                }
            }
        });
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        entrustTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(20));
        new TableColumn(entrustTable, SWT.NONE).setText("合约");
          
        tLayout.addColumnData(new ColumnWeightData(10));
        new TableColumn(entrustTable, SWT.NONE).setText("买卖");
        
        tLayout.addColumnData(new ColumnWeightData(10));
        new TableColumn(entrustTable, SWT.NONE).setText("开平");
        
        tLayout.addColumnData(new ColumnWeightData(10));
        new TableColumn(entrustTable, SWT.NONE).setText("报单手数");
        
        tLayout.addColumnData(new ColumnWeightData(20));
        new TableColumn(entrustTable, SWT.NONE).setText("报单价格");
        
        tLayout.addColumnData(new ColumnWeightData(10));
        new TableColumn(entrustTable, SWT.NONE).setText("成交手数");

        tLayout.addColumnData(new ColumnWeightData(35));
        new TableColumn(entrustTable, SWT.NONE).setText("挂单状况");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(entrustTable, SWT.NONE).setText("报单时间");
        
    }
    
    public void createDealTable(Composite parent){
        
        dealTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        dealTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(dealTable, SWT.NONE).setText("合约");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(dealTable, SWT.NONE).setText("买卖");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(dealTable, SWT.NONE).setText("开平");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(dealTable, SWT.NONE).setText("成交手数");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(dealTable, SWT.NONE).setText("成交价格");

        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(dealTable, SWT.NONE).setText("成交时间");
        
    }
    
    public void createPositionTable(Composite parent){
        
        positionTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        
        positionTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("合约");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("买卖");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("数量");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("持仓均价");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("现价");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("持仓盈亏");
        
    }
    
    public void initMarketSocket(){
        //如果socket为空 或者断开连接则创建一个socket
        if(marketSocket == null || !(marketSocket.isConnected() == true && marketSocket.isClosed() == false)){
            try {
                logger.info("=====================开始与行情服务器建立连接==============");
                marketSocket = new Socket(MARKET_IP,MARKET_PORT);
                marketSocket.setKeepAlive(false);
            } catch (Exception e) {
                logger.error("与行情服务器通信失败",e);
            }
        }
    }
    
    public void initTradeSocket(){
        //如果socket为空 或者断开连接则创建一个socket
        if(tradeSocket == null || !(tradeSocket.isConnected() == true && tradeSocket.isClosed() == false)){
            try {
                logger.info("=====================开始与交易服务器建立连接==============");
                tradeSocket = new Socket(TRADE_IP,TRADE_PORT);
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
    
    public void recreateSocket(){
        
        if(marketSocket != null){
            try {
                marketSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            this.marketSocket = new Socket(MARKET_IP,3393);
        } catch (Exception e) {
            logger.error("与行情服务器通信失败",e);
        }
    }

    public Socket getSocket() {
        return marketSocket;
    }

    public void setSocket(Socket socket) {
        this.marketSocket = socket;
    }

    public void setTradeSocket(Socket tradeSocket) {
        this.tradeSocket = tradeSocket;
    }

    public Socket getTradeSocket() {
        return tradeSocket;
    }

    public List<UserContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<UserContract> contracts) {
        this.contracts = contracts;
    }
    
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<InvestorPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<InvestorPosition> positions) {
        this.positions = positions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Table getEntrustTable() {
        return entrustTable;
    }

    public void setEntrustTable(Table entrustTable) {
        this.entrustTable = entrustTable;
    }

    public Table getDealTable() {
        return dealTable;
    }

    public void setDealTable(Table dealTable) {
        this.dealTable = dealTable;
    }

    public Table getPositionTable() {
        return positionTable;
    }

    public void setPositionTable(Table positionTable) {
        this.positionTable = positionTable;
    }
    
    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
    
    public Label getHand() {
        return hand;
    }

    public void setHand(Label hand) {
        this.hand = hand;
    }

    /**
     * 不成交即撤单
     * @return
     */
    public Button getKillButton() {
        return killButton;
    }
    
    /**
     * 不成交即撤单
     * @param killButton
     */
    public void setKillButton(Button killButton) {
        this.killButton = killButton;
    }

    public Label getVolumePermit() {
        return volumePermit;
    }

    public void setVolumePermit(Label volumePermit) {
        this.volumePermit = volumePermit;
    }

    public Text getPriceText() {
        return priceText;
    }

    public void setPriceText(Text priceText) {
        this.priceText = priceText;
    }
    
    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }


    public UserAvailableMemorySave getInitAvailable() {
        return initAvailable;
    }

    public void setInitAvailable(UserAvailableMemorySave initAvailable) {
        this.initAvailable = initAvailable;
    }




    /**
     * 撤单事件
     * @author BHQH-CXYWB
     */
    public class CancelOrderSelection extends SelectionAdapter{
        
        private Order order;
        
        public CancelOrderSelection(Order order) {
            this.order = order;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            cancelOrder(order);
        }
    }
    
    public void order(char direction, String mode, int keyCode){
        
        if(StringUtils.isEmpty(volume.getText())){
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(CommonConstant.VOLUME_MISS);
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
            return;
        }
        
        //下单入参
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        
        // 合约代码
        String contractNo = combo.getText();
        inputOrderField.setInstrumentID(contractNo);
        //买卖方向
        inputOrderField.setDirection(direction);
        
        //开平标志
        Map<String,Object> map = getOffsetFlag(contractNo, direction, Integer.parseInt(volume.getText()));

        inputOrderField.setCombOffsetFlag((String) map.get("offsetFlag"));
        
        if(map.get("offsetFlag").equals("3")){
            //如果是平仓 ，数量为持仓数量
            inputOrderField.setVolumeTotalOriginal((int) map.get("volumn"));
        }else {
            // 数量
            inputOrderField.setVolumeTotalOriginal(Integer.parseInt(volume.getText()));
        }
        
        // 价格
        try {
            Double price = getPrice(contractNo, mode, keyCode);
            inputOrderField.setLimitPrice(price);
        } catch (Exception e1) {
            logger.error("获取价格失败",e1);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("获取价格失败");
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
            return;
        }
        
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
        
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("order");
        stringBuffer.append("|"+userName);
        stringBuffer.append("|"+JSON.toJSONString(inputOrderField));
        logger.info("下单请求参数："+stringBuffer.toString());
        
        PrintWriter out;
        try {
            out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
            out.println(stringBuffer.toString());
            out.flush();
        } catch (Exception e) {
            logger.error("快捷下单失败");
        }
        
    }
    
    //TODO
    /**
     * 获取开平仓标志
     * @param contractNo 合约代码
     * @param direction 买卖方向
     * @param volumn 数量
     * @return
     */
    public Map<String,Object> getOffsetFlag(String contractNo, char direction ,int volumn){
        
        logger.debug("获取开平仓标志......");
        String offsetFlag = "0";
        Map<String, Object> map = new HashMap<String, Object>();
        TableItem[] items = positionTable.getItems();
        //轮询持仓表，查看是否有反方向的持仓，如果有则判断数量，数量小于等于持仓数量就平仓
        if(items != null && items.length >0){
            for(TableItem item : items){
                if(StringUtils.isEmpty(item.getText())){
                    continue;
                }
                InvestorPosition position = (InvestorPosition) item.getData();
                if(contractNo.equals(position.getInstrumentid())){//如果合约相同
                    
                    logger.debug("持仓方向："+position.getPosidirection()+",当前交易方向："+direction);
                    if(position.getPosidirection().equals(direction+"")){//同方向
                        continue;
                    }
                    
                    //如果反方向,则平仓，平仓数量等于持仓数量
                    logger.debug("平仓数量："+position.getPosition());
                    offsetFlag = "3";
                    map.put("volumn", position.getPosition().intValue());
                    break;
                }
            }
        }
        map.put("offsetFlag", offsetFlag);
        logger.debug("开平仓标志为："+offsetFlag);
        return map;
    }
    
    /**
     * 获取价格
     * @param contractNo
     * @param mode
     * @param keyCode
     * @return
     * @throws FutureException
     */
    public Double getPrice(String contractNo, String mode, int keyCode) throws FutureException{
        
        logger.debug("合约："+contractNo+",模式："+mode+",按键："+keyCode);
        
        Double price = 0.0;
        
        UserContract contract = null;
        String bidPrice1 = "";
        String askPrice1 = "";
        
        for (TableItem item : marketTable.getItems()) {
            if(contractNo.equals(item.getText(0))){
                contract = (UserContract) item.getData();
                bidPrice1 = item.getText(3);
                askPrice1 = item.getText(4);
                break;
            }
        }
        
        if(contract == null || StringUtils.isEmpty(bidPrice1)|| StringUtils.isEmpty(askPrice1)){
            logger.warn("获取价格失败");
            throw new FutureException("", CommonConstant.GET_PRICE_FIELD);
        }
        
        if(mode.equals("模式一")){
            if(keyCode == SWT.KEYPAD_1){
                //买一价
                price = Double.valueOf(bidPrice1);
            }else if (keyCode == SWT.KEYPAD_4) {
                //买一价+1
                price = Double.valueOf(bidPrice1)+contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_7) {
                //买一价+2
                price = Double.valueOf(bidPrice1)+contract.getTickSize().doubleValue()*2;
            }else if (keyCode == SWT.KEYPAD_3) {
                //卖一价
                price = Double.valueOf(askPrice1);
            }else if (keyCode == SWT.KEYPAD_6) {
                //卖一价 -1
                price = Double.valueOf(askPrice1)-contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_9) {
                //卖一价 -2
                price = Double.valueOf(askPrice1)-contract.getTickSize().doubleValue()*2;
            }
        }else if (mode.equals("模式二")) {
            if(keyCode == SWT.KEYPAD_1){
                //买一价+1
                price = Double.valueOf(bidPrice1)+contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_4) {
                //买一价+2
                price = Double.valueOf(bidPrice1)+contract.getTickSize().doubleValue()*2;
            }else if (keyCode == SWT.KEYPAD_7) {
                //买一价+3
                price = Double.valueOf(bidPrice1)+contract.getTickSize().doubleValue()*3;
            }else if (keyCode == SWT.KEYPAD_3) {
                //卖一价-1
                price = Double.valueOf(askPrice1)-contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_6) {
                //卖一价 -2
                price = Double.valueOf(askPrice1)-contract.getTickSize().doubleValue()*2;
            }else if (keyCode == SWT.KEYPAD_9) {
                //卖一价 -3
                price = Double.valueOf(askPrice1)-contract.getTickSize().doubleValue()*3;
            }
        }else if (mode.equals("模式三")) {
            if(keyCode == SWT.KEYPAD_1){
                //买一价+1
                price = Double.valueOf(bidPrice1)+contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_4) {
                //买一价
                price = Double.valueOf(bidPrice1);
            }else if (keyCode == SWT.KEYPAD_7) {
                //买一价-1
                price = Double.valueOf(bidPrice1)-contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_3) {
                //卖一价-1
                price = Double.valueOf(askPrice1)-contract.getTickSize().doubleValue();
            }else if (keyCode == SWT.KEYPAD_6) {
                //卖一价 
                price = Double.valueOf(askPrice1);
            }else if (keyCode == SWT.KEYPAD_9) {
                //卖一价 +1
                price = Double.valueOf(askPrice1)+contract.getTickSize().setScale(2).doubleValue();
                logger.debug("卖一价 +1:"+price);
            }
        }
        logger.debug("获取价格："+price);
        return price;
    }
    
    /**
     * 撤单方法
     * @param order
     */
    public void cancelOrder(Order order){

        CThostFtdcInputOrderActionField field = new CThostFtdcInputOrderActionField();
        //合约号
        field.setInstrumentID(order.getInstrumentid());
        //frontID
        field.setFrontID(order.getFrontid().intValue());
        //sessionID
        field.setSessionID(order.getSessionid().intValue());
        //orderRef
        field.setOrderRef(order.getOrderref());
        //actionFlag
        field.setActionFlag(THOST_FTDC_AF_Delete);
        //撤单数
        field.setVolumeChange(order.getVolumetotaloriginal().intValue()-order.getVolumetraded().intValue());
        //orderSysID
        field.setOrderSysID(order.getOrdersysid());
        //exchangeID
        field.setExchangeID(order.getExchangeid());
        //买卖标志放在用户ID用来区分走哪条路
        if((order.getDirection().equals("0") && order.getComboffsetflag().equals("0")) 
                || order.getDirection().equals("1") && !order.getComboffsetflag().equals("0")){
            field.setUserID("0");
        }else {
            field.setUserID("1");
        }
        
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cancel");
        stringBuffer.append("|"+userName);
        stringBuffer.append("|"+JSON.toJSONString(field));
        logger.debug("发送撤单请求："+stringBuffer.toString());
        
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
            out.println(stringBuffer.toString());
            out.flush();
        } catch (Exception e1) {
            logger.error("获取输出流失败",e1);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(CommonConstant.CONNECT_FIELD);
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
        }
        
    }
    
    /**
     * 平仓方法
     */
    public void closeOrder(InvestorPosition position){
    	
    	logger.debug("平现有持仓："+JSON.toJSONString(position));
    	//下单入参
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        
        // 合约代码
        String instrumentid = position.getInstrumentid();
        inputOrderField.setInstrumentID(instrumentid);
        
        String lowerLimitPrice = "0";
        String upperLimitPrice = "0";
        
        //获取涨跌停价格
        for (TableItem item : marketTable.getItems()) {
            if(instrumentid.equals(item.getText(0))){
                lowerLimitPrice = item.getText(9);
                upperLimitPrice = item.getText(8);
                break;
            }
        }
        
        //买卖方向
        if(position.getPosidirection().equals("0")){

			inputOrderField.setDirection(THOST_FTDC_D_Sell);
			//买开 --> 卖平 ，以跌停价平仓
			
			BigDecimal price = new BigDecimal(lowerLimitPrice);
			logger.debug("清仓价格为跌停价："+price);
			inputOrderField.setLimitPrice(price.doubleValue());
        }else {
        	//卖开 --> 买平 ，以涨停价平仓
        	BigDecimal price = new BigDecimal(upperLimitPrice);
			//跌停价
			logger.debug("清仓价格为涨停价："+price);
        	inputOrderField.setDirection(THOST_FTDC_D_Buy);
		}
        
        //开平标志
        inputOrderField.setCombOffsetFlag("3");
        
        //数量为持仓数量
        inputOrderField.setVolumeTotalOriginal(position.getPosition().intValue());
        
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
        
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("order");
        stringBuffer.append("|"+userName);
        stringBuffer.append("|"+JSON.toJSONString(inputOrderField));
        logger.info("清仓下单请求参数："+stringBuffer.toString());
        
        PrintWriter out;
        try {
            out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
            out.println(stringBuffer.toString());
            out.flush();
        } catch (Exception e) {
            logger.error("快捷下单失败");
        }
    	
    }
    
    
    /**
     * 市价反手方法
     */
    public void inverseOrder(InvestorPosition position){
        
        logger.debug("平现有持仓："+JSON.toJSONString(position));
        //下单入参
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        
        // 合约代码
        String instrumentid = position.getInstrumentid();
        inputOrderField.setInstrumentID(instrumentid);
        
        String lowerLimitPrice = "0";
        String upperLimitPrice = "0";
        
        //获取涨跌停价格
        for (TableItem item : marketTable.getItems()) {
            if(instrumentid.equals(item.getText(0))){
                lowerLimitPrice = item.getText(9);
                upperLimitPrice = item.getText(8);
                break;
            }
        }
        
        //买卖方向
        if(position.getPosidirection().equals("0")){

            inputOrderField.setDirection(THOST_FTDC_D_Sell);
            //买开 --> 卖平 ，以跌停价平仓
            
            BigDecimal price = new BigDecimal(lowerLimitPrice);
            logger.debug("清仓价格为跌停价："+price);
            inputOrderField.setLimitPrice(price.doubleValue());
        }else {
            //卖开 --> 买平 ，以涨停价平仓
            BigDecimal price = new BigDecimal(upperLimitPrice);
            //跌停价
            logger.debug("清仓价格为涨停价："+price);
            inputOrderField.setDirection(THOST_FTDC_D_Buy);
            inputOrderField.setLimitPrice(price.doubleValue());
        }
        
        //开平标志
        inputOrderField.setCombOffsetFlag("3");
        
        //数量为持仓数量
        inputOrderField.setVolumeTotalOriginal(position.getPosition().intValue());
        
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
        
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("order");
        stringBuffer.append("|"+userName);
        stringBuffer.append("|"+JSON.toJSONString(inputOrderField));
        logger.info("清仓下单请求参数："+stringBuffer.toString());
        
        PrintWriter out;
        try {
            out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
            out.println(stringBuffer.toString());
            out.flush();
        } catch (Exception e) {
            logger.error("市价反手平仓失败");
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("市价反手平仓失败");
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
        }
        
        //等待50毫秒
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            logger.error("市价反手等待失败");
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("市价反手等待失败");
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
        }
        
        //下反手单
        inputOrderField.setCombOffsetFlag("0");
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer1.append("order");
        stringBuffer1.append("|"+userName);
        stringBuffer1.append("|"+JSON.toJSONString(inputOrderField));
        logger.info("反手下单请求参数："+stringBuffer1.toString());
        try {
            out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
            out.println(stringBuffer1.toString());
            out.flush();
        } catch (Exception e) {
            logger.error("市价反手开仓失败");
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("市价反手开仓失败");
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
        }
        
    }
    
    
    /**
     * 全部撤单事件
     * @author caojia
     */
    public class CancelAllOrderSelection extends SelectionAdapter{
        
        private TableItem[] tableItems;
        
        public CancelAllOrderSelection(TableItem[] tableItems) {
            this.tableItems = tableItems;
        }
        @Override
        public void widgetSelected(SelectionEvent e) {
            
            if(tableItems == null || tableItems.length < 1){
                return;
            }
            
            for (TableItem tableItem : tableItems) {
                Order order = (Order) tableItem.getData();
                
                logger.debug("撤单："+JSON.toJSONString(order));
                
                if(order.getOrderstatus().equals("5") || order.getOrderstatus().equals("0")
                    || order.getOrderstatus().equals("2") || order.getOrderstatus().equals("4")){
                    
                    continue;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                cancelOrder(order);
            }
            
        }
    }
    
    /**
     * 快捷下单监听
     * @author caojia
     */
    public class quickKeyListener implements Listener{

        @Override
        public void handleEvent(Event e) {

            logger.debug("快捷下单,keyCode:"+e.keyCode+",keychar:"+e.character);
            if(e.keyCode == SWT.KEYPAD_1 || e.keyCode == 49 || e.character == '1'){
                order( THOST_FTDC_D_Buy, mode.getText(), SWT.KEYPAD_1);
            }else if (e.keyCode == SWT.KEYPAD_4 || e.keyCode == 52 || e.character == '4') {
                order( THOST_FTDC_D_Buy, mode.getText(), SWT.KEYPAD_4);
            }else if (e.keyCode == SWT.KEYPAD_7 || e.keyCode == 55 || e.character == '7') {
                order( THOST_FTDC_D_Buy, mode.getText(), SWT.KEYPAD_7);
            }else if (e.keyCode == SWT.KEYPAD_3 || e.keyCode == 51 || e.character == '3') {
                order( THOST_FTDC_D_Sell, mode.getText(), SWT.KEYPAD_3);
            }else if (e.keyCode == SWT.KEYPAD_6 || e.keyCode == 54 || e.character == '6') {
                order( THOST_FTDC_D_Sell, mode.getText(), SWT.KEYPAD_6);
            }else if (e.keyCode == SWT.KEYPAD_9 || e.keyCode == 57 || e.character == '9') {
                order( THOST_FTDC_D_Sell, mode.getText(), SWT.KEYPAD_9 );
            }else if (e.keyCode == SWT.KEYPAD_0 || e.keyCode == 48 || e.character == '0') {//撤单
                logger.debug("快捷键全部撤单");
                if(entrustTable.getItems() == null || entrustTable.getItems().length < 1){
                    return;
                }
                
                for (TableItem tableItem : entrustTable.getItems()) {
                    Order order = (Order) tableItem.getData();
                    if(order.getOrderstatus().equals("5") || order.getOrderstatus().equals("0")
                        || order.getOrderstatus().equals("2") || order.getOrderstatus().equals("4")){
                        
                        continue;
                    }
                    
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    
                    cancelOrder(order);
                }
            }else if (e.character == 'q' || e.character == 'Q') {
				//一键清仓
            	logger.debug("一键清仓");
            	
            	if(entrustTable.getItems() == null || entrustTable.getItems().length < 1){
                    return;
                }
                
                for (TableItem tableItem : entrustTable.getItems()) {
                    Order order = (Order) tableItem.getData();
                    if(order.getOrderstatus().equals("5") || order.getOrderstatus().equals("0")
                        || order.getOrderstatus().equals("2") || order.getOrderstatus().equals("4")){
                        
                        continue;
                    }
                    
                    cancelOrder(order);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            	
            	try {
					if(positionTable.getItems() == null || positionTable.getItemCount() <1){
						return;
					}
					for(TableItem item : positionTable.getItems()){
						if(StringUtils.isEmpty(item.getText(0))){
							continue;
						}
						
						InvestorPosition position = (InvestorPosition) item.getData();
						closeOrder(position);
						
					}
				} catch (Exception e1) {
					logger.error("一键清仓失败",e1);
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
		            box.setMessage(CommonConstant.CLOSE_FIELD);
		            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
		            box.open();
				}
			}
        }
    }
    
    /**
     * 根据合约编号查询合约属性
     * @param contractNo
     * @return
     */
    public UserContract getContractByContractNo(String contractNo){
        
        //logger.debug("查询"+contractNo+"合约属性");
        
        UserContract userContract = null;
        
        if(this.contracts != null){
            for(UserContract contract : contracts){
                if(contractNo.equals(contract.getContractNo())){
                    userContract = contract;
                    break;
                }
            }
        }
        //logger.debug("返回"+contractNo+"合约属性"+JSON.toJSONString(userContract));
        return userContract;
    }
    
    //是否上海合约
    public boolean checkSHPosition(String HyName) {
        //check 持仓是否是上海的。只有上海要平今平昨
        boolean retFlg = false;
        if(HyName.length() == 6){
            for (String hyName : SHINST) {
                if(HyName.substring(0, 2).equals(hyName)){
                    //上海
                    return true;
                }
            }
            
        }
        return retFlg;
        
    }
}
