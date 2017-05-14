package com.bohai.subAccount.vo;
//成交记录
public class SettlemenetPart1Body {
	
	private String tmpStr = "|";
	
	private String Date                         ; 
	private String Exchange                     ;
	private String Product                      ;
	private String Instrument                   ;
	private String BS                          ;
	private String SH                          ;
	private String Price                        ;
	private String Lots                         ;
	private String Turnover                     ;
	private String OC                          ;
	private String Fee                          ;
	private String Realized                  ;
	private String Premium         ;
	private String TransNo                    ;
	
	private String retStr;
	public String getRetStr() {
		//|20170512|上期所  |铝                |     al1706     |买   |投          | 13840.000|     1|    69200.00|开                |      3.00|        0.00|                 0.00|98274       |
		retStr = "";
		retStr = tmpStr;
		//成交日期 半角
		retStr = retStr + Date;		
		retStr = retStr + tmpStr;
		//交易所 全角
		retStr = retStr + retStrAddSpace(Exchange,2,8);
		retStr = retStr + tmpStr;
		//品种 半角
		retStr = retStr + retStrAddSpace(Product,1,18);
		retStr = retStr + tmpStr;
		// 合约 半角
		retStr = retStr + retStrAddSpace(Instrument,1,16);
		retStr = retStr + tmpStr;
		// 买/卖 全角
		retStr = retStr + retStrAddSpace(BS,2,5);
		retStr = retStr + tmpStr;
		//投/保 全角
		retStr = retStr + retStrAddSpace(SH,2,12);
		retStr = retStr + tmpStr;
		//成交价 半角
		retStr = retStr + retStrAddSpace(Price,1,10);
		retStr = retStr + tmpStr;
		//手数 半角
		retStr = retStr + retStrAddSpace(Lots,1,6);
		retStr = retStr + tmpStr;
		//成交额 半角
		retStr = retStr + retStrAddSpace(Turnover,1,12);
		retStr = retStr + tmpStr;
		//开平 全角
		retStr = retStr + retStrAddSpace(OC,2,18);
		retStr = retStr + tmpStr;
		//手续费 半角
		retStr = retStr + retStrAddSpace(Fee,1,10);
		retStr = retStr + tmpStr;
		//平仓盈亏 半角
		retStr = retStr + retStrAddSpace(Realized,1,12);
		retStr = retStr + tmpStr;
		//权利金收支  半角
		retStr = retStr + retStrAddSpace(Premium,1,21);
		retStr = retStr + tmpStr;
		//成交序号  半角
		retStr = retStr + retStrAddSpace(TransNo,1,12);
		retStr = retStr + tmpStr;
		return retStr;
	}
	public void setDate(String date) {
		Date = date;
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
	public void setBS(String bS) {
		BS = bS;
	}
	public void setSH(String sH) {
		SH = sH;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public void setLots(String lots) {
		Lots = lots;
	}
	public void setTurnover(String turnover) {
		Turnover = turnover;
	}
	public void setOC(String oC) {
		OC = oC;
	}
	public void setFee(String fee) {
		Fee = fee;
	}
	public void setRealizedP(String Realized) {
		Realized = Realized;
	}
	public void setPremiumReceived(String Premium) {
		Premium = Premium;
	}
	public void setTransNo(String transNo) {
		TransNo = transNo;
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
