package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.alarm.ao.BaselineCheckAo;
import com.taobao.csp.alarm.baseline.AppBaseLineProcessor;
import com.taobao.csp.alarm.baseline.BaseLineProcessor;
import com.taobao.csp.alarm.baseline.HostBaseLineProcessor;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.time.cache.KeyCache;
import com.taobao.csp.time.util.AmlineFlash;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.web.po.BaselineCheckPo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/data/show.do")
public class BaselineController extends BaseController {
	private static Logger logger = Logger.getLogger(BaseController.class);

	@RequestMapping(params = "method=baseline")
	public ModelAndView baseline(String appName, String keyName, String propertyName, String flag, String time) {
		ModelAndView view = new ModelAndView("/time/data/baseline");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (time == null)
			time = sdf.format(new Date());
		CspKeyInfo cki = KeyAo.get().getKeyInfo(keyName);
		if (flag == null || flag.equals(""))
			flag = "no";
		Integer keyId = 0;
		if (keyName != null && !keyName.equals("")) {
			keyId = KeyCache.getCache().getKeyInfo(keyName).getKeyId();
		}
		view.addObject("cki", cki);
		view.addObject("flag", flag);
		view.addObject("keyId", keyId);
		view.addObject("propertyName", propertyName);
		view.addObject("keyName", keyName);
		view.addObject("appName", appName);
		view.addObject("time", time);
		return view;
	}

