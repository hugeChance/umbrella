package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;

public interface CapitalRateService {
	
	/**
	 * 根据用户ID查询交易员资金
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public CapitalRate getUserByUserName(String subuserid) throws FutureException;
	
	/**
	 * 保存交易员资金
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveCapitalRate(CapitalRate capitalRate) throws FutureException;
	
	/**
	 * 更新交易员资金
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateCapitalRate(CapitalRate capitalRate) throws FutureException;
	
	/**
	 * 删除交易员资金
	 * @param id
	 * @throws FutureException
	 */
	public void deleteCapitalRate(String subuserid) throws FutureException;
	
	/**
	 * 入金
	 * @param id
	 * @throws FutureException
	 */
	public void addCapitalRate(CapitalRate capitalRate) throws FutureException;
	
	/**
	 * 出金
	 * @param id
	 * @throws FutureException
	 */
	public void distractCapitalRate(CapitalRate capitalRate) throws FutureException;
	
	
}
