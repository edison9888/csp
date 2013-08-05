
/**
 * monitorstat-depend-web
 */
package com.taobao.csp.depend.web.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import com.taobao.csp.depend.dao.UrlDao;
import com.taobao.csp.depend.po.url.UrlUv;

/**
 * @author xiaodu
 *
 * 下午1:43:43
 */

@Controller
@RequestMapping("/fetch/data.do")
public class HadoopDataFetchAction {

	private static Logger logger = Logger.getLogger(HadoopDataFetchAction.class);

	@Resource(name = "urlDao")
	private UrlDao urlDao;	
	/**
	 * 在gateway上的/u01/xiaodu/report/domain_url_uv 目录下会有一个执行hive的脚本run.sh
	 * 这个脚本在执行完hive sql 后会调用这个请求，触发获取结果数据的操作
	 *@author xiaodu
	 * @param date
	 *TODO
	 */
	@RequestMapping(params = "method=fetchUv")
	public void fetchUvData(String date,HttpServletResponse response){
		Connection conn = new Connection("172.24.208.7");//这个是gateway上的ip
		try{

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d = sdf.parse(date);

			conn.connect(null,2000,2000);
			boolean isAuthenticated = conn.authenticateWithPassword("xiaodu", "Hello_123");
			if(!isAuthenticated){
				return ;
			}

			SCPClient client = conn.createSCPClient();
			client.get("/u01/xiaodu/report/domain_url_uv/result/"+date+".domain_uv_hive", "/tmp");

			parseURL(d, "/tmp/"+date+".domain_uv_hive","domain");

			client = conn.createSCPClient();
			client.get("/u01/xiaodu/report/domain_url_uv/result/"+date+".url_uv_hive", "/tmp");

			parseURL(d, "/tmp/"+date+".url_uv_hive","url");

			response.getWriter().write(date+"ok");
			response.flushBuffer();
			return ;
		}catch (Exception e) {
			logger.error("", e);
		}

		try {
			response.getWriter().write(date+"fail");
			response.flushBuffer();
			return ;
		} catch (IOException e) {
			logger.error("", e);
		}

	}




	private void parseURL(Date date,String filePath,String type){
		File file =  new File(filePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line =reader.readLine())!= null){
				String[] tmp = StringUtils.split(line,"\01");

				if(tmp.length !=3){
					continue;
				}

				String url = tmp[0];
				String uv = tmp[1];
				String ipv =  tmp[2];

				UrlUv obj = new UrlUv();
				obj.setUrl(url);
				obj.setUv(Integer.parseInt(uv));
				obj.setIpv(Integer.parseInt(ipv));
				obj.setUrlType(type);
				obj.setCollectTime(date);
				urlDao.addUrlUv(obj);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		file.delete();
	}


}
