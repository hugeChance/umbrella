package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.vo.UserContractTradeRule;

public interface TradeRuleService {
	
	/**
	 * 根据用户编号查询交易规则
	 * @param userNo
	 * @return
	 * @throws FutureException
	 */
	public List<UserContractTradeRule> getTradeRulesByUserNo(String userNo) throws FutureException;
	
	/**
	 * 根据组ID查询交易规则
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<UserContractTradeRule> getTradeRulesByGroupId(String groupId) throws FutureException;

	/**
	 * 保存交易规则
	 * @param rule
	 * @throws FutureException
	 */
	public void saveOrUpdateTradeRule(TradeRule rule) throws FutureException;
	
	/**
	 * 更新交易规则
	 * @param rule
	 * @throws FutureException
	 */
	public void updateTradeRule(TradeRule rule) throws FutureException;
	
	/**
	 * 删除交易规则
	 * @param id
	 * @throws FutureException
	 */
	public void removeTradeRule(String id) throws FutureException;
	
	/**
	 * 删除交易规则并解除用户合约关系
	 * @param tradeRuleId
	 * @param userContractId
	 * @throws FutureException
	 */
	public void removeUserTradeRule(String tradeRuleId, String userContractId) throws FutureException;
	
	/**
	 * 查询交易规则
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<TradeRule> getTradeRulesByAll() throws FutureException;
}
