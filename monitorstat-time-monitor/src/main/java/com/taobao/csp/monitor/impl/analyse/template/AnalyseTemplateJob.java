package com.taobao.csp.monitor.impl.analyse.template;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.MonitorLog;
import com.taobao.monitor.common.po.RowAnalyseConfigWeb;
import com.taobao.monitor.common.util.BufferedReader2;
import com.taobao.monitor.common.util.CommonUtil;

public class AnalyseTemplateJob extends AbstractDataAnalyse {
	private static final Logger logger = Logger.getLogger(AnalyseTemplateJob.class);

	public static final String KEY_PREFIX = "key_";
	public static final String SEPERATOR = "`";

	RowAnalyseConfig[] realConfigArray = null;
	public static Set<String> filterSet = new HashSet<String>(); 	//双11为notify增加过滤 
	static {
		//为双11增加的过滤
//		filterSet.add("postedRet_total_by_group");
//		filterSet.add("postedRet_S_by_group");
//		filterSet.add("postedRet_NH_by_group");
//		filterSet.add("postedRet_EC_by_group");
//		filterSet.add("postedRet_TO_by_group");
//		filterSet.add("postedRet_UK_by_group");
//		filterSet.add("postedRet_F_by_group");
//		filterSet.add("postedRet_E_by_group");
//		filterSet.add("postedRet_TPB_by_group");
//		filterSet.add("postedRet_NL_by_group");
//		filterSet.add("output_OverFlow_by_group");

		filterSet.add("postedRet_total_by_topic");
		filterSet.add("postedRet_S_by_topic");
		filterSet.add("postedRet_NH_by_topic");
		filterSet.add("postedRet_EC_by_topic");
		filterSet.add("postedRet_TO_by_topic");
		filterSet.add("postedRet_UK_by_topic");
		filterSet.add("postedRet_F_by_topic");
		filterSet.add("postedRet_E_by_topic");
		filterSet.add("postedRet_TPB_by_topic");
		filterSet.add("postedRet_NL_by_topic");
		filterSet.add("output_OverFlow_by_topic");
	}
	
