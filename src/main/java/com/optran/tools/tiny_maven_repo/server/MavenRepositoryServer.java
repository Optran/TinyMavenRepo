package com.optran.tools.tiny_maven_repo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.optran.tools.tiny_maven_repo.http.processor.HttpProcessor;

public class MavenRepositoryServer implements Runnable {
	private static final Logger logger = Logger.getLogger(MavenRepositoryServer.class);
	private int port;
	private int threads;
	private ServerSocket serverSocket;
	private ExecutorService service;
	public MavenRepositoryServer(int port, int threads) throws IOException {
		this.port = port;
		this.threads = threads;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("Unable to start server on port ("+port+")", e);
			return;
		}
		logger.info("Listening on port ("+port+") with "+threads+" thread"+((threads>1)?"s":""));
		service = Executors.newFixedThreadPool(threads);
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				HttpProcessor processor = new HttpProcessor(socket);
				service.execute(processor);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
