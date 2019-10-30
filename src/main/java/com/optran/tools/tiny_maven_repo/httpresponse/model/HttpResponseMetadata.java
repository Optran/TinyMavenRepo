package com.optran.tools.tiny_maven_repo.httpresponse.model;

public class HttpResponseMetadata {
	private String protocol;
	private String returnCode;
	private String returnCodeDescription;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnCodeDescription() {
		return returnCodeDescription;
	}
	public void setReturnCodeDescription(String returnCodeDescription) {
		this.returnCodeDescription = returnCodeDescription;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpResponseMetadata [protocol=");
		builder.append(protocol);
		builder.append(", returnCode=");
		builder.append(returnCode);
		builder.append(", returnCodeDescription=");
		builder.append(returnCodeDescription);
		builder.append("]");
		return builder.toString();
	}
}
