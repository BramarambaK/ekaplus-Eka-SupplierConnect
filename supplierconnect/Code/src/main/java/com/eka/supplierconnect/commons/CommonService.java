package com.eka.supplierconnect.commons;

import java.net.URI;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.supplierconnect.constant.ErrorConstants;
import com.eka.supplierconnect.exception.ConnectException;
import com.eka.supplierconnect.exception.SupplierConnectException;
import com.eka.supplierconnect.validator.CommonValidator;
import com.eka.supplierconnect.webclient.BaseHttpClient;

@Service
public class CommonService {
	final static Logger logger = ESAPI.getLogger(CommonService.class);

	@Value("${eka_connect_host}")
	private String ekaConnectHost;

	@Autowired
	public RestTemplate restTemplate;


	@Autowired
	private BaseHttpClient httpClient;
	
	@Autowired
	private ContextProvider contextProvider;
	
	@Autowired
	private CommonValidator commonValidator;

	/**
	 * Assign the attributes being passed from client as a header attributes.
	 * 
	 */

	public HttpHeaders getHttpHeader() {

		HttpHeaders headers = new HttpHeaders();
		
        HttpServletRequest httpRequest = contextProvider.getCurrentContext().getRequest();
		

		Enumeration<?> names = httpRequest.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, commonValidator.cleanData(httpRequest.getHeader(name)));
		}
		
		return headers;

	}

	public HttpHeaders getHttpHeader(HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();

		Enumeration<?> names = request.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, commonValidator.cleanData(request.getHeader(name)));
		}
		addDefaultHeaders(headers);
		return headers;

	}

