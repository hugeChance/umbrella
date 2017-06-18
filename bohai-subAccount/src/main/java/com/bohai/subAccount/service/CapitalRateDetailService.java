package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.CapitalRateDetail;
import com.bohai.subAccount.exception.FutureException;

public interface CapitalRateDetailService {
	
	/**
	 * 根据用户ID查询交易员资金
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<CapitalRateDetail> getUserByUserName(String subuserid) throws FutureException;
	
	/**
	 * 保存CTP返回报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveCapitalRateDetail(CapitalRateDetail capitalRateDetail) throws FutureException;
	
	
	
	
}
