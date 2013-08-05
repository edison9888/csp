package com.taobao.csp.capacity.tools;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class SomeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		HttpClient httpClient = new HttpClient();
		String url = "http://172.18.111.1/cgi-bin/ims_csp.py";
		
		GetMethod httpGet = new GetMethod(url);
		int httpStatus = httpClient.executeMethod(httpGet);
		
		StringBuffer contentBuffer = new StringBuffer();
		InputStream in = httpGet.getResponseBodyAsStream();
		byte[] byteA = new byte[1024];
		int size = 0;
		while ((size = in.read(byteA)) > 0) {
			contentBuffer.append(new String(byteA, 0, size, "GBK"));
		}

		String content = contentBuffer.toString();
		System.out.println(content);
		
		JSONArray array = JSONArray.fromObject(content);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String dateS = sf.format(calendar.getTime());
		
		String deleteSql = "delete from scp_capacity_ranking where app_type='ims' and ranking_date='" + dateS + "';";
		System.out.println(deleteSql);
		
		
		for (int i = 0; i < array.size(); i++) {
			String sql = "INSERT INTO scp_capacity_ranking VALUES (";
			JSONObject object = (JSONObject)array.get(i);
			
			String group_name = object.getString("groupName");
			String ops_name = object.getString("opsName");
			long single_load = object.getLong("single_load");
			long cluster_load = object.getLong("cluster_load");
			int rooms = object.getInt("rooms");
			int machine_num = object.getInt("machine_num");
			int single_cap = object.getInt("single_capacity");
			
			double level = (double)cluster_load * 100 / single_cap / machine_num;
			DecimalFormat df = new DecimalFormat("##.##");
			String levelS = df.format(level);
			
			int forestMachine = (int)Math.ceil((double)cluster_load / 0.4 / single_cap);
			int increaseOrDecrease = forestMachine - machine_num;
			
			int id = 0;
			if (group_name.equals("NORMAL_IMS48G")) {
				id = 645001;
			}
			if (group_name.equals("NORMAL_IMS24G")) {
				id = 645002;
			}
			if (group_name.equals("ECLIENT_IMS48G")) {
				id = 645003;
			}
			if (group_name.equals("ECLIENT_IMS24G")) {
				id = 645004;
			}
			
			
			sql = sql + id + ",'��������',";
			sql = sql + levelS + "," + single_load + "," + single_cap + ",201," + "'" + dateS + "',";
			sql = sql + "'��̨����ƽ��:10000|��̨����:" + single_load + "|�ɼ�ʱ��:1970-01-01 00:00|�ɼ�����:127.0.0.1|��Ⱥ����:" + cluster_load + "|��̨����:" + single_cap + "|ѹ��ʱ��:1970-01-01|��Ⱥ����:" +(single_cap*machine_num) + "|������:" + machine_num + "|������:2|����ˮλ:" +levelS + "%|������׼:40%|ҵ��������:0%|Ԥ�⼯Ⱥ����:" + cluster_load + "|Ԥ������ˮλ:" + levelS + "%|Ԥ�������:" + forestMachine + "|Ԥ���������:" + increaseOrDecrease + "|',";
			sql = sql + "'" + group_name + "',";
			sql = sql + "'" + ops_name + "');";
			
			System.out.println(sql);
		}

	}

}
