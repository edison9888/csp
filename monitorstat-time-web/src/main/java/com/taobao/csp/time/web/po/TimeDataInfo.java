
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.po;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.PropNameDescUtil;

/**
 *@author wb-lixing
 *2012-3-28 ����10:54:48 
 */
public class TimeDataInfo{
	
	
	private int appId;
	
	private String appName; //Ӧ������
	
	private String fullKeyName; //���key����������
	
	private String keyName; //չʾչʾ�õ����ƣ��Ὣ��key�����ƽص�
	
	private String ip; //Ӧ��ip
	
	//pv-refer url��Ӧ��Ӧ����
	private String referAppName = "";
	
	private String site;//Ӧ��ip ���ڻ���
	
	private Map<String,Object> originalPropertyMap = new HashMap<String, Object>();
	
	private String mainProp;
	private double mainValue; //����������
	
	private String mainRate = "";// ������rate
	private long time;
	
	private String ftime;
	
	public String getReferAppName() {
		return referAppName;
	}

	public void setReferAppName(String referAppName) {
		this.referAppName = referAppName;
	}

	public String getMainRate() {
		return mainRate;
	}

	public void setMainRate(String mainRate) {
		this.mainRate = mainRate;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	//ȡ��ԭʼ��ֵ
	public Map<String, Object> getOriginalPropertyMap(){
		
		if(originalPropertyMap.size()>0){
			for(Map.Entry<String, Object> entry:originalPropertyMap.entrySet()){
				if(entry.getValue() instanceof Float){
					
					Float f = (Float)entry.getValue();
					if(Float.isInfinite(f)||Float.isNaN(f)){
						entry.setValue(0);
					}else{
						entry.setValue(DataUtil.transformDouble(entry.getValue()));
					}
					
				}else if(entry.getValue() instanceof Double ){
					
					Double f = (Double)entry.getValue();
					if(Double.isInfinite(f)||Double.isNaN(f)){
						entry.setValue(0);
					}else{
						entry.setValue(DataUtil.transformDouble(entry.getValue()));
					}
					
				}
			}
		}
		
		return originalPropertyMap;
	}
	
	//���ƾ���ת����ֵ
	public Map<String, Object> getPropertyMap() {
		
		Map<String, Object> m = new HashMap<String, Object>();
		
		for(Map.Entry<String, Object> entry:originalPropertyMap.entrySet()){
			
			String key = entry.getKey();
			Object v = entry.getValue();
			if(PropNameDescUtil.getDesc(key)!=null){
				key = PropNameDescUtil.getDesc(key);
			}
			try{
				if (entry.getValue() instanceof Float){
					v = DataUtil.transformFloat(entry.getValue());
				} else if(entry.getValue() instanceof Double ){
					v = DataUtil.transformDouble(entry.getValue());
				} else if(entry.getValue() instanceof Long) {
					v = DataUtil.transformLong(entry.getValue());
				}
			}catch (Exception e) {
				v=0;
			}
			m.put(key, v);
		}
		return m;
	}


	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public double getMainValue() {
		BigDecimal bd = new BigDecimal(mainValue);
		bd = bd.setScale(2, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}

	public void setMainValue(double mainValue) {
		this.mainValue = mainValue;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFullKeyName() {
		return fullKeyName;
	}

	public void setFullKeyName(String fullKeyName) {
		this.fullKeyName = fullKeyName;
	}
	
	

	public String getMainProp() {
		return mainProp;
	}

	public void setMainProp(String mainProp) {
		this.mainProp = mainProp;
	}

	public String getMainValueRate() {
		
		if(getIp()!=null){
			return BaseLineCache.get().getScale(this.getAppName(), this.getKeyName(), this.getMainProp(), this.getFtime(), this.getMainValue(),getIp());
		}else{
			return BaseLineCache.get().getScale(this.getAppName(), this.getFullKeyName(), this.getMainProp(), this.getFtime(), this.getMainValue());
		}
	}
	
	public String getValueRate(String propName) {
		if(getIp()!=null){
			return BaseLineCache.get().getScale(this.getAppName(), this.getKeyName(), propName, this.getFtime(), this.getMainValue(),getIp());
		}else{
			return BaseLineCache.get().getScale(this.getAppName(), this.getFullKeyName(),propName, this.getFtime(), this.getMainValue());
		}
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public void setOriginalPropertyMap(Map<String, Object> originalPropertyMap) {
		if(originalPropertyMap != null)
			this.originalPropertyMap = originalPropertyMap;
	}
}
