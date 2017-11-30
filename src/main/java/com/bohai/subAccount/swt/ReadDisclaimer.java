package com.bohai.subAccount.swt;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ReadDisclaimer extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ReadDisclaimer(Shell parent, int style) {
		super(parent, style);
		setText("软件使用免责申明");
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
		shell.setSize(658, 457);
		shell.setText(getText());
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
			}
		});
		button.setBounds(285, 391, 80, 27);
		button.setText("确    定");
		
		Label lblNewLabel = new Label(shell, SWT.BORDER);
		lblNewLabel.setBounds(10, 53, 632, 261);
		lblNewLabel.setText("     本软件为渤海期货股份有限公司（以下简称“渤海期货”）根据客户要求免费提供给客户使用。虽然经过详细的测试，\r\n但由于软件开发的复杂性，渤海期货不能保证本软件与所有的软硬件系统完全兼容，不能保证本软件完全没有错误。如\r\n果出现不兼容及软件错误的情况，用户可报告渤海期货（电话：13916702735），获得技术支持。如果无法解决兼容性\r\n问题，用户可以删除本软件。渤海期货不对本软件做任何明示或暗示的无瑕疵担保，并且明确拒绝包括渤海期\r\n货工作人员在内的任何人做出的担保。\r\n     使用本软件的风险由用户自行承担，在适用法律允许的最大范围内，渤海期货对因使用或不能使用本软件所产生的损\r\n害及风险，包括但不限于交易损失、贸易中断、用户信息的丢失或任何其它直接或间接的经济损失，渤海期货不承担任\r\n何责任。\r\n     本软件可能遭受信息源异常、卫星传输线路故障、通讯线路故障、网络故障、黑客攻击、病毒侵入、他人蓄意破坏、\r\n技术发展限制、法律法规变更、政府禁令、监管机构要求、自然灾害、战争、骚乱、罢工、公共卫生事件、电力和通讯\r\n故障等事件及不可抗力事件影响，渤海期货不对由此造成的服务中断、延迟、数据丢失、错误、遗漏等信息异常或信息\r\n传递异常所致后果负责，不对交易异常引致的盈利或亏损以及其他情形负责。\r\n\r\n                                                                                                  声明人：渤海期货股份有限公司\r\n                                                                                                     日期：2017年12月1日\r\n\r\n");
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("宋体", 12, SWT.BOLD));
		lblNewLabel_1.setBounds(285, 26, 80, 21);
		lblNewLabel_1.setText("免责申明");
		
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(10, 320, 632, 50);
		lblNewLabel_2.setText("尊敬的用户：\r\n如您已全部理解并自愿接受上述渤海期货就本软件做出的免责声明，请点击“确认”键继续。\r\n");

	}
}
