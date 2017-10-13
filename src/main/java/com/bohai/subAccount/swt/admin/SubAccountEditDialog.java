package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.dao.CapitalRateMapper;
import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.utils.GenerateCodeUtil;
import com.bohai.subAccount.utils.SpringContextUtil;

public class SubAccountEditDialog extends Dialog {
    
    static Logger logger = Logger.getLogger(SubAccountEditDialog.class);

	protected Object result;
	protected Shell shell;
	private Text username;
	private Text passwd;
	private Text passwdConfirm;
	//private Text instrumentId;
	private Text limit;
	
	private GroupInfo groupInfo; 
	private oldAdminViewMain adminView;
	private UserInfo userInfo;
	private MainForm mainForm;
	private Text text;
	
	private String capRate;
	private Text forceLimitText;
	private Text forceRateText;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SubAccountEditDialog(Shell parent, int style, UserInfo userInfo,GroupInfo groupInfo, oldAdminViewMain adminView) {
		super(parent, style);
		setText("修改用户");
		this.groupInfo = groupInfo;
		this.adminView = adminView;
		this.userInfo = userInfo;
	}
	
   /**
    * @wbp.parser.constructor
    */
   public SubAccountEditDialog(Shell parent, int style, UserInfo userInfo, MainForm mainForm,String capRate) {
        super(parent, style);
        setText("修改用户");
        this.mainForm = mainForm;
        this.userInfo = userInfo;
        this.capRate = capRate;
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
		shell.setSize(314, 413);
		shell.setText(getText());
		shell.setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		
		Label usernameLabel = new Label(composite, SWT.NONE);
		usernameLabel.setAlignment(SWT.RIGHT);
		usernameLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		usernameLabel.setBounds(56, 21, 80, 23);
		usernameLabel.setText("用户名：");
		
		username = new Text(composite, SWT.BORDER);
		username.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		username.setBounds(150, 21, 113, 23);
		username.setEnabled(false);
		
		Label passwdLabel = new Label(composite, SWT.NONE);
		passwdLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwdLabel.setAlignment(SWT.RIGHT);
		passwdLabel.setBounds(56, 58, 80, 23);
		passwdLabel.setText("密码：");
		
		passwd = new Text(composite, SWT.BORDER);
		passwd.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwd.setBounds(150, 59, 113, 23);
		
		Label passwdConfirmLabel = new Label(composite, SWT.NONE);
		passwdConfirmLabel.setAlignment(SWT.RIGHT);
		passwdConfirmLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwdConfirmLabel.setBounds(33, 99, 103, 23);
		passwdConfirmLabel.setText("确认密码：");
		
		passwdConfirm = new Text(composite, SWT.BORDER);
		passwdConfirm.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		passwdConfirm.setBounds(150, 99, 113, 23);
		
		Label groupLabel = new Label(composite, SWT.NONE);
		groupLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupLabel.setAlignment(SWT.RIGHT);
		groupLabel.setBounds(33, 141, 103, 23);
		groupLabel.setText("用户组：");
		
		Combo groupCombo = new Combo(composite, SWT.NONE);
		groupCombo.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupCombo.setEnabled(false);
		groupCombo.setBounds(150, 141, 113, 23);
		groupCombo.setText(userInfo.getGroupName());
		
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
		limitLabel.setBounds(33, 182, 103, 23);
		limitLabel.setText("资金额度：");
		
		limit = new Text(composite, SWT.BORDER);
		limit.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		limit.setBounds(150, 182, 113, 23);
		
		//初始化数据
		username.setText(userInfo.getUserName());
		passwd.setText(userInfo.getUserPwd());
		passwdConfirm.setText(userInfo.getUserPwd());
		limit.setText(StringUtils.isEmpty(userInfo.getCapital())?"":userInfo.getCapital().toString());
		
		
		//配资比例
		text = new Text(composite, SWT.BORDER);
        text.setText("");
        text.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        text.setBounds(150, 222, 113, 23);

		if(!StringUtils.isEmpty(capRate)){
		    text.setText(capRate);
		}
		
		Label lblPeizi = new Label(composite, SWT.NONE);
		lblPeizi.setText("资金调入金额：");
		lblPeizi.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lblPeizi.setAlignment(SWT.RIGHT);
		lblPeizi.setBounds(20, 222, 116, 23);
		
		Label forceRateLab = new Label(composite, SWT.NONE);
		forceRateLab.setText("强平比例：");
		forceRateLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceRateLab.setAlignment(SWT.RIGHT);
		forceRateLab.setBounds(22, 259, 114, 23);
		
		forceRateText = new Text(composite, SWT.BORDER);
		forceRateText.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		forceRateText.setBounds(150, 259, 113, 23);
		
		Label forceLimitLab = new Label(composite, SWT.NONE);
		forceLimitLab.setText("强平金额：");
		forceLimitLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceLimitLab.setAlignment(SWT.RIGHT);
		forceLimitLab.setBounds(20, 298, 116, 23);
		
		forceLimitText = new Text(composite, SWT.BORDER);
		forceLimitText.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		forceLimitText.setBounds(150, 298, 113, 23);
		
		forceRateText.setText(StringUtils.isEmpty(userInfo.getForceRate()) ?"" : userInfo.getForceRate());
        forceLimitText.setText(StringUtils.isEmpty(userInfo.getForceLimit()) ?"" : userInfo.getForceLimit());
		
		Button addButton = new Button(composite, SWT.NONE);
		addButton.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		addButton.setBounds(45, 339, 80, 27);
		addButton.setText("更新");
		
		Button cancel = new Button(composite, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		cancel.setBounds(193, 339, 80, 27);
		cancel.setText("取消");
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
				
				if(!StringUtils.isEmpty(forceLimitText.getText())){
				    try {
                        new BigDecimal(forceLimitText.getText());
                    } catch (Exception e1) {
                        logger.error("强平金额设置失败",e1);
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
                        box.setMessage("强平金额设置失败");
                        box.setText("警告");
                        box.open();
                        return;
                    }
				}
				
                if(!StringUtils.isEmpty(forceRateText.getText())){
                    try {
                        BigDecimal b = new BigDecimal(forceRateText.getText());
                        if( b.compareTo(new BigDecimal("1")) >= 0 ){
                            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
                            box.setMessage("强平比例不能大于或等于1");
                            box.setText("警告");
                            box.open();
                            return;
                        }
                    } catch (Exception e1) {
                        logger.error("强平比例设置失败",e1);
                        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
                        box.setMessage("强平比例设置失败");
                        box.setText("警告");
                        box.open();
                        return;
                    }
                    
                }
				
				UserInfoService userInfoService = (UserInfoService) SpringContextUtil.getBean("userInfoService");
				userInfo.setUserName(username.getText());
				userInfo.setUserPwd(passwd.getText());
				//userInfo.setContract(instrumentId.getText());
				userInfo.setGroupId(userInfo.getGroupId());
				userInfo.setCapital(new BigDecimal(limit.getText()));
				userInfo.setUpdateTime(new Date());
				userInfo.setForceLimit(forceLimitText.getText());
				userInfo.setForceRate(forceRateText.getText());
				
				try {
					userInfoService.updateUser(userInfo);
					
					CapitalRate capitalRate = new CapitalRate();
	                capitalRate.setUserName(username.getText());
	                capitalRate.setUserCapital(new BigDecimal(limit.getText()));
	                //capitalRate.setUserCapitalRate(new BigDecimal(text.getText()));
	                //配资资金
	                capitalRate.setHostCapital1(new BigDecimal(text.getText()));
	                CapitalRateMapper capitalRateMapper = (CapitalRateMapper) SpringContextUtil.getBean("capitalRateMapper");
	                capitalRateMapper.updateByPrimaryKeySelective(capitalRate);
					
					
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
					box.setMessage("更新成功");
					box.setText("提示");
					box.open();
					if(adminView != null){
//					    adminView.refreshUserTree();
					}
					if(mainForm != null){
					    mainForm.refreshSubaccountByGroupId(userInfo.getGroupId());
					}
					shell.dispose();
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("警告");
					box.open();
				}
			}
		});
		
	}

}
