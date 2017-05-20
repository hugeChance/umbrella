package com.bohai.subAccount.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.CloseDetailMapper;
import com.bohai.subAccount.entity.CloseDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CloseDetailService;

@Service("CloseDetailService")
public class CloseDetailServiceImpl implements CloseDetailService {
	
	static Logger logger = Logger.getLogger(CloseDetailServiceImpl.class);
	
	@Autowired
	private CloseDetailMapper closeDetailMapper;

	@Override
	public void saveCloseDetail(CloseDetail CloseDetail) throws FutureException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCloseDetail(CloseDetail CloseDetail) throws FutureException {
		// TODO Auto-generated method stub
		
	}

	

	

	
}
