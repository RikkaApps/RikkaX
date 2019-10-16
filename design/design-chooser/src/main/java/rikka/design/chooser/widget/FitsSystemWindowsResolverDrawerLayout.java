package rikka.design.chooser.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import java.util.Objects;

import rikka.design.chooser.R;

public class FitsSystemWindowsResolverDrawerLayout extends ResolverDrawerLayout {

    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    @Nullable
    private WindowInsets mLastInsets;

    public FitsSystemWindowsResolverDrawerLayout(Context context) {
        this(context, null);
    }

    public FitsSystemWindowsResolverDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitsSystemWindowsResolverDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

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
            setPadding(mPaddingLeft + insets.getSystemWindowInsetLeft(),
                    mPaddingTop,
                    mPaddingRight + insets.getSystemWindowInsetRight(),
                    mPaddingBottom);

            View space = findViewById(R.id.space);
            ResolverDrawerLayout.LayoutParams lp = (LayoutParams) space.getLayoutParams();
            lp.height = insets.getSystemWindowInsetBottom();
            space.setLayoutParams(lp);

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
