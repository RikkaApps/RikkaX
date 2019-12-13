package rikka.material.app

import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import rikka.material.internal.ThemedAppCompatActivity
import rikka.material.widget.AppBarLayout

open class MaterialActivity : ThemedAppCompatActivity(), AppBarOwner, TranslucentSystemBars {

    private var appBar: AppBar? = null

    @CallSuper
    override fun onAttachedToWindow() {
        if (shouldApplyTranslucentSystemBars()) {
            onApplyTranslucentSystemBars()
        }
    }

    override fun getAppBar(): AppBar? {
        return appBar
    }

    override fun setAppBar(appBarLayout: AppBarLayout, toolbar: Toolbar) {
        super.setSupportActionBar(toolbar)
        appBar = AppBar(supportActionBar!!, appBarLayout)
    }
}