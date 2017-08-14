package com.bohai.subAccount.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.CloseRuleMapper;
import com.bohai.subAccount.dao.TradeRuleMapper;
import com.bohai.subAccount.dao.UserContractMapper;
import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.vo.UserContractTradeRule;

/**
 * @author caojia
 */
@Service("tradeRuleService")
public class TradeRuleServiceImpl implements TradeRuleService {
	
	static Logger logger = Logger.getLogger(TradeRuleServiceImpl.class);

	@Autowired
	private TradeRuleMapper tradeRuleMapper;
	
	@Autowired
	private UserContractMapper userContractMapper;
	
	@Autowired
	private CloseRuleMapper closeRuleMapper;
	
	@Override
	public List<UserContractTradeRule> getTradeRulesByUserNo(String userNo) throws FutureException {

		logger.info("根据用户编号查询交易规则，userNo:"+userNo);
		List<UserContractTradeRule> list = null;
		
		try {
			list = this.tradeRuleMapper.selectTradeRulesByUserNo(userNo);
		} catch (Exception e) {
			logger.error("根据用户编号查询交易规则失败",e);
			throw new FutureException("", "根据用户编号查询交易规则失败");
		}
		
		return list;
	}

	@Override
	public List<UserContractTradeRule> getTradeRulesByGroupId(String groupId) throws FutureException {

		logger.info("根据用户组ID查询交易规则，groupId:"+groupId);
		List<UserContractTradeRule> list = null;
		
		try {
			list = this.tradeRuleMapper.selectTradeRulesByGroupId(groupId);
			logger.info("查询结果："+JSON.toJSONString(list));
		} catch (Exception e) {
			logger.error("根据用户组ID查询交易规则失败",e);
			throw new FutureException("", "根据用户组ID查询交易规则失败");
		}
		
		return list;
	}

	@Override
	public void saveOrUpdateTradeRule(TradeRule rule) throws FutureException {

		
		if(StringUtils.isEmpty(rule.getId())){
			logger.info("保存交易规则入参："+JSON.toJSONString(rule));
			try {
				this.tradeRuleMapper.insert(rule);
			} catch (Exception e) {
				logger.error("保存交易规则失败",e);
				throw new FutureException("", "保存交易规则失败");
			}
		}else {
			this.updateTradeRule(rule);
		}
	}

	@Override
	public void updateTradeRule(TradeRule rule) throws FutureException {

		logger.info("更新交易规则入参："+JSON.toJSONString(rule));
		try {
			this.tradeRuleMapper.updateByPrimaryKey(rule);
		} catch (Exception e) {
			logger.error("更新交易规则失败",e);
			throw new FutureException("", "更新交易规则失败");
		}

	}

	@Override
	public void removeTradeRule(String id) throws FutureException {

		logger.info("删除交易规则入参，ID:"+id);
		try {
			this.tradeRuleMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("删除交易规则失败",e);
			throw new FutureException("", "删除交易规则失败");
		}
		
	}

	@Override
	public void removeUserTradeRule(String tradeRuleId, String userContractId) throws FutureException {

		if(!StringUtils.isEmpty(tradeRuleId)){
			//this.removeTradeRule(tradeRuleId);
		}
		
		logger.info("删除用户合约关系，ID:"+userContractId);
		
		try {
			this.userContractMapper.deleteByPrimaryKey(userContractId);
		} catch (Exception e) {
			logger.error("删除用户合约关系失败",e);
			throw new FutureException("", "删除用户合约关系失败");
		}
		
	}

	@Override
	public List<TradeRule> getTradeRulesByAll() throws FutureException {
		logger.info("查询合约");
		List<TradeRule> list = null;
		
		try {
			list = this.tradeRuleMapper.selectTradeRulesByAll();
			logger.info("查询结果："+JSON.toJSONString(list));
		} catch (Exception e) {
			logger.error("查询交易规则失败",e);
			throw new FutureException("", "查询交易规则失败");
		}
		return list;
	}
	
	

}
