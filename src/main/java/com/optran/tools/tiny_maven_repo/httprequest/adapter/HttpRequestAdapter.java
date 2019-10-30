package com.optran.tools.tiny_maven_repo.httprequest.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.optran.tools.tiny_maven_repo.common.CommonUtils;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequest;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequestHeaderEntry;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequestHeaders;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequestMetadata;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequestPayload;
import com.optran.tools.tiny_maven_repo.httprequest.model.HttpRequestType;

public class HttpRequestAdapter {
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final Logger logger = Logger.getLogger(HttpRequestAdapter.class);

	public static HttpRequest read(InputStream is) throws IOException {
		HttpRequest request = new HttpRequest();

		Scanner scn = new Scanner(is);
		boolean first = true;
		boolean readNext = true;
		while (readNext) {
			String line = scn.nextLine();
			if (first) {
				first = false;
				HttpRequestMetadata metadata = parseMetadata(line);
				if (metadata == null) {
					return null;
				} else {
					request.setRequestMetadata(metadata);
				}
			} else {
				HttpRequestHeaderEntry entry = parseHeaderEntry(line);
				if (null != entry) {
					request.getRequestHeaders().getEntries().put(entry.getKey().toLowerCase(), entry);
				}
			}
			if (StringUtils.isBlank(line)) {
				readNext = false;
			}
		}

		HttpRequestHeaderEntry entry = request.getRequestHeaders().getEntries().get(CONTENT_LENGTH);
		if (null != entry) {
			String valueStr = StringUtils.trim(entry.getValue());
			if (StringUtils.isNumeric(valueStr)) {
				int value = Integer.parseInt(valueStr);
				byte[] data = new byte[value];
				for (int i = 0; i < value; i++) {
					data[i] = (byte) is.read();
				}
				request.getRequestPayload().setPayload(data);
			}
		}

		return request;
	}

	private static HttpRequestHeaderEntry parseHeaderEntry(String line) {
		if (line.contains(":")) {
			int index = line.indexOf(":");
			if (index >= 0) {
				String key = StringUtils.substring(line, 0, index);
				String value = StringUtils.trim(StringUtils.substring(line, index + 1));
				HttpRequestHeaderEntry entry = new HttpRequestHeaderEntry(key, value);
				return entry;
			}
		}
		return null;
	}

	private static HttpRequestMetadata parseMetadata(String line) {
		HttpRequestMetadata metadata = new HttpRequestMetadata();
		String[] elements = line.split("[ \r\t\n]+");
		if (elements.length < 3) {
			return null;
		}

		String requestTypeStr = elements[0];
		HttpRequestType requestType = null;
		String resource = elements[1];
		Map<String, String> resourceParams = new HashMap<String, String>();
		String protocol = elements[2];

		if ("GET".equalsIgnoreCase(requestTypeStr)) {
			requestType = HttpRequestType.GET;
		} else if ("HEAD".equalsIgnoreCase(requestTypeStr)) {
			requestType = HttpRequestType.HEAD;
		} else if ("POST".equalsIgnoreCase(requestTypeStr)) {
			requestType = HttpRequestType.POST;
		} else if ("PUT".equalsIgnoreCase(requestTypeStr)) {
			requestType = HttpRequestType.PUT;
		} else if ("DELETE".equalsIgnoreCase(requestTypeStr)) {
			requestType = HttpRequestType.DELETE;
		} else {
			logger.error("Invalid or Unsupported HTTP header.");
			return null;
		}

		try {
			resource = URLDecoder.decode(resource, "UTF-8");
			String resourceMapStr = null;
			if (resource.contains("?")) {
				resourceMapStr = resource.substring(resource.indexOf("?") + 1).replace('&', '\n');
				resource = resource.substring(0, resource.indexOf("?"));
			}
			resource = CommonUtils.formatResourcePath(resource);
			if (StringUtils.isNotBlank(resourceMapStr)) {
				Properties urlProperties = new Properties();
				urlProperties.load(new ByteArrayInputStream(resourceMapStr.getBytes("UTF-8")));
				for (Object key : urlProperties.keySet()) {
					if (key instanceof String) {
						String keyStr = (String) key;
						resourceParams.put(keyStr, urlProperties.getProperty(keyStr));
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding exception. This should not occur", e);
			return null;
		} catch (IOException e) {
			logger.error("I/O exception. This should not occur", e);
			return null;
		}

		metadata.setRequestType(requestType);
		metadata.setResource(resource);
		metadata.setResourceParams(resourceParams);
		metadata.setProtocol(protocol);
		return metadata;
	}

	public static void write(HttpRequest request, OutputStream os) throws IOException {
		HttpRequestMetadata metadata = request.getRequestMetadata();
		HttpRequestHeaders requestHeaders = request.getRequestHeaders();
		HttpRequestPayload payload = request.getRequestPayload();
		
		StringBuilder out = new StringBuilder();
		
		if(null!=metadata) {
			out.append(metadata.getRequestType());
			out.append(" ");
			
			String resource = metadata.getResource();
			Map<String, String>resourceParams = metadata.getResourceParams();
			out.append(resource);
			if(!resourceParams.isEmpty()) {
				out.append("?");
				boolean first = true;
				for(String key:resourceParams.keySet()) {
					if(first) {
						first=false;
					} else {
						out.append("&");
					}
					out.append(key);
					out.append("=");
					out.append(resourceParams.get(key));
				}
			}
			out.append(" ");
			
			out.append(metadata.getProtocol());
			out.append(" \n");
		}
		
		if(null!=requestHeaders) {
			Map<String, HttpRequestHeaderEntry>entriesMap = requestHeaders.getEntries();
			if(null!=entriesMap && !entriesMap.isEmpty()) {
				for (HttpRequestHeaderEntry entry:entriesMap.values()) {
					out.append(entry.getKey());
					out.append(": ");
					out.append(entry.getValue());
					out.append("\n");
				}
			}
		}
		out.append("\n");
		
		os.write(out.toString().getBytes("UTF-8"));
		
		if(payload!=null) {
			byte[]data = payload.getPayload();
			if(data!=null) {
				os.write(data);
			}
		}
		os.flush();
	}
}









































