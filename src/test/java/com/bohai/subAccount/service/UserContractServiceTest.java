package com.bohai.subAccount.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.exception.FutureException;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserContractServiceTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private UserContractService userContractService;
	
	@Test
	public void queryUserContractByUserName() throws FutureException{
		
		System.out.println(JSON.toJSON(this.userContractService.queryUserContractByUserName("caojia")));
	}
	
	
	@Test
	public void queryUserContractByAll() throws FutureException{
		
		
		System.out.println(JSON.toJSONString(this.userContractService.queryUserContractByAll()));
	}
}
