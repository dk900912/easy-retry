package io.github.dk900912.retry.classifier;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A {@link Classifier} for exceptions that has only two classes (true and false).
 * If the object to be classified is one of the provided types, then the non-default
 * value is returned.
 *
 * @author dukui
 */
public class BinaryExceptionClassifier implements Classifier<Throwable, Boolean> {

	private final ConcurrentMap<Class<? extends Throwable>, Boolean> classified;

	public BinaryExceptionClassifier() {
		classified = new ConcurrentHashMap<>();
	}

	public void registerClassifiedException(Class<? extends Throwable> classifiedException) {
		classified.put(classifiedException, true);
	}

	@Override
	public Boolean classify(Throwable classifiable) {
		if (classifiable == null) {
			return false;
		}

		Class<? extends Throwable> exceptionClass = classifiable.getClass();
		return this.classified.getOrDefault(exceptionClass, false);
	}
}
