package com.bohai.subAccount;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * Unit test for simple App.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JDBCTest extends AbstractJUnit4SpringContextTests{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private SqlSessionTemplate sessionTemplate;
	
	@Before
	public void prepare(){
		System.out.println("t_future_market表共有： "+JdbcTestUtils.countRowsInTable(jdbcTemplate, "t_future_market")+"条数据");
		
	}
	
	@Test
	public void test(){
		System.out.println(123);
	}

}
