package com.taobao.csp.loadrun.core.run;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.control.ControlAtrribute;
import com.taobao.csp.loadrun.core.control.ControlFactory;
import com.taobao.csp.loadrun.core.control.IControl;

/**
 * 
 * @author xiaodu
 * @version 2011-6-23 下午04:15:07
 * @author youji.zj 2011-12-01 修改成自动读取配置文件进行修改
 */
public class ApacheLoadrunTask extends BaseLoadrunTask {
	
	private static final Logger logger = Logger.getLogger(ApacheLoadrunTask.class);

	public static String[] jkConfigNames = new String[] { "1_4", "2_4", "3_4", "4_4" };

	/*** 分流后等待多长时间  单位为分钟 ***/
	private long waitForLoadTime = 2;

	private Map<String, IpApache> apacheControlMap = new ConcurrentHashMap<String, IpApache>();
	
	private IControl currentControl;
	
	private Object lock = new Object();
	
	private Object recoverLock = new Object();

	public ApacheLoadrunTask(LoadrunTarget target) throws Exception {
		super(target);
	}
	
	public Map<String, IpApache> getApacheControlMap() {
		return apacheControlMap;
	}
	
	/**
	 * 手动压测的入口
	 * 传入doLoadrun 的参数为两个  
	 *  feature[0] 分流的IP
	 *  feature[1]配置文件的名称
	 */
	public void doLoadrun(String ... feature) throws Exception {
		String ip = feature[0];
		String configFeature = feature[1];
		
		try {
			currentControl =  apacheControlMap.get(ip) == null ? null : apacheControlMap.get(ip).getControl();
			if (currentControl == null) {
				currentControl = ControlFactory.getApacheSplitControl(this.getTarget(), ip); 
			}
		} catch(Exception e) {
			this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
			return;
		} 
		
		backupApacheConfig();
		doload(ip, configFeature);
		
		// 存一开始的采集数据
		recordData();
	}
	
	/**
	 * 自动压测的入口
	 * apache分流的压测 传入的feature 数据 用来表示 分流的机器数量 格式为 ip1,ip2,ip3
	 * @throws Exception 
	 */
	@Override
	protected void autoControl(String feature) throws Exception {
		String[] configs =jkConfigNames;
		String configFeature = this.getTarget().getConfigFeature();
		if(configFeature != null){
			configs = configFeature.split(",");
		}

		logger.info(" 自动开始控制apache分流压测");
		if (feature != null) {
			String[] ips = feature.split(",");
			for (String ip : ips) {
				Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
				Matcher m = pattern.matcher(ip);
				if (m.find() && isTaskRun()) {
					try {
						currentControl =  apacheControlMap.get(ip) == null ? null : apacheControlMap.get(ip).getControl();
						if (currentControl == null) {
							currentControl = ControlFactory.getApacheSplitControl(this.getTarget(), ip); 
						}
					} catch(Exception e) {
						this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
						continue;
					}

					backupApacheConfig();
					
					for (String jk : configs) {
						if (isTaskRun()) {
							logger.info("开始分流 ip:"+ip+" apache "+jk+"配置!");	
							startLoad(); 
							try {
								doload(ip, jk);
								if(isTaskRun()){
									synchronized (lock) {
										try{
											lock.wait(waitForLoadTime*60*1000);
										}catch (InterruptedException e) {
											logger.error(e);
										}
									}
								}
								recordData();
							}catch (Exception e) {
								this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
								logger.error("分流 ip:"+ip+" apache "+jk+"配置!",e);
							}
							
							// 结束该ip的一次分流压测
							endLoad();
							logger.info("结束分流 ip:"+ip+" apache "+jk+"配置!");
						}
					}
				}
			}
		} else {
			throw new Exception("apache 的分流配置ip列表为空 ...");
		}
	}
	
	public void stopTask() {
		
		synchronized (lock) {
			lock.notifyAll();
		}
		
		super.stopTask();
		recoverApacheConfig();
		logger.info("关闭 ApacheLoadrunTask !");
		
	}
	
	private void doload(String ip, String configFeature) throws Exception{
		this.getTarget().setCurControlFeature(ip+":"+configFeature);
				
		IpApache ipApache = new IpApache();
		ipApache.setIp(ip);
		ipApache.setConfigName(configFeature);
		ipApache.setControl(currentControl);
		
		String [] ratios = configFeature.split("_");
		if (ratios.length < 2) {
			logger.error(ip + "分流比例设置错误");
		}
		
		currentControl.putAttribute(ControlAtrribute.RATIO_LOCAL, ratios[0]);
		currentControl.putAttribute(ControlAtrribute.RATIO_TARGET, ratios[1]);
		
		currentControl.control();
		apacheControlMap.put(ip, ipApache);
	}
	
	private void backupApacheConfig() throws Exception {
		boolean success = currentControl.backup();
		logger.info("backup config " + success);
	}
	
	private void recoverApacheConfig() {
		synchronized (recoverLock) {
			logger.info("开始恢复 apache 默认配置! 数量:"+apacheControlMap.size());
			for (Map.Entry<String, IpApache> entry : apacheControlMap.entrySet()) {
				try {
					IControl control = entry.getValue().getControl();
					boolean success = control.reset();
					
					logger.info("recoverApacheConfig ip:"+entry.getKey()+" apache 默认配置!" + success);
				} catch (Exception e) {
					loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
					logger.error("recoverApacheConfig ip:"+entry.getKey()+" apache 默认配置!",e);
				}
			}
		}
	}
}
