package com.bohai.subAccount.swt.admin;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.utils.SpringContextUtil;
import org.eclipse.swt.widgets.Combo;

public class AccountEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text accountNo;
	private Text passwd;
	//期货公司代码
	private Text brokerId;
	
	private Combo combo;
	
	private MainAccount mainAccount;
	private oldAdminViewMain mainView;
	private MainForm mainForm;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AccountEditDialog(Shell parent, int style, MainAccount mainAccount, oldAdminViewMain mainView) {
		super(parent, style);
		setText("更新主账户");
		this.mainAccount = mainAccount;
		this.mainView = mainView;
	}
	
   public AccountEditDialog(Shell parent, int style, MainAccount mainAccount, MainForm mainForm) {
        super(parent, style);
        setText("更新主账户");
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
		shell.setSize(296, 314);
		shell.setText("更新账户");
		shell.setLayout(null);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_2.setAlignment(SWT.RIGHT);
		label_2.setBounds(10, 33, 125, 23);
		label_2.setText("期货公司代码：");
		
		brokerId = new Text(shell, SWT.BORDER);
		brokerId.setBounds(160, 29, 106, 27);
		
		Label label = new Label(shell, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 74, 125, 23);
		label.setText("投资者代码：");
		
		accountNo = new Text(shell, SWT.BORDER);
		accountNo.setBounds(160, 75, 106, 23);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(10, 117, 125, 23);
		label_1.setText("密码：");
		
		passwd = new Text(shell, SWT.BORDER|SWT.PASSWORD);
		passwd.setBounds(160, 114, 106, 23);
		
		brokerId.setText(mainAccount.getBrokerId());
		accountNo.setText(mainAccount.getAccountNo());
		passwd.setText(mainAccount.getPasswd());
		
		combo = new Combo(shell, SWT.NONE);
        combo.setBounds(161, 156, 105, 23);
        String[] s = {"账户主","账户备"};
        combo.setItems(s);
        combo.setText(mainAccount.getAccountType().equals("1")?"账户主":"账户备");
        
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.setBounds(37, 220, 64, 27);
		button.setText("更新");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MainAccountService mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
				
				MainAccount mainAccount = new MainAccount();
				mainAccount.setId(AccountEditDialog.this.mainAccount.getId());
				mainAccount.setAccountNo(accountNo.getText());
				mainAccount.setBrokerId(brokerId.getText());
				mainAccount.setPasswd(passwd.getText());
				mainAccount.setUpdateTime(new Date());
				if(StringUtils.isEmpty(combo.getText())){
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("账户类型不能为空！");
                    box.setText("错误");
                    box.open();
                }else if (combo.getText().equals("账户主")) {
                    mainAccount.setAccountType("1");
                }else if (combo.getText().equals("账户备")) {
                    mainAccount.setAccountType("2");
                }
				
				try {
					mainAccountService.updateMainAccount(mainAccount);
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("更新成功");
					box.setText("提示");
					box.open();
					if(mainView != null){
//					    mainView.refreshMainAccount();
					}
					if(mainForm != null){
					    mainForm.refreshMainAccount();
					}
					shell.dispose();
				} catch (FutureException e1) {
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage(e1.getMessage());
					box.setText("错误");
					box.open();
				}
			}
		});
		
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.setBounds(181, 220, 68, 27);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancel.setText("取消");
		
		Label accountType = new Label(shell, SWT.NONE);
		accountType.setAlignment(SWT.RIGHT);
		accountType.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		accountType.setBounds(37, 156, 98, 23);
		accountType.setText("账户类型：");
		
	}

}
