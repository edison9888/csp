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
 * CSP定时入库任务
 * 1.3分钟入库
 * 2.入库之后12分钟后定时淘汰，此时如果有更新，则取消该定时任务
 * 
 * 锁全部在PropertyEntry级别
 * @author bishan.ct
 *
 */
public class CspTimeTask extends AbstraceTimeTask{

	private static final Logger logger = Logger.getLogger(CspTimeTask.class);
	private static DataEvent hbaseEvent=ValueEventFactory.hbaseEvent;
	private static DataEvent apphbaseEvent=ValueEventFactory.apphbaseEvent;
	
	private boolean isTenTask;//是否10分钟的任务
	
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
			//淘汰
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
	 * 数据淘汰
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
		
		//检查下整个key有没有过期
		//线程安全
		if(currentM-ake.getRecentlyAcceptTim()>Constants.twelMinute){
			if(MemcacheStandard.get().rempoveKey(ake.getFullKeyName())){
				MonitorLog.addStat("datacache", new String[]{"timKeyeEvict"}, new Long[]{1L});
				return;
			}
		}
		//检查整个属性是否过期
		//线程安全
		if(currentM-pe.getRecentlyAcceptTim()>Constants.twelMinute){
			if(ake.removeProperty(pe.getPropertyName())){
				MonitorLog.addStat("datacache", new String[]{"timProeEvict"}, new Long[]{1L});
				return;
			}
		}
		
		/**
		 * 线程安全,锁住了整个属性，期间不会有对value的操作
		 */
		synchronized (pe) {	
			//小于12分钟的不能淘汰
			if(currentM<deadLine){
				MonitorLog.addStat("datacache", new String[]{"evictnotsame"}, new Long[]{1L});
				return;
			}
			pe.getValues()[index]=null;
			MonitorLog.addStat("datacache", new String[]{"timValueEvict"}, new Long[]{1L});
		}
	}
	/**
	 * 初始状态，过期时间3分钟后
	 * 1.有可能有定时淘汰任务正在计时，则执行移出该任务
	 * 2.不排除load很高，3之前3分钟的数据还未入库，此时数据就会丢失，不过这种情况应该很少
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
	 * 3分钟入库，理论上此时该value已经不会有写入，所以没有加锁
	 * 实际可能有个别value还是有写入，这部分会丢失
	 * 如果在3分钟内数据就被覆盖，那写入数据会异常，这种情况暂时没做处理
	 */
	private void insertHbase(){
		MonitorLog.addStat("datacache", new String[]{"wheelgointohbase"}, new Long[]{1L});
		
		if(pe==null) return;
		KeyEntry ake=pe.getKeyEntry();
		if(ake==null) return;
		
		
		synchronized (pe) {
			//并发原因，不该被调度的执行了
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
				logger.error("缓存数据异常 为空");
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
