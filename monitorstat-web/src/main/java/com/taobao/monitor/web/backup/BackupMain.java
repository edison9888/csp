package com.taobao.monitor.web.backup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorBackupAo;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/**
 * 数据备份的主类
 * @author wuhaiqian.pt
 *
 */
public class BackupMain {
	
	private static final Logger logger = Logger.getLogger(AverageFilter.class);

	private static BackupMain backupMain = new BackupMain();
	
	public static BackupMain get() {
		
		return backupMain;
	}
	
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.set(2011, 0, 15);	//2010-12-22
		Date date = cal.getTime();
//		System.out.println(date);
//		String date1 = sdf.format(date);
//		cal.add(Calendar.DAY_OF_MONTH, -2);
//		
//		System.out.println(cal.getTime());
//		//新建过滤器
//		Filter f2 = new JumpFilter(3);
//		Filter f3 = new LimitTimeFilter("01:00","07:00");
//
//		f2.setAccept(true);
//		f3.setAccept(true);
//		//设置过滤链
//		f2.setNext(f3);

		
		//备份指定日期的数据
//		BackupMain.get().backupOneDay(cal.getTime(), f2);
//		BackupMain.get().backuplimitDay("2010-12-28","2010-12-30",f2);
//		BackupMain.get().backupOneYear("2010", f1);
	}
	
	/**
	 * 备份指定日期的数据
	 * @param startDate	//格式为yyyy-MM-dd
	 * @param endDate
	 * @param f
	 */
	public void backuplimitDay(String startDate, String endDate, Filter f) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		String startYear = startDate.substring(0, 4);
		String startMonth = startDate.substring(5, 7);
		String startDay = startDate.substring(8, 10);
		String endYear = endDate.substring(0, 4);
		String endMonth = endDate.substring(5, 7);
		String endDay = endDate.substring(8, 10);
		calStart.set(Integer.parseInt(startYear), Integer.parseInt(startMonth)-1,Integer.parseInt(startDay));
		calEnd.set(Integer.parseInt(endYear), Integer.parseInt(endMonth)-1,Integer.parseInt(endDay));
		
		
        while(calStart.compareTo(calEnd) <= 0) {

            //做每天的备份
        	BackupMain.get().backupOneDay(calStart.getTime(), f);
            //循环，每次天数加1
        	calStart.set(Calendar.DATE, calStart.get(Calendar.DATE) + 1);
        	
        }
	}
	
	public void backupOneYear(String year, Filter f) {
		
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), 0, 1);
        while(true) {
        	
            //做每天的备份
        	BackupMain.get().backupOneDay(cal.getTime(), f);
            //循环，每次天数加1
        	cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
            if(!format.format(cal.getTime()).substring(0, 4).equals(year))
            	break;
        }
        
	}
	
	/**
	 * 备份指定一天的数据
	 * @param date
	 * @param filter
	 */
	public void backupOneDay(Date date , Filter filter) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String date1 = sdf.format(date);
		List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
		int sum = 0;
		int nowSum = 0;
		for(AppInfoPo app : appList) {
			List<KeyPo> keyList = KeyAo.get().findAllAppKey(app.getAppId());
			for(KeyPo key : keyList) {

				Map<String, KeyValuePo> map = MonitorTimeAo.get().findKeyValueByDate(app.getAppId(),key.getKeyId(), date);
				sum += map.size();
				nowSum += BackupMain.get().filterData(filter, date,map);
			}
			logger.info("删除应用" + app.getAppName() + sdf1.format(date) + "的数据" );
			MonitorTimeAo.get().deleteMonitorData(app.getAppId(), sdf1.format(date));
		}
		System.out.println(date1 + "原来大小：" + sum);
		System.out.println(date1 + "现在大小：" + nowSum);
		System.out.println(date1 + "压缩率：" + Arith.div((sum - nowSum), sum) * 100 + "%");
		
	}
	
	/**
	 * 根据过滤链的开头和所给的时间过滤map数据并且插入到备份数据库中
	 * @param f
	 * @param date
	 */
	public int filterData(Filter f, Date date, Map<String, KeyValuePo> map) {
		
		//平均的过滤器为默认需要的
		Filter f1 = new AverageFilter();
		f1.setAccept(true);		//打开过滤器开关
		f1.setNext(f);
		
		int nowSum = 0; //压缩后的大小
		if(map != null && map.size()!= 0) {
			Map<String, KeyValuePo> filterMap = f1.doFilter(map);
			nowSum += filterMap.size();
			MonitorBackupAo.get().addMonitorData(filterMap);
		}
		return nowSum;	//返回过滤后的数据大小
	}
	
}
