package com.bohai.subAccount.swt.riskback;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.SubTradingaccountService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.swt.admin.AdminViewMain;
import com.bohai.subAccount.swt.riskback.helper.PositionThread;
import com.bohai.subAccount.swt.riskback.helper.TradeReceiveThread;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserPositionVO;

import swing2swt.layout.BorderLayout;

public class RiskControlDialog extends Dialog {

	protected Object result;
	public Shell shell;
	private TableItem item;
	private Table positionTable;
	private InvestorPositionService investorPositionService;
	private SubTradingaccountService subTradingaccountService;
	private UserContractService userContractService;
	private TradeService tradeService;
	static Logger logger = Logger.getLogger(AdminViewMain.class);
/*    private Socket socket;
    private Socket tradeSocket;*/
	//投资者持仓信息取得
    private List<InvestorPosition2> investorPositions = null;
    private List<InvestorPosition2> investorPositions2 = null;
    private List<UserContract> userContracts;
    private SubTradingaccount subTradingaccount = new SubTradingaccount();

    private String strUserName = "";
    //动态权益
    private Label rightValue;
    //持仓盈亏
    private Label positionWinLab;
    //平仓盈亏
    private Label closeWinLab;
    //可用资金
    private Label availableLab;
    //总成交数
    private Label label;
    //用户名
    private Label accountValue;
    
