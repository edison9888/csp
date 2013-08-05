package com.taobao.monitor.stat;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.accept.ScpAcceptPool;
import com.taobao.monitor.stat.analyse.Analyse;
import com.taobao.monitor.stat.config.AppAnalyseInfo;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.util.Config;
import com.taobao.monitor.stat.util.DepIpInfoContain;

public class RunApp  {
	private static final Logger logger =  Logger.getLogger(RunApp.class);
	
	
	public static void  main(String[] args){
		RunApp app = new RunApp();
		app.execute(args[0],args[1]);
		
	}
	

	
	
	private void setDefaultAnalyseInfo(AppAnalyseInfo defaultAnalyseInfo,String opsName){
		defaultAnalyseInfo.setOpsfreeName(opsName);
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.GCLogAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.MbeanLogAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.PvAnalyse");
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.ApacheLogAnalyse");
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
	
	
	public void execute(String opsName,String day)  {
		
		
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
			
			DepIpInfoContain.get().clear();
			String logFilePath = Config.getValue("LOG_PATH");
			if(logFilePath == null){
				logger.error("��Ҫ��ʱ�ļ�Ŀ¼!");
				System.exit(-1);
			}
			
			String HOSTNAME = System.getenv("HOSTNAME");
			
			
			if(HOSTNAME == null){
				logger.error("��Ҫ����������!");
				System.exit(-1);
			}
			
			List<AppInfoPo> appList = new ArrayList<AppInfoPo>();
			if(opsName != null){
				AppInfoPo po = AppInfoAo.get().getAppInfoByOpsName(opsName);
				if(po != null){
					appList.add(po);
				}
			}
			
			for(AppInfoPo app:appList){
				if(app.getDayDeploy() ==1){
					try {
						createTmpDir(logFilePath,app.getOpsName());
						AppAnalyseInfo defaultAnalyseInfo = new AppAnalyseInfo();
						setDefaultAnalyseInfo(defaultAnalyseInfo,app.getOpsName());
						logger.info("��ʼ����Ӧ��:"+app.getOpsName());
						doScpLog(app,defaultAnalyseInfo,cal);
						doAnalyse(app.getOpsName(),defaultAnalyseInfo.getAnalyseList(),cal);
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
	
	
	private void doAnalyse(String appName,Set<String> analyseList,Calendar cal){
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
					a.setCollectTime(cal);
					a.analyseLogFile(rc);					
				}else{
					logger.error(analyseclass+" �Ǽ̳�Analyse");
				}
			}catch(Exception e){
				logger.error("����analyse ����",e);
			}
		}
	}
	
	
	private void doScpLog(AppInfoPo app,AppAnalyseInfo po,Calendar cal){	
		Map<String,List<HostPo>> map = OpsFreeHostCache.get().getHostMapNoCache(app.getOpsField(), app.getOpsName()) ;
		if(map==null||map.size()==0){
			logger.info(po.getOpsfreeName()+" �޷���ȡ��������IP");
			return ;
		}
		
		logger.info("��ʼ��ȡ"+po.getOpsfreeName()+" ��־");
		
		for(Map.Entry<String, List<HostPo>> entry:map.entrySet()){
			String sitename = entry.getKey();
			
			
			List<HostPo> list = entry.getValue();
			for(HostPo h:list){
				h.setUserName(app.getLoginName());
				h.setUserPassword(app.getLoginPassword());
			}
			
			long time = System.currentTimeMillis();
			ScpAcceptPool pool = new ScpAcceptPool(po,entry.getValue(),Config.getValue("LOG_PATH"),cal);
			pool.doScp();
			logger.info(po.getOpsfreeName()+" "+sitename+" ��������:"+entry.getValue().size()+" ʹ��ʱ��"+(System.currentTimeMillis()-time));
		}
		
		
	}
	


}
