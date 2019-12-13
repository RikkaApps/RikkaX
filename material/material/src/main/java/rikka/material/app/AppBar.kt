package rikka.material.app

import androidx.appcompat.app.ActionBar
import rikka.material.internal.ActionBarWrapper
import rikka.material.widget.AppBarLayout

open class AppBar(actionBar: ActionBar, private val appBarLayout: AppBarLayout) : ActionBarWrapper(actionBar) {

    override fun setElevation(elevation: Float) {
        appBarLayout.elevation = elevation
    }

    override fun getElevation(): Float {
        return appBarLayout.elevation
    }

    open fun isRaised(): Boolean {
        return appBarLayout.isRaised
    }

    open fun setRaised(raised: Boolean) {
        appBarLayout.isRaised = raised
    }
}