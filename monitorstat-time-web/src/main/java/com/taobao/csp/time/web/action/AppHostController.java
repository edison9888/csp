/**
 * 
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.HostAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 *@author wb-lixing 2012-3-8 下午04:19:53
 * 
 *         应用机器列表管理
 */
@Controller
@RequestMapping("/app/conf/host/show.do")
public class AppHostController {

	/**
	 *@author wb-lixing
	 *2012-3-12 下午01:44:48 
	 */
	@RequestMapping(params = "method=ipImport")
	public void ipImport(HttpServletRequest request, HttpServletResponse response, int appId) throws IOException{
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		Map<String, Object> result = new HashMap<String, Object>();

		getUploadFile(request, result);
		List<HostPo> list = null;
		String message = null;
		InputStream in = (InputStream) result.get("in");
		try {
			list = handleExcelForIpImport(in, appInfo.getOpsName());
			if(list==null || list.size()==0 ){
				message ="<font color=\"red\">CspCacheTBHostInfos中没有对应的数据或文件中没有符合要求的记录!</font>";
			}
		} catch (MissPrimaryColException e) {
			log.error("缺少必须的列!" + e.getClass().getName());
			message = "<font color=\"red\">缺少必须的列!</font>";
		} catch (Exception e) {
			message = "<font color=\"red\">文件处理时发生异常，可能是文件格式不正确造成的，请检查文件格式!</font>";
			log.error("文件上传、处理异常，可能原因是文件格式不符合要求! \t  异常类：\t" + e.getClass());
			e.printStackTrace();

		} finally {
			in.close();
		}
		
		// 如果当前message为空（表示没异常）；
		if (message == null) {

			if (list != null && list.size() > 0) {
				
				// 保存前，先删除
				boolean a = HostAo.get().deleteHostListSyncByOpsName(
						appInfo.getOpsName());
				if (a) {
					boolean b = HostAo.get().addHostListSync(list);
					if (!b) {
						message = "<font color=\"red\">保存到数据库时出错!</font>";
					}
				} else {
					message = "<font color=\"red\">删除原有列表失败!</font>";
				}
			} else {
				// list没数据，则更新message
				message = "<font color=\"red\">文件中没有符合要求的记录!</font>";
			}
		}
		message = message== null?"":message;
		message = URLEncoder.encode(message,"utf-8");
		String redirectUrl = request.getContextPath()+ "/app/conf/host/show.do?method=showIndex&appId="+appId+ "&messageForIpImport=" +message;
	//	log.debug("################重定向: " +redirectUrl);
		response.sendRedirect(redirectUrl);
		
		
	}
	
	
	/**
	 *@author wb-lixing
	 *2012-3-12 下午01:46:34
	 *@param in
	 *@param opsName
	 *@return 
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws MissPrimaryColException 
	 */
	private List<HostPo> handleExcelForIpImport(InputStream in, String opsName) throws InvalidFormatException, IOException, MissPrimaryColException {
		List<HostPo> list = new ArrayList<HostPo>();

		Workbook wb = WorkbookFactory.create(in);
		Sheet sheet = wb.getSheetAt(0);

		boolean firstRow = true;
		// 保存列名和列索引的对应关系
		// 把索引作为key，而不是列名作为key，因为下面遍历某行的所有列时会使用到“索引key”
		Map<Integer, String> colIndexNameMap = new HashMap<Integer, String>();
		int colIndex = -1;

		for (Iterator<Row> rowIter = sheet.rowIterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			// 搜集列名和位置的映射关系（以便在下面直接使用索引访问列）

			if (firstRow) {
				for (Iterator<Cell> cellIter = row.cellIterator(); cellIter
						.hasNext();) {
					Cell cell = cellIter.next();
					colIndex++;
					String name = cell.getStringCellValue();
					// 简单地去两端空白
					name = name.trim();
					// 小写化，方便后面的处理（equals、判断name是否存在）
					name = name.toLowerCase();
					colIndexNameMap.put(colIndex, name);
				}
				firstRow = false;
				continue;
			}
			HostPo hostPo = new HostPo();
			for (Iterator<Cell> iter = row.cellIterator(); iter.hasNext();) {
				Cell cell = iter.next();
				String colName = colIndexNameMap.get(cell.getColumnIndex());
				fill(hostPo, cell, colName);
			}
			check2(hostPo);
			log.debug("################IP: "+hostPo.getHostIp());
			//根据IP去获取对应的HostPo实例
			hostPo = CspCacheTBHostInfos.get().getHostInfoByIp(hostPo.getHostIp());
			//如果HostPo实例为null，不处理
			if(hostPo==null){
				log.debug("################hostPo为null ");
				continue;
			
			}
			// hostPo对应的应用名是固定的，每个文件对应一个应用名
			hostPo.setOpsName(opsName);
			// 此种方式添加使用此标识号：-99
			hostPo.setCpsVersion(-99);

			
			list.add(hostPo);

		}

		return list;
	}


	


