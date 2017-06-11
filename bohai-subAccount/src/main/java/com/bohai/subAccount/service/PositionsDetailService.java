package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.exception.FutureException;

public interface PositionsDetailService {
	
	/**
	 * 
	 * @param 
	 * @throws FutureException
	 */
	public int doFindPositionsDetail(String  Subuserid,String Combokey,String Direction,String Instrumentid,int Volume) throws FutureException;
	
	public void updateVolumn(String subuserid,String Combokey,int volume) throws FutureException;
	
	public void deleteAll() throws FutureException;
	
	public void insertTodayPositions() throws FutureException;
	
	public List<PositionsDetail> getPositionsForUser(String subuserid,String dataString) throws FutureException;
}
