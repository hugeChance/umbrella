package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.InputOrderMapper;
import com.bohai.subAccount.dao.UserLoginMapper;
import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.InputOrderService;

@Service("inputOrderService")
public class InputOrderServiceImpl implements InputOrderService {
	
	static Logger logger = Logger.getLogger(InputOrderServiceImpl.class);
	
	@Autowired
	private InputOrderMapper inputOrderMapper;

	@Override
	public List<InputOrder> getUserByUserName(String subuserid) throws FutureException  {

		logger.info("InputOrder getUserByUserName入參：userName = "+subuserid);
		
		List<InputOrder> list = null;
		try {
			list = inputOrderMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询InputOrder失败",e);
			throw new FutureException("","查询InputOrder失败");
		}
		
		return list;
	}

	@Override
	public void saveInputOrder(InputOrder inputOrder) throws FutureException {
		logger.info("saveInputOrder入參："+JSON.toJSONString(inputOrder));
		try {
			inputOrderMapper.insert(inputOrder);
		} catch (Exception e) {
			logger.error("插入InputOrder失败",e);
			throw new FutureException("","插入InputOrder失败");
		}
		

	}

	@Override
	public void updateInputOrder(InputOrder inputOrder) throws FutureException {
		logger.info("updateInputOrder入參："+JSON.toJSONString(inputOrder));
		try {
			inputOrderMapper.update(inputOrder);
		} catch (Exception e) {
			logger.error("更新InputOrder失败",e);
			throw new FutureException("","更新InputOrder失败");
		}

	}

	@Override
	public void deleteInputOrder(String subuserid) throws FutureException {
		logger.info("deleteInputOrder入參："+subuserid);
		try {
			inputOrderMapper.delete(subuserid);
		} catch (Exception e) {
			logger.error("删除失败",e);
			throw new FutureException("","删除失败");
		}

	}
	
	public String getSubUserID(int frontID,int sessionID,String orderRef) throws FutureException{
		String retStr ="";
		logger.info("getSubUserID入參："+frontID + ":" + sessionID + ":" + orderRef) ;
		try {
			retStr = inputOrderMapper.getSubUserID(frontID,sessionID,orderRef);
		} catch (Exception e) {
			logger.error("getSubUserID失败",e);
			throw new FutureException("","getSubUserID失败");
		}
		return retStr;
	}
	

}
