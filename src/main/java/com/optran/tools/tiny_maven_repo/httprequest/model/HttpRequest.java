package com.optran.tools.tiny_maven_repo.httprequest.model;

public class HttpRequest {
	private HttpRequestMetadata requestMetadata;
	private HttpRequestHeaders requestHeaders;
	private HttpRequestPayload requestPayload;
	
	public HttpRequest() {
		requestMetadata = new HttpRequestMetadata();
		requestHeaders = new HttpRequestHeaders();
		requestPayload = new HttpRequestPayload();
	}

	public HttpRequestMetadata getRequestMetadata() {
		return requestMetadata;
	}

	public void setRequestMetadata(HttpRequestMetadata requestMetadata) {
		this.requestMetadata = requestMetadata;
	}

	public HttpRequestHeaders getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(HttpRequestHeaders requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public HttpRequestPayload getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(HttpRequestPayload requestPayload) {
		this.requestPayload = requestPayload;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpRequest [requestMetadata=");
		builder.append(requestMetadata);
		builder.append(", requestHeaders=");
		builder.append(requestHeaders);
		builder.append(", requestPayload=");
		builder.append(requestPayload);
		builder.append("]");
		return builder.toString();
	}
}
