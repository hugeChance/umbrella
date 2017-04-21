package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;

public interface UserInfoService {
	
	/**
	 * 根据用户组ID查询用户信息
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<UserInfo> getUsersByGroupId(String groupId) throws FutureException;
	
	/**
	 * 查询用户信息
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<UserInfo> getUsersByGroup() throws FutureException;
	
	/**
	 * 保存用户信息
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveUser(UserInfo userInfo) throws FutureException;
	
	/**
	 * 更新用户信息
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateUser(UserInfo userInfo) throws FutureException;
	
	/**
	 * 删除用户信息
	 * @param id
	 * @throws FutureException
	 */
	public void deleteUser(UserInfo userInfo) throws FutureException;
	
	/**
	 * 登录查询
	 */
	public int checkUser(String userName, String password) throws FutureException;
	
}
