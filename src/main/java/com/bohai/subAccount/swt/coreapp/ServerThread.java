package com.bohai.subAccount.swt.coreapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ServerThread implements Runnable {
	
	static Logger logger = Logger.getLogger(ServerThread.class);
	
    private static final String ip = "localhost";
    private static final int port = 3394;

	
	private CoreappView coreappView;
	

	
	public ServerThread(CoreappView coreappView) {
		this.coreappView = coreappView;
		
	}

	@Override
	public void run() {

		logger.info("监听客户端线程启动...");
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true){
				Socket client = serverSocket.accept();
				new Thread(new Task(client,coreappView)).start(); 
				logger.info("客户端已接入："+client.getInetAddress());
				coreappView.addClient(client);
//				Display.getDefault().syncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						
//					}
//				});
			}
		} catch (IOException e) {
			logger.error("创建socket服务端失败",e);
		}
	}
	
	     
}
	  
	 

