package com.taobao.monitor.trade.dailyreport;

import static com.taobao.monitor.common.util.Utlitites.fromatLong;
import static com.taobao.monitor.common.util.Utlitites.scale;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.taobao.monitor.web.vo.HsfPo;

import freemarker.template.Template;

public class HsfoutDataPutter {
	private MonitorVoFacade voFacade;

	public HsfoutDataPutter(MonitorVoFacade facade) {

		this.voFacade = facade;
	}

	public void putDatas(Map<String, Object> destMap) {

		if (destMap == null) {
			throw new RuntimeException("map arg to receive params cann't be null");
		}
		Map<String, Object> m = new HashMap<String, Object>();
		destMap.put("hsfout", m);
		List<Map<String, String>> records = new ArrayList<Map<String, String>>();
		m.put("records", records);

		if (voFacade.vo.getOutHsfList().size() > 0) {
			List<HsfPo> currentHsfList = voFacade.vo.getOutHsfList();
			List<HsfPo> tongbiHsfList = voFacade.tongqiVo.getOutHsfList();
			List<HsfPo> huanbiHsfList = voFacade.huanbiVo.getOutHsfList();

			Collections.sort(currentHsfList);

			Map<String, HsfPo> tongbiMap = new HashMap<String, HsfPo>();

			for (HsfPo tongbiPo : tongbiHsfList) {
				String key = tongbiPo.getAim() + "_" + tongbiPo.getClassName() + "_" + tongbiPo.getMethodName();
				tongbiMap.put(key, tongbiPo);
			}

			Map<String, HsfPo> huanbiMap = new HashMap<String, HsfPo>();
			for (HsfPo huanbiPo : huanbiHsfList) {
				String key = huanbiPo.getAim() + "_" + huanbiPo.getClassName() + "_" + huanbiPo.getMethodName();
				tongbiMap.put(key, huanbiPo);
			}

			int entryCnt = 0;
			Map<String, String> rec = null;
			for (HsfPo po : currentHsfList) {
				if (entryCnt++ >= 13)
					break;
				rec = new HashMap<String, String>();
				records.add(rec);
				String key = po.getAim() + "_" + po.getClassName() + "_" + po.getMethodName();
				String clz = po.getClassName();
				String mtd = po.getHtmlMethodName();
				rec.put("clz", clz);
				rec.put("method", mtd);

				String exeCnt = fromatLong(po.getExeCount());
				String huanbiCnt = scale(po.getExeCount(), tongbiMap.get(key) == null ? null : tongbiMap.get(key)
						.getExeCount());
				String tongbiCnt = scale(po.getExeCount(), huanbiMap.get(key) == null ? null : huanbiMap.get(key)
						.getExeCount());
				String baseCnt = scale(po.getExeCount(), voFacade.getBaseValue(po.getCountkeyId()));
				rec.put("callCount", exeCnt);
				rec.put("callCountTongbi", tongbiCnt);
				rec.put("callCountHuanbi", huanbiCnt);
				rec.put("callCountBaseline", baseCnt);

				String avg = po.getAverageExe();
				String tongbiAvg = scale(po.getAverageExe(), tongbiMap.get(key) == null ? null : tongbiMap.get(key)
						.getAverageExe());
				String huanbiAvg = scale(po.getAverageExe(), huanbiMap.get(key) == null ? null : huanbiMap.get(key)
						.getAverageExe());
				String baseAvg = scale(po.getAverageExe(), voFacade.getBaseValue(po.getAverageKeyId()));
				rec.put("avgTime", avg);
				rec.put("avgTimeTongbi", tongbiAvg);
				rec.put("avgTimeHuanbi", huanbiAvg);
				rec.put("avgTimeBaseline", baseAvg);

				String excCnt = po.getExecptionNum() == null ? " - " : po.getExecptionNum();
				String tongbiExCnt = scale(po.getExecptionNum(), tongbiMap.get(key) == null ? null : tongbiMap.get(key)
						.getExecptionNum());
				String huanbiExCnt = scale(po.getExecptionNum(), huanbiMap.get(key) == null ? null : huanbiMap.get(key)
						.getExecptionNum());
				String baseExCnt = scale(po.getExecptionNum(), voFacade.getBaseValue(po.getExcCountkeyId()));
				rec.put("exCount", excCnt);
				rec.put("exCountTongbi", tongbiExCnt);
				rec.put("exCountHuanbi", huanbiExCnt);
				rec.put("exCountBaseline", baseExCnt);

				String bizExcCnt = po.getBizExecptionNum() == null ? " - " : po.getBizExecptionNum();
				String tongbiBizExCnt = scale(po.getBizExecptionNum(), tongbiMap.get(key) == null ? null : tongbiMap
						.get(key).getBizExecptionNum());
				String huanbiBizExCnt = scale(po.getBizExecptionNum(), huanbiMap.get(key) == null ? null : huanbiMap
						.get(key).getBizExecptionNum());
				String baseBizExCnt = scale(po.getBizExecptionNum(), voFacade.getBaseValue(po.getBizCountkeyId()));
				rec.put("bizExCount", bizExcCnt);
				rec.put("bizExCountTongbi", tongbiBizExCnt);
				rec.put("bizExCountHuanbi", huanbiBizExCnt);
				rec.put("bizExCountBaseline", baseBizExCnt);
			}
		}
	}
	
	public static void main(String[] a) throws Exception {
		Template tpl = TfTemplateTest.getTemplate("hsfout.tpl");
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(arrayOut, Charset.forName("GBK"));
		Map<String,Object> destMap = new HashMap<String, Object>();
		Map<String, Object> hsfout = new HashMap<String, Object>();
		destMap.put("hsfout", hsfout);
		List<Map<String, String>> records = new ArrayList<Map<String, String>>();
		hsfout.put("records", records);
		
		Map<String, String> rec = new HashMap<String, String>();
		rec.put("clz","test");
		rec.put("method","test");

		rec.put("callCount","test");
		rec.put("callCountTongbi","test");
		rec.put("callCountHuanbi","test");
		rec.put("callCountBaseline","test");

		rec.put("avgTime","test");
		rec.put("avgTimeTongbi","test");
		rec.put("avgTimeHuanbi","test");
		rec.put("avgTimeBaseline","test");

		rec.put("exCount","test");
		rec.put("exCountTongbi","test");
		rec.put("exCountHuanbi","test");
		rec.put("exCountBaseline","test");

		rec.put("bizExCount","test");
		rec.put("bizExCountTongbi","test");
		rec.put("bizExCountHuanbi","test");
		rec.put("bizExCountBaseline","test");
		records.add(rec);
		tpl.process(destMap, out);
		System.out.println(arrayOut.toString());
	}
}
