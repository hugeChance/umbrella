package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.Trade2;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;

public interface TradeService {
	
	/**
	 * 根据用户ID查询成交回报
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<Trade> getUserByUserName2(String subuserid) throws FutureException;
	
	/**
	 * 根据用户ID查询成交数
	 * @param subuserids
	 * @return
	 * @throws FutureException
	 */
	public List<Trade2> getVolumes(String subuserid) throws FutureException;
	
	
	/**
	 * 保存成交回报
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveTrade(Trade Trade) throws FutureException;
	
	/**
	 * 更新成交回报
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateTrade(Trade Trade) throws FutureException;
	
	/**
	 * 删除成交回报
	 * @param id
	 * @throws FutureException
	 */
	public void deleteTrade(String subuserid) throws FutureException;
	
}
