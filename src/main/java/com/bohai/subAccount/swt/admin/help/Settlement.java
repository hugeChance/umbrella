package com.bohai.subAccount.swt.admin.help;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.springframework.util.StringUtils;

import com.bohai.subAccount.constant.CommonConstant;
import com.bohai.subAccount.entity.InvestorPosition;
import com.bohai.subAccount.entity.Trade;
import com.bohai.subAccount.entity.UserContract;
import com.bohai.subAccount.exception.FutureException;
import com.bohai.subAccount.service.UserContractService;
import com.bohai.subAccount.utils.Arith;
import com.bohai.subAccount.utils.DateFormatterUtil;
import com.bohai.subAccount.utils.SpringContextUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 结算类
 * @author caojia
 */
public class Settlement {
	
	static Logger logger = Logger.getLogger(Settlement.class);
	
	public void clear(String filePath,String userName,List<InvestorPosition> position) throws WriteException, IOException, FutureException{
		
		UserContractService userContractService = (UserContractService) SpringContextUtil.getBean("userContractService");
		
		logger.info("生成成交清单文件");
		
		String[] title = {"合约代码","买卖","成交手数","开仓均价","平仓均价","开仓时间","平仓时间","盈亏","手续费"}; 
		
		StringBuffer sb = new StringBuffer(filePath);
		sb.append("\\"+DateFormatterUtil.getCurrentDateStr().substring(0, 10));
		File directiory=new File(sb.toString());
		if(!directiory.exists()){
			directiory.mkdirs();
		}
		sb.append("\\"+userName+".xls");
		OutputStream os = null;
		try {
			os = new FileOutputStream(sb.toString());
		} catch (FileNotFoundException e) {
			logger.error("创建输出流失败",e);
		}
		
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet sheet = wb.createSheet("成交清单", 0);
		
		Label label; 
		for(int i=0;i<title.length;i++){ 
			label = new Label(i,0,title[i]); 
			// 将定义好的单元格添加到工作表中 
			sheet.addCell(label);
		}
		
		for(int i=0;i<position.size();i++){
			//合约代码
			Label lab0 = new Label(0, i+1, position.get(i).getInstrumentid());
			sheet.addCell(lab0);
			//买卖
			Label lab1 = new Label(1, i+1, position.get(i).getPosidirection().equals("0")?"买":"卖");
			sheet.addCell(lab1);
			//成交手数
			Label lab2 = new Label(2, i+1, position.get(i).getOpenvolume().toString());
			sheet.addCell(lab2);
			//开仓均价
			Label lab3 = new Label(3, i+1, position.get(i).getOpenamount().toString());
			sheet.addCell(lab3);
			//平仓均价
			Label lab5 = new Label(4, i+1, position.get(i).getCloseamount().toString());
			sheet.addCell(lab5);
			//开仓时间
			Label lab4 = new Label(5, i+1, StringUtils.isEmpty(position.get(i).getOpentime())?"":position.get(i).getOpentime().substring(8));
			sheet.addCell(lab4);
			//平仓时间
			Label lab6 = new Label(6, i+1, StringUtils.isEmpty(position.get(i).getClosetime())?"":position.get(i).getClosetime().substring(8));
			sheet.addCell(lab6);
			//合约单位
			Integer unit = userContractService.getContractUnitByContractNo(position.get(i).getInstrumentid());
			//盈亏
			//买入开仓盈亏 = （平仓价 - 开仓价）*手数*合约单位
			Double win = Arith.mul(Arith.sub(position.get(i).getCloseamount(),position.get(i).getOpenamount()),
					Arith.mul(unit, position.get(i).getOpenvolume()));
			if(position.get(i).getPosidirection().equals("1")){
				//卖出开仓盈亏 = （开仓价 - 平仓价）*手数*合约单位
				win = Arith.mul(win, "-1");
			}
			Label lab7 = new Label(7, i+1, win.toString());
			sheet.addCell(lab7);
			
			Label lab8 = new Label(8, i+1, StringUtils.isEmpty(position.get(i).getCommission())?"":position.get(i).getCommission().toString());
			sheet.addCell(lab8);
		}
		
		
		// 写入数据 
		wb.write();
		// 关闭文件 
		wb.close();
		
		os.close();
		
		logger.info("生成成功，文件目录："+sb.toString());
		
	} 
		
}
