package com.bohai.subAccount.vo;
//成交记录
public class SettlemenetPart4Head {
	
	private String tmpStr = "\r\n";
	
	private String retPart4Head1;

	public String getRetPart4Head1() {
		retPart4Head1 = "                                                              开仓明细 Positions Detail " ;
		retPart4Head1 = retPart4Head1 + tmpStr;
		retPart4Head1 = retPart4Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
		retPart4Head1 = retPart4Head1 + tmpStr;
		retPart4Head1 = retPart4Head1 + "| 交易所 |       品种       |      合约      |开仓日期 |   投/保    |买/卖|持仓量 |    开仓价     |     昨结算     |     结算价     |  浮动盈亏  |  盯市盈亏 |  保证金   |       期权市值       |";
		retPart4Head1 = retPart4Head1 + tmpStr;
		retPart4Head1 = retPart4Head1 + "|Exchange|     Product      |   Instrument   |Open Date|    S/H     | B/S |Positon|Pos. Open Price|   Prev. Sttl   |Settlement Price| Accum. P/L |  MTM P/L  |  Margin   | Market Value(Options)|";
		retPart4Head1 = retPart4Head1 + tmpStr;
		retPart4Head1 = retPart4Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	    return retPart4Head1;
	}

	
	

}
