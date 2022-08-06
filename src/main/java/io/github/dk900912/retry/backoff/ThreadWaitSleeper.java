package io.github.dk900912.retry.backoff;

/**
 * Simple {@link Sleeper} implementation that just blocks the current Thread with sleep
 * period.
 *
 * @author dukui
 */
public class ThreadWaitSleeper implements Sleeper {
	@Override
	public void sleep(long backOffPeriod) throws InterruptedException {
		Thread.sleep(backOffPeriod);
	}
}
