package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
	protected void writeToResponse(HttpServletResponse response, String str)
			throws IOException {
		PrintWriter out = response.getWriter();
		out.print(str);
		out.flush();
	}

	protected void writeJSONToResponseJSONArray(HttpServletResponse response,
			Object obj) throws IOException {
		
		JSONArray jsonarr = JSONArray.fromObject(obj);
		String jsonStr = jsonarr.toString();
		writeJSONToResponse(response, jsonStr);
	}
	protected void writeJSONToResponseJSONObject(HttpServletResponse response,
			Object obj) throws IOException {
		
		JSONObject jsonObj = JSONObject.fromObject(obj);
		String jsonStr = jsonObj.toString();
		writeJSONToResponse(response, jsonStr);
	}
	
	/**
	 *  π”√FastJson
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	protected void writeFastJsonForObject(HttpServletResponse response,
			Object obj) throws IOException {
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(obj);
		writeJSONToResponse(response, jsonStr);
	}
	
	protected void writeFastJsonForArray(HttpServletResponse response,
			Object obj) throws IOException {
		String jsonStr = com.alibaba.fastjson.JSONArray.toJSONString(obj);
		writeJSONToResponse(response, jsonStr);
	}
	
	protected void writeJSONToResponse(HttpServletResponse response, String str)
			throws IOException {
		response.setContentType("application/json");
		writeToResponse(response, str);
	}

	private static final Logger log = LoggerFactory
			.getLogger(BaseController.class);
}
