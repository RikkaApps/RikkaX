package rikka.lazy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Thread-safe lazy initialization.
 * Lock is used to ensure that only one thread can initialize the value.
 *
 * @param <T> Type of the value
 */
public class SynchronizedLazy<T> implements Lazy<T> {

    private volatile Object value = LazyInternal.UNINITIALIZED_VALUE;
    private final LazyInitializer<T> initializer;
    private final Object lock;

    /**
     * Create the Lazy instance.
     */
    public SynchronizedLazy(LazyInitializer<T> initializer) {
        this(initializer, null);
    }

    /**
     * Create the Lazy instance with specific lock object.
     *
     * @param lock The lock object, if the value is null, the instance itself will be used
     */
    public SynchronizedLazy(LazyInitializer<T> initializer, @Nullable Object lock) {
        this.initializer = initializer;
        this.lock = lock == null ? this : lock;
    }

    @Override
    public T get() {
        if (value != LazyInternal.UNINITIALIZED_VALUE) {
            //noinspection unchecked
            return (T) value;
        }

        synchronized (lock) {
            if (value != LazyInternal.UNINITIALIZED_VALUE) {
                //noinspection unchecked
                return (T) value;
            }

            value = initializer.invoke();
            //noinspection unchecked
            return (T) value;
        }
    }

    @Override
    public final boolean isInitialized() {
        return value != LazyInternal.UNINITIALIZED_VALUE;
    }

    @NonNull
    @Override
    public String toString() {
        return "SynchronizedLazy{" +
                "value=" + (value == LazyInternal.UNINITIALIZED_VALUE ? "(uninitialized)" : value.toString()) +
                '}';
    }
}
