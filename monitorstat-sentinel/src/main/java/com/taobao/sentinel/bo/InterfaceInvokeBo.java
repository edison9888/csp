package com.taobao.sentinel.bo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.taobao.sentinel.dao.InterfaceInvokeDao;
import com.taobao.sentinel.po.InterfaceInvokePo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * interface invoke apply
 * @author youji.zj
 *
 */
public class InterfaceInvokeBo {
	@Resource(name = "interfaceInvokeDao")
	private InterfaceInvokeDao interfaceInvokeDao;

	public InterfaceInvokeDao getInterfaceInvokeDao() {
		return interfaceInvokeDao;
	}

	public void setInterfaceInvokeDao(InterfaceInvokeDao interfaceInvokeDao) {
		this.interfaceInvokeDao = interfaceInvokeDao;
	}
	
	public InterfaceInvokePo findById(String id) {
		return interfaceInvokeDao.findById(id);
	}
	
	public List<InterfaceInvokePo> findAll() {
		return interfaceInvokeDao.findAll();
	}
	
	public List<InterfaceInvokePo> findByApp(String appName) {
		return interfaceInvokeDao.findByApp(appName);
	}
	
	public List<InterfaceInvokePo> findByRefApp(String refApp) {
		return interfaceInvokeDao.findByRefApp(refApp);
	}
	
	public List<InterfaceInvokePo> findByAppAndRefApp(String appName, String refApp) {
		return interfaceInvokeDao.findByAppAndRefApp(appName, refApp);
	}
	
	public List<String> findAppNames() {
		return interfaceInvokeDao.findAppNames();
	}
	
	public List<String> findRefAppNames() {
		return interfaceInvokeDao.findRefAppNames();
	}
	
	public void add(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		String refApp = request.getParameter("refApp").trim();
		String estimateQps = request.getParameter("estimateQps").trim();
		String strong = request.getParameter("strong").trim();
		String remark = request.getParameter("remark").trim();
		String user = LocalUtil.getCurrentUser(request);
		
		InterfaceInvokePo po = new InterfaceInvokePo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setInterfaceInfo(interfaceInfo);
		po.setRefApp(refApp);
		po.setUser(user);
		po.setStrong(strong);
		po.setEstimateQps(Integer.parseInt(estimateQps));
		po.setRemark(remark);
	
		interfaceInvokeDao.add(po);
	}
	
	public void update(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		String refApp = request.getParameter("refApp").trim();
		String estimateQps = request.getParameter("estimateQps").trim();
		String strong = request.getParameter("strong").trim();
		String remark = request.getParameter("remark").trim();
		String user = LocalUtil.getCurrentUser(request);
		String id = request.getParameter("id");
		
		InterfaceInvokePo po = new InterfaceInvokePo();
		po.setId(id);
		po.setAppName(appName);
		po.setInterfaceInfo(interfaceInfo);
		po.setRefApp(refApp);
		po.setUser(user);
		po.setStrong(strong);
		po.setEstimateQps(Integer.parseInt(estimateQps));
		po.setRemark(remark);
	
		interfaceInvokeDao.update(po);
	}
	
	public List<InterfaceInvokePo> findList(String appName, String refApp) {
		List<InterfaceInvokePo> list = new ArrayList<InterfaceInvokePo>();
		
		if (StringUtils.isEmpty(appName) && StringUtils.isEmpty(refApp)) {
			list = findAll();
		}
		
		if (!StringUtils.isEmpty(appName) && StringUtils.isEmpty(refApp)) {
			list = findByApp(appName);
		}
		
		if (StringUtils.isEmpty(appName) && !StringUtils.isEmpty(refApp)) {
			list = findByRefApp(refApp);
		}
		
		if (!StringUtils.isEmpty(appName) && !StringUtils.isEmpty(refApp)) {
			list = findByAppAndRefApp(appName, refApp);
		}
		
		return list;
	}
	
	public boolean detele(String id) {
		return interfaceInvokeDao.delete(id);
	}
	
	public boolean checkExist(String appName, String refApp, String interfaceInfo) {
		return interfaceInvokeDao.checkExist(appName, refApp, interfaceInfo);
	}
	
	public boolean checkExistExceptSelf(String appName, String refApp, String interfaceInfo, String id) {
		return interfaceInvokeDao.checkExistExceptSelf(appName, refApp, interfaceInfo,id);
	}
}
