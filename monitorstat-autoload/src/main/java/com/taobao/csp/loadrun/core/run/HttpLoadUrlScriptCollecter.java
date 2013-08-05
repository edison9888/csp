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

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * 脚本方式采集http_load压测所需要的url
 * @author youji.zj
 * @version 2012-07-05
 */
public class HttpLoadUrlScriptCollecter implements IUrlCollector {
	
	public static Logger logger = Logger.getLogger(HttpLoadUrlScriptCollecter.class);

	private LoadrunTarget target;
	
	private BufferedWriter writer = null;
	
	public HttpLoadUrlScriptCollecter(LoadrunTarget target) {
		this.target = target;
	}
	
	
	@Override
	public String getFilePath() throws Exception {
		try {

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
		}
	}
	
	private void fetch() throws IOException {

		String command = this.target.getHttpLoadLog(); // 采集脚本存在这个字段
		if (command == null || command.length() == 0
				|| command.split("\\?").length != 2)
			return;
		String[] scriptArr = command.trim().split("\\?");
		String scriptUrl = scriptArr[0];
		String urlParameters = scriptArr[1];

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
		while ((line = rd.readLine()) != null) {
			putFetchLine(line);
		}
		rd.close();
	}
	
	private void putFetchLine(String line) {
		if (line.length() > 4000 || line.length() < 20) {
			return;
		}
		
		try {
			writer.write(line);
			writer.newLine();
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
