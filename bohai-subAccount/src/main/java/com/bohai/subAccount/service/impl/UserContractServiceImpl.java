package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.UserContractMapper;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserContractService;

@Service("userContractService")
public class UserContractServiceImpl implements UserContractService {
	
	static Logger logger = Logger.getLogger(UserContractServiceImpl.class);
	
	@Autowired
	private UserContractMapper userContractMapper;

	@Override
	public void updateUserContract(UserContract contract) throws FutureException {
		logger.info("更新用户合约关系入参："+JSON.toJSONString(contract));
		
		try {
			int result = this.userContractMapper.updateByPrimaryKey(contract);
			if(result < 1){
				logger.warn("没有数据被更新");
			}
		} catch (Exception e) {
			logger.error("更新用户合约 关系失败",e);
			throw new FutureException("", "更新用户合约 关系失败");
		}
		
	}

	@Override
	public void saveUserContract(UserContract contract) throws FutureException {
		
		logger.info("保存用户合约关系入参："+JSON.toJSONString(contract));
		
		//查询用户合约是否已经绑定
		Integer count = this.userContractMapper.countByContractNo(contract);
		if(count > 0){
			logger.warn("合约已分配");
			throw new FutureException("", "合约已属于其他用户");
		}
		
		try {
			this.userContractMapper.insert(contract);
		} catch (Exception e) {
			logger.error("保存用户合约 关系失败",e);
			throw new FutureException("", "保存用户合约 关系失败");
		}
		
	}

	@Override
	public List<UserContract> queryUserContractByUserName(String userName) throws FutureException {
		logger.info("根据用户名查询用户合约信息入参："+userName);
		List<UserContract> list = null;
		try {
			list = this.userContractMapper.queryUserContractByUserName(userName);
		} catch (Exception e) {
			logger.error("根据用户名查询用户合约信息失败",e);
			throw new FutureException("", "根据用户名查询用户合约信息失败");
		}
		return list;
	}

	@Override
	public List<UserContract> queryUserContractByAll() throws FutureException {
		logger.info("查询ALL用户合约信息");
		List<UserContract> list = null;
		try {
			list = this.userContractMapper.queryUserContractByAll();
		} catch (Exception e) {
			logger.error("根据用户名查询用户合约信息失败",e);
			throw new FutureException("", "根据用户名查询用户合约信息失败");
		}
		return list;
	}

	@Override
	public Integer getContractUnitByContractNo(String contractNo) throws FutureException {
		Integer unit = 0;
		try {
			unit = this.userContractMapper.getContractUnitByContractNo(contractNo);
		} catch (Exception e) {
			logger.error("查询合约单位失败",e);
			throw new FutureException("", "查询合约单位失败");
		}
		return unit;
	}

    @Override
    public UserContract queryUserContractByUserNameAndContract(String userName, String contractNo)
            throws FutureException {

        UserContract contract = null;
        
        try {
            contract = this.userContractMapper.selectByUserNameAndContract(userName, contractNo);
        } catch (Exception e) {
            logger.error("查询用户合约失败",e);
            throw new FutureException("", "查询用户合约失败");
        }
        
        return contract;
    }


}
