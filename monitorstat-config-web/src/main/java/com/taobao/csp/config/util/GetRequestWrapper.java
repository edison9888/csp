package com.taobao.csp.config.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @Auther shigangxing@He Nan Province
 * 
 * */
public class GetRequestWrapper extends HttpServletRequestWrapper {

	public GetRequestWrapper(HttpServletRequest request,
			String requestCharacterEncoding, String uriEncoding) {
		super(request);
		this.reqEnc = requestCharacterEncoding;
		this.uriEncoding = uriEncoding;

	}

	@Override
	public Map getParameterMap() {
		log.debug("getClass(): " + getClass() + "\t方法名："
				+ Thread.currentThread().getStackTrace()[1].getMethodName());

		// 不能在原Map上改，因为已经上锁
		Map dest = new HashMap();
		Map map = super.getParameterMap();

		for (Object item : map.entrySet()) {
			Entry entry = (Entry) item;
			String key = (String) entry.getKey();

			if (entry.getValue() != null) {

				String[] vs = ((String[]) entry.getValue());
				String[] values = Arrays.copyOf(vs, vs.length);

				// 处理“单个参数名，对应多个参数值”的情形
				for (int i = 0; i < values.length; i++)
					try {
						values[i] = new String(values[i].getBytes(uriEncoding),
								reqEnc);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				dest.put(key, values);
			}

		}
		return dest;
	}

	@Override
	public String getParameter(String name) {
		log.debug("getClass(): " + getClass() + "\t方法名："
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}

		try {
			value = new String(value.getBytes(uriEncoding),
					reqEnc);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return value;

	}

	@Override
	public String[] getParameterValues(String name) {
		log.debug(getClass()+ ": getParameterValues()");
		
		String[] vs = super.getParameterValues(name);
		
		if(vs ==null)
				return vs;
		String[] values = Arrays.copyOf(vs, vs.length);

		// 处理“单个参数名，对应多个参数值”的情形
		for (int i = 0; i < values.length; i++) {
			try {
				values[i] = new String(values[i].getBytes(uriEncoding),
						reqEnc);
				log.debug("values["+i+"]: "+values[i]);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

		return values;
	}

	private String uriEncoding;
	private String reqEnc;
	private static final Logger log = LoggerFactory.getLogger(GetRequestWrapper.class);

}
