package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.MainAccountMapper;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.MainAccountService;

/**
 * @author caojia
 */
@Service("mainAccountService")
public class MainAccountServiceImpl implements MainAccountService{
	
	static Logger logger = Logger.getLogger(MainAccountServiceImpl.class);
	
	@Autowired
	private MainAccountMapper mainAccountMapper;

	@Override
	public void saveMainAccount(MainAccount account) throws FutureException {

		logger.info("保存主账户信息入参："+JSON.toJSONString(account));
		
		Integer count = this.mainAccountMapper.selectCount();
		
		if(count >0){
			logger.warn("只能添加一个"+(account.getAccountType().equals("1") ?"主":"备")+"账户");
			throw new FutureException("", "只能添加一个"+(account.getAccountType().equals("1") ?"主":"备")+"账户");
		}
		
		try {
			this.mainAccountMapper.insert(account);
		} catch (Exception e) {
			logger.error("保存主账户信息失败",e);
			throw new FutureException("", "保存主账户信息失败");
		}
		
	}

	@Override
	public void updateMainAccount(MainAccount account) throws FutureException {
		logger.info("更新主账户信息入参："+JSON.toJSONString(account));
		
		Integer count = this.mainAccountMapper.selectCount();
        
        if(count >0){
            logger.warn("只能添加一个"+(account.getAccountType().equals("1") ?"主":"备")+"账户");
            throw new FutureException("", "只能添加一个"+(account.getAccountType().equals("1") ?"主":"备")+"账户");
        }
		
		try {
			this.mainAccountMapper.updateByPrimaryKeySelective(account);
		} catch (Exception e) {
			logger.error("更新主账户信息失败",e);
			throw new FutureException("", "更新主账户信息失败");
		}
		
	}

	@Override
	public void removeMainAccount(String id) throws FutureException {
		logger.info("删除主账户信息入参,id:"+id);
		try {
			this.mainAccountMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("删除主账户信息失败",e);
			throw new FutureException("", "删除主账户信息失败");
		}
	}

	@Override
	public List<MainAccount> getMainAccount() throws FutureException {
		
		List<MainAccount> list = null;
		
		try {
			list = this.mainAccountMapper.selectAll();
		} catch (Exception e) {
			logger.error("查询主账户信息失败",e);
			throw new FutureException("", "查询主账户信息失败");
		}
		
		return list;
	}

}
