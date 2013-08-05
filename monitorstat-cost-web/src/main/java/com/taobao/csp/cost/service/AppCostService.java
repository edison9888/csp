package com.taobao.csp.cost.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.taobao.csp.cost.dao.CostDayDaoNew;
import com.taobao.csp.cost.dao.CspAppDependDao;
import com.taobao.csp.cost.dao.CspDependencyDao;
import com.taobao.csp.cost.dao.CspHsfDependDao;
import com.taobao.csp.cost.dao.CspTairDependDao;
import com.taobao.csp.cost.dao.CspTddlDependDao;
import com.taobao.csp.cost.dao.CspWebDependDao;
import com.taobao.csp.cost.po.AppCostPo;
import com.taobao.csp.cost.po.CostAppTotal;
import com.taobao.csp.cost.po.CostAppType;
import com.taobao.csp.cost.po.SimpleCostPo;
import com.taobao.csp.cost.po.SimpleHostInfo;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.CustomDateUtil;
import com.taobao.monitor.common.util.DateUtil;

/**
 * Ӧ������ҵ����
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-3
 */
public class AppCostService {
	private static Logger logger = Logger.getLogger(AppCostService.class);
	

	@Resource(name = "dependencyDao")
	private CspDependencyDao dependencyDao;
	@Resource(name = "cspHsfDependDao")	
	private CspHsfDependDao cspHsfDependDao;	//hsf����
	@Resource(name = "cspTairDependDao")
	private CspTairDependDao cspTairDependDao;	//tair����
	@Resource(name = "cspWebDependDao")
	private CspWebDependDao cspWebDependDao;	//web uv pv ����
	@Resource(name = "cspAppDependDao")
	private CspAppDependDao cspAppDependDao;	//congif_server,diamond
	@Resource(name = "cspTddlDependDao")
	private CspTddlDependDao cspTddlDependDao;	//tddl
	@Resource(name = "hostCostService")
	private HostCostService hostCostService;	//����

	@Resource(name = "costDayDaoNew")
	private CostDayDaoNew costDayDaoNew;
	@Resource(name = "appCostService")
	private AppCostService appCostService;
	
	//��Ʒ���·�ƽ���ɱ�����
	static Cache<String, List<CostAppTotal>> groupCosts = CacheBuilder.newBuilder().
			expireAfterWrite(1, TimeUnit.HOURS).maximumSize(5000).build();
	
	//��Ʒ����
	static Cache<String, List<CostAppTotal>> appCosts = CacheBuilder.newBuilder().
			expireAfterWrite(1, TimeUnit.HOURS).maximumSize(5000).build();
	
	public List<CostAppTotal> getPerTopCost(Date startTime,Date endTime){
		
		List<CostAppTotal> inner_costs=costDayDaoNew.getAvgMonthCost("ALL", null, 
				startTime, endTime);
				
		Collections.sort(inner_costs, new Comparator<CostAppTotal>() {
			//����
			@Override
			public int compare(CostAppTotal o1, CostAppTotal o2) {
				double mid=o1.getPrePreCost()-o2.getPrePreCost();
				if(mid>0)
					return -1;
				else if(mid<0)
					return 1;
				else 
					return 0;
			}
		});
		if(inner_costs.size()>10){
			return inner_costs.subList(0, 10);
		}
			
		return inner_costs;
	}
	/**
	 * ȡ��appĳ��ʱ���ڵĳɱ�
	 * 
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @param isForce
	 * @return
	 */
	public List<CostAppTotal> getAppTotalCost(String name,String dateStr,
			Date startTime,Date endTime,String isForce){
		
		List<CostAppTotal> costs=appCosts.getIfPresent(name+dateStr);
		if(costs==null || StringUtils.isNotBlank(isForce)){
			List<CostAppTotal>  lcatp=costDayDaoNew.getAppCost(name, startTime, endTime);
			
			appCosts.put(name+dateStr, lcatp);
			costs=lcatp;
		}
		return costs;
	}
	
	public void removeTopCostCache(){
		groupCosts.invalidateAll();
		appCosts.invalidateAll();
	}
	
