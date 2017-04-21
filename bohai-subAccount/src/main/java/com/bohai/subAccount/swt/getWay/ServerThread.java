package com.bohai.subAccount.swt.getWay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.bohai.subAccount.utils.DateFormatterUtil;

public class ServerThread implements Runnable {
	
	static Logger logger = Logger.getLogger(ServerThread.class);
	
    private static final String ip = "localhost";
    private static final int port = 3393;

	
	private GetWay getWay;
	
	private Table marketTable;
	
	public ServerThread(GetWay getWay, Table marketTable) {
		this.getWay = getWay;
		this.marketTable = marketTable;
	}

	@Override
	public void run() {

		logger.info("监听客户端线程启动...");
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true){
				Socket client = serverSocket.accept();
				logger.info("客户端已接入："+client.getInetAddress());
				getWay.addClient(client);
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run() {
						TableItem item = new TableItem(marketTable, SWT.None);
						item.setText(0, client.getInetAddress().getHostAddress());
						item.setText(1, client.getPort()+"");
						item.setText(2, DateFormatterUtil.getCurrentDateStr());
						item.setText(3, "否");
					}
				});
			}
		} catch (IOException e) {
			logger.error("创建socket服务端失败",e);
		}
	}

}
