package rikka.core.app

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import rikka.core.view.menu.MenuItemImpl

open class DelegateDialog : Dialog {

    var parent: DelegateDialogParent? = null

    open val menuInflater by lazy {
        MenuInflater(context)
    }

    protected val homeMenuItem = object : MenuItemImpl() {
        override fun getItemId(): Int {
            return android.R.id.home
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    override fun onBackPressed() {
        if (parent?.onBackPressed() == true) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if (hasMenu) {
            parent!!.onCreateDialogOptionsMenu(menu, menuInflater)
        }
        return hasMenu
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if (hasMenu) {
            parent!!.onPrepareDialogOptionsMenu(menu)
        }
        return hasMenu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val hasMenu = parent?.hasDialogOptionsMenu() == true
        if ((hasMenu || item == homeMenuItem) && parent!!.onDialogOptionsItemSelected(item)) {
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