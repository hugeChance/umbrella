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
import com.bohai.subAccount.service.PositionsDetailService;

@Service("PositionsDetailService")
public class PositionsDetailServiceImpl implements PositionsDetailService {
	
	static Logger logger = Logger.getLogger(PositionsDetailServiceImpl.class);
	
	@Autowired
	private PositionsDetailMapper positionsDetailMapper;
	
	@Autowired
	private BuyDetailMapper buyDetailMapper;
	

	@Override
	public int doFindPositionsDetail(String Subuserid, String Combokey, String Direction, String Instrumentid,
			int Volume) throws FutureException {
		List<PositionsDetail> listPositionsDetail = null;
		
		//买开，卖平。卖开，买平。
		if(Direction.equals("0")){
			Direction = "1";
		} else {
			Direction = "0";
		}
		int retVolume = Volume;
		
		listPositionsDetail = positionsDetailMapper.findPositionsDetail(Subuserid, Direction, Instrumentid);
		
		for (PositionsDetail positionsDetail : listPositionsDetail) {
			if (positionsDetail.getVolume() >= retVolume) {
				//更新positionsDetail表 有余数 positionsDetail - retVolume
				updateVolumn(positionsDetail.getSubuserid(),positionsDetail.getCombokey(),positionsDetail.getVolume() - retVolume);
				
				//更新对应的BUY表让平仓和其对应
				BuyDetail buyDetail = new BuyDetail();
				buyDetail.setCombokey(positionsDetail.getCombokey());
				buyDetail.setSellcombokey(Combokey);
				buyDetail.setSellvolume(Short.valueOf(String.valueOf(retVolume)));
				//更新buyDetail表对应关系
				buyDetailMapper.updateBuyDetail(buyDetail);
				retVolume = 0;
				break;
			} else {
				retVolume = retVolume - positionsDetail.getVolume();
				//更新positionsDetail表  Volume = 0;
				updateVolumn(positionsDetail.getSubuserid(),positionsDetail.getCombokey(),0);
				
				//更新对应的BUY表让平仓和其对应
				BuyDetail buyDetail = new BuyDetail();
				buyDetail.setCombokey(positionsDetail.getCombokey());
				buyDetail.setSellcombokey(Combokey);
				buyDetail.setSellvolume(positionsDetail.getVolume());
				//更新buyDetail表对应关系
				buyDetailMapper.updateBuyDetail(buyDetail);
				
				
			}
		}
		return retVolume;
	}

	@Override
	public void updateVolumn(String subuserid, String Combokey,int volume) {
		PositionsDetail positionsDetail = new PositionsDetail();
		positionsDetail.setCombokey(Combokey);
		positionsDetail.setSubuserid(subuserid);
		positionsDetail.setVolume(Short.valueOf(String.valueOf(volume)));
		positionsDetailMapper.updatePositionsDetail(positionsDetail);
	}

	@Override
	public void deleteAll() {
		positionsDetailMapper.deleteAll();
		
	}

	@Override
	public void insertTodayPositions() {
		positionsDetailMapper.insertTodayPositions();
		
	}

	

	
	


	

	
}
