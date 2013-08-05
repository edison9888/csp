//
//package com.taobao.monitor.stat.db.impl;
//
//import java.sql.Date;
//import java.sql.ResultSet;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.monitor.common.db.base.MysqlRouteBase;
//import com.taobao.monitor.common.util.Constants;
//
///**
// * 
// * @author xiaodu
// * @version 2010-4-7 上午09:23:46
// */
//public class SelfMonitorDataDao extends MysqlRouteBase{
//	
//	
//	private static  Logger log = Logger.getLogger(SelfMonitorDataDao.class);
//
//	public SelfMonitorDataDao() {
//	}
//	
//	
//	
//	//LOAD/ memory_used/ memory_free swap_used  : 
//	//select no.name1,ns.display_name,sc.start_time,sc.output from nagios_hostgroup_members hm,
//	//nagios_objects no,nagios_services ns,nagios_servicechecks sc ,nagios_hostgroups hg
//	//where  hg.alias like '%item.cm3%' and  hm.hostgroup_id = hg.hostgroup_id and hm.host_object_id = no.object_id     
//	//and hm.host_object_id = ns.host_object_id     and ns.display_name = 'MEM_CPU_LOAD'     
//	//and ns.service_object_id = sc.service_object_id     and sc.instance_id = 3     
//	//and sc.start_time between '2010-04-04 21:00:00' and '2010-04-04 22:00:00';
//	//
//	/**
//	 * 2、 qps/rtime ： 
//	 * select     no.name1,     ns.display_name,     
//	 * sc.start_time,     sc.output from     nagios_hostgroup_members hm,     
//	 * nagios_objects no,     nagios_services ns,     nagios_servicechecks sc , 
//	 * nagios_hostgroups hg where  hg.alias like '%item.cm3%' and  
//	 * hm.hostgroup_id = hg.hostgroup_id and hm.host_object_id = no.object_id     
//	 * and hm.host_object_id = ns.host_object_id     and ns.display_name = 'QPS_RESPTIME'     
//	 * and ns.service_object_id = sc.service_object_id     and sc.instance_id = 3     
//	 * and sc.start_time between '2010-04-04 21:00:00' and '2010-04-04 22:00:00';
//	 * 
//	 * 
//	 * | detail62.cm3 | MEM_CPU_LOAD | 2010-04-04 21:00:11 | OK! CPU:18% IOWAIT:0%  Memory(4G):52% Swap(2G):1%  Load: 1.38, 1.46, 1.53 |
//	 * 
//	 * | detail59.cm3 | QPS_RESPTIME | 2010-04-04 21:59:26 | OK! Current QPS:13, Last 3 times(avg):4: Current Response Time:103(ms), Last 3 times(avg):97(ms)          |
//	 */
//	
//	
//	public Map<String,Float> findAppPeakValueAverage(List<String> groupList,String collectTime){
//		
//		
//		String startTime = collectTime+" 20:30:00";
//		String endTime = collectTime+" 22:30:00";
//		
//		String sqlIn = formatArray2Sqlin(groupList);
//		
//		String sql = "select sc.output from nagios_hostgroup_members hm,nagios_objects no, " +
//				" nagios_services ns,nagios_servicechecks sc , nagios_hostgroups hg where " +
//				" hg.alias in ("+sqlIn+") and  hm.hostgroup_id = hg.hostgroup_id " +
//				" and hm.host_object_id = no.object_id and hm.host_object_id = ns.host_object_id " +
//				" and ns.display_name = 'MEM_CPU_LOAD' and ns.service_object_id = sc.service_object_id " +
//				" and sc.instance_id in (3,7) and sc.start_time between ? and ?";
//		
//		final Map<String, List<Float>> cacheMap = new HashMap<String, List<Float>>();
//		
//		try {
//			this.query(sql, new Object[]{startTime,endTime}, new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					String value = rs.getString("output");
//					patternValue(value,cacheMap);
//					
//				}});
//		} catch (Exception e) {
//			log.error("", e);
//		}		
//		
//		String sql2 = "select sc.output from " +
//				" nagios_hostgroup_members hm,     nagios_objects no,     " +
//				" nagios_services ns,     nagios_servicechecks sc , nagios_hostgroups hg where  " +
//				" hg.alias in ("+sqlIn+") and  hm.hostgroup_id = hg.hostgroup_id and hm.host_object_id = no.object_id     " +
//				" and hm.host_object_id = ns.host_object_id     and ns.display_name = 'QPS_RESPTIME'     " +
//				" and ns.service_object_id = sc.service_object_id     and sc.instance_id in (3,7)    " +
//				" and sc.start_time between ? and ?";
//		
//		//OK! Current QPS:13, Last 3 times(avg):4: Current Response Time:103(ms), Last 3 times(avg):97(ms)
//		try {
//			this.query(sql2, new Object[]{startTime,endTime}, new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					String value = rs.getString("output");					
//					patternValue2("QPS",value,cacheMap);
//					patternValue2("ResT",value,cacheMap);
//					
//				}});
//		} catch (Exception e) {
//			log.error("", e);
//		}
//		
//		Map<String,Float> map = new HashMap<String, Float>();		
//		Iterator<Map.Entry<String, List<Float>>> it = cacheMap.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<String, List<Float>> entry = it.next();
//			String key = entry.getKey();
//			
//			if("ResT".equals(key)){				
//				List<Float> valueList = entry.getValue();
//				float _error_i=0;
//				float _error_c = 0f;
//				float _c = 0;
//				float _i = 0;
//				for(Float f:valueList){
//					if(f>1000){
//						_error_i++;
//						_error_c+=f;
//					}else{
//						_c+=f;
//						_i++;
//					}
//				}
//				
//				if(_error_i*100/valueList.size()>5){
//					if(valueList.size()>0)
//						map.put(entry.getKey(), (_c+_error_c)/valueList.size());
//					else
//						map.put(entry.getKey(), 0f);
//				}else{
//					if(_i>0){
//						map.put(entry.getKey(), _c/_i);
//					}else{
//						map.put(entry.getKey(), 0f);
//					}
//				}
//				
//				
//				
//			}else{
//				float _c = 0;
//				for(Float _v:entry.getValue()){
//					_c+=_v;				
//				}
//				if(entry.getValue().size()>0)
//					map.put(entry.getKey(), _c/entry.getValue().size());
//				else
//					map.put(entry.getKey(), 0f);
//			}
//		}
//		return map;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	/**
//	 * 将数据全部取出
//	 * @param app
//	 * @param collectTime yyyy-MM-dd HH:mm
//	 * @return
//	 */
//	public Map<String,Map<String, Double>> findAppLoadCupAverage2(List<String> groupList,String collectTime){	
//		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");		
//		String startTime = collectTime+" 00:00:00";
//		String endTime = collectTime+" 23:59:59";		
//		String sqlIn = formatArray2Sqlin(groupList);		
//		String sql = "select sc.output,sc.end_time from nagios_hostgroup_members hm,nagios_objects no, " +
//				" nagios_services ns,nagios_servicechecks sc , nagios_hostgroups hg where " +
//				" hg.alias in ("+sqlIn+") and  hm.hostgroup_id = hg.hostgroup_id " +
//				" and hm.host_object_id = no.object_id and hm.host_object_id = ns.host_object_id " +
//				" and ns.display_name = 'MEM_CPU_LOAD' and ns.service_object_id = sc.service_object_id " +
//				" and sc.instance_id in (3,7) and sc.start_time between ? and ?";
//		
//		final Map<String,Map<String, InnerCount>> innerLoadCpuMap = new HashMap<String, Map<String, InnerCount>>(); 
//		
//		final Pattern pattern = Pattern.compile("([\\w\\(\\)]+):([\\d,\\s\\.]+)%?");	
//		
//		
//		try {
//			this.query(sql, new Object[]{startTime,endTime}, new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					try{
//						String line = rs.getString("output");
//						String end_time = rs.getString("end_time");
//						String mapKey = end_time.substring(0, 16);					
//						Map<String, InnerCount> countMap = innerLoadCpuMap.get(mapKey);
//						if(countMap==null){
//							countMap = new HashMap<String, InnerCount>();
//							innerLoadCpuMap.put(mapKey, countMap);
//						}
//						Matcher matcher = pattern.matcher(line);		
//						while(matcher.find()){
//							String key = matcher.group(1);
//							String value = matcher.group(2);						
//							InnerCount inner = countMap.get(key);						
//							if(inner==null){
//								inner = new InnerCount();
//								countMap.put(key, inner);
//							}
//							
//							if("Load".equals(key)){
//								String[] _v = value.replaceAll(" ", "").split(",");
//								double _f = 0;
//								for(String v:_v){
//									Double _n = Double.parseDouble(v);
//									if(_f<_n){
//										_f=_n;
//									}
//								}				
//								inner.count+=_f;
//								inner.size++;
//							}else{
//								inner.count+=Double.parseDouble(value);
//								inner.size++;
//							}
//						}
//					}catch (Exception e) {
//						log.error("", e);
//					}					
//				}});
//		} catch (Exception e) {
//			log.error("", e);
//		}		
//		
//		Map<String,Map<String, Double>> result = new HashMap<String, Map<String,Double>>();
//		
//		Iterator<Map.Entry<String,Map<String, InnerCount>>> it = innerLoadCpuMap.entrySet().iterator();
//		while(it.hasNext()){			
//			Map.Entry<String,Map<String, InnerCount>> entry = it.next();
//			String time = entry.getKey();			
//			Map<String, InnerCount> v = entry.getValue();
//			
//			Map<String, Double> rMap = result.get(time);
//			if(rMap==null){
//				rMap = new HashMap<String, Double>();
//				result.put(time, rMap);
//			}
//			
//			Iterator<Map.Entry<String, InnerCount>> vit = v.entrySet().iterator();			
//			while(vit.hasNext()){
//				Map.Entry<String, InnerCount> vEntry = vit.next();
//				String key = vEntry.getKey();
//				InnerCount count = vEntry.getValue();
//				
//				String outKey = "SELF_"+key+"_"+Constants.AVERAGE_USERTIMES_FLAG;				
//				if(count.size>0){					
//					rMap.put(outKey, count.count/count.size);
//				}				
//			}
//		}		
//		return result;
//	}
//	
//	
//	
//	
//	/**
//	 * 取得每分钟的QPS 和RT 的数据
//	 * @param app
//	 * @param collectTime
//	 * @return
//	 */
//	public Map<String,Map<String, Double>> findAppQpsRtAverage2(List<String> groupList,String collectTime){
//		
//		String sqlIn = formatArray2Sqlin(groupList);	
//		String startTime = collectTime+" 00:00:00";
//		String endTime = collectTime+" 23:59:59";	
//		
//		
//		final Map<String,InnerCount> innerQPsMap = new HashMap<String, InnerCount>(); 
//		final Map<String,InnerCount> innerRtMap = new HashMap<String, InnerCount>(); 
//		
//		
//		final Pattern qpspattern = Pattern.compile("(QPS):([\\d]+)[^\\d]");
//		
//		final Pattern rtpattern = Pattern.compile("(ResT):([\\d]+)[^\\d]");
//		
//		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");	
//		
//		String sql2 = "select sc.output,sc.end_time from " +
//				" nagios_hostgroup_members hm,     nagios_objects no,     " +
//				" nagios_services ns,     nagios_servicechecks sc , nagios_hostgroups hg where  " +
//				" hg.alias in ("+sqlIn+") and  hm.hostgroup_id = hg.hostgroup_id and hm.host_object_id = no.object_id     " +
//				" and hm.host_object_id = ns.host_object_id     and ns.display_name = 'QPS_RESPTIME'     " +
//				" and ns.service_object_id = sc.service_object_id     and sc.instance_id in (3,7)    " +
//				" and sc.start_time between ? and ?";
//		
//		//OK! Current QPS:13, Last 3 times(avg):4: Current Response Time:103(ms), Last 3 times(avg):97(ms)
//		try {
//			this.query(sql2, new Object[]{startTime,endTime}, new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					try{
//						String line = rs.getString("output");
//						String end_time = rs.getString("end_time");	
//						
//						String mapKey = end_time.substring(0, 16);
//						
//						InnerCount qps = innerQPsMap.get(mapKey);
//						if(qps==null){
//							qps = new InnerCount();
//							innerQPsMap.put(mapKey, qps);
//						}											
//						Matcher matcher1 = qpspattern.matcher(line);		
//						while(matcher1.find()){			
//							String value = matcher1.group(2);						
//							qps.count+=Double.parseDouble(value);
//							qps.size++;
//						}
//						
//						InnerCount rt =  innerRtMap.get(mapKey);
//						if(rt==null){
//							rt = new InnerCount();
//							innerRtMap.put(mapKey, rt);
//						}	
//						Matcher matcher2 = rtpattern.matcher(line);		
//						while(matcher2.find()){			
//							String value = matcher2.group(2);
//							rt.count+=Double.parseDouble(value);
//							rt.size++;
//						}
//					}catch (Exception e) {
//						log.error("", e);
//					}					
//				}});
//		} catch (Exception e) {
//			log.error("", e);
//		}
//		
//		
//		
//		
//		Map<String,Map<String, Double>> result = new HashMap<String, Map<String,Double>>();
//		
//		
//		Iterator<Map.Entry<String, InnerCount>> it = innerQPsMap.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<String, InnerCount> entry = it.next();
//			String time = entry.getKey();
//			InnerCount count =  entry.getValue();			
//			String outKey = "IN_QPS";
//			
//			Map<String, Double> rMap = result.get(time);
//			if(rMap==null){
//				rMap = new HashMap<String, Double>();
//				result.put(time, rMap);
//			}
//			if(count.size>0){					
//				rMap.put(outKey, count.count/count.size);
//			}
//		}
//		
//		
//		it = innerRtMap.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<String, InnerCount> entry = it.next();
//			String time = entry.getKey();
//			InnerCount count =  entry.getValue();			
//			String outKey = "IN_ResT";
//			
//			Map<String, Double> rMap = result.get(time);
//			if(rMap==null){
//				rMap = new HashMap<String, Double>();
//				result.put(time, rMap);
//			}
//			if(count.size>0){					
//				rMap.put(outKey, count.count/count.size);
//			}
//		}
//		
//		return result;
//	}
//	
//	/**
//	 *OK! Current QPS:13, Last 3 times(avg):4: Current Response Time:103(ms), Last 3 times(avg):97(ms)
//	
//	 * @param line
//	 * @return
//	 */
//	private void patternValue2(String key,String line,Map<String, List<Float>> cacheMap){
//		Pattern pattern = Pattern.compile("("+key+"):([\\d]+)[^\\d]");		
//		Matcher matcher = pattern.matcher(line);		
//		while(matcher.find()){			
//			String outKey = "IN_"+key;			
//			String value = matcher.group(2);
//			
//			List<Float> list = cacheMap.get(outKey);
//			if(list==null){
//				list = new ArrayList<Float>();
//				cacheMap.put(outKey, list);
//			}
//			list.add(Float.parseFloat(value));
//		}
//	}
//	
//	
//	
//	
//	/**
//	 * OK! CPU:18% IOWAIT:0%  Memory(4G):52% Swap(2G):1%  Load: 1.38, 1.46, 1.53
//	
//	 * @param line
//	 * @return
//	 */
//	private void patternValue(String line,Map<String, List<Float>> cacheMap){
//		Pattern pattern = Pattern.compile("([\\w\\(\\)]+):([\\d,\\s\\.]+)%?");		
//		Matcher matcher = pattern.matcher(line);		
//		while(matcher.find()){
//			String key = matcher.group(1);
//			String value = matcher.group(2);
//			
//			String outKey = "SELF_"+key+"_"+Constants.AVERAGE_USERTIMES_FLAG;
//			
//			List<Float> list = cacheMap.get(outKey);
//			
//			if(list==null){
//				list = new ArrayList<Float>();
//				cacheMap.put(outKey, list);
//			}
//			if("Load".equals(key)){
//				String[] _v = value.replaceAll(" ", "").split(",");
//				float _f = 0;
//				for(String v:_v){
//					float _n = Float.parseFloat(v);
//					if(_f<_n){
//						_f=_n;
//					}
//				}				
//				list.add(_f);
//			}else{
//				list.add(Float.parseFloat(value));
//			}
//		}
//	}
//	
//	/**
//	 * 
//	 * select count(*) from nagios_hostgroups nhg, 
//	 * nagios_hostgroup_members nhm, nagios_hosts nh, nagios_objects no ,
//	 * nagios_statehistory nsh where nhg.alias='item.cm3' and nhm.hostgroup_id = nhg.hostgroup_id
//	 *  and nhm.host_object_id = nh.host_object_id and nh.display_name = no.name1 and no.object_id = nsh.object_id
//	 * 
//	 * 
//	 * @param appName
//	 * @return
//	 */
//	public Map<String,Long> findAppAlarmCount(List<String> groupList,String collectDate){
//		
//		String sqlIn = formatArray2Sqlin(groupList);
//		
//		String sql = "select no.name1 host, no.name2 service, count(*) cnt " +
//				" from nagios_hostgroups nh,nagios_hostgroup_members hm,nagios_notifications nn," +
//				" nagios_objects no, nagios_services ss	where   nh.alias in ("+sqlIn+")  " +
//				" and nh.hostgroup_id = hm.hostgroup_id" +
//				"  and hm.host_object_id = ss.host_object_id   and ss.service_object_id = nn.object_id   " +
//				"  and nn.start_time between ? and ?" +
//				"   and nn.object_id = no.object_id	";
//		
//		
//		
//		final Map<String,Long> alarmCount = new HashMap<String, Long>();
//		
//		try {
//			this.query(sql, new Object[]{collectDate+" 00:00:00",collectDate+" 23:59:59"},new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					
//					String service = rs.getString("service");
//					
//					long c = rs.getLong("cnt");
//					
//					Long count = alarmCount.get("ALARM_"+service);
//					if(count==null){
//						alarmCount.put("ALARM_"+service, c);
//					}else{
//						alarmCount.put("ALARM_"+service, c+count);
//					}
//				}});
//		} catch (Exception e) {
//			log.error("", e);
//		}		
//		
//		return alarmCount;
//	}
//	
//
//	public  String formatArray2Sqlin(List<String> args ){
//		
//		if(args!=null&&args.size()>0){
//			StringBuilder sb = new StringBuilder();
//			for(int i=0;i<args.size();i++){
//				String v = args.get(i);
//				sb.append("'"+v+"'");
//				if(i<args.size()-1)
//					sb.append(",");					
//			}		
//			return sb.toString();
//		}
//		
//		return "";
//	}
//	
//	
//	
//	private class InnerCount{
//		private double count;
//		private long size;
//	}
//	
//
//}
