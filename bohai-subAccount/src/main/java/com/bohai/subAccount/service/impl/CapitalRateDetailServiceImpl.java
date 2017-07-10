package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.CapitalRateDetailMapper;
import com.bohai.subAccount.dao.CapitalRateMapper;
import com.bohai.subAccount.entity.CapitalRate;
import com.bohai.subAccount.entity.CapitalRateDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CapitalRateDetailService;
import com.bohai.subAccount.service.CapitalRateService;

@Service("CapitalRateDetailService")
public class CapitalRateDetailServiceImpl implements CapitalRateDetailService {
	
	static Logger logger = Logger.getLogger(CapitalRateDetailServiceImpl.class);
	
	@Autowired
	private CapitalRateDetailMapper capitalRateDetailMapper;

	@Override
	public List<CapitalRateDetail> getUserByUserName(String subuserid)throws FutureException  {

		logger.info("CapitalRateDetail getUserByUserName入參：userName = "+subuserid);
		
		List<CapitalRateDetail> list = null;
		try {
			list = capitalRateDetailMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询getUserByUserName失败",e);
			throw new FutureException("","查询getUserByUserName失败");
		}
		
		return list;
	}


	@Override
	public void saveCapitalRateDetail(CapitalRateDetail capitalRateDetail) throws FutureException {
		logger.info("saveCapitalRateDetail入參："+JSON.toJSONString(capitalRateDetail));
		try {
			capitalRateDetailMapper.insert(capitalRateDetail);
		} catch (Exception e) {
			logger.error("插入saveCapitalRateDetail失败",e);
			throw new FutureException("","配资表明细 出金插入数据失败！插入saveCapitalRateDetail失败");
		}
		
	}

}
