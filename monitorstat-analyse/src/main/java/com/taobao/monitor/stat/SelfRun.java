package com.taobao.monitor.stat;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.stat.analyse.Analyse;
import com.taobao.monitor.stat.config.AppAnalyseInfo;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.util.Config;
import com.taobao.monitor.stat.util.DepIpInfoContain;

public class SelfRun  {
	private static final Logger logger =  Logger.getLogger(SelfRun.class);
	
	
	public static void main(String[] args){
		SelfRun gc = new SelfRun();
		if(args==null||args.length==0)
			gc.execute(null);
		else
			gc.execute(args[0]);
	}
	

	
	
	private void setDefaultAnalyseInfo(AppAnalyseInfo defaultAnalyseInfo,String opsName){
		defaultAnalyseInfo.setOpsfreeName(opsName);
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.SelfAnalyse");
	}
	
	
	public void execute(String opsName) {
		
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
			}else{
				appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
			}
			
			
			
			for(AppInfoPo app:appList){
				if(app.getDayDeploy() ==1){
					try {
						createTmpDir(logFilePath,app.getOpsName());
						AppAnalyseInfo defaultAnalyseInfo = new AppAnalyseInfo();
						setDefaultAnalyseInfo(defaultAnalyseInfo,app.getOpsName());
						logger.info("��ʼ����Ӧ��:"+app.getOpsName());
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
	
	
	

}
