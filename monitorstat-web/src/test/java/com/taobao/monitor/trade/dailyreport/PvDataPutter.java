package com.taobao.monitor.trade.dailyreport;

import static com.taobao.monitor.common.util.Utlitites.fromatLong;
import static com.taobao.monitor.common.util.Utlitites.scale;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.taobao.monitor.web.vo.OtherHaBoLogRecord;
import com.taobao.monitor.web.vo.OtherKeyValueVo;

import freemarker.template.Template;

public class PvDataPutter {
	private MonitorVoFacade voFacade;
	public PvDataPutter(MonitorVoFacade voFacade) {

		this.voFacade = voFacade;
	}

	public void putDatas(Map<String, Object> destMap) {

		if (destMap == null) {
			throw new RuntimeException("map arg to receive params cann't be null");
		}
		List<Map<String, String>> records = new ArrayList<Map<String, String>>();
		Map<String, List<Map<String, String>>> pvMap = new HashMap<String, List<Map<String, String>>>();
		pvMap.put("records", records);
		destMap.put("pvs", pvMap);

		if (voFacade.vo.getOtherKeyValueMap().size() > 0) {

			// 这里处理逻辑基于只有一条entry , 就是pv 。 否则要过滤
			for (Map.Entry<String, OtherKeyValueVo> entry : voFacade.vo.getOtherKeyValueMap().entrySet()) {
				String keyName = entry.getKey();
				destMap.put("appName", keyName);
				
				OtherKeyValueVo keyValue = entry.getValue();
				OtherKeyValueVo tongqikeyValue = voFacade.tongqiVo.getOtherKeyValueMap().get(keyName);
				OtherKeyValueVo huanbikeyValue = voFacade.huanbiVo.getOtherKeyValueMap().get(keyName);

				Map<String, OtherHaBoLogRecord> valueMap = keyValue.getKeyMap();
				List<Map.Entry<String, OtherHaBoLogRecord>> valueMapList = new ArrayList<Map.Entry<String, OtherHaBoLogRecord>>(
						valueMap.entrySet());

				Collections.sort(valueMapList, new Comparator<Map.Entry<String, OtherHaBoLogRecord>>() {
					public int compare(Map.Entry<String, OtherHaBoLogRecord> e1,
							Map.Entry<String, OtherHaBoLogRecord> e2) {

						return (int) (Integer.parseInt(e2.getValue().getExeCount().getValueStr()) - Integer.parseInt(e1
								.getValue().getExeCount().getValueStr()));
					}
				});
				for (Map.Entry<String, OtherHaBoLogRecord> valueEntry : valueMapList) {
					
					String valueName = valueEntry.getKey();
					OtherHaBoLogRecord record = valueEntry.getValue();
					if (record == null) {
						continue;
					}

					Integer countId = record.getExeCount() == null ? 0 : record.getExeCount().getKeyId();
					Integer averageId = record.getAverageExeTime() == null ? 0 : record.getAverageExeTime().getKeyId();

					String count = record.getExeCount() == null ? null : record.getExeCount().getValueStr();
					String average = record.getAverageExeTime() == null ? null : record.getAverageExeTime()
							.getValueStr();

					String tongqicount = null;
					String tongqiaverage = null;
					if (tongqikeyValue != null) {
						OtherHaBoLogRecord v = tongqikeyValue.getKeyMap().get(valueName);
						if (v != null) {
							tongqicount = v.getExeCount() == null ? null : v.getExeCount().getValueStr();
							tongqiaverage = v.getAverageExeTime() == null ? null : v.getAverageExeTime().getValueStr();
						}

					}

					String huanbicount = null;
					String huanbiaverage = null;
					if (huanbikeyValue != null) {
						OtherHaBoLogRecord v = huanbikeyValue.getKeyMap().get(valueName);
						if (v != null) {
							huanbicount = v.getExeCount() == null ? null : v.getExeCount().getValueStr();
							huanbiaverage = v.getAverageExeTime() == null ? null : v.getAverageExeTime().getValueStr();
						}

					}

					String key = valueEntry.getKey();
					String c = fromatLong(count);
					String tongbiCnt = scale(count, tongqicount);
					String huanbiCnt = scale(count, huanbicount);
					String baselineCnt = scale(count, voFacade.getBaseValue(countId));

					String avg = average;
					String tongbiAvg = scale(average, tongqiaverage);
					String huanbiAvg = scale(average, huanbiaverage);
					String baselineAvg = scale(average, voFacade.getBaseValue(averageId));
					
					Map<String, String> r = new HashMap<String, String>();
					r.put("type", key);
					r.put("count", c);
					r.put("bongbi", tongbiCnt);
					r.put("huanbi", huanbiCnt);
					r.put("baseline", baselineCnt);

					r.put("time", avg);
					r.put("timeTongbi", tongbiAvg);
					r.put("timeHuanbi", huanbiAvg);
					r.put("timeBaseline", baselineAvg);
					records.add(r);
				}
			}
		}
	}
	
	public static void main(String [] a) throws Exception {
		Template tpl = TfTemplateTest.getTemplate("pv.tpl");
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(arrayOut, Charset.forName("GBK"));
		List<Map<String, String>> records = new ArrayList<Map<String, String>>();
		Map<String, Object> rm = new HashMap<String, Object>();
		Map<String, String> m = new HashMap<String, String>();
		Map<String,Object> pvs = new HashMap<String, Object>();
		
		pvs.put("appName","buy-pv");
		pvs.put("records", records);
		rm.put("pvs", pvs);
		
		m.put("type", "立即购买");
		m.put("count", 1+"");
		m.put("tongbi", 2+"");
		m.put("huanbi", 2.5+"");
		m.put("baseline", 2.1+"");

		m.put("time", 50+"");
		m.put("timeTongbi", 55+"");
		m.put("timeHuanbi", 45+"");
		m.put("timeBaseline", 60+"");
		
		records.add(m);
		tpl.process(rm, out);
		System.out.println(arrayOut.toString());
	}
	
}
