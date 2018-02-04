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
	 * 取得平仓记录上海以外
	 * @param userInfo
	 * @throws FutureException
	 */
	public List<SellDetail> getSellDetail(String strdate) throws FutureException;
	
	/**
	 * 取得平仓记录上海平今
	 * @param userInfo
	 * @throws FutureException
	 */
	public List<SellDetail> getUserByDateForSH3(String strdate) throws FutureException;
	
	/**
	 * 取得平仓记录上海平昨
	 * @param userInfo
	 * @throws FutureException
	 */
	public List<SellDetail> getUserByDateForSH4(String strdate) throws FutureException;
	
	/**
	 * 按子账号取得平仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public List<SellDetail> getSellDetailForUser(String subUserId,String strdate) throws FutureException;
	
}
