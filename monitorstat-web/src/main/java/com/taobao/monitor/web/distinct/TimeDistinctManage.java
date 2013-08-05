package com.taobao.monitor.web.distinct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.ao.MonitorLoadRunAo;
import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * �Ա�ÿ��ı仯���Ե����һ������ǰ�����ݽ��жԱ� ��Ҫ���ڲ��� ������ص�ı仯�Ƚϴ�Ĳ���
 * 
 * @author xiaodu
 * @version 2010-9-8 ����05:38:47
 */
public class TimeDistinctManage {

	public static Map<Integer, TimeDistinctManage> manageMap = new HashMap<Integer, TimeDistinctManage>();

	public static TimeDistinctManage get(int appId) {

		synchronized (TimeDistinctManage.class) {
			TimeDistinctManage manage = manageMap.get(appId);
			if (manage == null) {
				manage = new TimeDistinctManage(appId);
				manageMap.put(appId, manage);
				return manage;
			} else {
				Date date = manage.getCurrentDate();
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String c = sdf.format(date);
				String h = sdf.format(cal.getTime());

				if (c.equals(h)) {
					return manage;
				} else {
					manageMap.remove(appId);
					manage = new TimeDistinctManage(appId);
					manageMap.put(appId, manage);
					return manage;
				}
			}
		}
	}

	private Date currentDate;

	private Date tongqiDate;

	private int appId;

	public TimeDistinctManage(int appId) {
		this.appId = appId;
		resetDistinctTime();
		findNewAndLostMonitorKey();
	}

	private List<KeyPo> newKeyPoList = new ArrayList<KeyPo>();
	private List<KeyPo> lostKeyPoList = new ArrayList<KeyPo>();
	private List<KeyPo> currentKeyPoList = new ArrayList<KeyPo>();

	public Date getCurrentDate() {
		return currentDate;
	}

	public Date getTongqiDate() {
		return tongqiDate;
	}

	public List<KeyPo> getNewKeyPoList() {
		return newKeyPoList;
	}

	public List<KeyPo> getLostKeyPoList() {
		return lostKeyPoList;
	}

	public List<KeyPo> getCurrentKeyPoList() {
		return currentKeyPoList;
	}

	public void resetDistinctTime() {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		currentDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		tongqiDate = cal.getTime();

	}

