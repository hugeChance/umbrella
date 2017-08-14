package com.bohai.subAccount.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class OrderServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	private OrderService orderService;
	
	@Test
	public void getUserCloseOrderByInstrumentIDAndDirection() throws FutureException{
		
		List<Order> list =  this.orderService.getUserCloseOrderByInstrumentIDAndDirection("朱俊辉", "rb1710", "0");
		System.out.println(JSON.toJSONString(list));
	}
	
	@Test
	public void getCanelByUserName() throws FutureException{
		
		List<Order> list = this.orderService.getCanelByUserName("朱俊辉");
		
		System.out.println(JSON.toJSONString(list));
	}

}
