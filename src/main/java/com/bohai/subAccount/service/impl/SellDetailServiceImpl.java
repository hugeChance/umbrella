package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.SellDetailMapper;
import com.bohai.subAccount.entity.SellDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.SellDetailService;

@Service("sellDetailService")
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
	
	

	@Override
	public List<SellDetail> getSellDetailForUser(String subUserId, String strdate) throws FutureException {
		logger.info("getSellDetailForUser getSellDetailForUser入參：strdate = "+strdate + ",subUserId = " + subUserId);
		
		List<SellDetail> list = null;
		
		try {
			list = sellDetailMapper.getSellDetailForUser(subUserId,strdate);
		} catch (Exception e) {
			logger.error("查询getSellDetail失败",e);
			throw new FutureException("","查询getSellDetail失败");
		}
		
		return list;
	}

	@Override
	public List<SellDetail> getUserByDateForSH3(String strdate) throws FutureException {
		logger.info("SellDetailService getUserByDateForSH3入參：strdate = "+strdate);
		
		List<SellDetail> list = null;
		
		try {
			list = sellDetailMapper.getUserByDateForSH3(strdate);
		} catch (Exception e) {
			logger.error("查询getUserByDateForSH3失败",e);
			throw new FutureException("","查询getUserByDateForSH3失败");
		}
		
		return list;
	}

	@Override
	public List<SellDetail> getUserByDateForSH4(String strdate) throws FutureException {
		logger.info("SellDetailService getUserByDateForSH4入參：strdate = "+strdate);
		
		List<SellDetail> list = null;
		
		try {
			list = sellDetailMapper.getUserByDateForSH4(strdate);
		} catch (Exception e) {
			logger.error("查询getUserByDateForSH4失败",e);
			throw new FutureException("","查询getUserByDateForSH4失败");
		}
		
		return list;
	}

	


	

	
}
