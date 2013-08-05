package com.taobao.www.manager.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;

import com.taobao.www.dao.PressureDao;
import com.taobao.www.dao.impl.PressureDaoImpl;
import com.taobao.www.entity.AppConfig;
import com.taobao.www.entity.AppMachine;
import com.taobao.www.entity.PressureResult;
import com.taobao.www.manager.PressureService;

@SuppressWarnings("serial")
public class PressureServiceImpl extends BaseService implements PressureService {

	private static final Logger logger = Logger.getLogger(PressureServiceImpl.class);
	// 线程池设置
	static final ExecutorService pool = Executors.newFixedThreadPool(30);

	/**
	 * 功能：当前用户下的所有压测应用信息。
	 * 
	 * @param userName
	 * 
	 * @return list
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public List<AppConfig> getAllPreConfigs(String userName) {
		return expansionDao.getAllPreConfigs(userName);
	}

	/**
	 * 功能：保存当前用户下压测配置信息，并且返回新增应用配置的id
	 * 
	 * @param appConfig
	 * 
	 * @return id
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public Integer saveOneAppConfig(AppConfig appConfig) {
		List<AppMachine> list = new ArrayList<AppMachine>();
		Integer id = 0;
		int result = expansionDao.saveOneAppConfig(appConfig);
		if (result != 0) {
			long configId = expansionDao.getObjectMaxId("config");
			int appId = Integer.parseInt(String.valueOf(configId));
			id = appId;
			list = getAllMachineDataByAppName(appId, appConfig.getAppName());
			if (list != null && list.size() > 0) { // 保存应用信息和应用机器信息成功
				saveAllAppMachine(list);
			}
		}
		return id;
	}

	/**
	 * 功能：根据id来删除当前用户下压测配置信息.
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public boolean delOneAppConfig(Integer id) {
		return expansionDao.delOneAppConfig(id);
	}

	/**
	 * 功能：根据id和id集合来删除当前用户下压测机器信息.
	 * 
	 * @param flag
	 *            多个id用","分割.
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public void delAppMachineById(String flag) {
		if (flag.indexOf(",") > 0) {
			expansionDao.delMultipleAppMachineById(flag);
		} else {
			expansionDao.delAppMachineById(Integer.parseInt(flag));
		}
	}

	/**
	 * 功能：更新当前用户下压测配置信息.
	 * 
	 * @param appConfig
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public boolean updateOneAppConfig(AppConfig appConfig) {
		return expansionDao.updateOneAppConfig(appConfig);
	}

	/**
	 * 功能：保存当前用户下压测机器信息.
	 * 
	 * @param list
	 *            压测机器集合
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public void saveAllAppMachine(List<AppMachine> list) {
		for (int i = 0; i < list.size(); i++) {
			AppMachine appMachine = list.get(i);
			expansionDao.saveOneAppMachine(appMachine);
		}
	}

	/**
	 * 功能：展示当前用户下应用的机器信息.
	 * 
	 * @param id
	 *            应用id
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public List<AppMachine> getAllAppMachines(Integer id) {
		return expansionDao.getAllAppMachines(id);
	}

	/**
	 * 功能：统计当前用户下应用的机器个数信息.
	 * 
	 * @param id
	 *            应用id
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public int getAllAppMachineCount(Integer id) {
		return expansionDao.getAllAppMachinesCount(id);
	}

	/**
	 * 功能：根据id获取当前用户下一个应用配置信息.
	 * 
	 * @param id
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public AppConfig getOneAppConfigMessage(int id) {
		return expansionDao.getOneAppConfigMessage(id);
	}

	/**
	 * 功能：上传httpload,headerfile到当前应用的所有机器上.
	 * 
	 * @param id
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public boolean executeAndUploadFile(int appId) {
		boolean isResult = false;
		userMap.put(manualCurUser, "0");
		List<AppMachine> list = expansionDao.getOneAppMachineByAppId(appId);
		if (list != null && list.size() > 0) {
			isResult = true;
		}
		return isResult;
	}

	/**
	 * 功能：在当前应用的所有机器上生成待压测的url地址。
	 * 
	 * @param appId
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public int generatePressUrl(int appId) {
		int isResult = 0;
		String username = "";
		String preKinds = "";
		AppConfig config = getOneAppConfigMessage(appId);
		username = config.getUserName();
		preKinds = config.getPreKinds();
		List<AppMachine> list = expansionDao.getOneAppMachineByAppId(appId);
		if (list != null && list.size() > 0) {
			if (preKinds.equals("half")) {
				list.subList(0, list.size() / 2 + 1);
			}
			CountDownLatch latch = null;
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); // 得到前一天
			String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			String yearString = yestedayDate.split("-")[0];
			String monthString = yestedayDate.split("-")[1];
			List<GeneratedUrlTask> urlTaskL = generateUrlTasks(list, yestedayDate, yearString, monthString, appId);
			if (urlTaskL == null || urlTaskL.size() == 0) {
				logger.warn("no proper machine...");
			} else {
				int taskSize = urlTaskL.size();
				logger.info(appId + " task num is :" + taskSize);
				latch = new CountDownLatch(taskSize);
			}

			userMap.put(username, "4");
			for (GeneratedUrlTask task : urlTaskL) {
				task.setLatch(latch);
				pool.submit(task);
			}
			isResult = 1;
		} else {
			isResult = 2;
		}
		return isResult;
	}

	/**
	 * 功能：同步当前应用的机器信息.
	 * 
	 * @param appId
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public int synchronousMacchineInfo(int appId) {
		int isResult = 0;
		AppConfig config = getOneAppConfigMessage(appId);
		List<AppMachine> macList = expansionDao.getAllAppMachines(appId);
		if (macList != null && macList.size() > 0) {
			expansionDao.delMultipleAppMachine(macList);
		}
		List<AppMachine> listMac = getAllMachineDataByAppName(appId, config.getAppName());
		if (listMac != null && listMac.size() > 0) {
			saveAllAppMachine(listMac);
			isResult = 1;
		} else {
			isResult = 2;
		}
		return isResult;
	}

	/**
	 * 功能：效验用户和用户密码信息.
	 * 
	 * @param config
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public boolean checkUserAndPass(AppConfig config) {
		boolean isResult = false;
		Connection conn = loginJumpMachine(config.getUserName(), config.getUserPass());
		if (conn != null) {
			isResult = true;
		}
		return isResult;
	}

	/**
	 * 功能：执行全量压测
	 * 
	 * @param appId
	 *            应用Id
	 * @param exeutetime
	 *            压测时间
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param requestTotle
	 *            每秒请求
	 * @return 返回执行结果
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getPressureInfo(int appId, int exeutetime, String username, String password, int requestTotle,
			int prePort, String flag) {

		CountDownLatch latch = null;
		userMap.put(username, "0");

		String remoteFile = "/home/" + username + "/";

		List<AppMachine> list = expansionDao.getOneAppMachineByAppId(appId);
		if (list == null || list.size() == 0) {
			return null;
		}

		if (list != null && list.size() > 0 && flag.equals("half")) {
			list.subList(0, list.size() / 2 + 1);
		}

		userMap.put(username, "2"); // 获取压测机器负载信息。

		List<LoadTask> taskL = generateTasks(list, exeutetime, requestTotle, prePort);
		if (taskL == null || taskL.size() == 0) {
			logger.warn("no proper machine...");
		} else {
			int taskSize = taskL.size();
			logger.info(appId + " task num is :" + taskSize);
			latch = new CountDownLatch(taskSize);
		}

		userMap.put(username, "4");
		for (LoadTask task : taskL) {
			task.setLatch(latch);
			pool.submit(task);
		}

		try {
			logger.info("wait for generating load result...");
			latch.await();
			logger.info("finish wait, start to wait for load end...");
			int waitTime = exeutetime + 5;
			TimeUnit.SECONDS.sleep(waitTime);
			logger.info("have wait " + waitTime + " second and end load...");
		} catch (InterruptedException e2) {
			logger.error(e2);
		}

		userMap.put(username, "7"); // 正在分析压测日志信息。
		List<PressureResult> listObj = new ArrayList<PressureResult>();
		logger.info(appId + " Analysis log start ");
		for (AppMachine mac : list) {
			String ip = mac.getMachineIp();
			String machineName = mac.getMachineName();
			String logName = machineName + "-" + exeutetime + "-" + requestTotle + ".log";
			String logUrl = "http://" + ip + ":8082/get" + remoteFile + logName + "?begin=0&end=200000&encode=text";
			PressureResult preResult = getLogContentByUrl(logUrl, ip, appId);
			if (preResult != null) {
				preResult.setAppId(appId);
				preResult.setTimeTotle(exeutetime);
				preResult.setUserName(username);
				preResult.setMacName(machineName);
				listObj.add(preResult);
			}
			try {
				new CopyLog(mac.getMachineIp(), username, password).executeCommands("chmod 777 *  \r ");
				new CopyLog(mac.getMachineIp(), username, password).executeCommands(" rm -rf " + logName + " \r ");
			} catch (IOException e) {
				logger.info(" delete pressure log is failed !", e);
			}
		}
		logger.info(appId + " Analysis log end ");

		userMap.put(username, "8"); // 正在保存压测结果信息。

		if (listObj != null || listObj.size() > 0) {
			saveAllPressureResult(listObj);
		}
		listObj.clear();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		userMap.put(username, "9");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return "压测成功！";

	}

	private List<LoadTask> generateTasks(List<AppMachine> list, int exeutetime, int requestTotle, int prePort) {
		logger.info("generate tasks..." + exeutetime + "," + requestTotle + "," + prePort);
		List<LoadTask> taskL = new ArrayList<LoadTask>();

		double loadVal = Double.parseDouble(ResourceBundle.getBundle("common").getString("LOADAVG_RATE"));
		for (AppMachine machine : list) {
			double load = getLoadInfo(machine);
			if (load > loadVal) {
				continue;
			}

			LoadTask task = new LoadTask(machine, exeutetime, requestTotle, prePort);
			taskL.add(task);
		}

		logger.info("end generate tasks..." + exeutetime + "," + requestTotle + "," + prePort);
		return taskL;
	}

	private List<GeneratedUrlTask> generateUrlTasks(List<AppMachine> list, String timeString, String yearString,
			String monthString, int appId) {

		List<GeneratedUrlTask> taskL = new ArrayList<GeneratedUrlTask>();

		for (AppMachine machine : list) {
			GeneratedUrlTask task = new GeneratedUrlTask(machine, timeString, yearString, monthString, path, appId);
			taskL.add(task);
		}
		return taskL;
	}

	private double getLoadInfo(AppMachine machine) {
		double load = 0d;

		String ip = machine.getMachineIp();
		String username = machine.getUserName();
		String machineName = machine.getMachineName();
		String password = machine.getPassword();

		String remoteFile = "/home/" + username + "/";
		String loadName = machineName + "-loadavg.log";
		String command = "nohup uptime >" + loadName + "& \r";
		String loadUrl = "http://" + ip + ":8082/get" + remoteFile + loadName + "?begin=0&end=200000&encode=text";

		try {
			new CopyLog(ip, username, password).executeCommands(command);
			load = getLoadAvgInformation(loadUrl);
			// delete load info file
			new CopyLog(ip, username, password).executeCommands(" chmod 777 * \r ");
			new CopyLog(ip, username, password).executeCommands(" rm -rf " + loadName + " \r");

		} catch (Exception e) {
			logger.info(" get load information is failed ！", e);
		}

		return load;
	}

	/**
	 * 功能：保存压测结果信息
	 * 
	 * @param list
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-12-27
	 * 
	 */
	private void saveAllPressureResult(List<PressureResult> list) {
		for (int i = 0; i < list.size() && list != null && list.size() > 0; i++) {
			PressureResult result = list.get(i);
			expansionDao.saveOnePressureResult(result);
		}
	}

