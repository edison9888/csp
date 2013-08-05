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
 *@author wb-lixing 2012-3-8 ����04:19:53
 * 
 *         Ӧ�û����б����
 */
@Controller
@RequestMapping("/app/conf/host/show.do")
public class AppHostController {

	/**
	 *@author wb-lixing
	 *2012-3-12 ����01:44:48 
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
				message ="<font color=\"red\">CspCacheTBHostInfos��û�ж�Ӧ�����ݻ��ļ���û�з���Ҫ��ļ�¼!</font>";
			}
		} catch (MissPrimaryColException e) {
			log.error("ȱ�ٱ������!" + e.getClass().getName());
			message = "<font color=\"red\">ȱ�ٱ������!</font>";
		} catch (Exception e) {
			message = "<font color=\"red\">�ļ�����ʱ�����쳣���������ļ���ʽ����ȷ��ɵģ������ļ���ʽ!</font>";
			log.error("�ļ��ϴ��������쳣������ԭ�����ļ���ʽ������Ҫ��! \t  �쳣�ࣺ\t" + e.getClass());
			e.printStackTrace();

		} finally {
			in.close();
		}
		
		// �����ǰmessageΪ�գ���ʾû�쳣����
		if (message == null) {

			if (list != null && list.size() > 0) {
				
				// ����ǰ����ɾ��
				boolean a = HostAo.get().deleteHostListSyncByOpsName(
						appInfo.getOpsName());
				if (a) {
					boolean b = HostAo.get().addHostListSync(list);
					if (!b) {
						message = "<font color=\"red\">���浽���ݿ�ʱ����!</font>";
					}
				} else {
					message = "<font color=\"red\">ɾ��ԭ���б�ʧ��!</font>";
				}
			} else {
				// listû���ݣ������message
				message = "<font color=\"red\">�ļ���û�з���Ҫ��ļ�¼!</font>";
			}
		}
		message = message== null?"":message;
		message = URLEncoder.encode(message,"utf-8");
		String redirectUrl = request.getContextPath()+ "/app/conf/host/show.do?method=showIndex&appId="+appId+ "&messageForIpImport=" +message;
	//	log.debug("################�ض���: " +redirectUrl);
		response.sendRedirect(redirectUrl);
		
		
	}
	
	
	/**
	 *@author wb-lixing
	 *2012-3-12 ����01:46:34
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
		// �����������������Ķ�Ӧ��ϵ
		// ��������Ϊkey��������������Ϊkey����Ϊ�������ĳ�е�������ʱ��ʹ�õ�������key��
		Map<Integer, String> colIndexNameMap = new HashMap<Integer, String>();
		int colIndex = -1;

		for (Iterator<Row> rowIter = sheet.rowIterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			// �Ѽ�������λ�õ�ӳ���ϵ���Ա�������ֱ��ʹ�����������У�

			if (firstRow) {
				for (Iterator<Cell> cellIter = row.cellIterator(); cellIter
						.hasNext();) {
					Cell cell = cellIter.next();
					colIndex++;
					String name = cell.getStringCellValue();
					// �򵥵�ȥ���˿հ�
					name = name.trim();
					// Сд�����������Ĵ���equals���ж�name�Ƿ���ڣ�
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
			//����IPȥ��ȡ��Ӧ��HostPoʵ��
			hostPo = CspCacheTBHostInfos.get().getHostInfoByIp(hostPo.getHostIp());
			//���HostPoʵ��Ϊnull��������
			if(hostPo==null){
				log.debug("################hostPoΪnull ");
				continue;
			
			}
			// hostPo��Ӧ��Ӧ�����ǹ̶��ģ�ÿ���ļ���Ӧһ��Ӧ����
			hostPo.setOpsName(opsName);
			// ���ַ�ʽ���ʹ�ô˱�ʶ�ţ�-99
			hostPo.setCpsVersion(-99);

			
			list.add(hostPo);

		}

		return list;
	}


	


	/**
	 *@author wb-lixing
	 *2012-3-12 ����01:56:10
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
	 *2012-3-9 ����05:08:34 
	 * @throws IOException 
	 */
	@RequestMapping(params = "method=delete")
	public void delete(int appId, String[] ip, HttpServletRequest request, HttpServletResponse response) throws IOException{
		//log.debug("-----------------"+ Arrays.toString(ip));
		String message = null;
		if(ip!=null && ip.length>0){
			
			boolean a = HostAo.get().deleteHostListSync(ip);
			log.debug("-----------------ɾ���ɹ�");
			if(!a){
				message ="<font color=\"red\">ɾ��ʧ��!</font>" ;
			}else{
				message ="<font color=\"blue\">ɾ���ɹ�!</font>" ;
			}
		}
		message = message== null?"":message;
		message = URLEncoder.encode(message,"utf-8");
		String redirectUrl = request.getContextPath()+ "/app/conf/host/show.do?method=showIndex&appId="+appId+ "&deleteMessage=" +message;
	//	log.debug("################�ض���: " +redirectUrl);
		response.sendRedirect(redirectUrl);
	}
	
