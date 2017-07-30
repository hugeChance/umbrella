package com.bohai.subAccount.service;

import java.util.List;

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
	public void doFindPositionsDetail(String  Subuserid,String Combokey,String Direction,String Instrumentid,long Volume) throws FutureException;
	
	
	public void updateBuyDetail(String Combokey,String SellCombokey,int Volume)throws FutureException;

	void updateBuySell(BuyDetail buyDetail)throws FutureException;
	
	List<BuyDetail> getBuyDetailForComboKey(String comboKey) throws FutureException;

	
}
