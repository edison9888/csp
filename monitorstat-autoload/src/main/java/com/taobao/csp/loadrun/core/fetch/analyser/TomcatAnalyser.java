package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;

/**
 * tomcat信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class TomcatAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(TomcatAnalyser.class);
	
	private Pattern pattern = Pattern.compile("\\[(\\d{2}/\\w+/\\d{4}:\\d{2}:\\d{2}:\\d{2})\\s{1}\\+\\d+\\]"); // [28/Apr/2010:10:46:06	// +0800]
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);// 28/Apr/2010:00:00:01
	
	private IFetchTask task;
	
	public TomcatAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
	}

	@Override
	public void analyse(String line) {
		
		Date time = parseLogLineCollectTime(line);		
		
		try{
			String[] pv = line.split(" "); //[09/Oct/2010:14:27:48 +0800] 200 GET 279853 59
			if (pv.length == 6) {
					String httpStatus = pv[2];
					String rest = pv[5];
					String pagesize =  pv[4];
					int _rest = 0;
					int _pagesize = 0;
					try{
						_rest = Integer.parseInt(rest)*1000;
					}catch (Exception e) {
					}
					try{
						_pagesize = Integer.parseInt(pagesize);
					}catch (Exception e) {
					}
					task.putData(ResultKey.Tomcat_Pv,1d,time);
					
					if("200".equals(httpStatus.trim())){
						task.putData(ResultKey.Tomcat_State_200, 1d, time);
					}
					
					task.putData(ResultKey.Tomcat_Rest,new Double(_rest),time);
					task.putData(ResultKey.Tomcat_PageSize,new Double(_pagesize),time);
			}
		}catch (Exception e) {
			logger.error("", e);
		}
		
	}

	private Date parseLogLineCollectTime(String logRecord) {
		Matcher m = pattern.matcher(logRecord);
		if (m.find()) {
			String time = m.group(1);
			try {
				Date date = sdf.parse(time);
				return date;
			} catch (ParseException e) {
			}
		}
		return null;
	}
}
