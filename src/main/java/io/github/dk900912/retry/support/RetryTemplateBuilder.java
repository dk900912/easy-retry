package io.github.dk900912.retry.support;

import io.github.dk900912.retry.backoff.BackOffPolicy;
import io.github.dk900912.retry.backoff.ExponentialBackOffPolicy;
import io.github.dk900912.retry.classifier.BinaryExceptionClassifier;
import io.github.dk900912.retry.policy.BinaryExceptionClassifierRetryPolicy;
import io.github.dk900912.retry.policy.CompositeRetryPolicy;
import io.github.dk900912.retry.policy.MaxAttemptsRetryPolicy;
import io.github.dk900912.retry.policy.RetryPolicy;

/**
 * Fluent API to configure new instance of RetryTemplate.
 *
 * @author dukui
 */
public class RetryTemplateBuilder {

	private RetryPolicy retryPolicy;

	private BackOffPolicy backOffPolicy;

	private BinaryExceptionClassifier exceptionClassifier = new BinaryExceptionClassifier();

	public RetryTemplateBuilder maxAttempts(int maxAttempts) {
		this.retryPolicy = new MaxAttemptsRetryPolicy(maxAttempts);
		return this;
	}

	public RetryTemplateBuilder exponentialBackoff(long initialInterval, double multiplier, long maxInterval) {
		this.backOffPolicy = new ExponentialBackOffPolicy(initialInterval, maxInterval, multiplier);
		return this;
	}

	public RetryTemplateBuilder retryOn(Class<? extends Throwable> throwable) {
		this.exceptionClassifier.registerClassifiedException(throwable);
		return this;
	}

	public RetryTemplate build() {
		RetryTemplate retryTemplate = new RetryTemplate();

		if (this.retryPolicy == null) {
			this.retryPolicy = new MaxAttemptsRetryPolicy();
		}

		if (this.backOffPolicy == null) {
			this.backOffPolicy = new ExponentialBackOffPolicy();
		}
		retryTemplate.setBackOffPolicy(this.backOffPolicy);

		CompositeRetryPolicy finalPolicy = new CompositeRetryPolicy();
		finalPolicy.setPolicies(new RetryPolicy[] { this.retryPolicy, new BinaryExceptionClassifierRetryPolicy(this.exceptionClassifier)});
		retryTemplate.setRetryPolicy(finalPolicy);

		return retryTemplate;
	}
}
