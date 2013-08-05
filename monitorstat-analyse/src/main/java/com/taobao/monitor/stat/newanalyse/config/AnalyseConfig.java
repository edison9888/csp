
package com.taobao.monitor.stat.newanalyse.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.stat.newanalyse.impl.AnalyseFileLog;
import com.taobao.monitor.stat.newanalyse.impl.SumBo;


/**
 * 
 * @author xiaodu
 * @version 2010-5-10 上午09:47:49
 */
public class AnalyseConfig {
	
	private static String SCHEDULE_XML_PATH="analyse_schedul.xml";
	
	private static final Logger logger =  Logger.getLogger(AnalyseConfig.class);
	
	private List<AnalyseFileLog> analyseFileLogList = new ArrayList<AnalyseFileLog>();
	
	
	public AnalyseConfig(String configPath) throws DocumentException{
		logger.info("读取配置文件路："+configPath);
		
		URL url = Utlitites.getResource(configPath);		
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(url.toString());			
		parse(document);
	}
	
	
	
	
	private void parse(Document document){
		
		List list = document.selectNodes("/analyse/logFile");
		
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Element host = (Element) iter.next();
			
			AnalyseFileLog analyseFileLog = new AnalyseFileLog();
			
			String logName =  host.attributeValue("logName");
			String appName =  host.attributeValue("appName");
			String recordSeparator =  host.attributeValue("recordSeparator");
			String fieldSeparator =  host.attributeValue("fieldSeparator");
			String fieldList =  host.attributeValue("fieldList");
			String indb =  host.attributeValue("indb");
			
			String collectTimePattern =  host.attributeValue("collectTimePattern");
			
			String collectTimeFormat =  host.attributeValue("collectTimeFormat");
			
			analyseFileLog.setAppName(appName);
			analyseFileLog.setLogName(logName);
			
			analyseFileLog.setRecordSeparator(recordSeparator.charAt(0));
			analyseFileLog.setFieldSeparator(fieldSeparator);
			analyseFileLog.setCollectTimeFormat(collectTimeFormat);
			analyseFileLog.setCollectTimePattern(collectTimePattern);
			
			String[] fields = fieldList.split(",");
			for(int i=0;i<fields.length;i++){
				String field = fields[i];				
				String[] _filed = field.split(":");				
				analyseFileLog.getFieldList().add(fields[i]);
				
				if(_filed.length==1){
					analyseFileLog.getFieldMap().put(_filed[0], "String");
					analyseFileLog.getFieldIndexMap().put(_filed[0], i);
				}else{
					analyseFileLog.getFieldMap().put(_filed[0], _filed[1]);
					analyseFileLog.getFieldIndexMap().put(_filed[0], i);
				}
				
			}
			
			
						
			List attributes = host.attributes();
			for(int i=0;i<attributes.size();i++){
				Attribute att = (Attribute)attributes.get(i);
				String attrName = att.getName();
				String attrValue = att.getValue();
				String[] actions = attrName.split("_");
				
				if("sum".equals(actions[0])){//OUT_HSF_${key1}_${key2}:key1
					analyseFileLog.getSumList().add(attrValue);
					
					SumBo bo = new SumBo();
					
					String[] _values = attrValue.split(":");
					bo.setIndexKeyName(_values[1]);
					bo.setOutKeyName(_values[0]);
					
					analyseFileLog.getSumMap().put(actions[1], bo);
				}
				if("average".equals(actions[0])){
					analyseFileLog.getAverageList().add(attrValue);
					analyseFileLog.getAverageMap().put(actions[1], attrValue);
				}
				if("averageMachine".equals(actions[0])){
					analyseFileLog.getAverageMachineList().add(attrValue);
					analyseFileLog.getAverageMachineMap().put(actions[1], attrValue);
				}				
			}
			
			
			String[] indbs =  indb.split(",");			
			for(String _indb:indbs){
				analyseFileLog.getIndbList().add(_indb);
			}
			
			
			analyseFileLogList.add(analyseFileLog);
		}
		
		
	}




	public List<AnalyseFileLog> getAnalyseFileLogList() {
		return analyseFileLogList;
	}




	public void setAnalyseFileLogList(List<AnalyseFileLog> analyseFileLogList) {
		this.analyseFileLogList = analyseFileLogList;
	}
	

}
