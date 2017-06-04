package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;

public interface UserContractService {

	/**
	 * 更新用户合约关系
	 * @param contract
	 * @throws FutureException
	 */
	public void updateUserContract(UserContract contract) throws FutureException;
	
	/**
	 * 保存用户合约关系
	 * @param contract
	 * @return 
	 * @throws FutureException
	 */
	public Integer saveUserContract(UserContract contract) throws FutureException;
	
	/**
	 * 根据用户名查询用户合约信息
	 * @param userName
	 * @return
	 * @throws FutureException
	 */
	public List<UserContract> queryUserContractByUserName(String userName)  throws FutureException;
	
	/**
	 * 查询用户合约信息
	 * @param userName
	 * @return
	 * @throws FutureException
	 */
	public List<UserContract> queryUserContractByAll()  throws FutureException;
	
	/**
	 * 根据合约编号查询合约单位
	 * @return
	 * @throws FutureException
	 */
	public Integer getContractUnitByContractNo(String contractNo) throws FutureException;
	
	/**
	 * 根据合约和用户名查询合约信息
	 * @param userName
	 * @param contractNo
	 * @return
	 * @throws FutureException
	 */
	public UserContract queryUserContractByUserNameAndContract(String userName,String contractNo) throws FutureException;
	
	
}
