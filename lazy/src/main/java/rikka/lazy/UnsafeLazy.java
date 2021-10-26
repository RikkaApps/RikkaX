package rikka.lazy;

import androidx.annotation.NonNull;

/**
 * "Unsafe "lazy initialization.
 * <p>
 * No locks are used when {@link #get()} is called. If {@link #get()} is called from multiple threads,
 * the behavior is undefined.
 *
 * @param <T> Type of the value
 */
public class UnsafeLazy<T> implements Lazy<T> {

    private volatile Object value = LazyInternal.UNINITIALIZED_VALUE;
    private final LazyInitializer<T> initializer;

    /**
     * Create the Lazy instance.
     */
    public UnsafeLazy(LazyInitializer<T> initializer) {
        this.initializer = initializer;
    }

    @Override
    public final T get() {
        if (value == LazyInternal.UNINITIALIZED_VALUE) {
            value = initializer.invoke();
        }
        //noinspection unchecked
        return (T) value;
    }

    @Override
    public final boolean isInitialized() {
        return value != LazyInternal.UNINITIALIZED_VALUE;
    }

    @NonNull
    @Override
    public String toString() {
        return "UnsafeLazy{" +
                "value=" + (value == LazyInternal.UNINITIALIZED_VALUE ? "(uninitialized)" : value.toString()) +
                '}';
    }
}