	/**
	 * ��ȡĳ��ʱ����ڵĳɱ�����
	 * 
	 * @param level  GROUP_COUNT ���� LINE_COUNT
	 * @param name   ����Ƕ�����Ʒ�ߣ���Ҫָ��һ��������
	 * @param date	 ʱ��
	 * @return
	 */
	public List<CostAppTotal> getTopCost(String level,String name,
			Date startTime,Date endTime,String isForce){

		Calendar cd=Calendar.getInstance();
		cd.setTime(startTime);
		int month=cd.get(Calendar.MONTH);
		
		//�����·��Ƿ��Ѿ�����
		String monthCacheKey=level+name+"-"+month;
		
		List<CostAppTotal> costs=groupCosts.getIfPresent(monthCacheKey);
		
		if(costs==null || StringUtils.isNotBlank(isForce)){
		
			List<CostAppTotal> inner_costs=costDayDaoNew.getAvgMonthCost(level, name, 
					startTime, endTime);
			
			Collections.sort(inner_costs, new Comparator<CostAppTotal>() {
				//����
				@Override
				public int compare(CostAppTotal o1, CostAppTotal o2) {
					double mid=o1.getPreCostAll()-o2.getPreCostAll();
					if(mid>0)
						return -1;
					else if(mid<0)
						return 1;
					else 
						return 0;
				}
				
			});
			groupCosts.put(monthCacheKey, inner_costs);
			return inner_costs;
		}
		return costs;
	}
	

	private static final String DATE_YMDHMSFORMAT = "MM-dd"; 
	private static final ThreadLocal<SimpleDateFormat> dateMDSFormat = 
			new ThreadLocal<SimpleDateFormat>() {
	        @Override
	        protected SimpleDateFormat initialValue() {
	            return new SimpleDateFormat(DATE_YMDHMSFORMAT);
	        }
	    };
	    
	
	
