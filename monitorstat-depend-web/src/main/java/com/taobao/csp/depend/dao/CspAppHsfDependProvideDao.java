package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppExceptionListPo;
import com.taobao.csp.depend.po.hsf.AppProvderInterfaceSummary;
import com.taobao.csp.depend.po.hsf.AppProviderSummary;
import com.taobao.csp.depend.po.hsf.HsfProvideMachine;
import com.taobao.csp.depend.po.report.ConsumeHSFReport;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;


/**
 * 用于访问HSF 依赖表的信息
 * @author xiaodu
 *
 */
public class CspAppHsfDependProvideDao  extends MysqlRouteBase{

	private static final Logger logger =  Logger.getLogger(CspAppHsfDependProvideDao.class);

	public CspAppHsfDependProvideDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}


	/**
	 * 统计针对某个center 应用的，每个依赖的应用的调用总量
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppConsumerSummary> sumCenterAppHsfToAppConsumer(String provideAppName, String collectDay){

		List<AppConsumerSummary> list = null;
		final Map<String, AppConsumerSummary> map = new HashMap<String, AppConsumerSummary>();
		String sql = "select customer_app,provider_machine_cm,call_num as allnum from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? ";

		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("customer_app") + "-" + rs.getString("provider_machine_cm");
					if(map.containsKey(key)) {
						AppConsumerSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
					} else {
						AppConsumerSummary sum = new AppConsumerSummary();
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setOpsName(rs.getString("customer_app"));
						sum.setProvideSiteName(rs.getString("provider_machine_cm"));
						map.put(key, sum);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppConsumerSummary>(map.values());
		} else {
			list = new ArrayList<AppConsumerSummary>();
		}
		return list;
	}

	/**
	 * 统计某个center的接口的调用总量
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppProvderInterfaceSummary> sumAppProviderInterfaceToCenterApp(String provideAppName, String collectDay){

		List<AppProvderInterfaceSummary> list = null;

		String sql = "select provider_machine_cm,key_name,call_num as allnum from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? ";

		final Map<String, AppProvderInterfaceSummary> map = new HashMap<String, AppProvderInterfaceSummary>();

		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("key_name") + "-" + rs.getString("provider_machine_cm");
					if(map.containsKey(key)) {
						AppProvderInterfaceSummary po = map.get(key);
						po.setCallAllNum(po.getCallAllNum() + rs.getLong("allnum"));
					} else {
						AppProvderInterfaceSummary sum = new AppProvderInterfaceSummary();
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setInterfaceName(rs.getString("key_name"));
						sum.setProvideSiteName(rs.getString("provider_machine_cm"));
						map.put(key, sum);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppProvderInterfaceSummary>(map.values());
		} else {
			list = new ArrayList<AppProvderInterfaceSummary>();
		}
		return list;
	}

	/**
	 * 提供HSf 某个接口的应用调用统计
	 * @param provideAppName
	 * @param name
	 * @param collectDay
	 * @return
	 */
	public List<AppConsumerSummary> sumOneHsfInterfaceToAppConsumer(String provideAppName,String name,String collectDay){

		final List<AppConsumerSummary> list = new ArrayList<AppConsumerSummary>();

		String sql = "select customer_app,provider_machine_cm,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? and key_name=?" +
				" and collect_date=? group by customer_app,provider_machine_cm";

		try {
			this.query(sql, new Object[]{provideAppName,name,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppConsumerSummary sum = new AppConsumerSummary();
					sum.setCallAllNum(rs.getLong("allnum"));
					sum.setOpsName(rs.getString("customer_app"));
					sum.setProvideSiteName(rs.getString("provider_machine_cm"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;

	}

	/**
	 * 提供HSf 某个接口的应用调用统计
	 * @param provideAppName
	 * @param name
	 * @param collectDay
	 * @return
	 */
	public List<HsfProvideMachine> findHsfProvideInterfaceMachine(String provideAppName,String name,String collectDay){

		final List<HsfProvideMachine> list = new ArrayList<HsfProvideMachine>();

		String sql = "select provider_machine_ip,provider_machine_cm,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? and key_name=?" +
				" and collect_date=? group by provider_machine_ip,provider_machine_cm";

		try {
			this.query(sql, new Object[]{provideAppName,name,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					HsfProvideMachine sum = new HsfProvideMachine();
					sum.setCallnum(rs.getLong("allnum"));
					sum.setProviderMachineIp(rs.getString("provider_machine_ip"));
					sum.setProviderMachineCm(rs.getString("provider_machine_cm"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;

	}

	/**
	 * 提供某个consume 应用调用了center的机器分布
	 * @param provideAppName
	 * @param name
	 * @param collectDay
	 * @return
	 */
	public List<HsfProvideMachine> sumConsumeAppToCenterMachine(String provideAppName,String consumeName,String collectDay){

		final List<HsfProvideMachine> list = new ArrayList<HsfProvideMachine>();

		String sql = "select provider_machine_ip,provider_machine_cm,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? and customer_app=?" +
				" and collect_date=? group by provider_machine_ip,provider_machine_cm";

		try {
			this.query(sql, new Object[]{provideAppName,consumeName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					HsfProvideMachine sum = new HsfProvideMachine();
					sum.setCallnum(rs.getLong("allnum"));
					sum.setProviderMachineIp(rs.getString("provider_machine_ip"));
					sum.setProviderMachineCm(rs.getString("provider_machine_cm"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;

	}

	public List<HsfProvideMachine> getProviderAppMachineAll(String provideAppName, String collectDay){
		final List<HsfProvideMachine> list = new ArrayList<HsfProvideMachine>();
		String sql = "select provider_machine_ip,provider_machine_cm,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? group by provider_machine_ip,provider_machine_cm";
		try {
			this.query(sql, new Object[]{provideAppName, collectDay}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HsfProvideMachine sum = new HsfProvideMachine();
					sum.setCallnum(rs.getLong("allnum"));
					sum.setProviderMachineIp(rs.getString("provider_machine_ip"));
					sum.setProviderMachineCm(rs.getString("provider_machine_cm"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}	

	/**
	 * 查询consumer 应用针对某个center应用的接口使用情况
	 * @param provideAppName
	 * @param consumeAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppProvderInterfaceSummary> sumConsumeAppToCenterAppInterface(String provideAppName,String consumeAppName, String collectDay){

		final List<AppProvderInterfaceSummary> list = new ArrayList<AppProvderInterfaceSummary>();

		String sql = "select provider_machine_cm,key_name,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? and  customer_app=? " +
				" and collect_date=? group by key_name,provider_machine_cm";

		try {
			this.query(sql, new Object[]{provideAppName,consumeAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppProvderInterfaceSummary sum = new AppProvderInterfaceSummary();
					sum.setCallAllNum(rs.getLong("allnum"));
					sum.setInterfaceName(rs.getString("key_name"));
					sum.setProvideSiteName(rs.getString("provider_machine_cm"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	} 

	/**
	 * 查询Center接口调用异常，统计总数
	 * sumAppProviderException
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppExceptionListPo> sumAppProviderException(String provideAppName, String collectDay){

		final List<AppExceptionListPo> list = new ArrayList<AppExceptionListPo>();

		String sql = "select customer_app,key_name,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? and key_name like '%exception%' GROUP BY customer_app ORDER BY allnum DESC";

		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppExceptionListPo sum = new AppExceptionListPo();
					sum.setAllnum(rs.getLong("allnum"));
					sum.setKeyname(rs.getString("key_name"));
					sum.setCustomerApp(rs.getString("customer_app"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}	

	/**
	 * 查询Center接口调用异常，显示详细的接口调用
	 * sumAppProviderException
	 * @param provideAppName
	 * @param collectDay
	 * @param customerName
	 * @return
	 */
	public List<AppExceptionListPo> sumAppProviderExceptionDetail(String provideAppName, String collectDay, String customerName){

		final List<AppExceptionListPo> list = new ArrayList<AppExceptionListPo>();

		String sql = "select key_name,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? and key_name like '%exception%' and customer_app=? GROUP BY customer_app,key_name ORDER BY allnum DESC, key_name";

		try {
			this.query(sql, new Object[]{provideAppName,collectDay,customerName}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppExceptionListPo sum = new AppExceptionListPo();
					sum.setAllnum(rs.getLong("allnum"));
					sum.setKeyname(rs.getString("key_name"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}		

	/**
	 * 为我消费的HSF统计异常报表信息
	 * @param opsNameString
	 * @param collectDay
	 * @return
	 */
	public Map<String, ConsumeHSFReport> getExceptionReport(final List<AppInfoPo> list, final String collectDay){

		final Map<String, ConsumeHSFReport> map = new HashMap<String, ConsumeHSFReport>();

		final Object[] param = new Object[list.size() + 1];
		String strIn = "";
		for(int i=0; i<list.size(); i++) {
			param[i] = list.get(i).getOpsName();
			strIn += "?,";
		}
		if(list.size() > 0) {
			strIn = strIn.substring(0, strIn.length() - 1);
		}
		param[list.size()] = collectDay;

		String sql = "select key_name, provider_app, call_num from csp_hsf_consumer_app_detail where provider_app in (" + strIn + ") and collect_date=? ";
		try {
			this.query(sql, param, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String keyName = rs.getString("key_name");
					String appName = rs.getString("provider_app");	//ops_name
					long callNum = rs.getLong("call_num");

					keyName = (keyName == null || keyName.trim().equals("")) ? "-":keyName.toLowerCase();
					appName = (appName == null || appName.trim().equals("")) ? "-":appName.toLowerCase();

					if(map.containsKey(appName)) {
						ConsumeHSFReport po = map.get(appName);
						po.setCallNumber(po.getCallNumber() + callNum);
						if(keyName.indexOf("bizexception") > 0) {
							po.setBizExceptionNum(po.getBizExceptionNum() + callNum);
						} else if (keyName.indexOf("exception") > 0) {
							po.setExceptionNum(po.getExceptionNum() + callNum);
						}
					} else {
						ConsumeHSFReport po = new ConsumeHSFReport();
						po.setAppName(appName);
						po.setOpsName(appName);
						po.setCallNumber(callNum);
						if(keyName.indexOf("bizexception") > 0) {
							po.setBizExceptionNum(callNum);
						} else if (keyName.indexOf("exception") > 0) {
							po.setExceptionNum(callNum);
						}
						map.put(appName, po);
					}
				}
			});
		} catch (Exception e) {
			logger.error("getExceptionReport:", e);
		}
		return map;
	}	

	public List<AppProviderSummary> getAppHistoryCall(String sourceAppName, String targetAppName, String startDate, String endDate){
		List<AppProviderSummary> list = null;
		String sql = null;
		Object[] param = null;
		if(targetAppName != null) {
			//FIXME provider的summary表，没有consumer name 的字段，暂时从detail表拉数据
			return toBeDelete(sourceAppName, targetAppName, startDate, endDate);
			//sql = "select * FROM csp_app_dep_hsf_provide_summary WHERE CONSUME_NAME = ? and PROVIDER_NAME = ? and COLLECT_TIME BETWEEN ? and ?";
			//param = new Object[]{sourceAppName, targetAppName, startDate, endDate};
		} else {
			sql = "select * FROM csp_app_dep_hsf_provide_summary WHERE PROVIDER_NAME = ? and COLLECT_TIME BETWEEN ? and ?";
			param = new Object[]{sourceAppName, startDate, endDate};
		}

		final Map<String, AppProviderSummary> map = new HashMap<String, AppProviderSummary>();

		try {
			this.query(sql, param, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					if(rs.getString("provider_group") != null && rs.getString("provider_group").equals("All")) {
						String key = rs.getString("collect_time");
						if(map.containsKey(key)) {
							AppProviderSummary sum = map.get(key);
							sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("CALL_SUM"));
						} else {
							AppProviderSummary sum = new AppProviderSummary();
							sum.setCallAllNum(rs.getLong("CALL_SUM"));
							sum.setCollectDate(rs.getDate("collect_time"));
							map.put(key, sum);
						}             
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppProviderSummary>(map.values());
		} else {
			list = new ArrayList<AppProviderSummary>();
		}   
		return list;
	}
	//FIXME to be deleted , just for tmp
	public List<AppProviderSummary> toBeDelete(String sourceAppName, String targetAppName, String startDate, String endDate){

		List<AppProviderSummary> list = null;
		final Map<String, AppProviderSummary> map = new HashMap<String, AppProviderSummary>();
		String sql = "select call_num, collect_date from csp_hsf_provider_app_detail where provider_app=? and customer_app=? " +
				" and collect_date between ? and ? ";

		try {
			this.query(sql, new Object[]{sourceAppName,targetAppName,startDate,endDate}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String key = rs.getString("collect_date");
					if(map.containsKey(key)) {
						AppProviderSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("call_num"));
					} else {
						AppProviderSummary sum = new AppProviderSummary();
						sum.setCallAllNum(rs.getLong("call_num"));
						sum.setCollectDate(rs.getDate("collect_date"));
						map.put(key, sum);
					}  
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppProviderSummary>(map.values());
		} else {
			list = new ArrayList<AppProviderSummary>();
		}
		return list;
	}

	/**
	 * 返回Interface和Consume的映射关系 
	 * @return
	 */
	public Map<String, Set<String>> getProvideInterfaceAndConsumeApp(String provideAppName, String collectDay) {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		String sql = "select customer_app,key_name from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? ";
		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Set<String> set = null;
					String key = rs.getString("key_name");
					if(map.containsKey(key)) {
						set = map.get(key);
					} else {
						set = new HashSet<String>();
						map.put(key, set);
					}
					set.add(rs.getString("customer_app"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}
	//  public List<AppConsumerSummary> getInterfaceHistoryCall(String provideAppName,String name,  String startDate, String endDate){
	//    List<AppConsumerSummary> list = new ArrayList<AppConsumerSummary>();
	//    String sql = "select collect_date,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? and key_name=?" +
	//        " and collect_date between ? and ?";
	//    final HashMap<String, AppConsumerSummary> map = new HashMap<String, AppConsumerSummary>();
	//    try {
	//      this.query(sql, new Object[]{provideAppName,name,startDate,endDate}, new SqlCallBack() {
	//        @Override
	//        public void readerRows(ResultSet rs) throws Exception {
	//          String key = rs.getString("collect_date");
	//          if(!map.containsKey(key)) {
	//            AppConsumerSummary sum = new AppConsumerSummary();
	//            sum.setCallAllNum(rs.getLong("allnum"));
	//            sum.setCollectDate(rs.getDate("collect_date"));
	//            map.put(key, sum);
	//          } else {
	//            AppConsumerSummary sum = map.get(key);
	//            sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
	//          }
	//        }
	//      });
	//    } catch (Exception e) {
	//      logger.error("", e);
	//    }
	//    if(map.size() > 0) {
	//      list = new ArrayList<AppConsumerSummary>(map.values());
	//    } else {
	//      list = new ArrayList<AppConsumerSummary>();
	//    }   
	//    return list;
	//  }
	public List<AppExceptionListPo> getHsfProvideMethods(String provideAppName, String collectDay){

		final List<AppExceptionListPo> list = new ArrayList<AppExceptionListPo>();

		String sql = "select key_name,sum(call_num) as allnum from csp_hsf_provider_app_detail where provider_app=? " +
				" and collect_date=? GROUP BY key_name ORDER BY allnum DESC";
		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppExceptionListPo sum = new AppExceptionListPo();
					sum.setAllnum(rs.getLong("allnum"));
					sum.setKeyname(rs.getString("key_name"));
					list.add(sum);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}	
	
	public List<String> getAppListFromProvider(String collect_time) {
		String sql = "select distinct provider_name from csp_app_dep_hsf_provide_summary where provider_group = 'All' and collect_time = ?";
		final List<String> appList = new ArrayList<String>();
		try {
			this.query(sql, new Object[]{collect_time}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					appList.add(rs.getString("provider_name"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return appList;
	}
	
	
	private void setAppProviderSummary(AppProviderSummary summary, ResultSet rs) throws SQLException {
		summary.setCallAllNum(rs.getLong("call_sum"));
		summary.setCollectDate(rs.getDate("collect_time"));
		summary.setConsumeSiteName("");
		summary.setOpsName("provider_name");
	}
	public AppProviderSummary getHsfSummaryInfo(String collect_time, String provider_name) {
		final AppProviderSummary summary = new AppProviderSummary();
		String sql = "select * from csp_app_dep_hsf_provide_summary where provider_group = 'All' and collect_time = ? and provider_name = ?";
		try {
			this.query(sql, new Object[]{collect_time, provider_name}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setAppProviderSummary(summary, rs);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return summary;
	}
	
	public List<AppProviderSummary> getHsfSummaryInfoList(String collect_time) {
		String sql = "select * from csp_app_dep_hsf_provide_summary where provider_group = 'All' and collect_time = ?";
		final List<AppProviderSummary> list = new ArrayList<AppProviderSummary>();
		try {
			this.query(sql, new Object[]{collect_time}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					try {
						AppProviderSummary summary = new AppProviderSummary();
						setAppProviderSummary(summary, rs);
						list.add(summary);
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
}
