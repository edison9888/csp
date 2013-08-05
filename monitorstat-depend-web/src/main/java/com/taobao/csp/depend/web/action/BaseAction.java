package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.util.ConstantParameters;

public class BaseAction implements ConstantParameters {
	
	Logger logger = Logger.getLogger(BaseAction.class);
	/**
	 * ajax方式返回字符串
	 * @param str			要返回的字符串
	 * @param response		需要的输出流
	 * @return
	 */
	public boolean writeResponse(String str, HttpServletResponse response){
		boolean ret = true;
		try{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(str);
			response.flushBuffer();
			pw.close();
		}catch (Exception e) {
			logger.debug("Ajax返回信息是异常", e);
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
