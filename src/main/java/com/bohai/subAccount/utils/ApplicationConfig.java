package com.bohai.subAccount.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.util.StringUtils;

public class ApplicationConfig {
	
	private static final String APPLICATION_PROPERTY_FILE_NAME = "application.properties";
	
	private static File sFile = new File(APPLICATION_PROPERTY_FILE_NAME);
	
	private static Properties properties;
	
	static {
		/*try {
			//获取配置文件输入流
			InputStream inputStream = UserConfig.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
			
			properties = new Properties();
			//载入配置文件
			properties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			if (!sFile.exists()) {
				sFile.createNewFile();
			}
			System.out.println("配置文件路徑："+sFile.getAbsolutePath());
			
			properties = new Properties();
			
			FileInputStream fis = new FileInputStream(sFile);
			
			properties.load(fis);
			
			fis.close();
			
			//初始化交易地址
			if(StringUtils.isEmpty(getProperty("tradeAddr"))){
				setProperty("tradeAddr", "localhost");
			}
			
			//初始化行情地址
			if(StringUtils.isEmpty(getProperty("marketAddr"))){
				setProperty("marketAddr", "localhost");
			}
			
			//行情前置机
			if(StringUtils.isEmpty(getProperty("marketFrontAddr"))){
				setProperty("marketFrontAddr", "tcp://218.202.237.33:10012");
			}
			
			//交易前置机
			if(StringUtils.isEmpty(getProperty("tradeFrontAddr"))){
				setProperty("tradeFrontAddr", "tcp://218.202.237.33:10002");
			}
			
			//默认手数
			if(StringUtils.isEmpty(getProperty("volume"))){
				setProperty("volume", "1");
			}
			
			if(StringUtils.isEmpty(getProperty("addressFirst"))){
                setProperty("addressFirst", "localhost");
            }
			
			if(StringUtils.isEmpty(getProperty("portFirst"))){
			    setProperty("portFirst", "3398");
			}
			

            if(StringUtils.isEmpty(getProperty("addressSecond"))){
                setProperty("addressSecond", "localhost");
            }
			
			if(StringUtils.isEmpty(getProperty("portSecond"))){
			    setProperty("portSecond", "3399");
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过属性名称查询属性值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key){
		
		String value = "";
		try {
			value = properties.getProperty(key);
			
			System.out.println(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	/**
	 * 设置属性
	 * @param key
	 * @param value
	 */
	public static void setProperty(String key, String value){
		
		//先删除所有
		//removeAllProperties();
		
		
		try {
			properties.setProperty(key, value);
			
			//URL url = UserConfig.class.getClassLoader().getResource(PROPERTY_FILE_NAME);
			
			//String afterDecode = URLDecoder.decode(url.getFile(), "UTF-8");  
			
			//System.out.println(afterDecode);
			//OutputStream out = new FileOutputStream(afterDecode);
			
			OutputStream out = new FileOutputStream(sFile);
			//保存属性
			properties.store(out, "");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
