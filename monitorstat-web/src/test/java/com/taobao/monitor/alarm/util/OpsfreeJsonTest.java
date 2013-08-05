package com.taobao.monitor.alarm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import com.taobao.monitor.common.po.HSFProviderHosts;

public class OpsfreeJsonTest {
	@Test
	public void test_case1_获取tradeplatform的机器列表() throws Exception {
		String systemName = "tradeplatform";
		String opsfreeUrl = "http://opsfree2.corp.taobao.com:9999/api/v2.1/products/dumptree?_username=droid/csp&notree=1&leafname=";
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(opsfreeUrl + systemName);
		try {
			client.executeMethod(method);
		} catch (Exception e) {
			method.releaseConnection();
		}
		String origin = method.getResponseBodyAsString();
		System.out.println(analyseK2Json(origin));

	}

	/***
	 * from csp
	 * 
	 * @param result
	 * @return
	 */
	private HSFProviderHosts analyseK2Json(String result) {
		JSONArray object = JSONArray.fromObject(result);
		if (object.size() == 0) {
			return null;
		}
		JSONObject json = (JSONObject) object.get(0);
		Set set = json.keySet();
		for (Object j : set) {
			JSONArray array = (JSONArray) json.get(j);
			for (int i = 0; i < array.size(); i++) {
				JSONObject p = (JSONObject) array.get(i);
				JSONObject nodegroup_info = (JSONObject) p
						.get("nodegroup_info");
				JSONObject detail = (JSONObject) nodegroup_info.get("detail");
				if (detail == null)
					continue;
				// String nodegroup_name = detail.getString("nodegroup_name");
				JSONArray child = (JSONArray) p.get("child");
				if (child == null)
					continue;

				int totalCnt = 0;
				int cm3Cnt = 0;
				int cm4Cnt = 0;
				int cm5Cnt = 0;
				List<String> cm3List = new ArrayList<String>();
				List<String> cm4List = new ArrayList<String>();
				List<String> cm5List = new ArrayList<String>();
				for (int c = 0; c < child.size(); c++) {
					JSONObject childNode = (JSONObject) child.get(c);
					// 只处理在线状态的机器
					if (HSFProviderHosts.stateOnline.equalsIgnoreCase(childNode
							.optString("state"))) {
						totalCnt++;
						if (HSFProviderHosts.siteCm3.equalsIgnoreCase(childNode
								.optString("site"))){
							cm3Cnt++;
							cm3List.add(childNode.getString("dns_ip"));
						}else  if (HSFProviderHosts.siteCm4.equalsIgnoreCase(childNode
								.optString("site"))) {
							cm4Cnt++;
							cm4List.add(childNode.getString("dns_ip"));
						}else if (HSFProviderHosts.siteCm5.equalsIgnoreCase(childNode
								.optString("site"))) {
							cm5Cnt++;
							cm5List.add(childNode.getString("dns_ip"));
						}
					}
				}
				HSFProviderHosts hosts = new HSFProviderHosts(totalCnt,cm3Cnt,cm4Cnt,cm5Cnt,cm3List,cm4List,cm5List,"tradeplatform");
				return hosts;
			}
		}

		return null;
	}
}
