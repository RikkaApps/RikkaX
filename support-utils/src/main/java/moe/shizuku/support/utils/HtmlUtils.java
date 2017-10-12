package moe.shizuku.support.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by rikka on 2017/10/12.
 */

public class HtmlUtils {

    public static Spanned fromHtml(String source) {
        Spanned htmlDescription;
        if (Build.VERSION.SDK_INT >= 24) {
            htmlDescription = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            htmlDescription = Html.fromHtml(source);
        }

        String descriptionWithOutExtraSpace = htmlDescription.toString().trim();

        return (Spanned) htmlDescription.subSequence(0, descriptionWithOutExtraSpace.length());
    }
}
