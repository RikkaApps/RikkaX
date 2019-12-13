package rikka.material.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import java.util.Objects;

import rikka.material.R;

public class BorderScrollView extends FitsSystemWindowsNestedScrollView implements BorderView {

    private BorderViewDelegate mBorderViewDelegate;

    public BorderScrollView(@NonNull Context context) {
        this(context, null);
    }

    public BorderScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.borderViewStyle);
    }

    public BorderScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderViewDelegate = new BorderViewDelegate(this, context, attrs, defStyleAttr);
    }

    @Override
    public BorderViewDelegate getBorderViewDelegate() {
        return mBorderViewDelegate;
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            NestedScrollView.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
            int parentSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            scrollRange = Math.max(0, childSize - parentSpace);
        }
        return scrollRange;
    }

    @Override
    public void updateBorderStatus() {
        final int offset = getScrollY();
        final int range = getScrollRange();
        final boolean isShowingTopBorder, isShowingBottomBorder;
        final boolean isTop, isBottom;
        if (range != 0) {
            isTop = offset == 0;
            isBottom = offset == range;
        } else {
            //isTop = isBottom = false;
            return;
        }
        isShowingTopBorder = getBorderTopStyle() == BorderStyle.ALWAYS
                || (getBorderTopStyle() == BorderStyle.TOP_OR_BOTTOM && isTop)
                || (getBorderTopStyle() == BorderStyle.SCROLLED && !isTop);
        isShowingBottomBorder = getBorderBottomStyle() == BorderStyle.ALWAYS
                || (getBorderBottomStyle() == BorderStyle.TOP_OR_BOTTOM && isBottom)
                || (getBorderBottomStyle() == BorderStyle.SCROLLED && !isBottom);

        if (!Objects.equals(getBorderViewDelegate().isShowingTopBorder(), isShowingTopBorder) || !Objects.equals(getBorderViewDelegate().isShowingBottomBorder(), isShowingBottomBorder)) {
            onBorderVisibilityChanged(isShowingTopBorder,
                    getBorderViewDelegate().isShowingTopBorder(),
                    isShowingBottomBorder,
                    getBorderViewDelegate().isShowingBottomBorder());
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        updateBorderStatus();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        updateBorderStatus();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

        getBorderViewDelegate().onDrawForeground(canvas);
    }
}