	public AnalyseTemplateJob(String appName, String ip,String feature) throws Exception {
		super(appName, ip, feature);

		String strLog = String.format("传入参数，初始化解析器->appName=%s,ip=%s,feature=%s", appName,ip,feature);
		//logger.info(strLog);

		RowAnalyseConfigWeb[] array = null;
		try {
			array = CommonUtil.getObjectArrayByJsonStr(
					RowAnalyseConfigWeb.class, feature);
		} catch (Exception e) {
			logger.info(strLog);
			logger.error("", e);
		}
		if(array == null) {
			try {
				RowAnalyseConfigWeb po = CommonUtil.getObjectByJsonStr(
						RowAnalyseConfigWeb.class, feature);
				array = new RowAnalyseConfigWeb[]{po};
			} catch (Exception e) {
				logger.info(strLog);
				logger.error("", e);
			}
		}
		if(array == null) {
			logger.info(strLog);
			logger.error("配置的json有问题->" + feature);
			throw new Exception("配置的json有问题->" + feature);
		}
		//logger.info("json解析成功，生成对象");

		Arrays.sort(array);

		realConfigArray = new RowAnalyseConfig[array.length];
		for(int i=0; i<array.length; i++) {
			RowAnalyseConfig config = new RowAnalyseConfig();
			realConfigArray[i] = config;

			RowAnalyseConfigWeb webConfig = array[i];
			String operateString = webConfig.getLogConfigWeb();
			Pattern p =Pattern.compile("\\{([A-Za-z|\\d|_|\\-]+)}");
			Matcher m = p.matcher(webConfig.getLogConfigWeb());

			Map<String, KeyScope> keyScopeMap = new HashMap<String, KeyScope>();
			Map<String, ValueOperate> valueOperateMap = new HashMap<String, ValueOperate>();			
			Map<Integer, String> indexMap = new HashMap<Integer, String>();
			int iIndex=0;
			while(m.find()) {
				String key = m.group(0);
				String keySimple = m.group(1);	//匹配后同时去掉两个大括号{}
				//if(keySimple.startsWith(KEY_PREFIX))
				//	operateString = operateString.replace(key, "([A-Za-z|\\d|_|.|-|:]+)");
				//else
				//	operateString = operateString.replace(key, "(\\d+)");
				operateString = operateString.replace(key, "([A-Za-z|\\d|_|.|\\-|:]+)");
				indexMap.put(1 + iIndex++, keySimple);	//后面正则匹配，默认从1开始。
			}
			operateString = operateString.replaceAll(" ", "\\\\s+");

			int valueNum = webConfig.getValueNumber();
			int keyNum = webConfig.getKeyNumber();

			if((valueNum + keyNum + 1) != iIndex) {
				//+1 表示{datetime}这个参数
				logger.error("用户输入的参数个数与实际解析的参数不等！iIndex=" + iIndex 
						+ ";valueNum=" + valueNum + ";keyNum=" + keyNum);
				continue;
			}

			config.setTimeFormat(webConfig.getTimeFormat());
			config.setLogConfigWeb(webConfig.getLogConfigWeb());			
			config.setRegularString(operateString);
			config.setIndexMap(indexMap);
			config.setKeyScopeMap(keyScopeMap);
			config.setValueOperateMap(valueOperateMap);

			//如: key1,key2,key3;key3,key2,key1
			String orderConfig = webConfig.getKeyOrder();
			String[] orderArray = orderConfig.split(";");
			List<String>[] orderListArray = new List[orderArray.length];
			int yIndex = 0;
			for(String order : orderArray) {
				String[] orderTmpArray = order.split(",");
				List<String> list = Arrays.asList(orderTmpArray);
				orderListArray[yIndex++] = list;
			}
			config.setOrderArray(orderListArray);

			//key,scope,例如: key1,NO;key2,APP;key3,ALL;key4,HOST;
			String keyConfig = webConfig.getKeyConfig();
			String[] keyConfigArray = keyConfig.split(";");
			for(String tmp: keyConfigArray) {
				String[] tmpArray = tmp.split(",");
				if(tmpArray.length != 2) {
					logger.error("keyConfig配置信息错误，tmp个数不为2,tmp=" + tmp);
					continue;
				}
				keyScopeMap.put(tmpArray[0], KeyScope.valueOf(tmpArray[1]));
			}			

			//property,operate,例如: E-times,ADD;C-times,REPLACE
			String valueConfig = webConfig.getValueConfig();
			String[] valueConfigArray = valueConfig.split(";");
			for(String tmp: valueConfigArray) {
				String[] tmpArray = tmp.split(",");
				if(tmpArray.length != 2) {
					logger.error("valueConfig配置信息错误，tmp个数不为2,tmp=" + tmp);
					continue;
				}
				valueOperateMap.put(tmpArray[0], ValueOperate.valueOf(tmpArray[1]));
			}
			
			//关于RootConfig,发送的根节点
			String rootConfig = webConfig.getRootKeyConfig();
			if(rootConfig != null && !rootConfig.trim().equals("")) {
				String[] tmpArray = rootConfig.split(",");
				if(tmpArray.length == 2) {
					config.setBaseKey(tmpArray[0]);
					config.setBaseKeyScope(KeyScope.valueOf(tmpArray[1]));
				} else {
					logger.error("rootConfig没有配置或配置错误->" + rootConfig);					
				}
			}
		}
	}

