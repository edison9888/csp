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
 * ���ݱ��ݵ�����
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
//		//�½�������
//		Filter f2 = new JumpFilter(3);
//		Filter f3 = new LimitTimeFilter("01:00","07:00");
//
//		f2.setAccept(true);
//		f3.setAccept(true);
//		//���ù�����
//		f2.setNext(f3);

		
		//����ָ�����ڵ�����
//		BackupMain.get().backupOneDay(cal.getTime(), f2);
//		BackupMain.get().backuplimitDay("2010-12-28","2010-12-30",f2);
//		BackupMain.get().backupOneYear("2010", f1);
	}
	
	/**
	 * ����ָ�����ڵ�����
	 * @param startDate	//��ʽΪyyyy-MM-dd
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

            //��ÿ��ı���
        	BackupMain.get().backupOneDay(calStart.getTime(), f);
            //ѭ����ÿ��������1
        	calStart.set(Calendar.DATE, calStart.get(Calendar.DATE) + 1);
        	
        }
	}
	
	public void backupOneYear(String year, Filter f) {
		
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), 0, 1);
        while(true) {
        	
            //��ÿ��ı���
        	BackupMain.get().backupOneDay(cal.getTime(), f);
            //ѭ����ÿ��������1
        	cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
            if(!format.format(cal.getTime()).substring(0, 4).equals(year))
            	break;
        }
        
	}
	
	/**
	 * ����ָ��һ�������
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
			logger.info("ɾ��Ӧ��" + app.getAppName() + sdf1.format(date) + "������" );
			MonitorTimeAo.get().deleteMonitorData(app.getAppId(), sdf1.format(date));
		}
		System.out.println(date1 + "ԭ����С��" + sum);
		System.out.println(date1 + "���ڴ�С��" + nowSum);
		System.out.println(date1 + "ѹ���ʣ�" + Arith.div((sum - nowSum), sum) * 100 + "%");
		
	}
	
	/**
	 * ���ݹ������Ŀ�ͷ��������ʱ�����map���ݲ��Ҳ��뵽�������ݿ���
	 * @param f
	 * @param date
	 */
	public int filterData(Filter f, Date date, Map<String, KeyValuePo> map) {
		
		//ƽ���Ĺ�����ΪĬ����Ҫ��
		Filter f1 = new AverageFilter();
		f1.setAccept(true);		//�򿪹���������
		f1.setNext(f);
		
		int nowSum = 0; //ѹ����Ĵ�С
		if(map != null && map.size()!= 0) {
			Map<String, KeyValuePo> filterMap = f1.doFilter(map);
			nowSum += filterMap.size();
			MonitorBackupAo.get().addMonitorData(filterMap);
		}
		return nowSum;	//���ع��˺�����ݴ�С
	}
	
}
