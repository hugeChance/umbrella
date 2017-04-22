package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.CapitalRateMapper;
import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CapitalRateService;

@Service("CapitalRateService")
public class CapitalRateServiceImpl implements CapitalRateService {
	
	static Logger logger = Logger.getLogger(CapitalRateServiceImpl.class);
	
	@Autowired
	private CapitalRateMapper capitalRateMapper;

	@Override
	public List<CapitalRate> getUserByUserName(String subuserid)throws FutureException  {

		logger.info("Order getUserByUserName入參：userName = "+subuserid);
		
		List<CapitalRate> list = null;
		try {
			list = capitalRateMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询order失败",e);
			throw new FutureException("","查询order失败");
		}
		
		return list;
	}


	@Override
	public void saveOrder(CapitalRate capitalRate) throws FutureException {
		logger.info("order入參："+JSON.toJSONString(capitalRate));
		try {
			capitalRateMapper.insert(capitalRate);
		} catch (Exception e) {
			logger.error("插入order失败",e);
			throw new FutureException("","插入order失败");
		}
		
	}

}
