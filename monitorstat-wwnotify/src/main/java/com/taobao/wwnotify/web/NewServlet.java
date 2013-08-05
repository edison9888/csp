package com.taobao.wwnotify.web;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.taobao.wwnotify.biz.TextConvertHtml;
import com.taobao.wwnotify.biz.WwNotifyMessageManager;

/**
 * 
 * @author wzm2162
 * 
 */

public class NewServlet extends HttpServlet {
	
	private static final Logger logger =  Logger.getLogger(NewServlet.class);	
	
	
	private static final long serialVersionUID = 4766977069197312353L;
	private static final Logger log = Logger.getLogger(NewServlet.class);
	private static final String sign_key = "asdfioasdjfoaofi!@#RQ$R@#$%WERGSGW#$";
	private static final String sign_key_b2b = "b2bW@#RASDFQR@#SDVAWS@#$";
	private WwNotifyMessageManager wwNotifyMessageManager;

	public void init(ServletConfig config) throws ServletException {
		String host = config.getInitParameter("host");
		Integer port = Integer.parseInt(config.getInitParameter("port"));
		
		wwNotifyMessageManager = new WwNotifyMessageManager();
		wwNotifyMessageManager.setHost(host);
		wwNotifyMessageManager.setPort(port);
		wwNotifyMessageManager.init();
	}
	
	public Map<String,String> senderMap = new ConcurrentHashMap<String, String>();
	
	
	
	public String getSenderSign(String key){
		String sign = senderMap.get(key);
		if(sign == null){
			synchronized (senderMap) {
				sign = senderMap.get(key);
				if(sign == null){
					ResourceBundle bundle = ResourceBundle.getBundle("relation");
					String value = bundle.getString(key);
					if(value != null){
						senderMap.put(key, value);
						return value;
					}else{
						return null;
					}
				}else{
					return sign;
				}
			}
			
		}else{
			return sign;
		}
		
	}
	
	

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=GBK");
		req.setCharacterEncoding("GBK");
		
		String nick = req.getParameter("nick");
		String subject = req.getParameter("subject");
		String memo = req.getParameter("context");
		String sign = req.getParameter("sign");
		String from = req.getParameter("from");
		String sender = req.getParameter("sender");
		String mainTitle = req.getParameter("mainTitle");
		
		if(mainTitle == null){
			mainTitle = "系统内部消息";
		}
		
		
		if(sender == null){
			String sk = null;
			if ("b2b".equals(from)) {
				sk = sign_key_b2b;
			} else {
				sk = sign_key;
			}
			
			String result = "...";
			try {
				if (StringUtil.isNotBlank(nick) && StringUtil.isNotBlank(subject) && StringUtil.isNotBlank(memo)) {
					String si = DigestUtils.shaHex(nick + subject + memo + sk);
					if (si.equals(sign)) {
						if ("b2b".equals(from)) {
							wwNotifyMessageManager.sendNotifyMessage("b2b", nick,mainTitle, subject, memo);
							logger.info("b2b Notify: " + nick + ", subject: " + subject + ", content: " + memo);
						} else {
							String memo_new = TextConvertHtml.urlConvertHtml(memo);
							wwNotifyMessageManager.sendNotifyMessage(nick,mainTitle, subject, memo_new);
							logger.info("taobao Notify: " + nick + ", subject: " + subject + ", content: " + memo_new);
						}
						result = "ok";
						
					} else {
						result = "invalid";
					}
				} else {
					result = "miss params";
				}
			} catch (Throwable e) {
				result = "error";
				log.error("", e);
			}
			resp.getWriter().println(result);
		}else{
			String result = "...";
			try {
				if (StringUtil.isNotBlank(nick) && StringUtil.isNotBlank(subject) && StringUtil.isNotBlank(memo)) {
					String si = DigestUtils.shaHex(nick + subject + memo + getSenderSign(sender));
					if (si.equals(sign)) {
						String memo_new = TextConvertHtml.urlConvertHtml(memo);
						wwNotifyMessageManager.sendNotifyMessage(nick,mainTitle, subject, memo_new);
						logger.info("taobao Notify sender:"+sender+" " + nick + ", subject: " + subject + ", content: " + memo_new);
						result = "ok";
						
					} else {
						result = "invalid";
					}
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
}

