package rikka.material.app

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import rikka.core.app.DelegateDialog
import rikka.core.app.ThemedDelegateDialogFragment

open class MaterialDialogFragment : ThemedDelegateDialogFragment(), TranslucentSystemBars {

    override fun getDialog(): MaterialDialog? {
        return super.getDialog() as MaterialDialog?
    }

    override fun onCreateDialog(context: Context, savedInstanceState: Bundle?): MaterialDialog {
        return MaterialDialog(requireContext(), theme)
    }
}