	/**
	 * ���أ�ĳ��Ӧ�õ����гɱ���Ϣ
	 * 
	 * 
	 * @param appName
	 * @return
	 */
	public AppCostPo getAppSummary(String appName,Date date){
		AppCostPo acp=new AppCostPo();
		
		Map<String,List<SimpleCostPo>> costList=new HashMap<String, List<SimpleCostPo>>();
		Date sdate=CustomDateUtil.getStartDay(date);
		Date edate=CustomDateUtil.getEndDay(date);
		
		List<CostAppType> tairApps=
				costDayDaoNew.getTypeAppCostBy(appName, CostType.TAIR.name(), sdate, edate);
		List<CostAppType> dbApps=
				costDayDaoNew.getTypeAppCostBy(appName, CostType.DB.name(), sdate, edate);
		List<CostAppType> hsfApps=
				costDayDaoNew.getTypeAppCostBy(appName, CostType.HSF_PV.name(), sdate, edate);
		List<CostAppType> hostApps=
				costDayDaoNew.getTypeAppCostBy(appName, CostType.HOST.name(), sdate, edate);
		//app�Ļ��������ɱ���host+DB+tair��
		List<CostAppType> appDirectAll=
			costDayDaoNew.getTypeAppCostBy(appName, CostType.DIRECT_ALL.name(), sdate, edate);
		if(appDirectAll!=null && appDirectAll.size()>0){
			acp.setDirectAll(appDirectAll.get(0).getDependCost());
		}
		//��������A��Ӧ���б�
		List<CostAppType> cost_cat=costDayDaoNew.getTypeAppCostBy(appName,sdate, edate);
		acp.setCostCat(cost_cat);
		
		Collections.sort(cost_cat, new Comparator<CostAppType>() {
			//����
			@Override
			public int compare(CostAppType o1, CostAppType o2) {
				double mid=o1.getDependCost()-o2.getDependCost();
				if(mid>0)
					return -1;
				else if(mid<0)
					return 1;
				else 
					return 0;
			}
		});
		
		//List<SimpleCostPo> appTairNum= cspTairDependDao.getAppTairGSummary(appName,date);
		//List<SimpleCostPo>  appDbNum= cspTddlDependDao.getAppDbSummary(appName,date);
		//List<SimpleCostPo> dc=cspHsfDependDao.getAppHsfSummary(appName,date);
		
		List<SimpleCostPo> appTairNum=convertAppToSimple(tairApps);
		List<SimpleCostPo>  appDbNum= convertAppToSimple(dbApps);
		List<SimpleCostPo> dc=convertAppToSimple(hsfApps);
		
		acp.setAppName(appName);
		
		acp.setHsfCost(dc);
		costList.put(CostType.TAIR.toString(), appTairNum);
		costList.put(CostType.DB.toString(), appDbNum);
		acp.setBaseDependList(costList);
		
		//����hsf���ܳɱ�
		double hsfTotal=0;
		for(SimpleCostPo dcp:dc){
			hsfTotal+=dcp.getConsumerCost();
		}
		//�������������ÿ���ֵ���ɣ�tair/db
		double basrDependTotal=0;
		Map<String,Double> baseCostMap=new HashMap<String, Double>();
		for(Map.Entry<String, List<SimpleCostPo>> bdps:acp.getBaseDependList().entrySet()){
			for(SimpleCostPo scp:bdps.getValue()){
				//tair/dby�������ܳɱ�
				basrDependTotal+=scp.getConsumerCost();
				//
				double addCost=scp.getConsumerCost();
				//db��tair�ֱ���ܺͣ�������ʾ��ͼ
				if(baseCostMap.containsKey(bdps.getKey())){
					double nowCost=baseCostMap.get(bdps.getKey());
					baseCostMap.put(bdps.getKey(), nowCost+addCost);
				}else{
					baseCostMap.put(bdps.getKey(), addCost);
				}
			}
		}
		
		//��hsf
		acp.setTotalHsfCost(Double.parseDouble(
				new DecimalFormat("#.#").format(hsfTotal)));
		//����������������tair/tddl��
		acp.setTotalBaseDependCost(Double.parseDouble(
				new DecimalFormat("#.#").format(basrDependTotal)));
		acp.setBaseDependCostTotal(baseCostMap);
		
		//�����ܳɱ�===========================================================
		//���ĳ��tairӦ�õ��ܳɱ�
		double hostAllTotal=0;
		//double hostAllTotal=hostCostService.getAppTotalCost(appName);
		//List<HostPo> hosts=hostInfos.getHostInfoListByOpsName(appName);
		
		//ÿ�ֻ��͵ĳɱ�
		List<SimpleHostInfo> hostCosts=new ArrayList<SimpleHostInfo>();
		
		for(CostAppType catp:hostApps){
			SimpleHostInfo simpleInfo=new SimpleHostInfo();
			String[] hs=catp.getCostName().split(",");//����;�Ƿ������
			
			simpleInfo.setHostType(hs[1]);
			simpleInfo.setMacName(hs[0]);
			simpleInfo.setTotalPrice(catp.getDependCost());
			simpleInfo.setHostNum(Integer.valueOf(String.valueOf(catp.getCallNum())));
			hostCosts.add(simpleInfo);
			
			hostAllTotal=hostAllTotal+catp.getDependCost();
		}
		acp.setHostInfos(hostCosts);
		acp.setTotalHostCost(Double.parseDouble(
				new DecimalFormat("#.#").format(hostAllTotal)));
		
		//ȫ��========================================================
		double allTotal=hsfTotal+basrDependTotal+hostAllTotal;
		acp.setTotalAllCost(Double.parseDouble(
				new DecimalFormat("#.#").format(allTotal)));
		
		//�����ͼ=====================================================
		caculatePercent(acp);
		
		//�ɱ�����
		Date now=new Date();
		Date eMonthdate=CustomDateUtil.getStartDay(now);
		
		Calendar cd=Calendar.getInstance();
		cd.setTime(now);
		cd.set(Calendar.MONTH, cd.get(Calendar.MONTH)-2);
		
		Date sMonthdate=CustomDateUtil.getStartDay(cd.getTime());
		
		List<CostAppTotal> rss=appCostService.getAppTotalCost(appName,"Trend",
				sMonthdate, eMonthdate, null);
		JSONArray ja=new JSONArray();
		JSONArray jaDate=new JSONArray();
		
		Collections.sort(rss, new Comparator<CostAppTotal>() {
			//����
			@Override
			public int compare(CostAppTotal o1, CostAppTotal o2) {
				double mid=o1.getsTime().getTime()-o2.getsTime().getTime();
				if(mid>0)
					return 1;
				else if(mid<0)
					return -1;
				else 
					return 0;
			}
			
		});
		int i=0;
		for(CostAppTotal cat:rss){
			if(cat.getCostAll()>0 && i%2==0){
				ja.add(cat.getCostAll());
				jaDate.add(dateMDSFormat.get().format(cat.getsTime()));
			}
		}
		acp.setCostTrend(ja.toString());
		acp.setCostTrendDay(jaDate.toString());
		
		return acp;
		
	}
	
