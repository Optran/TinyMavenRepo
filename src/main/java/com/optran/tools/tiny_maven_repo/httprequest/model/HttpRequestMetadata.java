package com.optran.tools.tiny_maven_repo.httprequest.model;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestMetadata {
	private HttpRequestType requestType;
	private String resource;
	private Map<String, String> resourceParams;
	private String protocol;
	
	public HttpRequestMetadata() {
		resourceParams = new HashMap<String, String>();
	}

	public HttpRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(HttpRequestType requestType) {
		this.requestType = requestType;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Map<String, String> getResourceParams() {
		return resourceParams;
	}

	public void setResourceParams(Map<String, String> resourceParams) {
		this.resourceParams = resourceParams;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpRequestMetadata [requestType=");
		builder.append(requestType);
		builder.append(", resource=");
		builder.append(resource);
		builder.append(", resourceParams=");
		builder.append(resourceParams);
		builder.append(", protocol=");
		builder.append(protocol);
		builder.append("]");
		return builder.toString();
	}
}
