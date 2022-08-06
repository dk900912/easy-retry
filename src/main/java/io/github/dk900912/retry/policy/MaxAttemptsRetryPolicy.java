package io.github.dk900912.retry.policy;


import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.RetryContextSupport;

/**
 * Simple retry policy that is aware only about attempt count and retries a fixed number
 * of times. The number of attempts includes the initial try.
 *
 * @author dukui
 */
public class MaxAttemptsRetryPolicy implements RetryPolicy {

	private static final int DEFAULT_MAX_ATTEMPTS = 3;

	private int maxAttempts = DEFAULT_MAX_ATTEMPTS;

	public MaxAttemptsRetryPolicy() {}

	public MaxAttemptsRetryPolicy(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	@Override
	public boolean canRetry(RetryContext context) {
		return context.getRetryCount() < this.maxAttempts;
	}

	@Override
	public void close(RetryContext status) {}

	@Override
	public void registerThrowable(RetryContext context, Throwable throwable) {
		((RetryContextSupport) context).registerThrowable(throwable);
	}

	@Override
	public RetryContext open(RetryContext parent) {
		return new RetryContextSupport(parent);
	}

}
