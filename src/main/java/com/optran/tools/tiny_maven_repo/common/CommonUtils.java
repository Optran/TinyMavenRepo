package com.optran.tools.tiny_maven_repo.common;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {
	public static String formatResourcePath(String path) {
		path = StringUtils.trim(path);
		/*
		 * path = path.replace('\\', '/'); if (path.endsWith("/") && path.length()>1) {
		 * path = path.substring(0, path.length() - 1); }
		 */
		return path;
	}

	public static SSLSocket callHttps(String url) {
		return callHttps(url, 443);
	}
	public static SSLSocket callHttps(String url, int port) {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, null, null);
			SSLSocketFactory factory = sslContext.getSocketFactory();

			SSLSocket sslSocket = (SSLSocket) factory.createSocket(url, port);
			sslSocket.startHandshake();

			return sslSocket;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
