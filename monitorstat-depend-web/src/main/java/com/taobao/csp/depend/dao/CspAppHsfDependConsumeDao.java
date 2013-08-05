package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.hsf.AppConsumerInterfaceSummary;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppExceptionListPo;
import com.taobao.csp.depend.po.hsf.AppHsfProvidePo;
import com.taobao.csp.depend.po.hsf.AppProviderSummary;
import com.taobao.csp.depend.po.hsf.HsfProvideMachine;
import com.taobao.csp.depend.po.report.ConsumeHSFReport;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;


/**
 * 用于访问Center应用调用其他应用的HSF信息。
 * @author zhongting
 *
 */
public class CspAppHsfDependConsumeDao  extends MysqlRouteBase{

	private static final Logger logger =  Logger.getLogger(CspAppHsfDependConsumeDao.class);

	public CspAppHsfDependConsumeDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}


	/**
	 * 根据应用查询出所有的应用提供的服务
	 * @param provideAppName 
	 * @param collectDay
	 * @return
	 */
	public List<AppHsfProvidePo> findAppProvideHsfDetail(String provideAppName,Date collectDay){

		String sql = "select * from ms_monitor_distribe_provider where provider_app=? and collect_date=?";

		final List<AppHsfProvidePo> poList = new ArrayList<AppHsfProvidePo>();

		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					AppHsfProvidePo po = new AppHsfProvidePo();
					po.setCallNum(rs.getLong("call_num"));
					po.setCollectDay(new Date(rs.getTimestamp("collect_date").getTime()));
					po.setCustomerApp(rs.getString("customer_app"));
					po.setKeyName(rs.getString("key_name"));
					po.setProvideMachineCm(rs.getString("customer_machine_cm"));
					po.setProvideMachineIp(rs.getString("customer_machine_ip"));
					po.setProviderApp(rs.getString("provider_app"));
					po.setUseTime(rs.getDouble("use_time"));

					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return poList;
	}

	/**
	 * 统计针对某个center 应用的，调用的应用的总量
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppProviderSummary> sumCenterAppHsfToAppConsumer(String consumeAppName, String collectDay){

		List<AppProviderSummary> list = null;
		final Map<String, AppProviderSummary> map = new HashMap<String, AppProviderSummary>();

		String sql = "select provider_app,customer_machine_cm,call_num as allnum from csp_hsf_consumer_app_detail where customer_app=? " +
				" and collect_date=? ";

		try {
			this.query(sql, new Object[]{consumeAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("provider_app") + "-" + rs.getString("customer_machine_cm");
					if(map.containsKey(key)) {
						AppProviderSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
					} else {
						AppProviderSummary sum = new AppProviderSummary();
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setOpsName(rs.getString("provider_app"));
						sum.setConsumeSiteName(rs.getString("customer_machine_cm"));
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
	 * 统计某个center的接口调用外部接口的总量,返回结果按照key，应用名称聚合
	 * @param consumeAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppConsumerInterfaceSummary> sumCenterAppToAppProviderInterface(String consumeAppName, String collectDay){

		List<AppConsumerInterfaceSummary> list = null;
		final Map<String, AppConsumerInterfaceSummary> map = new HashMap<String, AppConsumerInterfaceSummary>();

		String sql = "select provider_app,customer_machine_cm,key_name,call_num as allnum from csp_hsf_consumer_app_detail where customer_app=? " +
				" and collect_date=? ";

		try {
			this.query(sql, new Object[]{consumeAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("key_name") + "-" + rs.getString("provider_app");
					if(map.containsKey(key)) {
						AppConsumerInterfaceSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
					} else {
						AppConsumerInterfaceSummary sum = new AppConsumerInterfaceSummary();
						sum.setProviderAppName(rs.getString("provider_app"));
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setInterfaceName(rs.getString("key_name"));
						sum.setConsumeSiteName(rs.getString("customer_machine_cm"));
						map.put(key, sum);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppConsumerInterfaceSummary>(map.values());
		} else {
			list = new ArrayList<AppConsumerInterfaceSummary>();
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

		List<AppConsumerSummary> list = null;

		String sql = "select customer_app,provider_machine_cm,call_num as allnum from csp_hsf_provider_app_detail where provider_app=? and key_name=?" +
				" and collect_date=? ";
		final HashMap<String, AppConsumerSummary> map = new HashMap<String, AppConsumerSummary>();
		try {
			this.query(sql, new Object[]{provideAppName,name,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("customer_app") + "-" + rs.getString("provider_machine_cm");
					if(!map.containsKey(key)) {
						AppConsumerSummary sum = new AppConsumerSummary();
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setOpsName(rs.getString("customer_app"));
						sum.setProvideSiteName(rs.getString("provider_machine_cm"));
						map.put(key, sum);
					} else {
						AppConsumerSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
					}
				}
			});
		} catch (Exception e) {
			logger.error("sumOneHsfInterfaceToAppConsumer:", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppConsumerSummary>(map.values());
		} else {
			list = new ArrayList<AppConsumerSummary>();
		}		
		return list;
	}

	/**
	 * 调用某个接口的机器的统计
	 * @param customerAppName
	 * @param name
	 * @param collectDay
	 * @return
	 */
	public List<HsfProvideMachine> findHsfConsumeInterfaceMachine(String customerAppName,String name,String collectDay){

		List<HsfProvideMachine> list = null;
		final HashMap<String, HsfProvideMachine> map = new HashMap<String, HsfProvideMachine>();
		String sql = "select customer_machine_ip,customer_machine_cm, call_num as allnum from csp_hsf_consumer_app_detail where customer_app=? and key_name=?" +
				" and collect_date=? ";

		try {
			this.query(sql, new Object[]{customerAppName,name,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("customer_machine_ip") + "-" + rs.getString("customer_machine_cm");
					if(!map.containsKey(key)) {
						HsfProvideMachine sum = new HsfProvideMachine();
						sum.setCallnum(rs.getLong("allnum"));
						sum.setProviderMachineIp(rs.getString("customer_machine_ip"));
						sum.setProviderMachineCm(rs.getString("customer_machine_cm"));
						map.put(key, sum);
					} else {
						HsfProvideMachine sum = map.get(key);
						sum.setCallnum(sum.getCallnum() + rs.getLong("allnum"));
					}					
				}
			});
		} catch (Exception e) {
			logger.error("findHsfConsumeInterfaceMachine:", e);
		}
		if(map.size() > 0) {
			list = new ArrayList<HsfProvideMachine>(map.values());
		} else {
			list = new ArrayList<HsfProvideMachine>();
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
	public List<HsfProvideMachine> sumCenterMachineToProvideApp(String provideAppName,String consumeName,String collectDay){

		List<HsfProvideMachine> list = null;
		final HashMap<String, HsfProvideMachine> map = new HashMap<String, HsfProvideMachine>();

		String sql = "select customer_machine_ip,customer_machine_cm, call_num as allnum from csp_hsf_consumer_app_detail where provider_app=? and customer_app=?" +
				" and collect_date=? ";

		try {
			this.query(sql, new Object[]{provideAppName,consumeName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String key = rs.getString("customer_machine_ip") + "-" + rs.getString("customer_machine_cm");
					if(!map.containsKey(key)) {
						HsfProvideMachine sum = new HsfProvideMachine();
						sum.setCallnum(rs.getLong("allnum"));
						sum.setProviderMachineIp(rs.getString("customer_machine_ip"));
						sum.setProviderMachineCm(rs.getString("customer_machine_cm"));
						map.put(key, sum);
					} else {
						HsfProvideMachine sum = map.get(key);
						sum.setCallnum(sum.getCallnum() + rs.getLong("allnum"));
					}						
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		if(map.size() > 0) {
			list = new ArrayList<HsfProvideMachine>(map.values());
		} else {
			list = new ArrayList<HsfProvideMachine>();
		}			
		return list;
	}

	/**
	 * 统计针对在center，center应用具体调用了某个应用的所有接口信息。
	 * @param provideAppName
	 * @param consumeAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppConsumerInterfaceSummary> sumCenterAppToProviderAppInterface(String provideAppName,String consumeAppName, String collectDay){

		List<AppConsumerInterfaceSummary> list = null;
		final HashMap<String, AppConsumerInterfaceSummary> map = new HashMap<String, AppConsumerInterfaceSummary>();

		String sql = "select customer_machine_cm,key_name, call_num as allnum from csp_hsf_consumer_app_detail where provider_app=? and  customer_app=? " +
				" and collect_date=?";

		try {
			this.query(sql, new Object[]{provideAppName,consumeAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String key = rs.getString("key_name") + "-" + rs.getString("customer_machine_cm");
					if(!map.containsKey(key)) {
						AppConsumerInterfaceSummary sum = new AppConsumerInterfaceSummary();
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setInterfaceName(rs.getString("key_name"));
						sum.setConsumeSiteName(rs.getString("customer_machine_cm"));
						map.put(key, sum);
					} else {
						AppConsumerInterfaceSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
					}						
				}
			});
		} catch (Exception e) {
			logger.error("AppConsumerInterfaceSummary:", e);
		}
		if(map.size() > 0) {
			list = new ArrayList<AppConsumerInterfaceSummary>(map.values());
		} else {
			list = new ArrayList<AppConsumerInterfaceSummary>();
		}			
		return list;
	} 

	/**
	 * 查询Center接口调用异常，统计总数
	 * sumAppConsumerException
	 * @param consumeAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppExceptionListPo> sumAppConsumerException(String consumeAppName, String collectDay){

		List<AppExceptionListPo> list = null;
		final HashMap<String, AppExceptionListPo> map = new HashMap<String, AppExceptionListPo>();

		String sql = "select provider_app,key_name,call_num as allnum from csp_hsf_consumer_app_detail where customer_app=? " +
				" and collect_date=? ";

		try {
			this.query(sql, new Object[]{consumeAppName,collectDay}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String keyName = rs.getString("key_name");
					if(keyName == null || keyName.toLowerCase().indexOf("exception") < 0) {
						return;
					}

					String key = rs.getString("provider_app");
					key = (key == null) ? "-":key;

					if(!map.containsKey(key)) {
						AppExceptionListPo sum = new AppExceptionListPo();
						sum.setAllnum(rs.getLong("allnum"));
						sum.setKeyname(rs.getString("key_name"));
						sum.setCustomerApp(rs.getString("provider_app"));
						map.put(key, sum);
					} else {
						AppExceptionListPo sum = map.get(key);
						sum.setAllnum(sum.getAllnum() + rs.getLong("allnum"));
					}						
				}
			});
		} catch (Exception e) {
			logger.error("sumAppConsumerException异常", e);
		}
		if(map.size() > 0) {
			list = new ArrayList<AppExceptionListPo>(map.values());
		} else {
			list = new ArrayList<AppExceptionListPo>();
		}			
		Collections.sort(list);
		return list;
	}	

	/**
	 * 查询Center接口调用异常，显示详细的接口调用
	 * sumAppConsumerExceptionDetail
	 * @param provideAppName
	 * @param collectDay
	 * @param customerName
	 * @return
	 */
	public List<AppExceptionListPo> sumAppConsumerExceptionDetail(String consumerAppName, String collectDay, String customerName){

		List<AppExceptionListPo> list = null;
		final HashMap<String, AppExceptionListPo> map = new HashMap<String, AppExceptionListPo>();

		String sql = "select key_name, call_num as allnum from csp_hsf_consumer_app_detail where customer_app=? " +
				" and collect_date=? and provider_app=?";

		try {
			this.query(sql, new Object[]{consumerAppName,collectDay,customerName}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String keyName = rs.getString("key_name");
					if(keyName == null || keyName.toLowerCase().indexOf("exception") < 0) {
						return;
					}

					String key = rs.getString("provider_app") + "-" + rs.getString("key_name");
					if(!map.containsKey(key)) {
						AppExceptionListPo sum = new AppExceptionListPo();
						sum.setAllnum(rs.getLong("allnum"));
						sum.setKeyname(rs.getString("key_name"));
						map.put(key, sum);
					} else {
						AppExceptionListPo sum = map.get(key);
						sum.setAllnum(sum.getAllnum() + rs.getLong("allnum"));
					}						
				}
			});
		} catch (Exception e) {
			logger.error("sumAppConsumerExceptionDetail异常：", e);
		}
		if(map.size() > 0) {
			list = new ArrayList<AppExceptionListPo>(map.values());
		} else {
			list = new ArrayList<AppExceptionListPo>();
		}			
		Collections.sort(list);
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

		String sql = "select key_name, customer_app, call_num from csp_hsf_consumer_app_detail where customer_app in (" + strIn + ") and collect_date=? ";
		try {
			this.query(sql, param, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String keyName = rs.getString("key_name");
					String appName = rs.getString("customer_app");	//ops_name
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
			sql = "select * FROM csp_app_dep_hsf_consume_summary WHERE CONSUME_NAME = ? and PROVIDER_NAME = ? and COLLECT_TIME BETWEEN ? and ?";
			param = new Object[]{sourceAppName, targetAppName, startDate, endDate};
		} else {
			sql = "select * FROM csp_app_dep_hsf_consume_summary WHERE CONSUME_NAME = ? and COLLECT_TIME BETWEEN ? and ?";
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

	public List<AppProviderSummary> getAppConsumeSummary(String sourceAppName, String startDate){
		List<AppProviderSummary> list = null;
		String sql = null;
		Object[] param = null;
		sql = "select * FROM csp_app_dep_hsf_consume_summary WHERE CONSUME_NAME = ? and COLLECT_TIME =?";
		param = new Object[]{sourceAppName, startDate};

		//appName, AppProviderSummary
		final Map<String, AppProviderSummary> map = new HashMap<String, AppProviderSummary>();

		try {
			this.query(sql, param, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					if(rs.getString("provider_group") != null && rs.getString("provider_group").equals("All")) {
						String key = rs.getString("provider_name");
						if(map.containsKey(key)) {
							AppProviderSummary sum = map.get(key);
							sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("CALL_SUM"));
						} else {
							AppProviderSummary sum = new AppProviderSummary();
							sum.setCallAllNum(rs.getLong("CALL_SUM"));
							sum.setOpsName(rs.getString("provider_name"));
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

	/**
	 * 提供HSf 某个接口的应用调用统计
	 * @param provideAppName
	 * @param name
	 * @param collectDay
	 * @return
	 */
	public List<AppConsumerSummary> getInterfaceHistoryCall(String provideAppName, String name, String startDate, String endDate){

		List<AppConsumerSummary> list = null;

		String sql = "select collect_date,call_num as allnum from csp_hsf_consumer_app_detail where customer_app=? and key_name=?" +
				" and collect_date between ? and ? ";
		final HashMap<String, AppConsumerSummary> map = new HashMap<String, AppConsumerSummary>();
		try {
			this.query(sql, new Object[]{provideAppName, name, startDate, endDate}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					String key = rs.getString("collect_date");
					if(!map.containsKey(key)) {
						AppConsumerSummary sum = new AppConsumerSummary();
						sum.setCallAllNum(rs.getLong("allnum"));
						sum.setCollectDate(rs.getDate("collect_date"));
						map.put(key, sum);
					} else {
						AppConsumerSummary sum = map.get(key);
						sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("allnum"));
					}
				}
			});
		} catch (Exception e) {
			logger.error("getInterfaceHistoryCall:", e);
		}

		if(map.size() > 0) {
			list = new ArrayList<AppConsumerSummary>(map.values());
		} else {
			list = new ArrayList<AppConsumerSummary>();
		}   
		return list;
	}

	private void setAppConsumerSummary(AppConsumerSummary summary, ResultSet rs, String type) throws SQLException {
		if(type.equalsIgnoreCase("medepend")) {
			summary.setOpsName(rs.getString("provider_name"));
		} else {	//dependme
			summary.setOpsName(rs.getString("consume_name"));
		}
		summary.setCallAllNum(rs.getLong("call_sum"));
		summary.setCollectDate(rs.getDate("collect_time"));
		summary.setProvideSiteName("");
	}

	public List<AppConsumerSummary> getHsfSummaryInfoListMeDepend(String collect_time, String appName) {
		return getSummmaryListByType(collect_time, appName, "medepend");
	}

	public List<AppConsumerSummary> getHsfSummaryInfoListDependMe(String collect_time, String appName) {
		return getSummmaryListByType(collect_time, appName, "dependme");
	}

	private List<AppConsumerSummary> getSummmaryListByType(String collect_time, String appName, final String type) {
		String sql = "";
		if(type.equalsIgnoreCase("medepend")) {
			sql = "select * from csp_app_dep_hsf_consume_summary where provider_group = 'All' and collect_time = ? and consume_name = ?";
		} else {	//dependme
			sql = "select * from csp_app_dep_hsf_consume_summary where provider_group = 'All' and collect_time = ? and provider_name = ?";
		}
		final List<AppConsumerSummary> list = new ArrayList<AppConsumerSummary>();
		try {
			this.query(sql, new Object[]{collect_time, appName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					try {
						AppConsumerSummary summary = new AppConsumerSummary();
						setAppConsumerSummary(summary, rs, type);
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
