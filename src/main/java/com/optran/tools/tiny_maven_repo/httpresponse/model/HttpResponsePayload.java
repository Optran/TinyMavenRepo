package com.optran.tools.tiny_maven_repo.httpresponse.model;

import java.util.Arrays;

public class HttpResponsePayload {
	private byte[] payload;

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpResponsePayload [payload=");
		builder.append(Arrays.toString(payload));
		builder.append("]");
		return builder.toString();
	}

}
