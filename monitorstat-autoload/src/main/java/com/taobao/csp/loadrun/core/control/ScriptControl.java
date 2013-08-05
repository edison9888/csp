package com.taobao.csp.loadrun.core.control;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/***
 * 脚本方式压测的控制类
 * @author youji.zj
 * @version 2012-06-25
 *
 */
public class ScriptControl implements IControl {
	
	public static Logger logger = Logger.getLogger(ScriptControl.class);

	String ip;
	
	/*** control的属性集合 ***/
	private Map<ControlAtrribute, String> atrrs = new HashMap<ControlAtrribute, String>();
	
	private volatile boolean backuped = false;
	
	private volatile boolean reseted = false;
	
	public ScriptControl(String ip) {
		this.ip = ip;
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
		String scriptBack = getAttribute(ControlAtrribute.SCRIPT_BACK);
		
		if (!StringUtils.isEmpty(scriptBack) && !backuped) {
			String command = "http://" + this.ip + ":8082/scriptexcute?token=dcbeb81d186a89a11c1515ced9022bca&script=" + scriptBack + "&timeout=30";
			logger.info(command);
			executeCommand(command);
			backuped = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void control() throws Exception {
		String scriptRun = getAttribute(ControlAtrribute.SCRIPT_RUN);
		
		String loadIp = getAttribute(ControlAtrribute.LOAD_IP);
		String ratioLocal = getAttribute(ControlAtrribute.RATIO_LOCAL);
		String ratioTarget = getAttribute(ControlAtrribute.RATIO_TARGET);
		String proxyLocal = getAttribute(ControlAtrribute.PROXY_LOCAL);
		String proxyTarget = getAttribute(ControlAtrribute.PROXY_TARGET);
		
		String params;
		
		// 判断压测的类型是否为apache分流，apache代理或者其它
		if (ratioLocal != null && ratioTarget != null) {
			params = "localhost:" + ratioLocal + "," + loadIp + ":" + ratioTarget;
		} else if (proxyLocal != null && proxyTarget != null) {
			params = "localhost:" + proxyLocal + "," + loadIp + ":" + proxyTarget;
		} else {
			params = loadIp;
		}
		
		if (!StringUtils.isEmpty(scriptRun)) {
			String command = "http://" + this.ip + ":8082/scriptexcute?token=dcbeb81d186a89a11c1515ced9022bca&script=" + scriptRun + "&args=" + params + "&timeout=30";
			logger.info(command);
			executeCommand(command);
		}
	}

	@Override
	public boolean reset() throws Exception {
		String scriptReset = getAttribute(ControlAtrribute.SCRIPT_RESET);
		
		if (!StringUtils.isEmpty(scriptReset) && !reseted) {
			String command = "http://" + this.ip + ":8082/scriptexcute?token=dcbeb81d186a89a11c1515ced9022bca&script=" + scriptReset + "&timeout=30";	
			logger.info(command);
			executeCommand(command);
			reseted = true;
			return true;
		} else {
			return false;
		}
	}
	
	private void executeCommand(String command) throws Exception {
		logger.info("command is " + command);
		if (command == null) return;
		String[] scriptArr = command.split("\\?");
		if (scriptArr.length != 2) return;
		
		URL url = new URL(scriptArr[0]);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);

		// send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(scriptArr[1]);
		wr.flush();
		wr.close();

		// Get Response
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = rd.readLine()) != null) {
			logger.info("script response :" + line);
		}
		rd.close();
	}

}
