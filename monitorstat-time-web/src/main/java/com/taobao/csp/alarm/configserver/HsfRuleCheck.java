
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.configserver;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.common.lang.StringUtil;
import com.taobao.config.client.Subscriber;
import com.taobao.config.client.SubscriberDataObserver;
import com.taobao.config.client.SubscriberRegistrar;
import com.taobao.config.client.SubscriberRegistration;
import com.taobao.csp.alarm.rule.FlowControlRule;
import com.taobao.csp.alarm.rule.RouteRule;
import com.taobao.csp.alarm.util.StringSplitter;
import com.taobao.csp.time.util.Arith;
import com.taobao.diamond.client.DiamondSubscriber;
import com.taobao.diamond.client.impl.DiamondClientFactory;
import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListenerAdapter;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

/**
 * @author xiaodu
 *
 * 下午4:11:51
 */
public class HsfRuleCheck  extends ManagerListenerAdapter implements SubscriberDataObserver{

	private static final Logger log = Logger.getLogger(HsfRuleCheck.class);

	private static final String HEADER_FLOW_CONTROL_RULE = "flowControl@";

	private static final String HEADER_ROUTING_RULE = "Groovy_v200907@";

	private static final String SUBSCRIBER_PREFIX = "HSFSubscriber-";

	private static final String GROUP_HSF = "HSF";

	private static final String RULES_SUFFIX = ".RULES";

    private static final float THREADSHOLD_VALUE = 0.5f;

	private Subscriber subscriber;

	private SubscriberRegistration cs_registration = null;

	private DiamondManager diamondManager =null;

	private List<String> onlines = new ArrayList<String>();

	private GroovyClassLoader groovyloader = new GroovyClassLoader();

    private volatile boolean configServerRegistionFlag = false;

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	private Map<String,Class<?>> cacheGroovyClass = new ConcurrentHashMap<String, Class<?>>();

	private ConcurrentHashMap<String, Map<String, HostPo>> ipMapWithAppName = new ConcurrentHashMap<String,Map<String, HostPo>>();
	private HsfRuleInfo hsfrule;

	public HsfRuleCheck(HsfRuleInfo hsfrule){
		this.hsfrule = hsfrule;
		createRegistration();
		createDiamond();
	}


	public void update(HsfRuleInfo hsfrule){
		this.hsfrule = hsfrule;
	}


	private void createRegistration(){
        String serviceUniqueName = hsfrule.getInterfaceName() + ":" + hsfrule.getVersion();
        String cs_subscriberId = SUBSCRIBER_PREFIX + serviceUniqueName;
        cs_registration = new SubscriberRegistration(cs_subscriberId, serviceUniqueName);
        cs_registration.setGroup(GROUP_HSF);
        subscriber = SubscriberRegistrar.register(cs_registration);
        configServerRegistionFlag = true;
        subscriber.setDataObserver(this);

        log.info("configserver Registration subscriberName=" + cs_subscriberId + "dataId=" + serviceUniqueName + "");

	}

	private void createDiamond(){

		 diamondManager = new DefaultDiamondManager(GROUP_HSF, getDiamondDataId(), this);

		 log.info("diamond Registration group=" + GROUP_HSF + ", dataId=" + getDiamondDataId() + "");

	}

    private String getDiamondDataId() {
        return hsfrule.getInterfaceName() + ":" + hsfrule.getVersion() + RULES_SUFFIX;
    }


	/**
	 * 如果发生configserver推送事件 ，需要检查下configserver和diamond中的数据是否正常
	 *@author xiaodu
	 *TODO
	 */
	private void checkingDiamondGroovy(){
        String config = diamondManager.getAvailableConfigureInfomation(10000);
        StringSplitter stringSplitter = new StringSplitter(config, HEADER_FLOW_CONTROL_RULE, HEADER_ROUTING_RULE);

        if(stringSplitter.has(HEADER_ROUTING_RULE)){
            receiveConfigInfo(stringSplitter.get(HEADER_ROUTING_RULE));
        }

        if (stringSplitter.has(HEADER_FLOW_CONTROL_RULE) && stringSplitter.has(HEADER_ROUTING_RULE)) {
            receiveFlowControlRule(stringSplitter.get(HEADER_FLOW_CONTROL_RULE),stringSplitter.get(HEADER_ROUTING_RULE));
        } else if (!stringSplitter.has(HEADER_FLOW_CONTROL_RULE)){
            log.info("without flow control,appName=" + hsfrule.getAppName() + ",interface=" +  hsfrule.getInterfaceName());
        }

	}






