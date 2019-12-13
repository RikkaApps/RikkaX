package rikka.material.internal

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialogFragment
import rikka.material.app.MaterialDialog

open class ThemedAppCompatDialogFragment : AppCompatDialogFragment() {

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
            onDialogShow(it as MaterialDialog)
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