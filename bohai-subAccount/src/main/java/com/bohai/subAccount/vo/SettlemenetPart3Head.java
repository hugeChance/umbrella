package com.bohai.subAccount.vo;
//持仓汇总
public class SettlemenetPart3Head {
	
	private String tmpStr = "\r\n";
	
	private String retPart3Head1;

	public String getRetPart3Head1() {
		retPart3Head1 = "                                                         持仓汇总 Positions" ;
		retPart3Head1 = retPart3Head1 + tmpStr;
		retPart3Head1 = retPart3Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
		retPart3Head1 = retPart3Head1 + tmpStr;
		retPart3Head1 = retPart3Head1 + "|       品种       |      合约      |    买持     |    买均价   |     卖持     |    卖均价    |  昨结算  |  今结算  |持仓盯市盈亏|  保证金占用   |  投/保     |   多头期权市值   |   空头期权市值    |";
		retPart3Head1 = retPart3Head1 + tmpStr;
		retPart3Head1 = retPart3Head1 + "|      Product     |   Instrument   |  Long Pos.  |Avg Buy Price|  Short Pos.  |Avg Sell Price|Prev. Sttl|Sttl Today|  MTM P/L   |Margin Occupied|    S/H     |Market Value(Long)|Market Value(Short)|";
		retPart3Head1 = retPart3Head1 + tmpStr;
		retPart3Head1 = retPart3Head1 + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	    return retPart3Head1;
	}

	
	

}
