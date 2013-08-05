package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.Constants;

public class HuijinMymallAnalyse extends AbstractDataAnalyse {
	private static final Logger logger = Logger.getLogger(HuijinMymallAnalyse.class);
	SimpleDateFormat collectTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public HuijinMymallAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void analyseOneLine(String line) {
		try {
			if (result == null) {
				result = new Result();
			}
			Matcher m = LINE_PATTERN.matcher(line);
			if (m.find()) {
				// 2 3
				// flag, 用户id, 订购类型, 订购id [,额外信息]
				String flag = m.group(2);
				String extra = m.group(3);
				result.set(flag, extra);
			}
		} catch (Exception e) {
			// do sth.
		}		
	}

	@Override
	public void release() {
		result = null;
		lastParseTime = null;
		
	}

	@Override
	public void submit() {
		if (result == null) {
			return;
		}
		HashMap<String, Long> data = result.getData();
		for (Map.Entry<String, Long> entry : data.entrySet()) {
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), new Date().getTime(), new String[]{KeyConstants.HJ_MYMALL, entry.getKey()},
						new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"Huijin-mall"}, 
						new Object[]{entry.getValue(),},new ValueOperate[]{ValueOperate.ADD});
			} catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
	}
	
	
	/* many members are package-visible for test inspect */

	static class Result {
		HashMap<String, Long> data = new HashMap<String, Long>();
		long open_time_total = 0L;
		long open_count = 0L;

		/**
		 * @param extra
		 *            "" 或者 ",1234" ...
		 */
		void set(String flag, String extra) {
			boolean flagChecked = inc("Sub_error", flag, "订购异常数_" + Constants.COUNT_TIMES_FLAG)
					|| inc("user_limit", flag, "用户因销售限制不能订购_" + Constants.COUNT_TIMES_FLAG)
					|| inc("order_close", flag, "订单非正常关闭数_" + Constants.COUNT_TIMES_FLAG) || inc("refund_error", flag, "退款金额异常数_" + Constants.COUNT_TIMES_FLAG)
					|| inc("payError_aplipay", flag, "充值成功订购失败记录数_" + Constants.COUNT_TIMES_FLAG)
					|| inc("user_error", flag, "用户跳转异常页面数_" + Constants.COUNT_TIMES_FLAG)
					|| inc("user_success", flag, "用户中转正常页面数_" + Constants.COUNT_TIMES_FLAG);
			if (!flagChecked && "open_time".equalsIgnoreCase(flag)) {
				open_time_total += Long.valueOf(extra.substring(1));
				open_count += 1;
			}
		}

		/**
		 * 如果 flag1 == flag2, key 对应的值 +1
		 */
		boolean inc(String flag1, String flag2, String key) {
			if (!flag1.equalsIgnoreCase(flag2))
				return false;
			Long v = (Long) data.get(key);
			v = (v == null) ? 1L : v + 1L;
			data.put(key, v);
			return true;
		}

		HashMap<String, Long> getData() {
			return data;
		}
	}
	
	
	Result result = null;

	String lastParseTime = null;

	final static Pattern TIME_PATTERN = Pattern.compile("(\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d)");

	final static Pattern LINE_PATTERN = Pattern.compile(TIME_PATTERN +
	/* flag, 用户id, 订购类型, 订购id [,额外信息] */
	/* NOTE 为匹配效率考虑，最后一项没有使用 ",(\\w+)?" */
	":\\d\\d,mymall_(\\w+),\\d+,\\w+,\\d+(.*)");

	protected String parseLogLineCollectTime(String line) {
		Matcher m = TIME_PATTERN.matcher(line);
		return m.find() ? m.group(1) : null;
	}

	protected String parseLogLineCollectDate(String line) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

}
