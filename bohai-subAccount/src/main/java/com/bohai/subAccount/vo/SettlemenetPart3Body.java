package com.bohai.subAccount.vo;
//持仓汇总
public class SettlemenetPart3Body {
	
	private String tmpStr = "|";
	
	private String Product                ;
	private String Instrument             ;
	private String LongPos                ;
	private String AvgBuyPrice            ;
	private String ShortPos               ;
	private String AvgSellPrice           ;
	private String Prev                   ;
	private String SttlToday              ;
	private String MTM                    ;
	private String MarginOccupied         ;
	private String SH                     ;
	private String MarketValueLong        ;
	private String MarketValueShort       ;


	

	
	private String retStr;
	
	
	public String getRetStr() {
		//|        铝        |     al1706     |            1|    13840.000|             0|         0.000| 13765.000| 13790.000|     -250.00|        6895.00|投          |              0.00|               0.00|

		retStr = "";
		retStr = tmpStr;
		//品种
		retStr = retStr + retStrAddSpace(Product,2,18);
		retStr = retStr + tmpStr;
		//合约
		retStr = retStr + retStrAddSpace(Instrument,1,16);
		retStr = retStr + tmpStr;
		//买持
		retStr = retStr + retStrAddSpace(LongPos,1,13);
		retStr = retStr + tmpStr;
		//买均价
		retStr = retStr + retStrAddSpace(AvgBuyPrice,1,13);
		retStr = retStr + tmpStr;
		//卖持
		retStr = retStr + retStrAddSpace(ShortPos,1,14);
		retStr = retStr + tmpStr;
		//卖均价
		retStr = retStr + retStrAddSpace(AvgSellPrice,1,14);
		retStr = retStr + tmpStr;
		//昨结算
		retStr = retStr + retStrAddSpace(Prev,1,10);
		retStr = retStr + tmpStr;
		//今结算
		retStr = retStr + retStrAddSpace(SttlToday,1,10);
		retStr = retStr + tmpStr;
		//持仓盯市盈亏
		retStr = retStr + retStrAddSpace(MTM,1,12);
		retStr = retStr + tmpStr;
		//保证金占用
		retStr = retStr + retStrAddSpace(MarginOccupied,1,15);
		retStr = retStr + tmpStr;
		//投/保
		retStr = retStr + retStrAddSpace(SH,2,12);
		retStr = retStr + tmpStr;
		//多头期权市值
		if(MarketValueLong == null) {
			retStr = retStr + "0.00";
			retStr = retStr + tmpStr;
		} else {
			retStr = retStr + retStrAddSpace(MarketValueLong,1,18);
			retStr = retStr + tmpStr;
		}
		
		//空头期权市值
		if(MarketValueShort == null) {
			retStr = retStr + "0.00";
			retStr = retStr + tmpStr;
		} else {
			retStr = retStr + retStrAddSpace(MarketValueShort,1,19);
			retStr = retStr + tmpStr;
		}
		
		
		return retStr;
	}


	


	public void setProduct(String product) {
		Product = product;
	}





	public void setInstrument(String instrument) {
		Instrument = instrument;
	}





	public void setLongPos(String longPos) {
		LongPos = longPos;
	}





	public void setAvgBuyPrice(String avgBuyPrice) {
		AvgBuyPrice = avgBuyPrice;
	}





	public void setShortPos(String shortPos) {
		ShortPos = shortPos;
	}





	public void setAvgSellPrice(String avgSellPrice) {
		AvgSellPrice = avgSellPrice;
	}





	public void setPrev(String prev) {
		Prev = prev;
	}





	public void setSttlToday(String sttlToday) {
		SttlToday = sttlToday;
	}





	public void setMTM(String mTM) {
		MTM = mTM;
	}





	public void setMarginOccupied(String marginOccupied) {
		MarginOccupied = marginOccupied;
	}





	public void setSH(String sH) {
		SH = sH;
	}





	public void setMarketValueLong(String marketValueLong) {
		MarketValueLong = marketValueLong;
	}





	public void setMarketValueShort(String marketValueShort) {
		MarketValueShort = marketValueShort;
	}





	public String retStrAddSpace(String tmpStr,int style,int length ){
		int strleng = 0;
		String strSpace = "";
		if (style == 1){
			//半角
			if(length <= tmpStr.length()){
				return tmpStr.substring(0,length);
			} else {
				strleng = length-tmpStr.length();
				strSpace = "";
				for(int i =0;i < strleng;i ++){
					strSpace = strSpace + " ";
				}
				return tmpStr + strSpace;
			}
		} else {
			//全角
			if(length <= tmpStr.length() * 2){
				return tmpStr.substring(0,length/2);
			} else {
				strleng = length-tmpStr.length()*2;
				strSpace = "";
				for(int i =0;i < strleng;i ++){
					strSpace = strSpace + " ";
				}
				return tmpStr + strSpace;
			}
			
		}
		

	}
	

}
