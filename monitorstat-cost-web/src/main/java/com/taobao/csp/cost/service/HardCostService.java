package com.taobao.csp.cost.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.csp.cost.dao.CspTairDependDao;
import com.taobao.csp.cost.dao.CspTddlDependDao;
import com.taobao.csp.cost.dao.HostHardDao;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.OpsFreeUtil;

/**
 * ��������Ļ����ɱ�
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-11-6
 */
public class HardCostService {

	private static Log logger = LogFactory.getLog(HardCostService.class);
	static String hardStr="7500;4;176.7;";
	static String tairHardStr="24103;16;823.4;";
	static String dbHardStr="65536;16;1169;";
	@Resource
	private HostHardDao hostHardDao;
	@Resource(name = "cspTairDependDao")
	private CspTairDependDao cspTairDependDao;
	@Resource(name = "cspTddlDependDao")
	private CspTddlDependDao cspTddlDependDao;

	/**
	 * ����hostName��ȡ�����ɱ����л���
	 * 
	 * @param hostName
	 * @return	Ӳ����Ϣ�ͳɱ�
	 */
	public List getBaseCostByHostName(String hostName){
		String hardIo=hostHardDao.getHostHardInfo(hostName);
		List al=new ArrayList();
		al.add(0, hardIo);
		al.add(1, getCostByHardInfo(hardIo));
		return al;
	}
	
	/**
	 * ���һ�ѻ�����Ӳ���ɱ�
	 * 
	 * @param ips
	 * @return
	 */
	public double getBaseCostByIpList(Set<String> ips,String reqType){
		double ic=0;
		if(ips==null || ips.size()<1){
			return ic;
		}
		for(String ip:ips){
			if(ip.split(":").length>1){
				ip=ip.split(":")[0];
			}
			String hardIo=hostHardDao.getHostHardInfoByIp(ip);
			if(StringUtils.isBlank(hardIo) || 
					hardIo.split(";").length!=3){
				logger.warn("get host info error,use default��"+hardIo+","+ip);
				if(reqType.equals(CostType.TAIR.name())){
					hardIo=tairHardStr;
				}else if(reqType.equals(CostType.DB.name())){
					hardIo=dbHardStr;
				}else{
					hardIo=hardStr;
				}
			}
			ic=ic+getCostByHardInfo(hardIo);
		}
		return Double.parseDouble(new DecimalFormat("#.#").format(ic));
	}
	
	/**
	 * ����Ӳ����Ϣ����ɱ�
	 * @param hardInfo
	 * 
	 * @return	���������λ
	 */
	private static double getCostByHardInfo(String hardInfo){
		if(StringUtils.isBlank(hardInfo) || 
				hardInfo.split(";").length!=3){
			hardInfo=hardStr;
		}
		String[] hardInfos=hardInfo.split(";");
		double mem=0;
		double cpu=0;
		double disk=0;
		
		try{
			mem=Double.parseDouble(hardInfos[0]);
			cpu=Double.parseDouble(hardInfos[1]);
			disk=Double.parseDouble(hardInfos[2]);
		}catch(Exception e){
			mem=7500;//7g
			cpu=5;	//5core
			disk=120;//120G
		}
		//���Ӳ����Ϣ
		checkHardInfo(hardInfo,hardInfos);
		double hardTotal=(mem/1024)*120+cpu*50+(disk/10)*5;
		double otherCost=hardTotal*0.07;
		
		double result=Double.parseDouble(new DecimalFormat("#.#").format(hardTotal+otherCost));
		return result;
	}

	/**
	 * ���û��Ӳ����Ϣ�Ļ���
	 * 
	 * @param as
	 * @param hs
	 * @param isDeleteAll
	 * @return
	 */
	public Set<String> getNoHardHosts(String[] as,String[] hs,boolean isDeleteAll,
			Date date,String type) {
		//
		if(isDeleteAll){
			hostHardDao.deleteHostHardInfo(null, null);
		}else{
			if(as!=null || hs!=null){
				hostHardDao.deleteHostHardInfo(as, hs);
			}
		}
		
		if(StringUtils.isNotBlank(type) && type.equals("tair")){
			Set<String> rs=new HashSet<String>();
			
			Map<String, Set<String>> tairMachines = cspTairDependDao.getTairMachines(date);
			for(Map.Entry<String, Set<String>> me:tairMachines.entrySet()){
				for(String ip:me.getValue()){
					if(ip.split(":").length>1){
						ip=ip.split(":")[0];
					}
					if(hostHardDao.getHostHardInfoByIp(ip)==null){

						rs.add(me.getKey()+","+ip);
					}
				}
			}
			return rs;
		}else{
			Set<String> rs=hostHardDao.findNoHardInfoHost();
			return rs;
		}
		
//		Calendar cal=Calendar.getInstance();
//		cal.setTime(date);
//		int tableId=cal.get(Calendar.DAY_OF_YEAR);
//		
//		Map<String, Set<String>> dbMachines = cspTddlDependDao.getDbMachines(tableId,date);
//		for(Map.Entry<String, Set<String>> me:dbMachines.entrySet()){
//			for(String ip:me.getValue()){
//				if(ip.split(":").length>1){
//					ip=ip.split(":")[0];
//				}
//				if(hostHardDao.getHostHardInfoByIp(ip)==null){
//					rs.add(me.getKey()+","+ip);
//				}
//			}
//		}
	}
	
