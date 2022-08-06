package io.github.dk900912.retry.backoff;

import java.io.Serializable;

/**
 * Strategy interface for backoff policies to delegate the pausing of execution.
 *
 * @author dukui
 */
public interface Sleeper extends Serializable {

	/**
	 * Pause for the specified period using whatever means available.
	 * @param backOffPeriod the backoff period
	 * @throws InterruptedException the exception when interrupted
	 */
	void sleep(long backOffPeriod) throws InterruptedException;

}
