package rikka.core.app

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment

open class DelegateDialogFragment : DialogFragment(), DelegateDialogParent {

    private var _hasDialogOptionsMenu: Boolean = false

    override fun setHasDialogOptionsMenu(hasMenu: Boolean) {
        _hasDialogOptionsMenu = hasMenu
    }

    override fun hasDialogOptionsMenu(): Boolean {
        return _hasDialogOptionsMenu
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DelegateDialog(context!!, theme)
    }

    override fun onBackPressed(): Boolean {
        return true
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
}