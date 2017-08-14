package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.UserLoginMapper;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserLoginService;

@Service("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {
	
	static Logger logger = Logger.getLogger(UserLoginServiceImpl.class);
	
	@Autowired
	private UserLoginMapper userLoginMapper;

	@Override
	public List<UserLogin> getUserByUserName(String userName) throws FutureException  {

		logger.info("getUserByUserName入參：userName = "+userName);
		
		List<UserLogin> list = null;
		try {
			list = userLoginMapper.getUserByUserName(userName);
		} catch (Exception e) {
			logger.error("查询失败",e);
			throw new FutureException("","查询失败");
		}
		
		return list;
	}

	@Override
	public void saveUserLogin(UserLogin userLogin) throws FutureException {
		logger.info("saveUserLogin入參："+JSON.toJSONString(userLogin));
		try {
			userLoginMapper.insertSelective(userLogin);
		} catch (Exception e) {
			logger.error("插入失败",e);
			throw new FutureException("","插入失败");
		}
		

	}

	@Override
	public void updateUserLogin(UserLogin userLogin) throws FutureException {
		logger.info("updateUserLogin入參："+JSON.toJSONString(userLogin));
		try {
			userLoginMapper.update(userLogin);
		} catch (Exception e) {
			logger.error("更新失败",e);
			throw new FutureException("","更新失败");
		}

	}

	@Override
	public void deleteUserLogin(String userName) throws FutureException {
		logger.info("deleteUserLogin入參："+userName);
		try {
			userLoginMapper.delete(userName);
		} catch (Exception e) {
			logger.error("删除失败",e);
			throw new FutureException("","删除失败");
		}

	}

}
