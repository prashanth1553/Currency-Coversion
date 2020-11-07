package com.currencyconversion.exception;

public class CustomResponseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomResponseException(String msg) {
		super(msg);
	}

}
