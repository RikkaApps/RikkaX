@file:Suppress("unused")

package rikka.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.EdgeEffect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import me.zhanghai.android.fastscroll.FastScroller
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import me.zhanghai.android.fastscroll.PopupTextProvider
import me.zhanghai.android.fastscroll.Predicate
import rikka.recyclerview.utils.R
import kotlin.math.roundToInt

fun RecyclerView.addVerticalPadding(paddingTop: Int = 8, paddingBottom: Int = 8) {
    addItemDecoration(VerticalPaddingDecoration(context, paddingTop, paddingBottom))
}

private class VerticalPaddingDecoration constructor(context: Context, paddingTop: Int = 8, paddingBottom: Int = 8) : ItemDecoration() {

    private var paddingTop: Int = (paddingTop * context.resources.displayMetrics.density).roundToInt()
    private var paddingBottom: Int = (paddingBottom * context.resources.displayMetrics.density).roundToInt()
    private var allowTop: Boolean = true
    private var allowBottom: Boolean = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.adapter == null) {
            return
        }
        val position = parent.getChildAdapterPosition(view)
        val count = parent.adapter!!.itemCount
        if (position == 0 && allowTop) {
            outRect.top = paddingTop
        } else if (position == count - 1 && allowBottom) {
            outRect.bottom = paddingBottom
        }
    }
}

fun RecyclerView.fixEdgeEffect(overScrollIfContentScrolls: Boolean = true, alwaysClipToPadding: Boolean = true) {
    if (overScrollIfContentScrolls) {
        val listener = OverScrollIfContentScrollsListener()
        addOnLayoutChangeListener(listener)
        setTag(R.id.tag_rikka_recyclerView_OverScrollIfContentScrollsListener, listener)
    } else {
        val listener = getTag(R.id.tag_rikka_recyclerView_OverScrollIfContentScrollsListener) as? OverScrollIfContentScrollsListener
        if (listener != null) {
            removeOnLayoutChangeListener(listener)
            setTag(R.id.tag_rikka_recyclerView_OverScrollIfContentScrollsListener, null)
        }
    }

    edgeEffectFactory = if (alwaysClipToPadding && !clipToPadding) {
        AlwaysClipToPaddingEdgeEffectFactory()
    } else {
        RecyclerView.EdgeEffectFactory()
    }
}

private class OverScrollIfContentScrollsListener : View.OnLayoutChangeListener {
    private var show = true
    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        if (shouldDrawOverScroll(v as RecyclerView) != show) {
            show = !show
            if (show) {
                v.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS)
            } else {
                v.setOverScrollMode(View.OVER_SCROLL_NEVER)
            }
        }
    }

    fun shouldDrawOverScroll(recyclerView: RecyclerView): Boolean {
        if (recyclerView.layoutManager == null || recyclerView.adapter == null || recyclerView.adapter!!.itemCount == 0) {
            return false
        }
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val itemCount = recyclerView.layoutManager!!.itemCount
            val firstPosition: Int = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
            val lastPosition: Int = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
            return firstPosition != 0 || lastPosition != itemCount - 1
        }
        return true
    }
}

