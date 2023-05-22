package hr.fer.oprpp1.custom.collections;

/**
 * A functional class that has a method that processes an object.
 *
 * @author Ian Golob
 */
public interface Processor<T> {

    void process(T value);

}
