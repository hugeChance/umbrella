package com.bohai.subAccount.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.GroupRuleMapper;
import com.bohai.subAccount.entity.GroupRule;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.GroupRuleService;

@Service("groupRuleService")
public class GroupRuleServiceImpl implements GroupRuleService {
	
	static Logger logger = Logger.getLogger(GroupRuleServiceImpl.class);
	
	@Autowired
	private GroupRuleMapper groupRuleMapper;

	@Override
	public int saveGroupRule(GroupRule groupRule) throws FutureException {
		
		if(this.groupRuleMapper.countAll()>0){
			throw new FutureException("","不能重复设置组规则");
		}

		logger.debug("保存用户组规则入参："+JSON.toJSONString(groupRule));
		int result = 0;
		try {
			result = this.groupRuleMapper.insert(groupRule);
		} catch (Exception e) {
			logger.error("保存用户组规则失败",e);
			throw new FutureException("","保存用户组规则失败");
		}
		logger.debug("保存用户组规则结果："+result);
		
		return result; 
	}

	@Override
	public GroupRule getGroupRule() throws FutureException {
		
		GroupRule groupRule = null;

		List<GroupRule> list = new ArrayList<GroupRule>();
		
		try {
			list = this.groupRuleMapper.selectAll();
			if(list != null && list.size() >0){
				groupRule = list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询组规则失败",e);
			throw new FutureException("", "查询组规则失败");
		}
		
		return groupRule;
	}

	@Override
	public int getCount() throws FutureException {

		return this.groupRuleMapper.countAll();
	}

	@Override
	public int removeGroupRule(String id) throws FutureException {
		logger.debug("删除用户组规则，主键id:"+id);
		
		return this.groupRuleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int updateGroupRule(GroupRule groupRule) throws FutureException {
		logger.debug("更新组规则入参："+JSON.toJSONString(groupRule));
		
		int result = 0;
		try {
			result = this.groupRuleMapper.updateByPrimaryKey(groupRule);
		} catch (Exception e) {
			logger.error("更新组规则失败",e);
			throw new FutureException("","更新组规则失败");
		}
		return result;
	}

}
