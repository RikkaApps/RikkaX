package moe.shizuku.support.design;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class DialogNestedScrollView extends NestedScrollView {

    public interface OnBorderVisibilityChangedListener {
        void onBorderVisibilityChanged(boolean top, boolean oldTop, boolean bottom, boolean oldBottom);
    }

    private OnBorderVisibilityChangedListener mBorderVisibilityChangedListener;

    private boolean isTopBorderShowed = false;
    private boolean isBottomBorderShowed = false;

    private boolean isTopBorderEnabled = true;
    private boolean isBottomBorderEnabled = true;

    private final Drawable mBorderLineDrawable;

    public DialogNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public DialogNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public static Drawable resolveDrawable(Resources.Theme theme, @AttrRes int attrId) {
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        Drawable res = a.getDrawable(0);
        a.recycle();
        return res;
    }

    public DialogNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderLineDrawable = resolveDrawable(context.getTheme(), android.R.attr.listDivider);
    }

    public OnBorderVisibilityChangedListener getBorderVisibilityChangedListener() {
        return mBorderVisibilityChangedListener;
    }

    public void setBorderVisibilityChangedListener(OnBorderVisibilityChangedListener borderVisibilityChangedListener) {
        mBorderVisibilityChangedListener = borderVisibilityChangedListener;
    }

    public boolean isTopBorderEnabled() {
        return isTopBorderEnabled;
    }

    public void setTopBorderEnabled(boolean topBorderEnabled) {
        isTopBorderEnabled = topBorderEnabled;
    }

    public boolean isBottomBorderEnabled() {
        return isBottomBorderEnabled;
    }

    public void setBottomBorderEnabled(boolean bottomBorderEnabled) {
        isBottomBorderEnabled = bottomBorderEnabled;
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

    private void updateBorderStatus() {
        final int offset = getScrollY();
        final int range = getScrollRange();
        final boolean isTopBorderShowed, isBottomBorderShowed;
        if (range != 0) {
            isTopBorderShowed = offset > 0;
            isBottomBorderShowed = offset < range - 1;
        } else {
            isTopBorderShowed = isBottomBorderShowed = false;
        }

        if (this.isTopBorderShowed != isTopBorderShowed || this.isBottomBorderShowed != isBottomBorderShowed) {
            if (getBorderVisibilityChangedListener() != null) {
                getBorderVisibilityChangedListener().onBorderVisibilityChanged(isTopBorderShowed, this.isTopBorderShowed, isBottomBorderShowed, this.isBottomBorderShowed);
            }

            this.isTopBorderShowed = isTopBorderShowed;
            this.isBottomBorderShowed = isBottomBorderShowed;

            postInvalidate();
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

        if (mBorderLineDrawable == null)
            return;

        mBorderLineDrawable.setBounds(0, 0, canvas.getWidth(), mBorderLineDrawable.getIntrinsicHeight());

        int c = canvas.save();
        canvas.translate(0, getScrollY());
        if (isTopBorderEnabled() && isTopBorderShowed) mBorderLineDrawable.draw(canvas);
        canvas.translate(0, canvas.getHeight() - mBorderLineDrawable.getIntrinsicHeight());
        if (isBottomBorderEnabled() && isBottomBorderShowed) mBorderLineDrawable.draw(canvas);
        canvas.restoreToCount(c);
    }
}
