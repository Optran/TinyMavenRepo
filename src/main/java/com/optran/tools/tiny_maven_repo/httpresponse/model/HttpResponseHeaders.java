package com.optran.tools.tiny_maven_repo.httpresponse.model;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeaders {
	private Map<String, HttpResponseHeaderEntry> entries;

	public HttpResponseHeaders() {
		entries = new HashMap<String, HttpResponseHeaderEntry>();
	}

	public Map<String, HttpResponseHeaderEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, HttpResponseHeaderEntry> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpResponseHeaders [entries=");
		builder.append(entries);
		builder.append("]");
		return builder.toString();
	}

}
