package com.bohai.subAccount.dao;

import java.util.List;

import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.entity.PositionsDetail;

public interface BuyDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_BUY_DETAIL
     *
     * @mbggenerated Sun Jun 04 13:56:33 CST 2017
     */
    int insert(BuyDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_BUY_DETAIL
     *
     * @mbggenerated Sun Jun 04 13:56:33 CST 2017
     */
    int insertSelective(BuyDetail record);
    
    void updateBuyDetail(BuyDetail buyDetail);
    
    List<BuyDetail> findBuyDetail(String Subuserid,String Instrumentid,String Direction);
    
    void updateBuySell(BuyDetail buyDetail);
    
    List<BuyDetail> getBuyDetailForComboKey(String comboKey);
    
    List<BuyDetail> getBuyDetailForComboKey2(String comboKey);
}