private class AlwaysClipToPaddingEdgeEffectFactory : RecyclerView.EdgeEffectFactory() {

    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {

        return object : EdgeEffect(view.context) {
            private var ensureSize = false

            private fun ensureSize() {
                if (ensureSize) return
                ensureSize = true

                when (direction) {
                    DIRECTION_LEFT -> {
                        setSize(view.measuredHeight - view.paddingTop - view.paddingBottom,
                                view.measuredWidth - view.paddingLeft - view.paddingRight)
                    }
                    DIRECTION_TOP -> {
                        setSize(view.measuredWidth - view.paddingLeft - view.paddingRight,
                                view.measuredHeight - view.paddingTop - view.paddingBottom)
                    }
                    DIRECTION_RIGHT -> {
                        setSize(view.measuredHeight - view.paddingTop - view.paddingBottom,
                                view.measuredWidth - view.paddingLeft - view.paddingRight)
                    }
                    DIRECTION_BOTTOM -> {
                        setSize(view.measuredWidth - view.paddingLeft - view.paddingRight,
                                view.measuredHeight - view.paddingTop - view.paddingBottom)
                    }
                }
            }

            override fun draw(c: Canvas): Boolean {
                ensureSize()

                val restore = c.save()
                when (direction) {
                    DIRECTION_LEFT -> {
                        c.translate(view.paddingBottom.toFloat(), 0f)
                    }
                    DIRECTION_TOP -> {
                        c.translate(view.paddingLeft.toFloat(), view.paddingTop.toFloat())
                    }
                    DIRECTION_RIGHT -> {
                        c.translate(-view.paddingTop.toFloat(), 0f)
                    }
                    DIRECTION_BOTTOM -> {
                        c.translate(view.paddingRight.toFloat(), view.paddingBottom.toFloat())
                    }
                }
                val res = super.draw(c)
                c.restoreToCount(restore)
                return res
            }
        }
    }
}

fun RecyclerView.addFastScroller(parent: View? = null) {
    val builder = FastScrollerBuilder(this).useMd2Style()
    builder.setViewHelper(VerticalLinearRecyclerViewHelper(this, parent))
    builder.build()
}

private class VerticalLinearRecyclerViewHelper(private val view: RecyclerView, private val parent: View?) : FastScroller.ViewHelper {

    override fun addOnPreDrawListener(onPreDraw: Runnable) {
        view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(canvas: Canvas, parent: RecyclerView,
                                state: RecyclerView.State) {
                onPreDraw.run()
            }
        })
    }

    override fun addOnScrollChangedListener(onScrollChanged: Runnable) {
        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onScrollChanged.run()
            }
        })
    }

    override fun addOnTouchEventListener(onTouchEvent: Predicate<MotionEvent>) {
        view.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

            var dragging: Boolean? = null

            override fun onInterceptTouchEvent(recyclerView: RecyclerView,
                                               event: MotionEvent): Boolean {
                return onTouchEventResult(onTouchEvent.test(event))
            }

            override fun onTouchEvent(recyclerView: RecyclerView,
                                      event: MotionEvent) {
                onTouchEventResult(onTouchEvent.test(event))
            }

            private fun onTouchEventResult(dragging: Boolean): Boolean {
                if (this.dragging != dragging) {
                    this.dragging = dragging
                    parent?.isEnabled = !dragging
                }
                return dragging
            }
        })
    }

    override fun getScrollRange(): Int {
        return view.computeVerticalScrollRange() + view.paddingTop + view.paddingBottom
    }

    override fun getScrollOffset(): Int {
        return view.computeVerticalScrollOffset()
    }

    override fun scrollTo(offset: Int) {
        view.stopScroll()

        val oldOffset = scrollOffset
        val percentage = (offset - oldOffset).toFloat() / scrollRange
        val scrollingBy = (percentage * scrollRange).toInt()
        view.scrollBy(0, scrollingBy)
    }

    override fun getPopupText(): String? {
        val adapter = view.adapter
        if (adapter !is PopupTextProvider) {
            return null
        }
        val popupTextProvider = adapter as PopupTextProvider
        val position = getItemAdapterPositionForPopup()
        return if (position == RecyclerView.NO_POSITION) {
            null
        } else popupTextProvider.getPopupText(position)
    }

    private fun getItemCount(): Int {
        return verticalLinearLayoutManager?.itemCount ?: return 0
    }

    private fun getItemAdapterPositionForPopup(): Int {
        if (view.childCount == 0) {
            return RecyclerView.NO_POSITION
        }
        return verticalLinearLayoutManager?.findFirstCompletelyVisibleItemPosition() ?: return RecyclerView.NO_POSITION
    }

    private val verticalLinearLayoutManager: LinearLayoutManager?
        get() {
            val layoutManager = view.layoutManager as? LinearLayoutManager ?: return null
            return if (layoutManager.orientation != RecyclerView.VERTICAL) {
                null
            } else layoutManager
        }

}