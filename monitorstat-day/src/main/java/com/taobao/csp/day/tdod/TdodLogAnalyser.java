package com.taobao.csp.day.tdod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.Log;

public class TdodLogAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(TdodLogAnalyser.class);
	
	private Map<String, Integer> cache = new HashMap<String, Integer>();
	
	public TdodLogAnalyser(String appName, HostInfo hostInfo, char lineSplit) {
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
			/**
			 * 2012/10/22 00:14:17 [info] 10665#0: [Shopsystem] workmode: checkcode, cookie: , address: 203.208.60.186, url: www.zkj.cc/do/job.php, PassMode: 0, phase: blacklist
			 */
			String[] args = line.split(",");
			if (args.length ==6){
				String PassMode = args[4].trim();
				if (PassMode.endsWith("0")) { //TMD server端开启拦截.
					String time = args[0].trim().substring(0,16) + ":00";
					if (!passFilterCollectTime(time)) {
						return;
					}
					
					String workMode = args[0].substring(args[0].lastIndexOf(':') + 1).trim();
					if (workMode.equals("punish") || workMode.equals("checkcode") || workMode.equals("hourglass")) {
						String key = getAppName() + "$$" + time;
						Integer blockCount = cache.get(key);
						if (blockCount == null) {
							cache.put(key, 1);
						} else{
							cache.put(key, blockCount + 1);
						}					
					}
				} else {
					//TMD 观察者模式或其他
					return;
				}
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
			
			TdodLog po = new TdodLog();
			String [] fields  = key.split("\\$\\$");
			if (fields.length == 2) {
				po.setAppName(fields[0]);
				po.setCollectTime(fields[1]);
			}
			po.setBlockCount(value);
			
			list.add(po);
		}
		
		return list;
	}
	
	private boolean passFilterCollectTime(String collectTime) {
		boolean pass = false;
		if (collectTime != null && collectTime.compareTo("2012/11/10 23:30:00") >=0 
				&& collectTime.compareTo("2012/11/11 23:59:59") <= 0) {
			pass = true;
		}
		
		return pass;
	}
	
}