	/**
	 *@author wb-lixing 2012-3-8 ����04:30:03
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
			log.error("ȱ�ٱ������!" + e.getClass().getName());
			message = "<font color=\"red\">ȱ�ٱ������!</font>";
		} catch (Exception e) {
			message = "<font color=\"red\">�ļ�����ʱ�����쳣���������ļ���ʽ����ȷ��ɵģ������ļ���ʽ!</font>";
			log.error("�ļ��ϴ��������쳣������ԭ�����ļ���ʽ������Ҫ��! \t  �쳣�ࣺ\t" + e.getClass());
			e.printStackTrace();

		} finally {
			in.close();
		}
		
		// �����ǰmessageΪ�գ���ʾû�쳣����
		if (message == null) {

			if (list != null || list.size() > 0) {
				
				// ����ǰ����ɾ��
				boolean a = HostAo.get().deleteHostListSyncByOpsName(
						appInfo.getOpsName());
				if (a) {
					boolean b = HostAo.get().addHostListSync(list);
					if (!b) {
						message = "<font color=\"red\">���浽���ݿ�ʱ����!</font>";
					}
				} else {
					message = "<font color=\"red\">ɾ��ԭ���б�ʧ��!</font>";
				}
			} else {
				// listû���ݣ������message
				message = "<font color=\"red\">�ļ���û�з���Ҫ��ļ�¼!</font>";
			}
		}
		message = message== null?"":message;
		message = URLEncoder.encode(message,"utf-8");
		String redirectUrl = request.getContextPath()+ "/app/conf/host/show.do?method=showIndex&appId="+appId+ "&message=" +message;
	//	log.debug("################�ض���: " +redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	/**
	 *@author wb-lixing 2012-3-8 ����06:19:05
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
		// �����������������Ķ�Ӧ��ϵ
		// ��������Ϊkey��������������Ϊkey����Ϊ�������ĳ�е�������ʱ��ʹ�õ�������key��
		Map<Integer, String> colIndexNameMap = new HashMap<Integer, String>();
		int colIndex = -1;

		for (Iterator<Row> rowIter = sheet.rowIterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			// �Ѽ�������λ�õ�ӳ���ϵ���Ա�������ֱ��ʹ�����������У�

			if (firstRow) {
				for (Iterator<Cell> cellIter = row.cellIterator(); cellIter
						.hasNext();) {
					Cell cell = cellIter.next();
					colIndex++;
					String name = cell.getStringCellValue();
					// �򵥵�ȥ���˿հ�
					name = name.trim();
					// Сд�����������Ĵ����ж�name�Ƿ���ڣ�
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
			// hostPo��Ӧ��Ӧ�����ǹ̶��ģ�ÿ���ļ���Ӧһ��Ӧ����
			hostPo.setOpsName(opsName);
			// ���ַ�ʽ���ʹ�ô˱�ʶ�ţ�-99
			hostPo.setCpsVersion(-99);

			check(hostPo);
			list.add(hostPo);

		}

		return list;
	}

	/**
	 *@author wb-lixing 2012-3-9 ����02:02:29
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
