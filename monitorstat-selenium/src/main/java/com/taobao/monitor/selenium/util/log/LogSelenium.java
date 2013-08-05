/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import com.thoughtworks.selenium.Selenium;

/**
 * ��չselenium�ܹ���¼comment��д��־
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-24 - ����06:52:38
 * @version 1.0
 */
public interface LogSelenium extends Selenium {
	
    /**
     * ��չselenium�ܹ��ṩ��־��comments
     * 
     * @param comment ��Ҫ����ʽ�������comment
     */
    void logComment(final String comment);

    /**
     * ��ȡ��һ����Ļ��ͼ��д·������־.
     *
     * �����������쳣ʱʹ��.
     *
     * @param baseName 
     *        �м䲿�ֲ����ļ���������LoggingResultsFormatter��չ����ʱ�����.
     */
    void logAutomaticScreenshot(final String baseName);

    /**
     * ��¼һ������.
     *
     * ��Ҫ��LoggingAssert������װjunit4����
     *
     * @param assertionName �׳��Ķ���
     * @param assertionMessage �׳��Ķ�����Ϣ
     * @param assertionCondition ���¶��Ե�ʧ����Ϣ.
     */
    void logAssertion(final String assertionName, final String assertionMessage, final String 
    		assertionCondition);

    /**
     * ��¼һ����Դ�������ƣ��ļ���ͨ���ڴ�������������
     * ��Ļ���ջ��йز��Ը���ϸ����Ϣ
     *
     * @param description ��������reource
     * @param pathFile ����ļ���Դ������·��
     */
    void logResource(final String description, final String pathFile);
}
