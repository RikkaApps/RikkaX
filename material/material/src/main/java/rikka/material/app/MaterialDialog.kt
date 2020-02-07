package rikka.material.app

import android.app.ActionBar
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.Toolbar
import rikka.material.widget.AppBarLayout

open class MaterialDialog : AppCompatDialog, AppBarOwner, TranslucentSystemBars {

    var parent: MaterialDialogParent? = null

    private var appBar: AppBar? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun getAppBar(): AppBar? {
        return appBar
    }

    override fun setAppBar(appBarLayout: AppBarLayout, toolbar: Toolbar) {
        delegate.setSupportActionBar(toolbar)
        appBar = AppBar(supportActionBar!!, appBarLayout)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return when (featureId) {
            Window.FEATURE_OPTIONS_PANEL -> {
                if (onOptionsItemSelected(item)) {
                    return true
                }
                if (item.itemId == android.R.id.home && supportActionBar != null && supportActionBar.displayOptions and ActionBar.DISPLAY_HOME_AS_UP != 0) {
                    onBackPressed()
                    return true
                }
                false
            }
            Window.FEATURE_CONTEXT_MENU -> {
                onContextItemSelected(item)
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        if (parent?.onBackPressed() == true) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if (hasMenu) {
            parent!!.onCreateDialogOptionsMenu(menu, delegate.menuInflater)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if (hasMenu) {
            parent!!.onPrepareDialogOptionsMenu(menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if (hasMenu && parent!!.onDialogOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOptionsMenuClosed(menu: Menu) {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if (hasMenu) {
            parent!!.onDialogOptionsMenuClosed(menu)
            return
        }
        super.onOptionsMenuClosed(menu)
    }
}