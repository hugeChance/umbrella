package com.bohai.subAccount.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.InvestorPosition2;

public interface InvestorPositionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_INVESTOR_POSITION
     *
     * @mbggenerated Mon Feb 20 16:25:22 CST 2017
     */
    int insert(InvestorPosition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_INVESTOR_POSITION
     *
     * @mbggenerated Mon Feb 20 16:25:22 CST 2017
     */
    int insertSelective(InvestorPosition record);
    
    @Select("select count(1) from T_INVESTOR_POSITION where SUBUSERID = #{0}")
    int countByUserName(String name);
    
    List<InvestorPosition> getUserByUserName(String subuserid);
    
    List<InvestorPosition2> getUserByUserName2(String subuserid);
    
    List<InvestorPosition2> getUserByUserName3(String subuserid);
    
    int update(InvestorPosition record);
    
    int delete(String userName);
    
    int subTractionPosition(InvestorPosition record);
    
    InvestorPosition getUserByOpenPosition(InvestorPosition record);
    
    int updateOpenPosition(InvestorPosition record);
    
    int updateClosePosition(InvestorPosition record);
    
    List<InvestorPosition> getSubUserPostion(String subuserid);
    
    List<InvestorPosition> getSubUserPostion2(String subuserid, String instrumentid, String posidirection);
    
    /**
     * 删除持仓为0的数据
     * @return 删除条数
     */
    @Delete("delete from T_INVESTOR_POSITION ")
    int deleteNoPosition();
    
    List<InvestorPosition> getUserClosePostion(String userName);
    
    /**
     * @author caojia
     * 查询客户持仓信息（不包含已平持仓）
     * @param userName
     * @return
     */
    List<InvestorPosition> getUserUnClosePostion(String userName);
}