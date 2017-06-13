package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bohai.subAccount.dao.CapitalRateMapper;
import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.utils.GenerateCodeUtil;
import com.bohai.subAccount.utils.SpringContextUtil;

public class SubAccountAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text username;
	private Text passwd;
	private Text passwdConfirm;
	//private Text instrumentId;
	private Text limit;
	
	private GroupInfo groupInfo; 
	private AdminViewMain adminView;
	private MainForm mainForm;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SubAccountAddDialog(Shell parent, int style, GroupInfo groupInfo, AdminViewMain adminView) {
		super(parent, style);
		setText("添加新用户");
		this.groupInfo = groupInfo;
		this.adminView = adminView;
	}
	
	   /**
	    * @wbp.parser.constructor
	    */
	   public SubAccountAddDialog(Shell parent, int style, GroupInfo groupInfo, MainForm mainForm) {
	        super(parent, style);
	        setText("添加新用户");
	        this.groupInfo = groupInfo;
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
		shell.setSize(314, 384);
		shell.setText(getText());
		shell.setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		
		Label usernameLabel = new Label(composite, SWT.NONE);
		usernameLabel.setAlignment(SWT.RIGHT);
		usernameLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		usernameLabel.setBounds(46, 37, 80, 23);
		usernameLabel.setText("用户名：");
		
		username = new Text(composite, SWT.BORDER);
		username.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		username.setBounds(140, 37, 113, 23);
		
		Label passwdLabel = new Label(composite, SWT.NONE);
		passwdLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwdLabel.setAlignment(SWT.RIGHT);
		passwdLabel.setBounds(46, 74, 80, 23);
		passwdLabel.setText("密码：");
		
		passwd = new Text(composite, SWT.BORDER);
		passwd.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwd.setBounds(140, 75, 113, 23);
		
		Label passwdConfirmLabel = new Label(composite, SWT.NONE);
		passwdConfirmLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwdConfirmLabel.setBounds(46, 115, 80, 23);
		passwdConfirmLabel.setText("确认密码：");
		
		passwdConfirm = new Text(composite, SWT.BORDER);
		passwdConfirm.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwdConfirm.setBounds(140, 115, 113, 23);
		
		Label groupLabel = new Label(composite, SWT.NONE);
		groupLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupLabel.setAlignment(SWT.RIGHT);
		groupLabel.setBounds(46, 157, 80, 23);
		groupLabel.setText("用户组：");
		
		Combo groupCombo = new Combo(composite, SWT.NONE);
		groupCombo.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupCombo.setEnabled(false);
		groupCombo.setBounds(140, 157, 113, 23);
		groupCombo.setText(groupInfo.getGroupName());
		
		/*Label instrumentIdLabel = new Label(composite, SWT.NONE);
		instrumentIdLabel.setAlignment(SWT.RIGHT);
		instrumentIdLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		instrumentIdLabel.setBounds(46, 245, 80, 23);
		instrumentIdLabel.setText("合约种类：");*/
		
		/*instrumentId = new Text(composite, SWT.BORDER);
		instrumentId.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		instrumentId.setBounds(140, 246, 113, 23);*/
		
		Label limitLabel = new Label(composite, SWT.NONE);
		limitLabel.setAlignment(SWT.RIGHT);
		limitLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		limitLabel.setBounds(46, 198, 80, 23);
		limitLabel.setText("资金额度：");
		
		limit = new Text(composite, SWT.BORDER);
		limit.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		limit.setBounds(140, 198, 113, 23);
		
		Button addButton = new Button(composite, SWT.NONE);
		addButton.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		addButton.setBounds(46, 305, 80, 27);
		addButton.setText("添加");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(passwd.getText() != null){
					if(!passwd.getText().equals(passwdConfirm.getText())){
						MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
						box.setMessage("密码输入不一致");
						box.setText("警告");
						box.open();
						return;
					}
				}
				
				UserInfoService userInfoService = (UserInfoService) SpringContextUtil.getBean("userInfoService");
				UserInfo userInfo = new UserInfo();
				userInfo.setUserNo(GenerateCodeUtil.generateCustNo());
				userInfo.setUserName(username.getText());
				userInfo.setUserPwd(passwd.getText());
				//userInfo.setContract(instrumentId.getText());
				userInfo.setGroupId(groupInfo.getId());
				userInfo.setCapital(new BigDecimal(limit.getText()));
				userInfo.setCreateTime(new Date());
				userInfo.setUpdateTime(new Date());
				try {
					userInfoService.saveUser(userInfo);
					
					CapitalRate capitalRate = new CapitalRate();
	                capitalRate.setUserName(username.getText());
	                capitalRate.setUserCapital(new BigDecimal(limit.getText()));
	                capitalRate.setUserCapitalRate(new BigDecimal(text.getText()));
	                //配资资金
	                capitalRate.setHostCapital1(new BigDecimal(limit.getText()).multiply(new BigDecimal(text.getText())));
	                CapitalRateMapper capitalRateMapper = (CapitalRateMapper) SpringContextUtil.getBean("capitalRateMapper");
	                capitalRateMapper.insert(capitalRate);
	                
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
					box.setMessage("添加成功");
					box.setText("提示");
					box.open();
					if (adminView != null) {
					    adminView.refreshUserTree();
                    }
					if(mainForm != null){
					    mainForm.refreshUserTree(mainForm.userTree,"1");
					    //mainForm.refreshUserTree(mainForm.instrumentUserTree);
					    //mainForm.refreshUserTree(mainForm.riskUserTree);
					}
					shell.dispose();
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("警告");
					box.open();
				} catch (Exception e2) {
				    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
                    box.setMessage(e2.getMessage());
                    box.setText("警告");
                    box.open();
                }
				
			}
		});
		
		Button cancel = new Button(composite, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		cancel.setBounds(194, 305, 80, 27);
		cancel.setText("取消");
		
		Label label = new Label(composite, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBounds(10, 238, 116, 23);
		label.setText("资金调入比例：");
		
		text = new Text(composite, SWT.BORDER);
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		text.setBounds(140, 238, 113, 23);

	}
}
