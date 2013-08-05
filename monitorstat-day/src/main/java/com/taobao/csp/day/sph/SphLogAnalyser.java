package com.taobao.csp.day.sph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.Log;

public class SphLogAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(SphLogAnalyser.class);
	
	private Map<String, Integer> cache = new HashMap<String, Integer>();
	
	public SphLogAnalyser(String appName, HostInfo hostInfo, char lineSplit) {
		super(appName, hostInfo, lineSplit);
	}
	
	@Override
	public List<Log> analyse(String segment) {
		logger.debug("analyse segment");
		segment = this.getResidue() + segment;
		int lastFlag = segment.lastIndexOf(String.valueOf(this.getLineSplit()));
		String newResidue = new String(segment.substring(lastFlag + 1, segment.length()).toCharArray());
		this.setResidue(newResidue);
		segment = segment.substring(0, lastFlag + 1);

		cache.clear();
		String [] lines = segment.split(String.valueOf(this.getLineSplit()));
		for (String line : lines) {
			analyseOneLine(line);
		}
		
		List<Log> list = generateLogPos();
		cache.clear();
		
		return list;
	}
	
	public void analyseOneLine(String line) {
		logger.debug("analyse line");
		try {
			String[] values = line.trim().split("\\s+");
			if(values.length < 3) {
				return;
			}
			
			String time = values[0] + " " + values[1];
			String blockKey = values[2].split("-")[0];
			String action = "undefined";
			if (values.length > 4) {
				action = values[3];
			}
			String minute = transferTimeToMinite(time);
			String key = minute + "$$" + this.getAppName() + "$$" + this.getHostInfo().getIp() + "$$" + blockKey + "$$" + action;
			if (cache.containsKey(key)) {
				int count = cache.get(key) + 1;
				cache.put(key, count);
			} else {
				cache.put(key, 1);
			}
			
		} catch (Exception e) {
			logger.error("analyse error", e);
		}
	}
	
	@Override
	public boolean isCurrentLog(String segment) {
		boolean isCurrentLog = true;
		
		return isCurrentLog;
	}
	
	private List<Log> generateLogPos() {
		List<Log> list = new ArrayList<Log>();
		for (Map.Entry<String, Integer> entry : cache.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			
			SphLog po = new SphLog();
			String [] fields  = key.split("\\$\\$");
			if (fields.length == 5) {
				po.setCollectTime(fields[0]);
				po.setAppName(fields[1]);
				po.setIp(fields[2]);
				po.setBlockKey(fields[3]);
				po.setAction(fields[4]);
			}
			po.setBlockCount(value);
			
			list.add(po);
		}
		
		return list;
	}
	
	private static String transferTimeToMinite(String time) throws Exception {
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-");
		SimpleDateFormat secondFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat miniteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		String month = monthFormat.format(Calendar.getInstance().getTime());
		String wholeTime = month + time;
		
		String minute = miniteFormat.format(secondFormat.parse(wholeTime));
		
		return minute;
	}
}
