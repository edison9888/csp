package com.taobao.www.common;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.taobao.www.arkclient.csp.ManualCurUser;

@SuppressWarnings("serial")
public class CommonAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	public HttpServletRequest request;

	public HttpServletResponse response;

	public ActionContext ctx = ActionContext.getContext();

	protected HttpServletRequest requests = (HttpServletRequest) ctx

	.get(ServletActionContext.HTTP_REQUEST);

	protected String urlPath = requests.getRequestURI();

	public String baseUrl = ResourceBundle.getBundle("common").getString("BASE_URL");

	public CommonAction() {
		ManualCurUser.getLoginUserName(requests);
	}

	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * 功能：比较两个日期相差时分秒。
	 * 
	 * @param startTime
	 *            比较日期1
	 * @param endTime
	 *            比较日期2
	 * @return 返回比较的结果
	 * @author wb-tangjinge
	 * @time 2011-11-27
	 */
	protected String dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff = 0;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff % nd / nh + "小时" + diff % nd % nh / nm + "分钟" + diff % nd % nh % nm / ns + "秒";
	}
}
