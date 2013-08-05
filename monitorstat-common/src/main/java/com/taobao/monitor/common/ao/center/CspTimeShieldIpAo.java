package com.taobao.monitor.common.ao.center;

import java.util.List;

import com.taobao.monitor.common.db.impl.center.CspTimeShieldIpDao;

public class CspTimeShieldIpAo {
	private static CspTimeShieldIpAo cspTimeShieldAo = new CspTimeShieldIpAo();
	private CspTimeShieldIpDao cspTimeShieldDao = new CspTimeShieldIpDao();
	private CspTimeShieldIpAo(){
	}
	public static CspTimeShieldIpAo get(){
		return cspTimeShieldAo;
	}
	public List<String> findAllIps(){
		return cspTimeShieldDao.findAllIps();
	}
	public boolean addIps(List<String> ips){
		return cspTimeShieldDao.addIps(ips);
	}
	public boolean  deleteIps(List<String> ips){
		return cspTimeShieldDao.deleteIps(ips);
	}
	public static void main(String args[]){
		List<String> ips = CspTimeShieldIpAo.get().findAllIps();
		ips.add("test2");
		CspTimeShieldIpAo.get().addIps(ips);
		System.out.println(ips);
		CspTimeShieldIpAo.get().deleteIps(ips);
		ips = CspTimeShieldIpAo.get().findAllIps();
		System.out.println(ips);
		System.exit(0);
	}
}
