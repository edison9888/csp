
package com.taobao.monitor.stat.newanalyse;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-5-10 обнГ05:00:39
 */
public interface FetchData {
	
	
	public void doFetchData(ReaderRecord readerRecord);	
	
	public interface ReaderRecord{
		public void doReaderRecord(String[] datas,Date collectTime);
	}

}
