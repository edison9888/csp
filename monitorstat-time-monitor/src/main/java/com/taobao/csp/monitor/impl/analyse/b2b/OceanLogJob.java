package com.taobao.csp.monitor.impl.analyse.b2b;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.BufferedReader2;

public class OceanLogJob extends AbstractDataAnalyse {
	private static final Logger logger = Logger.getLogger(OceanLogJob.class);
	//<time,<namespace,<name:version,<id,int[2]>>>>
	Map<Long,Map<String,Map<String,Map<Long,long[]>>>> applicationMap = new HashMap<Long, Map<String,Map<String,Map<Long,long[]>>>>();
	//<time,<namespace,<name:version,<exceptionname,<ip,int[2]>>>>> value
	Map<Long,Map<String,Map<String,Map<String,Map<String,long[]>>>>> exceptionMap = new HashMap<Long, Map<String,Map<String,Map<String,Map<String,long[]>>>>>();

	//反向记录调用信息<time,<appid,<name:version,<namespace,int[2]>>>>
	Map<Long,Map<Long,Map<String,Map<String,long[]>>>> appIdApplicatonMap = new HashMap<Long, Map<Long,Map<String,Map<String,long[]>>>>();

	private static final String VALID_APP_ID = "-1";	//不合法的APPID

	public OceanLogJob(String appName, String ip, String feature) {
		super(appName, ip, feature);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void analyseOneLine(String line) {
		//日志格式:
		//[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16
		//[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 0 1 7
		//[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 1 7
		if(line != null) {
			try {
				String[] array = line.trim().split(" ");
				if (array.length < 8)
					return;
				final String time = array[2];
				long timeL = Long.parseLong(time);	//去掉秒和毫秒，同步缓存
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(timeL));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				timeL = cal.getTimeInMillis();  

				final String flag = array[3];
				final String namespace = array[4];
				final String name_version = array[5];
				if (flag.equals("Ocean_Service_Exception")) {
					//array length = 9
					int position = name_version.lastIndexOf(":");
					String ip = name_version.substring(position + 1);
					String name_versionTmp = name_version.substring(0, position);
					String exceptionName = array[6];

					String callNum = array[7];
					String callTime = array[8];
					long callNumL = Long.parseLong(callNum);
					long callTimeL = Long.parseLong(callTime);

					Map<String, Map<String, Map<String, Map<String, long[]>>>> timeMap = exceptionMap
							.get(timeL);
					if (timeMap == null) {
						timeMap = new HashMap<String, Map<String, Map<String, Map<String, long[]>>>>();
						exceptionMap.put(timeL, timeMap);
					}

					Map<String, Map<String, Map<String, long[]>>> namespaceMap = timeMap
							.get(namespace);
					if (namespaceMap == null) {
						namespaceMap = new HashMap<String, Map<String, Map<String, long[]>>>();
						timeMap.put(namespace, namespaceMap);
					}

					Map<String, Map<String, long[]>> name_VersionMap = namespaceMap
							.get(name_versionTmp);
					if (name_VersionMap == null) {
						name_VersionMap = new HashMap<String, Map<String, long[]>>();
						namespaceMap.put(name_versionTmp, name_VersionMap);
					}

					Map<String, long[]> exceptionMap = name_VersionMap.get(exceptionName);
					if (exceptionMap == null) {
						exceptionMap = new HashMap<String, long[]>();
						name_VersionMap.put(exceptionName, exceptionMap);
					}

					long[] valueArray = exceptionMap.get(ip);
					if (valueArray == null) {
						valueArray = new long[] { 0, 0 };
						exceptionMap.put(ip, valueArray);
					}
					valueArray[0] += callNumL;
					valueArray[1] += callTimeL;
				} else if (flag.equals("Ocean_Service_Application")) {
					//array length = 9 or 8

					String appidStr = VALID_APP_ID;
					String callNum = "";
					String callTime = "";
					if (array.length == 8) {
						callNum = array[6];
						callTime = array[7];
					} else if (array.length == 9) {
						appidStr = array[6];
						callNum = array[7];
						callTime = array[8];
					}

					final long appId = Long.parseLong(appidStr);
					final long callNumL = Long.parseLong(callNum);
					final long callTimeL = Long.parseLong(callTime);

					Map<String, Map<String, Map<Long, long[]>>> timeMap = applicationMap
							.get(timeL);
					if (timeMap == null) {
						timeMap = new HashMap<String, Map<String, Map<Long, long[]>>>();
						applicationMap.put(timeL, timeMap);
					}

					Map<String, Map<Long, long[]>> namespaceMap = timeMap.get(namespace);
					if (namespaceMap == null) {
						namespaceMap = new HashMap<String, Map<Long, long[]>>();
						timeMap.put(namespace, namespaceMap);
					}

					Map<Long, long[]> appIdMap = namespaceMap.get(name_version);
					if (appIdMap == null) {
						appIdMap = new HashMap<Long, long[]>();
						namespaceMap.put(name_version, appIdMap);
					}

					long[] valueArray = appIdMap.get(appId);
					if (valueArray == null) {
						valueArray = new long[] { 0, 0 };
						appIdMap.put(appId, valueArray);
					}
					valueArray[0] += callNumL;
					valueArray[1] += callTimeL;

					if(appidStr.equals(VALID_APP_ID))
						return;

					//新增按照appid反向存储数据					
					Map<Long, Map<String, Map<String, long[]>>> appIdTimeMap = appIdApplicatonMap.get(timeL);
					if (appIdTimeMap == null) {
						appIdTimeMap = new HashMap<Long, Map<String, Map<String, long[]>>>();
						appIdApplicatonMap.put(timeL, appIdTimeMap);
					}

					Map<String, Map<String, long[]>> appIdVersionMap = appIdTimeMap.get(appId);
					if (appIdVersionMap == null) {
						appIdVersionMap = new HashMap<String, Map<String, long[]>>();
						appIdTimeMap.put(appId, appIdVersionMap);
					}

					//final String namespace = array[4];
					Map<String,long[]> appIdNamespaceMap = appIdVersionMap.get(name_version);
					if(appIdNamespaceMap == null) {
						appIdNamespaceMap = new HashMap<String,long[]>();
						appIdVersionMap.put(name_version, appIdNamespaceMap);
					} 

					long[] appIdValueArray = appIdNamespaceMap.get(namespace);
					if (appIdValueArray == null) {
						appIdValueArray = new long[] { 0, 0 };
						appIdNamespaceMap.put(namespace, appIdValueArray);
					}
					appIdValueArray[0] += callNumL;
					appIdValueArray[1] += callTimeL;					
				}
				//logger.info(Arrays.toString(array));
				//logger.info(array.length + "\t" + Arrays.toString(array));
			} catch (Exception e) {
				logger.error("实时采集出现异常,line->" + line, e);
			}
		}
	}

