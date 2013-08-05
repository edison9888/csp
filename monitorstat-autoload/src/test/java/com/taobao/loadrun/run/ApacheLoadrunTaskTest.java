
package com.taobao.loadrun.run;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.csp.loadrun.core.run.ApacheLoadrunTask;
import com.taobao.loadrun.BaseTest;


/**
 * 
 * @author xiaodu
 * @version 2011-6-29 ÏÂÎç01:48:43
 */
public class ApacheLoadrunTaskTest extends BaseTest{
	
	
	private ApacheLoadrunTask task = null;
	
	
	private String default_config = 
		"LoadModule jk_module /opt/taobao/install/httpd/modules/mod_jk.so\n" +
		"" +
		"JkWorkerProperty worker.list=local\n" +
		"JkWorkerProperty worker.local.type=ajp13\n" +
		"JkWorkerProperty worker.local.host=localhost\n" +
		"JkWorkerProperty worker.local.port=8009\n" +
		"JkWorkerProperty worker.local.socket_timeout=30\n" +
		"JkWorkerProperty worker.local.socket_keepalive=1\n" +
		"JkWorkerProperty worker.local.recycle_timeout=15\n" +
		"JkMountCopy  All\n" +
		"JkLogFile /home/admin/hesper/logs/hesper_jk.log\n" +
		"#JkLogLevel debug\n" +
		"JkLogLevel warn\n" +
		"JkOptions +ForwardURICompat\n" +
		"JkMount /*.htm local\n" +
		"JkMount /*.jhtml local\n" +
		"JkMount /*.esi local\n" +
		"JkMount /*.vhtml local\n" +
		"JkMount /*.do local\n" +
		"JkMount /*.jsp local\n" +
		"JkMount /*.html local\n";
	
	
	private String config_4_4 = 
		"LoadModule jk_module /opt/taobao/install/httpd/modules/mod_jk.so\n" +
		"" +
		"JkWorkerProperty worker.list=local\n" +
		"JkWorkerProperty worker.local.type=ajp13\n" +
		"JkWorkerProperty worker.local.host=${TargetIp}\n" +
		"JkWorkerProperty worker.local.port=8009\n" +
		"JkWorkerProperty worker.local.socket_timeout=30\n" +
		"JkWorkerProperty worker.local.socket_keepalive=1\n" +
		"JkWorkerProperty worker.local.recycle_timeout=15\n" +
		"JkMountCopy  All\n" +
		"JkLogFile /home/admin/hesper/logs/hesper_jk.log\n" +
		"#JkLogLevel debug\n" +
		"JkLogLevel warn\n" +
		"JkOptions +ForwardURICompat\n" +
		"JkMount /*.htm local\n" +
		"JkMount /*.jhtml local\n" +
		"JkMount /*.esi local\n" +
		"JkMount /*.vhtml local\n" +
		"JkMount /*.do local\n" +
		"JkMount /*.jsp local\n" +
		"JkMount /*.html local\n";
	
	private String config_3_4 = 
		"LoadModule jk_module /opt/taobao/install/httpd/modules/mod_jk.so\n" +
		"JkWorkerProperty worker.list=local,target,lb\n" +
		"JkWorkerProperty worker.local.type=ajp13\n" +
		"JkWorkerProperty worker.local.host=localhost\n" +
		"JkWorkerProperty worker.local.port=8009\n" +
		"JkWorkerProperty worker.local.socket_timeout=30\n" +
		"JkWorkerProperty worker.local.socket_keepalive=1\n" +
		"JkWorkerProperty worker.local.recycle_timeout=15\n" +
		"JkWorkerProperty worker.local.lbfactor=1\n" +
		"JkWorkerProperty worker.target.type=ajp13\n" +
		"JkWorkerProperty worker.target.host=${TargetIp}\n" +
		"JkWorkerProperty worker.target.port=8009\n" +
		"JkWorkerProperty worker.target.socket_timeout=30\n" +
		"JkWorkerProperty worker.target.socket_keepalive=1\n" +
		"JkWorkerProperty worker.target.recycle_timeout=15\n" +
		"JkWorkerProperty worker.target.lbfactor=3\n" +
		"" +
		"JkWorkerProperty worker.lb.type=lb\n" +
		"JkWorkerProperty worker.lb.balance_workers=local,target\n" +
		"JkLogFile /home/admin/cai/logs/itemmanager_jk.log\n" +
		"JkLogLevel warn \n" +
		"JkOptions +ForwardURICompat\n"+ 
		"JkMount /*.htm local\n" +
		"JkMount /*.jhtml local\n" +
		"JkMount /*.esi local\n" +
		"JkMount /*.vhtml local\n" +
		"JkMount /*.do local\n" +
		"JkMount /*.jsp local\n" +
		"JkMount /*.html local\n";
	
