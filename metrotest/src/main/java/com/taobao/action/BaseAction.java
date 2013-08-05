package com.taobao.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class BaseAction {
	Logger logger = Logger.getLogger(BaseAction.class);
	public boolean writeHTMLToResponse(String str, HttpServletResponse response){
		boolean ret = true;
		try{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(str);
			response.flushBuffer();
			pw.close();
		}catch (Exception e) {
			logger.debug("AjaxÂÒÂë", e);
		}
		return ret;
	}

	protected void writeJSONToResponseJSONArray(HttpServletResponse response,
			Object obj) throws IOException {

		JSONArray jsonarr = JSONArray.fromObject(obj);
		String jsonStr = jsonarr.toString();
		logger.debug(jsonStr);
		writeJSONToResponse(response, jsonStr);
	}
	protected void writeJSONToResponseJSONObject(HttpServletResponse response,
			Object obj) throws IOException {

		JSONObject jsonObj = JSONObject.fromObject(obj);
		String jsonStr = jsonObj.toString();
		writeJSONToResponse(response, jsonStr);
	}
	protected void writeJSONToResponse(HttpServletResponse response, String str)
			throws IOException {
		response.setContentType("application/json");
		writeToResponse(response, str);
	}
	protected void writeToResponse(HttpServletResponse response, String str)
			throws IOException {
		PrintWriter out = response.getWriter();
		out.print(str);
		out.flush();
	}
}
