package com.bohai.subAccount.vo;
//平仓明细
public class SettlemenetPart2Head {
	
	private String tmpStr = "\r\n";
	
	private String retPart2Head1;

	public String getRetPart2Head1() {
		retPart2Head1 = "                                                         平仓明细 Position Closed " ;
		retPart2Head1 = retPart2Head1 + tmpStr;
		retPart2Head1 = retPart2Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
		retPart2Head1 = retPart2Head1 + tmpStr;
		retPart2Head1 = retPart2Head1 + "| 平仓日期 | 交易所 |       品种       |      合约      |开仓日期 |买/卖|   手数   |     开仓价    |     昨结算     |   成交价   |  平仓盈亏  |     权利金收支      |";
		retPart2Head1 = retPart2Head1 + tmpStr;
		retPart2Head1 = retPart2Head1 + "|Close Date|Exchange|      Product     |   Instrument   |Open Date| B/S |   Lots   |Pos. Open Price|   Prev. Sttl   |Trans. Price|Realized P/L|Premium Received/Paid|";
		retPart2Head1 = retPart2Head1 + tmpStr;
		retPart2Head1 = retPart2Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	    return retPart2Head1;
	}

	
	

}
