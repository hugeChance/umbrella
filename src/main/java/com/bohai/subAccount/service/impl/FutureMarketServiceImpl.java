package com.bohai.subAccount.service.impl;

import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.FutureMarketDaoImpl;
import com.bohai.subAccount.dao.FutureMarketMapper;
import com.bohai.subAccount.entity.FutureMarket;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.FutureMarketService;

@Service("futureMarketService")
public class FutureMarketServiceImpl implements FutureMarketService {
    
    static Logger logger = Logger.getLogger(FutureMarketServiceImpl.class);
    
    @Autowired
    private FutureMarketMapper futureMarketMapper;
    
	@Autowired
	private FutureMarketDaoImpl futureMarketDao;

    @Override
    public FutureMarket queryFutureMarketByInstrument(String instrument) throws FutureException {

        logger.debug("查询合约："+instrument);
        FutureMarket market = null;
        try {
            market = futureMarketMapper.selectByInstrument(instrument);
        } catch (Exception e) {
            logger.error("查询合约行情失败",e);
            throw new FutureException("", "查询合约行情失败");
        }
        return market;
    }
    
    public void saveFutureMarket(CThostFtdcDepthMarketDataField dataField) {
		
//		dataField.getSettlementPrice()
    	if(!(dataField.getSettlementPrice() == Double.MAX_VALUE || dataField.getSettlementPrice() == Double.MIN_VALUE)  )
    	{
			int result = this.futureMarketDao.insert(dataField);
			if(result > 0 ){
				logger.debug("保存行情成功,合约代码： "+dataField.getInstrumentID());
			}else {
				logger.error("保存行情失败");
			}
    	}
		
	}

}
