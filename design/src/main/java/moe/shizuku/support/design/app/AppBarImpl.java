package moe.shizuku.support.design.app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.Nullable;

import moe.shizuku.support.design.RaisedView;
import moe.shizuku.support.design.widget.AppBarLayout;
import moe.shizuku.support.utils.ResourceUtils;

public class AppBarImpl extends AppBar {

    private static final int AFFECTS_LOGO_MASK =
            DISPLAY_SHOW_HOME | DISPLAY_USE_LOGO;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private int mDisplayOpts;

    private Drawable mIcon;
    private Drawable mLogo;
    private Drawable mNavIcon;

    private CharSequence mTitle;
    private CharSequence mSubtitle;
    private CharSequence mHomeDescription;

    private int mDefaultNavigationContentDescription;
    private Drawable mDefaultNavigationIcon;

    public AppBarImpl(AppBarLayout appBarLayout, Toolbar toolbar) {
        mAppBarLayout = appBarLayout;
        mToolbar = toolbar;
        mDefaultNavigationContentDescription = Resources.getSystem().getIdentifier("action_bar_up_description", "string", "android");
        mDefaultNavigationIcon = ResourceUtils.resolveDrawable(toolbar.getContext().getTheme(), android.R.attr.homeAsUpIndicator);
        mDisplayOpts = detectDisplayOptions();
    }

