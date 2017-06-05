package com.bohai.subAccount.swt.coreapp;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_AF_Delete;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_AnyPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcQryInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoConfirmField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradingAccountField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bohai.subAccount.constant.ErrorConstant;
import com.bohai.subAccount.dao.UseravailableindbMapper;
import com.bohai.subAccount.entity.BuyDetail;
import com.bohai.subAccount.entity.InputOrder;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.MainAccount;
import com.bohai.subAccount.entity.Order;
import com.bohai.subAccount.entity.PositionsDetail;
import com.bohai.subAccount.entity.SellDetail;
import com.bohai.subAccount.entity.SubTradingaccount;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.TradeRule;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserFrozenaccount;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.entity.UserLogin;
import com.bohai.subAccount.entity.Useravailableindb;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.BuyDetailService;
import com.bohai.subAccount.service.InputOrderService;
import com.bohai.subAccount.service.InvestorPositionService;
import com.bohai.subAccount.service.MainAccountService;
import com.bohai.subAccount.service.OrderService;
import com.bohai.subAccount.service.PositionsDetailService;
import com.bohai.subAccount.service.SellDetailService;
import com.bohai.subAccount.service.SubTradingaccountService;
import com.bohai.subAccount.service.TradeRuleService;
import com.bohai.subAccount.service.TradeService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.service.UserFrozenaccountService;
import com.bohai.subAccount.service.UserInfoService;
import com.bohai.subAccount.service.UserLoginService;
import com.bohai.subAccount.swt.coreapp.help.CtpConnectThread;
import com.bohai.subAccount.utils.ApplicationConfig;
import com.bohai.subAccount.utils.SpringContextUtil;
import com.bohai.subAccount.vo.UserAvailableMemorySave;
import com.bohai.subAccount.vo.UserFlgMemorySave;
import com.bohai.subAccount.vo.UserTradeRuleMemorySave;

public class CoreappView {
    
    static Logger logger = Logger.getLogger(CoreappView.class);
    
    private MainAccountService mainAccountService;
    
    int nRequestID = 0;
//    String orderRef = "";
    
    static AtomicInteger atomicInteger;
    int sessionID = 0;
    int frontID = 0;
    int CTPerrID = 0;
    int tradingAccountnRequestID = 0;
    
    /** 测试前置机地址 **/
    //static String frontAddr = "tcp://180.169.116.120:41205";
    //实盘交易地址
    
    //static String frontAddr = "tcp://180.169.116.119:41205";
    static String frontAddr = ApplicationConfig.getProperty("tradeFrontAddr");
    
    /** 交易API **/
    static JCTPTraderApi CTPApi;
    static MyTraderSpi CTPSpi;

    protected Shell shell;
    private Text tradeResponse;
    private Text ctpRequest;
    private Text ctpResponse;
    private Text tradeRequest;
    private MainAccount mainAccount;
    private UserLoginService userLoginService;
    private UserInfoService userInfoService;
    private UserContractService userContractService;
    
    private SubTradingaccountService subTradingaccountService;
    private InputOrderService inputOrderService;
    private InvestorPositionService investorPositionService;
    private TradeService tradeService;
    private OrderService orderService;
    private TradeRuleService tradeRuleService;
    
    private UserFrozenaccountService userFrozenaccountService;
    private UseravailableindbMapper useravailableindbMapper;
    
    private BuyDetailService buyDetailService;
    private SellDetailService sellDetailService;
    private PositionsDetailService positionsDetailService;
    
    
    private Socket CTPsocket;
    //买开卖平socket
    private Socket ctpFirst;
    //买开卖平输出流
    private PrintWriter outFirst;
    //卖开买平socket
    private Socket ctpSecond;
    //卖开买平输出流
    private PrintWriter outSecond;
    
     private List<Socket> clients;
     
     private Map<String,UserContract> mapUserContractMemorySave;
     
     private Map<String,String>  mapUserAccountMemorySave;
     
     private Map<String,UserFlgMemorySave> mapUserFlgMemorySave;
     
     private Map<String,UserTradeRuleMemorySave> mapTradeRuleMemorySave;
     
     private Map<String,UserAvailableMemorySave> mapAvailableMemorySave;
     
     
     
     
    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            CoreappView window = new CoreappView();
            window.loadSpringContext();
            atomicInteger = new AtomicInteger(0);
            window.setMemory();
            window.open();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void addClient(Socket client){
        if(clients == null){
            clients = new ArrayList<Socket>();
        }
        clients.add(client);
    }
    
    public void setMemory(){
        UserContract userContract = new UserContract();
        StringBuffer sb = new StringBuffer();
        mapUserContractMemorySave = new HashMap<String,UserContract>();
        mapUserAccountMemorySave = new HashMap<String,String>();
        mapUserFlgMemorySave = new HashMap<String,UserFlgMemorySave>();
        mapTradeRuleMemorySave = new HashMap<String,UserTradeRuleMemorySave>();
        mapAvailableMemorySave = new HashMap<String,UserAvailableMemorySave>();
        SubTradingaccount subTradingaccount = new SubTradingaccount();
        try {
            List<UserContract> listUserContract = userContractService.queryUserContractByAll();
            for (UserContract userContract2 : listUserContract) {
                logger.info("setMemory="+JSON.toJSONString(userContract2));
                
//                listUserContractMemorySave.set
                mapUserContractMemorySave.put(userContract2.getUserNo() +userContract2.getContractNo(), userContract2);
                
                
            }
            

            List<SubTradingaccount> listSubTradingaccount = subTradingaccountService.getUserByAllForAccount();
            for (SubTradingaccount subTradingaccount2 : listSubTradingaccount) {
                logger.info("setMemory="+JSON.toJSONString(subTradingaccount2));
                mapUserAccountMemorySave.put(subTradingaccount2.getAccountid(), String.valueOf(subTradingaccount2.getAvailable()));
            }
            
            List<UserInfo> listUserInfo =userInfoService.getUsersByGroup();
            for (UserInfo userInfo : listUserInfo) {
                UserFlgMemorySave userFlgMemorySave = new UserFlgMemorySave();
                userFlgMemorySave.setUserName(userInfo.getUserName());
                userFlgMemorySave.setFlag1("0");
                userFlgMemorySave.setFlag2("0");
                userFlgMemorySave.setFlag3("0");
                logger.info("setMemory="+JSON.toJSONString(userInfo));
                mapUserFlgMemorySave.put(userInfo.getUserName(), userFlgMemorySave);
            }
            
            //最大可开仓数
            List<TradeRule> listTradeRule = tradeRuleService.getTradeRulesByAll();
            for (TradeRule tradeRule : listTradeRule) {
                
                if(tradeRule.getEntrustCount() > 0) {
                    logger.info("set最大可开仓数="+JSON.toJSONString(tradeRule));
                    UserTradeRuleMemorySave userTradeRuleMemorySave = new UserTradeRuleMemorySave();
                    userTradeRuleMemorySave.setMaxCancelCount(tradeRule.getCancelCount());
                    userTradeRuleMemorySave.setMaxEntrustCount(tradeRule.getEntrustCount());
                    userTradeRuleMemorySave.setMaxOpenCount(tradeRule.getOpenCount());
                    userTradeRuleMemorySave.setContract(tradeRule.getContract());
                    userTradeRuleMemorySave.setRealCancelCount(0);
                    userTradeRuleMemorySave.setRealEntrustCount(0);
                    userTradeRuleMemorySave.setRealOpenCount(0);
                    logger.info("set最大可开仓数Memory="+JSON.toJSONString(userTradeRuleMemorySave));
                    mapTradeRuleMemorySave.put(tradeRule.getContract(),userTradeRuleMemorySave);
                }
                
            }
            
            //可用资金初始化计算

            double pcyk = 0;
            double totalpcyk = 0;
            double zybzj = 0;
            double totalzybzj = 0;
            for (SubTradingaccount subTradingaccount2 : listSubTradingaccount) {
                logger.info("可用资金初始化计算 SubTradingaccount="+JSON.toJSONString(subTradingaccount2));
                UserAvailableMemorySave userAvailableMemorySave = new UserAvailableMemorySave();
                userAvailableMemorySave.setUserName(subTradingaccount2.getAccountid());
                userAvailableMemorySave.setAvailable(subTradingaccount2.getAvailable().toString());
                BigDecimal frozenAvailable = subTradingaccount2.getFrozenmargin().add(subTradingaccount2.getFrozencommission());
                userAvailableMemorySave.setFrozenAvailable(frozenAvailable.toString());
                userAvailableMemorySave.setCommission(subTradingaccount2.getCommission().toString());
                userAvailableMemorySave.setInOutMoney("0");
                
                //平仓盈亏 和持仓盈亏 一句SQL查出
                List<InvestorPosition> getUserByUserName = investorPositionService.getUserByUserName(subTradingaccount2.getAccountid());
                if (getUserByUserName.size() > 0) {
                    for (InvestorPosition investorPosition2 : getUserByUserName) {
                        userContract = mapUserContractMemorySave.get(investorPosition2.getSubuserid() +investorPosition2.getInstrumentid());
                        if(investorPosition2.getPosition() == 0){
                            pcyk = 0;
                            //平仓盈亏
                            if(investorPosition2.getPosidirection().equals("0")){
                                //平仓盈亏 买开 * 手数
                                pcyk = (investorPosition2.getCloseamount().doubleValue()-investorPosition2.getOpenamount().doubleValue())*userContract.getContractUnit().doubleValue();
                                pcyk = pcyk* investorPosition2.getClosevolume();
                            }else{
                                //平仓盈亏 卖开 * 手数
                                pcyk = (investorPosition2.getOpenamount().doubleValue()-investorPosition2.getCloseamount().doubleValue())*userContract.getContractUnit().doubleValue();
                                pcyk = pcyk* investorPosition2.getClosevolume();
                            }
                            
                            totalpcyk = totalpcyk + pcyk;
                            logger.debug(investorPosition2.getInstrumentid()+"平仓盈亏:totalpcyk:"+totalpcyk);
                                
                        } else {
                            //占用保证金
                            //合约保证金 = 价格 * 合约单位 * 保证金比例 * 手数
                            
                            zybzj = investorPosition2.getOpenamount().doubleValue()  * userContract.getContractUnit().doubleValue() * userContract.getMargin().doubleValue() ;
                            zybzj = zybzj * investorPosition2.getPosition();
                            totalzybzj = totalzybzj + zybzj;
                            logger.debug(investorPosition2.getInstrumentid()+"占用保证金:totalzybzj:"+totalzybzj);
                            
                            pcyk = 0;
                            //平仓盈亏
                            if(investorPosition2.getPosidirection().equals("0")){
                                //平仓盈亏 买开 * 手数
                                pcyk = (investorPosition2.getCloseamount().doubleValue()-investorPosition2.getOpenamount().doubleValue())*userContract.getContractUnit().doubleValue();
                                pcyk = pcyk* investorPosition2.getClosevolume();
                            }else{
                                //平仓盈亏 卖开 * 手数
                                pcyk = (investorPosition2.getOpenamount().doubleValue()-investorPosition2.getCloseamount().doubleValue())*userContract.getContractUnit().doubleValue();
                                pcyk = pcyk* investorPosition2.getClosevolume();
                            }
                            totalpcyk = totalpcyk + pcyk;
                            logger.debug(investorPosition2.getInstrumentid()+"平仓盈亏:totalpcyk:"+totalpcyk);
                            
                            
                            
                        }
                    }
                    
                    userAvailableMemorySave.setMargin(String.valueOf(totalzybzj));
                    userAvailableMemorySave.setCloseWin(String.valueOf(totalpcyk));
                    
                } else {
                    userAvailableMemorySave.setCloseWin("0");
                    userAvailableMemorySave.setMargin("0");
                }
                userAvailableMemorySave.setPositionWin("0");
                
                logger.info("可用资金初始化计算 SubTradingaccount="+JSON.toJSONString(userAvailableMemorySave));
                mapAvailableMemorySave.put(subTradingaccount2.getAccountid(), userAvailableMemorySave);
                
            }
            
            
        } catch (FutureException e) {
            e.printStackTrace();
        }
        
        
        
    }
    
    public void availableMap(String userName,UserAvailableMemorySave userAvailableMemorySave){
        
        //发送给交易员
        StringBuffer sb = new StringBuffer();
        String available = availableCalc(userAvailableMemorySave);
        //可用=资金+平仓盈亏+持仓盈亏-手续费-冻结资金-占用保证金+出入金
//        double available = 0;
//        available = Double.valueOf(userAvailableMemorySave.getAvailable()) 
//                + Double.valueOf(userAvailableMemorySave.getCloseWin())
//                + Double.valueOf(userAvailableMemorySave.getPositionWin())
//                - Double.valueOf(userAvailableMemorySave.getCommission())
//                - Double.valueOf(userAvailableMemorySave.getFrozenAvailable())
//                - Double.valueOf(userAvailableMemorySave.getMargin())
//                + Double.valueOf(userAvailableMemorySave.getInOutMoney());
        sb.append("riskKYZJ|" + userName + "|riskKYZJ|"+available+"|"+JSON.toJSONString(userAvailableMemorySave));
        //logger.debug("可用资金推送："+sb.toString());
        SocketPrintOut(sb.toString());
        //socketStr = ;
        Display.getDefault().syncExec(new Runnable() {
                            
            @Override
            public void run() {
            getTradeResponse().append(sb.toString()+"\r\n");
            }
        });
    }
    
