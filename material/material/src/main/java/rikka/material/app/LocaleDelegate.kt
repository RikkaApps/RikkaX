package rikka.material.app

import android.app.Activity
import android.content.res.Configuration
import android.text.TextUtils

import java.util.Locale

class LocaleDelegate {

    /** locale of this instance  */
    private var locale = Locale.getDefault()

    /**
     * Return if current locale is different from default.
     *
     * Call this in [Activity.onResume] and if true you should recreate activity.
     *
     * @return locale changed
     */
    val isLocaleChanged: Boolean
        get() = defaultLocale != locale

    /**
     * Update locale of given configuration, call in [Activity.attachBaseContext].
     *
     * @param configuration Configuration
     */
    fun updateConfiguration(configuration: Configuration) {
        locale = defaultLocale

        configuration.setLocale(locale)
    }

    /**
     * A dirty fix for wrong layout direction after switching locale between LTR and RLT language,
     * call in [Activity.onCreate].
     *
     * @param activity Activity
     */
    fun onCreate(activity: Activity) {
        activity.window.decorView.layoutDirection = TextUtils.getLayoutDirectionFromLocale(locale)
    }

    companion object {

        /** current locale  */
        @JvmStatic
        var defaultLocale: Locale = Locale.getDefault()

        /** system locale  */
        @JvmStatic
        var systemLocale: Locale = Locale.getDefault()
    }
}
