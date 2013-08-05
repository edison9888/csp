package com.taobao.monitor.alarm.alipay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.time.util.TimeUtil;
import com.taobao.util.CollectionUtil;

public class AlipayBizAo {
	private static AlipayBizAo ao = new AlipayBizAo();
	public static AlipayBizAo get(){
		return ao;
	}
	
	private static final Logger logger = Logger.getLogger(AlipayBizAo.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	String url = "http://monitorstbweb.alipay.com/monitor4/mashup.json?method=queryDataByKey";

	String FIELD_CONTACT = ",";
	String CREATE_CONSTANTS = "create_y" + FIELD_CONTACT + "create_all" + FIELD_CONTACT + "create_time";
	String PAY_CONSTANTS = "pay_y" + FIELD_CONTACT + "pay_all" + FIELD_CONTACT + "pay_time";
	String SEND_CONSTANTS = "send_y" + FIELD_CONTACT + "send_all" + FIELD_CONTACT + "send_time";
	String CONFIRM_CONSTANTS = "confirm_y" + FIELD_CONTACT + "confirm_all" + FIELD_CONTACT + "confirm_time";
	String fields = CREATE_CONSTANTS + FIELD_CONTACT + PAY_CONSTANTS + FIELD_CONTACT + SEND_CONSTANTS + FIELD_CONTACT + CONFIRM_CONSTANTS;

	String descriptions = "$||$||$||$||$||$||$||$||$||$||$||$";

	String expressions = "$";

	//key�ĸ�ʽ�̶���ȷ���������ݵ�˳��
	String key = "{8^[f2,t0,t1,t2]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=create,t2=Y,;||{8^[f2,t0,t1]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=create,;||{{8^[f2,t0,t1]}{8^[f2,t0,t1]}}/{CUSTOM_#{k0}/#{k1}}{{SUM}{t0}{SUM}{f0}}/f2=�Ա�����,t0=TradeFacade,t1=create,;f2=�Ա�����,t0=TradeFacade,t1=create,;||{8^[f2,t0,t1,t2]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=pay,t2=Y,;||{8^[f2,t0,t1]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=pay,;||{{8^[f2,t0,t1]}{8^[f2,t0,t1]}}/{CUSTOM_#{k0}/#{k1}}{{SUM}{t0}{SUM}{f0}}/f2=�Ա�����,t0=TradeFacade,t1=pay,;f2=�Ա�����,t0=TradeFacade,t1=pay,;||{8^[f2,t0,t1,t2]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=sendGoods,t2=Y,;||{8^[f2,t0,t1]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=sendGoods,;||{{8^[f2,t0,t1]}{8^[f2,t0,t1]}}/{CUSTOM_#{k0}/#{k1}}{{SUM}{t0}{SUM}{f0}}/f2=�Ա�����,t0=TradeFacade,t1=sendGoods,;f2=�Ա�����,t0=TradeFacade,t1=sendGoods,;||{8^[f2,t0,t1,t2]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=confirmReceiveGoods,t2=Y,;||{8^[f2,t0,t1]}/{SUM}{f0}/f2=�Ա�����,t0=TradeFacade,t1=confirmReceiveGoods,;||{{8^[f2,t0,t1]}{8^[f2,t0,t1]}}/{CUSTOM_#{k0}/#{k1}}{{SUM}{t0}{SUM}{f0}}/f2=�Ա�����,t0=TradeFacade,t1=confirmReceiveGoods,;f2=�Ա�����,t0=TradeFacade,t1=confirmReceiveGoods,;";

	public Map<Date, AlipayBizPo> getAlipayBizPoByTime(Date start, Date end) {
		StringBuilder sb = new StringBuilder(url);
		try {
			sb.append("&").append("key=" + URLEncoder.encode(key, "gbk")).append("&startTime=").append(URLEncoder.encode(format.format(start), "gbk"))
			.append("&endTime=").append(URLEncoder.encode(format.format(end), "gbk")).append("&").append("descriptions=" + URLEncoder.encode(descriptions, "gbk")).append("&")
			.append("fields=" + URLEncoder.encode(fields, "gbk")).append("&").append("expressions=" + URLEncoder.encode(expressions, "gbk"));
		} catch (UnsupportedEncodingException e) {
			logger.warn("getAlipayBizPoByTime urlEncode exception,url=" + url + ",start=" + start + ",end=" + end,e);
		}
		
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(sb.toString());
		
		try {
			// ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
			getMethod.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
			getMethod.addRequestHeader("Accept", "text/plain");
			//���ó�ʱʱ��
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		    httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);

			
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response = getMethod.getResponseBodyAsString();// ��ȡ����
				JSONObject jsonObject = JSONObject.fromObject(response);

				JSONObject data = JSONObject.fromObject(jsonObject.get("datas"));
				Map<Date, AlipayBizPo> map = new HashMap<Date, AlipayBizPo>();
				Calendar c = Calendar.getInstance();
				for (Object key : data.keySet()) {
					JSONArray jsonArray = JSONArray.fromObject(data.get(key));
					AlipayBizPo alipayBizDo = getAlipayBizPoFromJsonArray(jsonArray);
					if (alipayBizDo == null) {
						continue;
					}
					c.setTimeInMillis(Long.parseLong(String.valueOf(key)));
					alipayBizDo.setTime(c.getTime());
					map.put(c.getTime(), alipayBizDo);
				}
				return map;
			} else {
				logger.warn("getAlipayBizPoByTime statusCode=" + statusCode + ",url=" +sb.toString());
			}
		} catch (Exception e) {
			logger.warn("getAlipayBizPoByTime exception,url=" + sb.toString(),e);
		}finally{
			getMethod.releaseConnection();
		}
		return null;
	}

	/**
	 * ֧�������ص�����˳�����ʽȷ�������ֱ��д�ɹ̶���
	 * @param jsonArray
	 * @return
	 */
	private AlipayBizPo getAlipayBizPoFromJsonArray(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.size(); i++) {
			if (Float.parseFloat(jsonArray.get(i).toString()) == 0) {
				return null;
			}
		}
		AlipayBizPo alipayBizDo = new AlipayBizPo();
		alipayBizDo.setCreateSuccess(Integer.parseInt(jsonArray.get(0).toString()));
		alipayBizDo.setCreateTotal(Integer.parseInt(jsonArray.get(1).toString()));
		alipayBizDo.setCreateTime(Float.parseFloat(jsonArray.get(2).toString()));
		
		alipayBizDo.setPaySuccess(Integer.parseInt(jsonArray.get(3).toString()));
		alipayBizDo.setPayTotal(Integer.parseInt(jsonArray.get(4).toString()));
		alipayBizDo.setPayTime(Float.parseFloat(jsonArray.get(5).toString()));
		
		alipayBizDo.setSendSuccess(Integer.parseInt(jsonArray.get(6).toString()));
		alipayBizDo.setSendTotal(Integer.parseInt(jsonArray.get(7).toString()));
		alipayBizDo.setSendTime(Float.parseFloat(jsonArray.get(8).toString()));
		
		alipayBizDo.setConfirmSuccess(Integer.parseInt(jsonArray.get(9).toString()));
		alipayBizDo.setConfirmTotal(Integer.parseInt(jsonArray.get(10).toString()));
		alipayBizDo.setConfirmTime(Float.parseFloat(jsonArray.get(11).toString()));
		return alipayBizDo;
	}
	
	
	public void checkAlipay(Date start, Date end){
		try {
			Map<Date, AlipayBizPo> alipayMap = getAlipayBizPoByTime(start,end);
			if(alipayMap == null){
				return;
			}
			
			Map<Date, AlipayBizPo> taobaoMap = new HashMap<Date, AlipayBizPo>();
			String appName = "tradeplatform";
			String key = "tp�����������`P1-trade_create";
			//��ȡcsp��ص�����  
			Map<String, Map<String, Object>> cspQuerymap = QueryUtil.querySingleRealTime(appName, key);

			for (Map.Entry<String, Map<String, Object>> entry : cspQuerymap
					.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				AlipayBizPo po = new AlipayBizPo();
				Map<String, Object> tMap = entry.getValue();
				po.setCreateSuccess((Integer) tMap.get("success"));
				po.setCreateTotal((Integer) tMap.get("total"));
				taobaoMap.put(new Date(Long.parseLong(entry.getKey())), po);
			}
			
			List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
			for (Map.Entry<Date, AlipayBizPo> entry : alipayMap.entrySet()) {
				Date alipayTime = entry.getKey();
				Date theMostNearTaobaoTime = TimeUtil.getTheMostNearTime(alipayTime,
						taobaoMap.keySet());
				AlipayBizPo taobaoData = taobaoMap.get(theMostNearTaobaoTime);
				if (taobaoData == null)
					continue;
				if (!compareIfNear(taobaoData, entry.getValue())) {
					// /����һ��TP�ĸ澯��¼
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setMode_name("��ֵ");
					po.setKey_scope("APP");
					po.setApp_name("tradeplatform");
					po.setKey_name("��֧�������������Ա�");
					po.setProperty_name("count");
					po.setAlarm_cause("��֧���������������������ƥ��");
					po.setAlarm_time(new Timestamp(alipayTime.getTime()));
					po.setAlarm_value("t=" + taobaoData.getCreateTotal() + ",a=" + entry.getValue().getCreateTotal());
					po.setIp("172.24.168.111");  //����д��һ̨tp������ip
					list.add(po);
				}
			}
			if(CollectionUtil.isNotEmpty(list)){
				CspTimeKeyAlarmRecordAo.get().insert(list);
			}
			
			
		} catch (Exception e) {
			logger.warn("checkAlipay exception,start=" + start +",end=" + end,e);
		}
		
	}

	public boolean compareIfNear(AlipayBizPo taobao, AlipayBizPo alipay) {
		if (taobao == null || alipay == null) {
			return true;
		}
		logger.warn("compareIfNear taobao=" + taobao + ",alipay=" + alipay);
		// �Ƿ���������һ���������������յ����������10%����
		if (BigDecimal.valueOf(Math.abs(taobao.getCreateTotal()- alipay.getCreateTotal())).divide(BigDecimal.valueOf(alipay.getCreateTotal()), 0,
						BigDecimal.ROUND_HALF_UP).floatValue() > 0.1f) {
			return false;
		}
		return true;
	}
		
	
}
