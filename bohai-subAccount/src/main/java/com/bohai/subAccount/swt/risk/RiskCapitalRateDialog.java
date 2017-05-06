package com.bohai.subAccount.swt.risk;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.entity.CapitalRateDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CapitalRateDetailService;
import com.bohai.subAccount.service.CapitalRateService;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.swt.admin.AdminViewMain;
import com.bohai.subAccount.swt.risk.helper.PositionThread;
import com.bohai.subAccount.swt.risk.helper.TradeReceiveThread;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserCapitalRateVO;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RiskCapitalRateDialog extends Dialog {

	private TableItem item;
	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private int returnCheckFlg = 0;
	private TradeService tradeService;
	
	private RiskManageView manageView;
	
	private CapitalRateService capitalRateService;
	private CapitalRateDetailService capitalRateDetailService;
	private CapitalRate capitalRate;
	
	private UserCapitalRateVO userCapitalRateVO;
	static Logger logger = Logger.getLogger(AdminViewMain.class);

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RiskCapitalRateDialog(Shell parent, int style,TableItem item,RiskManageView manageView) {
		super(parent, style);
		setText("交易员出入金");
		loadSpringContext();
		this.item = item;
		this.manageView = manageView;

	}
	
	public void loadSpringContext(){
		/*logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        logger.info("===================加载成功 !==================");*/
		capitalRateService = (CapitalRateService) SpringContextUtil.getBean("capitalRateService");
		capitalRateDetailService = (CapitalRateDetailService) SpringContextUtil.getBean("capitalRateDetailService");
		
		
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		// 查数据库得到 自有资金 配置比例
		capitalRate = new CapitalRate();	
		try {
			capitalRate = capitalRateService.getUserByUserName(item.getText(0));
		} catch (FutureException e) {

			logger.debug("查数据库得到 自有资金 配置比例失败！");
		}
		
		//
//		Thread thread = new Thread(new PositionThread(item.getText(), this, manageView));
//		thread.setDaemon(true);
//		thread.start();
		
//		Thread tradethread = new Thread(new TradeReceiveThread(this, tradeService));
//		tradethread.setDaemon(true);
//		tradethread.start();
		
		
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
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
		shell.setSize(374, 280);
		shell.setText("交易员出入金");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 10, 61, 17);		
		label.setText("交易员名：");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(77, 10, 61, 17);
		if (item != null)
			label_1.setText(item.getText(0));
		
		Label label_10 = new Label(shell, SWT.NONE);
		label_10.setBounds(144, 10, 61, 17);
		label_10.setText(label_1.getText());
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(217, 10, 61, 17);
		label_2.setText("配置比例：");
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(284, 10, 61, 17);
		label_3.setText("1 : " + capitalRate.getUserCapitalRate());
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(10, 44, 61, 17);
		label_4.setText("自有资金：");
		
		//自有资金=动态权益-配资资金1
		double doublei = Double.valueOf(item.getText(1));
		doublei = doublei - capitalRate.getHostCapital().doubleValue();
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(77, 44, 96, 17);
		label_5.setText(String.valueOf(doublei));
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(182, 44, 61, 17);
		lblNewLabel.setText("现有调配：");
		
		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setBounds(249, 44, 96, 17);
		label_6.setText(capitalRate.getHostCapital().toString());
		
		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setBounds(10, 80, 61, 17);
		label_7.setText("入金金额：");
		
		Label label_8 = new Label(shell, SWT.NONE);
		label_8.setBounds(10, 120, 61, 17);
		label_8.setText("出金金额：");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(77, 80, 96, 23);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(77, 117, 96, 23);
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(182, 80, 61, 17);
		lblNewLabel_1.setText("入金调配：");
		
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(249, 80, 96, 17);
		
		Label label_9 = new Label(shell, SWT.NONE);
		label_9.setBounds(182, 120, 61, 17);
		label_9.setText("出金调配：");
		
		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(249, 120, 96, 17);
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				returnCheckFlg = 0;
				double doublej = Double.valueOf(item.getText(1));
				doublej = doublej - capitalRate.getHostCapital().doubleValue();
				// 出入金CHECK
				if (checkCapital(item.getText(0),text.getText(),text_1.getText(),String.valueOf(doublej),"0") == 1){
					// 出入金操作
					setCapital(item.getText(0),text.getText(),text_1.getText(),capitalRate.getUserCapitalRate().toString());
				} else {
					MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION
	                        | SWT.YES );
	                messagebox.setText("提示");
	                messagebox.setMessage("您设置的出入金参数不正确，请重新设置！") ;
	                int message = messagebox.open();
	                e.doit = message == SWT.YES;
					return;
				}
			    
			}
		});
		button.setBounds(10, 161, 80, 27);
		button.setText("确      认");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setBounds(182, 161, 80, 27);
		button_1.setText("取      消");
		
		

	}
	
	private int checkCapital(String userName,String inCapital,String outCapital,String ownCapital,String capitalRetain){
		//如果是入金 则CHECK通过（不用CHECK）
		double tempdbin = 0; 
		double tempdbout = 0; 
		double tempOwnCapital = 0;
		double tempCapitalRetain = 0;
		if( inCapital.equals("")) {
			tempdbin = 0;
		} else {
			tempdbin = Double.valueOf(inCapital);
		}
		if( outCapital.equals("")) {
			tempdbout = 0;
		} else {
			tempdbout = Double.valueOf(outCapital);
		}
		
		
		if (tempdbout > 0 && tempdbin > 0) {
			//入金和出金都填写
			return 0;
		}
		
		if (tempdbin > 0) {
			//入金填写 CHECK通过
			return 1;
		}
		tempOwnCapital = Double.valueOf(ownCapital);
		tempCapitalRetain = Double.valueOf(capitalRetain);
		if (tempdbout > 0) {
			//出金 CHECK 和 盘中出金质押金（可用小于质押金则不能出金）对比
			if (tempOwnCapital > tempCapitalRetain){
				return 1;
			} else {
				return 0;
			}
			
		}
		return 0;
	}
	
	private void setCapital(String userName,String inCapital,String outCapital,String userCapitalRate)
	{
		double hostCapital1 = 0;
		double tmpAdd = 0;
		CapitalRateDetail capitalRateDetail = new CapitalRateDetail();
		CapitalRate capitalRate = new CapitalRate();
		//入金
		if( Double.valueOf(inCapital) > 0 ) {
			hostCapital1 = Double.valueOf(inCapital) * Double.valueOf(userCapitalRate);
			capitalRateDetail.setUserName(userName);
			capitalRateDetail.setInsertTime(new Date());
			capitalRateDetail.setHostCapital(new BigDecimal( hostCapital1));
			capitalRateDetail.setUserCapital(new BigDecimal(inCapital));
			tmpAdd = Double.valueOf(inCapital) + hostCapital1;
			//配资表明细 插入数据
			try {
				capitalRateDetailService.saveOrder(capitalRateDetail);
			} catch (FutureException e) {

				logger.debug("配资表明细 入金插入数据失败！");
			}
			
			//配资表总览更新数据
			capitalRate.setUserName(userName);
			capitalRate.setHostCapital(new BigDecimal( hostCapital1));
			capitalRate.setUpdateTime(new Date());
			capitalRate.setUserCapital(new BigDecimal(inCapital));
			try {
				capitalRateService.addCapitalRate(capitalRate);
			} catch (FutureException e) {

				logger.debug("配资表总览 入金更新数据失败！");
			}
			
			//推送COREAPP
			try {
				
				StringBuffer sb = new StringBuffer();
				sb.append("risk");
				sb.append("|"+userName);
				sb.append("|CRJ");
				sb.append("|");
				sb.append(tmpAdd);
				
				manageView.inoutCapital(sb.toString());
			} catch (FutureException e) {
				MessageBox messagebox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES );
                messagebox.setText("提示");
                messagebox.setMessage(e.getMessage()) ;
                messagebox.open();
				return;
			}
		}
		
		//出金
		if( Double.valueOf(outCapital) > 0 ) {
			double tmpinCapital = 0;
			hostCapital1 = Double.valueOf(outCapital) * Double.valueOf(userCapitalRate) * -1;
			capitalRateDetail.setUserName(userName);
			capitalRateDetail.setInsertTime(new Date());
			capitalRateDetail.setHostCapital(new BigDecimal( hostCapital1));
			tmpinCapital = Double.valueOf(inCapital) * -1;
			capitalRateDetail.setUserCapital(new BigDecimal(tmpinCapital));
			tmpAdd = Double.valueOf(tmpinCapital) + hostCapital1;
			//表明细 插入数据
			try {
				capitalRateDetailService.saveOrder(capitalRateDetail);
			} catch (FutureException e) {
				
				logger.debug("配资表明细 出金插入数据失败！");
			}
			
			//配资表总览更新数据
			capitalRate.setUserName(userName);
			capitalRate.setHostCapital(new BigDecimal( hostCapital1));
			capitalRate.setUpdateTime(new Date());
			capitalRate.setUserCapital(new BigDecimal(inCapital));
			try {
				capitalRateService.distractCapitalRate(capitalRate);
			} catch (FutureException e) {
				
				logger.debug("配资表总览 出金更新数据失败！");
			}
			
			//推送COREAPP
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("risk");
				sb.append("|"+userName);
				sb.append("|CRJ");
				sb.append("|");
				sb.append(tmpAdd);
				manageView.inoutCapital(sb.toString());
			} catch (FutureException e) {
				MessageBox messagebox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES );
                messagebox.setText("提示");
                messagebox.setMessage(e.getMessage()) ;
                messagebox.open();
				return;
			}
		}
		
	}

}
