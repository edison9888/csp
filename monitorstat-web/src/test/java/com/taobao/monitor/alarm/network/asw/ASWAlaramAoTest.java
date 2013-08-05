package com.taobao.monitor.alarm.network.asw;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.monitor.alarm.network.lb.NetworkDeviceAlarmPo;

public class ASWAlaramAoTest {
	@Test
	public void test_getAswNameByVirtualIp(){
		String virtualIp = "172.24.179.110";
		String hostMachineName = ASWAlaramAo.get().getHostMachineNameByVirtualIp(virtualIp);
		Assert.assertEquals(hostMachineName, "x179010.cm4");
		
		String hostMachineIp = ASWAlaramAo.get().getHostMachineIpByHostMachineName(hostMachineName);
		Assert.assertEquals(hostMachineIp, "172.24.179.10");
		
		String aswName = ASWAlaramAo.get().getASWNameByHostMachineIp(hostMachineIp);
		Assert.assertEquals(aswName, "ASW-T9E-06.cm4");
	}

	@Test
	public void test_getAswAlarmByVirtualIp_并且验证map的内容(){
		String virtualIp = "172.24.179.110";
		Calendar c = Calendar.getInstance();
		Date endTime = c.getTime();
		c.add(Calendar.MINUTE, -5);
		Date startTime = c.getTime();
		
		List<NetworkDeviceAlarmPo>  list = ASWAlaramAo.get().getlbAlarmData(virtualIp, startTime, endTime);
		Assert.assertEquals(0, list.size());
		
		Assert.assertNotNull(ASWAlaramAo.get().getAswNameByVirtualIpFromMap(virtualIp));
		
	}
}
