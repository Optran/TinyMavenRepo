package com.optran.tools.tiny_maven_repo.http.processor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.optran.tools.tiny_maven_repo.common.CommonUtils;
import com.optran.tools.tiny_maven_repo.httprequest.adapter.HttpRequestAdapter;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequest;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequestHeaderEntry;
import com.optran.tools.tiny_maven_repo.httpresponse.adapter.HttpResponseAdapter;
import com.optran.tools.tiny_maven_repo.httpresponse.model.HttpResponse;

public class HttpProcessor implements Runnable {
	private static final Logger logger = Logger.getLogger(HttpProcessor.class);
	private Socket socket;

	public HttpProcessor(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		Socket sslSocket = null;
		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			HttpRequest request = HttpRequestAdapter.read(is);
			if (request == null) {
				socket.close();
				return;
			}
			printRequest(request);
			request.getRequestHeaders().getEntries().put("host",
					new HttpRequestHeaderEntry("Host", "repo.maven.apache.org"));
			printRequest(request);

			sslSocket = CommonUtils.callHttps("apache.org");
			HttpRequestAdapter.write(request, sslSocket.getOutputStream());

			HttpResponse response = HttpResponseAdapter.read(sslSocket.getInputStream());
			printResponse(response);
			HttpResponseAdapter.write(response, os);

			sslSocket.close();
		} catch (Exception e) {
			logger.error("Unable to process http request!", e);
			try {
				badRequestError(socket.getOutputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
			try {
				sslSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private static void badRequestError(OutputStream os) throws UnsupportedEncodingException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("HTTP/1.1 400 Bad Request\n");
		sb.append("Content-Length: 0\n");
		sb.append("Connection: close\n");
		sb.append("\n");
		os.write(sb.toString().getBytes("UTF-8"));
	}

	private void printRequest(HttpRequest request) throws Exception {
		System.out.println("************************************************************************");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		HttpRequestAdapter.write(request, baos);
		System.out.println(new String(baos.toByteArray(), "UTF-8"));
		System.out.println("************************************************************************");
	}

	private void printResponse(HttpResponse response) throws Exception {
		System.out.println("************************************************************************");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		HttpResponseAdapter.write(response, baos);
		System.out.println(new String(baos.toByteArray(), "UTF-8"));
		System.out.println("************************************************************************");
	}
}
