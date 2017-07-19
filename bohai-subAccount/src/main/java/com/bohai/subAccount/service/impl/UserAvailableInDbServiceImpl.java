package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.UseravailableindbMapper;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.Useravailableindb;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserAvailableInDbService;

@Service("UserAvailableInDbService")
public class UserAvailableInDbServiceImpl implements UserAvailableInDbService {
	
	static Logger logger = Logger.getLogger(UserAvailableInDbServiceImpl.class);

	@Autowired
	private UseravailableindbMapper useravailableindbMapper;
	private Useravailableindb useravailableindb;
	@Override
	public Useravailableindb getUserByUserName(String subuserid) throws FutureException {
		logger.info("Useravailableindb getUserByUserName入參：userName = "+subuserid);
		
		try {
			useravailableindb = useravailableindbMapper.selectByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询Useravailableindb getUserByUserName失败",e);
			throw new FutureException("","查询Useravailableindb getUserByUserName失败");
		}
		
		return useravailableindb;
	}

	@Override
	public List<Useravailableindb> selectAll() throws FutureException {
		logger.info("Useravailableindb selectAll入參：null");
		List<Useravailableindb> list = null;
		
		try {
			list = useravailableindbMapper.selectAll();
		} catch (Exception e) {
			logger.error("查询Useravailableindb selectAll失败",e);
			throw new FutureException("","查询Useravailableindb selectAll失败");
		}
		return list;
	}

	@Override
	public void saveUseravailableindb(Useravailableindb useravailableindb) throws FutureException {
		logger.info("saveUseravailableindb入參："+JSON.toJSONString(useravailableindb));
		try {
			useravailableindbMapper.insert(useravailableindb);
		} catch (Exception e) {
			logger.error("插入saveUseravailableindb失败",e);
			throw new FutureException("","插入saveUseravailableindb失败");
		}
		
	}

	@Override
	public void deleteAll() throws FutureException {
		logger.info("deleteAll入參：NULL");
		try {
			useravailableindbMapper.deleteAll();
		} catch (Exception e) {
			logger.error("删除deleteAll失败",e);
			throw new FutureException("","删除deleteAll失败");
		}
		
	}
	
	
}
