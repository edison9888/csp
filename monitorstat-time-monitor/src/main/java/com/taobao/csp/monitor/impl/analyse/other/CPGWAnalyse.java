package com.taobao.csp.monitor.impl.analyse.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

public class CPGWAnalyse extends AbstractDataAnalyse  {

	public CPGWAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
	}

	private static final Logger logger =  Logger.getLogger(CPGWAnalyse.class);
	private Map<Long, Map<String, long[]>> map = new HashMap<Long, Map<String, long[]>>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Calendar cal = Calendar.getInstance();
	
	@Override
	public void analyseOneLine(String line) {
		if(line == null)
			return;
		line = line.trim();

		String dateString = line.substring(0, 23);
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			logger.error(getAppName(), e);
			return;
		}
		
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long timeL = cal.getTimeInMillis();  
		String key = null;
		long costTime = 0;
		String flag = null;
		
		if (line.indexOf("IN HTTP ECARD") >= 0) {
			//2012-09-28 15:48:10,059 ERROR [analyseLogger] - IN HTTP ECARD 822428298 EcardResultHandler 63
			String[] array = line.split("IN HTTP ECARD");
			if(array.length == 2) {
				String tmp = array[1].trim();
				String[] array2 = tmp.split(" ");
				key = "IN_HTTP_ECARD_" + array2[1];
				costTime = Long.parseLong(array2[2]);
				if(array2.length == 4)
					flag = array2[3];
			}
		} else if(line.indexOf("OUT HTTP ECARD") >= 0) {
			//2012-09-28 15:44:19,980 ERROR [analyseLogger] - OUT HTTP ECARD 705752027 http://www.chongzhi8.com/CorpInt/taobao/orderQuery.jsp 15
			String[] array = line.split("OUT HTTP ECARD");
			if(array.length == 2) {
				String tmp = array[1].trim();
				String[] array2 = tmp.split(" ");
				key = "OUT_HTTP_ECARD_" + array2[1];
				costTime = Long.parseLong(array2[2]);
				if(array2.length == 4)
					flag = array2[3];
			}
		}
		if(key != null && flag != null) {
			Map<String, long[]> tmpMap = map.get(timeL);
			if(tmpMap == null) {
				tmpMap = new HashMap<String, long[]>();
				map.put(timeL, tmpMap);
			}
			long[] valueArray = tmpMap.get(key);
			
			if(valueArray == null) {
				valueArray = new long[]{0,0,0};
				tmpMap.put(key, valueArray);
			}
			valueArray[0] += 1;
			valueArray[1] += costTime;	
			if(flag.equalsIgnoreCase("false")) {
				valueArray[2] += 1;	//Ê§°ÜÂÊ
			}
		}
	}

	@Override
	public void submit() {
		for(Entry<Long, Map<String, long[]>> entry : map.entrySet()) {
			long time = entry.getKey();
			Map<String, long[]> keyMap = entry.getValue();
			for(Entry<String, long[]>  valueEntry :keyMap.entrySet()) {
				String key = valueEntry.getKey();
				long[] value = valueEntry.getValue();
				float failRate = (float)value[2]/value[0];
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, 
							new String[]{key}, 
							new KeyScope[]{KeyScope.ALL}, 
							new String[]{PropConstants.E_TIMES, PropConstants.C_TIME, PropConstants.FAIL_PERCENT},
							new Object[]{value[0],value[1],failRate}, 
							new ValueOperate[]{ValueOperate.ADD, ValueOperate.ADD, ValueOperate.REPLACE});
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
	}

	@Override
	public void release() {
		map.clear();		
	}

	public static void main(String[] args) {
		String[] array = new String[]{
				"2012-09-28 15:48:10,059 ERROR [analyseLogger] - OUT HTTP ECARD 822441944 http://www.esaipai.com/TaoBaoIF/TaoBao_Shop/TaoBao_Query_Notify 220 false",
				"2012-09-28 15:48:10,059 ERROR [analyseLogger] - OUT HTTP ECARD 822441944 http://www.esaipai.com/TaoBaoIF/TaoBao_Shop/TaoBao_Query_Notify 220 true",
				"2012-09-28 15:48:10,059 ERROR [analyseLogger] - OUT HTTP ECARD 822441944 http://www.esaipai.com/TaoBaoIF/TaoBao_Shop/TaoBao_Query_Notify 220 false",
		};
		CPGWAnalyse analyse = new CPGWAnalyse("","","");
		for(String str : array) {
			analyse.analyseOneLine(str);
		}
		analyse.submit();
	}
}
