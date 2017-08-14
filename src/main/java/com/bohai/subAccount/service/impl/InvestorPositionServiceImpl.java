package com.bohai.subAccount.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.InvestorPositionMapper;
import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.InvestorPosition2;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.InvestorPositionService;

@Service("investorPositionService")
public class InvestorPositionServiceImpl implements InvestorPositionService {
	
	static Logger logger = Logger.getLogger(InvestorPositionServiceImpl.class);
	
	@Autowired
	private InvestorPositionMapper investorPositionMapper;

	@Override
	public List<InvestorPosition> getUserByUserName(String subuserid) throws FutureException  {

		logger.info("InvestorPosition getUserByUserName入參：userName = "+subuserid);
		
		List<InvestorPosition> list = null;
		try {
			list = investorPositionMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询InvestorPosition失败",e);
			throw new FutureException("","查询InvestorPosition失败");
		}
		
		return list;
	}

	@Override
	public List<InvestorPosition2> getUserByUserName2(String subuserid) throws FutureException  {

		logger.info("InvestorPosition getUserByUserName入參：userName = "+subuserid);
		
		List<InvestorPosition2> list = null;
		try {
			list = investorPositionMapper.getUserByUserName2(subuserid);
		} catch (Exception e) {
			logger.error("查询InvestorPosition失败",e);
			throw new FutureException("","查询InvestorPosition失败");
		}
		
		return list;
	}
	
	@Override
	public List<InvestorPosition2> getUserByUserName3(String subuserid) throws FutureException  {

		logger.info("InvestorPosition getUserByUserName入參：userName = "+subuserid);
		
		List<InvestorPosition2> list = null;
		try {
			list = investorPositionMapper.getUserByUserName3(subuserid);
		} catch (Exception e) {
			logger.error("查询InvestorPosition失败",e);
			throw new FutureException("","查询InvestorPosition失败");
		}
		
		return list;
	}
	
	@Override
	public void saveInvestorPosition(InvestorPosition investorPosition)throws FutureException {
		logger.info("saveInvestorPosition入參："+JSON.toJSONString(investorPosition));
		try {
			investorPositionMapper.insert(investorPosition);
		} catch (Exception e) {
			logger.error("插入InvestorPosition失败",e);
			throw new FutureException("","插入InvestorPosition失败");
		}
		

	}

	@Override
	public void updateInvestorPosition(InvestorPosition investorPosition)  throws FutureException {
		logger.info("updateInvestorPosition入參："+JSON.toJSONString(investorPosition));
		try {
			investorPositionMapper.update(investorPosition);
		} catch (Exception e) {
			logger.error("更新InvestorPosition失败",e);
			throw new FutureException("","更新InvestorPosition失败");
		}

	}

	@Override
	public void deleteInvestorPositionr(String subuserid) throws FutureException {
		logger.info("deleteInvestorPosition入參："+subuserid);
		try {
			investorPositionMapper.delete(subuserid);
		} catch (Exception e) {
			logger.error("删除InvestorPosition失败",e);
			throw new FutureException("","删除InvestorPosition失败");
		}

	}

	@Override
	public void subTractionPosition(InvestorPosition investorPosition) throws FutureException {
		logger.info("subTractionPosition入參："+JSON.toJSONString(investorPosition));
		try {
			investorPositionMapper.subTractionPosition(investorPosition);
		} catch (Exception e) {
			logger.error("更新InvestorPosition失败",e);
			throw new FutureException("","更新InvestorPosition失败");
		}
		
	}

	@Override
	public void setOpenPosition(InvestorPosition inputInvestorPosition) throws FutureException {
		
		int shoushu = 0;
		double avg = 0;
		double commission = 0;
		int position = 0;
		
		logger.info("setOpenPosition入參："+JSON.toJSONString(inputInvestorPosition));
		//同持仓多空方向，同用户，同合约 没有持仓则INSERT 否则 读取原开仓数据 再 算术平均后更新数据
		InvestorPosition getInvestorPosition = investorPositionMapper.getUserByOpenPosition(inputInvestorPosition);
		
		InvestorPosition setInvestorPosition = new InvestorPosition();
		
		if(getInvestorPosition == null){
			inputInvestorPosition.setCloseamount(new BigDecimal("0"));
			inputInvestorPosition.setClosevolume(Long.valueOf("0"));
			investorPositionMapper.insert(inputInvestorPosition);
		} else {
			// 读取原开仓数据 再 算术平均后更新数据
			avg = getInvestorPosition.getOpenvolume().doubleValue() * getInvestorPosition.getOpenamount().doubleValue() + inputInvestorPosition.getOpenvolume().doubleValue() * inputInvestorPosition.getOpenamount().doubleValue() ;
			avg = avg / (getInvestorPosition.getOpenvolume().doubleValue() + inputInvestorPosition.getOpenvolume().doubleValue());
			shoushu = getInvestorPosition.getOpenvolume().intValue() + inputInvestorPosition.getOpenvolume().intValue();
			setInvestorPosition.setSubuserid(getInvestorPosition.getSubuserid());
			setInvestorPosition.setInstrumentid(getInvestorPosition.getInstrumentid());
			position = getInvestorPosition.getPosition().intValue() + inputInvestorPosition.getPosition().intValue();
			commission = getInvestorPosition.getCommission().doubleValue() + inputInvestorPosition.getCommission().doubleValue();
			
			//持仓多空方向
			setInvestorPosition.setPosidirection(String.valueOf(getInvestorPosition.getPosidirection()));
		    //investorPosition.setPositiondate(pTrade.getTradingDay());
			//投机套保标志
			setInvestorPosition.setHedgeflag(String.valueOf(getInvestorPosition.getHedgeflag()));
			//今日持仓
			setInvestorPosition.setPosition(Long.valueOf(String.valueOf(position)));
			//开仓金额
			setInvestorPosition.setOpenamount(new BigDecimal(avg));
			//开仓量
			setInvestorPosition.setOpenvolume(Long.valueOf(String.valueOf(shoushu)));
			//开仓手续费
			setInvestorPosition.setCommission(new BigDecimal(commission));
			
			logger.info("读取原开仓数据 再 算术平均后更新数据入參："+JSON.toJSONString(setInvestorPosition));
			investorPositionMapper.updateOpenPosition(setInvestorPosition);
		}
		
	}

