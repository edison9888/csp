package com.taobao.csp.day.pinkie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.HdfsFileOutputter;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.Log;
import com.taobao.csp.day.base.YuntiLogType;
import com.taobao.csp.monitor.DataAnalyse;
import com.taobao.csp.monitor.util.CollcectDiamondUtil;
import com.taobao.csp.monitor.util.CollectFlag;

public class PinkieAccessLogAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(PinkieAccessLogAnalyser.class);
	
	private StringBuffer cache = null;;
	
	private String collectTime = null;
	private DataAnalyse analyse=null;
	private long count = 0;
	
	static{
		CollcectDiamondUtil.diamondCollectListener();
	}
	
	public PinkieAccessLogAnalyser(String appName, HostInfo hostInfo, char lineSplit) {
		super(appName, hostInfo, lineSplit);
//		analyse=new ApacheLogJob(appName, hostInfo.getIp(), "");
	}
	
	@Override
	public List<Log> analyse(String segment) {
		logger.debug("analyse segment");
		List<Log> list = new ArrayList<Log>();
		
		segment = this.getResidue() + segment;
		int lastFlag = segment.lastIndexOf(String.valueOf(this.getLineSplit()));
		if (lastFlag == -1) {
			this.setResidue(segment);
			return list;
		}
		
		String newResidue = new String(segment.substring(lastFlag + 1, segment.length()).toCharArray());
		this.setResidue(newResidue);
		segment = segment.substring(0, lastFlag + 1);
		
		if (StringUtils.isBlank(segment)) {
			return list;
		}

//		cache = new StringBuffer();
		String [] lines = segment.split(String.valueOf(this.getLineSplit()));
		
		for (String line : lines) {
//			analyseOneLine(line);
			writeToYunti(line);
		}
//		analyse.submit();
//		analyse.release();
		
		list = generateLogPos(lines[0]);
		
		clearTrace();
		
		
		return list;
	}
	
	static AtomicLong al=new AtomicLong(0);
	static AtomicLong bl=new AtomicLong(0);
	
	@Override
	public void analyseOneLine(String line) {
		if(al.incrementAndGet()%1000==0){
			logger.warn("===collect"+CollectFlag.IS_STORM+","+CollectFlag.IS_ALL_STORM+","+getAppName());
		}
		
		//全部由storm采集
		if(CollectFlag.IS_ALL_STORM){
			analyse.analyseOneLine(line);
		}else{
			//storm采集，但是分应用
			if(CollectFlag.IS_STORM && CollectFlag.storms.containsKey(getAppName())){
				if(bl.incrementAndGet()%1000==0){
					logger.warn("===come here to analyse,"+line);
				}
				analyse.analyseOneLine(line);
			}
		}
		
	}
	
	@Override
	public boolean isCurrentLog(String segment) {
		boolean isCurrentLog = true;
		
		return isCurrentLog;
	}
	
	private List<Log> generateLogPos(String line) {
		List<Log> list = new ArrayList<Log>();

//		try {
//			// which is set to null by clearTrace after an analysis
//			if (collectTime == null) {
//				SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd HH:mm");
//				SimpleDateFormat sdfInfo = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm", Locale.ENGLISH);
//				Pattern pattern = Pattern.compile("(\\[\\d{2}\\/\\w{3}\\/\\d{4}:\\d{2}:\\d{2})");
//				Matcher matcher = pattern.matcher(line);
//				if (matcher.find()) {
//					collectTime = sdfDay.format(sdfInfo.parse(matcher.group(1)));
//				}
//			}
//
//			if (collectTime == null) {
//				SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd HH:mm");
//				SimpleDateFormat wirelessSdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
//				Pattern wireleseePattern = Pattern.compile("(\\d{4}:\\d{2}:\\d{2}:\\d{2}:\\d{2})");
//				Matcher matcher = wireleseePattern.matcher(line);
//				if (matcher.find()) {
//					collectTime = sdfDay.format(wirelessSdf.parse(matcher.group(1)));
//				}
//			}
//		} catch (Exception e) {
//			logger.error(this.getAppName() + "can not get time:" + line, e);
//		} finally {
//			if (collectTime == null) {
//				logger.warn(this.getAppName() + "can not get time:" + line);
//				Calendar calendar = Calendar.getInstance();
//				SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd HH:mm");
//				collectTime = sdfDay.format(calendar.getTime());
//			}
//		}

		PinkieLog log = new PinkieLog();
		list.add(log);

		return list;
	}
	
	private void writeToYunti(String line) {
		HdfsFileOutputter.output(this.getAppName() + ":" + this.getHostInfo().toString(), YuntiLogType.webserver, line);
	}
	
	private void clearTrace() {
		cache = null;
		collectTime = null;
		count = 0;
	}

}
