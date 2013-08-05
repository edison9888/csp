package com.taobao.wwnotify.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.taobao.wwnotify.biz.TextConvertHtml;
import com.taobao.wwnotify.biz.WwNotifyMessageManager;


/**
 * 
 * @author wzm2162
 * 
 */
public class MesgServlet extends HttpServlet {
	private static final long serialVersionUID = 4766977069197312353L;
	private static final Logger log = Logger.getLogger(MesgServlet.class);
	private WwNotifyMessageManager wwNotifyMessageManager;

	public void init(ServletConfig config) throws ServletException {
		String host = config.getInitParameter("host");
		Integer port = Integer.parseInt(config.getInitParameter("port"));

		wwNotifyMessageManager = new WwNotifyMessageManager();
		wwNotifyMessageManager.setHost(host);
		wwNotifyMessageManager.setPort(port);
		wwNotifyMessageManager.init();
	}

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=GBK");
		req.setCharacterEncoding("GBK");

		String nick = req.getParameter("user");
		String subject = req.getParameter("subject");
		String memo = req.getParameter("msg");
		
		memo = TextConvertHtml.urlConvertHtml(memo);

		String result = "...";
		try {
			if (StringUtil.isNotBlank(nick) && StringUtil.isNotBlank(subject) && StringUtil.isNotBlank(memo)) {
				wwNotifyMessageManager.sendNotifyMessage(nick,"mianTitle",subject, memo);
				result = "ok";
				System.out.println("Notify: " + nick + ", subject: " + subject + ", content: " + memo);
			} else {
				result = "miss params";
			}
		} catch (Throwable e) {
			result = "error";
			log.error("", e);
		}
		resp.getWriter().println(result);
	}
}
