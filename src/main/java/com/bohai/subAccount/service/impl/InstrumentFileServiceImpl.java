package com.bohai.subAccount.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bohai.subAccount.dao.UserInfoMapper;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.entity.UserInfo;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.FileService;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.service.UserInfoService;

@Service("instrumentFileService")
public class InstrumentFileServiceImpl implements FileService {

    static Logger logger = Logger.getLogger(InstrumentFileServiceImpl.class);
    
    @Autowired
    private UserContractService userContractService;
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Override
    @Transactional
    public void Import(String fileName) throws FutureException {

        try {
            File file = new File(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            for(int i=1 ; i <= sheet.getLastRowNum(); i++){
                //用户名
                String userName = sheet.getRow(i).getCell(0).getStringCellValue().trim();
                if(userName == null || userName.equals("")){
                    throw new FutureException("", "第"+ i +"行用户名不能为空");
                }
                
                UserInfo userInfo = this.userInfoMapper.selectByUserName(userName);
                if(userInfo == null){
                    throw new FutureException("", "无此用户信息："+userName);
                }
                
                UserContract userContract = new UserContract();
                userContract.setUserNo(userInfo.getUserNo());
                
                //合约号
                String instrumentId = sheet.getRow(i).getCell(1).getStringCellValue().trim();
                userContract.setContractNo(instrumentId);
                //开仓手续费
                String openCharge = sheet.getRow(i).getCell(2).getStringCellValue().trim();
                if(StringUtils.isNotBlank(openCharge)){
                    userContract.setOpenCharge(new BigDecimal(openCharge));
                }
                //平今手续费
                String closeCharge = sheet.getRow(i).getCell(3).getStringCellValue().trim();
                if(StringUtils.isNotBlank(closeCharge)){
                    userContract.setCloseCurrCharge(new BigDecimal(closeCharge));
                }
                //开仓手续费比例
                String openChargeRate = sheet.getRow(i).getCell(4).getStringCellValue().trim();
                if(StringUtils.isNotBlank(openChargeRate)){
                    userContract.setOpenChargeRate(new BigDecimal(openChargeRate));
                }
                //平今手续费比例
                String closeChargeRate = sheet.getRow(i).getCell(5).getStringCellValue().trim();
                if(StringUtils.isNotBlank(closeChargeRate)){
                    userContract.setCloseCurrChargeRate(new BigDecimal(closeChargeRate));
                }
                
                //保证金比例
                String margin = sheet.getRow(i).getCell(6).getStringCellValue().trim();
                if(StringUtils.isBlank(margin)){
                    throw new FutureException("", "保证金比例不能为空");
                }
                userContract.setMargin(new BigDecimal(margin));
                
                //合约单位
                String unit = sheet.getRow(i).getCell(7).getStringCellValue().trim();
                if(StringUtils.isBlank(unit)){
                    throw new FutureException("", "合约单位不能为空");
                }
                userContract.setContractUnit(Integer.parseInt(unit));
                
                //最小变动单位
                String tick = sheet.getRow(i).getCell(8).getStringCellValue().trim();
                if(StringUtils.isBlank(tick)){
                    throw new FutureException("", "最小变动单位不能为空");
                }
                userContract.setTickSize(new BigDecimal(tick));
                
                this.userContractService.saveUserContract(userContract);
                
            }
            
        } catch (Exception e) {
            
            logger.error("导入文件失败");
            throw new FutureException("", e.getMessage());
        }
        
    }

	@Override
	public void ImportJSTxt(String fileName) throws FutureException {
		 File file = new File(fileName);
		 try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while((s = br.readLine())!=null){
				//逐行读取结算单
				
				
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}

}
