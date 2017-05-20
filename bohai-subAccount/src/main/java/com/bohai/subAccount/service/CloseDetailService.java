package com.bohai.subAccount.service;

import com.bohai.subAccount.entity.CloseDetail;
import com.bohai.subAccount.exception.FutureException;

public interface CloseDetailService {
	
	/**
	 * 保存平仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveCloseDetail(CloseDetail CloseDetail) throws FutureException;
	
	/**
	 * 更新平仓记录
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateCloseDetail(CloseDetail CloseDetail) throws FutureException;
	
	
	
}
