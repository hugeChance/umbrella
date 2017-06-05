package com.bohai.subAccount.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.BuyDetailMapper;
import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuyDetailService;

@Service("BuyDetailService")
public class BuyDetailServiceImpl implements BuyDetailService {
	
	static Logger logger = Logger.getLogger(BuyDetailServiceImpl.class);
	
	@Autowired
	private BuyDetailMapper buyDetailMapper;

	@Override
	public void saveBuyDetail(BuyDetail buyDetail) throws FutureException {
		buyDetailMapper.insert(buyDetail);
		
	}

	@Override
	public void updateBuyDetail(BuyDetail buyDetail) throws FutureException {
		//用户 ，平仓合约，平仓方向,平仓数量，平仓价格
		String subuserid = "";
		String closeINSTRUMENTID = "";
		String closeDIRECTION = "";
		String closeVOLUME ="";
		subuserid = buyDetail.getSubuserid();
		closeINSTRUMENTID = buyDetail.getInstrumentid();
		closeDIRECTION = buyDetail.getDirection();
//		closeVOLUME = buysellDetail.getVolume2();
		
		
	}

	@Override
	public void doFindPositionsDetail(String Subuserid, String Combokey, String Direction, String Instrumentid,
			int Volume) throws FutureException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBuyDetail(String Combokey, String SellCombokey, int Volume) {
		BuyDetail buyDetail = new BuyDetail();
		buyDetail.setCombokey(Combokey);
		buyDetail.setSellcombokey(SellCombokey);
		buyDetail.setSellvolume(Short.valueOf(String.valueOf(Volume)));
		
		
		buyDetailMapper.updateBuyDetail(buyDetail);
		
	}



	

	
}