    public String availableCalc(UserAvailableMemorySave userAvailableMemorySave){
        
        //logger.debug("可用资金计算入参："+JSON.toJSONString(userAvailableMemorySave));
        double available = 0;
        available = Double.valueOf(userAvailableMemorySave.getAvailable()) 
                + Double.valueOf(userAvailableMemorySave.getCloseWin())
                + Double.valueOf(userAvailableMemorySave.getPositionWin())
                - Double.valueOf(userAvailableMemorySave.getCommission())
                - Double.valueOf(userAvailableMemorySave.getFrozenAvailable())
                - Double.valueOf(userAvailableMemorySave.getMargin())
                + Double.valueOf(userAvailableMemorySave.getInOutMoney());
        return String.valueOf(available);
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
        List<MainAccount> listmainAccount = findMainAcc();
        if(listmainAccount != null){
            mainAccount = listmainAccount.get(0);
        } else {
            logger.error("查询主账号失败！！！");
            return;
        }
        
        //连接CTP
        /*Thread connect = new Thread(new ConnectCTP());
        connect.setDaemon(true);
        connect.start();*/
        
        try {
            //买开  卖平路线   账户主
            
            MainAccount accountPrimary = this.mainAccountService.getAccountByType("1");
            ctpFirst = new Socket(ApplicationConfig.getProperty("addressFirst"),
                    Integer.parseInt(ApplicationConfig.getProperty("portFirst")));
            outFirst = new PrintWriter(new OutputStreamWriter(ctpFirst.getOutputStream(),"UTF-8"));
            Thread buyConnect = new Thread(new CtpConnectThread(CoreappView.this,ctpFirst));
            buyConnect.setDaemon(true);
            buyConnect.start();
            
            //登录
            
            CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
            userLoginField.setBrokerID(accountPrimary.getBrokerId());
            userLoginField.setUserID(accountPrimary.getAccountNo());
            userLoginField.setPassword(accountPrimary.getPasswd());
            outFirst.println("reqUserLogin|"+JSON.toJSONString(userLoginField)+"|1");
            outFirst.flush();
        } catch (FutureException e) {
            logger.error("查询主账户信息失败",e);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("查询主账户信息失败");
            box.setText("错误");
            box.open();
        } catch (Exception e) {
            logger.error("连接前置机1失败",e);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("连接前置机1失败");
            box.setText("错误");
            box.open();
        }
            
        try {
            //卖开 买平路线     账户备
            MainAccount accountSecondary = this.mainAccountService.getAccountByType("2");
            ctpSecond = new Socket(ApplicationConfig.getProperty("addressSecond"),
                    Integer.parseInt(ApplicationConfig.getProperty("portSecond")));
            outSecond = new PrintWriter(new OutputStreamWriter(ctpSecond.getOutputStream(),"UTF-8"));
            Thread sellConnect = new Thread(new CtpConnectThread(CoreappView.this,ctpSecond));
            sellConnect.setDaemon(true);
            sellConnect.start();
            
            //登录
            CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
            userLoginField.setBrokerID(accountSecondary.getBrokerId());
            userLoginField.setUserID(accountSecondary.getAccountNo());
            userLoginField.setPassword(accountSecondary.getPasswd());
            outSecond.println("reqUserLogin|"+JSON.toJSONString(userLoginField)+"|1");
            outSecond.flush();
        }  catch (FutureException e) {
            logger.error("查询备账户信息失败",e);
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("查询备账户信息失败");
            box.setText("错误");
            box.open();
        } catch (Exception e) {
            logger.error("连接前置机2失败");
            MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
            box.setMessage("连接前置机2失败");
            box.setText("错误");
            box.open();
        }
        
            
        //服务端线程
        Thread serverThread = new Thread(new ServerThread(this));
        serverThread.setDaemon(true);
        serverThread.start();
        
        //TEST
        
//        CTPApi.reqQryExchange(pQryExchange, nRequestID)
        
        
        
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        
        
    }
    
    public List<MainAccount> findMainAcc(){
        List<MainAccount> list = null;
        try {
            list = mainAccountService.getMainAccount();
        } catch (FutureException e) {

            e.printStackTrace();
        }
        return list;

    }
    
    
    
    private int CTPapiSet(){
        //下单操作
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
//        CTPApi.reqOrderInsert(inputOrderField, ++nRequestID);
        return 0;
    }
    
