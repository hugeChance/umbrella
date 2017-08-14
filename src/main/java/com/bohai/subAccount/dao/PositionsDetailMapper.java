package com.bohai.subAccount.dao;

import java.util.List;

import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.entity.PositionsDetail2;

public interface PositionsDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_POSITIONS_DETAIL
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    int insert(PositionsDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_POSITIONS_DETAIL
     *
     * @mbggenerated Sun Jun 04 12:42:18 CST 2017
     */
    List<PositionsDetail> findPositionsDetail(String Subuserid,String Direction, String Instrumentid);
    
    void updatePositionsDetail(PositionsDetail positionsDetail);
    
    void deleteAll();
    
    void insertTodayPositions();
    
    List<PositionsDetail> getPositionsForUser (String subuserid, String dataString);
    
    List<PositionsDetail2> findGroupByPositionsDetail();
    
    
}