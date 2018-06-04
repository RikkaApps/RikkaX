package moe.shizuku.support.compat;

/**
 * A Predicate can determine a true or false value for any input of its
 * parameterized type. For example, a {@code RegexPredicate} might implement
 * {@code Predicate<String>}, and return true for any String that matches its
 * given regular expression.
 * <p/>
 * <p/>
 * Implementors of Predicate which may cause side effects upon evaluation are
 * strongly encouraged to state this fact clearly in their API documentation.
 */
public interface Predicate<T> {
    boolean apply(T t);
}
