
package com.taobao.csp.config.ao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.config.dao.MonitorAlarmDao;
import com.taobao.csp.config.po.AlarmDataPo;


/**
 *
 * @author xiaodu
 * @version 2010-4-19 上午11:36:00
 */
public class MonitorAlarmAo {

	private static final Logger logger =  Logger.getLogger(MonitorAlarmAo.class);

	private static MonitorAlarmAo  ao = new MonitorAlarmAo();
	private MonitorAlarmDao dao = new MonitorAlarmDao();
	

	public static  MonitorAlarmAo get(){
		return ao;
	}

	/**
	 *查询出所有需要告警的key 信息
	 * @param name
	 * @return
	 */
	public List<AlarmDataPo> findAllAlarmKeyByAimAndLikeName(Integer appId,String keyName){
		return dao.findAllAlarmKeyByAimAndLikeName(appId, keyName,null);
	}
	

		
}
