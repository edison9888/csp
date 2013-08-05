
/**
 * monitorstat-monitor
 */
package com.taobao.csp.dataserver.example;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mysql.jdbc.TimeUtil;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * 
 * TairClient 2.2.4^Aget/hit^A/172.24.13.28:5191$49^A25^A25^A2011-11-30 00:01:41^Aitem138076.cm4
 * 
 * delete/error/KEYTOLARGE
 * delete/exception
 * delete
 * 
 * get
 * get/error/KEYTOLARGE
 * get/hit
 * get/len
 * 
 * incr/error/ITEMSIZEERROR
 * 
 * addCount/error/NSERROR
 * addCount
 * addCount/exception
 * 
 * mdelete/error/NSERROR
 * mdelete/error/KEYTOLARGE
 * mdelete/error/PARTSUCC
 * mdelete
 * mdelete/exception
 * 
 * mget
 * mget/len
 * mget/hit
 * mget/error/PARTSUCC
 * 
 * 
 * put/error/INVALIDARG
 * put/exception
 * put/len
 * 
 * addItems/error/NSERROR
 * addItems/len
 * addItems/exception
 * 
 * 
 * getAndRemove/error/KEYTOLARGE
 * getAndRemove/hit
 * getAndRemove/len
 * 
 * getItems/hit
 * getItems/len
 * 
 * removeItems
 * 
 * getItemCount
 * 
 * @author xiaodu
 *
 * ÏÂÎç6:50:29
 */
public class TairConsumerLogJob {
	
	private static final Logger logger = Logger.getLogger(TairConsumerLogJob.class);
	
