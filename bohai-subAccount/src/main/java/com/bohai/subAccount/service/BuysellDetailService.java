package com.bohai.subAccount.service;

import com.bohai.subAccount.entity.BuysellDetail;
import com.bohai.subAccount.exception.FutureException;

public interface BuysellDetailService {
	
	/**
	 * 保存开仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveBuysellDetail(BuysellDetail BuysellDetail) throws FutureException;
	
	/**
	 * 更新开仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateBuysellDetail(BuysellDetail BuysellDetail) throws FutureException;
	
	
	
}
