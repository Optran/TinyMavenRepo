package com.optran.tools.tiny_maven_repo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.optran.tools.tiny_maven_repo.configuration.ApplicationConfiguration;
import com.optran.tools.tiny_maven_repo.configuration.MavenRepositoryConfiguration;
import com.optran.tools.tiny_maven_repo.server.MavenRepositoryServer;
import com.optran.tools.tiny_maven_repo.shared.ApplicationSession;

public class LaunchServer {
	private static final Logger logger = Logger.getLogger(LaunchServer.class);
	private static int port = -1;
	private static int threads = -1;

	public static void main(String[] args) {
		logger.info("Starting TinyMavenRepo...");
		boolean success = true;
		success = loadApplicationProperties();
		if (success) {
			try {
				startServer();
			} catch (IOException e) {
				logger.error("Unable to start server.", e);
			}
		}
	}

	private static void startServer() throws IOException {
		Thread serverThread = new Thread(new MavenRepositoryServer(port, threads));
		serverThread.setDaemon(false);
		serverThread.start();
	}

	private static boolean loadApplicationProperties() {
		try {
			Properties sysProps = System.getProperties();
			String applicationConfigurationPath = (String) sysProps.get("tiny_maven_repo.application.properties");
			if (StringUtils.isBlank(applicationConfigurationPath)) {
				applicationConfigurationPath = "config/application.properties";
			}

			File applicationConfigurationFile = new File(applicationConfigurationPath);
			if (applicationConfigurationFile.exists() && applicationConfigurationFile.isFile()) {
				Properties applicationProperties = new Properties();
				applicationProperties.load(new FileInputStream(applicationConfigurationFile));
				boolean appSetupSuccess = setupApplicationProperties(applicationProperties);
				if(!appSetupSuccess) {
					return false;
				}
				Map<String, MavenRepositoryConfiguration> mavenRepositoryConfiguration = loadProperties(
						applicationProperties);
				ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(
						mavenRepositoryConfiguration);
				ApplicationSession.setApplicationConfiguration(applicationConfiguration);
			} else {
				logger.warn("Unable to read application properties, proceeding with defaults!");
			}
			return true;
		} catch (IOException e) {
			logger.error("Unable to read the application properties file!", e);
			return false;
		}
	}

	private static boolean setupApplicationProperties(Properties applicationProperties) {
		String portStr = applicationProperties.getProperty("application.port");
		if (StringUtils.isBlank(portStr)) {
			portStr = "80";
		}
		if(!StringUtils.isNumeric(portStr)) {
			logger.error("The port supplied is not valid");
			return false;
		}
		port = Integer.parseInt(portStr);
		
		
		String threadStr = applicationProperties.getProperty("application.threads");
		if (StringUtils.isBlank(portStr)) {
			threadStr = "50";
		}
		if(!StringUtils.isNumeric(threadStr)) {
			logger.error("The number of threads supplied is not valid");
			return false;
		}
		threads = Integer.parseInt(threadStr);
		return true;
	}

	private static Map<String, MavenRepositoryConfiguration> loadProperties(Properties applicationProperties) {
		Map<String, MavenRepositoryConfiguration> mavenRepositoryConfiguration = new ConcurrentHashMap<String, MavenRepositoryConfiguration>();
		Map<String, MavenRepositoryConfiguration> configuratiionMapping = new HashMap<String, MavenRepositoryConfiguration>();
		for (Object key : applicationProperties.keySet()) {
			if (key == null || !(key instanceof String)) {
				continue;
			}
			String propertyKey = (String) key;
			String[] keyList = propertyKey.split("\\.");
			if (keyList.length != 2) {
				continue;
			}
			String repositoryName = StringUtils.trim(keyList[0]);
			if (StringUtils.isBlank(repositoryName)) {
				continue;
			}
			if ("application".equalsIgnoreCase(repositoryName)) {
				continue;
			}
			MavenRepositoryConfiguration config = configuratiionMapping.get(repositoryName);
			if (null == config) {
				config = new MavenRepositoryConfiguration(repositoryName);
				configuratiionMapping.put(repositoryName, config);
			}
			String attribute = keyList[1];
			if (StringUtils.isBlank(attribute)) {
				continue;
			}
			config.setProperty(attribute, StringUtils.trim(applicationProperties.getProperty(propertyKey)));
		}
		for (String configKey : configuratiionMapping.keySet()) {
			MavenRepositoryConfiguration config = configuratiionMapping.get(configKey);
			if (null != config && config.isValid()) {
				mavenRepositoryConfiguration.put(config.getPath(), config);
			} else {
				logger.warn("Rejecting config " + configKey + " due to invalid data setup.");
			}
		}
		return mavenRepositoryConfiguration;
	}
}
