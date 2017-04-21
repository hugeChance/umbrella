package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.Ctptradingaccount;
import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;

public interface CtpTradingAccountService {
	
	/**
	 * 根据CTP用户ID查询资金
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<Ctptradingaccount> getUserByUserName(String userName) throws FutureException;
	
	
	/**
	 * 保存输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveCtptradingaccount(Ctptradingaccount ctptradingaccount) throws FutureException;
	
	/**
	 * 更新输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateCtptradingaccount(Ctptradingaccount ctptradingaccount) throws FutureException;
	
	/**
	 * 删除输入报单
	 * @param id
	 * @throws FutureException
	 */
	public void deleteCtptradingaccount(String userName) throws FutureException;
	
}
