package com.bohai.subAccount.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.UserLogin;

public interface InputOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_INPUT_ORDER
     *
     * @mbggenerated Wed Feb 15 10:09:24 CST 2017
     */
    int insert(InputOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_INPUT_ORDER
     *
     * @mbggenerated Wed Feb 15 10:09:24 CST 2017
     */
    int insertSelective(InputOrder record);
    
    List<InputOrder> getUserByUserName(String userName);
    
    int update(InputOrder record);
    
    int delete(String userName);
    
    @Select("select SUBUSERID from T_INPUT_ORDER where FRONTID = #{0} and SESSIONID = #{1} and ORDERREF = #{2}"  )
    String getSubUserID(int frontID,int sessionID,String orderRef);
    
    InputOrder getSubUserInfo(Map<String,Object> map);
    
    /**
     * 删除所有数据
     * @return 删除条数
     */
    @Delete("delete from T_INPUT_ORDER")
    int deleteAll();
}