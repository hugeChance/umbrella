package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.BuyDetailMapper;
import com.bohai.subAccount.dao.PositionsDetailMapper;
import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.entity.SellDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuyDetailService;

@Service("buyDetailService")
public class BuyDetailServiceImpl implements BuyDetailService {
	
	static Logger logger = Logger.getLogger(BuyDetailServiceImpl.class);
	
	@Autowired
	private BuyDetailMapper buyDetailMapper;
	
	private PositionsDetailMapper positionsDetailMapper;

	@Override
	public void saveBuyDetail(BuyDetail buyDetail) throws FutureException {
		logger.info("saveBuyDetail saveBuyDetail入參：buyDetail = " + JSON.toJSONString(buyDetail));
		
		
		buyDetailMapper.insert(buyDetail);
		
	}

	@Override
	public void updateBuyDetail(BuyDetail buyDetail) throws FutureException {
		logger.info("updateBuyDetail updateBuyDetail入參：buyDetail = " + JSON.toJSONString(buyDetail));
		
		
		//用户 ，平仓合约，平仓方向,平仓数量，平仓价格
		buyDetailMapper.updateBuyDetail(buyDetail);

	}
	@Override
	public void updateBuySell(BuyDetail buyDetail){
		logger.info("updateBuySell updateBuySell入參：buyDetail = " + JSON.toJSONString(buyDetail));
		
		buyDetailMapper.updateBuySell(buyDetail);
	}

	@Override
	public void doFindPositionsDetail(String Subuserid, String Combokey, String Direction, String Instrumentid,
			long Volume) throws FutureException {
		logger.info("doFindPositionsDetail doFindPositionsDetail入參：Subuserid = "+Subuserid + ",Combokey = " + Combokey + ",Direction = " + Direction + ",Instrumentid = " + Instrumentid + ",Volume = " + Volume);
		List<BuyDetail> listBuyDetail = null;
		//买开，卖平。卖开，买平。
		if(Direction.equals("0")){
			Direction = "1";
		} else {
			Direction = "0";
		}
		long retVolume = Volume;
		listBuyDetail = buyDetailMapper.findBuyDetail(Subuserid,Instrumentid,Direction);
		
		for (BuyDetail buyDetail : listBuyDetail) {
			long nowVolume = 0;
			//buy 表中的余额比平仓数量大
			nowVolume = buyDetail.getVolume() - buyDetail.getSellvolume();
			if (nowVolume >= retVolume) {
				buyDetail.setSellcombokey(Combokey);
				nowVolume = buyDetail.getSellvolume() + retVolume;
				buyDetail.setSellvolume(Long.valueOf(String.valueOf(nowVolume)));
				updateBuySell(buyDetail);
				break;
			} else {
				//一条开仓还不够平仓的。
				retVolume = retVolume - nowVolume;
				buyDetail.setSellcombokey(Combokey);
				nowVolume = buyDetail.getSellvolume() + nowVolume;
				buyDetail.setSellvolume(Long.valueOf(String.valueOf(nowVolume)));
				updateBuySell(buyDetail);
			}
			
		}
		
		
		
	}

	@Override
	public void updateBuyDetail(String Combokey, String SellCombokey, int Volume) {
		logger.info("updateBuyDetail updateBuyDetail入參：Combokey = "+Combokey + ",SellCombokey = " + SellCombokey + ",Volume = " + Volume);
		BuyDetail buyDetail = new BuyDetail();
		buyDetail.setCombokey(Combokey);
		buyDetail.setSellcombokey(SellCombokey);
		buyDetail.setSellvolume(Long.valueOf(String.valueOf(Volume)));
		
		
		buyDetailMapper.updateBuyDetail(buyDetail);
		
	}

	@Override
	public List<BuyDetail> getBuyDetailForComboKey(String comboKey) throws FutureException{
		logger.info("getBuyDetailForComboKey getBuyDetailForComboKey入參：comboKey = "+comboKey );
		
		List<BuyDetail> list = null;
		
		try {
			list = buyDetailMapper.getBuyDetailForComboKey(comboKey);
		} catch (Exception e) {
			logger.error("查询getBuyDetailForComboKey失败",e);
			throw new FutureException("","查询getBuyDetailForComboKey失败");
		}
		
		return list;
	}



	

	
}
