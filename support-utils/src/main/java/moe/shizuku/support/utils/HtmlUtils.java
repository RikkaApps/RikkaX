package moe.shizuku.support.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by rikka on 2017/10/12.
 */

public class HtmlUtils {

    private static final int FROM_HTML_MODE_LEGACY;

    static {
        if (Build.VERSION.SDK_INT >= 24) {
            FROM_HTML_MODE_LEGACY = Html.FROM_HTML_MODE_LEGACY;
        } else {
            FROM_HTML_MODE_LEGACY = 0;
        }
    }

    public static Spanned fromHtml(String source) {
        return fromHtml(source, FROM_HTML_MODE_LEGACY, null, null);
    }

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter) {
        return fromHtml(source, FROM_HTML_MODE_LEGACY, imageGetter, null);
    }

    public static Spanned fromHtml(String source, Html.TagHandler tagHandler) {
        return fromHtml(source, FROM_HTML_MODE_LEGACY, null, tagHandler);
    }

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter, Html.TagHandler tagHandler) {
        return fromHtml(source, FROM_HTML_MODE_LEGACY, imageGetter, tagHandler);
    }

    public static Spanned fromHtml(String source, int flags, Html.ImageGetter imageGetter, Html.TagHandler tagHandler) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= 24) {
            spanned = Html.fromHtml(source, flags, imageGetter, tagHandler);
        } else {
            spanned = Html.fromHtml(source, imageGetter, tagHandler);
        }

        int i = spanned.length();
        do {
            i --;
        } while (i >= 0 && Character.isWhitespace(spanned.charAt(i)));

        return (Spanned) spanned.subSequence(0, i + 1);
    }
}
