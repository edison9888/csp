
package com.taobao.monitor.common.messagesend;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 ÏÂÎç04:02:22
 */
public abstract class MessageSendImpl implements MessageSend {
	
	private static final Logger logger =  Logger.getLogger(MessageSendImpl.class);
	
	private ResourceBundle bundle;
	
	private static final int MOVE_LIMIT = 5;
	
	public MessageSendImpl(){
		String propertyname= "report";
		Properties props = System.getProperties();
		Object systemname = props.get("os.name");
		if(systemname!=null){				
			String name = (String)systemname;
			if(name.toLowerCase().indexOf("window")>-1){
				propertyname = "report_windows";
			}				
		}
		bundle = ResourceBundle.getBundle(propertyname);
	}
	
	public String getValue(String key){
		return bundle.getString(key);
	}
	
	
	/**
	* <p>Description: </p>
	* @param url
	* @param parms
	* @return
	* byte[]
	* @author tom
	* @ÉÏÎç11:36:36 - 2010-5-12
	 */
	protected byte[] getBody(String url,Map parms){
	      byte[] body=null;
	      HttpClient httpClient = new HttpClient();
	      PostMethod postMethod = new PostMethod(url);
	      postMethod.getParams().setContentCharset("GBK");  
	      NameValuePair[] data = new NameValuePair[parms.keySet().size()]; 
	      Iterator it = parms.entrySet().iterator();
	      int i=0;
	      while (it.hasNext()) { 
	         Map.Entry entry = (Map.Entry) it.next(); 
	         Object key = entry.getKey(); 
	         Object value = entry.getValue(); 
	         data[i]=new NameValuePair(key.toString(),value.toString());
	         i++;
	      }
	      postMethod.setRequestBody(data);
	      try {
	         int statusCode = httpClient.executeMethod(postMethod);
//	         int reGet = 0;
//	         while (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
//	        	 reGet++;
//	        	 if (reGet > 5) {
//	        		 logger.warn("too many redirect!!!!!");
//	        		 break;
//	        	 }
//	        	 
//	            Header locationHeader = postMethod.getResponseHeader("location");
//	            String location = null;
//	            if (locationHeader != null) { 
//	               location = locationHeader.getValue(); 
//	               statusCode = httpClient.executeMethod(postMethod);	              
//	               logger.info("The page was redirected to:" + location+" :"+statusCode);
//	            } else {
//	            	logger.info("Location field value is null.");
//	            }
//	          }
	         
	         logger.info("message send status is: " + statusCode);
	         body = postMethod.getResponseBody();
	         logger.info("message body: " + body);
	      } catch (Exception e) {
	    	  logger.info("",e);
	      }
	      return body;
	   }

}
