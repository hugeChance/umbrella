package com.bohai.subAccount;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_D_Buy;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_LimitPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;

import com.alibaba.fastjson.JSON;
import com.bohai.subAccount.entity.UserContract;

public class Test {
	
	public static void main(String[] args) throws ParseException {
		Calendar c = Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int mouth=c.get(Calendar.MONTH)+1;
		int day=c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute=c.get(Calendar.MINUTE);
		int second=c.get(Calendar.SECOND);
		
		Date date = new Date();//获取当前时间 
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date); 
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int mouth2=c.get(Calendar.MONTH);
		int year2=c.get(Calendar.YEAR);
		int day2=calendar.get(Calendar.DAY_OF_MONTH );
	}
	
	public void test4(){
	    System.out.println("123:"+null);
	}
	
	
	public void test() throws ParseException{
		
		
		Long st = Long.valueOf("33308");
		
		System.out.println(st);
		
		Map<String, UserContract> map = new HashMap<String, UserContract>();
		UserContract contract = new UserContract();
		map.put("1", contract);
		
		UserContract c1 = map.get("1");
		c1.setContractNo("123");
		
		System.out.println(JSON.toJSON(map));
		
		int i = 3;
		
		short s = 2;
		
		System.out.println("int+short:"+(i+s));
		
		System.out.println(i <= s);
		
		String s1 = "1";
		
		char c = (char)'0';
		
		System.out.println("char比较："+ (c == '0'));
		
		String d = "103.2000000000003";
		
		BigDecimal b = new BigDecimal(d);
		
		System.out.println(b);
		
		
		BigDecimal bd = new BigDecimal("1234.1895472").setScale(2, RoundingMode.HALF_UP);
		System.out.println(bd.toString());
		
		//System.out.println(s1.equals(""));
		
		/*for(int i =0 ;i<1;i++){
			System.out.println(i);
		}*/
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date d = sdf.parse("14:25:1");
		Date d1 = sdf.parse("14:1:14");
		
		System.out.println(d1.before(d));*/
	}
	
	public void test3(){
	    String s = null;
	    
	    System.out.println(s);
	}

}
