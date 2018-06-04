package moe.shizuku.support.compat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Fung Gwo on 2018/2/25.
 */

public class Optional<T> {

    private final @Nullable T value;

    private static final Optional<?> EMPTY = new Optional<>(null);

    private Optional(@Nullable T value) {
        this.value = value;
    }

    public static <T> Optional<T> of(@Nullable T value) {
        return value != null ? new Optional<>(value) : Optional.<T>empty();
    }

    public static <T> Optional<T> of(@NonNull Callable<T> valueGetter) {
        T value = null;
        try {
            value = valueGetter.call();
        } catch (Exception ignored) {

        }
        return Optional.of(value);
    }

    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public @NonNull T orElse(T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public @Nullable T get() {
        return value;
    }

    public void ifNotNull(Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public static @NonNull <T> List<T> safeList(@Nullable List<T> nullableList) {
        return Optional.of(nullableList).orElse(Collections.<T>emptyList());
    }

}
