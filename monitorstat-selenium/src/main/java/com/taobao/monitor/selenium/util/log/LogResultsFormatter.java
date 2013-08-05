/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

/**
 * 负责格式化日志处理：可以写进文件、保存数据库...
 * 同时也负责处理日志存储的编码格式
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午04:01:58
 * @version 1.0
 */
public interface LogResultsFormatter {
    /**
     * 格式化评论.
     * 
     * 当一个新的测试方法被添加到test用例中时，comments会被logCommandProcessor发布
     * 
     * @param logBean logBean包含要被记录的日志.
     */
    void commentLogEvent(LogBean LogBean);

    /**
     * 格式化selenium命令返回的信息，不管成功或者失败.
     * @param LogBean 
     * selenium：包含一个命令的所有记录信息
     */
    void commandLogEvent(LogBean LogBean);

    /**
     * 格式化返回boolean类型结果的selenium command
     * 
     * 注意：不能使返回成功或者失败这种状态是没有其他信息的。
     * 
     * e.g. selenium commands like isElementPresent
     * @param LogBean LogBean含有selenium用于记录一个命令的所有信息
     */
    void booleanCommandLogEvent(LogBean LogBean);

    /**
     * 为已经完成的测试方法完成格式化日志
     * LogBean可能包含一系列持有单一selenium command的孩子LogBean
     * @param LogBean 包含所有的记录测试方法及其命令的信息       
     */
    void methodLogEvent(LogBean LogBean);
    
    /**
     * 格式化处理测试汇总信息
     * 包含：浏览器信息，测试commands，异常信息
     * 
     * @param metricsBean 期间收集的指标信息
     */
    void headerLogEvent(TestMetricsBean metricsBean);

    /**
     * 类似于formatHeader()，但是之前所有的command都已经被格式化.
     */
    void footerLogEvent();

    /**
     * 获取查看截图的相对路径
     * 获取截图的访问地址：可使用HTTP或类似的前缀活相对路径更容易使用的前端访问
     * @return 相对路径
     */
    String getScreenShotBaseUri();

    /**
     * 设置截图了连接的相对路径 如picture/。
     * @param screenShotBaseUri 相对路径
     */
    void setScreenShotBaseUri(String screenShotBaseUri);

    /**
     * 截屏生成文件到一个设置的据对路径当发生错误的情况下（如超时等待）。
     * @param baseName 
     * @return 截屏将要采取的绝对路径
     */
    String generateFilenameForAutomaticScreenshot(String baseName);

    /**
     * 获取generateFilenameForAutomaticScreenshot()使用来保截屏到(filesystem-)位置
     * 使用绝对路径如D:\workspace\selenium-test\target\loggingResults\screenshots
     * @return current 文件存放路径
     */
    String getAutomaticScreenshotPath();

    /**
     * 
     * 设置generateFilenameForAutomaticScreenshot()使用来保截屏到(filesystem-)位置
     * 使用绝对路径如D:\workspace\selenium-test\target\loggingResults\screenshots
     * generateFilenameForAutomaticScreenshot()
     * 
     * @param automaticScreenshotPath 文件保存路径
     */
    void setAutomaticScreenshotPath(String automaticScreenshotPath);
}
