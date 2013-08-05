/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import org.hamcrest.Matcher;

/**
 * junit方法封装.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-25 - 下午03:45:32
 * @version 1.0
 */
public final class LogAssert {

    private LogAssert() {
        // static class only
    }

    /**
     * 
     * 
     * @param message
     * @param condition
     * @param selenium
     * 2011-5-25 - 下午06:13:33
     */
    public static void assertTrue(java.lang.String message, boolean condition, LogSelenium selenium) {
        try {
            org.junit.Assert.assertTrue(message, condition);
        } catch (AssertionError e) {
            selenium.logAssertion("assertTrue", message, "condition=" + condition);
            throw e;
        }
    }

    /**
     * 
     * 
     * @param message
     * @param condition
     * @param selenium
     * 2011-5-25 - 下午06:13:41
     */
    public static void assertFalse(java.lang.String message, boolean condition, LogSelenium selenium) {
        try {
            org.junit.Assert.assertFalse(message, condition);
        } catch (AssertionError e) {
            selenium.logAssertion("assertFalse", message, "condition=" + condition);
            throw e;
        }
    }

    /**
     * 
     * 
     * @param message
     * @param expected
     * @param actual
     * @param selenium
     * 2011-5-25 - 下午06:13:58
     */
    public static void assertEquals(java.lang.String message, Object expected, Object actual, LogSelenium selenium) {
        try {
            org.junit.Assert.assertEquals(message, expected, actual);
        } catch (AssertionError e) {
            selenium.logAssertion("assertEquals", message, "expected=" + expected + " actual=" + actual);
            throw e;
        }
    }

    /**
     * 
     * 
     * @param <T>
     * @param message
     * @param actual
     * @param matcher
     * @param selenium
     * 2011-5-25 - 下午06:14:02
     */
    public static <T> void assertThat(java.lang.String message, T actual, Matcher<T> matcher, LogSelenium selenium) {
        try {
            org.junit.Assert.assertThat(message, actual, matcher);
        } catch (AssertionError e) {
            selenium.logAssertion("assertThat", message, e.getMessage());
            throw e;
        }
    }

}
