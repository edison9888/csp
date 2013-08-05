package com.taobao.www.arkclient.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CodeEncryptService {
	public static String Encrypt(String source) {
		//加密
		byte[] bytIn = null;
		try {
			bytIn = source.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		char[] chars = EncryptService.filter(new BASE64Encoder().encode(bytIn)).toCharArray();
		char[] result = new char[chars.length];

		for (int i = 0; i < chars.length; i++) {
			result[chars.length - i - 1] = chars[i];
		}
		return new String(result, 0, result.length);
	}

	public static String Decrypt(String source) throws UnsupportedEncodingException {
		//解密
		char[] chars = source.toCharArray();
		char[] result = new char[chars.length];
		for (int i = 0; i < chars.length; i++) {
			result[chars.length - i - 1] = chars[i];
		}
		byte[] bytIn = null;
		try {
			bytIn = new BASE64Decoder().decodeBuffer(new String(result, 0, result.length));
		} catch (IOException e) {
			return null;
		}
		return new String(bytIn, "UTF-8");
	}
}