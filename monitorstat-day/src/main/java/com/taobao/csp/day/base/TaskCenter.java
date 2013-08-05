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
 * 持续采集的任务中心，存在备份日志和日志的滚动逻辑
 * 1、任务的加载
 * 2、任务的分配
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
	 * 注册线程池
	 * @param pool
	 */
	public void registThreadPool(CustomizationFixedTheadPool pool){
		pools.add(pool);
	}
	
	/***
	 * 加载task前的检验
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
		
		// 3个小时同步一次任务
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
			
			// 状态设置为挂起,worker线程进入wait释放锁后，拿到锁开始同步
			this.setHangup(true); 
			synchronized (taskLock) {
				// 清空旧的task
				Map<String, AbstractLogTask> oldTaskM = new HashMap<String, AbstractLogTask>();

				for (AbstractLogTask task : tasks) {
					oldTaskM.put(task.identityKey(), task);
				}
				tasks.clear();
				
				// 应用、机器、日志
				for (AppInfoPo app : list) {
					String opsName = app.getOpsName();
					
					// 测试用
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
						AbstractAnalyser analyser = null; // 等到确定不是已经存在的task后再初始化
						DataType dataType = this.getDataType();
						String logPath = AppLogPathConfig.getLogPath(opsName, dataType);
						long fetchSize = DayConfig.getFetchSize(dataType);
						
						AbstractLogTask task = createTask(analyser, opsName, hostInfo, dataType, logPath, fetchSize);
						if (!checkTask(dataType, task)) continue;   // taskcenter不存在能执行该task的线程池

						// 并非第一次加载，如果task已经存在，用已经存在的task
						if (oldTaskM.containsKey(task.identityKey())) {
							AbstractLogTask oldTask = oldTaskM.get(task.identityKey());
							task = oldTask;
						} else {
							task.setAnalyser(AnalyserFactory.getInstance(opsName, hostInfo, dataType));
						}

						task.setValiable(true);  // 把所有task打成可用标志
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
	 * 专门用于测试的方法
	 */
//	private void loadTasks() {
//		logger.info("load tasks");
//		Map<String, Set<String>> appHostM = DayUtil.getTddlSimulate();
//		this.setHangup(true); // 状态设置为挂起
//		
//		synchronized (taskLock) {
//			// 清空旧的task
//			for (List<LogDataFetcherTask> taskL : tasks.values()) {
//				taskL.clear();
//			}
//			
//			// 应用、机器、日志
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

							// 状态为挂起时，释放锁，等待同步完成的信号发出
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
	 * 校验task是否能被该TaskCenter提交
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
	 * 定时同步任务
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
