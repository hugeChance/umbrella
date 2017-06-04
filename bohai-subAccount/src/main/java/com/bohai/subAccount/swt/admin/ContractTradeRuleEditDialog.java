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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.utils.SpringContextUtil;
import org.eclipse.swt.widgets.Combo;

public class ContractTradeRuleEditDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text cancelCount;
	private Text wtCount;
	//期货公司代码
	private Text contractNo;
	
	private Text openCount;
	
	private AdminViewMain mainView;
	private MainForm mainForm;
	
	private TableItem tableItem;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ContractTradeRuleEditDialog(Shell parent, int style, AdminViewMain mainView) {
		super(parent, style);
		setText("添加合约交易限制");
		this.mainView = mainView;
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ContractTradeRuleEditDialog(Shell parent, int style, MainForm mainForm, TableItem tableItem) {
        super(parent, style);
        setText("添加合约交易限制");
        this.mainForm = mainForm;
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
		shell.setSize(296, 300);
		shell.setText("更新合约交易规则");
		shell.setLayout(null);
		
		Label contractLab = new Label(shell, SWT.NONE);
		contractLab.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		contractLab.setAlignment(SWT.RIGHT);
		contractLab.setBounds(10, 33, 125, 23);
		contractLab.setText("合约代码：");
		
		contractNo = new Text(shell, SWT.BORDER);
		contractNo.setBounds(160, 29, 106, 27);
		
		Label label = new Label(shell, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 74, 125, 23);
		label.setText("撤单数：");
		
		cancelCount = new Text(shell, SWT.BORDER);
		cancelCount.setBounds(160, 75, 106, 23);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(10, 117, 125, 23);
		label_1.setText("委托数：");
		
		wtCount = new Text(shell, SWT.BORDER);
		wtCount.setBounds(160, 114, 106, 23);
		
		openCount = new Text(shell, SWT.BORDER);
        openCount.setBounds(160, 162, 106, 23);
        
        Label accountType = new Label(shell, SWT.NONE);
        accountType.setAlignment(SWT.RIGHT);
        accountType.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
        accountType.setBounds(39, 162, 96, 23);
        accountType.setText("开仓数：");
		
		Button button = new Button(shell, SWT.NONE);
		button.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		button.setBounds(39, 207, 64, 27);
		button.setText("更新");
		
		TradeRule rule = (TradeRule) tableItem.getData();
		
		
		contractNo.setText(StringUtils.isEmpty(rule.getContract())?"":rule.getContract());
		cancelCount.setText(StringUtils.isEmpty(rule.getCancelCount())?"":rule.getCancelCount().toString());
		wtCount.setText(StringUtils.isEmpty(rule.getEntrustCount())?"":rule.getEntrustCount().toString());
		openCount.setText(StringUtils.isEmpty(rule.getOpenCount())?"":rule.getOpenCount().toString());
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    
			    TradeRule tradeRule = new TradeRule();
			    
			    tradeRule.setId(rule.getId());
			    
			    tradeRule.setContract(contractNo.getText());
			    
			    tradeRule.setCancelCount(Integer.parseInt(cancelCount.getText()));
			    //委托数
			    tradeRule.setEntrustCount(Integer.parseInt(wtCount.getText()));
			    //开仓数
			    tradeRule.setOpenCount(Integer.parseInt(openCount.getText()));
			    
			    TradeRuleService tradeRuleService = (TradeRuleService) SpringContextUtil.getBean("tradeRuleService");
			    
			    try {
                    tradeRuleService.saveOrUpdateTradeRule(tradeRule);
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("更新成功");
                    box.setText("提示");
                    box.open();
                    if(mainForm != null){
                        mainForm.refreshContractTradeRule();
                    }
                    shell.dispose();
                } catch (FutureException e1) {
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage(e1.getMessage());
                    box.setText("错误");
                    box.open();
                }
			    
			    
				/*MainAccountService mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
				
				MainAccount mainAccount = new MainAccount();
				mainAccount.setAccountNo(accountNo.getText());
				mainAccount.setBrokerId(brokerId.getText());
				mainAccount.setPasswd(passwd.getText());
				mainAccount.setCreateTime(new Date());
				if(StringUtils.isEmpty(combo.getText())){
				    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("账户类型不能为空！");
                    box.setText("错误");
                    box.open();
                    return;
				}else if (combo.getText().equals("账户主")) {
                    mainAccount.setAccountType("1");
                }else if (combo.getText().equals("账户备")) {
                    mainAccount.setAccountType("2");
                }
				
				try {
					mainAccountService.saveMainAccount(mainAccount);
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("添加成功");
					box.setText("提示");
					box.open();
					if(mainView != null){
					    mainView.refreshMainAccount();
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
				}*/
			    
			    
			}
		});
		
		Button cancel = new Button(shell, SWT.NONE);
		cancel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		cancel.setBounds(180, 207, 68, 27);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancel.setText("取消");
		
		
	}
}
