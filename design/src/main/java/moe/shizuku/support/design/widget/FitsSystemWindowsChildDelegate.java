package moe.shizuku.support.design.widget;

import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.Nullable;

import java.util.Objects;

class FitsSystemWindowsChildDelegate {

    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;
    private View mView;

    @Nullable
    private WindowInsets mLastInsets;

    FitsSystemWindowsChildDelegate(View view) {
        mView = view;
        mView.setOnApplyWindowInsetsListener((v, insets) -> onWindowInsetChanged(insets));

        mPaddingLeft = view.getPaddingLeft();
        mPaddingTop = view.getPaddingTop();
        mPaddingRight = view.getPaddingRight();
        mPaddingBottom = view.getPaddingBottom();
    }

    private WindowInsets onWindowInsetChanged(final WindowInsets insets) {
        WindowInsets newInsets = null;

        if (mView.getFitsSystemWindows()) {
            // If we're set to fit system windows, keep the insets
            newInsets = insets;
        }

        // If our insets have changed, keep them and trigger a layout...
        if (!Objects.equals(mLastInsets, newInsets)) {
            mLastInsets = newInsets;
            mView.setPadding(mPaddingLeft + getInsetLeft(),
                    mPaddingTop + getInsetTop(),
                    mPaddingRight + getInsetRight(),
                    mPaddingBottom + getInsetBottom());
            return insets.consumeSystemWindowInsets();
        }

        return insets;
    }

    public int getInsetTop() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
    }

    public int getInsetLeft() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetLeft() : 0;
    }

    public int getInsetRight() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetRight() : 0;
    }

    public int getInsetBottom() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetBottom() : 0;
    }
}
