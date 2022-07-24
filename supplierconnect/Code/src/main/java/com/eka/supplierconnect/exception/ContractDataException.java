package com.eka.supplierconnect.exception;

public class ContractDataException extends RuntimeException {   
	
	private static final long serialVersionUID = 1L;

	public ContractDataException(String message) {
        super(message);
    }

    public ContractDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