	/**
	 * ����Ӳ���ɱ�
	 * 
	 * @param infos
	 * @return
	 */
	public String insertHardHosts(List<String> infos) {
		if(infos==null || infos.size()==0){
			return "����Ϊ��";
		}
		StringBuilder sbR=new StringBuilder();
		
		String[] hs=null;
		for(String ss:infos){
			hs=ss.split(",");
			if(hs.length!=4){
				sbR.append(ss).append("|��ʽ����").append("<br/>");
			}else{
				if(ss.contains("Authentication failed")){
					sbR.append(ss).append("|��֤ʧ��").append("<br/>");
				}else if(ss.contains("timed out")){

					sbR.append(ss).append("|timed out �����ǻ����Ѿ�����").append("<br/>");
				}else{
					String[] hards=hs[3].split(";");

					String returnStr=checkHardInfo(ss,hards);
					if(!returnStr.equals("")){
						sbR.append(returnStr).append("<br/>");
					}
				}
				
				if(!hostHardDao.insertHostHardInfo(hs)){
					sbR.append(ss).append("|�������ݿ��쳣").append("<br/>");
				}
			}
		}
		return sbR.toString();
	}
	
	/**
	 * ����DB�ĳɱ���Ϣ
	 * @param tableId
	 * @return
	 */
	public String insertDbCost(int tableId){
		StringBuilder sbR=new StringBuilder();
		
		
		Map<String, Set<String>> tairMachines = cspTddlDependDao.getDbMachines(tableId);
		for(Map.Entry<String, Set<String>> me:tairMachines.entrySet()){
			for(String ip:me.getValue()){
				if(ip.split(":").length>1){
					ip=ip.split(":")[0];
				}
				//�Ѿ���Ӳ����Ϣ�����ü���
				if(hostHardDao.getHostHardInfoByIp(ip)!=null){
					continue;
				}
				
				sbR.append(me.getKey()).append(",").append(ip);
				//���ip��Ӧ��hostname
				String hname=OpsFreeUtil.getHostNameFromIp(ip);
				if(StringUtils.isNotBlank(hname)){
					hname=hname.replace(".tbsite.net", "");
					
					//���Ӳ����Ϣ
					int[] dbHardInfo=OpsFreeUtil.getHardInfoFromArmory(hname);
					if(dbHardInfo==null){
						sbR.append("��Ӳ����Ϣ��aromy��ȡʧ�ܣ��쳣").append("<br/>");
						continue;
					}
					String hInfo=dbHardInfo[0]+";"+dbHardInfo[1]+";"+dbHardInfo[2];
					
					sbR.append(hname).append(",").append(hname);
					if(!hostHardDao.insertHostHardInfo(new String[]{me.getKey(),hname,ip,hInfo})){
						sbR.append("|�������ݿ��쳣").append("<br/>");
					}else{
						sbR.append("|�������ݿ�ɹ�").append("<br/>");
					}
				}else{
					sbR.append("��hostname��ȡʧ�ܣ��쳣").append("<br/>");
				}
			}
		}
		
		return sbR.toString();
	}
	
	private static String checkHardInfo(String fullInfo,String[] hardInfos){
		double mem=0;
		double cpu=0;
		double disk=0;
		StringBuilder sb=new StringBuilder();
		
		try{
			mem=Double.parseDouble(hardInfos[0]);
			cpu=Double.parseDouble(hardInfos[1]);
			disk=Double.parseDouble(hardInfos[2]);
		}catch(Exception e){
			logger.warn("hard parse double exception,"+fullInfo,e);
		}
		if(mem<=0){
			sb.append("�ڴ�Ϊ0:").append(fullInfo);
		}
		if(cpu<=0){
			sb.append("CPUΪ0:").append(fullInfo);
		}
		if(disk<=0){
			sb.append("Ӳ��Ϊ0:").append(fullInfo);
		}
		if(!sb.toString().equals("")){
			logger.warn("hard parse double exception,"+sb.toString());
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(getCostByHardInfo("7500;5;80.4;")+
				getCostByHardInfo("7500;5;108.4;")+2*getCostByHardInfo("3072;16;36.0;")+
				getCostByHardInfo("3072;16;165.0;"));
		System.out.println(3*getCostByHardInfo("48384;16;856.4;"));
	}
}
