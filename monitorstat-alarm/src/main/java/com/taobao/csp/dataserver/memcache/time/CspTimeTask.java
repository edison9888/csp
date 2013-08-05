package com.taobao.csp.dataserver.memcache.time;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.MemcacheStandard;
import com.taobao.csp.dataserver.memcache.entry.KeyEntry;
import com.taobao.csp.dataserver.memcache.entry.PropertyEntry;
import com.taobao.csp.dataserver.memcache.event.DataEvent;
import com.taobao.csp.dataserver.memcache.event.ValueEventFactory;
import com.taobao.monitor.MonitorLog;

/**
 * CSP��ʱ�������
 * 1.3�������
 * 2.���֮��12���Ӻ�ʱ��̭����ʱ����и��£���ȡ���ö�ʱ����
 * 
 * ��ȫ����PropertyEntry����
 * @author bishan.ct
 *
 */
public class CspTimeTask extends AbstraceTimeTask{

	private static final Logger logger = Logger.getLogger(CspTimeTask.class);
	private static DataEvent hbaseEvent=ValueEventFactory.hbaseEvent;
	private static DataEvent apphbaseEvent=ValueEventFactory.apphbaseEvent;
	
	private boolean isTenTask;//�Ƿ�10���ӵ�����
	
	public CspTimeTask(PropertyEntry pe,int index){
		this.pe= pe;
		this.index=index;
		//timeout = new HashedWheelTimeout();
	}
	
	private int index;
	private long deadLine;
	private PropertyEntry pe;
	
	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}

	@Override
	public void run() {
		try{
			//��̭
			if(isTenTask){
				evict();
			}else{
				insertHbase();
			}
		}catch(Exception e){
			logger.warn("time wheel ",e);
		}
	}
	
	/**
	 * ������̭
	 */
	private void evict(){
		MonitorLog.addStat("datacache", new String[]{"tryEvict"}, new Long[]{1L});
		
		if(pe==null){
			return;
		}
		KeyEntry ake=pe.getKeyEntry();
		if(ake==null){
			return;
		}
		long currentM=System.currentTimeMillis();
		
		//���������key��û�й���
		//�̰߳�ȫ
		if(currentM-ake.getRecentlyAcceptTim()>Constants.twelMinute){
			if(MemcacheStandard.get().rempoveKey(ake.getFullKeyName())){
				MonitorLog.addStat("datacache", new String[]{"timKeyeEvict"}, new Long[]{1L});
				return;
			}
		}
		//������������Ƿ����
		//�̰߳�ȫ
		if(currentM-pe.getRecentlyAcceptTim()>Constants.twelMinute){
			if(ake.removeProperty(pe.getPropertyName())){
				MonitorLog.addStat("datacache", new String[]{"timProeEvict"}, new Long[]{1L});
				return;
			}
		}
		
		/**
		 * �̰߳�ȫ,��ס���������ԣ��ڼ䲻���ж�value�Ĳ���
		 */
		synchronized (pe) {	
			//С��12���ӵĲ�����̭
			if(currentM<deadLine){
				MonitorLog.addStat("datacache", new String[]{"evictnotsame"}, new Long[]{1L});
				return;
			}
			pe.getValues()[index]=null;
			MonitorLog.addStat("datacache", new String[]{"timValueEvict"}, new Long[]{1L});
		}
	}
	/**
	 * ��ʼ״̬������ʱ��3���Ӻ�
	 * 1.�п����ж�ʱ��̭�������ڼ�ʱ����ִ���Ƴ�������
	 * 2.���ų�load�ܸߣ�3֮ǰ3���ӵ����ݻ�δ��⣬��ʱ���ݾͻᶪʧ�������������Ӧ�ú���
	 */
	public void initStat(){
		isTenTask=false;
		deadLine=System.currentTimeMillis()+Constants.threeMinute;
		
		if(getMySet()!=null){
			MonitorLog.addStat("datacache", new String[]{"timeremove"}, new Long[]{1L});
        	getMySet().remove(timeout);
        	setMySet(null);
		}
	}
	
	public void evictStat(){
		isTenTask=true;
		deadLine=System.currentTimeMillis()+Constants.twelMinute;
		setMySet(null);
		//CspTimeSchedu.timer.newTimeout(this, 12, TimeUnit.MINUTES);
	}
	
	/**
	 * 3������⣬�����ϴ�ʱ��value�Ѿ�������д�룬����û�м���
	 * ʵ�ʿ����и���value������д�룬�ⲿ�ֻᶪʧ
	 * �����3���������ݾͱ����ǣ���д�����ݻ��쳣�����������ʱû������
	 */
	private void insertHbase(){
		MonitorLog.addStat("datacache", new String[]{"wheelgointohbase"}, new Long[]{1L});
		
		if(pe==null) return;
		KeyEntry ake=pe.getKeyEntry();
		if(ake==null) return;
		
		
		synchronized (pe) {
			//����ԭ�򣬲��ñ����ȵ�ִ����
			long nowt=System.currentTimeMillis();
			long syst=deadLine-nowt;
			
			if(syst>0){
				MonitorLog.addStat("datacache", new String[]{"wheelnotsame"}, new Long[]{1L,
						syst});
				return;
			}
			
			String appName = pe.getKeyEntry().getAppName();
			String propname = pe.getPropertyName();
			if (pe.getValues()[index] == null) {
				logger.error("���������쳣 Ϊ��");
				return;
			}
//			DataEntry e2 = pe.getValues()[index];
//			
//			Object value = e2.getValue();
//			long collectime = e2.getTime();
//			String fullName = pe.getKeyEntry().getFullKeyName();
//			
//			if (ake.getKeyScope() == KeyScope.APP) {
//				apphbaseEvent.onEvent(appName, propname, fullName, collectime, value);
//			}else if(ake.isHbase()){
//				hbaseEvent.onEvent(appName, propname, fullName, collectime, value);
//			}
//		
		}
		
	}
}
