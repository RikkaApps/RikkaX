package rikka.material.app

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import rikka.layoutinflater.view.LayoutInflaterFactory

class ToolbarTitleAlignmentFix : LayoutInflaterFactory.OnViewCreatedListener {

    companion object {
        @JvmStatic
        val LISTENER = ToolbarTitleAlignmentFix()
    }

    override fun onViewCreated(view: View, parent: View?, name: String, context: Context, attrs: AttributeSet) {
        if (view is Toolbar) {
            view.contentInsetStartWithNavigation =
                context.resources.getDimensionPixelSize(androidx.appcompat.R.dimen.abc_action_bar_content_inset_with_nav)
        }
    }
}
