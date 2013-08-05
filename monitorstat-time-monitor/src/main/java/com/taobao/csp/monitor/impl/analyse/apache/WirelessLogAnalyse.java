package com.taobao.csp.monitor.impl.analyse.apache;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.esotericsoftware.minlog.Log;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * 
 * 分析无线应用nginx日志
 * 
 * nginx配置： log_format wirelessLog
 * "$remote_addr||$request_time_usec||$request_length||$year:$month:$day:$hour:$minute:$second||$request_method http://$host$request_uri||$status||$bytes_sent||$http_referer||$http_user_agent||$sent_http_TBTrack_Id||ea=$sent_http_Mkt;$sent_http_Cat;$sent_http_Sell;$sent_http_WapVersion||pp=$sent_http_Shop_Id||$sent_http_X_TB_BD||$sent_http_X_TB_M||$upstream_http_x_up_calling_line_id||$upstream_http_Via||$upstream_http_x_forwarded_for||$upstream_http_x_network_info||$upstream_http_x_source_id||$sent_http_X_TB_OS||$upstream_http_Q_UA||abtest=$sent_http_abtest||$sent_http_ads||$upstream_http_x_up_bear_type||$sent_http_X_TB_BR $sent_http_X_TB_BRV"
 * 示例:
 * 
 * 211.137.185.10||66704||1522||2013:05:16:14:59:50||POST
 * http://b.m.taobao.com/buy.htm?sid=
 * 1ebd8c50bcc0219f3b92f0e6eae61754||200||11732||http://a.m.taobao.com/i17559094168.htm?sid=1ebd8c50bcc0219f3b92f0e6eae61754&abtest=9||MQQBrowser/3.4
 * (Nokia5236;SymbianOS/9.1
 * Series60/3.0)||du=1640845639||ea=-;-;-;-||pp=-||Nokia
 * ||5236||-||-||-||-||-||Symbian OS||-||abtest=-||-||-||QQ -

 * 
 * 
 * 
 * 分析目标是： 获取PV的流量 时间 大小 "C-200","C-302","c-other"
 * 
 * 通过IP 获取区域信息和网络信息
 * 
 * 自身URL(TOP10) 信息和refer (TOP20)信息
 * 
 * @author wb-lixing 2013-05-16 15:08
 */
public class WirelessLogAnalyse extends AbstractDataAnalyse {

	private static final Logger logger = Logger.getLogger(ApacheLogJob.class);

	private SimpleDateFormat rTimeFormat = new SimpleDateFormat(
			"yyyy:MM:dd:HH:mm", Locale.ENGLISH);

