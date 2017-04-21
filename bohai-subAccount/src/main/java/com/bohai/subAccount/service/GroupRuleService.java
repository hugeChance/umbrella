package com.bohai.subAccount.service;

import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.exception.FutureException;

/**
 * 组规则Service
 * @author caojia
 */
public interface GroupRuleService {

	/**
	 * 保存组规则
	 * @param groupRule
	 * @return
	 * @throws FutureException
	 */
	public int saveGroupRule(GroupRule groupRule) throws FutureException;
	
	/**
	 * 更新组规则
	 * @param groupRule
	 * @return
	 * @throws FutureException
	 */
	public int updateGroupRule(GroupRule groupRule) throws FutureException;
	
	/**
	 * 查询组规则
	 * @return
	 * @throws FutureException
	 */
	public GroupRule getGroupRule() throws FutureException;
	
	/**
	 * 查询总记录数
	 * @return
	 * @throws FutureException
	 */
	public int getCount() throws FutureException;
	
	public int removeGroupRule(String id) throws FutureException;
}
