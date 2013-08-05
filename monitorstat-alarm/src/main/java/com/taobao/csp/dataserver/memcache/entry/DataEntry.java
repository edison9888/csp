
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.entry;

import java.util.Calendar;

import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.dataserver.memcache.event.ValueEventFactory;
import com.taobao.csp.dataserver.memcache.time.HashedWheelTimeout;

/**
 * @author xiaodu
 *
 * 下午4:03:37
 */
public class DataEntry{
	
	private static final long serialVersionUID = -5467963947496454543L;

	
	/**
	 * 入库以后，该标志位置为true
	 * 已经过了3分钟入库，用来判断是否还有数据需要写入
	 */
	private boolean isTimeOut;
	protected ValueItem vi=null;
	/**
	 * 每个DataEntry附带一个HashedWheelTimeout，不用每次创建
	 * 因为DataEnry和index是绑定的
	 */
	protected HashedWheelTimeout timewheelout;

	public DataEntry(PropertyEntry pt,int index){
		this();
		timewheelout=new HashedWheelTimeout(pt,index);
	}

	public DataEntry(){
		this.vi=new ValueItem();
	}
	
	public Object getValue() {
		return vi.getValue();
	}

	/**
	 * 设置时间和值，用于初始化
	 * @param time
	 * @param value
	 */
	public void setTimeAndValue(long time, Object value){
		vi.setTime(time);
		vi.setValue(value);
	}
	/**
	 * 将值按照operate进行操作
	 * 
	 * @param time
	 * @param newValue
	 * @param operate
	 */
	public void opTimeAndValue(long time, Object newValue,ValueOperate operate,int second){
		vi.setValue(operate.operate(vi.getValue(), newValue));
	}
	/**
	 * 把值写入queue
	 * @param appName
	 * @param propname
	 * @param fullName
	 * @param scope
	 */
	public void putValueToQueue(String appName,String propname,String fullName,
			KeyScope scope) {
		Object value = vi.getValue();
		long collectime = vi.getTime();

		if (scope == KeyScope.APP) {
			ValueEventFactory.apphbaseEvent.onEvent(appName, propname,
					fullName, collectime,value);
		} else {
			ValueEventFactory.hbaseEvent.onEvent(appName, propname,
					fullName, collectime,value);
		}
	}
	
	public ValueItem getVi() {
		return vi;
	}

	public boolean isTimeOut() {
		return isTimeOut;
	}
	
	public void setTimeOut(boolean isTimeOut) {
		this.isTimeOut = isTimeOut;
	}
	public long getTime() {
		return vi.getTime();
	}
	
	public HashedWheelTimeout getTimewheelout() {
		return timewheelout;
	}

	public void setTimewheelout(HashedWheelTimeout timewheelout) {
		this.timewheelout = timewheelout;
	}
	
}
