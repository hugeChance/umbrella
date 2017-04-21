package com.bohai.subAccount.vo;

import java.util.List;

import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.SubTradingaccount;

/**
 * 用户持仓VO类
 * @author caojia
 */
public class UserPositionVO {

	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 用户资金信息
	 */
	private SubTradingaccount subTradingaccount;
	
	/**
	 * 用户持仓信息
	 */
	private List<InvestorPosition> investorPositions;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SubTradingaccount getSubTradingaccount() {
		return subTradingaccount;
	}

	public void setSubTradingaccount(SubTradingaccount subTradingaccount) {
		this.subTradingaccount = subTradingaccount;
	}

	public List<InvestorPosition> getInvestorPositions() {
		return investorPositions;
	}

	public void setInvestorPositions(List<InvestorPosition> investorPositions) {
		this.investorPositions = investorPositions;
	}
	
	
}
