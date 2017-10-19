package com.bohai.subAccount.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface InvestorPositionOldMapper {
    /**
     * 删除持仓为0的数据
     * @return 删除条数
     */
    @Delete("delete from T_INVESTOR_OLD_POSITION ")
    int deletePosition();
    
    @Insert("insert into T_INVESTOR_OLD_POSITION select * from T_INVESTOR_POSITION")
    int insertPosition();
    
    @Select("select * from T_INVESTOR_OLD_POSITION")
    List<Map<String,Object>> selectOldPosition ();
    
    @Select("select INSTRUMENTID,POSIDIRECTION,sum(POSITION) as POSITION from T_INVESTOR_OLD_POSITION group by INSTRUMENTID,POSIDIRECTION")
    List<Map<String,Object>> selectOldGroupByPosition ();

}