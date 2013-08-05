package com.taobao.monitor.alarm.mail;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.web.ao.MonitorStatisticsDataAo;
import com.taobao.monitor.web.po.MonitorStatisticsDataPo;



public class UrlSendEmailJob implements Job {
	
	/***
	 * data need to be send
	 * String is the key which is keyId
	 * Long is the time
	 * Double is the value
	 */
	private static Map<String, Map<Long, Double>> nowData;
	private static Map<String, Map<Long, Double>> weekData;
	
	private static String host;
	private static String from;
	private static String to;
	private static String port;
	private static Email_Autherticator authentication;
	
	/*** 需要发邮件的key与他们的名称 **/
	private static Map<String,String> keyNames = new HashMap<String, String>();
	
	/*** 存放key对应的图片名称 **/
	private static Map<String, String> keyChartNames = new HashMap<String, String>();
	
	/*** 按key存放当日总pv  **/
	private static Map<String, Integer> totalPvNow = new HashMap<String, Integer>();
	private static Map<String, Integer> totalPvWeek = new HashMap<String, Integer>();
	
	/*** 按key存放统计的机器数  **/
	private static Map<String, Integer> siteCounts = new HashMap<String, Integer>();
	
	/*** 各个key的波动比例 (星期) **/
	private static List<Map.Entry<String, Integer>> buyWaveEntriesWeek = new ArrayList<Map.Entry<String,Integer>>();
	private static List<Map.Entry<String, Integer>> orderWaveEntriesWeek = new ArrayList<Map.Entry<String,Integer>>();
	private static List<Map.Entry<String, Integer>> cartWaveEntriesWeek = new ArrayList<Map.Entry<String,Integer>>();
	
	/*** 各个key的波动比例 (当日) **/
	private static List<Map.Entry<String, Integer>> buyWaveEntriesDay = new ArrayList<Map.Entry<String,Integer>>();
	private static List<Map.Entry<String, Integer>> orderWaveEntriesDay = new ArrayList<Map.Entry<String,Integer>>();
	private static List<Map.Entry<String, Integer>> cartWaveEntriesDay = new ArrayList<Map.Entry<String,Integer>>();
	
	/*** key定义  **/
	private static final String BUY_KEY = "60968";
	private static final String ORDER_KEY = "60873";
	private static final String CART_KEY = "65398";

	private static final Logger logger = Logger.getLogger(UrlSendEmailJob.class);
	
	private static Object lock = new Object();
	
	static {
		Properties pro = new Properties();
		InputStream in = UrlSendEmailJob.class.getClassLoader().getResourceAsStream("mail.properties");
		try {
			pro.load(in);
			
			host = pro.getProperty("host");
			from = pro.getProperty("from");
			to = pro.getProperty("to");
			port = pro.getProperty("port");
			String user = pro.getProperty("user");
			String pwd = pro.getProperty("pwd");
			authentication = new Email_Autherticator(user, pwd);
		} catch (IOException e) {
			logger.error("邮件配置解析错误", e);
		}
		
		// 初始化key
		keyNames.put(BUY_KEY, "Buy Now");
		keyNames.put(ORDER_KEY, "Confirm Order");
		keyNames.put(CART_KEY, "Add Cart");
		
		// 初始化要生成的图片名称
		keyChartNames.put(BUY_KEY, "buy");
		keyChartNames.put(ORDER_KEY, "order");
		keyChartNames.put(CART_KEY, "cart");
		
		// 初始化当天数据的map
		nowData = new HashMap<String, Map<Long,Double>>();
		nowData.put(BUY_KEY, new HashMap<Long, Double>(2000));
		nowData.put(ORDER_KEY, new HashMap<Long, Double>(2000));
		nowData.put(CART_KEY, new HashMap<Long, Double>(2000));
		
		// 初始化上一星期数据的map
		weekData = new HashMap<String, Map<Long,Double>>();
		weekData.put(BUY_KEY, new HashMap<Long, Double>(2000));
		weekData.put(ORDER_KEY, new HashMap<Long, Double>(2000));
		weekData.put(CART_KEY, new HashMap<Long, Double>(2000));
		
		// 初始化当天总pv
		totalPvNow.put(BUY_KEY, 0);
		totalPvNow.put(ORDER_KEY, 0);
		totalPvNow.put(CART_KEY, 0);
		
		// 初始化上个星期总pv
		totalPvWeek.put(BUY_KEY, 0);
		totalPvWeek.put(ORDER_KEY, 0);
		totalPvWeek.put(CART_KEY, 0);
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("enter send import url mail job");
		if (isDevEnviroment()) return;
		
		synchronized (lock) {
			//mockTestData();
			prepareDate();
			
			prepareWaveData();
			generateChart();
			sendMail();
			// 每天发完邮件清理当天数据
			clear();
		}
		
		logger.info("finish send import url mail job");
		
	}
	

	
	private void prepareDate() {
		logger.info("enter prepare data");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date beginNow = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date endNow = calendar.getTime();
		
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		Date endWeek = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date beginWeek = calendar.getTime();
		
		prepareDataIntel(330, 60968, beginNow, endNow, true);
		prepareDataIntel(330, 60873, beginNow, endNow, true);
		prepareDataIntel(341, 65398, beginNow, endNow, true);
		
		prepareDataIntel(330, 60968, beginWeek, endWeek, false);
		prepareDataIntel(330, 60873, beginWeek, endWeek, false);
		prepareDataIntel(341, 65398, beginWeek, endWeek, false);
		
		logger.info("parepare data finished");
	}
	
