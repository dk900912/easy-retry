package io.github.dk900912.retry.policy;

import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.RetryContextSupport;
import io.github.dk900912.retry.classifier.BinaryExceptionClassifier;

/**
 * A policy, that is based on {@link BinaryExceptionClassifier}. Usually, binary
 * classification is enough for retry purposes.
 *
 * @author dukui
 */
public class BinaryExceptionClassifierRetryPolicy implements RetryPolicy {

	private final BinaryExceptionClassifier exceptionClassifier;

	public BinaryExceptionClassifierRetryPolicy(BinaryExceptionClassifier exceptionClassifier) {
		this.exceptionClassifier = exceptionClassifier;
	}

	@Override
	public boolean canRetry(RetryContext context) {
		Throwable t = context.getLastThrowable();
		return t == null || exceptionClassifier.classify(t);
	}

	@Override
	public void close(RetryContext context) {}

	@Override
	public void registerThrowable(RetryContext context, Throwable throwable) {
		((RetryContextSupport) context).registerThrowable(throwable);
	}

	@Override
	public RetryContext open(RetryContext parent) {
		return new RetryContextSupport(parent);
	}

}
