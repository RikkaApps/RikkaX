package rikka.material.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.ActionMode;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

@SuppressLint("RestrictedApi")
public class ActionBarWrapper extends ActionBar {

    private ActionBar actionBar;

    public ActionBarWrapper(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public void setCustomView(View view) {
        actionBar.setCustomView(view);
    }

    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        actionBar.setCustomView(view, layoutParams);
    }

    public void setCustomView(int resId) {
        actionBar.setCustomView(resId);
    }

    public void setIcon(int resId) {
        actionBar.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        actionBar.setIcon(icon);
    }

    public void setLogo(int resId) {
        actionBar.setLogo(resId);
    }

    public void setLogo(Drawable logo) {
        actionBar.setLogo(logo);
    }

    @Deprecated
    public void setListNavigationCallbacks(SpinnerAdapter adapter, ActionBar.OnNavigationListener callback) {
        actionBar.setListNavigationCallbacks(adapter, callback);
    }

    @Deprecated
    public void setSelectedNavigationItem(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Deprecated
    public int getSelectedNavigationIndex() {
        return actionBar.getSelectedNavigationIndex();
    }

    @Deprecated
    public int getNavigationItemCount() {
        return actionBar.getNavigationItemCount();
    }

    public void setTitle(CharSequence title) {
        actionBar.setTitle(title);
    }

    public void setTitle(int resId) {
        actionBar.setTitle(resId);
    }

    public void setSubtitle(CharSequence subtitle) {
        actionBar.setSubtitle(subtitle);
    }

    public void setSubtitle(int resId) {
        actionBar.setSubtitle(resId);
    }

    public void setDisplayOptions(int options) {
        actionBar.setDisplayOptions(options);
    }

    public void setDisplayOptions(int options, int mask) {
        actionBar.setDisplayOptions(options, mask);
    }

    public void setDisplayUseLogoEnabled(boolean useLogo) {
        actionBar.setDisplayUseLogoEnabled(useLogo);
    }

    public void setDisplayShowHomeEnabled(boolean showHome) {
        actionBar.setDisplayShowHomeEnabled(showHome);
    }

    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    public void setDisplayShowTitleEnabled(boolean showTitle) {
        actionBar.setDisplayShowTitleEnabled(showTitle);
    }

    public void setDisplayShowCustomEnabled(boolean showCustom) {
        actionBar.setDisplayShowCustomEnabled(showCustom);
    }

    public void setBackgroundDrawable(@Nullable Drawable d) {
        actionBar.setBackgroundDrawable(d);
    }

    public void setStackedBackgroundDrawable(Drawable d) {
        actionBar.setStackedBackgroundDrawable(d);
    }

    public void setSplitBackgroundDrawable(Drawable d) {
        actionBar.setSplitBackgroundDrawable(d);
    }

    public View getCustomView() {
        return actionBar.getCustomView();
    }

    @Nullable
    public CharSequence getTitle() {
        return actionBar.getTitle();
    }

    @Nullable
    public CharSequence getSubtitle() {
        return actionBar.getSubtitle();
    }

    @ActionBar.NavigationMode
    @Deprecated
    public int getNavigationMode() {
        return actionBar.getNavigationMode();
    }

    @Deprecated
    public void setNavigationMode(int mode) {
        actionBar.setNavigationMode(mode);
    }

    @ActionBar.DisplayOptions
    public int getDisplayOptions() {
        return actionBar.getDisplayOptions();
    }

    @Deprecated
    public ActionBar.Tab newTab() {
        return actionBar.newTab();
    }

    @Deprecated
    public void addTab(ActionBar.Tab tab) {
        actionBar.addTab(tab);
    }

    @Deprecated
    public void addTab(ActionBar.Tab tab, boolean setSelected) {
        actionBar.addTab(tab, setSelected);
    }

    @Deprecated
    public void addTab(ActionBar.Tab tab, int position) {
        actionBar.addTab(tab, position);
    }

    @Deprecated
    public void addTab(ActionBar.Tab tab, int position, boolean setSelected) {
        actionBar.addTab(tab, position, setSelected);
    }

    @Deprecated
    public void removeTab(ActionBar.Tab tab) {
        actionBar.removeTab(tab);
    }

    @Deprecated
    public void removeTabAt(int position) {
        actionBar.removeTabAt(position);
    }

    @Deprecated
    public void removeAllTabs() {
        actionBar.removeAllTabs();
    }

    @Deprecated
    public void selectTab(ActionBar.Tab tab) {
        actionBar.selectTab(tab);
    }

    @Nullable
    @Deprecated
    public ActionBar.Tab getSelectedTab() {
        return actionBar.getSelectedTab();
    }

    @Deprecated
    public ActionBar.Tab getTabAt(int index) {
        return actionBar.getTabAt(index);
    }

    @Deprecated
    public int getTabCount() {
        return actionBar.getTabCount();
    }

    public int getHeight() {
        return actionBar.getHeight();
    }

    public void show() {
        actionBar.show();
    }

    public void hide() {
        actionBar.hide();
    }

    public boolean isShowing() {
        return actionBar.isShowing();
    }

    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener) {
        actionBar.addOnMenuVisibilityListener(listener);
    }

    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener) {
        actionBar.removeOnMenuVisibilityListener(listener);
    }

    public void setHomeButtonEnabled(boolean enabled) {
        actionBar.setHomeButtonEnabled(enabled);
    }

    public Context getThemedContext() {
        return actionBar.getThemedContext();
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean isTitleTruncated() {
        return actionBar.isTitleTruncated();
    }

    public void setHomeAsUpIndicator(@Nullable Drawable indicator) {
        actionBar.setHomeAsUpIndicator(indicator);
    }

    public void setHomeAsUpIndicator(int resId) {
        actionBar.setHomeAsUpIndicator(resId);
    }

    public void setHomeActionContentDescription(@Nullable CharSequence description) {
        actionBar.setHomeActionContentDescription(description);
    }

    public void setHomeActionContentDescription(int resId) {
        actionBar.setHomeActionContentDescription(resId);
    }

    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        actionBar.setHideOnContentScrollEnabled(hideOnContentScroll);
    }

    public boolean isHideOnContentScrollEnabled() {
        return actionBar.isHideOnContentScrollEnabled();
    }

    public int getHideOffset() {
        return actionBar.getHideOffset();
    }

    public void setHideOffset(int offset) {
        actionBar.setHideOffset(offset);
    }

    public void setElevation(float elevation) {
        actionBar.setElevation(elevation);
    }

    public float getElevation() {
        return actionBar.getElevation();
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public void setDefaultDisplayHomeAsUpEnabled(boolean enabled) {
        actionBar.setDefaultDisplayHomeAsUpEnabled(enabled);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public void setShowHideAnimationEnabled(boolean enabled) {
        actionBar.setShowHideAnimationEnabled(enabled);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public void onConfigurationChanged(Configuration config) {
        actionBar.onConfigurationChanged(config);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public void dispatchMenuVisibilityChanged(boolean visible) {
        actionBar.dispatchMenuVisibilityChanged(visible);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return actionBar.startActionMode(callback);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean openOptionsMenu() {
        return actionBar.openOptionsMenu();
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean closeOptionsMenu() {
        return actionBar.closeOptionsMenu();
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean invalidateOptionsMenu() {
        return actionBar.invalidateOptionsMenu();
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean onMenuKeyEvent(KeyEvent event) {
        return actionBar.onMenuKeyEvent(event);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean onKeyShortcut(int keyCode, KeyEvent ev) {
        return actionBar.onKeyShortcut(keyCode, ev);
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public boolean collapseActionView() {
        return actionBar.collapseActionView();
    }

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    public void setWindowTitle(CharSequence title) {
        actionBar.setWindowTitle(title);
    }
}
