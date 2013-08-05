package com.taobao.csp.time.custom.arkclient.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptService {

	SecretKeySpec skeySpec;

	public static String Encrypt(String source, String key, String iv) {
		KeyGenerator kg = null;
		byte[] ptext;
		byte[] ctext = null;
		Byte[] bytIn;
		try {
			kg = KeyGenerator.getInstance("AES");// 获取密匙生成器
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(new BASE64Decoder().decodeBuffer(key));
			kg.init(128, secureRandom); // 初始化
			// DES算法必须是56位
			// DESede算法可以是112位或168位
			// AES算法可以是128、192、256位
			SecretKey skey = kg.generateKey(); // 生成密匙，可用多种方法来保存密匙

			AlgorithmParameterSpec paramSpec = new IvParameterSpec(

			new BASE64Decoder().decodeBuffer(iv));

			Cipher cp = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 创建密码器

			cp.init(Cipher.ENCRYPT_MODE, skey, paramSpec); // 初始化

			ptext = source.getBytes("UTF-8");

			ctext = cp.doFinal(ptext); // 加密

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Byte> list = new LinkedList<Byte>();
		for (byte byteIn : ctext)
			list.add(byteIn);

		Collections.reverse(list);

		bytIn = list.toArray(new Byte[0]);

		byte[] result = new byte[bytIn.length];

		for (int i = 0; i < bytIn.length; i++)
			result[i] = bytIn[i];

		String temp = new BASE64Encoder().encode(result);

		return filter(temp.replaceAll("\n|\r", ""));
	}

	public static String Decrypt(String source, String key, String iv) {
		Byte[] bytIn;
		byte[] bytes;
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES"); // 获取密匙生成器

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(new BASE64Decoder().decodeBuffer(key));

			kg.init(128, secureRandom); // 初始化
			// DES算法必须是56位
			// DESede算法可以是112位或168位
			// AES算法可以是128、192、256位
			SecretKey skey = kg.generateKey();

			bytes = new BASE64Decoder().decodeBuffer(source);

			AlgorithmParameterSpec paramSpec = new IvParameterSpec(

			new BASE64Decoder().decodeBuffer(iv));

			List<Byte> list = new LinkedList<Byte>();
			for (byte byteIn : bytes)
				list.add(byteIn);
			Collections.reverse(list);

			bytIn = list.toArray(new Byte[0]);

			byte[] result = new byte[bytIn.length];

			for (int i = 0; i < bytIn.length; i++)
				result[i] = bytIn[i];
			// 解密
			Cipher cp;
			cp = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 创建密码器
			cp.init(Cipher.DECRYPT_MODE, skey, paramSpec); // 初始化
			byte[] ptext = cp.doFinal(result); // 解密
			String str = new String(ptext, "UTF-8"); // 重新显示明文
			return str;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encode(byte[] a) {
		int totalBits = a.length * 8;
		int nn = totalBits % 6;
		int curPos = 0;// process bits
		StringBuffer toReturn = new StringBuffer();
		while (curPos < totalBits) {
			int bytePos = curPos / 8;
			switch (curPos % 8) {
			case 0:
				toReturn.append(codec_table[(a[bytePos] & 0xfc) >> 2]);
				break;
			case 2:

				toReturn.append(codec_table[(a[bytePos] & 0x3f)]);
				break;
			case 4:
				if (bytePos == a.length - 1) {
					toReturn.append(codec_table[((a[bytePos] & 0x0f) << 2) & 0x3f]);
				} else {
					int pos = (((a[bytePos] & 0x0f) << 2) | ((a[bytePos + 1] & 0xc0) >> 6)) & 0x3f;
					toReturn.append(codec_table[pos]);
				}
				break;
			case 6:
				if (bytePos == a.length - 1) {
					toReturn.append(codec_table[((a[bytePos] & 0x03) << 4) & 0x3f]);
				} else {
					int pos = (((a[bytePos] & 0x03) << 4) | ((a[bytePos + 1] & 0xf0) >> 4)) & 0x3f;
					toReturn.append(codec_table[pos]);
				}
				break;
			default:
				// never hanppen
				break;
			}
			curPos += 6;
		}
		if (nn == 2) {
			toReturn.append("==");
		} else if (nn == 4) {
			toReturn.append("=");
		}
		return toReturn.toString();

	}

	private static char[] codec_table = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };

	public static String filter(String str) {
		String output = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13)
				sb.append(str.subSequence(i, i + 1));
		}
		output = new String(sb);
		return output;
	}
}
