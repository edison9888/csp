package com.taobao.csp.dataserver.collect;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.io.ClientUtil;
import com.taobao.csp.dataserver.item.KeyObject;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueObjectBox;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.dataserver.key.DBMediaKeyCache;
import com.taobao.csp.dataserver.packet.request.standard.RequestSecondStandard;
import com.taobao.csp.dataserver.packet.request.standard.RequestStandard;
import com.taobao.monitor.MonitorLog;

public class CollectDataUtilMulti {
	
	public static AppCollect collect(String appname, String hostIp, long collectTime){
		AppCollect collect = new AppCollect(appname,hostIp,collectTime);
		return collect;
	}
	
	
	
	/**
	 * 所有日志收集程序的公共接口,发送多条的版本
	 * @param appname		应用名称
	 * @param hostIp		日志来源机器Ip
	 * @param collectTime	采集时间
	 * @param keys			组织的key
	 * @param scopes		key的作用于，参考{@link #KeyScope}
	 * @param propertyNames 要存得属性的名称
	 * @param values		属性值
	 * @param os			属性的计算规则，参考{@link #ValueOperate}
	 * @throws Exception
	 */
	public static void collect(String appname, String hostIp, long collectTime, String[] keys,KeyScope[] scopes,String[] propertyNames, Object[] values) throws Exception{
		
		ValueOperate[] vo = new ValueOperate[values.length];
		for(int i=0;i<values.length;i++){
			vo[i] = ValueOperate.ADD;
		}
		collect(appname,hostIp,collectTime,keys,scopes,propertyNames,values,vo);
	}

	public static void collect(String appname, String hostIp, long collectTime,
			String[] keys,KeyScope[] scopes,String[] propertyNames, 
			Object[] values,ValueOperate[] os) throws Exception{

		collect(appname,hostIp,collectTime,keys,scopes,propertyNames,
				values,os,false);
	}

	/**
	 * 所有日志收集程序的公共接口,发送多条的版本
	 * @param appname		应用名称
	 * @param hostIp		日志来源机器Ip
	 * @param collectTime	采集时间
	 * @param keys			组织的key
	 * @param scopes		key的作用于，参考{@link #KeyScope}
	 * @param propertyNames 要存得属性的名称
	 * @param values		属性值
	 * @param os			属性的计算规则，参考{@link #ValueOperate}
	 * @param isSecond		是否秒级别
	 * @throws Exception
	 */
	public static void collect(String appname, String hostIp, long collectTime,
			String[] keys,KeyScope[] scopes,String[] propertyNames, 
			Object[] values,ValueOperate[] os,boolean isSecond) throws Exception{

		if(keys == null || values == null || scopes == null || os == null || propertyNames == null) {
			throw new Exception("参数为null"); 
		} else if((keys.length != scopes.length) || 
				!(values.length == os.length && os.length == propertyNames.length)) {
			throw new Exception("参数长度不相同");
		} else if(keys.length == 0 || propertyNames.length == 0){
			throw new Exception("参数长度为0");
		}
		
		StringBuilder sb = new StringBuilder();
		
		KeyObject[] keyObjectArray = new KeyObject[keys.length];		
		int parentKeyId=0;
		for(int i=0; i<keys.length; i++) {
			String parentKeyName = sb.toString();
			if(i != 0){
				sb.append(Constants.S_SEPERATOR);
			}else{
				parentKeyName = "";
			}
			sb.append(keys[i]);

			//keyId,有可能是null，不过应该非常少
			Integer keyId=
					DBMediaKeyCache.get().put(appname,sb.toString(),propertyNames,
							scopes[i].toString(), parentKeyName);

			KeyObject ko=null;
			if(keyId==null){
				throw new Exception("keyId为null"); 
			}
			ko=new KeyObject(String.valueOf(keyId), 
					scopes[i],String.valueOf(parentKeyId));
			parentKeyId=keyId;
		
			keyObjectArray[i] =ko;
		}
		
		ValueObjectBox box = new ValueObjectBox();
		for(int i=0; i<propertyNames.length; i++) {
			box.append(propertyNames[i], values[i], os[i]);
		}
		
		for(int i=0; i<keyObjectArray.length; i++) {
			//过滤scope为NO的情况
			if(keyObjectArray[i].getScope() != KeyScope.NO) {
				long t = System.currentTimeMillis();
				RequestStandard request=null;
				if(isSecond){
					request = new RequestSecondStandard(appname,hostIp, 
							collectTime, keyObjectArray[i], box);
				}else{
					request = new RequestStandard(appname,hostIp, 
							collectTime, keyObjectArray[i], box);
				}
				String key = appname+Constants.S_SEPERATOR+keyObjectArray[i].getKeyName();
				ClientUtil.invoke(key, request);
				MonitorLog.addStat(Constants.COLLECTION_APP_SEND, new String[]{appname}, new Long[]{1l,System.currentTimeMillis()-t});
				//MemcacheStandard.get().putData(request,true);
			}
		}
	} 
	
}