	/**
	 * 功能：终止压测信息。
	 * 
	 * @param flag
	 *            终止标志
	 * @param username
	 *            用户名
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-12-27
	 * 
	 */
	public void stopPressInfo(String username) {
		userMap.put(username, "1");
	}

	/**
	 * 功能：跟踪压测进展。
	 * 
	 * @param username
	 *            用户名
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-12-27
	 * 
	 */
	public String trackingPressInfo(String username) {
		return userMap.get(username);
	}

	/**
	 * 功能：根据应用id和时间来获取压测结果信息.
	 * 
	 * @param appId
	 *            应用id
	 * @param querydate
	 *            时间
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-12-27
	 * 
	 */
	public List<PressureResult> getAllPressureResultMessage(String querydate, int appId) {
		return expansionDao.getAllPressureResultMessage(querydate, appId);
	}

	/**
	 * 功能：效验用户是否有新增应用的权限。
	 * 
	 * @param appName
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */
	public boolean checkUserAddAppName(String appName, String email) {
		Integer appId = expansionDao.getCspAppMessage(appName);
		String permissionDesc = expansionDao.getCspAppPurviewMessage(email);
		if (appId == null || appId == 0 || permissionDesc == null || "".equals(permissionDesc)) {
			return false;
		} else {
			return getAllAppIdMessageByEmail(permissionDesc, appId);
		}
	}

