package com.bohai.subAccount.swt.admin.provider;

import java.util.Map;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.alibaba.fastjson.JSON;

public class TableViewerLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int columnIndex) {
		System.out.println(JSON.toJSONString(arg0));
		System.out.println(columnIndex);
		if(columnIndex == 0){
			return ((Map<String,String>)arg0).get("rowNo");
		}

		if(columnIndex == 1){
			return ((Map<String,String>)arg0).get("accountNo");
		}
		
		if(columnIndex == 2){
			return ((Map<String,String>)arg0).get("ip");
		}
		
		if(columnIndex == 3){
			return ((Map<String,String>)arg0).get("createTime");
		}
		
		if(columnIndex == 4){
			return ((Map<String,String>)arg0).get("updateTime");
		}
		
		return "";
	}

}
