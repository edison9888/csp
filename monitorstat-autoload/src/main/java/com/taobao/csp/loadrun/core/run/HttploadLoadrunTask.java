package com.taobao.csp.loadrun.core.run;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.UtilShell;

/**
 * 
 * @author xiaodu
 * @version 2011-6-23 下午04:15:07
 */
public class HttploadLoadrunTask extends BaseLoadrunTask{
	
	private static final Logger logger = Logger.getLogger(HttploadLoadrunTask.class);
	
	
	private IUrlCollector getHttpFile = null;
	
	private String apacheFilePath = null;
	
	private String curParallel;

	public String getCurParallel() {
		return curParallel;
	}

	public HttploadLoadrunTask(LoadrunTarget target) throws Exception {
		super(target);
		if (target.getMode() == AutoLoadMode.SSH) {
			getHttpFile = new GetHttpLoadUrlFile_n(target);
		} else {
			getHttpFile = new HttpLoadUrlScriptCollecter(target);
		}

		apacheFilePath = getHttpFile.getFilePath();
	}

	@Override
	public void stopTask() {
		super.stopTask();
	}

	/**
	 * httpload 传入的feature 数据 用来表示 需要压测的用户数量
	 * 格式为 p:5,10,15,20,25,30;
	 * @throws Exception 
	 */
	protected void autoControl(String feature) throws Exception {
		
		//Pattern pattern = Pattern.compile("p:(\\d+,?);");
		
		if(feature != null){
			//Matcher m = pattern.matcher(feature);
			if(true){
				//String f = m.group(1);
				String[] parallels = feature.split(",");
				for(String parallel:parallels){
					if(isTaskRun()){
						doLoadrun(parallel);
					}
				}
			}
		}else{
			throw new Exception("httpload 传入的feature 数据 用来表示 需要压测的用户数量 为空...");
		}
	}
	
	
	public void doLoadrun(String ... feature){
		curParallel = feature[0];
		
		this.getTarget().setCurControlFeature(curParallel);
		
		int time = 60;
		try{
			time = Integer.parseInt(this.getTarget().getConfigFeature());
			if(time <20){
				time =10;
			}
		}catch (Exception e) {
		}
		logger.info("开始httpload压测  用户量:"+feature[0]);
		startLoad();
		try{
			int n = Integer.parseInt(feature[0]);
			
			// 原始url压jboss
			String loadRunShell = this.getTarget().getHttploadpath()+" -parallel " + n + " -seconds " + time+" -proxy "
			+ this.getTarget().getTargetIp() + ":7001 "+this.apacheFilePath;
			
			// 修改url为ip:7001压jboss（不用代理），只有这种方式会改url
			if (this.getTarget().getHttploadProxy().equals("no")) {
				loadRunShell = this.getTarget().getHttploadpath()+" -parallel " + n + " -seconds " + time +" "+ this.apacheFilePath;
			}
			
			// 代理模式压apache或者nginx
			if (this.getTarget().getHttploadProxy().equals("directProxy")) {
				loadRunShell = this.getTarget().getHttploadpath()+" -parallel " + n + " -seconds " + time + "  -proxy "
				+ this.getTarget().getTargetIp()+":80 " + this.apacheFilePath;
			}
			
		    // ssl方式（代理）压apache或者nginx
			if (this.getTarget().getHttploadProxy().equals("ssl")) {
				loadRunShell = this.getTarget().getHttploadpath()+" -parallel " + n + " -seconds " + time + " -cipher fastsec" + " -proxy "
				+ this.getTarget().getTargetIp()+":443 " + this.apacheFilePath;
			}
			
			String result = UtilShell.comandReturnResult(loadRunShell);
			if(result !=null){
				double qps = analyseAverageQps(result);
				double rest = analyseAverageResT(result);
				double pagesize = analyseAveragePageSize(result);
				double fetches =analyseFetches(result);
				Map<String,Double> map = analyseResponseCode(result);
				for(Map.Entry<String,Double> entry:map.entrySet()){
					if("200".equals(entry.getKey()))
						this.getLoadResult().put(ResultKey.Http_State_200, entry.getValue());
					if("302".equals(entry.getKey()))
						this.getLoadResult().put(ResultKey.Http_State_302, entry.getValue());
				}
				this.getLoadResult().put(ResultKey.Http_Fetches, fetches);
				this.getLoadResult().put(ResultKey.Qps, qps);
				this.getLoadResult().put(ResultKey.Rest, rest);
				this.getLoadResult().put(ResultKey.PageSize, pagesize);
			}
			logger.info("开始httpload压测 执行"+loadRunShell);
			recordData();
		}catch (Exception e) {
			this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
		}
		endLoad();
		logger.info("结束httpload压测 用户量:"+feature[0]);
		
	}
	
	private double analyseAverageQps(String str) {
		try{
		Pattern pattern = Pattern.compile("\\s*([\\w\\.]+)\\s*fetches/sec,");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			String num = matcher.group(1);
			return Double.parseDouble(num);
		}
		}catch (Exception e) {
		}
		return -1;
	}

	private double analyseAverageResT(String str) {

		Pattern pattern = Pattern.compile("\\s*msecs/first-response:\\s*([\\w\\.]+)\\s*mean,");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			String num = matcher.group(1);
			return Double.parseDouble(num);
		}

		return -1;
	}

	private double analyseAveragePageSize(String str) {

		Pattern pattern = Pattern.compile("([\\w\\.]+)\\s*mean bytes/connection");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			String num = matcher.group(1);
			return Arith.div(Double.parseDouble(num), 1024, 2);
		}

		return -1;
	}
	
	private Map<String,Double> analyseResponseCode(String str){
		
		Map<String,Double> map = new HashMap<String, Double>();
		try{
			Pattern pattern = Pattern.compile("code\\s*(\\w{3})\\s*--\\s*([\\w]*)");
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				map.put(matcher.group(1), Double.valueOf(matcher.group(2)));
			}
		}catch (Exception e) {
		}
		return map;
	}
	
	private double analyseFetches(String str) {

		int idx = str.indexOf("fetches");
		
		String num = str.substring(0, idx);
		try{
			return Double.valueOf(num.trim());
		}catch (Exception e) {
		}
		return -1;
	}
	
//	67766 fetches, 5 max parallel, 8.59491e+08 bytes, in 60 seconds
//	12683.2 mean bytes/connection
//	1129.43 fetches/sec, 1.43248e+07 bytes/sec
//	msecs/connect: 0.485051 mean, 11.599 max, 0.337 min
//	msecs/first-response: 3.07317 mean, 530.93 max, 0.706 min
//	2496 bad byte counts
//	HTTP response codes:
//	  code 200 -- 58211
//	  code 302 -- 9555
	
	
	public static void main(String[] args){
		Pattern pattern = Pattern.compile("code\\s*(\\w{3})\\s*--\\s*([\\w]*)");
		String str = ("	67766 fetches, 5 max parallel, 8.59491e+08 bytes, in 60 seconds//	12683.2 mean bytes/connection//	1129.43 fetches/sec, 1.43248e+07 bytes/sec//	msecs/connect: 0.485051 mean, 11.599 max, 0.337 min//	msecs/first-response: 3.07317 mean, 530.93 max, 0.706 min//	2496 bad byte counts//	HTTP response codes://	  code 200 -- 58211//	  code 302 -- 9555");
		int idx = str.indexOf("fetches");
		
		String num = str.substring(0, idx);
		System.out.println(num);
	}

}