	private void prepareWaveData() {
		logger.info("enter prepareWaveData");
		prepareWaveDataIntel(BUY_KEY, buyWaveEntriesWeek, buyWaveEntriesDay);
		prepareWaveDataIntel(ORDER_KEY, orderWaveEntriesWeek, orderWaveEntriesDay);
		prepareWaveDataIntel(CART_KEY, cartWaveEntriesWeek, cartWaveEntriesDay);
		logger.info("finish prepareWaveData");
	}
	
	private void prepareWaveDataIntel(String keyId, List<Map.Entry<String, Integer>> waveEntriesWeek,
			List<Map.Entry<String, Integer>> waveEntriesDay) {
		Map <String, Integer> originDataWeek = new HashMap<String, Integer>();
		Map <String, Integer> originDataDay = new HashMap<String, Integer>();
		
		Map<Long, Double> dataNow = nowData.get(keyId);
		
		Map<Long, Double> dataWeek = weekData.get(keyId);
		
		for (Map.Entry<Long, Double> entry : dataNow.entrySet()) {
			Long key = entry.getKey();
			Date date = new Date(key.longValue());
			SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
			int hour = Integer.valueOf(hourFormat.format(date));
			if ((hour >= 0 && hour <=8) || (hour == 23)) {
				continue;
			}
			
			Double nowValue = entry.getValue();
			
			// deal with week wave
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			String dateKey = format.format(date);
			
			Double weekValue = dataWeek.get(key);
			if (weekValue == null || weekValue == 0) {
				originDataWeek.put(dateKey,100);
			} else {
				originDataWeek.put(dateKey, (int)(nowValue/weekValue*100));
			}
			
			// deal with minute wave
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, -1);
			Long minuteKey = calendar.getTimeInMillis();
			Double miniteValue = dataNow.get(minuteKey);
			if (miniteValue == null || miniteValue == 0) {
				originDataDay.put(dateKey,100);
			} else {
				originDataDay.put(dateKey, (int)(nowValue/miniteValue*100));
			}
		}
		