	private String appname = null;
	private String unknow = null;
	public TairConsumerLogJob(String appName,String ip){
		this.appname = appName;
		this.unknow = ip;
	} 
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	private Map<Long,Map<String,Map<String,Map<String,Map<String,Integer>>>>> timeTairMap = new HashMap<Long, Map<String,Map<String,Map<String,Map<String,Integer>>>>>();
	
	
	public void submit(){
		
		for(Map.Entry<Long,Map<String,Map<String,Map<String,Map<String,Integer>>>>> entry:timeTairMap.entrySet()){
			long time = entry.getKey();
			Map<String,Map<String,Map<String,Map<String,Integer>>>> groupName = entry.getValue();
			for(Map.Entry<String,Map<String,Map<String,Map<String,Integer>>>> groupEtrny:groupName.entrySet()){
				String groupname = groupEtrny.getKey();
				Map<String,Map<String,Map<String,Integer>>> nsMap = groupEtrny.getValue();
				for(Map.Entry<String,Map<String,Map<String,Integer>>> nsEntry:nsMap.entrySet()){
					String nsName = nsEntry.getKey();
					Map<String,Map<String,Integer>> actionMap = nsEntry.getValue();
					for(Map.Entry<String,Map<String,Integer>> actionEntry:actionMap.entrySet()){
						String action = actionEntry.getKey();
						Map<String,Integer> pMap = actionEntry.getValue();
						List<String> keylist = new ArrayList<String>();
						List<Object> valuelist = new ArrayList<Object>();
						List<ValueOperate> oplist = new ArrayList<ValueOperate>();
						Integer e = pMap.get("E-times");
						keylist.add("E-times");
						valuelist.add(e);
						oplist.add(ValueOperate.ADD);
						Integer r = pMap.get("C-time");
						keylist.add("C-time");
						valuelist.add(r/e);
						oplist.add(ValueOperate.AVERAGE);
						Integer len = pMap.get("P-size");
						if(len != null){
							keylist.add("P-size");
							valuelist.add(len/e);
							oplist.add(ValueOperate.AVERAGE);
						}
						Integer hit = pMap.get("sucRate");
						if(hit != null){
							keylist.add("sucRate");
							valuelist.add(((float)hit)/e);
							oplist.add(ValueOperate.REPLACE);
						}
						Integer exception = pMap.get("exception");
						if(exception != null){
							keylist.add("exception");
							valuelist.add(exception);
							oplist.add(ValueOperate.ADD);
						}
						
						try {
							CollectDataUtilMulti.collect(appname, unknow, time, new String[]{KeyConstants.TAIR_CONSUMER,groupname,nsName,action}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, keylist.toArray(new String[]{}), valuelist.toArray(),oplist.toArray(new ValueOperate[]{}));
						} catch (Exception e1) {
							logger.error("·¢ËÍÊ§°Ü", e1);
						}
						
					}
				}
			}
		}
		
		
	}
	
	
	public void analyseLine(String line) throws ParseException{
		
		String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
		
		if(logResult[0].indexOf("TairClient")==-1){
			return ;
		}
		
		String action = logResult[1];
		String tairInfo = logResult[2];
		int executes = Integer.parseInt(logResult[4]);
		int times =  Integer.parseInt(logResult[3]);
		String collectTime = logResult[5].substring(0, 16);;
		
		
		String[] infos = tairInfo.split("\\$");
		String namespace = "unknow";
		String groupName = "unknow";
		String ip="";
		if(infos.length == 2){
			ip = infos[0];
			namespace =  infos[1];
		}else if(infos.length == 3){
			ip = infos[0];
			namespace =  infos[1];
			groupName =  infos[2];
		}else{
			return ;
		}
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(rTimeFormat.parse(collectTime));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long time = cal.getTimeInMillis();
		
		
		Map<String,Map<String,Map<String,Map<String,Integer>>>> groupmap = timeTairMap.get(time);
		if(groupmap == null){
			groupmap = new HashMap<String, Map<String,Map<String,Map<String,Integer>>>>();
			timeTairMap.put(time, groupmap);
		}
		
		Map<String,Map<String,Map<String,Integer>>> nsmap = groupmap.get(groupName);
		if(nsmap == null){
			nsmap = new HashMap<String, Map<String,Map<String,Integer>>>();
			groupmap.put(groupName, nsmap);
		}
		
		Map<String,Map<String,Integer>> actionMap = nsmap.get(namespace);
		if(actionMap== null){
			actionMap = new HashMap<String, Map<String,Integer>>();
			nsmap.put(namespace, actionMap);
		}
		
		String[] a = action.split("/");
		String an = a[0];
		Map<String,Integer> values = actionMap.get(an);
		if(values == null){
			values = new HashMap<String,Integer>();
			actionMap.put(an, values);
		}
		
		if(a.length ==1){//
			Integer e = values.get("E-times");
			if(e == null){
				values.put("E-times", executes);
			}else{
				values.put("E-times", executes+e);
			}
			Integer r = values.get("C-time");
			if(r == null){
				values.put("C-time", times);
			}else{
				values.put("C-time", times+r);
			}
		}else if(a.length ==2){
			if(a[1].equals("P-size")){
				Integer r = values.get("P-size");
				if(r == null){
					values.put("P-size", times);
				}else{
					values.put("P-size", times+r);
				}
			}else
			
			if(a[1].equals("sucRate")){
				Integer r = values.get("sucRate");
				if(r == null){
					values.put("sucRate", times);
				}else{
					values.put("sucRate", times+r);
				}
			}else 
			if(a[1].equals("exception")){
				Integer r = values.get("exception");
				if(r == null){
					values.put("exception", times);
				}else{
					values.put("exception", times+r);
				}
			} 
		}
		
	}
	
	

	public static void main(String[] args){
		try {
			
			
			TairConsumerLogJob job = new TairConsumerLogJob("itemcenter","172.23.208.52");
			
			
			BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\work\\csp\\monitorstat-web\\target\\xiaodu_w"),"gbk"));
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\xiaodu"), '\02');
			String line = null;
			while((line=reader.readLine())!=null){
				
				writer.write(line);
				
				job.analyseLine(line);
				writer.newLine();
				
			}
			
			job.submit();
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	
}
