package com.bohai.subAccount.exception;

/**
 * 异常类
 * @author caojia
 *
 */
public class FutureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8641651971143311657L;
	
	private String errorCode;
	
	public FutureException(String errorCode, String errorMsg){
		super(errorMsg);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	

}
