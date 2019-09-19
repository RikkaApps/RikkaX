package moe.shizuku.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import java.util.Objects;

import moe.shizuku.support.design.R;

public class BorderScrollView extends FitsSystemWindowsNestedScrollView {

    public interface OnBorderVisibilityChangedListener {
        void onBorderVisibilityChanged(boolean top, boolean oldTop, boolean bottom, boolean oldBottom);
    }

    private OnBorderVisibilityChangedListener mBorderVisibilityChangedListener;

    private boolean isShowingTopBorder, isShowingBottomBorder;

    public enum BorderStyle {
        NEVER, TOP_OR_BOTTOM, SCROLLED, ALWAYS
    }

    private BorderStyle borderTopStyle, borderBottomStyle;
    private Drawable borderTopDrawable, borderBottomDrawable;

    public BorderScrollView(@NonNull Context context) {
        this(context, null);
    }

    public BorderScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.borderScrollViewStyle);
    }

    public BorderScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderScrollView, defStyleAttr, R.style.Widget_Design_BorderScrollView);
        borderTopDrawable = a.getDrawable(R.styleable.BorderScrollView_borderTopDrawable);
        borderBottomDrawable = a.getDrawable(R.styleable.BorderScrollView_borderBottomDrawable);
        switch (a.getInt(R.styleable.BorderScrollView_borderTopStyle, 0)) {
            case 0:
                borderTopStyle = BorderStyle.NEVER;
                break;
            case 1:
                borderTopStyle = BorderStyle.TOP_OR_BOTTOM;
                break;
            case 3:
                borderTopStyle = BorderStyle.ALWAYS;
                break;
            case 2:
            default:
                borderTopStyle = BorderStyle.SCROLLED;
                break;
        }
        switch (a.getInt(R.styleable.BorderScrollView_borderBottomStyle, 0)) {
            case 0:
                borderBottomStyle = BorderStyle.NEVER;
                break;
            case 1:
                borderBottomStyle = BorderStyle.TOP_OR_BOTTOM;
                break;
            case 3:
                borderBottomStyle = BorderStyle.ALWAYS;
                break;
            case 2:
            default:
                borderBottomStyle = BorderStyle.SCROLLED;
                break;
        }
        a.recycle();

        isShowingTopBorder = borderTopStyle == BorderStyle.TOP_OR_BOTTOM || borderTopStyle == BorderStyle.ALWAYS;
        isShowingBottomBorder = borderBottomStyle == BorderStyle.ALWAYS;
    }

    public OnBorderVisibilityChangedListener getBorderVisibilityChangedListener() {
        return mBorderVisibilityChangedListener;
    }

    public void setBorderVisibilityChangedListener(OnBorderVisibilityChangedListener borderVisibilityChangedListener) {
        mBorderVisibilityChangedListener = borderVisibilityChangedListener;
    }

    public BorderStyle getBorderTopStyle() {
        return borderTopStyle;
    }

    public void setBorderTopStyle(BorderStyle borderTopStyle) {
        if (borderTopStyle != this.borderTopStyle) {
            this.borderTopStyle = borderTopStyle;
            updateBorderStatus();
        }
    }

    public BorderStyle getBorderBottomStyle() {
        return borderBottomStyle;
    }

    public void setBorderBottomStyle(BorderStyle borderBottomStyle) {
        if (borderBottomStyle != this.borderBottomStyle) {
            this.borderBottomStyle = borderBottomStyle;
            updateBorderStatus();
        }
    }

    public Drawable getBorderTopDrawable() {
        return borderTopDrawable;
    }

    public void setBorderTopDrawable(Drawable borderTopDrawable) {
        if (borderTopDrawable != this.borderTopDrawable) {
            this.borderTopDrawable = borderTopDrawable;
            postInvalidate();
        }
    }

    public Drawable getBorderBottomDrawable() {
        return borderBottomDrawable;
    }

    public void setBorderBottomDrawable(Drawable borderBottomDrawable) {
        if (borderBottomDrawable != this.borderBottomDrawable) {
            this.borderBottomDrawable = borderBottomDrawable;
            postInvalidate();
        }
    }

    public boolean isShowingTopBorder() {
        return isShowingTopBorder;
    }

    public boolean isShowingBottomBorder() {
        return isShowingBottomBorder;
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            NestedScrollView.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
            int parentSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            scrollRange = Math.max(0, childSize - parentSpace);
        }
        return scrollRange;
    }

    public void onBorderVisibilityChanged(boolean top, boolean oldTop, boolean bottom, boolean oldBottom) {
        if (getBorderVisibilityChangedListener() != null) {
            getBorderVisibilityChangedListener().onBorderVisibilityChanged(top, oldTop, bottom, oldBottom);
        }

        this.isShowingTopBorder = top;
        this.isShowingBottomBorder = bottom;

        postInvalidate();
    }

    private void updateBorderStatus() {
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

        if (!Objects.equals(this.isShowingTopBorder, isShowingTopBorder) || !Objects.equals(this.isShowingBottomBorder, isShowingBottomBorder)) {
            onBorderVisibilityChanged(isShowingTopBorder,
                    this.isShowingTopBorder,
                    isShowingBottomBorder,
                    this.isShowingBottomBorder);
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

        if (borderTopDrawable == null && borderBottomDrawable == null) {
            return;
        }

        int c = canvas.save();

        if (borderTopDrawable != null) {
            int dy = getScrollY() + getPaddingTop();
            canvas.translate(0, dy);
            if (isShowingTopBorder()) {
                borderTopDrawable.setBounds(0, 0, canvas.getWidth(), borderTopDrawable.getIntrinsicHeight());
                onDrawBoardTop(borderTopDrawable, canvas);
            }
            canvas.translate(0, -dy);
        }

        if (borderBottomDrawable != null) {
            int dy = getScrollY() + canvas.getHeight() - borderBottomDrawable.getIntrinsicHeight() - getPaddingBottom();
            canvas.translate(0, dy);

            if (isShowingBottomBorder()) {
                borderBottomDrawable.setBounds(0, 0, canvas.getWidth(), borderBottomDrawable.getIntrinsicHeight());
                onDrawBoardBottom(borderBottomDrawable, canvas);
            }
        }

        canvas.restoreToCount(c);
    }

    public void onDrawBoardTop(Drawable drawable, Canvas canvas) {
        drawable.draw(canvas);
    }

    public void onDrawBoardBottom(Drawable drawable, Canvas canvas) {
        drawable.draw(canvas);
    }
}
