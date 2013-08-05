
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.baseline;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.csp.time.util.DataUtil;

/**
 * @author xiaodu
 *
 * 下午2:56:51
 */
public class AppBaseLineProcessor extends BaseLineProcessor{
	
	private static Logger logger = Logger.getLogger(AppBaseLineProcessor.class);

	/**
	 * @param appName
	 * @param keyName
	 * @param propertyName
	 * @param date
	 */
	public AppBaseLineProcessor(String appName, String keyName, String propertyName, Date date) {
		super(appName, keyName, propertyName,KeyScope.APP.toString(), date);
	}

	@Override
	public void process(Date date) {
		
		Map<Date,String> dateMap = QueryHistoryUtil.querySingle(this.getAppName(), this.getKeyName(), this.getPropertyName(), date);
		if(dateMap==null||dateMap.size()==0){
			logger.info("没有历史数据");return;
		}

		for(Map.Entry<Date,String> entry:dateMap.entrySet()){
			try{
				putData(formatDate(entry.getKey()), DataUtil.transformDouble(entry.getValue()));
			}catch (Exception e) {
			}
		}
	}

	
	@Override
	protected String hbaseRowID() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getProcessDate());
		
		int week = cal.get(Calendar.DAY_OF_WEEK);
		
		return Util.combinBaseLineRowID(this.getAppName(), getKeyName(), getPropertyName())+Constants.S_SEPERATOR+week;
	}
}
