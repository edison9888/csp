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
	
	/*** �ռ��೤ʱ�����־  ***/
	private long waitForLoadTime = 2;

	/*** �������Ļ��� ***/
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
	 * �ֶ�ѹ������
	 * feature[0]Ϊѹ����������ip
	 * �������ֻ���ֶ�ѹ���ʱ�����
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
		
		// ��һ��ʼ�Ĳɼ�����
		recordData();
	}

	/***
	 * feature Ϊ�������������:ip1,ip2...
	 * �Զ�ѹ������
	 */
	@Override
	protected void autoControl(String feature) throws Exception {	
		logger.info("�Զ���ʼnginx����ѹ��");
		
		if (feature != null) {
			String[] ips = feature.split(",");
			for (String ip : ips) {
				Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
				Matcher m = pattern.matcher(ip);
				if (m.find()) {
					if (isTaskRun()) {
						logger.info("��ʼnginx���� ip:"+ip+" ");
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
							logger.error("���� ip:" + ip, e);
						}

						endLoad();
						logger.info("�������� ip:" + ip);
					}
					
				}
			}
		}else {
			throw new Exception("apache�Ĵ�������ip�б�Ϊ�� ...");
		}
	}
	
	public void stopTask() {
		
		synchronized (lock) {
			lock.notifyAll();
		}
		
		super.stopTask();
		recoverNginxConfig();
		logger.info("�ر� NginxLoadrunTask !");
	}
	
	private void doload(String ip) throws Exception{
		// ��ʾ��ѹ��������
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
			logger.info("��ʼ�ָ� nginx Ĭ������! ����:" + controls.size());
			String ip = null;
			for (Map.Entry<String, IControl> entry : controls.entrySet()) {
				try {
					ip = entry.getKey();
					IControl control = entry.getValue();
					boolean seccess = control.reset();
					logger.info("recoverApacheConfig ip:" + ip + " apache Ĭ������!" + seccess);
				} catch (Exception e) {
					loadrunListen.error(this.getLoadrunId(), this.getTarget(), e);
					logger.error("recoverApacheConfig ip:" + ip + " apache Ĭ������!", e);
				}
			}
		}
	}
}
