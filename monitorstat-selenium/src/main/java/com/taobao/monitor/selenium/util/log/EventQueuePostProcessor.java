/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.service.UseCaseCache;
import com.taobao.monitor.selenium.util.SeleniumConstant;
import com.taobao.monitor.selenium.util.Sequence;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-24 - ����06:10:09
 * @version 1.0
 */
public class EventQueuePostProcessor {
    private LogResultsFormatter formatter;

    boolean logMethodsAsComments = true;

    /**
     * ����logEventsQueue�¼������˺��ʽ���������
     *
     * @param outputFormatter ��ʽ����־��ʵ�ֽӿ�
     * @param loggingEventsQueue ��¼�����¼�(�ȴ��¼����������¼)
     * @param seleniumTestMetrics ����ָ��ͻ�����Ϣ
     */
    public EventQueuePostProcessor(LogResultsFormatter outputFormatter, List<LogBean> loggingEventsQueue,
            TestMetricsBean seleniumTestMetrics, boolean logMethodsAsComments) {
        this.formatter = outputFormatter;
        this.logMethodsAsComments = logMethodsAsComments;
        formatAllGatheredInformations(loggingEventsQueue, seleniumTestMetrics);
    }

    /**
     * ��ʽ��command���ж��Ƿ�������һ�����ۣ��в������������ֻ��һ�����������
     * 
     * @param LogBean ��¼command��ϸ��Ϣ
     */
    void formatAndOutputCommand(LogBean LogBean) {

        if (SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_COMMENT.getName().equals(LogBean.getCommandName())) {
            formatter.commentLogEvent(LogBean);
            return;
        }

        boolean srcResult = false;
        String result = LogBean.getResult();

        // FIXME: these if's should be done inside LogBean.
        if (result.startsWith(LogCommandProcessor.SELENIUM_RC_OK_RESULT)) {
            if (result.endsWith(LogCommandProcessor.SELENIUM_CORE_BOOLEAN_RESULT_TRUE)
                    || result.endsWith(LogCommandProcessor.SELENIUM_CORE_BOOLEAN_RESULT_FALSE)) {
                LogBean.setCommandSuccessful(result.endsWith(LogCommandProcessor.SELENIUM_CORE_BOOLEAN_RESULT_TRUE));
                formatter.booleanCommandLogEvent(LogBean);
                return;
            } else {
                srcResult = true;
            }
        }
        LogBean.setCommandSuccessful(srcResult);
        formatter.commandLogEvent(LogBean);
    }

    /**
     * Added text prefix if we are inside a wait loop.
     */
    private static final String WAIT_PREFIX = "(Wait)";

    /**
     * ����loggingEventsQueue�͸�ʽ�����
     *
     * @param loggingEventsQueue ��¼�����¼�(�ȴ��¼����������¼)
     * @param seleniumTestMetrics ����ָ��ͻ�����Ϣ
     */
    public void formatAllGatheredInformations(List<LogBean> loggingEventsQueue, TestMetricsBean seleniumTestMetrics) {
    	markUcPrimaryKey(loggingEventsQueue, seleniumTestMetrics);
    	formatter.headerLogEvent(seleniumTestMetrics);
        boolean insideWait = false;
        long startWaitMillis = 0;
        LogBean lastWaitLoggingEvent = null;
        String currentMethodName = "";
        LogBean rootLogBean = new LogBean();
        boolean completeResult = true;
        for (LogBean currentLoggingEvent : loggingEventsQueue) {
            if (currentLoggingEvent.isWaitInvolved()) {
                lastWaitLoggingEvent = currentLoggingEvent;
                if (!insideWait) {
                    insideWait = true;
                    startWaitMillis = currentLoggingEvent.getCmdStartMillis();
                } // else do nothing as we are inside a wait
            } else {
                if (insideWait) {
                    insideWait = false;
                    lastWaitLoggingEvent.setWaitDeltaMillis(currentLoggingEvent.getCmdEndMillis() - startWaitMillis);
                    startWaitMillis = 0;
                    // lastWaitLoggingEvent is really the last wait command. So output it to the formatter now
                    String s = lastWaitLoggingEvent.getCommandName();
                    if (!s.startsWith(WAIT_PREFIX)) {
                        lastWaitLoggingEvent.setCommandName(WAIT_PREFIX + s);
                    }
                    formatAndOutputCommand(lastWaitLoggingEvent);
                }
                if (!currentLoggingEvent.getSourceMethod().equals(currentMethodName)) {
                    if (StringUtils.isNotEmpty(currentMethodName)) {
                        postProcessMethod(completeResult, rootLogBean, currentMethodName);
                        completeResult = true;
                    }

                    currentMethodName = currentLoggingEvent.getSourceMethod();
                    rootLogBean = new LogBean();
                    rootLogBean.setCallingClass(currentLoggingEvent.getCallingClass());
                    logNewMethodEntered(currentMethodName, rootLogBean);
                }
                formatAndOutputCommand(currentLoggingEvent);
                rootLogBean.addChild(currentLoggingEvent);
            }
            completeResult = completeResult && currentLoggingEvent.isCommandSuccessful();
        }

        postProcessMethod(completeResult, rootLogBean, currentMethodName);
        formatter.footerLogEvent();
    }

