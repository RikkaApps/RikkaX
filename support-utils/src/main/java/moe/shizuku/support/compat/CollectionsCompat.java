package moe.shizuku.support.compat;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    /**
     * Clone ArrayList safely
     *
     * It is usually used like:
     * "bundle.putExtra("my_list", CollectionsCompat.cloneArrayList(list).get());"
     *
     * Returning Optional<> makes this api flexible, you can use it like:
     * "CollectionsCompat.cloneArrayList(list).orElse(new ArrayList<>());"
     *
     * @param collection Original data
     * @param <E> Element type
     * @return Optional of ArrayList clone
     */
    @NonNull
    public static <E> Optional<ArrayList<E>> cloneArrayList(@Nullable Collection<E> collection) {
        return Optional.of(collection).map(new Function<Collection<E>, ArrayList<E>>() {
            @Override
            public ArrayList<E> apply(Collection<E> input) {
                return new ArrayList<>(input);
            }
        });
    }

}
