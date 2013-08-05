
package com.taobao.monitor.stat.analyse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.util.UseInfo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-8 上午10:41:15
 */
public class ApacheLogAnalyse extends Analyse {
	
	private static final Logger logger =  Logger.getLogger(ApacheLogAnalyse.class);
	private AppInfoPo app;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(20);
	
	private Queue<Future<ApacheRun>> queue = new ConcurrentLinkedQueue<Future<ApacheRun>>();
	
	private Map<String ,UseInfo> useAllMap = new HashMap<String ,UseInfo>();
	
	private Map<String ,Long> siteAllPvMap = new HashMap<String ,Long>();
	
	private Map<String, Long> statusAllMap = new HashMap<String, Long>();// 存储出现的状态码，key为状态码，value为此状态码出现次数
	private int hostNum = 0;
	
	private long allPv = 0;
	
	
	
	public ApacheLogAnalyse(String opsName){
		super(opsName);
		this.app = AppInfoAo.get().getAppInfoByOpsName(opsName);;
	}
	
	
	
	
	public void analyseLogFile(ReportContentInterface content) {
		
		
		
		allPv = 0;
		hostNum = 0;
		logger.info("start ApacheLogAnalyse ");
		
		List<HostPo> list = OpsFreeHostCache.get().getHostListNoCache(app.getOpsField(), app.getOpsName());
		
		CountDownLatch count = new CountDownLatch(list.size());
		
		for(HostPo info:list){
			logger.info("开始处理 ip:"+info.getHostIp());
			parseApache(info,count);
		}
		
		logger.info("开始等待  "+app.getOpsName()+" apache 日志处理结果 机器数量为:"+list.size());
		
		try {
			count.await();
		} catch (InterruptedException e) {
		}
		
		Future<ApacheRun> future = null;
		while((future = queue.poll())!=null){
			try {
				ApacheRun run = future.get(60, TimeUnit.SECONDS);
				
				if(run.isSuccess()){
					hostNum++;
				}
				
				for(Map.Entry<String ,Long> entry:run.getSitePvMap().entrySet()){
					Long l = siteAllPvMap.get(entry.getKey());
					if(l == null){
						siteAllPvMap.put(entry.getKey(), entry.getValue());
					}else{
						siteAllPvMap.put(entry.getKey(), entry.getValue()+l);
					}
				}
				
				
				for(Map.Entry<String ,Long> entry:run.getStatusMap().entrySet()){
					Long l = statusAllMap.get(entry.getKey());
					if(l == null){
						statusAllMap.put(entry.getKey(), entry.getValue());
					}else{
						statusAllMap.put(entry.getKey(), entry.getValue()+l);
					}
				}
				
				
				for(Map.Entry<String ,UseInfo> entry:run.getUseMap().entrySet()){
					UseInfo l = useAllMap.get(entry.getKey());
					if(l == null){
						useAllMap.put(entry.getKey(), entry.getValue());
					}else{
						l.setCalls(entry.getValue().getCalls()+l.getCalls());
						l.setTime(entry.getValue().getTime()+l.getTime());
					}
				}
			} catch (Exception e) {
			}
		}
		
		
		
		insertToDb(content);
		
	}
	
	private void parseApache(HostPo info,CountDownLatch count){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/yyyy-MM-dd");
		String path = "/home/admin/cai/logs/cronolog/"+sdf.format(this.getParseLogCalendar().getTime())+"-taobao-access_log"; 
		ApacheRun run = new ApacheRun(info,path,count);
		Future<ApacheRun> f = threadPool.submit(run, run);
		queue.add(f);
	}
	

