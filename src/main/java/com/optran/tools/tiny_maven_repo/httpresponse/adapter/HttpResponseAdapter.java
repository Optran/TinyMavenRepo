package com.optran.tools.tiny_maven_repo.httpresponse.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.optran.tools.tiny_maven_repo.httpresponse.model.HttpResponse;
import com.optran.tools.tiny_maven_repo.httpresponse.model.HttpResponseHeaderEntry;
import com.optran.tools.tiny_maven_repo.httpresponse.model.HttpResponseHeaders;
import com.optran.tools.tiny_maven_repo.httpresponse.model.HttpResponseMetadata;
import com.optran.tools.tiny_maven_repo.httpresponse.model.HttpResponsePayload;

public class HttpResponseAdapter {
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final Logger logger = Logger.getLogger(HttpResponseAdapter.class);

	public static HttpResponse read(InputStream is) throws IOException {
		HttpResponse response = new HttpResponse();

		Scanner scn = new Scanner(is);
		boolean first = true;
		boolean readNext = true;
		while (readNext) {
			String line = scn.nextLine();
			if (first) {
				first = false;
				HttpResponseMetadata responseMetadata = parseMetadata(line);
				if (responseMetadata == null) {
					return null;
				} else {
					response.setResponseMetadata(responseMetadata);
				}
			} else {
				HttpResponseHeaderEntry entry = parseHeaderEntry(line);
				if (null != entry) {
					try {
						response.getResponseHeaders().getEntries().put(entry.getKey().toLowerCase(), entry);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (StringUtils.isBlank(line)) {
				readNext = false;
			}
		}
		
		HttpResponseHeaderEntry entry = response.getResponseHeaders().getEntries().get(CONTENT_LENGTH);
		if (null != entry) {
			String valueStr = StringUtils.trim(entry.getValue());
			if (StringUtils.isNumeric(valueStr)) {
				int value = Integer.parseInt(valueStr);
				byte[] data = new byte[value];
				for (int i = 0; i < value; i++) {
					data[i] = (byte) is.read();
				}
				response.getResponsePayload().setPayload(data);
			}
		}

		return response;
	}

	private static HttpResponseHeaderEntry parseHeaderEntry(String line) {
		if (line.contains(":")) {
			int index = line.indexOf(":");
			if (index >= 0) {
				String key = StringUtils.substring(line, 0, index);
				String value = StringUtils.trim(StringUtils.substring(line, index + 1));
				HttpResponseHeaderEntry entry = new HttpResponseHeaderEntry(key, value);
				return entry;
			}
		}
		return null;
	}

	private static HttpResponseMetadata parseMetadata(String line) {
		line = StringUtils.trim(line);
		int index = line.indexOf(' ');
		String protocol = line.substring(0, index);
		line = line.substring(index + 1);

		index = line.indexOf(' ');
		String returnCode = line.substring(0, index);
		line = line.substring(index + 1);

		String returnCodeDescription = line;

		if (StringUtils.isNotBlank(protocol) && StringUtils.isNotBlank(returnCode)
				&& StringUtils.isNotBlank(returnCodeDescription)) {
			HttpResponseMetadata metadata = new HttpResponseMetadata();
			metadata.setProtocol(protocol);
			metadata.setReturnCode(returnCode);
			metadata.setReturnCodeDescription(returnCodeDescription);
			return metadata;
		}
		return null;
	}

	public static void write(HttpResponse response, OutputStream os) throws IOException {
		HttpResponseMetadata responseMetadata = response.getResponseMetadata();
		HttpResponseHeaders responseHeaders = response.getResponseHeaders();
		HttpResponsePayload responsePayload = response.getResponsePayload();
		
		StringBuilder out = new StringBuilder();
		
		if(null!=responseMetadata) {
			out.append(responseMetadata.getProtocol());
			out.append(" ");
			out.append(responseMetadata.getReturnCode());
			out.append(" ");
			out.append(responseMetadata.getReturnCodeDescription());
			out.append("\n");
		}
		
		if(null!=responseHeaders) {
			Map<String, HttpResponseHeaderEntry>entriesMap = responseHeaders.getEntries();
			if(null!=entriesMap && !entriesMap.isEmpty()) {
				for (HttpResponseHeaderEntry entry:entriesMap.values()) {
					out.append(entry.getKey());
					out.append(": ");
					out.append(entry.getValue());
					out.append("\n");
				}
			}
		}
		out.append("\n");
		
		os.write(out.toString().getBytes("UTF-8"));
		
		if(responsePayload!=null) {
			byte[]data = responsePayload.getPayload();
			if(data!=null) {
				os.write(data);
			}
		}
		os.flush();
	}
}
