package com.bohai.subAccount.service.impl;

import java.math.BigDecimal;

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
	public CapitalRate getUserByUserName(String subuserid)throws FutureException  {

		logger.info("capitalRate getUserByUserName入參：userName = "+subuserid);
		
		CapitalRate capitalRate = null;
		try {
			capitalRate = capitalRateMapper.selectByPrimaryKey(subuserid);
		} catch (Exception e) {
			logger.error("查询capitalRate失败",e);
			throw new FutureException("","查询capitalRate失败");
		}
		
		return capitalRate;
	}


	@Override
	public void saveCapitalRate(CapitalRate capitalRate) throws FutureException {
		logger.info("capitalRate入參："+JSON.toJSONString(capitalRate));
		try {
			capitalRateMapper.insert(capitalRate);
		} catch (Exception e) {
			logger.error("插入capitalRate失败",e);
			throw new FutureException("","插入capitalRate失败");
		}
		
	}


	@Override
	public void updateCapitalRate(CapitalRate capitalRate) throws FutureException {
		logger.info("updateCapitalRate入參："+JSON.toJSONString(capitalRate));
		try {
			capitalRateMapper.updateByPrimaryKeySelective(capitalRate);
		} catch (Exception e) {
			logger.error("更新capitalRate失败",e);
			throw new FutureException("","更新capitalRate失败");
		}
		
	}


	@Override
	public void deleteCapitalRate(String subuserid) throws FutureException {
		logger.info("deleteCapitalRate入參："+subuserid);
		try {
			capitalRateMapper.deleteByPrimaryKey(subuserid);
		} catch (Exception e) {
			logger.error("删除capitalRate失败",e);
			throw new FutureException("","删除capitalRate失败");
		}
		
	}


	@Override
	public void addCapitalRate(CapitalRate capitalRate) throws FutureException {
		logger.info("addCapitalRate入參："+JSON.toJSONString(capitalRate));
		CapitalRate tmpcapitalRate = new CapitalRate();
		CapitalRate newcapitalRate = new CapitalRate();
		double userCapitaltmp = 0;
		double hostCapital1tmp = 0;
		
		try {
		
			//取得已有配资表总览的资金
			tmpcapitalRate = capitalRateMapper.selectByPrimaryKey(capitalRate.getUserName());
			userCapitaltmp = capitalRate.getUserCapital().doubleValue();
			hostCapital1tmp = capitalRate.getHostCapital1().doubleValue();
			userCapitaltmp = userCapitaltmp + tmpcapitalRate.getUserCapital().doubleValue();
			hostCapital1tmp = hostCapital1tmp + tmpcapitalRate.getHostCapital1().doubleValue();
			
			newcapitalRate.setUserName(capitalRate.getUserName());
			newcapitalRate.setUpdateTime(capitalRate.getUpdateTime());
			newcapitalRate.setUserCapital(new BigDecimal(userCapitaltmp));
			newcapitalRate.setUserCapitalRate(tmpcapitalRate.getUserCapitalRate());
			newcapitalRate.setHostCapital1(new BigDecimal(hostCapital1tmp));
			capitalRateMapper.updateByPrimaryKeySelective(newcapitalRate);
		} catch (Exception e) {
			logger.error("插入addCapitalRate失败！",e);
			throw new FutureException("","插入addCapitalRate失败！");
		}
		
	}


	@Override
	public void distractCapitalRate(CapitalRate capitalRate) throws FutureException {
		logger.info("distractCapitalRate入參："+JSON.toJSONString(capitalRate));
		CapitalRate tmpcapitalRate = new CapitalRate();
		CapitalRate newcapitalRate = new CapitalRate();
		double userCapitaltmp = 0;
		double hostCapital1tmp = 0;
		
		try {
		
			//取得已有配资表总览的资金
			tmpcapitalRate = capitalRateMapper.selectByPrimaryKey(capitalRate.getUserName());
			userCapitaltmp = capitalRate.getUserCapital().doubleValue() * -1 ;
			hostCapital1tmp = capitalRate.getHostCapital1().doubleValue() * -1;
			userCapitaltmp = userCapitaltmp + tmpcapitalRate.getUserCapital().doubleValue();
			hostCapital1tmp = hostCapital1tmp + tmpcapitalRate.getHostCapital1().doubleValue();
			
			newcapitalRate.setUserName(capitalRate.getUserName());
			newcapitalRate.setUpdateTime(capitalRate.getUpdateTime());
			newcapitalRate.setUserCapital(new BigDecimal(userCapitaltmp));
			newcapitalRate.setUserCapitalRate(tmpcapitalRate.getUserCapitalRate());
			newcapitalRate.setHostCapital1(new BigDecimal(hostCapital1tmp));
			capitalRateMapper.updateByPrimaryKeySelective(newcapitalRate);
		} catch (Exception e) {
			logger.error("插入distractCapitalRate失败！",e);
			throw new FutureException("","插入distractCapitalRate失败！");
		}
		
	}

}
