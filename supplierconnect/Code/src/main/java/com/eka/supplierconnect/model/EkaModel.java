package com.eka.supplierconnect.model;

import org.springframework.data.annotation.Id;

/**
 * @author Ranjan.Jha
 * 
 *         Any model can extend this as a base class to reuse the common
 *         properties across project
 */

public class EkaModel {

	@Id
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