	/* (non-Javadoc)
	 * @see com.taobao.config.client.SubscriberDataObserver#handleData(java.lang.String, java.util.List)
	 */
	@Override
	public void handleData(String dataId, List<Object> datas) {
        log.info("configserver handleData,dataId="+dataId+",datas=" + datas);
        if(!configServerRegistionFlag) {
            log.info("configserver interface listenned closed,interface=" + hsfrule.getInterfaceName());
            return;
        }
		String appName = this.hsfrule.getAppName();
		Map<String, HostPo> ipMap = ipMapWithAppName.get(appName);
		if(ipMap == null){
			ipMap = CspSyncOpsHostInfos.findHostsByOpsName(appName);
			ipMapWithAppName.putIfAbsent(appName, ipMap);
		}
		if(ipMap == null || ipMap.size() ==0){
			log.info("ip from opsfree is null, interface=" + hsfrule.getInterfaceName());
			return;
		}
		
		int offline = 0;

		List<String> serverips = new ArrayList<String>();

		for (Object provider : datas) {
			int index = ((String) provider).indexOf(":");
			String serverIp = ((String) provider).substring(0,index);
			HostPo host = ipMap.get(serverIp);
			if(host != null){
				serverips.add(serverIp);
			}else{
				offline++;
			}
		}

		onlines = serverips;
		log.info("configserver dataId："+dataId+", onlines size=" +  onlines.size());
		
		if(Arith.div(onlines.size(), ipMap.size())<0.8){
			String msg = hsfrule.getInterfaceName()+" 在configserver推送中发现有"+offline+"机器ip 列表不在opsfree 不足80%,configServer size=" +
			onlines.size() + ",opsfree size=" + ipMap.size();
			//记录日志
			log.info(msg);
			//插入告警记录表
			storeTheAlarmRecord(ipMap.size(),onlines.size());
			//发送旺旺消息
			ConfigServerListen.sendAlarm("接口服务configserver推送",msg);
		}

		checkingDiamondGroovy();
	}

	//插入告警记录
	private void storeTheAlarmRecord(int wholeSize,int aliveSize){
		List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		// /插入一条TP的告警记录
		CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
		po.setMode_name("阀值");
		po.setKey_scope("APP");
		po.setApp_name("configserver");
		po.setKey_name("configserver推送地址列表过少");
		po.setProperty_name("count");
		po.setAlarm_cause("configserver推送地址列表过少");
		po.setAlarm_time(new Timestamp(new Date().getTime()));
		po.setAlarm_value("configSize" + aliveSize + ",wholeSize=" + wholeSize);
		po.setIp("172.24.168.111");  //随意写的一台tp机器的ip
		list.add(po);
		CspTimeKeyAlarmRecordAo.get().insert(list);
	}

    

