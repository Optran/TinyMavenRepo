package com.optran.tools.tiny_maven_repo.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class ApplicationConfiguration {
	private static final Logger logger = Logger.getLogger(ApplicationConfiguration.class);
	private Map<String, MavenRepositoryConfiguration>mavenRepositoryConfiguration;

	public ApplicationConfiguration(Map<String, MavenRepositoryConfiguration>mavenRepositoryConfiguration) {
		mavenRepositoryConfiguration = new ConcurrentHashMap<String, MavenRepositoryConfiguration>();
	}
	
	public MavenRepositoryConfiguration getMavenRepositoryConfiguration(String path) {
		return mavenRepositoryConfiguration.get(path);
	}
}
