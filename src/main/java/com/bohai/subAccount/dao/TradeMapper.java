package com.bohai.subAccount.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;

import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.Trade2;

public interface TradeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_TRADE
     *
     * @mbggenerated Wed Feb 15 10:09:24 CST 2017
     */
    int insert(Trade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_TRADE
     *
     * @mbggenerated Wed Feb 15 10:09:24 CST 2017
     */
    int insertSelective(Trade record);

//    List<Trade> getUserByUserNames(String[] userNames);
    
    List<Trade> getUserByUserName2(String userName);
    
    List<Trade2> getVolumes(String userName);
    
    int update(Trade record);
    
    int delete(String userName);
    
    /**
     * 删除所有数据
     * @return 删除条数
     */
    @Delete("delete from t_trade")
    int deleteAll();
}