	/**
	 * ȡ��ѹ��qps
	 * 
	 * @return
	 */
	public double findAppLoadRunQps() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		return MonitorLoadRunAo.get().findRecentlyAppLoadRunQps(appId,currentDate);
	}

	/**
	 * ��ǰֵ �� ����ǰ������ͺ�������ıȽ� B2 B1 B0 A0 F0 F1 F2
	 * 
	 * 
	 * 
	 * @param currentMap
	 */
	public void m3(Map<String, KeyValuePo> currentMap) {
		int offset = 3;

		List<KeyValuePo> currentList = new ArrayList<KeyValuePo>();
		currentList.addAll(currentMap.values());
		Collections.sort(currentList);

		for (int i = 0; i < currentList.size(); i++) {
			String v = currentList.get(i).getValueStr();

			double bValue = 0;
			int bSize = 0;
			for (int b = i - offset; b < i && b > -1; b++) {
				bValue = Arith.add(bValue, Double.parseDouble(currentList.get(b).getValueStr()));
				bSize++;
			}
			double fValue = 0;
			int fSize = 0;
			for (int f = i + offset; i < f && f < currentList.size(); f--) {
				fValue = Arith.add(fValue, Double.parseDouble(currentList.get(f).getValueStr()));
				fSize++;
			}

			if (bSize == offset && fSize == offset) {
				bValue = Arith.div(bValue, bSize, 4);
				fValue = Arith.div(fValue, fSize, 4);
				double s = Double.parseDouble(v);
				double average = Arith.div(Arith.add(bValue, fValue), 2);
				if (s < Arith.mul(average, 0.5) || s > Arith.mul(average, 1.5)) {
					// �����ǰ���ֵ�� ǰ��������ƽ�� �Ҵ���1.5�� ��˵�������һ���쳣��
					currentList.get(i).setValueStr(average + "");
				}
			}

		}

	}

	/**
	 * ȡ�ø߷��ڵ�ƽ��ֵ
	 * 
	 * @param valueMap
	 * @return
	 */
	public double peakAverageValue(Map<String, KeyValuePo> valueMap) {
		m3(valueMap);
		List<KeyValuePo> valueList = new ArrayList<KeyValuePo>();
		valueList.addAll(valueMap.values());

		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

		double all = 0;
		int size = 0;
		for (KeyValuePo po : valueList) {
			int time = Integer.parseInt(sdf.format(po.getCollectTime()));
			if (2030 < time && time < 2230) {
				all = Arith.add(Double.parseDouble(po.getValueStr()), all);
				size++;
			}
		}

		if (size > 0) {
			return Arith.div(all, size, 2);
		}

		return -1;
	}

	/**
	 * ȡ������������ʱ���ڣ����ֵ�һ�����ֵ
	 * 
	 * @param valueMap
	 * @return
	 */
	public double pvMaxValue(Map<String, KeyValuePo> valueMap) {
		List<KeyValuePo> valueList = new ArrayList<KeyValuePo>();
		valueList.addAll(valueMap.values());

		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

		double maxPv = 0;
		for (KeyValuePo po : valueList) {
			int time = Integer.parseInt(sdf.format(po.getCollectTime()));
			if (830 < time && time < 2359) {
				if (Double.parseDouble(po.getValueStr()) > maxPv) {
					maxPv = Double.parseDouble(po.getValueStr());
				}
			}
		}
		return maxPv;
	}

	/**
	 * ȡ����һ���г��ַ��ʵ���ߵ��ֵ ���������˵
	 * 
	 * @return
	 */
	public double findMaxPv() {
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId, 175, currentDate);
		return pvMaxValue(currentMap);
	}

	public double findSameMaxPv() {
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId, 175, tongqiDate);
		return pvMaxValue(currentMap);
	}

	/**
	 * ȥ����Ӧʱ���ڸ߷����е�ƽ������
	 * 
	 * @return
	 */
	public double findPeakRestPvValue() {
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId, 176, currentDate);
		return peakAverageValue(currentMap);
	}

	public double findSameRestPvValue() {
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId, 176, tongqiDate);
		return peakAverageValue(currentMap);
	}

	/**
	 * ȡ������������ʱ���ڣ���Ӧʱ�� ����һ�� �����쳣����
	 * 
	 * @param valueMap
	 * @return
	 */
	public double pvRestValue(Map<String, KeyValuePo> valueMap) {
		m3(valueMap);
		List<KeyValuePo> valueList = new ArrayList<KeyValuePo>();
		valueList.addAll(valueMap.values());
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

		double maxPv = 0;
		for (KeyValuePo po : valueList) {
			int time = Integer.parseInt(sdf.format(po.getCollectTime()));
			if (830 < time && time < 2359) {
				if (Double.parseDouble(po.getValueStr()) > maxPv) {
					maxPv = Double.parseDouble(po.getValueStr());
				}
			}
		}
		return maxPv;
	}

	/**
	 * ��ѯ��pv �ı仯�������C ʹ����Ҫ�ӿ� ��Ҫ�漰Ϊqps �ı仯��rest �仯��ҳ���С�仯 | 3198 |
	 * PV_PAGESIZE_AVERAGEUSERTIMES | NULL | NULL | | 176 |
	 * PV_REST_AVERAGEUSERTIMES | NULL | NULL | | 175 | PV_VISIT_COUNTTIMES |
	 * NULL | NULL |
	 */
	public void findPvDifferent() {
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId, 175, currentDate);
		Map<String, KeyValuePo> tongqiMap = MonitorTimeAo.get().findKeyValueByDate(appId, 175, tongqiDate);

		peakAverageValue(currentMap);

		float qps = 0;
		float rt = 0;
		float page = 0;

	}

	private Map<String, List<DistinctInterface>> mapDistinctCommon = new HashMap<String, List<DistinctInterface>>();

	/**
	 * �ҳ���ͨ��ص��б仯�Ƚϴ�� monitorKeyMap.put("OUT_SearchEngine","����");
	 * monitorKeyMap.put("OUT_TairClient","tair");
	 * monitorKeyMap.put("OUT_HSF-Consumer","OUT_HSF-Consumer");
	 * monitorKeyMap.put("IN_HSF-ProviderDetail","IN_HSF-ProviderDetail");
	 * monitorKeyMap.put("OUT_PageCache","OUT_PageCache");
	 * monitorKeyMap.put("OUT_forest_client","OUT_forest_client");
	 * 
	 * @param keyname
	 * @return
	 */
	public List<DistinctInterface> findDistinctCommon(String keyname) {
		List<DistinctInterface> list = mapDistinctCommon.get(keyname);
		if (list == null) {
			list = new ArrayList<DistinctInterface>();
			mapDistinctCommon.put(keyname, list);

			Map<String, DistinctInterface> map = new HashMap<String, DistinctInterface>();
			for (KeyPo po : currentKeyPoList) {
				if (po.getKeyName().startsWith(keyname)) {
					int keyId = po.getKeyId();
					String keyName = po.getKeyName();

					String[] keys = keyName.split("_");

					String key = keyName.substring(0, keyName.lastIndexOf("_"));

					Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId, keyId,
							currentDate);
					Map<String, KeyValuePo> tongqiMap = MonitorTimeAo.get()
							.findKeyValueByDate(appId, keyId, tongqiDate);

					double current = peakAverageValue(currentMap);
					double tongqi = peakAverageValue(tongqiMap);

					DistinctInterface d = map.get(key);
					if (d == null) {
						d = new DistinctInterface();
						d.setKeyName(key);
						map.put(key, d);
					}

					if (keyName.indexOf(Constants.COUNT_TIMES_FLAG) > 0) {
						d.setCount(current);
						d.setSameCount(tongqi);
					}
					if (keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG) > 0) {
						d.setResponseTime(current);
						d.setSameResponseTime(tongqi);
					}
				}
			}
			for (Map.Entry<String, DistinctInterface> entry : map.entrySet()) {

				if ((Math.abs(entry.getValue().getCount() - entry.getValue().getSameCount()) > 500)
						|| (Math.abs(entry.getValue().getResponseTime() - entry.getValue().getSameResponseTime()) > 10)) {

					if (entry.getValue().getCount() > Arith.mul(entry.getValue().getSameCount(), 1.3)
							|| entry.getValue().getCount() < Arith.mul(entry.getValue().getSameCount(), 0.7)
							|| entry.getValue().getResponseTime() > Arith.mul(entry.getValue().getSameResponseTime(),
									1.3)
							|| entry.getValue().getResponseTime() < Arith.mul(entry.getValue().getSameResponseTime(),
									0.7)) {
						list.add(entry.getValue());
					}

				}

			}
		}
		return list;
	}

	/**
	 * �ҳ�Other ���쳣�ĵ�
	 * 
	 * @return
	 */
	public List<DistinctInterface> findDistinctOther() {

		List<DistinctInterface> list = mapDistinctCommon.get("findDistinctOther()");
		if (list == null) {
			list = new ArrayList<DistinctInterface>();
			mapDistinctCommon.put("findDistinctOther()", list);

			Map<String, DistinctInterface> map = new HashMap<String, DistinctInterface>();
			for (KeyPo po : currentKeyPoList) {
				if (po.getKeyName().startsWith("OTHER")) {
					int keyId = po.getKeyId();
					String keyName = po.getKeyName();

					String[] keys = keyName.split("_");

					String key = keys[1] + "_" + keys[2];

					Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId,keyId, 
							currentDate);
					Map<String, KeyValuePo> tongqiMap = MonitorTimeAo.get()
							.findKeyValueByDate(appId, keyId, tongqiDate);

					double current = peakAverageValue(currentMap);
					double tongqi = peakAverageValue(tongqiMap);

					DistinctInterface d = map.get(key);
					if (d == null) {
						d = new DistinctInterface();
						d.setKeyName(key);
						map.put(key, d);
					}

					if (keyName.indexOf(Constants.COUNT_TIMES_FLAG) > 0) {
						d.setCount(current);
						d.setSameCount(tongqi);

					}
					if (keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG) > 0) {
						d.setResponseTime(current);
						d.setSameResponseTime(tongqi);

					}
				}
			}

			for (Map.Entry<String, DistinctInterface> entry : map.entrySet()) {

				if ((Math.abs(entry.getValue().getCount() - entry.getValue().getSameCount()) > 500)
						|| (Math.abs(entry.getValue().getResponseTime() - entry.getValue().getSameResponseTime()) > 10)) {

					if (entry.getValue().getCount() > Arith.mul(entry.getValue().getSameCount(), 1.3)
							|| entry.getValue().getCount() < Arith.mul(entry.getValue().getSameCount(), 0.7)
							|| entry.getValue().getResponseTime() > Arith.mul(entry.getValue().getSameResponseTime(),
									1.3)
							|| entry.getValue().getResponseTime() < Arith.mul(entry.getValue().getSameResponseTime(),
									0.7)) {
						list.add(entry.getValue());
					}

				}

			}
		}
		return list;
	}

	public List<DistinctInterface> findExceptionDifferent() {

		List<DistinctInterface> list = mapDistinctCommon.get("findExceptionDifferent()");
		if (list == null) {
			list = new ArrayList<DistinctInterface>();
			mapDistinctCommon.put("findExceptionDifferent()", list);

			for (KeyPo po : currentKeyPoList) {
				if (po.getKeyName().startsWith("EXCEPTION")) {
					int keyId = po.getKeyId();
					Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId,keyId, 
							currentDate);
					Map<String, KeyValuePo> tongqiMap = MonitorTimeAo.get()
							.findKeyValueByDate(appId, keyId, tongqiDate);

					int currentExcNum = 0;
					int sameExcNum = 0;
					Set<Integer> siteSet = new HashSet<Integer>();
					for (KeyValuePo keyValuePo : currentMap.values()) {
						siteSet.addAll(keyValuePo.getSiteValueMap().keySet());
						for (Double v : keyValuePo.getSiteValueMap().values()) {
							currentExcNum += v;
						}
					}
					if (siteSet.size() > 0) {
						currentExcNum = currentExcNum / siteSet.size();
					}
					siteSet.clear();
					for (KeyValuePo keyValuePo : tongqiMap.values()) {
						siteSet.addAll(keyValuePo.getSiteValueMap().keySet());
						for (Double v : keyValuePo.getSiteValueMap().values()) {
							sameExcNum += v;
						}
					}
					if (siteSet.size() > 0) {
						sameExcNum = sameExcNum / siteSet.size();
					}
					if (sameExcNum < 20 && currentExcNum < 20) {
						continue;
					}
					DistinctInterface d = new DistinctInterface();
					d.setKeyName(po.getKeyName());
					d.setCount(currentExcNum);
					d.setSameCount(sameExcNum);
					list.add(d);
				}
			}
		}
		return list;
	}

	/**
	 * �ҳ�Ӧ���³��ֺ���ʧ�Ľӿ�
	 */
	public void findNewAndLostMonitorKey() {

		List<KeyPo> currentList = MonitorTimeAo.get().findAppAllMonitorKey(appId, currentDate);
		currentKeyPoList.addAll(currentList);
		List<KeyPo> tongqiList = MonitorTimeAo.get().findAppAllMonitorKey(appId, tongqiDate);
		// ����
		for (KeyPo po : currentList) {
			if (!tongqiList.contains(po)) {
				newKeyPoList.add(po);
			}
		}
		// ��ʧ
		for (KeyPo po : tongqiList) {
			if (!currentList.contains(po)) {
				lostKeyPoList.add(po);
			}
		}
		// ��ǰ���е�
		for (KeyPo po : newKeyPoList) {
			currentKeyPoList.remove(po);
		}
		// ��ǰ���е�
		for (KeyPo po : lostKeyPoList) {
			currentKeyPoList.remove(po);
		}

	}

}
