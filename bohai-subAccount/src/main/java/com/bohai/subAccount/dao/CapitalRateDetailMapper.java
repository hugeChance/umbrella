package com.bohai.subAccount.dao;

import com.bohai.subAccount.entity.CapitalRateDetail;

public interface CapitalRateDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_CAPITAL_RATE_DETAIL
     *
     * @mbggenerated Sun Jun 11 19:00:26 CST 2017
     */
    int deleteByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_CAPITAL_RATE_DETAIL
     *
     * @mbggenerated Sun Jun 11 19:00:26 CST 2017
     */
    int insert(CapitalRateDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_CAPITAL_RATE_DETAIL
     *
     * @mbggenerated Sun Jun 11 19:00:26 CST 2017
     */
    int insertSelective(CapitalRateDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_CAPITAL_RATE_DETAIL
     *
     * @mbggenerated Sun Jun 11 19:00:26 CST 2017
     */
    CapitalRateDetail selectByPrimaryKey(String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_CAPITAL_RATE_DETAIL
     *
     * @mbggenerated Sun Jun 11 19:00:26 CST 2017
     */
    int updateByPrimaryKeySelective(CapitalRateDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_CAPITAL_RATE_DETAIL
     *
     * @mbggenerated Sun Jun 11 19:00:26 CST 2017
     */
    int updateByPrimaryKey(CapitalRateDetail record);
}