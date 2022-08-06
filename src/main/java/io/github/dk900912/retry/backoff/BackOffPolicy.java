package io.github.dk900912.retry.backoff;

import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.exception.BackOffInterruptedException;
import io.github.dk900912.retry.support.RetryTemplate;

/**
 * Strategy interface to control back off between attempts in a single
 * {@link RetryTemplate retry operation}.
 *
 * @author dukui
 */
public interface BackOffPolicy {

	/**
	 * Start a new block of back off operations.
	 * @param context the {@link RetryContext} context, which might contain information
	 * that we can use to decide how to proceed.
	 * @return the implementation-specific {@link BackOffContext} or '<code>null</code>'.
	 */
	BackOffContext start(RetryContext context);

	/**
	 * Back off/pause in an implementation-specific fashion.
	 * @param backOffContext the {@link BackOffContext}
	 * @throws BackOffInterruptedException if the attempt at back off is interrupted.
	 */
	void backOff(BackOffContext backOffContext) throws BackOffInterruptedException;

}
