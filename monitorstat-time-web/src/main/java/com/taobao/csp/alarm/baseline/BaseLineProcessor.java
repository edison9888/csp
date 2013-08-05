
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.baseline;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.BaselineCheckAo;
import com.taobao.csp.dao.hbase.base.HBaseUtil;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.util.DataSmoothing;
import com.taobao.csp.time.web.po.BaselineCheckPo;

/**
 * @author xiaodu
 *
 * 下午1:54:07
 */
public abstract class BaseLineProcessor {
	
	private static final Logger logger = Logger.getLogger(BaseLineProcessor.class);
	
	private Date processDate;
	
	protected String appName;
	
	protected String keyName;
	
	protected String propertyName;
	
	private int weeksize =3;
	
	private String scope=null;
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	
	private static List<String> dayTimeList = new ArrayList<String>();
	
	static {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		while(day ==cal.get(Calendar.DAY_OF_MONTH) ){
			dayTimeList.add(sdf.format(cal.getTime()));
			cal.add(Calendar.MINUTE, 1);
		}
	}
	
	
	

	protected Date getProcessDate() {
		return processDate;
	}

	protected String getAppName() {
		return appName;
	}

	protected String getKeyName() {
		return keyName;
	}

	protected String getPropertyName() {
		return propertyName;
	}

	protected Map<String, SameTime> getMapTime() {
		return mapTime;
	}

	private Map<String,SameTime> mapTime = new HashMap<String, SameTime>();
	
	public BaseLineProcessor(String appName,String keyName,String propertyName,String scope,Date date){
		this.processDate = date;
		this.keyName = keyName;
		this.appName = appName;
		this.propertyName = propertyName;
		this.scope = scope;
	}
	
	
	
	protected void putData(String time,Double value){
		
		SameTime st = mapTime.get(time);
		
		if(st == null){
			st =new SameTime(time);
			mapTime.put(time, st);
		}
		
		st.addValue(value);
		
	}
	
	
	/**
	 * 获取所有时间点的平均值
	 * @return
	 */
	protected Map<String,Double> getMean(){
		
		Map<String,Double> temp = new HashMap<String, Double>();
		
		for(Map.Entry<String,SameTime> entry: mapTime.entrySet()){
			temp.put(entry.getKey(), entry.getValue().getMean());
		}
		return temp;
	}
	
	
	
	/**
	 * 返回基线数据，这个基线数据是重Hbase里面查询出来,
	 * 
	 * 如果不存在，将返回空
	 * 
	 * @return Map<HH:mm,Double>
	 */
	public Map<String,Double> getBaseLine(){
		
		Map<String,Double> valuemap = new HashMap<String, Double>();
		
		HashMap<String, String> map = HBaseUtil.queryCellValue(hbaseRowID(), new String[]{Constants.BASELINE_HBASE_COL});
		if(map != null){
			String data = map.get(Constants.BASELINE_HBASE_COL);
			if(data != null){
				String[] tmp = data.split(";");
				for(String t:tmp){
					String[] v = t.split(",");
					valuemap.put(v[0],Double.parseDouble(v[1]) );
				}
			}
		}
		return valuemap;
	}
	
	abstract void process(Date date);
	
	public void process(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.processDate);
		
		for(int i=1;i<=weeksize;i++){
			cal.add(Calendar.DAY_OF_MONTH, -7);
			process(cal.getTime());
		}
		record("开始计算");
		toHbase();
	}
	
	
	/**
	 * 格式化时间
	 * @param date
	 * @return HH:mm
	 */
	String formatDate(Date date){
		return sdf.format(date);
	}
	
	
	private void record(String msg){
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.processDate);
		BaselineCheckPo po = new BaselineCheckPo();
		po.setAppName(appName);
		po.setKeyName(keyName);
		po.setPropertyName(propertyName);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		po.setWeekDay(week);
		po.setScope(scope);
		po.setState(msg);
		po.setProcessTime(this.getProcessDate());
		BaselineCheckAo.get().updateState(po);
	}
	
	 abstract String hbaseRowID();
	
	
	/**
	 * 将生成的基线数据存入Hbase
	 */
	private void toHbase(){
		
		StringBuilder sb = new StringBuilder();
		
		Map<String,Double> map = getMean();
		
		double[] yvar = new double[dayTimeList.size()];
		double[] xvar = new double[dayTimeList.size()];
		
		for(int i=0;i<dayTimeList.size();i++){
			String time = dayTimeList.get(i);
			xvar[i] = i;
			Double v = map.get(time);
			if(v ==null){
				if(i!=0){
					String pre = dayTimeList.get(i-1);
					Double t = map.get(pre);
					if(t!= null){
						map.put(time, t);
						yvar[i] = t;
					}else{
						map.put(time, 0d);
						yvar[i] = 0;
					}
				}else{
					map.put(time, 0d);
					yvar[i] = 0;
				}
			}else{
				yvar[i] = v.doubleValue();
			}
		}
		
		
		try {
			double[] ytmp = null;
			if(PropConstants.C_TIME.equals(propertyName)){
				ytmp = DataSmoothing.apacheLoess(xvar, yvar);
			}else{
				ytmp = DataSmoothing.cspSmooth(xvar, yvar);
			}
			if(ytmp != null){
				if(ytmp.length == dayTimeList.size()){
					for(int i=0;i<dayTimeList.size();i++){
						map.put(dayTimeList.get(i), ytmp[i]);
					}
				}
			}
		} catch (Exception e1) {
			logger.error("smoothing "+getAppName()+" "+getKeyName()+" "+getPropertyName()+" "+scope,e1);
		}
		record("计算完毕");
		DecimalFormat df1 = new DecimalFormat(".##");
		
		for(Map.Entry<String,Double> entry: map.entrySet()){
			sb.append(entry.getKey()).append(",").append(df1.format(entry.getValue())).append(";");		
		}
		
		try {
			String str = sb.toString();
			HBaseUtil.addRow(hbaseRowID(), Constants.BASELINE_HBASE_COL, str);
		} catch (IOException e) {
			logger.info(e);
		}
		record("入库完毕");
	}

}
