package com.taobao.csp.cost.web.action;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.cost.po.CostAppTotal;
import com.taobao.csp.cost.po.CostTreePo;
import com.taobao.csp.cost.po.HostCostDetail;
import com.taobao.csp.cost.service.AppCostService;
import com.taobao.csp.cost.service.HostCostService;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.CustomDateUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.DateUtil;

/**
 * 显示应用的容量信息
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-8-30
 */
@Controller
@RequestMapping("/appCost.do")
public class CostAppAction {

	private static Logger logger = Logger.getLogger(CostAppAction.class);
	@Resource(name = "appCostService")
	private AppCostService appCostService;
	@Resource(name = "hostCostService")
	private HostCostService hostCostService;
	

	/**
	 * 显示单机的成本信息
	 * 
	 * @param appName
	 * @param dateStr
	 * @return
	 */
	@RequestMapping(params = "method=showApp")
	public ModelAndView showCapacityCostListNormal(String appName,String dateStr) {
		ModelAndView view = new ModelAndView("/cost/cost_index");
		
		if(appName==null || appName.trim().equals("")){
			appName="detail";
		}
		Date date=new Date();
		if(StringUtils.isNotBlank(dateStr)){
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}
		view.addObject("appName", appName);
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		boolean isOk=false;
		Calendar calNow = Calendar.getInstance();
		int nowday=calNow.get(Calendar.DAY_OF_YEAR);
		int thatDay=cal.get(Calendar.DAY_OF_YEAR);
		//指定的时间比当前时间大，取当前时间
		if( nowday<thatDay){
			cal.setTime(new Date());
		}else if(nowday==thatDay){
			isOk=true;
		}
		
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
		
		/**
		 * 周三或者周6
		 * 双11特殊
		 */
		if(StringUtils.isBlank(dateStr) || !dateStr.equals("2012-11-11")){
			if(dayOfWeek==Calendar.THURSDAY ||(isOk && dayOfWeek==Calendar.SATURDAY)
					|| dayOfWeek==Calendar.FRIDAY){
				cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			}
			else if(dayOfWeek==Calendar.SUNDAY || dayOfWeek==Calendar.MONDAY  
					|| dayOfWeek==Calendar.TUESDAY || 
							(isOk &&dayOfWeek==Calendar.WEDNESDAY)){
				//上周的周六
				int i=cal.get(Calendar.WEEK_OF_MONTH);
				cal.set(Calendar.WEEK_OF_MONTH, i-1);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			}
		}
		
		dateStr=DateUtil.getDateYMDFormat().format(cal.getTime());
		view.addObject("dateStr", dateStr);
		
		AppInfoPo appInfo = AppInfoAo.get().getAppInfoByOpsName(appName);
		view.addObject("appId", appInfo.getAppId());
		//详细信息
		view.addObject("appInfos", appCostService.getAppSummary(appName,cal.getTime()));
	
		return view;
	} 
	
