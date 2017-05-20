package com.bohai.subAccount.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.BuysellDetailMapper;
import com.bohai.subAccount.entity.BuysellDetail;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuysellDetailService;

@Service("BuysellDetailService")
public class BuysellDetailServiceImpl implements BuysellDetailService {
	
	static Logger logger = Logger.getLogger(BuysellDetailServiceImpl.class);
	
	@Autowired
	private BuysellDetailMapper buysellDetailMapper;

	@Override
	public void saveBuysellDetail(BuysellDetail BuysellDetail) throws FutureException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBuysellDetail(BuysellDetail BuysellDetail) throws FutureException {
		// TODO Auto-generated method stub
		
	}

	

	
}
