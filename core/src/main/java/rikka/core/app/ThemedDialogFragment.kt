package rikka.core.app

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment

open class ThemedDialogFragment : DialogFragment() {

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = onCreateDialog(requireContext(), savedInstanceState)
        setupDialog(dialog)
        onDialogCreated(dialog, savedInstanceState)
        onApplyUserThemeResource(dialog.context.theme)
        return dialog
    }

    @CallSuper
    protected open fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            onDialogShow(it as DelegateDialog)
        }
    }

    open fun onCreateDialog(context: Context, savedInstanceState: Bundle?): Dialog {
        return Dialog(context, theme)
    }

    open fun onDialogCreated(dialog: Dialog, savedInstanceState: Bundle?) {

    }

    open fun onDialogShow(dialog: Dialog) {

    }

    open fun onApplyUserThemeResource(theme: Resources.Theme) {
    }
}

open class ThemedDelegateDialogFragment : ThemedDialogFragment(), DelegateDialogParent {

    private var _hasDialogOptionsMenu: Boolean = false

    override fun setHasDialogOptionsMenu(hasMenu: Boolean) {
        _hasDialogOptionsMenu = hasMenu
    }

    override fun hasDialogOptionsMenu(): Boolean {
        return _hasDialogOptionsMenu
    }

    override fun onCreateDialog(context: Context, savedInstanceState: Bundle?): DelegateDialog {
        return DelegateDialog(context, theme)
    }

    override fun setupDialog(dialog: Dialog) {
        super.setupDialog(dialog)

        (dialog as DelegateDialog).parent = this
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