	@Override
	public void analyseOneLine(String line) {
		/**
		 * 日志格式:
		 * [2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16
		 */
		if(line == null || realConfigArray == null || realConfigArray.length == 0) {
			return;
		} else if(line.startsWith("[")) {	//去掉日志前面的[2012-07-12 16:12:52]
			int index = line.indexOf("]");
			if(index > 0 && index != line.length() - 1) {
				line = line.substring(index + 1,line.length()).trim();				
			}
		}

		for(RowAnalyseConfig realConfig : realConfigArray) {
			Pattern pattern = realConfig.getPattern();
			Matcher matcher = pattern.matcher(line);
			if(matcher.find()) {
				int iCount = matcher.groupCount();
				Map<Integer, String> indexMap = realConfig.getIndexMap(); 				

				if(iCount != indexMap.size()) {
					logger.error(String.format("group count不匹配,iCount=%d,indexMap.size()=%d", 
							iCount,indexMap.size()));
					continue;
				}

				Map<String, String> oneTimeRecordMap = new HashMap<String, String>();
				for(int i=1; i<=iCount; i++) {
					String value = matcher.group(i);
					String key = indexMap.get(i);
					oneTimeRecordMap.put(key, value);
					//map结构：key,value. key是用户配置正则的名称，value是日志中对应的真实值
				}

				//使用真实的值，拼接成key值
				List<String>[] orderArray = realConfig.getOrderArray();
				for(List<String> orderList : orderArray) {
					KeyScope[] scopeArray = new KeyScope[orderList.size()];

					StringBuilder sb = new StringBuilder();
					int i = 0;
					for(String key : orderList) {
						sb.append(oneTimeRecordMap.get(key)).append(SEPERATOR);
						scopeArray[i++] = realConfig.getKeyScopeMap().get(key);
					}
					if(sb.length() >=1) {
						sb.deleteCharAt(sb.length() - 1);
					}
					String keyTmp = sb.toString();
					try {
						ValuePo po = new ValuePo(keyTmp, scopeArray);
						realConfig.addValueToMap(po, oneTimeRecordMap);
					} catch (Exception e) {
						logger.error("添加信息异常->",e);
					}
				}
				break;	//匹配一次就跳出
			}  
		}
	}
	@Override
	public void submit() {
		for(RowAnalyseConfig realConfig : realConfigArray) {
			realConfig.submitValue();
		}
	}

	@Override
	public void release() {
		for(RowAnalyseConfig realConfig : realConfigArray) {
			realConfig.clearValueMap();
		}
	}

	public class LogConfig {
		public char LINE_SEPERATOR = '\n';
		public char KEY_SEPERATOR = '`';
		public String LOG_SEPEATOR_STRING = " ";	//日志中的空白

		private List<RowAnalyseConfig> rowAnalyseList = new ArrayList<RowAnalyseConfig>();

		public List<RowAnalyseConfig> getRowAnalyseList() {
			return rowAnalyseList;
		}

		public void setRowAnalyseList(List<RowAnalyseConfig> rowAnalyseList) {
			this.rowAnalyseList = rowAnalyseList;
		}

		public void addRowAnalyseConfig(RowAnalyseConfig rowAnalyseConfig) {
			rowAnalyseList.add(rowAnalyseConfig);
		}
	}	

	public class RowAnalyseConfig {
		private String timeFormat;	//默认 为空，表示毫秒数

		private String logConfigWeb;
		private String regularString;
		private String baseKey = null;
		private KeyScope baseKeyScope = null;
		
		private Pattern pattern = null;

		private Map<Integer, String> indexMap = new HashMap<Integer, String>();
		private Map<String, KeyScope> keyScopeMap = new HashMap<String, KeyScope>();
		private Map<String, ValueOperate> valueOperateMap = new HashMap<String, ValueOperate>();
		private Map<Long, Map<String, ValuePo>> timeValueMap = new HashMap<Long, Map<String, ValuePo>>();

		private List<String>[] orderArray = null;
		
