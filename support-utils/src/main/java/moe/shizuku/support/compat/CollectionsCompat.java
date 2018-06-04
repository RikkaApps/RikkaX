package moe.shizuku.support.compat;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionsCompat {

    @NonNull
    public static <E, D, T extends Collection<D>> T mapTo(@NonNull Collection<E> source,
                                                          @NonNull T destination,
                                                          @NonNull Function<E, D> mapper) {
        for (E element : source) {
            destination.add(mapper.apply(element));
        }
        return destination;
    }

    @NonNull
    public static <E, D> List<D> mapToList(@NonNull Collection<E> source,
                                           @NonNull Function<E, D> mapper) {
        return mapTo(source, new ArrayList<D>(), mapper);
    }

    @NonNull
    public static <E, T extends Collection<E>> T filterTo(@NonNull Collection<E> source,
                                                          @NonNull T destination,
                                                          @NonNull Predicate<E> predicate) {
        for (E element : source) {
            if (predicate.apply(element)) {
                destination.add(element);
            }
        }
        return destination;
    }

    @NonNull
    public static <E> List<E> filterToList(@NonNull Collection<E> source,
                                           @NonNull Predicate<E> predicate) {
        return filterTo(source, new ArrayList<E>(), predicate);
    }

    public static <E> boolean anyMatch(@NonNull Collection<E> source,
                                       @NonNull Predicate<E> predicate) {
        for (E element : source) {
            if (predicate.apply(element)) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean noneMatch(@NonNull Collection<E> source,
                                        @NonNull Predicate<E> predicate) {
        return !anyMatch(source, predicate);
    }

    public static <E> boolean allMatch(@NonNull Collection<E> source,
                                       @NonNull Predicate<E> predicate) {
        for (E element : source) {
            if (!predicate.apply(element)) {
                return false;
            }
        }
        return true;
    }

}