    /**
     * ��¼ִ�в��Ժ��ÿһ������
     * 
     * @param completeMethodResult ���Է����Ľ��������ɹ�����true
     * @param currentLogBean �������Է�������ϸ����
     * @param currentMethod ��ǰִ�еĲ��Է���
     */
    void postProcessMethod(boolean completeMethodResult, LogBean currentLogBean, String currentMethod) {
        currentLogBean.setCommandSuccessful(completeMethodResult);
        currentLogBean.setArgs(new String[] {"executing " + currentLogBean.getCallingClass() + "::" + currentMethod});
        formatter.methodLogEvent(currentLogBean);
    }

    /**
     * ��ִ��һ��selenium command��Ϊһ��commentʱ�Ż����
     * @param currentMethodName ��ǰ�ķ���
     * @param LogBean comment LogBean
     */
    void logNewMethodEntered(final String currentMethodName, LogBean LogBean) {
        if (logMethodsAsComments) {
            LogBean.setCommandName(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_COMMENT.getName());
            LogBean.setArgs(new String[] {"executing " + currentMethodName + "()"});
            formatAndOutputCommand(LogBean);
        }
    }
    
    /**
     * �������key
     * 
     * @param loggingEventsQueue
     * @param seleniumTestMetrics
     * 2011-6-9 - ����10:36:58
     */
    private void markUcPrimaryKey(List<LogBean> loggingEventsQueue, 
			TestMetricsBean seleniumTestMetrics){
    	//�ϲ���ͬ��������Ϣ���������������Ƿ�ɹ�
    	Map<String, String> errorMap = new HashMap<String, String>();
        //����key
    	long usecaseResultId = Long.valueOf(new Date().getTime()+""+Sequence.next());
    	seleniumTestMetrics.setUsecaseResultId(usecaseResultId);
    	//����UC������Ϣ
    	UseCase uc = null;
    	for (LogBean currentLoggingEvent : loggingEventsQueue) {
    		String[] args = currentLoggingEvent.getArgs();
    		if(args != null && args.length == 1){
    			String arg = args[0];
    			if(arg.startsWith(SeleniumConstant.SELENIUM_UC_NAME)){//��ȡUC_name
    				int index = arg.indexOf("=")+1;
    				String ucName = arg.substring(index);
    				ucName = ucName.substring(ucName.indexOf(".")+1,
    						ucName.indexOf(":"));
    				int rcId = Integer.valueOf(arg
    						.substring(arg.indexOf(":")+1));
    				uc = UseCaseCache.getUcByName(ucName);
    				if(uc != null)uc.setRcId(rcId);
    				break;
    			}
    		}
    	}
    	seleniumTestMetrics.setUseCase(uc);
    	//��ֵ
    	Date currentDate = new Date();
    	seleniumTestMetrics.setUsecaseId(uc.getUseCaseId());
    	for (LogBean currentLoggingEvent : loggingEventsQueue) {
    		String seleniumCore = currentLoggingEvent.getResult();
    		if(seleniumCore != null && !seleniumCore.equalsIgnoreCase("")){
    			errorMap.put(currentLoggingEvent.getCallingClass(), 
    					seleniumCore);
    		}
    		currentLoggingEvent.setGmtCreate(currentDate);
    		currentLoggingEvent.setUsecaseId(uc.getUseCaseId());
    		currentLoggingEvent.setUsecaseResultId(usecaseResultId);
    	}
    	//ͳ�ƴ�������
    	int finallyState = 0;
    	for(Map.Entry<String, String> error:errorMap.entrySet()){
    		String seCoreResult = error.getValue(); 
    		if(seCoreResult.equalsIgnoreCase("OK,false") ||
    				seCoreResult.startsWith("ERROR")){
    			finallyState = 1;
    			break;
    		}
    	}
    	seleniumTestMetrics.setFinallyState(finallyState);
    }
}
