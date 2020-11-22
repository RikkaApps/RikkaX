package rikka.material.app

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import rikka.material.view.LayoutInflaterFactory
import rikka.material.widget.WindowInsetsHelper

open class MaterialViewInflater(delegate: AppCompatDelegate) : LayoutInflaterFactory(delegate) {

    override fun onViewCreated(view: View, parent: View?, name: String, context: Context, attrs: AttributeSet) {
        WindowInsetsHelper.attach(view, attrs)
    }
}