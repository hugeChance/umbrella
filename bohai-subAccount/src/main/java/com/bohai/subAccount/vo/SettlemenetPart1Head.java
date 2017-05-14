package com.bohai.subAccount.vo;
//成交记录
public class SettlemenetPart1Head {
	
	private String tmpStr = "\r\n";
	
	private String retPart1Head1;

	public String getRetPart1Head1() {
		retPart1Head1 = "                                                              成交记录 Transaction Record " ;
		retPart1Head1 = retPart1Head1 + tmpStr;
		retPart1Head1 = retPart1Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
		retPart1Head1 = retPart1Head1 + tmpStr;
		retPart1Head1 = retPart1Head1 + "|成交日期| 交易所 |       品种       |      合约      |买/卖|   投/保    |  成交价  | 手数 |   成交额   |       开平       |  手续费  |     权利金收支      |  成交序号  |";
		retPart1Head1 = retPart1Head1 + tmpStr;
		retPart1Head1 = retPart1Head1 + "|  Date  |Exchange|     Product      |   Instrument   | B/S |    S/H     |   Price  | Lots |  Turnover  |       O/C        |   Fee    |Premium Received/Paid|  Trans.No. |";
		retPart1Head1 = retPart1Head1 + tmpStr;
		retPart1Head1 = retPart1Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	    return retPart1Head1;
	}

	
	

}