	@Override
	public void submit() {
		//logger.info("------------------for Ocean_Service_Application case---------------------------");
		for(Entry<Long, Map<String, Map<String, Map<Long, long[]>>>> entry:applicationMap.entrySet()) {
			//logger.info("time=" + entry.getKey());
			long time = entry.getKey();
			Map<String,Map<String,Map<Long,long[]>>> namespaceMap = entry.getValue();
			for(Entry<String, Map<String, Map<Long, long[]>>> namespaceEntry :namespaceMap.entrySet()) {
				//logger.info("namespace=" + namespaceEntry.getKey());
				String namespace = namespaceEntry.getKey();
				Map<String, Map<Long, long[]>> nameversionMap = namespaceEntry.getValue();
				for(Entry<String, Map<Long, long[]>> nameversionEntry : nameversionMap.entrySet()) {
					//logger.info("nameversion=" + nameversionEntry.getKey());
					String nameversion = nameversionEntry.getKey();
					Map<Long, long[]> appIdMap = nameversionEntry.getValue();
					for(Entry<Long, long[]> appIdEntry : appIdMap.entrySet()) {
						//logger.info("appId=" + appIdEntry.getKey());
						String appIdStr = appIdEntry.getKey() + "";
						long[] valueArray = appIdEntry.getValue();
						Long[] valueObj = new Long[]{valueArray[0], valueArray[1]};

						try {
							if(appIdStr.equals(VALID_APP_ID)) {
								CollectDataUtilMulti.collect(getAppName(), getIp(), time, 
										new String[]{KeyConstants.B2B_OCEAN_APPLICATION,namespace,nameversion}, 
										new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, 
										new String[]{"E-times","C-time"},valueObj, new ValueOperate[]{ValueOperate.ADD, ValueOperate.ADD});
							} else {
								CollectDataUtilMulti.collect(getAppName(), getIp(), time, 
										new String[]{KeyConstants.B2B_OCEAN_APPLICATION,namespace,nameversion,appIdStr}, 
										new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, 
										new String[]{"E-times","C-time"},valueObj, new ValueOperate[]{ValueOperate.ADD, ValueOperate.ADD});
							}
							//logger.info("发送application");
						} catch (Exception e) {
							logger.error("发送失败", e);
						}
					}
				}
			}
		}
		//logger.info("------------------for Ocean_Service_Application Reverse case---------------------------");
		for(Entry<Long, Map<Long, Map<String, Map<String, long[]>>>> entry:appIdApplicatonMap.entrySet()) {
			long time = entry.getKey();
			Map<Long,Map<String,Map<String,long[]>>>  appIdApplicatonMap = entry.getValue();
			for(Entry<Long, Map<String, Map<String, long[]>>> appIdApplicatonEntry : appIdApplicatonMap.entrySet()) {
				Long appId = appIdApplicatonEntry.getKey();
				Map<String, Map<String, long[]>> nameversionMap = appIdApplicatonEntry.getValue();
				for(Entry<String, Map<String, long[]>> nameversionEntry : nameversionMap.entrySet()) {
					String nameversion = nameversionEntry.getKey();
					Map<String, long[]> appIdMap = nameversionEntry.getValue();
					for(Entry<String, long[]> appIdEntry : appIdMap.entrySet()) {
						//logger.info("appId=" + appIdEntry.getKey());
						String namespace = appIdEntry.getKey() + "";
						long[] valueArray = appIdEntry.getValue();
						Long[] valueObj = new Long[]{valueArray[0], valueArray[1]};

						try {
							CollectDataUtilMulti.collect(getAppName(), getIp(), time, 
									new String[]{KeyConstants.B2B_OCEAN_APPLICATION_REVERSE,appId + "",nameversion,namespace}, 
									new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, 
									new String[]{"E-times","C-time"},valueObj, new ValueOperate[]{ValueOperate.ADD, ValueOperate.ADD});
							//logger.info("发送B2B_OCEAN_APPLICATION_REVERSE");
						} catch (Exception e) {
							logger.error("发送失败", e);
						}
					}
				}
			}
		}

		//logger.info("------------------for Ocean_Service_Exception case---------------------------");
		for(Entry<Long,Map<String,Map<String,Map<String,Map<String,long[]>>>>> entry: exceptionMap.entrySet()) {
			//logger.info("time=" + entry.getKey());
			long time = entry.getKey();
			Map<String,Map<String,Map<String,Map<String,long[]>>>> namespaceExceptionMap = entry.getValue();
			for(Entry<String,Map<String,Map<String,Map<String,long[]>>>> namespaceEntry :namespaceExceptionMap.entrySet()) {
				//logger.info("namespace=" + namespaceEntry.getKey());
				String namespace = namespaceEntry.getKey();
				Map<String,Map<String,Map<String,long[]>>> nameversionMap = namespaceEntry.getValue();
				for(Entry<String,Map<String,Map<String,long[]>>> nameversionEntry : nameversionMap.entrySet()) {
					//logger.info("nameversion=" + nameversionEntry.getKey());
					String nameversion = nameversionEntry.getKey();
					Map<String,Map<String,long[]>> exceptionMap = nameversionEntry.getValue();
					for(Entry<String,Map<String,long[]>> exceptionEntry : exceptionMap.entrySet()) {
						//logger.info("exceptionName=" + exceptionEntry.getKey());
						String exceptionName = exceptionEntry.getKey();
						Map<String, long[]> ipMap = exceptionEntry.getValue();
						for(Entry<String, long[]> ipEntry : ipMap.entrySet()) {
							//logger.info("ip=" + ipEntry.getKey());
							String ip = ipEntry.getKey(); // host级别汇总在alarm cache做
							long[] valueArray = ipEntry.getValue();
							Long[] valueObj = new Long[]{valueArray[0], valueArray[1]};
							try {
								CollectDataUtilMulti.collect(getAppName(), getIp(), time, 
										new String[]{KeyConstants.B2B_OCEAN_EXCEPTION,namespace,nameversion,exceptionName,ip}, 
										new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"},valueObj, new ValueOperate[]{ValueOperate.ADD, ValueOperate.ADD});
								//logger.info("发送exception");
							} catch (Exception e) {
								logger.error("发送失败", e);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void release() {
		applicationMap.clear();
		exceptionMap.clear();
		applicationMap.clear();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16
		//[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 0 1 7
		//[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 1 7
		//    OceanLogJob obj = new OceanLogJob("","","");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.51 InvokeMethodException 2 16");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 3 7");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.51 InvokeMethodException 3 7");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException123 3 7");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 0 1 7");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 0 2 6");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 1 7");
		//    obj.analyseOneLine("[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Application system currentTime:1 1 8");
		//    obj.submit();

		//4个小时
		for(int i=0; i<60*4; i++) {
			try {
				OceanLogJob job = new OceanLogJob("ocean","123.123.13.13","");
//				BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("F:\\test\\monitor.log_f"),"gbk"));
//				BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\test\\openapi_request_summary.log"));
		        BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\tmp\\monitor.log_f"),"gbk"));
		        BufferedReader2 reader = new BufferedReader2(new FileReader("F:\\test\\openapi_request_summary.log"));				
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
			try {
				TimeUnit.SECONDS.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
