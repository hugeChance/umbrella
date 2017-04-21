package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hraink.futures.jctp.util.StringUtil;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CloseRuleService;
import com.bohai.subAccount.utils.SpringContextUtil;

public class CloseRuleEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text contractText;
	private Text tickSize;
	private Text hop;
	private Text forceCloseRate;
	
	private AdminViewMain main;
	private TreeItem treeItem;
	private TableItem tableItem;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CloseRuleEditDialog(Shell parent, int style, AdminViewMain main, TreeItem treeItem, TableItem tableItem) {
		super(parent, style);
		setText("更新平仓规则");
		this.main = main;
		this.treeItem = treeItem;
		this.tableItem = tableItem;
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
		shell.setSize(304, 299);
		shell.setText(getText());
		
		Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBounds(24, 10, 112, 23);
		label.setText("用户组：");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_1.setBounds(166, 10, 73, 23);
		//获取用户组
		if(treeItem != null){
			if(treeItem.getParentItem() == null){
				label_1.setText(((GroupInfo)treeItem.getData()).getGroupName());
			}else {
				label_1.setText(((GroupInfo)treeItem.getParentItem().getData()).getGroupName());
			}
		}
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_2.setText("合约：");
		label_2.setBounds(24, 48, 112, 23);
		
		contractText = new Text(shell, SWT.BORDER);
		contractText.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		contractText.setBounds(166, 41, 90, 27);
		contractText.setEnabled(false);
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_3.setText("最小变动单位：");
		label_3.setBounds(10, 87, 126, 23);
		
		tickSize = new Text(shell, SWT.BORDER);
		tickSize.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tickSize.setBounds(166, 84, 90, 27);
		tickSize.setEnabled(false);
		
		hop = new Text(shell, SWT.BORDER);
		hop.setBounds(166, 130, 90, 26);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setAlignment(SWT.RIGHT);
		label_4.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_4.setText("跳数：");
		label_4.setBounds(24, 133, 112, 23);
		
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_5.setAlignment(SWT.RIGHT);
		label_5.setText("强平比例：");
		label_5.setBounds(24, 175, 112, 23);
		
		forceCloseRate = new Text(shell, SWT.BORDER);
		forceCloseRate.setBounds(166, 172, 90, 27);
		
		CloseRule rule = (CloseRule) tableItem.getData();
		//合约编号
		contractText.setText(rule.getContractNo());
		//最小跳动单位
		tickSize.setText(StringUtils.isEmpty(rule.getTickSize())?"":rule.getTickSize().toString());
		//条数
		hop.setText(StringUtils.isEmpty(rule.getHop())?"":rule.getHop().toString());
		//强平比例
		forceCloseRate.setText(StringUtils.isEmpty(rule.getForceCloseRate())?"":rule.getForceCloseRate().toString());
		
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.setBounds(38, 224, 80, 27);
		button.setText("更新");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//CloseRule closeRule = new CloseRule();
				rule.setForceCloseRate(StringUtils.isEmpty(forceCloseRate.getText())? null : new BigDecimal(forceCloseRate.getText()));
				rule.setContractNo(contractText.getText());
				rule.setTickSize(StringUtils.isEmpty(tickSize.getText()) ? null : new BigDecimal(tickSize.getText()));
				rule.setHop(StringUtils.isEmpty(hop.getText()) ? null : Integer.parseInt(hop.getText()));
				
				CloseRuleService closeRuleService = (CloseRuleService) SpringContextUtil.getBean("closeRuleService");
				try {
					closeRuleService.updateCloseRule(rule);
					
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("更新成功");
					box.setText("提示");
					box.open();
					
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText(CommonConstant.MESSAGE_BOX_ERROR);
					box.open();
					return;
				}
				
				main.refreshTradeRule(treeItem);
				shell.close();
			}
		});
		
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.setBounds(176, 224, 80, 27);
		cancel.setText("取消");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

}
