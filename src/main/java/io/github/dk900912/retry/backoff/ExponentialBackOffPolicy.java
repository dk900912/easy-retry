package io.github.dk900912.retry.backoff;

import io.github.dk900912.retry.RetryContext;
import io.github.dk900912.retry.exception.BackOffInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;

/**
 * Implementation of {@link BackOffPolicy} that increases the back off period for each
 * retry attempt in a given setup to a limit.
 *
 * This implementation is thread-safe and suitable for concurrent access.
 *
 * @author dukui
 */
public class ExponentialBackOffPolicy implements BackOffPolicy {

	private static final Logger logger = LoggerFactory.getLogger(ExponentialBackOffPolicy.class);

	public static final long DEFAULT_INITIAL_INTERVAL = 500L;

	public static final long DEFAULT_MAX_INTERVAL = 10000L;

	public static final double DEFAULT_MULTIPLIER = 2;

	private long initialInterval = DEFAULT_INITIAL_INTERVAL;

	private long maxInterval = DEFAULT_MAX_INTERVAL;

	private double multiplier = DEFAULT_MULTIPLIER;

	private final Sleeper sleeper = new ThreadWaitSleeper();

	public ExponentialBackOffPolicy() {}

	public ExponentialBackOffPolicy(long initialInterval, long maxInterval, double multiplier) {
		this.initialInterval = initialInterval > 1 ? initialInterval : 1;
		this.multiplier = Math.max(multiplier, 1.0);
		this.maxInterval = maxInterval > 0 ? maxInterval : 1;
	}

	@Override
	public BackOffContext start(RetryContext context) {
		return new ExponentialBackOffContext(this.initialInterval, this.multiplier, this.maxInterval);
	}

	@Override
	public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
		ExponentialBackOffContext context = (ExponentialBackOffContext) backOffContext;
		try {
			long sleepTime = context.getSleepAndIncrement();
			logger.info("Sleeping for {}", sleepTime);
			this.sleeper.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
		}
	}

	public long getInitialInterval() {
		return this.initialInterval;
	}

	public long getMaxInterval() {
		return this.maxInterval;
	}

	public double getMultiplier() {
		return this.multiplier;
	}

	static class ExponentialBackOffContext implements BackOffContext {

		private long interval;

		private final double multiplier;

		private final long maxInterval;

		public ExponentialBackOffContext(long interval, double multiplier, long maxInterval) {
			this.interval = interval;
			this.multiplier = multiplier;
			this.maxInterval = maxInterval;
		}

		public synchronized long getSleepAndIncrement() {
			long sleep = getInterval();
			long max = getMaxInterval();
			if (sleep > max) {
				sleep = max;
			} else {
				this.interval = getNextInterval();
			}
			return sleep;
		}

		protected long getNextInterval() {
			return (long) (this.interval * getMultiplier());
		}

		public double getMultiplier() {
			return this.multiplier;
		}

		public long getInterval() {
			return this.interval;
		}

		public long getMaxInterval() {
			return this.maxInterval;
		}
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ExponentialBackOffPolicy.class.getSimpleName() + "[", "]")
				.add("initialInterval=" + initialInterval)
				.add("maxInterval=" + maxInterval)
				.add("multiplier=" + multiplier)
				.toString();
	}
}
