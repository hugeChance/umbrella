package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;

public interface UserLoginService {
	
	/**
	 * 根据用户ID查询用户信息
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<UserLogin> getUserByUserName(String userName) throws FutureException;
	
	
	/**
	 * 保存用户信息
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveUserLogin(UserLogin userLogin) throws FutureException;
	
	/**
	 * 更新用户信息
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateUserLogin(UserLogin userLogin) throws FutureException;
	
	/**
	 * 删除用户信息
	 * @param id
	 * @throws FutureException
	 */
	public void deleteUserLogin(String userName) throws FutureException;
	
}