	/**
	 *@author wb-lixing
	 *2012-3-12 下午01:56:10
	 *@param hostPo 
	 * @throws MissPrimaryColException 
	 */
	private void check2(HostPo hostPo) throws MissPrimaryColException {
		// TODO Auto-generated method stub
		if (hostPo.getHostIp() == null ) {
			log.debug("====================" + hostPo.getHostIp() );
			throw new MissPrimaryColException();
		}
	}


	/**
	 *@author wb-lixing
	 *2012-3-9 下午05:08:34 
	 * @throws IOException 
	 */
	@RequestMapping(params = "method=delete")
	public void delete(int appId, String[] ip, HttpServletRequest request, HttpServletResponse response) throws IOException{
		//log.debug("-----------------"+ Arrays.toString(ip));
		String message = null;
		if(ip!=null && ip.length>0){
			
			boolean a = HostAo.get().deleteHostListSync(ip);
			log.debug("-----------------删除成功");
			if(!a){
				message ="<font color=\"red\">删除失败!</font>" ;
			}else{
				message ="<font color=\"blue\">删除成功!</font>" ;
			}
		}
		message = message== null?"":message;
		message = URLEncoder.encode(message,"utf-8");
		String redirectUrl = request.getContextPath()+ "/app/conf/host/show.do?method=showIndex&appId="+appId+ "&deleteMessage=" +message;
	//	log.debug("################重定向: " +redirectUrl);
		response.sendRedirect(redirectUrl);
	}
	
