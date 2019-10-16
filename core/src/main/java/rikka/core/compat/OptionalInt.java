package rikka.core.compat;


import androidx.annotation.Nullable;

/**
 * Created by Fung Gwo on 2018/2/22.
 */

public class OptionalInt {

    private final Integer value;

    public static final OptionalInt EMPTY = new OptionalInt(null);

    private OptionalInt(@Nullable Integer value) {
        this.value = value;
    }

    public static OptionalInt of(int value) {
        return new OptionalInt(value);
    }

    public static OptionalInt of(String value) {
        try {
            int result = Integer.valueOf(value);
            return OptionalInt.of(result);
        } catch (Exception e) {
            return OptionalInt.EMPTY;
        }
    }

    public int getAsInt() {
        return orElse(0);
    }

    public int orElse(int defaultValue) {
        return value == null ? defaultValue : value;
    }

}
