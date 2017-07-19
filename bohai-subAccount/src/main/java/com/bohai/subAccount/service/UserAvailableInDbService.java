package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.Useravailableindb;
import com.bohai.subAccount.exception.FutureException;

public interface UserAvailableInDbService {
	
	/**
	 * 根据用户ID查询盘后可用资金
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public Useravailableindb getUserByUserName(String subuserid) throws FutureException;
	
	public List<Useravailableindb> selectAll() throws FutureException;
	
	/**
	 * 保存盘后可用资金
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveUseravailableindb(Useravailableindb useravailableindb) throws FutureException;
	
	
	
	/**
	 * 删除盘后可用资金
	 * @param id
	 * @throws FutureException
	 */
	public void deleteAll() throws FutureException;
	
}
