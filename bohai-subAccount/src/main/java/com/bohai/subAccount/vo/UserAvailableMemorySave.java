package com.bohai.subAccount.vo;

public class UserAvailableMemorySave {
	
	private String userName;
	//静态资金
	private String available;
	//平仓盈亏
	private String closeWin;
	//持仓盈亏
	private String positionWin;
	//手续费
	private String commission;
	//冻结资金
	private String frozenAvailable;
	//保证金
	private String margin;
	//出入金
	private String inOutMoney;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getCloseWin() {
		return closeWin;
	}

	public void setCloseWin(String closeWin) {
		this.closeWin = closeWin;
	}

	public String getPositionWin() {
		return positionWin;
	}

	public void setPositionWin(String positionWin) {
		this.positionWin = positionWin;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getFrozenAvailable() {
		return frozenAvailable;
	}

	public void setFrozenAvailable(String frozenAvailable) {
		this.frozenAvailable = frozenAvailable;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getInOutMoney() {
		return inOutMoney;
	}

	public void setInOutMoney(String inOutMoney) {
		this.inOutMoney = inOutMoney;
	}

	

}
