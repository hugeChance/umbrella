package com.bohai.subAccount.dao;

import org.apache.ibatis.annotations.Select;

import com.bohai.subAccount.entity.FutureMarket;

public interface FutureMarketMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_FUTURE_MARKET
     *
     * @mbggenerated Fri Jan 13 09:40:50 CST 2017
     */
    int insert(FutureMarket record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_FUTURE_MARKET
     *
     * @mbggenerated Fri Jan 13 09:40:50 CST 2017
     */
    int insertSelective(FutureMarket record);
    
    @Select(" select * from t_future_market where INSTRUMENT_ID = #{0} "
            + " and settlement_price is not null "
            + " and trading_day = to_char(sysdate,'yyyymmdd') "
            + " where rownum = 1 ")
    FutureMarket selectByInstrument2(String instrument);
    	
    
    @Select("select settlement_price from ( select settlement_price from t_future_market where INSTRUMENT_ID = #{0} "
            + "and settlement_price is not null "
            + "and trading_day = to_char(sysdate,'yyyymmdd') order by CREATE_TIME desc) "
            + "where rownum = 1 ")
    String selectByInstrument(String instrument);
    
    @Select("select PRE_SETTLEMENT_PRICE from ( select PRE_SETTLEMENT_PRICE from t_future_market where INSTRUMENT_ID = #{0} "
            + "and settlement_price is not null "
            + "and trading_day = to_char(sysdate,'yyyymmdd') order by CREATE_TIME desc) "
            + "where rownum = 1 ")
    String selectByInstrumentPre(String instrument);
    
    @Select("select 2 from dual")
    int selectdual();
}