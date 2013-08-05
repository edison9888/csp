
package com.taobao.csp.loadrun.core.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.ethz.ssh2.SCPClient;

/**
 * @author youji.zj
 * @version 2011-12-01
 */
public class BaseControl extends BaseSsh2Exec implements IControl {

	private static final Logger logger = Logger.getLogger(BaseControl.class);
	
	/*** control的属性集合 ***/
	private Map<ControlAtrribute, String> atrrs = new HashMap<ControlAtrribute, String>();
	
	private volatile boolean backuped = false;
	
	private volatile boolean reseted = false;
	
	private String appUser = "admin";
	
	public BaseControl(String targetip, String userName, String password, String appUser) throws Exception {
		super(targetip, userName, password);
		putAttribute(ControlAtrribute.TEMP_FILE_NAME, "tmp_config.xml");  // 临时文件名
		putAttribute(ControlAtrribute.BACK_FILE_NAME, "back_config.xml"); // 备份文件名
		putAttribute(ControlAtrribute.LOCAL_PATH, "loadrun/config/"); // 存放目标机器取过来的配置文件
		
		if (StringUtils.isNotBlank(appUser)) {
			this.appUser = appUser;
		}
	}
	
	public String getAppUser() {
		return appUser;
	}

	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}
	
	@Override
	public void putAttribute(ControlAtrribute attr, String value) {
		atrrs.put(attr, value);
		
	}

	@Override
	public String getAttribute(ControlAtrribute attr) {
		return atrrs.get(attr);
	}
	
	
	@Override
	public boolean backup() throws Exception {

		if (!backuped) {
			List<String> backCommands = generateBackupCommands();
			doCommand(backCommands);
			backuped = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void control() throws Exception {
		createTempConfig();

		// 相对目录 loadrun/config/tmp_config.xml
		String path = getAttribute(ControlAtrribute.LOCAL_PATH)
				+ getAttribute(ControlAtrribute.TEMP_FILE_NAME);
		sendConfig(path);
		List<String> cotrolCommands = generateControlCommands();
		doCommand(cotrolCommands);
	}
	
	@Override
	public boolean reset() throws Exception {
		if (!reseted) {
			try {
				List<String> resetCommands = generateResetCommands();
				doCommand(resetCommands);
				reseted = true;
				return true;
			} finally {
				close();
			}	
		} else {
			return false;
		}
	}
	
	@Override
	protected void doCommand(List<String> commands) throws IOException {
		if (commands == null) return;
		for (String command : commands) {
			write(command, getOut());
			String str = read(getIn(),1500);
			logger.info(str);
			this.doResponse(str);
		}
	}
	
	/*** 原抽象方法的实现，由于不知道其它影响，所以原抽象方法未动***/
	public void doCommand() throws IOException {}
	
	/***
	 * 备份配置
	 * @return
	 */
	protected List<String> generateBackupCommands() {
		List<String> commands = new ArrayList<String>();
		commands.add("export LANG=en_US");
		
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		String backName = getAttribute(ControlAtrribute.BACK_FILE_NAME);
		if (!this.appUser.equals(this.getTargetUserName())) {
			commands.add("sudo -u " + this.appUser + " cp -f " + configPath + " " + "/tmp/" + backName);
			commands.add("sudo chmod 777 /tmp/" + backName);
		} else {
			commands.add("cp -f " + configPath + " " + "/tmp/" + configPath);
			commands.add("chmod 777 /tmp/" + configPath);
		}
		
		return commands;
	}
	
	/***
	 * 1、替换
	 * 2、重启
	 * @return 压测执行阶段命令
	 */
	protected List<String> generateControlCommands() {
		List<String> commands = new ArrayList<String>();
		commands.add("export LANG=en_US");
		
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		String tempName = getAttribute(ControlAtrribute.TEMP_FILE_NAME);
		if(!this.appUser.equals(this.getTargetUserName())){
			commands.add("sudo chmod 777 /tmp/" + tempName);
			commands.add("sudo -u " + this.appUser + " cp -f /tmp/" + tempName + " " + configPath);
		} else {
			commands.add("chmod 777 /tmp/" + tempName);
			commands.add("cp -f /tmp/" + tempName + " " + configPath);
		}
		
		if (launchCommand() != null) {
			commands.add(launchCommand());
		}
		return commands;
	}
	
	/***
	 * 1、还原配置
	 * 2、重启
	 * @return 压测完恢复的命令
	 */
	protected List<String> generateResetCommands() {
		List<String> commands = new ArrayList<String>();
		commands.add("export LANG=en_US");
		
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		String backName = getAttribute(ControlAtrribute.BACK_FILE_NAME);
		if(!this.appUser.equals(this.getTargetUserName())){
			commands.add("sudo -u " + this.appUser + " cp -f "+ "/tmp/" + backName + " " + configPath);
		} else {
			commands.add("cp -f "+ "/tmp/" + backName + " " + configPath);
		}
		
		if (launchCommand() != null) {
			commands.add(launchCommand());
		}
		return commands;
	}
	
	/***
	 * 获取重启的命令
	 * 被具体的control重写
	 * @return
	 */
	protected String launchCommand() {
		return null; 
	}
	
	/***
	 * 获取启动用户
	 * 广告那边直接是以root用户启动主线程，子线程在ads用户下
	 * @return
	 */
	protected String getLauchUser() {
		StringBuilder user = new StringBuilder(" ");
		
		if (appUser != null && (!appUser.trim().equals("ads"))) {
			user.append("-u ").append(appUser).append(" ");
		}
		
		return user.toString();
	}

	/*** 方法需要在不同的apache压测策略中被重写 ***/
	protected void createTempConfig() throws Exception { }
	
	/***
	 * 将生成的临时配置文件发送到压测机器
	 * @param configPath
	 * @throws Exception
	 */
	private void sendConfig(String configPath) throws Exception{
		try{
			SCPClient scp = this.getTargetSSHConn().createSCPClient();
			scp.put(configPath, "/tmp");
		}catch (Exception e) {
			throw new  Exception("scp " + configPath + " /tmp 发送文件执行出错:" 
					+ this.getTargetIp() + "" + this.getTargetUserName() + "/"+this.getTargetPassword() + " " + e.getMessage());
		}
		logger.info("send config :" + configPath + " 到" + this.getTargetIp() + "的 " + "/tmp");
	}

}
