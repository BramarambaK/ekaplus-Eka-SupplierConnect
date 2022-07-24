package com.eka.supplierconnect.commons;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.stereotype.Component;

@Component("commonUtils")
public class CommonUtils {
	final static  Logger logger = ESAPI.getLogger(CommonUtils.class);
	
	public String getTRMErrorListMsgIfExist(String exceptionMsg,String prefix,String suffix,String compareString) {
		String errMsgs = null;
		logger.debug(Logger.EVENT_SUCCESS,"compareString:"+compareString +" prefix:"+prefix+" suffix:"+suffix);	
		if(exceptionMsg.contains(compareString)) {
			errMsgs = org.apache.commons.lang3.StringUtils.substringBetween(exceptionMsg, prefix, suffix);
			logger.debug(Logger.EVENT_SUCCESS,"errMsgs:"+errMsgs);
		}else {
			logger.debug(Logger.EVENT_SUCCESS,"compareString "+compareString+" not found in exception msg.");
		}
		return errMsgs;
	}

}
