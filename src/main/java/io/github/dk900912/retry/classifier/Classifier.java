package io.github.dk900912.retry.classifier;

import java.io.Serializable;

/**
 * Interface for a classifier. At its simplest a {@link Classifier} is just a map from
 * objects of one type to objects of another type.
 *
 * @author dukui
 * @param <C> the type of the thing to classify
 * @param <T> the output of the classifier
 */
public interface Classifier<C, T> extends Serializable {

	/**
	 * Classify the given object and return an object of a different type, possibly an
	 * enumerated type.
	 * @param classifiable the input object. Can be null.
	 * @return an object. Can be null, but implementations should declare if this is the
	 * case.
	 */
	T classify(C classifiable);

}
