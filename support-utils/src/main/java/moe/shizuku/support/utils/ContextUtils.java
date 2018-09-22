package moe.shizuku.support.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;

import androidx.annotation.Nullable;

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
    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }
}
