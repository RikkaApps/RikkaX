package rikka.lazy;

/**
 * Lazy initialization helper.
 *
 * @param <T> Type of the value
 */
public interface Lazy<T> {

    /**
     * Get the value. If the value hasn't initialized, {@link LazyInitializer#invoke()} will be called.
     */
    T get();

    /**
     * Check if the value is initialized.
     */
    boolean isInitialized();
}
