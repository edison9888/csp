
package com.taobao.monitor.web.distrib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author xiaodu
 * @version 2010-11-15 ����04:44:33
 */
public class ProviderDisribFlowBo {
	
	private String providerAppName;
	
	private long allCalls; //ȫ���ĵ�����
	
	private long allExceptionNum;
	
	private Set<String> customerMethodNameSet = new HashSet<String>();
	
	private Set<String> customerAppNameSet = new HashSet<String>();
	
	private Map<String,Long> cmCallMap = new HashMap<String,Long>();//�������÷ֲ�
	
	private List<AppDistribFlowBo> appBoList = new ArrayList<AppDistribFlowBo>();//ȫ�������ҵ�Ӧ��
	//Map<appName,AppDistribFlowBo>
	private Map<String,AppDistribFlowBo> appBoMap = new HashMap<String, AppDistribFlowBo>();
	
	//Map<appName,exceptionNum>
	private Map<String,Long> appExceptionNumMap = new HashMap<String,Long>();//Ӧ��exception �ֲ�
	//Map<ExceptionName,exceptionNum>
	private Map<String,Long> exceptionNameNumMap = new HashMap<String, Long>();//�쳣���� �ֲ�
	
	//������Ӱ�������������Ϣ��ʾʱ�ӵģ���Ҫӵ��key��list��Map
	private List<KeyDistribFlowBo> keyBoList = new ArrayList<KeyDistribFlowBo>();//�ṩ������key
	private Map<String,KeyDistribFlowBo> keyBoMap = new HashMap<String, KeyDistribFlowBo>();
	
	/**
	 * �ṩ�����з���
	 * @return
	 */
	public Map<String,KeyDistribFlowBo> getAllMethodCall(){		
		Map<String,KeyDistribFlowBo> keyMap = new HashMap<String, KeyDistribFlowBo>();		
		for(AppDistribFlowBo bo:appBoList){
			Map<String, KeyDistribFlowBo> map = bo.getKeyMap();		//Map<appName,KeyDistribFlowBo>
			for(Map.Entry<String, KeyDistribFlowBo> entry:map.entrySet()){
				KeyDistribFlowBo key = entry.getValue();
				String keyName = key.getKeyName();
				long keyCall = key.getCallNum();
				String[] keyNames = keyName.split("_");
		  		String tmpKeyName = keyNames[2]+"_"+keyNames[3];
		  		
		  		KeyDistribFlowBo k = keyMap.get(tmpKeyName);
		  		if(k == null){
		  			k = new KeyDistribFlowBo();
		  			k.setKeyName(tmpKeyName);		  			
		  			keyMap.put(tmpKeyName, k);
		  		}
		  		k.getCallThisMethodAppSet().add(bo.getAppName());
		  		k.setCallNum(keyCall+k.getCallNum());
			}
		}
		return keyMap;
	}
	
	
	
	
	public String getProviderAppName() {
		return providerAppName;
	}
	public void setProviderAppName(String providerAppName) {
		this.providerAppName = providerAppName;
	}
	
	public List<AppDistribFlowBo> getAppBoList() {
		return appBoList;
	}

	public void setAppBoList(List<AppDistribFlowBo> appBoList) {
		this.appBoList = appBoList;
	}

	public long getAllExceptionNum() {
		return allExceptionNum;
	}
	public void setAllExceptionNum(long allExceptionNum) {
		this.allExceptionNum = allExceptionNum;
	}
	
	public Map<String, Long> getAppExceptionNumMap() {
		return appExceptionNumMap;
	}
	public void setAppExceptionNumMap(Map<String, Long> appExceptionNumMap) {
		this.appExceptionNumMap = appExceptionNumMap;
	}
	
	public Map<String, Long> getExceptionNameNumMap() {
		return exceptionNameNumMap;
	}
	public void setExceptionNameNumMap(Map<String, Long> exceptionNameNumMap) {
		this.exceptionNameNumMap = exceptionNameNumMap;
	}
	public long getAllCalls() {
		return allCalls;
	}
	public void setAllCalls(long allCalls) {
		this.allCalls = allCalls;
	}
	public Map<String, Long> getCmCallMap() {
		return cmCallMap;
	}
	public void setCmCallMap(Map<String, Long> cmCallMap) {
		this.cmCallMap = cmCallMap;
	}
	public int getCustomerAppNum() {
		return customerAppNameSet.size();
	}
	
	public int getCustomerMethodNum() {
		return customerMethodNameSet.size();
	}

	public Set<String> getCustomerMethodNameSet() {
		return customerMethodNameSet;
	}
	public void setCustomerMethodNameSet(Set<String> customerMethodNameSet) {
		this.customerMethodNameSet = customerMethodNameSet;
	}
	public Set<String> getCustomerAppNameSet() {
		return customerAppNameSet;
	}
	public void setCustomerAppNameSet(Set<String> customerAppNameSet) {
		this.customerAppNameSet = customerAppNameSet;
	}
	public Map<String, AppDistribFlowBo> getAppBoMap() {
		return appBoMap;
	}
	public void setAppBoMap(Map<String, AppDistribFlowBo> appBoMap) {
		this.appBoMap = appBoMap;
	}




	public List<KeyDistribFlowBo> getKeyBoList() {
		return keyBoList;
	}




	public void setKeyBoList(List<KeyDistribFlowBo> keyBoList) {
		this.keyBoList = keyBoList;
	}




	public Map<String, KeyDistribFlowBo> getKeyBoMap() {
		return keyBoMap;
	}




	public void setKeyBoMap(Map<String, KeyDistribFlowBo> keyBoMap) {
		this.keyBoMap = keyBoMap;
	}
	
	

}
