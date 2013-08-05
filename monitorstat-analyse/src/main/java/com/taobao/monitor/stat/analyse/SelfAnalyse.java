
package com.taobao.monitor.stat.analyse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 分5分钟在库表中 取值 在做平均
 * @author xiaodu
 * @version 2010-4-8 上午10:51:36
 */
public class SelfAnalyse extends Analyse {
	
	private static final Logger logger =  Logger.getLogger(SelfAnalyse.class);
	//private SelfMonitorDataDao dao = new SelfMonitorDataDao(Config.getValue("MYSQL_SELF_URL_KEY"),Config.getValue("MYSQL_SELF_UER_KEY"),Config.getValue("MYSQL_SELF_PSW_KEY"));
	private AppInfoPo app;
	
	private ReportContent ceontent = new ReportContent();
	
	public SelfAnalyse(String opsName){
		super(opsName);
		this.app = ceontent.getAppByName(opsName);
	}
	
	
	public void analyseLogFile(ReportContentInterface content) {
		logger.info("开始SelfJob:"+this.getAppName());
		if(this.app==null){
			logger.info("no app");
			return ;
		}
		String feature = app.getFeature();
		
		if(feature!=null){
			String[] _features =  feature.split(";");
			String nagiosGroup = null;		
			for(String _featrue:_features){
				if(_featrue.indexOf("nagiosGroup")>-1){
					String[] _nagiosType = _featrue.split(":");
					if(_nagiosType.length==2){
						nagiosGroup = _nagiosType[1];
					}
					
				}
			}		
			if(nagiosGroup!=null){
				String[] _groups = nagiosGroup.split(",");
				
				List<String> groupList = new ArrayList<String>();
				for(String g:_groups)
					groupList.add(g);		
				try{
//				//取得所有告警点
				//Map<String,Long> alarmCount  = dao.findAppAlarmCount(groupList,this.getCollectDate());
				//ReportContent.getInstance().putReportDataByCount(app.getName(), alarmCount, this.getCollectDate());
			
				//dao.close();
				}catch (Exception e) {
					logger.info("",e);
				}
				
			}
		}		
		
		
		
		
		//39代表response time
		//38代表qps
		//CPU     18 CPU user 19	CPU	sys
		//IOWAIT 20
		//Load  9
		//Men  2	MEM	memtotal 3	MEM	memfree

		//Swap 6 MEM swaptotal	7 MEM swapfree
		
		
		String opsfree = this.getAppName();
		logger.info("开始SelfJob:"+opsfree);
		//IN_ResT
		List<TimeValue> timeValueResT = getTimevalue(opsfree,39);
		intoDbByTime("IN_ResT",timeValueResT);
		double maxAverageResT = getMaxAverage(timeValueResT);
		intoDbByDay("IN_ResT",maxAverageResT);
		
		//IN_QPS
		List<TimeValue> timeValueQps = getTimevalue(opsfree,38);
		intoDbByTime("IN_QPS",timeValueQps);
		double maxAverageQps = getMaxAverage(timeValueQps);
		intoDbByDay("IN_QPS",maxAverageQps);
		
		
		
		//SELF_CPU_AVERAGEUSERTIMES
		Map<Long,Double> timeValueCpuUser = getTimevalueMap(opsfree,18);
		Map<Long,Double> timeValueCpuSys = getTimevalueMap(opsfree,19);
		
		List<TimeValue> cpu = new ArrayList<TimeValue>();
		
		for(Map.Entry<Long, Double> entry:timeValueCpuUser.entrySet()){
			TimeValue tv = new TimeValue();
			Long time = entry.getKey();
			Double value = entry.getValue();
			Double d = timeValueCpuSys.get(time);
			if(d!=null){				
				Double scale = Arith.add(d, value);
				tv.setTime(time);
				tv.setValue(scale);
				cpu.add(tv);
			}
		}
		double maxAveragecpu = getMaxAverage(cpu);	
		intoDbByTime("SELF_CPU_AVERAGEUSERTIMES",cpu);
		intoDbByDay("SELF_CPU_AVERAGEUSERTIMES",maxAveragecpu);
		
		
		
		//Load
		List<TimeValue> timeValueLoad = getTimevalue(opsfree,9);
		intoDbByTime("SELF_Load_AVERAGEUSERTIMES",timeValueLoad);
		double maxAverageLoad = getMaxAverage(timeValueLoad);
		intoDbByDay("SELF_Load_AVERAGEUSERTIMES",maxAverageLoad);
		
		//SELF_IOWAIT_AVERAGEUSERTIMES
		List<TimeValue> timeValueIowait = getTimevalue(opsfree,20);
		intoDbByTime("SELF_IOWAIT_AVERAGEUSERTIMES",timeValueIowait);
		double maxAverageIowait = getMaxAverage(timeValueIowait);
		intoDbByDay("SELF_IOWAIT_AVERAGEUSERTIMES",maxAverageIowait);
		
		//SELF_Memory(4G)_AVERAGEUSERTIMES
		Map<Long,Double> timeValueMemtotal = getTimevalueMap(opsfree,2);
		Map<Long,Double> timeValuememfree = getTimevalueMap(opsfree,3);		
		List<TimeValue> memory = new ArrayList<TimeValue>();
		for(Map.Entry<Long, Double> entry:timeValueMemtotal.entrySet()){
			TimeValue tv = new TimeValue();
			Long time = entry.getKey();
			Double value = entry.getValue();
			Double d = timeValuememfree.get(time);
			if(d!=null){				
				Double scale = Arith.mul(Arith.sub(1, Arith.div(d, value)), 100);
				tv.setTime(time);
				tv.setValue(scale);
				memory.add(tv);
			}
		}
		double maxAveragememory = getMaxAverage(memory);		
		intoDbByTime("SELF_Memory_AVERAGEUSERTIMES",memory);
		intoDbByDay("SELF_Memory_AVERAGEUSERTIMES",maxAveragememory);

		
		
		
		//SELF_Swap(2G)_AVERAGEUSERTIMES
		Map<Long,Double> timeValueswaptotal = getTimevalueMap(opsfree,6);
		Map<Long,Double> timeValueswapfree = getTimevalueMap(opsfree,7);		
		List<TimeValue> swap = new ArrayList<TimeValue>();
		for(Map.Entry<Long, Double> entry:timeValueswaptotal.entrySet()){
			TimeValue tv = new TimeValue();
			Long time = entry.getKey();
			Double value = entry.getValue();
			Double d = timeValueswapfree.get(time);
			if(d!=null){				
				Double scale = Arith.mul(Arith.sub(1, Arith.div(d, value)), 100);
				tv.setTime(time);
				tv.setValue(scale);
				swap.add(tv);
			}
		}
		double maxAverageswap = getMaxAverage(swap);		
		intoDbByTime("SELF_Swap_AVERAGEUSERTIMES",swap);
		intoDbByDay("SELF_Swap_AVERAGEUSERTIMES",maxAverageswap);
		
		
		
		logger.info("end self");
		
	}
	
	
	
	
	
