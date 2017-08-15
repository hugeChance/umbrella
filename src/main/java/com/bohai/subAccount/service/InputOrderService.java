package com.bohai.subAccount.service;

import java.util.List;
import java.util.Map;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;

public interface InputOrderService {
	
	/**
	 * 根据用户ID查询输入报单
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InputOrder> getUserByUserName(String subuserid) throws FutureException;
	
	
	/**
	 * 保存输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveInputOrder(InputOrder inputOrder) throws FutureException;
	
	/**
	 * 更新输入报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateInputOrder(InputOrder inputOrder) throws FutureException;
	
	/**
	 * 删除输入报单
	 * @param id
	 * @throws FutureException
	 */
	public void deleteInputOrder(String subuserid) throws FutureException;
	
	/**
	 * 查询SUBUSERID
	 * @param id
	 * @throws FutureException
	 */
	public String getSubUserID(int frontID,int sessionID,String orderRef) throws FutureException;
	
	/**
	 * 查询getSubUserInfo
	 * @param id
	 * @throws FutureException
	 */
	public InputOrder getSubUserInfo(Map<String,Object> map) throws FutureException;
	
	
}