    public void loadSpringContext(){
        logger.info("===================开始加载Spring配置文件 ...==================");
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        logger.info("===================加载成功 !==================");
        
        
        mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
        
         userLoginService = (UserLoginService)SpringContextUtil.getBean("userLoginService");
         
         userInfoService = (UserInfoService)SpringContextUtil.getBean("userInfoService");
         
         subTradingaccountService = (SubTradingaccountService) SpringContextUtil.getBean("subTradingaccountService");
         inputOrderService = (InputOrderService) SpringContextUtil.getBean("inputOrderService");
         investorPositionService = (InvestorPositionService) SpringContextUtil.getBean("investorPositionService");
         tradeService = (TradeService) SpringContextUtil.getBean("tradeService");
         
         userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
         
         orderService= (OrderService) SpringContextUtil.getBean("orderService");
         
         userFrozenaccountService = (UserFrozenaccountService) SpringContextUtil.getBean("userFrozenaccountService");
         
         tradeRuleService = (TradeRuleService) SpringContextUtil.getBean("tradeRuleService");
        
          useravailableindbMapper = (UseravailableindbMapper) SpringContextUtil.getBean("useravailableindbMapper");
        
          buyDetailService = (BuyDetailService)SpringContextUtil.getBean("buyDetailService");
          
          sellDetailService = (SellDetailService)SpringContextUtil.getBean("sellDetailService");
          
          positionsDetailService = (PositionsDetailService)SpringContextUtil.getBean("positionsDetailService");
//        groupInfoService = (GroupInfoService) SpringContextUtil.getBean("groupInfoService");
//        mainAccountService = (MainAccountService) SpringContextUtil.getBean("mainAccountService");
//        userInfoService = (UserInfoService) SpringContextUtil.getBean("userInfoService");
//        tradeRuleService = (TradeRuleService) SpringContextUtil.getBean("tradeRuleService");
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if(outFirst != null){
                    CThostFtdcUserLogoutField pUserLogout = new CThostFtdcUserLogoutField();
                    pUserLogout.setBrokerID("090985");
                    StringBuffer sb = new StringBuffer();
                    sb.append("reqUserLogout|");
                    sb.append(JSON.toJSONString(pUserLogout)+"|");
                    sb.append(++nRequestID);
                    outFirst.println(sb.toString());
                    outFirst.flush();
                }
                if(outSecond != null){
                    CThostFtdcUserLogoutField pUserLogout = new CThostFtdcUserLogoutField();
                    pUserLogout.setBrokerID("090985");
                    StringBuffer sb = new StringBuffer();
                    sb.append("reqUserLogout|");
                    sb.append(JSON.toJSONString(pUserLogout)+"|");
                    sb.append(++nRequestID);
                    outSecond.println(sb.toString());
                    outSecond.flush();
                }
            }
        });
        shell.setSize(613, 399);
        shell.setText("SWT Application");
        shell.setLayout(null);
        
        tradeResponse = new Text(shell, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
        tradeResponse.setBounds(339, 194, 232, 139);
        
        ctpRequest = new Text(shell, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
        ctpRequest.setBounds(29, 27, 232, 139);
        
        ctpResponse = new Text(shell, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
        ctpResponse.setBounds(339, 27, 232, 139);
        
        tradeRequest = new Text(shell, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
        tradeRequest.setBounds(29, 194, 232, 139);
        
        Label lblCtp = new Label(shell, SWT.NONE);
        lblCtp.setBounds(31, 10, 61, 17);
        lblCtp.setText("CTP请求");
        
        Label lblCtp_1 = new Label(shell, SWT.NONE);
        lblCtp_1.setText("CTP应答");
        lblCtp_1.setBounds(339, 10, 61, 17);
        
        Label label = new Label(shell, SWT.NONE);
        label.setText("交易员请求");
        label.setBounds(29, 172, 61, 17);
        
        Label label_1 = new Label(shell, SWT.NONE);
        label_1.setText("交易员应答");
        label_1.setBounds(339, 172, 61, 17);

    }
    
    public synchronized void getCTPSocket(){
        //如果socket为空 或者断开连接则创建一个socket
        if(CTPsocket == null || !(CTPsocket.isConnected() == true && CTPsocket.isClosed() == false)){
            try {
                logger.info("=====================开始与行情服务器建立连接==============");
                CTPsocket = new Socket(InetAddress.getLocalHost(),3394);
            } catch (Exception e) {
                logger.error("与行情服务器通信失败",e);
            }
        }
    }
    
    public class ConnectCTP implements Runnable{

        public void run() {
            String dataPath = "ctpdata/test/";
            
//            CTPApi = JCTPTraderApi.createFtdcTraderApi();
            CTPApi = JCTPTraderApi.createFtdcTraderApi(dataPath);

            CTPSpi = new MyTraderSpi(CTPApi,CoreappView.this);
            
            //注册traderpi
            CTPApi.registerSpi(CTPSpi);
            //注册公有流
            CTPApi.subscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            //注册私有流
            CTPApi.subscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESTART);
            //注册前置机地址
            CTPApi.registerFront(frontAddr);
            
            CTPApi.init();
            CTPApi.join();
            
            //回收api和JCTP
            CTPApi.release();
        }
        
    }
    
    public void onRtnTrade(CThostFtdcTradeField pTrade) {
        //如果该报单由交易所进行了撮合成交，交易所再次返回该报单的状态（已成交）。并通过此函数返回该笔成 交。 报单成交之后，一个报单回报（OnRtnOrder）和一个成交回报（OnRtnTrade）会被发送到客户端，报单回报 中报单的状态为“已成交”。但是仍然建议客户端将成交回报作为报单成交的标志，因为 CTP 的交易核心在 收到 OnRtnTrade 之后才会更新该报单的状态。如果客户端通过报单回报来判断报单成交与否并立即平仓，有 极小的概率会出现在平仓指令到达 CTP 交易核心时该报单的状态仍未更新，就会导致无法平仓。
        logger.info("成交");
        logger.info(JSON.toJSONString(pTrade));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpResponse().append(JSON.toJSONString(pTrade)+"\r\n");
            }
        });
        
        //确定子账号
        String subAccount = "";
         // select SUBUSERID from T_INPUT_ORDER where FRONTID = FRONTID and SESSIONID = SESSIONID and ORDERREF = pTrade.getOrderRef()
        
        try {
            subAccount = inputOrderService.getSubUserID(frontID,sessionID, pTrade.getOrderRef());
        } catch (FutureException e) {

            e.printStackTrace();
        }
        logger.info("确定子账号：" + subAccount);
        
        if (StringUtils.isEmpty(subAccount)){
            return;
        }

        logger.info("成交step1");
        // 插入T_TRADE数据
        Trade trade = new Trade();
        trade.setBrokerid(pTrade.getBrokerID());
        trade.setInvestorid(pTrade.getInvestorID());
        trade.setInstrumentid(pTrade.getInstrumentID());
        trade.setOrderref(pTrade.getOrderRef());
        trade.setUserid(pTrade.getUserID());
        trade.setExchangeid(pTrade.getExchangeID());
        trade.setTradeid(pTrade.getTradeID());
        trade.setDirection(String.valueOf(pTrade.getDirection()));
        trade.setOrdersysid(pTrade.getOrderSysID());
        trade.setParticipantid(pTrade.getParticipantID());
        trade.setClientid(pTrade.getClientID());
        trade.setTradingrole(String.valueOf(pTrade.getTradingRole()));
        trade.setExchangeinstid(pTrade.getExchangeInstID());
        trade.setOffsetflag(String.valueOf(pTrade.getOffsetFlag()));
        trade.setHedgeflag(String.valueOf(pTrade.getHedgeFlag()));
        trade.setPrice(new BigDecimal(String.valueOf(pTrade.getPrice())));
        trade.setVolume(Long.valueOf(String.valueOf(pTrade.getVolume())));
        trade.setTradedate(pTrade.getTradeDate());
        trade.setTradetime(pTrade.getTradeTime());
        trade.setTradetype(String.valueOf(pTrade.getTradeType()));
        trade.setPricesource(String.valueOf(pTrade.getPriceSource()));
        trade.setTraderid(pTrade.getTraderID());
        trade.setOrderlocalid(pTrade.getOrderLocalID());
        trade.setClearingpartid(pTrade.getClearingPartID());
        trade.setBusinessunit(pTrade.getBusinessUnit());
        trade.setSequenceno(Long.valueOf(pTrade.getSequenceNo()));
        trade.setTradingday(pTrade.getTradingDay());
        trade.setSettlementid(Long.valueOf(String.valueOf(pTrade.getSettlementID())));
        trade.setBrokerorderseq(Long.valueOf(String.valueOf(pTrade.getBrokerOrderSeq())));
        trade.setTradesource(String.valueOf(pTrade.getTradeSource()));
        trade.setSubuserid(subAccount);
        trade.setFrontid(new BigDecimal(frontID));
        trade.setSessionid(new BigDecimal(sessionID));
        if(pTrade.getOffsetFlag() == '0'){
        	BuyDetail buyDetail = new BuyDetail();
            String tmpStrCombokey = "";
            
            try {
    			BeanUtils.copyProperties(buyDetail, trade);
    			tmpStrCombokey = trade.getTradedate()+trade.getExchangeid()+trade.getOrdersysid();
    			buyDetail.setCombokey(tmpStrCombokey);
    			buyDetail.setSellvolume(Short.valueOf("0"));
    			
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			logger.error("BeanUtils.copyProperties(buyDetail, trade); error",e1);
    			
    		}
        }
        
        if(pTrade.getOffsetFlag() == '1'){
        	SellDetail sellDetail = new SellDetail();
            String tmpStrCombokey = "";
            
            try {
    			BeanUtils.copyProperties(sellDetail, trade);
    			tmpStrCombokey = trade.getTradedate()+trade.getExchangeid()+trade.getOrdersysid();
    			sellDetail.setCombokey(tmpStrCombokey);
    			
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			logger.error("BeanUtils.copyProperties(sellDetail, trade); error",e1);
    			
    		}
        }
        
        
        try {
            tradeService.saveTrade(trade);
        } catch (FutureException e) {
        	logger.error("tradeService.saveTrade(trade); error",e);
            e.printStackTrace();
        }
        // update T_ORDER  set ORDERSTATUS = '1' ,STATUSMSG = "已成交" where FRONTID = FRONTID and SESSIONID = SESSIONID and ORDERREF = pTrade.getOrderRef() and ORDERSTATUS is null
        
        //撤单解冻资金 T_USER_FROZENACCOUNT 中查询FROZENMARGIN，FROZENCOMMISSION
        // T_USER_FROZENACCOUNT subAccount pOrder.getFrontID() pOrder.getSessionID() pOrder.getOrderRef()
        logger.info("成交step2");
        UserFrozenaccount userFrozenaccount = new UserFrozenaccount();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("subAccount", subAccount);
        map.put("frontID", frontID);
        map.put("sessionID", sessionID);
        map.put("orderRef", pTrade.getOrderRef());
        map.put("volume", pTrade.getVolume());
        
        logger.info("！！！！！成交解冻userFrozenaccountService："+JSON.toJSONString(map));
        
        try {
            userFrozenaccount = userFrozenaccountService.getUserByTradeUnfrozen(map);
        } catch (FutureException e) {
            // 
            e.printStackTrace();
            
        }
        logger.info("成交step3");
        if(userFrozenaccount != null){
            logger.info("成交step3-1");
        
            map = new HashMap<String,Object>();
            map.put("subAccount", subAccount);
            map.put("volume", pTrade.getVolume());
            map.put("frozenmargin", userFrozenaccount.getFrozenmargin());
            map.put("frozencommission", userFrozenaccount.getFrozencommission());
            //T_SUB_TRADINGACCOUNT 冻结资金减去，可用资金加上
            logger.info("！！！！！成交解冻subTradingaccountService："+JSON.toJSONString(map));
            try {
                subTradingaccountService.updateUnfrozen(map);
            } catch (FutureException e) {
            
                e.printStackTrace();
            }
            
            //解冻资金做可用计算
            UserAvailableMemorySave userAvailableMemorySave  = mapAvailableMemorySave.get(subAccount);
            logger.info("解冻资金做可用计算Trade"+JSON.toJSONString(userAvailableMemorySave));
            double tempdb = userFrozenaccount.getFrozenmargin().doubleValue() + userFrozenaccount.getFrozencommission().doubleValue();
            tempdb =  Double.valueOf(userAvailableMemorySave.getFrozenAvailable()) - tempdb;                
            userAvailableMemorySave.setFrozenAvailable(String.valueOf(tempdb));
            availableMap(subAccount,userAvailableMemorySave);
            
            
        
        } 
        
        logger.info("成交step4");
        
        //计算实际的手续费，持仓价格 更新子资金账户（曹晓欣）COMMISSION字段
        //数量 VOLUME 价格PRICE 买卖方向DIRECTION 开平标志OFFSETFLAG 子账户SUBUSERID
        String heyue = pTrade.getInstrumentID();
        UserContract userContract = new UserContract();
        String userAccount = mapUserAccountMemorySave.get(subAccount);
        userContract = mapUserContractMemorySave.get(subAccount +heyue);
        
        logger.info("成交step5");
        
        //手数
        int shoushu = pTrade.getVolume();
         //合约保证金 = 价格 * 合约单位 * 保证金比例 * 手数
        double heyuebaozhengjin = pTrade.getPrice()  * userContract.getContractUnit().doubleValue() * userContract.getMargin().doubleValue() * shoushu;
        
        
        
        
        
        //手续费
        double commission = 0;
        
        if(pTrade.getOffsetFlag() == '0'){
            //开仓手续费 = 手数 * 开仓手续费 + 价格 * 合约单位* 手数  * 开仓手续费比例  
            commission = shoushu * userContract.getOpenCharge().doubleValue() +    pTrade.getPrice()  * userContract.getContractUnit().doubleValue() * userContract.getOpenChargeRate().doubleValue()*shoushu ;
            logger.info("开仓手续费手数 ="+shoushu);
            logger.info("开仓开仓手续费  ="+ userContract.getOpenCharge().doubleValue() );
            logger.info("开仓手续费价格 ="+pTrade.getPrice());
            logger.info("开仓合约单位 ="+userContract.getContractUnit().doubleValue() );
            logger.info("开仓手开仓手续费比例 ="+userContract.getOpenChargeRate().doubleValue());
            
            
            
            
        } else {
            //平仓手续费 = 手数 * 平仓手续费 + 价格 * 合约单位* 手数  * 平仓手续费比例  
            commission = shoushu * userContract.getCloseCurrCharge().doubleValue() +    pTrade.getPrice()  * userContract.getContractUnit().doubleValue() * userContract.getCloseCurrChargeRate().doubleValue()*shoushu ;
        }
        map = new HashMap<String,Object>();
        map.put("subAccount", subAccount);
        map.put("commission", commission);
        try {
            logger.info("子账号：" + subAccount +"实际手续费" + commission);
            subTradingaccountService.setCommission(map);
        } catch (FutureException e) {
            
            e.printStackTrace();
        }
        logger.info("成交step6");
        InvestorPosition investorPosition = new InvestorPosition();
        //计算持仓明细
        logger.info("计算持仓明细----0为开仓，else为平仓："+pTrade.getOffsetFlag());
        if(String.valueOf(pTrade.getOffsetFlag()).equals("0")){
            logger.info("成交step6-1");
            //开仓
            //查询投资者持仓（曹晓欣） 合约代码INSTRUMENTID 持仓多空方向POSIDIRECTION 子账户SUBUSERID 有无同向持仓
        
            investorPosition.setSubuserid(subAccount);
            investorPosition.setInstrumentid(pTrade.getInstrumentID());
            //持仓多空方向
            investorPosition.setPosidirection(String.valueOf(pTrade.getDirection()));
            //investorPosition.setPositiondate(pTrade.getTradingDay());
            //投机套保标志
            investorPosition.setHedgeflag(String.valueOf(pTrade.getHedgeFlag()));
            //今日持仓
            investorPosition.setPosition(Long.valueOf(String.valueOf(pTrade.getVolume())));
            //开仓金额
            investorPosition.setOpenamount(new BigDecimal(pTrade.getPrice()));
            //开仓量
            investorPosition.setOpenvolume(Long.valueOf(String.valueOf(pTrade.getVolume())));
            //开仓手续费
            investorPosition.setCommission(new BigDecimal(commission));
            
            //同持仓多空方向，同用户，同合约 没有持仓则INSERT 否则 读取原开仓数据 再 算术平均后更新数据
            
            try {
                investorPositionService.setOpenPosition(investorPosition);
            } catch (FutureException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
        } else {
            //平仓
            logger.info("成交step6-2");
            //补丁 平仓的时候 资金冻结表是不用查的返回一定是NULL
            //发送给交易员
            StringBuffer sb = new StringBuffer();
            sb.append("onRtnTrade|" + subAccount + "|onRtnTrade|" + JSON.toJSONString(trade));
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
            //平仓表插入 FOR 结算单 start
//            | 平仓日期 | 交易所 |       品种       |      合约      |开仓日期 |买/卖|   手数   |     开仓价    |     昨结算     |   成交价   |  平仓盈亏  |     权利金收支      |
//            |Close Date|Exchange|      Product     |   Instrument   |Open Date| B/S |   Lots   |Pos. Open Price|   Prev. Sttl   |Trans. Price|Realized P/L|Premium Received/Paid|
//            ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//            |20170512  |上期所  |铝                |al1707          |20170512 |   卖|         1|      13880.000|       13805.000|   13875.000|      -25.00|                 0.000|

            //平仓日期
            pTrade.getTradeDate();
            //交易所
            pTrade.getExchangeID();
            //合约
            pTrade.getInstrumentID();
            
            
            
            
            
            
            //平仓表插入 FOR 结算单 end
 
            
            //补丁结束
            
            investorPosition.setSubuserid(subAccount);
            investorPosition.setInstrumentid(pTrade.getInstrumentID());
            investorPosition.setPosidirection(String.valueOf(pTrade.getDirection()));
            //investorPosition.setPositiondate(pTrade.getTradingDay());
            investorPosition.setHedgeflag(String.valueOf(pTrade.getHedgeFlag()));
            investorPosition.setPosition(Long.valueOf(String.valueOf(pTrade.getVolume())));
            investorPosition.setCloseamount(new BigDecimal(pTrade.getPrice()));
            investorPosition.setClosevolume(Long.valueOf(String.valueOf(pTrade.getVolume())));
            //平仓手续费
            investorPosition.setCommission(new BigDecimal(commission));
            
                        
            //反向 持仓多空方向，同用户，同合约  没有持仓 return 否则 读取原持平仓数据 再 算术平均后更新数据
            try {
                investorPositionService.setClosePosition(investorPosition);
            } catch (FutureException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        
        logger.info("成交step3补丁");
        if(userFrozenaccount != null){
            //发送给交易员
            StringBuffer sb = new StringBuffer();
            sb.append("onRtnTrade|" + subAccount + "|onRtnTrade|" + JSON.toJSONString(trade));
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
        }
        
        
        
        
        
        
        logger.info("成交step7");
        List<InvestorPosition> listInvestorPosition = new ArrayList<InvestorPosition>();
        try {
            listInvestorPosition = investorPositionService.getSubUserPostion(subAccount);
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int i = 1;
        StringBuffer sb = new StringBuffer();
        if(listInvestorPosition.size() == 0){
            //空持仓返回
            
            sb.append("onPosition|" + subAccount + "|0|0|" );
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
        }
        for (InvestorPosition investorPosition2 : listInvestorPosition) {
            //发送给交易员持仓数据
            sb.delete(0, sb.length());
            sb.append("onPosition|" + subAccount + "|" +i + "|"+listInvestorPosition.size() + "|" + JSON.toJSONString(investorPosition2));
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
            i = i +1;
        }
        
        //手续费做可用计算
        UserAvailableMemorySave userAvailableMemorySave  = mapAvailableMemorySave.get(subAccount);
        logger.info("手续费做可用计算"+JSON.toJSONString(userAvailableMemorySave));
        double tempdb = commission;
        tempdb =  Double.valueOf(userAvailableMemorySave.getCommission()) + tempdb;                
        userAvailableMemorySave.setCommission(String.valueOf(tempdb));
        
        //平仓盈亏做可用计算 占用保证金
        try {
             listInvestorPosition = investorPositionService.getUserByUserName(subAccount);
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        double pcyk = 0;
        double totalpcyk = 0;
        double zybzj = 0;
        double totalzybzj = 0;
        
        
        for (InvestorPosition investorPosition2 : listInvestorPosition) {
            
            UserContract positionContract = mapUserContractMemorySave.get(investorPosition2.getSubuserid() +investorPosition2.getInstrumentid());
            if(investorPosition2.getPosition() == 0){
                pcyk = 0;
                //平仓盈亏
                if(investorPosition2.getPosidirection().equals("0")){
                    //平仓盈亏 买开 * 手数
                    pcyk = (investorPosition2.getCloseamount().doubleValue()-investorPosition2.getOpenamount().doubleValue())*positionContract.getContractUnit().doubleValue();
                    pcyk = pcyk* investorPosition2.getClosevolume();
                }else{
                    //平仓盈亏 卖开 * 手数
                    pcyk = (investorPosition2.getOpenamount().doubleValue()-investorPosition2.getCloseamount().doubleValue())*positionContract.getContractUnit().doubleValue();
                    pcyk = pcyk* investorPosition2.getClosevolume();
                }
                
                totalpcyk = totalpcyk + pcyk;
                logger.info(investorPosition2.getInstrumentid()+"平仓盈亏:totalpcyk:"+totalpcyk);
                    
            } else {
                //占用保证金
                //合约保证金 = 价格 * 合约单位 * 保证金比例 * 手数
                zybzj = investorPosition2.getOpenamount().doubleValue()  * positionContract.getContractUnit().doubleValue() * positionContract.getMargin().doubleValue() ;
                zybzj = zybzj * investorPosition2.getPosition();
                totalzybzj = totalzybzj + zybzj;
                logger.info(investorPosition2.getInstrumentid()+"占用保证金:totalzybzj:"+totalzybzj);
                
                pcyk = 0;
                //平仓盈亏
                if(investorPosition2.getPosidirection().equals("0")){
                    //平仓盈亏 买开 * 手数
                    pcyk = (investorPosition2.getCloseamount().doubleValue()-investorPosition2.getOpenamount().doubleValue())*positionContract.getContractUnit().doubleValue();
                    pcyk = pcyk* investorPosition2.getClosevolume();
                }else{
                    //平仓盈亏 卖开 * 手数
                    pcyk = (investorPosition2.getOpenamount().doubleValue()-investorPosition2.getCloseamount().doubleValue())*positionContract.getContractUnit().doubleValue();
                    pcyk = pcyk* investorPosition2.getClosevolume();
                }
                totalpcyk = totalpcyk + pcyk;
                logger.info(investorPosition2.getInstrumentid()+"平仓盈亏:totalpcyk:"+totalpcyk);
                
                
                
            }
        }
        
        userAvailableMemorySave.setMargin(String.valueOf(totalzybzj));
        userAvailableMemorySave.setCloseWin(String.valueOf(totalpcyk));
        availableMap(subAccount,userAvailableMemorySave);
        
        
        
        
        logger.info("成交step8");            
    }
        

    

    public void onRspQryInvestorPositionDetail(
            CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        logger.info("持仓明细查询回调");
        logger.info(JSON.toJSONString(pInvestorPositionDetail));
        
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        //查询持仓
//        CThostFtdcQryInvestorPositionField pQryInvestorPosition = new CThostFtdcQryInvestorPositionField();
//        pQryInvestorPosition.setBrokerID(mainAccount.getBrokerId());
//        pQryInvestorPosition.setInstrumentID("a1705");
//        pQryInvestorPosition.setInvestorID(mainAccount.getAccountNo());
//        logger.info(pQryInvestorPosition);
//        logger.info(CTPApi.reqQryInvestorPosition(pQryInvestorPosition, nRequestID));
    }
    
    

    public void onRspQryInvestorPosition(
            CThostFtdcInvestorPositionField pInvestorPosition,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        logger.info("持仓查询回调");
        logger.info(JSON.toJSONString(pInvestorPosition));
        
    }


    public void onRspSettlementInfoConfirm(
            CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        logger.info("结算单确认回调");
    }
    

    public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
            boolean bIsLast) {
        logger.info("错误回调");
        logger.info(JSON.toJSONString(pRspInfo));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpResponse().append(JSON.toJSONString(pRspInfo)+"\r\n");
            }
        });
    }

    public void onErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder,
            CThostFtdcRspInfoField pRspInfo) {
        //报盘将通过交易核心检查的报单发送到交易所前置，交易所会再次校验该报单。如果交易所认为该报单不合 法，交易所会将该报单撤销，将错误信息返回给报盘，并返回更新后的该报单的状态。当客户端接收到该错 误信息后，就会调用 OnErrRtnOrderInsert 函数， 而更新后的报单状态会通过调用函数 OnRtnOrder 发送到客 户端。 如果交易所认为该报单合法，则只返回该报单状态（此时的状态应为：“尚未触发”）。

        logger.info("报单录入错误回调");
        logger.info(JSON.toJSONString(pInputOrder));
        logger.info(JSON.toJSONString(pRspInfo));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpResponse().append(JSON.toJSONString(pInputOrder)+"\r\n");
            }
        });
    }
    

    public void onRtnOrder(CThostFtdcOrderField pOrder) {
        //交易系统返回的报单状态，每次报单状态发生变化时被调用。一次报单过程中会被调用数次：交易系统将报 单向交易所提交时（上述流程 8 中的第 2 个过程），交易所撤销或接受该报单时，该报单成交时。
        //8.2， 交易核心向交易前置发送了第一个报单回报后，立即产生向交易所申请该报单插入的申请报文，该报文 被报盘管理订阅。 
        logger.info("报单");
        logger.info(JSON.toJSONString(pOrder));
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpResponse().append(JSON.toJSONString(pOrder)+"\r\n");
            }
        });
        
        //确定子账号
        String subAccount = "";
        // select SUBUSERID from T_INPUT_ORDER where FRONTID = FRONTID and SESSIONID = SESSIONID and ORDERREF = pTrade.getOrderRef()
                
        try {
            subAccount = inputOrderService.getSubUserID(frontID,sessionID, pOrder.getOrderRef());
        } catch (FutureException e) {
            // 
            e.printStackTrace();
        }
        
        logger.info("onRtnOrder确定子账号：" + subAccount);
                
        if (StringUtils.isEmpty(subAccount)){
            return;
        }
        
        //插入T_ORDER数据
        Order order = new Order();
        order.setBrokerid(pOrder.getBrokerID());
        order.setInvestorid(pOrder.getInvestorID());
        order.setInstrumentid(pOrder.getInstrumentID());
        order.setOrderref(pOrder.getOrderRef());
        order.setUserid(pOrder.getUserID());
        order.setOrderpricetype(String.valueOf(pOrder.getOrderPriceType()));
        order.setDirection(String.valueOf(pOrder.getDirection()));
        order.setComboffsetflag(pOrder.getCombOffsetFlag());
        order.setCombhedgeflag(pOrder.getCombHedgeFlag());
        order.setLimitprice(new BigDecimal(String.valueOf(pOrder.getLimitPrice())));
        order.setVolumetotaloriginal(Long.valueOf(String.valueOf(pOrder.getVolumeTotalOriginal())));
        order.setTimecondition(String.valueOf(pOrder.getTimeCondition()));
        order.setGtddate(pOrder.getGTDDate());
        order.setVolumecondition(String.valueOf(pOrder.getVolumeCondition()));
        order.setMinvolume(Long.valueOf(String.valueOf(pOrder.getMinVolume())));
        order.setContingentcondition(String.valueOf(pOrder.getContingentCondition()));
        order.setStopprice(new BigDecimal(pOrder.getStopPrice()));
        order.setForceclosereason(String.valueOf(pOrder.getForceCloseReason()));
        order.setIsautosuspend(Long.valueOf(String.valueOf(pOrder.getIsAutoSuspend())));
        order.setBusinessunit(pOrder.getBusinessUnit());
        order.setRequestid(Long.valueOf(String.valueOf(pOrder.getRequestID())));
        order.setOrderlocalid(pOrder.getOrderLocalID());
        order.setExchangeid(pOrder.getExchangeID());
        order.setParticipantid(pOrder.getParticipantID());
        order.setClientid(pOrder.getClientID());
        order.setExchangeinstid(pOrder.getExchangeInstID());
        order.setTraderid(pOrder.getTraderID());
        order.setInstallid(Long.valueOf(String.valueOf(pOrder.getInstallID())));
        order.setOrdersubmitstatus(String.valueOf(pOrder.getOrderSubmitStatus()));
        order.setNotifysequence(Long.valueOf(String.valueOf(pOrder.getNotifySequence())));
        order.setTradingday(pOrder.getTradingDay());
        order.setSettlementid(Long.valueOf(String.valueOf(pOrder.getSettlementID())));
        order.setOrdersysid(pOrder.getOrderSysID());
        order.setOrdersource(String.valueOf(pOrder.getOrderSource()));
        order.setOrderstatus(String.valueOf(pOrder.getOrderStatus()));
        order.setOrdertype(String.valueOf(pOrder.getOrderType()));
        order.setVolumetraded(Long.valueOf(String.valueOf(pOrder.getVolumeTraded())));
        order.setVolumetotal(Long.valueOf(String.valueOf(pOrder.getVolumeTotal())));
        order.setInsertdate(pOrder.getInsertDate());
        order.setInserttime(pOrder.getInsertTime());
        order.setActivetime(pOrder.getActiveTime());
        order.setSuspendtime(pOrder.getSuspendTime());
        order.setUpdatetime(pOrder.getUpdateTime());
        order.setCanceltime(pOrder.getCancelTime());
        order.setActivetraderid(pOrder.getActiveTraderID());
        order.setClearingpartid(pOrder.getClearingPartID());
        order.setSequenceno(Long.valueOf(pOrder.getSequenceNo()));
        order.setFrontid(new BigDecimal(pOrder.getFrontID()));
        order.setSessionid(new BigDecimal(pOrder.getSessionID()));
        order.setUserproductinfo(pOrder.getUserProductInfo());
        order.setStatusmsg(pOrder.getStatusMsg());
        order.setUserforceclose(Long.valueOf(String.valueOf(pOrder.getUserForceClose())));
        order.setActiveuserid(pOrder.getActiveUserID());
        order.setBrokerorderseq(Long.valueOf(String.valueOf(pOrder.getBrokerOrderSeq())));
        order.setRelativeordersysid(pOrder.getRelativeOrderSysID());
        order.setZcetotaltradedvolume(Long.valueOf(String.valueOf(pOrder.getZCETotalTradedVolume())));
        order.setIsswaporder(Long.valueOf(String.valueOf(pOrder.getIsSwapOrder())));
        order.setSubuserid(subAccount);
        
        try {
            orderService.saveOrder(order);
        } catch (FutureException e) {
            // 
            e.printStackTrace();
        }
        
        
        
        //撤单解冻资金 T_USER_FROZENACCOUNT 中查询FROZENMARGIN，FROZENCOMMISSION
        if(String.valueOf(pOrder.getOrderStatus()).equals("5") && pOrder.getCombOffsetFlag().equals("0")){
            // T_USER_FROZENACCOUNT subAccount pOrder.getFrontID() pOrder.getSessionID() pOrder.getOrderRef()
            UserFrozenaccount userFrozenaccount = new UserFrozenaccount();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("subAccount", subAccount);
            map.put("frontID", pOrder.getFrontID());
            map.put("sessionID", pOrder.getSessionID());
            map.put("orderRef", pOrder.getOrderRef());
            // caoxx 20170306 modify
            map.put("volume", pOrder.getVolumeTotal());
            logger.info("！！！！！userFrozenaccountService撤单解冻onRtnOrder："+JSON.toJSONString(map));
            try {
                userFrozenaccount = userFrozenaccountService.getUserByUnfrozen(map);
                if(userFrozenaccount == null){
                    return;
                }
            } catch (FutureException e) {
                // 
                e.printStackTrace();
            }
            map = new HashMap<String,Object>();
            map.put("subAccount", subAccount);
            map.put("volume", pOrder.getVolumeTotalOriginal());
            map.put("frozenmargin", userFrozenaccount.getFrozenmargin());
            map.put("frozencommission", userFrozenaccount.getFrozencommission());
            //T_SUB_TRADINGACCOUNT 冻结资金减去，可用资金加上
            logger.info("！！！！！updateUnfrozen撤单解冻onRtnOrder："+JSON.toJSONString(map));
            try {
                subTradingaccountService.updateUnfrozen(map);
            } catch (FutureException e) {
                // 
                e.printStackTrace();
            }
            
            //解冻资金做可用计算
            UserAvailableMemorySave userAvailableMemorySave  = mapAvailableMemorySave.get(subAccount);
            logger.info("解冻资金做可用计算CANCEL:"+JSON.toJSONString(userAvailableMemorySave));
            double tempdb = userFrozenaccount.getFrozenmargin().doubleValue() + userFrozenaccount.getFrozencommission().doubleValue();
            tempdb =  Double.valueOf(userAvailableMemorySave.getFrozenAvailable()) - tempdb;                
            userAvailableMemorySave.setFrozenAvailable(String.valueOf(tempdb));
            availableMap(subAccount,userAvailableMemorySave);
            
        }
        
        //发送给交易员
        StringBuffer sb = new StringBuffer();
        sb.append("onRtnOrder|" + subAccount + "|onRtnOrder|" + JSON.toJSONString(order));
        SocketPrintOut(sb.toString());
        //socketStr = ;
        Display.getDefault().syncExec(new Runnable() {
                    
            @Override
            public void run() {
                getTradeResponse().append(sb.toString()+"\r\n");
            }
        });
        
        //报单回报使用的函数是 OnRtnOrder。核心数据结构为 CThostFtdcOrderField。 报单回报主要作用是通知客户端该报单的最新状态，如已提交，已撤销，未触发，已成交等。 每次报单状态有变化，该函数都会被调用一次。
        //VolumeTotalOriginal&VolumeTraded&VolumeTotal 上述三个字段分别对应该报单的原始报单数量，已成交数量和剩余数量。
        //如果报单是分笔成交，则每次成交都会有一次 OnRtnOrder 返回。


    }
    

    public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        //综合交易平台交易核心返回的包含错误信息的报单响应，对应于上一节中的第 7 步的第 1 种情况。
        //第 7 步的第 1 种情况,交易前置从交易核心订阅到错误的报单响应报文，以对话模式将该报文转发给交易终端。
        logger.info(pRspInfo.getErrorMsg());
        logger.info(JSON.toJSONString(pInputOrder));
    }
    

    public void onRspOrderAction(
            CThostFtdcInputOrderActionField pInputOrderAction,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
        logger.info("撤单返回");
        logger.info(JSON.toJSONString(pInputOrderAction));
        logger.info(JSON.toJSONString(pRspInfo));
        
        //确定子账号
        String subAccount = "";
        // select SUBUSERID from T_INPUT_ORDER where FRONTID = FRONTID and SESSIONID = SESSIONID and ORDERREF = pTrade.getOrderRef()
                        
        try {
            subAccount = inputOrderService.getSubUserID(pInputOrderAction.getFrontID(),pInputOrderAction.getSessionID(), pInputOrderAction.getOrderRef());
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //发送给交易员
        StringBuffer sb = new StringBuffer();
        sb.append("onRspOrderAction|" + subAccount + "|onRspOrderAction|" + JSON.toJSONString(pInputOrderAction)+ "|" + JSON.toJSONString(pRspInfo));
        SocketPrintOut(sb.toString());
        //socketStr = ;
        Display.getDefault().syncExec(new Runnable() {
                            
            @Override
            public void run() {
            getTradeResponse().append(sb.toString()+"\r\n");
            }
        });
        
        
        
    }
    //报单序列号说明 ，在综合交易平台和交易所中，每笔报单都有 3 组唯一序列号，保证其与其他报单是不重复的。
    //FrontID+SessionID+OrderRef 登陆之后，交易核心会返回对应此次连接的前置机编号 FrontID 和会话编号 SessionID。这两个编号在此次连 接中是不变的。 OrderRef 是报单操作的核心数据结构 CThostFtdcInputOrderField 中的一个字段。开发者可以让 OrderRef 在一 次登录期间从 MaxOrderRef 起逐一递增，以保证报单的唯一性。开发者也可以选择不对它赋值，则交易核心 会自动赋予一个唯一性的值。
    //ExchangeID+TraderID+OrderLocalID 交易核心将报单提交到报盘管理之后由交易核心生成 OrderLocalID 并返回给客户端的。ExchangeID 合约所在 交易所的代码，TraderID 由交易核心选定返回。客户端也可以通过这组序列号进行撤单操作。 与第一组序列号不同的是：该序列号是由综合交易平台的交易核心维护。
    //ExchangeID+OrderSysID 交易所在接收了报单之后，会为该报单生成报单在交易所的编号 OrderSysID。再经由综合交易平台转发给客 户端。ExchangeID 是固定的。 客户端也可以通过这组序列号进行撤单操作。

    //撤单
    //撤单使用的函数是 ReqOrderAction。核心的数据结构是 CThostFtdcInputOrderActionField。 
    //撤单操作与报单操作很类似，但需要注意两个地方。 
    //1. 字段 ActionFlag 由于国内的交易所目前只支持撤单，不支持改单操作，因此函数 ReqOrderAction 只支持撤单操作，字段 ActionFlag 的赋值目前只能是 THOST_FTDC_AF_Delete。
    // 序列号  撤单操作需要对应可以定位该报单的序列号。上一节最后介绍的三组报单序列号都可以用来撤单。
    //撤单响应和回报
    //OnRspOrderAction：撤单响应。交易核心返回的含有错误信息的撤单响应。
    //OnRtnOrder：交易核心确认了撤单指令的合法性后，将该撤单指令提交给交易所，同时返回对应报单的新状 态。 
    //OnErrRtnOrderAction：交易所会再次验证撤单指令的合法性，如果交易所认为该指令不合法，交易核心通过 此函数转发交易所给出的错误。如果交易所认为该指令合法，同样会返回对应报单的新状态（OnRtnOrder）。
    
    //请求报单， ReqOrderInsert 
    
    //CTP用户登入应答
    public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        
//        JSON.toJSONString(pRspUserLogin);
        logger.info("ERRORID = "+pRspInfo.getErrorID());
        logger.info(pRspUserLogin.getLoginTime());
        logger.info(pRspUserLogin.getCZCETime());
        logger.info(pRspUserLogin.getDCETime());
        logger.info(pRspUserLogin.getFFEXTime());
        logger.info(pRspUserLogin.getSHFETime());
        logger.info("getMaxOrderRef=" +pRspUserLogin.getMaxOrderRef());
        logger.info("nRequestID=" + nRequestID);
        logger.info("getSessionID=" + pRspUserLogin.getSessionID());
        logger.info("getFrontID=" + pRspUserLogin.getFrontID());
        sessionID = pRspUserLogin.getSessionID();
        frontID = pRspUserLogin.getFrontID();
//        orderRef = pRspUserLogin.getMaxOrderRef();
        atomicInteger.getAndSet(Integer.parseInt(pRspUserLogin.getMaxOrderRef()));
        this.nRequestID = nRequestID;
        CTPerrID = pRspInfo.getErrorID();
        if(CTPerrID != 0){
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES);
                    box.setMessage("主账户登入失败！");
                    box.setText("错误");
                    box.open();
                    shell.dispose();
                }
            });
        }
        
        
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }
    
    public void onRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        // INSERTＤＢ
        logger.info("资金回调");
        logger.info(JSON.toJSONString(pTradingAccount));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        //查询持仓明细
                CThostFtdcQryInvestorPositionDetailField positionField = new CThostFtdcQryInvestorPositionDetailField();
                positionField.setBrokerID(mainAccount.getBrokerId());
                positionField.setInstrumentID("a1705");
                positionField.setInvestorID(mainAccount.getAccountNo());
                logger.info(positionField);
                //logger.info(CTPApi.reqQryInvestorPositionDetail(positionField, ++nRequestID));
                
                
    }
    
    public void subOrderAction(String subAccount,String strJson){
        logger.info("subOrderAction:" + subAccount + JSON.toJSONString(strJson));
        nRequestID = nRequestID + 1;
        JSONObject json = JSON.parseObject(strJson);
        
        //最大撤单CHECK
                if(mapTradeRuleMemorySave.size() > 0){
                    UserTradeRuleMemorySave userTradeRuleMemorySave = new UserTradeRuleMemorySave();
                    userTradeRuleMemorySave = mapTradeRuleMemorySave.get(json.getString("instrumentID"));
                    //最大撤单量  < 已经撤单量 + 当前撤单量
                    if(userTradeRuleMemorySave.getMaxCancelCount() < userTradeRuleMemorySave.getRealCancelCount() + json.getIntValue("volumeChange")){
                        //不能撤单 最大撤单量受限
                        StringBuffer sb = new StringBuffer();
                        sb.append("onRtnOrder|" + subAccount + "|error|不能撤单 最大撤单量受限");
                        SocketPrintOut(sb.toString());
                        //socketStr = ;
                        Display.getDefault().syncExec(new Runnable() {
                                            
                            @Override
                            public void run() {
                            getTradeResponse().append(sb.toString()+"\r\n");
                            }
                        });
                        return;
                    } else {
                        int realCancelCount = 0;
                        realCancelCount = userTradeRuleMemorySave.getRealCancelCount() + json.getIntValue("volumeChange");
                        logger.info("realCancelCount:"+realCancelCount);
                        userTradeRuleMemorySave.setRealCancelCount(realCancelCount);
                    }
                    
                }
                
        
        
        CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
        pInputOrderAction.setBrokerID(mainAccount.getBrokerId());
        pInputOrderAction.setInvestorID(mainAccount.getAccountNo());
        // caoxx2 start orderSysID + exchangeID 撤单
//        pInputOrderAction.setOrderRef(json.getString("orderRef"));
//        pInputOrderAction.setFrontID(Integer.valueOf(json.getString("frontID")));
//        pInputOrderAction.setSessionID(Integer.valueOf(json.getString("sessionID")));
        // caoxx2 end
        pInputOrderAction.setInstrumentID(json.getString("instrumentID"));
        pInputOrderAction.setActionFlag(json.getString("actionFlag").toCharArray()[0]);
        
        //caoxx2 start orderSysID + exchangeID 撤单
        pInputOrderAction.setOrderSysID(json.getString("orderSysID"));
        pInputOrderAction.setExchangeID(json.getString("exchangeID"));
        //caoxx2 end
        
        StringBuffer sb = new StringBuffer();
        sb.append("reqOrderAction|");
        sb.append(JSON.toJSONString(pInputOrderAction)+"|");
        sb.append(nRequestID);
        
        // caoxx2 start 自成交 前置新规改修
        if (json.getString("userID").equals("0")){
            //买开卖平前置1
            //logger.info("zzzzz"+CTPApi.reqOrderAction(pInputOrderAction, nRequestID));
            outFirst.println(sb.toString());
            outFirst.flush();
        } else if(json.getString("userID").equals("1")){
            //卖开买平前置2
            //logger.info("zzzzz"+CTPApi.reqOrderAction(pInputOrderAction, nRequestID));
            outSecond.println(sb.toString());
            outSecond.flush();
        }
        // caoxx2 end 自成交 前置新规改修
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpRequest().append(JSON.toJSONString(pInputOrderAction)+"\r\n");
            }
        });
        
        
        
        
        
    }
    
    public void subOrder(String subAccount,String strJson){
        logger.info("subOrder:" + subAccount + JSON.toJSONString(strJson));
        
        
        JSONObject json = JSON.parseObject(strJson);
        
        
        //最大委托CHECK
        if(mapTradeRuleMemorySave.size() > 0){
            UserTradeRuleMemorySave userTradeRuleMemorySave = new UserTradeRuleMemorySave();
            userTradeRuleMemorySave = mapTradeRuleMemorySave.get(json.getString("instrumentID"));
            //最大委托量  < 已经委托量 + 当前委托量
            if(userTradeRuleMemorySave.getMaxEntrustCount() < userTradeRuleMemorySave.getRealEntrustCount() + json.getIntValue("volumeTotalOriginal")){
                //不能开仓 最大开仓量受限
                StringBuffer sb = new StringBuffer();
                sb.append("onRtnOrder|" + subAccount + "|error|不能委托  最大委托量受限");
                SocketPrintOut(sb.toString());
                //socketStr = ;
                Display.getDefault().syncExec(new Runnable() {
                                    
                    @Override
                    public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                    }
                });
                return;
            } else {
                int realOpenCount = 0;
                realOpenCount = userTradeRuleMemorySave.getRealEntrustCount() + json.getIntValue("volumeTotalOriginal");
                logger.info("realOpenCount:"+realOpenCount);
                userTradeRuleMemorySave.setRealEntrustCount(realOpenCount);
            }
            
        }
        
        
        
        
        UserFlgMemorySave userFlgMemorySave = new UserFlgMemorySave();
        userFlgMemorySave = mapUserFlgMemorySave.get(subAccount);
        if(userFlgMemorySave.getFlag1().equals("1")){
            //交易员已被强平限制
            StringBuffer sb = new StringBuffer();
            sb.append("onRtnOrder|" + subAccount + "|error|强平限制");
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                                
                @Override
                public void run() {
                getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
            return;
        }
        
        if(userFlgMemorySave.getFlag2().equals("1")){
            //交易员已被只许平仓
            if(json.getString("combOffsetFlag").equals("0"))
            {
                //开仓指令ERROR
                StringBuffer sb = new StringBuffer();
                sb.append("onRtnOrder|" + subAccount + "|error|只许平仓");
                SocketPrintOut(sb.toString());
                //socketStr = ;
                Display.getDefault().syncExec(new Runnable() {
                                    
                    @Override
                    public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                    }
                });
                return;
            }
        }
        
