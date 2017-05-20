package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.exception.FutureException;

public interface MainAccountService {
	
	/**
	 * 查询所有主账户
	 * @return
	 * @throws FutureException
	 */
	public List<MainAccount> getMainAccount() throws FutureException;
	
	/**
	 * 保存主账户
	 * @param account
	 * @throws FutureException 
	 */
	public void saveMainAccount(MainAccount account) throws FutureException;
	
	/**
	 * 更新主账户
	 * @param account
	 */
	public void updateMainAccount(MainAccount account) throws FutureException;
	
	/**
	 * 删除子账户
	 * @param account
	 */
	public void removeMainAccount(String id) throws FutureException;
	
	/**
	 * 根据账户类型查询账户信息
	 * @param accountType
	 * @return
	 * @throws FutureException
	 */
	public MainAccount getAccountByType(String accountType) throws FutureException;

}
