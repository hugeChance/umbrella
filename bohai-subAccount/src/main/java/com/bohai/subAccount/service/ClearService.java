package com.bohai.subAccount.service;

/**
 * 初始化数据服务
 * @author caojia
 */
public interface ClearService {
	
	/**
	 * 备份数据
	 */
	public void backUp();
	
	/**
	 * 还原资金及冻结信息
	 */
	public void init();
}
