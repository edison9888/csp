package com.taobao.monitor.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.AlarmThread;
import com.taobao.monitor.alarm.alipay.thread.JudgeAlipayBizThread;
import com.taobao.monitor.alarm.deploy.FetchDeployThread;
import com.taobao.monitor.alarm.opsfree.OpsFreeAppHostAmountThread;
import com.taobao.monitor.alarm.source.thread.AlarmSourceDependencyRefresh;
import com.taobao.monitor.alarm.trade.notify.thread.JudgeNotifyMessageThread;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.stable.RetrievedMonitorThread;
import com.taobao.monitor.web.cache.CacheTimeData;
import com.taobao.monitor.web.jprof.AutoJprofManage;
import com.taobao.monitor.web.schedule.ScheduleControl;

/**
 * 
 * @author xiaodu
 * @version 2010-5-12 ����10:19:20
 */
public class InitServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(InitServlet.class);
	
	public void init() throws ServletException {
		
		
		Thread thread = new Thread(){
			
			public void run(){
				logger.info("��ʼ------------------------------");
				
				
				System.out.println("------------------------------");
				OpsFreeAppHostAmountThread.get().startup(); //���ڴ�opsFree�ϻ�ȡӦ�û������������������ô�configserver��ȡ�����б��жϵķ�ֵ
				
				AlarmSourceDependencyRefresh.get().startup();//����ˢ��key ��������Ϣ
				
				FetchDeployThread.get().startup();
				ScheduleControl.startSchedule();//������ʱ����
				CacheTimeData.get().cacheData();//һЩ pv load cpu�Ȼ���
				RetrievedMonitorThread.startup();
				
				CspSyncOpsHostInfos.startupSyncThread();
				JudgeNotifyMessageThread.get().startup(); //��notify����Ϣ�����бȽ�
				JudgeAlipayBizThread.get().startup(); //��֧����������������������жԱ�
				
				logger.info("����------------------------------");
			}
			
		};
		thread.setDaemon(false);
		thread.start();


//		BaseLineThread thread = new BaseLineThread();
//		thread.start();
		//BaseLineAlarmThread.get().startup();
		
	}

	
	public void init(ServletConfig config) throws ServletException {
		String realpath = config.getServletContext().getRealPath("/");
		System.out.println(realpath);
		AutoJprofManage.get().setRealPath(realpath);
		super.init(config);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		
//		List<Navigation> list = new ArrayList<Navigation>();
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("�ձ�ģ��");
//			navigation.setHref("./index_day.jsp");
//			List<AppInfoPo> listApp = MonitorDayAo.get().findAllApp();
//			for(AppInfoPo po:listApp){
//				SubNavigation sub = new SubNavigation();
//				sub.setText(po.getAppName());
//				sub.setHref("./app_detail.jsp?selectAppName="+po.getAppName());
//				sub.setId(po.getAppId()+"");
//				navigation.getChildren().add(sub);
//			}
//			list.add(navigation);
//		}
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("ʵʱģ��");
//			navigation.setHref("./index_time.jsp");
//			List<AppInfoPo> listApp = AppInfoAo.get().findAllAppInfo();
//			for(AppInfoPo po:listApp){
//				SubNavigation sub = new SubNavigation();
//				sub.setText(po.getAppName());
//				sub.setHref("./time/app_time.jsp?appName="+po.getAppName());
//				sub.setId(po.getAppId()+"");
//				navigation.getChildren().add(sub);
//			}
//			list.add(navigation);
//		}
//		
//		
//		
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("Jprofģ��");
//			navigation.setHref("./jprof/manage_jprof_host.jsp");
//			List<JprofHost> listApp = MonitorJprofAo.get().findAllJprofHosts();
//			for(JprofHost po:listApp){
//				SubNavigation sub = new SubNavigation();
//				sub.setText(po.getAppName());
//				sub.setHref("./jprof/manage_jprof_class_method.jsp?appName="+po.getAppName());
//				sub.setId(po.getId()+"");
//				navigation.getChildren().add(sub);
//			}
//			list.add(navigation);
//		}
//		
//		
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("�����Զ�ѹ��ģ��");
//			navigation.setHref("./load/app_loadrun_list.jsp");
//			List<LoadRunHost> listApp = MonitorLoadRunAo.get().findAllLoadRunHost();
//			for(LoadRunHost po:listApp){
//				SubNavigation sub = new SubNavigation();
//				sub.setText(AppCache.get().getKey(po.getAppId()).getAppName());
//				sub.setHref("./load/app_loadrun_detail.jsp?appId="+po.getAppId()+"&hostip="+po.getHostIp());
//				sub.setId(po.getAppId()+"");
//				navigation.getChildren().add(sub);
//			}
//			list.add(navigation);
//		}
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("����ģ��");
//			navigation.setHref("./health/index.jsp");
//			List<RatingApp> listApp = MonitorRatingAo.get().findAllRatingApp();
//			for(RatingApp po:listApp){
//				SubNavigation sub = new SubNavigation();
//				sub.setText(AppCache.get().getKey(po.getAppId()).getAppName());
//				sub.setHref("./health/manage_rating_app.jsp");
//				sub.setId(po.getAppId()+"");
//				navigation.getChildren().add(sub);
//			}
//			list.add(navigation);
//		}
//		
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("�澯��ʷ���ݲ�ѯ");
//			navigation.setHref("./load/app_loadrun_list.jsp");
//			
//			SubNavigation s1 = new SubNavigation();
//			s1.setText("��ѯ�澯��Ϣ");
//			s1.setHref("./alarm/alarm_record_detail.jsp");
//			
//			navigation.getChildren().add(s1);
//			
//			SubNavigation s2 = new SubNavigation();
//			s2.setText("�鿴�澯��Ϣ����");
//			s2.setHref("./alarm/alarm_record.jsp");
//			
//			navigation.getChildren().add(s2);
//			
//			SubNavigation s3 = new SubNavigation();
//			s2.setText("�쳣��Ϣ�鿴");
//			s2.setHref("./alarm/alarm_record_exception.jsp");			
//			navigation.getChildren().add(s3);
//			list.add(navigation);
//			
//		}
//		
//		{
//			Navigation navigation = new Navigation();
//			navigation.setText("�ܱ�ģ��");
//			navigation.setHref("./week_report.jsp");
//			list.add(navigation);
//		}
//		
//		
//		
//		try {
//			JSONArray json = JSONArray.fromObject(list);
//			resp.setContentType("text/html;charset=utf-8"); 
//			resp.getWriter().write(json.toString());
//			resp.flushBuffer();
//		} catch (IOException e) {
//		}
//		return;
//		
//	}
	
//	public class Navigation{
//		private String text;
//		private String href;
//		private String hrefTarget = "_blank";
//		private List<SubNavigation> children = new ArrayList<SubNavigation>();
//		public String getText() {
//			return text;
//		}
//		public void setText(String text) {
//			this.text = text;
//		}
//		public List<SubNavigation> getChildren() {
//			return children;
//		}
//		public void setChildren(List<SubNavigation> children) {
//			this.children = children;
//		}
//		public String getHref() {
//			return href;
//		}
//		public void setHref(String href) {
//			this.href = href;
//		}
//		public String getHrefTarget() {
//			return hrefTarget;
//		}
//		public void setHrefTarget(String hrefTarget) {
//			this.hrefTarget = hrefTarget;
//		}
//		
//		
//	}
//	
//	
//	public class SubNavigation{
//		private String id;
//		private String text;
//		private String href;
//		private String hrefTarget = "_blank";
//		private boolean leaf = true;
//		public String getId() {
//			return id;
//		}
//		public void setId(String id) {
//			this.id = id;
//		}
//		public String getText() {
//			return text;
//		}
//		public void setText(String text) {
//			this.text = text;
//		}
//		public String getHref() {
//			return href;
//		}
//		public void setHref(String href) {
//			this.href = href;
//		}
//		public String getHrefTarget() {
//			return hrefTarget;
//		}
//		public void setHrefTarget(String hrefTarget) {
//			this.hrefTarget = hrefTarget;
//		}
//		public boolean isLeaf() {
//			return leaf;
//		}
//		public void setLeaf(boolean leaf) {
//			this.leaf = leaf;
//		}
//		
//		
//		
//		
//	}
	

}
