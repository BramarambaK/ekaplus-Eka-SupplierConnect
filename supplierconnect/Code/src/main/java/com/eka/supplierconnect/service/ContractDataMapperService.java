package com.eka.supplierconnect.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.supplierconnect.commons.CommonService;
import com.eka.supplierconnect.commons.CommonUtils;
import com.eka.supplierconnect.commons.DateUtility;
import com.eka.supplierconnect.exception.ConnectException;
import com.eka.supplierconnect.exception.ContractDataException;
import com.eka.supplierconnect.factory.RestTemplateGetRequestBodyFactory;
import com.eka.supplierconnect.model.DeliveryItem;
import com.eka.supplierconnect.model.MailInfo;
import com.eka.supplierconnect.payload.JsonBuilder;
import com.eka.supplierconnect.validator.CommonValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

/**
 * @author Vijayalakshmi.Nair
 *
 */
@Service
public class ContractDataMapperService {

	@Autowired
	RestTemplate restTemplate;
	/** The mongo template. */
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	CommonService commonService;	

	@Value("${eka_connect_host}")
	private String ekaConnectHost;
	
	@Autowired
	CommonValidator validator;
	@Autowired
	CommonUtils commonUtils;
	@Autowired
	private RestTemplateGetRequestBodyFactory restTemplateGetRequestBody;

	final static  Logger logger = ESAPI.getLogger(ContractDataMapperService.class);
	private static final String SUPPLIERCONNECT_UUID = "467a28cc-bc93-4e38-8ff5-0a56ae128f3b";
	private static final String DATA = "data";
	private static final String QUALITY = "contractQuality";
	private static final String INCOTERM = "incoTermId";
	private static final String DESTINATION = "incoTermDestination";	
	private static final String TAB_REQUEST_ID = "tabRequestId";
	private static final String ENTITY_VERSION_MAP = "entityVersionMap";
	private static final String TOTAL_NO_OF_RECORDS = "totalNoOfRecords";
	private static final String LISTING_CREATED_DATE = "listingCreatedDate";
	private static final String LISTING_CREATED_BY = "lisitngCreatedBy";
	private static final String LISTING_UPDATED_DATE = "lisitingUpdatedDate";
	private static final String LISTING_UPDATED_BY = "listingUpdatedBy";
	private static final String ISSUE_DATE = "issueDate";
	private static final String CP_NAME = "cpName";
	private static final String CONTRACT_REF_NO = "contractRefNo";
	private static final String CONTRACT_TYPE = "contractType";
	private static final String PRODUCT_GROUP_TYPE = "productGroupType";
	private static final String ASSET_CLASS = "assetclass";
	private static final String CONTRACT_QTY = "contractqty";
	private static final String CONTRACT_STATUS = "contractStatus";
	private static final String DELIVERY_ITEM_REF_NO = "deliveryItemRefNo";
	private static final String QUALITY_NAME = "qualityName";
	private static final String COUNTRY_OF_ORIGIN = "countryOfOrigin";
	private static final String COUNTRY_OF_LOADING = "countryOfLoading";
	private static final String LOADING_DATE = "loadingDate";
	private static final String ESTIMATED_ARRIVAL_DATE = "estimatedArrivalDate";
	private static final String EMAIL = "email";
	private static final String QUOTA_MONTH = "quotaMonth";
	private static final String TOLLING_SERVICE_TYPE = "tollingServiceType";
	private static final String TRADE_TYPE = "tradeType";
	private static final String ITEM_STATUS = "itemStatus";
	private static final String ATTRIBUTES = "attributes";
	private static final String LOCATION = "location";
	private static final String TRAXYS_ORG = "traxysOrg";
	private static final String INCO_TERM_LOCATION = "incotermLocation";
	private static final String QP = "qp";
	private static final String QUOTA_QTY = "quotaQty";
	private static final String QUOTA_QTY_UNIT = "quotaQtyUnit";
	private static final String QUOTA_QTY_BASIS = "quotaQtyBasis";
	private static final String QUOTA_OPEN_QTY = "quotaOpenQty";
	private static final String QUOTA_CALL_OFF_QTY = "quotaCalloffQty";
	private static final String QUOTA_DELIVERED_RECEIVED_QTY = "quotaDeliveredReceivedQty";
	private static final String QUOTA_INVOICED_QTY = "quotaInvoicedQty";
	private static final String QUOTA_PRICE_FIXED_QTY = "quotaPriceFixedQty";
	private static final String ALLOCATED_QTY = "allocatedQty";
	private static final String PCDIID = "pcdiId";
	private static final String PRICE_OPT_CALL_OFF_STATUS = "priceOptionCallOffStatus";
	private static final String PHYSICAL_OPT_PRESENT = "physicalOptPresent";
	private static final String STRATEGY = "strategy";
	private static final String BOOK_PROFIT_CENTER = "bookProfitCenter";
	private static final String TRADER = "trader";
	private static final String QP_PRICING_BASIS = "qpPricingBasis";
	private static final String QP_PRICING = "qpPricing";
	private static final String PRICING = "pricing";
	private static final String QUOTA_QTY_MAX = "quotaQuantityMax";
	private static final String QUOTA_QUANTITY_BASIS = "quotaQuantityBasis";
	private static final String TO_BE_CALLED_OFF_QTY = "toBeCalledOffQty";
	private static final String CALLED_OFF_QTY = "calledOffQty";
	private static final String EXECUTED_QTY = "executedQty";
	private static final String PRICING_STATUS = "pricingStatus";
	private static final String FIXED_PRICE_QTY = "fixedPriceQty";
	private static final String TITLE_TRANSFER_QTY = "titleTransferQty";
	private static final String PROV_INVOICED_QTY = "provInvoicedQty";
	private static final String FINAL_INVOICED_QTY = "finalInvoicedQty";
	private static final String PAY_IN_CURRENCY = "payInCurrency";
	private static final String FULLFILLMENT_STATUS = "fullfillmentStatus";
	private static final String PASS_THROUGH = "passThrough";
	private static final String DEAL_TYPE = "dealType";
	private static final String FULLFILLMENT_QTY = "fullfillmentQty";
	private static final String ORDER_LINE_NO = "orderLineNo";
	private static final String IS_INTER_COMPANY_CONTRACT = "isInterCompanyContract";
	private static final String INTER_COMPANY_CONTRACT_REF_NO = "interCompanyContractRefNo";
	private static final String RNUM = "rnum";
	private static final String INTERNAL_CONTRACT_REF_NO = "internalContractRefNo";
	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String OPEN_QTY = "openQty";
	private static final String FILTER_CITY_1 = "RONNSKAR";
	private static final String FILTER_CITY_2 = "HARJAVALTA";
	private static final String FILTER_CITY_3 = "Rönnskär";
	private static final String FILTER_CITY_4 = "SKELLEFTEHAMN";
	private static final String FILTER_CITY_5 = "HELSINGBORG";
	private static final String FILTER_CITY_6 = "PORI";
	private static final String RESPONSE = "response";
	private static final String GMRREFNO = "GmrRefNo";
	private static final String CONTAINER_FLAG_YES = "loose-001";
	private static final String CONTAINER_FLAG_NO = "loose-002";
	private static final String MODE_OF_TRANSPORT_RAIL = "Rail";
	private static final String CP_ADDR = "cpAddress";
	private static final String DEL_LOCATION = "delLocation";
	private static final String EXECUTABLE_QTY = "executableQty";
	private static final String EXECUTABLE_QTY_SUBSTR = "executableQtySubStr";
	private static final String OPEN_QTY_SUBSTR = "openQtySubStr";

