package moe.shizuku.support.utils;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;


public class ResourceUtils {

    /**
     * Check if the current language is RTL
     *
     * @param context Context
     * @return Result
     */
    public static boolean isRtl(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Check if the current ui mode is nighjt mode
     *
     * @param context Context
     * @return Result
     */
    public static boolean isNightMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES) > 0;
    }

    private static TypedValue getTypedValue(Resources.Theme theme, int attrId) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attrId, typedValue, true);
        return typedValue;
    }

    public static @ColorInt int resolveColor(Context context, @AttrRes int attrId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getTheme().getResources().getColor(getTypedValue(context.getTheme(), attrId).resourceId, context.getTheme());
        } else {
            return ContextCompat.getColor(context, getTypedValue(context.getTheme(), attrId).resourceId);
        }
    }

    public static ColorStateList resolveColorStateList(Context context, @AttrRes int attrId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getTheme().getResources().getColorStateList(getTypedValue(context.getTheme(), attrId).resourceId, context.getTheme());
        } else {
            return ContextCompat.getColorStateList(context, getTypedValue(context.getTheme(), attrId).resourceId);
        }
    }
}
