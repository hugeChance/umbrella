package com.bohai.subAccount.service;

import java.util.List;

import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;

public interface OrderService {
	
	/**
	 * 根据用户ID查询CTP返回报单
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<Order> getUserByUserName(String subuserid) throws FutureException;
	
	/**
	 * 风控用查询CTP返回报单用于RISK撤单
	 * @param groupId
	 * @return
	 * @throws FutureException
	 */
	public List<Order>getCanelByUserName(String subuserid) throws FutureException;
	
	
	/**
	 * 保存CTP返回报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void saveOrder(Order order) throws FutureException;
	
	/**
	 * 更新CTP返回报单
	 * @param userInfo
	 * @throws FutureException
	 */
	public void updateOrder(Order inputOrder) throws FutureException;
	
	/**
	 * 删除CTP返回报单
	 * @param id
	 * @throws FutureException
	 */
	public void deleteOrder(String subuserid) throws FutureException;
	
	/**
	 * 根据ORDERREF,FRONTID,SESSIONID查询CTP返回报单
	 * @param ORDERREF
	 * @param FRONTID
	 * @param SESSIONID
	 * @return
	 * @throws FutureException
	 */
	public Order getOrderByCondition(String frontID,String sessionID,String orderRef) throws FutureException;
	
	/**
	 * 根据合约号，开仓方向查询用户平仓的挂单
	 * @param subuserid 用户id
	 * @param instrumentID 合约编号
	 * @param direction 买卖方向
	 * @return
	 * @throws FutureException
	 */
	public List<Order> getUserCloseOrderByInstrumentIDAndDirection(String subuserid, String instrumentID, String direction) throws FutureException;
	
}
