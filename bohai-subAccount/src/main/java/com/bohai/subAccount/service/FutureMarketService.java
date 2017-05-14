package com.bohai.subAccount.service;

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
}
