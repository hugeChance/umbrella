package com.bohai.subAccount.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.InputOrderHistoryMapper;
import com.bohai.subAccount.dao.InputOrderMapper;
import com.bohai.subAccount.dao.InvestorPositionHistoryMapper;
import com.bohai.subAccount.dao.InvestorPositionMapper;
import com.bohai.subAccount.dao.OrderHistoryMapper;
import com.bohai.subAccount.dao.OrderMapper;
import com.bohai.subAccount.dao.SubTradingaccountMapper;
import com.bohai.subAccount.dao.TradeHistoryMapper;
import com.bohai.subAccount.dao.TradeMapper;
import com.bohai.subAccount.dao.UserFrozenaccountMapper;
import com.bohai.subAccount.service.ClearService;

@Service("clearService")
public class ClearServiceImpl implements ClearService {
	
	static Logger logger = Logger.getLogger(ClearServiceImpl.class);
	
	@Autowired
	private InputOrderHistoryMapper inputOrderHistoryMapper;
	
	@Autowired
	private InputOrderMapper InputOrderMapper;
	
	@Autowired
	private OrderHistoryMapper orderHistoryMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private TradeHistoryMapper tradeHistoryMapper;
	
	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private InvestorPositionHistoryMapper InvestorPositionHistoryMapper;
	
	@Autowired
	private InvestorPositionMapper investorPositionMapper;
	
	@Autowired
	private SubTradingaccountMapper subTradingaccountMapper;
	
	@Autowired
	private UserFrozenaccountMapper userFrozenaccountMapper;

	@Override
	public void backUp() {

		int count = 0;
		logger.debug("开始备份T_INPUT_ORDER表");
		count = this.inputOrderHistoryMapper.backup();
		logger.debug("T_INPUT_ORDER表已备份"+count+"条数据完成");
		
		logger.debug("开始删除T_INPUT_ORDER表");
		count = InputOrderMapper.deleteAll();
		logger.debug("T_INPUT_ORDER表已删除"+count+"条数据完成");
		
		
		
		logger.debug("开始备份T_ORDER表");
		count = orderHistoryMapper.backup();
		logger.debug("T_ORDER表已备份"+count+"条数据完成");
		
		logger.debug("开始删除T_ORDER表");
		count = orderMapper.deleteAll();
		logger.debug("T_ORDER表已删除"+count+"条数据完成");
		
		
		
		logger.debug("开始备份T_TRADE表");
		count = tradeHistoryMapper.backup();
		logger.debug("T_TRADE表已备份"+count+"条数据完成");
		
		logger.debug("开始删除T_TRADE表");
		count = tradeMapper.deleteAll();
		logger.debug("T_TRADE表已删除"+count+"条数据完成");
		
		
		
		logger.debug("开始备份T_INVESTOR_POSITION表");
		count = InvestorPositionHistoryMapper.backup();
		logger.debug("T_INVESTOR_POSITION表已备份"+count+"条数据完成");
		
		logger.debug("开始删除T_INVESTOR_POSITION表");
		count = investorPositionMapper.deleteNoPosition();
		logger.debug("T_INVESTOR_POSITION表已删除"+count+"条数据完成");
		
	}

	@Override
	public void init() {
		
		logger.debug("开始还原用户资金");
		int count = 0;
		count = subTradingaccountMapper.init();
		logger.debug("更新完成：共"+count+"条记录");

		logger.debug("删除冻结信息");
		count = userFrozenaccountMapper.deleteAll();
		logger.debug("共删除"+count+"条记录");
	}

}
