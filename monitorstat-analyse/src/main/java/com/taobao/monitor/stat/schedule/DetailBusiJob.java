/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.stat.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.db.impl.DetailBusiDataDAO;
import com.taobao.monitor.common.db.impl.DetailMonitorDAO;
import com.taobao.monitor.common.po.DetailStatisticDO;
import com.taobao.monitor.stat.util.CalendarUtil;

/**
 *  ���job����Ҫ���þ��Ǵ�ʵʱͳ�Ʊ�ms_monitor_data_20100518 ��ȥȡ��ǰһ������ݣ�
 *  ���л��ܺ󣬷ŵ�BUSI_DAILY_DATA����,����ÿ��key��1������ݻ��ܳ�һ������
 *  
 *  Ҫ�Ľ��� �������ָ��ĳһ���ͳ�ƣ�
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-2:����08:03:56
 *
 */
public class DetailBusiJob implements Job {
    
    private static  Logger log = Logger.getLogger(DetailBusiJob.class);
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    private static final String  PREFIX_PAGE = "detail_page";
    private static final String  PREFIX_RESP = "detail_resp";
    private static final String  PREFIX_REFE = "detail_refer";
    
    //ֱ��д���ɣ���˸տ�ʼ���ɵ�key�ǲ�����һ��group�ģ����groupId����detail�Լ�Ϊ��ͳ�Ʒ������
    private int[] groupIds = new int[]{1,2,3};
    
    private static Map<Integer,String> groupId2prefix = new HashMap<Integer,String>();

    DetailBusiDataDAO  detailBusiDataDAO =   new DetailBusiDataDAO();

    
    DetailMonitorDAO detailDAO =  new DetailMonitorDAO();
    
    
    static {
	
	groupId2prefix.put(1,PREFIX_PAGE );
	groupId2prefix.put(2,PREFIX_RESP );
	groupId2prefix.put(3,PREFIX_REFE );
	
	
    }
    /* (non-Javadoc) 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
	log.warn("��ʼͳ��   " +CalendarUtil.getPreviousTime(null)+ " ������");
	
	
	
	for (int i = 0; i < groupIds.length; i++) {
	    
	    genstatistic( groupIds[i]);
	}
	
	
	log.warn(CalendarUtil.getPreviousTime(null)+ " ������ͳ�ƽ���");

    }
    /**
     * 
     */
    private void genstatistic(int groupId) {
	Date date = CalendarUtil.getStartTime(null, -1);
	
	String tableSuffix = sdf.format(date);
	
	//���ҵ���Ӧgroup ��key �б�
	Map<Integer,String> pagekeyMap = detailDAO.queryKeyInfoByLikedName(groupId2prefix.get(groupId));
	
	List<Integer> keyList = new ArrayList<Integer>();
	keyList.addAll(pagekeyMap.keySet());
	StringBuilder keyIds = new StringBuilder();
	boolean first = true;
	
	//�õ�idString
	for (Iterator<Integer> iterator = pagekeyMap.keySet().iterator(); iterator.hasNext();) {
		   
	    if( first ){
		first = false;	
		keyIds.append(iterator.next());
		
	    }else{
		keyIds.append(",");
		keyIds.append(iterator.next());
	    }
	    
	}
	
	
	
	//��ѯ�õ���Щkey��Ӧ�Ĳ�������,�ⷽ�����ص������Ѿ�����keyֵ�������ۼ�
	Map<Integer,Integer> groupMap = detailDAO.queryStatic(tableSuffix, keyIds.toString(), 1, CalendarUtil.getStartTime(null, -1), CalendarUtil.getEndTime(null, -1));
	
	int sum = 0;
	
	//������ܵĲ�����¼��
	for (Iterator<Integer> iterator = groupMap.keySet().iterator(); iterator.hasNext();) {
		   
	    Integer key = (Integer) iterator.next(); 	    
	    sum = sum + groupMap.get(key);
	    
	}
	//����װÿ��key��ͳ����Ϣ,�������ݿ�
	for (Iterator<Integer> iterator = groupMap.keySet().iterator(); iterator.hasNext();) {
	   
	    Integer key = (Integer) iterator.next();
	    DetailStatisticDO statistic = new DetailStatisticDO();
	    statistic.setAppId(1);
	    statistic.setCreateDate(new Date());
	    statistic.setStatisticDate(CalendarUtil.getStartTime(null, -1));
	    statistic.setGroupId(groupId);
	    statistic.setKeyId(key);
	    statistic.setVaue(groupMap.get(key));
	    statistic.setGroupSum(sum);	
	    
	    detailBusiDataDAO.insertDailyStatisticData(statistic);
	    
	}
    }

}
