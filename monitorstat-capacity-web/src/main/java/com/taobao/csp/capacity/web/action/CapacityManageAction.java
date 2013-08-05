
package com.taobao.csp.capacity.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.arkclient.csp.Permisson;
import com.taobao.arkclient.csp.UserPermissionCheck;
import com.taobao.csp.capacity.bo.CapacityCostBo;
import com.taobao.csp.capacity.bo.CapacityRankingBo;
import com.taobao.csp.capacity.dao.CapacityDao;
import com.taobao.csp.capacity.dao.CapacityPvDao;
import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.csp.capacity.util.LocalUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.db.impl.capacity.CapacityCapDao;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityCapPo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.util.TBProductCache;

/**
 * 
 * @author xiaodu
 * @version 2011-9-19 下午01:46:31
 */
@Controller
@RequestMapping("/capacity/manage.do")
public class CapacityManageAction {
	
	@Resource(name = "capacityDao")
	private CapacityDao capacityDao;
	
	@Resource(name = "capacityRankingBo")
	private CapacityRankingBo capacityRankingBo;
	
	@Resource(name = "capacityCostBo")
	private CapacityCostBo capacityCostBo;
	
	@Resource(name = "capacityPvDao")
	private CapacityPvDao capacityPvDao;
	
	@Resource(name = "userPermissionCheck")
	private UserPermissionCheck userPermissionCheck;
	
	@Resource(name = "capacityCapDao")
	private CapacityCapDao capacityCapDao;
	
	@RequestMapping(params = "method=addCapacityAppDataSource", method = RequestMethod.POST)
	public ModelAndView addCapacityAppDataSource(HttpServletRequest request,
			   HttpServletResponse response) {
//		boolean hasPermission = userPermissionCheck.check(request, Permisson.ADD_APP, Permisson.NO_AIM);
//		if (!hasPermission) {
//			ModelAndView noPermisson = new ModelAndView("/permission");
//			return noPermisson;
//		}
		
		String returnView = "/capacity/app_capacity_add";
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("action", "add");
		
		String appId = request.getParameter("selectAppId");
		String dataSource = request.getParameter("dataSource");
		String appType = request.getParameter("appType");
		String dataFeature = request.getParameter("dataFeature");
		int growthRate = Integer.parseInt(request.getParameter("growthRate"));
		String itemName = request.getParameter("itemName");
		String dataName = request.getParameter("dataName");
		
		boolean hasExist = true;
		CapacityAppPo capacityDataSource = capacityDao.getCapacityApp(Integer.valueOf(appId));
		if (capacityDataSource == null || capacityDataSource.getAppId() == 0) {
			hasExist = false;
		}
		
		// 已经存在了
		if (hasExist) {
			modelAndView.addObject("success", "false");
			return modelAndView;
		}
		
		capacityDataSource = new CapacityAppPo();
		capacityDataSource.setAppId(Integer.valueOf(appId).intValue());
		capacityDataSource.setDatasourceName(dataSource);
		capacityDataSource.setAppType(appType);
		capacityDataSource.setDataFeature(dataFeature);
		capacityDataSource.setGrowthRate(growthRate);
		capacityDataSource.setItemName(itemName);
		capacityDataSource.setDataName(dataName);
		capacityDao.insertToCapacityAppDataSource(capacityDataSource);
		modelAndView.addObject("success", "true");
		return modelAndView;
	}
	
	@RequestMapping(params = "method=showAddPage", method = RequestMethod.GET) 
	public ModelAndView showAddPage(HttpServletRequest request,
			   HttpServletResponse response) {
//		boolean hasPermission = userPermissionCheck.check(request, Permisson.ADD_APP, Permisson.NO_AIM);
//		if (!hasPermission) {
//			ModelAndView noPermisson = new ModelAndView("/permission");
//			return noPermisson;
//		}
		
		String returnView = "/capacity/app_capacity_add";
		ModelAndView modelAndView = new ModelAndView(returnView);
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
		modelAndView.addObject("appList", appList);
		return modelAndView;
	}
	
