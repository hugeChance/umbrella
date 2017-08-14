package com.bohai.subAccount.swt.trader;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class QuickOrderModeDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String mode;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public QuickOrderModeDialog(Shell parent, int style, String mode) {
		super(parent, style);
		setText("快捷下单说明");
		this.mode = mode;
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
		shell.setSize(294, 188);
		shell.setText(getText());
		
		Label labelSeven = new Label(shell, SWT.NONE);
		labelSeven.setBounds(36, 51, 19, 17);
		labelSeven.setText("7：");
		
		Label labelFour = new Label(shell, SWT.NONE);
		labelFour.setText("4：");
		labelFour.setBounds(36, 84, 19, 17);
		
		Label labelTwo = new Label(shell, SWT.NONE);
		labelTwo.setText("1：");
		labelTwo.setBounds(36, 115, 19, 17);
		
		Label labelNine = new Label(shell, SWT.NONE);
		labelNine.setText("9：");
		labelNine.setBounds(162, 51, 19, 17);
		
		Label labelSix = new Label(shell, SWT.NONE);
		labelSix.setText("6：");
		labelSix.setBounds(162, 84, 19, 17);
		
		Label labelThree = new Label(shell, SWT.NONE);
		labelThree.setText("3：");
		labelThree.setBounds(162, 115, 19, 17);
		
		Label modeLabel = new Label(shell, SWT.NONE);
		modeLabel.setAlignment(SWT.CENTER);
		modeLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		modeLabel.setBounds(96, 10, 97, 24);
		modeLabel.setText(mode);
		
		Label label1 = new Label(shell, SWT.NONE);
		label1.setBounds(61, 115, 82, 17);
		label1.setText("New Label");
		
		Label label4 = new Label(shell, SWT.NONE);
		label4.setText("New Label");
		label4.setBounds(61, 84, 82, 17);
		
		Label label7 = new Label(shell, SWT.NONE);
		label7.setText("New Label");
		label7.setBounds(61, 51, 82, 17);
		
		Label label9 = new Label(shell, SWT.NONE);
		label9.setText("New Label");
		label9.setBounds(187, 51, 82, 17);
		
		Label label6 = new Label(shell, SWT.NONE);
		label6.setText("New Label");
		label6.setBounds(187, 84, 82, 17);
		
		Label label3 = new Label(shell, SWT.NONE);
		label3.setText("New Label");
		label3.setBounds(187, 115, 82, 17);

		if(mode != null && mode.equals("模式一")){
			label1.setText("买一价");
			label4.setText("买一价+1");
			label7.setText("买一价+2");
			label3.setText("卖一价");
			label6.setText("卖一价-1");
			label9.setText("卖一价-2");
		}else if(mode != null && mode.equals("模式二")){
			label1.setText("买一价+1");
			label4.setText("买一价+2");
			label7.setText("买一价+3");
			label3.setText("卖一价-1");
			label6.setText("卖一价-2");
			label9.setText("卖一价-3");
		}else if(mode != null && mode.equals("模式三")){
			label1.setText("买一价+1");
			label4.setText("买一价");
			label7.setText("买一价-1");
			label3.setText("卖一价-1");
			label6.setText("卖一价");
			label9.setText("卖一价+1");
		}
		
	}
}
