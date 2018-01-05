package com.bohai.subAccount.service;

import com.bohai.subAccount.exception.FutureException;

/**
 * 文件上传
 * @author BHQH-CXYWB
 *
 */
public interface FileService {
    
    public void Import(String fileName) throws FutureException;

}
