package io.github.dk900912.retry.policy;

import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.RetryContextSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link RetryPolicy} that composes a list of other policies and delegates calls to
 * them in order.
 *
 * @author dukui
 */
public class CompositeRetryPolicy implements RetryPolicy {

	private RetryPolicy[] policies = new RetryPolicy[0];

	@Override
	public boolean canRetry(RetryContext context) {
		RetryContext[] contexts = ((CompositeRetryContext) context).contexts;
		RetryPolicy[] policies = ((CompositeRetryContext) context).policies;

		boolean retryable = true;
		for (int i = 0; i < contexts.length; i++) {
			if (!policies[i].canRetry(contexts[i])) {
				retryable = false;
			}
		}

		return retryable;
	}

	@Override
	public void close(RetryContext context) {
		RetryContext[] contexts = ((CompositeRetryContext) context).contexts;
		RuntimeException exception = null;
		for (int i = 0; i < contexts.length; i++) {
			try {
				policies[i].close(contexts[i]);
			} catch (RuntimeException e) {
				if (exception == null) {
					exception = e;
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	@Override
	public RetryContext open(RetryContext parent) {
		List<RetryContext> list = new ArrayList<>();
		for (RetryPolicy policy : this.policies) {
			list.add(policy.open(parent));
		}
		return new CompositeRetryContext(parent, list, this.policies);
	}

	@Override
	public void registerThrowable(RetryContext context, Throwable throwable) {
		RetryContext[] contexts = ((CompositeRetryContext) context).contexts;
		RetryPolicy[] policies = ((CompositeRetryContext) context).policies;
		for (int i = 0; i < contexts.length; i++) {
			policies[i].registerThrowable(contexts[i], throwable);
		}
		((RetryContextSupport) context).registerThrowable(throwable);
	}

	public void setPolicies(RetryPolicy[] policies) {
		this.policies = Arrays.asList(policies).toArray(new RetryPolicy[policies.length]);
	}

	private static class CompositeRetryContext extends RetryContextSupport {

		RetryContext[] contexts;

		RetryPolicy[] policies;

		public CompositeRetryContext(RetryContext parent, List<RetryContext> contexts, RetryPolicy[] policies) {
			super(parent);
			this.contexts = contexts.toArray(new RetryContext[contexts.size()]);
			this.policies = policies;
		}
	}
}
