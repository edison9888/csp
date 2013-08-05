package com.taobao.monitor.common;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.monitor.common.ao.center.AppInfoAo;

public class AppInfoAoTest {
	@Test
	public void test_case1(){
		Collection<Integer> jmxAppIdList =  AppInfoAo.get().getJmxAppMap().values();
		Assert.assertEquals(2, jmxAppIdList.size());
		System.out.println(jmxAppIdList);
	}
	

}