	/* (non-Javadoc)
	 * @see com.taobao.diamond.manager.ManagerListener#receiveConfigInfo(java.lang.String)
	 */
	@Override
	public void receiveConfigInfo(String configInfo) {
		log.info("diamond dataId=" + hsfrule.getInterfaceName() + ",groovy is" + configInfo);
		if(CollectionUtils.isEmpty(onlines)){
			log.info("diamond dataId：" + hsfrule.getInterfaceName()  + ",configserver data is null");
			return;
		}
		try {
           	String groovyStr = parseGroovy(configInfo);
           	Class<?> clazz = cacheGroovyClass.get(groovyStr);
           	 if(clazz == null){
           		clazz = groovyloader.parseClass(groovyStr);
           		cacheGroovyClass.put(groovyStr, clazz);
           	 }
           	GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
           	
           	String report = validateInterfaceRules(groovyObject, onlines, configInfo);
           	if(StringUtil.isNotBlank(report)){
           		StringBuffer sb = new StringBuffer();
           		sb.append("diamond dataId=" + getDiamondDataId()).append(report);
           		ConfigServerListen.sendAlarm("接口路由规则中配置问题", sb.toString());
           	}
           	
           	//校验这个接口的方法级别的路由信息
            Class<?> serviceInterface;
            try {
                serviceInterface = Class.forName( hsfrule.getInterfaceName());
            } catch (ClassNotFoundException e) {
            	log.error("[Address Component] Interface class not found,dataId= " + getDiamondDataId(), e);
                return;
            }

            List<String> allMethodSigs = new ArrayList<String>();
            for (Method m : serviceInterface.getMethods()) {
                allMethodSigs.add(RouteRule.joinMethodSigs(m));
            }
            
            //csp这里不配置到方法级别,自动检测这个接口内的所有方法 和参数级别的路由方式

            /**
             * 获得方法级和参数级路由规则
             */
            Method m_mathodRoutingRule = getMethod(clazz, "mathodRoutingRule", new Class[] { String.class,
                    String[].class });
            Method m_argsRoutingRule = getMethod(clazz, "argsRoutingRule", new Class[] { String.class, String[].class });


            for (String m : allMethodSigs) {
                Object[] args = getMethodSigArgs(m);
                if (m_mathodRoutingRule != null) {
                	//这里面每个都要校验
                	String checkResult = validateMethodRules(groovyObject,onlines,m_mathodRoutingRule,args);
                	if(StringUtil.isNotBlank(checkResult)){
                		StringBuffer sb = new StringBuffer();
                   		sb.append("diamond dataId=" + getDiamondDataId()).append(report);
                   		log.info(sb.toString());
                   		ConfigServerListen.sendAlarm("接口路由规则中配置问题", report);
                	}
                }
                if (m_argsRoutingRule != null) {
                	//TODO  先不校验参数级别的
                }
            }
			} catch (Exception e) {
				log.warn("async validateRoutingRules exception,dataId=" + getDiamondDataId()  + ",configInfo=" + configInfo,e);
			}

	}

    private Object[] getMethodSigArgs(String m) {
    	  String[] methodSigs = ((String) m).split(RouteRule.METHOD_SIGS_JOINT_MARK);
          String[] paramTypeNames = new String[methodSigs.length - 1];
          System.arraycopy(methodSigs, 1, paramTypeNames, 0, paramTypeNames.length);
          return new Object[] { methodSigs[0], paramTypeNames };
	}


