/**
 * 
 */
package com.taobao.csp.depend.web.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.CspTddlConsumeDao;
import com.taobao.csp.depend.po.tddl.ConsumeTDDLDetail;
import com.taobao.csp.depend.po.tddl.MainResultPo;
import com.taobao.csp.depend.service.impl.TDDLServiceImpl;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.csp.depend.util.SQLPreParser;
import com.taobao.monitor.common.util.page.Pagination;

/**
 *@author wb-lixing 2012-4-16 下午05:00:36
 */

@Controller
@RequestMapping("/tddl/show.do")
public class TDDLAction extends BaseAction {


	@Resource(name = "cspTddlConsumeDao")
	private CspTddlConsumeDao cspTddlConsumeDao;

	// !!是""，而不是null,因为要用于sql拼接
	private static final String BLANK = "";
	private static final int PAGE_SIZE = 50;
	private static final Logger logger = Logger.getLogger(TDDLAction.class);


	private TDDLServiceImpl service = new TDDLServiceImpl();


	/**
	 * 查看单DB、单应用下，sql的情况
	 * 
	 * 其中每条sql占比：单条SQL占 “单DB（ip、port不同的同名DB，算作一个）、单应用”的统计结果。
	 * 
	 * @author wb-lixing 2012-5-11 上午11:39:48
	 */
	@RequestMapping(params = "method=itemDetail")
	public ModelAndView itemDetail(HttpServletRequest request, HttpServletResponse response) {
		int pageNo = 1;
		int pageSize = PAGE_SIZE;
		String pageNoParam = request.getParameter("pageNo");
		String pageSizeParam = request.getParameter("pageSize"); 

		//没有用户输入，直接从链接进去，所以肯定有值，
		String appName = request.getParameter("appName");
		String dbName = request.getParameter("dbName");
		String query1Type = request.getParameter("query1Type");
		String query1Value= request.getParameter("query1Value");
		String sortType = request.getParameter("sortType"); 

		if(sortType == null || sortType.trim().equals("")) {
			sortType = "execNum";// default is by execute
		}

		if (pageNoParam != null) {
			try {
				pageNo = Integer.parseInt(pageNoParam);
			} catch (NumberFormatException e) {
				logger.error("", e);
			}
		}
		if (pageSizeParam != null) {
			if (pageSizeParam.trim().equals(""))
				pageSize = PAGE_SIZE;
			try {
				pageSize = Integer.parseInt(pageSizeParam);
			} catch (NumberFormatException e) {
				pageSize = PAGE_SIZE;
				logger.error("", e);;
			}
		}

		String totalExecNum = request.getParameter("totalExecNum");
		String totalElaTime = request.getParameter("totalElaTime");

		String searchsql = request.getParameter("searchsql");
		if(searchsql != null)
			searchsql = searchsql.trim();
		else
			searchsql = "";

		if(query1Type.equals("2") || (query1Type.equals("1") && (totalExecNum == null || totalElaTime == null || "0".equals(totalExecNum) || "0".equals(totalElaTime)))) {  
			//查询历史
			Long[] array = new Long[2]; // Long Float
			array[0] = new Long(0);
			array[1] = new Long(0);
			try {
				cspTddlConsumeDao.queryItemDetailTotal(array, appName, dbName, query1Type, query1Value, searchsql);
				totalExecNum = array[0] + "";
				totalElaTime = array[1] + "";
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("", e);
				if(totalExecNum == null) totalExecNum = 0 + "";
				if(totalElaTime == null) totalElaTime = 0 + "";
			}
		}

		Pagination<ConsumeTDDLDetail> page = cspTddlConsumeDao.itemDetail(appName, dbName, query1Type, query1Value, pageNo, pageSize, sortType, searchsql);
		ModelAndView view = new ModelAndView("depend/appinfo/tddl/item_detail");
		view.addObject("pagination", page);
		// 因为当首次访问，没有传appName、selectDate时，这时会使用此方法中定义的默认值
		view.addObject("appName", appName);
		view.addObject("dbName", dbName);
		view.addObject("query1Type", query1Type);
		view.addObject("query1Value", query1Value);
		view.addObject("sortType", sortType);
		view.addObject("totalExecNum", totalExecNum);
		view.addObject("totalElaTime", totalElaTime);
		view.addObject("searchsql", searchsql);
		return view;
	}

