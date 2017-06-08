package com.bohai.subAccount.service;

import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.exception.FutureException;

public interface BuyDetailService {
	
	/**
	 * 保存开仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveBuyDetail(BuyDetail BuyDetail) throws FutureException;
	
	/**
	 * 更新开仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateBuyDetail(BuyDetail BuyDetail) throws FutureException;
	
	/**
	 * 查找平今仓
	 * @param userInfo
	 * @throws FutureException
	 */
	public void doFindPositionsDetail(String  Subuserid,String Combokey,String Direction,String Instrumentid,int Volume) throws FutureException;
	
	
	public void updateBuyDetail(String Combokey,String SellCombokey,int Volume);

	void updateBuySell(BuyDetail buyDetail);

	
}
