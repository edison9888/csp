/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.entry;

import java.util.Calendar;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.filter.HbaseFilter;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.dataserver.memcache.time.CspTimeSchedu;
import com.taobao.monitor.MonitorLog;

/**
 * 
 * key��������
 * 
 * @author xiaodu
 * 
 * ����7:07:53
 */
public class PropertyEntry implements Item {

	private KeyEntry keyentry; // ָ��������Զ�Ӧ��key
	private String proName; // ������
	private long recentlyAcceptTime = -1; // ������յ����ݵ�ʱ��
	

	private DataEntry[] values = null; // ���ݼ���

	private int recentlyIndex = -1; // ���һ����������

	private long recentlyTime = -1;
	boolean isSecond=false;

	/**
	 * ��ȡkey ʵ��
	 * 
	 * @return
	 */
	public KeyEntry getKeyEntry() {
		return keyentry;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public DataEntry[] getValues() {
		return values;
	}

	/**
	 * ������������
	 * 
	 * @return
	 */
	public String getPropertyName() {
		return proName;
	}

	public PropertyEntry(KeyEntry key, String proName,boolean isSecond) {
		this.keyentry = key;
		this.proName = proName;
		this.isSecond=isSecond;
		if (isSecond) {
			values = new SecondDataEntry[Constants.CACHE_TIME_INTERVAL];
		}else{
			values = new DataEntry[Constants.CACHE_TIME_INTERVAL];
		}
	}

	static boolean HOST_NOT_IN_HBASE = false;

	/**
	 * �ɼ�������,�뻺��
	 * �ϵ���ⷽʽ������д���
	 * 
	 * �µ���ⷽʽ��
	 * 	ÿ��value��һ��timeout����timehashwheel��Ҫ���TimeOut������е���
	 * 	�´�����value���߸��ǵ�valueȫ���ҵ�һ��ʱ������
	 * 
	 * @param index
	 * @param time
	 * @param obj
	 * @param operate
	 * @param second ����
	 */
	public void appendData(int index, long time, Object obj,
			ValueOperate operate,int second) {
		
		recentlyAcceptTime = System.currentTimeMillis();
		recentlyIndex = index;
	
		if (values[index] == null) {
			if (isSecond) {
				values[index] = new SecondDataEntry(this, index);
			} else {
				values[index] = new DataEntry(this, index);
			}
			values[index].setTimeAndValue(time, obj);
			
			if (keyentry.isHbase()) {
				// �¼����Ԫ�أ�3���Ӻ����
				CspTimeSchedu.cspTask(values[index].getTimewheelout());
			}
			recentlyTime = time;
		} else {
			long minuteTime=time;
			long indexMinuteTime=values[index].getTime();
			//�뼶�������Ҫת��Ϊ���������λ��
			if (isSecond) {
				Calendar cal = Calendar.getInstance();

				cal.setTimeInMillis(time);
				cal.set(Calendar.SECOND, 0);
				minuteTime = cal.getTime().getTime();
				
				cal.setTimeInMillis(indexMinuteTime);
				cal.set(Calendar.SECOND, 0);
				indexMinuteTime = cal.getTime().getTime();
			}
			long timeDif = minuteTime - indexMinuteTime;
			
			if (timeDif < 0) {
				// logger.error("ItemArrayStandard�������ݵ����棬ʱ���쳣������ʱ����ڿͻ���ʱ��");
			} else if (timeDif == 0) {
				// 60*1000��ʱ�����1������
				values[index].opTimeAndValue(time, obj, operate,second);

				// �Ѿ���⣬��������д��
				if (values[index].isTimeOut()) {
					values[index].setTimeOut(false);
				}
			} else {
				// ����д֮ǰ���
				if (!values[index].isTimeOut()) {
					MonitorLog.addStat("datacache",
							new String[] { "dataDelay" }, new Long[] { 1L });
					
					String appName = this.getKeyEntry().getAppName();
					String propname = this.getPropertyName();
					String fullName = this.getKeyEntry().getFullKeyName();
					
					values[index].putValueToQueue(appName, propname, 
							fullName, this.getKeyEntry().getKeyScope());
				}
				values[index].setTimeAndValue(time, obj);
				recentlyTime = time;

				if (keyentry.isHbase()) {
					// �¼����Ԫ�أ�3���Ӻ����,֮ǰ���λ�õ����������������
					CspTimeSchedu.cspTask(values[index].getTimewheelout());
				}
			}
		}
		long curTime = System.currentTimeMillis();

		/*
		 * zk�ڵ�仯ֻ��HOST�����Key�������д�����zk�ڵ�仯10�����ڶ�KenEntry��isHbase�����бȽϣ�10������ѡ�����
		 */
		if (curTime - HbaseFilter.getLastUpdateTime() <= 600000) {
			boolean isHbase = this.keyentry.isHbase();
			boolean isHbaseNew = HbaseFilter.getHbaseFilter().isHbase(
					this.keyentry.getFullKeyName(), this.keyentry.getAppName(),
					this.keyentry.getKeyScope());

			if (isHbase == true && isHbaseNew == false) {
				this.keyentry.setHbase(false);
			} else if (isHbase == false && isHbaseNew == true) {
				this.keyentry.setHbase(true);
				MonitorLog
						.addStat("hbasefilter", new String[] { "key_add" },
								new Long[] { 1l,System.currentTimeMillis() - curTime });
			}
		}
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void free() {
		if (recentlyIndex != -1) {
			for (int i = 0; i < Constants.CACHE_TIME_INTERVAL; i++) {
				if (recentlyIndex != i)
					values[i] = null;
			}
		}
	}

	@Override
	public long getRecentlyTime() {
		return recentlyTime;
	}

	public int getRecentlyIndex() {
		return this.recentlyIndex;
	}

	public DataEntry getRecentlyData() {

		if (values != null && recentlyIndex != -1) {
			return values[recentlyIndex];
		}
		return null;
	}

	@Override
	public long getRecentlyAcceptTim() {
		// TODO Auto-generated method stub
		return recentlyAcceptTime;
	}

}