//        orderRef = String.valueOf(Integer.parseInt(orderRef)+ 1);
        int tmpint = atomicInteger.incrementAndGet();
        String order = String.valueOf(tmpint);
        nRequestID = nRequestID + 1;
        
        //计算可用资金
//        mapUserContractMemorySave
        //合约名
        String heyue = json.getString("instrumentID");
        UserContract userContract = new UserContract();
//        String userAccount = mapUserAccountMemorySave.get(subAccount);
        UserAvailableMemorySave userAvailableMemorySave  = mapAvailableMemorySave.get(subAccount);
        String userAccount = availableCalc(userAvailableMemorySave);
        userContract = mapUserContractMemorySave.get(subAccount + heyue);
        //账户可用资金
        double keyongzijin;
        //开仓手续费
        double shouxufeikaicang;
        //平仓手续费
        double shouxufeipingcang;
        //合约保证金
        double heyuebaozhengjin;
        //总冻结资金
        double zongdongjiezijin;
        //手数
        int shoushu;
        
        int reti = 0;
        
        //开仓指令
        if (json.getString("combOffsetFlag").equals("0"))
        {
            shoushu = json.getIntValue("volumeTotalOriginal");
            heyuebaozhengjin = json.getDoubleValue("limitPrice");
            //合约保证金 = 价格 * 合约单位 * 保证金比例 * 手数
            heyuebaozhengjin = heyuebaozhengjin * userContract.getContractUnit().doubleValue() * userContract.getMargin().doubleValue() * shoushu;

            //开仓手续费 = 手数 * 开仓手续费 + 价格 * 合约单位  * 手数 * 开仓手续费比例  
            shouxufeikaicang = shoushu * userContract.getOpenCharge().doubleValue() +   json.getDoubleValue("limitPrice") * userContract.getContractUnit().doubleValue() * shoushu * userContract.getOpenChargeRate().doubleValue() ;
            
            logger.info("subOrder手数 ="+shoushu);
            logger.info("subOrder开仓手续费 ="+userContract.getOpenCharge().doubleValue());
            logger.info("subOrder价格 ="+heyuebaozhengjin);
            logger.info("subOrder合约单位 ="+userContract.getContractUnit().doubleValue());
            logger.info("subOrder开仓手续费比例  ="+userContract.getOpenChargeRate().doubleValue());
            
            
            //总冻结资金 = 合约保证金 + 开仓手续费
            zongdongjiezijin =  heyuebaozhengjin + shouxufeikaicang;
            
            //账户可用资金
            keyongzijin = Double.valueOf(userAccount);
            
            logger.info("subOrder账户可用资金="+keyongzijin);
            logger.info("subOrder总冻结资金="+zongdongjiezijin);
            logger.info("subOrder开仓手续费="+shouxufeikaicang);
            logger.info("subOrder合约保证金="+heyuebaozhengjin);
            
            if(keyongzijin < zongdongjiezijin ) {
                // 资金不足 返回子账户
                //发送给交易员
                StringBuffer sb = new StringBuffer();
                sb.append("onRtnOrder|" + subAccount + "|error|资金不足");
                SocketPrintOut(sb.toString());
                //socketStr = ;
                Display.getDefault().syncExec(new Runnable() {
                                    
                    @Override
                    public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                    }
                });
                return;
            } else {
                // T_SUB_TRADINGACCOUNT 中的FROZENMARGIN= heyuebaozhengjin FROZENCOMMISSION = shouxufeikaicang
                try {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("subAccount", subAccount);
                    map.put("heyuebaozhengjin", heyuebaozhengjin);
                    map.put("shouxufeikaicang", shouxufeikaicang);
                    reti = subTradingaccountService.setFrozen(map);
                } catch (FutureException e) {
                    // 
                    e.printStackTrace();
                }
                
                // T_USER_FROZENACCOUNT 中添加记录  FROZEN_FLAG = 0 FROZENMARGIN = heyuebaozhengjin FROZENCOMMISSION = shouxufeikaicang
                UserFrozenaccount userFrozenaccount = new UserFrozenaccount();
                userFrozenaccount.setUserName(subAccount);
                userFrozenaccount.setFrontid(new BigDecimal(frontID));
                userFrozenaccount.setSessionid(new BigDecimal(sessionID));
                userFrozenaccount.setOrderref(order);
                userFrozenaccount.setFrozenmargin(new BigDecimal(heyuebaozhengjin));
                userFrozenaccount.setFrozencash(new BigDecimal("0"));
                userFrozenaccount.setFrozencommission(new BigDecimal(shouxufeikaicang));
                userFrozenaccount.setOffsetflag("0");
                userFrozenaccount.setFrozenFlag("0");
                userFrozenaccount.setVolumetotaloriginal(Long.valueOf(String.valueOf(shoushu)));
                userFrozenaccount.setLimitprice(json.getBigDecimal("limitPrice"));
                
                //冻结资金做可用计算
                userAvailableMemorySave  = mapAvailableMemorySave.get(subAccount);
                double tempdb = heyuebaozhengjin + shouxufeikaicang;
                tempdb =  Double.valueOf(userAvailableMemorySave.getFrozenAvailable()) + tempdb;                
                userAvailableMemorySave.setFrozenAvailable(String.valueOf(tempdb));
                availableMap(subAccount,userAvailableMemorySave);
                
                
                try {
                    userFrozenaccountService.saveUserFrozenaccount(userFrozenaccount);
                } catch (FutureException e) {
                    // 
                    e.printStackTrace();
                }
                
            }
            
        } 
        
        InputOrder inputOrder = new InputOrder();
        
        inputOrder.setBrokerid(mainAccount.getBrokerId());
        inputOrder.setInvestorid(mainAccount.getAccountNo());
        inputOrder.setFrontid(new BigDecimal(frontID));
        inputOrder.setSessionid(new BigDecimal(sessionID));
        inputOrder.setOrderref(order);
        inputOrder.setSubuserid(subAccount);
        inputOrder.setInstrumentid(json.getString("instrumentID"));                    
        inputOrder.setUserid(mainAccount.getAccountNo());            
        inputOrder.setOrderpricetype(json.getString("orderPriceType"));       
        inputOrder.setDirection(json.getString("direction"));            
        inputOrder.setComboffsetflag(json.getString("combOffsetFlag"));     
        inputOrder.setCombhedgeflag(json.getString("combHedgeFlag"));        
        inputOrder.setLimitprice(json.getBigDecimal("limitPrice"));           
        inputOrder.setVolumetotaloriginal(json.getLong("volumeTotalOriginal"));    
        inputOrder.setTimecondition(json.getString("timeCondition"));        
        inputOrder.setGtddate(json.getString("GTDDate"));                
        inputOrder.setVolumecondition(json.getString("volumeCondition"));        
        inputOrder.setMinvolume(json.getLong("minVolume"));              
        inputOrder.setContingentcondition(json.getString("contingentCondition"));    
        inputOrder.setStopprice(json.getBigDecimal("stopPrice"));              
        inputOrder.setForceclosereason(json.getString("forceCloseReason"));       
        inputOrder.setIsautosuspend(json.getLong("isAutoSuspend"));   
        inputOrder.setRequestid(Long.valueOf(String.valueOf(nRequestID)));              
   
        //插入数据库T_INPUT_ORDER
        try {
            inputOrderService.saveInputOrder(inputOrder);
        } catch (FutureException e) {
            // 
            e.printStackTrace();
        }
        
        
        
        
        
        logger.info("下单操作START");
        //下单操作
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        //期货公司代码
        inputOrderField.setBrokerID(mainAccount.getBrokerId());
        //投资者代码
        inputOrderField.setInvestorID(mainAccount.getAccountNo());
        // 合约代码
        inputOrderField.setInstrumentID(json.getString("instrumentID"));
        ///报单引用        
        inputOrderField.setOrderRef(order);
        // 用户代码
        inputOrderField.setUserID(mainAccount.getAccountNo());
        // 报单价格条件
        inputOrderField.setOrderPriceType(json.getString("orderPriceType").toCharArray()[0]);
        // 买卖方向
        inputOrderField.setDirection(json.getString("direction").toCharArray()[0]);
        // 组合开平标志
        inputOrderField.setCombOffsetFlag(json.getString("combOffsetFlag"));
        // 组合投机套保标志
        inputOrderField.setCombHedgeFlag(json.getString("combHedgeFlag"));
        // 价格
        inputOrderField.setLimitPrice(json.getDoubleValue("limitPrice"));
        // 数量
        inputOrderField.setVolumeTotalOriginal(json.getIntValue("volumeTotalOriginal"));
        // 有效期类型
        inputOrderField.setTimeCondition(json.getString("timeCondition").toCharArray()[0]);
        // GTD日期
        inputOrderField.setGTDDate(json.getString("GTDDate"));
        // 成交量类型
        inputOrderField.setVolumeCondition(json.getString("volumeCondition").toCharArray()[0]);
        // 最小成交量
        inputOrderField.setMinVolume(json.getIntValue("minVolume"));
        // 触发条件
        inputOrderField.setContingentCondition(json.getString("contingentCondition").toCharArray()[0]);
        // 止损价
        inputOrderField.setStopPrice(json.getDoubleValue("stopPrice"));
        // 强平原因
        inputOrderField.setForceCloseReason(json.getString("forceCloseReason").toCharArray()[0]);
        // 自动挂起标志
        inputOrderField.setIsAutoSuspend(json.getIntValue("isAutoSuspend"));
        
        //logger.info("zzzzz"+CTPApi.reqOrderInsert(inputOrderField, nRequestID));
        StringBuffer sb = new StringBuffer();
        sb.append("reqOrderInsert|");
        sb.append(JSON.toJSONString(inputOrderField)+"|");
        sb.append(nRequestID);
        
        String direction = json.getString("direction");
        String offset = json.getString("combOffsetFlag");
        if((direction.equals("0")&& offset.equals("0")) || (direction.equals("1") && !offset.equals("0"))){
            //买开卖平
            outFirst.println(sb.toString());
            outFirst.flush();
        }else {
            outSecond.println(sb.toString());
            outSecond.flush();
        }
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpRequest().append(JSON.toJSONString(inputOrderField)+"\r\n");
            }
        });
        
        
    }
    
    /**
     * 方法加同步，不然报java.util.ConcurrentModificationException
     * @param market
     */
    public synchronized void SocketPrintOut(String market){
        
        if(clients != null){
            for (Socket socket : clients) {
                try {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
                    
                    out.println(market);
                } catch (Exception e) {
                    logger.error("获取输出流失败",e);
                }
            }
        }
        
//        Display.getDefault().syncExec(new Runnable() {
//            
//            @Override
//            public void run() {
//                tradeResponse.append("接收到行情"+market+"\r\n");
//            }
//        });
    }

    public Text getTradeResponse() {
        return tradeResponse;
    }

    public void setTradeResponse(Text tradeResponse) {
        this.tradeResponse = tradeResponse;
    }

    public Text getCtpRequest() {
        return ctpRequest;
    }

    public void setCtpRequest(Text ctpRequest) {
        this.ctpRequest = ctpRequest;
    }

    public Text getCtpResponse() {
        return ctpResponse;
    }

    public void setCtpResponse(Text ctpResponse) {
        this.ctpResponse = ctpResponse;
    }

    public Text getTradeRequest() {
        return tradeRequest;
    }

    public void setTradeRequest(Text tradeRequest) {
        this.tradeRequest = tradeRequest;
    }
    
    
    public void subLoginout(String subAccount,String password){
        //CHECK 子账户用户名密码
        int reti = 1;
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(subAccount);
        userLogin.setUserPwd(password);
        userLogin.setUserNo("");
        userLogin.setUpdateTime(new Date());
//        try {
//            userLoginService.saveUserLogin(userLogin);
//        } catch (FutureException e) {
//            socketStr = "subLogin|" + subAccount + "|" + ErrorConstant.USER_LOGIN_FAILED_CODE+"|"+ErrorConstant.USER_LOGIN_FAILED_MSG;
//            SocketPrintOut(socketStr);
//            getTradeResponse().append(socketStr+"\r\n");
//            return ;
//        }
    }
    public void closeAccount(){
        //结算单准备生成，先把    
        logger.info("mapAvailableMemorySave导出");
        String userName = "";
        
        for (Map.Entry<String,UserAvailableMemorySave> entry : mapAvailableMemorySave.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            logger.info("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
            userName = entry.getKey();
            UserAvailableMemorySave userAvailableMemorySave = entry.getValue();
            Useravailableindb useravailableindb = new Useravailableindb();
            useravailableindb.setUsername(userAvailableMemorySave.getUserName());
            useravailableindb.setAvailable(new BigDecimal(userAvailableMemorySave.getAvailable()));
            useravailableindb.setClosewin(new BigDecimal(userAvailableMemorySave.getCloseWin()));
            useravailableindb.setCommission(new BigDecimal(userAvailableMemorySave.getCommission()));
            useravailableindb.setFrozenavailable(new BigDecimal(userAvailableMemorySave.getFrozenAvailable()));
            useravailableindb.setInoutmoney(new BigDecimal(userAvailableMemorySave.getInOutMoney()));
            useravailableindb.setMargin(new BigDecimal(userAvailableMemorySave.getMargin()));
            useravailableindb.setPositionwin(new BigDecimal(userAvailableMemorySave.getPositionWin()));
            
            useravailableindbMapper.insert(useravailableindb);
        }
        
        logger.info("mapAvailableMemorySave导出完成！");
        
        //逐笔对冲清算开始
        logger.info("逐笔对冲清算开始！");
        
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        int retInt =0;
        //取得结算日平仓数据
        try {
        	List<SellDetail> listSellDetail  = sellDetailService.getSellDetail(dateString);
        	for (SellDetail sellDetail : listSellDetail) {
        		retInt = 0;
        		//根据平仓数据 先查找历史持仓表中有无，再查找今日开仓表
        		//子用户 
//        		sellDetail.getSubuserid();
        		//COMBOKEY
//        		sellDetail.getCombokey();
        		//方向 
//        		sellDetail.getDirection();
        		//合约 
//        		sellDetail.getInstrumentid();
        		//平仓数量
//        		sellDetail.getVolume();
        		
        		try {
					retInt = positionsDetailService.doFindPositionsDetail(sellDetail.getSubuserid(), sellDetail.getCombokey(), sellDetail.getDirection(), sellDetail.getInstrumentid(), sellDetail.getVolume());
					
				} catch (Exception e) {
					
					logger.error("positionsDetailService.doFindPositionsDetail;",e);
					e.printStackTrace();
				}
        		
        		
        		if (retInt == 0) {
        			// 先平了隔夜仓。
        		} else {
        			// 平今仓
        			try {
						buyDetailService.doFindPositionsDetail(sellDetail.getSubuserid(), sellDetail.getCombokey(), sellDetail.getDirection(), sellDetail.getInstrumentid(), retInt);
					} catch (Exception e) {
						logger.error("buyDetailService.doFindPositionsDetail;",e);
						e.printStackTrace();
					}
        		}
        		
    		}
        	
        	
        	
		} catch (FutureException e) {
			// TODO Auto-generated catch block
			logger.error("sellDetailService.getSellDetail(dateString);",e);
			e.printStackTrace();
		}
        
        
        
        
        
        logger.info("逐笔对冲清算完成！");
    }
    
    public void subLogin(String subAccount,String password){
        
        //CHECK 子账户用户名密码
        int reti = 0;
        StringBuffer sb = new StringBuffer();
        
        try {
            reti = userInfoService.checkUser(subAccount,password);
        } catch (FutureException e1) {
            e1.printStackTrace();
        }
        
        if (reti == 1){
            // 正常
            UserLogin userLogin = new UserLogin();
            userLogin.setUserName(subAccount);
            userLogin.setUserPwd(password);
            userLogin.setUserNo("");
            userLogin.setUpdateTime(new Date());
            try {
                userLoginService.saveUserLogin(userLogin);
            } catch (FutureException e) {
                sb.delete(0, sb.length());
                sb.append("subLogin|" + subAccount + "|" + ErrorConstant.USER_LOGIN_FAILED_CODE+"|"+ErrorConstant.USER_LOGIN_FAILED_MSG);
                SocketPrintOut(sb.toString());
                getTradeResponse().append(sb.toString()+"\r\n");
                return ;
            }
            //交易员登入CHECK正常
            sb.append("subLogin|" + subAccount + "|login|" + "00000");
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
            /*Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    getTradeResponse().append(socketStr+"\r\n");
                }
            });*/
            
            //传子账户订阅的合约
            UserContract userContract = new UserContract();
            try {
                List<UserContract> listUserContract = userContractService.queryUserContractByUserName(subAccount);
                for (UserContract userContract2 : listUserContract) {
                    sb.delete(0, sb.length());
                    sb.append("subLogin|" + subAccount + "|" + "listUserContract|" + JSON.toJSONString(userContract2));
                    SocketPrintOut(sb.toString());
                    Display.getDefault().syncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            getTradeResponse().append(sb.toString()+"\r\n");
                        }
                    });
                }
            } catch (FutureException e) {
                e.printStackTrace();
            }
            

            
            // 传送子账户资金信息
