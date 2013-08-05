package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.Constants;

public class HuijinRatingAnalyse extends AbstractDataAnalyse {
	private static final Logger logger = Logger.getLogger(HuijinMymallAnalyse.class);
	public HuijinRatingAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
	}
	
	private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d)");

	private static final Pattern LINE_PATTERN = Pattern.compile(TIME_PATTERN + ":\\d\\d,impact_dtl,(.+)");

	private final SimpleDateFormat collectTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	// 佣金额统计结果
	static class Sum {
		// 结构均为 count, amount
		private double[] point = new double[2];
		private double[] ka = new double[2];
		private double[] kaRate = new double[2];
		private double[] online = new double[2];
		private double[] onlineRate = new double[2];

		// 设值
		void set(String kind1, String kind2, double amount) {
			if (kind1.equals("B2C_CMI_ONLINE")) {
				if (kind2.endsWith("10003")) {
					inc(onlineRate, amount);
				} else if (kind2.endsWith("90001") || kind2.endsWith("90002")) {
					inc(online, amount);
				}
			} else if (kind1.equals("B2C_CMI_KA")) {
				if (kind2.endsWith("10003")) {
					inc(kaRate, amount);
				} else if (kind2.endsWith("90001") || kind2.endsWith("90002")) {
					inc(ka, amount);
				}
			} else if (kind1.equals("POINT")) {
				if (kind2.endsWith("10003")) {
					inc(point, amount);
				}
			}
		}

		// 取值
		String[][] get() {
			/* TODO 注释掉的 4 项需要更改日志配置才能获得准确数值 */
			return new String[][] { makePair("积分笔数", point[0]), makePair("积分金额", point[1]),
			// makePair("KeyAccount交易笔数", ka[0]),
					makePair("KeyAccount交易额", ka[1]),
					// makePair("KeyAccount佣金笔数", kaRate[0]),
					makePair("KeyAccount佣金额", kaRate[1]),
					// makePair("Online交易笔数", online[0]),
					// makePair("Online交易额", online[1]),
					makePair("Online佣金笔数", onlineRate[0]), makePair("Online佣金额", onlineRate[1]) };
		}

		private void inc(double[] arr, double amount) {
			arr[0] = arr[0] + 1;
			arr[1] = arr[1] + amount;
		}

		private String[] makePair(String key, double value) {
			return new String[] { "OTHER_汇金计费统计_" + key + "_" + Constants.COUNT_TIMES_FLAG, String.valueOf((long) value) };
		}
	}

	private Sum sum = new Sum();

	protected String parseLogLineCollectTime(String line) {
		Matcher m = TIME_PATTERN.matcher(line);
		return m.find() ? m.group(1) : null;
	}

	protected String parseLogLineCollectDate(String ling) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	@Override
	public void analyseOneLine(String line) {
		Matcher m = LINE_PATTERN.matcher(line);
		if (m.find()) {
			String[] tokens = m.group(2).split(",");
			if (tokens.length == 8) {
				String kind1 = tokens[1];
				String kind2 = tokens[4];
				double amount = Double.parseDouble(tokens[5]);
				sum.set(kind1, kind2, amount);
			}
		}
	}

	@Override
	public void release() {
		sum = null;
	}

	@Override
	public void submit() {
		for (String[] pair : sum.get()) {
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), new Date().getTime(), new String[]{KeyConstants.HJ_RATE, pair[0]},
						new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"Huijin-rate"}, 
						new Object[]{Long.parseLong(pair[1])},new ValueOperate[]{ValueOperate.ADD});
			} catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
	}
	
	public static void main(String[] args) {
		HuijinRatingAnalyse analyse = new HuijinRatingAnalyse("aaaa", "25.363.2.3","");
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("d:/test/log.txt")));
			String line;
			while ((line = br.readLine()) != null) {
				analyse.analyseOneLine(line);
			}
			
			analyse.submit();
			analyse.release();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
