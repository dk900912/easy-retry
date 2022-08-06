package io.github.dk900912.retry.exception;

/**
 * @author dukui
 */
public class BackOffInterruptedException extends RetryException {

	public BackOffInterruptedException(String msg) {
		super(msg);
	}

	public BackOffInterruptedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
