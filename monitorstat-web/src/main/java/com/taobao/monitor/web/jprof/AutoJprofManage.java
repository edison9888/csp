
package com.taobao.monitor.web.jprof;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.web.ao.MonitorJprofAo;
import com.taobao.monitor.web.core.po.JprofHost;
import com.taobao.monitor.common.po.AppInfoPo;


/**
 * 
 * @author xiaodu
 * @version 2010-8-10 下午04:17:03
 */
public class AutoJprofManage {	
	public static AutoJprofManage manage = new AutoJprofManage();
	
	private AutoJprofManage(){}
	
	public static AutoJprofManage get(){
		return manage;
	}
	
	private static final Logger logger =  Logger.getLogger(AutoJprofManage.class);
	
	
	private String realPath ;
	
	
	
	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	private List<JprofHost> findAllJprofHosts(){
		return MonitorJprofAo.get().findAllJprofHosts();
	}
	
	
	public String getUploadCollectDay(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	public String getAutoCollectDay(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
	public void doAutoCollectJprofile(){
		
		List<JprofHost> jprofHosts = findAllJprofHosts();
		logger.info("jprof host size:"+jprofHosts.size());
		List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
		Map<String,Integer> mapApp = new  HashMap<String, Integer>();
		for(AppInfoPo app :appList){
			mapApp.put(app.getAppName(), app.getAppId());
		}
		for(JprofHost host:jprofHosts){			
			try {
				
				logger.info("start jprof :"+host.getAppName()+":"+host.getHostIp()+":"+host.getFilePath());
				
				ScpAccept scp = new ScpAccept(host);
				scp.getRemoteFile(host.getFilePath(), getLocalTmpPath(host,getAutoCollectDay()));
				
				JprofSort sort = new JprofSort();
				sort.intoDb(this.getLocalTmpPath(host,getAutoCollectDay()), host,getAutoCollectDay());
				
				JprofStack srack = new JprofStack();
				srack.intoDb(this.getLocalTmpPath(host,getAutoCollectDay()), host,getAutoCollectDay());
				
				logger.info("end jprof :"+host.getAppName()+":"+host.getHostIp()+":"+host.getFilePath());
				
			} catch (Exception e) {
				logger.error("", e);
			}
		}		
	}
		
	
	public String getLocalTmpPath(JprofHost host,String collectDay){
		String p = getRealPath()+"/jprof/upload/"+host.getAppName()+"/jprof_"+collectDay+".txt";
		createLocalDir(p);
		return p;
	}
	
	
	
	/**
	  * 创建本地 目录
	  * @param localFile
	  */
	 private void createLocalDir(String localFile){
		 File file = new File(localFile);
		 if(!file.exists()){
			 String path = file.getParent();
			 File filepath = new File(path);
			 if(!filepath.exists()){
				 filepath.mkdirs();
			 }else{				 
				 if(!filepath.isDirectory()){
					 file.delete();
					 filepath.mkdirs();
				 }
				 
			 }			 
		 }		 
	 }
	
	
	
	
	public void doParseJprofile(JprofHost host){
		JprofSort sort = new JprofSort();
		sort.intoDb(this.getLocalTmpPath(host,getUploadCollectDay()), host,getUploadCollectDay());
		
		JprofStack srack = new JprofStack();
		srack.intoDb(this.getLocalTmpPath(host,getUploadCollectDay()), host,getUploadCollectDay());		
	}
	
	
	public static void main(String[] args){
		
	}
	

}
