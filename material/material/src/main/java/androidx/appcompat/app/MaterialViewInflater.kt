package androidx.appcompat.app

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.R
import rikka.material.app.MaterialViewInflaterImpl

open class MaterialViewInflater(private val context: Context) : LayoutInflater.Factory2 {

    private val appCompatViewInflater by lazy {
        val a: TypedArray = context.obtainStyledAttributes(R.styleable.AppCompatTheme)
        val viewInflaterClassName = a.getString(R.styleable.AppCompatTheme_viewInflaterClass)

        val vewInflater = if (viewInflaterClassName == null) {
            AppCompatViewInflater()
        } else {
            try {
                val viewInflaterClass = Class.forName(viewInflaterClassName)
                viewInflaterClass.getDeclaredConstructor()
                        .newInstance() as AppCompatViewInflater
            } catch (t: Throwable) {
                Log.i("MaterialActivity", "Failed to instantiate custom view inflater "
                        + viewInflaterClassName + ". Falling back to default.", t)
                AppCompatViewInflater()
            }
        }
        a.recycle()
        vewInflater
    }

    private val impl = MaterialViewInflaterImpl()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        var view = appCompatViewInflater.createView(parent, name, context, attrs,
                false, false, false, false)
        if (view == null) {
            view = impl.createViewFromTag(context, name, attrs)
        }
        impl.onViewCreated(view, parent, name, context, attrs)
        return view
    }
}