package com.bohai.subAccount.vo;
//平仓明细
public class SettlemenetPart2Body {
	
	private String tmpStr = "|";
	
	private String CloseDate  ;
	private String Exchange   ;
	private String Product    ;
	private String Instrument ;
	private String OpenDate   ;
	private String BS         ;
	private String Lots       ;
	private String Pos        ;
	private String Prev       ;
	private String Trans      ;
	private String Realized   ;
	private String Premium    ;

	
	private String retStr;
	
	
	public String getRetStr() {
		//|20170512  |上期所  |铝                |al1707          |20170512 |   卖|         1|      13880.000|       13805.000|   13875.000|      -25.00|                 0.000|

		retStr = "";
		retStr = tmpStr;
		//平仓日期
		retStr = retStr + retStrAddSpace(CloseDate,1,10);
		retStr = retStr + tmpStr;
		//交易所
		retStr = retStr + retStrAddSpace(Exchange,2,8);
		retStr = retStr + tmpStr;
		//品种
		retStr = retStr + retStrAddSpace(Product,1,18);
		retStr = retStr + tmpStr;
		//合约
		retStr = retStr + retStrAddSpace(Instrument,1,16);
		retStr = retStr + tmpStr;
		//开仓日期
		retStr = retStr + retStrAddSpace(OpenDate,1,9);
		retStr = retStr + tmpStr;
		//买/卖
		retStr = retStr + retStrAddSpace(BS,1,5);
		retStr = retStr + tmpStr;
		//手数
		retStr = retStr + retStrAddSpace(Lots,1,10);
		retStr = retStr + tmpStr;
		//开仓价
		retStr = retStr + retStrAddSpace(Pos,1,15);
		retStr = retStr + tmpStr;
		//昨结算
		retStr = retStr + retStrAddSpace(Prev,1,16);
		retStr = retStr + tmpStr;
		//成交价
		retStr = retStr + retStrAddSpace(Trans,1,12);
		retStr = retStr + tmpStr;
		//平仓盈亏
		retStr = retStr + retStrAddSpace(Realized,1,12);
		retStr = retStr + tmpStr;
		//权利金收支
		retStr = retStr + retStrAddSpace(Premium,1,21);
		retStr = retStr + tmpStr;
		
		return retStr;
	}


	public void setCloseDate(String closeDate) {
		CloseDate = closeDate;
	}


	public void setExchange(String exchange) {
		Exchange = exchange;
	}


	public void setProduct(String product) {
		Product = product;
	}


	public void setInstrument(String instrument) {
		Instrument = instrument;
	}


	public void setOpenDate(String openDate) {
		OpenDate = openDate;
	}


	public void setBS(String bS) {
		BS = bS;
	}


	public void setLots(String lots) {
		Lots = lots;
	}


	public void setPos(String pos) {
		Pos = pos;
	}


	public void setPrev(String prev) {
		Prev = prev;
	}


	public void setTrans(String trans) {
		Trans = trans;
	}


	public void setRealized(String realized) {
		Realized = realized;
	}


	public void setPremium(String premium) {
		Premium = premium;
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
