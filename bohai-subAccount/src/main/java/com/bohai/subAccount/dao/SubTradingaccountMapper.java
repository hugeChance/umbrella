package com.bohai.subAccount.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.vo.UserAccountVO;

public interface SubTradingaccountMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SUB_TRADINGACCOUNT
     *
     * @mbggenerated Thu Feb 09 17:23:29 CST 2017
     */
    int insert(SubTradingaccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SUB_TRADINGACCOUNT
     *
     * @mbggenerated Thu Feb 09 17:23:29 CST 2017
     */
    int insertSelective(SubTradingaccount record);
    
    int update(SubTradingaccount record);
    
    List<SubTradingaccount> getUserByUserName(String subuserid);
    
    int delete(String subuserid);
    
    UserAccountVO getUserByUserName10(String subuserid);
    
    int setFrozen(Map<String,Object> map);
    
    List<SubTradingaccount> getUserByAllForAccount();
    
    int updateUnfrozen(Map<String,Object> map);
    
    int setCommission(Map<String,Object> map);
    
    /**
     * 数据初始化
     * @return 更新条数
     */
    int init();
    
    @Update("update T_SUB_TRADINGACCOUNT set AVAILABLE = AVAILABLE + FROZENMARGIN + FROZENCOMMISSION")
    int updateCloseOper1();
    
    @Update("update T_SUB_TRADINGACCOUNT set FROZENMARGIN = '0',FROZENCOMMISSION = '0',CLOSEPROFIT = '0',COMMISSION = '0'")
    int updateCloseOper2();
    
    
}