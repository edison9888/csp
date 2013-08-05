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
 * @version 2011-6-23 ����04:15:07
 * @author youji.zj 2011-12-01 �޸ĳ��Զ���ȡ�����ļ������޸�
 */
public class ApacheLoadrunTask extends BaseLoadrunTask {
	
	private static final Logger logger = Logger.getLogger(ApacheLoadrunTask.class);

	public static String[] jkConfigNames = new String[] { "1_4", "2_4", "3_4", "4_4" };

	/*** ������ȴ��೤ʱ��  ��λΪ���� ***/
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
	 * �ֶ�ѹ������
	 * ����doLoadrun �Ĳ���Ϊ����  
	 *  feature[0] ������IP
	 *  feature[1]�����ļ�������
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
		
		// ��һ��ʼ�Ĳɼ�����
		recordData();
	}
	
	/**
	 * �Զ�ѹ������
	 * apache������ѹ�� �����feature ���� ������ʾ �����Ļ������� ��ʽΪ ip1,ip2,ip3
	 * @throws Exception 
	 */
	@Override
	protected void autoControl(String feature) throws Exception {
		String[] configs =jkConfigNames;
		String configFeature = this.getTarget().getConfigFeature();
		if(configFeature != null){
			configs = configFeature.split(",");
		}

		logger.info(" �Զ���ʼ����apache����ѹ��");
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
							logger.info("��ʼ���� ip:"+ip+" apache "+jk+"����!");	
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
								logger.error("���� ip:"+ip+" apache "+jk+"����!",e);
							}
							
							// ������ip��һ�η���ѹ��
							endLoad();
							logger.info("�������� ip:"+ip+" apache "+jk+"����!");
						}
					}
				}
			}
		} else {
			throw new Exception("apache �ķ�������ip�б�Ϊ�� ...");
		}
	}
	
	public void stopTask() {
		
		synchronized (lock) {
			lock.notifyAll();
		}
		
		super.stopTask();
		recoverApacheConfig();
		logger.info("�ر� ApacheLoadrunTask !");
		
	}
	
	private void doload(String ip, String configFeature) throws Exception{
		this.getTarget().setCurControlFeature(ip+":"+configFeature);
				
		IpApache ipApache = new IpApache();
		ipApache.setIp(ip);
		ipApache.setConfigName(configFeature);
		ipApache.setControl(currentControl);
		
		String [] ratios = configFeature.split("_");
		if (ratios.length < 2) {
			logger.error(ip + "�����������ô���");
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
			logger.info("��ʼ�ָ� apache Ĭ������! ����:"+apacheControlMap.size());
			for (Map.Entry<String, IpApache> entry : apacheControlMap.entrySet()) {
				try {
					IControl control = entry.getValue().getControl();
					boolean success = control.reset();
					
					logger.info("recoverApacheConfig ip:"+entry.getKey()+" apache Ĭ������!" + success);
				} catch (Exception e) {
					loadrunListen.error(this.getLoadrunId(),this.getTarget(), e);
					logger.error("recoverApacheConfig ip:"+entry.getKey()+" apache Ĭ������!",e);
				}
			}
		}
	}
}
