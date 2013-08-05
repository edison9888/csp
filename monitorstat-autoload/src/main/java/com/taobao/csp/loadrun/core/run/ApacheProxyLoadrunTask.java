package com.taobao.csp.loadrun.core.run;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.control.ControlAtrribute;
import com.taobao.csp.loadrun.core.control.ControlFactory;
import com.taobao.csp.loadrun.core.control.IControl;

public class ApacheProxyLoadrunTask extends BaseLoadrunTask {
private static final Logger logger = Logger.getLogger(ApacheProxyLoadrunTask.class);
	
	/*** 收集多长时间的日志  ***/
	private long waitForLoadTime = 2;

	/*** 参与代理的机器 ***/
	private Map<String, IControl> controls = new ConcurrentHashMap<String, IControl>();
	
	private IControl currentControl;

	private Object lock = new Object();
	
	private Object recoverLock = new Object();
	
	public ApacheProxyLoadrunTask(LoadrunTarget target) throws Exception {
		super(target);
	}

	public Map<String, IControl> getControls() {
		return controls;
	}

	/***
	 * feature[0]为压测代理机器的ip
	 * 这个方法只在手动压测的时候调用
	 */
	public void doLoadrun(String... feature) throws Exception {
		String ip = feature[0];
		String configFeature = feature[1];
		try {
			currentControl =  controls.get(ip);
			if (currentControl == null) {
				currentControl = ControlFactory.getApacheProxyControl(this.getTarget(), ip); 
			}
		} catch (Exception e) {
			this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
			return;
		}
		
		backupApacheConfig();
		doload(ip, configFeature);
		
		// 存一开始的数据
		recordData();
	}

	public void stopTask() {
		
		synchronized (lock) {
			lock.notifyAll();
		}
		
		super.stopTask();
		recoverApacheConfig();
		logger.info("关闭 ApacheLoadrunTask !");
	}

	/***
	 * feature 为代理机器的序列:ip1,ip2...
	 * 自动压测的入口
	 */
	@Override
	protected void autoControl(String feature) throws Exception {	
		List<String> configs = new ArrayList<String>();
		String configFeature = this.getTarget().getConfigFeature();
		if(configFeature != null){
			String [] features = configFeature.split(",");
			for (String config : features) {
				boolean passCheck = true;
				String [] ratios = config.split("_");
				if (ratios.length != 2) continue;
				for (String ratio : ratios) {
					if (ratio.split(":").length != 2) {
						passCheck = false;
						break;
					}
				}
				if (passCheck) {
					configs.add(config.trim());
				}
			}
		}
		if (configs.size() == 0) configs.add(null); // ssh方式
		
		logger.info("自动开始apache代理压测");

		if (feature != null) {
			String[] ips = feature.split(",");
			for (String ip : ips) {
				Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
				Matcher m = pattern.matcher(ip);
				if (m.find()) {
					if (isTaskRun()) {
						logger.info("开始apache代理 ip:"+ip+" ");
						
						try {
							currentControl =  controls.get(ip);
							if (currentControl == null) {
								currentControl = ControlFactory.getApacheProxyControl(this.getTarget(), ip); 
							}
						} catch (Exception e) {
							this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
							return;
						} 

						backupApacheConfig();
					
						for (String config : configs) {
							if (isTaskRun()) {
								startLoad();
								try {
									doload(ip, config);
									if(isTaskRun()){
										synchronized (lock) {
											try{
												lock.wait(waitForLoadTime*60*1000);
											}catch (InterruptedException e) {
											}
										}
									}
									
									recordData();
								} catch (Exception e) {
									this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
									logger.error("代理 ip:" + ip, e);
								}

								endLoad();
								logger.info("结束代理 ip:" + ip);
							}
						}
					}
					
				}
			}
		} else {
			throw new Exception("apache的代理配置ip列表为空 ...");
		}
	}
	
	private void doload(String ip, String config) throws Exception{
		// 显示在压测结果界面
		this.getTarget().setCurControlFeature(ip + " " + config);	
		
		IpApache ipApache = new IpApache();
		ipApache.setIp(ip);
		ipApache.setConfigName(null);

		// config 为null为ssh方式
		if (config != null) {
			String [] ratios = config.split("_");
			if (ratios.length < 2) {
				logger.error(ip + "分流比例设置错误");
				return;
			} else {
				currentControl.putAttribute(ControlAtrribute.PROXY_LOCAL, ratios[0]);
				currentControl.putAttribute(ControlAtrribute.PROXY_TARGET, ratios[1]);
			}
		}
		
		currentControl.control();
		controls.put(ip, currentControl);
	}
	
	private void backupApacheConfig() throws Exception {
		boolean success = currentControl.backup();
		logger.info("backup config " + success);
	}
	
	private void recoverApacheConfig() {
		synchronized (recoverLock) {
			logger.info("开始恢复 apache 默认配置! 数量:" + controls.size());
			String ip = null;
			for (Map.Entry<String, IControl> entry : controls.entrySet()) {
				try {
					ip = entry.getKey();
					IControl control = entry.getValue();
					boolean success = control.reset();
					logger.info("recoverApacheConfig ip:" + ip + " apache 默认配置!" + success);
				} catch (Exception e) {
					loadrunListen.error(this.getLoadrunId(), this.getTarget(), e);
					logger.error("recoverApacheConfig ip:" + ip + " apache 默认配置!", e);
				}
			}
		}
	}
}
