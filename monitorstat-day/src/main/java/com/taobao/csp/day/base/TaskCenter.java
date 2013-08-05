package com.taobao.csp.day.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.csp.day.config.AppLogPathConfig;
import com.taobao.csp.day.config.DayConfig;
import com.taobao.csp.day.util.DayUtil;
import com.taobao.csp.day.util.PinkieUtil;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

/***
 * �����ɼ����������ģ����ڱ�����־����־�Ĺ����߼�
 * 1������ļ���
 * 2������ķ���
 * @author youji.zj
 * @version 1.0
 */
public abstract class TaskCenter {
	
	public static Logger logger = Logger.getLogger(TaskCenter.class);
	
	private List<AbstractLogTask> tasks = new CopyOnWriteArrayList<AbstractLogTask>();
	
	private List<CustomizationFixedTheadPool> pools = new CopyOnWriteArrayList<CustomizationFixedTheadPool>(); 
	
	private Object taskLock = new Object();
	
	private volatile boolean hangup;
	
	private volatile boolean stop;
	
	protected TaskCenter() {
		logger.info("TaskCenter constructor!!!");
		init();
	}

	public boolean isHangup() {
		return hangup;
	}

	public void setHangup(boolean hangup) {
		this.hangup = hangup;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	/***
	 * ע���̳߳�
	 * @param pool
	 */
	public void registThreadPool(CustomizationFixedTheadPool pool){
		pools.add(pool);
	}
	
	/***
	 * ����taskǰ�ļ���
	 * @return
	 */
	protected boolean passVerify() {
		boolean appInterfaceAvailable = DayUtil.checkGetAppInterfaceAvailable();
		return appInterfaceAvailable;
	}
	
	protected abstract List<AppInfoPo> getApps();
	
	protected abstract int taskWaitTime();
	
	protected abstract DataType getDataType();
	
	protected abstract AbstractLogTask createTask(AbstractAnalyser analyser, String appName, 
			HostInfo hostInfo, DataType dataType, String realLogPath, long fetchSize);
	
	private List<AbstractLogTask> getTasks() {
		List<AbstractLogTask> taskL;
		synchronized (taskLock) {
			taskL = tasks;
		}
		return taskL;
	}

	private void init() {
		logger.info("init task center...");
		assignTasks();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		// 3��Сʱͬ��һ������
		scheduler.scheduleWithFixedDelay(new SyncWorker(), 30 , 60 * 60 * 12, TimeUnit.SECONDS);
	}
	
	private void loadTasks() {
		try {
			logger.info("load tasks....");
			if (!passVerify()) {
				logger.warn("loadTasks fail verify....");
				return;
			}
			logger.info("pass verify and load tasks....");
			
			List<AppInfoPo> list = getApps();
			logger.info("app num is:" + list.size());
			
			// ״̬����Ϊ����,worker�߳̽���wait�ͷ������õ�����ʼͬ��
			this.setHangup(true); 
			synchronized (taskLock) {
				// ��վɵ�task
				Map<String, AbstractLogTask> oldTaskM = new HashMap<String, AbstractLogTask>();

				for (AbstractLogTask task : tasks) {
					oldTaskM.put(task.identityKey(), task);
				}
				tasks.clear();
				
				// Ӧ�á���������־
				for (AppInfoPo app : list) {
					String opsName = app.getOpsName();
					
					// ������
//					if (!opsName.equals("bp_sem") && !opsName.equals("bpbsev_kgb")) {
//						continue;
//					}
					List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(opsName);
					if (hosts == null) {
						logger.warn(opsName + " host info is null");
						continue;
					}

					logger.info(opsName + " hosts num is:" + hosts.size());
					for (HostPo host : hosts) {
						HostInfo hostInfo = PinkieUtil.getHostInfo(host);
						AbstractAnalyser analyser = null; // �ȵ�ȷ�������Ѿ����ڵ�task���ٳ�ʼ��
						DataType dataType = this.getDataType();
						String logPath = AppLogPathConfig.getLogPath(opsName, dataType);
						long fetchSize = DayConfig.getFetchSize(dataType);
						
						AbstractLogTask task = createTask(analyser, opsName, hostInfo, dataType, logPath, fetchSize);
						if (!checkTask(dataType, task)) continue;   // taskcenter��������ִ�и�task���̳߳�

						// ���ǵ�һ�μ��أ����task�Ѿ����ڣ����Ѿ����ڵ�task
						if (oldTaskM.containsKey(task.identityKey())) {
							AbstractLogTask oldTask = oldTaskM.get(task.identityKey());
							task = oldTask;
						} else {
							task.setAnalyser(AnalyserFactory.getInstance(opsName, hostInfo, dataType));
						}

						task.setValiable(true);  // ������task��ɿ��ñ�־
						List<AbstractLogTask> taskL = this.getTasks();
						logger.info("add task: " + task.toString());
						taskL.add(task);
					}
				}

				oldTaskM.clear();
				this.setHangup(false);
				logger.info(this.getClass().getName() + " load task num is: " + this.getTasks().size());
				taskLock.notifyAll();
			}

			
		} catch (Exception e) {
			logger.error(e);
		}
		
		logger.info("finished tasks....");
	}
	
	/***
	 * ר�����ڲ��Եķ���
	 */
//	private void loadTasks() {
//		logger.info("load tasks");
//		Map<String, Set<String>> appHostM = DayUtil.getTddlSimulate();
//		this.setHangup(true); // ״̬����Ϊ����
//		
//		synchronized (taskLock) {
//			// ��վɵ�task
//			for (List<LogDataFetcherTask> taskL : tasks.values()) {
//				taskL.clear();
//			}
//			
//			// Ӧ�á���������־
//			for (Map.Entry<String, Set<String>> entry : appHostM.entrySet()) {
//				String opsName = entry.getKey();
//				Set<String> hostS = entry.getValue();
//				for (String host : hostS) {
//					for (DataType dataType : DataType.values()) {
//						AbstractAnalyser analyser = AnalyserFactory.getInstance(opsName, dataType);
//						String logPath = AppLogPathConfig.getLogPath(opsName, dataType);
//						long fetchSize = DayConfig.getFetchSize(dataType);
//						LogDataFetcherTask task = new LogDataFetcherTask(analyser, opsName, host, dataType, logPath, fetchSize);
//
//						List<LogDataFetcherTask> taskL = this.getTasks(dataType);
//						taskL.add(task);
//					}
//				}
//			}
//			
//			for (DataType dataType : DataType.values()) {
//				logger.info(dataType + " task size is " + this.getTasks(dataType).size());
//			}
//			
//			this.setHangup(false);
//			taskLock.notifyAll();
//		}
//	}
	
	private void assignTasks() {
		try {
			Thread workerThead = new Thread() {
				@Override
				public void run() {
					synchronized (taskLock) {
						while (!isStop()) {
							logger.info("assign task run ...");

							// ״̬Ϊ����ʱ���ͷ������ȴ�ͬ����ɵ��źŷ���
							if (isHangup()) {
								try {
									taskLock.wait();
								} catch (InterruptedException e) {
									logger.warn(e);
								}
							}
							
							long maxMemory = Runtime.getRuntime().maxMemory();
							long totalMemory = Runtime.getRuntime().totalMemory();
							long freeMemory = Runtime.getRuntime().freeMemory();
							logger.info("maxMemory:" + maxMemory + " " + "totalMemory:" + totalMemory + " " + "freeMemory:" + freeMemory);
							logger.info("task size is:" + getTasks().size());
							
							for (AbstractLogTask task : getTasks()) {
								for (CustomizationFixedTheadPool pool : pools) {
									pool.submit(task);
								}
							}

							try {
								TimeUnit.SECONDS.sleep(taskWaitTime());
							} catch (InterruptedException e) {
								logger.warn(e);
							}
						}
					}
				}
			};

			workerThead.start();
			logger.info("task assign start...");
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/***
	 * У��task�Ƿ��ܱ���TaskCenter�ύ
	 * @param dataType
	 * @param task
	 * @return
	 */
	private boolean checkTask(DataType dataType, AbstractLogTask task) {
		boolean passCheck = false;
		if (pools == null || pools.size() == 0) return passCheck;
		
		for (CustomizationFixedTheadPool pool : pools) {
			if (pool.checkSumbit(task)) {
				passCheck = true;
			}
		}
		
		return passCheck;
	}
	
	/***
	 * ��ʱͬ������
	 * @author youji.zj
	 * @version 1.0
	 *
	 */
	class SyncWorker implements Runnable {

		@Override
		public void run() {
			loadTasks();
		}
		
	}
}
