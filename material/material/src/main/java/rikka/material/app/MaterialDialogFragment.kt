package rikka.material.app

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.widget.Toolbar
import rikka.material.internal.ThemedAppCompatDialogFragment
import rikka.material.widget.AppBarLayout

open class MaterialDialogFragment : ThemedAppCompatDialogFragment(), MaterialDialogParent, AppBarOwner, TranslucentSystemBars {

    private var _hasDialogOptionsMenu: Boolean = false

    override fun setHasDialogOptionsMenu(hasMenu: Boolean) {
        _hasDialogOptionsMenu = hasMenu
    }

    override fun hasDialogOptionsMenu(): Boolean {
        return _hasDialogOptionsMenu
    }

    override fun onCreateDialog(context: Context, savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(context, theme)
    }

    override fun setupDialog(dialog: Dialog) {
        super.setupDialog(dialog)

        (dialog as MaterialDialog).parent = this
    }

    @MainThread
    override fun openDialogOptionsMenu() {
        dialog?.openOptionsMenu()
    }

    @MainThread
    override fun closeDialogOptionsMenu() {
        dialog?.closeOptionsMenu()
    }

    @MainThread
    override fun invalidateDialogOptionsMenu() {
        dialog?.openOptionsMenu()
    }

    override fun getAppBar(): AppBar? {
        return (dialog as MaterialDialog).appBar
    }

    override fun setAppBar(appBarLayout: AppBarLayout, toolbar: Toolbar) {
        (dialog as MaterialDialog).setAppBar(appBarLayout, toolbar)
    }
}