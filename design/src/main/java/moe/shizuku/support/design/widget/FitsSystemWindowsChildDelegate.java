package moe.shizuku.support.design.widget;

import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.Nullable;

import java.util.Objects;

class FitsSystemWindowsChildDelegate {

    private View mView;

    @Nullable
    private WindowInsets mLastInsets;

    FitsSystemWindowsChildDelegate(View view) {
        mView = view;
        mView.setOnApplyWindowInsetsListener((v, insets) -> onWindowInsetChanged(insets));
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
            mView.setPadding(mView.getPaddingLeft() + getInsetLeft(),
                    mView.getPaddingTop() + getInsetTop(),
                    mView.getPaddingRight() + getInsetRight(),
                    mView.getPaddingBottom() + getInsetBottom());
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