	@Override
	public void setClosePosition(InvestorPosition inputInvestorPosition) throws FutureException {
		int shoushu = 0;
		double avg = 0;
		double commission = 0;
		int position = 0;
		logger.info("setClosePosition入參："+JSON.toJSONString(inputInvestorPosition));
		
		if(String.valueOf(inputInvestorPosition.getPosidirection()).equals("0")){
			//买开是卖平
			inputInvestorPosition.setPosidirection("1");
		} else {
			inputInvestorPosition.setPosidirection("0");
		}
		
		
		InvestorPosition getInvestorPosition = investorPositionMapper.getUserByOpenPosition(inputInvestorPosition);
		
		InvestorPosition setInvestorPosition = new InvestorPosition();
		
		
		avg = getInvestorPosition.getClosevolume().doubleValue() * getInvestorPosition.getCloseamount().doubleValue() + inputInvestorPosition.getClosevolume().doubleValue() * inputInvestorPosition.getCloseamount().doubleValue() ;
		avg = avg / (getInvestorPosition.getClosevolume().doubleValue() + inputInvestorPosition.getClosevolume().doubleValue());
		shoushu = getInvestorPosition.getClosevolume().intValue() + inputInvestorPosition.getClosevolume().intValue();
		position = getInvestorPosition.getPosition().intValue() - inputInvestorPosition.getPosition().intValue();
		commission = getInvestorPosition.getCommission().doubleValue() + inputInvestorPosition.getCommission().doubleValue();
		
		
		
		
		setInvestorPosition.setSubuserid(inputInvestorPosition.getSubuserid());
		setInvestorPosition.setInstrumentid(inputInvestorPosition.getInstrumentid());
		
		setInvestorPosition.setPosidirection(String.valueOf(getInvestorPosition.getPosidirection()));
		
		setInvestorPosition.setHedgeflag(String.valueOf(getInvestorPosition.getHedgeflag()));
		setInvestorPosition.setPosition(Long.valueOf(Short.valueOf(String.valueOf(position))));
		setInvestorPosition.setCloseamount(new BigDecimal(avg));
		setInvestorPosition.setClosevolume(Long.valueOf(String.valueOf(shoushu)));
		//平仓手续费
		setInvestorPosition.setCommission(new BigDecimal(commission));
		
		logger.info("读取原平仓数据 再 算术平均后更新数据入參："+JSON.toJSONString(setInvestorPosition));
		investorPositionMapper.updateClosePosition(setInvestorPosition);
		
	}

	@Override
	public List<InvestorPosition> getSubUserPostion(String subuserid) throws FutureException {
		logger.info("InvestorPosition getSubUserPostion入參：userName = "+subuserid);
		
		List<InvestorPosition> list = null;
		try {
			list = investorPositionMapper.getSubUserPostion(subuserid);
		} catch (Exception e) {
			logger.error("查询getSubUserPostion失败",e);
			throw new FutureException("","查询getSubUserPostion失败");
		}
		
		return list;
	}

	@Override
	public List<InvestorPosition> getUserClosePostion(String userName) throws FutureException {
		List<InvestorPosition> list = null;
		
		try {
			list = this.investorPositionMapper.getUserClosePostion(userName);
		} catch (Exception e) {
			logger.error("查询用户已平仓盈亏失败",e);
			throw new FutureException("","查询用户已平仓盈亏失败");
		}
		return list;
	}

	@Override
	public List<InvestorPosition> getSubUserPostion2(String subuserid, String instrumentid, String posidirection)
			throws FutureException {
		logger.info("InvestorPosition getSubUserPostion2入參：userName = "+subuserid+":instrumentid="+instrumentid+":posidirection="+posidirection);
		
		List<InvestorPosition> list = null;
		try {
			list = investorPositionMapper.getSubUserPostion2(subuserid,instrumentid,posidirection);
		} catch (Exception e) {
			logger.error("查询getSubUserPostion2失败",e);
			throw new FutureException("","查询getSubUserPostion2失败");
		}
		
		return list;

	}

	@Override
	public List<InvestorPosition> getUserUnClosePostion(String subuserid) throws FutureException {
		logger.info("查询客户未平持仓信息入參：userName = "+subuserid);
		
		List<InvestorPosition> list = null;
		try {
			list = investorPositionMapper.getUserUnClosePostion(subuserid);
		} catch (Exception e) {
			logger.error("查询客户未平持仓信息失败",e);
			throw new FutureException("","查询客户未平持仓信息失败");
		}
		return list;
	}

}
