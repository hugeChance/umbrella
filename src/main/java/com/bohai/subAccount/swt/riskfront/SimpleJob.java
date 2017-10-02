/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package com.bohai.subAccount.swt.riskfront;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.utils.SpringContextUtil;

/**
 * <p>
 * This is just a simple job that gets fired off many times by example 1
 * </p>
 * 
 * @author Bill Kratzer
 */
public class SimpleJob implements Job {
	
	//static final String TRADE_IP = "10.0.0.202";
	static final String TRADE_IP = ApplicationConfig.getProperty("tradeAddr");

    private static Logger _log = Logger.getLogger(SimpleJob.class);
    private Socket tradeSocket;
    private UserInfoService userInfoService;

    /**
     * Quartz requires a public empty constructor so that the
     * scheduler can instantiate the class whenever it needs.
     */
    public SimpleJob() {
    }

    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     * 
     * @throws JobExecutionException
     *             if there is an exception while executing the job.
     */
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
    	
    	_log.debug("------------触发定时任务--------------");
    	
    	try {
			RiskFrontView riskManageView = (RiskFrontView) context.getJobDetail().getJobDataMap().get("risk");
			Display.getDefault().syncExec(new Runnable() {
				
				@Override
				public void run() {
					riskManageView.closeJob();
				}
			});
			
		} catch (Exception e1) {
			_log.error("定时任务调用UI线程执行强平操作失败",e1);
		}
    	
    	_log.debug("------------定时任务执行完成--------------");
    	
        // This job simply prints out its job name and the
        // date and time that it is running
//    	System.out.println("=========================================调度启动===========================================");
//        JobKey jobKey = context.getJobDetail().getKey();
//        _log.info("SimpleJob says: " + jobKey + " executing at " + new Date());
/*    	getTradeSocket();
    	try {
    		List<UserInfo> userInfos = null;
    		userInfoService = (UserInfoService) SpringContextUtil.getBean("userInfoService");
    		try {
				userInfos = userInfoService.getUsersByGroup();
			} catch (FutureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		PrintWriter out = new PrintWriter(new OutputStreamWriter(tradeSocket.getOutputStream(),"UTF-8"));
            StringBuffer stringBuffer;
            for (UserInfo item : userInfos) {
            	stringBuffer = new StringBuffer();
            	stringBuffer.append("risk|");
                stringBuffer.append(item.getUserName() + "|");
                stringBuffer.append("QPXZ" + "|");
                _log.info("强平限制参数：" + stringBuffer.toString());
                out.println(stringBuffer.toString());
                out.flush();
            }

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    
    public void getTradeSocket(){
        //如果socket为空 或者断开连接则创建一个socket
        if(tradeSocket == null || !(tradeSocket.isConnected() == true && tradeSocket.isClosed() == false)){
            try {
            	_log.info("=====================开始与交易服务器建立连接==============");
                tradeSocket = new Socket(TRADE_IP,3394);
                tradeSocket.setKeepAlive(false);
            } catch (Exception e) {
            	_log.error("与交易服务器通信失败",e);
            }
        }
    }

}
