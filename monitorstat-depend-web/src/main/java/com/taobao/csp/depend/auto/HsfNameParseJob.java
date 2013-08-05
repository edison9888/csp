//package com.taobao.csp.depend.auto;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.Map.Entry;
//
//import org.apache.log4j.Logger;
//
//import com.ibm.icu.util.Calendar;
//import com.taobao.csp.depend.dao.CspDependentDao;
//import com.taobao.csp.depend.util.MethodUtil;
//import com.taobao.monitor.common.util.HsfServiceCodeMap;
//public class HsfNameParseJob {
//
//	private static final Logger logger = Logger.getLogger(HsfNameParseJob.class);
//	private CspDependentDao cspDependentDao;
//
//	public void startJob() {
//		logger.info("HSF名称转化任务开始");
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
//		Date selectDate = cal.getTime();
//		
//		String[] appNames = new String[]{"classes","cucrm","cuxiao","dpc","dpm",
//				"ecrm","itemcenter","itemtools","jtbas","lifemarketweb","marketing","marketingcenter",
//				"pamirsmarketing","picturecenter","rxu","selleradmin","sellercenter","sellermanager",
//				"sportalapps","tadget","tmallpromotion","tmallrefund","tmallsell","udc","wpc","xu"};
//		
//		long startOut = System.currentTimeMillis();
//		
//		for(String appName: appNames) {
//			try {
//				String[] typeArray = new String[] { "provide", "consume" };
//				for (String type : typeArray) {
//					long start = System.currentTimeMillis();
//					Set<String> keySet = cspDependentDao.getDistinctHsfKeyByTime(
//							selectDate, type, appName);
//					logger.info(appName + "查询distinct 耗时->" + (System.currentTimeMillis() - start));
//					//老接口，新接口
//					Map<String, String> mapNew = new HashMap<String, String>();
//					for (String key : keySet) {
//						String simpleKey = MethodUtil.simplifyHsfInterfaceName(key);
//						//com.taobao.tc.service.TcTradeService:1.0.0_modifyWapDetailTradeFee
//						String[] array = simpleKey.split("_");
//						String oldInterface = array[0];
//						String method = array[1];
//						String[] arrayNew = HsfServiceCodeMap.get()
//								.translateServiceMethod(oldInterface, method);
//						if (arrayNew[1].equals(array[1]))
//							continue;
//						String interfaceAndVersion = simpleKey.substring(0,
//								simpleKey.lastIndexOf('_'));
//						String prefix = "IN_HSF-ProviderDetail_";
//						mapNew.put(key, prefix + interfaceAndVersion + "_"
//								+ arrayNew[1]);
//					}
//					for (Entry<String, String> entry : mapNew.entrySet()) {
//						cspDependentDao.updateHsfKey(entry.getKey(),
//								entry.getValue(), selectDate, type, appName);
//					}
//					logger.info(type + "更新完毕耗时->" + (System.currentTimeMillis() - start));
//				}
//			} catch (Exception e) {
//				logger.error("", e);
//			}			
//		}
//
//		logger.info("HSF名称转化任务结束,耗时->" + (System.currentTimeMillis() - startOut));
//	}
//
//	public CspDependentDao getCspDependentDao() {
//		return cspDependentDao;
//	}
//
//	public void setCspDependentDao(CspDependentDao cspDependentDao) {
//		this.cspDependentDao = cspDependentDao;
//	}
//
//	public static void main(String[] args) {
//	}
//}
