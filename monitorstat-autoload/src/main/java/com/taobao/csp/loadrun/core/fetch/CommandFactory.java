package com.taobao.csp.loadrun.core.fetch;

import java.util.Calendar;

import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.monitor.common.util.TimeConvertUtil;

/***
 * 获取fetch的采集指令，目前针对淘宝和b2b
 * @author youji.zj
 * @version 2012-06-22
 *
 */
public class CommandFactory {
	
	/***
	 * 获取apache日志，b2b用script方式，淘宝用ssh方式
	 * 如果淘宝需要支持script,日志路径需要支持可配置
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getApacheFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f /home/admin/cai/logs/cronolog/"+TimeConvertUtil.formatCurrentDate("yyyy/MM/yyyy-MM-dd-")+"taobao-access_log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			Calendar calendar = Calendar.getInstance();
			int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			
			command = "http://" + ip + ":8082/" + 
				"tail/home/admin/output/logs/cookie_logs/" + index + "/cookie_log?"+ getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * 获取cpu信息，b2b用script方式，淘宝用ssh方式
	 * 如果淘宝需要支持script，需要加入对于的哈勃脚本
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getCpuFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "mpstat 1";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/scriptexcute?token=dcbeb81d186a89a11c1515ced9022bca&script=mpstat.sh&timeout=20";
		}
		
		return command;
	}
	
	/***
	 * 获取gc信息，b2b用script方式，淘宝用ssh方式
	 * 如果淘宝需要支持script，gc日志的路径需支持可配置
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getGcFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "http://" + ip + ":8082/" + 
			"tail/home/admin/logs/gc.log?" + getTaskId() + "&encode=text";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
				"tail/home/admin/output/logs/csp/gc.log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * hsf信息获取，b2b没有这个日志
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getHsfFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f -c 0 /home/admin/logs/monitor/monitor-app-org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader.log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
			"tail/home/admin/logs/monitor/monitor-app-org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader.log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * eagleeye信息获取，b2b没有这个日志
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getEagleeyeFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f /home/admin/logs/eagleeye/eagleeye.log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
			"tail/home/admin/logs/eagleeye/eagleeye.log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * io信息获取
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getIoFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tsar --traffic -l -i 1";
		}
		
		// todo 后续换成脚本方式的话，这里需要修改
		if (mode == AutoLoadMode.SCRIPT) {
			command = "tsar --traffic -l -i 1";
		}
		
		return command;
	}
	
	/***
	 * 线程数获取
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getThreadCountFetchCommand(AutoLoadMode mode, String ip) {
		String command = "while (true); do ps -o nlwp= -p `ps -ef | grep -e \"org.apache.catalina.startup.B[o]otstrap\" " +
		" -e \"org.jb[o]ss.Main\" | awk '{print $2}'`; sleep 1; done;";
		
		
		return command;
	}
	
	/***
	 * Tair信息获取，b2b没有这个日志
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getTairFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f -c 0 /home/admin/logs/monitor/monitor-app-org.jboss.mx.loading.UnifiedClassLoader3.log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
			"tail/home/admin/logs/monitor/monitor-app-org.jboss.mx.loading.UnifiedClassLoader3.log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * Tddl信息获取，b2b没有这个日志
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getTddlFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f -n 0 /home/admin/logs/tddl/tddl-matrix-statistic.log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
			"tail/home/admin/logs/tddl/tddl-atom-statistic.log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * jvm 信息获取,b2b没有这个日志
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getJvmFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f /home/admin/logs/mbean.log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
			"tail/home/admin/logs/mbean.log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * load信息获取，b2b用script方式，淘宝用ssh方式
	 * 如果淘宝要用script方式，需要加入对于脚本
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getLoadFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "top -bi -d 1";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/scriptexcute?token=dcbeb81d186a89a11c1515ced9022bca&script=top.sh&timeout=20";
		}
		
		return command;
	}
	
	/***
	 * 获取tomcat信息，b2b暂时不取这些信息
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getTomcatFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tail -f /home/admin/logs/tomcataccess/localhost_access_log."+TimeConvertUtil.formatCurrentDate("yyyy-MM-dd")+".log";
		}
		
		if (mode == AutoLoadMode.SCRIPT) {
			command = "http://" + ip + ":8082/" + 
				"tail/home/admin/logs/tomcataccess/localhost_access_log."+TimeConvertUtil.formatCurrentDate("yyyy-MM-dd")+".log?" + getTaskId() + "&encode=text";
		}
		
		return command;
	}
	
	/***
	 * 每次压测都取不同的task id
	 * @return
	 */
	private static String getTaskId() {
		return "task_id=" + (System.currentTimeMillis() % 1000 + System.currentTimeMillis() % 1000000);
	}
	
	public static void main(String [] args) {
		Calendar calendar = Calendar.getInstance();
		
		System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
	}
}
