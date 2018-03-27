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
        Spanned htmlDescription;
        if (Build.VERSION.SDK_INT >= 24) {
            htmlDescription = Html.fromHtml(source, flags, imageGetter, tagHandler);
        } else {
            htmlDescription = Html.fromHtml(source, imageGetter, tagHandler);
        }

        String htmlDescriptionString = htmlDescription.toString();

        int len = htmlDescriptionString.length();
        int st = 0;
        while ((st < len) && htmlDescriptionString.charAt(len - 1) == ' ') {
            len--;
        }
        htmlDescriptionString = htmlDescriptionString.substring(0, len);

        return (Spanned) htmlDescription.subSequence(0, htmlDescriptionString.length());
    }
}
