/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

/**
 * ������ص�����.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-24 - ����05:52:48
 * @version 1.0
 */
public interface LogNotifier {
	
    /**
     * �ص�������֪��������Ȼ����Ӷ���ҵ����
     * @param instance ����Ӧ�ó����ʵ��
     * @param commandName selenium command name
     * @param args selenium command����
     * @param result selenium���ؽ��
     * @param exception
     * @param cmdStartMillis Start time
     * @return True (Ĭ��): ����
     *         False: ������
     */
    Boolean errorLogging(Object instance, String commandName, String[] args, String result, Throwable exception,
            long cmdStartMillis);

    /**
     * �ص�������֪���������ǣ���������
     * @param instance ����Ӧ�ó����ʵ��
     * @param pathFile Full path and file to the resource
     * @return True (Ĭ��): ����
     *         False: ������
     */
    Boolean makeScreenshot(Object instance, String pathFile);
}
