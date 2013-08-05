/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

/**
 * 类似与回调函数.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午05:52:48
 * @version 1.0
 */
public interface LogNotifier {
	
    /**
     * 回调函数告知发生错误，然后添加额外业务处理
     * @param instance 调用应用程序的实例
     * @param commandName selenium command name
     * @param args selenium command参数
     * @param result selenium返回结果
     * @param exception
     * @param cmdStartMillis Start time
     * @return True (默认): 截屏
     *         False: 不截屏
     */
    Boolean errorLogging(Object instance, String commandName, String[] args, String result, Throwable exception,
            long cmdStartMillis);

    /**
     * 回调函数告知发生错误是，截屏处理
     * @param instance 调用应用程序的实例
     * @param pathFile Full path and file to the resource
     * @return True (默认): 截屏
     *         False: 不截屏
     */
    Boolean makeScreenshot(Object instance, String pathFile);
}
