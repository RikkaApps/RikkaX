package rikka.core.util;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

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

    public static boolean resolveBoolean(Resources.Theme theme, @AttrRes int attrId, boolean defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        boolean res = a.getBoolean(0, defaultResult);
        a.recycle();
        return res;
    }

    public static Drawable resolveDrawable(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        Drawable res = a.getDrawable(0);
        a.recycle();
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Typeface resolveFont(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        Typeface res = a.getFont(0);
        a.recycle();
        return res;
    }

    public static float resolveFloat(Resources.Theme theme, @AttrRes int attrId, float defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        float res = a.getFloat(0, defaultResult);
        a.recycle();
        return res;
    }

    public static float resolveDimension(Resources.Theme theme, @AttrRes int attrId, float defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        float res = a.getDimension(0, defaultResult);
        a.recycle();
        return res;
    }

    public static int resolveDimensionPixelOffset(Resources.Theme theme, @AttrRes int attrId, int defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int res = a.getDimensionPixelOffset(0, defaultResult);
        a.recycle();
        return res;
    }

    public static int resolveDimensionPixelSize(Resources.Theme theme, @AttrRes int attrId, int defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int res = a.getDimensionPixelSize(0, defaultResult);
        a.recycle();
        return res;
    }

    public static int resolveInt(Resources.Theme theme, @AttrRes int attrId, int defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int res = a.getInt(0, defaultResult);
        a.recycle();
        return res;
    }

    public static int resolveInteger(Resources.Theme theme, @AttrRes int attrId, int defaultResult) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int res = a.getInteger(0, defaultResult);
        a.recycle();
        return res;
    }

    public static CharSequence resolveText(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        CharSequence res = a.getText(0);
        a.recycle();
        return res;
    }

    public static CharSequence[] resolveTextArray(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        CharSequence[] res = a.getTextArray(0);
        a.recycle();
        return res;
    }
}
