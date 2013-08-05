package com.taobao.csp.day.gc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.HdfsFileOutputter;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.Log;
import com.taobao.csp.day.base.YuntiLogType;

public class GcLogAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(GcLogAnalyser.class);
	
	private long count = 0;
	
	public GcLogAnalyser(String appName, HostInfo hostInfo, char lineSplit) {
		super(appName, hostInfo, lineSplit);
	}
	
	@Override
	public List<Log> analyse(String segment) {
		logger.debug("analyse segment");
		List<Log> list = new ArrayList<Log>();
		
//		segment = this.getResidue() + segment;
//		int lastFlag = segment.lastIndexOf(String.valueOf(this.getLineSplit()));
//		String newResidue = new String(segment.substring(lastFlag + 1, segment.length()).toCharArray());
//		this.setResidue(newResidue);
//		segment = segment.substring(0, lastFlag + 1);
		
		if (StringUtils.isBlank(segment)) {
			return list;
		}

//		cache = new StringBuffer();
		long begin = System.currentTimeMillis();
		String [] lines = segment.split(String.valueOf(this.getLineSplit()));
		long medium = System.currentTimeMillis();
//		logger.info("split use time:" + (medium - begin) + ", size is:" + lines.length);
		
		for (String line : lines) {
			analyseOneLine(line);
			
			writeToYunti(line);
		}
		
		long end = System.currentTimeMillis();
//		logger.info("write yunti, lines:" + lines.length + ", use time:" + (end - medium));
		
		count = lines.length;
		
		list = generateLogPos(lines[0]);
		
		clearTrace();
		
		return list;
	}
	
	@Override
	public void analyseOneLine(String line) {
		
	}
	
	@Override
	public boolean isCurrentLog(String segment) {
		boolean isCurrentLog = true;
		
		return isCurrentLog;
	}
	
	private List<Log> generateLogPos(String line) {
		List<Log> list = new ArrayList<Log>();

		GcLog log = new GcLog();
		log.setAppName(this.getAppName());
		log.setCount(count);
		
		list.add(log);

		return list;
	}
	
	private void writeToYunti(String line) {
		HdfsFileOutputter.output(this.getAppName() + ":" + this.getHostInfo().toString(), YuntiLogType.gc, line);
	}
	
	private void clearTrace() {
		count = 0;
	}

}
