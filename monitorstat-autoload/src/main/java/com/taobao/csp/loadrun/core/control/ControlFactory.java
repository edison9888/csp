package com.taobao.csp.loadrun.core.control;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;

/***
 * control的生产者
 * @author youji.zj
 * @version 2012-06-25
 *
 */
public class ControlFactory {
	
	/***
	 * apache 分流的control
	 * @param target
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static IControl getApacheSplitControl(LoadrunTarget target, String ip) throws Exception {
		IControl control;
		if (target.getMode() == AutoLoadMode.SSH) {
			control = new ApacheSplitFlowControl(ip, target.getTargetUserName(), target.getTargetPassword(), target.getAppUser());
			
			control.putAttribute(ControlAtrribute.BIN_PATH, target.getApacheBinPath());  // 启动路径
			control.putAttribute(ControlAtrribute.CONFIG_PATH, target.getJkConfigPath()); // 配置路径
			control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // 目标机器ip
		} else {
			control = provideScriptControl(target, ip);
		}
		
		return control;
	}
	
	/***
	 * apache 代理的control
	 * @param target
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static IControl getApacheProxyControl(LoadrunTarget target, String ip) throws Exception {
		IControl control;
		if (target.getMode() == AutoLoadMode.SSH) {
			control = new ApacheProxyControl(ip, target.getTargetUserName(), target.getTargetPassword(), target.getAppUser());
			
			control.putAttribute(ControlAtrribute.BIN_PATH, target.getApacheBinPath());  // 启动路径
			control.putAttribute(ControlAtrribute.CONFIG_PATH, target.getJkConfigPath()); // 配置路径
			control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // 目标机器ip
		} else {
			control = provideScriptControl(target, ip);
		}
		
		return control;
	}

	/***
	 * nginx 代理的control
	 * @param target
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static IControl getNginxControl(LoadrunTarget target, String ip) throws Exception {
		IControl control;
		if (target.getMode() == AutoLoadMode.SSH) {
			control = new NginxProxyControl(ip, target.getTargetUserName(), target.getTargetPassword(), target.getAppUser());
			
			control.putAttribute(ControlAtrribute.BIN_PATH, target.getApacheBinPath());  // 启动路径
			control.putAttribute(ControlAtrribute.CONFIG_PATH, target.getJkConfigPath()); // 配置路径
			control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // 目标机器ip
		} else {
			control = provideScriptControl(target, ip);
		}
		
		return control;
	}
	
	private static IControl provideScriptControl(LoadrunTarget target, String ip)  {
		IControl control = new ScriptControl(ip);
		
		control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // 目标机器ip
		control.putAttribute(ControlAtrribute.SCRIPT_BACK, target.getBackScript()); // 备份脚本
		control.putAttribute(ControlAtrribute.SCRIPT_RUN, target.getRunScript());  // 启动脚本
		control.putAttribute(ControlAtrribute.SCRIPT_RESET, target.getResetScript()); // 恢复脚本
		
		return control;
	}

}
