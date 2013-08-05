package com.taobao.arkclient.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class IdEncryptService {

	public static String Encrypt(String source) throws UnsupportedEncodingException {
		Byte[] bytIn;
		byte[] bytes = source.getBytes("UTF-8");
		bytIn = new Byte[bytes.length];
		for (int i = 0; i < bytes.length; i++)
			bytIn[i] = bytes[i];

		List<Byte> list = new LinkedList<Byte>();
		for (byte byteIn : bytIn)
			list.add(byteIn);
		Collections.reverse(list);
		bytIn = list.toArray(new Byte[0]);

		byte[] result = new byte[bytIn.length];

		for (int i = 0; i < bytIn.length; i++)
			result[i] = bytes[i];

		return EncryptService.filter(new BASE64Encoder().encode(result));
	}

	public static String Decrypt(String source) throws UnsupportedEncodingException {
		Byte[] bytIn;
		byte[] bytes;
		try {

			bytes = new BASE64Decoder().decodeBuffer(source);
		} catch (IOException e) {
			return null;
		}
		List<Byte> list = new LinkedList<Byte>();
		for (byte byteIn : bytes)
			list.add(byteIn);
		Collections.reverse(list);
		bytIn = list.toArray(new Byte[0]);
		byte[] result = new byte[bytIn.length];
		for (int i = 0; i < bytIn.length; i++)
			result[i] = bytes[i];
		return new String(result, "UTF-8");
	}

}
