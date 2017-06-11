package com.bohai.subAccount.swt.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.GroupInfoService;
import com.bohai.subAccount.service.GroupRuleService;
import com.bohai.subAccount.utils.SpringContextUtil;

import swing2swt.layout.BorderLayout;

/**
 * @author caojia
 * 创建用户组对话框
 */
public class GroupRuleAddDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text forceCloseRate;
	private Text forceCloseTime;
	private Text openTime;
	private Combo groupNameText;
	//private Combo accountCombo;
	
	private AdminViewMain adminView;
	
	private GroupInfo groupInfo;
	private MainForm mainForm;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public GroupRuleAddDialog(Shell parent, int style, GroupInfo groupInfo, MainForm mainForm) {
		super(parent, style);
		setText("添加用户组规则");
		this.groupInfo = groupInfo;
		//this.adminView = adminView;
		this.mainForm = mainForm;
	}
	
   public GroupRuleAddDialog(Shell parent, int style, AdminViewMain adminView) {
        super(parent, style);
        setText("添加用户组规则");
        //this.groupInfo = groupInfo;
        this.adminView = adminView;
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
		shell.setSize(335, 302);
		shell.setText(getText());
		shell.setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		
		/*Label accountLabel = new Label(composite, SWT.NONE);
		accountLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		accountLabel.setAlignment(SWT.RIGHT);
		accountLabel.setBounds(51, 70, 80, 26);
		accountLabel.setText("交易账号：");*/
		
		/*accountCombo = new Combo(composite, SWT.NONE);
		accountCombo.setBounds(169, 69, 118, 26);
		accountCombo.setEnabled(false);
		accountCombo.setText(mainAccount.getAccountNo());*/
		
		Label forceCloseRateLabel = new Label(composite, SWT.NONE);
		forceCloseRateLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceCloseRateLabel.setAlignment(SWT.RIGHT);
		forceCloseRateLabel.setBounds(51, 76, 80, 25);
		forceCloseRateLabel.setText("强平比例：");
		
		forceCloseRate = new Text(composite, SWT.BORDER);
		forceCloseRate.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceCloseRate.setBounds(169, 73, 118, 26);
		
		Label forceCloseTimeLabel = new Label(composite, SWT.NONE);
		forceCloseTimeLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		forceCloseTimeLabel.setAlignment(SWT.RIGHT);
		forceCloseTimeLabel.setBounds(51, 163, 80, 26);
		forceCloseTimeLabel.setText("强平时间：");
		
		forceCloseTime = new Text(composite, SWT.BORDER|SWT.MULTI);
		forceCloseTime.setBounds(169, 164, 118, 25);
		
		Label openTimeLabel = new Label(composite, SWT.NONE);
		openTimeLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		openTimeLabel.setAlignment(SWT.RIGHT);
		openTimeLabel.setBounds(51, 117, 80, 26);
		openTimeLabel.setText("开仓时间：");
		
		openTime = new Text(composite, SWT.BORDER);
		openTime.setBounds(169, 118, 118, 26);
		
		
		Button create = new Button(composite, SWT.NONE);
		create.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		create.setBounds(51, 220, 91, 27);
		create.setText("创建");
		create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GroupRuleService groupRuleService = (GroupRuleService) SpringContextUtil.getBean("groupRuleService");
				GroupRule grouprule = new GroupRule();
				
				GroupInfo groupInfo = (GroupInfo) groupNameText.getData(groupNameText.getText());
				grouprule.setGroupId(groupInfo.getId());
				grouprule.setForceCloseRate(StringUtils.isEmpty(forceCloseRate.getText())?null:new BigDecimal(forceCloseRate.getText()));
				grouprule.setOpenTime(StringUtils.isEmpty(openTime.getText())?null:openTime.getText());
				grouprule.setCloseTime(StringUtils.isEmpty(forceCloseTime.getText())?null:forceCloseTime.getText());
				try {
					groupRuleService.saveGroupRule(grouprule);
					
					MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
					box.setMessage("创建成功！");
					box.setText("提示");
					box.open();
					if(adminView != null){
					    adminView.refreshGroupRuleTable();
					}
					
					if (mainForm != null) {
					    mainForm.refreshGroupRuleTable();
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
		cancel.setBounds(197, 220, 91, 27);
		cancel.setText("取消");
		
		Label label = new Label(composite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setAlignment(SWT.RIGHT);
		label.setBounds(51, 28, 80, 20);
		label.setText("组名：");
		
		groupNameText = new Combo(composite, SWT.BORDER);
		groupNameText.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		groupNameText.setBounds(169, 25, 119, 25);
		GroupInfoService groupInfoService = (GroupInfoService) SpringContextUtil.getBean("groupInfoService");
		
		List<GroupInfo> list;
        try {
            list = groupInfoService.getGroups();
            if(list == null || list.size() <1){
                MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                box.setMessage("请先添加用户组");
                box.setText("警告");
                box.open();
                shell.dispose();
            }
            int i = 0;
            String[] groupName = new String[list.size()];
            for (GroupInfo groupInfo2 : list) {
                groupName[i++] = groupInfo2.getGroupName();
                groupNameText.setData(groupInfo2.getGroupName(), groupInfo2);
            }
            groupNameText.setItems(groupName);
        } catch (FutureException e1) {
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e1.getMessage());
            box.setText("警告");
            box.open();
            shell.dispose();
        }
		
		
		
		
	}
}
