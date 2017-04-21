package com.bohai.subAccount.swt.trader.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewerContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){//对这个参数进行类型的判断.
			return ((List)inputElement).toArray();//将setInput传过来的List变成一个数组输出
		}else{
			return new Object[0];
		}
	}

}
