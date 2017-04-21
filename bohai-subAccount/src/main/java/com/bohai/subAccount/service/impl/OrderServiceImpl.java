package com.bohai.subAccount.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.OrderMapper;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.OrderService;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
	static Logger logger = Logger.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public List<Order> getUserByUserName(String subuserid)throws FutureException  {

		logger.info("Order getUserByUserName入參：userName = "+subuserid);
		
		List<Order> list = null;
		try {
			list = orderMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询order失败",e);
			throw new FutureException("","查询order失败");
		}
		
		return list;
	}

	@Override
	public void saveOrder(Order order) throws FutureException {
		logger.info("order入參："+JSON.toJSONString(order));
		try {
			orderMapper.insert(order);
		} catch (Exception e) {
			logger.error("插入order失败",e);
			throw new FutureException("","插入order失败");
		}
		

	}

	@Override
	public void updateOrder(Order order) throws FutureException {
		logger.info("updateInputOrder入參："+JSON.toJSONString(order));
		try {
			orderMapper.update(order);
		} catch (Exception e) {
			logger.error("更新order失败",e);
			throw new FutureException("","更新order失败");
		}

	}

	@Override
	public void deleteOrder(String subuserid) throws FutureException {
		logger.info("deleteorder入參："+subuserid);
		try {
			orderMapper.delete(subuserid);
		} catch (Exception e) {
			logger.error("删除order失败",e);
			throw new FutureException("","删除order失败");
		}

	}

	@Override
	public Order getOrderByCondition( String frontID, String sessionID,String orderRef) throws FutureException {
		logger.info("getOrderByCondition入參："+orderRef + ":" + frontID + ":" + sessionID);
		
		Order retOrder = null;
		Map map = new HashMap<String,Object>();
		map.put("frontID", frontID);
		map.put("sessionID", sessionID);
		map.put("orderRef", orderRef);
		retOrder = orderMapper.getOrderByCondition(map);
		
		if(StringUtils.isEmpty( retOrder))
		{
			retOrder = orderMapper.getOrderByCondition2(map);
		}
		

		return retOrder;
	}

	@Override
	public List<Order> getCanelByUserName(String subuserid) throws FutureException {
		logger.info("Order getCanelByUserName入參：userName = "+subuserid);
		
		List<Order> list = null;
		try {
			list = orderMapper.getCanelByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询getCanelByUserName失败",e);
			throw new FutureException("","查询getCanelByUserName失败");
		}
		
		return list;
	}

	@Override
	public List<Order> getUserCloseOrderByInstrumentIDAndDirection(String subuserid, String instrumentID,
			String direction) throws FutureException {
		logger.debug("查询用户"+subuserid+"对合约"+instrumentID+"方向为"+direction+"的平仓报单");
		List<Order> list = null;
		
		try {
			list = orderMapper.getUserCloseOrderByInstrumentIDAndDirection(subuserid, instrumentID, direction);
		} catch (Exception e) {
			logger.error("查询平仓挂单失败",e);
			throw new FutureException("","查询平仓挂单失败");
		}
		return list;
	}

}
