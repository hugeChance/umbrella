package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.exception.FutureException;

public interface InvestorPositionService {
	
	/**
	 * 根据用户ID查询投资者持仓
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition> getUserByUserName(String subuserid) throws FutureException;
	
	/**
	 * @author caojia
	 * 查询客户未平的持仓信息
	 * @param subuserid
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition> getUserUnClosePostion(String subuserid) throws FutureException;
	
	/**
	 * 根据用户ID查询投资者持仓2
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition2> getUserByUserName2(String subuserid) throws FutureException;
	
	/**
	 * 根据用户ID查询投资者持仓3
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition2> getUserByUserName3(String subuserid) throws FutureException;
	
	/**
	 * 根据用户ID查询投资者持仓
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition> getSubUserPostion(String subuserid) throws FutureException;
	
	/**
	 * 根据用户ID查询投资者持仓
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition> getSubUserPostion2(String subuserid,String instrumentid,String posidirection) throws FutureException;
	
	/**
	 * 根据用户名查询已平仓盈亏
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<InvestorPosition> getUserClosePostion(String userName) throws FutureException;
	
	
	/**
	 * 保存投资者持仓
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveInvestorPosition(InvestorPosition investorPosition) throws FutureException;
	
	/**
	 * 更新投资者持仓
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateInvestorPosition(InvestorPosition investorPosition) throws FutureException;
	
	/**
	 * 删除投资者持仓
	 * @param id
	 * @throws FutureException
	 */
	public void deleteInvestorPositionr(String subuserid) throws FutureException;
	
	public void subTractionPosition(InvestorPosition investorPosition) throws FutureException;
	
	/**
	 * 投资者开仓持仓计算
	 * @param InvestorPosition
	 * @throws FutureException
	 */
	public void setOpenPosition(InvestorPosition investorPosition) throws FutureException;
	
	/**
	 * 投资者平仓持仓计算
	 * @param InvestorPosition
	 * @throws FutureException
	 */
	public void setClosePosition(InvestorPosition investorPosition) throws FutureException;
	
}
