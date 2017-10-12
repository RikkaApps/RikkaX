package moe.shizuku.support.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by rikka on 2017/10/12.
 */

public class ClipboardUtils {

    public static boolean put(Context context, CharSequence str) {
        return put(context, ClipData.newPlainText("label", str));
    }

    public static boolean put(Context context, ClipData clipData) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clipData);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
