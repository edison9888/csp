/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import com.thoughtworks.selenium.Selenium;

/**
 * 扩展selenium能够记录comment和写日志
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午06:52:38
 * @version 1.0
 */
public interface LogSelenium extends Selenium {
	
    /**
     * 扩展selenium能够提供日志的comments
     * 
     * @param comment 需要被格式换处理的comment
     */
    void logComment(final String comment);

    /**
     * 采取的一个屏幕截图，写路径到日志.
     *
     * 当发生错误异常时使用.
     *
     * @param baseName 
     *        中间部分产生文件名。可由LoggingResultsFormatter扩展（如时间戳）.
     */
    void logAutomaticScreenshot(final String baseName);

    /**
     * 记录一个断言.
     *
     * 需要的LoggingAssert类来包装junit4断言
     *
     * @param assertionName 抛出的断言
     * @param assertionMessage 抛出的断言信息
     * @param assertionCondition 导致断言的失败信息.
     */
    void logAssertion(final String assertionName, final String assertionMessage, final String 
    		assertionCondition);

    /**
     * 记录一个资源（二进制）文件。通常在错误的情况下增加
     * 屏幕快照或有关测试更详细的信息
     *
     * @param description 文字描述reource
     * @param pathFile 添加文件资源的完整路径
     */
    void logResource(final String description, final String pathFile);
}