		waveEntriesWeek.addAll(originDataWeek.entrySet());
		waveEntriesDay.addAll(originDataDay.entrySet());
		sorterWaves(waveEntriesWeek);
		sorterWaves(waveEntriesDay);	
	}
	
	private void sorterWaves( List<Map.Entry<String, Integer>> waveEntries) {
		Collections.sort(waveEntries, new Comparator<Map.Entry<String, Integer>>(){

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}});
	}
	
	private void prepareDataIntel(int appId, int keyId, Date beginDate, Date endDate, boolean isNowData) {
		logger.info("enter prepareDataIntel appId :" + appId + " keyId:" + keyId + "beginDate" + beginDate + "endDate" + endDate);
		List <MonitorStatisticsDataPo> poList = MonitorStatisticsDataAo.get().findStatisticsData(appId, keyId, beginDate, endDate);
		
		Calendar calendar = Calendar.getInstance();
		for(MonitorStatisticsDataPo po : poList) {
			Date date = po.getCollectTime();
			calendar.setTime(date);
			calendar.set(Calendar.YEAR, 2000);
			calendar.set(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			int siteCount = po.getSiteCount() == 0 ? 1 :  po.getSiteCount();
			double averageValue = new BigDecimal(String.valueOf(po.getTotalData())).divide(new BigDecimal(siteCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			Integer existSiteCount = siteCounts.get(String.valueOf(keyId));
			if (existSiteCount == null || existSiteCount < siteCount) {
				siteCounts.put(String.valueOf(keyId), siteCount);
			}
			
			if (nowData.get(String.valueOf(keyId)) == null || weekData.get(String.valueOf(keyId)) == null) continue;
			if (totalPvNow.get(String.valueOf(keyId)) == null || totalPvWeek.get(String.valueOf(keyId)) == null) continue;
			
			if (isNowData) {
				nowData.get(String.valueOf(keyId)).put(calendar.getTimeInMillis(), averageValue);
				Double totalPv = totalPvNow.get(String.valueOf(po.getKeyId())) + averageValue;
				totalPvNow.put(String.valueOf(po.getKeyId()), (int)totalPv.doubleValue());
			} else {
				weekData.get(String.valueOf(keyId)).put(calendar.getTimeInMillis(), averageValue);
				Double totalPv = totalPvWeek.get(String.valueOf(po.getKeyId())) + averageValue;
				totalPvWeek.put(String.valueOf(po.getKeyId()), (int)totalPv.doubleValue());
			}
			
		}
		logger.info("finish prepareDataIntel appId :" + appId + " keyId:" + keyId + "beginDate" + beginDate + "endDate" + endDate);
	}
	
	private void clear() {
		logger.info("enter clear");
		for (Map.Entry<String, Map<Long, Double>> entry : nowData.entrySet()) {
			Map<Long, Double> valueMap = entry.getValue(); 
			valueMap.clear();
			totalPvNow.put(entry.getKey(), 0);
		}
		for (Map.Entry<String, Map<Long, Double>> entry : weekData.entrySet()) {
			Map<Long, Double> valueMap = entry.getValue(); 
			valueMap.clear();
			totalPvWeek.put(entry.getKey(), 0);
		}
		
		buyWaveEntriesWeek.clear();
		orderWaveEntriesWeek.clear();
		cartWaveEntriesWeek.clear();
		
		buyWaveEntriesDay.clear();
		orderWaveEntriesDay.clear();
		cartWaveEntriesDay.clear();
		
		logger.info("finish clear");
	}
	
	/***
	 * 数据转换成图片
	 */
	private void generateChart() {
		logger.info("enter generateChart");
		for (Map.Entry<String, String> entry : keyChartNames.entrySet()) {
			String keyId = entry.getKey();
			Map<Long, Double> nowValue = nowData.get(keyId);
			Map<Long, Double> weekValue = weekData.get(keyId);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			Date now = calendar.getTime();
			String line_1 =  simpleDateFormat.format(now);
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			Date week = calendar.getTime();
			String line_2 = simpleDateFormat.format(week);

			String titleMessage = keyNames.get(keyId) + " pv chart ";
			TimeSeriesCollection lineDataSet = new TimeSeriesCollection();
			TimeSeries timeSeries_1 = new TimeSeries(line_1, Minute.class);
			TimeSeries timeSeries_2 = new TimeSeries(line_2, Minute.class);
			
			if (nowValue != null) {
				for (Map.Entry<Long, Double> timeEntry : nowValue.entrySet()) {
					long time = timeEntry.getKey();
					double value = timeEntry.getValue();
					Date date = new Date(time);
					RegularTimePeriod period = RegularTimePeriod.createInstance(Minute.class, date, RegularTimePeriod.DEFAULT_TIME_ZONE);
					timeSeries_1.add(period, value);
				}
			}
			
			if (weekValue != null) {
				for (Map.Entry<Long, Double> timeEntry : weekValue.entrySet()) {
					long time = timeEntry.getKey();
					double value = timeEntry.getValue();
					Date date = new Date(time);
					RegularTimePeriod period = RegularTimePeriod.createInstance(Minute.class, date, RegularTimePeriod.DEFAULT_TIME_ZONE);
					timeSeries_2.add(period, value);
				}
			}
			
			lineDataSet.addSeries(timeSeries_1);
			lineDataSet.addSeries(timeSeries_2);
			
			JFreeChart chart = ChartFactory.createTimeSeriesChart("title", "Time", "Average PV", lineDataSet, true, true, true);
			
			// 设置标题
			TextTitle title = new TextTitle(titleMessage, new Font("黑体", Font.BOLD, 12));
			chart.setTitle(title);
			chart.setAntiAlias(true);
			
			String fileName = keyChartNames.get(keyId);
			try {
				 ChartUtilities.saveChartAsPNG(new File(fileName), chart, 1000, 500);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info("finishi generateChart");
	}
	
	private void sendMail() {
		logger.info("enter sendMail");
		boolean isOk = checkParamaters();
		if (!isOk) {
			logger.error("邮件配置解析错误");
			return;
		}
		
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getDefaultInstance(props, authentication);
		MimeMessage message = new MimeMessage(session);	
		try {
			message.setFrom(new InternetAddress(from));
			
			StringTokenizer tokenizer = new StringTokenizer(to, ";");
			while (tokenizer.hasMoreTokens()) {
				String toAddress = tokenizer.nextToken();
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			}
			
			message.setSubject("重要url走势图（供参考）");
			String body = getBody();
			
			MimeMultipart multipart = new MimeMultipart("related");  
			MimeBodyPart htmlBodyPart = new MimeBodyPart();  
			htmlBodyPart.setContent(body, "text/html;charset=gbk"); 
			multipart.addBodyPart(htmlBodyPart);  
			
			for (Map.Entry<String, String> entry : keyChartNames.entrySet()) {
				String keyId = entry.getKey();
				String fileName = keyChartNames.get(keyId);
				MimeBodyPart jpgBodyPart = new MimeBodyPart();  
	
				FileDataSource fds_1 = new FileDataSource(fileName); 
				jpgBodyPart.setDataHandler(new DataHandler(fds_1));  
				jpgBodyPart.setContentID(fileName + "_png"); 
				multipart.addBodyPart(jpgBodyPart); 
			}
			
			message.setContent(multipart);
			message.saveChanges();
			Transport.send(message);
		} catch (AddressException e) {
			logger.error("邮件异常",e);
		} catch (MessagingException e) {
			logger.error("邮件异常",e);
		}
		logger.info("finish sendMail");
	}
	
	private String getBody() {
		
		StringBuffer body = new StringBuffer();
		
		body.append(getBasicInfo());
		
		body.append("</br>");
		
		body.append(getWavesInfo("订单确认10大波动点", orderWaveEntriesWeek, orderWaveEntriesDay));
		
		body.append("</br>");
		
		body.append(getWavesInfo("立即购买10大波动点", buyWaveEntriesWeek, buyWaveEntriesDay));
		
		body.append("</br>");
		
		body.append(getWavesInfo("加入购物车10大波动点", cartWaveEntriesWeek, cartWaveEntriesDay));
		
		body.append("</br>");
		
		body.append(getChartInfo());
		
		return body.toString();
	}	
	
	private String getBasicInfo() {
		StringBuffer body = new StringBuffer();
		
		body.append("<table border=\"1\" width=\"1000\">\n");
		body.append("<tr><td colspan=\"4\" align=\"center\"><font size=\"6\">基本信息</font></td></tr>\n");
		body.append("<tr><td>页面类型</td><td>图表展示方式</td><td>当日均值总和</td><td>上个星期均值总和</td></tr>\n");
		body.append("<tr><td>订单确认</td><td>").append(siteCounts.get(ORDER_KEY)).append("台服务器均值</td><td>").append(totalPvNow.get(ORDER_KEY)).append("</td><td>" + totalPvWeek.get(ORDER_KEY)).append("</td></tr>\n");
		body.append("<tr><td>立即购买</td><td>").append(siteCounts.get(BUY_KEY)).append("台服务器均值</td><td>").append(totalPvNow.get(BUY_KEY)).append("</td><td>" + totalPvWeek.get(BUY_KEY)).append("</td></tr>\n");
		body.append("<tr><td>加入购物车</td><td>").append(siteCounts.get(CART_KEY)).append("台服务器均值</td><td>").append(totalPvNow.get(CART_KEY)).append("</td><td>" + totalPvWeek.get(CART_KEY)).append("</td></tr>\n");
		body.append("</table>");
		
		return body.toString();
	}
	
	private String getWavesInfo(String title, List<Map.Entry<String, Integer>> waveWeek,
			List<Map.Entry<String, Integer>> waveDay) {
		StringBuffer body = new StringBuffer();
		
		body.append("<table border=\"1\" width=\"1000\">\n");
		body.append("<tr><td colspan=\"11\" align=\"center\"><font size=\"6\">").append(title).append("</font></td></tr>\n");
		
		body.append("<font color=\"red\">");
		body.append("<tr>");
		body.append("<font color=\"black\"><td rowspan=\"4\">星期</td></font>");
		for (int i = 0; i < 10; i++) {
			String value = "no data";
			if (waveWeek.size() > i) {
				value = waveWeek.get(i).getKey();
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("<tr>");
		for (int i = 0; i < 10; i++) {
			String value = "no data";
			if (waveWeek.size() > i) {
				value = waveWeek.get(i).getValue() + "%";
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("</font>");
		body.append("<font color=\"green\">");
		body.append("<tr>");
		for (int i = waveWeek.size(); i > waveWeek.size() - 10; i--) {
			String value = "no data";
			if (i > 0) {
				value = waveWeek.get(i - 1).getKey();
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("<tr>");
		for (int i = waveWeek.size(); i > waveWeek.size() - 10; i--) {
			String value = "no data";
			if (i > 0) {
				value = waveWeek.get(i - 1).getValue() + "%";
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("</font>");
		
		body.append("<font color=\"red\">");
		body.append("<tr>");
		body.append("<font color=\"black\"><td rowspan=\"4\">分钟</td></font>");
		for (int i = 0; i < 10; i++) {
			String value = "no data";
			if (waveDay.size() > i) {
				value = waveDay.get(i).getKey();
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("<tr>");
		for (int i = 0; i < 10; i++) {
			String value = "no data";
			if (waveDay.size() > i) {
				value = waveDay.get(i).getValue() + "%";
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("</font>");
		body.append("<font color=\"green\">");
		body.append("<tr>");
		for (int i = waveDay.size(); i > waveDay.size() - 10; i--) {
			String value = "no data";
			if (i > 0) {
				value = waveDay.get(i - 1).getKey();
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("<tr>");
		for (int i = waveDay.size(); i > waveDay.size() - 10; i--) {
			String value = "no data";
			if (i > 0) {
				value = waveDay.get(i - 1).getValue() + "%";
			}
			body.append("<td>").append(value).append("</td>");
		}
		body.append("</tr>");
		body.append("</font>");
		
		body.append("</table>");
		
		return body.toString();
	}
	
	private String getChartInfo() {
		StringBuffer body = new StringBuffer();
		
		body.append("<table width=\"1000\">");
		for (Map.Entry<String, String> entry : keyChartNames.entrySet()) {
			String keyId = entry.getKey();
			String chartName = keyChartNames.get(keyId);
			body.append("<tr><td>&nbsp;</td></tr>");
			body.append("<tr><td>&nbsp;</td></tr>");
			body.append("<tr width=\"100%\"><td width=\"100%\" align=\"left\"><img src = \"cid:").append(chartName).append("_png\" /> </td></tr>");;

		}
		body.append("</table>");
		
		return body.toString();		
	}
	
	private static boolean  checkParamaters() {
		boolean isOk = (host != null && from != null && to != null && port != null && authentication.getStrUser() != null && authentication.getStrPwd() != null);
		return isOk;
	}
	
   public static void main(String args[]) throws Exception {
	   //mockTestData();
	   UrlSendEmailJob job = new UrlSendEmailJob();
	   job.execute(null);
	}
	
    /***
     * 模拟数据，用于测试
     */
	public static void mockTestData() {
		try 
		{
			Calendar calendar = Calendar.getInstance();
			Date now = calendar.getTime();
			long times = calendar.getTimeInMillis();
			double value = 500;
			Random random = new Random(47);

			OutputStream out = new FileOutputStream(new File("D:\\tmp2\\a.txt"));
			OutputStreamWriter oWriter = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter writer = new BufferedWriter(oWriter);

			writer.write("now data \n");
			for (Map.Entry<String, String> entry : keyChartNames.entrySet()) {

				String keyId = entry.getKey();
				Map<Long, Double> data = new HashMap<Long, Double>();
				for (int i = 0; i < 1500; i++) {
					data.put(times, value);
					writer.write(calendar.getTime() + ":" + value + "\n");
					calendar.add(Calendar.MINUTE, -1);
					times = calendar.getTimeInMillis();
					int a = random.nextInt() % 2;
					int b = random.nextInt(100);
					if (a == 0 && value > b) {
						value = (value - b);
					} else {
						value = (value + b);
					}
				}
				nowData.put(keyId, data);
				calendar.setTime(now);
			}

			writer.write("week data \n");
			for (Map.Entry<String, String> entry : keyChartNames.entrySet()) {

				String keyId = entry.getKey();
				Map<Long, Double> data = new HashMap<Long, Double>();
				for (int i = 0; i < 1500; i++) {
					writer.write(times + ":" + value + "\n");
					data.put(times, value);
					calendar.add(Calendar.MINUTE, -1);
					times = calendar.getTimeInMillis();
					int a = random.nextInt() % 2;
					int b = random.nextInt(100);
					if (a == 0 && value > b) {
						value = (value - b);
					} else {
						value = (value + b);
					}
				}
				weekData.put(keyId, data);
				calendar.setTime(now);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isDevEnviroment() {
		boolean isDev = false;
		String os = System.getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("windows") != -1) {
			isDev = true;
		}
		return isDev;
	}
	
}
