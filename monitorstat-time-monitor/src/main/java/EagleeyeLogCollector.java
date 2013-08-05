import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;


/**
 * monitorstat-time-monitor
 */

/**
 * @author xiaodu
 *
 * 下午2:17:34
 */
public class EagleeyeLogCollector  {
	
	
	
	
	/**
	 *@author xiaodu
	 * @param args
	 *TODO
	 */
	public static void main(String[] args) {
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		Connection conn = new Connection("172.24.208.7");
		try{
		conn.connect(null,2000,2000);
		boolean isAuthenticated = conn.authenticateWithPassword( "xiaodu", "Hello_123");
		if(!isAuthenticated){
			System.exit(-1);
		}
		}catch (Exception e) {
		}
		
		while(true){
			
			System.out.println("开始采集");
			long t = System.currentTimeMillis();
			try {
				List<AppInfoPo> list = AppInfoAo.get().findAllEffectiveAppInfo();
				
				for(AppInfoPo apppo:list){
					String app = apppo.getAppName();
					System.out.println("开始采集"+app);
					List<HostPo> hostlist = CspCacheTBHostInfos.get().getHostInfoListByOpsName(app);
					if(app.indexOf("tair")>0){
						continue;
					}
					
					if(hostlist==null||hostlist.size()==0){
						continue;
					}
					for(HostPo po:hostlist){
						String ip = po.getHostIp();
						GetMethod get = new GetMethod("http://"+ip+":8082/tail/home/admin/.eagleeye/eagleeye.log?task_id=321129&encode=text");
						get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
						try{
							int code = httpClient.executeMethod(get);
							if(code == 200){
								SCPClient scp =conn.createSCPClient();
								InputStream in = get.getResponseBodyAsStream();
								ByteArrayOutputStream outstream = new ByteArrayOutputStream(10240);
								 byte[] buffer = new byte[4096];
					                int len;
					                while ((len = in.read(buffer)) > 0) {
					                    outstream.write(buffer, 0, len);
					                }
					                outstream.close(); 
					                if(outstream.size()>0)//eagle_xxx.txt
					                	scp.put(outstream.toByteArray(), "eagle_"+ip+""+t+".txt", "/var/jiuren","0777");
							}
						}catch (Exception e) {
							e.printStackTrace();
						}finally{
							get.releaseConnection();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("结束采集耗时"+(System.currentTimeMillis()-t)+" 。。等待一分钟");
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
			}
			
		}
	}


}
