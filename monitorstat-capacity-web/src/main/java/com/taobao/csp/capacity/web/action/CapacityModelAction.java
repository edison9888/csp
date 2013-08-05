package com.taobao.csp.capacity.web.action;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.capacity.bo.CapacityModelBo;
import com.taobao.csp.capacity.dao.CapacityModelDao;
import com.taobao.csp.capacity.po.CapacityModelPo;

/**
 * 
 * 功能：容量预测模型action
 * 
 * @author wb-tangjinge E-mail:wb-tangjinge@taobao.com
 * @version 1.0
 * @since 2013-1-22 下午4:25:07
 */
@Controller
@RequestMapping("/model.do")
public class CapacityModelAction {

	@Resource(name = "capacityModelDao")
	private CapacityModelDao capacityModelDao;
	
	@Resource(name = "capacityModelBo")
	private CapacityModelBo capacityModelBo;

	/**
	 * 功能：容量预测模型信息展示
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */

	@RequestMapping(params = "method=showModelList")
	public ModelAndView showCapacityModelList() {

		List<CapacityModelPo> modelList = capacityModelDao.findCapacityModelPoList();

		ModelAndView view = new ModelAndView("/capacity/index_capacity_model");
		view.addObject("modelList", modelList);
		return view;
	}

	/**
	 * 功能：新增容量预测模型信息
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */

	@RequestMapping(params = "method=addCapacityModel")
	public ModelAndView addCapacityModel(HttpServletRequest request, HttpServletResponse response) {
		String returnView = "/capacity/model_capacity_add";
		ModelAndView modelAndView = new ModelAndView(returnView);
		return modelAndView;
	}

	/**
	 * 功能：保存新增对象信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=saveCapacityModel", method = RequestMethod.POST)
	public ModelAndView saveCapacityModel(HttpServletRequest request, HttpServletResponse response) {

		String resFrom = request.getParameter("resFrom");
		String resTo = request.getParameter("resTo");
		String relRatio = request.getParameter("relRatio");
		String returnView = "/capacity/model_capacity_add";
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("action", "add");
		CapacityModelPo po = new CapacityModelPo();
		po.setId(UUID.randomUUID().toString());
		po.setResFrom(resFrom);
		po.setResTo(resTo);
		po.setRelRatio(Double.parseDouble(relRatio));
		capacityModelDao.insertToCapacityModelData(po);
		modelAndView.addObject("success", "true");
		return modelAndView;
	}

	/**
	 * 功能：获取要修改对象信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=editCapacityModel", method = RequestMethod.GET)
	public ModelAndView editCapacityModel(HttpServletRequest request, HttpServletResponse response) {
		String modelId = request.getParameter("modelId");
		String returnView = "/capacity/model_capacity_edit";
		ModelAndView modelAndView = new ModelAndView(returnView);
		CapacityModelPo po = null;
		po = capacityModelDao.editCapacityModelData(modelId);
		modelAndView.addObject("capacityModelPo", po);
		return modelAndView;
	}

	/**
	 * 功能：保存新增对象信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=updateCapacityModel", method = RequestMethod.POST)
	public ModelAndView updateCapacityModel(HttpServletRequest request, HttpServletResponse response) {

		String modelId = request.getParameter("modelId");
		String relRatio = request.getParameter("relRatio");
		String returnView = "/capacity/model_capacity_edit";
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("action", "edit");
		CapacityModelPo po = new CapacityModelPo();
		po.setId(modelId);
		po.setRelRatio(Double.parseDouble(relRatio));
		boolean editInfo = capacityModelDao.updateToCapacityModelData(po);
		if (editInfo) {
			modelAndView.addObject("success", "true");
		} else {
			modelAndView.addObject("success", "false");
		}
		return modelAndView;
	}

	/**
	 * 功能：删除对象信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=delCapacityModel", method = RequestMethod.POST)
	public void delCapacityModel(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String modelId = request.getParameter("modelId");
		String result = "true";
		boolean editInfo = capacityModelDao.delToCapacityModelData(modelId);
		if (!editInfo) {
			result = "false";
		}
		Writer writer = response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
	}

	/**
	 * 功能：模型计算信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=capacityModelCompute")
	public ModelAndView capacityModelCompute(HttpServletRequest request, HttpServletResponse response) {
		String returnView = "/capacity/model_capacity_compute";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("querydate", format.format(new Date()));
		return modelAndView;
	}

	/**
	 * 功能：模型计算信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=modelComputeResult")
	public ModelAndView modelComputeResult(HttpServletRequest request, HttpServletResponse response) {
		String returnView = "/capacity/model_compute_result";
		String computeItems = request.getParameter("computeItems");
		String startTime = request.getParameter("startTime");

		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("computeItems", computeItems);
		modelAndView.addObject("computeStartTime", startTime);
		return modelAndView;
	}

	/**
	 * 功能：获取模型计算结果信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=modelResultInfo", method = RequestMethod.GET)
	public void modelResultInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String items = request.getParameter("computeItems");
		String startTime = request.getParameter("computeStartTime");
		String result = capacityModelBo.collectData(Double.parseDouble(items), startTime);
		
		Writer writer = response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
	}

	/**
	 * 功能：自动计算模型对象信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=autoComputeModel", method = RequestMethod.GET)
	public ModelAndView autoComputeModel(HttpServletRequest request, HttpServletResponse response) {
		String modelId = request.getParameter("modelId");
		String returnView = "/capacity/model_capacity_auto";
		ModelAndView modelAndView = new ModelAndView(returnView);
		CapacityModelPo po = null;
		po = capacityModelDao.editCapacityModelData(modelId);
		modelAndView.addObject("capacityModelPo", po);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		
		modelAndView.addObject("querydate", format.format(calendar.getTime()));
		return modelAndView;
	}

	/**
	 * 功能：获取模型计算结果信息。
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-23
	 */
	@RequestMapping(params = "method=autoComputerModelRelRatio", method = RequestMethod.POST)
	public void autoComputerModelRelRatio(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String modelId = request.getParameter("modelId");
		String startTime = request.getParameter("computeStartTime");
		// 此处处理结果信息。
		String relRatio = "20";
		CapacityModelPo po = new CapacityModelPo();
		po.setId(modelId);
		po.setRelRatio(Double.parseDouble(relRatio));
//		boolean editInfo = true;
		 boolean editInfo = capacityModelDao.updateToCapacityModelData(po);
		String result = " ";
		if (editInfo) {
			result = "1";
		} else {
			result = "0";
		}
		Writer writer = response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
	}
}
