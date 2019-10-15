package rikka.design.app

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toolbar
import androidx.annotation.CallSuper
import rikka.core.app.ThemedFragmentActivity
import rikka.core.view.menu.MenuItemImpl
import rikka.design.widget.AppBarLayout

open class MaterialActivity : ThemedFragmentActivity(), AppBarOwner, TranslucentSystemBars {

    private var appBarImpl: AppBarImpl? = null

    override fun getAppBar(): AppBar? {
        return appBarImpl
    }

    override fun setAppBar(appBarLayout: AppBarLayout, toolbar: Toolbar) {
        appBarImpl = AppBarImpl(appBarLayout, toolbar)

        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        toolbar.setNavigationOnClickListener {
            onOptionsItemSelected(object : MenuItemImpl() {
                override fun getItemId(): Int {
                    return android.R.id.home
                }
            })
        }

        try {
            var label = packageManager.getActivityInfo(
                    componentName, PackageManager.GET_META_DATA).labelRes
            if (label == 0) {
                label = packageManager.getApplicationInfo(
                        packageName, PackageManager.GET_META_DATA).labelRes
            }
            if (label != 0) {
                appBar?.setTitle(label)
            }
        } catch (ignored: PackageManager.NameNotFoundException) {

        }

        invalidateOptionsMenu()
    }

    override fun invalidateOptionsMenu() {
        if (appBarImpl != null) {
            val menu = appBarImpl!!.menu
            menu.clear()
            dispatchCreateOptionsMenu(menu, menuInflater)
            dispatchPrepareOptionsMenu(menu)
        } else {
            super.invalidateOptionsMenu()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (super.onOptionsItemSelected(item)) {
            return true
        }
        return dispatchOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (appBarImpl != null && appBarImpl!!.collapseActionView()) {
            return
        }
        super.onBackPressed()
    }

    private fun dispatchPrepareOptionsMenu(menu: Menu) {
        onPrepareOptionsMenu(menu)

        if (!supportFragmentManager.isStateSaved) {
            for (fragment in supportFragmentManager.fragments) {
                if (fragment.isAdded && !fragment.isHidden) {
                    fragment.onPrepareOptionsMenu(menu)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun dispatchCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        onCreateOptionsMenu(menu)

        if (!supportFragmentManager.isStateSaved) {
            for (fragment in supportFragmentManager.fragments) {
                if (fragment.isAdded && !fragment.isHidden && fragment.hasOptionsMenu()) {
                    fragment.onCreateOptionsMenu(menu, inflater)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun dispatchOptionsItemSelected(item: MenuItem): Boolean {
        if (supportFragmentManager.isStateSaved) {
            return false
        }
        for (fragment in supportFragmentManager.fragments) {
            if (fragment.isAdded && !fragment.isHidden && fragment.hasOptionsMenu()) {
                if (fragment.onOptionsItemSelected(item)) {
                    return true
                }
            }
        }
        return false
    }

    @CallSuper
    override fun onAttachedToWindow() {
        if (shouldApplyTranslucentSystemBars()) {
            onApplyTranslucentSystemBars()
        }
    }
}