	private static final String DELIVERY_ITEM_LIST_PATH = "/api/logistic/deliveryItemList";
	private static final String CREATE_GMR_PATH = "/api/logistic/shipment";
	private static final String CONTRACT_DETAILS_PATH = "/api/logistic/contract";
	private static final String ASSAY_URL_PATH = "/api/logistic/viewAssay";
	private static final String STOCK_URL_PATH = "/api/logistic/viewStocks";
	private static final String WEIGHING_AND_SAMPLING_ASSAY = "Weighing and Sampling Assay";
	private static final String ASSAY_TYPE = "assayType";
	private static final String EKA_CTRM_HOST = "eka_ctrm_host";
	private static final String STOCK_LIST = "stockList";
	private static final String ELEMENT_LIST = "elementList";
	private static final String ELEMENT_NAME = "elementName";
	private static final String SHOW_IN_SUPPLIER_CONNECT = "showInSupplierConnect";
	private static final String ELEMENT_ID = "elementId";
	private static final String TYPICAL = "typical";
	private static final String RATIO_NAME = "ratioName";
	private static final String USE_BOLIDEN_ASSAY = "useBolidenAssay";
	private static final String CTRM_ECONOMIC_VALUES_URI = "${platform_url}/trm/api/v1/scm-gmr-economic-value";
	private static final String VIEWGMR_URL_PATH = "/api/logistic/viewGmr";
	private static final String GMR_PAYLOAD = "payload";
	private static final String EMAIL_DATA = "emailData";
	private static final String INCO_LOCATION = "incoLocation";
	private static final String SUPPLIER_NAME = "supplierName";	
	private static final String GMR_CREATED_DATE = "gmr_create_date";
	private static final String SUBJECT = "subject";
	private static final String MAIL_BODY = "mail_body";
	private static final String MAIL_FROM_ADDR = "gmr_mail_fromAddr";
	private static final String TOTAL_COUNT = "totalCount";
	private static final String COMMA_SPACE = ", ";
	private static final String ASSAY_WINNER = "assayWinner";
	private static final String UMPIRE_PAYMENT = "umpirePayment";
	private static final String SELF_UMPIRE_PAYMENT = "Supplier Pays";
	private static final String EVEN_UMPIRE_PAYMENT = "Split Payment";
	private static final String CP_UMPIRE_PAYMENT = "Boliden Pays";
	private static final String SELF_ASSAY_WINNER = "Self";
	private static final String EVEN_ASSAY_WINNER = "Even";
	private static final String CP_ASSAY_WINNER = "CP";
	private static final String GMR_APPROVAL_STATUS_DRAFT = "Draft";
	private static final String NET_OPEN_QTY_SUBSTR="netOpenQtySubStr";
	private static final String NET_OPEN_QTY="netOpenQty";
	private static final String LOTNO="lotNo";
	private static final String TILDE_SEPARATOR="~";
	/**
	 * @return
	 */
	public String fetchDeliveryItemList(HttpServletRequest req) {

		// HttpHeaders headers = new HttpHeaders();
		String response = null;		
		List<DeliveryItem> deliveryItemList = new ArrayList<DeliveryItem>();
		try {
			long start1 = System.currentTimeMillis();
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + DELIVERY_ITEM_LIST_PATH;
			long end1 = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of connect prop call in advice del:" + (end1 - start1) + "ms" );
			HttpHeaders headers = commonService.getHttpHeader(req);
			headers.add("Content-Type", "application/json;charset=UTF-8");
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			// headers.add("username", "e-bolprpa");			
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);
			restTemplate.setMessageConverters(messageConverters);
			Map<String, Object> inp = new LinkedHashMap<String, Object>();			
			HttpEntity<Object> entity = new HttpEntity<Object>(inp, headers);			
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("***URI" + uri));	
			logger.debug(Logger.EVENT_SUCCESS,	"Headers:" + headers );
			long start = System.currentTimeMillis();
			deliveryItemList = Arrays.asList(restTemplate.exchange(uri, HttpMethod.POST, entity, DeliveryItem[].class).getBody());
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of TRM advice del API Call:" + (end - start) + "ms" );
			if (deliveryItemList != null) {		
				start = System.currentTimeMillis();
				deliveryItemList.sort((DeliveryItem o1, DeliveryItem o2) -> o2.getDeliveryItemRefNo()
						.compareTo(o1.getDeliveryItemRefNo()));
				response = mergeDataForList(deliveryItemList);
				end = System.currentTimeMillis();
				logger.debug(Logger.EVENT_SUCCESS,	"Total time of advice del sort and merge:" + (end - start) + "ms" );
				// deliveryItemList.forEach(System.out::println);				
			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from API or result is empty"));
			}
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("***fetchDeliveryItemList deliveryItemList size from ctrm:" + deliveryItemList.size()));			
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in deliverylist api: "+httpClientErrorException),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC002", "HttpClientErrorException", "supplierconnect");
			throw new ContractDataException(errMsg1,httpClientErrorException);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in deliverylist api:"+resourceAccessException),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC001", "ResourceAccessException", "supplierconnect");			
			throw new ResourceAccessException(errMsg1+":"+resourceAccessException);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC003", "Exception", "supplierconnect");	
			throw new ContractDataException(errMsg1, e);
		}
		return response;
	}

	/**
	 * @return
	 */
	public String fetchAssayList(Object input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);
		String response = null;
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		try {
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CONTRACT_DETAILS_PATH;
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");
			ObjectWriter owr = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String inpJson = owr.writeValueAsString(input);
			JSONObject inpJsonObj = new JSONObject(inpJson);
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("pcdiId", inpJsonObj.get("pcdiId"));
			HttpEntity<Object> entity = new HttpEntity<Object>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);
			restTemplate.setMessageConverters(messageConverters);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("***fetchAssayList resttemplate URI" + uri+ " input pcdid : "+inp.get("pcdiId")));			
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);

			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from API or result is empty"));
				arrayToJson = new String();
			}
			JSONObject quality = new JSONObject(arrayToJson);
			List<Map<String, Object>> mapList = new Gson().fromJson(quality.get("productDefinitions").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			List<Map<String, Object>> assayingRulesList = new Gson().fromJson(quality.get("assayingRules").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());

			JSONObject attributeJson = new JSONObject();
			JSONArray attributeArray = new JSONArray();
			Map<String, Map<String, String>> assayRuleMap = new HashMap<String, Map<String, String>>();
			Map<String, String> elementAssayRuleMap = new HashMap<String, String>();
			String qualityId = inpJsonObj.get("qualityId").toString();			
			String assayRule = null;
			String finalAssayBasisId = null;
			if (assayingRulesList != null) {
				Map<String, Object> assyRule = null;
				Iterator<Map<String, Object>> assayRuleIt = assayingRulesList.iterator();
				while (assayRuleIt.hasNext()) {
					assyRule = (Map<String, Object>) assayRuleIt.next();					
					if (assyRule.containsKey("qualityTemplateIdList")) {
						ObjectWriter obw = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String assayJson = obw.writeValueAsString(assyRule);
						JSONObject assayObj = new JSONObject(assayJson);
						List<String> assayQltyGson = new Gson().fromJson(
								assayObj.get("qualityTemplateIdList").toString(), new TypeToken<List<String>>() {
								}.getType());
						finalAssayBasisId = assyRule.get("finalAssayBasisId") == null ? ""
								: assyRule.get("finalAssayBasisId").toString();
						assayRule = finalAssayBasisId.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
								: (finalAssayBasisId.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay"
										: finalAssayBasisId);
						elementAssayRuleMap.put(assyRule.get("elementId").toString(), assayRule);
						if (assayQltyGson != null && assayQltyGson.contains(qualityId)) {
							assayRuleMap.put(qualityId, elementAssayRuleMap);
						}else {
							elementAssayRuleMap.remove(assyRule.get("elementId").toString());
						}
					}
				}
				if (assayRuleMap.size() == 0) {
					String errMsg1 = commonService.getErrorMessage("SC008", "QualityId not available", "supplierconnect");
					throw new ContractDataException(errMsg1);
				}
			}
			if (mapList != null) {
				Map<String, Object> obj = null;
				Iterator<Map<String, Object>> it = mapList.iterator();
				while (it.hasNext()) {
					obj = (Map<String, Object>) it.next();
					if (obj.containsKey("qualityAndChemicalAtributesMap")) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(obj);
						JSONObject q = new JSONObject(json);
						Map<String, List<Map<String, Object>>> attributeMap = new Gson().fromJson(
								q.get("qualityAndChemicalAtributesMap").toString(),
								new TypeToken<Map<String, List<Map<String, Object>>>>() {
								}.getType());
						if (attributeMap != null) {
							Iterator<Map.Entry<String, List<Map<String, Object>>>> itr = attributeMap.entrySet()
									.iterator();
							while (itr.hasNext()) {
								Map.Entry<String, List<Map<String, Object>>> attrEntry = itr.next();
								if (attrEntry.getKey().equalsIgnoreCase(qualityId)) {
									List<Map<String, Object>> attrList = attrEntry.getValue();
									Map eachObj = null;
									String elementName = null;
									String elementId = null;
									String typical = null;
									String ratioName = null;
									String minValue = null;
									String maxValue = null;
									String showInSupplierConnect = null;
									Map<String, String> elemAssyRuleMap = assayRuleMap.get(qualityId);
									for (Map<String, Object> attr : attrList) {
										elementName = attr.get("elementName") == null ? ""
												: attr.get("elementName").toString();
										elementId = attr.get("elementId") == null ? ""
												: attr.get("elementId").toString();
										typical = attr.get("typical") == null ? "" : attr.get("typical").toString();
										ratioName = attr.get("ratioName") == null ? ""
												: attr.get("ratioName").toString();
										maxValue = attr.get("maxValue") == null ? "" : attr.get("maxValue").toString();
										minValue = attr.get("minValue") == null ? "" : attr.get("minValue").toString();
										showInSupplierConnect = attr.get("showInSupplierConnect") == null ? "" : attr.get("showInSupplierConnect").toString();
										eachObj = new HashMap();
										eachObj.put("elementName", elementName);
										eachObj.put("elementId", elementId);
										eachObj.put("typical", typical);
										eachObj.put("ratioName", ratioName);
										eachObj.put("minValue", minValue);
										eachObj.put("maxValue", maxValue);
										eachObj.put("showInSupplierConnect", showInSupplierConnect);
										setAssayValues(eachObj, attr);
										//if element has not assayrule store empty value
										if (elemAssyRuleMap != null) {
											eachObj.put("assayRule", elemAssyRuleMap.get(elementId)==null?"":elemAssyRuleMap.get(elementId));
										}
										attributeArray.put(eachObj);
									}
								}
							}
							// attributeMap.get("QAT-321").get(0).get("elementName");
						}
					}
				}
			}
			attributeJson.put(DATA, attributeArray);
			response = attributeJson.toString();
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("***fetchAssayList response " + response));
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("httpClientErrorException in fetchAssayList method"),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC009", "httpClientErrorException inside method fetchAssayList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("resourceAccessException in fetchAssayList method"),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC010", "resourceAccessException inside method fetchAssayList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC011", "Assay List API Call Failed.", "supplierconnect");			
			throw new ConnectException(errMsg1+e.getLocalizedMessage());
			//throw new ContractDataException(errMsg1, e);
		} // different exceptions need to be identified and handled in future code changes
		return response;
	}

	/**
	 * @param payload
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object createGmr(String input, HttpServletRequest req, String tenantID) {
		String response = new String();
		tenantID=validator.cleanData(tenantID);
		JSONObject responseObj = new JSONObject();
		try {
			long start1 = System.currentTimeMillis(); 
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CREATE_GMR_PATH;	
			long end1 = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of connect prop call in create gmr:" + (end1 - start1) + "ms" );
			ResponseEntity<Object> result = null;			
			// ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// String json = ow.writeValueAsString(input);
			List<String> idList = new ArrayList<String>();
			start1 = System.currentTimeMillis(); 
			Map<String,Object> paylodMap = createPayload(input, idList, req);
			end1 = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of creategmr payload Call:" + (end1 - start1) + "ms" );
			String payload = paylodMap.containsKey(GMR_PAYLOAD)? paylodMap.get(GMR_PAYLOAD).toString():"";
			logger.info(Logger.EVENT_SUCCESS,"Create GMR payload:"+payload);
			Map<String,String> emailMap = paylodMap.containsKey(EMAIL_DATA)? (Map<String,String>)paylodMap.get(EMAIL_DATA):new HashMap<String,String>();
			if(StringUtils.isEmpty(payload)) {
				logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Create GMR payload is null or empty"));
//				String errMsg = commonService.getErrorMessage("SC025", "Create GMR payload is null or empty", "supplierconnect");
				throw new ContractDataException(commonService.getErrorMessage("SC025", "Create GMR payload is null or empty", "supplierconnect"));
			}
			System.out.println("Payload:" + payload);
			HttpHeaders headers = getHttpHeader(req);
			headers.add("Content-Type", "application/json;charset=UTF-8");
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");			
			HttpEntity<Object> entity = new HttpEntity<Object>(payload, headers);
			long start = System.currentTimeMillis(); 
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of TRM create gmr API Call:" + (end - start) + "ms" );
			if (result != null) {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("response entity:"+ result.toString()));
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("result.getBody():" + result.getBody()));				
				Object obj = result.getBody();				
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String json = ow.writeValueAsString(obj);
					logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("json response from ctrm create gmr api:" + json));
					JSONObject respObj = new JSONObject(json);
					logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("respObj.toString() from ctrm create gmr api:" + respObj.toString()));				
					responseObj.put("gmrRefNo", respObj.optString("gmrRefNo"));				
					Map<String, Object> inputData = new HashMap<String, Object>();
					inputData.put(RESPONSE, result.getBody());
					inputData.put(GMRREFNO, respObj.optString("gmrRefNo"));	
					start = System.currentTimeMillis();
					updateResponse(idList.get(0), inputData, tenantID);
					end = System.currentTimeMillis();
					logger.debug(Logger.EVENT_SUCCESS,	"Total time of create gmr mongo update:" + (end - start) + "ms" );
					response = result.getBody().toString();
					logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("response:" + response));	
					logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("Sending email on GMR creation."));
					emailMap.put(GMRREFNO, respObj.optString("gmrRefNo").toString());
					emailMap.put(GMR_CREATED_DATE, respObj.opt("createdDate").toString().substring(0, 11));
					emailMap.put(SUBJECT, "gmr_create_subject");
					emailMap.put(MAIL_BODY, "gmr_create_mailbody");
					emailMap.put(MAIL_FROM_ADDR, MAIL_FROM_ADDR);
					/*
					 * Removed send email as part of gmr creation process for jira SC-3235
					 */
					//prepareGMREmail(emailMap,req);
				}catch(Exception e) {
					logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Error while processing response json from ctrm"));				
				}
				start = System.currentTimeMillis();
				refreshElasticOnGmrOp(headers,req);
				end = System.currentTimeMillis();
				logger.debug(Logger.EVENT_SUCCESS,	"Total time of refreshElasticOnGmrOp method call:" + (end - start) + "ms" );
				return result.getBody();
			} else {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from create gmr API or result is empty"));
			}

		} catch (HttpStatusCodeException ex) {
			logger.error(Logger.EVENT_FAILURE,"HttpStatusCodeException inside createGmr() -> "+ex.getRawStatusCode() + "" + ex.getResponseBodyAsString() + ex.getResponseHeaders()+":",ex);
			JSONObject exceptionJson = null;
			String errMsgs = null;
			try {
				exceptionJson= new JSONObject(ex.getResponseBodyAsString());
				JSONArray errList = exceptionJson.optJSONArray("ErrorList: ");
				if(!StringUtils.isEmpty(errList) && errList.length() >0) {
					errMsgs = errList.getString(0);
				}
			}catch(JSONException je) {
				logger.error(Logger.EVENT_FAILURE,"error while parsing exception msg to json ",je);
			}
			String errcodemsg = commonService.getErrorMessage("SC006", "GMR Creation Failed.", "supplierconnect");	
			String errMsg = "GMR Creation Failed.";						
			if(!StringUtils.isEmpty(errMsgs)) {
				errMsg = errMsg +"-"+ errMsgs;				
			}					
			throw new ConnectException(errMsg,ex,errcodemsg,"SC006");
		} catch (ResourceAccessException e) {
			logger.error(Logger.EVENT_FAILURE,"resourceAccessException inside createGmr()",e);				
			String errMsg = commonService.getErrorMessage("121", "ResourceAccessException", "connect");
			throw new ConnectException(errMsg,e,e.getLocalizedMessage(),"SC006");			
		} catch (Exception e) {		
			logger.error(Logger.EVENT_FAILURE,"Exception inside createGmr()->",e);
			String errcodemsg = commonService.getErrorMessage("SC006", "GMR Creation Failed.", "supplierconnect");			
			throw new ConnectException(errcodemsg,e,e.getLocalizedMessage(),"SC006");
		}// different exceptions need to be identified and handled in future code changes
		logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("response:" + response));
		return responseObj;

	}

	/**
	 * * param id
	 * 
	 * @param data
	 * @param objName
	 * @param appName
	 * @return
	 * 
	 */
	private Object updateResponse(String id, Map<String, Object> data, String tenantId) {
		Query query = new Query(Criteria.where("_id").is(id));
		data.put("sys__updatedOn", mongoTemplate.getConverter().convertToMongoType(LocalDateTime.now()));
		Update update = new Update();
		Iterator<Entry<String, Object>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			update.set((String) pair.getKey(), pair.getValue());
		}
		mongoTemplate.updateFirst(query, update, resovleCollectionName(validator.cleanData(tenantId)));
		return id;
	}

	public HttpHeaders getHttpHeader(HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();

		Enumeration<?> names = request.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, request.getHeader(name));
		}

		// addUserNameToHeaders(headers);

		return headers;

	}

	/**
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> fetchDataList(List<Map<String, String>> input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		Map<String, Object> responseEntities = new LinkedHashMap<String, Object>();
		try {
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CONTRACT_DETAILS_PATH;
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");
			// ObjectWriter owr = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// String inpJson = owr.writeValueAsString(input.get(0).get("pcdiId"));
			String inpJson = input.get(0).get("pcdiId");
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("pcdiId", Integer.parseInt(inpJson));
			HttpEntity<Map> entity = new HttpEntity<Map>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);				
			restTemplate.setMessageConverters(messageConverters);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchDataList URI:"+uri+ " input pcdid : "+inpJson));		
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchEntityList:No Response from API or result is empty"));
				arrayToJson = new String();
			}
			List<String> entites = new ArrayList<String>();
			for (Map<String, String> entVal : input) {
				entites.add(entVal.get("entity").toString());
			}

			JSONObject quality = new JSONObject(arrayToJson);
			Map<String, Object> contractMap = new Gson().fromJson(quality.toString(),
					new TypeToken<Map<String, Object>>() {
					}.getType());
			List<Map<String, Object>> mapList = new Gson().fromJson(quality.get("productDefinitions").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());

			JSONObject qualityJson = new JSONObject();
			JSONObject qtyUnitJson = new JSONObject();
			List<Object> qualityArr = new ArrayList<Object>();
			List<Object> qtyUnitArr = new ArrayList<Object>();
			String supplierId = "";
			String supplierName = "";
			if (contractMap != null) {
				Iterator<Map.Entry<String, Object>> contractIt = contractMap.entrySet().iterator();
				while (contractIt.hasNext()) {
					Map.Entry<String, Object> contractEntry = contractIt.next();
					if (contractEntry.getKey().equalsIgnoreCase("cpId")) {
						supplierId = contractEntry.getValue().toString();
					}
					if (contractEntry.getKey().equalsIgnoreCase("cpName")) {
						supplierName = contractEntry.getValue().toString();
					}
				}
			}
			if (mapList != null) {
				Map<String, Object> obj = null;
				Iterator<Map<String, Object>> it = mapList.iterator();
				while (it.hasNext()) {
					obj = (Map<String, Object>) it.next();
					String qtyUnitId = "";
					String qtyUnitName = "";
					String maxLotSize = "";
					String maxParticleSize = "";

					Map<String, Object> qtyUnitObj = new LinkedHashMap<>();
					if (obj.containsKey("qtyUnitId")) {
						qtyUnitId = obj.get("qtyUnitId") == null || obj.get("qtyUnitId").toString().equals("") ? ""
								: obj.get("qtyUnitId").toString();
						qtyUnitName = obj.get("qtyUnitName") == null || obj.get("qtyUnitName").toString().equals("")
								? ""
								: obj.get("qtyUnitName").toString();
						qtyUnitObj.put("key", obj.get("qtyUnitId"));
						qtyUnitObj.put("value", obj.get("qtyUnitName"));
						Map<String, Object> qtyUnitExtJson = new LinkedHashMap<>();
						qtyUnitObj.put("extJson", qtyUnitExtJson.toString());
						qtyUnitArr.add(qtyUnitObj);
					}
					qtyUnitJson.put("qtyUnitId", qtyUnitArr);
					Map<String, List<String>> particleAndLotMap = new LinkedHashMap<String, List<String>>();
					if (obj.containsKey("qualityAndPhysicalAtributesMap")) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(obj);
						JSONObject q = new JSONObject(json);
						Map<String, List<Map<String, Object>>> attributeMap = new Gson().fromJson(
								q.get("qualityAndPhysicalAtributesMap").toString(),
								new TypeToken<Map<String, List<Map<String, Object>>>>() {
								}.getType());
						if (attributeMap != null) {
							Iterator<Map.Entry<String, List<Map<String, Object>>>> itr = attributeMap.entrySet()
									.iterator();
							while (itr.hasNext()) {
								Map.Entry<String, List<Map<String, Object>>> attrEntry = itr.next();
								List<Map<String, Object>> attrList = attrEntry.getValue();
								for (Map<String, Object> attr : attrList) {
									// System.out.println(attr.get("attributeName"));
									List<String> sizeList = new ArrayList<String>();
									if (attr.get("attributeName").toString().equalsIgnoreCase("Maximum Lot Size")) {
										maxLotSize = attr.get("attributeValue") == null ? ""
												: attr.get("attributeValue").toString();
									}
									if (attr.get("attributeName").toString()
											.equalsIgnoreCase("Maximum Particle size")) {
										maxParticleSize = attr.get("attributeValue") == null ? ""
												: attr.get("attributeValue").toString();
									}
									sizeList.add(0, maxLotSize);
									sizeList.add(1, maxParticleSize);
									particleAndLotMap.put(attrEntry.getKey(), sizeList);
								}
							}
						}
					}

					if (obj.containsKey("productQualities")) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(obj);

						JSONObject q = new JSONObject(json);
						List<Map<String, Object>> qaList = new Gson().fromJson(q.get("productQualities").toString(),
								new TypeToken<List<Map<String, Object>>>() {
								}.getType());
						if (qaList != null) {
							Iterator<Map<String, Object>> qaIt = qaList.iterator();
							Map<String, Object> qaObj = null;
							Map<String, Object> eachObj = null;
							String particleSize = null;
							String lotSize = null;
							while (qaIt.hasNext()) {
								qaObj = (Map<String, Object>) qaIt.next();
								eachObj = new LinkedHashMap<String, Object>();
								eachObj.put("key",
										qaObj.get("qualityTemplateId") == null ? "" : qaObj.get("qualityTemplateId"));
								eachObj.put("value", qaObj.get("qualityName") == null ? "" : qaObj.get("qualityName"));
								List<Map<String, Object>> extJson = new ArrayList();
								Map<String, Object> extMap = new LinkedHashMap<String, Object>();
								if (particleAndLotMap.containsKey(qaObj.get("qualityTemplateId"))) {
									lotSize = particleAndLotMap.get(qaObj.get("qualityTemplateId")).get(0);
									particleSize = particleAndLotMap.get(qaObj.get("qualityTemplateId")).get(1);
								} else {
									particleSize = "";
									lotSize = "";
								}
								extMap.put("key", "maxLotSize");
								extMap.put("value", lotSize);
								extJson.add(extMap);
								extMap = new LinkedHashMap<String, Object>();
								extMap.put("key", "particleSize");
								extMap.put("value", particleSize);
								extJson.add(extMap);
								extMap = new LinkedHashMap<String, Object>();
								extMap.put("key", "qtyUnitId");
								extMap.put("value", qtyUnitId);
								extJson.add(extMap);
								extMap = new LinkedHashMap<String, Object>();
								extMap.put("key", "qtyUnitName");
								extMap.put("value", qtyUnitName);
								extJson.add(extMap);
								extMap = new LinkedHashMap<String, Object>();
								extMap.put("key", "supplierId");
								extMap.put("value", supplierId);
								extJson.add(extMap);
								extMap = new LinkedHashMap<String, Object>();
								extMap.put("key", "supplierName");
								extMap.put("value", supplierName);
								extJson.add(extMap);

								eachObj.put("extJson", extJson);
								qualityArr.add(eachObj);
							}
						}
					}
				}
			}
			qualityJson.put(QUALITY, qualityArr);
			responseEntities.put(QUALITY, qualityArr);
			// responseEntities.put("qtyUnitId", qtyUnitArr);
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("HttpClientErrorException in fetchDataList:"),httpClientErrorException);
			String errMsg = commonService.getErrorMessage("SC038", "HttpClientErrorException inside method fetchDataList", "supplierconnect");
			throw new ContractDataException(errMsg);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("ResourceAccessException in fetchDataList:"),resourceAccessException);
			String errMsg = commonService.getErrorMessage("SC039", "ResourceAccessException in fetchDataList", "supplierconnect");
			throw new ContractDataException(errMsg);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = commonService.getErrorMessage("SC040", "Fetch Data List API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg+ e.getLocalizedMessage());
		}
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchDataList response:"+responseEntities.toString()));
		return responseEntities;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> fetchEntityList(List<Map<String, String>> input, HttpServletRequest req) {

		HttpHeaders headers = commonService.getHttpHeader(req);
		logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("header in fetchentitylist:"+headers)); 
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		Map<String, Object> responseEntities = new LinkedHashMap<String, Object>();
		try {
			String payload = new Gson().toJson(input);
			logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchEntityList payload:"+payload));
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CONTRACT_DETAILS_PATH;
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");
			// ObjectWriter owr = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// String inpJson = owr.writeValueAsString(input.get(0).get("pcdiId"));
			String inpJson = input.get(0).get("pcdiId");
			String deliveryItem = input.get(0).get("deliveryItem");
			logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("deliveryitem: " + deliveryItem));
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("pcdiId", Integer.parseInt(inpJson));
			HttpEntity<Map> entity = new HttpEntity<Map>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchEntityList URI:"+uri+ " input pcdid : "+inp.get("pcdiId")));
			//restTemplate.setMessageConverters(messageConverters);
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchEntityList:No Response from API or result is empty"));
				arrayToJson = new String();
			}
			List<String> entites = new ArrayList<String>();
			for (Map<String, String> entVal : input) {
				entites.add(entVal.get("entity").toString());
			}

			JSONObject quality = new JSONObject(arrayToJson);
			Map<String, Object> contractMap = new Gson().fromJson(quality.toString(),
					new TypeToken<Map<String, Object>>() {
					}.getType());
			List<Map<String, Object>> mapList = new Gson().fromJson(quality.get("productDefinitions").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			List<Map<String, Object>> deliveryItems = new Gson().fromJson(quality.get("deliveryItems").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());

			JSONObject qualityJson = new JSONObject();
			JSONObject qtyUnitJson = new JSONObject();
			List<Object> qualityArr = new ArrayList<Object>();
			List<Object> qtyUnitArr = new ArrayList<Object>();
			String supplierId = "";
			String supplierName = "";
			if (contractMap != null) {
				Iterator<Map.Entry<String, Object>> contractIt = contractMap.entrySet().iterator();
				while (contractIt.hasNext()) {
					Map.Entry<String, Object> contractEntry = contractIt.next();
					if (contractEntry.getKey().equalsIgnoreCase("cpId")) {
						supplierId = contractEntry.getValue().toString();
					}
					if (contractEntry.getKey().equalsIgnoreCase("cpName")) {
						supplierName = contractEntry.getValue().toString();
					}
				}
			}
			if (mapList != null) {
				Map<String, Object> obj = null;
				Iterator<Map<String, Object>> it = mapList.iterator();
				while (it.hasNext()) {
					obj = (Map<String, Object>) it.next();
					String qtyUnitId = "";
					String qtyUnitName = "";
					String maxLotSize = "";
					String maxParticleSize = "";

					Map<String, Object> qtyUnitObj = new LinkedHashMap<>();
					if (obj.containsKey("qtyUnitId")) {
						qtyUnitId = obj.get("qtyUnitId") == null || obj.get("qtyUnitId").toString().equals("") ? ""
								: obj.get("qtyUnitId").toString();
						qtyUnitName = obj.get("qtyUnitName") == null || obj.get("qtyUnitName").toString().equals("")
								? ""
								: obj.get("qtyUnitName").toString();
						qtyUnitObj.put("key", obj.get("qtyUnitId"));
						qtyUnitObj.put("value", obj.get("qtyUnitName"));
						Map<String, Object> qtyUnitExtJson = new LinkedHashMap<>();
						qtyUnitObj.put("extJson", qtyUnitExtJson.toString());
						qtyUnitArr.add(qtyUnitObj);
					}
					qtyUnitJson.put("qtyUnitId", qtyUnitArr);
					Map<String, List<String>> particleAndLotMap = new LinkedHashMap<String, List<String>>();
					if (obj.containsKey("qualityAndPhysicalAtributesMap")) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(obj);
						JSONObject q = new JSONObject(json);
						Map<String, List<Map<String, Object>>> attributeMap = new Gson().fromJson(
								q.get("qualityAndPhysicalAtributesMap").toString(),
								new TypeToken<Map<String, List<Map<String, Object>>>>() {
								}.getType());
						if (attributeMap != null) {
							Iterator<Map.Entry<String, List<Map<String, Object>>>> itr = attributeMap.entrySet()
									.iterator();
							while (itr.hasNext()) {
								Map.Entry<String, List<Map<String, Object>>> attrEntry = itr.next();
								List<Map<String, Object>> attrList = attrEntry.getValue();
								for (Map<String, Object> attr : attrList) {
									// System.out.println(attr.get("attributeName"));
									List<String> sizeList = new ArrayList<String>();
									if (attr.get("attributeName").toString().equalsIgnoreCase("Maximum Lot Size")) {
										maxLotSize = attr.get("attributeValue") == null ? ""
												: attr.get("attributeValue").toString();
									}
									if (attr.get("attributeName").toString()
											.equalsIgnoreCase("Maximum Particle size")) {
										maxParticleSize = attr.get("attributeValue") == null ? ""
												: attr.get("attributeValue").toString();
									}
									sizeList.add(0, maxLotSize);
									sizeList.add(1, maxParticleSize);
									particleAndLotMap.put(attrEntry.getKey(), sizeList);
								}
							}
						}
					}

					if (obj.containsKey("productQualities")) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(obj);

						JSONObject q = new JSONObject(json);
						List<Map<String, Object>> qaList = new Gson().fromJson(q.get("productQualities").toString(),
								new TypeToken<List<Map<String, Object>>>() {
								}.getType());
						if (qaList != null) {
							Iterator<Map<String, Object>> qaIt = qaList.iterator();
							Map<String, Object> qaObj = null;
							Map<String, Object> eachObj = null;
							String particleSize = null;
							List<String> qualityList = new ArrayList<String>();
							String qualName = "";
							String qualId = "";
							String lotSize = null;
							if (deliveryItems != null) {
								Map<String, Object> deliveryObj = null;
								Iterator<Map<String, Object>> deliveryIt = deliveryItems.iterator();
								while (deliveryIt.hasNext()) {
									deliveryObj = (Map<String, Object>) deliveryIt.next();
									String qltyName = "";									
									Double x = (Double) deliveryObj.get("deliveryItemNo");
									Integer delItemNo = x.intValue();
									logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("before substring: " + deliveryItem));
									deliveryItem = deliveryItem.substring(deliveryItem.lastIndexOf("-") + 1);
									if (deliveryItem.equals(delItemNo.toString())) {
										qltyName = deliveryObj.get("selectedQualityName").toString();
										if (qltyName.contains(",")) {
											qualityList.addAll(Arrays.asList(qltyName.split(",")));
										} else
											qualityList.add(qltyName);
									}
								}
							}
							while (qaIt.hasNext()) {
								qaObj = (Map<String, Object>) qaIt.next();
								eachObj = new LinkedHashMap<String, Object>();
								qualName = qaObj.get("qualityName") == null ? "" : qaObj.get("qualityName").toString();
								if (qualityList.contains(qualName)) {
									qualId = qaObj.get("qualityTemplateId") == null ? ""
											: qaObj.get("qualityTemplateId").toString();
									eachObj.put("key", qualId);
									eachObj.put("value", qualName);

									List<Map<String, Object>> extJson = new ArrayList<Map<String, Object>>();
									Map<String, Object> extMap = new LinkedHashMap<String, Object>();
									if (particleAndLotMap.containsKey(qaObj.get("qualityTemplateId"))) {
										lotSize = particleAndLotMap.get(qaObj.get("qualityTemplateId")).get(0);
										particleSize = particleAndLotMap.get(qaObj.get("qualityTemplateId")).get(1);
									} else {
										particleSize = "";
										lotSize = "";
									}
									extMap.put("key", "maxLotSize");
									extMap.put("value", lotSize);
									extJson.add(extMap);
									extMap = new LinkedHashMap<String, Object>();
									extMap.put("key", "particleSize");
									extMap.put("value", particleSize);
									extJson.add(extMap);
									extMap = new LinkedHashMap<String, Object>();
									extMap.put("key", "qtyUnitId");
									extMap.put("value", qtyUnitId);
									extJson.add(extMap);
									extMap = new LinkedHashMap<String, Object>();
									extMap.put("key", "qtyUnitName");
									extMap.put("value", qtyUnitName);
									extJson.add(extMap);
									extMap = new LinkedHashMap<String, Object>();
									extMap.put("key", "supplierId");
									extMap.put("value", supplierId);
									extJson.add(extMap);
									extMap = new LinkedHashMap<String, Object>();
									extMap.put("key", "supplierName");
									extMap.put("value", supplierName);
									extJson.add(extMap);

									eachObj.put("extJson", extJson);
									qualityArr.add(eachObj);
								}
							}
						}
					}
				}
			}
			qualityJson.put(QUALITY, qualityArr);
			responseEntities.put(QUALITY, qualityArr);
			// responseEntities.put("qtyUnitId", qtyUnitArr);
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchEntityList: "),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC016", "HttpClientErrorException inside method fetchEntityList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchEntityList: "),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC017", "ResourceAccessException in fetchEntityList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC018", "Entity List API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg1+ e.getLocalizedMessage());
		} // different exceptions need to be identified and handled in future code changes
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchEntityList response:"+responseEntities.toString()));
		return responseEntities;
	}

	/**
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> fetchIncoTermList(List<Map<String, String>> input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		Map<String, Object> responseEntities = new LinkedHashMap<String, Object>();
		try {
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CONTRACT_DETAILS_PATH;
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");
			String inpJson = input.get(0).get("pcdiId");
			String deliveryItem = input.get(0).get("deliveryItem");
			logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("deliveryitem: " + deliveryItem));
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("pcdiId", Integer.parseInt(inpJson));
			HttpEntity<Map> entity = new HttpEntity<Map>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);						
			restTemplate.setMessageConverters(messageConverters);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchIncoTermList uri:"+uri+ " input pcdid : "+inp.get("pcdiId")));
			long start = System.currentTimeMillis();
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of trm API Call for fetchIncoTermList:" + (end - start) + "ms" );
			start = System.currentTimeMillis();
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchIncoTermList method: No Response from API or result is empty"));
				arrayToJson = new String();
			}
			List<String> entites = new ArrayList<String>();
			for (Map<String, String> entVal : input) {
				entites.add(entVal.get("entity").toString());
			}
			JSONObject quality = new JSONObject(arrayToJson);
			List<Object> incoPcbIdList = new ArrayList<Object>();
			List<Map<String, Object>> mapList = new Gson().fromJson(quality.get("deliveryBasis").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			List<Map<String, Object>> deliveryItems = new Gson().fromJson(quality.get("deliveryItems").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			if (deliveryItems != null) {
				Map<String, Object> deliveryObj = null;
				Iterator<Map<String, Object>> deliveryIt = deliveryItems.iterator();
				while (deliveryIt.hasNext()) {
					deliveryObj = (Map<String, Object>) deliveryIt.next();
					Double x = (Double) deliveryObj.get("deliveryItemNo");
					Integer delItemNo = x.intValue();
					logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("before substring: " + deliveryItem));
					deliveryItem = deliveryItem.substring(deliveryItem.lastIndexOf("-") + 1);
					if (deliveryItem.equals(delItemNo.toString())) {
						incoPcbIdList = (List<Object>) (deliveryObj.get("selectedIncoTermLocations"));
					}

				}
			}
			List<Object> incotermList = new ArrayList<Object>();
			if (mapList != null) {
				Map<String, Object> obj = null;
				Iterator<Map<String, Object>> it = mapList.iterator();
				while (it.hasNext()) {
					obj = (Map<String, Object>) it.next();
					Map<String, Object> incoObj = new LinkedHashMap<>();
					if (incoPcbIdList.contains(obj.get("pcdbId"))) {
						if (obj.containsKey("incoTermId")) {
							incoObj.put("key",
									obj.get("incoTermId") == null || obj.get("incoTermId").toString().equals("") ? ""
											: obj.get("incoTermId"));
							incoObj.put("value",
									obj.get("incoTermName") == null || obj.get("incoTermName").toString().equals("")
											? ""
											: obj.get("incoTermName"));
							incoObj.put("incoLocation",
									obj.get("incoLocation") == null || obj.get("incoLocation").toString().equals("")
											? ""
											: obj.get("incoLocation").toString());
							incoObj.put("countryId",
									obj.get("countryId") == null || obj.get("countryId").toString().equals("") ? ""
											: obj.get("countryId").toString());
							incoObj.put("stateId",
									obj.get("stateId") == null || obj.get("stateId").toString().equals("") ? ""
											: obj.get("stateId").toString());
							incoObj.put("cityId",
									obj.get("cityId") == null || obj.get("cityId").toString().equals("") ? ""
											: obj.get("cityId").toString());
							incoObj.put("countryName",
									obj.get("countryName") == null || obj.get("countryName").toString().equals("") ? ""
											: obj.get("countryName").toString());
							incoObj.put("stateName",
									obj.get("stateName") == null || obj.get("stateName").toString().equals("") ? ""
											: obj.get("stateName").toString());
							incoObj.put("cityName",
									obj.get("cityName") == null || obj.get("cityName").toString().equals("") ? ""
											: obj.get("cityName").toString());
							List<Map<String, Object>> extJson = new ArrayList();
							Map<String, Object> extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "incoLocation");
							extMap.put("value", incoObj.get("cityName") + ", " + incoObj.get("countryName"));
							extJson.add(extMap);
							extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "dischargeCountryId");
							extMap.put("value", incoObj.get("countryId"));
							extJson.add(extMap);
							extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "dischargeCityId");
							extMap.put("value", incoObj.get("cityId"));
							extJson.add(extMap);
							extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "city");
							extMap.put("value", "City");
							extJson.add(extMap);
							incoObj.put("extJson", extJson);
							incotermList.add(incoObj);
						}
					}
				}
			}
			end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of processing trm response in fetchIncoTermList:" + (end - start) + "ms" );
			responseEntities.put(INCOTERM, incotermList);
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchIncoTermList: "),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC019", "HttpClientErrorException inside method fetchIncoTermList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchIncoTerm method: "),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC020", "ResourceAccessException in fetchIncoTermList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC021", "Inco List API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg1+ e.getLocalizedMessage());
		} // different exceptions need to be identified and handled in future code changes
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchIncoTermList response:"+responseEntities.toString()));
		return responseEntities;
	}

	/**
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> fetchDestinationList(List<Map<String, String>> input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		Map<String, Object> responseEntities = new LinkedHashMap<String, Object>();
		try {
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CONTRACT_DETAILS_PATH;
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");
			String inpJson = input.get(0).get("pcdiId");
			String deliveryItem = input.get(0).get("deliveryItem");
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("pcdiId", Integer.parseInt(inpJson));
			HttpEntity<Map> entity = new HttpEntity<Map>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchDestinationList uri:"+uri + " input pcdid : "+inpJson));			
			//restTemplate.setMessageConverters(messageConverters);
			long start = System.currentTimeMillis(); 
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of trm API Call for fetchDestinationList:" + (end - start) + "ms" );
			start = System.currentTimeMillis(); 
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchDestinationList method: No Response from API or result is empty"));
				arrayToJson = new String();
			}
			List<String> entites = new ArrayList<String>();
			for (Map<String, String> entVal : input) {
				entites.add(entVal.get("entity").toString());
			}
			JSONObject quality = new JSONObject(arrayToJson);
			List<Object> incoPcbIdList = new ArrayList<Object>();
			List<Map<String, Object>> mapList = new Gson().fromJson(quality.get("deliveryBasis").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			List<Map<String, Object>> deliveryItems = new Gson().fromJson(quality.get("deliveryItems").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			if (deliveryItems != null) {
				Map<String, Object> deliveryObj = null;
				Iterator<Map<String, Object>> deliveryIt = deliveryItems.iterator();
				while (deliveryIt.hasNext()) {
					deliveryObj = (Map<String, Object>) deliveryIt.next();
					Double x = (Double) deliveryObj.get("deliveryItemNo");
					Integer delItemNo = x.intValue();
					deliveryItem = deliveryItem.substring(deliveryItem.lastIndexOf("-") + 1);
					if (deliveryItem.equals(delItemNo.toString())) {
						incoPcbIdList = (List<Object>) (deliveryObj.get("selectedIncoTermLocations"));
					}

				}
			}
			List<Object> destinationList = new ArrayList<Object>();
			if (mapList != null) {
				Map<String, Object> obj = null;
				Iterator<Map<String, Object>> it = mapList.iterator();
				String cityCountryValue = null;
				while (it.hasNext()) {
					obj = (Map<String, Object>) it.next();
					Map<String, Object> incoObj = new LinkedHashMap<>();
					if (incoPcbIdList.contains(obj.get("pcdbId"))) {
						if (obj.containsKey("incoTermId")) {
							incoObj.put("incoTermId",
									obj.get("incoTermId") == null || obj.get("incoTermId").toString().equals("") ? ""
											: obj.get("incoTermId").toString());
							incoObj.put("countryId",
									obj.get("countryId") == null || obj.get("countryId").toString().equals("") ? ""
											: obj.get("countryId").toString());
							incoObj.put("stateId",
									obj.get("stateId") == null || obj.get("stateId").toString().equals("") ? ""
											: obj.get("stateId").toString());
							incoObj.put("cityId",
									obj.get("cityId") == null || obj.get("cityId").toString().equals("") ? ""
											: obj.get("cityId").toString());
							incoObj.put("countryName",
									obj.get("countryName") == null || obj.get("countryName").toString().equals("") ? ""
											: obj.get("countryName").toString());
							incoObj.put("stateName",
									obj.get("stateName") == null || obj.get("stateName").toString().equals("") ? ""
											: obj.get("stateName").toString());
							incoObj.put("cityName",
									obj.get("cityName") == null || obj.get("cityName").toString().equals("") ? ""
											: obj.get("cityName").toString());
							cityCountryValue = incoObj.get("cityName") + ", " + incoObj.get("countryName");
							incoObj.put("key",
									obj.get("incoLocation") == null || obj.get("incoLocation").toString().equals("")
											? ""
											: obj.get("incoLocation"));
							incoObj.put("value", cityCountryValue);
							List<Map<String, Object>> extJson = new ArrayList<Map<String, Object>>();
							Map<String, Object> extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "incoTermId");
							extMap.put("value", incoObj.get("incoTermId"));
							extJson.add(extMap);
							extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "dischargeCountryId");
							extMap.put("value", incoObj.get("countryId"));
							extJson.add(extMap);
							extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "dischargeCityId");
							extMap.put("value", incoObj.get("cityId"));
							extJson.add(extMap);
							extMap = new LinkedHashMap<String, Object>();
							extMap.put("key", "city");
							extMap.put("value", "City");
							extJson.add(extMap);
							incoObj.put("extJson", extJson);
							destinationList.add(incoObj);
						}
					}
				}
			}
			responseEntities.put(DESTINATION, destinationList);
			end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time to process trm response in fetchDestinationList:" + (end - start) + "ms" );
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchDestinationLIst: "),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC022", "HttpClientErrorException inside method fetchDestinationList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchDestinationList : "),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC023", "ResourceAccessException in fetchDestinationList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC024", "Destination List API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg1+ e.getLocalizedMessage());
		} // different exceptions need to be identified and handled in future code changes
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchDestinationList response:"+responseEntities.toString()));
		return responseEntities;
	}

	/**
	 * @param deliveryItemList
	 * @return
	 */
	private String mergeDataForList(List<DeliveryItem> deliveryItemList) {
		JSONObject delItemObjJson = new JSONObject();
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("mergeDataForList starts"));
		if (deliveryItemList != null) {
			Iterator<DeliveryItem> it = deliveryItemList.iterator();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
			try {
				DeliveryItem delItem = null;
				JSONArray delItemArr = new JSONArray();
				JSONObject eachObj = null;
				String[] froStr = null;
				String execQty = null;
				String finalInvQty = null;
				String execQtyStr = null;
				String finalQtyStr = null;
				String unit = null;
				String opQty = null;
				String location = null;
				Double opQtyVal = 0.0;
				Double toBeCalledOffQtyVal = 0.0;
				String toBeCalledOffQty = null;
				String[] locArr = null;
				boolean locHasFilterCities = false;
				LocalDate toDte = null;
				LocalDate fromDte = null;
				LocalDate today = LocalDate.now();
				String toDateStr = null;
				String fromDateStr = null;
				boolean dateCheck = false;
				String[] fromDateArr = null;
				String[] toDateArr = null;
				String openQty = null;
				Double openQtyVal = 0.0;
				String executableQty = null;
				Double executableQtyVal = 0.0;
				String deliveryRef = null;
				String netOpenQty = null;
				Double netOpenQtyVal = 0.0;
				logger.info(Logger.EVENT_SUCCESS,"Filter City with special character:"+FILTER_CITY_3.toUpperCase());
				while (it.hasNext()) {
					delItem = (DeliveryItem) it.next();
					opQty = delItem.getQuotaOpenQty();
					toBeCalledOffQty = delItem.getToBeCalledOffQty();
					deliveryRef=delItem.getDeliveryItemRefNo().toString();
					if(deliveryRef.equals("SCT-2190-BLD-1")) {
						logger.info(Logger.EVENT_SUCCESS,delItem.toString());
					};
					location = delItem.getLocation();
					locArr = location.split(",");
					String locStr = null;
					String destLocation = "";
					toBeCalledOffQtyVal = 0.0;
					dateCheck = false;
					openQty = delItem.getOpenQty();
					executableQty = delItem.getExecutableQty();
					netOpenQty = delItem.getNetOpenQty();
					int i = 0;
					if (opQty != null) {
						opQty = opQty.substring(0, opQty.indexOf(" ")).trim();
						opQtyVal = Double.parseDouble(opQty);
					}
					if (toBeCalledOffQty != null && !toBeCalledOffQty.equals("N/A")) {
						toBeCalledOffQty = toBeCalledOffQty.substring(0, toBeCalledOffQty.indexOf(" ")).trim();
						toBeCalledOffQtyVal = Double.parseDouble(toBeCalledOffQty);
					}
					if (openQty != null && !openQty.equals("N/A")) {
						openQty = openQty.substring(0, openQty.indexOf(" ")).trim();
						openQtyVal = Double.parseDouble(openQty);
					}
					if (executableQty != null && !executableQty.equals("N/A")) {
						executableQty = executableQty.substring(0, executableQty.indexOf(" ")).trim();
						executableQtyVal = Double.parseDouble(executableQty);
					}
					if (netOpenQty != null && !netOpenQty.equals("N/A")) {
						netOpenQty = netOpenQty.substring(0, netOpenQty.indexOf(" ")).trim();
						netOpenQtyVal = Double.parseDouble(netOpenQty);
					}
					if (location != null) {
						// location = location.substring(location.lastIndexOf(",")+1);
						while ((i + 3) <= locArr.length) {
							i += 3;
							locStr = locArr[i - 1].toUpperCase();							
							if (locStr.contains(FILTER_CITY_1) || locStr.contains(FILTER_CITY_2)
									|| locStr.contains(FILTER_CITY_3.toUpperCase())) {
								if (i > 3)
									destLocation = destLocation + "," + locArr[i - 1];
								else
									destLocation = destLocation + locArr[i - 1];
								locHasFilterCities = true;
							} else {
								locHasFilterCities = false;
								break;
							}
						}

						if (locHasFilterCities) {
						/*if condition changed as part of jira SC-3329
						 * if (openQtyVal != 0.0 || executableQtyVal != 0.0) {
						 */
						//Added check for netOpen value to be non negative SC-3800	
						if (netOpenQtyVal > 0.0 ) {
							if (null != delItem.getQuotaMonth() && !StringUtils.isEmpty(delItem.getQuotaMonth())) {
								froStr = delItem.getQuotaMonth().split("To");
								delItem.setFromDate(froStr[0].trim());
								if (froStr.length > 1)
									delItem.setToDate(froStr[1].trim());
								else
									delItem.setToDate("");
							}
							fromDateStr = delItem.getFromDate();
							toDateStr = delItem.getToDate();
							fromDateArr = fromDateStr.split(" ");
							if (fromDateArr.length > 1) {
								fromDte = LocalDate.parse(fromDateArr[1] + "-" + fromDateArr[0] + "-01",
										monthFormatter);

							} else {
								fromDte = LocalDate.parse(fromDateStr, formatter);
							}
							delItem.setFromDate(fromDte.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
							if (toDateStr == null || toDateStr.equals("")) {
								delItem.setToDate(fromDateStr);
							}
							toDateStr = delItem.getToDate();
							if (toDateStr != null && !toDateStr.equals("")) {
								toDateArr = toDateStr.split(" ");
								if (toDateArr.length <= 1) {
									toDte = LocalDate.parse(toDateStr, formatter);
								} else {
									toDte = LocalDate.parse(toDateArr[1] + "-" + toDateArr[0] + "-01", monthFormatter);
									toDte = toDte.plusDays(toDte.lengthOfMonth() - 1);
									// System.out.println("toDte:"+toDte+ " condition : "+(toDte.isAfter(today)
									// ||toDte.isEqual(today)));
								}								
								delItem.setToDate(toDte.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
								if (toDte.isAfter(today) || toDte.isEqual(today)) {
									dateCheck = true;
								} else {
									dateCheck = false;
								}
							}else {
								dateCheck = true;
							}
							if (dateCheck) {
								execQty = delItem.getExecutedQty();
								finalInvQty = delItem.getFinalInvoicedQty();
								//Changing openQty to netOpenQty as part of story SC-3329
								//delItem.setOpenQty(delItem.getQuotaOpenQty());
								delItem.setOpenQty(delItem.getNetOpenQty());
								delItem.setDelLocation(destLocation);
								eachObj = new JSONObject();
								setObjectValue(eachObj, delItem);
								delItemArr.put(eachObj);
							}
						}
					 }
					}
				}
				delItemObjJson.put(DATA, delItemArr);
				delItemObjJson.put(TOTAL_COUNT, !StringUtils.isEmpty(delItemArr)?delItemArr.length():0);
			} catch (Exception e) {
				logger.debug(Logger.EVENT_SUCCESS,"error in preparing response",e);				
			}
			logger.debug(Logger.EVENT_SUCCESS, "deliveryItem size after filtering:"+(delItemObjJson.has(TOTAL_COUNT)?delItemObjJson.get(TOTAL_COUNT):0));
		}
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("mergeDataForList ends"));
		return delItemObjJson.toString();
	}

	/**
	 * @param eachObj
	 * @param delItem
	 */
	private void setObjectValue(JSONObject eachObj, DeliveryItem delItem) {		
		try {
			eachObj.put(TAB_REQUEST_ID, delItem.getTabRequestId() == null ? "" : delItem.getTabRequestId());
			eachObj.put(ENTITY_VERSION_MAP, delItem.getEntityVersionMap() == null ? "" : delItem.getEntityVersionMap());
			eachObj.put(TOTAL_NO_OF_RECORDS,
					delItem.getTotalNoOfRecords() == null ? "" : delItem.getTotalNoOfRecords());
			eachObj.put(LISTING_CREATED_DATE,
					delItem.getListingCreatedDate() == null ? "" : delItem.getListingCreatedDate());
			eachObj.put(LISTING_CREATED_BY, delItem.getLisitngCreatedBy() == null ? "" : delItem.getLisitngCreatedBy());
			eachObj.put(LISTING_UPDATED_DATE,
					delItem.getLisitingUpdatedDate() == null ? "" : delItem.getLisitingUpdatedDate());
			eachObj.put(LISTING_UPDATED_BY, delItem.getListingUpdatedBy() == null ? "" : delItem.getListingUpdatedBy());
			eachObj.put(ISSUE_DATE, delItem.getIssueDate() == null ? "" : delItem.getIssueDate());
			eachObj.put(CP_NAME, delItem.getCpName() == null ? "" : delItem.getCpName());
			eachObj.put(CONTRACT_REF_NO, delItem.getContractRefNo() == null ? "" : delItem.getContractRefNo());
			eachObj.put(CONTRACT_TYPE, delItem.getContractType() == null ? "" : delItem.getContractType());
			eachObj.put(PRODUCT_GROUP_TYPE, delItem.getProductGroupType() == null ? "" : delItem.getProductGroupType());
			eachObj.put(ASSET_CLASS, delItem.getAssetclass() == null ? "" : delItem.getAssetclass());
			eachObj.put(CONTRACT_QTY, delItem.getContractqty() == null ? "" : delItem.getContractqty());
			eachObj.put(CONTRACT_STATUS, delItem.getContractStatus() == null ? "" : delItem.getContractStatus());
			eachObj.put(DELIVERY_ITEM_REF_NO,
					delItem.getDeliveryItemRefNo() == null ? "" : delItem.getDeliveryItemRefNo());
			eachObj.put(QUALITY_NAME, delItem.getQualityName() == null ? "" : delItem.getQualityName());
			eachObj.put(QUOTA_MONTH, delItem.getQuotaMonth() == null ? "" : delItem.getQuotaMonth());
			eachObj.put(TOLLING_SERVICE_TYPE,
					delItem.getTollingServiceType() == null ? "" : delItem.getTollingServiceType());
			eachObj.put(TRADE_TYPE, delItem.getTradeType() == null ? "" : delItem.getTradeType());
			eachObj.put(ITEM_STATUS, delItem.getItemStatus() == null ? "" : delItem.getItemStatus());
			eachObj.put(ATTRIBUTES, "Details");
			eachObj.put(LOCATION, delItem.getLocation() == null ? "" : delItem.getLocation());
			eachObj.put(TRAXYS_ORG, delItem.getTraxysOrg() == null ? "" : delItem.getTraxysOrg());
			eachObj.put(INCO_TERM_LOCATION, delItem.getIncotermLocation() == null ? "" : delItem.getIncotermLocation());
			eachObj.put(QP, delItem.getQp() == null ? "" : delItem.getQp());
			eachObj.put(QUOTA_QTY, delItem.getQuotaQty() == null ? "" : delItem.getQuotaQty());
			eachObj.put(QUOTA_QTY_UNIT, delItem.getQuotaQtyUnit() == null ? "" : delItem.getQuotaQtyUnit());
			eachObj.put(QUOTA_QTY_BASIS, delItem.getQuotaQtyBasis() == null ? "" : delItem.getQuotaQtyBasis());
			eachObj.put(QUOTA_OPEN_QTY, delItem.getQuotaOpenQty() == null ? "" : delItem.getQuotaOpenQty());
			eachObj.put(QUOTA_CALL_OFF_QTY, delItem.getQuotaCalloffQty() == null ? "" : delItem.getQuotaCalloffQty());
			eachObj.put(QUOTA_DELIVERED_RECEIVED_QTY,
					delItem.getQuotaDeliveredReceivedQty() == null ? "" : delItem.getQuotaDeliveredReceivedQty());
			eachObj.put(QUOTA_INVOICED_QTY, delItem.getQuotaInvoicedQty() == null ? "" : delItem.getQuotaInvoicedQty());
			eachObj.put(QUOTA_PRICE_FIXED_QTY,
					delItem.getQuotaPriceFixedQty() == null ? "" : delItem.getQuotaPriceFixedQty());
			eachObj.put(ALLOCATED_QTY, delItem.getAllocatedQty() == null ? "" : delItem.getAllocatedQty());
			eachObj.put(PCDIID, delItem.getPcdiId() == null ? "" : delItem.getPcdiId());
			eachObj.put(PRICE_OPT_CALL_OFF_STATUS,
					delItem.getPriceOptionCallOffStatus() == null ? "" : delItem.getPriceOptionCallOffStatus());
			eachObj.put(PHYSICAL_OPT_PRESENT,
					delItem.getPhysicalOptPresent() == null ? "" : delItem.getPhysicalOptPresent());
			eachObj.put(STRATEGY, delItem.getStrategy() == null ? "" : delItem.getStrategy());
			eachObj.put(BOOK_PROFIT_CENTER, delItem.getBookProfitCenter() == null ? "" : delItem.getBookProfitCenter());
			eachObj.put(TRADER, delItem.getTrader() == null ? "" : delItem.getTrader());
			eachObj.put(QP_PRICING, delItem.getQpPricing() == null ? "" : delItem.getQpPricing());
			eachObj.put(QP_PRICING_BASIS, delItem.getQpPricingBasis() == null ? "" : delItem.getQpPricingBasis());
			eachObj.put(PRICING, delItem.getPricing() == null ? "" : delItem.getPricing());
			eachObj.put(QUOTA_QTY_MAX, delItem.getQuotaQuantityMax() == null ? "" : delItem.getQuotaQuantityMax());
			eachObj.put(QUOTA_QUANTITY_BASIS,
					delItem.getQuotaQuantityBasis() == null ? "" : delItem.getQuotaQuantityBasis());
			eachObj.put(TO_BE_CALLED_OFF_QTY,
					delItem.getToBeCalledOffQty() == null ? "" : delItem.getToBeCalledOffQty());
			eachObj.put(CALLED_OFF_QTY, delItem.getCalledOffQty() == null ? "" : delItem.getCalledOffQty());
			eachObj.put(EXECUTED_QTY, delItem.getExecutedQty() == null ? "" : delItem.getExecutedQty());
			eachObj.put(PRICING_STATUS, delItem.getPricingStatus() == null ? "" : delItem.getPricingStatus());
			eachObj.put(FIXED_PRICE_QTY, delItem.getFixedPriceQty() == null ? "" : delItem.getFixedPriceQty());
			eachObj.put(TITLE_TRANSFER_QTY, delItem.getTitleTransferQty() == null ? "" : delItem.getTitleTransferQty());
			eachObj.put(PROV_INVOICED_QTY, delItem.getProvInvoicedQty() == null ? "" : delItem.getProvInvoicedQty());
			eachObj.put(FINAL_INVOICED_QTY, delItem.getFinalInvoicedQty() == null ? "" : delItem.getFinalInvoicedQty());
			eachObj.put(PAY_IN_CURRENCY, delItem.getPayInCurrency() == null ? "" : delItem.getPayInCurrency());
			eachObj.put(FULLFILLMENT_STATUS,
					delItem.getFullfillmentStatus() == null ? "" : delItem.getFullfillmentStatus());
			eachObj.put(PASS_THROUGH, delItem.getPassThrough() == null ? "" : delItem.getPassThrough());
			eachObj.put(DEAL_TYPE, delItem.getDealType() == null ? "" : delItem.getDealType());
			eachObj.put(FULLFILLMENT_QTY, delItem.getFullfillmentQty() == null ? "" : delItem.getFullfillmentQty());
			eachObj.put(ORDER_LINE_NO, delItem.getOrderLineNo() == null ? "" : delItem.getOrderLineNo());
			eachObj.put(IS_INTER_COMPANY_CONTRACT,
					delItem.getIsInterCompanyContract() == null ? "" : delItem.getIsInterCompanyContract());
			eachObj.put(INTER_COMPANY_CONTRACT_REF_NO,
					delItem.getInterCompanyContractRefNo() == null ? "" : delItem.getInterCompanyContractRefNo());
			eachObj.put(RNUM, delItem.getRnum() == null ? "" : delItem.getRnum());
			eachObj.put(INTERNAL_CONTRACT_REF_NO,
					delItem.getInternalContractRefNo() == null ? "" : delItem.getInternalContractRefNo());
			eachObj.put(FROM_DATE, delItem.getFromDate() == null ? "" : delItem.getFromDate());
			eachObj.put(TO_DATE, delItem.getToDate() == null ? "" : delItem.getToDate());
			eachObj.put(OPEN_QTY, delItem.getOpenQty() == null ? "" : delItem.getOpenQty());
			eachObj.put(CP_ADDR, delItem.getCpAddress() == null ? "" : delItem.getCpAddress());
			eachObj.put(DEL_LOCATION, delItem.getDelLocation() == null ? "" : delItem.getDelLocation());
			eachObj.put(EXECUTABLE_QTY, delItem.getExecutableQty() == null ? "" : delItem.getExecutableQty());
			Double openQtySubStr = delItem.getOpenQty() == null ?0.0:Double.parseDouble(delItem.getOpenQty().substring(0, delItem.getOpenQty().indexOf(" ")));
			Double executableQtySubStr = delItem.getExecutableQty() == null ?0.0:Double.parseDouble(delItem.getExecutableQty().substring(0, delItem.getExecutableQty().indexOf(" ")));
			Double netOpenQtySubStr = delItem.getNetOpenQty() == null ?0.0:Double.parseDouble(delItem.getNetOpenQty().substring(0, delItem.getNetOpenQty().indexOf(" ")));
			eachObj.put(OPEN_QTY_SUBSTR, openQtySubStr);
			eachObj.put(EXECUTABLE_QTY_SUBSTR, executableQtySubStr);
			eachObj.put(NET_OPEN_QTY_SUBSTR, netOpenQtySubStr);
			eachObj.put(NET_OPEN_QTY, delItem.getNetOpenQty() == null ? "" : delItem.getNetOpenQty());
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("error in setting values in jsonobject:" + e.getMessage()));
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private Map<String,Object> createPayload(Object gmr, List<String> idList, HttpServletRequest req) {
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("createPayload starts"));
		JSONObject objJson = new JSONObject(gmr.toString());
		JsonArray stockArray = new JsonArray();
		JsonArray elementArray = new JsonArray();
		JsonBuilder stockBuilder = new JsonBuilder();
		JsonBuilder elementBuilder = new JsonBuilder();
		LocalDate dt = LocalDate.now();		

		String pcdiId = objJson.opt("pcdiId") == null || objJson.opt("pcdiId").equals("") ? ""
				: objJson.opt("pcdiId").toString();
		String itemQualityId = objJson.opt("Quality") == null ? "" : objJson.opt("Quality").toString();
		// String itemQualityId = "QAT-321";
		String itemQty = objJson.opt("advicedNetweight") == null || objJson.opt("advicedNetweight").equals("") ? "0"
				: objJson.opt("advicedNetweight").toString();
		String itemIncoTermId = objJson.opt("incoTermId") == null ? "" : objJson.opt("incoTermId").toString();
		String modeOfTransport = objJson.opt("modeOfTransport") == null || objJson.opt("modeOfTransport").equals("")
				? "Rail"
				: objJson.opt("modeOfTransport").toString();
		String activityDate = dt.toString();// objJson.opt("activityDate") == null ? "" :
											// objJson.opt("activityDate").toString();
		String qtyUnitId = objJson.opt("qtyUnitId") == null ? "" : objJson.opt("qtyUnitId").toString();
		String loadingCountryId = objJson.opt("LoadingLocationCountry") == null ? ""
				: objJson.opt("LoadingLocationCountry").toString();
		String loadingCityId = objJson.opt("LoadingLocationCity") == null ? ""
				: objJson.opt("LoadingLocationCity").toString();
		String dischargeCountryId = objJson.opt("dischargeCountryId") == null ? ""
				: objJson.opt("dischargeCountryId").toString();
		String dischargeCityId = objJson.opt("dischargeCityId") == null ? ""
				: objJson.opt("dischargeCityId").toString();
		String countryOfOriginCountryId = objJson.opt("CountryOfOrigin") == null ? ""
				: objJson.opt("CountryOfOrigin").toString();
		String expectedTimeOfArrival = objJson.opt("ExpectedArrivalDate") == null
				|| objJson.opt("ExpectedArrivalDate").equals("Invalid date") ? ""
						: objJson.opt("ExpectedArrivalDate").toString();
		String noOfContainers = objJson.opt("noOfContainers") == null ? "" : objJson.opt("noOfContainers").toString();
		String netWeight = objJson.opt("netWeight") == null ? "" : objJson.opt("netWeight").toString();
		String deliveryItemRefNo = objJson.opt("deliveryItemRefNo") == null ? ""
				: objJson.opt("deliveryItemRefNo").toString();
		String destination = objJson.opt("Destination") == null ? "" : objJson.opt("Destination").toString();
		String element = objJson.opt("Element") == null ? "" : objJson.opt("Element").toString();
		String advicedNetWeight = objJson.opt("AdvicedNetWeight") == null ? ""
				: objJson.opt("AdvicedNetWeight").toString();
		String countryOfOrigin = objJson.opt("CountryOfOrigin") == null ? ""
				: objJson.opt("CountryOfOrigin").toString();
		String particleSize = objJson.opt("ParticleSize") == null ? "" : objJson.opt("ParticleSize").toString();
		String supplierAddress = objJson.opt("cpAddress") == null ? "" : objJson.opt("cpAddress").toString();
		String quality = objJson.opt("Quality") == null ? "" : objJson.opt("Quality").toString();
		String loadingLocation = objJson.opt("LoadingLocation") == null ? ""
				: objJson.opt("LoadingLocation").toString();
		String supplier = objJson.opt("cpName") == null ? "" : objJson.opt("cpName").toString();
		String materialDescription = objJson.opt("MaterialDescription") == null ? ""
				: objJson.opt("MaterialDescription").toString();
		String SupplierReference = objJson.opt("SupplierReference") == null ? ""
				: objJson.opt("SupplierReference").toString();
		String lotSize = objJson.opt("LotSize") == null ? "" : objJson.opt("LotSize").toString();
		String loadingDate = objJson.opt("LoadingDate") == null || objJson.opt("LoadingDate").equals("Invalid date")
				? ""
				: objJson.opt("LoadingDate").toString();
		String SupplierRepresentative = (objJson.opt("SupplierRepresentative") == null || objJson.optString("SupplierRepresentative").equalsIgnoreCase("none")) 
				? "": objJson.opt("SupplierRepresentative").toString();
		logger.debug(Logger.EVENT_SUCCESS,"SupplierRepresentative:"+SupplierRepresentative);
		String estimatedValue = objJson.opt("EstimatedValue") == null ? "" : objJson.opt("EstimatedValue").toString();
		String specialInstructionsToSmelter = objJson.opt("SpecialInstructionsToSmelter") == null ? ""
				: objJson.opt("SpecialInstructionsToSmelter").toString();
		String containerFlag = objJson.opt("containerFlag") == null ? "" : objJson.opt("containerFlag").toString();
		String supplierId = objJson.opt("supplierId") == null ? "" : objJson.opt("supplierId").toString();
		String qualityName = objJson.opt("qualityName") == null ? "" : objJson.opt("qualityName").toString();
		String incoLocation = objJson.opt("incoLocation") == null ? "" : objJson.opt("incoLocation").toString();
		String countryOfOriginDisplayName = objJson.opt("CountryOfOriginDisplayName") == null ? "" : objJson.opt("CountryOfOriginDisplayName").toString();
		String loadingLocationCountryDisplayName = objJson.opt("LoadingLocationCountryDisplayName") == null ? "" : objJson.opt("LoadingLocationCountryDisplayName").toString();
		String expectedArrivalDate = objJson.opt("ExpectedArrivalDate") == null ? "" : objJson.opt("ExpectedArrivalDate").toString();
		String materialClassification = objJson.opt("materialClassification") == null ? "" : objJson.opt("materialClassification").toString();

		String id = objJson.opt("_id") == null ? "-1" : objJson.opt("_id").toString();
		idList.add(id);
		String isApplyContainerCharge = "N";
		if(StringUtils.isEmpty(pcdiId)) {
			logger.debug(Logger.EVENT_SUCCESS,"pcdiId:"+pcdiId);
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("pcdiId in the request body is empty or blank."));
			String errMsg = commonService.getErrorMessage("SC026", "pcdiId in the request body is empty or blank.", "supplierconnect");
			throw new ConnectException(errMsg);
		}
		boolean isApplyCharge = getContainerChargeFromContract(pcdiId, req);
		Map<String,Object> userMap = fetchUserInfo(req);
		if(StringUtils.isEmpty(userMap)) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("User details map is empty."));
			userMap = new HashMap<String,Object>();
			//throw new ConnectException("User details map is empty.");
		}
		List<String> userCPNames = new ArrayList<String>();
		List<String> userPermCodes = new ArrayList<String>();
		if(new Integer(3).equals(userMap.get("userType"))) {
			if(userMap.containsKey("businessPartys")) {
				userCPNames=(List<String>)userMap.get("businessPartys");
			}
			if(!StringUtils.isEmpty(userCPNames) && !userCPNames.contains(supplier)) {
				logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Cp names of the contract and the GMR doesn;t match or Business Partner list is empty."));
				String errMsg = commonService.getErrorMessage("SC027", "Cp names of the contract and the GMR doesn;t match or Business Partner list is empty.", "supplierconnect");
				throw new ConnectException(errMsg);
			}
		}else {
			if(userMap.containsKey("permCodes")) {
				userPermCodes=(List<String>)userMap.get("permCodes");
			}
			if(!userPermCodes.contains("ADMIN_FARMER_APPROVAL")) {
				logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("User Not Authorized."));
				String errMsg = commonService.getErrorMessage("SC037", "User Not Authorized.", "supplierconnect");
				throw new ConnectException(errMsg);
			}			
		}		
		JsonBuilder userInfoBuilder = new JsonBuilder();
		userInfoBuilder.add("contactDetails",userMap.containsKey("contactDetails")? userMap.get("contactDetails").toString():"");
		userInfoBuilder.add("bankDetails",userMap.containsKey("bankDetails")? userMap.get("bankDetails").toString():"");
		userInfoBuilder.add("userName",userMap.containsKey("userName")? userMap.get("userName").toString():"");
		userInfoBuilder.add("createFirstName",userMap.containsKey("firstName")? userMap.get("firstName").toString():"");
		userInfoBuilder.add("createLastName",userMap.containsKey("lastName")? userMap.get("lastName").toString():"");
		userInfoBuilder.add("updateFirstName","");
		userInfoBuilder.add("updateLastName","");
		userInfoBuilder.add("email",userMap.containsKey("email")? userMap.get("email").toString():"");
		userInfoBuilder.add("mobile",userMap.containsKey("mobile")? userMap.get("mobile").toString():"");
		userInfoBuilder.add("phone",userMap.containsKey("phone")? userMap.get("phone").toString():"");
		userInfoBuilder.add("fax",userMap.containsKey("fax")? userMap.get("fax").toString():"");
		userInfoBuilder.add("address",userMap.containsKey("address")? userMap.get("address").toString():"");
		userInfoBuilder.add("country",userMap.containsKey("country")? userMap.get("country").toString():"");
		userInfoBuilder.add("state",userMap.containsKey("state")? userMap.get("state").toString():"");
		userInfoBuilder.add("city",userMap.containsKey("city")? userMap.get("city").toString():"");
		userInfoBuilder.add("zipcode",userMap.containsKey("zipcode")? userMap.get("zipcode").toString():"");
		userInfoBuilder.add("website",userMap.containsKey("website")? userMap.get("website").toString():"");
		String userName = (userMap.containsKey("firstName")? userMap.get("firstName").toString():"") +" "+ (userMap.containsKey("lastName")? userMap.get("lastName").toString():"");		
		String email = userMap.containsKey("email")? userMap.get("email").toString():"";
		Map<String,String> emailMap = new HashMap<String,String>();
		emailMap.put(INCO_LOCATION, incoLocation);
		emailMap.put(CP_NAME, supplier);
		emailMap.put(SUPPLIER_NAME, userName);
		emailMap.put(QUALITY_NAME, qualityName);
		emailMap.put(COUNTRY_OF_ORIGIN, countryOfOriginDisplayName);
		emailMap.put(COUNTRY_OF_LOADING, loadingLocationCountryDisplayName);
		emailMap.put(LOADING_DATE, DateUtility.dateFormat(loadingDate));
		emailMap.put(ESTIMATED_ARRIVAL_DATE, DateUtility.dateFormat(expectedArrivalDate));
		emailMap.put(EMAIL, email);
		
		if (modeOfTransport.equalsIgnoreCase(MODE_OF_TRANSPORT_RAIL)
				&& containerFlag.equalsIgnoreCase(CONTAINER_FLAG_YES)) {
			if (isApplyCharge) {
				isApplyContainerCharge = "Y";
			} else {
				isApplyContainerCharge = "N";
			}
		} else if (modeOfTransport.equalsIgnoreCase(MODE_OF_TRANSPORT_RAIL)
				&& containerFlag.equalsIgnoreCase(CONTAINER_FLAG_NO)) {
			isApplyContainerCharge = "N";
		} else if (!modeOfTransport.equalsIgnoreCase(MODE_OF_TRANSPORT_RAIL)) {
			isApplyContainerCharge = "N";
		}
		JSONObject assayObj = objJson.optJSONObject("assaying");
		String useBolidenAssay = "";
		if (null != assayObj) {
			JSONArray useBolidenAssayArray = assayObj.optJSONArray("useBolidenAssay");
			if(useBolidenAssayArray != null && useBolidenAssayArray.length() > 0) {
				useBolidenAssay = useBolidenAssayArray.getString(0);
			}
			JSONArray elementsArray = assayObj.optJSONArray("elements");
			int k = 0;
			if (elementsArray != null && elementsArray.length() > 0) {
				while (k < elementsArray.length()) {
					JSONObject elementsObj = elementsArray.getJSONObject(k);
					elementBuilder = new JsonBuilder();
					elementBuilder.add("elementId", elementsObj.optString("elementId"));
					elementBuilder.add("typical", elementsObj.optString("estimatedValue"));
					String assayRule = elementsObj.optString("elementAssayRule").toString();
					/*
					 * Added the below useBolidenAssay flag check for element level flag as part
					 * of ticket SC-3127					 * 
					 */					
					logger.debug(Logger.EVENT_SUCCESS, "useBolidenAssay="+useBolidenAssay);
					if(useBolidenAssay.equalsIgnoreCase("Y") || useBolidenAssay.equalsIgnoreCase("Yes")){
						logger.debug(Logger.EVENT_SUCCESS, "inside useBolidenAssay Y condition:"+useBolidenAssay);
						if(assayRule.equals("Assay Exchange")) {
							elementBuilder.add("useBolidenAssay", "Y");
						}
					}
					elementArray.add(elementBuilder.json);
					k++;
				}
			}
		}

		JSONArray stocksArray = objJson.optJSONArray("containerDetails");
		int j = 0;
		String transportId = "";
		if (stocksArray != null && stocksArray.length() > 0) {
			String remarks = null;
			String containerDet = "";
			while (j < stocksArray.length()) {
				JSONObject stocksObj = stocksArray.getJSONObject(j);
				stockBuilder = new JsonBuilder();
				transportId = stocksObj.opt("transportId") == null ? "" : stocksObj.opt("transportId").toString();
				if (!transportId.equals("")) {
					containerDet = transportId;
				} else {
					containerDet = stocksObj.optString("containerNo");
				}
				stockBuilder.add("netWeight", stocksObj.optString("netWeight"));
				stockBuilder.add("grossWeight", stocksObj.optString("grossWeight"));
				stockBuilder.add("tareWeight", stocksObj.optString("tareWeight"));
				stockBuilder.add("containerNo", containerDet);
				stockBuilder.add("containerSize", stocksObj.optString("containerSize"));
				stockBuilder.add("customerSealNo", stocksObj.optString("sealNo"));
				stockBuilder.add("isContainerApplicable", containerFlag.equalsIgnoreCase(CONTAINER_FLAG_YES)?"Y":"N");
				stockBuilder.add("noOfBags", stocksObj.optString("totalPackage"));
				remarks = "Loose-" + stocksObj.optString("loose") +COMMA_SPACE+ "Drums-" + stocksObj.optString("drums")+COMMA_SPACE+ "Bags-"
						+ stocksObj.optString("bags")+COMMA_SPACE + "Boxes-" + stocksObj.optString("boxes")+COMMA_SPACE + "Pallets-"
						+ stocksObj.optString("pallets");
				stockBuilder.add("remarks", remarks);
				stockArray.add(stockBuilder.json);
				j++;
			}
		}
		String json = new JsonBuilder().add("pcdiId", pcdiId).add("itemQualityId", itemQualityId)
				.add("itemQty", itemQty).add("itemIncoTermId", itemIncoTermId)
				.add("itemIncoTermCountryId", dischargeCountryId).add("itemIncoTermCityId", dischargeCityId)
				.add("modeOfTransport", modeOfTransport).add("activityDate", activityDate)
				.add("loadingDate", loadingDate).add("qtyUnitId", qtyUnitId).add("loadingCountryId", loadingCountryId)
				.add("loadingCityId", loadingCityId).add("dischargeCountryId", dischargeCountryId)
				.add("dischargeCityId", dischargeCityId).add("countryOfOriginCountryId", countryOfOriginCountryId)
				.add("expectedTimeOfArrival", expectedTimeOfArrival).add("noOfContainers", noOfContainers)
				.add("supplierRefNo", SupplierReference).add("comments", materialDescription)
				.add("specialInstructions", specialInstructionsToSmelter)
				.add("isApplyContainerCharge", isApplyContainerCharge).add("createSupplierAssay", " ")
				.add("internalGMRRefNo", " ").add("vesselName", transportId).add("senderId", supplierId)
				.add("suppRepId", SupplierRepresentative).add("sendersAddress", supplierAddress)
				.add(STOCK_LIST, stockArray).add(ELEMENT_LIST, elementArray)
				.add("supplierElementList", elementArray)
				.add("portalUserDetails", userInfoBuilder)
				.add("useBolidenAssay",useBolidenAssay)
				.add("materialClassification",materialClassification)
				.add("gmrApprovalStatus",GMR_APPROVAL_STATUS_DRAFT)
				.toJson();
		Map<String,Object> payLoadMap = new HashMap<String,Object>();
		payLoadMap.put(GMR_PAYLOAD, json);
		payLoadMap.put(EMAIL_DATA, emailMap);
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("createPayload ends"));
		return payLoadMap;

	}

	/**
	 * 
	 * @return
	 * 
	 */
	private String resovleCollectionName(String tenantId) {
		return tenantId + "_Data";
	}

	@SuppressWarnings("rawtypes")
	private boolean getContainerChargeFromContract(String pcdid, HttpServletRequest req) {
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("getContainerChargeFromContract starts"));
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		boolean isCharges = false;
		try {
			String uri = getPropertyFromConnect(req, EKA_CTRM_HOST) + CONTRACT_DETAILS_PATH;
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.add("username", "e-bolprpa");
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("pcdiId", Integer.parseInt(pcdid));
			HttpEntity<Map> entity = new HttpEntity<Map>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);
			restTemplate.setMessageConverters(messageConverters);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("getContainerChargeFromContract uri:"+uri + " input pcdid : "+inp.get("pcdiId")));
			long start = System.currentTimeMillis();
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of trm API Call for getContainerChargeFromContract:" + (end - start) + "ms" );
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);
			} else {
				logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("getContainerChargeFromContract method: No Response from API or result is empty"));
				arrayToJson = new String();
			}
			JSONObject quality = new JSONObject(arrayToJson);
			List<Map<String, Object>> mapList = new Gson().fromJson(quality.get("additionalChargeDTOs").toString(),
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
			if (mapList != null) {
				Map<String, Object> obj = null;
				Iterator<Map<String, Object>> it = mapList.iterator();
				while (it.hasNext()) {
					obj = (Map<String, Object>) it.next();
					if (obj.containsKey("isActive") && obj.containsKey("additionalChargeName")) {
						if (obj.get("isActive").toString().equalsIgnoreCase("true")
								&& obj.get("additionalChargeName").toString().equalsIgnoreCase("Container Charges")) {
							isCharges = true;
							break;
						}
					}
				}
			}
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,"HttpClientErrorException in getContainerChargeFromContract API:  -> " + httpClientErrorException.getRawStatusCode() + ""
					+ httpClientErrorException.getResponseBodyAsString() + httpClientErrorException.getResponseHeaders(), httpClientErrorException);
			String errcodemsg = commonService.getErrorMessage("SC041", "HttpClientErrorException in getContainerChargeFromContract API:", "supplierconnect");
			throw new ContractDataException(errcodemsg, httpClientErrorException);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("ResourceAccessException in getContainerChargeFromContract api: "),resourceAccessException);
			String errcodemsg = commonService.getErrorMessage("SC041", "ResourceAccessException in getContainerChargeFromContract api:", "supplierconnect");
			throw new ContractDataException(errcodemsg, resourceAccessException);
		} catch (Exception e) {
			e.printStackTrace();
			String errcodemsg = commonService.getErrorMessage("SC042", "getContainerChargeFromContract API Call Failed.", "supplierconnect");
			throw new ConnectException(errcodemsg+ e.getLocalizedMessage());
		}
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("getContainerChargeFromContract response:"+isCharges));
		return isCharges;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> fetchAssayAndStockList(Object input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);		
		ResponseEntity<Object> result = null;		
		String arrayToJson = null;
		Map<String,Object> finalAssayAndStockMap = new HashMap<>();
		try {
			String ctrmUri = getPropertyFromConnect(req, EKA_CTRM_HOST);
			String uri = ctrmUri + ASSAY_URL_PATH;			
			String stockUri = ctrmUri + STOCK_URL_PATH;
			headers.add("Content-Type", "application/json;charset=UTF-8");
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			
			ObjectWriter owr = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String inpJson = owr.writeValueAsString(input);
			JSONObject inpJsonObj = new JSONObject(inpJson);
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("intGmrRefNo", inpJsonObj.get("intGmrRefNo"));
			inp.put("gmrRefNo", inpJsonObj.get("gmrRefNo"));
			HttpEntity<Object> entity = new HttpEntity<Object>(inp, headers);			
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchAssayAndStockList : intGmrRefNo : "+inpJsonObj.get("intGmrRefNo")+ " gmrRefNo : "+inpJsonObj.get("gmrRefNo")));
			long start = System.currentTimeMillis();
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of trm viewassay API Call in method fetchAssayAndStockList:" + (end - start) + "ms" );
			start = System.currentTimeMillis();
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from CTRM viewASsay API or result is empty"));
				arrayToJson = new String();
			}
			
			JSONArray jarr = new JSONArray(arrayToJson);
			JSONObject assay = null;
			int index = 0;
			List<String> grdIdList = null;
			Map assayAndStockJson = null;						
			String assayHeaderType = null;
			String assayHeaderTypeVal = null;
			int grdIndex = 0;
			Map<String,List<String>> grdMap = new HashMap<String,List<String>>();			
			String assayTypeName = null;
			String grdId = null;
			String useBolidenAssay = null;
			boolean containsWNS = false;
			while (jarr.length() > grdIndex) {
				assay = jarr.getJSONObject(grdIndex);
				assayTypeName = assay.get(ASSAY_TYPE).toString();				
				grdId = assay.get("grdId").toString();
				useBolidenAssay =  StringEscapeUtils.escapeJava(assay.get("useBolidenAssay").toString());
				List<String> grdLst = grdMap.get(assayTypeName);
				if(grdLst == null) {
					grdLst = new ArrayList<String>();
					grdLst.add(grdId);
					grdMap.put(assayTypeName,grdLst);
				}else {
					grdMap.get(assayTypeName).add(grdId);
				}				
				grdIndex++;
				grdLst = null;
			}
			if(grdMap.containsKey(WEIGHING_AND_SAMPLING_ASSAY)) {
				containsWNS = true;
			}
			assay = null;						
			while (jarr.length() > index) {			
				assay = jarr.getJSONObject(index);
				List stockArr = null;
				List assayArr = null;
				index++;
				assayAndStockJson = new HashMap();				
				grdIdList = new ArrayList<String>();
				assayHeaderType = assay.get(ASSAY_TYPE).toString();
				assayHeaderTypeVal = assayHeaderType.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
						: (assayHeaderType.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay" : assayHeaderType);

				List<Map<String, Object>> assaySubLotDtoLst = new Gson().fromJson(
						assay.get("assaySublotMappingDTOs").toString(), new TypeToken<List<Map<String, Object>>>() {
						}.getType());
				// to get Assay rules from contractFinalAssayRulesDTOs property
				List<Map<String, Object>> contractFinalAssayRulesList = new Gson().fromJson(
						assay.get("contractFinalAssayRulesDTOs").toString(),new TypeToken<List<Map<String, Object>>>() {
						}.getType());
				Map<String, String> elementAssayRuleMap = null;
				if (contractFinalAssayRulesList != null) {
					Map<String, Object> assyRule = null;
					Iterator<Map<String, Object>> assayRuleIt = contractFinalAssayRulesList.iterator();
					String finalAssayType = null;
					String assayRule = null;
					elementAssayRuleMap = new HashMap<String, String>();
					while (assayRuleIt.hasNext()) {
						assyRule = (Map<String, Object>) assayRuleIt.next();
						if (assyRule.containsKey(ELEMENT_ID)) {
							finalAssayType = assyRule.get("finalAssayType") == null ? "": assyRule.get("finalAssayType").toString();
							assayRule = finalAssayType.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
									: (finalAssayType.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay"
											: finalAssayType);
							elementAssayRuleMap.put(assyRule.get(ELEMENT_ID).toString(), assayRule);
						}
					}
				}
				Map<String, Object> assaySubLotObj = null;
				Iterator<Map<String, Object>> prodIt = assaySubLotDtoLst.iterator();
				while (prodIt.hasNext()) {
					assaySubLotObj = (Map<String, Object>) prodIt.next();
					if (assaySubLotObj.containsKey("chemicalAttributesDTOs")) {
						List<Map<String, Object>> chemicalDtoList = (List<Map<String, Object>>) assaySubLotObj.get("chemicalAttributesDTOs");
						Map<String, Object> chemicalDtoObj = null;
						Iterator<Map<String, Object>> chemicalIt = chemicalDtoList.iterator();
						Map eachObj = null;
						String elementName = null;
						String elementId = null;
						String typical = null;
						String ratioName = null;
						String showInSupplierConnect = null;
						String umpirePayment = null;
						assayArr = new ArrayList();
						while (chemicalIt.hasNext()) {
							chemicalDtoObj = (Map<String, Object>) chemicalIt.next();
							elementName = chemicalDtoObj.get(ELEMENT_NAME) == null ? "": chemicalDtoObj.get(ELEMENT_NAME).toString();
							elementId = chemicalDtoObj.get(ELEMENT_ID) == null ? "": chemicalDtoObj.get(ELEMENT_ID).toString();
							typical = chemicalDtoObj.get(TYPICAL) == null ? "" : chemicalDtoObj.get(TYPICAL).toString();
							ratioName = chemicalDtoObj.get(RATIO_NAME) == null ? "": chemicalDtoObj.get(RATIO_NAME).toString();
							showInSupplierConnect = chemicalDtoObj.get(SHOW_IN_SUPPLIER_CONNECT) == null ? "": chemicalDtoObj.get(SHOW_IN_SUPPLIER_CONNECT).toString();
							umpirePayment = chemicalDtoObj.get(ASSAY_WINNER) == null ? ""
									: (chemicalDtoObj.get(ASSAY_WINNER).toString().equalsIgnoreCase(SELF_ASSAY_WINNER)
											? SELF_UMPIRE_PAYMENT
											: chemicalDtoObj.get(ASSAY_WINNER).toString()
													.equalsIgnoreCase(EVEN_ASSAY_WINNER)
															? EVEN_UMPIRE_PAYMENT
															: chemicalDtoObj.get(ASSAY_WINNER).toString()
																	.equalsIgnoreCase(CP_ASSAY_WINNER)
																			? CP_UMPIRE_PAYMENT
																			: "");
							eachObj = new HashMap();
							eachObj.put(ELEMENT_NAME, elementName);
							eachObj.put(ELEMENT_ID, elementId);
							eachObj.put(TYPICAL, typical);
							eachObj.put(RATIO_NAME, ratioName);
							eachObj.put(SHOW_IN_SUPPLIER_CONNECT, showInSupplierConnect);
							eachObj.put(UMPIRE_PAYMENT, umpirePayment);							
							setAssayValues(eachObj, chemicalDtoObj);
							if (elementAssayRuleMap != null && elementAssayRuleMap.size() > 0) {
								eachObj.put("assayRule", elementAssayRuleMap.getOrDefault(elementId, " ").toString());
							}
							assayArr.add(eachObj);
						}
					}
				}
				if (assay.get(ASSAY_TYPE).toString().equalsIgnoreCase(WEIGHING_AND_SAMPLING_ASSAY)) {
					assayAndStockJson.put("activityDate",assay.get("activityDate") == null ? "" : assay.get("activityDate").toString());
				}
				//if WNS assay type available lotno will be populated else lotno is blank
				if (containsWNS && !assayHeaderTypeVal.contains("Provisional Assay")) {
					assayAndStockJson.put(LOTNO,StringUtils.isEmpty(assay.get(LOTNO)) ? "" : assay.get(LOTNO).toString());
				}else {
					assayAndStockJson.put(LOTNO,"");
				}
				grdIdList = grdMap.get(assayHeaderType);
				long start1 = System.currentTimeMillis();
				stockArr = (fetchStockList(inp, grdIdList, req, stockUri)).toList();
				long end1 = System.currentTimeMillis();
				logger.debug(Logger.EVENT_SUCCESS,	"Total time of trm API Call for viewstock:" + (end1 - start1) + "ms" );
				assayAndStockJson.put(STOCK_LIST, stockArr);
				assayAndStockJson.put(ELEMENT_LIST, assayArr);											
				List<Object> assayAndStockList = (List) finalAssayAndStockMap.get(assayHeaderTypeVal);
				if (assayAndStockList == null) {
					assayAndStockList = new ArrayList<>();
					assayAndStockList.add(assayAndStockJson);
					finalAssayAndStockMap.put(assayHeaderTypeVal, assayAndStockList);
				} else {
					assayAndStockList.add(assayAndStockJson);
					finalAssayAndStockMap.put(assayHeaderTypeVal, assayAndStockList);					
				}				
				assayAndStockList = null;
			}			
			finalAssayAndStockMap.put(USE_BOLIDEN_ASSAY,useBolidenAssay);
			end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of processing response of trm viewassay API in fetchAssayAndStockList :" + (end - start) + "ms" );
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("HttpClientErrorException in fetchAssayandStockList : "),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC012", "httpClientErrorException inside method fetchAssayAndStockList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("ResourceAccessException in fetchAssayAndStockList: "),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC013", "ResourceAccessException in fetchAssayAndStockList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC014", "fetchAssayAndStockList API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg1+ e.getLocalizedMessage());
		}
		logger.debug(Logger.EVENT_SUCCESS,("fetchAssayAndStockList response:"+finalAssayAndStockMap.size()));
		return finalAssayAndStockMap;
	}

	public JSONArray fetchStockList(Map<String, Object> input, List<String> grdList, HttpServletRequest req,String uri) {

		HttpHeaders headers = commonService.getHttpHeader(req);
		ResponseEntity<Object> result = null;
		String arrayToJson = null;
		JSONArray stkArray = new JSONArray();
		try {			
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			HttpEntity<Object> entity = new HttpEntity<Object>(input, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);			
			restTemplate.setMessageConverters(messageConverters);*/
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from API or result is empty"));
				arrayToJson = new String();
			}
			JSONArray jarr = new JSONArray(arrayToJson);
			JSONObject stock = null;
			int index = 0;
			String sealNo = null;
			String remarks = null;
			String noOfBags = null;
			String netWeight = null;
			String grossWeight = null;
			String tareWeight = null;
			String noOfContainers = null;
			String containerSize = null;
			String containerNo = null;
			// String[] remarkArr = null;
			JSONObject eachObj = null;
			// String[] packArr = null;
			// String val = null;
			while (jarr.length() > index) {
				stock = jarr.getJSONObject(index);
				index++;
				/*
				 * Commenting out the below if condition as part of jira SC-4250. We will no longer match the grdid from viewAssay api
				 * with the internalGrdRefNo of trm viewStocks api. we will fetch all the stock records from trm that is part of the 
				 * gmr. 
				 * 
				 */
				//if (grdList.contains(stock.get("internalGrdRefNo").toString())) {
					sealNo = stock.optString("customerSealNo","") ;
					remarks = stock.optString("remarks","");
					noOfBags = stock.optString("noOfBags","") ;
					netWeight = stock.optString("netWeight","");
					grossWeight = stock.optString("grossWeight","") ;
					tareWeight = stock.optString("tareWeight","");
					noOfContainers = stock.optString("noOfConatiners") ;
					containerSize = stock.optString("containerSize","");
					containerNo = stock.optString("containerNo","") ;
					eachObj = new JSONObject();
					/*
					 * if(!remarks.equals("")) { remarkArr = remarks.split(","); for(String pack :
					 * remarkArr) { packArr = pack.split("-"); if(packArr.length >0) val =
					 * packArr[1]; else val = ""; eachObj.put(packArr[0], val); } }
					 */
					eachObj.put("internalGrdRefNo", stock.optString("internalGrdRefNo",""));
					eachObj.put("sealNo", sealNo);
					eachObj.put("remarks", remarks);
					eachObj.put("noOfBags", noOfBags);
					eachObj.put("netWeight", netWeight);
					eachObj.put("grossWeight", grossWeight);
					eachObj.put("tareWeight", tareWeight);
					eachObj.put("noOfContainers", noOfContainers);
					eachObj.put("containerSize", containerSize);
					eachObj.put("containerNo", containerNo);
					eachObj.put("isContainerApplicable", stock.optString("isContainerApplicable",""));
					stkArray.put(eachObj);
				//}
			}

		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchStockList: "+httpClientErrorException));
			String errMsg = commonService.getErrorMessage("SC028", "error in calling property eka_ctrm_host from connect inside method fetchStockList", "supplierconnect");
			throw new ContractDataException(errMsg);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchStockList: "+resourceAccessException));
			String errMsg = commonService.getErrorMessage("SC029", "error in fetching Stock List from specified ctrm host", "supplierconnect");
			throw new ContractDataException(errMsg);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = commonService.getErrorMessage("SC030", "fetchStockList API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg+ e.getLocalizedMessage());
		}
		return stkArray;
	}

	/**
	 * Usage:The <i>fetchGMREconomicValues</i> method has the logic to map each elementid of a gmr with  economic values .
	 *  
	 * @param input
	 * 				The Map containing input values - intGmrRefNo and gmrRefNo
	 * @return MAP This method returns map of elementid with  economic values
	 */
	public Map<String, Map<String, String>> fetchGMREconomicValues(Map<String, Object> input, HttpServletRequest req) {

		HttpHeaders headers = commonService.getHttpHeader(req);
		ResponseEntity<Object> economicRespEntity = null;
		/****
		 * TODO : accessing direct CTRM api as the platform wrapper api is giving error.
		 * have to point to wrapper api once its ready
		 * 
		 */
		String economicValuesuri1 = CTRM_ECONOMIC_VALUES_URI;
		String platform_url = getPropertyFromConnect(req, "platform_url");
		//economicValuesuri = economicValuesuri.replace("${platform_url}", platform_url);
		String ctrmUri = getPropertyFromConnect(req, EKA_CTRM_HOST);
		String economicValuesuri = ctrmUri + "/api/logistic/economicValueDetails";
		String economicArrayToJson = null;
		Map<String, Map<String, String>> elementEconomicMap = new HashMap<String, Map<String, String>>();
		headers.add("Content-Type", "application/json;charset=UTF-8");
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		HttpEntity<Object> entity = new HttpEntity<Object>(input, headers);				
		/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);*/
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchGMREconomicValues economicValueDetails : intGmrRefNo : "
				+ input.get("intGmrRefNo") + " gmrRefNo : " + input.get("gmrRefNo")));		
		
		try {
			economicRespEntity = restTemplate.exchange(economicValuesuri, HttpMethod.POST, entity, Object.class);

			if (economicRespEntity != null) {
				Object economicObj = economicRespEntity.getBody();
				ObjectMapper mapper = new ObjectMapper();
				economicArrayToJson = mapper.writeValueAsString(economicObj);

			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from CTRM economicValueDetails API or result is empty"));
				economicArrayToJson = new String();
			}
		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML(
					"HttpClientErrorException inside fetchAssayAndStockList() while calling CTRM economicValueDetails API-> "
							+ he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders()+": "+he));
			String errcodemsg = commonService.getErrorMessage("SC031",
					"HttpClientErrorException inside fetchAssayAndStockList() while calling CTRM economicValueDetails API . Please try again!:",
					"supplierconnect");
			throw new ConnectException(errcodemsg + he.getLocalizedMessage());
		} catch (HttpStatusCodeException ex) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML(
					"RestClientException inside fetchAssayAndStockList() while calling CTRM economicValueDetails API -> "
							+ ex.getRawStatusCode() + "" + ex.getResponseBodyAsString() + ex.getResponseHeaders()
					+": "+ex));
			String errcodemsg = commonService.getErrorMessage("SC031",
					"HttpClientErrorException inside fetchAssayAndStockList() while calling CTRM economicValueDetails API . Please try again!",
					"supplierconnect");
			throw new ConnectException(errcodemsg + ex.getLocalizedMessage());
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchAssayAndStockList method while calling CTRM economicValueDetails API:"
					+ e.getLocalizedMessage()));
			String errcodemsg = commonService.getErrorMessage("SC032",
					"Exception in fetchAssayAndStockList method while calling CTRM economicValueDetails API:", "supplierconnect");
			throw new ConnectException(errcodemsg + e.getLocalizedMessage());
		}
		JSONArray econoArr = new JSONArray(economicArrayToJson);
		JSONObject ecoJson = null;
		Map<String, String> economicValueMap = null;
		int rowIndex = 0;
		String elemId = null;
		String elementName = null;
		String diffAssayValue = null;
		String outOfSplitLimit = null;
		String selfAssaySplitLimit = null;
		String splittingLimit = null;
		String assayValueUnitName = null;
		String umpireLabName = null;
		String stockRefNo = null;
		while (econoArr.length() > rowIndex) {
			ecoJson = econoArr.getJSONObject(rowIndex);
			elemId = ecoJson.optString("elementId","");
			elementName = ecoJson.optString("elementName", "" );
			diffAssayValue = ecoJson.optString("diffAssayValue","" );
			outOfSplitLimit = ecoJson.optString("outOfSplitLimit", "" );
			selfAssaySplitLimit = ecoJson.optString("selfAssaySplitLimit", "");
			assayValueUnitName = ecoJson.optString("assayValueUnitName", "");
			umpireLabName = ecoJson.optString("umpireLabName", "" );
			stockRefNo = ecoJson.optString("stockRefNo", "" );
			splittingLimit = selfAssaySplitLimit + " " + assayValueUnitName;
			economicValueMap = new HashMap<String, String>();
			economicValueMap.put("elementName", elementName);
			economicValueMap.put("assayDifference", diffAssayValue);
			economicValueMap.put("outOfSplitLimit", outOfSplitLimit);
			economicValueMap.put("splittingLimit", splittingLimit);
			economicValueMap.put("umpireLabName", umpireLabName);
			economicValueMap.put("assayValueUnitName", assayValueUnitName);
			economicValueMap.put("selfAssaySplitLimit", selfAssaySplitLimit);
			economicValueMap.put("stockRefNo", stockRefNo);
			elementEconomicMap.put(stockRefNo+TILDE_SEPARATOR+elemId, economicValueMap);
			rowIndex++;
			economicValueMap = null;
		}
		return elementEconomicMap;
	}

	@SuppressWarnings("unchecked")
	public void setAssayValues(Map eachObj, Map<String, Object> attr) {		
		String assayType = null;
		String isReturnable = null;
		String isPayable = null;
		isReturnable = attr.get("isReturnable") == null ? "N" : attr.get("isReturnable").toString();
		isPayable = attr.get("isElemForPricing") == null ? null : attr.get("isElemForPricing").toString();
		assayType = (isReturnable.equalsIgnoreCase("Y") ? "Return"
				: (StringUtils.isEmpty(isPayable))?"": (isPayable.equalsIgnoreCase("Y") || isPayable.equalsIgnoreCase("true") ? "Payable" : "Penalty"));
		eachObj.put("tabRequestId", attr.get("tabRequestId") == null ? "" : attr.get("tabRequestId").toString());
		eachObj.put("id", attr.get("id") == null ? "" : attr.get("id").toString());
		eachObj.put("createdBy", attr.get("createdBy") == null ? "" : attr.get("createdBy").toString());
		eachObj.put("createdDate", attr.get("createdDate") == null ? "" : attr.get("createdDate").toString());
		eachObj.put("updatedBy", attr.get("updatedBy") == null ? "" : attr.get("updatedBy").toString());
		eachObj.put("updatedDate", attr.get("updatedDate") == null ? "" : attr.get("updatedDate").toString());
		eachObj.put("isActive", attr.get("isActive") == null ? "false" : attr.get("isActive").toString());
		eachObj.put("version", attr.get("version") == null ? "" : attr.get("version").toString());
		eachObj.put("pqcaId", attr.get("pqcaId") == null ? "" : attr.get("pqcaId").toString());
		eachObj.put("assayHeader", attr.get("assayHeader") == null ? "" : attr.get("assayHeader").toString());
		eachObj.put("unitOfMeasure", attr.get("unitOfMeasure") == null ? "" : attr.get("unitOfMeasure").toString());
		eachObj.put("isElemForPricing",
				attr.get("isElemForPricing") == null ? "" : attr.get("isElemForPricing").toString());
		eachObj.put("isAssayExchange",
				attr.get("isAssayExchange") == null ? "false" : attr.get("isAssayExchange").toString());
		eachObj.put("basis", attr.get("basis") == null ? "" : attr.get("basis").toString());
		eachObj.put("uniqueId", attr.get("uniqueId") == null ? "" : attr.get("uniqueId").toString());
		eachObj.put("cheRejection", attr.get("cheRejection") == null ? "" : attr.get("cheRejection").toString());
		eachObj.put("productId", attr.get("productId") == null ? "" : attr.get("productId").toString());
		eachObj.put("isDeductible", attr.get("isDeductible") == null ? "" : attr.get("isDeductible").toString());
		eachObj.put("ratioNumeratorId",
				attr.get("ratioNumeratorId") == null ? "" : attr.get("ratioNumeratorId").toString());
		eachObj.put("ratioDenominatorId",
				attr.get("ratioDenominatorId") == null ? "" : attr.get("ratioDenominatorId").toString());
		eachObj.put("payablePercentage",
				attr.get("payablePercentage") == null ? "" : attr.get("payablePercentage").toString());
		eachObj.put("isReturnable", isReturnable);
		eachObj.put("priceType", attr.get("priceType") == null ? "" : attr.get("priceType").toString());
		eachObj.put("priceValue", attr.get("priceValue") == null ? "" : attr.get("priceValue").toString());
		eachObj.put("priceUnitId", attr.get("priceUnitId") == null ? "" : attr.get("priceUnitId").toString());
		eachObj.put("isElementPartOfContractualAssay", attr.get("isElementPartOfContractualAssay") == null ? ""
				: attr.get("isElementPartOfContractualAssay").toString());
		eachObj.put("isPureFreeMetalElement",
				attr.get("isPureFreeMetalElement") == null ? "" : attr.get("isPureFreeMetalElement").toString());
		eachObj.put("splitLimit", attr.get("splitLimit") == null ? "" : attr.get("splitLimit").toString());
		eachObj.put("isMarkedForDeletion",
				attr.get("isMarkedForDeletion") == null ? "false" : attr.get("isMarkedForDeletion").toString());
		eachObj.put("isNewlyAdded", attr.get("isNewlyAdded") == null ? "false" : attr.get("isNewlyAdded").toString());
		eachObj.put("isMarkedForDeletionPrd",
				attr.get("isMarkedForDeletionPrd") == null ? "false" : attr.get("isMarkedForDeletionPrd").toString());
		eachObj.put("isNewlyAddedPrd",
				attr.get("isNewlyAddedPrd") == null ? "false" : attr.get("isNewlyAddedPrd").toString());
		eachObj.put("isActiveEntry",
				attr.get("isActiveEntry") == null ? "false" : attr.get("isActiveEntry").toString());
		eachObj.put("umpireId", attr.get("umpireId") == null ? "" : attr.get("umpireId").toString());
		eachObj.put("umpireName", attr.get("umpireName") == null ? "" : attr.get("umpireName").toString());
		eachObj.put("umpireRefNo", attr.get("umpireRefNo") == null ? "" : attr.get("umpireRefNo").toString());
		eachObj.put("wgtAvgTypical", attr.get("wgtAvgTypical") == null ? "" : attr.get("wgtAvgTypical").toString());
		eachObj.put("payableQty", attr.get("payableQty") == null ? "" : attr.get("payableQty").toString());
		eachObj.put("payableQtyUnitId",
				attr.get("payableQtyUnitId") == null ? "" : attr.get("payableQtyUnitId").toString());
		eachObj.put("elementPrice", attr.get("elementPrice") == null ? "" : attr.get("elementPrice").toString());
		eachObj.put("pricingPriceUnitId",
				attr.get("pricingPriceUnitId") == null ? "" : attr.get("pricingPriceUnitId").toString());
		eachObj.put("mirrorSeqPqca", attr.get("mirrorSeqPqca") == null ? "" : attr.get("mirrorSeqPqca").toString());
		eachObj.put(ASSAY_TYPE, assayType);		
	}

	@SuppressWarnings("rawtypes")
	private String getPropertyFromConnect(HttpServletRequest req, String propertyName) {
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("inside method getPropertyFromConnect"));
		String propertyUri = ekaConnectHost + "/property/" + SUPPLIERCONNECT_UUID + "/" + propertyName;
		Enumeration<String> headerNames = req.getHeaderNames();
		HttpHeaders httpHeaders = new HttpHeaders();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			httpHeaders.add(headerName, req.getHeader(headerName));
		}		 
		HttpEntity<Map> entityForPropRequest = new HttpEntity<Map>(httpHeaders);
		ResponseEntity<Map> propResult = null;		
		propResult = restTemplate.exchange(propertyUri, HttpMethod.GET, entityForPropRequest, Map.class);
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("method getPropertyFromConnect ends"));
		return (String) propResult.getBody().get("propertyValue");
	}
	/**
	 * Usage:The <i>fetchUserInfo</i> method calls the userinfo api from connect to get the info of 
	 * the logged in user.
	 *  
	 * @param req	The HttpServletRequest object
	 * @return MAP This method returns map of user details
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> fetchUserInfo( HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);		
		ResponseEntity<Object> result = null;		
		Map<String,Object> userMap = new HashMap<String,Object>();
		try {
			String uri = ekaConnectHost +"/api/userinfo";			
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));			
			HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
			long start = System.currentTimeMillis();
			result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Object.class);
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of connect userinfo API Call:" + (end - start) + "ms" );
			if (result != null) {
				Object userInfo = result.getBody();
				userMap = (Map)userInfo;
			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from connect userinfo API or result is empty"));
				
			}			
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("***fetchUserInfo response " + userMap));
		} catch (HttpClientErrorException he) {			
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("HttpClientErrorException inside fetchUserInfo() while calling connect userinfo API-> "
							+ he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders()+": "+he));
			String errcodemsg = commonService.getErrorMessage("SC043", "HttpClientErrorException inside fetchUserInfo() while calling connect userinfo API.Please try again!:", "supplierconnect");
			throw new ConnectException(errcodemsg + he.getLocalizedMessage());
		} catch (HttpStatusCodeException ex) {			
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("RestClientException inside fetchUserInfo()while calling connect userinfo API -> "
							+ ex.getRawStatusCode() + "" + ex.getResponseBodyAsString() + ex.getResponseHeaders()+": "+ex));
			String errcodemsg = commonService.getErrorMessage("SC043", "HttpClientErrorException inside fetchUserInfo() while calling connect userinfo API.Please try again!:", "supplierconnect");
			throw new ConnectException(errcodemsg + ex.getLocalizedMessage());
		} catch (Exception e) {			
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception in fetchUserInfo method while calling Platform currentUser API:"+ e.getLocalizedMessage()));
			String errcodemsg = commonService.getErrorMessage("SC044", "Exception in fetchUserInfo method while calling Platform currentUser API:", "supplierconnect");
			throw new ConnectException(errcodemsg + e.getLocalizedMessage());
		}// different exceptions need to be identified and handled in future code changes
		return userMap;
	}
	/**
	 * Usage:The <i>fetchGMR</i> method fetches gmr details for the given internal gmr ref no .
	 *  
	 * @param input
	 * 				The Map containing input values - intGmrRefNo
	 * @return MAP This method returns map of gmr details
	 */
	public Map<String, Object> fetchGMR(Object input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);
		ResponseEntity<Object> gmrResponseEntity = null;
		String gmrJson = null;
		try {
			logger.info(Logger.EVENT_SUCCESS,
					ESAPI.encoder().encodeForHTML("fetchGMR : intGmrRefNo : " + input == null ? "" : input.toString()));
			String ctrmUri = getPropertyFromConnect(req, EKA_CTRM_HOST);
			String gmrURI = ctrmUri + VIEWGMR_URL_PATH;
			headers.add("Content-Type", "application/json;charset=UTF-8");
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			HttpEntity<Object> entity = new HttpEntity<Object>(input, headers);
			gmrResponseEntity = restTemplate.exchange(gmrURI, HttpMethod.POST, entity, Object.class);
			if (gmrResponseEntity != null) {
				Object economicObj = gmrResponseEntity.getBody();
				ObjectMapper mapper = new ObjectMapper();
				gmrJson = mapper.writeValueAsString(economicObj);
			} else {
				logger.info(Logger.EVENT_SUCCESS,
						ESAPI.encoder().encodeForHTML("No Response from CTRM viewGMR API or result is empty"));
				gmrJson = new String();
			}
		} catch (HttpClientErrorException he) {
			logger.error(Logger.EVENT_FAILURE, ESAPI.encoder()
					.encodeForHTML("HttpClientErrorException inside fetchGMR() while calling CTRM viewGmr API-> "
							+ he.getRawStatusCode() + "" + he.getResponseBodyAsString() + he.getResponseHeaders() + ": "
							+ he));
			String errcodemsg = commonService.getErrorMessage("SC003",
					"HttpClientErrorException inside fetchGMR() while calling CTRM viewGmr API . Please try again!:",
					"supplierconnect");
			throw new ConnectException(errcodemsg + he.getLocalizedMessage());
		} catch (HttpStatusCodeException ex) {
			logger.error(Logger.EVENT_FAILURE,
					ESAPI.encoder()
							.encodeForHTML("HttpStatusCodeException inside fetchGMR()while calling CTRM viewGmr API -> "
									+ ex.getRawStatusCode() + "" + ex.getResponseBodyAsString()
									+ ex.getResponseHeaders() + ": " + ex));
			String errcodemsg = commonService.getErrorMessage("SC066",
					"HttpStatusCodeException inside fetchGMR() while calling CTRM viewGmr API . Please try again!",
					"supplierconnect");
			throw new ConnectException(errcodemsg + ex.getLocalizedMessage());

		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,
					ESAPI.encoder()
							.encodeForHTML("ResourceAccessException inside fetchGMR()while calling CTRM viewGmr API -> "
									+ resourceAccessException.getLocalizedMessage()));
			String errcodemsg = commonService.getErrorMessage("SC046",
					"error in fetching fetchGMR data from specified ctrm host:", "supplierconnect");
			throw new ConnectException(errcodemsg + resourceAccessException.getLocalizedMessage());
		} catch (Exception e) {
			logger.error(Logger.EVENT_FAILURE, ESAPI.encoder().encodeForHTML(
					"Exception in fetchGMR method while calling CTRM viewGmr API:" + e.getLocalizedMessage()));
			String errcodemsg = commonService.getErrorMessage("SC007",
					"Exception in fetchGMR method while calling CTRM viewGmr API:", "supplierconnect");
			throw new ConnectException(errcodemsg + e.getLocalizedMessage());
		}
		Map<String, Object> gmrMap = new Gson().fromJson(gmrJson.toString(), new TypeToken<Map<String, Object>>() {
		}.getType());

		return gmrMap;
	}	
	/**
	 * Usage:The <i>createGMREmail</i> method will prepare the values to be subsitituted in the email that will be sent
	 * when gmr is created or updated.	 * 
	 * 
	 * @param emailMap
	 * 						The Map which contains GMR values that will be substituted in the subject and email body
	 * like gmr refno,suppliername etc.	
	 * 
	 *  @param req
	 * 						The HttpServletRequest object
	 * 
	 */
	public Object prepareGMREmail(Map<String,Object> emailMap,HttpServletRequest req) {
		String email = null;
		try {
			logger.debug(Logger.EVENT_SUCCESS,("Inside prepareGMREmail method.")+emailMap);
			Map<String, String> propMap = new HashMap<String, String>();
			Map<String,String> emailValueMap = new HashMap<String,String>();
			long start = System.currentTimeMillis();
			propMap = getPropertyListFromConnect(req, SUPPLIERCONNECT_UUID);							
			String location = (String)emailMap.get(INCO_LOCATION);
			String approvalStatus = (String)emailMap.getOrDefault("approvalStatus",null);
			String toAddr = null;
			String incoEmail = null;
			if (location.toUpperCase().contains(FILTER_CITY_1) || location.toUpperCase().contains(FILTER_CITY_4)
					|| location.toUpperCase().contains(FILTER_CITY_5)) {
				toAddr = propMap.get("mail_toaddr_ronnskar");
				incoEmail = toAddr;
			} else if (location.toUpperCase().contains(FILTER_CITY_2) || location.toUpperCase().contains(FILTER_CITY_6)) {
				toAddr = propMap.get("mail_toaddr_harjavalta");
				incoEmail = toAddr;
			} else {
				toAddr = propMap.get("mail_toaddr_default");
				incoEmail = toAddr;
			}
			if (!StringUtils.isEmpty(approvalStatus) && approvalStatus.equalsIgnoreCase("Incomplete")) {
				String platformUrl = propMap.getOrDefault("platform_url","");
				String emailAddrs = getEmailAddrsFromPlatform(emailMap,req,platformUrl);
				//emailAddrs = "vijayalakshmi.nair@eka1.com";
				toAddr = emailAddrs;
				emailMap.put("email", incoEmail);
			}			
			emailValueMap.put("subject", propMap.getOrDefault(emailMap.get(SUBJECT),""));
			emailValueMap.put("message", propMap.getOrDefault(emailMap.get(MAIL_BODY),""));
			emailValueMap.put("toAddr", toAddr);
			emailValueMap.put("fromAddr", propMap.getOrDefault(emailMap.get(MAIL_FROM_ADDR),"noreply@ekaplus.com"));
			email = sendGMREmail(emailValueMap,emailMap,req);	
			logger.info(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML(email));	
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of prepareGMREmail API Call:" + (end - start) + "ms" );
		}catch(ConnectException e) {
			logger.error(Logger.EVENT_FAILURE,("Exception in  prepareGMREmail."),e);
			throw e;
		}catch(Exception e) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Error in method prepareGMREmail."),e);
			String errlocalMsg = commonService.getErrorMessage("SC046", "Unable to send Email", "supplierconnect");							
			throw new ConnectException(errlocalMsg,e,e.getLocalizedMessage(),"SC046");			
		}
		return email;
	}
	/**
	 * Usage:Calling platform api to get list of email id's of user belonging to the input role and cpname
	 * @param emailMap
	 * @param req
	 * @param platformUrl
	 * @return
	 */
	private String getEmailAddrsFromPlatform(Map<String,Object> emailMap,HttpServletRequest req
			,String platformUrl) {
		String emailInfo = "";
		try {	
			HttpHeaders header1 = commonService.getHttpHeader(req);	
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", header1.getFirst("authorization"));
			ResponseEntity<Object> result = null;		
			Map<String,Object> inpMap = new HashMap<String,Object>();
			inpMap.put("businessPartner", emailMap.getOrDefault("businessPartner", ""));
			inpMap.put("roles", emailMap.getOrDefault("roles", ""));			
			logger.debug(Logger.EVENT_SUCCESS, "inpMap:"+inpMap);			
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(platformUrl + "/spring/registration/getUserMailByRoleAndBp");
			headers.setContentType(MediaType.APPLICATION_JSON);						
			HttpEntity<Object> httpEntity = new HttpEntity<Object>(inpMap,headers);			
			result = restTemplateGetRequestBody.getRestTemplate().exchange(uriBuilder.build().toUri(),
					HttpMethod.GET, httpEntity, Object.class);
			if (result != null) {
				logger.debug(Logger.EVENT_SUCCESS, "result not null:"+result);
				Object emailObj = result.getBody();
				if(emailObj instanceof Map) {
					List<String> emailList = (List<String>)((Map)emailObj).get("email");
					if(null != emailList && emailList.size()>0) {
						emailInfo = validator.cleanData(String.join(",", emailList));
					}else {
						logger.error(Logger.EVENT_FAILURE, "No EmailId found for "+inpMap);
						throw new ConnectException("No EmailId's found for this User");
					}
				}				
			} else {
				logger.error(Logger.EVENT_FAILURE,("No Response from getUserMailByRoleAndBp API or result is empty"));
				throw new ConnectException("No EmailId's found for this User");
			}
			logger.debug(Logger.EVENT_SUCCESS, "emailInfo:"+emailInfo);
			return emailInfo;
		}catch(Exception e) {
			logger.error(Logger.EVENT_FAILURE,("Exception in getEmailAddrsFromPlatform: "),e);
			String errcodemsg = commonService.getErrorMessage("SC045", "No EmailId found", "supplierconnect");	
			String errlocalMsg = errcodemsg;
			errcodemsg = errcodemsg +" for CP "+emailMap.getOrDefault("businessPartner", "")+" and Role "+emailMap.getOrDefault("roles", "");
			throw new ConnectException(errlocalMsg,e,errcodemsg,"SC045");
		}		
	}
	/**
	 * Usage:The <i>getPropertyListFromConnect</i> method will return map of connect properties
	 * 
	 * @param req
	 * 					The HttpServletRequest
	 * @param appUUID
	 * 					The app id	 
	 *
	 * @return	Map This method returns map of connect properties
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, String> getPropertyListFromConnect(HttpServletRequest req, String appUUID) {
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("inside method getPropertyListFromConnect"));
		String propertyUri = ekaConnectHost + "/property/" + appUUID + "/list";
		Enumeration<String> headerNames = req.getHeaderNames();
		HttpHeaders httpHeaders = new HttpHeaders();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			httpHeaders.add(headerName, req.getHeader(headerName));
		}
		Map<String, Object> inp = new LinkedHashMap<String, Object>();
		HttpEntity<Map> entityForPropRequest = new HttpEntity<Map>(inp, httpHeaders);
		ResponseEntity<Map> propResult = null;
		Map<String, String> propValues = new HashMap<String, String>();
		propResult = restTemplate.exchange(propertyUri, HttpMethod.POST, entityForPropRequest, Map.class);
		propValues = propResult.getBody();
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("method getPropertyListFromConnect ends"));
		return propValues;
	}
	/**
	 * Usage:The <i>replaceWithGMRData</i> method will replace the placeholder words within the < > brackets
	 * in the input string with respective gmr values
	 * 
	 * @param String
	 * 					The input string with placeholder words to be replaced	 
	 *
	 * @return	String The output string with the replaced values
	 */
	private String replaceWithGMRData(String toBeReplaced,Map<String,Object> emailDataMap) {
		return StringSubstitutor.replace(toBeReplaced, emailDataMap, "<", ">");
		
	}
	/**
	 * Usage:The <i>sendGMREmail</i> method will send email with gmr related data when its triggered either 
	 * after gmr creation or gmr/assay update
	 * 
	 * @param emailValueMap
	 * 						The Map which contains values like to address, from address, subject ,message etc.
	 * @param emailDataMap
	 * 						The Map which contains GMR values that will be substituted in the subject and email body
	 * like gmr refno,suppliername etc.	
	 * 
	 *  @param req
	 * 						The HttpServletRequest object 
	 *
	 * @return	String The output string indicates if email was sent successfully or not.
	 */
	private String sendGMREmail(Map<String,String> emailValueMap,Map<String,Object> emailDataMap,HttpServletRequest req) {
		String emailInfo = "";
		try {	
			HttpHeaders headers = commonService.getHttpHeader(req);		
			ResponseEntity<Object> result = null;			
			String toAddr = validator.cleanData(emailValueMap.getOrDefault("toAddr", ""));
			String[] toAddress = null;
			if(!StringUtils.isEmpty(toAddr) && toAddr.contains(","))
				toAddress = toAddr.split(",");				 
			else				
				toAddress = new String[] {toAddr};
//			String[] toAddress = new String[] {validator.cleanData(validator.cleanData("Bramaramba.Kanike@eka1.com"))};
			MailInfo mailInfo = new MailInfo();
			mailInfo.setFromAddress(validator.cleanData(emailValueMap.getOrDefault("fromAddr","noreply@ekaplus.com")));
			mailInfo.setSubject(replaceWithGMRData(validator.cleanData(ESAPI.encoder().encodeForHTML(emailValueMap.getOrDefault("subject", ""))).isEmpty() ? "" : emailValueMap.getOrDefault("subject", ""), emailDataMap));
			String message = replaceWithGMRData(validator.cleanData(ESAPI.encoder().encodeForHTML(emailValueMap.getOrDefault("message", ""))).isEmpty() ? "" : emailValueMap.getOrDefault("message", ""), emailDataMap);
			if (!StringUtils.isEmpty(emailDataMap.getOrDefault("approvalStatus",null)) && ((String)emailDataMap.get("approvalStatus")).equalsIgnoreCase("Incomplete")) {
				if(message.startsWith("*") && message.endsWith("*")) {
					if(message.contains(",")) {
						
					}					
				}
			}			
			mailInfo.setMessage(replaceWithGMRData(validator.cleanData(ESAPI.encoder().encodeForHTML(emailValueMap.getOrDefault("message", ""))).isEmpty() ? "" : emailValueMap.getOrDefault("message", ""), emailDataMap));
			mailInfo.setToAddress(toAddress);
			mailInfo.setContentType("text/html");
			logger.debug(Logger.EVENT_SUCCESS, ESAPI.encoder().encodeForHTML("mailInfo:"+mailInfo));
			String uri = validator.cleanData(ekaConnectHost +"/file/supplierconnect/"+SUPPLIERCONNECT_UUID+"/sendEmail/v2");
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));			
			HttpEntity<Object> httpEntity = new HttpEntity<Object>(mailInfo,headers);					
			result = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Object.class);
			if (result != null) {
				logger.debug(Logger.EVENT_SUCCESS, ESAPI.encoder().encodeForHTML("result not null:"+result));
				Object emailObj = result.getBody();
				if(!StringUtils.isEmpty(emailObj))
					emailInfo = validator.cleanData(emailObj.toString());
			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from connect sendEmail API or result is empty"));				
			}
			logger.debug(Logger.EVENT_SUCCESS, ESAPI.encoder().encodeForHTML("emailInfo:"+emailInfo));
			return emailInfo;
		}catch(Exception e) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("Exception occured while sending email: "),e);
			//return validator.cleanData("Email Couldn't be sent. Please try again.");
			throw e;
		}		
	}
	/**
	 * Usage:The <i>notifyGMRChange</i> method will be called from TRM in case of GMR Create/Edit/Delete/otheroperation 
	 *  and with the gmr details sent from tRM we will refresh the elastic data
	 * 
	 * @param input	The input object consisting of gmr data				
	 * 
	 * @param req	The HttpServletRequest object 
	 *
	 * @return	String The output string indicates status message if notification sent to connect
	 */	
	public String notifyGMRChange(Object input,HttpServletRequest req) {
		String response = "Done";
		try {	
			HttpHeaders headers = commonService.getHttpHeader(req);		
			ResponseEntity<Object> result = null;				
			Map<String,Object> payload = createWorkflowPayload(input);
			
			String uri = ekaConnectHost +"/workflow?deleteOlderRecords=false";
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));			
			HttpEntity<Object> httpEntity = new HttpEntity<Object>(payload,headers);					
			result = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Object.class);
			if (result != null) {
				Object resultObj = result.getBody();
				if(!StringUtils.isEmpty(resultObj))
					response = "Got Response";
			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from connect /workflow API"));				
			}			
			return response;
		}catch(Exception e) {
			logger.error(Logger.EVENT_FAILURE,"Exception occured while notifyGMRChange: ",e);
			response = "GMR Notification Failed.";
			return response;
		}		
	}
	/**
	 *  Usage:The <i>createWorkflowPayload</i> will create workflow payload for refreshing elastic
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Object> createWorkflowPayload(Object input) {
		//JSONObject objJson = new JSONObject(input.toString());
		List<Map<String,Object>> dataList =  new ArrayList<Map<String,Object>>();		
		if(!StringUtils.isEmpty(input) ) {
			dataList =(List<Map<String,Object>>) input;
		}		
		if(!StringUtils.isEmpty(dataList) && dataList.size() >0) {
			dataList.stream().forEach(map -> {
				if(map.containsKey("gmrOperation")) {
					String operation = map.get("gmrOperation").toString();
					map.put("sys__data__state", operation);
				}
			});
		}
		Map<String,Object> outptMap = new LinkedHashMap<String,Object>();
		Map<String,Object> payLdMap = new LinkedHashMap<String,Object>();
		payLdMap.put("payLoadArray", dataList);
		outptMap.put("elastic_pushgmrlist", payLdMap);
		Map<String,Object> payLoadMap = new LinkedHashMap<String,Object>();
		payLoadMap.put("appId", SUPPLIERCONNECT_UUID);
		payLoadMap.put("workflowTaskName", "elastic_pushgmrlist");
		payLoadMap.put("task", "elastic_pushgmrlist");
		payLoadMap.put("payLoadData", "");
		payLoadMap.put("output", outptMap);		
		logger.debug(Logger.EVENT_SUCCESS,("input to connect workflow:"+payLoadMap.toString()));
		return payLoadMap;		
	}
	/**
	 *  Usage:The <i>refreshElasticOnGmrOp</i> will call notifyDataChange endpoint in connect to forcibly refresh
	 *  elastic index with latest gmrlisting data
	 * @param input
	 * @return
	 */	
	public void refreshElasticOnGmrOp(HttpHeaders headers,HttpServletRequest req) {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Map<String,Object> response = new HashMap<String,Object>();
		CompletableFuture.runAsync(() -> {
			ExecutorService chunkExecutor = Executors.newFixedThreadPool(1);
			List<Callable<Map<String, Object>>> tasksList = new ArrayList<>();
			try {
				Callable<Map<String, Object>> callableTask = () -> {
					Map<String, Object> payLoadMap = new LinkedHashMap<String, Object>();					
					payLoadMap.put("appId", SUPPLIERCONNECT_UUID);
					payLoadMap.put("objectAction", "CREATE");
					HttpHeaders headr = new HttpHeaders();
					headr.set("Authorization", headers.getFirst("Authorization"));
					headr.set("X-TenantID", headers.getFirst("X-TenantID"));		
					headr.setContentType(MediaType.APPLICATION_JSON_UTF8);
					HttpEntity<Object> entity = new HttpEntity<Object>(payLoadMap, headr);					
					restTemplate.exchange(
							UriComponentsBuilder.fromHttpUrl(ekaConnectHost).path("/common/pushDataToElastic").build().toUri(),
							HttpMethod.POST, entity, Object.class);
					response.put("Response", "Success");
					return response;
				};
				tasksList.add(callableTask);

			} catch (Exception e) {
				logger.error(Logger.EVENT_FAILURE, "exception occured while calling refreshElasticOnGmrOp ", e);
			}
			try {
				List<Future<Map<String, Object>>> results = chunkExecutor.invokeAll(tasksList);
				for (Future<Map<String, Object>> result : results) {
					result.get();
				}
			} catch (Exception e) {
				logger.error(Logger.EVENT_FAILURE, "Error during chunk call:" + e);
			}
			logger.debug(Logger.EVENT_SUCCESS, "asynch call Completed");
		}, executor);
	}
	/**
	 * @param input
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> fetchAssayAndStockListWithEconomicVal(Object input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);		
		ResponseEntity<Object> result = null;		
		String arrayToJson = null;
		Map<String,Object> finalAssayAndStockMap = new HashMap<>();
		try {
			long start = System.currentTimeMillis();
			String ctrmUri = getPropertyFromConnect(req, EKA_CTRM_HOST);
			String uri = ctrmUri + ASSAY_URL_PATH;			
			String stockUri = ctrmUri + STOCK_URL_PATH;
			headers.add("Content-Type", "application/json;charset=UTF-8");
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			
			ObjectWriter owr = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String inpJson = owr.writeValueAsString(input);
			JSONObject inpJsonObj = new JSONObject(inpJson);
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("intGmrRefNo", inpJsonObj.get("intGmrRefNo"));
			inp.put("gmrRefNo", inpJsonObj.get("gmrRefNo"));
			HttpEntity<Object> entity = new HttpEntity<Object>(inp, headers);			
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchAssayAndStockList : intGmrRefNo : "+inpJsonObj.get("intGmrRefNo")+ " gmrRefNo : "+inpJsonObj.get("gmrRefNo")));				
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from CTRM viewASsay API or result is empty"));
				arrayToJson = new String();
			}
			
			JSONArray jarr = new JSONArray(arrayToJson);
			JSONObject assay = null;
			int index = 0;
			List<String> grdIdList = null;
			Map assayAndStockJson = null;							
			String assayHeaderType = null;
			String assayHeaderTypeVal = null;
			int grdIndex = 0;
			Map<String,List<String>> grdMap = new HashMap<String,List<String>>();			
			String assayTypeName = null;
			String grdId = null;
			String useBolidenAssay = null;
			String qualityId = null;
			boolean containsWNS = false;
			while (jarr.length() > grdIndex) {
				assay = jarr.getJSONObject(grdIndex);
				assayTypeName = assay.get(ASSAY_TYPE).toString();				
				grdId = assay.get("grdId").toString();
				useBolidenAssay =  StringEscapeUtils.escapeJava(assay.get("useBolidenAssay").toString());
				List<String> grdLst = grdMap.get(assayTypeName);
				if(grdLst == null) {
					grdLst = new ArrayList<String>();
					grdLst.add(grdId);
					grdMap.put(assayTypeName,grdLst);
				}else {
					grdMap.get(assayTypeName).add(grdId);
				}				
				grdIndex++;
				grdLst = null;
				qualityId = assay.has("qualityId")?assay.get("qualityId").toString():null;
			}
			if(grdMap.containsKey(WEIGHING_AND_SAMPLING_ASSAY)) {
				containsWNS = true;
			}
			assay = null;
			long ecostart = System.currentTimeMillis(); 
			Map<String, Map<String, String>> elementEconomicMap = fetchGMREconomicValues(inp, req);
			long ecoend = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of GMR Economic API Call:" + (ecoend - ecostart) + "ms" );
			List<Object> contractAssayList = null;
			if(!StringUtils.isEmpty(qualityId) && inpJsonObj.has("pcdiId")) {
				Map<String, Object> inpMap = new LinkedHashMap<String, Object>();
				inpMap.put("pcdiId", inpJsonObj.get("pcdiId"));
				inpMap.put("qualityId", qualityId);	
				logger.debug(Logger.EVENT_SUCCESS, "input for contract api call:"+inpMap);
				String contractAssayResponse = fetchAssayList(inpMap,req);
				long conend = System.currentTimeMillis();
				logger.debug(Logger.EVENT_SUCCESS,	"Total time of contract assay API Call:" + (conend - ecoend) + "ms" );
				if(!StringUtils.isEmpty(contractAssayResponse)) {							
					JSONObject contractAssayJson = new JSONObject(contractAssayResponse);
					//System.out.println(contractAssayJson);
					contractAssayList = contractAssayJson.has("data")?(List<Object>)((JSONArray)contractAssayJson.get("data")).toList():null;
				}
			}else {
				logger.debug(Logger.EVENT_SUCCESS, "qualityId or pcdiId missing.Cannot call contract assay API:");
			}
			List<String> processedAssayList = new ArrayList<String>();
			List<String> assayElemlist = null;
			while (jarr.length() > index) {
				assay = jarr.getJSONObject(index);
				List stockArr = null;
				List assayArr = null;
				index++;
				assayAndStockJson = new HashMap();
				grdIdList = new ArrayList<String>();
				assayHeaderType = assay.get(ASSAY_TYPE).toString();
				String lotNo = StringUtils.isEmpty(assay.get(LOTNO))?"":assay.get(LOTNO).toString();
				assayHeaderTypeVal = assayHeaderType.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
						: (assayHeaderType.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay" : assayHeaderType);

				processedAssayList.add(assayHeaderType);
				List<Map<String, Object>> assaySubLotDtoLst = new Gson().fromJson(
						assay.get("assaySublotMappingDTOs").toString(), new TypeToken<List<Map<String, Object>>>() {
						}.getType());
				// to get Assay rules from contractFinalAssayRulesDTOs property
				List<Map<String, Object>> contractFinalAssayRulesList = new Gson().fromJson(
						assay.get("contractFinalAssayRulesDTOs").toString(),new TypeToken<List<Map<String, Object>>>() {
						}.getType());
				Map<String, String> elementAssayRuleMap = null;
				if (contractFinalAssayRulesList != null) {
					Map<String, Object> assyRule = null;
					Iterator<Map<String, Object>> assayRuleIt = contractFinalAssayRulesList.iterator();
					String finalAssayType = null;
					String assayRule = null;
					elementAssayRuleMap = new HashMap<String, String>();
					while (assayRuleIt.hasNext()) {
						assyRule = (Map<String, Object>) assayRuleIt.next();
						if (assyRule.containsKey(ELEMENT_ID)) {
							finalAssayType = assyRule.get("finalAssayType") == null ? "": assyRule.get("finalAssayType").toString();
							assayRule = finalAssayType.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
									: (finalAssayType.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay"
											: finalAssayType);
							elementAssayRuleMap.put(assyRule.get(ELEMENT_ID).toString(), assayRule);
						}
					}
				}

				Map<String, Object> assaySubLotObj = null;
				Iterator<Map<String, Object>> prodIt = assaySubLotDtoLst.iterator();
				while (prodIt.hasNext()) {
					assaySubLotObj = (Map<String, Object>) prodIt.next();
					if (assaySubLotObj.containsKey("chemicalAttributesDTOs")) {
						List<Map<String, Object>> chemicalDtoList = (List<Map<String, Object>>) assaySubLotObj.get("chemicalAttributesDTOs");
						Map<String, Object> chemicalDtoObj = null;
						Iterator<Map<String, Object>> chemicalIt = chemicalDtoList.iterator();
						Map eachObj = null;
						String elementName = null;
						String elementId = null;
						String typical = null;
						String ratioName = null;
						String showInSupplierConnect = null;
						String umpirePayment = null;
						assayArr = new ArrayList();
						assayElemlist = new ArrayList<String>();
						while (chemicalIt.hasNext()) {
							chemicalDtoObj = (Map<String, Object>) chemicalIt.next();
							elementName = chemicalDtoObj.get(ELEMENT_NAME) == null ? ""	: chemicalDtoObj.get(ELEMENT_NAME).toString();
							elementId = chemicalDtoObj.get(ELEMENT_ID) == null ? ""	: chemicalDtoObj.get(ELEMENT_ID).toString();
							typical = chemicalDtoObj.get(TYPICAL) == null ? "" : chemicalDtoObj.get(TYPICAL).toString();
							ratioName = chemicalDtoObj.get(RATIO_NAME) == null ? ""	: chemicalDtoObj.get(RATIO_NAME).toString();
							showInSupplierConnect = chemicalDtoObj.get(SHOW_IN_SUPPLIER_CONNECT) == null ? "": chemicalDtoObj.get(SHOW_IN_SUPPLIER_CONNECT).toString();
							umpirePayment = chemicalDtoObj.get(ASSAY_WINNER) == null ? ""
									: (chemicalDtoObj.get(ASSAY_WINNER).toString().equalsIgnoreCase(SELF_ASSAY_WINNER)
											? SELF_UMPIRE_PAYMENT
											: chemicalDtoObj.get(ASSAY_WINNER).toString()
													.equalsIgnoreCase(EVEN_ASSAY_WINNER)
															? EVEN_UMPIRE_PAYMENT
															: chemicalDtoObj.get(ASSAY_WINNER).toString()
																	.equalsIgnoreCase(CP_ASSAY_WINNER)
																			? CP_UMPIRE_PAYMENT
																			: "");
							eachObj = new HashMap();
							eachObj.put(ELEMENT_NAME, elementName);
							eachObj.put(ELEMENT_ID, elementId);
							eachObj.put(TYPICAL, typical);
							eachObj.put(RATIO_NAME, ratioName);
							eachObj.put(SHOW_IN_SUPPLIER_CONNECT, showInSupplierConnect);
							eachObj.put(UMPIRE_PAYMENT, umpirePayment);
							eachObj.put("economicValues", elementEconomicMap.get(lotNo+TILDE_SEPARATOR+elementId));
							setAssayValues(eachObj, chemicalDtoObj);
							if (elementAssayRuleMap != null && elementAssayRuleMap.size() > 0) {
								eachObj.put("assayRule", elementAssayRuleMap.getOrDefault(elementId, " ").toString());
							}
							assayElemlist.add(elementId);
							assayArr.add(eachObj);
						}
					}
				}
				if (assay.get(ASSAY_TYPE).toString().equalsIgnoreCase(WEIGHING_AND_SAMPLING_ASSAY)) {
					assayAndStockJson.put("activityDate",assay.get("activityDate") == null ? "" : assay.get("activityDate").toString());
				}
				// if WNS assay type available lotno will be populated else lotno is blank
				if (containsWNS && !assayHeaderTypeVal.contains("Provisional Assay")) {
					assayAndStockJson.put(LOTNO,
							StringUtils.isEmpty(assay.get(LOTNO)) ? "" : assay.get(LOTNO).toString());
				} else {
					assayAndStockJson.put(LOTNO, "");
				}
				grdIdList = grdMap.get(assayHeaderType);
				stockArr = (fetchStockList(inp, grdIdList, req, stockUri)).toList();
				assayAndStockJson.put(STOCK_LIST, stockArr);
				/*
				 * * Adding the missing element from contract assay list as part of jira SC-3068
				 */
				if (!StringUtils.isEmpty(contractAssayList)) {
					logger.debug(Logger.EVENT_SUCCESS, "contractAssayList not null.");
					Iterator<Object> cIt = contractAssayList.iterator();
					Map<String, Object> cObj = null;
					Map eachObj = null;
					while (cIt.hasNext()) {
						String umpirePayment = null;
						cObj = (Map<String, Object>) cIt.next();
						String elementId = cObj.get(ELEMENT_ID) == null ? "" : cObj.get(ELEMENT_ID).toString();
						if (!assayElemlist.contains(elementId)) {
							logger.debug(Logger.EVENT_SUCCESS,"Adding missing element from contract assay " + elementId);
							umpirePayment = cObj
									.get(ASSAY_WINNER) == null
											? ""
											: (cObj.get(ASSAY_WINNER).toString().equalsIgnoreCase(SELF_ASSAY_WINNER)
													? SELF_UMPIRE_PAYMENT
													: cObj.get(ASSAY_WINNER).toString()
															.equalsIgnoreCase(EVEN_ASSAY_WINNER)
																	? EVEN_UMPIRE_PAYMENT
																	: cObj.get(ASSAY_WINNER).toString()
																			.equalsIgnoreCase(CP_ASSAY_WINNER)
																					? CP_UMPIRE_PAYMENT
																					: "");
							eachObj = new HashMap();
							eachObj.put(ELEMENT_NAME, cObj.containsKey(ELEMENT_NAME) ? cObj.get(ELEMENT_NAME) : "");
							eachObj.put(ELEMENT_ID, elementId);
							eachObj.put(TYPICAL, cObj.containsKey(TYPICAL) ? cObj.get(TYPICAL) : "");
							eachObj.put(RATIO_NAME, cObj.containsKey(RATIO_NAME) ? cObj.get(RATIO_NAME) : "");
							eachObj.put(SHOW_IN_SUPPLIER_CONNECT,cObj.containsKey(SHOW_IN_SUPPLIER_CONNECT) ? cObj.get(SHOW_IN_SUPPLIER_CONNECT): "");
							eachObj.put(UMPIRE_PAYMENT, umpirePayment);
							eachObj.put("economicValues", elementEconomicMap.get(lotNo+TILDE_SEPARATOR+elementId));
							eachObj.put("assayRule", cObj.containsKey("assayRule") ? cObj.get("assayRule") : "");
							setAssayValues(eachObj, cObj);
							assayArr.add(eachObj);
						}
					}
				}
				assayAndStockJson.put(ELEMENT_LIST, assayArr);
				List<Object> assayAndStockList = (List) finalAssayAndStockMap.get(assayHeaderTypeVal);
				if (assayAndStockList == null) {
					assayAndStockList = new ArrayList<>();
					assayAndStockList.add(assayAndStockJson);
					finalAssayAndStockMap.put(assayHeaderTypeVal, assayAndStockList);
				} else {
					assayAndStockList.add(assayAndStockJson);
					finalAssayAndStockMap.put(assayHeaderTypeVal, assayAndStockList);
				}
				assayAndStockList = null;
			}			
			finalAssayAndStockMap.put(USE_BOLIDEN_ASSAY,useBolidenAssay);			
			long end = System.currentTimeMillis();
			logger.debug(Logger.EVENT_SUCCESS,	"Total time of GMR Economic API Call:" + (end - start) + "ms" );
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("httpClientErrorException in fetchAssayAndStockListWithEconomicVal : "),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC015", "httpClientErrorException inside method fetchAssayAndStockListWithEconomicVal", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("ResourceAccessException in fetchAssayAndStockListWithEconomicVal: "),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC013", "ResourceAccessException inside method fetchAssayAndStockListWithEconomicVal", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC014", "fetchAssayAndStockListWithEconomicVal API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg1+ e.getLocalizedMessage());
		}
		logger.debug(Logger.EVENT_SUCCESS,("fetchAssayAndStockListWithEconomicVal response:"+finalAssayAndStockMap.size()));
		return finalAssayAndStockMap;
	}
	@SuppressWarnings("unchecked")
	public Object fetchAssayAndStockList1(Object input, HttpServletRequest req) {
		HttpHeaders headers = commonService.getHttpHeader(req);
		String response = null;
		ResponseEntity<Object> result = null;		
		String arrayToJson = null;
		try {
			String ctrmUri = getPropertyFromConnect(req, EKA_CTRM_HOST);
			String uri = ctrmUri + ASSAY_URL_PATH;			
			String stockUri = ctrmUri + STOCK_URL_PATH;
			headers.add("Content-Type", "application/json;charset=UTF-8");
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			// headers.add("username", "e-bolprpa");
			ObjectWriter owr = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String inpJson = owr.writeValueAsString(input);
			JSONObject inpJsonObj = new JSONObject(inpJson);
			Map<String, Object> inp = new LinkedHashMap<String, Object>();
			inp.put("intGmrRefNo", inpJsonObj.get("intGmrRefNo"));
			inp.put("gmrRefNo", inpJsonObj.get("gmrRefNo"));
			HttpEntity<Object> entity = new HttpEntity<Object>(inp, headers);
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);
			restTemplate.setMessageConverters(messageConverters);*/
			logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchAssayAndStockList : intGmrRefNo : "+inpJsonObj.get("intGmrRefNo")+ " gmrRefNo : "+inpJsonObj.get("gmrRefNo")));			
			result = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);

			if (result != null) {
				Object obj = result.getBody();
				ObjectMapper mapper = new ObjectMapper();
				arrayToJson = mapper.writeValueAsString(obj);

			} else {
				logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("No Response from CTRM viewASsay API or result is empty"));
				arrayToJson = new String();
			}
			
			JSONArray jarr = new JSONArray(arrayToJson);
			JSONObject assay = null;
			int index = 0;
			List<String> grdIdList = null;
			JSONObject assayAndStockJson = null;
			JSONArray assayAndStockArr = null;
			JSONObject finalAssayAndStockJson = new JSONObject();			
			String assayHeaderType = null;
			String assayHeaderTypeVal = null;
			int grdIndex = 0;
			Map<String,List<String>> grdMap = new HashMap<String,List<String>>();			
			String assayTypeName = null;
			String grdId = null;
			String useBolidenAssay = null;
			while (jarr.length() > grdIndex) {
				assay = jarr.getJSONObject(grdIndex);
				assayTypeName = assay.get(ASSAY_TYPE).toString();				
				grdId = assay.get("grdId").toString();
				useBolidenAssay =  StringEscapeUtils.escapeJava(assay.get("useBolidenAssay").toString());
				List<String> grdLst = grdMap.get(assayTypeName);
				if(grdLst == null) {
					grdLst = new ArrayList<String>();
					grdLst.add(grdId);
					grdMap.put(assayTypeName,grdLst);
				}else {
					grdMap.get(assayTypeName).add(grdId);
				}				
				grdIndex++;
				grdLst = null;
			}
			assay = null;
			//Map<String, Map<String, String>> elementEconomicMap = fetchGMREconomicValues(inp, req);
			List<String> processedAssayList = new ArrayList<String>();
			while (jarr.length() > index) {
				assay = jarr.getJSONObject(index);
				JSONArray stockArr = null;
				JSONArray assayArr = null;
				index++;
				assayAndStockJson = new JSONObject();
				assayAndStockArr = new JSONArray();
				grdIdList = new ArrayList<String>();
				assayHeaderType = assay.get(ASSAY_TYPE).toString();				
				
					// grdIdList.add(assay.get("grdId").toString());
					if (!processedAssayList.contains(assayHeaderType)) {
						processedAssayList.add(assayHeaderType);
						List<Map<String, Object>> assaySubLotDtoLst = new Gson().fromJson(
								assay.get("assaySublotMappingDTOs").toString(),
								new TypeToken<List<Map<String, Object>>>() {
								}.getType());
						// to get Assay rules from contractFinalAssayRulesDTOs property
						List<Map<String, Object>> contractFinalAssayRulesList = new Gson().fromJson(
								assay.get("contractFinalAssayRulesDTOs").toString(),
								new TypeToken<List<Map<String, Object>>>() {
								}.getType());
						Map<String, String> elementAssayRuleMap = null;
						if (contractFinalAssayRulesList != null) {
							Map<String, Object> assyRule = null;
							Iterator<Map<String, Object>> assayRuleIt = contractFinalAssayRulesList.iterator();
							String finalAssayType = null;
							String assayRule = null;
							elementAssayRuleMap = new HashMap<String, String>();
							while (assayRuleIt.hasNext()) {
								assyRule = (Map<String, Object>) assayRuleIt.next();
								if (assyRule.containsKey(ELEMENT_ID)) {
									finalAssayType = assyRule.get("finalAssayType") == null ? ""
											: assyRule.get("finalAssayType").toString();
									assayRule = finalAssayType.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
											: (finalAssayType.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay"
													: finalAssayType);
									elementAssayRuleMap.put(assyRule.get(ELEMENT_ID).toString(), assayRule);
								}
							}
						}

						Map<String, Object> assaySubLotObj = null;
						Iterator<Map<String, Object>> prodIt = assaySubLotDtoLst.iterator();						
						while (prodIt.hasNext()) {
							assaySubLotObj = (Map<String, Object>) prodIt.next();
							if (assaySubLotObj.containsKey("chemicalAttributesDTOs")) {
								List<Map<String, Object>> chemicalDtoList = (List<Map<String, Object>>) assaySubLotObj
										.get("chemicalAttributesDTOs");
								Map<String, Object> chemicalDtoObj = null;
								Iterator<Map<String, Object>> chemicalIt = chemicalDtoList.iterator();
								JSONObject eachObj = null;
								String elementName = null;
								String elementId = null;
								String typical = null;
								String ratioName = null;
								String showInSupplierConnect = null;
								String umpirePayment = null;
								assayArr = new JSONArray();
								while (chemicalIt.hasNext()) {
									chemicalDtoObj = (Map<String, Object>) chemicalIt.next();
									elementName = chemicalDtoObj.get(ELEMENT_NAME) == null ? ""
											: chemicalDtoObj.get(ELEMENT_NAME).toString();
									elementId = chemicalDtoObj.get(ELEMENT_ID) == null ? ""
											: chemicalDtoObj.get(ELEMENT_ID).toString();
									typical = chemicalDtoObj.get(TYPICAL) == null ? ""
											: chemicalDtoObj.get(TYPICAL).toString();
									ratioName = chemicalDtoObj.get(RATIO_NAME) == null ? ""
											: chemicalDtoObj.get(RATIO_NAME).toString();
									showInSupplierConnect = chemicalDtoObj.get(SHOW_IN_SUPPLIER_CONNECT) == null ? ""
											: chemicalDtoObj.get(SHOW_IN_SUPPLIER_CONNECT).toString();
									umpirePayment = chemicalDtoObj.get(ASSAY_WINNER) == null ? ""
											: (chemicalDtoObj.get(ASSAY_WINNER).toString().equalsIgnoreCase(SELF_ASSAY_WINNER)?SELF_UMPIRE_PAYMENT
													:chemicalDtoObj.get(ASSAY_WINNER).toString().equalsIgnoreCase(EVEN_ASSAY_WINNER)?EVEN_UMPIRE_PAYMENT 
															:chemicalDtoObj.get(ASSAY_WINNER).toString().equalsIgnoreCase(CP_ASSAY_WINNER)?CP_UMPIRE_PAYMENT
																	:"");
									eachObj = new JSONObject();
									eachObj.put(ELEMENT_NAME, elementName);
									eachObj.put(ELEMENT_ID, elementId);
									eachObj.put(TYPICAL, typical);
									eachObj.put(RATIO_NAME, ratioName);
									eachObj.put(SHOW_IN_SUPPLIER_CONNECT, showInSupplierConnect);
									eachObj.put(UMPIRE_PAYMENT, umpirePayment);
									//eachObj.put("economicValues", elementEconomicMap.get(elementId));
									setAssayValues(eachObj.toMap(), chemicalDtoObj);
									if (elementAssayRuleMap != null && elementAssayRuleMap.size() > 0) {
										eachObj.put("assayRule",
												elementAssayRuleMap.getOrDefault(elementId, " ").toString());
									}									
									assayArr.put(eachObj);
								}
							}
						}
						if (assay.get(ASSAY_TYPE).toString().equalsIgnoreCase(WEIGHING_AND_SAMPLING_ASSAY)) {
							assayAndStockJson.put("activityDate",
									assay.get("activityDate") == null ? "" : assay.get("activityDate").toString());
						}
						grdIdList = grdMap.get(assayHeaderType);
						stockArr = fetchStockList(inp, grdIdList, req, stockUri);
						assayAndStockJson.put(STOCK_LIST, stockArr);
						assayAndStockJson.put(ELEMENT_LIST, assayArr);
						assayAndStockArr.put(assayAndStockJson);
						assayHeaderTypeVal = assayHeaderType.equalsIgnoreCase("Self Assay") ? "Boliden Assay"
								: (assayHeaderType.equalsIgnoreCase("CounterParty Assay") ? "Supplier Assay"
										: assayHeaderType);
						finalAssayAndStockJson.put(assayHeaderTypeVal, assayAndStockArr);
					}
				 

			}
			
			finalAssayAndStockJson.put(USE_BOLIDEN_ASSAY,useBolidenAssay);
			response = finalAssayAndStockJson.toString();
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("HttpClientErrorException in fetchAssayandStockList : "),httpClientErrorException);
			String errMsg1 = commonService.getErrorMessage("SC012", "httpClientErrorException inside method fetchAssayAndStockList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (ResourceAccessException resourceAccessException) {
			logger.error(Logger.EVENT_FAILURE,ESAPI.encoder().encodeForHTML("ResourceAccessException in fetchAssayAndStockList: "),resourceAccessException);
			String errMsg1 = commonService.getErrorMessage("SC013", "ResourceAccessException in fetchAssayAndStockList", "supplierconnect");
			throw new ContractDataException(errMsg1);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg1 = commonService.getErrorMessage("SC014", "fetchAssayAndStockList API Call Failed.", "supplierconnect");
			throw new ConnectException(errMsg1+ e.getLocalizedMessage());
		}
		logger.debug(Logger.EVENT_SUCCESS,ESAPI.encoder().encodeForHTML("fetchAssayAndStockList response:"+response));
		return response;
	}
}