//            SubTradingaccount subTradingaccount = new SubTradingaccount();
//            try {
//                subTradingaccount = subTradingaccountService.getUserByUserName2(subAccount);
//            } catch (FutureException e) {
//                sb.delete(0, sb.length());
//                sb.append("subLogin|" + subAccount + "|" + ErrorConstant.USER_LOGIN_FAILED_CODE+"|"+ErrorConstant.USER_LOGIN_FAILED_MSG);
//                SocketPrintOut(sb.toString());
//                getTradeResponse().append(sb.toString()+"\r\n");
//                return ;
//            }
            UserAvailableMemorySave userAvailableMemorySave = mapAvailableMemorySave.get(subAccount);
            //可用=资金+平仓盈亏+持仓盈亏-手续费-冻结资金-占用保证金+出入金
            logger.debug("可用资金打印："+JSON.toJSONString(userAvailableMemorySave));
            double available = 0;
            available = Double.valueOf(userAvailableMemorySave.getAvailable()) 
                    + Double.valueOf(userAvailableMemorySave.getCloseWin())
                    + Double.valueOf(userAvailableMemorySave.getPositionWin())
                    - Double.valueOf(userAvailableMemorySave.getCommission())
                    - Double.valueOf(userAvailableMemorySave.getFrozenAvailable())
                    - Double.valueOf(userAvailableMemorySave.getMargin())
                    + Double.valueOf(userAvailableMemorySave.getInOutMoney());
            SubTradingaccount subTradingaccount = new SubTradingaccount();
            subTradingaccount.setAccountid(subAccount);
            subTradingaccount.setAvailable(new BigDecimal(available));
            sb.delete(0, sb.length());
            sb.append("subLogin|" + subAccount + "|" + "subTradingaccount|" + JSON.toJSONString(subTradingaccount));
            SocketPrintOut(sb.toString());
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
            
            // 传送子账户委托信息
