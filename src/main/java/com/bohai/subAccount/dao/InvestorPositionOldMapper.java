package com.bohai.subAccount.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.InvestorPosition2;

public interface InvestorPositionOldMapper {
    /**
     * 删除持仓为0的数据
     * @return 删除条数
     */
    @Delete("delete from T_INVESTOR_OLD_POSITION ")
    int deletePosition();
    
    @Insert("insert into T_INVESTOR_OLD_POSITION select * from T_INVESTOR_POSITION")
    int insertPosition();

}