package com.bohai.subAccount.vo;

public class SettlemenetTitleVO {
	
	//公司名
	private String companyName;
	//用户名
	private String userName;
	//日期
	private String todayDate;
	//期初结存
	private String balance_Start;
	//期末结存
	private String balance_End;
	//出 入 金
	private String deposit;
	//客户权益
	private String client_Equity;
	//平仓盈亏
	private String realized;
	//持仓盯市盈亏
	private String MTM;
	//手 续 费
	private String commission;
	//保证金占用
	private String margin;
	//可用资金
	private String fund_availible;
	//风 险 度
	private String risk_Degree;
	//应追加资金
	private String margin_Call;
	//自有资金
	private String cust_availible;
	//配资资金
	private String host_availible;
	
	
	
	//输出字段
	private String retStr;
	
	private String tmpStr = "\r\n";
	
	public String getRetStr() {
		//结算单头部输出
		retStr = "";
		retStr = retStr + "                                           " + companyName +"系统";
		retStr = retStr + tmpStr;
		retStr = retStr + "                                                                    制表时间 Creation Date：" + todayDate;
		retStr = retStr + tmpStr;
		retStr = retStr + "----------------------------------------------------------------------------------------------------";
		retStr = retStr + tmpStr;
		retStr = retStr + "                                           交易结算单";
		retStr = retStr + tmpStr;
		retStr = retStr + "客户名称 Client Name：  " + userName;
		retStr = retStr + tmpStr;
		retStr = retStr + "日期 Date：" + todayDate;
		retStr = retStr + tmpStr;
		retStr = retStr + tmpStr;
		retStr = retStr + tmpStr;
		retStr = retStr + "                   资金状况  币种：人民币  Account Summary  Currency：CNY ";
		retStr = retStr + tmpStr;
		retStr = retStr + "期初结存 Balance b/f：" + balance_Start;
		retStr = retStr + tmpStr;
		retStr = retStr + "期末结存 Balance c/f：" + balance_End;
		retStr = retStr + tmpStr;
		retStr = retStr + "出 入 金 Deposit/Withdrawal：" + deposit;
		retStr = retStr + tmpStr;
		retStr = retStr + "客户权益 Client Equity：" + client_Equity;
		retStr = retStr + tmpStr;
		retStr = retStr + "平仓盈亏 Realized P/L：" + realized;
		retStr = retStr + tmpStr;
		retStr = retStr + "持仓盯市盈亏 MTM P/L：" + MTM;
		retStr = retStr + tmpStr;
		retStr = retStr + "手 续 费 Commission：" + commission;
		retStr = retStr + tmpStr;
		retStr = retStr + "保证金占用 Margin Occupied：" + margin;
		retStr = retStr + tmpStr;
		retStr = retStr + "可用资金 Fund Avail.：" + fund_availible;
		retStr = retStr + tmpStr;
		retStr = retStr + "其中客户自有资金可用：" + cust_availible;
		retStr = retStr + tmpStr;
		retStr = retStr + "其中配资资金可用：" + host_availible;		
		retStr = retStr + tmpStr;
		retStr = retStr + "风 险 度 Risk Degree：" + risk_Degree;
		retStr = retStr + tmpStr;
		retStr = retStr + "应追加资金 Margin Call：" + margin_Call;
		retStr = retStr + tmpStr;
		
		
		return retStr;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	public String getBalance_Start() {
		return balance_Start;
	}
	public void setBalance_Start(String balance_Start) {
		this.balance_Start = balance_Start;
	}
	public String getBalance_End() {
		return balance_End;
	}
	public void setBalance_End(String balance_End) {
		this.balance_End = balance_End;
	}
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getClient_Equity() {
		return client_Equity;
	}
	public void setClient_Equity(String client_Equity) {
		this.client_Equity = client_Equity;
	}
	public String getRealized() {
		return realized;
	}
	public void setRealized(String realized) {
		this.realized = realized;
	}
	public String getMTM() {
		return MTM;
	}
	public void setMTM(String mTM) {
		MTM = mTM;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getMargin() {
		return margin;
	}
	public void setMargin(String margin) {
		this.margin = margin;
	}
	public String getFund_availible() {
		return fund_availible;
	}
	public void setFund_availible(String fund_availible) {
		this.fund_availible = fund_availible;
	}
	public String getRisk_Degree() {
		return risk_Degree;
	}
	public void setRisk_Degree(String risk_Degree) {
		this.risk_Degree = risk_Degree;
	}
	public String getMargin_Call() {
		return margin_Call;
	}
	public void setMargin_Call(String margin_Call) {
		this.margin_Call = margin_Call;
	}
	
	public String getCust_availible() {
		return cust_availible;
	}
	public void setCust_availible(String cust_availible) {
		this.cust_availible = cust_availible;
	}
	public String getHost_availible() {
		return host_availible;
	}
	public void setHost_availible(String host_availible) {
		this.host_availible = host_availible;
	}
	
	

}
