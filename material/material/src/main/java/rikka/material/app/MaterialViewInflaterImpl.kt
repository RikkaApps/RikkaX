package rikka.material.app

import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
import androidx.collection.SimpleArrayMap
import rikka.material.widget.WindowInsetsHelper
import java.lang.reflect.Constructor

open class MaterialViewInflaterImpl {

    open fun onViewCreated(view: View?, parent: View?, name: String, context: Context, attrs: AttributeSet) {
        if (view == null) {
            return
        }

        WindowInsetsHelper.attach(view, attrs)
    }

    private val constructorSignature = arrayOf(
            Context::class.java, AttributeSet::class.java)

    private val classPrefixList = arrayOf(
            "android.widget.",
            "android.view.",
            "android.webkit."
    )

    private val constructorMap = SimpleArrayMap<String, Constructor<out View?>>()

    open fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
        var name = name
        if (name == "view") {
            name = attrs.getAttributeValue(null, "class")
        }
        return try {
            if (-1 == name.indexOf('.')) {
                for (prefix in classPrefixList) {
                    val view: View? = createViewByPrefix(context, name, attrs, prefix)
                    if (view != null) {
                        return view
                    }
                }
                null
            } else {
                createViewByPrefix(context, name, attrs, null)
            }
        } catch (e: Exception) {
            null
        }
    }

    @Throws(ClassNotFoundException::class, InflateException::class)
    private fun createViewByPrefix(context: Context, name: String, attrs: AttributeSet, prefix: String?): View? {
        var constructor = constructorMap[name]
        return try {
            if (constructor == null) { // Class not found in the cache, see if it's real, and try to add it
                val clazz = Class.forName(
                        if (prefix != null) prefix + name else name,
                        false,
                        context.classLoader).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*constructorSignature)
                constructorMap.put(name, constructor)
            }
            constructor.isAccessible = true
            constructor.newInstance(context, attrs)
        } catch (e: Exception) {
            null
        }
    }
}