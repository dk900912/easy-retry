package io.github.dk900912.retry;

/**
 * Defines the basic set of operations implemented by {@link RetryOperations} to execute
 * operations with configurable retry behaviour.
 *
 * @author dukui
 */
public interface RetryOperations {

	/**
	 * Execute the supplied {@link RetryCallback} with the configured retry semantics. See
	 * implementations for configuration details.
	 * @param <T> the return value
	 * @param retryCallback the {@link RetryCallback}
	 * @param <E> the exception to throw
	 * @return the value returned by the {@link RetryCallback} upon successful invocation.
	 * @throws E any {@link Exception} raised by the {@link RetryCallback} upon unsuccessful retry.
	 * @throws E the exception thrown
	 */
	<T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;

}
