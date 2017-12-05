package moe.shizuku.support.utils;

/**
 * Created by rikka on 2017/11/23.
 */

public abstract class Singleton<T> {

    private T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized(this) {
            if (mInstance == null) {
                mInstance = create();
            }

            return mInstance;
        }
    }
}
