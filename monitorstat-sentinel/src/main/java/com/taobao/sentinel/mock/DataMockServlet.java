package com.taobao.sentinel.mock;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/***
 * mock data
 * @author youji.zj
 *
 */
public class DataMockServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(DataMockServlet.class);
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Writer writer = resp.getWriter();
		
		// data for map type
		if ("map".equals(req.getParameter("type"))) {
			Map<String, String> ipMap = new HashMap<String, String>();
			ipMap.put("192.168.1.1", "100");
			ipMap.put("192.168.1.2", "200");
			ipMap.put("192.168.1.3", "300");
			JSONObject object = JSONObject.fromObject(ipMap);
			writer.write(object.toString());
		}
		
		// data for list type
		if ("list".equals(req.getParameter("type"))) {
			List<String> ips = new ArrayList<String>();
		    ips.add("192.168.1.1");
			ips.add("192.168.1.2");
			ips.add("192.168.1.3");
			for (int i = 4; i < 200; i++) {
				ips.add("192.168.1." + i);
			}
			JSONArray array = JSONArray.fromObject(ips);
			writer.write(array.toString());
		}
		
		if ("alter".equals(req.getParameter("type"))) {
			
			writer.write("alter success");
		}
		
		writer.flush();
	}

}
