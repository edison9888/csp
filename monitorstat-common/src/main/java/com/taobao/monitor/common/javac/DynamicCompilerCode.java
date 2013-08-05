package com.taobao.monitor.common.javac;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicCompilerCode {

	private static DynamicCompilerCode dyn = new DynamicCompilerCode();

	public static DynamicCompilerCode get() {
		return dyn;
	}

	private File sourceDir = new File("script_dyna");

	private String default_package = "com.taobao.csp.adapter";

	private DynamicCompilerCode() {
		if (!sourceDir.exists()) {
			sourceDir.mkdirs();
		}
	}

	private Map<String, Class<?>> classMap = new ConcurrentHashMap<String, Class<?>>();
	
	public void clearCache(){
		classMap.clear();
	}

	public Class<?> getClassByCache(String code, String packageName) {
		String name = getMD5(code);
		if (name == null) {
			try {
				DynaCode dy = compiler(code, packageName);
				return dy.compileAndLoadClass();
			} catch (IOException e) {
				return null;
			}
		} else {
			Class<?> clazz = classMap.get(name);
			if (clazz == null) {
				try {
					DynaCode dy = compiler(code, packageName);
					clazz = dy.compileAndLoadClass();
					classMap.put(name, clazz);
				} catch (Exception e) {
					return null;
				}
			}
			return clazz;
		}

	}

	public String getMD5(String code) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(code.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public DynaCode compiler(String code) throws IOException {

		return compiler(code, default_package);
	}

	public DynaCode compiler(String code, String packageName) throws IOException {

		String className = getClassName(code);
		String tmpCode = alterPackage(code, packageName);

		File sourceFile = downLoadJavaCode(className, tmpCode);

		String classPath = extractClasspath();

		return new DynaCode(packageName + "." + className, sourceFile, classPath);
	}

	private File downLoadJavaCode(String className, String code) throws IOException {
		File file = new File(sourceDir, className + ".java");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(code);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

		return file;
	}

	private String extractClasspath() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		StringBuffer buf = new StringBuffer();

		while (cl != null) {
			if (cl instanceof URLClassLoader) {
				URL urls[] = ((URLClassLoader) cl).getURLs();

				for (int i = 0; i < urls.length; i++) {
					if (buf.length() > 0) {
						buf.append(File.pathSeparatorChar);
					}
					String s = urls[i].getFile();

					try {
						s = URLDecoder.decode(s, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						continue;
					}
					File f = new File(s);

					// f.isFile()
					buf.append(f.getAbsolutePath());
				}
			}
			cl = cl.getParent();
		}

		return buf.toString();
	}

	/**
	 * 从java 代码中获取 className
	 * 
	 * @param javaCode
	 * @return
	 */
	private String getClassName(String javaCode) {

		int index1 = javaCode.indexOf("class");
		int index2 = javaCode.indexOf("{");

		String className = javaCode.substring(index1 + 5, index2);

		String[] c = className.trim().split(" ");

		return c[0];
	}

	/**
	 * 替换java文件中的package 信息
	 * 
	 * @param javaCode
	 * @return
	 */
	private String alterPackage(String javaCode, String packageName) {

		Pattern pattern = Pattern.compile("package\\s+(\\w+\\.?)+;");
		Matcher m = pattern.matcher(javaCode);
		if (m.find()) {
			return m.replaceFirst("package " + packageName + ";");
		} else {
			javaCode = "package " + packageName + ";" + javaCode;
		}
		return javaCode;
	}

}