		public String getBaseKey() {
			return baseKey;
		}
		public void setBaseKey(String baseKey) {
			this.baseKey = baseKey;
		}
		public KeyScope getBaseKeyScope() {
			return baseKeyScope;
		}
		public void setBaseKeyScope(KeyScope baseKeyScope) {
			this.baseKeyScope = baseKeyScope;
		}
		public String getLogConfigWeb() {
			return logConfigWeb;
		}
		public void setLogConfigWeb(String logConfigWeb) {
			this.logConfigWeb = logConfigWeb;
		}
		public String getRegularString() {
			return regularString;
		}
		public void setRegularString(String regularString) {
			this.regularString = regularString;
		}
		/**
		 * 有缓存
		 * @return
		 */
		public Pattern getPattern() {	//throws an Exception
			if(pattern == null) {
				pattern = Pattern.compile(regularString);	//发生变化	
			}
			return pattern;
		}

		public Pattern getPatternNew() {
			pattern = Pattern.compile(regularString);	
			return pattern;
		} 

		public void setPattern(Pattern pattern) {
			this.pattern = pattern;
		}
		public String getTimeFormat() {
			return timeFormat;
		}
		public void setTimeFormat(String timeFormat) {
			this.timeFormat = timeFormat;
		}
		public Map<Integer, String> getIndexMap() {
			return indexMap;
		}
		public void setIndexMap(Map<Integer, String> indexMap) {
			this.indexMap = indexMap;
		}
		public Map<String, KeyScope> getKeyScopeMap() {
			return keyScopeMap;
		}
		public void setKeyScopeMap(Map<String, KeyScope> keyScopeMap) {
			this.keyScopeMap = keyScopeMap;
		}
		public Map<String, ValueOperate> getValueOperateMap() {
			return valueOperateMap;
		}
		public void setValueOperateMap(Map<String, ValueOperate> valueOperateMap) {
			this.valueOperateMap = valueOperateMap;
		}
		public List<String>[] getOrderArray() {
			return orderArray;
		}
		public void setOrderArray(List<String>[] orderArray) {
			this.orderArray = orderArray;
		}

		/**
		 * 添加一行数据，并更加属性的配置做简单的操作，目前支持“REPLACE”和“ADD”
		 * @param keyName
		 * @param oneTimeRecordMap
		 * @throws Exception 
		 */
		public void addValueToMap(ValuePo valuePo, Map<String, String> oneTimeRecordMap) throws Exception {
			String time = oneTimeRecordMap.get("datetime");
			String format = getTimeFormat();

			Calendar cal = Calendar.getInstance();
			long timeL = 0;
			Date date = null;
			if(format == null || format.trim().equals("") || format.toLowerCase().equals("long")) {
				timeL = Long.parseLong(time);	//去掉秒和毫秒，同步缓存
				date = new Date(timeL);
			} else {	//如果有配置日期的格式化信息，则格式化
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				date = sdf.parse(time);
			}
			cal.setTime(date);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			timeL = cal.getTimeInMillis();			

			if(timeValueMap.size() > 0 && !timeValueMap.containsKey(timeL)) {
				submitValue();	//来新一分钟的数据，就发送数据，并马上清空map
			}
			
			Map<String, ValuePo> valueMap = timeValueMap.get(timeL); 
			if(valueMap == null) {
				valueMap = new HashMap<String, ValuePo>();
				timeValueMap.put(timeL, valueMap);
			}
			String keyName = valuePo.getKeyName();

			if(!valueMap.containsKey(keyName)) {
				valuePo.setArray(new Object[valueOperateMap.size()]);
				valueMap.put(keyName,valuePo);
			}
			Object[] objArray = valueMap.get(keyName).getArray();
			int i=0;
			for(Entry<String, ValueOperate> entry : valueOperateMap.entrySet()) {
				String propertyName = entry.getKey();
				ValueOperate operate = entry.getValue();
				String realVale = oneTimeRecordMap.get(propertyName);
				if(realVale == null)
					realVale = "0";
				if(operate == ValueOperate.REPLACE) {
					objArray[i] = realVale;
				} else if(operate == ValueOperate.ADD){	//|| operate == ValueOperate.AVERAGE
					if(objArray[i] == null)
						objArray[i] = Long.parseLong(realVale);
					else	//FIXME 现在采集默认都是Long
						objArray[i] = Long.parseLong(realVale) + (Long)objArray[i];
				} else {
					logger.error("操作" + operate.toString() + "目前不支持");
				}
				i++;
			}
		}

