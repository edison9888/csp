package com.taobao.csp.dataserver.memcache.entry;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.dataserver.memcache.event.ValueEventFactory;

/**
 * �뼶���������
 * DataEntry�ڲ���һ���������ʾһ�����ڵ�����
 * SecondDataEntry�ڲ���60���������ʾ60�������
 * getTime()��getValue()���صĶ������һ�������
 *
 */
public class SecondDataEntry extends  DataEntry{
	
	private static final long serialVersionUID = -925697949573236168L;
	/**60s������**/
	private ValueItem[] values=new ValueItem[60];

	public SecondDataEntry(PropertyEntry pt,int index){
		super(pt,index);
	}
	

	public SecondDataEntry(){
		super();
	}
	
	/**
	 * ����������
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
	 * ��value���в������ۼ�/ƽ����
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
	 * �������
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
	 * ������������
	 * @return
	 */
	public ValueItem[] getValues() {
		return values;
	}

}
