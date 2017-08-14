package com.bohai.subAccount.swt.admin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EditDailog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private CLabel label_2;
	private Combo combo;
	private CLabel label_3;
	private Text text_3;
	private Button btnNewButton;
	private Button btnNewButton_1;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditDailog(Shell parent, int style) {
		super(parent, style);
		setText("修改用户");
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

		shell = new Shell(getParent(), getStyle() | SWT.SHEET);
		shell.setSize(330, 400);
		shell.setText(getText());
		
		CLabel lblNewLabel = new CLabel(shell, SWT.CENTER);
		lblNewLabel.setBounds(72, 29, 54, 24);
		lblNewLabel.setText("用户名：");
		
		text = new Text(shell, SWT.BORDER );
		text.setBounds(144, 29, 107, 24);
		
		CLabel label = new CLabel(shell, SWT.CENTER);
		label.setText("密码：");
		label.setBounds(84, 71, 42, 24);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(144, 71, 107, 24);
		
		CLabel label_1 = new CLabel(shell, SWT.CENTER);
		label_1.setText("确认密码：");
		label_1.setBounds(61, 112, 66, 24);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(144, 112, 107, 24);
		
		label_2 = new CLabel(shell, SWT.CENTER);
		label_2.setText("用户组：");
		label_2.setBounds(72, 152, 54, 24);
		
		combo = new Combo(shell, SWT.NONE);
		combo.setBounds(144, 150, 107, 26);
		
		label_3 = new CLabel(shell, SWT.CENTER);
		label_3.setText("资金额度：");
		label_3.setBounds(61, 197, 66, 24);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(144, 197, 107, 24);
		
		btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnNewButton.setBounds(72, 278, 81, 28);
		btnNewButton.setText("更新");
		
		btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnNewButton_1.setBounds(184, 278, 81, 28);
		btnNewButton_1.setText("取消");

	}
}
