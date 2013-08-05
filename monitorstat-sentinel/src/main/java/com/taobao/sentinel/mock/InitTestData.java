package com.taobao.sentinel.mock;

import com.taobao.sentinel.bo.BlackListAppBo;
import com.taobao.sentinel.bo.BlackListCustomerBo;
import com.taobao.sentinel.bo.BlackListInterfaceBo;
import com.taobao.sentinel.bo.FlowControlAppBo;
import com.taobao.sentinel.bo.FlowControlDependencyBo;
import com.taobao.sentinel.bo.FlowControlInterfaceBo;
import com.taobao.sentinel.bo.WhiteListCustomerBo;
import com.taobao.sentinel.bo.WhiteListInterfaceBo;
import com.taobao.sentinel.util.SpringUtil;

public class InitTestData {
	
	public static String interfaceInfo = "method:L2//com.taobao.item.service.ItemQueryService:queryItemById(long,com.taobao.item.domain.query.QueryItemOptionsDO,com.taobao.item.domain.AppInfoDO)";
	
	public static String interfaceInfo1 = "method://com.taobao.item.dao.ItemDAO:queryForOne(long)";
	
	public static void initTestData() {
		
		insertWhiteInterface();
		insertBlackInterface();
		
		insertBlackApp();
		
		insertWhiteCustomer();
		insertBlackCustomer();
		
		insertFlowApp();
		insertFlowInterface();
		insertFlowDependency();
		
		
	}
	
	public static void insertWhiteInterface() {
		WhiteListInterfaceBo whiteListInterfaceBo = (WhiteListInterfaceBo)SpringUtil.getBean("whiteListInterfaceBo");
		
		whiteListInterfaceBo.add4Test("itemcenter", "detail", interfaceInfo);
		whiteListInterfaceBo.add4Test("itemcenter", "hesper", interfaceInfo);
		whiteListInterfaceBo.add4Test("itemcenter", "tradeplatform", interfaceInfo);
		whiteListInterfaceBo.add4Test("itemcenter", "tbskip", interfaceInfo);
		whiteListInterfaceBo.add4Test("itemcenter", "shopsystem", interfaceInfo);
		whiteListInterfaceBo.add4Test("itemcenter", "topmq", "F:doSomething");
		
		whiteListInterfaceBo.add4Test("hesper", "topmq", "test");
	}
	
	public static void insertBlackInterface() {
		BlackListInterfaceBo blackListInterfaceBo = (BlackListInterfaceBo)SpringUtil.getBean("blackListInterfaceBo");
		
		blackListInterfaceBo.add4Test("itemcenter", "detail", interfaceInfo);
		blackListInterfaceBo.add4Test("itemcenter", "hesper", interfaceInfo);
		blackListInterfaceBo.add4Test("itemcenter", "tradeplatform", interfaceInfo);
		blackListInterfaceBo.add4Test("itemcenter", "tbskip", interfaceInfo);
		blackListInterfaceBo.add4Test("itemcenter", "shopsystem", interfaceInfo);
		blackListInterfaceBo.add4Test("itemcenter", "topmq", "F:doSomething");
		
		blackListInterfaceBo.add4Test("hesper", "topmq", "test");
	}
	
	public static void insertBlackApp() {
		BlackListAppBo blackListAppBo = (BlackListAppBo)SpringUtil.getBean("blackListAppBo");
		
		blackListAppBo.add4Test("itemcenter", "detail");
		blackListAppBo.add4Test("itemcenter", "hesper");
		blackListAppBo.add4Test("itemcenter", "tradeplatform");
		blackListAppBo.add4Test("itemcenter", "tbskip");
		blackListAppBo.add4Test("itemcenter", "shopsystem");
		
		blackListAppBo.add4Test("hesper", "topmq");
	}
	
	public static void insertWhiteCustomer() {
		WhiteListCustomerBo whiteListCustomerBo = (WhiteListCustomerBo)SpringUtil.getBean("whiteListCustomerBo");
		
		whiteListCustomerBo.add4Test("itemcenter", "detail", "diy://white_x");
		whiteListCustomerBo.add4Test("itemcenter", "hesper", "diy://white_x");
		whiteListCustomerBo.add4Test("itemcenter", "tradeplatform", "diy://white_x");
		whiteListCustomerBo.add4Test("itemcenter", "tbskip", "diy://white_x");
		whiteListCustomerBo.add4Test("itemcenter", "shopsystem", "diy://white_x");
		
		whiteListCustomerBo.add4Test("hesper", "topmq", "test");
		
	}
	
	public static void insertBlackCustomer() {
		BlackListCustomerBo blackListCustomerBo = (BlackListCustomerBo)SpringUtil.getBean("blackListCustomerBo");
		
		blackListCustomerBo.add4Test("itemcenter", "detail", "diy://black_y");
		blackListCustomerBo.add4Test("itemcenter", "hesper", "diy://black_y");
		blackListCustomerBo.add4Test("itemcenter", "tradeplatform", "diy://black_y");
		blackListCustomerBo.add4Test("itemcenter", "tbskip", "diy://black_y");
		blackListCustomerBo.add4Test("itemcenter", "shopsystem", "diy://black_y");

		blackListCustomerBo.add4Test("hesper", "topmq", "test");
	}
	
	public static void insertFlowApp() {
		FlowControlAppBo flowControlAppBo = (FlowControlAppBo)SpringUtil.getBean("flowControlAppBo");
		
		flowControlAppBo.add4Test("itemcenter", "detail", 0);
		flowControlAppBo.add4Test("itemcenter", "hesper", 0);
		flowControlAppBo.add4Test("itemcenter", "tradeplatform", 0);
		flowControlAppBo.add4Test("itemcenter", "tbskip", 0);
		flowControlAppBo.add4Test("itemcenter", "shopsystem", 0);
		
		flowControlAppBo.add4Test("hesper", "topmq", 2);
	}
	
	public static void insertFlowInterface() {
		FlowControlInterfaceBo flowControlInterfaceBo = (FlowControlInterfaceBo)SpringUtil.getBean("flowControlInterfaceBo");
		flowControlInterfaceBo.add4Test("itemcenter", "detail", interfaceInfo, 0);
		flowControlInterfaceBo.add4Test("itemcenter", "hesper", interfaceInfo, 0);
		flowControlInterfaceBo.add4Test("itemcenter", "tradeplatform", interfaceInfo, 0);
		flowControlInterfaceBo.add4Test("itemcenter", "tbskip", interfaceInfo, 0);
		flowControlInterfaceBo.add4Test("itemcenter", "shopsystem", interfaceInfo, 0);
		
		flowControlInterfaceBo.add4Test("hesper", "topmq", "test", 2);
	}
	
	public static void insertFlowDependency() {
		FlowControlDependencyBo flowControlDependencyBo = (FlowControlDependencyBo)SpringUtil.getBean("flowControlDependencyBo");
		flowControlDependencyBo.add4Test("itemcenter", "detail", interfaceInfo1, 0);
		flowControlDependencyBo.add4Test("itemcenter", "hesper", interfaceInfo1, 0);
		flowControlDependencyBo.add4Test("itemcenter", "tradeplatform", interfaceInfo1, 0);
		flowControlDependencyBo.add4Test("itemcenter", "tbskip", interfaceInfo1, 0);
		flowControlDependencyBo.add4Test("itemcenter", "shopsystem", interfaceInfo1, 0);
		
		flowControlDependencyBo.add4Test("hesper", "topmq", "Test", 2);
	}

}
