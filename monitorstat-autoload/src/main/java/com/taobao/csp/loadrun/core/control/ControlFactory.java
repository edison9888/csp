package com.taobao.csp.loadrun.core.control;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;

/***
 * control��������
 * @author youji.zj
 * @version 2012-06-25
 *
 */
public class ControlFactory {
	
	/***
	 * apache ������control
	 * @param target
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static IControl getApacheSplitControl(LoadrunTarget target, String ip) throws Exception {
		IControl control;
		if (target.getMode() == AutoLoadMode.SSH) {
			control = new ApacheSplitFlowControl(ip, target.getTargetUserName(), target.getTargetPassword(), target.getAppUser());
			
			control.putAttribute(ControlAtrribute.BIN_PATH, target.getApacheBinPath());  // ����·��
			control.putAttribute(ControlAtrribute.CONFIG_PATH, target.getJkConfigPath()); // ����·��
			control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // Ŀ�����ip
		} else {
			control = provideScriptControl(target, ip);
		}
		
		return control;
	}
	
	/***
	 * apache �����control
	 * @param target
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static IControl getApacheProxyControl(LoadrunTarget target, String ip) throws Exception {
		IControl control;
		if (target.getMode() == AutoLoadMode.SSH) {
			control = new ApacheProxyControl(ip, target.getTargetUserName(), target.getTargetPassword(), target.getAppUser());
			
			control.putAttribute(ControlAtrribute.BIN_PATH, target.getApacheBinPath());  // ����·��
			control.putAttribute(ControlAtrribute.CONFIG_PATH, target.getJkConfigPath()); // ����·��
			control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // Ŀ�����ip
		} else {
			control = provideScriptControl(target, ip);
		}
		
		return control;
	}

	/***
	 * nginx �����control
	 * @param target
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static IControl getNginxControl(LoadrunTarget target, String ip) throws Exception {
		IControl control;
		if (target.getMode() == AutoLoadMode.SSH) {
			control = new NginxProxyControl(ip, target.getTargetUserName(), target.getTargetPassword(), target.getAppUser());
			
			control.putAttribute(ControlAtrribute.BIN_PATH, target.getApacheBinPath());  // ����·��
			control.putAttribute(ControlAtrribute.CONFIG_PATH, target.getJkConfigPath()); // ����·��
			control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // Ŀ�����ip
		} else {
			control = provideScriptControl(target, ip);
		}
		
		return control;
	}
	
	private static IControl provideScriptControl(LoadrunTarget target, String ip)  {
		IControl control = new ScriptControl(ip);
		
		control.putAttribute(ControlAtrribute.LOAD_IP, target.getTargetIp());  // Ŀ�����ip
		control.putAttribute(ControlAtrribute.SCRIPT_BACK, target.getBackScript()); // ���ݽű�
		control.putAttribute(ControlAtrribute.SCRIPT_RUN, target.getRunScript());  // �����ű�
		control.putAttribute(ControlAtrribute.SCRIPT_RESET, target.getResetScript()); // �ָ��ű�
		
		return control;
	}

}
