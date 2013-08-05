package com.taobao.csp.day.apache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.Log;

/***
 * sph »’÷æ
 * @author youji.zj
 *
 */
public class ApacheSpecialLog implements Log {
	
	public static Logger logger = Logger.getLogger(ApacheSpecialLog.class);
	
	private String appName;
	
	private String requestUrl;
	
	private long requestNum;
	
	private long rt;
	
	private String httpCode;
	
	private String collectTime;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public long getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(long requestNum) {
		this.requestNum = requestNum;
	}

	public long getRt() {
		return rt;
	}

	public void setRt(long rt) {
		this.rt = rt;
	}

	public String getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public static String[] getKeys() {
		String [] keys  = new String[6];
		keys[0] = "appName";
		keys[1] = "requestUrl";
		keys[2] = "requestNum";
		keys[3] = "rt";
		keys[4] = "httpCode";
		keys[5] = "collectTime";
		
		return keys;
	}
	
	
	public static List<String> getGroupFields() {
		List<String> groupFields = new ArrayList<String>(); 
		groupFields.add("appName");
		
		return groupFields;
	}

	@Override
	public Object[] getValues() {
		Object [] objects  = new Object[6];
		objects[0] = appName;
		objects[1] = requestUrl;
		objects[2] = requestNum;
		objects[3] = rt;
		objects[4] = httpCode;
		objects[5] = collectTime;
		
		return objects;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sf.parse(this.getCollectTime());
		} catch (ParseException e) {
			logger.error(e);
			date = Calendar.getInstance().getTime();
		}
		
		return date;
	}
}
