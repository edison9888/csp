package com.taobao.monitor.trade.dailyreport;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

public class TFTemplateParser {
	private static Logger logger = Logger.getLogger("trade.report");

	public static String parseBuyContent(String searchDate, int selectAppId) {

		String result = "";

		MonitorVoFacade voFacade = null;
		try {
			Map<String, Object> root = new HashMap<String, Object>();
			voFacade = new MonitorVoFacade(searchDate, selectAppId);
			putCommonContent(root, voFacade);

			OrderCommitDataPutter ocdp = new OrderCommitDataPutter(voFacade);
			ocdp.putDatas(root);

			ByteArrayOutputStream arrayOut = TemplateUtils.process("tfBuy.tpl", root);
			result = arrayOut.toString();

		} catch (Exception e) {
			logger.warn("parseTfBuy template exception ", e);
		}
		return result;
	}

	public static String parseTmContent(String searchDate, int selectAppId) {

		String result = "";
		MonitorVoFacade voFacade = null;
		try {
			Map<String, Object> root = new HashMap<String, Object>();
			voFacade = new MonitorVoFacade(searchDate, selectAppId);
			
			putCommonContent(root, voFacade);
			
			ByteArrayOutputStream arrayOut = TemplateUtils.process("tfTm.tpl", root);
			result = arrayOut.toString();
			
		} catch (Exception e) {
			logger.warn("parseTfBuy template exception ", e);
		}
		return result;
	}

	private static void putCommonContent(Map<String, Object> root, MonitorVoFacade voFacade) {

		PerformanceDataPutter pdp = new PerformanceDataPutter(voFacade);
		pdp.putData(root);

		HsfoutDataPutter hsfdp = new HsfoutDataPutter(voFacade);
		hsfdp.putDatas(root);

		PvDataPutter pvdp = new PvDataPutter(voFacade);
		pvdp.putDatas(root);
	}
}
