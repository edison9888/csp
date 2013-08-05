package com.taobao.arkclient.Service;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.taobao.arkclient.ssl.ReadHttpsURL;

public class RequestService {

	private final static String LOGINURL = "%1$s/Login.aspx?app=%2$s&redirectURL=%3$s";
	private final static String PROFILEURL = "%1$s/GetUserProfile.ashx?ticket=%2$s";
	private final static String SECRETURL = "%1$s/GetSecretKey.ashx?secretid=%2$s";
	private final static String SECRETNEWURL = SECRETURL + "&expired=%3$s";
	private final static String ERRORURL = "%1$s/Error.aspx?id=%2$s";

	static HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			return true;
		}
	};

	public static String RequestUrl(String url, Cookie cookie) {
		try {
			trustAllHttpsCertificates();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		try {
			if (cookie != null) {
				URI uri = new URI(url);
				CookieManager cookiemanager = new CookieManager();
				HttpCookie httpcookie = new HttpCookie(cookie.getName(), cookie.getValue());
				httpcookie.setDomain(cookie.getDomain());
				httpcookie.setMaxAge(cookie.getMaxAge());
				httpcookie.setSecure(cookie.getSecure());
				cookiemanager.getCookieStore().add(uri, httpcookie);
			}
			return ReadHttpsURL.invoke(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static void trustAllHttpsCertificates() throws Exception {

		//  Create a trust manager that does not validate certificate chains:  

		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];

		javax.net.ssl.TrustManager tm = new miTM();

		trustAllCerts[0] = tm;

		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

		sc.init(null, trustAllCerts, null);

		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	}

	public static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	public static Cookie ConvertCookie(Cookie value) {
		Cookie cookie = new Cookie(value.getName(), value.getValue());
		cookie.setPath(value.getPath());
		cookie.setDomain(value.getDomain());
		cookie.setMaxAge(value.getMaxAge());
		cookie.setSecure(value.getSecure());
		return cookie;
	}

	@SuppressWarnings("deprecation")
	public static String ProcessLoginUrl(HttpServletRequest request, HttpServletResponse response) {
		//重定向到登录
		String app = "";
		if (StringUtils.isBlank(request.getContextPath())) {
			app = String.format("%1$s://%2$s%3$s", request.getScheme(), request.getServerName(),
					request.getContextPath() + "/");
		} else {
			app = String.format("%1$s://%2$s%3$s", request.getScheme(), request.getServerName(),
					request.getContextPath());

		}
		String port = request.getLocalPort()+"";
		if(ConfigManager.getInstance().getValue("redirectPort")!=null){
			port = ConfigManager.getInstance().getValue("redirectPort");
		}
		String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + port
				+ request.getRequestURI();

		//原来没有带上参数导致请求的页面如果有参数就有错误了 已修复 wyc 9.25
		@SuppressWarnings("rawtypes")
		Enumeration enu = request.getParameterNames();
		int count = 0;
		while (enu.hasMoreElements()) {
			String par = (String) enu.nextElement();
			String val = request.getParameter(par);
			if (count == 0) {
				redirectUrl = redirectUrl + "?" + par + "=" + val;
			} else {
				redirectUrl = redirectUrl + "&" + par + "=" + val;
			}
			count++;
		}

		if (redirectUrl.endsWith("/") || redirectUrl.endsWith("\\"))
			redirectUrl = redirectUrl.substring(0, redirectUrl.length() - 1);
		return String.format(LOGINURL, ArkAuthenService.getArkServer(), URLEncoder.encode(app),
				URLEncoder.encode(redirectUrl));
	}

	public static String ProcessProfileUrl(String ticket) {

		return String.format(PROFILEURL, ArkAuthenService.getArkServer(), ticket);
	}

	public static String ProcessSecretUrl(String secretId) {
		return String.format(SECRETURL, ArkAuthenService.getArkServer(), secretId);
	}

	public static String ProcessSecretUrl(String secretId, boolean isexpired) {
		return String.format(SECRETNEWURL, ArkAuthenService.getArkServer(), secretId, isexpired);
	}

	public static String ProcessErrorUrl(int errorcode) {
		return String.format(ERRORURL, ArkAuthenService.getArkServer(), errorcode);
	}
}
