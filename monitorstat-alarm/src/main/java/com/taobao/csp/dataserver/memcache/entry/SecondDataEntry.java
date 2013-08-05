package com.taobao.csp.dataserver.memcache.entry;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.dataserver.memcache.event.ValueEventFactory;

/**
 * 秒级别的数据项
 * DataEntry内部有一个数据项，表示一分钟内的数据
 * SecondDataEntry内部有60个数据项，表示60秒的数据
 * getTime()和getValue()返回的都是最近一秒的数据
 *
 */
public class SecondDataEntry extends  DataEntry{
	
	private static final long serialVersionUID = -925697949573236168L;
	/**60s的数据**/
	private ValueItem[] values=new ValueItem[60];

	public SecondDataEntry(PropertyEntry pt,int index){
		super(pt,index);
	}
	

	public SecondDataEntry(){
		super();
	}
	
	/**
	 * 设置秒数据
	 */
	public void setTimeAndValue(long time, Object value) {
		super.setTimeAndValue(time, value);
		clear();
		int index = (int)(time % Constants.CACHE_SECOND);
		
		if(values[index]==null){
			values[index]=new ValueItem();
		}
		values[index].setValue(value);
		values[index].setTime(time);
	}
	private void clear(){
		for(ValueItem vi:values){
			if(vi!=null){
				vi.setValue(null);
				vi.setTime(0);
			}
		}
	}
	/**
	 * 对value进行操作（累加/平均）
	 * 
	 * @param time
	 * @param value
	 */
	public void opTimeAndValue(long time, Object newValue, ValueOperate operate,
			int second) {
		int index =second % Constants.CACHE_SECOND;
		
		if(values[index]==null){
			values[index]=new ValueItem();
		}
		Object oldValue=values[index].getValue();
		Object newResult=operate.operate(oldValue, newValue);
		
		values[index].setValue(newResult);
		values[index].setTime(time);
		
		super.setTimeAndValue(time, newResult);
	}
	
	/**
	 * 数据入库
	 */
	public void putValueToQueue(String appName, String propname,
			String fullName, KeyScope scope) {
		for(int i=0;i<values.length;i++){
			ValueItem vi=values[i];
			if(vi!=null){
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
		}
	}
	/**
	 * 返回所有数据
	 * @return
	 */
	public ValueItem[] getValues() {
		return values;
	}

}
