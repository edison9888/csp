//package com.taobao.csp.time.job;
//
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.csp.dao.hbase.base.HBaseUtil;
//import com.taobao.csp.dataserver.Constants;
//import com.taobao.csp.dataserver.KeyConstants;
//import com.taobao.csp.dataserver.PropConstants;
//import com.taobao.csp.dataserver.query.QueryHistoryUtil;
//import com.taobao.csp.dataserver.util.Util;
//import com.taobao.csp.time.web.po.IndexMinuteTable;
//import com.taobao.monitor.common.cache.AppInfoCache;
//import com.taobao.monitor.common.po.AppInfoPo;
//import com.taobao.monitor.common.util.GroupManager;
//
///**
// * ��������ƽ����Ϣ
// * @author zhongting.zy
// *
// */
//public class GroupCalculateJob {
//	private static final Logger logger = Logger.getLogger(GroupCalculateJob.class);
//
//	public void calculateGroupInfo() {
//		calculateGroupInfoByTime(null);
//	}
//	
//	public void calculateGroupInfoByTime(Date now) {
//		final long start = System.currentTimeMillis();
//		String capacity = System.getenv("GROUP_JOB");
//		if (capacity == null || !capacity.equals("true")) {
//			logger.info("���Ǽ���RT����������ʱ�������");
//			return;
//		}
//		
//		if(now == null) {
//			now = new Date();
//		}
//		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(now);
//		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE - 5));	//Ĭ�ϲ�ȡ5����ǰ������
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//		
//		logger.info("��ʼ����������ֵ����ǰʱ��->" + now.toString());
//		
//		final long collectTime = cal.getTimeInMillis();
//		final int count = 1;	//�ۼ�ʱ��Ϊ1����
//		
//		//��ȡ���еķ���
//		Map<String, Map<String, List<String>>> mainGroup = GroupManager.get().getGroupInfo();
//		for(Entry<String, Map<String, List<String>>> entryMain : mainGroup.entrySet()) {
//			String opsName = entryMain.getKey();
//			Map<String, List<String>> groupMap = entryMain.getValue();//��ѯ�����л�������Ϣ-> <groupname, list<ip>>
//			//�з�������
//			for(Entry<String, List<String>> entry : groupMap.entrySet()) {
//				
//				//��¼���з��ӵ�����
//				Map<String, IndexMinuteTable> indexMinuteMap = new HashMap<String, IndexMinuteTable>();
//				
//				final String groupName = entry.getKey();
//				final int machineSize = groupMap.get(groupName).size();				
//				AppInfoPo appInfo = AppInfoCache.getAppInfoByAppName(opsName);
//				if(appInfo == null)
//					continue;
//
//				//����ͬһ�����������еĻ���
//				List<String> ipList = entry.getValue();
//				for(String ip : ipList) {
//					//������Ϣ
//					Map<Date,Map<String,String>> cur_top = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
//							KeyConstants.TOPINFO, ip, new String[]{PropConstants.CPU, PropConstants.LOAD}, now);
//					//JVM��Ϣ
//					Map<Date,Map<String,String>> cur_jvm = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
//							KeyConstants.JVMINFO, ip, new String[]{PropConstants.JVMGC, PropConstants.JVMMEMORY}, now);
//					//������Ϣ
//					for(Entry<Date,Map<String,String>> tmpEntry : cur_top.entrySet()) {
//						Date date = tmpEntry.getKey();
//						Map<String,String> map = tmpEntry.getValue();
//						IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
//						if(index == null) {
//							index = new IndexMinuteTable();
//							indexMinuteMap.put(date.getTime() + "", index);
//						}
//						index.setCpu(Float.parseFloat(map.get(PropConstants.CPU)) + index.getCpu());
//						index.setLoad(Double.parseDouble(map.get(PropConstants.LOAD)) + index.getLoad());
//					}
//
//					//JVM��Ϣ
//					for(Entry<Date,Map<String,String>> tmpEntry : cur_jvm.entrySet()) {
//						Date date = tmpEntry.getKey();
//						Map<String,String> map = tmpEntry.getValue();
//						IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
//						if(index == null) {
//							index = new IndexMinuteTable();
//							indexMinuteMap.put(date.getTime() + "", index);
//						}
//						index.setGc(Integer.parseInt(map.get(PropConstants.JVMGC)) + index.getGc());
//						index.setMemory(Float.parseFloat(map.get(PropConstants.JVMMEMORY)) + index.getMemory());
//					}
//
//					//PV���
//					if(appInfo.getAppType().equalsIgnoreCase("pv")) {
//						//PV���
//						Map<Date,Map<String,String>> cur_pv = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
//								KeyConstants.PV, ip, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME, PropConstants.C_200}, now);
//
//						for(Entry<Date,Map<String,String>> tmpEntry : cur_pv.entrySet()) {
//							Date date = tmpEntry.getKey();
//							Map<String,String> map = tmpEntry.getValue();
//							IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
//							if(index == null) {
//								index = new IndexMinuteTable();
//								indexMinuteMap.put(date.getTime() + "", index);
//							}
//							index.setPv(Long.parseLong(map.get(PropConstants.E_TIMES)) + index.getPv());
//							index.setRt(Long.parseLong(map.get(PropConstants.C_TIME)) + index.getRt());
//							index.setPv200(Long.parseLong(map.get(PropConstants.C_200)) + index.getPv200());
//						}
//					} else {
//						//HSF���
//						Map<Date,Map<String,String>> cur_hsf = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
//								KeyConstants.HSF_PROVIDER, ip, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME}, now);
//
//						for(Entry<Date,Map<String,String>> tmpEntry : cur_hsf.entrySet()) {
//							Date date = tmpEntry.getKey();
//							Map<String,String> map = tmpEntry.getValue();
//							IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
//							if(index == null) {
//								index = new IndexMinuteTable();
//								indexMinuteMap.put(date.getTime() + "", index);
//							}
//							index.setPv(Long.parseLong(map.get(PropConstants.E_TIMES)) + index.getPv());
//							index.setRt(Long.parseLong(map.get(PropConstants.C_TIME)) + index.getRt());
//						}
//					}
//				}
//
//				IndexMinuteTable indexSum = indexMinuteMap.get(collectTime + "");
//				if(indexSum != null) {
//					//����1���ӵ�ƽ��ֵ
//					indexSum.setCpu(indexSum.getCpu()/(count*machineSize));
//					indexSum.setLoad(indexSum.getLoad()/(count*machineSize));
//					indexSum.setGc(indexSum.getGc()/(count*machineSize));
//					indexSum.setMemory(indexSum.getMemory()/(count*machineSize));
//					
////					//��ȺQPS
////					indexSum.setQps(indexSum.getPv()/(count*60));
////					//������QPS
////					indexSum.setQpsHost(indexSum.getPv()/(count*machineSize*60));
//					
//					final String appNameCombine = appInfo.getOpsName() + Constants.S_SEPERATOR + groupName;
//					try {
//						String fullName = Util.combinAppKeyName(appNameCombine, KeyConstants.TOPINFO);
//						String rowkey = HBaseUtil.getMD5String(fullName) + Constants.S_SEPERATOR + collectTime;
//						HBaseUtil.addRow(rowkey, new String[]{PropConstants.CPU, PropConstants.LOAD}, 
//								new Object[]{indexSum.getCpu() + "", indexSum.getLoad() + ""});
//					} catch (IOException e) {
//						logger.error(String.format("Ӧ��%s-����%s-��%s-��ӳ���,key:%s,�쳣:%s", appInfo.getOpsName(), groupName, now.toString(), 
//								KeyConstants.TOPINFO, e.toString()));
//					}
//
//					try {
//						String fullName = Util.combinAppKeyName(appNameCombine, KeyConstants.JVMINFO);
//						String rowkey = HBaseUtil.getMD5String(fullName) + Constants.S_SEPERATOR + collectTime;
//						HBaseUtil.addRow(rowkey, new String[]{PropConstants.JVMGC, PropConstants.JVMMEMORY}, 
//								new Object[]{indexSum.getGc() + "", indexSum.getMemory() + ""});
//					} catch (IOException e) {
//						logger.error(String.format("Ӧ��%s-����%s-��%s-��ӳ���,key:%s,�쳣:%s", appInfo.getOpsName(), groupName, now.toString(), 
//								KeyConstants.JVMINFO, e.toString()));
//					}
//					
//					if(appInfo.getAppType().equalsIgnoreCase("pv")) {
//						try {
//							String fullName = Util.combinAppKeyName(appNameCombine, KeyConstants.PV);
//							String rowkey = HBaseUtil.getMD5String(fullName) + Constants.S_SEPERATOR + collectTime;
//							HBaseUtil.addRow(rowkey, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME, PropConstants.C_200}, 
//									new Object[]{indexSum.getPv() + "", indexSum.getRt() + "", indexSum.getPv200() + ""});
//						} catch (IOException e) {
//							logger.error(String.format("Ӧ��%s-����%s-��%s-��ӳ���,key:%s,�쳣:%s", appInfo.getOpsName(), groupName, now.toString(), 
//									KeyConstants.PV, e.toString()));
//						}						
//					} else {
//						try {
//							String fullName = Util.combinAppKeyName(appNameCombine, KeyConstants.HSF_PROVIDER);
//							String rowkey = HBaseUtil.getMD5String(fullName) + Constants.S_SEPERATOR + collectTime;
//							HBaseUtil.addRow(rowkey, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME}, 
//									new Object[]{indexSum.getPv() + "", indexSum.getRt() + ""});
//						} catch (IOException e) {
//							logger.error(String.format("Ӧ��%s-����%s-��%s-��ӳ���,key:%s,�쳣:%s", appInfo.getOpsName(), groupName, now.toString(), 
//									KeyConstants.HSF_PROVIDER, e.toString()));
//						}						
//					}
//				} else {
//					logger.error(String.format("Ӧ��%s-����%s-��%s-�������", appInfo.getOpsName(), groupName, now.toString()));
//				}
//			}
//		}
//		logger.info("ִ��calculateGroupInfo��������ʱ(ms)->" + (System.currentTimeMillis()-start));
//	}	
//	
//	public static void main(String[] args) {
//		new GroupCalculateJob().calculateGroupInfo();
//	}
//}
