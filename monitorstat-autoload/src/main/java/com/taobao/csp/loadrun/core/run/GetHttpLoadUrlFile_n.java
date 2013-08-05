package com.taobao.csp.loadrun.core.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.HttpLoadLogType;
import com.taobao.csp.loadrun.core.shell.BaseShell;

/***
 * ssh方式采集http——load压测的url
 * @author youji.zj
 * @version 2012-07-05
 *
 */
public class GetHttpLoadUrlFile_n extends BaseShell implements IUrlCollector {
	
	private static final Logger logger = Logger.getLogger(GetHttpLoadUrlFile_n.class);

	private LoadrunTarget target;

	private BufferedWriter writer = null;

	private String type = null;
	

	private int v1 = 20;
	private int v2 = 22;

	private Pattern pattern = Pattern.compile("\\[(\\d{2}/\\w+/\\d{4}:\\d{2}:\\d{2}:\\d{2})\\s{1}\\+\\d+\\]");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
	
	
	private String shellCommand = null;

	public GetHttpLoadUrlFile_n(LoadrunTarget target) throws IOException {
		super(target.getTargetIp(), target.getTargetUserName(), target.getTargetPassword());
		
		String rush = target.getAppRushHours();
		if (rush != null) {
			String[] r = rush.split("-");
			try {
				v1 = Integer.valueOf(r[0]);
				v2 = Integer.valueOf(r[1]);
			} catch (Exception e) {
			}
		}
		
		this.target = target;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String d = sdf.format(cal.getTime());
		
		String path = "" + d + "-taobao-access_log";
		
		this.shellCommand = " awk -F ':' '{if($2 >="+v1+" && $2 <"+v2+"){print $0}}' " + path;
		
		if(HttpLoadLogType.apache.equals(target.getHttpLoadLogType())){
			path = "/home/admin/cai/logs/cronolog/" + d + "-taobao-access_log";
			
			this.shellCommand = " awk -F ':' '{if($2 >="+v1+" && $2 <"+v2+"){print $0}}' " + path;
			
		}
		if(HttpLoadLogType.nginx.equals(target.getHttpLoadLogType())){
			path = "/home/admin/cai/logs/cronolog/" + d + "-taobao-access_log";
			this.shellCommand = " awk -F ':' '{if($2 >="+v1+" && $2 <"+v2+"){print $0}}' " + path;
		}
		
		logger.info(this.shellCommand);
		
		this.type = target.getHttploadProxy();
		
	}

	@Override
	public String getFilePath() throws Exception {
		try {
			this.initSshConnect();

			String fileName = "load/apachelog/" + this.target.getAppName() + "-apache";

			File file = new File("load/apachelog/");
			
			System.out.println(file.getAbsolutePath());
			
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
					file.mkdirs();
				}
			} else {
				file.mkdirs();
			}

			writer = new BufferedWriter(new FileWriter(fileName));

			this.fetch();
			
			fileName = file.getAbsolutePath()+File.separator+this.target.getAppName() + "-apache";
			
