package com.taobao.monitor.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.ao.MonitorUserAo;
import com.taobao.monitor.web.vo.LoginUserPo;

/**
 * 这是是用来处理用户登陆判断的servlet
 * @author wuhaiqian.pt
 *
 */
public class RegisterServlet extends HttpServlet{

	private static final Logger logger =  Logger.getLogger(RegisterServlet.class);

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf8");
		String name = request.getParameter("name");
		String wangwang = request.getParameter("wangwang");
		String phone= request.getParameter("phone");
		String mail= request.getParameter("mail");
		String nameExist = "no";
		String wangwangExist = "no";
		String phoneExist = "no";
		String mailExist = "no";
		Map<String,String> responseMap = new HashMap<String, String>();
		
		List<LoginUserPo> userList = MonitorUserAo.get().findAllUser();
		
		for(LoginUserPo user : userList) {
			
			if(null != name && name.equals(user.getName()) && !"".equals(name)) {
				nameExist = "yes";
			}
			if(null != wangwang && wangwang.equals(user.getWangwang())) {
				wangwangExist = "yes";
			}
			if(null != phone && phone.equals(user.getPhone())) {
				phoneExist = "yes";
			}
			if(null != mail && mail.equals(user.getMail())) {
				mailExist = "yes";
			}
		}
		
		responseMap.put("nameExist", nameExist);
		responseMap.put("wangwangExist", wangwangExist);
		responseMap.put("phoneExist", phoneExist);
		responseMap.put("mailExist", mailExist);
		responseMap.put("name", name);
		responseMap.put("wangwang", wangwang);
		responseMap.put("phone", phone);
		responseMap.put("mail",mail );
		
		//转化为json对象
		JSONObject json = JSONObject.fromObject(responseMap);		
		response.setContentType("text/html;charset=utf-8"); 
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();
		} catch (IOException e) {
			
			logger.info("传输json出错");
			
		}
		
	}

}
