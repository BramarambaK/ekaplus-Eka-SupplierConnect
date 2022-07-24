package com.eka.supplierconnect.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eka.supplierconnect.exception.ConnectException;
import com.eka.supplierconnect.model.FileModel;
import com.eka.supplierconnect.model.SupplierDocument;
import com.eka.supplierconnect.service.ContractDataMapperService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class UtilClass {
	final static Log logger = LogFactory.getLog(UtilClass.class);
	@Autowired
	private ContractDataMapperService contractDataService;

	@Autowired
	CommonService commonService;

	/**
	 * Usage:The <i>cpMatchCheckForDocumentOperations</i> method checks if the cp matches for the entity this method is called from
	 *  
	 * @param input
	 * 				The Map containing input values - intGmrRefNo
	 * @return boolean This method returns boolean value yes if the cp is matching else throws exception
	 */
	public boolean cpMatchCheckForDocumentOperations(Object inputStr, HttpServletRequest req) {
		boolean isMatch = true;
		Map<String,Object> userMap = contractDataService.fetchUserInfo(req);			
		if(StringUtils.isEmpty(userMap)) {
			logger.error("User details map is empty.");
			userMap = new HashMap<String,Object>();				
		}
		List<String> userCPNames = new ArrayList<String>();
		List<String> userPermCodes = new ArrayList<String>();
		SupplierDocument supplierDocument = null;
		String internalGmrRefNo = null;
		if(inputStr instanceof FileModel) {
			FileModel fileModel =(FileModel)inputStr;
			if(!StringUtils.isEmpty(fileModel.getInternalRefNo()) ) {
				internalGmrRefNo = fileModel.getInternalRefNo();
			}else {
				supplierDocument = new Gson().fromJson(fileModel.getOtherAttributes().toString(),
						new TypeToken<SupplierDocument>() {
						}.getType());
				internalGmrRefNo = supplierDocument.getInternalRefNo();	
			}			
		}		
		if(new Integer(3).equals(userMap.get("userType"))) {
			Map<String,Object> input = new HashMap<String,Object>();
			input.put("intGmrRefNo", internalGmrRefNo);
			Map<String,Object> gmrMap = contractDataService.fetchGMR(input,req);
			if(userMap.containsKey("businessPartys")) {
				userCPNames=(List<String>)userMap.get("businessPartys");
			}
			if(!StringUtils.isEmpty(userCPNames) && !userCPNames.contains(gmrMap.getOrDefault("supplier",""))
					) {
				req.setAttribute("isCpCheck", "Y");
				logger.error("Cp names of the contract and the GMR doesn;t match or Business Partner list is empty.");
				throw new ConnectException(commonService.getErrorMessage("SC027", "Cp names of the contract and the GMR doesn;t match or Business Partner list is empty.", "supplierconnect"));
			}
		}else {
			if(userMap.containsKey("permCodes")) {
				userPermCodes=(List<String>)userMap.get("permCodes");
			}
			if(!userPermCodes.contains("ADMIN_FARMER_APPROVAL")) {
				req.setAttribute("isCpCheck", "Y");
				logger.error("User Not Authorized.");
				throw new ConnectException(commonService.getErrorMessage("SC037", "User Not Authorized.", "supplierconnect"));
			}			
		}		
		return isMatch;
	}	
}
