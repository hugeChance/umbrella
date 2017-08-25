package com.bohai.subAccount.service;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;

import com.bohai.subAccount.entity.FutureMarket;
import com.bohai.subAccount.exception.FutureException;

public interface FutureMarketService {

    /**
     * 根据合约查询最新结算价
     * @param instrument
     * @return
     * @throws FutureException
     */
    FutureMarket queryFutureMarketByInstrument(String instrument) throws FutureException;
    
    /**
	 * 保存期货行情
	 * @param dataField
	 */
	public void saveFutureMarket(CThostFtdcDepthMarketDataField dataField);
}
