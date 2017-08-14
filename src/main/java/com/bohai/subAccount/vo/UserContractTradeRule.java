package com.bohai.subAccount.vo;

import java.math.BigDecimal;
import java.util.Date;

public class UserContractTradeRule {
	private String id;

    private String userNo;
    
    private String userName;

    private String contractNo;

    private BigDecimal openCharge;

    private BigDecimal closeCurrCharge;

    private BigDecimal closeLastCharge;

    private BigDecimal margin;

    private Integer contractUnit;

    private BigDecimal tickSize;

    private Date createTime;

    private Date updateTime;

    private BigDecimal openChargeRate;

    private BigDecimal closeCurrChargeRate;
    
    private String tradeRuleId;
    
    private String groupId;
    
    private String groupName;
    
    private Integer cancelCount;
    
    private Integer entrustCount;
    
    private Integer openCount;
    

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public BigDecimal getOpenCharge() {
		return openCharge;
	}

	public void setOpenCharge(BigDecimal openCharge) {
		this.openCharge = openCharge;
	}

	public BigDecimal getCloseCurrCharge() {
		return closeCurrCharge;
	}

	public void setCloseCurrCharge(BigDecimal closeCurrCharge) {
		this.closeCurrCharge = closeCurrCharge;
	}

	public BigDecimal getCloseLastCharge() {
		return closeLastCharge;
	}

	public void setCloseLastCharge(BigDecimal closeLastCharge) {
		this.closeLastCharge = closeLastCharge;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public Integer getContractUnit() {
		return contractUnit;
	}

	public void setContractUnit(Integer contractUnit) {
		this.contractUnit = contractUnit;
	}

	public BigDecimal getTickSize() {
		return tickSize;
	}

	public void setTickSize(BigDecimal tickSize) {
		this.tickSize = tickSize;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getOpenChargeRate() {
		return openChargeRate;
	}

	public void setOpenChargeRate(BigDecimal openChargeRate) {
		this.openChargeRate = openChargeRate;
	}

	public BigDecimal getCloseCurrChargeRate() {
		return closeCurrChargeRate;
	}

	public void setCloseCurrChargeRate(BigDecimal closeCurrChargeRate) {
		this.closeCurrChargeRate = closeCurrChargeRate;
	}

	public String getTradeRuleId() {
		return tradeRuleId;
	}

	public void setTradeRuleId(String tradeRuleId) {
		this.tradeRuleId = tradeRuleId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getCancelCount() {
		return cancelCount;
	}

	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}

	public Integer getEntrustCount() {
		return entrustCount;
	}

	public void setEntrustCount(Integer entrustCount) {
		this.entrustCount = entrustCount;
	}

	public Integer getOpenCount() {
		return openCount;
	}

	public void setOpenCount(Integer openCount) {
		this.openCount = openCount;
	}
    
}