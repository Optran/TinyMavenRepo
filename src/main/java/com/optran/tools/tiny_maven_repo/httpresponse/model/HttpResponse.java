package com.optran.tools.tiny_maven_repo.httpresponse.model;

public class HttpResponse {
	private HttpResponseMetadata responseMetadata;
	private HttpResponseHeaders responseHeaders;
	private HttpResponsePayload responsePayload;
	public HttpResponse() {
		responseMetadata = new HttpResponseMetadata();
		responseHeaders = new HttpResponseHeaders();
		responsePayload = new HttpResponsePayload();
	}
	public HttpResponseMetadata getResponseMetadata() {
		return responseMetadata;
	}
	public void setResponseMetadata(HttpResponseMetadata responseMetadata) {
		this.responseMetadata = responseMetadata;
	}
	public HttpResponseHeaders getResponseHeaders() {
		return responseHeaders;
	}
	public void setResponseHeaders(HttpResponseHeaders responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	public HttpResponsePayload getResponsePayload() {
		return responsePayload;
	}
	public void setResponsePayload(HttpResponsePayload responsePayload) {
		this.responsePayload = responsePayload;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpResponse [responseMetadata=");
		builder.append(responseMetadata);
		builder.append(", responseHeaders=");
		builder.append(responseHeaders);
		builder.append(", responsePayload=");
		builder.append(responsePayload);
		builder.append("]");
		return builder.toString();
	}
}