	@Override
	protected void insertToDb(ReportContentInterface content) {
		int start = 21;
		int end =22;
		String rushHours = app.getAppRushHours();
		
		try{
			if(rushHours!=null){
				String[] tmp = rushHours.split("-");
				if(tmp.length == 2){
					start = Integer.parseInt(tmp[0]);
					end  = Integer.parseInt(tmp[1]);
				}
			}
		}catch (Exception e) {
		}
		
		
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		content.putReportDataByCount(app.getOpsName(), "PV_VISIT_COUNTTIMES", allPv,this.getCollectDate());	
		
		for(Map.Entry<String ,Long> e:siteAllPvMap.entrySet()){
			content.putReportDataByCount(app.getOpsName(), "PV_VISIT_"+e.getKey()+"_COUNTTIMES", e.getValue(),this.getCollectDate());	
		}
		
		for(Map.Entry<String, Long> e:statusAllMap.entrySet()) {
			if(e.getKey().length() == 3)
			{
				content.putReportDataByCount(app.getOpsName(), "PV_VISIT_STATE_"+e.getKey()+"_COUNTTIMES", e.getValue(), this.getCollectDate());
			}
		}
		
		UseInfo qps = new UseInfo();
		int s = 0;
		for(Map.Entry<String, UseInfo> entry:useAllMap.entrySet()){
			String time = entry.getKey();
			UseInfo info = entry.getValue();
			try {
				Date date1 = sdf1.parse(time);
				int hours = date1.getHours();
				if(hours >=start &&hours <= end){
					qps.setCalls(qps.getCalls()+info.getCalls());
					qps.setTime(qps.getTime()+info.getTime());
					s++;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			content.putReportData(app.getOpsName(), "PV_VISIT_COUNTTIMES", info.getCalls(), entry.getKey());
			if(info.getCalls() > 0)
			content.putReportData(app.getOpsName(), "PV_REST_AVERAGEUSERTIMES", Arith.div(info.getTime(), info.getCalls(),2)+"", entry.getKey());
		}
		if(qps.getCalls() == 0){
			content.putReportDataByCount(app.getOpsName(), "PV_REST_AVERAGEUSERTIMES", "-1",this.getCollectDate());
		}else{
			content.putReportDataByCount(app.getOpsName(), "PV_REST_AVERAGEUSERTIMES", Arith.div(qps.getTime(), qps.getCalls(),2)+"",this.getCollectDate());
		}
		if(s*60*hostNum == 0){
			content.putReportDataByCount(app.getOpsName(), "PV_QPS_AVERAGEUSERTIMES", "-1",this.getCollectDate());
		}else{
			content.putReportDataByCount(app.getOpsName(), "PV_QPS_AVERAGEUSERTIMES", Arith.div(qps.getCalls(), (s*60*hostNum),2)+"",this.getCollectDate());
		}
	}
	
	
	private class ApacheRun implements Runnable{
		private HostPo info;
		private String path;
		private CountDownLatch down;
		
		private boolean success;
		
		private Map<String ,UseInfo> useMap = new HashMap<String ,UseInfo>();
		
		private Map<String ,Long> sitePvMap = new HashMap<String ,Long>();
		
		private Map<String, Long> statusMap = new HashMap<String, Long>();// 存储出现的状态码，key为状态码，value为此状态码出现次数
		
		
		public boolean isSuccess() {
			return success;
		}

		public Map<String, UseInfo> getUseMap() {
			return useMap;
		}

		public Map<String, Long> getSitePvMap() {
			return sitePvMap;
		}

		public Map<String, Long> getStatusMap() {
			return statusMap;
		}

		public ApacheRun(HostPo info,String path,CountDownLatch down){
			this.info = info;
			this.path = path;
			this.down = down;
		}

		@Override
		public void run() {
			Connection conn = new Connection(info.getHostIp());
			try {			
				conn.connect(null,5000,5000);
				boolean auth = conn.authenticateWithPassword(ApacheLogAnalyse.this.app.getLoginName(), ApacheLogAnalyse.this.app.getLoginPassword());
				if(auth){
					try{
						parseRest(conn,path,info.getHostSite());
					}catch (Exception e) {
						logger.info(info.getHostIp()+"load ApacheLogAnalyse ",e);
					}
				}
				
			} catch (Exception e) {
				logger.error("parseApachePv error ip:"+info.getHostIp(),e);
			}finally{
				conn.close();
			}
			down.countDown();
			logger.info("结束:"+info.getHostIp());
		}
		
		private void parseRest(Connection conn,String path,String site) throws IOException{
			SimpleDateFormat sdf = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss",Locale.ENGLISH);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			Session session = conn.openSession();
			//String shell = "awk -F '\"' '{print $1}' "+path+"|awk '{if(NF==6){a[$5]+=$2;b[$5]++;}else if(NF==5){a[$4]+=$2;b[$4]++;}}END{for(i in a){print i ,a[i],b[i]}}'";
			String shell = "awk -F '\"' '{print $1 $3}' "+path+"|awk '{if(NF==8){a[$5]+=$2;b[$5]++;c[$7]++}else if(NF==7){a[$4]+=$2;b[$4]++;c[$6]++}}END{for(i in a){print i ,a[i],b[i]};for(i in c){print i,c[i]}}'";
			
			session.execCommand(shell);
			logger.info(shell);
			InputStream stdout = new StreamGobbler(session.getStdout());			
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			long pv = 0;
			while((line=br.readLine())!=null){
				String[] tokens = StringUtils.split(line, " ");
				if(tokens.length == 3){
					try{
						String date = sdf1.format(sdf.parse(tokens[0]));
						long time = Long.parseLong(tokens[1]);
						long call = Long.parseLong(tokens[2]);
						
						UseInfo info = useMap.get(date);
						if(info == null){
							info = new UseInfo();
							useMap.put(date, info);
						}
						info.setTime(info.getTime()+time);
						info.setCalls(info.getCalls()+call);
						pv+=call;
						allPv+=call;
					}catch (Exception e) {
						logger.info(e);
					}
				}else if(tokens.length == 2){
					String statusCode =tokens[0];// 状态码
					long times = Long.valueOf(tokens[1]);// 此状态码出现的次数
					if(statusMap.get(statusCode) == null) {					
						statusMap.put(statusCode, times);
					} else {
						statusMap.put(statusCode, statusMap.get(statusCode)+times);
					}
					
				}
			}
			
			Long siteNum = sitePvMap.get(site);
			if(siteNum == null){
				sitePvMap.put(site, pv);
			}else{
				sitePvMap.put(site, pv+siteNum);
			}
			
			br.close();
			session.close();
			success = true;
			
			
			
		}
		
	}

}
