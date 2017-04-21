package com.bohai.subAccount.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.bohai.subAccount.vo.UserVO;

/**
 * 用户配置信息
 * @author caojia
 */
public class UserConfig {
	
	
	private static final String PROPERTY_FILE_NAME = "user.properties";
	
	private static File sFile = new File(PROPERTY_FILE_NAME);
	
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
			} catch (Exception e) {
			e.printStackTrace();
			}
		
		
	}
	
	/**
	 * 通过属性名称查询属性值
	 * @param key
	 * @return
	 */
	public static String getPropertyByKey(String key){
		
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 读取Properties的全部信息
	 * @throws IOException
	 */
    public static List<UserVO> getAllProperties() {

    	List<UserVO> list = new ArrayList<UserVO>();
    	
    	try {
			Enumeration en = properties.propertyNames(); //得到配置文件的名字
			 
			while(en.hasMoreElements()) {
			    String strKey = (String) en.nextElement();
			    if(strKey.equals("rememberMe")){
			    	continue;
			    }
			    
			    String strValue = properties.getProperty(strKey);
			    UserVO vo = new UserVO();
			    vo.setUserName(strKey);
			    vo.setPasswd(strValue);
			    list.add(vo);
			     System.out.println(strKey + "=" + strValue);
			 }
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	return list;
        
     }
    
    /**
     * 删除所有属性
     */
    public static void removeAllProperties(){
    	
    	try {
			Enumeration en = properties.propertyNames(); //得到配置文件的名字
			 
			while(en.hasMoreElements()) {
			    String strKey = (String) en.nextElement();
			    properties.remove(strKey);
			 }
			
			/*URL url = UserConfig.class.getClassLoader().getResource(PROPERTY_FILE_NAME);
			
			String afterDecode = URLDecoder.decode(url.getFile(), "UTF-8");  
			System.out.println(afterDecode);
			OutputStream out = new FileOutputStream(afterDecode);*/
			OutputStream out = new FileOutputStream(sFile);
			//保存属性
			properties.store(out, "");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }
    
    public static void removePropertyByKey(String key){
    	
    	properties.remove(key);
    	try {
			/*URL url = UserConfig.class.getClassLoader().getResource(PROPERTY_FILE_NAME);
			
			String afterDecode = URLDecoder.decode(url.getFile(), "UTF-8");  
			System.out.println(afterDecode);
			OutputStream out = new FileOutputStream(afterDecode);*/
			
			OutputStream out = new FileOutputStream(sFile);
			//保存属性
			properties.store(out, "update " + key );
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
    	
    	UserConfig.setProperty("caojia", "123465");
    	//UserConfig.getPropertyByKey("caojia");
		//UserConfig.getAllProperties();
		//UserConfig.removePropertyByKey("caojia1");
    	//UserConfig.removeAllProperties();
	}

}
