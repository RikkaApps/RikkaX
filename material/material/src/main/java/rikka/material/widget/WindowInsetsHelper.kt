package rikka.material.widget

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity.*
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rikka.material.R

private typealias ApplyInsetsCallback<T> = (insets: Insets, left: Boolean, top: Boolean, right: Boolean, bottom: Boolean) -> T

private class ApplyPadding(private val padding: Rect) : ApplyInsetsCallback<Unit> {

    override fun invoke(insets: Insets, left: Boolean, top: Boolean, right: Boolean, bottom: Boolean) {
        padding.left += if (left) insets.left else 0
        padding.top += if (top) insets.top else 0
        padding.right += if (right) insets.right else 0
        padding.bottom += if (bottom) insets.bottom else 0
    }
}

private class ConsumeInsets : ApplyInsetsCallback<Insets> {

    override fun invoke(insets: Insets, left: Boolean, top: Boolean, right: Boolean, bottom: Boolean): Insets {
        val insetsLeft = if (left) 0 else insets.left
        val insetsTop = if (top) 0 else insets.top
        val insetsRight = if (right) 0 else insets.right
        val insetsBottom = if (bottom) 0 else insets.bottom
        return Insets.of(insetsLeft, insetsTop, insetsRight, insetsBottom)
    }
}

open class WindowInsetsHelper private constructor(
        private val view: View,
        private val fitSystemWindows: Int,
        private val consumeSystemWindows: Int) : OnApplyWindowInsetsListener {

    internal var initialPaddingLeft: Int = view.paddingLeft
    internal var initialPaddingTop: Int = view.paddingTop
    internal var initialPaddingRight: Int = view.paddingRight
    internal var initialPaddingBottom: Int = view.paddingBottom

    private var lastInsets: WindowInsetsCompat? = null

    open fun setInitialPadding(left: Int, top: Int, right: Int, bottom: Int) {
        initialPaddingLeft = left
        initialPaddingTop = top
        initialPaddingRight = right
        initialPaddingBottom = bottom

        lastInsets?.let { applyWindowInsets(it) }
    }

    open fun setInitialPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        val isRTL = view.layoutDirection == View.LAYOUT_DIRECTION_RTL
        if (isRTL) {
            setInitialPadding(start, top, end, bottom)
        } else {
            setInitialPadding(start, top, end, bottom)
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun <T> applyInsets(insets: Insets, fit: Int, callback: ApplyInsetsCallback<T>): T {
        val relativeMode = fit and RELATIVE_LAYOUT_DIRECTION == RELATIVE_LAYOUT_DIRECTION

        val isRTL = view.layoutDirection == View.LAYOUT_DIRECTION_RTL

        val left: Boolean
        val top = fit and TOP == TOP
        val right: Boolean
        val bottom = fit and BOTTOM == BOTTOM

        if (relativeMode) {
            val start = fit and START == START
            val end = fit and END == END
            left = (!isRTL && start) || (isRTL && end)
            right = (!isRTL && end) || (isRTL && start)
        } else {
            left = fit and LEFT == LEFT
            right = fit and RIGHT == RIGHT
        }

        return callback.invoke(insets, left, top, right, bottom)
    }

    private fun applyWindowInsets(windowInsets: WindowInsetsCompat): WindowInsetsCompat {
        val padding = Rect(initialPaddingLeft, initialPaddingTop, initialPaddingRight, initialPaddingBottom)
        applyInsets(windowInsets.systemWindowInsets, fitSystemWindows, ApplyPadding(padding))

        view.setPadding(padding.left, padding.top, padding.right, padding.bottom)

        return WindowInsetsCompat.Builder(windowInsets)
                .setSystemWindowInsets(applyInsets(windowInsets.systemWindowInsets, consumeSystemWindows, ConsumeInsets()))
                .build()
    }

    override fun onApplyWindowInsets(view: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        if (lastInsets == insets) {
            return insets
        }

        lastInsets = insets

        return applyWindowInsets(insets)
    }

    companion object {

        @JvmStatic
        fun attach(view: View, attrs: AttributeSet) {
            val a = view.context.obtainStyledAttributes(attrs, R.styleable.WindowInsetsHelper, 0, 0)
            val edgeToEdge = a.getBoolean(R.styleable.WindowInsetsHelper_edgeToEdge, false)
            val fitSystemWindowsInsets = a.getInt(R.styleable.WindowInsetsHelper_fitSystemWindowsInsets, 0)
            val consumeSystemWindowsInsets = a.getInt(R.styleable.WindowInsetsHelper_consumeSystemWindowsInsets, 0)
            a.recycle()

            attach(view, edgeToEdge, fitSystemWindowsInsets, consumeSystemWindowsInsets)
        }

        @JvmStatic
        fun attach(view: View, layoutBelowSystemUi: Boolean, fitSystemWindowsInsets: Int, consumeSystemWindowsInsets: Int) {
            if (layoutBelowSystemUi) {
                view.systemUiVisibility = (view.systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            }

            if (fitSystemWindowsInsets == 0) {
                return
            }

            val listener = WindowInsetsHelper(view, fitSystemWindowsInsets, consumeSystemWindowsInsets)
            ViewCompat.setOnApplyWindowInsetsListener(view, listener)
            view.setTag(R.id.tag_rikka_material_WindowInsetsHelper, listener)
        }
    }
}

val View.windowInsetsHelper: WindowInsetsHelper?
    get() {
        val value = getTag(R.id.tag_rikka_material_WindowInsetsHelper)
        return if (value is WindowInsetsHelper) value else null
    }

val View.initialPaddingLeft: Int
    get() = windowInsetsHelper?.initialPaddingLeft ?: 0

val View.initialPaddingTop: Int
    get() = windowInsetsHelper?.initialPaddingTop ?: 0

val View.initialPaddingRight: Int
    get() = windowInsetsHelper?.initialPaddingRight ?: 0

val View.initialPaddingBottom: Int
    get() = windowInsetsHelper?.initialPaddingBottom ?: 0

val View.initialPaddingStart: Int
    get() = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) initialPaddingRight else initialPaddingLeft

val View.initialPaddingEnd: Int
    get() = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) initialPaddingLeft else initialPaddingRight

fun View.setInitialPadding(left: Int, top: Int, right: Int, bottom: Int) {
    windowInsetsHelper?.setInitialPadding(left, top, right, bottom)
}

fun View.setInitialPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
    windowInsetsHelper?.setInitialPaddingRelative(start, top, end, bottom)
}