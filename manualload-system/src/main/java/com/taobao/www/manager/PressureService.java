package com.taobao.www.manager;

import java.util.List;

import com.taobao.www.entity.AppConfig;
import com.taobao.www.entity.AppMachine;
import com.taobao.www.entity.PressureResult;

public interface PressureService {

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
	public List<AppMachine> getAllAppMachines(Integer id);

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
	public int getAllAppMachineCount(Integer id);

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
	public Integer saveOneAppConfig(AppConfig appConfig);

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
	public boolean delOneAppConfig(Integer id);

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
	public void delAppMachineById(String flag);

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
	public boolean updateOneAppConfig(AppConfig appConfig);

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
	public List<AppConfig> getAllPreConfigs(String userName);

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
	public boolean executeAndUploadFile(int appId);

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
	public int generatePressUrl(int appId);

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
	public int synchronousMacchineInfo(int appId);

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
	public AppConfig getOneAppConfigMessage(int id);

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
			int prePort, String flag);

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
	public List<PressureResult> getAllPressureResultMessage(String querydate, int appId);

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
	public void stopPressInfo(String username);

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
	public String trackingPressInfo(String username);

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
	public boolean checkUserAndPass(AppConfig config);

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
	public boolean checkUserAddAppName(String appName, String email);

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
	public boolean checkAddAppName(String appName);

}
