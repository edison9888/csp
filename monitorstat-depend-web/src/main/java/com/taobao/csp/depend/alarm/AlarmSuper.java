package com.taobao.csp.depend.alarm;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.alarm.AlarmConfigPo;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;

public class AlarmSuper {
	private static final Logger logger = Logger.getLogger(AlarmSuper.class);
	String wangwangString = null;
	String emailString = null;
	MessageSend wangwangSend = MessageSendFactory.create(MessageSendType.WangWang);
	MessageSend emailSend = MessageSendFactory.create(MessageSendType.Email);
	
	Set<String> emailSet = new HashSet<String>();
	Set<String> wangwangSet = new HashSet<String>();
	
	public AlarmSuper() {
		emailSet.add("zhongting.zy@taobao.com");
//		emailSet.add("youji.zj@taobao.com");
//		emailSet.add("xiaodu@taobao.com");
//		emailSet.add("bishan.ct@taobao.com");
		
		wangwangSet.add("中亭");
//		wangwangSet.add("小赌");
//		wangwangSet.add("毕衫");
//		wangwangSet.add("游骥");
	}
	
	public void sendWangwang(String msg, AlarmConfigPo config) {
		logger.info("旺旺报警信息字数->" + msg.length());
		String wangwangs = config.getWangwangString();
		String[] wangwangArray = wangwangs.trim().split(",");
		for(String wangwang : wangwangArray) {
			wangwangSend.send(wangwang, "CSP依赖系统预警提示", msg);
			logger.info("成功发送旺旺信息给->" + wangwang);
		}
		for(String wangwang : wangwangSet) {
			wangwangSend.send(wangwang.trim(), "CSP依赖系统预警提示", msg);
			logger.info("成功发送旺旺信息给->" + wangwang);
		}
	}
	
	public void sendEmail(String msg, AlarmConfigPo config) {
		logger.info("Email报警信息字数->" + msg.length());
		String emails = config.getEmailString();
		String[] emailArray = emails.split(",");
		for(String email : emailArray) {
			emailSend.send(email, "CSP依赖系统提示", msg);
			logger.info("成功发送Email给->" + email);
		}
		for(String email : emailSet) {
			emailSend.send(email, "CSP依赖系统提示", msg);
			logger.info("成功发送Email给->" + email);
		}
	}
	
	public static void main(String[] args) {
		AlarmSuper po = new AlarmSuper();
		AlarmConfigPo config = new AlarmConfigPo();
		config.setWangwangString("中亭");
		config.setEmailString("zhongting.zy@taobao.com");
		String msg = "sssss<br/>3333";
		po.sendEmail(msg, config);
		po.sendWangwang(msg, config);
		System.out.println("over");
	}
}
