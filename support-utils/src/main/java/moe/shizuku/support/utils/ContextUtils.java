package moe.shizuku.support.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Created by rikka on 2017/9/9.
 */

public class ContextUtils {

    public static File getExternalCacheFile(Context context, String filename) {
        File parent = context.getExternalCacheDir();
        if (parent != null) {
            return new File(parent, filename);
        }

        return new File(context.getCacheDir(), filename);
    }

    public static File getExternalFile(Context context, String filename) {
        File parent = context.getExternalFilesDir(null);
        if (parent != null) {
            return new File(parent, filename);
        }

        return new File(context.getFilesDir(), filename);
    }

    @Nullable
    public static <T extends Activity> T getActivity(@Nullable Context context) {
        if (context instanceof Activity) {
            //noinspection unchecked
            return (T) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }

    @NonNull
    public static <T extends Activity> T requireActivity(@NonNull Context context) {
        return requireNonNull(ContextUtils.<T>getActivity(requireNonNull(context)));
    }
}
