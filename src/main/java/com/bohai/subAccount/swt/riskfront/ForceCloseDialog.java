package com.bohai.subAccount.swt.riskfront;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.utils.SpringContextUtil;

public class ForceCloseDialog extends Dialog {
    
    static Logger logger = Logger.getLogger(ForceCloseDialog.class);

    protected Object result;
    protected Shell shell;
    private Text userNameText;
    private Text forceRateText;
    private Label forceLimitLab;
    private Text forceLimitText;

    private TableItem tableItem;
    private RiskFrontView view;
    
    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public ForceCloseDialog(Shell parent, int style, TableItem item, RiskFrontView view) {
        super(parent, style);
        setText("强平设置");
        this.tableItem = item;
        this.view = view;
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
        shell.setSize(362, 300);
        shell.setText("强平设置");
        
        Label userNameLab = new Label(shell, SWT.NONE);
        userNameLab.setAlignment(SWT.RIGHT);
        userNameLab.setBounds(52, 35, 89, 26);
        userNameLab.setText("用户名：");
        
        userNameText = new Text(shell, SWT.BORDER|SWT.READ_ONLY);
        userNameText.setBounds(181, 36, 114, 26);
        //用户名
        userNameText.setText(tableItem.getText(0));
        
        Label forceRateLab = new Label(shell, SWT.NONE);
        forceRateLab.setText("强平比例：");
        forceRateLab.setAlignment(SWT.RIGHT);
        forceRateLab.setBounds(52, 92, 89, 26);
        
        forceRateText = new Text(shell, SWT.BORDER);
        forceRateText.setBounds(181, 92, 114, 26);
        //强平比例
        forceRateText.setText(tableItem.getText(5));
        
        forceLimitLab = new Label(shell, SWT.NONE);
        forceLimitLab.setText("强平金额：");
        forceLimitLab.setAlignment(SWT.RIGHT);
        forceLimitLab.setBounds(52, 147, 89, 26);
        
        forceLimitText = new Text(shell, SWT.BORDER);
        forceLimitText.setBounds(181, 147, 114, 26);
        //强平金额
        forceLimitText.setText(tableItem.getText(6));
        
        Button comfirm = new Button(shell, SWT.NONE);
        comfirm.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(userNameText.getText());
                userInfo.setForceRate(forceRateText.getText().trim());
                userInfo.setForceLimit(forceLimitText.getText().trim());
                UserInfoMapper userInfoMapper = (UserInfoMapper) SpringContextUtil.getBean("userInfoMapper");
                
                try {
                    userInfoMapper.updateUserForceClose(userInfo);
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("更新成功");
                    box.setText(CommonConstant.MESSAGE_BOX_NOTICE);
                    box.open();
                    Table table = view.getSubAccountTable();
                    TableItem[] item = table.getItems();
                    if(item != null ){
                        for (TableItem tableItem : item) {
                            if(tableItem.getText(0).equals(userNameText.getText())){
                                tableItem.setText(5, forceRateText.getText().trim());
                                tableItem.setText(6, forceLimitText.getText().trim());
                                break;
                            }
                        }
                    }
                    
                    shell.dispose();
                    
                } catch (Exception e1) {
                    logger.error("更新强平比例失败",e1);
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("强平设置失败"+e1.getMessage());
                    box.setText(CommonConstant.MESSAGE_BOX_ERROR);
                    box.open();
                }
            }
        });
        comfirm.setBounds(52, 205, 89, 36);
        comfirm.setText("更新");
        
        Button cancel = new Button(shell, SWT.NONE);
        cancel.setText("取消");
        cancel.setBounds(206, 205, 89, 36);

    }
}