	/**
	 * 功能：效验新增应用名。
	 * 
	 * @param appName
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */
	public boolean checkAddAppName(String appName) {
		Integer appId = expansionDao.getCspAppMessage(appName);
		if (appId == null || appId == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 功能：判断该用户是否有新增应用的权限。
	 * 
	 * @param permissionDesc
	 * 
	 * @param appId
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */
	public boolean getAllAppIdMessageByEmail(String permissionDesc, Integer appId) {
		Map<Integer, Integer> appIdMap = new TreeMap<Integer, Integer>();
		String[] data = permissionDesc.split(";");
		for (String type : data) {
			String[] tData = type.split(":");
			String[] appData = tData[1].split(",");
			if (tData[0].equals("autoLoadrun") || tData[0].equals("manualLoadrun")) {
				for (String aId : appData) {
					appIdMap.put(Integer.parseInt(aId), Integer.parseInt(aId));
				}
			}
		}
		return appIdMap.get(appId) == null ? false : true;
	}

	private PressureDao expansionDao = new PressureDaoImpl();

	public PressureDao getExpansionDao() {
		return expansionDao;
	}

	public void setExpansionDao(PressureDao expansionDao) {
		this.expansionDao = expansionDao;
	}

}

class LoadTask implements Runnable {

	private static final Logger logger = Logger.getLogger(LoadTask.class);

	private AppMachine mac;

	private int exeutetime;

	private int requestTotle;

	private int prePort;

	private CountDownLatch latch;

	public LoadTask(AppMachine mac, int exeutetime, int requestTotle, int prePort) {
		this.mac = mac;
		this.exeutetime = exeutetime;
		this.requestTotle = requestTotle;
		this.prePort = prePort;

	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {

		String ip = mac.getMachineIp();
		String username = mac.getUserName();
		String machineName = mac.getMachineName();
		String password = mac.getPassword();
		String command;
		String logName = machineName + "-" + exeutetime + "-" + requestTotle + ".log";
		logger.info("request  totle value is :" + requestTotle + " ,  exeute  time value :" + exeutetime);
		try {
			command = "cat " + username + "-urls \r";
			boolean data = new CopyLog(ip, username, password).getExecuteMachineName(command);

			if (data) {
				command = "nohup ./http_load -head ./headerfile -rate " + requestTotle + " -seconds " + exeutetime
						+ " -proxy 127.0.0.1:" + prePort + " " + username + "-urls >" + logName + " & \r";
				logger.info(" 开始执行压测命令:" + ip);
				new CopyLog(ip, username, password).executeCommands("chmod 777 * \r ");
				new CopyLog(ip, username, password).executeCommands(command);
				logger.info(" 压测任务已提交:" + ip);
			} else {
				logger.info(" pressure url address is null : " + ip);
			}

			if (latch != null) {
				latch.countDown();
			}

		} catch (Exception e) {
			logger.error(" pressure is failed :" + mac.getMachineIp(), e);
		}
	}
}

class GeneratedUrlTask implements Runnable {

	private static final Logger logger = Logger.getLogger(GeneratedUrlTask.class);

	private AppMachine mac;

	private String timeString;

	private String yearString;

	private String monthString;

	private String path;

	private int appId;

	private CountDownLatch latch;

	public GeneratedUrlTask(AppMachine mac, String timeString, String yearString, String monthString, String path,
			int appId) {
		super();
		this.mac = mac;
		this.timeString = timeString;
		this.yearString = yearString;
		this.monthString = monthString;
		this.path = path;
		this.appId = appId;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		String ip = mac.getMachineIp();
		String username = mac.getUserName();
		String password = mac.getPassword();
		String remoteFile = "/home/" + username + "/";
		String command = " nohup awk '/GET.*200/' /home/admin/cai/logs/cronolog/"
				+ yearString
				+ "/"
				+ monthString
				+ "/"
				+ timeString
				+ "-taobao-access_log|awk -F '\"'  '{print $2}'|awk '{if(!match($2,\"status.taobao\") && length($2) < 150 ) print $2}'"
				+ "|awk -F 'GET' '{print $1}'|head -n 200000 > " + username + "-urls & \r";
		try {
			new CopyLog(ip, username, password).sendRemoteFile(path + "http_load", remoteFile);
			logger.info("Upload  http_load is success ,appId :=" + appId + ",UserName : = " + username
					+ " , machineIp :=" + ip + " , Time:%s" + new Date());
			new CopyLog(ip, username, password).sendRemoteFile(path + "headerfile", remoteFile);
			logger.info("Upload  headerfile is success ,appId :=" + appId + ", UserName : = " + username
					+ " , machineIp :=" + ip + " , Time:%s " + new Date());
			new CopyLog(ip, username, password).executeCommands(command);
			logger.info("execute get url address command  is success ,appId :=" + appId + ", UserName : = " + username
					+ " , machineIp :=" + ip + " , Time:%s " + new Date());
		} catch (Exception e) {
			logger.error(" executeCommands is failed ,appId :=" + appId + ",UserName : = " + username
					+ " , machineIp :=" + ip + " , Time:%s " + new Date(), e);
		}
	}
}
