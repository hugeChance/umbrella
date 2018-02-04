package com.bohai.subAccount.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.FutureMarketMapper;
import com.bohai.subAccount.dao.InputOrderHistoryMapper;
import com.bohai.subAccount.dao.InputOrderMapper;
import com.bohai.subAccount.dao.InvestorPositionHistoryMapper;
import com.bohai.subAccount.dao.InvestorPositionMapper;
import com.bohai.subAccount.dao.InvestorPositionOldMapper;
import com.bohai.subAccount.dao.OrderHistoryMapper;
import com.bohai.subAccount.dao.OrderMapper;
import com.bohai.subAccount.dao.SubTradingaccountHistoryMapper;
import com.bohai.subAccount.dao.SubTradingaccountMapper;
import com.bohai.subAccount.dao.TradeHistoryMapper;
import com.bohai.subAccount.dao.TradeMapper;
import com.bohai.subAccount.dao.UserContractMapper;
import com.bohai.subAccount.dao.UserFrozenaccountMapper;
import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.dao.UseravailableindbMapper;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.Useravailableindb;
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
	
	@Autowired
	private InvestorPositionOldMapper investorPositionOldMapper;

	@Autowired
	private SubTradingaccountHistoryMapper subTradingaccountHistoryMapper;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private UserContractMapper userContractMapper;
	
	@Autowired
	private FutureMarketMapper futureMarketMapper;
	
	@Autowired
	private UseravailableindbMapper useravailableindbMapper;

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
		
		
		
		logger.debug("开始备份T_SUB_TRADINGACCOUNT表");
		count = subTradingaccountHistoryMapper.backup();
		logger.debug("T_TRADE表已备份"+count+"条数据完成");
		
		
		
		
		
//		logger.debug("开始备份T_INVESTOR_POSITION表");
//		count = InvestorPositionHistoryMapper.backup();
//		logger.debug("T_INVESTOR_POSITION表已备份"+count+"条数据完成");
//		
//		logger.debug("开始删除T_INVESTOR_POSITION表");
//		count = investorPositionMapper.deleteNoPosition();
//		logger.debug("T_INVESTOR_POSITION表已删除"+count+"条数据完成");
		
	}

	@Override
	public void init() {
		int count = 0;
		
		//逐日盯市的结算 
		//计算新的冻结保证金
		// POSITION = 0 删除
		investorPositionMapper.deleteNoPosition();
		// 有持仓但是有平仓信息的要减去。新的交易日要去除老平仓
		investorPositionMapper.updateSubtractOldPosition();
		// 计算结算前的结算价和买入价的价差。做盯市盈亏持仓计算，计入总资金
		List<UserInfo> allUser = userInfoMapper.selectByGroup();
		for (UserInfo userInfo : allUser) {
			//取得用户下的所有合约
			
			List<InvestorPosition> listInvestorPosition =  investorPositionMapper.getSubUserPostion(userInfo.getUserName());
			BigDecimal userAllPositionWin = new BigDecimal(0); 
			if(listInvestorPosition.size() > 0){
				//有持仓
				for (InvestorPosition investorPosition : listInvestorPosition) {
					BigDecimal tempBigDec = new BigDecimal(0); 
					BigDecimal tempBigDec2 = new BigDecimal(0); 
					UserContract userContract= userContractMapper.selectByUserNameAndContract(userInfo.getUserName(), investorPosition.getInstrumentid());
					String market = futureMarketMapper.selectByInstrument(investorPosition.getInstrumentid());
					if(investorPosition.getPosidirection().equals("0")){
						//买开持仓
						//结算合约的持仓盯市盈亏 一个合约的盯市盈亏 = （结算价 - 买价） * 合约单位  * 开仓手数
						tempBigDec = new BigDecimal(market).subtract(investorPosition.getOpenamount());
						tempBigDec2 = tempBigDec.multiply(new BigDecimal(userContract.getContractUnit())).multiply(new BigDecimal(investorPosition.getOpenvolume()));
						userAllPositionWin = userAllPositionWin.add(tempBigDec2);
					} else {
						//卖开持仓
						//结算合约的持仓盯市盈亏 一个合约的盯市盈亏 = （买价 - 结算价 ） * 合约单位  * 开仓手数
						tempBigDec = investorPosition.getOpenamount().subtract(new BigDecimal(market));
						tempBigDec2 = tempBigDec.multiply(new BigDecimal(userContract.getContractUnit())).multiply(new BigDecimal(investorPosition.getOpenvolume()));
						userAllPositionWin = userAllPositionWin.add(tempBigDec2);
					}
					//更新InvestorPosition结算价
					InvestorPosition investorPositionTmp = new InvestorPosition();
					investorPositionTmp.setInstrumentid(investorPosition.getInstrumentid());
					investorPositionTmp.setPosidirection(investorPosition.getPosidirection());
					investorPositionTmp.setSubuserid(userInfo.getUserName());
					investorPositionTmp.setOpenamount(new BigDecimal(market));
					investorPositionMapper.updateUserOpenAmount(investorPositionTmp);
				}
			}
		    //计算最新subTradingaccount表的POSITIONPROFIT 持仓盈亏
//			subTradingaccountMapper.updatePositionProfit(userInfo.getUserName(), userAllPositionWin.toString()); 下面【结算最终客户权益，为明天开盘的权益（包含了保证金）】已实现
			
			//计算最新持仓保证金根据结算价 自动在COREAPP SETMEMORY中计算
			
			//结算最终客户权益，为明天开盘的权益（包含了保证金）。  T_USERAVAILABLEINDB.AVAILABLE + CLOSEWIN + POSITIONWIN - COMMISSION 
			Useravailableindb useravailableindb = useravailableindbMapper.selectByUserName2(userInfo.getUserName());
			BigDecimal userOpenAmountAvailable = new BigDecimal(0);
			userOpenAmountAvailable = useravailableindb.getAvailable().add(useravailableindb.getClosewin()).add(userAllPositionWin).subtract(useravailableindb.getCommission()).add(useravailableindb.getInoutmoney());
			
			userInfoMapper.updateUserCapital(userInfo.getUserName(), userOpenAmountAvailable);
			
		}
		
		investorPositionMapper.updateCloseVolumeZero();
		
		
		
//		logger.debug("开始更新T_SUB_TRADINGACCOUNT表把冻结资金换成可用，把资金退还");
//		count = subTradingaccountMapper.updateCloseOper1();
//		logger.debug("T_SUB_TRADINGACCOUNT表已更新"+count+"条数据完成");
		
		logger.debug("开始更新T_SUB_TRADINGACCOUNT表把平仓盈亏手续费清空");
		count = subTradingaccountMapper.updateCloseOper2();
		logger.debug("T_SUB_TRADINGACCOUNT表已更新"+count+"条数据完成");
		
		logger.debug("开始更新T_USER_INFO数据");
		count = 0;
		count = subTradingaccountMapper.init();
		logger.debug("更新完成：共"+count+"条记录");
		
		logger.debug("开始还原用户资金");
		 count = 0;
		count = subTradingaccountMapper.init();
		logger.debug("更新完成：共"+count+"条记录");

		logger.debug("删除冻结信息");
		count = userFrozenaccountMapper.deleteAll();
		logger.debug("共删除"+count+"条记录");
		
		logger.debug("删除T_INVESTOR_OLD_POSITION信息");
		count = investorPositionOldMapper.deletePosition();
		logger.debug("共删除"+count+"条记录");
		
		logger.debug("插入T_INVESTOR_OLD_POSITION信息");
		count = investorPositionOldMapper.insertPosition();
		logger.debug("共插入"+count+"条记录");
	}
	
}
