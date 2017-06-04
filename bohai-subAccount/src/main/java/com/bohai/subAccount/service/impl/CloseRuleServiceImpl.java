package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.CloseRuleMapper;
import com.bohai.subAccount.dao.UserContractMapper;
import com.bohai.subAccount.entity.CloseRule;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.CloseRuleService;

@Service("closeRuleService")
public class CloseRuleServiceImpl implements CloseRuleService {
	
	static Logger logger = Logger.getLogger(CloseRuleServiceImpl.class);
	
	@Autowired
	private CloseRuleMapper closeRuleMapper;
	
	@Autowired
	private UserContractMapper userContractMapper;

	@Override
	public void saveCloseRule(CloseRule closeRule) throws FutureException {
		
		logger.debug("保存平仓规则入参："+JSON.toJSONString(closeRule));
		
		if(StringUtils.isEmpty(closeRule.getContractNo())){
			throw new FutureException("", "合约代码为空");
		}
		
		UserContract contract = new UserContract();
		contract.setContractNo(closeRule.getContractNo());
		int count = this.userContractMapper.countByContractNo(contract);
		
		if(count < 1){
			throw new FutureException("", "请先添加合约交易规则");
		}
		
		
		try {
			this.closeRuleMapper.insert(closeRule);
		} catch (Exception e) {
			logger.error("保存平仓规则失败");
			throw new FutureException("", "保存平仓规则失败");
		}
	}

	@Override
	public List<CloseRule> getCloseRuleByUserNo(String userNo) throws FutureException {
		logger.debug("根据用户名查询平仓规则："+userNo);
		
		List<CloseRule> list = null;
		
		try {
			list = this.closeRuleMapper.getCloseRuleByUserNo(userNo);
		} catch (Exception e) {
			logger.error("查询平仓规则失败");
			throw new FutureException("", "查询平仓规则失败");
		}
		
		return list;
	}

	@Override
	public void updateCloseRule(CloseRule closeRule) throws FutureException {
		
		logger.debug("更新平仓规则："+JSON.toJSONString(closeRule));
		
		try {
			this.closeRuleMapper.updateByPrimaryKey(closeRule);
		} catch (Exception e) {
			logger.error("更新平仓规则失败");
			throw new FutureException("", "更新平仓规则失败");
		}

	}

	@Override
	public void removeCloseRuleById(String id) throws FutureException {
		logger.debug("删除平仓规则入参,id:"+id);

		this.closeRuleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<CloseRule> getAllCloseRule() throws FutureException {
		
		List<CloseRule> list = null;
		
		try {
			list = this.closeRuleMapper.getAllCloseRule();
		} catch (Exception e) {
			logger.error("查询平仓规则失败");
			throw new FutureException("", "查询平仓规则失败");
		}
		
		return list;
	}

	@Override
	public void removeCloseRuleByContractNo(String contractNo) throws FutureException {
		logger.debug("根据合约编号删除平仓规则入参,contractNo:"+contractNo);
		
		try {
			this.closeRuleMapper.deleteByContractNo(contractNo);
		} catch (Exception e) {
			logger.error("删除平仓规则失败",e);
			throw new FutureException("", "删除平仓规则失败");
		}
	}

    @Override
    public List<CloseRule> getCloseRuleByUserName(String userName) throws FutureException {
        logger.debug("根据用户名查询平仓规则："+userName);
        
        List<CloseRule> list = null;
        
        try {
            list = this.closeRuleMapper.getCloseRuleByUserName(userName);
        } catch (Exception e) {
            logger.error("查询平仓规则失败");
            throw new FutureException("", "查询平仓规则失败");
        }
        
        return list;
    }

    @Override
    public void removeCloseRuleByContractNoAndUserNo(String contractNo, String userNo) throws FutureException {
        logger.debug("根据合约编号和用户编号删除平仓规则入参,contractNo:"+contractNo+"     userNo:"+userNo);
        
        try {
            this.closeRuleMapper.deleteByContractNoAndUserNo(contractNo, userNo);
        } catch (Exception e) {
            logger.error("删除平仓规则失败",e);
            throw new FutureException("", "删除平仓规则失败");
        }
    }

}
