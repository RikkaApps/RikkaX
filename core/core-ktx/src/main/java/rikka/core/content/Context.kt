package rikka.core.content

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.unwrap(): Context {
    if (this is ContextWrapper) {
        return this.baseContext.unwrap()
    }
    return this
}

inline fun <reified T : Activity> Context.asActivity(): T {
    if (this is T) {
        return this
    } else {
        var context = this
        while (true) {
            if (context is ContextWrapper) {
                context = context.baseContext
                if (context is T) {
                    return context
                }
            } else {
                throw ClassCastException("Context instance $this is not Activity")
            }
        }
    }
}
