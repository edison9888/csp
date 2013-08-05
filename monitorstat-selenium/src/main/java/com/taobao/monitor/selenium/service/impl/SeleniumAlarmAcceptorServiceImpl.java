/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taobao.monitor.selenium.dao.SeleniumAlarmAcceptorDao;
import com.taobao.monitor.selenium.dao.model.SeleniumAlarmAcceptor;
import com.taobao.monitor.selenium.service.SeleniumAlarmAcceptorService;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-7-12 - 下午08:33:11
 * @version 1.0
 */
@Service
public class SeleniumAlarmAcceptorServiceImpl implements
		SeleniumAlarmAcceptorService {

	@Resource(name="seleniumAlarmAcceptorDao")
	private SeleniumAlarmAcceptorDao seleniumAlarmAcceptorDao;
	
	/**
	 * @param acceptors
	 * 2011-7-12 - 下午08:33:11
	 */
	@Override
	public void addAlarmAcceptor(long[] useCaseIdArr ,String[] addressArray, 
			String type) {
		if(useCaseIdArr != null && useCaseIdArr.length > 0){
			for(long useCaseId:useCaseIdArr){
				if(addressArray != null && addressArray.length > 0){
					StringBuffer adrs = new StringBuffer();
					for(String address:addressArray){
						adrs.append("'"+address+"',");
					}
					//1.根据reportId先删除addressArray已经配置的报表接受信息
					seleniumAlarmAcceptorDao.deleteAlarmAcceptor(useCaseId, 
							adrs.toString().substring(0, adrs.toString().lastIndexOf(",")),
							type);
					//2.配置用户应用的报表接受信息

					List<SeleniumAlarmAcceptor> acceptors = new ArrayList<SeleniumAlarmAcceptor>();
					SeleniumAlarmAcceptor acceptor = null;
					for(String address:addressArray){
							acceptor = new SeleniumAlarmAcceptor();
							acceptor.setAddress(address);
							acceptor.setType(type);
							acceptor.setUseCaseId(useCaseId);
							acceptors.add(acceptor);
					}
					seleniumAlarmAcceptorDao.addAlarmAcceptor(acceptors);
				}
			}
		}	
	}

	/**
	 * @param useCaseId
	 * @return
	 * 2011-7-12 - 下午08:33:11
	 */
	@Override
	public List<SeleniumAlarmAcceptor> getAlarmAcceptorByUcId(int useCaseId) {
		return seleniumAlarmAcceptorDao.getAlarmAcceptorByUseCaseId(useCaseId);
	}

	/**
	 * @param address
	 * @return
	 * 2011-7-12 - 下午09:16:55
	 */
	@Override
	public String getUseCaseIdsByAlarmAcceptor(String addressList) {
		List<Long> list = null;
		if(addressList != null && !addressList.equals("")){
			String[] addressArray = addressList.split(",");
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			list = seleniumAlarmAcceptorDao.getUseCaseIdsByAlarmAcceptor(adrs.toString()
					.substring(0, adrs.toString().lastIndexOf(",")));
		}
		StringBuffer useCaseIds = new StringBuffer();
		if(list == null)return "";
		for(Long useCaseId : list){
			useCaseIds.append(useCaseId+",");
		}
		String reStr = useCaseIds.toString();
		if( reStr != null && !reStr.equals("")){
			reStr = reStr.substring(0, reStr.lastIndexOf(","));
		}
		return reStr;
	}

	/**
	 * @param addres
	 * 2011-7-12 - 下午09:39:17
	 */
	@Override
	public void deleteSeleniumAlarmAcceptor(String addressList) {
		String[] addressArray = null;
		if(addressList != null && !addressList.equals("")){
			addressArray = addressList.split(",");
			if(addressArray == null || addressArray.length == 0)
				addressArray = new String[]{};
		}
		
		if(addressArray != null && addressArray.length > 0){
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			seleniumAlarmAcceptorDao.deleteSeleniumAlarmAcceptor(adrs.toString()
					.substring(0, adrs.toString().lastIndexOf(",")));
		}
	}

	/**
	 * @return
	 * 2011-7-12 - 下午10:02:55
	 */
	@Override
	public List<String> getAllAlarmAcceptor(String type) {
		return seleniumAlarmAcceptorDao.getAllAlarmAcceptor(type);
	}

}
