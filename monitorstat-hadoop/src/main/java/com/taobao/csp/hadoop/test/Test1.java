package com.taobao.csp.hadoop.test;

import java.io.InputStream;
import java.net.URLEncoder;

import com.taobao.csp.hadoop.biz.UrlPatternMatcher;
import com.taobao.csp.hadoop.biz.UrlPatternMatcherImpl;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		UrlPatternMatcher urlMatcher = new UrlPatternMatcherImpl().fromProperties("urlPattern");
		
		
		
		String url = urlMatcher.getMatchedUrl("http://store.taobao.com/shop/view_shop.htm");
		
		System.out.println(url);
		
		System.out.println(URLEncoder.encode("http://[\\S]+/view_shop.htm"));

	}

}
