package com.bohai.subAccount.service;

import java.util.List;
import java.util.Map;

import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;

public interface SubTradingaccountService {
	
	/**
	 * 根据用户ID查询输入报单
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<SubTradingaccount> getUserByUserName(String subuserid) throws FutureException;
	
	public List<SubTradingaccount> getUserByAllForAccount() throws FutureException;
	
	/**
	 * 根据用户ID查询输入报单
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public SubTradingaccount getUserByUserName2(String subuserid) throws FutureException;
	
	
	/**
	 * 保存输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveSubTradingaccount(SubTradingaccount subTradingaccount) throws FutureException;
	
	/**
	 * 更新输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateSubTradingaccount(SubTradingaccount subTradingaccount) throws FutureException;
	
	/**
	 * 删除输入报单
	 * @param id
	 * @throws FutureException
	 */
	public void deleteSubTradingaccount(String subuserid) throws FutureException;
	
	/**
	 * 设置冻结保证金，冻结手续费
	 * @param id
	 * @throws FutureException
	 */
	public int setFrozen(Map<String,Object> map) throws FutureException;
	
	/**
	 * 解冻保证金，冻结手续费
	 * @param id
	 * @throws FutureException
	 */
	public int updateUnfrozen(Map<String,Object> map) throws FutureException;
	
	/**
	 * 实际手续费计入
	 * @param id
	 * @throws FutureException
	 */
	public int setCommission(Map<String,Object> map) throws FutureException;
	
	
}