	/**
	 * 取得8点30到10点30的平均
	 * @param timeValue
	 * @return
	 */
	private double getMaxAverage(List<TimeValue> timeValue){
		int start = 203000;
		int end = 223000;
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		double sum = 0d;
		int size = 0;
		for(TimeValue tv:timeValue){
			Date date = new Date(tv.getTime());
			String str = sdf.format(date);
			int time = Integer.parseInt(str);			
			if(time>=start&&time<end){				
				sum = Arith.add(tv.getValue(), sum);
				size++;
			}
		}		
		if(size>0){
			return Arith.div(sum, size);
		}
		return 0;		
	}


	@Override
	protected void insertToDb(ReportContentInterface content) {
		
	}
	
	
	
	private void intoDbByDay(String keyName,Double value){		
		ceontent.putReportDataByCount(this.getAppName(), keyName,value.toString(), this.getCollectDate());
	}
	
	private void intoDbByTime(String keyName,List<TimeValue> timeValueList){		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");		
		
		for(TimeValue tv:timeValueList){
			Date date = new Date(tv.getTime());
			ceontent.putReportData(this.getAppName(),keyName, tv.getValue().toString(), sdf.format(date));	
		}
	}
	
	
	
	
	
	private List<TimeValue> getTimevalue(String opsfree,int type){
		
		
		String start = this.getCollectDate()+" 00:00:00";
		String end = this.getCollectDate()+" 23:59:59";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		
		List<TimeValue> timeValueList = new ArrayList<TimeValue>();
		boolean g = true;
		for(int i=0;i<3&&g;i++){		
			try{
				Date startDate = sdf.parse(start);
				Date endDate = sdf.parse(end);
				String interface_url = "http://172.23.100.101/cgi-bin/rrd_fetch.pl?name="+opsfree+"&srv_id="+type+"&type=app&start="+(startDate.getTime()/1000)+"&end="+(endDate.getTime()/1000);
				logger.info(interface_url);
				URL url = new URL(interface_url);
				URLConnection urlCon = url.openConnection();
				urlCon.setDoInput(true);
				urlCon.setConnectTimeout(30000);
				urlCon.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
				String line = null;
				while((line=reader.readLine())!=null){				
					String[] timeValue = line.split(";");
					for(String _timeValue:timeValue){
						String[] tv = _timeValue.split(":");
						if(tv.length==2){
							TimeValue _tv = new TimeValue();
							long time = Long.parseLong(tv[0])*1000;
							Double value = Double.parseDouble(tv[1]);
							if(value<0){
								continue;
							}
							_tv.setTime(time);
							_tv.setValue(value);
							timeValueList.add(_tv);
						}
					}			
				}
				g = false;
			}catch(Exception e){
				logger.error("",e);
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return timeValueList;
	}
	
	
	
	private Map<Long,Double> getTimevalueMap(String opsfree,int type){
		
		
		
		String start = this.getCollectDate()+" 00:00:00";
		String end = this.getCollectDate()+" 23:59:59";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<Long,Double> timeValueMap = new HashMap<Long, Double>();
		boolean g = true;
		for(int i=0;i<3&&g;i++){
			try{
				Date startDate = sdf.parse(start);
				Date endDate = sdf.parse(end);
				String interface_url = "http://172.23.100.101/cgi-bin/rrd_fetch.pl?name="+opsfree+"&srv_id="+type+"&type=app&start="+(startDate.getTime()/1000)+"&end="+(endDate.getTime()/1000);
				logger.info(interface_url);
				URL url = new URL(interface_url);
				URLConnection urlCon = url.openConnection();
				urlCon.setDoInput(true);
				urlCon.setConnectTimeout(30000);
				urlCon.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
				String line = null;
				while((line=reader.readLine())!=null){
					String[] timeValue = line.split(";");
					for(String _timeValue:timeValue){
						String[] tv = _timeValue.split(":");
						if(tv.length==2){
							long time = Long.parseLong(tv[0])*1000;
							Double value = Double.parseDouble(tv[1]);
							timeValueMap.put(time, value);
						}
					}			
				}
				g = false;
			}catch(Exception e){
				logger.error("",e);
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return timeValueMap;
	}
	
	
	
	private class TimeValue{
		private long time;
		private Double value;
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public Double getValue() {
			return value;
		}
		public void setValue(Double value) {
			this.value = value;
		}		
	}
	

}
