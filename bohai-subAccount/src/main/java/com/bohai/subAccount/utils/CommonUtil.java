package com.bohai.subAccount.utils;

import java.math.BigDecimal;

public class CommonUtil {
	
	public BigDecimal isNull(BigDecimal bigDate){
		
		BigDecimal bigRet = new BigDecimal(0);
		if (null != bigDate) {
			return bigDate;
		} 
		return bigRet;
	}

}
