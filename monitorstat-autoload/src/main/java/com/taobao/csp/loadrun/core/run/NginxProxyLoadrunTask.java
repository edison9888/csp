package com.taobao.csp.loadrun.core.run;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.control.ControlFactory;
import com.taobao.csp.loadrun.core.control.IControl;

public class NginxProxyLoadrunTask extends BaseLoadrunTask {
private static final Logger logger = Logger.getLogger(ApacheProxyLoadrunTask.class);
	
	/*** 收集多长时间的日志  ***/
	private long waitForLoadTime = 2;

	/*** 参与代理的机器 ***/
	private Map<String, IControl> controls = new ConcurrentHashMap<String, IControl>();
	
	private IControl currentControl;

	private Object lock = new Object();
	
	private Object recoverLock = new Object();

	public NginxProxyLoadrunTask(LoadrunTarget target) throws Exception {
		super(target);
	}
	
	
	public Map<String, IControl> getControls() {
		return controls;
	}

	/***
	 * 手动压测的入口
	 * feature[0]为压测代理机器的ip
	 * 这个方法只在手动压测的时候调用
	 */
	public void doLoadrun(String... feature) throws Exception {
		String ip = feature[0];
		
		try {
			currentControl =  controls.get(ip);
			if (currentControl == null) {
				currentControl = ControlFactory.getNginxControl(this.getTarget(), ip); 
			}
					
		} catch (Exception e) {
			this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
			return;
		}
		
		backupNginxConfig();

		doload(ip);
		
		// 存一开始的采集数据
		recordData();
	}

	/***
	 * feature 为代理机器的序列:ip1,ip2...
	 * 自动压测的入口
	 */
	@Override
	protected void autoControl(String feature) throws Exception {	
		logger.info("自动开始nginx代理压测");
		
		if (feature != null) {
			String[] ips = feature.split(",");
			for (String ip : ips) {
				Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
				Matcher m = pattern.matcher(ip);
				if (m.find()) {
					if (isTaskRun()) {
						logger.info("开始nginx代理 ip:"+ip+" ");
						startLoad();  
						try {
							currentControl =  controls.get(ip);
							if (currentControl == null) {
								currentControl = ControlFactory.getNginxControl(this.getTarget(), ip); 
							} 
						} catch (Exception e) {
							this.loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
						}
						
						try {
							if(isTaskRun()) {
								backupNginxConfig();
								doload(ip);
							}
							
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
		}else {
			throw new Exception("apache的代理配置ip列表为空 ...");
		}
	}
	
	public void stopTask() {
		
		synchronized (lock) {
			lock.notifyAll();
		}
		
		super.stopTask();
		recoverNginxConfig();
		logger.info("关闭 NginxLoadrunTask !");
	}
	
	private void doload(String ip) throws Exception{
		// 显示在压测结果界面
		this.getTarget().setCurControlFeature(ip);		
		
		IpApache ipApache = new IpApache();
		ipApache.setIp(ip);
		ipApache.setConfigName(null);
		
		currentControl.control();
		controls.put(ip, currentControl);
	}
	
	private void backupNginxConfig() throws Exception {
		boolean success = currentControl.backup();
		logger.info("backup config " + success);
	}
	
	private  void recoverNginxConfig() {
		synchronized (recoverLock) {
			logger.info("开始恢复 nginx 默认配置! 数量:" + controls.size());
			String ip = null;
			for (Map.Entry<String, IControl> entry : controls.entrySet()) {
				try {
					ip = entry.getKey();
					IControl control = entry.getValue();
					boolean seccess = control.reset();
					logger.info("recoverApacheConfig ip:" + ip + " apache 默认配置!" + seccess);
				} catch (Exception e) {
					loadrunListen.error(this.getLoadrunId(), this.getTarget(), e);
					logger.error("recoverApacheConfig ip:" + ip + " apache 默认配置!", e);
				}
			}
		}
	}
}
