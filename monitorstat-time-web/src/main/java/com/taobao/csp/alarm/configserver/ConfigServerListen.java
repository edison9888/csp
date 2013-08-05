
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
 *����߳��Ǽ���configserver ������Ϣ,ͨ������Ӧ��ip �Ƿ���opf����
 *
 *��ع���Ϊ:
 *�ȼ����configserver���͵������е�ip �Ƿ���opsfree�ϵ�ip
 *Ȼ��ʹ�� ���ip �ж�diamond�е������Ƿ����
 *
 * ����2:05:17
 */
public class ConfigServerListen {
	
	private static final Logger log = Logger.getLogger(ConfigServerListen.class);
	
	
	private static MessageSend messageSend = MessageSendFactory.create(MessageSendType.WangWang);
	
	private static String HSF_CONFIGSERVICE_NODE = "/csp/monitor/configserver";
	
	private static List<String> acceptList = Arrays.asList("С��","����","����","��ͤ","����", "��ȫ");
	
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
		
		
		log.info("���� configserver listen��"+info.toString());
		
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
		//����configserver�Ķ���
		startup();
	}
	
	
	

}
