
package com.taobao.monitor.stat.newanalyse;

import java.util.List;

import org.dom4j.DocumentException;

import com.taobao.monitor.stat.newanalyse.config.AnalyseConfig;
import com.taobao.monitor.stat.newanalyse.impl.AnalyseFileLog;

/**
 * 
 * @author xiaodu
 * @version 2010-5-11 обнГ12:00:06
 */
public class AnalyseContent {
	
	private AnalyseConfig  analyseConfig = null;
	
	public AnalyseContent(){
		try {
			analyseConfig = new AnalyseConfig("");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void startup(){		
		List<AnalyseFileLog> configList = analyseConfig.getAnalyseFileLogList();
		
	}
	

}
