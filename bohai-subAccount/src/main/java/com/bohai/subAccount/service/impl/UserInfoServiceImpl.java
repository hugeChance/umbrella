package com.bohai.subAccount.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.CloseRuleMapper;
import com.bohai.subAccount.dao.InvestorPositionMapper;
import com.bohai.subAccount.dao.SubTradingaccountMapper;
import com.bohai.subAccount.dao.TradeMapper;
import com.bohai.subAccount.dao.TradeRuleMapper;
import com.bohai.subAccount.dao.UserContractMapper;
import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserInfoService;

/**
 * @author caojia
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    
    static Logger logger = Logger.getLogger(UserInfoServiceImpl.class);
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Autowired
    private UserContractMapper userContractMapper;
    
    @Autowired
    private SubTradingaccountMapper subTradingaccountMapper;
    
    @Autowired
    private InvestorPositionMapper investorPositionMapper;
    
    @Autowired
    private CloseRuleMapper closeRuleMapper;
    
    @Autowired
    private TradeRuleMapper tradeRuleMapper;
    
    @Override
    public List<UserInfo> getUsersByGroupId(String groupId) throws FutureException {

        logger.info("根据用户组ID查询用户信息，用户组ID："+groupId);
        
        List<UserInfo> list = null;
        
        try {
            list = this.userInfoMapper.selectByGroupId(groupId);
        } catch (Exception e) {
            logger.error("查询用户信息失败",e);
            throw new FutureException("", "查询用户信息失败");
        }
        logger.debug("根据用户组ID查询用户信息返回结果"+JSON.toJSONString(list));
        return list;
    }
    
    @Override
    public List<UserInfo> getUsersByGroup() throws FutureException {

        logger.info("查询用户信息");
        
        List<UserInfo> list = null;
        
        try {
            list = this.userInfoMapper.selectByGroup();
        } catch (Exception e) {
            logger.error("查询用户信息失败",e);
            throw new FutureException("", "查询用户信息失败");
        }
        
        return list;
    }

    @Override
    public void saveUser(UserInfo userInfo) throws FutureException {
        
        logger.info("保存用户信息入参："+JSON.toJSONString(userInfo));
        
        try {
            this.userInfoMapper.insert(userInfo);
        } catch (Exception e) {
            logger.error("保存用户信息失败",e);
            throw new FutureException("", "保存用户信息失败");
        }
        
    	SubTradingaccount record = new SubTradingaccount();
    	record.setAccountid(userInfo.getUserName());
    	record.setAvailable(userInfo.getCapital());
    	record.setBalance(new BigDecimal("0"));
    	record.setCashin(new BigDecimal("0"));
    	record.setCloseprofit(new BigDecimal("0"));
    	record.setCommission(new BigDecimal("0"));
    	record.setCredit(new BigDecimal("0"));
    	record.setCurrmargin(new BigDecimal("0"));
    	record.setDeliverymargin(new BigDecimal("0"));
    	record.setDeposit(new BigDecimal("0"));
    	record.setExchangedeliverymargin(new BigDecimal("0"));
    	record.setExchangemargin(new BigDecimal("0"));
    	record.setFrozencash(new BigDecimal("0"));
    	record.setFrozencommission(new BigDecimal("0"));
    	record.setFrozenmargin(new BigDecimal("0"));
    	record.setInterest(new BigDecimal("0"));
    	record.setInterestbase(new BigDecimal("0"));
    	record.setMortgage(new BigDecimal("0"));
    	record.setPositionprofit(new BigDecimal("0"));
    	record.setPrebalance(new BigDecimal("0"));
    	record.setPrecredit(new BigDecimal("0"));
    	record.setPredeposit(new BigDecimal("0"));
    	record.setPremargin(new BigDecimal("0"));
    	record.setPremortgage(new BigDecimal("0"));
    	record.setReserve(new BigDecimal("0"));
    	record.setWithdraw(new BigDecimal("0"));
    	record.setWithdrawquota(new BigDecimal("0"));
    	
    	try {
			subTradingaccountMapper.insert(record);
		} catch (Exception e) {
			logger.error("保存用户资金失败",e);
            throw new FutureException("", "保存用户资金失败");
		}
    
        /*String contracts = userInfo.getContract();
        String[] contract = contracts.split(",");
        
        for (String s : contract) {
            UserContract userContract = new UserContract();
            userContract.setUserNo(userInfo.getUserNo());
            userContract.setContractNo(s);
            try {
                this.userContractMapper.insert(userContract);
            } catch (Exception e) {
                logger.error("保存用户合约关联信息失败",e);
                throw new FutureException("", "保存用户合约关联信息失败");
            }
        }*/
    }

    @Override
    public void updateUser(UserInfo userInfo) throws FutureException {

        logger.info("更新用户信息入参："+JSON.toJSONString(userInfo));
        
        try {
            this.userInfoMapper.updateByPrimaryKeySelective(userInfo);
        } catch (Exception e) {
            logger.error("更新用户信息失败",e);
            throw new FutureException("", "更新用户信息失败");
        }

    }

    @Override
    public void deleteUser(UserInfo userInfo) throws FutureException {
        
        logger.info("删除用户信息入参:"+JSON.toJSONString(userInfo));
        
        
        //查询持仓
        int count = investorPositionMapper.countByUserName(userInfo.getUserName());
        if(count > 0){
        	logger.warn("投资者有持仓,数量为:"+count);
        	throw new FutureException("", "投资者有持仓，不能删除");
        }
        
        //删除平仓规则
        try {
			int count1 = this.closeRuleMapper.deleteByUserNo(userInfo.getUserNo());
			logger.debug("删除"+count1+"条平仓规则");
		} catch (Exception e1) {
			logger.error("删除平仓规则失败");
        	throw new FutureException("", "删除平仓规则失败");
		}
        
        //删除交易规则
        try {
			int count1 = this.tradeRuleMapper.deleteByUserNo(userInfo.getUserNo());
			logger.debug("删除"+count1+"条交易规则");
		} catch (Exception e1) {
			logger.error("删除交易规则失败");
        	throw new FutureException("", "删除交易规则失败");
		}
        
        //删除用户合约
        try {
			int count1 = this.userContractMapper.deleteByUserNo(userInfo.getUserNo());
			logger.debug("删除"+count1+"条用户合约");
		} catch (Exception e1) {
			logger.error("删除用户合约失败");
			throw new FutureException("", "删除交易规则失败");
		}
        
        try {
            int count1 = this.userInfoMapper.deleteByPrimaryKey(userInfo.getId());
            logger.debug("删除"+count1+"条用户信息");
        } catch (Exception e) {
            logger.error("删除用户信息失败");
            throw new FutureException("", "删除用户信息失败");
        }
        
        //删除资金表
        try {
			int count1 = subTradingaccountMapper.delete(userInfo.getUserName());
			logger.debug("删除"+count1+"条用户资金信息");
		} catch (Exception e) {
			logger.error("删除用户资金失败",e);
            throw new FutureException("", "删除用户资金失败");
		}
        
    }

    @Override
    public int checkUser(String userName, String password) throws FutureException {
        logger.info("根据用户名密码查询用户入参：userName:"+userName+ ",password:"+password);
        
        int r = 0;
        try {
            r = this.userInfoMapper.countUserInfoByUserNameAndPasswd(userName, password);
        } catch (Exception e) {
            logger.error("验证用户名密码失败",e);
            throw new FutureException("", "验证用户名密码失败");
        }
        return r;
    }

}
