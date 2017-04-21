package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.TradeMapper;
import com.bohai.subAccount.dao.UserLoginMapper;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.Trade2;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.TradeService;

@Service("tradeService")
public class TradeServiceImpl implements TradeService {
	
	static Logger logger = Logger.getLogger(TradeServiceImpl.class);
	
	@Autowired
	private TradeMapper TradeMapper;

	@Override
	public List<Trade> getUserByUserName2(String subuserid) throws FutureException  {

		logger.info("Trade getUserByUserName入參：userName = "+subuserid);
		
		List<Trade> list = null;
		try {
			list = TradeMapper.getUserByUserName2(subuserid);
		} catch (Exception e) {
			logger.error("查询Trade失败",e);
			throw new FutureException("","查询Trade失败");
		}
		
		return list;
	}
	
	@Override
	public List<Trade2> getVolumes(String subuserid) throws FutureException  {

		logger.info("Trade getVolumes入參：userName = "+subuserid);
		
		List<Trade2> list = null;
		try {
			list = TradeMapper.getVolumes(subuserid);
		} catch (Exception e) {
			logger.error("查询Trade失败",e);
			throw new FutureException("","查询Trade失败");
		}
		
		return list;
	}

//	@Override
//	public List<Trade> getUserByUserNames(String[] subuserids) throws FutureException  {
//
//		logger.info("Trade getUserByUserName入參：userName = "+subuserids);
//		
//		List<Trade> list = null;
//		try {
//			list = TradeMapper.getUserByUserNames(subuserids);
//		} catch (Exception e) {
//			logger.error("查询Trade失败",e);
//			throw new FutureException("","查询Trade失败");
//		}
//		
//		return list;
//	}
	
	@Override
	public void saveTrade(Trade Trade) throws FutureException {
		logger.info("saveTrade入參："+JSON.toJSONString(Trade));
		try {
			TradeMapper.insert(Trade);
		} catch (Exception e) {
			logger.error("插入Trade失败",e);
			throw new FutureException("","插入Trade失败");
		}
		

	}

	@Override
	public void updateTrade(Trade Trade) throws FutureException {
		logger.info("updateTrade入參："+JSON.toJSONString(Trade));
		try {
			TradeMapper.update(Trade);
		} catch (Exception e) {
			logger.error("更新Trade失败",e);
			throw new FutureException("","更新Trade失败");
		}

	}

	@Override
	public void deleteTrade(String subuserid) throws FutureException {
		logger.info("deleteTrade入參："+subuserid);
		try {
			TradeMapper.delete(subuserid);
		} catch (Exception e) {
			logger.error("删除Trade失败",e);
			throw new FutureException("","删除Trade失败");
		}

	}

}
