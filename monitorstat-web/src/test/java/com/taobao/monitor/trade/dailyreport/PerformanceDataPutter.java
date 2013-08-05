package com.taobao.monitor.trade.dailyreport;

import static com.taobao.monitor.common.util.Utlitites.formatNull;
import static com.taobao.monitor.common.util.Utlitites.fromatLong;
import static com.taobao.monitor.common.util.Utlitites.scale;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.web.vo.MonitorVo;

import freemarker.template.Template;

public class PerformanceDataPutter {

	private MonitorVoFacade voFacade;

	public PerformanceDataPutter(MonitorVoFacade voFacade) {

		this.voFacade = voFacade;
	}

	public void putData(Map<String, Object> destMap) {

		if (destMap == null)
			throw new RuntimeException("map arg to receive params cann't be null");
		Map<String, String> p = new HashMap<String, String>();
		destMap.put("performance", p);

		fill(p, voFacade.vo, voFacade.tongqiVo, voFacade.huanbiVo, voFacade.appBaseMap);
	}

	private void fill(Map<String, String> p, MonitorVo vo, MonitorVo tongqiVo, MonitorVo huanbiVo,
			Map<Integer, KeyValueBaseLinePo> appBaseMap) {

		String cpu = vo.getCpu() == null ? " - " : vo.getCpu();
		String cpuTongbi = scale(vo.getCpu(), tongqiVo.getCpu());
		String cpuHuanbi = scale(vo.getCpu(), huanbiVo.getCpu());
		String cpuBaseline = scale(vo.getCpu(), voFacade.getBaseValue(vo.getCpuKeyId()));
		p.put("cpu", cpu);
		p.put("tongbiCpu", cpuTongbi);
		p.put("huanbiCpu", cpuHuanbi);
		p.put("baselineCpu", cpuBaseline);

		String io = vo.getIowait() == null ? " - " : vo.getIowait();
		String ioTongbi = scale(vo.getIowait(), tongqiVo.getIowait());
		String ioHuanbi = scale(vo.getIowait(), huanbiVo.getIowait());
		String ioBaseline = scale(vo.getIowait(), voFacade.getBaseValue(vo.getIowaitKeyId()));
		p.put("iowait", io);
		p.put("tongbiIowait", ioTongbi);
		p.put("huanbiIowait", ioHuanbi);
		p.put("baselineIowait", ioBaseline);

		String load = vo.getLoad() == null ? " - " : vo.getLoad();
		String loadTongbi = scale(vo.getLoad(), tongqiVo.getLoad());
		String loadHuanbi = scale(vo.getLoad(), huanbiVo.getLoad());
		String loadBaseline = scale(vo.getLoad(), voFacade.getBaseValue(vo.getLoadKeyId()));
		p.put("load", load);
		p.put("tongbiLoad", loadTongbi);
		p.put("huanbiLoad", loadHuanbi);
		p.put("baselineLoad", loadBaseline);

		String mem = vo.getMen() == null ? " - " : vo.getMen();
		String memTongbi = scale(vo.getMen(), tongqiVo.getMen());
		String memHuanbi = scale(vo.getMen(), huanbiVo.getMen());
		String memBaseline = scale(vo.getMen(), voFacade.getBaseValue(vo.getMenKeyId()));
		p.put("mem", mem);
		p.put("tongbiMem", memTongbi);
		p.put("huanbiMem", memHuanbi);
		p.put("baselineMem", memBaseline);

		String swp = vo.getSwap() == null ? " - " : vo.getSwap();
		String swpTongbi = scale(vo.getSwap(), tongqiVo.getSwap());
		String swpHuanbi = scale(vo.getSwap(), huanbiVo.getSwap());
		String swpBaseline = scale(vo.getSwap(), voFacade.getBaseValue(vo.getSwapKeyId()));
		p.put("swap", swp);
		p.put("tongbiSwap", swpTongbi);
		p.put("huanbiSwap", swpHuanbi);
		p.put("baselineSwap", swpBaseline);

		String hpv = fromatLong(vo.getPv());
		String hpvTongbi = scale(vo.getPv(), tongqiVo.getPv());
		String hpvHuanbi = scale(vo.getPv(), huanbiVo.getPv());
		String hpvBaseline = scale(vo.getPv(), voFacade.getBaseValue(vo.getPvKeyId()));
		p.put("pv", hpv);
		p.put("tongbiPv", hpvTongbi);
		p.put("huanbiPv", hpvHuanbi);
		p.put("baselinePv", hpvBaseline);

		String hqps = formatNull(vo.getQpsNum());
		String hqpsTongbi = scale(vo.getQpsNum(), tongqiVo.getQpsNum());
		String hqpsHuanbi = scale(vo.getQpsNum(), huanbiVo.getQpsNum());
		String hqpsBaseline = scale(vo.getQpsNum(), voFacade.getBaseValue(vo.getQpsKeyId()));
		p.put("qps", hqps);
		p.put("tongbiQps", hqpsTongbi);
		p.put("huanbiQps", hqpsHuanbi);
		p.put("baselineQps", hqpsBaseline);

		String hrt = formatNull(vo.getRtNum());
		String hrtTongbi = scale(vo.getRtNum(), tongqiVo.getRtNum());
		String hrtHuanbi = scale(vo.getRtNum(), huanbiVo.getRtNum());
		String hrtBaseline = scale(vo.getRtNum(), voFacade.getBaseValue(vo.getRtKeyId()));
		p.put("rt", hrt);
		p.put("tongbiRt", hrtTongbi);
		p.put("huanbiRt", hrtHuanbi);
		p.put("baselineRt", hrtBaseline);

		String cpv = fromatLong(vo.getApachePv());
		String cpvTongbi = scale(vo.getApachePv(), tongqiVo.getApachePv());
		String cpvHuanbi = scale(vo.getApachePv(), huanbiVo.getApachePv());
		String cpvBaseline = scale(vo.getApachePv(), voFacade.getBaseValue(vo.getApachePvKeyId()));
		p.put("cspPv", cpv);
		p.put("tongbiCspPv", cpvTongbi);
		p.put("huanbiCspPv", cpvHuanbi);
		p.put("baselineCspPv", cpvBaseline);

		String cqps = formatNull(vo.getApacheQps());
		String cqpsTongbi = scale(vo.getApacheQps(), tongqiVo.getApacheQps());
		String cqpsHuanbi = scale(vo.getApacheQps(), huanbiVo.getApacheQps());
		String cqpsBaseline = scale(vo.getApacheQps(), voFacade.getBaseValue(vo.getApacheQpsKeyId()));
		p.put("cspQps", cqps);
		p.put("tongbiCspQps", cqpsTongbi);
		p.put("huanbiCspQps", cqpsHuanbi);
		p.put("baselineCspQps", cqpsBaseline);

		String crt = formatNull(vo.getApacheRest());
		String crtTongbi = scale(vo.getApacheRest(), tongqiVo.getApacheRest());
		String crtHuanbi = scale(vo.getApacheRest(), huanbiVo.getApacheRest());
		String crtBaseline = scale(vo.getApacheRest(), voFacade.getBaseValue(vo.getApacheRestKeyId()));
		p.put("cspRt", crt);
		p.put("huanbiCspRt", crtHuanbi);
		p.put("tongbiCspRt", crtTongbi);
		p.put("baselineCspRt", crtBaseline);

	}

	@Test
	public void test() throws Exception {

		Map<String, String> m = new HashMap<String, String>();
		fill(m, new MonitorVo(), new MonitorVo(), new MonitorVo(), new HashMap<Integer, KeyValueBaseLinePo>());
		System.out.println(m.toString());

		Template tpl = TfTemplateTest.getTemplate("performance.tpl");
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(arrayOut, Charset.forName("GBK"));
		Map<String, Object> rm = new HashMap<String, Object>();
		rm.put("performance", m);
		tpl.process(rm, out);
		System.out.println(arrayOut.toString());
	}

}
