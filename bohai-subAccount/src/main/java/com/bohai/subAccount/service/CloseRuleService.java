package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.exception.FutureException;

public interface CloseRuleService {

	/**
	 * 保存平仓规则
	 * @param closeRule
	 * @throws FutureException
	 */
	public void saveCloseRule(CloseRule closeRule) throws FutureException;
	
	/**
	 * 查询所有平仓规则
	 * @return
	 * @throws FutureException
	 */
	public List<CloseRule> getAllCloseRule() throws FutureException;
	
	/**
	 * 根据用户编号查询平仓规则
	 * @param userNo
	 * @return
	 * @throws FutureException
	 */
	public List<CloseRule> getCloseRuleByUserNo(String userNo) throws FutureException;
	
	/**
	 * 更新平仓规则
	 * @param closeRule
	 * @throws FutureException
	 */
	public void updateCloseRule(CloseRule closeRule) throws FutureException;
	
	/**
	 * 删除平仓规则
	 * @param closeRule
	 * @throws FutureException
	 */
	public void removeCloseRuleById(String id) throws FutureException;
	
	/**
	 * 根据合约删除平仓规则
	 * @param contractNo
	 * @throws FutureException
	 */
	public void removeCloseRuleByContractNo(String contractNo) throws FutureException;
	
}