// Adding below method as upload api requires content-type as multipart/form data from postman
// once the File is passed we need to make content-type as application/json for trm upload and for property api	
	public HttpHeaders getHttpHeaderWithContentType() {

		HttpHeaders headers = new HttpHeaders();
		
        HttpServletRequest httpRequest = contextProvider.getCurrentContext().getRequest();
		

		Enumeration<?> names = httpRequest.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, httpRequest.getHeader(name));
		}
		addContentTypeToHeaders(headers);
		return headers;

	}
	
	// Adding below method as upload api requires content-type as multipart/form data from postman
	// once the File is passed we need to make content-type as application/json for trm upload and for property api	
	private void addContentTypeToHeaders(HttpHeaders headers) {
		// TODO Auto-generated method stub
		headers.remove("Content-Type");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
	}
	
	
	private void addDefaultHeaders(HttpHeaders headers) {
		// TODO Auto-generated method stub
		headers.add("Content-Type", "application/json");
	}

	public Object callWorkFlowDataAPI(HttpServletRequest req, JSONObject workFlowAPIData) {
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(ekaConnectHost + "/workflow/data");
			return httpClient.fireHttpRequest(uriBuilder.build().toUri(), HttpMethod.POST, workFlowAPIData.toString(),
					this.getHttpHeader(req), Object.class, "Error During invoking Workflow Data API").getBody();
		} catch (ConnectException ce) {
			ce.printStackTrace();
			throw new ConnectException(getErrorMessage("SC033", "Error while calling workflow data api through supplierconnect", "supplierconnect"), ce);
		} catch (Exception e) {
			throw new ConnectException(getErrorMessage("SC033", "Error while calling workflow data api through supplierconnect", "supplierconnect"), e);
		}

	}
	
	/**
	 * Usage:The <i>getPropertyFromConnect</i> fetches property from connect.If
	 * appUUID is passed as null, it will make the call for property at tenant
	 * level.
	 * <p>
	 * Note:Do not pass appUUID if you know that this property exists for tenant
	 * level only, as user-authorization issue might come for that app.
	 * </p>
	 * @param propertyName the name of property
	 * @param appUUID      the app uuid
	 * 
	 * @return
	 * @throws SupplierConnectException
	 */
	public String getPropertyFromConnect(String propertyName, String appUUID) {
		logger.debug(logger.EVENT_SUCCESS, ESAPI.encoder().encodeForHTML("inside method getPropertyFromConnect"));
		String propertyUri = null;
		if (Objects.nonNull(appUUID) && !StringUtils.isEmpty(appUUID))
			propertyUri = commonValidator.cleanData(ekaConnectHost + "/property/" + appUUID + "/" + propertyName);
		else
			propertyUri = commonValidator.cleanData(ekaConnectHost + "/property/" + propertyName);
		HttpHeaders httpHeaders = getHttpHeader();
		String propertyValue = null;
		ResponseEntity<Map> propResult = null;
		try {
			propResult = httpClient.fireHttpRequest(new URI(propertyUri), HttpMethod.GET, null, httpHeaders, Map.class);
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE, ESAPI.encoder()
					.encodeForHTML("Exception while calling getPropertyFromConnect " + e.getLocalizedMessage()), e);			
			throw new SupplierConnectException(getErrorMessage("049","Error in fetching properties", "supplierconnect")
					 + e.getLocalizedMessage());
		}
		if (propResult != null) {
			propertyValue = String.valueOf(propResult.getBody().get("propertyValue"));
		}
		logger.debug(logger.EVENT_SUCCESS, ESAPI.encoder()
				.encodeForHTML("method getPropertyFromConnect ends with returned propertyValue:" + propertyValue));
		return propertyValue;
	}
	
	/**
	 * Gets the current user details.
	 *
	 * @param headers the headers
	 * @param platform_url the platform url
	 * @return the current user details
	 */
	public LinkedHashMap getCurrentUserDetails(HttpHeaders headers, String platform_url) {
		ResponseEntity<Object> getCurrentUserRes = httpClient.fireHttpRequest(
				UriComponentsBuilder.fromHttpUrl(platform_url).path("/spring/smartapp/currentUser").build().toUri(),
				HttpMethod.GET, null, headers, Object.class);
		LinkedHashMap currentUserData = (LinkedHashMap) getCurrentUserRes.getBody();
		LinkedHashMap currentUserDetails = (LinkedHashMap) currentUserData.get("data");
		return currentUserDetails;
	}
	
	public String idsInData(Object data) {
		if (Objects.nonNull(data) && data instanceof List && !((List) data).isEmpty()) {
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
			List<String> ids = dataList.stream().filter(datum -> Objects.nonNull(datum.get("_id")))
					.map(datum -> datum.get("_id").toString()).collect(Collectors.toList());
			String csvIds = String.join(",", ids);
			return csvIds;
		}
		return "";
	}
	
	public void addDataOptionsWhileSaving(Map<String, Object> data) {
		Map<String, Object> copySrcVersion = new HashMap<>();
		copySrcVersion.put("copySourceVersion", true);
		data.put("sys__data__options", copySrcVersion);
	}
	
	/**
	 * Usage:The <i>getErrorMessage</i> fetches error from error bundle from connect
	 * 
	 * @param errorCode the error code
	 * @param name      the name of the document
	 * @return the error message for that locale
	 * @throws SupplierConnectException
	 */
	public String getErrorMessage(String errorCode, String defaultErrorMsg, String appName, Object... parameters)
			throws SupplierConnectException {
		ResponseEntity<String> errorResponse = null;
		try {
			// call connect get /meta/type/refTypeId--
			String url = ekaConnectHost + "/meta/" + appName + "/" + ErrorConstants.ERROR_BUNDLE_PATH + "/" + errorCode;
			HttpHeaders httpHeaders = getHttpHeader();
			Map<String, Object> payload = new HashMap<>();
			if (Objects.nonNull(parameters))
				payload.put("parameters", Arrays.asList(parameters));
			errorResponse = httpClient.fireHttpRequest(new URI(url), HttpMethod.POST, payload, httpHeaders,
					String.class);
			if (Objects.isNull(errorResponse) || StringUtils.isEmpty(errorResponse.getBody())) {
				logger.debug(Logger.EVENT_SUCCESS, ESAPI.encoder()
						.encodeForHTML("got error message null response from connect for appName:" + appName));
				return ErrorConstants.ERROR_MSG_UNAVAILABLE;
			}
			return errorResponse.getBody();
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE,
					ESAPI.encoder().encodeForHTML("error inside method getError due to :" + e.getLocalizedMessage()), e);
			String msg = (null != parameters) ? String.format(defaultErrorMsg, parameters) : defaultErrorMsg;
			return msg;
		}
	}
}
