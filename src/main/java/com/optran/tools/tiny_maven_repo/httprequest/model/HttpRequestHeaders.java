package com.optran.tools.tiny_maven_repo.httprequest.model;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeaders {
	private Map<String, HttpRequestHeaderEntry> entries;
	
	public HttpRequestHeaders() {
		entries = new HashMap<String, HttpRequestHeaderEntry>();
	}

	public Map<String, HttpRequestHeaderEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, HttpRequestHeaderEntry> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpRequestHeaders [entries=");
		builder.append(entries);
		builder.append("]");
		return builder.toString();
	}

}
