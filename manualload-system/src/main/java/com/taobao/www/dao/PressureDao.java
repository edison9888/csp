package com.taobao.www.dao;

import java.util.List;

import com.taobao.www.entity.AppConfig;
import com.taobao.www.entity.AppMachine;
import com.taobao.www.entity.PressureResult;

public interface PressureDao {

	/**
	 * 功能：保存压测配置对象
	 * 
	 * @return 新增压测对象的ID
	 * @author wb-tangjinge
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

	public boolean delAppMachineById(Integer id);

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
	public void saveOneAppMachine(AppMachine oneKey);

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
	public int getAllAppMachinesCount(Integer id);

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
	 * 功能：保存压测结果信息
	 * 
	 * @return 新增压测结果的ID
	 * @author wb-tangjinge
	 */
	public Integer saveOnePressureResult(PressureResult pressureResult);

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
	public List<AppMachine> getOneAppMachineByAppId(int appId);

	public List<AppMachine> getAllMachineByAppId(int appId);

	public boolean delAppMachineByAppId(Integer id);

	public void delMultipleAppMachineById(String flag);

	public void delMultipleAppMachine(List<AppMachine> macList);

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

	public long getObjectMaxId(String type);

	/**
	 * 功能： 得到一个应用和用户下所有的压测结果信息。
	 * 
	 * @param appId
	 *            应用id
	 * @param username
	 *            用户名
	 * @return
	 * @author wb-tangjinge
	 */
	public List<PressureResult> getAllPressureResultMessage(String querydate, int appId);

	/**
	 * 功能：获取csp系统的所有应用名称和id。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */

	public Integer getCspAppMessage(String appName);

	/**
	 * 功能：根据email获取权限信息。
	 * 
	 * @param email
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */

	public String getCspAppPurviewMessage(String email);

}
