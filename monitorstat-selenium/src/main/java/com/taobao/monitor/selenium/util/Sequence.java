/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 自增型ID获取.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午09:07:25
 * @version 1.0
 */
public final class Sequence {
	
	private final static AtomicLong sequenceNumber = new AtomicLong(0);
	
	/**
	 * 获取序列ID
	 * @return
	 * 2011-5-26 - 下午09:09:21
	 */
	public static long next() {
		synchronized (sequenceNumber) {
			return sequenceNumber.getAndIncrement();
		}
    }
}
