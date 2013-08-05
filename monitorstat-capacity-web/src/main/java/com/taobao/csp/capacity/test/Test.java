package com.taobao.csp.capacity.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String md5 = DigestUtils.md5Hex("taobao_daily"+sdf.format(new Date()));
		
		System.out.println(md5);

	}

}