	private String config_2_4 = 
		"LoadModule jk_module /opt/taobao/install/httpd/modules/mod_jk.so\n" +
		"JkWorkerProperty worker.list=local,target,lb\n" +
		"JkWorkerProperty worker.local.type=ajp13\n" +
		"JkWorkerProperty worker.local.host=localhost\n" +
		"JkWorkerProperty worker.local.port=8009\n" +
		"JkWorkerProperty worker.local.socket_timeout=30\n" +
		"JkWorkerProperty worker.local.socket_keepalive=1\n" +
		"JkWorkerProperty worker.local.recycle_timeout=15\n" +
		"JkWorkerProperty worker.local.lbfactor=2\n" +
		"JkWorkerProperty worker.target.type=ajp13\n" +
		"JkWorkerProperty worker.target.host=${TargetIp}\n" +
		"JkWorkerProperty worker.target.port=8009\n" +
		"JkWorkerProperty worker.target.socket_timeout=30\n" +
		"JkWorkerProperty worker.target.socket_keepalive=1\n" +
		"JkWorkerProperty worker.target.recycle_timeout=15\n" +
		"JkWorkerProperty worker.target.lbfactor=2\n" +
		"" +
		"JkWorkerProperty worker.lb.type=lb\n" +
		"JkWorkerProperty worker.lb.balance_workers=local,target\n" +
		"JkLogFile /home/admin/cai/logs/itemmanager_jk.log\n" +
		"JkLogLevel warn \n" +
		"JkOptions +ForwardURICompat\n"+ 
		"JkMount /*.htm local\n" +
		"JkMount /*.jhtml local\n" +
		"JkMount /*.esi local\n" +
		"JkMount /*.vhtml local\n" +
		"JkMount /*.do local\n" +
		"JkMount /*.jsp local\n" +
		"JkMount /*.html local\n";
	
	private String config_1_4 = 
		"LoadModule jk_module /opt/taobao/install/httpd/modules/mod_jk.so\n" +
		"JkWorkerProperty worker.list=local,target,lb\n" +
		"JkWorkerProperty worker.local.type=ajp13\n" +
		"JkWorkerProperty worker.local.host=localhost\n" +
		"JkWorkerProperty worker.local.port=8009\n" +
		"JkWorkerProperty worker.local.socket_timeout=30\n" +
		"JkWorkerProperty worker.local.socket_keepalive=1\n" +
		"JkWorkerProperty worker.local.recycle_timeout=15\n" +
		"JkWorkerProperty worker.local.lbfactor=3\n" +
		"JkWorkerProperty worker.target.type=ajp13\n" +
		"JkWorkerProperty worker.target.host=${TargetIp}\n" +
		"JkWorkerProperty worker.target.port=8009\n" +
		"JkWorkerProperty worker.target.socket_timeout=30\n" +
		"JkWorkerProperty worker.target.socket_keepalive=1\n" +
		"JkWorkerProperty worker.target.recycle_timeout=15\n" +
		"JkWorkerProperty worker.target.lbfactor=1\n" +
		"" +
		"JkWorkerProperty worker.lb.type=lb\n" +
		"JkWorkerProperty worker.lb.balance_workers=local,target\n" +
		"JkLogFile /home/admin/cai/logs/itemmanager_jk.log\n" +
		"JkLogLevel warn \n" +
		"JkOptions +ForwardURICompat\n"+ 
		"JkMount /*.htm local\n" +
		"JkMount /*.jhtml local\n" +
		"JkMount /*.esi local\n" +
		"JkMount /*.vhtml local\n" +
		"JkMount /*.do local\n" +
		"JkMount /*.jsp local\n" +
		"JkMount /*.html local\n";
	
	
	@Before
	public void init(){
		
		
		
		
		target.setLoadFeature("10.232.15.173,10.232.15.173,10.232.15.173,10.232.15.173");
		
		
		
		target.setApacheBinPath("/home/admin/hesper/bin/apachectl");
		target.setJkConfigPath("/home/admin/hesper/conf/");
		
		
		try {
			task = new ApacheLoadrunTask(this.target);
			
			task.runFetchs();
			
			task.runAutoControl();
			
			task.waitForStop();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void getResult(){
		
		Assert.assertEquals(task.getLoadResult().size(),0);
		
	}
	
	

}