		public void submitValue() {
			
			for(Entry<Long, Map<String, ValuePo>> entry: timeValueMap.entrySet()) {
				long time = entry.getKey();
				Map<String, ValuePo> valueMap = entry.getValue();
				for(Entry<String, ValuePo> valueEntry : valueMap.entrySet()) {
					ValuePo po = valueEntry.getValue();
					String[] propertyArray = new String[po.getArray().length];
					ValueOperate[] operateArray = new ValueOperate[po.getArray().length];
					int j=0;
					for(Entry<String,ValueOperate> entryValue : valueOperateMap.entrySet()) {
						propertyArray[j] = entryValue.getKey();
						operateArray[j] = entryValue.getValue();
						j++;
					}
					
					//					System.out.println(String.format("getAppName()=%s,getIp()=%s,time=%s", getAppName(),getIp(),time));
					//					System.out.println(Arrays.toString(keyNameArray));
					//					System.out.println(Arrays.toString(po.getKeyScopeArray()));
					//					System.out.println(Arrays.toString(propertyArray));
					//					System.out.println(time + "\t" + po.getKeyName());
					//					System.out.println(Arrays.toString(po.getArray()));
					//					System.out.println(Arrays.toString(operateArray));
					//					System.out.println("*************");
					
					String keyFull = valueEntry.getKey();
					KeyScope[] keyScopeArray = null;
					final KeyScope[] keyArray = po.getKeyScopeArray();
					
					if(baseKey != null && baseKeyScope != null && keyArray != null) {
						keyFull = baseKey + SEPERATOR + keyFull;
						keyScopeArray = new KeyScope[keyArray.length + 1];
						int m=1;
						keyScopeArray[0] = baseKeyScope;
						for(KeyScope scope : keyArray) {
							keyScopeArray[m++] = scope;
						}
					} else {
						keyScopeArray = keyArray;
					}
					String[] keyNameArray = keyFull.split(AnalyseTemplateJob.SEPERATOR);
					
					//FIXME 为双11，去掉对key3的监控
					if(keyNameArray.length == 4 && keyScopeArray.length == 4) {
						if(keyNameArray[0].equals("Notify-provider") 
								&& AnalyseTemplateJob.filterSet.contains(keyNameArray[1])) {
							keyScopeArray[3] = KeyScope.NO;
						}
					}
//					logger.info("**************");
//					logger.info("baseKey=" + baseKey + ";baseKeyScope=" + baseKeyScope);
//					logger.info("keyFull=" + keyFull + ",keyNameArray=" + Arrays.toString(keyNameArray));
//					logger.info("keyScopeArray =" + Arrays.toString(keyScopeArray));
//					logger.info("propertyArray =" + Arrays.toString(propertyArray));
//					logger.info("po.getArray() =" + Arrays.toString(po.getArray()));
//					logger.info("operateArray =" + Arrays.toString(operateArray));
//					logger.info("getAppName() =" + getAppName() + "\t time" + time);
//					logger.info("**************");
					long starTime = System.currentTimeMillis();
					try {
//						if(po.getArray().length == 2 && operateArray.length == 2) {
//							Object[] array = po.getArray();
//							//暂时增加对求均值的支持
//							if(operateArray[0] == ValueOperate.AVERAGE && operateArray[1] != ValueOperate.AVERAGE) {
//								array[0] = getAverage(array[0], array[1]);
//							} else if(operateArray[1] == ValueOperate.AVERAGE && operateArray[0] != ValueOperate.AVERAGE) {
//								array[1] = getAverage(array[1], array[0]);
//							} 
//						}
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, keyNameArray, keyScopeArray, 
								propertyArray, po.getArray(), operateArray);
						String[] column = new String[keyScopeArray.length + 2];
						int i=0;
						for(KeyScope scope : keyScopeArray) {
							column[i] = scope.toString();
							i++;
						}
						column[i++] = "template_send";
						column[i++] = getAppName();
						
//						//查调用时间为空的问题
//						if(po.getArray().length == 2) {
//							if(po.getArray()[0] == null || po.getArray()[1] == null)
//								MonitorLog.addStat("NotifyCtimeError", new String[]{"error"}, new Long[]{1l,System.currentTimeMillis() - starTime});
//						}
						
//						MonitorLog.addStat("NotifyProviderData", column, new Long[]{1l,System.currentTimeMillis() - starTime});
					} catch (Exception e) {
						//logger.error("发送异常->", e);
//						MonitorLog.addStat("NotifyProviderData", new String[]{"template_error"}, new Long[]{1l,System.currentTimeMillis() - starTime});
					}
				} 
			}
			clearValueMap();//发送完马上释放对象
		}

