package moe.shizuku.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

import java.util.Objects;

public class AppBarLayout extends LinearLayout {

    @Nullable
    private WindowInsets mLastInsets;

    public AppBarLayout(@NonNull Context context) {
        this(context, null);
    }

    public AppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AppBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(VERTICAL);

        setOnApplyWindowInsetsListener((v, insets) -> onWindowInsetChanged(insets));
    }

    WindowInsets onWindowInsetChanged(final WindowInsets insets) {
        WindowInsets newInsets = null;

        if (getFitsSystemWindows()) {
            // If we're set to fit system windows, keep the insets
            newInsets = insets;
        }

        // If our insets have changed, keep them and trigger a layout...
        if (!Objects.equals(mLastInsets, newInsets)) {
            mLastInsets = newInsets;
            requestLayout();
        }

        return insets;
    }

    final int getTopInset() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
    }

    /**
     * Whether the first child needs to be offset because it does not want to handle the top window
     * inset
     */
    private boolean shouldOffsetFirstChild() {
        if (getChildCount() > 0) {
            final View firstChild = getChildAt(0);
            return firstChild.getVisibility() != GONE && !firstChild.getFitsSystemWindows();
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If we're set to handle system windows but our first child is not, we need to add some
        // height to ourselves to pad the first child down below the status bar
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY
                && ViewCompat.getFitsSystemWindows(this)
                && shouldOffsetFirstChild()) {
            int newHeight = getMeasuredHeight();
            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                    // For AT_MOST, we need to clamp our desired height with the max height
                    newHeight =
                            MathUtils.clamp(
                                    getMeasuredHeight() + getTopInset(), 0, MeasureSpec.getSize(heightMeasureSpec));
                    break;
                case MeasureSpec.UNSPECIFIED:
                    // For UNSPECIFIED we can use any height so just add the top inset
                    newHeight += getTopInset();
                    break;
                default: // fall out
                    break;
            }
            setMeasuredDimension(getMeasuredWidth(), newHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
            // If we need to offset the first child, we need to offset all of them to make space
            final int topInset = getTopInset();
            for (int z = getChildCount() - 1; z >= 0; z--) {
                ViewCompat.offsetTopAndBottom(getChildAt(z), topInset);
            }
        }
    }
}
