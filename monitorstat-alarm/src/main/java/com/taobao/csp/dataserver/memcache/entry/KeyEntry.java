/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.entry;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.filter.HbaseFilter;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueObject;
import com.taobao.csp.dataserver.item.ValueObjectBox;
import com.taobao.csp.dataserver.memcache.core.Memcache;

/**
 * 
 * 
 * �洢key��ʵ����Ϣ
 * 
 * @author xiaodu
 * 
 *         ����10:44:49
 */
public class KeyEntry implements Item {

	private String keyName; // key������

	private String appName;

	private String fullKeyName;

	private Map<String, PropertyEntry> propertyMap = new HashMap<String, PropertyEntry>(); // ���key
																							// ���е����Լ���
	private Object propertyLock = new Object();
	
	private long recentlyAcceptTime = -1; // ������յ����ݵ�ʱ��

	private Memcache memcache;

	private KeyScope keyScope;

	private ReentrantLock reentrantLock = new ReentrantLock();

	private boolean hbase = true;

	public boolean isHbase() {
		return hbase;
	}

	public String getAppName() {
		return appName;
	}

	public KeyScope getKeyScope() {
		return keyScope;
	}

	public KeyEntry(String appName, String kn, String fullName,
			KeyScope keyScope, Memcache memcache) {
		this.keyName = kn;
		this.fullKeyName = fullName;
		this.appName = appName;
		this.keyScope = keyScope;
		this.memcache = memcache;
		this.hbase = HbaseFilter.getHbaseFilter().isHbase(fullName, appName,
				keyScope);
	}

	public String getKeyName() {
		return keyName;
	}

	public String getFullKeyName() {
		return fullKeyName;
	}

	public long getRecentlyAcceptTim() {
		return recentlyAcceptTime;
	}

	/**
	 * ȡ�����key ����ĳ������ʵ��
	 * 
	 * @param propName
	 * @return
	 */
	public PropertyEntry getProperty(String propName) {
		return propertyMap.get(propName);
	}

	/**
	 * ��ȡ���key ����ȫ�������Լ���
	 * 
	 * @return
	 */
	public Collection<PropertyEntry> getProperties() {
		return propertyMap.values();
	}

	/**
	 * �ɼ�������
	 * 
	 * @param time
	 * @param box
	 */
	public void addItem(long time, ValueObjectBox box,boolean isSecond) {
		Calendar cal = Calendar.getInstance();
		recentlyAcceptTime = cal.getTimeInMillis();

		cal.setTimeInMillis(time);
		int m = cal.get(Calendar.MINUTE);
		int second=0;
		if(isSecond){
			second= cal.get(Calendar.SECOND);
		}
		
		int index = m % Constants.CACHE_TIME_INTERVAL;
		
		for (ValueObject vo : box.getMapCollection()) {

			PropertyEntry pe = propertyMap.get(vo.getName());
			if (pe == null) {
				synchronized (propertyLock) {
					pe = propertyMap.get(vo.getName());
					if (pe == null) {
						pe = new PropertyEntry(this, vo.getName(),isSecond);
						propertyMap.put(vo.getName(), pe);
					}
				}
			}
			
			synchronized(pe){
				pe.appendData(index, time, vo.getValue(), 
						vo.getOperate(),second);
			}
		}
	}

	public ReentrantLock getReentrantLock() {
		return reentrantLock;
	}

	public void setReentrantLock(ReentrantLock reentrantLock) {
		this.reentrantLock = reentrantLock;
	}
	
	public boolean removeProperty(String propName) {
		synchronized (propertyLock) {
			PropertyEntry pe = propertyMap.get(propName);
			if(pe!=null){
				synchronized (pe) {
					long currentM=System.currentTimeMillis();
					pe.free();
					if(currentM-pe.getRecentlyAcceptTim()>Constants.twelMinute){
						propertyMap.remove(propName);
						return true;
					}
				}
				
			}
			return false;
		}
	}

	@Override
	public void destroy() {

		memcache.remove(this.getFullKeyName());
		this.propertyMap.clear();
		this.keyName = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.taobao.csp.dataserver.memcache.Item#free()
	 */
	@Override
	public void free() {

		for (Map.Entry<String, PropertyEntry> entry : propertyMap.entrySet()) {
			entry.getValue().free();
		}

	}

	public String toString() {
		return "keyName:" + keyName + ",fullKeyName:" + fullKeyName
				+ ",appName:" + appName + ",keyScope:" + keyScope.name();
	}

	@Override
	/**
	 * ��ȡ���key �����һ�λ�ȡ�����ݵ�ʱ��
	 * @return
	 */
	public long getRecentlyTime() {
		return recentlyAcceptTime;
	}

	public void setHbase(boolean hbase) {
		this.hbase = hbase;
	}

}
