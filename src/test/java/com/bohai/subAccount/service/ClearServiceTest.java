package com.bohai.subAccount.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bohai.subAccount.dao.InputOrderHistoryMapper;
import com.bohai.subAccount.dao.InvestorPositionOldMapper;
import com.bohai.subAccount.dao.OrderHistoryMapper;
import com.bohai.subAccount.dao.SubTradingaccountHistoryMapper;
import com.bohai.subAccount.dao.TradeHistoryMapper;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ClearServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    
    public static SqlSessionFactory sqlSessionFactory;

    @Before
    public void init(){
        String resource = "mybatis/mybatis-config.xml";
        if(sqlSessionFactory ==null){
            
            try {
                InputStream inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void backUp(){
        
        SqlSession session = sqlSessionFactory.openSession();
        /*InputOrderHistoryMapper inputOrderHistoryMapper = session.getMapper(InputOrderHistoryMapper.class);
        inputOrderHistoryMapper.backup();
        
        OrderHistoryMapper orderHistoryMapper = session.getMapper(OrderHistoryMapper.class);
        orderHistoryMapper.backup();
        
        TradeHistoryMapper tradeHistoryMapper = session.getMapper(TradeHistoryMapper.class);
        tradeHistoryMapper.backup();
        
        SubTradingaccountHistoryMapper subTradingaccountHistoryMapper = session.getMapper(SubTradingaccountHistoryMapper.class);
        subTradingaccountHistoryMapper.backup();*/
        
        InvestorPositionOldMapper investorPositionOldMapper = session.getMapper(InvestorPositionOldMapper.class);
        investorPositionOldMapper.deletePosition();
        investorPositionOldMapper.insertPosition();
        session.commit();
    }

}
