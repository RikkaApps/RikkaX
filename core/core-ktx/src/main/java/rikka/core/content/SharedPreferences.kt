@file:Suppress("unused")

package rikka.core.content

import android.content.SharedPreferences

fun SharedPreferences.Editor.put(key: String?, value: Any?): SharedPreferences.Editor {
    @Suppress("UNCHECKED_CAST")
    when (value) {
        is String -> putString(key, value)
        is Set<*> -> putStringSet(key, value as MutableSet<String>?)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Float -> putFloat(key, value)
        is Boolean -> putBoolean(key, value)
    }
    return this
}

fun SharedPreferences.Editor.putAll(map: Map<String?, *>): SharedPreferences.Editor {
    for ((key, value) in map) {
        put(key, value)
    }
    return this
}