package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.fetch.UrlElement;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/***
 * apache与nginx日志的分析类
 * @author youji.zj
 * @version 2012-07-05
 *
 */
public class ApacheAnalyse extends AbstractAnalyser {
	
	public static Logger logger = Logger.getLogger(ApacheAnalyse.class);
	
	private Pattern pattern = Pattern.compile("\\[(\\d{2}/\\w+/\\d{4}:\\d{2}:\\d{2}:\\d{2})\\s{1}\\+\\d+\\]"); // [28/Apr/2010:10:46:06	// +0800]
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);// 28/Apr/2010:00:00:01

	private IFetchTask task;
	
	private LoadrunTarget target;
	
	public ApacheAnalyse(IFetchTask task, LoadrunTarget target) {
		this.target = target;
		this.task = task;
	}
	
	@Override
	public void analyse(String line) {
		try{
			Date time = parseLogLineCollectTime(line);
			if (time == null)  return;
			
			if(target.getMode() == AutoLoadMode.SSH){
				String[] pv = line.split("\"");
				String rtStatus = pv[0];
				String[] _rtStatus = rtStatus.split(" ");
				String rt = _rtStatus[1]; // rt
				String httpStatus = pv[2];
				String requestUrl = cleanUrl(pv[1].split(" ")[1]);
				String[] statusAndPagesize = httpStatus.trim().split(" ");
				double rtTime = rt.indexOf(".") > -1  ? (double)(Float.parseFloat(rt) * 1000 * 1000) : Double.parseDouble(rt);
				
				task.putData(ResultKey.Apache_Pv, 1d, time);
				task.putData(ResultKey.Apache_Rest, rtTime, time);
				if (statusAndPagesize.length == 2) {
					String status = statusAndPagesize[0];
					String pagesize =  statusAndPagesize[1];
					if("200".equals(status.trim())){
						task.putData(ResultKey.Apache_State_200, 1d, time);
						
						if (requestUrl != null && requestUrl.indexOf("status.taobao") == -1) {
							long collectTime = transferDate(time, target);
							ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.URL, requestUrl, collectTime);
							task.putDetailData(resultDetailKey, 1, rtTime / 1000); // 微妙转换成毫秒
						}

					}
					if(StringUtils.isNumeric(pagesize))
						task.putData(ResultKey.Apache_PageSize, Double.parseDouble(pagesize), time);
				}
			} else {
				String[] pv = line.split("\"");
				String rtStatus = pv[2];
				String[] _rtStatus = rtStatus.trim().split(" +");
				String rt = _rtStatus[2]; // rt
				String httpStatus = _rtStatus[0];
				String pageSize = _rtStatus[1];
				String requestUrl = cleanUrl(pv[1].split(" ")[1]);
				double rtTime = rt.indexOf(".") > -1  ? (double)(Float.parseFloat(rt) * 1000 * 1000) : Double.parseDouble(rt);
				
				task.putData(ResultKey.Apache_Pv, 1d, time);
				task.putData(ResultKey.Apache_Rest, rtTime, time);

				if (requestUrl != null && "200".equals(httpStatus)) {
					task.putData(ResultKey.Apache_State_200, 1d, time);

					long collectTime = transferDate(time, target);
					ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.URL, requestUrl, collectTime);
					task.putDetailData(resultDetailKey, 1, rtTime / 1000); // 微妙转换成毫秒
				}
				if (StringUtils.isNumeric(pageSize)) {
					task.putData(ResultKey.Apache_PageSize, Double.parseDouble(pageSize), time);
				}
			}
		} catch (Exception e) {
			logger.error("分析apache 日志出错", e);
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
	
	/***
	 * 对url进行过滤，不满足条件返回null
	 * @param str
	 * @return
	 */
	private String cleanUrl(String str) {
		String realUrl;
		if (str.indexOf("?") > -1) {
			realUrl = str.substring(0, str.indexOf("?")).trim();
		} else {
			realUrl = str.trim();
		}
		
		List<UrlElement> urls = this.target.getUrl();
		
		for (UrlElement urlElement : urls) {
			if (urlElement.isDanymicUrl()) {
				String danamicUrl = urlElement.getAddress();
				Pattern pattern = Pattern.compile(danamicUrl);
				Matcher matcher = pattern.matcher(realUrl); 
				if (matcher.matches()) {
					return danamicUrl;
				}
			} else {
				String regularUrl = urlElement.getAddress();
				if (regularUrl.equals(realUrl)) {
					return realUrl;
				}
			}
		}
		
		return null;
	}

}
