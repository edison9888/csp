package com.taobao.www.arkclient.ssl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.taobao.www.arkclient.Service.ConfigManager;

public class ReadHttpsURL {

	static final int HTTPS_PORT = 443;

	private static ConfigManager configManager = ConfigManager.getInstance();

	public static String invoke(String url) throws NoSuchAlgorithmException, KeyManagementException, IOException,
			UnknownHostException {
		X509TrustManager xtm = new TrsX509TrustManager();
		TrustManager mytm[] = { xtm };
		SSLContext ctx = SSLContext.getInstance("SSL");

		ctx.init(null, mytm, null);

		SSLSocketFactory factory = ctx.getSocketFactory();
		String server = configManager.getValue("server");
		int port = Integer.parseInt(configManager.getValue("port"));
		Socket socket = factory.createSocket(server, port);
		socket.setKeepAlive(true);

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out.write("GET " + url + " HTTP/1.0\n\n");
		out.flush();
		String line;
		StringBuffer sb = new StringBuffer();
		String lastLine = "";
		while ((line = in.readLine()) != null) {
			sb.append(line + "\n");
			lastLine = line;
		}
		out.close();
		in.close();
		return lastLine;
	}
}

class TrsX509TrustManager implements X509TrustManager {
	TrsX509TrustManager() {
	}

	public void checkClientTrusted(X509Certificate chain[], String authType) throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate chain[], String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}