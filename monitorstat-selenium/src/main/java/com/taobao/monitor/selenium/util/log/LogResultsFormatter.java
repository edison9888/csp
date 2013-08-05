/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

/**
 * �����ʽ����־��������д���ļ����������ݿ�...
 * ͬʱҲ��������־�洢�ı����ʽ
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-24 - ����04:01:58
 * @version 1.0
 */
public interface LogResultsFormatter {
    /**
     * ��ʽ������.
     * 
     * ��һ���µĲ��Է�������ӵ�test������ʱ��comments�ᱻlogCommandProcessor����
     * 
     * @param logBean logBean����Ҫ����¼����־.
     */
    void commentLogEvent(LogBean LogBean);

    /**
     * ��ʽ��selenium����ص���Ϣ�����ܳɹ�����ʧ��.
     * @param LogBean 
     * selenium������һ����������м�¼��Ϣ
     */
    void commandLogEvent(LogBean LogBean);

    /**
     * ��ʽ������boolean���ͽ����selenium command
     * 
     * ע�⣺����ʹ���سɹ�����ʧ������״̬��û��������Ϣ�ġ�
     * 
     * e.g. selenium commands like isElementPresent
     * @param LogBean LogBean����selenium���ڼ�¼һ�������������Ϣ
     */
    void booleanCommandLogEvent(LogBean LogBean);

    /**
     * Ϊ�Ѿ���ɵĲ��Է�����ɸ�ʽ����־
     * LogBean���ܰ���һϵ�г��е�һselenium command�ĺ���LogBean
     * @param LogBean �������еļ�¼���Է��������������Ϣ       
     */
    void methodLogEvent(LogBean LogBean);
    
    /**
     * ��ʽ��������Ի�����Ϣ
     * �������������Ϣ������commands���쳣��Ϣ
     * 
     * @param metricsBean �ڼ��ռ���ָ����Ϣ
     */
    void headerLogEvent(TestMetricsBean metricsBean);

    /**
     * ������formatHeader()������֮ǰ���е�command���Ѿ�����ʽ��.
     */
    void footerLogEvent();

    /**
     * ��ȡ�鿴��ͼ�����·��
     * ��ȡ��ͼ�ķ��ʵ�ַ����ʹ��HTTP�����Ƶ�ǰ׺�����·��������ʹ�õ�ǰ�˷���
     * @return ���·��
     */
    String getScreenShotBaseUri();

    /**
     * ���ý�ͼ�����ӵ����·�� ��picture/��
     * @param screenShotBaseUri ���·��
     */
    void setScreenShotBaseUri(String screenShotBaseUri);

    /**
     * ���������ļ���һ�����õľݶ�·�����������������£��糬ʱ�ȴ�����
     * @param baseName 
     * @return ������Ҫ��ȡ�ľ���·��
     */
    String generateFilenameForAutomaticScreenshot(String baseName);

    /**
     * ��ȡgenerateFilenameForAutomaticScreenshot()ʹ������������(filesystem-)λ��
     * ʹ�þ���·����D:\workspace\selenium-test\target\loggingResults\screenshots
     * @return current �ļ����·��
     */
    String getAutomaticScreenshotPath();

    /**
     * 
     * ����generateFilenameForAutomaticScreenshot()ʹ������������(filesystem-)λ��
     * ʹ�þ���·����D:\workspace\selenium-test\target\loggingResults\screenshots
     * generateFilenameForAutomaticScreenshot()
     * 
     * @param automaticScreenshotPath �ļ�����·��
     */
    void setAutomaticScreenshotPath(String automaticScreenshotPath);
}
