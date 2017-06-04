package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
import org.springframework.util.StringUtils;

import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CloseRuleService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.utils.SpringContextUtil;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class CloseRuleAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text contractText;
	private Text tickSize;
	private Text hop;
	private Text forceCloseRate;
	private Label userNameLab;
	
	private AdminViewMain main;
	private TreeItem treeItem;
	
	private MainForm mainForm;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CloseRuleAddDialog(Shell parent, int style, AdminViewMain main, TreeItem treeItem) {
		super(parent, style);
		setText("添加开平仓规则");
		this.main = main;
		this.treeItem = treeItem;
	}
	
	   /**
	    * @wbp.parser.constructor
	    */
	   public CloseRuleAddDialog(Shell parent, int style, MainForm mainForm, TreeItem treeItem) {
	        super(parent, style);
	        setText("添加开平仓规则");
	        this.mainForm = mainForm;
	        this.treeItem = treeItem;
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
		shell.setSize(304, 320);
		shell.setText(getText());
		
		Label userNameLabel = new Label(shell, SWT.NONE);
        userNameLabel.setText("用户名：");
        userNameLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        userNameLabel.setAlignment(SWT.RIGHT);
        userNameLabel.setBounds(24, 20, 112, 23);
        
        userNameLab = new Label(shell, SWT.NONE);
        userNameLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        userNameLab.setBounds(166, 20, 90, 23);
        //获取用户名
        if(treeItem != null){
            userNameLab.setText(((UserInfo)treeItem.getData()).getUserName());
        }
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_2.setText("合约：");
		label_2.setBounds(24, 60, 112, 23);
		
		contractText = new Text(shell, SWT.BORDER);
		contractText.addFocusListener(new FocusAdapter() {
		    @Override
		    public void focusLost(FocusEvent e) {
		        
		        String contractNo = contractText.getText();
		        if(!StringUtils.isEmpty(contractNo)){
		            UserContractService contractService = (UserContractService) SpringContextUtil.getBean("userContractService");
		            try {
                        UserContract userContract = contractService.queryUserContractByUserNameAndContract(userNameLab.getText(), contractNo);
                        if(userContract == null){
                            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                            box.setMessage("请先添加合约信息");
                            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                            box.open();
                            return;
                        }
                        tickSize.setText(userContract.getTickSize().toString());
                        
                    } catch (FutureException e1) {
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                        box.setMessage(e1.getMessage());
                        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                        box.open();
                    }
		        }
		    }
		});
		contractText.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		contractText.setBounds(166, 57, 90, 27);
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_3.setText("最小变动单位：");
		label_3.setBounds(10, 99, 126, 23);
		
		tickSize = new Text(shell, SWT.BORDER);
		tickSize.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		tickSize.setBounds(166, 96, 90, 27);
		tickSize.setEnabled(false);
		
		hop = new Text(shell, SWT.BORDER);
		hop.setBounds(166, 142, 90, 26);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setAlignment(SWT.RIGHT);
		label_4.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_4.setText("跳数：");
		label_4.setBounds(24, 145, 112, 23);
		
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_5.setAlignment(SWT.RIGHT);
		label_5.setText("强平比例：");
		label_5.setBounds(24, 187, 112, 23);
		
		forceCloseRate = new Text(shell, SWT.BORDER);
		forceCloseRate.setBounds(166, 184, 90, 27);
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.setBounds(38, 236, 80, 27);
		button.setText("添加");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    
			    if(StringUtils.isEmpty(userNameLab.getText())){
			        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("用户名不能为空");
                    box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                    box.open();
                    return;
			    }
			    
			    if(StringUtils.isEmpty(tickSize.getText())){
			        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
			        box.setMessage("每跳变动单位");
			        box.setText(CommonConstant.MESSAGE_BOX_ERROR);
			        box.open();
			        return;
			    }
			    
				CloseRule closeRule = new CloseRule();
				closeRule.setForceCloseRate(StringUtils.isEmpty(forceCloseRate.getText())? null : new BigDecimal(forceCloseRate.getText()));
				closeRule.setContractNo(contractText.getText());
				closeRule.setTickSize(StringUtils.isEmpty(tickSize.getText()) ? null : new BigDecimal(tickSize.getText()));
				closeRule.setHop(StringUtils.isEmpty(hop.getText()) ? null : Integer.parseInt(hop.getText()));
				closeRule.setUserName(userNameLab.getText());
				
				CloseRuleService closeRuleService = (CloseRuleService) SpringContextUtil.getBean("closeRuleService");
				try {
					closeRuleService.saveCloseRule(closeRule);
					
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("保存成功");
					box.setText("提示");
					box.open();
					
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText(CommonConstant.MESSAGE_BOX_ERROR);
					box.open();
					return;
				}
				
				if(main != null){
				    main.refreshTradeRule(treeItem);
				}
				if(mainForm != null){
				    //刷新风控平仓信息
				    mainForm.refreshRiskClose(treeItem);
				}
				shell.close();
			}
		});
		
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.setBounds(176, 236, 80, 27);
		cancel.setText("取消");
		
		
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

}
