package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.SellDetailMapper;
import com.bohai.subAccount.entity.SellDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.SellDetailService;

@Service("SellDetailService")
public class SellDetailServiceImpl implements SellDetailService {
	
	static Logger logger = Logger.getLogger(SellDetailServiceImpl.class);
	
	@Autowired
	private SellDetailMapper sellDetailMapper;

	@Override
	public void saveSellDetail(SellDetail sellDetail)  throws FutureException {
		try {
			sellDetailMapper.insert(sellDetail);
		} catch (Exception e) {
			logger.error("插入saveSellDetail失败",e);
			throw new FutureException("","插入saveSellDetail失败");
		}
		
	}

	@Override
	public List<SellDetail> getSellDetail(String strdate)   throws FutureException {
		logger.info("SellDetailService getSellDetail入參：strdate = "+strdate);
		
		List<SellDetail> list = null;
		
		try {
			list = sellDetailMapper.getUserByDate(strdate);
		} catch (Exception e) {
			logger.error("查询getSellDetail失败",e);
			throw new FutureException("","查询getSellDetail失败");
		}
		
		return list;
		
	}

	


	

	
}
