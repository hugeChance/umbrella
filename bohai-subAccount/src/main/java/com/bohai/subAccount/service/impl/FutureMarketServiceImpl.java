package com.bohai.subAccount.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bohai.subAccount.dao.FutureMarketMapper;
import com.bohai.subAccount.entity.FutureMarket;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.FutureMarketService;

@Service("futureMarketService")
public class FutureMarketServiceImpl implements FutureMarketService {
    
    static Logger logger = Logger.getLogger(FutureMarketServiceImpl.class);
    
    @Autowired
    private FutureMarketMapper futureMarketMapper;

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

}
