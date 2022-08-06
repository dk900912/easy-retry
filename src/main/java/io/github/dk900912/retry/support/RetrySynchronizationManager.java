package io.github.dk900912.retry.support;

import io.github.dk900912.retry.RetryCallback;
import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.RetryOperations;

/**
 * Global variable support for retry clients. Normally it is not necessary for clients to
 * be aware of the surrounding environment because a {@link RetryCallback} can always use
 * the context it is passed by the enclosing {@link RetryOperations}. But occasionally it
 * might be helpful to have lower level access to the ongoing {@link RetryContext} so we
 * provide a global accessor here. The mutator methods ({@link #clear()} and
 * {@link #register(RetryContext)} should not be used except internally by
 * {@link RetryOperations} implementations.
 *
 * @author dukui
 */
public final class RetrySynchronizationManager {

	private RetrySynchronizationManager() {}

	private static final ThreadLocal<RetryContext> CONTEXT = new ThreadLocal<>();

	public static RetryContext getContext() {
		return CONTEXT.get();
	}

	public static RetryContext register(RetryContext context) {
		RetryContext oldContext = getContext();
		CONTEXT.set(context);
		return oldContext;
	}

	public static RetryContext clear() {
		RetryContext value = getContext();
		RetryContext parent = value == null ? null : value.getParent();
		CONTEXT.set(parent);
		return value;
	}

}
