package com.bohai.subAccount.vo;

public class UserTradeRuleMemorySave {
	
	private String contract;
	
	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public Integer getMaxCancelCount() {
		return maxCancelCount;
	}

	public void setMaxCancelCount(Integer maxCancelCount) {
		this.maxCancelCount = maxCancelCount;
	}

	public Integer getMaxEntrustCount() {
		return maxEntrustCount;
	}

	public void setMaxEntrustCount(Integer maxEntrustCount) {
		this.maxEntrustCount = maxEntrustCount;
	}

	public Integer getMaxOpenCount() {
		return maxOpenCount;
	}

	public void setMaxOpenCount(Integer maxOpenCount) {
		this.maxOpenCount = maxOpenCount;
	}

	public Integer getRealCancelCount() {
		return realCancelCount;
	}

	public void setRealCancelCount(Integer realCancelCount) {
		this.realCancelCount = realCancelCount;
	}

	public Integer getRealEntrustCount() {
		return realEntrustCount;
	}

	public void setRealEntrustCount(Integer realEntrustCount) {
		this.realEntrustCount = realEntrustCount;
	}

	public Integer getRealOpenCount() {
		return realOpenCount;
	}

	public void setRealOpenCount(Integer realOpenCount) {
		this.realOpenCount = realOpenCount;
	}

	private Integer maxCancelCount; 
	
	private Integer maxEntrustCount; 
	
	private Integer maxOpenCount; 
	
	private Integer realCancelCount; 
	
	private Integer realEntrustCount; 
	
	private Integer realOpenCount; 

}
