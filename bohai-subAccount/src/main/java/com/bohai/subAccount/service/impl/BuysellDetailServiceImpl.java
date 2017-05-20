package com.bohai.subAccount.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.BuysellDetailMapper;
import com.bohai.subAccount.entity.BuysellDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuysellDetailService;

@Service("BuysellDetailService")
public class BuysellDetailServiceImpl implements BuysellDetailService {
	
	static Logger logger = Logger.getLogger(BuysellDetailServiceImpl.class);
	
	@Autowired
	private BuysellDetailMapper buysellDetailMapper;

	@Override
	public void saveBuysellDetail(BuysellDetail buysellDetail) throws FutureException {
		buysellDetailMapper.insert(buysellDetail);
		
	}

	@Override
	public void updateBuysellDetail(BuysellDetail buysellDetail) throws FutureException {
		//用户 ，平仓合约，平仓方向,平仓数量，平仓价格
		String subuserid = "";
		String closeINSTRUMENTID = "";
		String closeDIRECTION = "";
		String closeVOLUME ="";
		subuserid = buysellDetail.getSubuserid();
		closeINSTRUMENTID = buysellDetail.getInstrumentid();
		closeDIRECTION = buysellDetail.getDirection();
//		closeVOLUME = buysellDetail.getVolume2();
		
		
	}

	

	
}
