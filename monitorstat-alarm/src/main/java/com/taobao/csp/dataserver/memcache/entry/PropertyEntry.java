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
 * key的属性类
 * 
 * @author xiaodu
 * 
 * 下午7:07:53
 */
public class PropertyEntry implements Item {

	private KeyEntry keyentry; // 指向这个属性对应的key
	private String proName; // 属性名
	private long recentlyAcceptTime = -1; // 最近接收到数据的时间
	

	private DataEntry[] values = null; // 数据集合

	private int recentlyIndex = -1; // 最近一次数据索引

	private long recentlyTime = -1;
	boolean isSecond=false;

	/**
	 * 获取key 实体
	 * 
	 * @return
	 */
	public KeyEntry getKeyEntry() {
		return keyentry;
	}

	/**
	 * 获取数据数组
	 * 
	 * @return
	 */
	public DataEntry[] getValues() {
		return values;
	}

	/**
	 * 返回属性名称
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
	 * 采集到数据,入缓存
	 * 老的入库方式：覆盖写入库
	 * 
	 * 新的入库方式：
	 * 	每个value绑定一个timeout对象，timehashwheel需要这个TimeOut对象进行调度
	 * 	新创建的value或者覆盖的value全部挂到一个时间轮上
	 * 
	 * @param index
	 * @param time
	 * @param obj
	 * @param operate
	 * @param second 秒数
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
				// 新加入的元素，3分钟后入库
				CspTimeSchedu.cspTask(values[index].getTimewheelout());
			}
			recentlyTime = time;
		} else {
			long minuteTime=time;
			long indexMinuteTime=values[index].getTime();
			//秒级别的数据要转换为分钟来算出位置
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
				// logger.error("ItemArrayStandard插入数据到缓存，时间异常，缓存时间大于客户端时间");
			} else if (timeDif == 0) {
				// 60*1000，时间差在1分钟内
				values[index].opTimeAndValue(time, obj, operate,second);

				// 已经入库，但是又有写入
				if (values[index].isTimeOut()) {
					values[index].setTimeOut(false);
				}
			} else {
				// 覆盖写之前入库
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
					// 新加入的元素，3分钟后入库,之前这个位置的数据理论上已入库
					CspTimeSchedu.cspTask(values[index].getTimewheelout());
				}
			}
		}
		long curTime = System.currentTimeMillis();

		/*
		 * zk节点变化只对HOST级别的Key的入库进行处理。在zk节点变化10分组内对KenEntry的isHbase做进行比较，10分组外选择忽略
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
