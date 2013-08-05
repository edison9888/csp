
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.configserver;

import com.taobao.csp.common.ZKClient;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.*;

/**
 * @author xiaodu
 * 
 *这个线程是监听configserver 推送信息,通过检查对应的ip 是否在opf上面
 *
 *监控规则为:
 *先监控在configserver推送的内容中的ip 是否在opsfree上的ip
 *然后使用 这个ip 判断diamond中的配置是否存在
 *
 * 下午2:05:17
 */
public class ConfigServerListen {
	
	private static final Logger log = Logger.getLogger(ConfigServerListen.class);
	
	
	private static MessageSend messageSend = MessageSendFactory.create(MessageSendType.WangWang);
	
	private static String HSF_CONFIGSERVICE_NODE = "/csp/monitor/configserver";
	
	private static List<String> acceptList = Arrays.asList("小赌","尊严","游骥","中亭","白雁", "书全");
	
	private static ConfigServerListen listen = null;
	
	private Map<HsfRuleInfo,HsfRuleCheck> checkMap = new HashMap<HsfRuleInfo, HsfRuleCheck>();
	
	
	static void sendAlarm(String title,String info){
		for(String a:acceptList)
			messageSend.send(a, title, info);
	}
	
	public static synchronized void startup(){
		if(listen == null){
			listen = new ConfigServerListen();
		}
	}
	
	public static void addHsfRuleInfo(HsfRuleInfo info){
		ZKClient.get().mkdirPersistent(HSF_CONFIGSERVICE_NODE+"/"+info.getInterfaceName(),info);
		
		
		log.info("新增 configserver listen："+info.toString());
		
	}
	
	
	public static List<HsfRuleInfo> getHsfRuleInfos(){
		List<String> hsfList = ZKClient.get().list(HSF_CONFIGSERVICE_NODE);
		
		 List<HsfRuleInfo> list = new ArrayList<HsfRuleInfo>();
		
		for(String hsf:hsfList){
			HsfRuleInfo obj = (HsfRuleInfo)ZKClient.get().getData(HSF_CONFIGSERVICE_NODE+"/"+hsf);
			list.add(obj);
		}
		return list;
	}
	
	private ConfigServerListen(){
		resetConfigserverHsf();
	}
	
	private void  resetConfigserverHsf(){
		List<String> hsfList = ZKClient.get().list(HSF_CONFIGSERVICE_NODE,new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				switch(event.getType()){
				case NodeChildrenChanged:
					resetConfigserverHsf();
				break;
				}
			}
		});
		for(String hsf:hsfList){
			HsfRuleInfo obj = (HsfRuleInfo)ZKClient.get().getData(HSF_CONFIGSERVICE_NODE+"/"+hsf);
			registListen(obj);
		}
	}
	
	
	private void registListen(HsfRuleInfo obj){
		HsfRuleCheck check = checkMap.get(obj);
		if(check != null) {
            check.removeRegistration();
            checkMap.remove(obj);
        }
        check = new HsfRuleCheck(obj);
        checkMap.put(obj, check);
	}
	

	public static void main(String agrs[]){
		//启动configserver的订阅
		startup();
	}
	
	
	

}
