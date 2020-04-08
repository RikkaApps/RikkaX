package rikka.material.chooser

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.IdBasedRecyclerViewAdapter

internal class ChooserItemAdapter(resolveInfo: List<ResolveInfo?>, private val parentFragment: ChooserFragment) : IdBasedRecyclerViewAdapter() {

    private val creator = BaseViewHolder.Creator<ResolveInfo> { inflater, parent -> ChooserItemViewHolder(inflater.inflate(R.layout.chooser_grid_item, parent, false)) }

    init {
        for ((index, it) in resolveInfo.withIndex()) {
            addItem(creator, it, index.toLong())
        }
    }

    private inner class ChooserItemViewHolder internal constructor(itemView: View) : BaseViewHolder<ResolveInfo?>(itemView), View.OnClickListener {

        private val icon: ImageView = itemView.findViewById(android.R.id.icon)
        private val title: TextView = itemView.findViewById(android.R.id.text1)

        init {
            setIsRecyclable(false)
        }

        override fun onClick(v: View) {
            val context = v.context
            val intent = Intent(parentFragment.getTargetIntent(data))
            intent.component = ComponentName(
                    data!!.activityInfo.packageName,
                    data!!.activityInfo.name)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
            } catch (ignored: Throwable) {
            }
            parentFragment.requireActivity().finish()
        }

        override fun onBind() {
            super.onBind()
            if (data == null) {
                icon.setImageDrawable(null)
                title.text = null
                itemView.setOnClickListener(null)
                itemView.isClickable = false
                itemView.isFocusable = false
                return
            }
            itemView.setOnClickListener(this)
            itemView.isFocusable = true
            val pm = itemView.context.packageManager
            GlobalScope.launch(Dispatchers.IO) {

            }
            parentFragment.lifecycleScope.launchWhenCreated {
                var drawable: Bitmap? = null
                var label: CharSequence? = null
                try {
                    withContext(Dispatchers.IO) {
                        drawable = parentFragment.appIconLoader.loadIcon(data!!.activityInfo.applicationInfo, false)
                        label = data!!.loadLabel(pm)
                    }
                } catch (e: Throwable) {

                }
                icon.setImageBitmap(drawable)
                title.text = label
            }
        }
    }

}

