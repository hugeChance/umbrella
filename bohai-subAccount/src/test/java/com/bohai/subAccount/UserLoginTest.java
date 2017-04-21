package com.bohai.subAccount;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserLoginService;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserLoginTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	private UserLoginService userLoginService;
	
	@Test
	public void test(){
		
		UserLogin userLogin = new UserLogin();
		userLogin.setUserName("caoxx");
		userLogin.setUserPwd("caoxx");
		userLogin.setUserNo("12345");
		userLogin.setUpdateTime(new Date());
		
		try {
			this.userLoginService.saveUserLogin(userLogin);
		} catch (FutureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
