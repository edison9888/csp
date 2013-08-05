/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.web.ao;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.DetailMonitorDAO;
import com.taobao.monitor.common.util.DetailStatisticHelper;
import com.taobao.monitor.web.core.dao.impl.MonitorTimeDao;
import com.taobao.monitor.web.vo.DetailBusiData;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 获取detail中自定义的监控数据
 * 
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version 2010-5-24:下午04:49:10
 * 
 */
public class DetailStatisticAO {

    private static final Logger logger = Logger.getLogger(DetailStatisticAO.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    DecimalFormat df1 = new DecimalFormat("###.00%");
    private static DetailStatisticAO ao = new DetailStatisticAO();
    private MonitorTimeDao dao = new MonitorTimeDao();

    private DetailMonitorDAO detailDAO = new DetailMonitorDAO();

    private DetailStatisticAO() {	

    }

    public static DetailStatisticAO get() {
	return ao;
    }

    public List<DetailBusiData> queryStatistic(String keyPrefix, String appname, String dateStr) {

	List<DetailBusiData> statList = new ArrayList<DetailBusiData>();

	Map<Integer, Integer> key4Statistic = new HashMap<Integer, Integer>();
	Date date = new Date();
	try {
	    date = sdf2.parse(dateStr);
	} catch (ParseException e1) {

		logger.error("", e1);
	}
	String tableSuffix = sdf.format(date);

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);

	Date end = cal.getTime();

	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));

	Date start = cal.getTime();

	// key
	Map<Integer, String> keyInfoMap = new HashMap<Integer, String>();
	//Map<String, String> pageMap = new HashMap<String, String>();
	try {
	    // int appId = dao.getAppId(appname);
	    // String keyIdStr = localeKeyIdCache.get(keyPrefix);
	    // if(keyIdStr == null){
	    String keyIdStr = dao.getKeyIdByLikeName(keyPrefix);
	    // localeKeyIdCache.put(keyPrefix, keyIdStr);
	    // }

	    // 得到统计好的数据
	    key4Statistic = detailDAO.queryStatic(tableSuffix, keyIdStr, 1, start, end);

	    // 得到key信息
	    keyInfoMap = detailDAO.queryKeyInfoByLikedName(keyPrefix);

	    // 统计汇总信息，用于页面上的输出
	    Integer sum = new Integer(0);

	    for (Iterator<Integer> iterator = key4Statistic.keySet().iterator(); iterator.hasNext();) {
		Integer key = (Integer) iterator.next();
		Integer value = key4Statistic.get(key);

		sum += value;

	    }

	    for (Iterator<Integer> iterator = key4Statistic.keySet().iterator(); iterator.hasNext();) {
		Integer key = (Integer) iterator.next();
		Integer value = key4Statistic.get(key);

		DetailBusiData busiData = new DetailBusiData();

		busiData.setKeyId(key);
		busiData.setName(keyInfoMap.get(key));
		busiData.setGroupId(DetailStatisticHelper.getGroupIdByPrefix(keyPrefix));
		busiData.setNumValue(value.doubleValue());
		busiData.setStrValue(df1.format(value.doubleValue() / sum.doubleValue()));
		
		statList.add(busiData);

	    }

	} catch (Exception e) {
	    logger.error("", e);
	}
	
	//Collections.sort(statList, );
	
	Collections.sort(statList, new Comparator<DetailBusiData>(){
	    public int compare(DetailBusiData arg0, DetailBusiData arg1) {
		return arg0.getNumValue() - arg1.getNumValue() > 0? -1:1;

	}
	});

	return statList;

    }

    
    public Map<String, List<KeyValuePo>> findInfoByKeyDate(String keyName,String appName,Date start,Date end){
		try {
			
			
			
			Map<String,Map<String,KeyValuePo>> map = dao.findLikeKeyByDate(keyName, appName, start, end);
			
			Map<String, List<KeyValuePo>> listMap = new HashMap<String, List<KeyValuePo>>();
			
			for(Map.Entry<String, Map<String,KeyValuePo>> entry:map.entrySet()){
				String key = entry.getKey();				
				Map<String,KeyValuePo> timeMap = entry.getValue();				
				for(KeyValuePo po:timeMap.values()){
					if(key.indexOf(keyName)>-1){					
						List<KeyValuePo> list = listMap.get(keyName);
						if(list==null){
							list = new ArrayList<KeyValuePo>();
							listMap.put(keyName, list);
						}
						list.add(po);
					}
					
					
				}
			}
			
			
			return listMap;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
		
	}
    
}