	@RequestMapping(params = "method=baselineCheck")
	public void baselineCheck(String appName, HttpServletResponse response, HttpServletRequest request) throws ParseException {
		List<CspKeyMode> list = KeyAo.get().getKeyModeByAppName(appName);
		for (CspKeyMode ckm : list) {
			try {
				for (int i = 1; i < 8; i++) {
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.DAY_OF_WEEK, i);
					Date tmp = cal.getTime();
					CspKeyInfo cki = KeyAo.get().getKeyInfo(ckm.getKeyName());
					BaselineCheckPo po = new BaselineCheckPo();
					po.setAppName(appName);
					po.setKeyName(ckm.getKeyName());
					po.setPropertyName(ckm.getPropertyName());
					int week = cal.get(Calendar.DAY_OF_WEEK);
					po.setWeekDay(week);
					if (cki.getKeyScope().equals("APP") || cki.getKeyScope().equals("ALL")) {
						po.setScope("APP");
						BaseLineProcessor processor = new AppBaseLineProcessor(appName, ckm.getKeyName(), ckm.getPropertyName(), tmp);
						Map<String, Double> map = processor.getBaseLine();

						if (map == null || map.size() == 0) {
							po.setState("未计算");
							BaselineCheckAo.get().updateState(po);
						} else {
							po.setState("入库完毕");
							BaselineCheckAo.get().updateState(po);
						}
					}
					if (cki.getKeyScope().equals("HOST") || cki.getKeyScope().equals("ALL")) {
						List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
						Set<String> set = new HashSet<String>();
						for (String ip : ipList) {
							set.add(CspCacheTBHostInfos.get().getHostInfoByIp(ip).getHostSite());
						}
						for (String site : set) {
							BaseLineProcessor hostprocessor = new HostBaseLineProcessor(site, appName, ckm.getKeyName(), ckm.getPropertyName(), tmp);
							Map<String, Double> map = hostprocessor.getBaseLine();
							po.setScope(site);
							if (map == null || map.size() == 0) {
								po.setState("未计算");
								BaselineCheckAo.get().updateState(po);
							} else {
								po.setState("入库完毕");
								BaselineCheckAo.get().updateState(po);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.info(e);
			}
		}
		try {
			response.sendRedirect(request.getContextPath() + "/data/show.do?method=baselineSateOfApp&appName=" + appName);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=renewAllBaseline")
	public void renew(String appName, HttpServletResponse response, HttpServletRequest request) throws ParseException {
		List<CspKeyMode> list = KeyAo.get().getKeyModeByAppName(appName);
		for (CspKeyMode ckm : list) {
			for (int i = 1; i < 8; i++) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_WEEK, i);
				Date tmp = cal.getTime();
				CspKeyInfo cki = KeyAo.get().getKeyInfo(ckm.getKeyName());
				if (cki.getKeyScope().equals("APP") || cki.getKeyScope().equals("ALL")) {
					BaseLineProcessor processor = new AppBaseLineProcessor(appName, ckm.getKeyName(), ckm.getPropertyName(), tmp);
					processor.process();
				}
				if (cki.getKeyScope().equals("HOST") || cki.getKeyScope().equals("ALL")) {
					List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
					Set<String> set = new HashSet<String>();
					for (String ip : ipList) {
						set.add(CspCacheTBHostInfos.get().getHostInfoByIp(ip).getHostSite());
					}
					for (String site : set) {
						BaseLineProcessor hostprocessor = new HostBaseLineProcessor(site, appName, ckm.getKeyName(), ckm.getPropertyName(), tmp);
						hostprocessor.process();
					}
				}
			}
		}
		try {
			response.sendRedirect(request.getContextPath() + "/data/show.do?method=baselineSateOfApp&appName=" + appName);
		} catch (IOException e) {
		}
	}

	@RequestMapping(params = "method=reprocess")
	public ModelAndView reprocess(String appName, String keyName, String propertyName, String flag, String time) throws ParseException {
		ModelAndView view = new ModelAndView("/time/data/baseline");
		CspKeyInfo cki = KeyAo.get().getKeyInfo(keyName);
		if (flag == null || flag.equals(""))
			flag = "no";
		Integer keyId = 0;
		if (keyName != null && !keyName.equals("")) {
			keyId = KeyCache.getCache().getKeyInfo(keyName).getKeyId();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(time);
			if (cki.getKeyScope().equals("APP") || cki.getKeyScope().equals("ALL")) {
				BaseLineProcessor processor = new AppBaseLineProcessor(appName, keyName, propertyName, date);
				processor.process();
			}
			if (cki.getKeyScope().equals("HOST") || cki.getKeyScope().equals("ALL")) {
				List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
				Set<String> set = new HashSet<String>();
				for (String ip : ipList) {
					set.add(CspCacheTBHostInfos.get().getHostInfoByIp(ip).getHostSite());
				}
				for (String site : set) {
					BaseLineProcessor hostprocessor = new HostBaseLineProcessor(site, appName, keyName, propertyName, date);
					hostprocessor.process();
				}
			}
		}
		view.addObject("cki", cki);
		view.addObject("flag", flag);
		view.addObject("keyId", keyId);
		view.addObject("propertyName", propertyName);
		view.addObject("keyName", keyName);
		view.addObject("appName", appName);
		view.addObject("time", time);
		return view;
	}

	@RequestMapping(params = "method=history")
	public ModelAndView history(String appName, String keyName, String flag, String time, String propertyName, String ip) {
		ModelAndView view = new ModelAndView("/time/data/history");
		if (flag == null || flag.equals(""))
			flag = "no";
		Integer keyId = 0;
		if (keyName != null && !keyName.equals("")) {
			keyId = KeyCache.getCache().getKeyInfo(keyName).getKeyId();

		}
		String ipflag = "no";
		if (ip != null && !ip.equals("")) {
			ipflag = "yes";
		}
		view.addObject("ipflag", ipflag);
		view.addObject("flag", flag);
		view.addObject("keyId", keyId);
		view.addObject("keyName", keyName);
		view.addObject("appName", appName);
		view.addObject("time", time);
		view.addObject("propertyName", propertyName);
		view.addObject("ip", ip);
		return view;
	}

	@RequestMapping(params = "method=baselineData")
	public void baselineData(HttpServletResponse response, String appName, String propertyName, Integer keyId, String time) throws ParseException {
		String keyName = KeyCache.getCache().getKeyInfo(keyId).getKeyName();
		AmlineFlash am = new AmlineFlash();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(time);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		BaseLineProcessor processor = new AppBaseLineProcessor(appName, keyName, propertyName, date);
		Map<String, Double> mapBaseline = processor.getBaseLine();
		for (Map.Entry<String, Double> entry : mapBaseline.entrySet()) {
			if (entry.getValue() == null)
				continue;
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				long tmp = format.parse(entry.getKey()).getTime();
				am.addValue("week-" + cal.get(Calendar.DAY_OF_WEEK), tmp, entry.getValue());
			} catch (ParseException e) {
			}
		}
		try {
			writeJSONToResponse(response, am.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "method=historyData")
	public void historyData(HttpServletResponse response, String appName, String propertyName, Integer keyId, String time) throws ParseException {
		String keyName = KeyCache.getCache().getKeyInfo(keyId).getKeyName();
		AmlineFlash am = new AmlineFlash();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(time);
		Map<Date, String> map = QueryHistoryUtil.querySingle(appName, keyName, propertyName, date);
		for (Map.Entry<Date, String> entry : map.entrySet()) {
			if (entry.getValue() == null)
				continue;
			am.addValue(sdf.format(date), entry.getKey().getTime(), DataUtil.transformDouble(entry.getValue()));
		}
		try {
			writeJSONToResponse(response, am.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "method=historyDataHost")
	public void historyDataHost(HttpServletResponse response, String appName, String propertyName, Integer keyId, String time, String ip) throws ParseException {
		String keyName = KeyCache.getCache().getKeyInfo(keyId).getKeyName();
		AmlineFlash am = new AmlineFlash();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(time);
		Map<Date, String> map = QueryHistoryUtil.querySingleHost(appName, keyName, ip, propertyName, date);
		for (Map.Entry<Date, String> entry : map.entrySet()) {
			if (entry.getValue() == null)
				continue;
			am.addValue(sdf.format(date), entry.getKey().getTime(), DataUtil.transformDouble(entry.getValue()));
		}
		try {
			writeJSONToResponse(response, am.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "method=baselineDataCM")
	public void baselineDataCM(HttpServletResponse response, String appName, String propertyName, Integer keyId, String time) throws ParseException {
		String keyName = KeyCache.getCache().getKeyInfo(keyId).getKeyName();
		AmlineFlash am = new AmlineFlash();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(time);
		List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
		Set<String> set = new HashSet<String>();
		for (String ip : ipList) {
			set.add(CspCacheTBHostInfos.get().getHostInfoByIp(ip).getHostSite());
		}
		for (String site : set) {
			BaseLineProcessor processor = new HostBaseLineProcessor(site, appName, keyName, propertyName, date);
			Map<String, Double> mapBaseline = processor.getBaseLine();
			for (Map.Entry<String, Double> entry : mapBaseline.entrySet()) {
				if (entry.getValue() == null)
					continue;
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				try {
					long tmp = format.parse(entry.getKey()).getTime();
					am.addValue(site + "-" + date, tmp, entry.getValue());
				} catch (ParseException e) {
				}
			}
		}
		try {
			writeJSONToResponse(response, am.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "method=baselineSateOfApp")
	public ModelAndView baselineStateOfApp(HttpServletRequest request, String appName) {
		ModelAndView view = new ModelAndView("/time/data/baseline");
		Integer count = 0;
		Integer stored = 0;
		Integer calculated = 0;
		String flag = "state";
		view.addObject("flag", flag);
		List<BaselineCheckPo> list = BaselineCheckAo.get().findByAppName(appName);
		if (list == null)
			return view;
		for (BaselineCheckPo po : list) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String timestr = "";
			if (po.getProcessTime() != null)
				timestr = sdf.format(po.getProcessTime()).toString();
			String url = request.getContextPath() + "/data/show.do?method=baseline&flag=yes&appName=" + po.getAppName() + "&keyName=" + po.getKeyName()
					+ "&propertyName=" + po.getPropertyName() + "&time=" + timestr;
			count++;
			try {
				if (po.getState().equals("计算完毕")) {
					calculated++;
				} else if (po.getState().equals("入库完毕")) {
					stored++;
				}
			} catch (Exception e) {
			}
			po.setUrl(url);
		}
		view.addObject("all", count);
		view.addObject("stored", stored);
		view.addObject("calculated", calculated);
		view.addObject("baselinechecks", list);
		return view;
	}

	public static void main(String args[]) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	}
}
