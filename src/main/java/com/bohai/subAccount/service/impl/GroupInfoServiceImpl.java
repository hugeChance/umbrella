package com.bohai.subAccount.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.dao.GroupInfoMapper;
import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.entity.GroupInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.GroupInfoService;

/**
 * @author caojia
 */

@Service("groupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService {
	
	static Logger logger = Logger.getLogger(GroupInfoServiceImpl.class);
	
	@Autowired
	private GroupInfoMapper groupInfoMapper;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Override
	public List<GroupInfo> getGroups() throws FutureException {
		
		List<GroupInfo> list = null;
		try {
			list = this.groupInfoMapper.selectAll();
		} catch (Exception e) {
			logger.error("查询用户组失败",e);
			throw new FutureException("", "查询用户组失败");
		}
		return list;
	}

	@Override
	public void saveGroupInfo(GroupInfo groupInfo) throws FutureException {
		
		logger.info("保存用户组信息入参："+JSON.toJSONString(groupInfo));
		
		try {
			this.groupInfoMapper.insertSelective(groupInfo);
		} catch (Exception e) {
			logger.error("保存用户组失败",e);
			throw new FutureException("", "保存用户组失败");
		}
	}

	@Override
	public void updateGroupInfo(GroupInfo groupInfo) throws FutureException {
		
		logger.info("更新用户组信息入参："+JSON.toJSONString(groupInfo));
		
		try {
			this.groupInfoMapper.updateByPrimaryKey(groupInfo);
		} catch (Exception e) {
			logger.error("更新用户组失败",e);
			throw new FutureException("", "更新用户组失败");
		}
	}

	@Override
	public void daleteGroupInfo(String id) throws FutureException {

		logger.info("删除用户组,id:"+id);
		
		int count = this.userInfoMapper.countByGroupId(id);
		if(count > 0){
			throw new FutureException("", "该用户组下有用户，不能删除");
		}
		
		try {
			this.groupInfoMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("删除用户组失败");
			throw new FutureException("", "删除用户组失败");
		}

	}

	@Override
	public GroupInfo getGroupInfoById(String id) throws FutureException {
		return this.groupInfoMapper.selectByPrimaryKey(id);
	}

}
