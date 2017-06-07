package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.SellDetail;
import com.bohai.subAccount.exception.FutureException;

public interface SellDetailService {
	
	/**
	 * 保存平仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveSellDetail(SellDetail sellDetail) throws FutureException;
	
	/**
	 * 取得平仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public List<SellDetail> getSellDetail(String strdate) throws FutureException;
	
	
	
}