	/**
	 *@author wb-lixing 2012-3-8 下午04:30:03
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(int appId, HttpServletRequest request) throws UnsupportedEncodingException {
		String message = request.getParameter("message");
		String deleteMessage = request.getParameter("deleteMessage");
		String messageForIpImport = request.getParameter("messageForIpImport");
		
			
		log.debug("----------------message: "+ message);
		if(message!=null){
			message = new String(message.getBytes("GBK"),"utf-8");
		}
		
		if(deleteMessage!=null){
			deleteMessage = new String(deleteMessage.getBytes("GBK"),"utf-8");
		}
		
		if(messageForIpImport!=null){
			messageForIpImport = new String(messageForIpImport.getBytes("GBK"),"utf-8");
		}
		
		
		
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<HostPo> list = HostAo.get().findAllSyncHostInfos(
				appInfo.getOpsName());
		ModelAndView view = new ModelAndView("/time/conf/host");

		view.addObject("appInfo", appInfo);
		view.addObject("list", list);
		view.addObject("message", message==null?"":message);
		view.addObject("deleteMessage", deleteMessage==null?"":deleteMessage);
		view.addObject("messageForIpImport",messageForIpImport);
		return view;
	}

	
	
	@RequestMapping(params = "method=upload")
	public void upload(HttpServletRequest request, HttpServletResponse response, int appId)
			throws IOException {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		Map<String, Object> result = new HashMap<String, Object>();

		getUploadFile(request, result);
		List<HostPo> list = null;
		List<HostPo> newList = null;
		String message = null;
		InputStream in = (InputStream) result.get("in");
		try {
			list = handleExcel(in, appInfo.getOpsName());
		} catch (MissPrimaryColException e) {
			log.error("缺少必须的列!" + e.getClass().getName());
			message = "<font color=\"red\">缺少必须的列!</font>";
		} catch (Exception e) {
			message = "<font color=\"red\">文件处理时发生异常，可能是文件格式不正确造成的，请检查文件格式!</font>";
			log.error("文件上传、处理异常，可能原因是文件格式不符合要求! \t  异常类：\t" + e.getClass());
			e.printStackTrace();

		} finally {
			in.close();
		}
		
		// 如果当前message为空（表示没异常）；
		if (message == null) {

			if (list != null || list.size() > 0) {
				
				// 保存前，先删除
				boolean a = HostAo.get().deleteHostListSyncByOpsName(
						appInfo.getOpsName());
				if (a) {
					boolean b = HostAo.get().addHostListSync(list);
					if (!b) {
						message = "<font color=\"red\">保存到数据库时出错!</font>";
					}
				} else {
					message = "<font color=\"red\">删除原有列表失败!</font>";
				}
			} else {
				// list没数据，则更新message
				message = "<font color=\"red\">文件中没有符合要求的记录!</font>";
			}
		}
		message = message== null?"":message;
		message = URLEncoder.encode(message,"utf-8");
		String redirectUrl = request.getContextPath()+ "/app/conf/host/show.do?method=showIndex&appId="+appId+ "&message=" +message;
	//	log.debug("################重定向: " +redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	/**
	 *@author wb-lixing 2012-3-8 下午06:19:05
	 * @param request
	 * @throws IOException
	 */
	private void getUploadFile(HttpServletRequest request,
			Map<String, Object> result) throws IOException {
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// Parse the request
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				String name = item.getFieldName();
				String value = item.getString();
				if ("appId".equals(name)) {
					result.put("appId", value);
				}
			} else {
				InputStream in = item.getInputStream();
				result.put("in", in);
				log.debug("---------in: " + in);
			}
		}

	}

	public List<HostPo> handleExcel(InputStream in, String opsName)
			throws Exception {
		List<HostPo> list = new ArrayList<HostPo>();

		Workbook wb = WorkbookFactory.create(in);
		Sheet sheet = wb.getSheetAt(0);

		boolean firstRow = true;
		// 保存列名和列索引的对应关系
		// 把索引作为key，而不是列名作为key，因为下面遍历某行的所有列时会使用到“索引key”
		Map<Integer, String> colIndexNameMap = new HashMap<Integer, String>();
		int colIndex = -1;

		for (Iterator<Row> rowIter = sheet.rowIterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			// 搜集列名和位置的映射关系（以便在下面直接使用索引访问列）

			if (firstRow) {
				for (Iterator<Cell> cellIter = row.cellIterator(); cellIter
						.hasNext();) {
					Cell cell = cellIter.next();
					colIndex++;
					String name = cell.getStringCellValue();
					// 简单地去两端空白
					name = name.trim();
					// 小写化，方便后面的处理（判断name是否存在）
					name = name.toLowerCase();
					colIndexNameMap.put(colIndex, name);
				}

				firstRow = false;
				continue;
			}
			HostPo hostPo = new HostPo();
			for (Iterator<Cell> iter = row.cellIterator(); iter.hasNext();) {
				Cell cell = iter.next();
				String colName = colIndexNameMap.get(cell.getColumnIndex());
				fill(hostPo, cell, colName);
			}
			// hostPo对应的应用名是固定的，每个文件对应一个应用名
			hostPo.setOpsName(opsName);
			// 此种方式添加使用此标识号：-99
			hostPo.setCpsVersion(-99);

			check(hostPo);
			list.add(hostPo);

		}

		return list;
	}

	/**
	 *@author wb-lixing 2012-3-9 下午02:02:29
	 *@param hostPo
	 *@param cell
	 *@param colName
	 *@param v
	 */
	private void fill(HostPo hostPo, Cell cell, String colName) {
// NODEGROUP, dns_ip, nodename, site,   rack, hdrs_chassis, state, model,  description,    vmparent
		
		if ("nodename".equals(colName)) {
			hostPo.setHostName(cell.getStringCellValue());
		} else if ("dns_ip".equals(colName)) {
			hostPo.setHostIp(cell.getStringCellValue());
		} else if ("site".equals(colName)) {
			hostPo.setHostSite(cell.getStringCellValue());
		} else if ("nodegroup".equals(colName)) {
			hostPo.setNodeGroup(cell.getStringCellValue());
		} else if ("state".equals(colName)) {
			hostPo.setState(cell.getStringCellValue());
		
		} else if ("description".equals(colName)) {
			hostPo.setDescription(cell.getStringCellValue());
		}else if("rack".equals(colName)){
			hostPo.setRack(cell.getStringCellValue());
		}else if("hdrs_chassis".equals(colName)){
			hostPo.setHdrs_chassis(cell.getStringCellValue());
		}else if("model".equals(colName)){
			hostPo.setHostType(cell.getStringCellValue());
		}else if("vmparent".equals(colName)){
			hostPo.setVmparent(cell.getStringCellValue());
		}

	}

	public void check(HostPo hostPo) throws Exception {
		if (hostPo.getHostIp() == null || hostPo.getHostName() == null) {
			log.debug("====================" + hostPo.getHostIp() + "\t "
					+ hostPo.getHostName());
			throw new MissPrimaryColException();
		}

	}

	private static final Logger log = Logger
			.getLogger(ApacheInfoController.class);
}

class MissPrimaryColException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MissPrimaryColException() {
	}

	public MissPrimaryColException(String str) {
		super(str);
	}

}
