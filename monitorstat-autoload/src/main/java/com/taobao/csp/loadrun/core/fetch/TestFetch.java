package com.taobao.csp.loadrun.core.fetch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.constant.ResultKey;

public class TestFetch {
	
	private static final Logger logger = Logger.getLogger(ApacheFetchTaskImpl.class);
	
	private static Pattern pattern = Pattern.compile("\\[(\\d{2}/\\w+/\\d{4}:\\d{2}:\\d{2}:\\d{2})\\s{1}\\+\\d+\\]"); // [28/Apr/2010:10:46:06	// +0800]
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);// 28/Apr/2010:00:00:01

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		FileInputStream inputStream  = new FileInputStream(new File("D:\\tmp2\\aaaa"));
		
		InputStreamReader inReader = new InputStreamReader(inputStream);
		
		BufferedReader bufferedReader = new BufferedReader(inReader);
		
		String readString;
		
		while ((readString = bufferedReader.readLine()) != null) {
			analyse(readString);
		}

	}
	
	public static void  analyse(String line) {
		try{
			
			Date time = parseLogLineCollectTime(line);
			if(time != null){
				String[] pv = line.split("\"");
				String rtStatus = pv[0];
				String[] _rtStatus = rtStatus.split(" ");
				String rt = _rtStatus[1]; // rt
				String httpStatus = pv[2];
				String[] statusAndPagesize = httpStatus.trim().split(" ");
				//putData(ResultKey.Apache_Pv, 1d, time);
				//putData(ResultKey.Apache_Rest, Double.parseDouble(rt), time);
				if (statusAndPagesize.length == 2) {
					String status = statusAndPagesize[0];
					String pagesize =  statusAndPagesize[1];
					if("200".equals(status.trim())){
						//putData(ResultKey.Apache_State_200, 1d, time);
					}
					if(StringUtils.isNumeric(pagesize)) {
						//putData(ResultKey.Apache_PageSize, Double.parseDouble(pagesize), time);
					}
						
				}
			}
			//logger.info("get apache :"+ResultKey.Apache_Pv+" "+time);
		}catch (Exception e) {
			System.err.println("分析apache 日志出错");
		}
	}
	
	
	protected static Date parseLogLineCollectTime(String logRecord) {
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
