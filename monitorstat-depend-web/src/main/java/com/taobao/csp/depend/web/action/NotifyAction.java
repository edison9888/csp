package com.taobao.csp.depend.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.NotifyDao;
import com.taobao.csp.depend.po.NotifyPo;

/**
 * @author wb-lixing
 */
@Controller
@RequestMapping("/show/notify.do")
public class NotifyAction {

	private static final Logger logger = Logger.getLogger(NotifyAction.class);

	@Resource(name = "notifyDao")
	private NotifyDao notifyDao;

	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(String opsName,  HttpServletRequest request) {
			
		//如果selectDate没值，默认是昨天
		String selectDate = request.getParameter("selectDate");
		if(selectDate == null ||selectDate.trim().isEmpty()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
			selectDate = sdf.format(cal.getTime());
		}
		
		
		ModelAndView view = new ModelAndView("/depend/appinfo/notify/index");
		List<NotifyPo> poList = notifyDao.getNotifyListByAppName(opsName,selectDate);
		// 获取针对单个应用的统计结果
		NotifyPo appStat = new NotifyPo();
		// 获取针对单个type的统计结果
		Map<String, NotifyPo> typeStat = new HashMap<String, NotifyPo>();
		// 获取针对单个机房，单个type的统计结果
		Map<String, List<NotifyPo>> siteStat = new HashMap<String, List<NotifyPo>>();

		for (NotifyPo po : poList) {
			doAppStat(po, appStat);
			doTypeStat(po, typeStat);
		}
		// 将typeStat从Map变为List，并排序
		List<NotifyPo> typeStatList = new ArrayList<NotifyPo>();
		typeStatList.addAll(typeStat.values());

		// 将typeStat按不同site归类
		for (NotifyPo po : typeStatList) {
			String s = po.getSiteName();
			List<NotifyPo> perSite = siteStat.get(s);
			if (perSite == null) {
				perSite = new ArrayList<NotifyPo>();
				siteStat.put(s, perSite);
			}
			perSite.add(po);
		}

		// 给siteStat中的List排序
		for (List<NotifyPo> perList : siteStat.values()) {
			Collections.sort(perList, new Comparator<NotifyPo>() {
				@Override
				public int compare(NotifyPo o1, NotifyPo o2) {
					return (int) (o2.getS_count() - o1.getS_count());
				}
			});
		}
		// 将siteStat转为List，并排序
		List<List<NotifyPo>> siteStatList = new ArrayList<List<NotifyPo>>();
		siteStatList.addAll(siteStat.values());
		// 按CM3,CM4,CM5排序
		Collections.sort(siteStatList, new Comparator<List<NotifyPo>>() {
			@Override
			public int compare(List<NotifyPo> o1, List<NotifyPo> o2) {
				NotifyPo po1 = o1.get(0);
				NotifyPo po2 = o2.get(0);
				int site1 = getSiteNum(po1);
				int site2 = getSiteNum(po2);
				return site1 - site2;
			}

			private int getSiteNum(NotifyPo po1) {
				String s = po1.getSiteName();
				int n = 0;
				if (s.length() > 3) {
					char ch = s.charAt(2);
					try {
						n = Integer.parseInt(ch + "");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				return n;
			}
		});


		view.addObject("opsName", opsName);
		view.addObject("selectDate",selectDate);
		request.setAttribute("appStat", appStat);
		view.addObject("typeStatList", typeStatList);
		view.addObject("siteStatList", siteStatList);

		return view;
	}

	/**
	 *@author wb-lixing 2012-5-31 下午04:19:23
	 *@param po
	 *@param typeStat
	 */
	private void doTypeStat(NotifyPo po, Map<String, NotifyPo> typeStat) {
		String s = po.getSiteName();
		String g = po.getGroupName();
		String t = po.getOperType();
		String k = g + '`' + t;
		NotifyPo stat = typeStat.get(k);
		if (stat == null) {
			stat = new NotifyPo();
			stat.setSiteName(s);
			stat.setGroupName(g);
			stat.setOperType(t);
			typeStat.put(k, stat);
		}

		stat.setF_count(stat.getF_count() + po.getF_count());
		stat.setNc_count(stat.getNc_count() + po.getNc_count());
		stat.setRa_f_count(stat.getRa_f_count() + po.getRa_f_count());
		stat.setRa_s_count(stat.getRa_s_count() + po.getRa_s_count());
		stat.setRe_count(stat.getRe_count() + po.getRe_count());

		stat.setS_count(stat.getS_count() + po.getS_count());
		stat.setTimeout_count(stat.getTimeout_count() + po.getTimeout_count());
		stat.setWs_count(stat.getWs_count() + po.getWs_count());
	}

	/**
	 *@author wb-lixing 2012-5-31 下午04:13:32
	 *@param po
	 *@param appStat
	 */
	private void doAppStat(NotifyPo po, NotifyPo stat) {
		/*
		 * long f_count = po.getF_count(); long nc_count = po.getNc_count();
		 * long ra_f_count = po.getRa_f_count(); long ra_s_count =
		 * po.getRa_s_count(); long re_count = po.getRe_count();
		 * 
		 * long s_count = po.getS_count(); long timeout_count =
		 * po.getTimeout_count(); long ws_count = po.getWs_count();
		 */
		stat.setF_count(stat.getF_count() + po.getF_count());
		stat.setNc_count(stat.getNc_count() + po.getNc_count());
		stat.setRa_f_count(stat.getRa_f_count() + po.getRa_f_count());
		stat.setRa_s_count(stat.getRa_s_count() + po.getRa_s_count());
		stat.setRe_count(stat.getRe_count() + po.getRe_count());

		stat.setS_count(stat.getS_count() + po.getS_count());
		stat.setTimeout_count(stat.getTimeout_count() + po.getTimeout_count());
		stat.setWs_count(stat.getWs_count() + po.getWs_count());
	}

}
