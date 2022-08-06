package io.github.dk900912.retry.policy;

import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.RetryOperations;

import java.io.Serializable;

/**
 * A {@link RetryPolicy} is responsible for allocating and managing resources needed by
 * {@link RetryOperations}. The {@link RetryPolicy} allows retry operations to be aware of
 * their context. Context can be internal to the retry framework, e.g. to support nested
 * retries.
 *
 * @author dukui
 *
 */
public interface RetryPolicy extends Serializable {

	/**
	 * @param context the current retry status
	 * @return true if the operation can proceed
	 */
	boolean canRetry(RetryContext context);

	/**
	 * Acquire resources needed for the retry operation. The callback is passed in so that
	 * marker interfaces can be used and a manager can collaborate with the callback to
	 * set up some state in the status token.
	 * @param parent the parent context if we are in a nested retry.
	 * @return a {@link RetryContext} object specific to this policy.
	 *
	 */
	RetryContext open(RetryContext parent);

	/**
	 * @param context a retry status created by the {@link #open(RetryContext)} method of
	 * this policy.
	 */
	void close(RetryContext context);

	/**
	 * Called once per retry attempt, after the callback fails.
	 * @param context the current status object.
	 * @param throwable the exception to throw
	 */
	void registerThrowable(RetryContext context, Throwable throwable);

}
