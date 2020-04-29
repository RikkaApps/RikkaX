package rikka.material.chooser

import android.content.Context
import android.graphics.drawable.AdaptiveIconDrawable
import android.os.Build
import me.zhanghai.android.appiconloader.AppIconLoader

internal object ChooserIconLoader {

    private var appIconLoader: AppIconLoader? = null

    fun get(context: Context): AppIconLoader {
        synchronized(this) {
            if (appIconLoader == null) {
                val shrinkNonAdaptiveIcons = Build.VERSION.SDK_INT >= 26 && context.applicationInfo.loadIcon(context.packageManager) is AdaptiveIconDrawable
                appIconLoader = AppIconLoader(context.resources.getDimensionPixelSize(R.dimen.chooser_icon_size), shrinkNonAdaptiveIcons, context)
            }
            return appIconLoader!!
        }
    }
}