		public void clearValueMap() {
			timeValueMap.clear();
		}
	}

	/**
	 * 中间对象，用来要发送的数据的值
	 */
	public class ValuePo {

		public ValuePo(String keyName, KeyScope[] keyScopeArray) {
			this.keyName = keyName;
			this.keyScopeArray = keyScopeArray;
		}

		private String keyName;		//map中key的值
		private KeyScope[] keyScopeArray;	// 存储分割的key的scope
		private Object[] array;	//存属性值的数组
		public KeyScope[] getKeyScopeArray() {
			return keyScopeArray;
		}
		public void setKeyScopeArray(KeyScope[] keyScopeArray) {
			this.keyScopeArray = keyScopeArray;
		}
		public Object[] getArray() {
			return array;
		}
		public void setArray(Object[] array) {
			this.array = array;
		}
		public String getKeyName() {
			return keyName;
		}
		public void setKeyName(String keyName) {
			this.keyName = keyName;
		}
	}
	
	/**
	 * @param obj1被 除数
	 * @param obj2 除数
	 * @return
	 */
	public Object getAverage(Object obj1, Object obj2) {
		Long lObj1 = 0l;
		try {
			lObj1 = Long.parseLong(obj1.toString());
		} catch (Exception e) {
			logger.info("",e);
			lObj1 = 0l;
		}
		Long lObj2 = 0l;
		try {
			lObj2 = Long.parseLong(obj2.toString());
		} catch (Exception e) {
			logger.info("",e);
			lObj2 = 0l;
		}
		if(lObj2 == 0) {
			return 0;
		} else {
			return lObj1/lObj2;
		}
	}
	
