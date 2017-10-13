package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Date;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.GroupInfoService;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.utils.SpringContextUtil;

/**
 * @author caojia
 * 创建用户组对话框
 */
public class GroupAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text forceCloseRate;
	private Text forceCloseTime;
	private Text openTime;
	private Text groupNameText;
	private Combo accountCombo;
	
	private oldAdminViewMain adminView;
	
	private MainAccount mainAccount;
	
	private MainForm mainForm;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public GroupAddDialog(Shell parent, int style, MainAccount mainAccount, oldAdminViewMain adminView) {
		super(parent, style);
		setText("创建用户组");
		this.mainAccount = mainAccount;
		this.adminView = adminView;
	}
	
   public GroupAddDialog(Shell parent, int style, MainAccount mainAccount, MainForm mainForm) {
        super(parent, style);
        setText("创建用户组");
        this.mainAccount = mainAccount;
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
		shell.setSize(335, 330);
		shell.setText(getText());
		shell.setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		
		Label accountLabel = new Label(composite, SWT.NONE);
		accountLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		accountLabel.setAlignment(SWT.RIGHT);
		accountLabel.setBounds(51, 70, 80, 26);
		accountLabel.setText("交易账号：");
		
		accountCombo = new Combo(composite, SWT.NONE);
		accountCombo.setBounds(169, 69, 118, 26);
		accountCombo.setEnabled(false);
		accountCombo.setText(mainAccount.getAccountNo());
		
		Label forceCloseRateLabel = new Label(composite, SWT.NONE);
		forceCloseRateLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceCloseRateLabel.setAlignment(SWT.RIGHT);
		forceCloseRateLabel.setBounds(51, 109, 80, 25);
		forceCloseRateLabel.setText("强平比例：");
		
		forceCloseRate = new Text(composite, SWT.BORDER);
		forceCloseRate.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceCloseRate.setBounds(169, 106, 118, 26);
		
		Label forceCloseTimeLabel = new Label(composite, SWT.NONE);
		forceCloseTimeLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceCloseTimeLabel.setAlignment(SWT.RIGHT);
		forceCloseTimeLabel.setBounds(51, 196, 80, 26);
		forceCloseTimeLabel.setText("强平时间：");
		
		forceCloseTime = new Text(composite, SWT.BORDER|SWT.MULTI);
		forceCloseTime.setBounds(169, 197, 118, 25);
		
		Label openTimeLabel = new Label(composite, SWT.NONE);
		openTimeLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openTimeLabel.setAlignment(SWT.RIGHT);
		openTimeLabel.setBounds(51, 150, 80, 26);
		openTimeLabel.setText("开仓时间：");
		
		openTime = new Text(composite, SWT.BORDER);
		openTime.setBounds(169, 151, 118, 26);
		
		
		Button create = new Button(composite, SWT.NONE);
		create.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		create.setBounds(51, 253, 91, 27);
		create.setText("创建");
		create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GroupInfoService groupInfoService = (GroupInfoService) SpringContextUtil.getBean("groupInfoService");
				GroupInfo groupInfo = new GroupInfo();
				groupInfo.setGroupName(groupNameText.getText());
				groupInfo.setAccountNo(mainAccount.getId());
				groupInfo.setCreateTime(new Date());
				groupInfo.setUpdatetime(new Date());
				try {
					groupInfoService.saveGroupInfo(groupInfo);
					
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("创建成功！");
					box.setText("提示");
					box.open();
					if(adminView != null){
//					    adminView.refreshUserTree();
					}
					if(mainForm != null){
					    mainForm.refreshUserTree(mainForm.userTree);
					}
					shell.dispose();
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
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
		cancel.setBounds(197, 253, 91, 27);
		cancel.setText("取消");
		
		Label label = new Label(composite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setAlignment(SWT.RIGHT);
		label.setBounds(51, 28, 80, 20);
		label.setText("组名：");
		
		groupNameText = new Text(composite, SWT.BORDER);
		groupNameText.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupNameText.setBounds(169, 25, 119, 25);
		
	}
}
