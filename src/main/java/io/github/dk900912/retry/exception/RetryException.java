package io.github.dk900912.retry.exception;

/**
 * @author dukui
 */
public class RetryException extends RuntimeException {

	public RetryException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RetryException(String msg) {
		super(msg);
	}

}