	private void receiveFlowControlRule(String rawFlowControlRule,String rawConfigInfo) {
        log.warn("diamond dataId=" + hsfrule.getInterfaceName() + ",flow control rule is" + rawFlowControlRule);
        try {
            FlowControlRule flowControlRule = handleFlowControlRuleData(rawFlowControlRule);
            if (flowControlRule == null) {
                return;
            }
            List<String> allAvailableAddresses = onlines;

            Map<String,List<String>> segmentFromConfigServerMap = splitIpsInSameSegment(onlines);
            
            //可能为空，这里面可以一次校验 最前面的map的数据，不涉及具体的interface method args
            Map<String, List<String>> ipsFromDiamondAll = getRulesFormDiamond(rawConfigInfo);
            
            //每个机房循环判断一次, map是configserver推送的地址
            for(Map.Entry<String, List<String>> entry : segmentFromConfigServerMap.entrySet()) {
                List<String> ipInSameSegmentsen = entry.getValue();
                List<String> availableAddresses;
                if (ipInSameSegmentsen.size() > 0
                        && flowControlRule.isLocalPreferred(allAvailableAddresses.size(), ipInSameSegmentsen.size())) {
                    availableAddresses = Collections.unmodifiableList(ipInSameSegmentsen);
                    //满足if条件，则证明采用了本地机房的容灾规则
                    //将这个与diamond的推送求交集，看符合groovy脚本的ip还有多少在这个列表里面
                    //如果符合条件的少于总的20%,则插入告警记录，并且发送旺旺消息
                    //ipsFromDiamondAll  <G1,<所有G1组的机器ip列表的正则表达式>>
                    for(Map.Entry<String, List<String>> innnerEntry:ipsFromDiamondAll.entrySet()){
                    	List<String> ipFromDiamond = innnerEntry.getValue();
                    	//这种情况是    "ALL":["*"], 判断没有意义
                    	if(ipFromDiamond.size() == 1){
                    		continue;
                    	}
                    	HashSet<String> contain = new HashSet<String>();
                    	//找出在diamon推送的机器列表里面存活的机器列表
                		for (int i = 0; i < ipFromDiamond.size(); i++) {
                			String rule = ruleReplace(ipFromDiamond.get(i));
                			Pattern p = Pattern.compile(rule);
                			for (String ip : availableAddresses) {
                				if (p.matcher(ip).matches()) {
                					contain.add(ip);
                				}
                			}
                		}
                		//contain保存的是G1组的，某个机房的存活的机器列表， ipFromDiamond.size()  是总共的diamond推送的机器ip列表的个数
                		if(Arith.div(contain.size(), ipFromDiamond.size())<=0.3){
                			String msg = hsfrule.getInterfaceName()+" flow control,"+ (ipFromDiamond.size() - contain.size() )
                			+" ips not alive，more than 30%,online=" + contain.size() + ",diamond size=" + ipFromDiamond.size() + "," + getDiamondDataId();
                			log.info(msg);
                			storeTheAlarmRecord(ipFromDiamond.size(),contain.size());
                			ConfigServerListen.sendAlarm("接口服务configserver推送",msg);
                		}else{
                			log.info(getDiamondDataId()+" flow control,"+ (ipFromDiamond.size() - contain.size() )
                        			+" ips not alive,less than 30%,online=" + contain.size() + ",diamond size=" + ipFromDiamond.size() + "," + getDiamondDataId());
                		}
                    }
                } else {
                    availableAddresses = Collections.unmodifiableList(allAvailableAddresses);
                }
                StringBuilder sb = new StringBuilder();
                //记录日志，判断是否某个接口的调用启用了本地机房容灾规则
                sb.append("diamond dataId=").append(getDiamondDataId())
                        .append(",all amount=")
                        .append(onlines.size())
                        .append(",local size=")
                        .append(availableAddresses.size());
                log.warn(sb.toString());
                
            }
        } catch (Exception e) {
            log.error("InterfaceName :" + hsfrule.getInterfaceName() + " receiveFlowControlRule error", e);
        }

    }

    private Map<String,List<String>> splitIpsInSameSegment(List<String> onlines) {
        Map<String, List<String>> segmentMap= new HashMap<String, List<String>>();
        for(String ip : onlines) {
            int index = ip.indexOf(".", ip.indexOf(".") + 1);
            String key = ip.substring(0, index);
            List <String> ips = segmentMap.get(key);
            if(ips == null) {
                List<String> values = new ArrayList<String>();
                values.add(ip);
                segmentMap.put(key, values);
            } else {
                ips.add(ip);
            }
        }
        return segmentMap;
    }

