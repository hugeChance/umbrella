package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.CtptradingaccountMapper;
import com.bohai.subAccount.entity.Ctptradingaccount;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CtpTradingAccountService;
import com.bohai.subAccount.service.OrderService;

@Service("ctpTradingAccountService")
public class CtpTradingAccountServiceImpl implements CtpTradingAccountService {
	
	static Logger logger = Logger.getLogger(CtpTradingAccountServiceImpl.class);
	
	@Autowired
	private CtptradingaccountMapper ctptradingaccountMapper;

	@Override
	public List<Ctptradingaccount> getUserByUserName(String userName)throws FutureException  {

		logger.info("ctpTradingAccountService getUserByUserName入參：userName = "+userName);
		
		List<Ctptradingaccount> list = null;
		try {
			list = ctptradingaccountMapper.getUserByUserName(userName);
		} catch (Exception e) {
			logger.error("查询ctpTradingAccountService失败",e);
			throw new FutureException("","查询ctpTradingAccountService失败");
		}
		
		return list;
	}

	@Override
	public void saveCtptradingaccount(Ctptradingaccount ctptradingaccount) throws FutureException {
		logger.info("ctpTradingAccountService入參："+JSON.toJSONString(ctptradingaccount));
		try {
			ctptradingaccountMapper.insert(ctptradingaccount);
		} catch (Exception e) {
			logger.error("插入order失败",e);
			throw new FutureException("","插入order失败");
		}
		

	}

	@Override
	public void updateCtptradingaccount(Ctptradingaccount ctptradingaccount) throws FutureException {
		logger.info("ctptradingaccountMapper入參："+JSON.toJSONString(ctptradingaccount));
		try {
			ctptradingaccountMapper.update(ctptradingaccount);
		} catch (Exception e) {
			logger.error("更新ctptradingaccount失败",e);
			throw new FutureException("","更新ctptradingaccount失败");
		}

	}

	@Override
	public void deleteCtptradingaccount(String userName) throws FutureException {
		logger.info("deletectptradingaccount入參："+userName);
		try {
			ctptradingaccountMapper.delete(userName);
		} catch (Exception e) {
			logger.error("删除ctptradingaccount失败",e);
			throw new FutureException("","删除ctptradingaccount失败");
		}

	}

}
