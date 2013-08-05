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
		
		wangwangSet.add("��ͤ");
//		wangwangSet.add("С��");
//		wangwangSet.add("����");
//		wangwangSet.add("����");
	}
	
	public void sendWangwang(String msg, AlarmConfigPo config) {
		logger.info("����������Ϣ����->" + msg.length());
		String wangwangs = config.getWangwangString();
		String[] wangwangArray = wangwangs.trim().split(",");
		for(String wangwang : wangwangArray) {
			wangwangSend.send(wangwang, "CSP����ϵͳԤ����ʾ", msg);
			logger.info("�ɹ�����������Ϣ��->" + wangwang);
		}
		for(String wangwang : wangwangSet) {
			wangwangSend.send(wangwang.trim(), "CSP����ϵͳԤ����ʾ", msg);
			logger.info("�ɹ�����������Ϣ��->" + wangwang);
		}
	}
	
	public void sendEmail(String msg, AlarmConfigPo config) {
		logger.info("Email������Ϣ����->" + msg.length());
		String emails = config.getEmailString();
		String[] emailArray = emails.split(",");
		for(String email : emailArray) {
			emailSend.send(email, "CSP����ϵͳ��ʾ", msg);
			logger.info("�ɹ�����Email��->" + email);
		}
		for(String email : emailSet) {
			emailSend.send(email, "CSP����ϵͳ��ʾ", msg);
			logger.info("�ɹ�����Email��->" + email);
		}
	}
	
	public static void main(String[] args) {
		AlarmSuper po = new AlarmSuper();
		AlarmConfigPo config = new AlarmConfigPo();
		config.setWangwangString("��ͤ");
		config.setEmailString("zhongting.zy@taobao.com");
		String msg = "sssss<br/>3333";
		po.sendEmail(msg, config);
		po.sendWangwang(msg, config);
		System.out.println("over");
	}
}
