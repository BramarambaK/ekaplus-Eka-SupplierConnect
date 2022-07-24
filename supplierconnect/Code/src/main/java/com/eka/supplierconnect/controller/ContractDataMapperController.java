package com.eka.supplierconnect.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eka.supplierconnect.service.ContractDataMapperService;
import com.eka.supplierconnect.validator.CommonValidator;

@RestController
@RequestMapping("/collection")
public class ContractDataMapperController {
	final static  Logger logger = ESAPI.getLogger(ContractDataMapperController.class);
	@Autowired
	ContractDataMapperService contractDataMapperService;
	@Autowired
	CommonValidator validator;

	@GetMapping(value="/fetchDeliveryItems")
	public String fetchDeliveryItems(HttpServletRequest req) {		
		String response = contractDataMapperService.fetchDeliveryItemList(req);
		return response;		
	}

	@PostMapping("/createGmr")
	public Object createGmr(@Valid @RequestHeader("X-TenantID") String tenantID, @RequestBody String gmr,
			HttpServletRequest req) {		
		logger.info(Logger.EVENT_SUCCESS,"inside createGMR :" + ESAPI.encoder().encodeForHTML(gmr.toString()));
		Object response = contractDataMapperService.createGmr(validator.htmlCheck(gmr), req, validator.cleanData(tenantID));
		return response;		
	}

	@PostMapping("/assayList")
	public String fetchAssayList(@RequestBody Object input, HttpServletRequest req) {
		String response = contractDataMapperService.fetchAssayList(input, req);
		return response;
	}

	@PostMapping("/mdm/entity")
	public Map<String, Object> fetchEntityList(@RequestBody List<Map<String, String>> input, HttpServletRequest req) {
		Map<String, Object> response = contractDataMapperService.fetchEntityList(input, req);
		return response;
	}

	@PostMapping("/mdm/incoTerm")
	public Map<String, Object> fetchIncoTermList(@RequestBody List<Map<String, String>> input, HttpServletRequest req) {
		Map<String, Object> response = contractDataMapperService.fetchIncoTermList(input, req);
		return response;
	}

	@PostMapping("/mdm/destination")
	public Map<String, Object> fetchDestinationList(@RequestBody List<Map<String, String>> input, HttpServletRequest req) {
		Map<String, Object> response = contractDataMapperService.fetchDestinationList(input, req);
		return response;
	}

	@PostMapping("/assayAndStockList")
	public @ResponseBody Map<String, Object> fetchAssayAndStockList(@RequestBody Object input, HttpServletRequest req) {
		Map<String, Object> response = contractDataMapperService.fetchAssayAndStockList(input, req);
		return response;
	}
	@PostMapping("/assayAndStockListWithEconomcVal")
	public @ResponseBody Map<String, Object> fetchAssayAndStockListWithEconomicVal(@RequestBody Object input, HttpServletRequest req) {
		Map<String, Object> response = contractDataMapperService.fetchAssayAndStockListWithEconomicVal(input, req);
		return response;
	}	
	/**
	 * Usage:The <i>fetchGMR</i> api fetches gmr details for the given internal gmr ref no .
	 *  
	 * @param input
	 * 				The Map containing input values - intGmrRefNo
	 * @return MAP This method returns map of gmr details
	 */
	@PostMapping("/fetchGMR")
	public Map<String, Object> fetchGMR(@RequestBody Object input, HttpServletRequest req) {
		Map<String, Object> response = contractDataMapperService.fetchGMR(input, req);
		return response;
	}
	
	/**
	 * Usage:The <i>sendGMREmail</i> api to be called after successful create gmr/edit gmr/edit assay will send email to the address configured
	 * as per the inco location.
	 *  
	 * @param input
	 * 				The Map containing input values - email related information like subject/message and gmr information like supplier name,location,
	 * gmrrefno,create/update date etc
	 * @return String This method returns success or failure message
	 */
	@PostMapping("/sendGMREmail")
	public Object sendGMREmail(@RequestBody Map<String, Object> input , HttpServletRequest req) {
		Object emailResponse = contractDataMapperService.prepareGMREmail(input, req);
		return emailResponse;
	}
	/**
	 * Usage:The <i>notifyGMRChange</i> api will be called from TRM in case of GMR Create/Edit/Delete/otheroperation.
	 *  
	 * @param input
	 * 				The input consisting of gmrdetails and operation(Create/Edit/Delete/otheroperation).
	 * @return MAP This method returns map of gmr details
	 */
	@PostMapping("/notifyGMRChange")
	public @ResponseBody Object  notifyGMRChange(@RequestBody Object input, HttpServletRequest req) {
			logger.info(Logger.EVENT_SUCCESS,"notifyGMRChange:" + (StringUtils.isEmpty(input)?input:input.toString()));
			String  response = contractDataMapperService.notifyGMRChange(input, req);
			return response;		
	}
}