    private  Map<String, List<String>>  getRulesFormDiamond(String rawConfigInfo) {
        try {
            String groovyStr = parseGroovy(rawConfigInfo);
            GroovyClassLoader loader = new GroovyClassLoader();
            Class<?> clazz = loader.parseClass(groovyStr);
            GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
            Map<String, List<String>> map = (Map<String, List<String>>) groovyObject.invokeMethod("routingRuleMap", null);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


	private String parseGroovy(String configInfo) throws IOException{
		BufferedReader reader = new BufferedReader(new StringReader(configInfo)) ;
    	String line = null;
    	StringBuffer groovyStr = new StringBuffer();
    	while((line = reader.readLine())!=null){
    		if(line.indexOf(HEADER_ROUTING_RULE)>-1){
    			continue;
    		}
    		if(line.indexOf(HEADER_FLOW_CONTROL_RULE)>-1){
    			break;
    		}
    		groovyStr.append(line).append("\n");
    	}
    	return groovyStr.toString();
	}

	/**
	 *
	 *@author xiaodu
	 * @param groovyObject
	 * @param ipMap
	 * @return
	 *TODO
	 */
	private String validateInterfaceRules(GroovyObject groovyObject, List<String> ips, String configInfo){

		Map<String, List<String>> map = (Map<String, List<String>>)groovyObject.invokeMethod("routingRuleMap", null);

		String interfaceRuleNames = (String)groovyObject.invokeMethod("interfaceRoutingRule", null);
		List<String> ruleslist = map.get(interfaceRuleNames);
       
        if(ruleslist == null){
			return null;
		}
       
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("通过配置") .append(configInfo).append("获得的机器列表为：[");
        for(String rule : ruleslist) {
            stringBuilder.append(rule).append(",");
        }
        stringBuilder.append("]");
        log.info(stringBuilder.toString());
		
		Set<Integer> contain = new HashSet<Integer>();
		Set<String> notContained = new HashSet<String>();
		for (int i = 0; i < ruleslist.size(); i++) {
			String rule = ruleReplace(ruleslist.get(i));
			Pattern p = Pattern.compile(rule);
			boolean isAlive =false;
			for (String ip : ips) {
				if (p.matcher(ip).matches()) {
					contain.add(i);
					isAlive = true;
				}
			}
			 if(!isAlive){
             	notContained.add(rule);
             }
		}
		if(CollectionUtils.isEmpty(notContained)){
			log.info("validateInterfaceRules all ip is alive," + getDiamondDataId()) ;
			return null;
		}
		//校验这个接口存活的是否满足80%
		if(Arith.div(contain.size(), ruleslist.size())>=0.8){
			log.info("validateInterfaceRules at least 80% ip is alive," +  getDiamondDataId());
			return null;
		}
		//不满足80%的情况，进行告警，发送旺旺消息
		//插入告警记录，contain 标识存活的，在configserver的地址列表内
		storeTheAlarmRecord(ruleslist.size(),contain.size());
		
		StringBuffer sb = new StringBuffer();
        for (String rule : notContained) {
            sb.append(rule).append(",");
        }
		return ("在路由中的ip 在configserver 配置中上查询不到:" + sb.toString());
	}

	/**
	 *
	 *@author xiaodu
	 * @param groovyObject
	 * @param ipMap
	 * @param methodName
	 * @param paramTypeStrs  这个参数使用，分割
	 * @return
	 *TODO
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	private String validateMethodRules(GroovyObject groovyObject, List<String> ips,  Method m, Object[] args) throws Exception{

		Map<String, List<String>> map = (Map<String, List<String>>)groovyObject.invokeMethod("routingRuleMap", null);
		Object methodRuleNames = m.invoke(groovyObject, args);
		List<String> ruleslist = map.get(methodRuleNames);
        StringBuilder stringBuilder = new StringBuilder();
        
        if(CollectionUtils.isEmpty(ruleslist)){
        	return null;
        }
        StringBuilder argsString = new StringBuilder();
        for(int i=0;i<args.length;i++){
        	argsString.append(args[i]);
        }
      
        stringBuilder.append("通过方法：").append(argsString.toString()).append("获得的机器列表为：[");
        for(String rule : ruleslist) {
            stringBuilder.append(rule).append(",");
        }
        stringBuilder.append("]");

        log.info(stringBuilder.toString());
		Set<Integer> contain = new HashSet<Integer>();
		Set<String> notContained = new HashSet<String>();
        
		for (int i = 0; i < ruleslist.size(); i++) {
                String rule = ruleReplace(ruleslist.get(i));
                Pattern p = Pattern.compile(rule);
                boolean isAlive = false;
                for (String ip : ips) {
                    if (p.matcher(ip).matches()) {
                        contain.add(i);
                        isAlive = true;
                    }
                }
                if(!isAlive){
                	notContained.add(rule);
                }
            }
        	
        	if(CollectionUtils.isEmpty(notContained)){
        		log.info("all ip address in diamond groovy is alive,argsString=" + argsString + ","+ getDiamondDataId());
        		return null;
        	}
        	
        	//校验这个接口存活的是否满足80%
    		if(Arith.div(contain.size(), ruleslist.size())>=0.8){
    			log.info("validateInterfaceRules at least 80% ip is alive,argsString=" + argsString + "," + getDiamondDataId());
    			return null;
    		}
        	
            //插入告警记录
    		storeTheAlarmRecord(ruleslist.size(),contain.size());
			StringBuffer sb = new StringBuffer();
            for (String rule : notContained) {
                sb.append(rule).append(",");
            }
			return (m.getName()+"[路由中的ip 在configserver上查询不到:"+sb.toString());

		}

    private FlowControlRule handleFlowControlRuleData(String flowControlRuleData) {
        DocumentBuilder documentBuilder = null;
        FlowControlRule flowControlRule = new FlowControlRule();
        try {
            documentBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("error create document builder!", e);
        }
        int beginIndex = flowControlRuleData.indexOf('@');
        String rule = flowControlRuleData.substring(beginIndex + 1);
        InputStream is = new ByteArrayInputStream(rule.getBytes());
        try {
            Document doc = documentBuilder.parse(is);
            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();


            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) { // 忽略非element结点
                    continue;
                }

                if (FlowControlRule.TAG_LOCALPREFERRED.equals(node.getNodeName())) {
                    String localPreferredSwitch = node.getTextContent().trim();
                    if (!FlowControlRule.SWITCH_ON.equals(localPreferredSwitch)
                            && !FlowControlRule.SWITCH_OFF.equals(localPreferredSwitch)) {
                        log.warn("[FlowControl Parser] Invalid isLocalPreferred valie! set 'off' as default.");
                        flowControlRule.setLocalPreferredSwitch(FlowControlRule.SWITCH_OFF);
                    } else {
                        flowControlRule.setLocalPreferredSwitch(localPreferredSwitch);
                    }
                } else if (FlowControlRule.TAG_THRESHOLD.equals(node.getNodeName())) {
                    String threshold = node.getTextContent().trim();
                    if (threshold == null || threshold.length() == 0) {
                        log.warn("[FlowControl Parser] Invalid threshold value! set 0.0F as default.");
                    } else {
                        float value = Float.valueOf(threshold);
                        flowControlRule.setThreshold(value);
                    }
                } else { // 当出现未知的标签结点时，解析失败
                    log.warn("[FlowControl Parser] Unsupported node: " + node);
                    return null;
                }
            }
            return flowControlRule;
        } catch (NumberFormatException e) {
            log.warn("[FlowControl Parser] Threshold must be a number value.", e);
            return null;
        } catch (Exception e) {
            log.error("handleFlowControlRuleData error.", e);
        }
        return null;
    }

	private String ruleReplace(String rule){
		rule = rule.replaceAll("\\*", "\\.\\*");
		rule = rule.replaceAll("\\.", "\\\\.");
		rule = rule.replaceAll("\\?", "\\.{1}");
		return rule;
	}

    public void removeRegistration() {
        DiamondSubscriber diamondSubscriber = DiamondClientFactory.getSingletonDiamondSubscriber();
        diamondSubscriber.removeDataId(getDiamondDataId());
        configServerRegistionFlag = false;
    }

    private  Method getMethod(Class<?> c, String name, Class<?>... parameterTypes)
    throws RouteRuleParserException {
    	try {
    		return c.getMethod(name, parameterTypes);
    		} catch (SecurityException e) {
    			throw new RouteRuleParserException("获取方法失败。方法名：" + name, e);
    			} catch (NoSuchMethodException e) {
    				return null;
    				}
    		}

}
