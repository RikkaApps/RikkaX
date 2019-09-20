package moe.shizuku.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import java.util.Objects;

public class FitsSystemWindowsFrameLayout extends FrameLayout {

    @Nullable
    private WindowInsets mLastInsets;

    public FitsSystemWindowsFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public FitsSystemWindowsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitsSystemWindowsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FitsSystemWindowsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupForInsets();
    }

    private void setupForInsets() {
        if (ViewCompat.getFitsSystemWindows(this)) {
            // First apply the insets listener
            setOnApplyWindowInsetsListener((v, insets) -> onWindowInsetChanged(insets));

            // Now set the sys ui flags to enable us to lay out in the window insets
            setSystemUiVisibility(getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(this, null);
        }
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
            setPadding(getPaddingLeft() + insets.getSystemWindowInsetLeft(),
                    getPaddingTop(),
                    getPaddingRight() + insets.getSystemWindowInsetRight(),
                    getPaddingBottom());

            requestLayout();
        }

        return insets.replaceSystemWindowInsets(0, insets.getSystemWindowInsetTop(), 0, insets.getSystemWindowInsetBottom());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mLastInsets == null && ViewCompat.getFitsSystemWindows(this)) {
            // We're set to fitSystemWindows but we haven't had any insets yet...
            // We should request a new dispatch of window insets
            ViewCompat.requestApplyInsets(this);
        }
    }

    @Override
    public void setFitsSystemWindows(boolean fitSystemWindows) {
        super.setFitsSystemWindows(fitSystemWindows);
        setupForInsets();
    }
}