//            InputOrder inputOrder = new InputOrder();
            List<InputOrder> inputOrder = null;
            Order order = new Order();
            
            
            try {
                inputOrder = inputOrderService.getUserByUserName(subAccount);
                for (InputOrder inputOrder2 : inputOrder) {
                    
                    inputOrder2.getOrderref();
                    inputOrder2.getFrontid();
                    inputOrder2.getSessionid();
                    order = orderService.getOrderByCondition(String.valueOf(inputOrder2.getFrontid()),String.valueOf(inputOrder2.getSessionid()),inputOrder2.getOrderref());
                    sb.delete(0, sb.length());
                    sb.append("subLogin|" + subAccount + "|" + "order|" + JSON.toJSONString(order));
                    SocketPrintOut(sb.toString());
                    Display.getDefault().syncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            getTradeResponse().append(sb.toString()+"\r\n");
                        }
                    });
                    
                }
            } catch (FutureException e) {
                
                sb.delete(0, sb.length());
                sb.append("subLogin|" + subAccount + "|" + ErrorConstant.USER_LOGIN_FAILED_CODE+"|"+ErrorConstant.USER_LOGIN_FAILED_MSG);
                SocketPrintOut(sb.toString());
                getTradeResponse().append(sb.toString()+"\r\n");
                return ;
            }
            
            // 传送子账户成交信息
            List<Trade> trade = null;
            try {
                trade = tradeService.getUserByUserName2(subAccount);
                for (Trade trade2 : trade) {
                    sb.delete(0, sb.length());
                    sb.append("subLogin|" + subAccount + "|" + "trade|" + JSON.toJSONString(trade2));
                    SocketPrintOut(sb.toString());
                    Display.getDefault().syncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            getTradeResponse().append(sb.toString()+"\r\n");
                        }
                    });
                }
            } catch (FutureException e) {
                sb.delete(0, sb.length());
                sb.append("subLogin|" + subAccount + "|" + ErrorConstant.USER_LOGIN_FAILED_CODE+"|"+ErrorConstant.USER_LOGIN_FAILED_MSG);
                SocketPrintOut(sb.toString());
                getTradeResponse().append(sb.toString()+"\r\n");
                return ;
            }
            
            
            
            // 传送子账户持仓信息
            List<InvestorPosition> listInvestorPosition = new ArrayList<InvestorPosition>();
            try {
                listInvestorPosition = investorPositionService.getSubUserPostion(subAccount);
            } catch (FutureException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (InvestorPosition investorPosition2 : listInvestorPosition) {
                //发送给交易员持仓数据
                sb.delete(0, sb.length());
                sb.append("subLogin|" + subAccount + "|onPosition|" + JSON.toJSONString(investorPosition2));
                SocketPrintOut(sb.toString());
                //socketStr = ;
                Display.getDefault().syncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        getTradeResponse().append(sb.toString()+"\r\n");
                    }
                });
            }
