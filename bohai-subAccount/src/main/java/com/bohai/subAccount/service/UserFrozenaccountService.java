package com.bohai.subAccount.service;

import java.util.List;
import java.util.Map;

import com.bohai.subAccount.entity.UserFrozenaccount;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;

public interface UserFrozenaccountService {
	
	/**
	 * 根据用户ID查询输入报单
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<UserFrozenaccount> getUserByUserName(String subuserid) throws FutureException;
	
	
	/**
	 * 保存输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveUserFrozenaccount(UserFrozenaccount userFrozenaccount) throws FutureException;
	
	/**
	 * 更新输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateUserFrozenaccount(UserFrozenaccount userFrozenaccount) throws FutureException;
	
	/**
	 * 删除输入报单
	 * @param id
	 * @throws FutureException
	 */
	public void deleteUserFrozenaccount(String subuserid) throws FutureException;
	
	public UserFrozenaccount getUserByUnfrozen(Map<String,Object> map) throws FutureException;
	
	public UserFrozenaccount getUserByTradeUnfrozen(Map<String,Object> map) throws FutureException;
	
}