	private List<SimpleCostPo> convertAppToSimple(List<CostAppType> apps){
		List<SimpleCostPo> scps=new ArrayList<SimpleCostPo>();
		if(apps!=null){
			for(CostAppType catp:apps){
				SimpleCostPo scp=new SimpleCostPo(catp.getCostType());
				scp.setCallNum(catp.getCallNum());
				scp.setConsumerCost(catp.getDependCost());
				scp.setDependName(catp.getCostName());
				scps.add(scp);
			}
		}
		return scps;
	}
	
	private void caculatePercent(AppCostPo acp){
		double hsfTotal=acp.getTotalHsfCost();
		double basrDependTotal=acp.getTotalBaseDependCost();
		double hostAllTotal=acp.getTotalHostCost();
		double allTotal=acp.getTotalAllCost();
		if(allTotal==0){
			return;
		}
		JSONArray jas=new JSONArray();
		JSONObject job=new JSONObject();
		
		//����
		JSONObject inner_job=new JSONObject();
		JSONArray inner_Array1=new JSONArray();
		JSONArray inner_Array2=new JSONArray();
		job=new JSONObject();
		
		job.put("name", "�����ɱ�");
		job.put("y", Double.parseDouble(new DecimalFormat("#.##").format(hostAllTotal/allTotal*100)));
		
		int i=0;
		for(SimpleHostInfo shi:acp.getHostInfos()){
			inner_Array1.add(i, shi.getMacName());
			inner_Array2.add(i,  Double.parseDouble(
					new DecimalFormat("#.##").format(shi.getTotalPrice()/allTotal*100)));
			i++;
		}
		inner_job.put("categories", inner_Array1);
		inner_job.put("data", inner_Array2);
		job.put("drilldown", inner_job);
		jas.add(job);
		
		//HSF
		if(hsfTotal>0){
			inner_job=new JSONObject();
			inner_Array1=new JSONArray();
			inner_Array2=new JSONArray();
			job=new JSONObject();
			
			job.put("name", "HSF�����ɱ�");
			job.put("y", Double.parseDouble(
					new DecimalFormat("#.##").format(hsfTotal/allTotal*100)));
			
			i=0;
			for(SimpleCostPo dcp:acp.getHsfCost()){
				inner_Array1.add(i, dcp.getDependName());
				inner_Array2.add(i,  Double.parseDouble(
						new DecimalFormat("#.##").format(dcp.getConsumerCost()/allTotal*100)));
				i++;
			}
			inner_job.put("categories", inner_Array1);
			inner_job.put("data", inner_Array2);
			job.put("drilldown", inner_job);
			jas.add(job);
		}
		
		
		if(basrDependTotal>0){
			//����
			i=0;
			inner_job=new JSONObject();
			inner_Array1=new JSONArray();
			inner_Array2=new JSONArray();
			job=new JSONObject();
			job.put("name", "���������ɱ�");
			job.put("y", Double.parseDouble(
					new DecimalFormat("#.##").format(basrDependTotal/allTotal*100)));
			
			//����tair�Լ�db
			for(Map.Entry<String, Double> dcp:acp.getBaseDependCostTotal().entrySet()){
				inner_Array1.add(i, dcp.getKey());
				inner_Array2.add(i,  Double.parseDouble(
						new DecimalFormat("#.##").format(dcp.getValue()/allTotal*100)));
				i++;
			}
			inner_job.put("categories", inner_Array1);
			inner_job.put("data", inner_Array2);
			job.put("drilldown", inner_job);
			jas.add(job);
		}
		
		acp.setJsonChart(jas.toString());
	}
	
	public static void main(String[] args){

		String aa="[{\"name\":\"�����ɱ�\",\"y\":\"75.99\",\"drilldown\":{\"categories\":[\"xen xen-domU\","+
		"\"unknown\",\"Tecal XH320\",\"ProLiant DL170e G6\"],\"data\":[\"48.26\",\"22.66\",\"1.33\",\"3.73\"]}}]";
		
		
		JSONArray jsa=JSONArray.fromObject(aa);
		System.out.println(jsa);
	}
}