	public WirelessLogAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
	}

	public void analyseOneLine(String line) {
		try {
			logger.warn("---------line: "+line);
			if (line.indexOf("status.taobao") > 0) {
				return;
			}
			
		//	String[] tmp = StringUtils.splitPreserveAllTokens(line, "||");
   String[] tmp = line.split("\\|\\|");
			// String[] p1 =StringUtils.splitByWholeSeparator( tmp[0], " ");

			String ip = tmp[0];
			ip = ip.trim();

			// response time
			int rest = Integer.parseInt(tmp[1]);
			// 请求发生的时间
			String time = tmp[3];
			String source_url = tmp[4];
			// $request_method http://$host$request_uri
			if (source_url != null) {
				String[] ss = source_url.split(" ");
				if (ss.length == 2) {
					source_url = ss[1];
				}
			} else {
				source_url = "null";
			}
			
			String httpCode = tmp[5];
			String pagesize = tmp[2];
			String refer_url = tmp[7];
		//只截取分钟部分	2013:05:16:14:59:50
			Date collectTime = rTimeFormat.parse(time.substring(0, 15));
			long cTime = collectTime.getTime();


			analyseSource(cTime, rest, pagesize, httpCode, source_url);
			analyseIp(cTime, ip);
			analyseRefer(cTime, refer_url);
		} catch (Exception e) {
			logger.error("分析" + line + " " + this.getAppName(), e);
		}
	}

	public void submit() {

		if (sourceUrl.size() == 0) {

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(),
						cal.getTimeInMillis(),
						new String[] { KeyConstants.PV },
						new KeyScope[] { KeyScope.ALL },
						new String[] { "E-times" }, new Object[] { 0 },
						new ValueOperate[] { ValueOperate.ADD });
			} catch (Exception e) {
				logger.error("发送失败", e);
			}

			return;
		}

		Map<Long, PvInfo> allMap = new HashMap<Long, PvInfo>();

		for (Map.Entry<Long, Map<String, PvInfo>> entry : sourceUrl.entrySet()) {
			Long time = entry.getKey();
			Map<String, PvInfo> v = entry.getValue();
			for (Map.Entry<String, PvInfo> ventry : v.entrySet()) {
				PvInfo m = ventry.getValue();

				PvInfo all = allMap.get(time);
				if (all == null) {
					all = new PvInfo();
					allMap.put(time, all);
				}

				all.allPv += m.allPv;
				all.hitpv += m.hitpv;
				all.pv += m.pv;
				all.rt += m.rt;
				all.pagesize += m.pagesize;
				all.c200 += m.c200;
				all.c204 += m.c204;
				all.c304 += m.c304;
				all.rterror += m.rterror;
				all.rt100 += m.rt100;
				all.rt500 += m.rt500;
				all.rt1000 += m.rt1000;
				if (m.pv < 1) {
					continue;
				}
				String url = ventry.getKey();
				int rt = (int) (m.rt / m.pv / 1000l);
				int p = (m.pagesize / m.pv);

				try {
					Object[] values = new Object[] { m.allPv, rt, p, m.c200,
							m.c302, m.rterror, m.rt100, m.rt500, m.rt1000 };
					CollectDataUtilMulti.collect(getAppName(), getIp(), time,
							new String[] { KeyConstants.PV, url },
							new KeyScope[] { KeyScope.NO, KeyScope.ALL },
							new String[] { "E-times", "C-time", "P-size",
									"C-200", "C-302", "rt_error", "rt_100",
									"rt_500", "rt_1000" }, values,
							new ValueOperate[] { ValueOperate.ADD,
									ValueOperate.AVERAGE, ValueOperate.AVERAGE,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD });
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}

		for (Map.Entry<Long, PvInfo> entry : allMap.entrySet()) {
			try {
				PvInfo m = entry.getValue();
				if (m.pv == 0) {
					continue;
				}

				int rt = (int) (m.rt / m.pv / 1000l);
				int p = (m.pagesize / m.pv);
				HostPo host = CspCacheTBHostInfos.get()
						.getHostInfoByIp(getIp());
				String cm = "CMX";
				if (host != null) {
					cm = host.getHostSite().toUpperCase();
				}

				if ("detail".equals(getAppName())) {
					Object[] values = new Object[] { m.allPv, rt, p, m.c200,
							m.c302, m.rterror, m.rt100, m.rt500, m.rt1000,
							m.c204, m.hitpv, m.c304, m.allPv };
					CollectDataUtilMulti.collect(getAppName(), getIp(),
							entry.getKey(), new String[] { KeyConstants.PV },
							new KeyScope[] { KeyScope.ALL }, new String[] {
									"E-times", "C-time", "P-size", "C-200",
									"C-302", "rt_error", "rt_100", "rt_500",
									"rt_1000", "C-204", "pv-hit", "C-304",
									"pv_" + cm }, values, new ValueOperate[] {
									ValueOperate.ADD, ValueOperate.AVERAGE,
									ValueOperate.AVERAGE, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD });
				} else {
					Object[] values = new Object[] { m.allPv, rt, p, m.c200,
							m.c302, m.rterror, m.rt100, m.rt500, m.rt1000,
							m.allPv };
					CollectDataUtilMulti.collect(getAppName(), getIp(),
							entry.getKey(), new String[] { KeyConstants.PV },
							new KeyScope[] { KeyScope.ALL }, new String[] {
									"E-times", "C-time", "P-size", "C-200",
									"C-302", "rt_error", "rt_100", "rt_500",
									"rt_1000", "pv_" + cm }, values,
							new ValueOperate[] { ValueOperate.ADD,
									ValueOperate.AVERAGE, ValueOperate.AVERAGE,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD, ValueOperate.ADD,
									ValueOperate.ADD });
				}

			} catch (Exception e) {
				logger.error("发送失败", e);
			}
		}

		for (Map.Entry<Long, Map<String, Integer>> entry : referTimeMap
				.entrySet()) {
			Long time = entry.getKey();
			for (Map.Entry<String, Integer> ventry : entry.getValue()
					.entrySet()) {
				String refer = ventry.getKey();
				Integer count = ventry.getValue();
				if (count < 5) {// 如果总量少于10的直接放弃
					continue;
				}
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time,
							new String[] { KeyConstants.PV_REFER, refer },
							new KeyScope[] { KeyScope.NO, KeyScope.ALL },
							new String[] { "E-times" }, new Object[] { count });
				} catch (Exception e) {
					logger.error("发送失败", e);
				}

			}
		}

		Map<Long, Map<String, Integer>> timeallnetMap = new HashMap<Long, Map<String, Integer>>();

		for (Map.Entry<Long, Map<String, Map<String, Integer>>> entry : regionTimeMap
				.entrySet()) {
			Long time = entry.getKey();
			Map<String, Map<String, Integer>> netMap = entry.getValue();
			for (Map.Entry<String, Map<String, Integer>> netentry : netMap
					.entrySet()) {
				String net = netentry.getKey();

				for (Map.Entry<String, Integer> provinceEntry : netentry
						.getValue().entrySet()) {
					String province = provinceEntry.getKey();
					Integer count = provinceEntry.getValue();

					Map<String, Integer> allnetMap = timeallnetMap.get(time);
					if (allnetMap == null) {
						allnetMap = new HashMap<String, Integer>();
						timeallnetMap.put(time, allnetMap);
					}

					Integer allnet = allnetMap.get(net);
					if (allnet == null) {
						allnetMap.put(net, count);
					} else {
						allnetMap.put(net, count + allnet);
					}

					if (count < 5) {// 如果总量少于10的直接放弃
						continue;
					}
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(),
								time, new String[] { KeyConstants.PV_REGION,
										province }, new KeyScope[] {
										KeyScope.NO, KeyScope.APP },
								new String[] { "E-times" },
								new Object[] { count });
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
				}
			}
		}

		for (Map.Entry<Long, Map<String, Integer>> entry : timeallnetMap
				.entrySet()) {

			for (Map.Entry<String, Integer> k : entry.getValue().entrySet()) {

				if (k.getValue() < 5) {// 如果总量少于10的直接放弃
					continue;
				}

				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(),
							entry.getKey(), new String[] {
									KeyConstants.PV_NETWORK, k.getKey() },
							new KeyScope[] { KeyScope.NO, KeyScope.APP },
							new String[] { "E-times" },
							new Object[] { k.getValue() });
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}

	}

	//
	// Map<时间,Map<URL,int[]>>
	// int[0]=pv,int[1]=rest,int[2]=psize,int[3]=c200,int[4]=c302,int[5]=cother
	Map<Long, Map<String, PvInfo>> sourceUrl = new HashMap<Long, Map<String, PvInfo>>();

	/**
	 * 解析URL的流量信息
	 * 
	 * @param rest
	 * @param pagesize
	 * @param httpCode
	 * @param source_url
	 */
	public void analyseSource(long cTime, int rt, String pagesize,
			String httpCode, String source_url) {
		String url = AppUrlCache.get().translateUrl(source_url);

		int code = 0;
		try {
			code = Integer.parseInt(httpCode);
		} catch (Exception e) {
		}
		int psize = 0;
		try {
			psize = Integer.parseInt(pagesize);
		} catch (Exception e) {
		}
		Map<String, PvInfo> urlMap = sourceUrl.get(cTime);
		if (urlMap == null) {
			urlMap = new HashMap<String, PvInfo>();
			sourceUrl.put(cTime, urlMap);
		}

		PvInfo v = urlMap.get(url);
		if (v == null) {
			v = new PvInfo();
			urlMap.put(url, v);
		}


		v.allPv += 1;

		if (rt >= 1000000) {// 如果这个请求时间大于一分钟 ，就记录上问题请求
			v.rterror += 1;// 问题
		} else {
			if (500000 < rt && rt < 1000000) {
				v.rt1000 += 1;
			} else if (100000 < rt && rt < 500000) {
				v.rt500 += 1;
			} else if (rt < 100000) {
				v.rt100 += 1;
			}
			v.pv += 1;// pv量
			v.rt += rt;// 响应时间
			v.pagesize += psize;// 字节数
			if (code == 200) {
				v.c200 += 1;
			} else if (code == 302) {
				v.c302 += 1;
			} else if (code == 204) {
				v.c204 += 1;
			} else if (code == 304) {
				v.c304 += 1;
			}
		}
	}

	// Map<URL,Integer>
	Map<Long, Map<String, Integer>> referTimeMap = new HashMap<Long, Map<String, Integer>>();

	/**
	 * 分析来源URL
	 * 
	 * @param referUrl
	 */
	protected void analyseRefer(long cTime, String referUrl) {
		if (referUrl == null) {
			return;
		}

		Map<String, Integer> referMap = referTimeMap.get(cTime);
		if (referMap == null) {
			referMap = new HashMap<String, Integer>();
			referTimeMap.put(cTime, referMap);
		}

		String refer = AppUrlCache.get().translateUrl(referUrl);

		Integer count = referMap.get(refer);
		if (count == null) {
			referMap.put(refer, 1);
		} else {
			referMap.put(refer, 1 + count);
		}
	}

	// Map<network,Map<province,Map<city,Integer>>>
	private Map<Long, Map<String, Map<String, Integer>>> regionTimeMap = new HashMap<Long, Map<String, Map<String, Integer>>>();

	/**
	 * 分析 ip的地区和网络 ，结果保存在regionMap中
	 * 
	 * @param ip
	 */
	protected void analyseIp(long cTime, String ip) {

		Map<String, Map<String, Integer>> regionMap = regionTimeMap.get(cTime);

		IpRegion region = IpRegionCache.getIpRegion(ip);
		String network = "未知";
		String province = "未知";

		if (region != null) {
			network = region.getNetwork();
			province = region.getProvince();
		}
		if (regionMap == null) {
			regionMap = new HashMap<String, Map<String, Integer>>();
			regionTimeMap.put(cTime, regionMap);
		}

		Map<String, Integer> netMap = regionMap.get(network);
		if (netMap == null) {
			netMap = new HashMap<String, Integer>();
			regionMap.put(network, netMap);
		}

		Integer count = netMap.get(province);
		if (count == null) {
			netMap.put(province, 1);
		} else {
			netMap.put(province, 1 + count);
		}
	}

	public static void main(String[] args) {
		String line=" 171.116.228.146||74238||2109||2013:05:18:20:38:57||POST http://b.m.taobao.com/buy.htm?sid=6aa0609afbce72847c9595a7f6a1dbb5||200||2840||http://b.m.taobao.com/buy.htm?sid=6aa0609afbce72847c9595a7f6a1dbb5&pds=buynow%23h%23detail||MQQBrowser/3.7/Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; fulinpengda_3m Build/GRK39F) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1||du=1686184847||ea=-;-;-;aXBob25l||pp=-||UCWEB||UCWEB||-||-||-||-||-||UCWEB||-||abtest=-||-||-||- -";
		String[] ss = line.split("\\|\\|");
	System.out.println(Arrays.toString(ss));
String a="a,b,c";
System.out.println(Arrays.toString(a.split(",")));
	}

	@Override
	public void release() {
		regionTimeMap.clear();
		sourceUrl.clear();
		referTimeMap.clear();
	}

}