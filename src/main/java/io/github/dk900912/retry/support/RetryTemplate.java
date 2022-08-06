package io.github.dk900912.retry.support;

import io.github.dk900912.retry.RetryCallback;
import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.RetryOperations;
import io.github.dk900912.retry.backoff.BackOffContext;
import io.github.dk900912.retry.backoff.BackOffPolicy;
import io.github.dk900912.retry.exception.BackOffInterruptedException;
import io.github.dk900912.retry.exception.RetryException;
import io.github.dk900912.retry.policy.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template class that simplifies the execution of operations with retry semantics.
 * <p>
 * This class is thread-safe and suitable for concurrent access when executing operations
 * and when performing configuration changes.
 *
 * @author dukui
 */
public class RetryTemplate implements RetryOperations {

	private static final Logger logger = LoggerFactory.getLogger(RetryTemplate.class);

	private BackOffPolicy backOffPolicy;

	private RetryPolicy retryPolicy;

	public static RetryTemplateBuilder builder() {
		return new RetryTemplateBuilder();
	}

	public void setBackOffPolicy(BackOffPolicy backOffPolicy) {
		this.backOffPolicy = backOffPolicy;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	@Override
	public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
		return doExecute(retryCallback);
	}

	/**
	 * Execute the callback once if the policy dictates that we can, otherwise execute the
	 * recovery callback.
	 * @param retryCallback the {@link RetryCallback}
	 * @param <T> the type of the return value
	 * @param <E> the exception type to throw
	 * @throws E an exception if the retry operation fails
	 * @return T the retried value
	 */
	private <T, E extends Throwable> T doExecute(RetryCallback<T, E> retryCallback) throws E {
		RetryPolicy retryPolicy = this.retryPolicy;
		BackOffPolicy backOffPolicy = this.backOffPolicy;

		RetryContext context = open(retryPolicy);
		RetrySynchronizationManager.register(context);

		try {
			BackOffContext backOffContext = backOffPolicy.start(context);
			while (canRetry(retryPolicy, context) && !context.isExhausted()) {
				try {
					logger.info("{}", context);
					return retryCallback.doWithRetry(context);
				} catch (Throwable e) {
					registerThrowable(retryPolicy, context, e);
					if (canRetry(retryPolicy, context) && !context.isExhausted()) {
						try {
							backOffPolicy.backOff(backOffContext);
						} catch (BackOffInterruptedException ex) {
							logger.info("Abort retry because interrupted: count={}", context.getRetryCount());
							throw ex;
						}
					}
				}
			}
			return handleRetryExhausted(context);
		} catch (Throwable e) {
			throw RetryTemplate.<E>wrapIfNecessary(e);
		} finally {
			close(retryPolicy, context);
			RetrySynchronizationManager.clear();
			logger.info("0={===> {} <===}=0", context);
		}
	}

	private boolean canRetry(RetryPolicy retryPolicy, RetryContext context) {
		return retryPolicy.canRetry(context);
	}

	private RetryContext open(RetryPolicy retryPolicy) {
		return retryPolicy.open(RetrySynchronizationManager.getContext());
	}

	private void close(RetryPolicy retryPolicy, RetryContext context) {
		retryPolicy.close(context);
		context.setExhausted();
	}

	private void registerThrowable(RetryPolicy retryPolicy, RetryContext context, Throwable e) {
		retryPolicy.registerThrowable(context, e);
	}

	private <T> T handleRetryExhausted(RetryContext context) throws Throwable {
		context.setExhausted();
		throw wrapIfNecessary(context.getLastThrowable());
	}

	private static <E extends Throwable> E wrapIfNecessary(Throwable throwable) throws RetryException {
		if (throwable instanceof Error) {
			throw (Error) throwable;
		} else if (throwable instanceof Exception) {
			@SuppressWarnings("unchecked")
			E rethrow = (E) throwable;
			return rethrow;
		} else {
			throw new RetryException("Exception in retry", throwable);
		}
	}
}
