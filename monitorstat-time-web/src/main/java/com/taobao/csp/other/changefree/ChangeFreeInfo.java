
/**
 * monitorstat-time-web
 */
package com.taobao.csp.other.changefree;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * �����ȡchangefree ����¼���Ϣ
 * 
 ���� http://changefree.corp.taobao.com/v2_changefree/app/index.php/outer/get_change_free_data

 �ճ� changefree.taobao.net/v2_changefree/app/index.php/outer/get_change_free_data
 post ����Ϊ:'starttime':'2012-03-20 10:23:00','endtime':'2012-03-25 01:23:00'
 id:�����ID, 
title:����,
change_programe":����Ű�,
change_device_name":�����豸
validate_method":��֤����,
change_reason:���ԭ��,
change_type:�������,
product_line":",
username:�����˻���,
start_time:ʵ�ʿ�ʼʱ��,
end_time:ʵ�ʽ���ʱ��
 * 
 * @author xiaodu
 *
 * ����11:25:05
 */
public class ChangeFreeInfo {
	
	private static final Logger logger =  Logger.getLogger(ChangeFreeInfo.class);
	
	public String changefreeUrl ="http://changefree.corp.taobao.com/v2_changefree/app/index.php/outer/get_change_free_data";
	
	private static ChangeFreeInfo changeFreeInfo = new ChangeFreeInfo();
	
	private ChangeFreeInfo(){
		
	}
	
	public static ChangeFreeInfo get(){
		return changeFreeInfo;
	}
	/**
	 * ��ȡ�������ĸ����¼�
	 *@author xiaodu
	 * @return
	 *TODO
	 */
	public List<ChangeFree> getRecentlyChangeFree(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Calendar cal = Calendar.getInstance();
		
		
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		String endtime = sdf.format(cal.getTime());
		
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		String starttime = sdf.format(cal.getTime());
		
		
		
		
		
		return handleFree(starttime,endtime);
	}
	
	
	private List<ChangeFree> handleFree(String starttime,String endtime){
		
		List<ChangeFree> list = new ArrayList<ChangeFree>();
		
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(changefreeUrl);
		
		post.addParameter("starttime", starttime);
		post.addParameter("endtime", endtime);
		
		try {
			int code = client.executeMethod(post);
			
			if(code == org.apache.commons.httpclient.HttpStatus.SC_OK){
				String body = post.getResponseBodyAsString();
				JSONArray object = JSONArray.fromObject(body);
				for(int i=0;i<object.size();i++){
					JSONObject obj = object.getJSONObject(i);
					ChangeFree cf = (ChangeFree)JSONObject.toBean(obj, ChangeFree.class);
					list.add(cf);
				}
			}
			
		} catch (Exception e) {
			logger.error("getCurrentChangeFree error", e);
		}
		return list;
	}
	
	
	
	

}
