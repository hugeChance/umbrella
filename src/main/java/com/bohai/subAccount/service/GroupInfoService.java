package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.exception.FutureException;

public interface GroupInfoService {
	
	public GroupInfo getGroupInfoById(String id) throws FutureException;
	
	/**
	 * 查询所有用户组
	 * @return
	 */
	public List<GroupInfo> getGroups() throws FutureException;
	
	/**
	 * 保存用户组信息
	 * @param groupInfo
	 * @throws FutureException
	 */
	public void saveGroupInfo(GroupInfo groupInfo) throws FutureException;
	
	/**
	 * 更新用户组信息
	 * @param groupInfo
	 * @throws FutureException
	 */
	public void updateGroupInfo(GroupInfo groupInfo) throws FutureException;
	
	/**
	 * 删除用户组信息
	 * @param id
	 * @throws FutureException
	 */
	public void daleteGroupInfo(String id) throws FutureException;
	

}
