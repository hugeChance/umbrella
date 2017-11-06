package com.bohai.subAccount.swt.riskfront;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
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
import org.springframework.util.StringUtils;

import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.entity.CapitalRateDetail;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CapitalRateDetailService;
import com.bohai.subAccount.service.CapitalRateService;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.swt.admin.oldAdminViewMain;
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
	
	private RiskFrontView manageView;
	
	private CapitalRateService capitalRateService;
	private CapitalRateDetailService capitalRateDetailService;
	private CapitalRate capitalRate;
	
	private UserInfoMapper userInfoMapper;
	
	private UserCapitalRateVO userCapitalRateVO;
	static Logger logger = Logger.getLogger(oldAdminViewMain.class);
	private Text text_2;
	private Text text_3;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RiskCapitalRateDialog(Shell parent, int style,TableItem item,RiskFrontView manageView) {
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
		capitalRateService = (CapitalRateService) SpringContextUtil.getBean("CapitalRateService");
		capitalRateDetailService = (CapitalRateDetailService) SpringContextUtil.getBean("CapitalRateDetailService");
		
		userInfoMapper = (UserInfoMapper) SpringContextUtil.getBean("userInfoMapper");
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
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(10, 44, 61, 17);
		label_4.setText("自有资金：");
		
		//自有资金=动态权益-配资资金1
		double doublei = Double.valueOf(item.getText(1) == null?"0":item.getText(1));
		doublei = doublei - capitalRate.getHostCapital1().doubleValue();
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(77, 44, 96, 17);
		label_5.setText(String.valueOf(doublei));
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(182, 10, 61, 17);
		lblNewLabel.setText("现有调配：");
		
		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setBounds(249, 10, 96, 17);
		label_6.setText(capitalRate.getHostCapital1().toString());
		
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
		
		Label label_9 = new Label(shell, SWT.NONE);
		label_9.setBounds(182, 120, 61, 17);
		label_9.setText("出金调配：");
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				returnCheckFlg = 0;
				double doublej = Double.valueOf(item == null?"0":item.getText(1));
				doublej = doublej - capitalRate.getHostCapital1().doubleValue();
				
				if(StringUtils.isEmpty(text.getText())) {
					text.setText("0");
				}
				if(StringUtils.isEmpty(text_1.getText())) {
					text_1.setText("0");
				}
				if(StringUtils.isEmpty(text_2.getText())) {
					text_2.setText("0");
				}
				if(StringUtils.isEmpty(text_3.getText())) {
					text_3.setText("0");
				}
				
				// 出入金CHECK
				if (item !=null && checkCapital(item.getText(0),text.getText(),text_1.getText(),String.valueOf(doublej),"0") == 1){
					// 出入金操作
					// 现在质押资金设了常量为0
//					setCapital(item.getText(0),text.getText(),text_1.getText(),capitalRate.getUserCapitalRate().toString(),text_2.getText(),text_3.getText());
					setCapital(item.getText(0),text.getText(),text_1.getText(),"0",text_2.getText(),text_3.getText());
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
		button.setBounds(10, 178, 80, 27);
		button.setText("确      认");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setBounds(185, 178, 80, 27);
		button_1.setText("取      消");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(249, 80, 96, 23);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(249, 120, 96, 23);
		
		

	}
	
	private int checkCapital(String userName,String inCapital,String outCapital,String ownCapital,String capitalRetain){
		//如果是入金 则CHECK通过（不用CHECK）
		double tempdbin = 0; 
		double tempdbout = 0; 
		double tempOwnCapital = 0;
		double tempCapitalRetain = 0;
		if( inCapital == null || inCapital.equals("")) {
			tempdbin = 0;
		} else {
			tempdbin = Double.valueOf(inCapital);
		}
		if( outCapital == null || outCapital.equals("")) {
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
	
	private void setCapital(String userName,String inCapital,String outCapital,String userCapitalRate,String hostInCapital,String hostOutCapital)
	{
		
		double tmpAdd = 0;
		CapitalRateDetail capitalRateDetail = new CapitalRateDetail();
		CapitalRate capitalRate = new CapitalRate();
		//入金
		
		if( (!inCapital.equals("")) && Double.valueOf(inCapital) > 0 ) {
			
			capitalRateDetail.setUserName(userName);
//			capitalRateDetail.setInsertTime(new Date());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd HHmmss");  
			java.util.Date date=new java.util.Date();  
			String str=sdf.format(date); 
			capitalRateDetail.setInsertTime(str);
			capitalRateDetail.setHostCapital(new BigDecimal( hostInCapital));
			capitalRateDetail.setUserCapital(new BigDecimal(inCapital));
			tmpAdd = Double.valueOf(inCapital) + Double.valueOf(hostInCapital);
			capitalRateDetail.setUserCapitalRate(new BigDecimal("0"));
			//配资表明细 插入数据
			try {
				capitalRateDetailService.saveCapitalRateDetail(capitalRateDetail);
			} catch (FutureException e) {

				logger.debug("配资表明细 入金插入数据失败！");
			}
			
			//配资表总览更新数据
			capitalRate.setUserName(userName);
			capitalRate.setHostCapital1(new BigDecimal( hostInCapital));
			capitalRate.setUpdateTime(str);
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
			
			
			
			try {
				String forceLimit = userInfoMapper.getUserCloseLimitByUserName(userName);
				if(!StringUtils.isEmpty(forceLimit)){
					UserInfo userInfo = new UserInfo();
					userInfo.setUserName(userName);
					userInfo.setUserName(userName);
					BigDecimal newLimit = new BigDecimal(forceLimit).add(new BigDecimal(inCapital));
					userInfo.setForceLimit(newLimit.toString());
					userInfoMapper.updateUserForceClose(userInfo);
					item.setText(6, newLimit.toString());
				}
			} catch (Exception e) {
				logger.error("入金更新平仓金额失败",e);
				MessageBox messagebox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES );
                messagebox.setText("错误");
                messagebox.setMessage("入金更新平仓金额失败"+e.getMessage()) ;
                messagebox.open();
				return;
			}
			
			
			
		}
		
		//出金
		if( (!outCapital.equals("")) && Double.valueOf(outCapital) > 0 ) {
			double tmpOutCapital = 0;
			
			capitalRateDetail.setUserName(userName);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd HHmmss");  
			java.util.Date date=new java.util.Date();  
			String str=sdf.format(date); 
			capitalRateDetail.setInsertTime(str);
			capitalRateDetail.setHostCapital(new BigDecimal( hostOutCapital).multiply(new BigDecimal("-1")));
			tmpOutCapital = Double.valueOf(outCapital) * -1;
			capitalRateDetail.setUserCapital(new BigDecimal(tmpOutCapital));
			tmpAdd = Double.valueOf(tmpOutCapital) + Double.valueOf(hostOutCapital) * -1;
			//表明细 插入数据
			try {
				capitalRateDetailService.saveCapitalRateDetail(capitalRateDetail);
			} catch (FutureException e) {
				
				logger.debug("配资表明细 出金插入数据失败！");
				MessageBox messagebox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES );
                messagebox.setText("错误");
                messagebox.setMessage(e.getMessage()) ;
                messagebox.open();
				return;
			}
			
			//配资表总览更新数据
			capitalRate.setUserName(userName);
			capitalRate.setHostCapital1(new BigDecimal( hostOutCapital));
			capitalRate.setUpdateTime(str);
			capitalRate.setUserCapital(new BigDecimal(outCapital));
			try {
				capitalRateService.distractCapitalRate(capitalRate);
			} catch (FutureException e) {
				
				logger.debug("配资表总览 出金更新数据失败！");
				MessageBox messagebox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES );
                messagebox.setText("错误");
                messagebox.setMessage(e.getMessage()) ;
                messagebox.open();
				return;
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
			
			try {
				String forceLimit = userInfoMapper.getUserCloseLimitByUserName(userName);
				if(!StringUtils.isEmpty(forceLimit)){
					UserInfo userInfo = new UserInfo();
					userInfo.setUserName(userName);
					userInfo.setUserName(userName);
					BigDecimal newLimit = new BigDecimal(forceLimit).subtract(new BigDecimal(inCapital));
					userInfo.setForceLimit(newLimit.toString());
					userInfoMapper.updateUserForceClose(userInfo);
					item.setText(6, newLimit.toString());
				}
			} catch (Exception e) {
				logger.error("出金更新平仓金额失败",e);
				MessageBox messagebox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES );
                messagebox.setText("错误");
                messagebox.setMessage("出金更新平仓金额失败"+e.getMessage()) ;
                messagebox.open();
				return;
			}
		}
		
	}

}
