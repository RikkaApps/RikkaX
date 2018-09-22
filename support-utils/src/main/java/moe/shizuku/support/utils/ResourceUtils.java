package moe.shizuku.support.utils;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;

public class ResourceUtils {

    private static String packageName;

    public static void setPackageName(String packageName) {
        ResourceUtils.packageName = packageName;
    }

    /**
     * Check if the current language is RTL
     *
     * @param configuration Configuration
     * @return Result
     */
    public static boolean isRtl(Configuration configuration) {
        return configuration.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Check if the current ui mode is night mode
     *
     * @param configuration Configuration
     * @return Result
     */
    public static boolean isNightMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_YES) > 0;
    }

    public static String resolveString(Resources resources, String identifier, String defaultResult) {
        assert packageName != null;
        int id = resources.getIdentifier(identifier, "string", packageName);
        if (id > 0) {
            return resources.getString(id);
        }
        return defaultResult;
    }

    public static @ColorInt int resolveColor(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int res = a.getColor(0, 0);
        a.recycle();
        return res;
    }

    public static ColorStateList resolveColorStateList(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        ColorStateList res = a.getColorStateList(0);
        a.recycle();
        return res;
    }
}
