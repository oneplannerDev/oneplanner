package com.ontide.oneplanner.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWrapper {

	private static Logger logger = null;

	public LogWrapper(Class clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}
	
	public void info(String msg) {
		logger.info(msg);
	}

	public void debug(String msg) {
		logger.debug(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}
	
	public void error(String msg, Throwable t) {
		logger.error(msg,t);
	}
}
