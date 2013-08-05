
package com.taobao.monitor.stat.newanalyse.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.taobao.monitor.stat.newanalyse.FetchData;
import com.taobao.monitor.stat.newanalyse.FetchData.ReaderRecord;

/**
 * 
 * @author xiaodu
 * @version 2010-5-10 ÏÂÎç04:54:19
 */
public class MonitorLogFileReader extends AbstractFileReader implements FetchData {
	

	
	public MonitorLogFileReader(File filePath){
		super(filePath);
	}
	
	
	public void doFetchData(ReaderRecord readerRecord){		
		try {
			AnalyseBufferedReader reader = new AnalyseBufferedReader(new FileReader(this.getFilePath()),this.getRecordSeparator());			
			String line = null;			
			while((line=reader.readLine())!=null){				
				String[] datas = StringUtils.splitPreserveAllTokens(line, this.getFieldSeparator());
				readerRecord.doReaderRecord(datas,doFetchTime(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
