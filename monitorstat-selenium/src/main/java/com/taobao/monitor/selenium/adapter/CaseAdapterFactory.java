/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.adapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.monitor.selenium.adapter.impl.DefaultCaseAdapter;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.service.SeleniumUseCaseService;
import com.taobao.monitor.selenium.util.DynaCode;

/**
 * 测试用例动态生成类.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午12:55:03
 * @version 1.0
 */
public class CaseAdapterFactory{
	private static final Logger logger = Logger.getLogger(CaseAdapterFactory.class);
	private static String casePath = "dyna_case";
	private static File pluginDir=new File(casePath+"/adapter");
	private static DynaCode dynacode=null;
	private static Map<String,Long> fileVersionMap=new HashMap<String, Long>();
	
	static{
		//4.通过dynacode.newProxyInstance获取动态对象.
		dynacode = new DynaCode();
		dynacode.addSourceDir(new File(casePath));
	}

	/**
	 * 获取UC适配器动态代理类
	 * @param adapterName
	 * @param adapterTime
	 * @param pluginTextService
	 * @param rcId
	 * @return
	 * 2011-5-26 - 下午03:46:44
	 */
	synchronized public static CaseAdapter generate(String adapterName, long adapterTime, 
			SeleniumUseCaseService useCaseService, int rcId) {
		
		Class c;
		Object classObj = null;
		CaseAdapter caseAdapter = null;
		try {
			if (!StringUtils.isBlank(adapterName)&&adapterTime!=0) {				
				//0.判断文件目录是否存在.
				//File pluginDir=new File(casePath+"/adapter");
				if(!pluginDir.isDirectory()){
					logger.info(pluginDir+" not exists,create it.");
					pluginDir.mkdirs();
				}				
				//1.判断适配器是否存在,如果不存在就直接到第3步.
				File adapter=new File(pluginDir,adapterName+".java");
				if(adapter.isFile()){
					Long lastModify=fileVersionMap.get(adapterName);
					if(lastModify==null){
						lastModify=adapter.lastModified();
						fileVersionMap.put(adapterName,lastModify);
					}
					//2.判断适配器源文件的lastModifyTime是否等于adapterTime					
					if(lastModify!=adapterTime){
						logger.warn(adapter+" have new version,update..,version="+adapterTime);
						try {
							adapter = downloadNewFile(adapterName, adapterTime, useCaseService);
							fileVersionMap.put(adapterName,adapterTime);
						} catch (Exception e) {
							logger.error("download new Adapter Error,continue use old version",e);
						}
						
					}
//					else{
//						logger.info(adapter+" no new version.version="+adapterTime);
//					}
				}
				else{
					logger.info(adapter+" local not exist,download from master.");
					//3.获取新的源文件.
					adapter = downloadNewFile(adapterName, adapterTime , useCaseService);
					fileVersionMap.put(adapterName,adapterTime);
				}
				//4.通过dynacode.newProxyInstance获取动态对象.
				caseAdapter=(CaseAdapter)dynacode.newProxyInstance(CaseAdapter.class,
						"adapter."+adapterName, rcId);
			}
		} catch (Exception e) {
			logger.error("Create Analyse Adapter Error!!!", e);
			throw new RuntimeException(e);
		}
		if (caseAdapter == null) {
			caseAdapter = new DefaultCaseAdapter();
		}

		return caseAdapter;
	}
	
	/**
	 * 获取最新的java文件
	 * 
	 * @param adapterName
	 * @param adapterTime
	 * @param useCaseService
	 * @return
	 * @throws IOException
	 * 2011-05-26 - 上午11:41:11
	 */
	private static File downloadNewFile(String adapterName, long adapterTime, 
			SeleniumUseCaseService useCaseService) throws IOException{
		
		UseCase useCaseText= useCaseService.getUseCaseText(adapterName, adapterTime);
		//写入java文件
		File adapter=new File(pluginDir,adapterName+".java");
		FileWriter fw=new FileWriter(adapter);
		if(useCaseText==null){
			logger.error(adapter+" master not find this file.");
			throw new NullPointerException(adapter+" master not find this file.");
		}
		if(useCaseText.getUseCaseSource()==null){
			logger.error(adapter+" content is null");
			throw new NullPointerException("["+adapter+"] content is null.");
		}
		
		try {
			fw.write(useCaseText.getUseCaseSource());
		} 
		finally{
			if(fw!=null)
				fw.close();
		}
		adapter.setLastModified(adapterTime);
		
		return adapter;
	}
	
	public static void main(String[] args) {
		String s="D:/workspace/coremonitor/monitorstat-selenium/dyna_case/adapter/SearchAdapter.java";
		File f=new File(s);
		if(!f.isFile())
			throw new NullPointerException();
		CaseAdapter logAdapter = CaseAdapterFactory.generate("SearchAdapter",f.lastModified(),null,0);
		try {
			logAdapter.execute(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}