	@RequestMapping(params = "method=showAddCapacityPage", method = RequestMethod.GET) 
	public ModelAndView showAddCapacityPage(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/single_capacity_add";
		ModelAndView modelAndView = new ModelAndView(returnView);
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
		modelAndView.addObject("appList", appList);
		return modelAndView;
	}
	
	@RequestMapping(params = "method=addSingleCapacity", method = RequestMethod.POST)
	public void addSingleCapacity(HttpServletRequest request,
			   HttpServletResponse response, String appName, int singleCapacity) throws Exception {
		
		CapacityCapPo po = new CapacityCapPo();
		po.setAppName(appName);
		po.setSingleCapacity(singleCapacity);
		po.setTime(Calendar.getInstance().getTime());
		po.setUser(LocalUtil.getCurrentUser(request));
		boolean success = capacityCapDao.addCapacityCap(po);
		
		PrintWriter writer = response.getWriter();
		writer.write(String.valueOf(success));
		
		writer.flush();
		writer.close();
	}
	
	@RequestMapping(params = "method=showEditAppDataSource", method = RequestMethod.GET)
	public ModelAndView editAppDataSource(HttpServletRequest request,
			   HttpServletResponse response) {		
		String appId = request.getParameter("appId");
//		boolean hasPermission = userPermissionCheck.check(request, Permisson.EDIT_APP, appId);
//		if (!hasPermission) {
//			ModelAndView noPermisson = new ModelAndView("/permission");
//			return noPermisson;
//		}
		
		String returnView = "/capacity/app_capacity_edit";
		AppInfoPo appInfoPo = AppInfoAo.get().findAppInfoById(Integer.valueOf(appId).intValue());
		CapacityAppPo editCapacityAppPo = capacityDao.getCapacityApp(Integer.valueOf(appId).intValue());
		ModelAndView modelAndView = new ModelAndView(returnView);
		ProductLine productLine = TBProductCache.getProductLineByAppName(appInfoPo.getOpsName());
		modelAndView.addObject("productLine", productLine);
		modelAndView.addObject("editCapacityAppPo", editCapacityAppPo);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=editCapacityAppDataSource", method = RequestMethod.POST)
	public ModelAndView editCapacityAppDataSource(HttpServletRequest request,
			   HttpServletResponse response) {
		String appId = request.getParameter("appId");
//		boolean hasPermission = userPermissionCheck.check(request, Permisson.EDIT_APP, appId);
//		if (!hasPermission) {
//			ModelAndView noPermisson = new ModelAndView("/permission");
//			return noPermisson;
//		}
		
		String newDataSource = request.getParameter("dataSource");
		String newAppType = request.getParameter("appType");
		String newDataFeature = request.getParameter("dataFeature");
		int  growthRate = Integer.parseInt(request.getParameter("growthRate"));
		String itemName = request.getParameter("itemName");
		String dataName = request.getParameter("dataName");
		
		CapacityAppPo editCapacityAppPo = new CapacityAppPo();
		editCapacityAppPo.setAppId(Integer.parseInt(appId));
		editCapacityAppPo.setDatasourceName(newDataSource);
		editCapacityAppPo.setAppType(newAppType);
		editCapacityAppPo.setDataFeature(newDataFeature);
		editCapacityAppPo.setGrowthRate(growthRate);
		editCapacityAppPo.setItemName(itemName);
		editCapacityAppPo.setDataName(dataName);
		
		boolean insertSuccess = capacityDao.updateCapacityAppDataSource(editCapacityAppPo);
		
		String returnView = "/capacity/app_capacity_edit";
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("action", "edit");
		modelAndView.addObject("success", insertSuccess);
		return modelAndView;	
	}
	
	@RequestMapping(params = "method=deleteAppDataSource", method = RequestMethod.GET)
	public void deleteAppDataSource(HttpServletRequest request,
			   HttpServletResponse response, String appId) throws IOException {
		String res = "success";
		boolean hasPermission = userPermissionCheck.check(request, Permisson.DELETE_APP, appId);
		if (!hasPermission) {
			res = "fail";
		} else {
			capacityDao.deleteCapacityAppDataSourceByAppId(Integer.parseInt(appId));
		}

		Writer writer = response.getWriter();
		writer.write(res);
		writer.flush();
		writer.close();
	}
	
	@RequestMapping(params = "method=reinsertCapacityPv", method = RequestMethod.POST)
	public ModelAndView reinsertCpapacityPv(HttpServletRequest request,
			   HttpServletResponse response) {
		boolean hasPermission = userPermissionCheck.check(request, Permisson.REINSERT_PV, Permisson.NO_AIM);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		String dateString = request.getParameter("date");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		boolean success = false;
		
		if (dateString != null) {
			try {
				date = simpleDateFormat.parse(dateString);
			} catch (ParseException e) {
				success = false;
			}
		}
		
		if (date != null) {
			capacityPvDao.deleteCpapacityByDate(date);
			capacityRankingBo.synchDataIntoCapacity(date);
			success = true;
		}
		String returnView = "/capacity/reinsert_capacitypv";
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("message", dateString  + "数据已重新插入");
		modelAndView.addObject("success", success);
		return modelAndView;	
	}
	
	@RequestMapping(params = "method=reflushRanking")
	public ModelAndView reflushRanking(HttpServletRequest request,
			   HttpServletResponse response) throws IOException{
		boolean hasPermission = userPermissionCheck.check(request, Permisson.REFLUSH_RANK, Permisson.NO_AIM);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		capacityRankingBo.executeRanking();
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("message", "重新计算容量排行完成");
		modelView.addObject("backurl", "/show.do?method=showCapacityLimit");
		modelView.setViewName("/message");
		return modelView;
	}
	
	@RequestMapping(params = "method=pushCostData")
	public ModelAndView pushCostData(HttpServletRequest request,
			   HttpServletResponse response, String date) throws IOException{
		boolean hasPermission = userPermissionCheck.check(request, Permisson.REFLUSH_RANK, Permisson.NO_AIM);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		capacityCostBo.caculateCostInfo();
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("message", "重新计算容量信息成功");
		modelView.addObject("backurl", "/show.do?method=showCapacityLimit");
		modelView.setViewName("/message");
		return modelView;
	}
	
	@RequestMapping(params = "method=alterCapacityCostRatio")
	public void alterCapacityCostRatio(HttpServletRequest request,
			   HttpServletResponse response, String appName, String ratio) throws IOException{
		capacityCostBo.alertCapacityCostRatio(appName, Double.valueOf(ratio));
	}
	
	@RequestMapping(params = "method=manageCapacityCap")
	public ModelAndView showCapacityCap() {

		List<CapacityCapPo> capacityCapPos = capacityCapDao.findAllCapacityCap();
		
		
		ModelAndView view = new ModelAndView("/capacity/index_capacity_cap");
		view.addObject("capacityCapPos", capacityCapPos);
		view.addObject("capList", capacityCapPos);
		return view;
	} 
	
	@RequestMapping(params = "method=alterCapacityCap", method = RequestMethod.POST)
	public void alterCapacityCap(HttpServletRequest request,
			   HttpServletResponse response, String appName, double singleCapacity) throws Exception {
		
		CapacityCapPo po = new CapacityCapPo();
		po.setAppName(appName);
		po.setSingleCapacity(singleCapacity);
		po.setTime(Calendar.getInstance().getTime());
		po.setUser(LocalUtil.getCurrentUser(request));
		boolean success = capacityCapDao.updateCapacityCap(po);
		
		PrintWriter writer = response.getWriter();
		writer.write(String.valueOf(success));
		
		writer.flush();
		writer.close();
	}
	
	@RequestMapping(params = "method=deleteSingleCapacity", method = RequestMethod.POST)
	public void deleteSingleCapacity(HttpServletRequest request,
			   HttpServletResponse response, String appName) throws IOException {

		boolean success = capacityCapDao.deleteCapacityCap(appName);

		PrintWriter writer = response.getWriter();
		writer.write(String.valueOf(success));
		
		writer.flush();
		writer.close();
	}
	
	@RequestMapping(params = "method=test")
	public void test(int day){
		
		final Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.DAY_OF_MONTH, day);
		
		Thread thread = new Thread(){
			public void run(){
				capacityRankingBo.synchDataIntoCapacity(cal.getTime());
			}
		};
		
		thread.setDaemon(false);
		thread.start();
	}
}
