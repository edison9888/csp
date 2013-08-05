package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.esotericsoftware.minlog.Log;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * 
 * @author bishan.ct
 *
 */
public class ChongzhideliverAnalyse  extends AbstractDataAnalyse{
	private static final Logger logger = Logger.getLogger(ChongzhideliverAnalyse.class);
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
	
	Map<Long,Map<String,Number[]>> tmpMap = new HashMap<Long, Map<String,Number[]>>();
	Map<Long,Map<String,Number[]>> tmp2Map = new HashMap<Long, Map<String,Number[]>>();
	
	
	public ChongzhideliverAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String line) {
		try {
			String[] tmp =  StringUtils.splitByWholeSeparator(line, " ");
			String time = tmp[0]+" "+tmp[1];
			
			Date collectTime = rTimeFormat.parse(time);
			long cTime = collectTime.getTime();
			
			String content=tmp[4];
			String[] cs=content.split(",");
			if(cs.length!=8){
				return;
			}
			double avgTime=Double.parseDouble(cs[7].split(":")[1]);
			if(avgTime<0){
				avgTime=0;
			}
			Number[] vs={Integer.parseInt(cs[3].split(":")[1]),
					Integer.parseInt(cs[4].split(":")[1]),
					Integer.parseInt(cs[5].split(":")[1]),
					Integer.parseInt(cs[6].split(":")[1]),avgTime};
			
			Map<String,Number[]> mmap=null;
			String firstKey=cs[0];
			
			int index_=cs[0].indexOf("_");
			if(index_>0){
				firstKey=cs[0].substring(0, index_);
				String secondKey=cs[0].substring(0, index_)+"`"+cs[0].substring(
						index_+1, cs[0].length());
				
				mmap=tmp2Map.get(cTime);
				if(mmap==null){
					mmap=new HashMap<String, Number[]>();
					tmp2Map.put(cTime, mmap);
				}
				
				Number[] mm=mmap.get(secondKey);
				if(mm!=null){
					mm[0]=mm[0].intValue()+vs[0].intValue();
					mm[1]=mm[1].intValue()+vs[1].intValue();
					mm[2]=mm[2].intValue()+vs[2].intValue();
					mm[3]=mm[3].intValue()+vs[3].intValue();
					mm[4]=mm[4].doubleValue()+vs[4].doubleValue();
					mm[5]=mm[5].intValue()+1;
				}else{
					mm=new Number[6];
					mm[0]=vs[0];
					mm[1]=vs[1];
					mm[2]=vs[2];
					mm[3]=vs[3];
					mm[4]=vs[4];
					mm[5]=1;
				}
				mmap.put(secondKey, mm);
			}
			
			//只有一级key
			mmap=tmpMap.get(cTime);
			if(mmap==null){
				mmap=new HashMap<String, Number[]>();
				tmpMap.put(cTime, mmap);
			}
			Number[] mm=mmap.get(firstKey);
			
			if(mm!=null){
				mm[0]=mm[0].intValue()+vs[0].intValue();
				mm[1]=mm[1].intValue()+vs[1].intValue();
				mm[2]=mm[2].intValue()+vs[2].intValue();
				mm[3]=mm[3].intValue()+vs[3].intValue();
				mm[4]=mm[4].doubleValue()+vs[4].doubleValue();
				mm[5]=mm[5].intValue()+1;
			}else{
				mm=new Number[6];
				mm[0]=vs[0];
				mm[1]=vs[1];
				mm[2]=vs[2];
				mm[3]=vs[3];
				mm[4]=vs[4];
				mm[5]=1;
			}
			mmap.put(firstKey, mm);
		
		} catch (Exception e) {
			logger.info("parse chongzhi deliver fail",e);
		}
		
	}

	@Override
	public void submit() {
		for (Map.Entry<Long,  Map<String,Number[]>> ventry : tmpMap.entrySet()) {
			long collectTime = ventry.getKey();
			Map<String,Number[]> vout = ventry.getValue();
			try {
				for(Map.Entry<String,Number[]> innerv : vout.entrySet()){
					String key=innerv.getKey();
					Number[] v=innerv.getValue();
					if(v[5].intValue()==0){
						continue;
					}
					CollectDataUtilMulti.collect(getAppName(),"",
							collectTime, new String[]{KeyConstants.CHONGZHIDE,key},
							new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{
								PropConstants.E_TIMES,
								PropConstants.SUCCESS_INVOKE,
								PropConstants.FAILURE_INVOKE,
								PropConstants.TIMEOUT_INVOKE,
								PropConstants.C_TIME}, 
							new Object[]{v[0].intValue(),v[1].intValue(),v[2].intValue(),v[3].intValue(),
								Double.parseDouble(new DecimalFormat("#.##").format(v[4].doubleValue()/v[5].intValue()))},
							new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD,
								ValueOperate.ADD,ValueOperate.ADD,ValueOperate.AVERAGE});
				}
				
			}catch (Exception e) {
				logger.info("chongzhi send fail", e);
			}
		}
		
		for (Map.Entry<Long,  Map<String,Number[]>> ventry : tmp2Map.entrySet()) {
			long collectTime = ventry.getKey();
			Map<String,Number[]> vout = ventry.getValue();
			try {
				for(Map.Entry<String,Number[]> innerv : vout.entrySet()){
					String key=innerv.getKey();
					Number[] v=innerv.getValue();
					String[] keys=key.split("`");
					
					CollectDataUtilMulti.collect(getAppName(),"",
							collectTime, new String[]{KeyConstants.CHONGZHIDE,keys[0],keys[1]},
							new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{
								PropConstants.E_TIMES,
								PropConstants.SUCCESS_INVOKE,
								PropConstants.FAILURE_INVOKE,
								PropConstants.TIMEOUT_INVOKE,
								PropConstants.C_TIME}, 
							new Object[]{v[0].intValue(),v[1].intValue(),v[2].intValue(),v[3].intValue(),
								Double.parseDouble(new DecimalFormat("#.##").format(v[4].doubleValue()/v[5].intValue()))
								},
							new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD,
								ValueOperate.ADD,ValueOperate.ADD,ValueOperate.AVERAGE});
				}
				
			}catch (Exception e) {
				logger.info("chongzhi send fail", e);
			}
		}
	}

	@Override
	public void release() {
		tmpMap.clear();
	}

	
	public static void main(String[] args){
String[] hh={
		"2012-12-10 01:13:59,643 INFO  - notify_consign_AA,,172.23.173.65,totalInvoke:24,successInvoke:1,failureInvoke:23,timeoutInvoke:0,averageTime:-1,",
		"2012-12-21 12:12:14,032 INFO  - TairManager_get,,172.24.151.56,totalInvoke:129,successInvoke:129,failureInvoke:10,timeoutInvoke:0,averageTime:1.565891472868217,",
		"2012-12-21 12:12:14,032 INFO  - http_http://117.34.88.134:9980/VirtualInterface/vMobileChargeFun.aspx,,172.24.151.56,totalInvoke:4,successInvoke:4,failureInvoke:0,timeoutInvoke:0,averageTime:993.5,",
		"2012-12-21 12:15:14,032 INFO  - http_http://www.172.com/mptopup/quicktopup/channelTopupForBigTBPlat.htm,,172.24.151.56,totalInvoke:3,successInvoke:3,failureInvoke:0,timeoutInvoke:0,averageTime:992.3333333333334,",
		"2012-12-21 12:15:14,032 INFO  - recharge_noActiveOrder,,172.24.151.56,totalInvoke:2,successInvoke:2,failureInvoke:0,timeoutInvoke:0,averageTime:-1.0,",
		"2012-12-21 12:15:14,032 INFO  - recharge_invokeCharge|83|江苏天瑞网信息科技有限公司,,172.24.151.56,totalInvoke:2,successInvoke:2,failureInvoke:0,timeoutInvoke:0,averageTime:-1.0,",
		"2012-12-21 13:26:15,249 INFO  - LogisticsConsignService_consign_AA,,172.24.151.54,totalInvoke:42,successInvoke:39,failureInvoke:3,timeoutInvoke:0,averageTime:186.1904761904762,"};	
ChongzhideliverAnalyse job = new ChongzhideliverAnalyse("chongzhi","","");


for(String h:hh){
	job.analyseOneLine(h);
}

job.submit();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream("D:\\tmp\\monitor.log.2012-12-10")));
			int i = 0;
			while((line=reader.readLine())!=null){
				job.analyseOneLine(line);
				i++;
			}
			job.submit();
			job.release();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