			return fileName;
		} finally {
			if (writer != null)
				writer.close();
			close();
		}
	}
	
	private Date parseLogLineCollectTime(String logRecord) {

		Matcher m = pattern.matcher(logRecord);
		if (m.find()) {
			String time = m.group(1);
			try {
				Date date = sdf.parse(time);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private void fetch() throws IOException {
		
		if(HttpLoadLogType.self.equals(this.target.getHttpLoadLogType())){
			String urls = this.target.getHttpLoadLog();
			String[] tmp = StringUtils.split(urls, "@");
			for(String url:tmp){
				putFetchLine(url);
			}
			
		} else {
			if (this.getTargetSSHConn() != null) {
				Session session = null;
				session = this.getTargetSSHConn().openSession();
				session.execCommand(this.shellCommand);
				InputStream stdout = new StreamGobbler(session.getStdout());
				BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
				String line = null;
				while ((line = br.readLine()) != null) {
					putFetchLine(line);
				}
			}
		}
	}
	
	private String getApacheUrl(String line){
		Date time = parseLogLineCollectTime(line);
		if(time ==null){
			return null;
		}
		if (time.getHours() >= v1 && time.getHours() <= v2) {
			String[] tmp = line.split("\"");
			if (tmp.length >= 5) {
				String url_tmp = tmp[1];
				String stauts_tmp = tmp[2];
				String[] urls = url_tmp.trim().split(" ");
				String[] stauts = stauts_tmp.trim().split(" ");
				if (stauts.length == 2 && urls.length == 2) {
					String http_stauts = stauts[0];
					String http_method = urls[0];
					if ("200".equals(http_stauts.trim()) && "GET".equals(http_method.trim())) {
						String url = urls[1];
						if (url.indexOf("status.taobao") < 0) {
							if (this.type == null) {
								logger.error("http_load type is null:" + this.getTargetIp());
								return null;
							}
							if ("proxy".equals(this.type) || "directProxy".equals(this.type) || "ssl".equals(this.type)) {
								return url;
							} else if ("no".equals(this.type)) {
								String u = "http://" + this.getTargetIp() + ":7001" + url.substring(url.indexOf("/", 8));
								return u;
							} else {
								logger.error(this.getTargetIp() + "http_load type is wrong:" + line);
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private String getNginxUrl(String line){
		Date time = parseLogLineCollectTime(line);
		if(time ==null){
			return null;
		}
		if (time.getHours() >= v1 && time.getHours() <= v2) {
			String[] tmp = line.split("\"");
			if (tmp.length >= 5) {
				String url_tmp = tmp[1];
				String stauts_tmp = tmp[2];
				String[] urls = url_tmp.trim().split(" ");
				String[] stauts = stauts_tmp.trim().split(" ");
				if (stauts.length == 2 && urls.length == 2) {
					String http_stauts = stauts[0];
					String http_method = urls[0];
					if ("200".equals(http_stauts.trim()) && "GET".equals(http_method.trim())) {
						String url = urls[1];
						if (url.indexOf("status.taobao") < 0) {
							if (this.type == null) {
								logger.error("http_load type is null:" + this.getTargetIp());
								return null;
							}
							if ("proxy".equals(this.type) || "directProxy".equals(this.type) || "ssl".equals(this.type)) {
								return url;
							} else if ("no".equals(this.type)) {
								String u = "http://" + this.getTargetIp() + ":7001" + url.substring(url.indexOf("/", 8));
								return u;
							} else {
								logger.error(this.getTargetIp() + "http_load type is wrong:" + line);
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据 /home/admin/cai/logs/cronolog/2010/09/2010-09-01-taobao-access_log
	 * 获取apache 日志 ，获取当天的最近的
	 * 
	 * 222.89.32.194 14526 - [01/Sep/2010:18:30:00 +0800]
	 * "GET http://login.taobao.com:80/member/newbie.htm" 302 319 "-""Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; 360SE)"
	 * 
	 * @throws IOException
	 */

	private void putFetchLine(String line) {

		if (line.length() > 4000 || line.length() < 20) {
			return;
		}
		String u = null;
		if(HttpLoadLogType.apache.equals(this.target.getHttpLoadLogType())){
			u = getApacheUrl(line);
		}
		
		if(HttpLoadLogType.nginx.equals(this.target.getHttpLoadLogType())){
			u = getNginxUrl(line);
		}
		
		// 已经是直接的url了
		if(HttpLoadLogType.self.equals(this.target.getHttpLoadLogType())){
			if (this.type == null) {
				logger.error("http_load type is null:" + this.getTargetIp());
				return;
			}
			if ("proxy".equals(this.type) || "directProxy".equals(this.type) || "ssl".endsWith(this.type)) {
				u = line;
			} else if ("no".equals(this.type)) {
				u = "http://" + this.getTargetIp() + ":7001" + line.substring(line.indexOf("/", 8));
			}  else {
				logger.error(this.getTargetIp() + "http_load type is wrong:" + line);
				return;
			}
			
		}
		
		if(u != null){
			try {
				writer.write(u);
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		// LoadrunTarget target = new LoadrunTarget();
		// target.setTargetIp("10.232.35.9");
		// target.setTargetUserName("xiaodu");
		// target.setTargetPassword("Hello_123");
		// target.setLoadrunType(AutoLoadType.httpLoad);
		// target.setHttpLoadLogType(HttpLoadLogType.apache);
		// target.setAppRushHours("09-11");
		// try {
		// GetHttpLoadUrlFile_n n = new GetHttpLoadUrlFile_n(target);
		// n.getFilePath();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		String scriptUrl = "http://10.20.168.39" + ":8082/scriptexcute";
		String urlParameters = "token=dcbeb81d186a89a11c1515ced9022bca&script=get_cookie_log.sh&args=09,1,1";
		URL url = new URL(scriptUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);

		// send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		// Get Response
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}

	}

}
