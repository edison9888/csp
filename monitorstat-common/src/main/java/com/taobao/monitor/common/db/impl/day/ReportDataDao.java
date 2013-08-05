package com.taobao.monitor.common.db.impl.day;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.ReportBasicDataPo;
import com.taobao.monitor.common.po.ReportInvokeDataPo;

/***
 * 报表数据
 * @author youji.zj
 *
 */
public class ReportDataDao extends MysqlRouteBase {
	
	public static Logger logger = Logger.getLogger(ReportDataDao.class);
	
	public ReportDataDao()  {
		super(DbRouteManage.get().getDbRouteByRouteId("Main_DAY"));
	}

	/***
	 * 查找基本信息
	 * @param appName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ReportBasicDataPo> findBasicReportData(String appName,Date startDate,Date endDate){
		
		final List<ReportBasicDataPo> datas = new ArrayList<ReportBasicDataPo>();
		
		String sql = "select * from ms_monitor_report_basic where app_name = ? and collect_date >= ? and collect_date <=? order by collect_date desc";
		try {
			this.query(sql, new Object[]{ appName, startDate, endDate }, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					ReportBasicDataPo po = new ReportBasicDataPo();
					po.setAppName(rs.getString("app_name"));
					po.setMachineInfo(rs.getString("machine_num"));
					po.setLoad(rs.getDouble("loads"));
					po.setPv(rs.getLong("pv"));
					po.setUv(rs.getInt("uv"));
					po.setQps(rs.getDouble("qps"));
					po.setRt(rs.getDouble("rt"));
					po.setCapacityLevel(rs.getDouble("capacity_level"));
					po.setSingleCost(rs.getDouble("single_cost"));
					po.setFullGcCount(rs.getInt("full_gc_count"));
					po.setFullGcTime(rs.getDouble("full_gc_time"));
					po.setYoungGcCount(rs.getInt("young_gc_count"));
					po.setYoungGcTime(rs.getDouble("young_gc_time"));
					po.setCollectDate(rs.getDate("collect_date"));
					
					datas.add(po);
				}});
		} catch (Exception e) {
			logger.error(e);
		}
		
		return datas;
	}
	
	/***
	 * 插入基本信息
	 * @param po
	 */
	public void addBasicReportData(ReportBasicDataPo po){
	
		String sql = "insert into ms_monitor_report_basic (app_name,machine_num,loads,pv,uv,qps,rt,capacity_level,single_cost,full_gc_count,full_gc_time," +
				"young_gc_count,young_gc_time,collect_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{ po.getAppName(), po.getMachineInfo(), po.getLoad(), po.getPv(), po.getUv(), po.getQps(), po.getRt(),
					po.getCapacityLevel(), po.getSingleCost(), po.getFullGcCount(), po.getFullGcTime(), po.getYoungGcCount(), po.getYoungGcTime(), po.getCollectDate()});
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/***
	 * 查找调用信息
	 * @param appName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Map<String, List<ReportInvokeDataPo>> findInvokeReportData(String appName,Date startDate,Date endDate){
		
		final Map<String, List<ReportInvokeDataPo>> datas = new HashMap<String, List<ReportInvokeDataPo>>();
		
		String sql = "select app_name,invoke_type,resource_name, sum(counts) as counts, sum(times*counts)/sum(counts) as times, max(collect_date) as collect_date" +
				" from ms_monitor_report_invoke where app_name = ? and collect_date >= ? and collect_date <=? group by app_name,invoke_type,resource_name";
		
		final DecimalFormat df = new DecimalFormat("00.00");
		try {
			this.query(sql, new Object[]{ appName, startDate, endDate }, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					ReportInvokeDataPo po = new ReportInvokeDataPo();
					
					String times = df.format(rs.getDouble("times"));
					String invokeType = rs.getString("invoke_type");
					po.setAppName(rs.getString("app_name"));
					po.setInvokeType(invokeType);
					po.setResouceName(rs.getString("resource_name"));
					po.setCount(rs.getLong("counts"));
					po.setTime(Double.parseDouble(times));
					po.setCollectDate(rs.getDate("collect_date"));
					
					List<ReportInvokeDataPo> pos = datas.get(invokeType);
					if (pos == null) {
						pos = new ArrayList<ReportInvokeDataPo>();
						datas.put(invokeType, pos);
					}
					
					if (pos.size() < 5) {
						pos.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error(e);
		}
		
		return datas;
	}
	
	/***
	 * 插入调用信息
	 * @param po
	 */
	public void addInvokeReportData(ReportInvokeDataPo po){
		
		String sql = "insert into ms_monitor_report_invoke (app_name,invoke_type,resource_name,counts,times,collect_date" +
				") values (?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{ po.getAppName(), po.getInvokeType(), po.getResouceName(), po.getCount(), po.getTime(), po.getCollectDate()});
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