	public static void main(String[] agrs) throws ParseException{
		Date date=new Date();
		String dateStr="2012-11-26";
		if(StringUtils.isNotBlank(dateStr)){
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		boolean isOk=false;
		Calendar calNow = Calendar.getInstance();
		int nowday=calNow.get(Calendar.DAY_OF_YEAR);
		int thatDay=cal.get(Calendar.DAY_OF_YEAR);
		//指定的时间比当前时间大，取当前时间
		if( nowday<thatDay){
			cal.setTime(new Date());
		}else if(nowday==thatDay){
			isOk=true;
		}
		
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
		
		if(StringUtils.isBlank(dateStr) || !dateStr.equals("2012-11-11")){
			if(dayOfWeek==Calendar.THURSDAY ||(isOk && dayOfWeek==Calendar.SATURDAY)
					|| dayOfWeek==Calendar.FRIDAY){
				cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			}
			else if(dayOfWeek==Calendar.SUNDAY || dayOfWeek==Calendar.MONDAY  
					|| dayOfWeek==Calendar.TUESDAY || 
							(isOk &&dayOfWeek==Calendar.WEDNESDAY)){
				//上周的周六
				int i=cal.get(Calendar.WEEK_OF_MONTH);
				cal.set(Calendar.WEEK_OF_MONTH, i-1);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			}
		}
		//System.out.println(cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println(cal.getTime());
	}
	
	/**
	 * 获取应用机器成本的详细信息
	 * 
	 * @param appName
	 * @return
	 */
	@RequestMapping(params = "method=showAppHosts")
	public ModelAndView showAppHosts(String appName) {
		ModelAndView view = new ModelAndView("/cost/cost_host_list");
		
		AppInfoPo appInfo = AppInfoAo.get().getAppInfoByOpsName(appName);
		
		view.addObject("appName", appName);
		view.addObject("appId", appInfo.getAppId());
		
		if(appInfo!=null){
			List<HostCostDetail> list = hostCostService.getAppHostDetail(appInfo.getOpsName());
			
			view.addObject("list", list);
		}
		
		return view;
	} 
	
	/**
	 * 编辑机器成本
	 * @param hostName
	 * @param cost
	 * @return
	 */
	@RequestMapping(params = "method=editHostCost")
	public ModelAndView showAppHosts(String hostName,String cost) {
		ModelAndView view = new ModelAndView("/ajax_result");
	
		if(hostName!=null || cost!=null){
			double doubleCost=0;
			try{
				doubleCost=Double.parseDouble(cost);
				doubleCost=Double.parseDouble(new DecimalFormat("#.#").format(doubleCost));

			}catch(NumberFormatException e){
				view.addObject("returnMsg","单价必须是数字");
			}
			if(doubleCost<100){
				view.addObject("returnMsg","数值必须大于100");
				return view;
			}
			//TODO 修改单击成本 先注释了
			//String returnMsg=hostCostService.editHostCost(hostName, doubleCost);
			String returnMsg="没球用";
			view.addObject("returnMsg",returnMsg);
		}else{
			view.addObject("returnMsg","参数不能为空");
		}
		return view;
	}
	
	/**
	 * 成本排行
	 * 
	 * @param appName
	 * @param dateStr
	 * @return
	 */
	@RequestMapping(params = "method=showTop")
	public ModelAndView showTop(String level,String appName,String dateStr,String requestType,
			String isForce) {
		ModelAndView view = new ModelAndView("/cost/cost_top_cost");
		
		if(StringUtils.isBlank(level)){
			level=CostType.GROUP_COUNT.name();
		}
		if(StringUtils.isBlank(appName)){
			appName="";
		}else{
			if(StringUtils.isBlank(requestType)){
				byte[] tb=null;
				try {
					tb = appName.getBytes("ISO-8859-1");
					appName=new String(tb,"gbk");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		Date date=new Date();
		
		if(dateStr!=null){
			try {
				date = new SimpleDateFormat("yyyy-MM").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}else{
			dateStr=getMonthDateFormat().format(date);
		}

		view.addObject("dateStr", dateStr);
	
		Date smonth=CustomDateUtil.getStartMonth(date);
		Date emonth=CustomDateUtil.getEndMonth(date);
		
		view.addObject("level",level);
		view.addObject("appName",appName);
	
		List<CostAppTotal> results=appCostService.getTopCost(level,appName,smonth,emonth,isForce);
		double total=0;
		
		JSONArray jas=new JSONArray();
		
		for(CostAppTotal ctp:results){
			total=total+ctp.getPreCostAll();
		}
		
		int size=0;
		double totalPercent=100;
		for(CostAppTotal ctp:results){
			JSONObject job=new JSONObject();
			JSONArray jasinner=new JSONArray();
			size++;
			if(size>10 || totalPercent<5){
//				job.put("name","其他");
//				job.put("y", 
//						Double.parseDouble(new DecimalFormat("#.#").format(totalPercent)));
				//jas.add(job);
				jasinner.add("其他");
				jasinner.add(Double.parseDouble(new DecimalFormat("#.###").format(totalPercent)));
				jas.add(jasinner);
				break;
			}
			double thisPercent=
					Double.parseDouble(new DecimalFormat("#.###").format(ctp.getPreCostAll()/total*100));
			totalPercent=totalPercent-thisPercent;
			
			jasinner.add(ctp.getsName());
			jasinner.add(thisPercent);
			jas.add(jasinner);
//			job.put("name",ctp.getAppName());
//			job.put("y", thisPercent);
			//jas.add(job);
		}
		
		//详细信息
		view.addObject("topInfos",results );
		view.addObject("jsonInfos",jas.toString());
		
		return view;
	} 
	
	@RequestMapping(params = "method=showPreTop")
	public ModelAndView showTop(String dateStr) {
		ModelAndView view = new ModelAndView("/cost/cost_pre_top");
		
		Date date=new Date();
		
		if(dateStr!=null){
			try {
				date = new SimpleDateFormat("yyyy-MM").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}else{
			dateStr=getMonthDateFormat().format(date);
		}

		view.addObject("dateStr", dateStr);
	
		Date smonth=CustomDateUtil.getStartMonth(date);
		Date emonth=CustomDateUtil.getEndMonth(date);
		
		List<CostAppTotal> results=appCostService.getPerTopCost(smonth,emonth);
		
		//详细信息
		view.addObject("topInfos",results );
		
		return view;
	} 
	
	private static final String MONTH_YMDFORMAT = "yyyy-MM";
	
	private static final ThreadLocal<SimpleDateFormat> dateYMFormat = 
			new ThreadLocal<SimpleDateFormat>() {
	        @Override
	        protected SimpleDateFormat initialValue() {
	            return new SimpleDateFormat(MONTH_YMDFORMAT);
	        }
	    };
	
	 public static DateFormat getMonthDateFormat() {  
	        return (DateFormat) dateYMFormat.get();  
	    } 
}