//            sb.delete(0, sb.length());
//            sb.append("subLogin|" + subAccount + "|" + "investorPosition|" + JSON.toJSONString(investorPosition));
//            SocketPrintOut(sb.toString());
//            Display.getDefault().syncExec(new Runnable() {
//                
//                @Override
//                public void run() {
//                    getTradeResponse().append(sb.toString()+"\r\n");
//                }
//            });
            
            
            
            
            
            
            
            sb.delete(0, sb.length());
            sb.append("subLogin|" + subAccount + "|login|" + "END");
            SocketPrintOut(sb.toString());
            //socketStr = ;
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(sb.toString()+"\r\n");
                }
            });
            
            
            
            
        } else {
            //错误
            String socketStr1 = "subLogin|" + subAccount + "|login|" + ErrorConstant.USER_LOGIN_FAILED_CODE+"|"+ErrorConstant.USER_LOGIN_FAILED_MSG;
            SocketPrintOut(socketStr1);
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getTradeResponse().append(socketStr1+"\r\n");
                }
            });
            
        }
        
        
//        
    }

    public List<Socket> getClients() {
        return clients;
    }

    public void setClients(List<Socket> clients) {
        this.clients = clients;
    }
    
    
    public void riskLogin(){
        logger.info("riskLogin");
    }
    
    public void riskCommand1(String subUserid){
        //强平限制
        //1.限制FLG设为1
        UserFlgMemorySave userFlgMemorySave = new UserFlgMemorySave();
        userFlgMemorySave = mapUserFlgMemorySave.get(subUserid);
        userFlgMemorySave.setFlag1("1");
        //2.强平此交易员下的所有持仓
        /*List<InvestorPosition> listInvestorPosition = new ArrayList<InvestorPosition>();
        try {
            listInvestorPosition = investorPositionService.getSubUserPostion(subUserid);
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (InvestorPosition investorPosition : listInvestorPosition) {
//            orderRef = String.valueOf(Integer.parseInt(orderRef)+ 1);
            int tempint = atomicInteger.incrementAndGet();
            String orderRef = String.valueOf(tempint);
            nRequestID = nRequestID + 1;
            
            
            InputOrder inputOrder = new InputOrder();
            
            inputOrder.setBrokerid(mainAccount.getBrokerId());
            inputOrder.setInvestorid(mainAccount.getAccountNo());
            inputOrder.setFrontid(new BigDecimal(frontID));
            inputOrder.setSessionid(new BigDecimal(sessionID));
            inputOrder.setOrderref(orderRef);
            inputOrder.setSubuserid(subUserid);
            inputOrder.setInstrumentid(investorPosition.getInstrumentid());                    
            inputOrder.setUserid(mainAccount.getAccountNo());            
            inputOrder.setOrderpricetype("0");
            if(investorPosition.getPosidirection().equals("0")){

                inputOrder.setDirection("1");   
            } else {
                inputOrder.setDirection("0");   
            }
                     
            inputOrder.setComboffsetflag("3");     
            inputOrder.setCombhedgeflag("1");        
            inputOrder.setLimitprice(new BigDecimal("0"));           
            inputOrder.setVolumetotaloriginal(investorPosition.getPosition());    
            inputOrder.setTimecondition(String.valueOf(THOST_FTDC_TC_GFD));        
            inputOrder.setGtddate("");                
            inputOrder.setVolumecondition(String.valueOf(THOST_FTDC_VC_AV));        
            inputOrder.setMinvolume(Long.valueOf("0"));              
            inputOrder.setContingentcondition(String.valueOf(THOST_FTDC_CC_Immediately));    
            inputOrder.setStopprice(new BigDecimal("0"));              
            inputOrder.setForceclosereason(String.valueOf(THOST_FTDC_FCC_NotForceClose));       
            inputOrder.setIsautosuspend(Long.valueOf("0"));   
            inputOrder.setRequestid(Long.valueOf(String.valueOf(nRequestID)));              
       
            //插入数据库T_INPUT_ORDER
            try {
                inputOrderService.saveInputOrder(inputOrder);
            } catch (FutureException e) {
                // 
                e.printStackTrace();
            }
            
            
            //根据持仓平仓
            logger.info("下单操作START");
            //下单操作
            CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
            //期货公司代码
            inputOrderField.setBrokerID(mainAccount.getBrokerId());
            //投资者代码
            inputOrderField.setInvestorID(mainAccount.getAccountNo());
            // 合约代码
            inputOrderField.setInstrumentID(investorPosition.getInstrumentid());
            ///报单引用        
            inputOrderField.setOrderRef(orderRef);
            // 用户代码
            inputOrderField.setUserID(mainAccount.getAccountNo());
            // 报单价格条件
            inputOrderField.setOrderPriceType(THOST_FTDC_OPT_AnyPrice);
            // 买卖方向
            if(investorPosition.getPosidirection().equals("0")){
                inputOrderField.setDirection((char)'1');
            } else {
                inputOrderField.setDirection((char)'0');
            }
            
            // 组合开平标志
            inputOrderField.setCombOffsetFlag("3");
            // 组合投机套保标志
            inputOrderField.setCombHedgeFlag("1");
            // 价格
            inputOrderField.setLimitPrice(0);
            // 数量
            inputOrderField.setVolumeTotalOriginal(investorPosition.getPosition().intValue());
            // 有效期类型
            inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
            // GTD日期
            inputOrderField.setGTDDate("");
            // 成交量类型
            inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
            // 最小成交量
            inputOrderField.setMinVolume(0);
            // 触发条件
            inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
            // 止损价
            inputOrderField.setStopPrice(0);
            // 强平原因
            inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
            // 自动挂起标志
            inputOrderField.setIsAutoSuspend(0);
            
            logger.info("riskCommand1"+CTPApi.reqOrderInsert(inputOrderField, nRequestID));
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getCtpRequest().append(JSON.toJSONString(inputOrderField)+"\r\n");
                }
            });
            
            
        }*/
        
        //3.撤单指令
        List<Order> listOrder = new ArrayList<Order>();
        try {
            listOrder = orderService.getCanelByUserName(subUserid);
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(listOrder == null || listOrder.size() < 1){
            return;
        }
        
        try {
            for (Order order : listOrder) {
                CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
                pInputOrderAction.setBrokerID(mainAccount.getBrokerId());
                pInputOrderAction.setInvestorID(mainAccount.getAccountNo());
//                pInputOrderAction.setOrderRef(order.getOrderref());
//                pInputOrderAction.setFrontID(order.getFrontid().intValue());
//                pInputOrderAction.setSessionID(order.getSessionid().intValue());
                pInputOrderAction.setExchangeID(order.getExchangeid());
                pInputOrderAction.setOrderSysID(order.getOrdersysid());
                pInputOrderAction.setInstrumentID(order.getInstrumentid());
                pInputOrderAction.setActionFlag(THOST_FTDC_AF_Delete);
                nRequestID = nRequestID + 1;
                //logger.info("riskCommand1:"+CTPApi.reqOrderAction(pInputOrderAction, nRequestID));
                StringBuffer sb = new StringBuffer();
                sb.append("reqOrderAction|");
                sb.append(JSON.toJSONString(pInputOrderAction)+"|");
                sb.append(nRequestID);
                String direction = order.getDirection();
                String offset = order.getComboffsetflag();
                if((direction.equals("0") && offset.equals("0")) && direction.equals("1") && !offset.equals("0")){
                    //买开卖平路线
                    outFirst.println(sb.toString());
                    outFirst.flush();
                }else {
                    //卖开买平路线
                    outSecond.println(sb.toString());
                    outSecond.flush();
                }
                
                Display.getDefault().syncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        getCtpRequest().append(JSON.toJSONString(pInputOrderAction)+"\r\n");
                    }
                });
            }
        } catch (Exception e) {
            logger.error("撤单失败",e);
        }
        
        
