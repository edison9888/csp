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
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.accept.ScpAcceptPool;
import com.taobao.monitor.stat.analyse.Analyse;
import com.taobao.monitor.stat.config.AppAnalyseInfo;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.util.Config;
import com.taobao.monitor.stat.util.DepIpInfoContain;

public class GCRun  {
	private static final Logger logger =  Logger.getLogger(GCRun.class);
	
	
	public static void main(String[] args){
		GCRun gc = new GCRun();
		if(args==null||args.length==0)
			gc.execute(null);
		else
			gc.execute(args[0]);
	}
	

	
	
	private void setDefaultAnalyseInfo(AppAnalyseInfo defaultAnalyseInfo,String opsName){
		defaultAnalyseInfo.setOpsfreeName(opsName);
		defaultAnalyseInfo.getAnalyseList().add("com.taobao.monitor.stat.analyse.GCLogAnalyse");
		defaultAnalyseInfo.getRemoteLogPath().add("/home/admin/logs/gc.log");
	}
	
	
	public void execute(String opsName) {
		
			DepIpInfoContain.get().clear();
			String logFilePath = Config.getValue("LOG_PATH");
			if(logFilePath == null){
				logger.error("需要临时文件目录!");
				System.exit(-1);
			}
			
			String HOSTNAME = System.getenv("HOSTNAME");
			
			
			if(HOSTNAME == null){
				logger.error("需要服务器名称!");
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
						logger.info("开始处理应用:"+app.getOpsName());
						doScpLog(app.getOpsField(),app.getOpsName(),defaultAnalyseInfo);
						doAnalyse(app.getOpsName(),defaultAnalyseInfo.getAnalyseList());
						logger.info("结束处理应用:"+app.getOpsName());
					} catch (Exception e1) {
						logger.error("分析"+app.getOpsName()+"出错",e1);
					}
				}else{
					logger.info(""+app.getOpsName()+"没有部署日报");
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
					logger.error(analyseclass+" 构造函数缺少");
					continue;	
				}
				
				Object obj = con.newInstance(appName);
				if(obj instanceof Analyse){
					Analyse a = (Analyse)obj;
					a.analyseLogFile(rc);
				}else{
					logger.error(analyseclass+" 非继承Analyse");
				}
			}catch(Exception e){
				logger.error("创建analyse 出错",e);
			}
		}
	}
	
	
	private void doScpLog(String opsField,String opsName,AppAnalyseInfo po){		
		Map<String,List<HostPo>> map = OpsFreeHostCache.get().getHostMapNoCache(opsField, opsName) ;
		if(map==null||map.size()==0){
			logger.info(po.getOpsfreeName()+" 无法获取机器数量IP");
			return ;
		}
		
		logger.info("开始获取"+po.getOpsfreeName()+" 日志");
		
		for(Map.Entry<String, List<HostPo>> entry:map.entrySet()){
			String sitename = entry.getKey();
			long time = System.currentTimeMillis();
			ScpAcceptPool pool = new ScpAcceptPool(po,entry.getValue(),Config.getValue("LOG_PATH"),null);
			pool.doScp();
			logger.info(po.getOpsfreeName()+" "+sitename+" 机器数量:"+entry.getValue().size()+" 使用时间"+(System.currentTimeMillis()-time));
		}
		
		
	}

	

}
