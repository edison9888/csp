//package com.taobao.monitor.web.baseline;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
//import com.taobao.monitor.common.util.Constants;
//import com.taobao.monitor.report.messageSender.HaBoMessageSenderServiceImpl;
//import com.taobao.monitor.report.messageSender.IHaBoMessageSenderService;
//import com.taobao.monitor.web.ao.MonitorAlarmAo;
//import com.taobao.monitor.web.ao.MonitorBaseLineAo;
//import com.taobao.monitor.web.vo.AlarmDataPo;
//
///**
// * 
// * @author xiaodu
// * @version 2010-9-20 上午10:29:55
// */
//public class BaseLineAlarmThread extends Thread {
//
//	private static final Logger logger = Logger.getLogger(BaseLineAlarmThread.class);
//
//	private IHaBoMessageSenderService messageSender = new HaBoMessageSenderServiceImpl();
//
//	private static BaseLineAlarmThread thread = new BaseLineAlarmThread();
//
//	public static BaseLineAlarmThread get() {
//		return thread;
//	}
//
//	private BaseLineAlarmThread() {
//		
//	}
//	
//	public void startup(){
//		this.setDaemon(true);
//		this.start();
//	}
//
//	public void run() {
//		
//		
//		while (true) {
//
//			try {
//				Thread.sleep(90000);
//			} catch (InterruptedException e) {
//			}
//			try{
//			List<AlarmDataPo> boList = MonitorAlarmAo.get().findAllAlarmWithAlarmTable();
//			
//			logger.info(" run BaseLineAlarmThread AlarmDataPo size :"+boList.size());
//			Calendar cal = Calendar.getInstance();
//			for (AlarmDataPo po : boList) {
//				int appId = po.getAppId();
//				int keyId = Integer.parseInt(po.getKeyId());
//				int siteId = po.getSiteId();
//
//				Map<Date, Double> limitValueMap = po.getLimitDataMap();
//				List<Date> sortDate = sortDateDesc(limitValueMap);
//				if (sortDate != null) {
//					for (int i = 1, offset = 0; i < sortDate.size() && offset < 1; i++, offset++) {
//						Date date = sortDate.get(i);
//						cal.setTime(date);
//						cal.add(Calendar.DAY_OF_MONTH, -1);
//						
//						KeyValueBaseLinePo baseLinePo = MonitorBaseLineAo.get().findKeyBaseValueByDate(appId, keyId,
//								cal.getTime());
//						if(baseLinePo == null){
//							logger.info("BaseLineAlarmThread :appId="+appId+" keyId="+keyId+" date"+date);
//							continue;
//						}
//						double b = baseLinePo.getBaseLineValue();
//						double v = limitValueMap.get(date);
//
//						if (keyId == 176) {
//							b = baseLinePo.getBaseLineValue() / 1000;
//							v = limitValueMap.get(date) / 1000;
//						}
//						StringBuilder message = new StringBuilder();
//						message.append("应用").append(po.getAppName()).append("[").append(po.getAppName()).append("]机器:")
//								.append(po.getSite()).append("值[").append(v + "").append("]").append("基线值:" + b);
//						logger.info(message.toString());
//
//						if (po.getKeyName().indexOf(Constants.AVERAGE_USERTIMES_FLAG) > 0) {
//							if (b < 5) {
//								if (v > 3 * b) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 5 && b < 10) {
//								if (v > 2 * b) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 10 && b < 20) {
//								if (v > 1.5 * b) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 20 && b < 100) {
//								if (v > 1.4 * b) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 100 && b < 1000) {
//								if (v > 1.3 * b) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 1000) {
//								if (v > 1.2 * b) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							}
//						} else {
//							if (b >= 100 && b < 1000) {
//								if (v > 2 * b || b > 2 * v) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 1000 && b < 10000) {
//								if (v > 1.5 * b || b > 1.5 * v) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							} else if (b >= 10000) {
//								if (v > 1.3 * b || b > 1.3 * v) {
//									if (putOver(appId, keyId, siteId, date)) {
//										setMsg(po, v, b);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//			}catch (Exception e) {
//				logger.info("", e);
//			}
//		}
//		
//
//	}
//
//	Map<String, Inner> map = new HashMap<String, Inner>();
//
//	private boolean putOver(int appId, int keyId, int sizeId, Date time) {
//		String key = appId + "_" + keyId + "_" + sizeId;
//
//		Inner inner = map.get(key);
//
//		if (inner == null) {
//			inner = new Inner();
//			map.put(key, inner);
//			inner.setTime(System.currentTimeMillis());
//			inner.getDateList().add(time);
//			inner.overBaseLineNum++;
//		} else {
//			if (System.currentTimeMillis() - inner.getTime() > 300000) {
//				map.remove(key);
//				putOver(appId, keyId, sizeId, time);
//			} else {
//				if (!inner.getDateList().contains(time)) {
//					inner.getDateList().add(time);
//					inner.overBaseLineNum++;
//				}
//			}
//		}
//
//		if (inner.overBaseLineNum >= 8) {
//			map.remove(key);
//			return true;
//		} else {
//			return false;
//		}
//
//	}
//
//	private void setMsg(AlarmDataPo po, double v, double b) {
//		String ms = po.getAlarmFeature();
//		if (ms == null) {
//			ms = po.getKeyName();
//		}
//		StringBuilder message = new StringBuilder();
//		message.append("应用").append(po.getAppName()).append("[").append(ms).append("]机器:").append(po.getSite()).append(
//				"告警值[").append(v + "").append("]").append("基线值:" + b);
//		String detail = "http://cm.taobao.net:9999/monitorstat/show.jsp?appId=" + po.getAppId() + "&keyId="
//				+ po.getKeyId() + "&appName=" + po.getAppName();
//		message.append("\n\n[查看详细数据:\n" + detail + "]");
//
//		try {
//			boolean result = messageSender.sendWangwang("ww", "", "", "", "", "1", "", message.toString(), "注意-核心监控",
//					"", "小赌;小邪");
//			if (!result) {
//				logger.info("小赌;小邪:" + message.toString() + " wangwang send fail!");
//			} else {
//				logger.info("小赌;小邪:" + message.toString() + " wangwang send success!");
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//
//	}
//
//	/**
//	 * 将告警值 按照时间降序 排序列表
//	 * 
//	 * @param dataMap
//	 * @return
//	 */
//	protected List<Date> sortDateDesc(Map<Date, Double> dataMap) {
//		List<Date> dataList = new ArrayList<Date>();
//		for (Date date : dataMap.keySet()) {
//			dataList.add(date);
//		}
//		Collections.sort(dataList, new Comparator<Date>() {
//			
//			public int compare(Date o1, Date o2) {
//				long thisTime = o1.getTime();
//				long anotherTime = o2.getTime();
//				return (thisTime < anotherTime ? 1 : (thisTime == anotherTime ? 0 : -1));
//			}
//		});
//		if (dataList.size() > 0) {
//			return dataList;
//		} else {
//			return null;
//		}
//	}
//
//	private class Inner {
//
//		private long time;
//
//		private int overBaseLineNum;
//
//		private List<Date> dateList = new ArrayList<Date>();
//
//		public long getTime() {
//			return time;
//		}
//
//		public void setTime(long time) {
//			this.time = time;
//		}
//
//		public int getOverBaseLineNum() {
//			return overBaseLineNum;
//		}
//
//		public void setOverBaseLineNum(int overBaseLineNum) {
//			this.overBaseLineNum = overBaseLineNum;
//		}
//
//		public List<Date> getDateList() {
//			return dateList;
//		}
//
//		public void setDateList(List<Date> dateList) {
//			this.dateList = dateList;
//		}
//
//	}
//
//}
