package com.bohai.subAccount.swt.admin.provider;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class AccountTreeContentProvider implements ITreeContentProvider {

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object[] getElements(Object arg0) {
		//打印出树的输入信息，通常用户可以通过输入信息构建树 
		  System.out.println(arg0); 
		  // File.listRoots()作为树的根节点 
		  return File.listRoots();
	}
	
	@Override
	public boolean hasChildren(Object arg0) {
		Object[] obj = getChildren(arg0);
		  //判断树是否有下一级节点，true为在节点显示"+"信息
		  return obj == null ? false : obj.length > 0;
	}
	
	@Override
	public Object getParent(Object arg0) {
		//返回树的上一 级节点 
		  return ((File) arg0).getParentFile();
	}
	
	@Override
	public Object[] getChildren(Object arg0) {
		 //返回树的下一级节点 
		  return ((File) arg0).listFiles(); 
	}

}
