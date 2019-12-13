package rikka.material.app

import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toolbar
import rikka.core.app.DelegateDialog
import rikka.material.widget.AppBarLayout

open class MaterialDialog : DelegateDialog, AppBarOwner, TranslucentSystemBars {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

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
            onOptionsItemSelected(homeMenuItem)
        }

        appBarLayout.post {
            invalidateOptionsMenu()
        }
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

    override fun onBackPressed() {
        if (appBarImpl != null && appBarImpl!!.collapseActionView()) {
            return
        }
        super.onBackPressed()
    }

    private fun dispatchPrepareOptionsMenu(menu: Menu) {
        onPrepareOptionsMenu(menu)
    }

    private fun dispatchCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        onCreateOptionsMenu(menu)
    }

    override fun onAttachedToWindow() {
        if (parent is TranslucentSystemBars) {
            if ((parent as TranslucentSystemBars).shouldApplyTranslucentSystemBars()) {
                (parent as TranslucentSystemBars).onApplyTranslucentSystemBars()
            }
        } else {
            if (shouldApplyTranslucentSystemBars()) {
                onApplyTranslucentSystemBars()
            }
        }
    }
}