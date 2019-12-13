package rikka.material.internal

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import rikka.material.R
import rikka.material.app.DayNightDelegate
import rikka.material.app.LocaleDelegate

open class ThemedAppCompatActivity: AppCompatActivity() {

    private val localeDelegate by lazy {
        LocaleDelegate()
    }

    private val dayNightDelegate by lazy {
        DayNightDelegate(this)
    }

    private var userThemeKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        localeDelegate.onCreate(this)
        dayNightDelegate.onCreate(savedInstanceState)
        resetTitle()
        fixWindowFlags()

        userThemeKey = computeUserThemeKey()

        super.onCreate(savedInstanceState)

        onApplyUserThemeResourceForDecorView()
    }

    open fun computeUserThemeKey(): String? {
        return null
    }

    open fun onApplyUserThemeResource(theme: Resources.Theme, isDecorView: Boolean) {
    }

    private fun onApplyUserThemeResourceForDecorView() {
        // apply style to DecorContext to correct theme of PopupWindow, etc.
        if (window?.decorView?.context?.theme != null) {
            onApplyUserThemeResource(window.decorView.context.theme!!, true)
        }
    }

    override fun onApplyThemeResource(theme: Resources.Theme, resid: Int, first: Boolean) {
        // apply real style and our custom style
        if (parent == null) {
            theme.applyStyle(resid, true)
        } else {
            try {
                theme.setTo(parent.theme)
            } catch (e: Exception) {
                // Empty
            }

            theme.applyStyle(resid, false)
        }

        onApplyUserThemeResource(theme, false)

        // only pass theme style to super, so styled theme will not be overwritten
        super.onApplyThemeResource(theme, R.style.ThemeOverlay, first)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dayNightDelegate.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()

        if (localeDelegate.isLocaleChanged
                || dayNightDelegate.isDayNightChanged
                || dayNightDelegate.calculateNightMode() != DayNightDelegate.getDefaultNightMode()
                || userThemeKey != computeUserThemeKey()) {
            recreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dayNightDelegate.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        val configuration = newBase.resources.configuration
        localeDelegate.updateConfiguration(configuration)
        dayNightDelegate.attachBaseContext(newBase, configuration)

        super.attachBaseContext(newBase.createConfigurationContext(configuration))
    }

    /**
     * Fix titles don't change when current locale is changed
     */
    private fun resetTitle() {
        var label = try {
            packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes
        } catch (ignored: PackageManager.NameNotFoundException) {
            0
        }
        if (label == 0) {
            label = applicationInfo.labelRes
        }
        if (label != 0) {
            setTitle(label)
        }
    }

    /**
     * Fix windowLightStatusBar not changed after applyStyle on Android O
     */
    private fun fixWindowFlags() {
        var a: TypedArray
        var flag = window.decorView.systemUiVisibility

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            a = obtainStyledAttributes(intArrayOf(android.R.attr.windowLightStatusBar))
            val windowLightStatusBar = a.getBoolean(0, false)
            a.recycle()

            flag = if (windowLightStatusBar) {
                flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            a = obtainStyledAttributes(intArrayOf(android.R.attr.windowLightNavigationBar))
            val windowLightNavigationBar = a.getBoolean(0, false)
            a.recycle()

            flag = if (windowLightNavigationBar) {
                flag or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                flag and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }

        window.decorView.systemUiVisibility = flag
    }
}