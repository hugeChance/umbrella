package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.entity.PositionsDetail2;
import com.bohai.subAccount.exception.FutureException;

public interface PositionsDetailService {
	
	/**
	 * 
	 * @param 
	 * @throws FutureException
	 */
	public long doFindPositionsDetail(String  Subuserid,String Combokey,String Direction,String Instrumentid,long tmplong) throws FutureException;
	
	public void updateVolumn(String subuserid,String Combokey,long volume) throws FutureException;
	
	public void deleteAll() throws FutureException;
	
	public void insertTodayPositions() throws FutureException;
	
	public List<PositionsDetail> getPositionsForUser(String subuserid,String dataString) throws FutureException;
	
	public List<PositionsDetail2> findGroupByPositionsDetail() throws FutureException;
}
