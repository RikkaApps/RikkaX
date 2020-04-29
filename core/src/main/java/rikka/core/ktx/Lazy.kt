@file:Suppress("unused")

package rikka.core.ktx

fun <T> unsafeLazy(initializer: () -> T): Lazy<T> = kotlin.lazy(LazyThreadSafetyMode.NONE, initializer)
