package com.bohai.subAccount.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.SubTradingaccountMapper;
import com.bohai.subAccount.dao.UserLoginMapper;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.SubTradingaccountService;
import com.bohai.subAccount.vo.UserAccountVO;

@Service("subTradingaccountService")
public class SubTradingaccountServiceImpl implements SubTradingaccountService {
	
	static Logger logger = Logger.getLogger(SubTradingaccountServiceImpl.class);
	
	@Autowired
	private SubTradingaccountMapper subTradingaccountMapper;

	@Override
	public List<SubTradingaccount> getUserByUserName(String subuserid) throws FutureException  {

		logger.info("getUserByUserName getUserByUserName入參：userName = "+subuserid);
		
		List<SubTradingaccount> list = null;
		try {
			list = subTradingaccountMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询SubTradingaccount失败",e);
			throw new FutureException("","查询SubTradingaccount失败");
		}
		
		return list;
	}

	@Override
	public void saveSubTradingaccount(SubTradingaccount subTradingaccount) throws FutureException {
		logger.info("saveSubTradingaccount入參："+JSON.toJSONString(subTradingaccount));
		try {
			subTradingaccountMapper.insert(subTradingaccount);
		} catch (Exception e) {
			logger.error("插入SubTradingaccount失败",e);
			throw new FutureException("","插入SubTradingaccount失败");
		}
		

	}

	@Override
	public void updateSubTradingaccount(SubTradingaccount subTradingaccount) throws FutureException {
		logger.info("updateSubTradingaccount入參："+JSON.toJSONString(subTradingaccount));
		try {
			subTradingaccountMapper.update(subTradingaccount);
		} catch (Exception e) {
			logger.error("更新SubTradingaccount失败",e);
			throw new FutureException("","更新SubTradingaccount失败");
		}

	}

	@Override
	public void deleteSubTradingaccount(String subuserid) throws FutureException {
		logger.info("deleteSubTradingaccount入參："+subuserid);
		try {
			subTradingaccountMapper.delete(subuserid);
		} catch (Exception e) {
			logger.error("删除失败",e);
			throw new FutureException("","删除失败");
		}

	}
	
	
	public UserAccountVO getUserByUserName10(String subuserid) throws FutureException {
		logger.info("getUserByUserName2 getUserByUserName10入參：userName = "+subuserid);
		
		UserAccountVO subTradingaccount = null;
		try {
			subTradingaccount = subTradingaccountMapper.getUserByUserName10(subuserid);
		} catch (Exception e) {
			logger.error("查询SubTradingaccount失败",e);
			throw new FutureException("","查询SubTradingaccount失败");
		}
		
		return subTradingaccount;
	}

	@Override
	public int setFrozen(Map<String,Object> map) throws FutureException {
		logger.info("setFrozen T_SUB_TRADINGACCOUNT冻结保证金入參：userName = "+map.get("subuserid")+":"+map.get("heyuebaozhengjin")+":"+map.get("shouxufeikaicang"));
		int reti = 0;
		try {
			reti = subTradingaccountMapper.setFrozen(map);
		} catch (Exception e) {
			logger.error("查询SubTradingaccount失败",e);
			throw new FutureException("","查询SubTradingaccount失败");
		}
		return reti;
	}

	@Override
	public List<SubTradingaccount> getUserByAllForAccount() throws FutureException {
		// TODO Auto-generated method stub
        logger.info("getUserByAllForAccount入參：无 = ");
		
		List<SubTradingaccount> list = null;
		try {
			list = subTradingaccountMapper.getUserByAllForAccount();
		} catch (Exception e) {
			logger.error("查询SubTradingaccount失败",e);
			throw new FutureException("","查询SubTradingaccount失败");
		}
		
		return list;
	}

	@Override
	public int updateUnfrozen(Map<String, Object> map) throws FutureException {
		logger.info("updateUnfrozen冻结保证金入參：userName = "+map.get("subAccount")+":"+map.get("volume")+":"+map.get("frozenmargin")+":"+map.get("frozencommission"));
		try {
			subTradingaccountMapper.updateUnfrozen(map);
		} catch (Exception e) {
			logger.error("查询SubTradingaccount失败",e);
			throw new FutureException("","查询SubTradingaccount失败");
		}
		return 0;
	}

	@Override
	public int setCommission(Map<String,Object> map) throws FutureException {
		logger.info("setCommission入參："+map.get("subAccount")+":"+String.valueOf(map.get("commission")));
		try {
			subTradingaccountMapper.setCommission(map);
		} catch (Exception e) {
			logger.error("插入setCommission失败",e);
			throw new FutureException("","插入setCommission失败");
		}
		return 0;
	}

}