//         orderService.getOrderByCondition
//        
//        logger.info("riskCommand1:" + subAccount + JSON.toJSONString(strJson));
//        nRequestID = nRequestID + 1;
//        JSONObject json = JSON.parseObject(strJson);
        
        
        
        
    }
    
    /**
     * 风控强平
     * @param userName 用户名
     * @param jsonStr 报单报文
     */
    public void riskForceClose(String userName, String jsonStr){
        
        logger.debug("风控强平请求报文，userName:"+userName+";"+jsonStr);
        JSONObject json = JSON.parseObject(jsonStr);
        int tempint = atomicInteger.incrementAndGet();
        String orderRef = String.valueOf(tempint);
        
        //根据合约编号，买卖方向查询是否有平仓挂单，如果有先撤单再平仓
        String instrumentID = json.getString("instrumentID");
        String direction = json.getString("direction");
        
        try {
            List<Order> list = this.orderService.getUserCloseOrderByInstrumentIDAndDirection(userName, instrumentID, direction);
            if(list != null && list.size() >0){
                for (Order order : list) {
                    CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
                    pInputOrderAction.setBrokerID(mainAccount.getBrokerId());
                    pInputOrderAction.setInvestorID(mainAccount.getAccountNo());
//                    pInputOrderAction.setOrderRef(order.getOrderref());
//                    pInputOrderAction.setFrontID(order.getFrontid().intValue());
//                    pInputOrderAction.setSessionID(order.getSessionid().intValue());
                    pInputOrderAction.setOrderSysID(order.getOrdersysid());
                    pInputOrderAction.setExchangeID(order.getExchangeid());
                    pInputOrderAction.setInstrumentID(order.getInstrumentid());
                    pInputOrderAction.setActionFlag(THOST_FTDC_AF_Delete);
                    nRequestID = nRequestID + 1;
                    //logger.info("riskCommand1:"+CTPApi.reqOrderAction(pInputOrderAction, nRequestID));
                    
                    StringBuffer sb = new StringBuffer();
                    sb.append("reqOrderAction|");
                    sb.append(JSON.toJSONString(pInputOrderAction)+"|");
                    sb.append(nRequestID);
                    
                    String offset = order.getComboffsetflag();
                    if((offset.equals("0") && direction.equals("0")) || (!offset.equals("0") && direction.equals("1"))){
                        outFirst.println(sb.toString());
                        outFirst.flush();
                    }else{
                        outSecond.println(sb.toString());
                        outSecond.flush();
                    }
                    
                    
                    Display.getDefault().syncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            getCtpRequest().append(JSON.toJSONString(pInputOrderAction)+"\r\n");
                        }
                    });
                }
            }
        } catch (FutureException e1) {
            
        } catch (Exception e) {
            logger.error("强平撤单失败",e);
        }
        
        
        
        InputOrder inputOrder = new InputOrder();
        
        inputOrder.setBrokerid(mainAccount.getBrokerId());
        inputOrder.setInvestorid(mainAccount.getAccountNo());
        inputOrder.setFrontid(new BigDecimal(frontID));
        inputOrder.setSessionid(new BigDecimal(sessionID));
        inputOrder.setOrderref(orderRef);
        inputOrder.setSubuserid(userName);
        inputOrder.setInstrumentid(instrumentID);                    
        inputOrder.setUserid(mainAccount.getAccountNo());            
        inputOrder.setOrderpricetype(json.getString("orderPriceType"));       
        inputOrder.setDirection(direction);            
        inputOrder.setComboffsetflag(json.getString("combOffsetFlag"));     
        inputOrder.setCombhedgeflag(json.getString("combHedgeFlag"));        
        inputOrder.setLimitprice(json.getBigDecimal("limitPrice"));           
        inputOrder.setVolumetotaloriginal(json.getLong("volumeTotalOriginal"));    
        inputOrder.setTimecondition(json.getString("timeCondition"));        
        inputOrder.setGtddate(json.getString("GTDDate"));                
        inputOrder.setVolumecondition(json.getString("volumeCondition"));        
        inputOrder.setMinvolume(json.getLong("minVolume"));              
        inputOrder.setContingentcondition(json.getString("contingentCondition"));    
        inputOrder.setStopprice(json.getBigDecimal("stopPrice"));              
        inputOrder.setForceclosereason(json.getString("forceCloseReason"));       
        inputOrder.setIsautosuspend(json.getLong("isAutoSuspend"));
        nRequestID = nRequestID + 1;
        inputOrder.setRequestid(Long.valueOf(String.valueOf(nRequestID)));              
   
        //插入数据库T_INPUT_ORDER
        try {
            inputOrderService.saveInputOrder(inputOrder);
        } catch (FutureException e) {
            // 
            e.printStackTrace();
        }
        
        logger.info("下单操作START");
        //下单操作
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        //期货公司代码
        inputOrderField.setBrokerID(mainAccount.getBrokerId());
        //投资者代码
        inputOrderField.setInvestorID(mainAccount.getAccountNo());
        // 合约代码
        inputOrderField.setInstrumentID(json.getString("instrumentID"));
        ///报单引用        
        inputOrderField.setOrderRef(orderRef);
        // 用户代码
        inputOrderField.setUserID(mainAccount.getAccountNo());
        // 报单价格条件
        inputOrderField.setOrderPriceType(json.getString("orderPriceType").toCharArray()[0]);
        // 买卖方向
        inputOrderField.setDirection(json.getString("direction").toCharArray()[0]);
        // 组合开平标志
        inputOrderField.setCombOffsetFlag(json.getString("combOffsetFlag"));
        // 组合投机套保标志
        inputOrderField.setCombHedgeFlag(json.getString("combHedgeFlag"));
        // 价格
        inputOrderField.setLimitPrice(json.getDoubleValue("limitPrice"));
        // 数量
        inputOrderField.setVolumeTotalOriginal(json.getIntValue("volumeTotalOriginal"));
        // 有效期类型
        inputOrderField.setTimeCondition(json.getString("timeCondition").toCharArray()[0]);
        // GTD日期
        inputOrderField.setGTDDate(json.getString("GTDDate"));
        // 成交量类型
        inputOrderField.setVolumeCondition(json.getString("volumeCondition").toCharArray()[0]);
        // 最小成交量
        inputOrderField.setMinVolume(json.getIntValue("minVolume"));
        // 触发条件
        inputOrderField.setContingentCondition(json.getString("contingentCondition").toCharArray()[0]);
        // 止损价
        inputOrderField.setStopPrice(json.getDoubleValue("stopPrice"));
        // 强平原因
        inputOrderField.setForceCloseReason(json.getString("forceCloseReason").toCharArray()[0]);
        // 自动挂起标志
        inputOrderField.setIsAutoSuspend(json.getIntValue("isAutoSuspend"));
        
        //logger.info("zzzzz"+CTPApi.reqOrderInsert(inputOrderField, nRequestID));
        StringBuffer sb = new StringBuffer();
        sb.append("reqOrderInsert|");
        sb.append(JSON.toJSONString(inputOrderField)+"|");
        sb.append(nRequestID);
        
        //开平仓标志
        String offset = json.getString("combOffsetFlag");
        if((offset.equals("0") && direction.equals("0")) || (!offset.equals("0") && direction.equals("1"))){
            //买开卖平
            outFirst.println(sb.toString());
            outFirst.flush();
        }else {
            outSecond.println(sb.toString());
            outSecond.flush();
        }
        
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                getCtpRequest().append(JSON.toJSONString(inputOrderField)+"\r\n");
            }
        });
        
    }
    
    public void riskCommand2(String subUserid){
        
        //一键撤单
        List<Order> listOrder = new ArrayList<Order>();
        try {
            listOrder = orderService.getCanelByUserName(subUserid);
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(listOrder == null || listOrder.size() < 1){
            return;
        }
        try {
            for (Order order : listOrder) {
                CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
                pInputOrderAction.setBrokerID(mainAccount.getBrokerId());
                pInputOrderAction.setInvestorID(mainAccount.getAccountNo());
                pInputOrderAction.setOrderRef(order.getOrderref());
                pInputOrderAction.setFrontID(order.getFrontid().intValue());
                pInputOrderAction.setSessionID(order.getSessionid().intValue());
                pInputOrderAction.setInstrumentID(order.getInstrumentid());
                pInputOrderAction.setActionFlag(THOST_FTDC_AF_Delete);
                
                //logger.info("riskCommand1:"+CTPApi.reqOrderAction(pInputOrderAction, nRequestID));
                String offset = order.getComboffsetflag();
                String direction = order.getDirection();
                
                StringBuffer sb = new StringBuffer();
                sb.append("reqOrderAction|");
                sb.append(JSON.toJSONString(pInputOrderAction)+"|");
                sb.append(nRequestID);
                if((offset.equals("0") && direction.equals("0")) || (!offset.equals("0") && direction.equals("1"))){
                    //买开卖平路线
                    outFirst.println(sb.toString());
                    outFirst.flush();
                }else {
                    outSecond.println(sb.toString());
                    outSecond.flush();
                }
                
                Display.getDefault().syncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        getCtpRequest().append(JSON.toJSONString(pInputOrderAction)+"\r\n");
                    }
                });
            }
        } catch (Exception e) {
            logger.error("撤单失败",e);
        }
        
    }
    
    public void riskCommand3(String subUserid){
        
        //只许平仓
        UserFlgMemorySave userFlgMemorySave = new UserFlgMemorySave();
        userFlgMemorySave = mapUserFlgMemorySave.get(subUserid);
        userFlgMemorySave.setFlag2("1");
        
        
    }
    
    public void riskCommand4(String subUserid){
        
        //撤销限制
        UserFlgMemorySave userFlgMemorySave = new UserFlgMemorySave();
        userFlgMemorySave = mapUserFlgMemorySave.get(subUserid);
        userFlgMemorySave.setFlag1("0");
        userFlgMemorySave.setFlag2("0");
        
        
    }
    
    public void riskCCYK(String subUserid,String available){
        
        //可用资金推送
        
        //解冻资金做可用计算 
        //logger.info("用户名:"+subUserid+"|持仓盈亏做可用计算:"+JSON.toJSONString(available));
        UserAvailableMemorySave userAvailableMemorySave =         mapAvailableMemorySave.get(subUserid);
        userAvailableMemorySave.setPositionWin(available);
        //发送给交易员
        availableMap(subUserid,userAvailableMemorySave);
        
        
        
        //MAP可用资金设定
        mapUserAccountMemorySave.put(subUserid, available);

    }
    
    public void riskCRJ(String subUserid,String available){
        double getOldInOutMoney = 0;
        //出入金推送
        //logger.info("用户名:"+subUserid+"|持仓盈亏做可用计算:"+JSON.toJSONString(available));
        UserAvailableMemorySave userAvailableMemorySave =         mapAvailableMemorySave.get(subUserid);
        getOldInOutMoney = Double.valueOf(userAvailableMemorySave.getInOutMoney());
        getOldInOutMoney = getOldInOutMoney + Double.valueOf(available);
        userAvailableMemorySave.setInOutMoney(String.valueOf(getOldInOutMoney));
        //发送给交易员
        availableMap(subUserid,userAvailableMemorySave);
        
        
        
        //MAP可用资金设定
        mapUserAccountMemorySave.put(subUserid, available);

    }
    
    public void riskTPXZ(String subUserid,String instrumentid,String posidirection){
        //强平subUserid交易员下的instrumentid持仓，方向为posidirection
        List<InvestorPosition> listInvestorPosition = new ArrayList<InvestorPosition>();
        try {
            listInvestorPosition = investorPositionService.getSubUserPostion2(subUserid,instrumentid,posidirection);
        } catch (FutureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (InvestorPosition investorPosition : listInvestorPosition) {
//                    orderRef = String.valueOf(Integer.parseInt(orderRef)+ 1);
            int tempint = atomicInteger.incrementAndGet();
            String orderRef = String.valueOf(tempint);
            nRequestID = nRequestID + 1;
            
            
            InputOrder inputOrder = new InputOrder();
            
            inputOrder.setBrokerid(mainAccount.getBrokerId());
            inputOrder.setInvestorid(mainAccount.getAccountNo());
            inputOrder.setFrontid(new BigDecimal(frontID));
            inputOrder.setSessionid(new BigDecimal(sessionID));
            inputOrder.setOrderref(orderRef);
            inputOrder.setSubuserid(subUserid);
            inputOrder.setInstrumentid(investorPosition.getInstrumentid());                    
            inputOrder.setUserid(mainAccount.getAccountNo());            
            inputOrder.setOrderpricetype("0");
            if(investorPosition.getPosidirection().equals("0")){

                inputOrder.setDirection("1");   
            } else {
                inputOrder.setDirection("0");   
            }
                     
            inputOrder.setComboffsetflag("3");     
            inputOrder.setCombhedgeflag("1");        
            inputOrder.setLimitprice(new BigDecimal("0"));           
            inputOrder.setVolumetotaloriginal(investorPosition.getPosition());    
            inputOrder.setTimecondition(String.valueOf(THOST_FTDC_TC_GFD));        
            inputOrder.setGtddate("");                
            inputOrder.setVolumecondition(String.valueOf(THOST_FTDC_VC_AV));        
            inputOrder.setMinvolume(Long.valueOf("0"));              
            inputOrder.setContingentcondition(String.valueOf(THOST_FTDC_CC_Immediately));    
            inputOrder.setStopprice(new BigDecimal("0"));              
            inputOrder.setForceclosereason(String.valueOf(THOST_FTDC_FCC_NotForceClose));       
            inputOrder.setIsautosuspend(Long.valueOf("0"));   
            inputOrder.setRequestid(Long.valueOf(String.valueOf(nRequestID)));              
       
            //插入数据库T_INPUT_ORDER
            try {
                inputOrderService.saveInputOrder(inputOrder);
            } catch (FutureException e) {
                // 
                e.printStackTrace();
            }
            
            
            //根据持仓平仓
            logger.info("下单操作START");
            //下单操作
            CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
            //期货公司代码
            inputOrderField.setBrokerID(mainAccount.getBrokerId());
            //投资者代码
            inputOrderField.setInvestorID(mainAccount.getAccountNo());
            // 合约代码
            inputOrderField.setInstrumentID(investorPosition.getInstrumentid());
            ///报单引用        
            inputOrderField.setOrderRef(orderRef);
            // 用户代码
            inputOrderField.setUserID(mainAccount.getAccountNo());
            // 报单价格条件
            inputOrderField.setOrderPriceType(THOST_FTDC_OPT_AnyPrice);
            // 买卖方向
            if(investorPosition.getPosidirection().equals("0")){
                inputOrderField.setDirection((char)'1');
            } else {
                inputOrderField.setDirection((char)'0');
            }
            
            // 组合开平标志
            inputOrderField.setCombOffsetFlag("3");
            // 组合投机套保标志
            inputOrderField.setCombHedgeFlag("1");
            // 价格
            inputOrderField.setLimitPrice(0);
            // 数量
            inputOrderField.setVolumeTotalOriginal(investorPosition.getPosition().intValue());
            // 有效期类型
            inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
            // GTD日期
            inputOrderField.setGTDDate("");
            // 成交量类型
            inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
            // 最小成交量
            inputOrderField.setMinVolume(0);
            // 触发条件
            inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
            // 止损价
            inputOrderField.setStopPrice(0);
            // 强平原因
            inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
            // 自动挂起标志
            inputOrderField.setIsAutoSuspend(0);
            logger.info("止损强平:"+JSON.toJSONString(inputOrderField));
            //logger.info("riskTPXZ"+CTPApi.reqOrderInsert(inputOrderField, nRequestID));
            
            StringBuffer sb = new StringBuffer();
            sb.append("reqOrderInsert|");
            sb.append(JSON.toJSONString(inputOrderField)+"|");
            sb.append(nRequestID);
            if(inputOrderField.getDirection() == '1'){
                //卖平
                outFirst.println(sb.toString());
                outFirst.flush();
            }else {
                //买平
                outSecond.println(sb.toString());
                outSecond.flush();
            }
            
            Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                    getCtpRequest().append(JSON.toJSONString(inputOrderField)+"\r\n");
                }
            });
            
        }
    }
    
    

}
