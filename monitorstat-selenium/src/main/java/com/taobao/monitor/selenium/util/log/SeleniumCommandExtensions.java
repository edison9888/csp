/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

/**
 * 包括所有的除了realcommandProcessor自身外的其他命令.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午05:42:02
 * @version 1.0
 */
public enum SeleniumCommandExtensions {
    COMMAND_EXTENSION_LOG_COMMENT("X-1and1-logComment"),
    COMMAND_EXTENSION_LOG_AUTO_SCREENSHOT("X-1and1-logAutoScreenshot"),
    COMMAND_EXTENSION_LOG_ASSERTION("X-1and1-logAssertion"),
    COMMAND_EXTENSION_LOG_RESOURCE("X-1and1-logResource");

    private String name;

    private SeleniumCommandExtensions(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }
}
