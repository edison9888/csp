package com.taobao.monitor.stat.schedule;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.accept.ScpAcceptPool;
import com.taobao.monitor.stat.analyse.Analyse;
import com.taobao.monitor.stat.config.AppAnalyseInfo;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.util.Config;
import com.taobao.monitor.stat.util.DepIpInfoContain;

public class AnalyseJob implements Job {
	private static final Logger logger =  Logger.getLogger(AnalyseJob.class);
	

	
	
	private void setDefaultAnalyseInfo(AppAnalyseInfo defaultAnalyseInfo,String opsName){
		defaultAnalyseInfo.setOpsfreeName(opsName);
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.GCLogAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.MbeanLogAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.PvAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.HostAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.MonitorLogAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.MonitorLogAnalyse2");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.ShareReportAnalyse");
		
		defaultAnalyseInfo.getRemoteLogPath().add("/home/admin/logs/mbean.log.${yyyy-MM-dd}");
		defaultAnalyseInfo.getRemoteLogPath().add("/home/admin/logs/gc.log");
		defaultAnalyseInfo.getRemoteLogPath().add("/home/admin/logs/monitor/monitor-app-org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader.log.${yyyy-MM-dd}");
		defaultAnalyseInfo.getRemoteLogPath().add("/home/admin/logs/monitor/monitor-app-org.jboss.mx.loading.UnifiedClassLoader3.log.${yyyy-MM-dd}");
		defaultAnalyseInfo.getRemoteLogPath().add("/home/admin/logs/monitor/monitor.log.${yyyy-MM-dd}");
	}
	
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
			DepIpInfoContain.get().clear();
			String logFilePath = Config.getValue("LOG_PATH");
			if(logFilePath == null){
				logger.error("��Ҫ��ʱ�ļ�Ŀ¼!");
				System.exit(-1);
			}
			
			String HOSTNAME = System.getenv("HOSTNAME");
			System.out.println("HOSTNAME:"+HOSTNAME);
			
			if(HOSTNAME == null){
				logger.error("��Ҫ����������!");
				System.exit(-1);
			}
			
			List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
			
			for(AppInfoPo app:appList){
				if(app.getAppStatus()==0&&app.getDayDeploy() == Constants.DEFINE_DATA_EFFECTIVE){
					try {
						createTmpDir(logFilePath,app.getOpsName());
						AppAnalyseInfo defaultAnalyseInfo = new AppAnalyseInfo();
						setDefaultAnalyseInfo(defaultAnalyseInfo,app.getOpsName());
						logger.info("��ʼ����Ӧ��:"+app.getOpsName());
						doScpLog(app,defaultAnalyseInfo);
						doAnalyse(app.getOpsName(),defaultAnalyseInfo.getAnalyseList());
						logger.info("��������Ӧ��:"+app.getOpsName());
					} catch (Exception e1) {
						logger.error("����"+app.getOpsName()+"����",e1);
					}
				}else{
					logger.info(""+app.getOpsName()+"û�в����ձ�");
				}
			}
	}
	
	
	private void createTmpDir(String tmpPath,String appName){
		File file = new File(tmpPath,appName);
		if(!file.exists()){
			file.mkdirs();
		}
		
	}
	
	
	private void doAnalyse(String appName,Set<String> analyseList){
		ReportContent rc = new ReportContent();
		Map<String,Class> mapClass = new HashMap<String, Class>();
		for(String analyseclass:analyseList){
			try{
				Class classObj = mapClass.get(analyseclass);
				if(classObj==null){
					classObj = Class.forName(analyseclass);
					mapClass.put(analyseclass, classObj);
				}						
				
				Constructor con = classObj.getConstructor(String.class);
				if(con==null){
					logger.error(analyseclass+" ���캯��ȱ��");
					continue;	
				}
				
				Object obj = con.newInstance(appName);
				if(obj instanceof Analyse){
					Analyse a = (Analyse)obj;
					a.analyseLogFile(rc);
				}else{
					logger.error(analyseclass+" �Ǽ̳�Analyse");
				}
			}catch(Exception e){
				logger.error("����analyse ����",e);
			}
		}
	}
	
	
	private void doScpLog(AppInfoPo appInfoPo,AppAnalyseInfo po){
		
		String opsField = appInfoPo.getOpsField();
		String opsName = appInfoPo.getOpsName();
		
		Map<String,List<HostPo>> map = OpsFreeHostCache.get().getHostMapNoCache(opsField, opsName) ;
		if(map==null||map.size()==0){
			logger.info(po.getOpsfreeName()+" �޷���ȡ��������IP");
			return ;
		}
		logger.info("��ʼ��ȡ"+po.getOpsfreeName()+" ��־");
		for(Map.Entry<String, List<HostPo>> entry:map.entrySet()){
			String sitename = entry.getKey();
			
			List<HostPo> list = entry.getValue();
			for(HostPo h:list){
				h.setUserName(appInfoPo.getLoginName());
				h.setUserPassword(appInfoPo.getLoginPassword());
			}
			long time = System.currentTimeMillis();
			ScpAcceptPool pool = new ScpAcceptPool(po,entry.getValue(),Config.getValue("LOG_PATH"),null);
			pool.doScp();
			logger.info(po.getOpsfreeName()+" "+sitename+" ��������:"+entry.getValue().size()+" ʹ��ʱ��"+(System.currentTimeMillis()-time));
		}
		
		
	}
	
	

}
