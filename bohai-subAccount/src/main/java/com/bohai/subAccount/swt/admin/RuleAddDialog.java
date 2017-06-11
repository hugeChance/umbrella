package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserContractTradeRule;

public class RuleAddDialog extends Dialog {

	static Logger logger = Logger.getLogger(RuleAddDialog.class);

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
	
	//private AdminViewMain main;
	private TreeItem treeItem;
	private Text contract;
	private Text tickSize;
	private MainForm mainForm;
	
	private TableItem tableItem;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RuleAddDialog(Shell parent, int style, MainForm mainForm, TreeItem treeItem) {
		super(parent, style);
		setText("添加合约");
		//this.main = main;
		this.treeItem = treeItem;
		this.mainForm = mainForm;
	}
	
   public RuleAddDialog(Shell parent, int style, MainForm mainForm, TableItem tableItem) {
        super(parent, style);
        setText("添加合约");
        //this.main = main;
        this.tableItem = tableItem;
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
		groupName.setBounds(118, 31, 129, 23);
		//获取用户组
		if(treeItem != null){
			if(treeItem.getParentItem() == null){
				groupName.setText(((GroupInfo)treeItem.getData()).getGroupName());
			}else {
				groupName.setText(((GroupInfo)treeItem.getParentItem().getData()).getGroupName());
			}
		}
		
		if(tableItem != null){
		    
		    Map<String, Object> map = (Map<String, Object>) tableItem.getData();
		    groupName.setText((String) map.get("USER_NAME"));
		}
		
		Label contractLabel = new Label(shell, SWT.NONE);
		contractLabel.setAlignment(SWT.RIGHT);
		contractLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		contractLabel.setBounds(269, 31, 73, 23);
		contractLabel.setText("合约：");
		
		contract = new Text(shell, SWT.BORDER);
		contract.setBounds(348, 32, 84, 23);
		
		/*Label cancelLabel = new Label(shell, SWT.NONE);
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
		openCount.setBounds(111, 123, 84, 23);*/
		
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
		
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.setBounds(71, 362, 95, 27);
		button.setText("添加");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UserContract userContract = new UserContract();
				userContract.setContractNo(contract.getText());
				
				if(treeItem != null){
				    Object o = treeItem.getData();
				    if(o instanceof UserInfo){
				        UserInfo user = (UserInfo) o;
				        userContract.setUserNo( user.getUserNo());
				    }
				}
				
				if(tableItem != null){
				    Map<String, Object> map = (Map<String, Object>) tableItem.getData();
				    userContract.setUserNo((String) map.get("USER_NO"));
				}
				
				if(contract.getText() == null && contract.getText().trim().equals("")){
				    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("合约编号不能为空");
                    box.setText("错误");
                    box.open();
                    return;
				}
				
				if(StringUtils.isEmpty(tickSize.getText())){
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("最小跳动单位必输");
					box.setText("错误");
					box.open();
					return;
				}
				
                if(StringUtils.isEmpty(unit.getText())){
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("合约单位必输");
                    box.setText("错误");
                    box.open();
                    return;
                }
                
                if(StringUtils.isEmpty(margin.getText())){
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("保证金必输");
                    box.setText("错误");
                    box.open();
                    return;
                }
				
				try {
                    userContract.setOpenCharge(StringUtils.isEmpty(openCharge.getText())?new BigDecimal("0"):new BigDecimal(openCharge.getText()));
                    userContract.setOpenChargeRate(StringUtils.isEmpty(openChargeRate.getText())?new BigDecimal("0"):new BigDecimal(openChargeRate.getText()));
                    userContract.setCloseCurrCharge(StringUtils.isEmpty(closeCurrCharge.getText())?new BigDecimal("0"):new BigDecimal(closeCurrCharge.getText()));
                    userContract.setCloseCurrChargeRate(StringUtils.isEmpty(closeCurrChargeRate.getText())?new BigDecimal("0"):new BigDecimal(closeCurrChargeRate.getText()));
                    userContract.setMargin(StringUtils.isEmpty(margin.getText())?null:new BigDecimal(margin.getText()));
                    userContract.setContractUnit(StringUtils.isEmpty(unit.getText())?null:Integer.parseInt(unit.getText()));
                    userContract.setTickSize(new BigDecimal(tickSize.getText()));
                } catch (NumberFormatException e2) {
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("数据格式出错"+e2.getMessage());
                    box.setText("错误");
                    box.open();
                    return;
                }
				UserContractService userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
				Integer userContractId = 0;
				try {
				    userContractId = userContractService.saveUserContract(userContract);
					
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("错误");
					box.open();
					return;
				}
				
				/*TradeRule tradeRule = new TradeRule();
				//合约ID
				tradeRule.setContractId(userContractId.toString());
				tradeRule.setContract(contract.getText());
				
				if(treeItem.getParent() == null){
					GroupInfo group = (GroupInfo) treeItem.getData();
					tradeRule.setGroupId(group.getId());
				}else {
					GroupInfo group = (GroupInfo) treeItem.getParentItem().getData();
					tradeRule.setGroupId(group.getId());
				}
				
				tradeRule.setEntrustCount(StringUtils.isEmpty(entrust.getText())?0:Integer.parseInt(entrust.getText()));
				tradeRule.setCancelCount(StringUtils.isEmpty(cancelCount.getText())?0:Integer.parseInt(cancelCount.getText()));
				tradeRule.setOpenCount(StringUtils.isEmpty(openCount.getText())?0:Integer.parseInt(openCount.getText()));
				tradeRule.setCreateTime(new Date());
				TradeRuleService ruleService = (TradeRuleService) SpringContextUtil.getBean("tradeRuleService");
				try {
					ruleService.saveOrUpdateTradeRule(tradeRule);
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("保存成功");
					box.setText("提示");
					box.open();
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("错误");
					box.open();
					return;
				}
				logger.debug("保存交易规则成功，刷新主页面表格");*/
				if(treeItem != null){
				    mainForm.refreshContract(treeItem);
				}
				if(tableItem != null){
				    Map<String, Object> map = (Map<String, Object>) tableItem.getData();
				    mainForm.refreshUserContract((String) map.get("USER_NO"));
				}
				shell.close();
			}
		});
		
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.setBounds(348, 362, 95, 27);
		cancel.setText("取消");
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("最小跳动单位：");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBounds(32, 318, 116, 23);
		
		tickSize = new Text(shell, SWT.BORDER);
		tickSize.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tickSize.setBounds(154, 315, 84, 23);
		
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});

	}
}
