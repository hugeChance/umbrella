package com.bohai.subAccount.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.UserFrozenaccountMapper;
import com.bohai.subAccount.dao.UserLoginMapper;
import com.bohai.subAccount.entity.UserFrozenaccount;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserFrozenaccountService;

@Service("userFrozenaccountService")
public class UserFrozenaccountServiceImpl implements UserFrozenaccountService {
	
	static Logger logger = Logger.getLogger(UserFrozenaccountServiceImpl.class);
	
	@Autowired
	private UserFrozenaccountMapper userFrozenaccountMapper;

	@Override
	public List<UserFrozenaccount> getUserByUserName(String subuserid) throws FutureException  {

		logger.info("UserFrozenaccount getUserByUserName入參：userName = "+subuserid);
		
		List<UserFrozenaccount> list = null;
		try {
			list = userFrozenaccountMapper.getUserByUserName(subuserid);
		} catch (Exception e) {
			logger.error("查询UserFrozenaccount失败",e);
			throw new FutureException("","查询UserFrozenaccount失败");
		}
		
		return list;
	}

	@Override
	public void saveUserFrozenaccount(UserFrozenaccount userFrozenaccount) throws FutureException {
		logger.info("saveUserFrozenaccount入參："+JSON.toJSONString(userFrozenaccount));
		try {
			userFrozenaccountMapper.insert(userFrozenaccount);
		} catch (Exception e) {
			logger.error("插入UserFrozenaccount失败",e);
			throw new FutureException("","插入UserFrozenaccount失败");
		}
		

	}

	@Override
	public void updateUserFrozenaccount(UserFrozenaccount userFrozenaccount) throws FutureException {
		logger.info("updateUserFrozenaccount入參："+JSON.toJSONString(userFrozenaccount));
		try {
			userFrozenaccountMapper.update(userFrozenaccount);
		} catch (Exception e) {
			logger.error("更新UserFrozenaccount失败",e);
			throw new FutureException("","更新UserFrozenaccount失败");
		}

	}

	@Override
	public void deleteUserFrozenaccount(String subuserid) throws FutureException {
		logger.info("deleteUserFrozenaccount入參："+subuserid);
		try {
			userFrozenaccountMapper.delete(subuserid);
		} catch (Exception e) {
			logger.error("删除失败",e);
			throw new FutureException("","删除失败");
		}

	}

	@Override
	public UserFrozenaccount getUserByUnfrozen(Map<String, Object> map) throws FutureException {
		logger.info("getUserByUnfrozen入參："+map.get("subAccount")+":"+map.get("frontID")+":"+map.get("sessionID")+":"+map.get("orderRef")+":"+map.get("volume"));
		
		UserFrozenaccount userFrozenaccount = new UserFrozenaccount();
		
		try {
			userFrozenaccount = userFrozenaccountMapper.getUserByUnfrozen(map);
			logger.info("volume:"+map.get("volume"));
			logger.info("getVolumetotaloriginal:"+userFrozenaccount.getVolumetotaloriginal());
			if(map.get("volume").equals(userFrozenaccount.getVolumetotaloriginal())){
				logger.info("全部释放:"+ map.get("volume"));
				//全部释放
				userFrozenaccountMapper.updateCancelUnfrozen(map);
			} else {
				//部分释放
				logger.info("部分释放");
				userFrozenaccountMapper.updatePartUnfrozen(map);
				double dbtmp = 0;
				// 部分保证金
				
				dbtmp = userFrozenaccount.getFrozenmargin().doubleValue() / userFrozenaccount.getVolumetotaloriginal().doubleValue() * Double.valueOf(String.valueOf(map.get("volume")));
				
				userFrozenaccount.setFrozenmargin(new BigDecimal(dbtmp));
				
				logger.info("部分保证金=" + dbtmp);
				
				// 部分手续费
				
				dbtmp = userFrozenaccount.getFrozencommission().doubleValue() / userFrozenaccount.getVolumetotaloriginal().doubleValue() * Double.valueOf(String.valueOf(map.get("volume")));
				
				userFrozenaccount.setFrozencommission(new BigDecimal(dbtmp));
				
				logger.info("部分手续费" + dbtmp);
				
				//部分数量改修
				userFrozenaccount.setVolumetotaloriginal(Long.valueOf(String.valueOf(map.get("volume"))));
				
				logger.info("部分数量" + map.get("volume"));
			}
			
			
			
		} catch (Exception e) {
			logger.error("查询UserFrozenaccount失败",e);
			throw new FutureException("","查询UserFrozenaccount失败");
		}
		
		
		return userFrozenaccount;
	}
	
	@Override
	public UserFrozenaccount getUserByTradeUnfrozen(Map<String, Object> map) throws FutureException {
		logger.info("getUserByTradeUnfrozen入參："+map.get("subAccount")+":"+map.get("frontID")+":"+map.get("sessionID")+":"+map.get("orderRef")+":"+map.get("volume"));
		

		UserFrozenaccount userFrozenaccount = null;
		try {
			 userFrozenaccount = userFrozenaccountMapper.getUserByUnfrozen(map);
			
			if(userFrozenaccount == null){
				return null;
			}
			
			logger.info("volume:"+map.get("volume"));
			logger.info("getVolumetotaloriginal:"+userFrozenaccount.getVolumetotaloriginal());
			if(map.get("volume").equals(userFrozenaccount.getVolumetotaloriginal())){
				logger.info("全部释放:"+ map.get("volume"));
				//全部释放
				userFrozenaccountMapper.updateTradeUnfrozen(map);
			} else {
				//部分释放
				logger.info("部分释放");
				userFrozenaccountMapper.updateTradePartUnfrozen(map);
				double dbtmp = 0;
				// 部分保证金
				
				dbtmp = userFrozenaccount.getFrozenmargin().doubleValue() / userFrozenaccount.getVolumetotaloriginal().doubleValue() * Double.valueOf(String.valueOf(map.get("volume")));
				
				userFrozenaccount.setFrozenmargin(new BigDecimal(dbtmp));
				
				logger.info("部分保证金=" + dbtmp);
				
				// 部分手续费
				
				dbtmp = userFrozenaccount.getFrozencommission().doubleValue() / userFrozenaccount.getVolumetotaloriginal().doubleValue() * Double.valueOf(String.valueOf(map.get("volume")));
				
				userFrozenaccount.setFrozencommission(new BigDecimal(dbtmp));
				
				logger.info("部分手续费" + dbtmp);
				
				//部分数量改修
				userFrozenaccount.setVolumetotaloriginal(Long.valueOf(String.valueOf(map.get("volume"))));
				
				logger.info("部分数量" + map.get("volume"));
			}
			
			
			
		} catch (Exception e) {
			logger.error("查询UserFrozenaccount失败",e);
			throw new FutureException("","查询UserFrozenaccount失败");
		}
		
		
		return userFrozenaccount;
	}

}
