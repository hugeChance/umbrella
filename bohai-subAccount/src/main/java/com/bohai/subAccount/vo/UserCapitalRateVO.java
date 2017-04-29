package com.bohai.subAccount.vo;

public class UserCapitalRateVO {
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 配资比例
	 */
	private double UserCapitalRate;
	
	/**
	 * 客户资金
	 */
	private double UserCapital;

	
	/**
	 * 配资资金1
	 */
	private double HostCapital1;

	
	/**
	 * 日期
	 */
	private String UpdateTime;
	
	/**
	 * 配资资金2
	 */
	private double HostCapital2;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getUserCapitalRate() {
		return UserCapitalRate;
	}

	public void setUserCapitalRate(double userCapitalRate) {
		UserCapitalRate = userCapitalRate;
	}

	public double getUserCapital() {
		return UserCapital;
	}

	public void setUserCapital(double userCapital) {
		UserCapital = userCapital;
	}

	public double getHostCapital1() {
		return HostCapital1;
	}

	public void setHostCapital1(double hostCapital1) {
		HostCapital1 = hostCapital1;
	}

	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

	public double getHostCapital2() {
		return HostCapital2;
	}

	public void setHostCapital2(double hostCapital2) {
		HostCapital2 = hostCapital2;
	}

	public double getHostCapitalRate() {
		return HostCapitalRate;
	}

	public void setHostCapitalRate(double hostCapitalRate) {
		HostCapitalRate = hostCapitalRate;
	}

	public double getUserOutCapitalRetain() {
		return UserOutCapitalRetain;
	}

	public void setUserOutCapitalRetain(double userOutCapitalRetain) {
		UserOutCapitalRetain = userOutCapitalRetain;
	}

	/**
	 * 配资资金再分配比例
	 */
	private double HostCapitalRate;

	/**
	 * 盘中出金质押金（可用小于质押金则不能出金）
	 */
	private double UserOutCapitalRetain;
	
}
