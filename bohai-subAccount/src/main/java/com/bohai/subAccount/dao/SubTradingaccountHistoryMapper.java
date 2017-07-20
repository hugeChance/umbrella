package com.bohai.subAccount.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.vo.UserAccountVO;

public interface SubTradingaccountHistoryMapper {
	
	@Insert("insert into T_SUB_TRADINGACCOUNT_HISTORY select t.*,to_char(sysdate,'yyyymmdd') from T_SUB_TRADINGACCOUNT t ")
    int backup();
    
    
}