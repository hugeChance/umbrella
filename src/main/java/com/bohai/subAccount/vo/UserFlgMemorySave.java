package com.bohai.subAccount.vo;

public class UserFlgMemorySave {
	
	private String userName;
	
	//强平限制
	private String Flag1;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFlag1() {
		return Flag1;
	}

	public void setFlag1(String flag1) {
		Flag1 = flag1;
	}

	public String getFlag2() {
		return Flag2;
	}

	public void setFlag2(String flag2) {
		Flag2 = flag2;
	}

	public String getFlag3() {
		return Flag3;
	}

	public void setFlag3(String flag3) {
		Flag3 = flag3;
	}

	//只准平仓
	private String Flag2;
	
	//交易员登入（暂时没有实现）
	private String Flag3;

}
