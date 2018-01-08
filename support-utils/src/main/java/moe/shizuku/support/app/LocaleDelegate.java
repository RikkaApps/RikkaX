package moe.shizuku.support.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.Locale;
import java.util.Objects;


public class LocaleDelegate {

    /** current locale **/
    private static Locale sDefaultLocale = Locale.getDefault();

    /** locale of this instance **/
    private Locale mLocale = Locale.getDefault();

    /**
     * Get default locale stored in this class.
     *
     * @return default locale
     */
    public static Locale getDefaultLocale() {
        return sDefaultLocale;
    }

    /**
     * Set default locale, this will not call {@link Locale#setDefault(Locale)}.
     *
     * @param newLocale new locale
     */
    public static void setDefaultLocale(Locale newLocale) {
        sDefaultLocale = newLocale;
    }

    /**
     * Return if current locale is different from default.
     * <p>Call this in {@link Activity#onResume()} and if true you should recreate activity.
     *
     * @return locale changed
     */
    public boolean isLocaleChanged() {
        return !Objects.equals(sDefaultLocale, mLocale);
    }

    /**
     * Update locale of context's configuration, call in {@link Activity#attachBaseContext(Context)}.
     *
     * @param newBase newBase
     */
    public void onAttachBaseContext(Context newBase) {
        if (Objects.equals(mLocale, sDefaultLocale)) {
            return;
        }

        mLocale = sDefaultLocale;

        Configuration configuration = newBase.getResources().getConfiguration();
        configuration.setLocale(mLocale);
    }

    /**
     * A dirty fix for wrong layout direction after switching locale between LTR and RLT language,
     * call in {@link Activity#onCreate(Bundle)}.
     *
     * @param activity Activity
     */
    public void onCreate(Activity activity) {
        activity.getWindow().getDecorView().setLayoutDirection(TextUtils.getLayoutDirectionFromLocale(mLocale));
    }
}
