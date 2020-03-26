package rikka.material.app

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.MaterialViewInflater
import androidx.appcompat.widget.Toolbar
import rikka.core.res.resolveColor
import rikka.material.R
import rikka.material.internal.ThemedAppCompatActivity
import rikka.material.widget.AppBarLayout

open class MaterialActivity : ThemedAppCompatActivity(), AppBarOwner, TranslucentSystemBars {

    private var appBar: AppBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installViewFactory(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)

        if (shouldApplyTranslucentSystemBars()) {
            onApplyTranslucentSystemBars()
        }
    }

    open fun installViewFactory(layoutInflater: LayoutInflater) {
        layoutInflater.factory2 = MaterialViewInflater(this)
    }

    override fun getAppBar(): AppBar? {
        return appBar
    }

    override fun setAppBar(appBarLayout: AppBarLayout, toolbar: Toolbar) {
        super.setSupportActionBar(toolbar)
        appBar = AppBar(supportActionBar!!, appBarLayout)
    }

    override fun shouldApplyTranslucentSystemBars(): Boolean {
        return true
    }

    @CallSuper
    override fun onApplyTranslucentSystemBars() {
        val window = window
        val theme = theme

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window?.decorView?.post {
                if (window.decorView.rootWindowInsets?.systemWindowInsetBottom ?: 0 >= Resources.getSystem().displayMetrics.density * 40) {
                    window.navigationBarDividerColor = theme.resolveColor(R.attr.navigationBarDividerColor)
                }
            }
        }
    }
}