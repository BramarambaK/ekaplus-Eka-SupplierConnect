package com.eka.supplierconnect.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eka.supplierconnect.validator.CommonValidator;

import ch.qos.logback.classic.Level;

@RestController
@RequestMapping(value = "/logger")
public class LogLevelController {
	
	@Autowired
	private CommonValidator validator;
	private final static Map<String, Level> VALID_LEVELS = new HashMap<String, Level>();
	static {
		VALID_LEVELS.put("TRACE", Level.TRACE);
		VALID_LEVELS.put("DEBUG", Level.DEBUG);
		VALID_LEVELS.put("INFO", Level.INFO);
		VALID_LEVELS.put("WARN", Level.WARN);
		VALID_LEVELS.put("ERROR", Level.ERROR);
	}

	private final Logger LOGGER = ESAPI.getLogger(LogLevelController.class);

	@RequestMapping(value = "/loglevel/{logLevel}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object setLogLevel(HttpServletRequest request,
			@PathVariable("logLevel") String logLevel,
			@Nullable @RequestBody String packageName) throws Exception {
    
		LOGGER.error(Logger.EVENT_SUCCESS, "changing log level ");
		return processRequest(validator.cleanData(logLevel),validator.cleanData(packageName));
	}

	private Object processRequest(String logLevel,String packageName) {
		if (!VALID_LEVELS.containsKey(logLevel))
			return ESAPI.encoder().encodeForHTML("Invalid Log Level. Should be one of "
					+ VALID_LEVELS.keySet());

		org.slf4j.Logger logger = null;
		if(packageName==null || packageName.trim().length()==0)
		logger = LoggerFactory
				.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		else
			logger = LoggerFactory
			.getLogger(packageName);	
		
		if (logger instanceof ch.qos.logback.classic.Logger) {
			((ch.qos.logback.classic.Logger) logger).setLevel(VALID_LEVELS
					.get(logLevel));

			LOGGER.error(Logger.EVENT_SUCCESS,
					"**********************************************************");
			LOGGER.error(Logger.EVENT_SUCCESS, "LOG level set to " + logLevel + " for "+ logger);
			LOGGER.error(Logger.EVENT_SUCCESS,
					"**********************************************************");
			return "Log level Updated to " + logLevel;
		} else {
			return "Log leve update failed";
		}
	}
}
