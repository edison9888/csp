
package com.taobao.csp.loadrun.core.run;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.config.sdk.ConfigAdvancedSDK;
import com.taobao.config.server.common.dataobject.Result;
import com.taobao.config.server.common.rule.WeightingRule;
import com.taobao.config.server.common.util.CSPattern;
import com.taobao.csp.loadrun.core.LoadrunTarget;

/**
 * 
 * @author xiaodu
 * @version 2011-6-23 下午04:15:07
 */
public class HsfLoadrunTask extends BaseLoadrunTask{
	
	private static final Logger logger = Logger.getLogger(HsfLoadrunTask.class);
	
	
	private ConfigAdvancedSDK sdk = new ConfigAdvancedSDK("commonconfig.config-host.taobao.com");
	//private ConfigAdvancedSDK sdk = new ConfigAdvancedSDK("10.232.16.8");
	private WeightingRule rule = null;
	
	private String[] interfaces = null;
	
	private long waitForLoadTime = 2;//分流后等待多长时间  单位为分钟
	
	private Object lock = new Object();
	
	private int curCopyNum = 1;
	
	
	
	

	public int getCurCopyNum() {
		return curCopyNum;
	}


	public HsfLoadrunTask(LoadrunTarget target) throws Exception {
		super(target);
		interfaces = target.getConfigFeature().split(",");
	}
		
	
	public void stopTask() {
		synchronized (lock) {
			lock.notifyAll();
		}
		
		
		super.stopTask();
		recoverHsfConfig();
		
	}
	
	
	
	private void recoverHsfConfig(){
		try{
			if(rule != null){
				Map<String,Result> resultMap = sdk.deleteWeightingRuleById(rule.getId());//先不管返回结果
			}			
		}catch (Exception e) {
		}
	}
	
	/**
	 * 1,2,3,4,5,6 使用短号相隔 后面表示需要copy的倍数
	 * @throws Exception 
	 */
	protected void autoControl(String feature) throws Exception {
		if(feature != null){
			String[] nums = feature.split(",");
			for(String num:nums){
				if(isTaskRun()){
					startLoad();
					try {
						doload(Integer.parseInt(num));
						
						logger.info("hsf 等待 runload");
						if(isTaskRun()){
							synchronized (lock) {
								try{
									for (int i = 0; i < 60; i++) {
										if (!this.isTaskRun()) break;
										lock.wait(waitForLoadTime*1000);
									}
								}catch (InterruptedException e) {
								}
							}
						}
						logger.info("hsf 等待 runload 结束");
						
						recordData();
					} catch (Exception e) {
						this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
					}
					endLoad();
				}
			}
		}else{
			throw new Exception("hsf 传入的feature 数据 用来表示压测倍数 为空...");
		}
		
	}
	
	
	
	private void doload(int copyNum) throws Exception{
		
		logger.info("hsf 拷贝"+this.getTarget().getTargetIp()+" 数量"+copyNum);
		
		curCopyNum = copyNum;
		
		this.getTarget().setCurControlFeature(copyNum+"");
		
		if(rule != null){
			Map<String,Result> resultMap = sdk.deleteWeightingRuleById(rule.getId());//先不管返回结果
		}
		
		// 等10秒钟再发新规则,configserver有问题
		TimeUnit.SECONDS.sleep(10);
		
		rule = newWeightingRule(this.getTarget().getAppId()+"_CSP_Loadrun_"+this.getTarget().getTargetIp()+"_"+copyNum,this.getTarget().getTargetIp(),interfaces,copyNum);
		if(rule == null){
			throw new Exception(" WeightingRule 创建失败");
		}
		
		Map<String,Result> resultMap = sdk.addWeightingRule(rule);
		
		logger.info("发送新的规则到configserver "+copyNum);
		
	}

	public void doLoadrun(String ... feature) throws Exception {
		
		String copyNum =feature[0];
		doload(Integer.parseInt(copyNum));
		
		recordData();
	}
	
	
	private WeightingRule newWeightingRule(String ruleName, String ip, String[] services, int copyCount) {
		 
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar cal = Calendar.getInstance();
		 cal.add(Calendar.DAY_OF_MONTH, 1);
		 
	      List<String> dataIdInclude = Arrays.asList(services);
	      List<String> emptyList = Collections.emptyList();
	      CSPattern dataIcPattern = new CSPattern(dataIdInclude, emptyList);
	      CSPattern ipPattern = new CSPattern(Arrays.asList(ip), emptyList);
	      CSPattern subPattern = new CSPattern(Arrays.asList("*"), emptyList);
	      Map<CSPattern, Integer> subMap = new HashMap<CSPattern, Integer>();
	      subMap.put(subPattern, copyCount);
	      try {
	         return new WeightingRule(ruleName, sdf.format(cal.getTime()), ipPattern, dataIcPattern, subMap);
	      } catch (ParseException e) {
	      }
	      return null;
	   }
	

}
