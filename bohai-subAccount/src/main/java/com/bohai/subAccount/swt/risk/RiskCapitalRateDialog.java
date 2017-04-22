package com.bohai.subAccount.swt.risk;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.SubTradingaccountService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.utils.SpringContextUtil;

import org.eclipse.swt.widgets.Button;

public class RiskCapitalRateDialog extends Dialog {

	private TableItem item;
	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	
	private RiskManageView manageView;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RiskCapitalRateDialog(Shell parent, int style,TableItem item,RiskManageView manageView) {
		super(parent, style);
		setText("交易员出入金");
		loadSpringContext();
		this.item = item;
		this.manageView = manageView;

	}
	
	public void loadSpringContext(){
		/*logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        logger.info("===================加载成功 !==================");*/
        
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
		shell.setSize(355, 227);
		shell.setText("交易员出入金");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 10, 61, 17);
		label.setText("交易员名：");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(77, 10, 61, 17);
		label_1.setText("王家玮");
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(182, 10, 61, 17);
		label_2.setText("配置比例：");
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(249, 10, 61, 17);
		label_3.setText("1 : 4.33");
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(10, 44, 61, 17);
		label_4.setText("现有资金：");
		
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(77, 44, 96, 17);
		label_5.setText("100000000.99");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(182, 44, 61, 17);
		lblNewLabel.setText("现有调配：");
		
		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setBounds(249, 44, 96, 17);
		label_6.setText("433000000.99");
		
		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setBounds(10, 80, 61, 17);
		label_7.setText("入金金额：");
		
		Label label_8 = new Label(shell, SWT.NONE);
		label_8.setBounds(10, 120, 61, 17);
		label_8.setText("出金金额：");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(77, 80, 96, 23);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(77, 117, 96, 23);
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(182, 80, 61, 17);
		lblNewLabel_1.setText("入金调配：");
		
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(249, 80, 96, 17);
		
		Label label_9 = new Label(shell, SWT.NONE);
		label_9.setBounds(182, 120, 61, 17);
		label_9.setText("出金调配：");
		
		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setBounds(249, 120, 96, 17);
		
		Button button = new Button(shell, SWT.NONE);
		button.setBounds(10, 161, 80, 27);
		button.setText("确      认");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setBounds(182, 161, 80, 27);
		button_1.setText("取      消");

	}

}
