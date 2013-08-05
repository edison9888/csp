package com.taobao.csp.dataserver.memcache.time;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.memcache.entry.KeyEntry;
import com.taobao.csp.dataserver.memcache.entry.PropertyEntry;
import com.taobao.monitor.MonitorLog;

public class HashedWheelTimeout implements Timeout,Runnable {

	private static final Logger logger =
            LoggerFactory.getLogger(HashedWheelTimeout.class);
	
	 private static final int ST_INIT = 0;
     private static final int ST_CANCELLED = 1;
     private static final int ST_EXPIRED = 2;

//     private final AbstraceTimeTask task;
//     final long deadline;
     //��timeout��task�󶨣����ⴴ���������
     long deadline;
     volatile int stopIndex;
     volatile long remainingRounds;
     private final AtomicInteger state = new AtomicInteger(ST_INIT);

//=========================�����������
     private int vindex;
  	 private PropertyEntry pe;
     private String appName ;
     private String propname ;
     private String fullName ;
     
     public HashedWheelTimeout(PropertyEntry pe,int vindex){
    	 this.pe= pe;
 		 this.vindex=vindex;
 		 appName = pe.getKeyEntry().getAppName();
 		 propname = pe.getPropertyName();
		 fullName = pe.getKeyEntry().getFullKeyName();
     }
     
     @Override
     public Timer getTimer() {
         return CspTimeSchedu.timer;
     }

     @Override
     public boolean cancel() {
         if (!state.compareAndSet(ST_INIT, ST_CANCELLED)) {
             return false;
         }

         CspTimeSchedu.timer.getWheel()[stopIndex].remove(this);
         return true;
     }

     @Override
     public boolean isCancelled() {
         return state.get() == ST_CANCELLED;
     }

     @Override
     public boolean isExpired() {
         return state.get() != ST_INIT;
     }

     public void expire() {
         if (!state.compareAndSet(ST_INIT, ST_EXPIRED)) {
             return;
         }
         try {
         	CspTimeSchedu.time_execute.execute(this);
         } catch (Throwable t) {
             if (logger.isWarnEnabled()) {
                 logger.warn( "An exception was thrown ", t);
             }

         }
     }

	public synchronized void initStat(long deadline){
		this.deadline = deadline;
		
		if(cancel()){
			//�ɹ�ȡ�������ҴӶ������Ƴ�
			state.compareAndSet(ST_CANCELLED, ST_INIT);
		}else{
			//�Ѿ���ִ��
			state.set(ST_INIT);
		}
		if (pe.getValues()[vindex] != null) {
			pe.getValues()[vindex].setTimeOut(false);
		}
	}
	/**
	 * 1.3������⣬�����ϴ�ʱ��value�Ѿ�������д�룬����PropertyEntry����
	 * 
	 * 2.ʵ�ʿ����и���value������д�룬�ⲿ��������isTimeOut��־λ����
	 * 
	 * 3.�����3���������ݾͱ����ǣ���д�����ݻ��쳣�����������ʱû������
	 */
    private void insertHbase(){
 		MonitorLog.addStat("datacache", new String[]{"wheelgointohbase"}, new Long[]{1L});
 		
 		if(pe==null) return;
 		KeyEntry ake=pe.getKeyEntry();
 		if(ake==null) return;
 		
 		synchronized (pe) {
 			DataEntry e2=pe.getValues()[vindex];
 			if (e2 == null) {
 				logger.error("���������쳣 Ϊ��");
 				return;
 			}
 			e2.setTimeOut(true);
 			
 			e2.putValueToQueue(appName, propname, fullName, 
 					pe.getKeyEntry().getKeyScope());
 		}
 	}
     
    public void run() {
		try{
			insertHbase();
		}catch(Exception e){
			logger.warn("time wheel ",e);
		}
	}
    
     public long getDeadline() {
			return deadline;
	}

		@Override
     public String toString() {
         long currentTime = System.currentTimeMillis();
         long remaining = deadline - currentTime;

         StringBuilder buf = new StringBuilder(192);
         buf.append(getClass().getSimpleName());
         buf.append('(');

         buf.append("deadline: ");
         if (remaining > 0) {
             buf.append(remaining);
             buf.append(" ms later, ");
         } else if (remaining < 0) {
             buf.append(-remaining);
             buf.append(" ms ago, ");
         } else {
             buf.append("now, ");
         }

         if (isCancelled()) {
             buf.append(", cancelled");
         }

         return buf.append(')').toString();
     }
}