//	public static void test() {
//		String log = "1342080772593 Ocean_Service_Application com.alibaba.ptqa.ocean.test HttpToHttpTest:1 4 106";
//		Pattern p =Pattern.compile("([A-Za-z|\\d|_|.|-|:]+)\\s+([A-Za-z|\\d|_|.|-|:]+)\\s+([A-Za-z|\\d|_|.|-|:]+)\\s+([A-Za-z|\\d|_|.|-|:]+):([A-Za-z|\\d|_|.|-|:]+)\\s+([A-Za-z|\\d|_|.|-|:]+)\\s+([A-Za-z|\\d|_|.|-|:]+)\\s+([A-Za-z|\\d|_|.|-|:]+)");
//		Matcher m = p.matcher(log);
//		while(m.find())
//		{
//			for(int i=0; i<=m.groupCount();i++) {
//				String key = m.group(i);
//				System.out.println(key + "**");					
//			}
//		}
//
//	}
	public static void main(String[] args)
	{
		//		if(new Date().getTime() > 1000) {
		//			AnalyseTemplateJob.test();
		//			return;
		//		}

		//日志举例
		//		[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16
		//		[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 0 1 7
		//		[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application com.alibaba.ptqa.ocean.test HttpToHttpTest:1 4 106
		//配置举例
		//		{datetime} {key_1} {key_2} {key_3}:{key_4}:{key_5} {key_6} {E-times} {C-times}
		//		{datetime} {key_1} {key_2} {key_3}:{key_4} {key_5} {E-times} {C-times}
		//		{datetime} {key_1} {key_2} {key_3}:{key_4} {E-times} {C-times}		

//		List list = new ArrayList();
//		RowAnalyseConfigWeb webPo = new RowAnalyseConfigWeb();
//		webPo.setKeyConfig("key_1,ALL;key_2,ALL;key_3,ALL;key_4,ALL;key_5,ALL;key_6,ALL;");
//		webPo.setKeyNumber(6);
//		webPo.setKeyOrder("key_1,key_2,key_3,key_4,key_5,key_6");
//		webPo.setLogConfigWeb("{datetime} {key_1} {key_2} {key_3}:{key_4}:{key_5} {key_6} {E-times} {C-times}");
//		webPo.setTimeFormat("long");
//		webPo.setValueConfig("E-times,ADD;C-times,REPLACE;");
//		webPo.setValueNumber(2);
//
//		RowAnalyseConfigWeb webPo2 = new RowAnalyseConfigWeb();
//		webPo2.setKeyConfig("key_1,ALL;key_2,ALL;key_3,ALL;key_4,ALL;key_5,ALL;");
//		webPo2.setKeyNumber(5);
//		webPo2.setKeyOrder("key_1,key_2,key_3,key_4,key_5;");
//		webPo2.setLogConfigWeb("{datetime} {key_1} {key_2} {key_3}:{key_4} {key_5} {E-times} {C-times}");
//		webPo2.setTimeFormat("long");
//		webPo2.setValueConfig("E-times,ADD;C-times,REPLACE;");
//		webPo2.setValueNumber(2);	
//
//		RowAnalyseConfigWeb webPo3 = new RowAnalyseConfigWeb();
//		webPo3.setKeyConfig("key_1,ALL;key_2,ALL;key_3,ALL;key_4,ALL");
//		webPo3.setKeyNumber(4);
//		webPo3.setKeyOrder("key_1,key_2,key_3,key_4;");
//		webPo3.setLogConfigWeb("{datetime} {key_1} {key_2} {key_3}:{key_4} {E-times} {C-times}");
//		webPo3.setTimeFormat("long");
//		webPo3.setValueConfig("E-times,ADD;C-times,REPLACE;");
//		webPo3.setValueNumber(2);			
//		list.add(webPo);
//		list.add(webPo2);
//		list.add(webPo3);
//		JSONArray jsonarr = JSONArray.fromObject(list);
//		String jsonStr = jsonarr.toString();		

		//		JSONObject jsonObj = JSONObject.fromObject(webPo);
		//		String jsonStr = jsonObj.toString();
		String jsonStr = "[{\"keyConfig\":\"key_1,APP;key_2,APP;key_3,APP;\",\"keyNumber\":3,\"keyOrder\":\"key_1,key_2,key_3;\",\"logConfigWeb\":\"{datetime} {key_1} {key_2} {key_3} {E-times} {C-time}\",\"regularString\":\"\",\"timeFormat\":\"long\",\"valueConfig\":\"E-times,ADD;C-time,ADD;\",\"valueNumber\":2,\"rootKeyConfig\":\"Notify-provider,NO\"}]";
		try {
			AnalyseTemplateJob job = new AnalyseTemplateJob("ocean","123.123.13.13",jsonStr);
			BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\tmp\\ss_out.txt"),"gbk"));
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\tmp\\deliverStatLog.log.2012-11-04_22"));				
			String line = null;
			while((line=reader.readLine())!=null){
				writer.write(line);
				job.analyseOneLine(line);
				writer.newLine();
			}
			job.submit();
			job.release();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}	
}
