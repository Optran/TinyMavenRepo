package com.optran.tools.tiny_maven_repo.configuration;

import org.apache.commons.lang3.StringUtils;

import com.optran.tools.tiny_maven_repo.common.CommonUtils;

public class MavenRepositoryConfiguration {
	/* Application constants */
	private static final String MVN_PATH = "path";
	private static final String MVN_REMOTE = "remote";
	/* Instance variables */
	private String repositoryName;
	private String path;
	private String remote;

	/* Constructor */
	public MavenRepositoryConfiguration(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	/* Custom control methods */
	public void setProperty(String property, String value) {
		if (MVN_PATH.equalsIgnoreCase(property)) {
			setPath(value);
		} else if (MVN_REMOTE.equalsIgnoreCase(property)) {
			setRemote(value);
		}
	}

	public boolean isValid() {
		return StringUtils.isNotBlank(path) && StringUtils.isNotBlank(remote);
	}

	/* Getters and setters */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

}
