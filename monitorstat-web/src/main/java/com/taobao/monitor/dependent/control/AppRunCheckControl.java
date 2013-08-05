package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 上午09:35:26
 */
public class AppRunCheckControl {
	
	
	private static final Logger logger =  Logger.getLogger(AppRunCheckControl.class);
	

	private String scriptId;

	private HttpClient httpClient = new HttpClient();
	
	
	private boolean runScriptResult = false;
	
	private String resultMsg = null;
	
	
	

	public AppRunCheckControl(String scriptId) {
		this.scriptId = scriptId;
	}
	
	public String getResult(){
		return resultMsg;
	}

	public boolean runCheck() {
		
		Map<String,String> parms = new HashMap<String, String>();
		parms.put("authenticity_token", "N9CK488wArGlLjfPlVQTSZFP8HcHyQ2fMrU3M51m1R8=");
		parms.put("suite_id", this.scriptId);
		parms.put("target", "return_id");
		parms.put("exec_ip", "10.232.28.17");
		parms.put("is_update_script", "1");
		
		PostMethod postMethod = new PostMethod("http://automan.taobao.net:3004/pc_job_suites/"+this.scriptId+"/create_job_suite");
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		NameValuePair[] data = new NameValuePair[parms.size()]; 
		int i=0;
		for(Map.Entry<String,String> entry:parms.entrySet()){
			Object key = entry.getKey(); 
	         Object value = entry.getValue(); 
	         data[i]=new NameValuePair(key.toString(),value.toString());
	         i++;
		}
		postMethod.setRequestBody(data);
		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY){
				 Header locationHeader = postMethod.getResponseHeader("location");
				 String location = locationHeader.getValue(); 
				 Check thread = new Check(location);
				 thread.start();
			}
			synchronized (httpClient) {
				httpClient.wait();
			}
		} catch (Exception e) {
			
		} finally {
			postMethod.releaseConnection();
		}

		return runScriptResult;
	}

	private class Check extends Thread {
		
		private String url = null;
		
		public Check(String url){
			this.url = url;
		}
		
		

//		<tr>
//        <td role="title" width="100" title="展示全部类目按钮校验">展示全部类目按钮校验</td>
//        <td role="status" width="20"><a target="_blank" href="http://automan.taobao.net/pc_jobs/logs/38683?caseId=29945">
//            <span style="color: red;">失败</span></a>
//    
//    
//        </td>
//      </tr>

		
		
		private Pattern scriptId = Pattern.compile("<td role=\"jobid\" name=\"scriptid_(\\d*)\">");
		
		
		private List<String> getScriptIds(String msg){
			List<String> list = new ArrayList<String>();
			Matcher m = scriptId.matcher(msg);
			while(m.find()){
				list.add(m.group(1));
			}
			return list;
		}
		
		private String[] getScriptResult(String scriptId,String msg){
			String patternStr = "<tr>\\s*<td\\s*role=\"title\"\\s*width\\=\"100\"\\s*title\\=\"([\u4e00-\u9fa5]*)\">\\s*([\u4e00-\u9fa5]*)\\s*</td>\\s*<td\\s*role=\"status\"\\s*width=\"20\">\\s*<a\\s*target\\=\"_blank\"\\s*href\\=\"http://automan.taobao.net/pc_jobs/logs/"+scriptId+"\\?caseId\\=\\d*\">\\s*(.*)\\s*</a>\\s*</td>\\s*</tr>";
			Pattern rePattern = Pattern.compile(patternStr);
			Matcher m = rePattern.matcher(msg);
			String[] s = new String[2];
			if(m.find()){
				String r1 = m.group(1);
				String r2 = m.group(2);
				String r3 = m.group(3);
				
				s[0] = r1;
				s[1] = r3;
			}
			return s;
		}
		
		
		public void run() {

			while (true) {

				GetMethod getMethod = new GetMethod(this.url);
				getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
				try {
					// 执行getMethod
					int statusCode = httpClient.executeMethod(getMethod);
					if ( statusCode == HttpStatus.SC_OK) {
					
						byte[] responseBody = getMethod.getResponseBody();
						String result = (new String(responseBody,"UTF-8"));
						List<String> scriptIds = getScriptIds(result);
						
						int reNum = 0;
						boolean runResult = true;
						for(String str:scriptIds){
							String[] r = getScriptResult(str,result);
							
							if(r[1]!=null){
								if(r[1].indexOf("失败")>-1){
									runResult &= false;
									reNum++;
								}
								if(r[1].indexOf("成功")>-1){
									runResult &= true;
									reNum++;
								}
							}
						}
						
						if(scriptIds.size()!=0&&reNum == scriptIds.size()){
							AppRunCheckControl.this.runScriptResult = runResult;
							break;
						}
						
						logger.info("等待"+this.url+"执行结果....");
						
					}else{
						logger.error(this.url+" 执行出错 httpcode:"+statusCode);
					}
					
					
				} catch (HttpException e) {
				} catch (IOException e) {
				} finally {
					getMethod.releaseConnection();
				}
				
				try {
					Thread.sleep(30*1000);
				} catch (InterruptedException e) {
				}
				
			}
			synchronized (httpClient) {
				httpClient.notifyAll();
			}
		}

	}
	
	public static void main(String[] args){
		
//		String msg = "<input type=\"checkbox\" value=\"38682\" name=\"jobs[]\" id=\"38682\" checked=\"checked\" />";
//		List<String> list = new ArrayList<String>();
//		Pattern scriptId = Pattern.compile("<input\\s*type=\"checkbox\"\\s*value=\"(\\d*)\"\\s*name=\".*\"\\s*id=\"\\1\"\\s*checked=\"checked\"\\s*\\/>");
//		Matcher m = scriptId.matcher(msg);
//		while(m.find()){
//			list.add(m.group(1));
//			System.out.println(m.group(1));
//		}
		
		AppRunCheckControl control = new AppRunCheckControl("176");
		control.runCheck();
		
	}

}
