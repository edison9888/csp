
package com.taobao.csp.day.ao;

import java.util.Date;
import java.util.List;

import com.taobao.csp.day.po.ApacheSpecialPo;
import com.taobao.csp.day.po.SphPo;
import com.taobao.csp.day.po.TddlPo;
import com.taobao.csp.day.po.TddlSummaryPo;
import com.taobao.csp.day.po.TdodPo;

/***
 * 日报数据入库接口
 * @author youji.zj
 * 
 * @version 2012-09-06
 */
public interface ReportContentInterface {
	
	public void putReportDateOfTempTDDL(List<TddlPo> tddlList);
	public void putReportDateOfDayTDDL(List<TddlPo> tddlList);
	public void putReportDateOfDayTDDLSummaryNew(Date date);
	
	public void updateMaxResponseOfDayTDDLSummaryNew(TddlSummaryPo po);
	public void updateMinResponseOfDayTDDLSummaryNew(TddlSummaryPo po);
	

	public void deleteTempTddl(String tableName, String collectTime);
	
	public List<TddlPo> findReportDataOfTddlTemp(String tableName, String startTime, String endTime, String appName, int oneTime, int index);
	
	public void putReportDateOfDayTDDLSummaryNew(List<TddlPo> tddlList);
	
	public void putReportDateOfSph(List<SphPo> list);
	
	public void putReportDateOfTdod(List<TdodPo> list);
	
	public void putReportDateOfApacheSpecial(List<ApacheSpecialPo> list);
}
