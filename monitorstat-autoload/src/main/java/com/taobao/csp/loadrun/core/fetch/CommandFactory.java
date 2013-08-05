package com.taobao.csp.loadrun.core.fetch;

import java.util.Calendar;

import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.monitor.common.util.TimeConvertUtil;

/***
 * ��ȡfetch�Ĳɼ�ָ�Ŀǰ����Ա���b2b
 * @author youji.zj
 * @version 2012-06-22
 *
 */
public class CommandFactory {
	
	/***
	 * ��ȡapache��־��b2b��script��ʽ���Ա���ssh��ʽ
	 * ����Ա���Ҫ֧��script,��־·����Ҫ֧�ֿ�����
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
	 * ��ȡcpu��Ϣ��b2b��script��ʽ���Ա���ssh��ʽ
	 * ����Ա���Ҫ֧��script����Ҫ������ڵĹ����ű�
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
	 * ��ȡgc��Ϣ��b2b��script��ʽ���Ա���ssh��ʽ
	 * ����Ա���Ҫ֧��script��gc��־��·����֧�ֿ�����
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
	 * hsf��Ϣ��ȡ��b2bû�������־
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
	 * eagleeye��Ϣ��ȡ��b2bû�������־
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
	 * io��Ϣ��ȡ
	 * @param mode
	 * @param ip
	 * @return
	 */
	public static String getIoFetchCommand(AutoLoadMode mode, String ip) {
		String command = null;
		if (mode == AutoLoadMode.SSH) {
			command = "tsar --traffic -l -i 1";
		}
		
		// todo �������ɽű���ʽ�Ļ���������Ҫ�޸�
		if (mode == AutoLoadMode.SCRIPT) {
			command = "tsar --traffic -l -i 1";
		}
		
		return command;
	}
	
	/***
	 * �߳�����ȡ
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
	 * Tair��Ϣ��ȡ��b2bû�������־
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
	 * Tddl��Ϣ��ȡ��b2bû�������־
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
	 * jvm ��Ϣ��ȡ,b2bû�������־
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
	 * load��Ϣ��ȡ��b2b��script��ʽ���Ա���ssh��ʽ
	 * ����Ա�Ҫ��script��ʽ����Ҫ������ڽű�
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
	 * ��ȡtomcat��Ϣ��b2b��ʱ��ȡ��Щ��Ϣ
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
	 * ÿ��ѹ�ⶼȡ��ͬ��task id
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
