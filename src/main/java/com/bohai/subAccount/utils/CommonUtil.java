package com.bohai.subAccount.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	
	public BigDecimal isNull(BigDecimal bigDate){
		
		BigDecimal bigRet = new BigDecimal(0);
		if (null != bigDate) {
			return bigDate;
		} 
		return bigRet;
	}
	
	
	static List<String> SHINST ;
    
    static {
        SHINST = new ArrayList<String>();
        SHINST.add("cu");
        SHINST.add("al");
        SHINST.add("zn");
        SHINST.add("pb");
        SHINST.add("ru");
        SHINST.add("fu");
        SHINST.add("rb");
        SHINST.add("wr");
        SHINST.add("au");
        SHINST.add("ag");
        SHINST.add("bu");
        SHINST.add("hc");
        SHINST.add("ni");
        SHINST.add("sn");
    }
    
    //是否上海合约
    public static boolean checkSHPosition(String HyName) {
        //check 持仓是否是上海的。只有上海要平今平昨
        boolean retFlg = false;
        if(HyName.length() == 6){
            for (String hyName : SHINST) {
                if(HyName.substring(0, 2).equals(hyName)){
                    //上海
                    return true;
                }
            }
            
        }
        return retFlg;
        
    }

}
