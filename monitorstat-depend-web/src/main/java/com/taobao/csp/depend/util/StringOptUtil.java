package com.taobao.csp.depend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhongting.zy
 *
 */
public class StringOptUtil {
	public static String combineSeveralString(String... args) {
		if(args == null)
			return null;
		String[] array = array_unique(args);
		StringBuilder sb = new StringBuilder();
		for(String tmp : array) {
			if(StringUtils.isNotBlank(tmp))
				sb.append(tmp).append(",");
		}
		if(sb.length() > 0)
			sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	//去除数组中重复的记录
	public static String[] array_unique(String[] a) {
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(a));
		return set.toArray(new String[0]);
	}

	
	public static void main(String[] args) {
		String ss = StringOptUtil.combineSeveralString("123,31222","dddggg","123,31222");
		System.out.println(ss);
	}
}
