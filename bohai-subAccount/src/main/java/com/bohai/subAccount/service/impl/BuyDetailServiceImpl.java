package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.BuyDetailMapper;
import com.bohai.subAccount.dao.PositionsDetailMapper;
import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuyDetailService;

@Service("BuyDetailService")
public class BuyDetailServiceImpl implements BuyDetailService {
	
	static Logger logger = Logger.getLogger(BuyDetailServiceImpl.class);
	
	@Autowired
	private BuyDetailMapper buyDetailMapper;
	
	private PositionsDetailMapper positionsDetailMapper;

	@Override
	public void saveBuyDetail(BuyDetail buyDetail) throws FutureException {
		buyDetailMapper.insert(buyDetail);
		
	}

	@Override
	public void updateBuyDetail(BuyDetail buyDetail) throws FutureException {
		//用户 ，平仓合约，平仓方向,平仓数量，平仓价格
		buyDetailMapper.updateBuyDetail(buyDetail);

	}
	@Override
	public void updateBuySell(BuyDetail buyDetail){
		buyDetailMapper.updateBuySell(buyDetail);
	}

	@Override
	public void doFindPositionsDetail(String Subuserid, String Combokey, String Direction, String Instrumentid,
			int Volume) throws FutureException {
		
		List<BuyDetail> listBuyDetail = null;
		//买开，卖平。卖开，买平。
		if(Direction.equals("0")){
			Direction = "1";
		} else {
			Direction = "0";
		}
		int retVolume = Volume;
		listBuyDetail = buyDetailMapper.findBuyDetail(Subuserid,Instrumentid,Direction);
		
		for (BuyDetail buyDetail : listBuyDetail) {
			int nowVolume = 0;
			//buy 表中的余额比平仓数量大
			nowVolume = buyDetail.getVolume() - buyDetail.getSellvolume();
			if (nowVolume >= retVolume) {
				buyDetail.setSellcombokey(Combokey);
				nowVolume = buyDetail.getSellvolume() + retVolume;
				buyDetail.setSellvolume(Short.valueOf(String.valueOf(nowVolume)));
				updateBuySell(buyDetail);
				break;
			} else {
				//一条开仓还不够平仓的。
				retVolume = retVolume - nowVolume;
				buyDetail.setSellcombokey(Combokey);
				nowVolume = buyDetail.getSellvolume() + nowVolume;
				buyDetail.setSellvolume(Short.valueOf(String.valueOf(nowVolume)));
				updateBuySell(buyDetail);
			}
			
		}
		
		
		
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
