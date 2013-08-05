
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.url;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.common.ZKClient;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;

/**
 * 
 * �����ҪURL
 * 
 * 
 * @author xiaodu
 *
 * ����3:26:47
 */
public class UrlMonitorThread implements Runnable{
	static {
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
				"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
	}
	private Thread thread = null;

	private List<CheckUrl> checkUrlList = new ArrayList<CheckUrl>();

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	private MessageSend messageSend = MessageSendFactory.create(MessageSendType.WangWang);

	private static String URL_MONITOR_NODE = "/csp/monitor/url";

	private List<String> acceptList = Arrays.asList("С��","����","����","��ͤ");


	private static UrlMonitorThread monitor = null;


	public static synchronized void startup(){

		if(monitor == null){
			monitor = new UrlMonitorThread();
		}
	}

	private UrlMonitorThread(){
		resetCheckUrl();
		thread = new Thread(this);
		thread.setDaemon(false); 
		thread.setName("URL-Monitor");
		thread.start();
	}


	public static void addCheckUrl(CheckUrl checkUrl){
		String nodeName = checkUrl.getUrl().replaceAll("\\/", "\\$");
		ZKClient.get().mkdirPersistent(URL_MONITOR_NODE+"/"+nodeName,checkUrl);
	}


	private void  resetCheckUrl(){

		List<CheckUrl> list = new ArrayList<CheckUrl>();

		List<String> urlList = ZKClient.get().list(URL_MONITOR_NODE,new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				switch(event.getType()){
				case NodeChildrenChanged:
					resetCheckUrl();
					break;
				}
			}
		});
		for(String url:urlList){
			CheckUrl obj = (CheckUrl)ZKClient.get().getData(URL_MONITOR_NODE+"/"+url);
			list.add(obj);
		}
		checkUrlList = list;

	}


	public void runUrlCheck(){
		for(CheckUrl url:checkUrlList){
			singleUrlCheck(url);
		}
	}

	public void singleUrlCheck(CheckUrl url){

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MINUTE, -2);

		UrlAppDataCheck urlapp = new UrlAppDataCheck(url,sdf.format(cal.getTime()));
		//Ӧ����������
		if(urlapp.checking()){
			return ;
		}
		//�ٸ��ݸ��������������仯���
		UrlHostDataCheck urlhost = new UrlHostDataCheck(url,sdf.format(cal.getTime()));

		if(urlhost.checking()){
			return ;
		}

		//���� �쳣URL

		StringBuffer mapapp=urlapp.report();
		StringBuffer maprefer=urlapp.reportReferAffect();
		StringBuffer maphost=urlhost.reportReferAffect();

		StringBuffer sb = new StringBuffer();

		sb.append("Ӧ�� ["+url.getAppName()+"] ��URL:"+url.getAppName()).append("<br/>");
		sb.append(mapapp).append("<br/>");
		sb.append(maphost).append("<br/>");
		sb.append(maprefer).append("<br/>");

		for(String a:acceptList)
			messageSend.send(a, "��ҪURL�������", sb.toString());

	}

	@Override
	public void run() {

		while(true){

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
			}

			runUrlCheck();
		}



	}








}
