package com.bohai.subAccount.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.exception.FutureException;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class InvestorPositionServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	private InvestorPositionService investorPositionService;
	
	@Test
	public void subTractionPosition() throws FutureException{
		
		InvestorPosition investorPosition = new InvestorPosition();
		
		investorPosition.setCloseamount(new BigDecimal("4276"));
		investorPosition.setClosevolume((long)1);
		investorPosition.setFrontid(new BigDecimal("1"));
		investorPosition.setInstrumentid("a1705");
		investorPosition.setSessionid(new BigDecimal("-530357006"));
		investorPosition.setOrderref("23");
		this.investorPositionService.subTractionPosition(investorPosition);
	}
	
	@Test
	public void getSubUserPostion2() throws FutureException{
		
		System.out.println(JSON.toJSONString(this.investorPositionService.getSubUserPostion2("test3", "m1705", "1")));
	}

}
