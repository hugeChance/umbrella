package com.bohai.subAccount.vo;
//成交记录
public class SettlemenetPart4Body {
	
	private String tmpStr = "|";
	

	private String Exchange                     ; //交易所
	private String Product                      ; //合约
	private String Instrument                   ; //合约
	private String OpenDate                          ; //开仓日期
	private String SH                          ; //投
	private String BS                          ; //买/卖
	private String Positon                         ; //VOLUME
	private String Price                        ; //PRICE
	private String Prev                        ; //昨结算
	private String Settlement                        ; //结算价
	private String Accum                     ; //浮动盈亏
	private String MTM                          ; //盯市盈亏
	private String Margin                       ; //保证金
	private String MarketValue                          ; //期权市值
	
	
	private String retStr;
	public String getRetStr() {
		//| 上期所 |        铝        |     al1706     | 20170512|投          |买   |      1|      13840.000|       13765.000|       13790.000|     -250.00|    -250.00|    6895.00|                  0.00|
		retStr = "";
		retStr = tmpStr;
		//交易所 全角
		retStr = retStr + retStrAddSpace(Exchange,2,8);
		retStr = retStr + tmpStr;
		//品种 半角
		retStr = retStr + retStrAddSpace(Product,1,18);
		retStr = retStr + tmpStr;
		// 合约 半角
		retStr = retStr + retStrAddSpace(Instrument,1,16);
		retStr = retStr + tmpStr;
		// 开仓日期 半角
		retStr = retStr + retStrAddSpace(OpenDate,1,9);
		retStr = retStr + tmpStr;
		//投/保 全角
		retStr = retStr + retStrAddSpace(SH,2,12);
		retStr = retStr + tmpStr;
		//买/卖全角
		retStr = retStr + retStrAddSpace(BS,2,5);
		retStr = retStr + tmpStr;
		//持仓量 半角
		retStr = retStr + retStrAddSpace(Positon,1,7);
		retStr = retStr + tmpStr;
		//开仓价 半角
		retStr = retStr + retStrAddSpace(Price,1,15);
		retStr = retStr + tmpStr;
		//昨结算 半角
		retStr = retStr + retStrAddSpace(Prev,1,16);
		retStr = retStr + tmpStr;
		//结算价 半角
		retStr = retStr + retStrAddSpace(Settlement,1,16);
		retStr = retStr + tmpStr;
		//浮动盈亏  半角
		retStr = retStr + retStrAddSpace(Accum,1,12);
		retStr = retStr + tmpStr;
		//盯市盈亏  半角
		retStr = retStr + retStrAddSpace(MTM,1,12);
		retStr = retStr + tmpStr;
		//保证金  半角
		retStr = retStr + retStrAddSpace(Margin,1,12);
		retStr = retStr + tmpStr;
		//期权市值  半角
		if(MarketValue == null)
		{
			retStr = retStr + "0.00";
			retStr = retStr + tmpStr;
		} else {
			retStr = retStr + retStrAddSpace(MarketValue,1,12);
			retStr = retStr + tmpStr;
		}
		
		return retStr;
	}
	
	
	public void setTmpStr(String tmpStr) {
		this.tmpStr = tmpStr;
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


	public void setSH(String sH) {
		SH = sH;
	}


	public void setBS(String bS) {
		BS = bS;
	}


	public void setPositon(String positon) {
		Positon = positon;
	}


	public void setPrice(String price) {
		Price = price;
	}


	public void setPrev(String prev) {
		Prev = prev;
	}


	public void setSettlement(String settlement) {
		Settlement = settlement;
	}


	public void setAccum(String accum) {
		Accum = accum;
	}


	public void setMTM(String mTM) {
		MTM = mTM;
	}


	public void setMargin(String margin) {
		Margin = margin;
	}


	public void setMarketValue(String marketValue) {
		MarketValue = marketValue;
	}


	public void setRetStr(String retStr) {
		this.retStr = retStr;
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
