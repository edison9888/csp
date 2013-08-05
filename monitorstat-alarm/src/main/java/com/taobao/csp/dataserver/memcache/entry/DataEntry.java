
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
 * ����4:03:37
 */
public class DataEntry{
	
	private static final long serialVersionUID = -5467963947496454543L;

	
	/**
	 * ����Ժ󣬸ñ�־λ��Ϊtrue
	 * �Ѿ�����3������⣬�����ж��Ƿ���������Ҫд��
	 */
	private boolean isTimeOut;
	protected ValueItem vi=null;
	/**
	 * ÿ��DataEntry����һ��HashedWheelTimeout������ÿ�δ���
	 * ��ΪDataEnry��index�ǰ󶨵�
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
	 * ����ʱ���ֵ�����ڳ�ʼ��
	 * @param time
	 * @param value
	 */
	public void setTimeAndValue(long time, Object value){
		vi.setTime(time);
		vi.setValue(value);
	}
	/**
	 * ��ֵ����operate���в���
	 * 
	 * @param time
	 * @param newValue
	 * @param operate
	 */
	public void opTimeAndValue(long time, Object newValue,ValueOperate operate,int second){
		vi.setValue(operate.operate(vi.getValue(), newValue));
	}
	/**
	 * ��ֵд��queue
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
