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
 *  这个job的主要作用就是从实时统计表ms_monitor_data_20100518 中去取出前一天的数据，
 *  进行汇总后，放到BUSI_DAILY_DATA表中,对于每个key，1天的数据汇总成一条数据
 *  
 *  要改进成 可以针对指定某一天的统计，
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-2:下午08:03:56
 *
 */
public class DetailBusiJob implements Job {
    
    private static  Logger log = Logger.getLogger(DetailBusiJob.class);
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    private static final String  PREFIX_PAGE = "detail_page";
    private static final String  PREFIX_RESP = "detail_resp";
    private static final String  PREFIX_REFE = "detail_refer";
    
    //直接写死吧，因此刚开始生成的key是不属于一个group的，这个groupId，是detail自己为了统计方便而加
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
	
	log.warn("开始统计   " +CalendarUtil.getPreviousTime(null)+ " 的数据");
	
	
	
	for (int i = 0; i < groupIds.length; i++) {
	    
	    genstatistic( groupIds[i]);
	}
	
	
	log.warn(CalendarUtil.getPreviousTime(null)+ " 的数据统计结束");

    }
    /**
     * 
     */
    private void genstatistic(int groupId) {
	Date date = CalendarUtil.getStartTime(null, -1);
	
	String tableSuffix = sdf.format(date);
	
	//先找到对应group 的key 列表
	Map<Integer,String> pagekeyMap = detailDAO.queryKeyInfoByLikedName(groupId2prefix.get(groupId));
	
	List<Integer> keyList = new ArrayList<Integer>();
	keyList.addAll(pagekeyMap.keySet());
	StringBuilder keyIds = new StringBuilder();
	boolean first = true;
	
	//得到idString
	for (Iterator<Integer> iterator = pagekeyMap.keySet().iterator(); iterator.hasNext();) {
		   
	    if( first ){
		first = false;	
		keyIds.append(iterator.next());
		
	    }else{
		keyIds.append(",");
		keyIds.append(iterator.next());
	    }
	    
	}
	
	
	
	//查询得到这些key对应的采样数据,这方法返回的数据已经根据key值进行了累加
	Map<Integer,Integer> groupMap = detailDAO.queryStatic(tableSuffix, keyIds.toString(), 1, CalendarUtil.getStartTime(null, -1), CalendarUtil.getEndTime(null, -1));
	
	int sum = 0;
	
	//先算出总的采样记录数
	for (Iterator<Integer> iterator = groupMap.keySet().iterator(); iterator.hasNext();) {
		   
	    Integer key = (Integer) iterator.next(); 	    
	    sum = sum + groupMap.get(key);
	    
	}
	//再组装每个key的统计信息,插入数据库
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