	/**
	 * 首页查询列表，和叶开沟通过，不再查询当天的数据。
	 * tddl 应用 or db列表
	 * 
	 * @throws ParseException
	 */
	@RequestMapping(params = "method=list")
	public ModelAndView appList(HttpServletRequest request,
			HttpServletResponse response) {
		int pageNo = 1;
		int pageSize = PAGE_SIZE;
		int type = 1;
		String name = BLANK;
		String typeParam = request.getParameter("type");
		type = typeParam != null ? Integer.parseInt(typeParam) : type;

		String nameParam = request.getParameter("name");
		name = nameParam != null ? nameParam : name;

		String pageNoParam = request.getParameter("pageNo");
		String pageSizeParam = request.getParameter("pageSize");

		if (pageNoParam != null) {
			try {
				pageNo = Integer.parseInt(pageNoParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (pageSizeParam != null) {
			if (pageSizeParam.trim().equals(""))
				pageSize = PAGE_SIZE;
			try {
				pageSize = Integer.parseInt(pageSizeParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				pageSize = PAGE_SIZE;
			}
		}
		String sortType = request.getParameter("sortType");
		if(sortType == null || sortType.trim().equals("")) {
			sortType = "execNum";// default is by execute, the other is elaTime,itemName
		}	

		Date day = MethodUtil.getDate(request.getParameter("day"));
		String dayStr = MethodUtil.getStringOfDate(day);
		Pagination<ConsumeTDDLDetail> page = cspTddlConsumeDao.list(type, name,
				day, pageNo, pageSize, sortType);			

		ModelAndView view = new ModelAndView("depend/appinfo/tddl/list");
		view.addObject("pagination", page);
		// 因为当首次访问，没有传appName、selectDate时，这时会使用此方法中定义的默认值
		view.addObject("name", name);
		view.addObject("type", type);
		view.addObject("day", dayStr);
		view.addObject("sortType", sortType);
		return view;
	}

	/**
	 * Tddl二级页面查询
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	@RequestMapping(params = "method=query")
	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws IOException, JSONException {
		int pageNo = 1;
		int pageSize = PAGE_SIZE;
		String query1Type = "1", query2Type = "1", query3Type = "1", ip = BLANK, port = BLANK;
		String name = BLANK;
		// 默认查昨天的
		String query1Value = MethodUtil.getOneDayPre();

		// 这样访问首页时，不用强制传pageNo、pageSize 。。。
		String pageNoParam = request.getParameter("pageNo");
		String pageSizeParam = request.getParameter("pageSize");
		String query1TypeParam = request.getParameter("query1Type");
		String query1ValueParam = request.getParameter("query1Value");
		String query2TypeParam = request.getParameter("query2Type");
		String query3TypeParam = request.getParameter("query3Type");
		String nameParam = request.getParameter("name");
		String ipParam = request.getParameter("ip");
		String portParam = request.getParameter("port");
		if (pageNoParam != null) {
			try {
				pageNo = Integer.parseInt(pageNoParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (pageSizeParam != null) {
			if (pageSizeParam.trim().equals(""))
				pageSize = PAGE_SIZE;
			try {
				pageSize = Integer.parseInt(pageSizeParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				pageSize = PAGE_SIZE;
			}
		}
		query1Type = query1TypeParam != null ? query1TypeParam : query1Type;
		query1Value = query1ValueParam != null
				&& !query1ValueParam.trim().equals("") ? query1ValueParam
						: query1Value;
		if (query2TypeParam != null)
			query2Type = query2TypeParam;

		if (query3TypeParam != null)
			query3Type = query3TypeParam;

		if (nameParam != null)
			name = nameParam;

		ip = ipParam != null ? ipParam : ip;
		port = portParam != null ? portParam : port;

		String sortType = request.getParameter("sortType"); 
		if(sortType == null || sortType.trim().equals("")) {
			sortType = "execNum";// default is by execute
		}		

		Pagination<ConsumeTDDLDetail> page = cspTddlConsumeDao.query(
				query1Type, query1Value, query2Type, query3Type, name, ip,
				port, pageNo, pageSize, sortType);

		// 汇总当前页数据
		ConsumeTDDLDetail groupResult = groupPage(page);

		ModelAndView view = new ModelAndView("depend/appinfo/tddl/query");

		view.addObject("pagination", page);
		// 因为当首次访问，没有传appName、selectDate时，这时会使用此方法中定义的默认值
		view.addObject("name", name);
		view.addObject("query1Type", query1Type);
		view.addObject("query1Value", query1Value);
		view.addObject("query2Type", query2Type);
		view.addObject("query3Type", query3Type);
		view.addObject("ip", ip);
		view.addObject("port", port);
		view.addObject("groupResult", groupResult);
		view.addObject("sortType", sortType);
		return view;
	}

	// @Resource(name="tddlService")
	// private TDDLService tddlService;

	/**
	 *@author wb-lixing 2012-4-19 下午06:12:19
	 *@param page
	 *@return
	 */
	private ConsumeTDDLDetail groupPage(Pagination<ConsumeTDDLDetail> page) {
		List<ConsumeTDDLDetail> list = page.getList();
		// group result
		ConsumeTDDLDetail gr = new ConsumeTDDLDetail();
		// 因为汇总平均值时，将等于0的过滤掉了。
		// 所以executeSumForTimeAverage只累加平均值不为0的excuteSum
		long executeSum = 0, executeSumForTimeAverage = 0;
		float timeAverage = 0;
		String maxTime = null, minTime = null;
		int maxTimeI = 0, minTimeI = 0;
		boolean initMaxTime = false, initMinTime = false;
		for (ConsumeTDDLDetail po : list) {
			long es = po.getExecuteSum();
			executeSum += es;
			// 平均值，再做均值
			float ta = po.getTimeAverage();
			if (ta != 0 && es != 0) {
				timeAverage += ta * es;
				executeSumForTimeAverage += es;
			}

			// maxTime、minTime有两种格式，格式1：带#，示例：24#12-03-29 12:26
			String mt = po.getMaxTime(), nt = po.getMinTime();
			if (mt.indexOf('#') != -1) {
				mt = mt.split("#")[0];
			}

			if (nt.indexOf('#') != -1) {
				nt = nt.split("#")[0];
			}

			int mti = Integer.parseInt(mt), nti = Integer.parseInt(nt);
			if (!initMaxTime) {
				maxTimeI = mti;
				maxTime = mt;
				initMaxTime = true;
			}

			if (!initMinTime) {
				// 不为0时，初始化成功
				if (nti != 0) {
					minTimeI = nti;
					minTime = nt;
					initMinTime = true;
				}
			}

			if (mti > maxTimeI) {
				maxTimeI = mti;
				maxTime = mt;
			}

			//	System.out.println("mti: " + mti);
			//	System.out.println("mt: " + mt);
			//	System.out.println("maxTimeI: " + maxTimeI);
			//	System.out.println("maxTime: " + maxTime);

			if (nti < minTimeI) {
				// 处理0值
				if (nti != 0) {
					minTimeI = nti;
					minTime = nt;
				}
			}
		}
		timeAverage = timeAverage / executeSumForTimeAverage;
		gr.setExecuteSum(executeSum);
		gr.setTimeAverage(timeAverage);
		gr.setMaxTime(maxTime);
		gr.setMinTime(minTime);
		return gr;
	}

	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(String tablename) {
		if(tablename != null)
			tablename = tablename.toLowerCase();
		ModelAndView view = new ModelAndView("/depend/appinfo/tddl/indextable");
		MainResultPo mainResultPo = service.getIndexInfoByTableName(tablename);
		view.addObject("mainResultPo", mainResultPo);
		view.addObject("tablename", tablename);
		return view;      
	}

	@RequestMapping(params = "method=seeMoreDetailFromSql")
	public ModelAndView seeMoreDetailFromSql(HttpServletRequest request, HttpServletResponse response, String sql, String appName, String dbName) {
		String tablename = SQLPreParser.findTableName(sql);    
		MainResultPo mainResultPo = service.getIndexInfoByTableName(tablename);
		List<ConsumeTDDLDetail> list = cspTddlConsumeDao.getRecentlySqlInfo(sql,appName,dbName);

		ModelAndView view = new ModelAndView("depend/appinfo/tddl/more_sql_detail");
		view.addObject("sql", sql);
		view.addObject("mainResultPo", mainResultPo);
		view.addObject("tablename", tablename);
		view.addObject("list", list);
		return view;
	}



	@RequestMapping(params = "method=gotoTddlHistoryGraph")
	public ModelAndView gotoTddlHistoryGraph(String name,String type, String startDate, String endDate) {
		ModelAndView view = new ModelAndView("/depend/appinfo/tddl/tddlhistorygraph");
		//时间不合法则默认显示昨天和昨天的14天前
		if(endDate == null) {
			endDate = MethodUtil.getStringOfDate(MethodUtil.getYestoday());  
		}
		if(startDate == null) {
			startDate = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(MethodUtil.getDate(endDate), 14));  
		}
		if(type == null) {
			type = "1";
		}
		List<ConsumeTDDLDetail> list = cspTddlConsumeDao.getHistoryGraphData(startDate, endDate, name, type);
		view.addObject("startDate", startDate);
		view.addObject("endDate", endDate);
		view.addObject("name", name);
		view.addObject("type", type);
		view.addObject("list", list);
		return view;
	}	
	
	
	/**
	 * @author 李星
	 * 2012-9-6 下午3:07:29
	 * 功能: tddl sqltext 分表合并,并导出
	 * 
	 */
	//http://localhost:8080/depend/tddl/show.do?method=tableMerge&appName=logisticscenter&dbName=lg_core_0000&selectDate=2012-08-03
	@RequestMapping(params = "method=tableMerge")
	public void  tableMerge(String appName, String dbName, String selectDate, HttpServletResponse response) {
		//System.out.println("---------"+ getClass());
		List<ConsumeTDDLDetail>  resultList = cspTddlConsumeDao.tableMergeList(appName,dbName,selectDate);
		
		renderExcel(resultList,response);
	}

	/**
	 * @author 李星
	 * 2012-9-7 下午2:44:12 
	 * @param resultList 
	 */
		
	private void renderExcel(List<ConsumeTDDLDetail> resultList, HttpServletResponse response) {
		Workbook wb = new HSSFWorkbook();
		// Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		//标题
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("SQL");
		row.createCell(1).setCellValue("执行量");
		row.createCell(2).setCellValue("平均时间");
		row.createCell(3).setCellValue("总时间");
		for (int i = 0; i < resultList.size(); i++) {
			row = sheet.createRow(i+1);
			ConsumeTDDLDetail record = resultList.get(i);
			row.createCell(0).setCellValue(record.getSqlText());
			row.createCell(1).setCellValue(record.getExecuteSumStr());
			row.createCell(2).setCellValue(record.getTimeAverage());
			row.createCell(3).setCellValue(record.getTotalTimeStr());
		}

		try {
			//\u6570\u636e\u5bfc\u51fa 数据导出
			response.setHeader("Content-Disposition", "attachment;filename=CSP-TDDL-export.xls");
			response.setHeader("Content-Type", "application/ms-excel");
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);
		//	out.flush();
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

	}
	
	/**
	 * @author 李星
	 * 2012-9-7 下午3:55:07 
	 * 
	 * 单张表的读写比例。
	 */
	@RequestMapping(params = "method=readWriteRate")
	public void readWriteRate(String appName, String dbName, String selectDate, HttpServletResponse response) {
		List<ConsumeTDDLDetail>  resultList = cspTddlConsumeDao.readWriteRate(appName,dbName,selectDate);
		renderExcel2(resultList,response);
	}
	
	/**
	 * @author 李星
	 * 2012-9-7 下午4:32:47 
	 */

	private void renderExcel2(List<ConsumeTDDLDetail> resultList, HttpServletResponse response) {
		Workbook wb = new HSSFWorkbook();
		// Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		//标题
		//tableName, 读次数，写次数，读写比例， 执行量，总时间，平均时间
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("表名");
		row.createCell(1).setCellValue("读次数");
		row.createCell(2).setCellValue("写次数");
		row.createCell(3).setCellValue("读写比例");
		row.createCell(4).setCellValue("执行量");
		row.createCell(5).setCellValue("总时间");
		row.createCell(6).setCellValue("平均时间");
		for (int i = 0; i < resultList.size(); i++) {
			row = sheet.createRow(i+1);
			ConsumeTDDLDetail record = resultList.get(i);
			row.createCell(0).setCellValue(record.getTableName());
			row.createCell(1).setCellValue(record.getReadCountStr());
			row.createCell(2).setCellValue(record.getWriteCountStr());
			row.createCell(3).setCellValue(record.getReadWriteRate());
			row.createCell(4).setCellValue(record.getExecuteSumStr());
			row.createCell(5).setCellValue(record.getTotalTimeStr());
			row.createCell(6).setCellValue(record.getTimeAverage());
		}

		try {
			//\u6570\u636e\u5bfc\u51fa 数据导出
			response.setHeader("Content-Disposition", "attachment;filename=CSP-TDDL-read-write-rate.xls");
			response.setHeader("Content-Type", "application/ms-excel");
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);
		//	out.flush();
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

	}
	
	
	
}
