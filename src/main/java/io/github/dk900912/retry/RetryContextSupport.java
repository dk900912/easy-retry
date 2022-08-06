package io.github.dk900912.retry;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author dukui
 */
public class RetryContextSupport implements RetryContext {

	private final RetryContext parent;

	private boolean exhausted = false;

	private int count;

	private Throwable lastException;

	public RetryContextSupport(RetryContext parent) {
		this.parent = parent;
	}

	public RetryContext getParent() {
		return this.parent;
	}

	@Override
	public boolean isExhausted() {
		return this.exhausted;
	}

	public void setExhausted() {
		this.exhausted = true;
	}

	@Override
	public int getRetryCount() {
		return this.count;
	}

	@Override
	public Throwable getLastThrowable() {
		return this.lastException;
	}

	public void registerThrowable(Throwable throwable) {
		this.lastException = throwable;
		if (throwable != null) {
			this.count++;
		}
	}


	@Override
	public String toString() {
		return new StringJoiner(", ", RetryContextSupport.class.getSimpleName() + "[", "]")
				.add("role=" + ((parent == null) ? "parent-" + Objects.hash(this) : "child-" + Objects.hash(this)))
				.add("exhausted=" + exhausted)
				.add("count=" + count)
				.add("lastException=" + lastException)
				.toString();
	}
}