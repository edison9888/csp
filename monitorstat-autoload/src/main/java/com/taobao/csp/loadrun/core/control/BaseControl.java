
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
	
	/*** control�����Լ��� ***/
	private Map<ControlAtrribute, String> atrrs = new HashMap<ControlAtrribute, String>();
	
	private volatile boolean backuped = false;
	
	private volatile boolean reseted = false;
	
	private String appUser = "admin";
	
	public BaseControl(String targetip, String userName, String password, String appUser) throws Exception {
		super(targetip, userName, password);
		putAttribute(ControlAtrribute.TEMP_FILE_NAME, "tmp_config.xml");  // ��ʱ�ļ���
		putAttribute(ControlAtrribute.BACK_FILE_NAME, "back_config.xml"); // �����ļ���
		putAttribute(ControlAtrribute.LOCAL_PATH, "loadrun/config/"); // ���Ŀ�����ȡ�����������ļ�
		
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

		// ���Ŀ¼ loadrun/config/tmp_config.xml
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
	
	/*** ԭ���󷽷���ʵ�֣����ڲ�֪������Ӱ�죬����ԭ���󷽷�δ��***/
	public void doCommand() throws IOException {}
	
	/***
	 * ��������
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
	 * 1���滻
	 * 2������
	 * @return ѹ��ִ�н׶�����
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
	 * 1����ԭ����
	 * 2������
	 * @return ѹ����ָ�������
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
	 * ��ȡ����������
	 * �������control��д
	 * @return
	 */
	protected String launchCommand() {
		return null; 
	}
	
	/***
	 * ��ȡ�����û�
	 * ����Ǳ�ֱ������root�û��������̣߳����߳���ads�û���
	 * @return
	 */
	protected String getLauchUser() {
		StringBuilder user = new StringBuilder(" ");
		
		if (appUser != null && (!appUser.trim().equals("ads"))) {
			user.append("-u ").append(appUser).append(" ");
		}
		
		return user.toString();
	}

	/*** ������Ҫ�ڲ�ͬ��apacheѹ������б���д ***/
	protected void createTempConfig() throws Exception { }
	
	/***
	 * �����ɵ���ʱ�����ļ����͵�ѹ�����
	 * @param configPath
	 * @throws Exception
	 */
	private void sendConfig(String configPath) throws Exception{
		try{
			SCPClient scp = this.getTargetSSHConn().createSCPClient();
			scp.put(configPath, "/tmp");
		}catch (Exception e) {
			throw new  Exception("scp " + configPath + " /tmp �����ļ�ִ�г���:" 
					+ this.getTargetIp() + "" + this.getTargetUserName() + "/"+this.getTargetPassword() + " " + e.getMessage());
		}
		logger.info("send config :" + configPath + " ��" + this.getTargetIp() + "�� " + "/tmp");
	}

}
