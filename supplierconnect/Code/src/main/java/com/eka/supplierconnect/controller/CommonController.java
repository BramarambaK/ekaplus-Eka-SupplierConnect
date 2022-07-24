package com.eka.supplierconnect.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Attributes;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eka.supplierconnect.service.ManifestService;

/**
 * Class Common Controller which would be used for common handler etc.
 */
@RestController
public class CommonController {
	private static final Logger logger = ESAPI.getLogger(CommonController.class);
	
	@Autowired
	ManifestService manifestService;	
	
	/**
	 * Gets the manifest attributes.
	 *
	 * @return the manifest attributes
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@GetMapping(value = "/common/getManifestInfo")
	public ResponseEntity<Attributes> getManifestAttributes() {
		ResponseEntity<Attributes> respEntity=new ResponseEntity<Attributes>(manifestService.getManifestAttributes(), HttpStatus.OK);
		logger.debug(Logger.EVENT_SUCCESS, "Manifest info called.");
		return respEntity;    		
	}
	
	
}
