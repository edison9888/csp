
package com.taobao.monitor.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.KeyValuePo;



/**
 * 
 * @author xiaodu
 * @version 2010-1-22 下午05:17:08
 */
public class AmLineFlash {
	
	
	private static  Logger log = Logger.getLogger(AmLineFlash.class);
	
	public static String createCharXml(Map<String, List<KeyValuePo>> map) throws ParseException{
		return createCharXml(map,true);
	}
	
	/**
	 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
	 * @param map key HH:mm结构时间，value 值
	 * @return amline 的xml 格式数据
	 * @throws ParseException
	 */
	public static String createCommonCharXml(Map<String,Map<String, Double>> map) throws ParseException{
		
		if(map.size()<1)return "";
		StringBuffer sb =  new StringBuffer("<chart>");			
		Set<String> timeSet = new HashSet<String>();
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){
			Map<String, Double> tmp = entry.getValue();
			timeSet.addAll(tmp.keySet());			
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(timeSet);
		Collections.sort(dateList,new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				
				int time1 = Integer.parseInt(o1.replaceAll(":", ""));
				int time2 = Integer.parseInt(o2.replaceAll(":", ""));
				
				if(time1>time2){
					return 1;
				}else if(time1<time2){
					return -1;
				}
				
				return 0;
			}});
		
		sb.append("<series>");
		for(int i=0;i<dateList.size();i++){			
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		int index=1;
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){			
			sb.append("<graph gid='"+index+"' title='"+entry.getKey()+"'>");				
			Map<String, Double> poMap = entry.getValue();			
			int allSize = poMap.size();	
			int renderSize = 0;
			double tmp = 0;
			for(int i=0;i<dateList.size();i++){
				String key = dateList.get(i);				
				Double label = poMap.get(key);
				if(label!=null){
					renderSize++;
					if(renderSize<allSize-1){//最后一个不要
						sb.append("<value xid='"+i+"'>"+label+"</value>");	
					}				
					tmp =label;					
				}else{
					sb.append("<value xid='"+i+"'>"+tmp+"</value>");
				}
				if(renderSize>=allSize){
					break;
				}
				
			}	
			sb.append("</graph>");
			index++;
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
	
	
	
	
	/**
	 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
	 * @param map key HH:mm结构时间，value 值
	 * @return amline 的xml 格式数据
	 * @throws ParseException
	 */
	public static String createCommonCharXmlExt(Map<String,Map<String, Double>> map) throws ParseException{
		
		if(map.size()<1)return "";
		StringBuffer sb =  new StringBuffer("<chart>");			
		Set<String> timeSet = new HashSet<String>();
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){
			Map<String, Double> tmp = entry.getValue();
			timeSet.addAll(tmp.keySet());			
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(timeSet);
		Collections.sort(dateList,new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				
				int time1 = Integer.parseInt(o1.replaceAll(":", ""));
				int time2 = Integer.parseInt(o2.replaceAll(":", ""));
				
				if(time1>time2){
					return 1;
				}else if(time1<time2){
					return -1;
				}
				
				return 0;
			}});
		
		sb.append("<series>");
		for(int i=0;i<dateList.size();i++){			
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		int index=1;
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){			
			sb.append("<graph gid='"+index+"' title='"+entry.getKey()+"'>");				
			Map<String, Double> poMap = entry.getValue();			
			int allSize = poMap.size();	
			int renderSize = 0;
			Double tmp = new Double(0);
			for(int i=0;i<dateList.size();i++){
				String key = dateList.get(i);				
				Double label = poMap.get(key);
				if(label!=null){
					renderSize++;
					if(renderSize<allSize-1){//最后一个不要
						sb.append("<value xid='"+i+"'>"+Math.round(label)+"</value>");	
					}				
					tmp =label;					
				}else{
					sb.append("<value xid='"+i+"'>"+Math.round(tmp)+"</value>");
				}
				if(renderSize>=allSize){
					break;
				}
				
			}	
			sb.append("</graph>");
			index++;
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
	
	
	/**
	 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
	 * @param map key HH:mm结构时间，value 值
	 * @return amline 的xml 格式数据
	 * @throws ParseException
	 */
	public static String createCommonCharXml1(Map<String,Map<String, Double>> map) throws ParseException{
		
		if(map.size()<1)return "";
		StringBuffer sb =  new StringBuffer("<chart>");			
		Set<String> timeSet = new HashSet<String>();
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){
			Map<String, Double> tmp = entry.getValue();
			timeSet.addAll(tmp.keySet());			
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(timeSet);
		Collections.sort(dateList,new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				
				int time1 = Integer.parseInt(o1.replaceAll(":", ""));
				int time2 = Integer.parseInt(o2.replaceAll(":", ""));
				
				if(time1>time2){
					return 1;
				}else if(time1<time2){
					return -1;
				}
				
				return 0;
			}});
		
		sb.append("<series>");
		for(int i=0;i<dateList.size();i++){			
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		int index=1;
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){			
			sb.append("<graph gid='"+index+"' title='"+entry.getKey()+"'>");				
			Map<String, Double> poMap = entry.getValue();			
			int renderSize = 0;
			double tmp = 0;
			for(int i=0;i<dateList.size();i++){
				String key = dateList.get(i);				
				Double label = poMap.get(key);
				if(label!=null){
					renderSize++;
					sb.append("<value xid='"+i+"'>"+label+"</value>");		
					tmp =label;					
				}else{
					sb.append("<value xid='"+i+"'>"+tmp+"</value>");
				}								
			}	
			sb.append("</graph>");
			index++;
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
	
	
		
		/**
		 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
		 * @param map  key 为名称 value 为数据
		 * @return
		 * @throws ParseException 
		 */
		public static String createCharXml(Map<String, List<KeyValuePo>> map,boolean type) throws ParseException{
			
			if(map.size()<1)return "";
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			Map<String,Map<String, Double>> newmap = new HashMap<String,Map<String, Double>>();
			
			for(Map.Entry<String, List<KeyValuePo>> entry:map.entrySet()){
				
				Map<String, Double> keyValueMap = newmap.get(entry.getKey());
				if(keyValueMap==null){
					keyValueMap = new HashMap<String, Double>();
					newmap.put(entry.getKey(), keyValueMap);
				}
				
				List<KeyValuePo> poList =entry.getValue();
				for(KeyValuePo po:poList){
					String time = sdf.format(po.getCollectTime());
					keyValueMap.put(time, Double.parseDouble(po.getValueStr()));
				}
				
			}
			return createCommonCharXml(newmap);
		}
		
		public static String createCharXml1(Map<String, List<KeyValuePo>> map,boolean type) throws ParseException{
			
			if(map.size()<1)return "";
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			Map<String,Map<String, Double>> newmap = new HashMap<String,Map<String, Double>>();
			
			for(Map.Entry<String, List<KeyValuePo>> entry:map.entrySet()){
				
				Map<String, Double> keyValueMap = newmap.get(entry.getKey());
				if(keyValueMap==null){
					keyValueMap = new HashMap<String, Double>();
					newmap.put(entry.getKey(), keyValueMap);
				}
				
				List<KeyValuePo> poList =entry.getValue();
				for(KeyValuePo po:poList){
					String time = sdf.format(po.getCollectTime());
					keyValueMap.put(time, Double.parseDouble(po.getValueStr()));
				}
				
			}
			return createCommonCharXml1(newmap);
		}		
		
		/**
		 * 用于构建一天内 以天为单位的X轴坐标走视图
		 * @param map  key 为名称 value 为数据
		 * @return
		 * @throws ParseException 
		 */
		public static String createCharDateXml(Map<String, List<KeyValuePo>> map) throws ParseException{
			return createCharXml(map,true);
		}
		
		
		/**
		 * 
		 * @param map
		 * @return
		 */
		public static String createPieXml(Map<String, Integer> map){
			
			StringBuilder sb = new StringBuilder();
			sb.append("<pie>");
			for(Map.Entry<String, Integer> entry:map.entrySet()){
				sb.append("<slice title='"+entry.getKey()+"'>"+entry.getValue()+"</slice>");
			}
			sb.append("</pie>");
			return sb.toString();
		}
			
		public static String createCharXml(String time1,List<KeyValuePo> keyValueList1,String time2,List<KeyValuePo> keyValueList2) throws ParseException{
			
			Map<String, List<KeyValuePo>> map = new HashMap<String, List<KeyValuePo>>();
			if(time1!=null&&keyValueList1!=null&&keyValueList1.size()>0){
				map.put(time1, keyValueList1);
			}
			if(time2!=null&&keyValueList2!=null&&keyValueList2.size()>0){
				map.put(time2, keyValueList2);
			}
			
			return createCharXml(map);
		}
	
	
		public static String createCharXml(List<KeyValuePo> keyValueList){
			Collections.sort(keyValueList);
			StringBuffer head =  new StringBuffer("<chart>");
			String cat = createCharCategories(keyValueList);
			head.append(cat);
			String dataSet = createDataSet(keyValueList);
			head.append(dataSet);		
			head.append("</chart>");
			return head.toString();
		}
		
		private static String createDataSet(List<KeyValuePo> keyValueList){
		
		StringBuilder sb = new StringBuilder();
		sb.append("<graphs>");
		sb.append("<graph gid='0' title='走势图'>");			
		for(int i=0;i<keyValueList.size();i++){
			KeyValuePo label = keyValueList.get(i);
			sb.append("<value xid='"+i+"'>"+label.getValueStr()+"</value>");
		}	
		sb.append("</graph>");	
		sb.append("</graphs>");
		return sb.toString();
	}
	
	
	
	private static String createCharCategories(List<KeyValuePo> keyValueList){
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("<series>");
		
		for(int i=0;i<keyValueList.size();i++){
			KeyValuePo po = keyValueList.get(i);
			sb.append("<value xid='"+i+"'>"+sdf.format(po.getCollectTime())+"</value>");			
		}
		sb.append("</series>");
		return sb.toString();
	}
	
	
	

	
	
	/**
	 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
	 * @param map key HH:mm结构时间，value 值
	 * @return amline 的xml 格式数据
	 * @throws ParseException
	 */
	public static String createCommonCharXm2(Map<String,Map<String, Long>> map) throws ParseException{
		
		if(map.size()<1)return "";
		StringBuffer sb =  new StringBuffer("<chart>");			
		Set<String> timeSet = new HashSet<String>();
		for(Map.Entry<String,Map<String, Long>> entry:map.entrySet()){
			Map<String, Long> tmp = entry.getValue();
			timeSet.addAll(tmp.keySet());			
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(timeSet);
		Collections.sort(dateList,new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				
				int time1 = Integer.parseInt(o1.replaceAll(":", ""));
				int time2 = Integer.parseInt(o2.replaceAll(":", ""));
				
				if(time1>time2){
					return 1;
				}else if(time1<time2){
					return -1;
				}
				
				return 0;
			}});
		
		sb.append("<series>");
		for(int i=0;i<dateList.size();i++){			
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		int index=1;
		for(Map.Entry<String,Map<String, Long>> entry:map.entrySet()){			
			sb.append("<graph gid='"+index+"' title='"+entry.getKey()+"'>");				
			Map<String, Long> poMap = entry.getValue();			
			int allSize = poMap.size();	
			int renderSize = 0;
			double tmp = 0;
			for(int i=0;i<dateList.size();i++){
				String key = dateList.get(i);				
				Long label = poMap.get(key);
				if(label!=null){
					renderSize++;
					sb.append("<value xid='"+i+"'>"+label+"</value>");			
					tmp =label;					
				}else{
					sb.append("<value xid='"+i+"'>"+tmp+"</value>");
				}
				if(renderSize>=allSize){
					break;
				}
				
			}	
			sb.append("</graph>");
			index++;
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
}