    private RiskManageView manageView;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RiskControlDialog(Shell parent, int style, TableItem item,RiskManageView manageView,List<UserContract> userContracts) {
		super(parent, style);
		setText("风险控制");
		loadSpringContext();
		this.item = item;
		this.manageView = manageView;
		this.userContracts = userContracts;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		
		createContents();
        label = new Label(shell, SWT.NONE);
        label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        label.setBounds(483, 49, 66, 21);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		
        //初始化行情socket
        //getMarketSocket();
        //初始化交易socket
        //getTradeSocket1();
        
        //行情接收线程
        /*Thread thread = new Thread(new MarketReceiveThread2(this, positionTable, strUserName, userContracts));
        
        thread.setDaemon(true);
        thread.start();*/
                
        //成交总数取得线程
		Thread tradethread = new Thread(new TradeReceiveThread(this, tradeService));
		tradethread.setDaemon(true);
		tradethread.start();
		
		Thread thread = new Thread(new PositionThread(item.getText(), this, manageView));
		thread.setDaemon(true);
		thread.start();

        /*//成交行情取得线程
		Thread tradethread3 = new Thread(new TradeReceiveThread3(this, investorPositionService, strUserName));
		tradethread3.setDaemon(true);
		tradethread3.start();*/

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				
				logger.debug("关闭dialog");
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
		
		shell.setSize(565, 420);
		shell.setText(getText());
		
		Label accountLabel = new Label(shell, SWT.NONE);
		accountLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		accountLabel.setBounds(10, 10, 75, 21);
		accountLabel.setText("账户号：");
		
		accountValue = new Label(shell, SWT.NONE);
		accountValue.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		accountValue.setBounds(91, 10, 75, 21);
		if (item != null)
			accountValue.setText(item.getText(0));
		    strUserName = accountValue.getText();
		
		Label rightLabel = new Label(shell, SWT.NONE);
		rightLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		rightLabel.setBounds(10, 49, 75, 21);
		rightLabel.setText("动态权益：");
		
		
		rightValue = new Label(shell, SWT.NONE);
		rightValue.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		rightValue.setBounds(91, 49, 86, 21);
		if (item != null)
			rightValue.setText(item.getText(1));
		
		Label amountLabel = new Label(shell, SWT.NONE);
		amountLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		amountLabel.setBounds(183, 49, 76, 21);
		amountLabel.setText("可用资金：");
		
		availableLab = new Label(shell, SWT.NONE);
		availableLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		availableLab.setBounds(265, 49, 86, 21);
		if (item != null)
			availableLab.setText(item.getText(2));
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancelLation();
			}
		});
		btnNewButton.setBounds(323, 247, 163, 53);
		btnNewButton.setText("一键撤单");
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openLimit();
			}
		});
		button.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		button.setBounds(45, 323, 163, 53);
		button.setText("限制开仓");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    // 强平限制操作
				getKyohei();
			}
		});
		button_1.setBounds(45, 247, 163, 53);
		button_1.setText("强平限制");
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deRestrict();
			}
		});
		button_2.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		button_2.setBounds(323, 323, 163, 53);
		button_2.setText("取消限制");
		
		Label label = new Label(shell, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBounds(183, 10, 75, 21);
		label.setText("持仓盈亏：");
		
		positionWinLab = new Label(shell, SWT.NONE);
		positionWinLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		positionWinLab.setBounds(265, 10, 86, 21);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_1.setBounds(376, 10, 75, 21);
		label_1.setText("平仓盈亏：");
		
		closeWinLab = new Label(shell, SWT.NONE);
		closeWinLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		closeWinLab.setBounds(474, 10, 75, 21);
		
		CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
		tabFolder.setBounds(0, 93, 559, 132);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tabItem.setText("持仓");
		tabFolder.setSelection(tabItem);
		
		createPositionTable(tabFolder);
		positionTable.setHeaderVisible(true);
		positionTable.setLinesVisible(true);
		tabItem.setControl(positionTable);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_2.setBounds(376, 49, 96, 21);
		label_2.setText("总成交手数：");
		
		//子账户信息取得
		getUserInfo();
	}
	
    /**
     *  强平限制操作
     */
    private void getKyohei() {
		boolean retResut = MessageDialog.openQuestion(shell, CommonConstant.MESSGAE_BOX_CONF, CommonConstant.SX001C);
		if (retResut) {
			setStrade("QPXZ");
			
			try {
				Thread.sleep(80);
			} catch (InterruptedException e1) {
				logger.error("线程睡眠失败",e1);
			}
			
			//20170314 add by caojia
			UserPositionVO userPositionVO = (UserPositionVO) this.item.getData();
			List<InvestorPosition> list = userPositionVO.getInvestorPositions();
			if(list != null && list.size() > 0){
				
				for(InvestorPosition position : list){
					
					try {
						StringBuffer sb = new StringBuffer();
						sb.append("risk");
						sb.append("|"+strUserName);
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
						inputOrderField.setInvestorID(strUserName);
						// 用户代码
						inputOrderField.setUserID(strUserName);
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
						PrintWriter out = new PrintWriter(new OutputStreamWriter(manageView.getTradeSocket().getOutputStream(),"UTF-8"));
						out.println(sb.toString());
						out.flush();
					} catch (Exception e) {
						logger.error("强平异常",e);
					}
					
				}
			}
			
		} else {
			return;
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
    
    public void createPositionTable(Composite parent){
        
        positionTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        
        positionTable.setLayout(tLayout);
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("合约");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("买卖数量");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("持仓均价");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("现价");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("持仓盈亏");
        
        tLayout.addColumnData(new ColumnWeightData(30));
        new TableColumn(positionTable, SWT.NONE).setText("可平量");
    }
    
    public void loadSpringContext(){
		/*logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        logger.info("===================加载成功 !==================");*/
        investorPositionService = (InvestorPositionService) SpringContextUtil.getBean("investorPositionService");
        subTradingaccountService = (SubTradingaccountService) SpringContextUtil.getBean("subTradingaccountService");
        userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
        tradeService = (TradeService) SpringContextUtil.getBean("tradeService");
	}
    
    /*public synchronized void getMarketSocket(){
    	//如果socket为空 或者断开连接则创建一个socket
    	if(socket == null || !(socket.isConnected() == true && socket.isClosed() == false)){
			try {
				logger.info("=====================开始与行情服务器建立连接==============");
				socket = new Socket("10.0.0.204",3393);
			} catch (Exception e) {
				logger.error("与行情服务器通信失败",e);
			}
		}
    }*/
    
	//子账户信息取得
	public void getUserInfo() {
		
		positionTable.setLayoutData(BorderLayout.CENTER);
		positionTable.setHeaderVisible(true);
		positionTable.setLinesVisible(true);
		
		UserPositionVO positionVO = (UserPositionVO) item.getData();
		
		if(positionVO.getInvestorPositions() == null || positionVO.getInvestorPositions().size() <1){
			return;
		}
		
		for(InvestorPosition position : positionVO.getInvestorPositions()){
			//for(TableItem dialogItem :)
			TableItem dialogItem = new TableItem(positionTable, SWT.NONE);
			//合约
			dialogItem.setText(0,position.getInstrumentid());
			//数量
			dialogItem.setText(1,position.getPosition().toString());
			//持仓均价
			dialogItem.setText(2,position.getOpenamount().toString());
			//现价
			dialogItem.setText(3, StringUtils.isEmpty(position.getLastPrice())?"0":position.getLastPrice().toString());
			//持仓盈亏
			dialogItem.setText(4, StringUtils.isEmpty(position.getPositionWin())?"0":position.getPositionWin().toString());
			//可平量
			dialogItem.setText(5,position.getPosition().toString());
		}
		
		
		
/*		try {
			//投资者持仓信息取得
			investorPositions = investorPositionService.getUserByUserName2(strUserName);
			//投资者平仓信息取得
			investorPositions2 = investorPositionService.getUserByUserName3(strUserName);
			//子账户资金信息
			subTradingaccount = subTradingaccountService.getUserByUserName2(strUserName);
			//合约信息
			userContracts = userContractService.queryUserContractByAll();
			
			for (InvestorPosition2 investorPosition : investorPositions) {
				TableItem userInfoTableItem = new TableItem(positionTable, SWT.NONE);
				userInfoTableItem.setData(investorPosition);
				userInfoTableItem.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
				//合约代号
				userInfoTableItem.setText(0, investorPosition.getInstrumentid());
				//买卖数量
				userInfoTableItem.setText(1, investorPosition.getTradingvolume().toString());
				//持仓均价
				userInfoTableItem.setText(2, investorPosition.getAverageposition().toString());
				//现价
				userInfoTableItem.setText(3, "0");
				//持仓盈亏
				userInfoTableItem.setText(4, "0");
				//可平量
				userInfoTableItem.setText(5, investorPosition.getAdjustablequantity().toString());
			}

			
		} catch (FutureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
    /*public void recreateSocket() {
    	try {
			this.socket = new Socket("10.0.0.204",3393);
		} catch (Exception e) {
			logger.error("与行情服务器通信失败",e);
		}
    }
    
    public void getTradeSocket1(){
        //如果socket为空 或者断开连接则创建一个socket
        if(tradeSocket == null || !(tradeSocket.isConnected() == true && tradeSocket.isClosed() == false)){
            try {
                logger.info("=====================开始与交易服务器建立连接==============");
                tradeSocket = new Socket("10.0.0.202",3394);
                tradeSocket.setKeepAlive(false);
            } catch (Exception e) {
            	MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
				box.setMessage("与交易服务器通信失败");
				box.setText(CommonConstant.MESSAGE_BOX_ERROR);
				box.open();
                logger.error("与交易服务器通信失败",e);
            }
        }
    }*/
    
    private void setStrade(String strFlg) {
    	try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(manageView.getTradeSocket().getOutputStream(),"UTF-8"));
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
            stringBuffer = new StringBuffer();
        	stringBuffer.append("risk|");
            stringBuffer.append(strUserName.trim() + "|");
            stringBuffer.append(strFlg.trim() + "|");
            logger.info(strlogInfo + stringBuffer.toString());
            out.println(stringBuffer.toString());
            out.flush();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    
	/*public Socket getSocket() {
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
	}*/
	
	//动态权益
	public Label getRightValue() {
		return rightValue;
	}

	public void setRightValue(Label rightValue) {
		this.rightValue = rightValue;
	}
	
	public Label getPositionWinLab() {
		return positionWinLab;
	}

	public void setPositionWinLab(Label positionWinLab) {
		this.positionWinLab = positionWinLab;
	}

	public Label getCloseWinLab() {
		return closeWinLab;
	}

	public void setCloseWinLab(Label closeWinLab) {
		this.closeWinLab = closeWinLab;
	}

	//总成交数
	public Label getLabel() {
		return label;
	}

	public Label getAvailableLab() {
		return availableLab;
	}

	public void setAvailableLab(Label availableLab) {
		this.availableLab = availableLab;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
	//用户名
	public Label getAccountValue() {
		return accountValue;
	}

	public void setAccountValue(Label accountValue) {
		this.accountValue = accountValue;
	}

	public List<InvestorPosition2> getInvestorPositions() {
		return investorPositions;
	}

	public void setInvestorPositions(List<InvestorPosition2> investorPositions) {
		this.investorPositions = investorPositions;
	}

	public List<InvestorPosition2> getInvestorPositions2() {
		return investorPositions2;
	}

	public void setInvestorPositions2(List<InvestorPosition2> investorPositions2) {
		this.investorPositions2 = investorPositions2;
	}
    public SubTradingaccount getSubTradingaccount() {
		return subTradingaccount;
	}

	public void setSubTradingaccount(SubTradingaccount subTradingaccount) {
		this.subTradingaccount = subTradingaccount;
	}

	public Table getPositionTable() {
		return positionTable;
	}

	public void setPositionTable(Table positionTable) {
		this.positionTable = positionTable;
	}
	
	
}
