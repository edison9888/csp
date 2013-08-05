/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util;

import com.thoughtworks.selenium.Selenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-30 - 下午05:19:18
 * @version 1.0
 */
public class SeleniumUtils {
	
	/**
	 * 暂停时间单位ms
	 * 
	 * @param millisecs
	 * 2011-6-30 - 下午05:21:34
	 */
    public static void pause(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
        }
    }
    
    
    /**
     * 等待页面元素可用
     * 
     * @param selenium
     * @param locator
     * @param seconds
     * @return
     * 2011-6-30 - 下午05:21:52
     */
    public static boolean waitForElementEditable(Selenium selenium, String locator, int seconds) {
        boolean elementEditable = false;
        for (int second = 0;; second++) {
            if (second >= seconds)
                break;
            try {
                if (selenium.isEditable(locator)) {
                    elementEditable = true;
                    break;
                }
            } catch (Exception e) {
            }
            pause(1000);
        }
        return elementEditable;
    }
        
    /**
     * 等待页面元素加载完
     * 
     * @param selenium
     * @param locator
     * @param seconds
     * @return
     * 2011-6-30 - 下午05:22:27
     */
    public static boolean waitForElement(Selenium selenium, String locator, int seconds) {
        boolean elementExist = false;
        for (int second = 0;; second++) {
            if (second >= seconds)
                break;
            try {
                if (selenium.isElementPresent(locator)) {
                    elementExist = true;
                    break;
                }
            } catch (Exception e) {
            }
            pause(1000);
        }
        return elementExist;
    }
    
    /**
     * 等待Text加载完
     * 
     * @param selenium
     * @param tmplName
     * @param seconds
     * @return
     * 2011-6-30 - 下午05:22:48
     */
    public static boolean waitForText(Selenium selenium, String tmplName, int seconds) {
        boolean textExist = false;
        for (int second = 0;; second++) {
            if (second >= seconds)
                break;
            try {
                if (selenium.isTextPresent(tmplName)) {
                    textExist = true;
                    break;
                }
            } catch (Exception e) {
            }
            //暂停1秒.
            pause(1000);
        }
        return textExist;
    }

    /**
     * @param mis
     */
    public static void waitForPageToLoad(Selenium selenium, String mis){
        boolean isLoaded = false;
        int count = 0;
        do{
            if(count++>3)break;
            try{
                selenium.waitForPageToLoad(mis);
                isLoaded = true;
            }catch(Exception ex){
                continue;
            }
        }while(!isLoaded);
    }
    
}
