package com.taobao.csp.monitor.impl.analyse.tair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.csp.monitor.impl.analyse.TimeUtil;

public class TairProviderLogJob extends AbstractDataAnalyse {
	private static Logger logger = Logger.getLogger(TairProviderLogJob.class);
	private Map<Long, Map<String, JSONObject>> map = new HashMap<Long, Map<String, JSONObject>>();
	private Map<Long,JSONObject> totalMap = new HashMap<Long,JSONObject>();

	public TairProviderLogJob(String appName,String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String line) {
		long time = TimeUtil.converMinuteTime(new Date());
		
		JSONObject json = JSONObject.fromObject(line);
		JSONArray jsonArray = json.getJSONArray("root");
		Map<String,JSONObject> jsonMap = map.get(time);
		if(jsonMap == null){
			jsonMap = new HashMap<String,JSONObject>();
			map.put(time, jsonMap);
		}
		for(Integer i=0;i<jsonArray.size()-1;i++){
			JSONObject jsonObject = (JSONObject)jsonArray.get(i);
			jsonMap.put(jsonObject.getString("nodeidentifer"), jsonObject);
		}
		JSONObject total = (JSONObject)jsonArray.getJSONObject(jsonArray.size()-1);
		totalMap.put(time, total);
	}

	@Override
	public void submit() {
		for(Map.Entry<Long, Map<String,JSONObject>> entry : map.entrySet()){
			long time = entry.getKey();
			for(Map.Entry<String, JSONObject> entry2 : entry.getValue().entrySet()){
				List<String> keylist = new ArrayList<String>();
				List<Object> valuelist = new ArrayList<Object>();
				List<ValueOperate> oplist = new ArrayList<ValueOperate>();
				String ip = entry2.getKey().substring(0, entry2.getKey().lastIndexOf(":"));
				JSONObject jsonObject = entry2.getValue();
				fillResult(jsonObject,keylist,valuelist,oplist);
				try {
					CollectDataUtilMulti.collect(getAppName(), ip, time, new String[]{KeyConstants.TAIR_PROVIDER}, new KeyScope[]{KeyScope.HOST}, keylist.toArray(new String[]{}), valuelist.toArray(),oplist.toArray(new ValueOperate[]{}));
				} catch (Exception e1) {
					logger.error("∑¢ÀÕ ß∞‹", e1);
				}
			}
		}
	}
	
	private void fillResult(JSONObject jsonObject,List<String> keylist,List<Object> valuelist,List<ValueOperate> oplist){
		Integer aliveCount =0;
		String stat = jsonObject.getString("nodestat");
		if(stat.equals("alive"))aliveCount=1;
		keylist.add("aliveCount");
		valuelist.add(aliveCount);
		oplist.add(ValueOperate.REPLACE);
		Long dataSize = jsonObject.getLong("dataSize");
		keylist.add("dataSize");
		valuelist.add(dataSize);
		oplist.add(ValueOperate.REPLACE);
		Double delay= jsonObject.getDouble("delay");
		keylist.add("delay");
		valuelist.add(delay);
		oplist.add(ValueOperate.REPLACE);
		Long evictCount = jsonObject.getLong("evictCount");
		keylist.add("evictCount");
		valuelist.add(evictCount);
		oplist.add(ValueOperate.REPLACE);
		Long getCount = jsonObject.getLong("getCount");
		keylist.add("getCount");
		valuelist.add(getCount);
		oplist.add(ValueOperate.REPLACE);
		Double hitRate = jsonObject.getDouble("hitRate");
		keylist.add("hitRate");
		valuelist.add(hitRate);
		oplist.add(ValueOperate.REPLACE);
		Long putCount = jsonObject.getLong("putCount");
		keylist.add("putCount");
		valuelist.add(putCount);
		oplist.add(ValueOperate.REPLACE);
		Long hitCount = jsonObject.getLong("hitCount");
		keylist.add("hitCount");
		valuelist.add(hitCount);
		oplist.add(ValueOperate.REPLACE);
		Long itemCount= jsonObject.getLong("itemCount");
		keylist.add("itemCount");
		valuelist.add(itemCount);
		oplist.add(ValueOperate.REPLACE);
		Long removeCount = jsonObject.getLong("removeCount");
		keylist.add("removeCount");
		valuelist.add(removeCount);
		oplist.add(ValueOperate.REPLACE);
	}
	@Override
	public void release() {
		map.clear();
	}

	public static void main(String args[]) throws Exception {
		
		TairProviderLogJob job = new TairProviderLogJob("","","");
		job.analyseOneLine("{totalproperty:8,root:[  {nodeidentifer:'172.24.142.20:5191',nodestat:'alive',delay:0.41 ,dataSize:957336283,evictCount:142,getCount:10790,hitRate:0.78 ,putCount:3113,hitCount:8392,itemCount:3536225,removeCount:98,useSize:1165212968},{nodeidentifer:'172.24.165.33:5191',nodestat:'alive',delay:0.45 ,dataSize:1026982054,evictCount:58,getCount:11149,hitRate:0.78 ,putCount:178,hitCount:8704,itemCount:4218771,removeCount:100,useSize:1271111600},{nodeidentifer:'172.23.201.30:5191',nodestat:'alive',delay:0.71 ,dataSize:956137379,evictCount:159,getCount:11779,hitRate:0.82 ,putCount:5005,hitCount:9665,itemCount:3535494,removeCount:97,useSize:1163861152},{nodeidentifer:'172.24.166.17:5191',nodestat:'alive',delay:0.5 ,dataSize:1027562400,evictCount:73,getCount:10062,hitRate:0.8 ,putCount:229,hitCount:8069,itemCount:4216014,removeCount:96,useSize:1271694240},{nodeidentifer:'172.23.201.20:5191',nodestat:'alive',delay:0.6 ,dataSize:954269257,evictCount:104,getCount:10925,hitRate:0.79 ,putCount:3687,hitCount:8602,itemCount:3536803,removeCount:91,useSize:1162048416},{nodeidentifer:'172.24.169.17:5191',nodestat:'alive',delay:0.49 ,dataSize:1029749882,evictCount:70,getCount:10897,hitRate:0.79 ,putCount:209,hitCount:8643,itemCount:4222480,removeCount:93,useSize:1274250920},{nodeidentifer:'172.24.162.17:5191',nodestat:'alive',delay:0.45 ,dataSize:1027419483,evictCount:85,getCount:11416,hitRate:0.79 ,putCount:214,hitCount:9075,itemCount:4219400,removeCount:91,useSize:1271782496},{nodeidentifer:'172.24.184.2:5191',nodestat:'alive',delay:0.56 ,dataSize:947711082,evictCount:160,getCount:11424,hitRate:0.77 ,putCount:3451,hitCount:8827,itemCount:3507036,removeCount:96,useSize:1153805872},{nodeidentifer:'total',nodestat:'virtual',area:1024,dataSize:7927167820,evictCount:851,getCount:88442,hitRate:0.79 ,putCount:16086,hitCount:69977,itemCount:30992223,removeCount:762,useSize:9733767664,quota:-1}]}");
		job.submit();
	}
}
