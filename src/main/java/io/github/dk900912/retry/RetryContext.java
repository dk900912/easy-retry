package io.github.dk900912.retry;

/**
 * Low-level access to ongoing retry operation. Normally not needed by clients, but can be
 * used to alter the course of the retry, e.g. force an early termination.
 *
 * @author dukui
 */
public interface RetryContext {

	/**
	 * Set exhausted flag (true)
	 */
	void setExhausted();

	/**
	 * Public accessor for the exhausted flag.
	 * @return true if the flag has been set.
	 */
	boolean isExhausted();

	/**
	 * Accessor for the parent context if retry blocks are nested.
	 * @return the parent or null if there is none.
	 */
	RetryContext getParent();

	/**
	 * Counts the number of retry attempts.
	 * @return the number of retries.
	 */
	int getRetryCount();

	/**
	 * Accessor for the exception object that caused the current retry.
	 * @return the last exception that caused a retry, or possibly null.
	 */
	Throwable getLastThrowable();

}
