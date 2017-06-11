package com.bohai.subAccount.swt.admin;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserContractTradeRule;

public class TradeRuleEditDialog extends Dialog {
	
	static Logger logger = Logger.getLogger(TradeRuleEditDialog.class);

	protected Object result;
	protected Shell shell;
	private Text cancelCount;
	private Text entrust;
	private Text openCount;
	private Text openCharge;
	private Text margin;
	private Text unit;
	private Text closeCurrCharge;
	private Text openChargeRate;
	private Text closeCurrChargeRate;
	
	private TableItem selected;
	//private AdminViewMain main;
	private TreeItem treeItem;
	private Text tickSize;
	
	private MainForm mainForm;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TradeRuleEditDialog(Shell parent, int style, TableItem selected, MainForm mainForm, TreeItem treeItem) {
		super(parent, style);
		setText("修改组规则");
		this.selected = selected;
		//this.main = main;
		this.treeItem = treeItem;
		this.mainForm = mainForm;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
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
		shell.setSize(521, 447);
		shell.setText(getText());
		
		Label groupLabel = new Label(shell, SWT.NONE);
		groupLabel.setAlignment(SWT.RIGHT);
		groupLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupLabel.setBounds(32, 31, 73, 23);
		groupLabel.setText("用户名：");
		
		Label groupName = new Label(shell, SWT.NONE);
		groupName.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupName.setBounds(118, 31, 73, 23);
		
		Label contractLabel = new Label(shell, SWT.NONE);
		contractLabel.setAlignment(SWT.RIGHT);
		contractLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		contractLabel.setBounds(269, 31, 73, 23);
		contractLabel.setText("合约：");
		
		Label contract = new Label(shell, SWT.NONE);
		contract.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		contract.setBounds(348, 31, 95, 23);
		
		Label cancelLabel = new Label(shell, SWT.NONE);
		cancelLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancelLabel.setAlignment(SWT.RIGHT);
		cancelLabel.setBounds(32, 76, 73, 23);
		cancelLabel.setText("撤单数：");
		
		cancelCount = new Text(shell, SWT.BORDER);
		cancelCount.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancelCount.setBounds(111, 76, 84, 23);
		
		Label entrustLabel = new Label(shell, SWT.NONE);
		entrustLabel.setAlignment(SWT.RIGHT);
		entrustLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		entrustLabel.setBounds(269, 76, 73, 23);
		entrustLabel.setText("委托数：");
		
		entrust = new Text(shell, SWT.BORDER);
		entrust.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		entrust.setBounds(348, 76, 84, 23);
		
		Label openLabel = new Label(shell, SWT.NONE);
		openLabel.setAlignment(SWT.RIGHT);
		openLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openLabel.setBounds(32, 123, 73, 23);
		openLabel.setText("开仓数：");
		
		openCount = new Text(shell, SWT.BORDER);
		openCount.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openCount.setBounds(111, 123, 84, 23);
		
		Label cutoff = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		cutoff.setBounds(0, 161, 515, 2);
		
		Label openChargeLabel = new Label(shell, SWT.NONE);
		openChargeLabel.setText("开仓手续费：");
		openChargeLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openChargeLabel.setAlignment(SWT.RIGHT);
		openChargeLabel.setBounds(21, 185, 105, 23);
		
		openCharge = new Text(shell, SWT.BORDER);
		openCharge.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openCharge.setBounds(132, 185, 84, 23);
		
		Label openChargeRateLabel = new Label(shell, SWT.NONE);
		openChargeRateLabel.setText("开仓手续费%：");
		openChargeRateLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openChargeRateLabel.setAlignment(SWT.RIGHT);
		openChargeRateLabel.setBounds(245, 185, 129, 23);
		
		openChargeRate = new Text(shell, SWT.BORDER);
		openChargeRate.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openChargeRate.setBounds(386, 185, 84, 23);
		
		Label closeCurrLabel = new Label(shell, SWT.NONE);
		closeCurrLabel.setText("平今手续费：");
		closeCurrLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		closeCurrLabel.setAlignment(SWT.RIGHT);
		closeCurrLabel.setBounds(21, 229, 105, 23);
		
		closeCurrCharge = new Text(shell, SWT.BORDER);
		closeCurrCharge.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		closeCurrCharge.setBounds(132, 229, 84, 23);
		
		Label closeCurrChargeRateLabel = new Label(shell, SWT.NONE);
		closeCurrChargeRateLabel.setText("平今手续费%：");
		closeCurrChargeRateLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		closeCurrChargeRateLabel.setAlignment(SWT.RIGHT);
		closeCurrChargeRateLabel.setBounds(257, 229, 116, 23);
		
		closeCurrChargeRate = new Text(shell, SWT.BORDER);
		closeCurrChargeRate.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		closeCurrChargeRate.setBounds(385, 229, 84, 23);
		
		Label marginLabel = new Label(shell, SWT.NONE);
		marginLabel.setText("保证金比例：");
		marginLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		marginLabel.setAlignment(SWT.RIGHT);
		marginLabel.setBounds(21, 275, 105, 23);
		
		margin = new Text(shell, SWT.BORDER);
		margin.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		margin.setBounds(132, 275, 84, 23);
		
		Label unitLabel = new Label(shell, SWT.NONE);
		unitLabel.setText("合约单位：");
		unitLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		unitLabel.setAlignment(SWT.RIGHT);
		unitLabel.setBounds(273, 275, 95, 23);
		
		unit = new Text(shell, SWT.BORDER);
		unit.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		unit.setBounds(386, 275, 84, 23);
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("最小跳动单位：");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBounds(21, 319, 116, 23);
		
		tickSize = new Text(shell, SWT.BORDER);
		tickSize.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tickSize.setBounds(143, 319, 84, 23);
		
		if(selected != null){
			UserContractTradeRule rule = (UserContractTradeRule) selected.getData();
			
			groupName.setText(rule.getUserName());//用户名
			contract.setText(rule.getContractNo());//合约
			cancelCount.setText(StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());//撤单数
			entrust.setText(StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());//委托数
			openCount.setText(StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());//开仓数
			
			openCharge.setText(StringUtils.isEmpty(rule.getOpenCharge())?"":rule.getOpenCharge().toString());//开仓手续费
			openChargeRate.setText(StringUtils.isEmpty(rule.getOpenChargeRate())?"":rule.getOpenChargeRate().toString());//开仓手续费比例
			closeCurrCharge.setText(StringUtils.isEmpty(rule.getCloseCurrCharge())?"":rule.getCloseCurrCharge().toString());//平今手续费
			closeCurrChargeRate.setText(StringUtils.isEmpty(rule.getCloseCurrChargeRate())?"":rule.getCloseCurrChargeRate().toString());//平今手续费比例
			margin.setText(StringUtils.isEmpty(rule.getMargin())?"":rule.getMargin().toString());//保证金比例
			unit.setText(StringUtils.isEmpty(rule.getContractUnit())?"":rule.getContractUnit().toString());//合约单位
			tickSize.setText(StringUtils.isEmpty(rule.getTickSize())?"":rule.getTickSize().toString());
		}
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.setBounds(70, 361, 95, 27);
		button.setText("更新");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(StringUtils.isEmpty(tickSize.getText())){
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("最小跳动单位必输");
					box.setText("错误");
					box.open();
					return;
				}
				
				UserContractTradeRule rule = (UserContractTradeRule) selected.getData();
				UserContract userContract = new UserContract();
				userContract.setContractNo(contract.getText());
				userContract.setId(rule.getId());
				userContract.setUserNo( rule.getUserNo());
				userContract.setOpenCharge(StringUtils.isEmpty(openCharge.getText())?new BigDecimal("0"):new BigDecimal(openCharge.getText()));
				userContract.setOpenChargeRate(StringUtils.isEmpty(openChargeRate.getText())?new BigDecimal("0"):new BigDecimal(openChargeRate.getText()));
				userContract.setCloseCurrCharge(StringUtils.isEmpty(closeCurrCharge.getText())?new BigDecimal("0"):new BigDecimal(closeCurrCharge.getText()));
				userContract.setCloseCurrChargeRate(StringUtils.isEmpty(closeCurrChargeRate.getText())?new BigDecimal("0"):new BigDecimal(closeCurrChargeRate.getText()));
				userContract.setMargin(StringUtils.isEmpty(margin.getText())?null:new BigDecimal(margin.getText()));
				userContract.setContractUnit(StringUtils.isEmpty(unit.getText())?null:Integer.parseInt(unit.getText()));
				userContract.setTickSize(new BigDecimal(tickSize.getText()));
				UserContractService userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
				try {
					userContractService.updateUserContract(userContract);
					
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("警告");
					box.open();
				}
				
				/*TradeRule tradeRule = new TradeRule();
				tradeRule.setId(rule.getTradeRuleId());
				tradeRule.setContract(rule.getContractNo());
				tradeRule.setGroupId(rule.getGroupId());
				tradeRule.setEntrustCount(StringUtils.isEmpty(entrust.getText())?0:Integer.parseInt(entrust.getText()));
				tradeRule.setCancelCount(StringUtils.isEmpty(cancelCount.getText())?0:Integer.parseInt(cancelCount.getText()));
				tradeRule.setOpenCount(StringUtils.isEmpty(openCount.getText())?0:Integer.parseInt(openCount.getText()));
				tradeRule.setCreateTime(new Date());
				TradeRuleService ruleService = (TradeRuleService) SpringContextUtil.getBean("tradeRuleService");
				try {
					ruleService.saveOrUpdateTradeRule(tradeRule);
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("更新成功");
					box.setText("提示");
					box.open();
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("警告");
					box.open();
				}
				logger.debug("更新交易规则成功，刷新主页面表格");*/
				if(treeItem != null){
				    mainForm.refreshContract(treeItem);
				}else {
                    
				    mainForm.refreshUserContract(rule.getUserNo());
                }
				
				shell.close();
			}
		});
		
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.setBounds(348, 361, 95, 27);
		cancel.setText("取消");
		
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});

	}
}
