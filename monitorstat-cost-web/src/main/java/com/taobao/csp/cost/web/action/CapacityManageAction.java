
package com.taobao.csp.cost.web.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.cost.service.AppCostService;
import com.taobao.csp.cost.service.DayCostServiceNew;
import com.taobao.csp.cost.service.HardCostService;
import com.taobao.monitor.common.util.DateUtil;

/**
 * 
 * �ɱ�����
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-10-24
 */
@Controller
@RequestMapping("/manage.do")
public class CapacityManageAction {

	private static Logger logger = Logger.getLogger(CapacityManageAction.class);
	
	@Resource(name = "dayCostServiceNew")
	private DayCostServiceNew dayCostServiceNew;
	@Resource(name = "hardCostService")
	private HardCostService hardCostService;
	@Resource(name = "appCostService")
	private AppCostService appCostService;
	
	/**
	 * ÿ�����һ�λ����ɱ�
	 * 
	 * @param request
	 * @param response
	 * @param date
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "method=pushCostData")
	public ModelAndView pushCostData(HttpServletRequest request,
			   HttpServletResponse response, String dateStr,String isForce) throws IOException{
		Date date=new Date();
		
		if(dateStr==null || dateStr.trim().equals("")){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND, 0);
			
			date=cal.getTime();
		}else{
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		int tableId=cal.get(Calendar.DAY_OF_YEAR);
		
		String dStr=DateUtil.getDateYMDFormat().format(date);

		long startTime=System.currentTimeMillis();
		dayCostServiceNew.caculateCostInfo(date,dStr,isForce,tableId);
		appCostService.removeTopCostCache();
		
		ModelAndView view = new ModelAndView("/ajax_result");
		view.addObject("returnMsg","����������Ͻ�ȥ�����ݰɣ�ɧ�꣡"+
				(System.currentTimeMillis()-startTime));
		
		return view;
	}
	
	/**
	 * ��ȡû��Ӳ����Ϣ�Ļ����б�
	 * 
	 * @param request
	 * @param response
	 * @param hosts		Ҫ���µĻ����б�
	 * @param apps		Ҫ���µ�Ӧ���б�
	 * @param delAll	ɾ������
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "method=getHardInfo")
	public ModelAndView getHardInfo(HttpServletRequest request,
			   HttpServletResponse response, String hosts,String apps,
			   String dateStr, String type,
			   String delAll) throws IOException{
		ModelAndView view = new ModelAndView("/ajax_result");
		String[] hs=null;
		if(StringUtils.isNotBlank(hosts)){
			hs=hosts.split(",");
		}
		String[] as=null;
		if(StringUtils.isNotBlank(apps)){
			as=apps.split(",");
		}
		
		Date date=new Date();
		
		if(dateStr==null || dateStr.trim().equals("")){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND, 0);
			
			date=cal.getTime();
		}else{
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}
		
		Set<String>  ll=hardCostService.getNoHardHosts(as,
				hs,StringUtils.isNotBlank(delAll),date,type);

		StringBuilder sb=new StringBuilder();
		for(String h:ll){
			sb.append(h).append("<br>");
		}
		view.addObject("returnMsg",sb.toString());
		return view;
	}
	
	/**
	 * ��ȡӲ����Ϣ
	 * 
	 * @param request
	 * @param response
	 * @param imPath	Ŀ¼
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "method=insertHardInfo")
	public ModelAndView importHardInfo(HttpServletRequest request,
			   HttpServletResponse response, String imPath) throws IOException{
		ModelAndView view = new ModelAndView("/ajax_result");
		if(StringUtils.isBlank(imPath)){
			view.addObject("returnMsg","�ӵ���");
			return view;
		}
		File f=new File(imPath);
		if(!f.exists()){
			view.addObject("returnMsg","�ļ������ڣ��ӵ���");
			return view;
		}
		FileInputStream fi=new FileInputStream(f);
		BufferedReader br=new BufferedReader(
				new InputStreamReader(fi)); 
		String str=null; 
		List<String> ls=new LinkedList<String>();
		
		while((str=br.readLine())!=null) { 
			if(StringUtils.isNotBlank(str)){
				ls.add(str); 
			}
		}
		
		if(ls.size()==0){
			view.addObject("returnMsg","�ļ�û���ݣ��ӵ���");
			return view;
		}
		String ts=hardCostService.insertHardHosts(ls);
		if(ts.equals("")){
			view.addObject("returnMsg","���³ɹ�������ȥ���ɣ�ɧ�꣡");
		}else{
			view.addObject("returnMsg","����ʧ��<br>"+ts);
		}
		return view;
	}

	/**
	 * ����DB�ɱ�
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "method=updateDbCost")
	public ModelAndView updateDbCost(HttpServletRequest request,String dateStr,
			   HttpServletResponse response) throws IOException{

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND, 0);
			
		if(StringUtils.isNotBlank(dateStr)){
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
				cal.setTime(date);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}
		
		int tableId=cal.get(Calendar.DAY_OF_YEAR);
		
		ModelAndView view = new ModelAndView("/ajax_result");
		String ts=hardCostService.insertDbCost(tableId);
		
		view.addObject("returnMsg","���½���<br>"+ts);
		
		return view;
	}

	public static void main(String[] args){
		Date date= new Date();
		String dateStr="2012-10-20";
		if(dateStr==null || dateStr.trim().equals("")){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND, 0);
			
			date=cal.getTime();
		}else{
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
				logger.warn("parse date exception",e);
			}
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		System.out.println(cal.get(Calendar.DAY_OF_YEAR));
		
		Calendar call = Calendar.getInstance();
		call.set(Calendar.HOUR_OF_DAY, 0);
		call.set(Calendar.MINUTE,0);
		call.set(Calendar.SECOND, 0);
		
		int tableId=call.get(Calendar.DAY_OF_YEAR);
		System.out.println(tableId);
		
		call.set(Calendar.DAY_OF_YEAR, 355);
		System.out.println(call.getTime());
	}
	
}
