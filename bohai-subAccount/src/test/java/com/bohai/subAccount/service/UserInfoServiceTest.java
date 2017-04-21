package com.bohai.subAccount.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.bohai.subAccount.exception.FutureException;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserInfoServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Test
	public void checkUser() throws FutureException{
		
		int s = 0;
			s= this.userInfoService.checkUser("caojia", "caojia");
		System.out.println(s);
	}

}
