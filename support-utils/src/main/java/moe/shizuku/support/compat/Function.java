package moe.shizuku.support.compat;

/**
 * Represents a function.
 *
 * @param <I> the type of the input to the function
 * @param <O> the type of the output of the function
 */
public interface Function<I, O> {
    /**
     * Applies this function to the given input.
     *
     * @param input the input
     * @return the function result.
     */
    O apply(I input);
}
