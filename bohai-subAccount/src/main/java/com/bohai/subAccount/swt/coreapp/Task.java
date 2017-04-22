package com.bohai.subAccount.swt.coreapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.swt.admin.AdminViewMain;
import com.bohai.subAccount.utils.SpringContextUtil;

/** 
 * 用来处理Socket请求的 
*/  
public class Task implements Runnable {  
	
	static Logger logger = Logger.getLogger(Task.class);

   private Socket socket;  
   
   private CoreappView coreappView;

     
   public Task(Socket socket, CoreappView coreappView) {  
      this.socket = socket;  
      this.coreappView = coreappView;
   }  
     
   public void run() {  
      try {  
         handleSocket();  
      } catch (Exception e) {  
         e.printStackTrace();  
      }  
   }
   
   /** 
    * 跟客户端Socket进行通信 
   * @throws Exception 
    */  
   private void handleSocket() throws Exception {  
      
      StringBuilder sb = new StringBuilder();  
      String[] templist;
      String controlID;
      String subAccount;
      String strJson;
      String password;
      int index; 
      String riskstr;
      
//      MainAccountService mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
      while (true) {
    	  
    	  
    	  String temp = br.readLine();
    	  
    	  Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				coreappView.getTradeRequest().append(temp+"\r\n");
			}
		});
    	  
         System.out.println(temp);
         templist = temp.split("\\|");
         controlID = templist[0];
         subAccount = templist[1];
         
         if(controlID.equals("hello")){
        	 logger.info("TASK登入操作");
        	 password = templist[2];
             //登入操作
        	 coreappView.subLogin(subAccount, password);
         }
         
         
         if(controlID.equals("order")){
        	 logger.info("TASK下单操作");
             //下单操作
        	 
             strJson = templist[2];
        	 coreappView.subOrder(subAccount, strJson);
         }
         
         if(controlID.equals("bye")){
        	 logger.info("TASK登入操作");
        	 password = templist[2];
             //登入操作
        	 coreappView.subLoginout(subAccount, password);
        	 socket.close();
        	 coreappView.getClients().remove(socket);
        	 break;
         }
         
         if(controlID.equals("cancel")){
        	 logger.info("TASK撤单操作");
             //下单操作
//        	 Thread.sleep(5);
             strJson = templist[2];
        	 coreappView.subOrderAction(subAccount, strJson);
         }
         
         if(controlID.equals("risk")){
        	 //logger.info("TASK-risk-命令");
        	 riskstr = templist[2];
             //下单操作
        	 //logger.info("操作交易员名："+subAccount+"|命令是:"+riskstr);
        	 if(riskstr.equals("QPXZ")){
        		 logger.info("操作交易员名："+subAccount+"|命令是:"+riskstr);
        		 //强平限制
        		 coreappView.riskCommand1(subAccount);
        	 }else if(riskstr.equals("FKQP")){
        		 logger.info("操作交易员名："+subAccount+"|命令是:"+riskstr);
        		 //风控强平
        		 coreappView.riskForceClose(subAccount,templist[3]);
        	 }else if(riskstr.equals("YJCD")){
        		 logger.info("操作交易员名："+subAccount+"|命令是:"+riskstr);
        		 //一键撤单
        		 coreappView.riskCommand2(subAccount);
        	 }else if(riskstr.equals("XZKC")){
        		 logger.info("操作交易员名："+subAccount+"|命令是:"+riskstr);
        		 //限制开仓
        		 coreappView.riskCommand3(subAccount);
        	 }else if(riskstr.equals("QXXZ")){
        		 logger.info("操作交易员名："+subAccount+"|命令是:"+riskstr);
        		 //取消限制
        		 coreappView.riskCommand4(subAccount);
        	 }else if(riskstr.equals("CCYK")){
        		 //持仓盈亏
        		 coreappView.riskCCYK(subAccount,templist[3]);
        	 }else if(riskstr.equals("TPXZ")){
        		 //用户名 合约号 开仓方向
        		 coreappView.riskTPXZ(subAccount,templist[3],templist[4]);
        	 }else if(riskstr.equals("CRJ")){
        		 //出入金
        		 coreappView.riskCRJ(subAccount,templist[3]);
        	 }
//        	 coreappView.subOrderAction(subAccount, strJson);
         }
         
         
         
         
         
         
//         if ((index = temp.indexOf("")) != -1) {//遇到eof时就结束接收  
//        	//读完后写一句  
//        	    Writer writer = new OutputStreamWriter(socket.getOutputStream());  
//        	      writer.write("我是服务器，客户端你好");  
//        	      writer.write("eof\n");  
//        	      writer.flush();  
//        	      writer.close();  
//        	 
//        	 
//        	 
//          sb.append(temp.substring(0, index));  
////             break;  
//         }  
         //sb.append(temp);  
      }  
      
      
//      br.close();  
//      socket.close();  
   }  
}  