    @Override
    public void setCustomView(View view) {

    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {

    }

    @Override
    public void setCustomView(int resId) {

    }

    @Override
    public void setIcon(int resId) {
        setIcon(resId != 0 ? mToolbar.getContext().getDrawable(resId) : null);
    }

    @Override
    public void setIcon(Drawable icon) {
        mIcon = icon;
        updateToolbarLogo();
    }

    @Override
    public void setLogo(int resId) {
        setLogo(resId != 0 ? mToolbar.getContext().getDrawable(resId) : null);
    }

    @Override
    public void setLogo(Drawable logo) {
        mLogo = logo;
        updateToolbarLogo();
    }

    private void updateToolbarLogo() {
        Drawable logo = null;
        if ((mDisplayOpts & DISPLAY_SHOW_HOME) != 0) {
            if ((mDisplayOpts & DISPLAY_USE_LOGO) != 0) {
                logo = mLogo != null ? mLogo : mIcon;
            } else {
                logo = mIcon;
            }
        }
        mToolbar.setLogo(logo);
    }

    @Override
    public void setTitle(int resId) {
        setTitle(resId != 0 ? mToolbar.getContext().getText(resId) : null);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if ((mDisplayOpts & DISPLAY_SHOW_TITLE) != 0) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public void setSubtitle(int resId) {
        setSubtitle(resId != 0 ? mToolbar.getContext().getText(resId) : null);
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        mSubtitle = subtitle;
        if ((mDisplayOpts & DISPLAY_SHOW_TITLE) != 0) {
            mToolbar.setSubtitle(subtitle);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setDisplayOptions(int options) {
        setDisplayOptions(options, 0xffffffff);
    }

    @Override
    public void setDisplayOptions(int options, int mask) {
        final int newOpts = options & mask | mDisplayOpts & ~mask;
        final int oldOpts = mDisplayOpts;
        final int changed = oldOpts ^ newOpts;
        mDisplayOpts = newOpts;
        if (changed != 0) {
            if ((changed & DISPLAY_HOME_AS_UP) != 0) {
                if ((newOpts & DISPLAY_HOME_AS_UP) != 0) {
                    updateHomeAccessibility();
                }
                updateNavigationIcon();
            }
            if ((changed & AFFECTS_LOGO_MASK) != 0) {
                updateToolbarLogo();
            }
            if ((changed & DISPLAY_SHOW_TITLE) != 0) {
                if ((newOpts & DISPLAY_SHOW_TITLE) != 0) {
                    mToolbar.setTitle(mTitle);
                    mToolbar.setSubtitle(mSubtitle);
                } else {
                    mToolbar.setTitle(null);
                    mToolbar.setSubtitle(null);
                }
            }
            /*if ((changed & DISPLAY_SHOW_CUSTOM) != 0 && mCustomView != null) {
                if ((newOpts & DISPLAY_SHOW_CUSTOM) != 0) {
                    mToolbar.addView(mCustomView);
                } else {
                    mToolbar.removeView(mCustomView);
                }
            }*/
        }
    }

    @Override
    public void setDisplayUseLogoEnabled(boolean useLogo) {
        setDisplayOptions(useLogo ? DISPLAY_USE_LOGO : 0, DISPLAY_USE_LOGO);
    }

    @Override
    public void setDisplayShowHomeEnabled(boolean showHome) {
        setDisplayOptions(showHome ? DISPLAY_SHOW_HOME : 0, DISPLAY_SHOW_HOME);
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        setDisplayOptions(showHomeAsUp ? DISPLAY_HOME_AS_UP : 0, DISPLAY_HOME_AS_UP);
    }

    @Override
    public void setDisplayShowTitleEnabled(boolean showTitle) {
        setDisplayOptions(showTitle ? DISPLAY_SHOW_TITLE : 0, DISPLAY_SHOW_TITLE);
    }

    @Override
    public void setDisplayShowCustomEnabled(boolean showCustom) {
        setDisplayOptions(showCustom ? DISPLAY_SHOW_CUSTOM : 0, DISPLAY_SHOW_CUSTOM);
    }

    @Override
    public void setBackgroundDrawable(@Nullable Drawable d) {
        mAppBarLayout.setBackground(d);
    }

    @Override
    public View getCustomView() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return mToolbar.getTitle();
    }

    @Override
    public CharSequence getSubtitle() {
        return mToolbar.getSubtitle();
    }

    @Override
    public int getDisplayOptions() {
        return mDisplayOpts;
    }

    @Override
    public int getHeight() {
        return mAppBarLayout.getHeight();
    }

    @Override
    public void show() {
        mAppBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        mAppBarLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean isShowing() {
        return mAppBarLayout.getVisibility() == View.VISIBLE;
    }

    @Override
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener) {

    }

    @Override
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener) {

    }

    @Override
    public void setHomeAsUpIndicator(Drawable indicator) {
        mNavIcon = indicator;
        updateNavigationIcon();
    }

    @Override
    public void setHomeAsUpIndicator(int resId) {
        setHomeAsUpIndicator(resId != 0 ? mToolbar.getContext().getDrawable(resId) : null);
    }

    @Override
    public void setHomeActionContentDescription(CharSequence description) {
        mHomeDescription = description;
        updateHomeAccessibility();
    }

    @Override
    public void setHomeActionContentDescription(int resId) {
        setHomeActionContentDescription(resId == 0 ? null : mToolbar.getContext().getString(resId));
    }

    @Override
    public void setElevation(float elevation) {
        mAppBarLayout.setElevation(elevation);
    }

    @Override
    public float getElevation() {
        return mAppBarLayout.getElevation();
    }

    @Override
    public boolean isRaised() {
        if (mAppBarLayout instanceof RaisedView) {
            return ((RaisedView) mAppBarLayout).isRaised();
        }
        return false;
    }

    @Override
    public void setRaised(boolean raised) {
        if (mAppBarLayout instanceof RaisedView) {
            ((RaisedView) mAppBarLayout).setRaised(raised);
        }
    }

    private void updateHomeAccessibility() {
        if ((mDisplayOpts & DISPLAY_HOME_AS_UP) != 0) {
            if (TextUtils.isEmpty(mHomeDescription)) {
                mToolbar.setNavigationContentDescription(mDefaultNavigationContentDescription);
            } else {
                mToolbar.setNavigationContentDescription(mHomeDescription);
            }
        }
    }

    private void updateNavigationIcon() {
        if ((mDisplayOpts & DISPLAY_HOME_AS_UP) != 0) {
            mToolbar.setNavigationIcon(mNavIcon != null ? mNavIcon : mDefaultNavigationIcon);
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }

    private int detectDisplayOptions() {
        int opts = DISPLAY_SHOW_TITLE | DISPLAY_SHOW_HOME |
                DISPLAY_USE_LOGO;
        if (mToolbar.getNavigationIcon() != null) {
            opts |= DISPLAY_HOME_AS_UP;
        }
        return opts;
    }
}
