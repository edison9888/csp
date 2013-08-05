package com.taobao.csp.hadoop.test;

import com.taobao.csp.hadoop.biz.UrlPatternMatcher;
import com.taobao.csp.hadoop.biz.UrlPatternMatcherImpl;

public class Test {

	public static void main(String[] args) {

		UrlPatternMatcher urlMatcher = new UrlPatternMatcherImpl()
				.fromProperties("urlPattern");
		
		System.out.println(urlMatcher.getMatchedUrl("http://list.taobao.com/itemlist/watch.htm"));
		
//		String aaString = "list.taobao.com/([\\w]+)/[\\S]+.htm";
//		
//		System.out.println(aaString.indexOf("/"));
	}

}
