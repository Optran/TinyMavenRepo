package com.optran.tools.tiny_maven_repo.shared;

import com.optran.tools.tiny_maven_repo.configuration.ApplicationConfiguration;

public class ApplicationSession {
	private static ApplicationConfiguration applicationConfiguration;
	public static synchronized ApplicationConfiguration getApplicationConfiguration() {
		return applicationConfiguration;
	}
	public static synchronized void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
		ApplicationSession.applicationConfiguration = applicationConfiguration;
	}
}
