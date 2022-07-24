package com.eka.supplierconnect.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.eka.supplierconnect.commons.ContextProvider;
import com.eka.supplierconnect.constant.GlobalConstants;
import com.eka.supplierconnect.model.ContextInfo;

/**
 * <p>
 * <code>PropertyInterceptor</code> make Property API call and injects the same
 * into ApplicationProps.
 * <p>
 * <hr>
 * 
 * @author Ranjan.Jha
 * @version 1.0
 */

@Component
public class ContextSetter implements AsyncHandlerInterceptor {

	@Autowired
	public ContextProvider contextProvider;

	final static Logger logger = ESAPI.getLogger(ContextSetter.class);

	private static final String X_REQUEST_ID = "X-Request-Id";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {		
		setTenantNameAndRequestIdToLog(request);
		setContextDefaultValues(request);
		
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();
		logger.info(Logger.EVENT_SUCCESS, "********* SupplierConnect-PreHandle Started......"+"Request Details: " + requestMethod + " " + requestURI);
		
		RequestResponseLogger.logRequest(request);
		
		logger.debug(Logger.EVENT_SUCCESS,
				ESAPI.encoder()
						.encodeForHTML("headers in current request: tenant:" + request.getHeader("X-TenantID")
								+ ",authToken:" + request.getHeader("Authorization"))
						+ ",content-type:" + request.getHeader("Content-Type"));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		RequestResponseLogger.logResponseHeaders(response);
	}
	
	@Override
	public  void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();
		response.addHeader("requestId", contextProvider.getCurrentContext().getRequestId());
		if(ex!=null){
			RequestResponseLogger.logResponseHeaderDetails(response);
		}
		logger.info(Logger.EVENT_SUCCESS, "********* SupplierConnect User Request completed......"+"Request Details: " + requestMethod + " " + requestURI);
		removeContext();
		MDC.clear();
	}

	public void setContextDefaultValues(HttpServletRequest request) {

		ContextInfo freshContext = new ContextInfo();
		freshContext.setRequest(request);
		contextProvider.setCurrentContext(freshContext);
		freshContext.setRequestId(MDC.get(GlobalConstants.REQUEST_ID));
		freshContext.setSourceDeviceId(MDC.get(GlobalConstants.SOURCE_DEVICE_ID));
	}

	public void removeContext() {

		contextProvider.remove();
	}

	private void setTenantNameAndRequestIdToLog(HttpServletRequest request) {
		String requestId = null;
		String tenantName = null;
		String sourceDeviceId = null;
		if (null != request.getHeader(GlobalConstants.REQUEST_ID)) {
			requestId = request.getHeader(GlobalConstants.REQUEST_ID);
		} else {
			requestId = UUID.randomUUID().toString().replace("-", "")+"-GEN";
		}
		if (null != request.getHeader(GlobalConstants.SOURCE_DEVICE_ID)) {
			sourceDeviceId = request.getHeader(GlobalConstants.SOURCE_DEVICE_ID);
		} else {
			sourceDeviceId = "na";

		}
		if (null == request.getHeader(GlobalConstants.X_TENANT_ID)) {
			tenantName = request.getServerName();
			tenantName = tenantName.split(GlobalConstants.REGEX_DOT)[0];
		} else {
			tenantName = request.getHeader(GlobalConstants.X_TENANT_ID);
		}
		MDC.put(GlobalConstants.REQUEST_ID, requestId);
		MDC.put("tenantName", tenantName);
		MDC.put(GlobalConstants.SOURCE_DEVICE_ID, sourceDeviceId);
	}

}
