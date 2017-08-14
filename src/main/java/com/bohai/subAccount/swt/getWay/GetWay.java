package com.bohai.subAccount.swt.getWay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.utils.DateFormatterUtil;
import com.bohai.subAccount.utils.SpringContextUtil;

import swing2swt.layout.BorderLayout;

public class GetWay {

    static Logger logger = Logger.getLogger(GetWay.class);
    public Shell shell;
    private Text marketText;
    private Table marketTable;
    private Composite composite;
    private MainAccountService mainAccountService;
    private MainAccount mainAccount;
    
    private Socket server;
    
    private List<Socket> clients;
    

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            GetWay window = new GetWay();
            window.loadSpringContext();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadSpringContext(){
		logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
        logger.info("===================加载成功 !==================");
	}

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        
      //查询主账号
  		List<MainAccount> listmainAccount;
		try {
			listmainAccount = mainAccountService.getMainAccount();
			if(listmainAccount != null && listmainAccount.size() >0){
	  			mainAccount = listmainAccount.get(0);
	  		} else {
	  			logger.error("查询主账号失败！！！");
	  			MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
	            box.setMessage("查询主账号失败！！！");
	            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
	            box.open();
	  		}
		} catch (FutureException e) {
			MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage(e.getMessage());
            box.setText(CommonConstant.MESSAGE_BOX_ERROR);
            box.open();
		}
  		
        //获取行情
        Thread marketReceiveThread = new Thread(new MarketReceiveThread(this));
        marketReceiveThread.setDaemon(true);
        marketReceiveThread.start();
        
        //服务端线程
        Thread serverThread = new Thread(new ServerThread(this, marketTable));
        serverThread.setDaemon(true);
        serverThread.start();
        
        //心跳线程
        Thread heartBeatThread = new Thread(new GetWayHeartBeatThread(this));
        heartBeatThread.setDaemon(true);
        heartBeatThread.start();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("行情转发服务端");
        shell.setLayout(new BorderLayout(0, 0));
        
        marketText = new Text(shell, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
        marketText.setLayoutData(BorderLayout.CENTER);
        
        marketTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION|SWT.H_SCROLL);
        marketTable.setLayoutData(BorderLayout.NORTH);
        marketTable.setHeaderVisible(true);
        marketTable.setLinesVisible(true);
        marketTable.setSize(0, 100);
        
        composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(BorderLayout.SOUTH);
        
        Button button = new Button(composite, SWT.NONE);
        button.setBounds(44, 27, 80, 27);
        button.setText("连接");
        
        Button button_1 = new Button(composite, SWT.NONE);
        button_1.setBounds(218, 27, 80, 27);
        button_1.setText("断开");
        
        Label lblNewLabel = new Label(composite, SWT.NONE);
        lblNewLabel.setBounds(363, 48, 61, 27);
        createMarketTable();

    }
    
    public void createMarketTable(){
            TableLayout tLayout = new TableLayout();//专用于表格的布局
            
            marketTable.setLayout(tLayout);
            
            tLayout.addColumnData(new ColumnWeightData(30));
            new TableColumn(marketTable, SWT.NONE).setText("远程ip");
            
            tLayout.addColumnData(new ColumnWeightData(30));
            new TableColumn(marketTable, SWT.NONE).setText("远程端口号");
            
            tLayout.addColumnData(new ColumnWeightData(30));
            new TableColumn(marketTable, SWT.NONE).setText("连接建立时间");
            
            tLayout.addColumnData(new ColumnWeightData(30));
            new TableColumn(marketTable, SWT.NONE).setText("是否断开");
            
    }
    
    public synchronized void addClient(Socket client){
    	if(clients == null){
    		clients = new ArrayList<Socket>();
    	}
    	clients.add(client);
    }
    
    //心跳
    public synchronized void heartBeat(){
    	
    	
    	try {
			if(clients != null){
				Iterator<Socket> iterator = clients.iterator();
				while(iterator.hasNext()){
					Socket socket = iterator.next();
					/*try {
						socket.sendUrgentData(0xFF);
					} catch (IOException e) {
						logger.error("发送心跳包失败",e);
						try {
							socket.close();
						} catch (IOException e1) {
							logger.error("关闭socket失败",e1);
						}
						iterator.remove();
					}*/
					try {
						PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
						out.println("test");
						out.flush();
						logger.info("向"+socket.getInetAddress()+"发送心跳数据成功");
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
						while(true){
							String s = in.readLine();
							if(s.equals("test")){
								logger.info("接收"+socket.getInetAddress()+"心跳数据成功");
								break;
							}
						}
					} catch (Exception e) {
						logger.error("建立心跳失败",e);
						iterator.remove();
					}
				}
				
				//刷新客户端列表
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						
						try {
							if(clients != null){
								//刷新客户端列表
								marketTable.removeAll();
								for (Socket client : clients) {
									TableItem tableItem= new TableItem(marketTable, SWT.NONE);
									tableItem.setText(0, client.getInetAddress().getHostAddress());
									tableItem.setText(1, client.getPort()+"");
									tableItem.setText(2, DateFormatterUtil.getCurrentDateStr());
									tableItem.setText(3, "否");
									shell.layout();
								}
							}
						} catch (Exception e) {
							logger.error("更新列表失败",e);
						}
					}
				});
			}
		} catch (Exception e) {
			logger.error("心跳异常",e);
		}
    	
    }

    public Socket getServer() {
        return server;
    }

    public void setServer(Socket server) {
        this.server = server;
    }
    
    public synchronized void printOut(String market){
    	
    	if(clients != null){
    		for (Socket socket : clients) {
    			try {
					PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
					
					out.println(market);
					out.flush();
				} catch (Exception e) {
					logger.error("获取输出流失败",e);
				}
			}
    	}
    	
    	Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				marketText.append("接收到行情"+market+"\r\n");
			}
		});
    }

	public MainAccount getMainAccount() {
		return mainAccount;
	}

	public void setMainAccount(MainAccount mainAccount) {
		this.mainAccount = mainAccount;
	}
    
}
