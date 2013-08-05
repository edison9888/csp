/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;


/**
 * Utility Methods to support searching in StackStraces related to Selenium and the logging extension
 * 实用工具方法支持查找和selenium有关的StackStraces和log扩展的commands.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午06:06:46
 * @version 1.0
 */
public final class StackTraceUtils {
    /**
     * for the string representation of calling code.
     */
    public static final String LINE_NUMBER_SEPARATOR = "#";

    private StackTraceUtils() {
       
    }

    /**
     * Debugs on stderr the current stack-trace.
     */
    public static void debugStackTrace() {
        StackTraceElement[] testElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : testElements) {
            System.err.println(stackTraceElementWithLinenumberAsString(stackTraceElement));
        }
    }

    /**
     * 
     * 
     * @param stackTraceElement
     * @return
     * 2011-5-26 - 下午06:12:11
     */
    public static String stackTraceElementWithLinenumberAsString(StackTraceElement stackTraceElement) {
        if (null != stackTraceElement) {
            return stackTraceElement.getClassName() + LINE_NUMBER_SEPARATOR + stackTraceElement.getLineNumber();
        } else {
            return "Internal ERROR stackTraceElement should not be null";
        }
    }

    /**
     * 
     * 
     * @param stackTraceElement
     * @param wantedClassName
     * @return
     * 2011-5-26 - 下午06:12:18
     */
    public static boolean isClassName(StackTraceElement stackTraceElement, String wantedClassName) {
        String className = stackTraceElement.getClassName();
        return className.contains(wantedClassName);
    }

    /**
     * 
     * 
     * @param testElements
     * @param preceedingClassName
     * @return
     * 2011-5-26 - 下午06:12:26
     */
    public static StackTraceElement getCurrentCallingClassAsStackTraceElement(StackTraceElement[] testElements,
            String preceedingClassName) {
        boolean found = false;
        StackTraceElement currentCallingClassAsStackTraceElement = null;
        for (StackTraceElement stackTraceElement : testElements) {
            if (found) {
                currentCallingClassAsStackTraceElement = stackTraceElement;
                break;
            }
            if (StackTraceUtils.isClassName(stackTraceElement, preceedingClassName)) {
                found = true;
                currentCallingClassAsStackTraceElement = stackTraceElement;
            }
        }
        return currentCallingClassAsStackTraceElement;
    }

    /**
     * 
     * 
     * @param testElements
     * @param className
     * @return
     * 2011-5-26 - 下午06:11:39
     */
    public static boolean isClassInStackTrace(StackTraceElement[] testElements, String className) {
        boolean result = false;
        for (StackTraceElement stackTraceElement : testElements) {
            if (stackTraceElement.getClassName().endsWith(className)) {
                result = true;
            }
        }
        return result;
    }
}
