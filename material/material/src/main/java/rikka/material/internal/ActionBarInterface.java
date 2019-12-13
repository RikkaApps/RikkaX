package rikka.material.internal;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.SpinnerAdapter;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBar.DisplayOptions;
import androidx.appcompat.app.ActionBar.LayoutParams;
import androidx.appcompat.app.ActionBar.NavigationMode;
import androidx.appcompat.app.ActionBar.OnMenuVisibilityListener;
import androidx.appcompat.app.ActionBar.OnNavigationListener;
import androidx.appcompat.app.ActionBar.Tab;
import androidx.appcompat.view.ActionMode;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

interface ActionBarInterface {

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     * <p>
     * Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.
     *
     * @param view Custom navigation view to place in the ActionBar.
     */
    void setCustomView(View view);

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * <p>Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.</p>
     *
     * <p>The display option {@link ActionBar#DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param view         Custom navigation view to place in the ActionBar.
     * @param layoutParams How this custom view should layout in the bar.
     * @see #setDisplayOptions(int, int)
     */
    void setCustomView(View view, LayoutParams layoutParams);

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * <p>Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.</p>
     *
     * <p>The display option {@link ActionBar#DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param resId Resource ID of a layout to inflate into the ActionBar.
     * @see #setDisplayOptions(int, int)
     */
    void setCustomView(int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     * <p>
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link ActionBar#DISPLAY_USE_LOGO}.
     *
     * @param resId Resource ID of a drawable to show as an icon.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    void setIcon(@DrawableRes int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     * <p>
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link ActionBar#DISPLAY_USE_LOGO}.
     *
     * @param icon Drawable to show as an icon.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    void setIcon(Drawable icon);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     * <p>
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link ActionBar#DISPLAY_USE_LOGO}.
     *
     * @param resId Resource ID of a drawable to show as a logo.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    void setLogo(@DrawableRes int resId);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     * <p>
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link ActionBar#DISPLAY_USE_LOGO}.
     *
     * @param logo Drawable to show as a logo.
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    void setLogo(Drawable logo);

    /**
     * Set the adapter and navigation callback for list navigation mode.
     * <p>
     * The supplied adapter will provide views for the expanded list as well as
     * the currently selected item. (These may be displayed differently.)
     * <p>
     * The supplied OnNavigationListener will alert the application when the user
     * changes the current list selection.
     *
     * @param adapter  An adapter that will provide views both to display
     *                 the current navigation selection and populate views
     *                 within the dropdown navigation menu.
     * @param callback An OnNavigationListener that will receive events when the user
     *                 selects a navigation item.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void setListNavigationCallbacks(SpinnerAdapter adapter,
                                    OnNavigationListener callback);

    /**
     * Set the selected navigation item in list or tabbed navigation modes.
     *
     * @param position Position of the item to select.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void setSelectedNavigationItem(int position);

    /**
     * Get the position of the selected navigation item in list or tabbed navigation modes.
     *
     * @return Position of the selected item.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    int getSelectedNavigationIndex();

    /**
     * Get the number of navigation items present in the current navigation mode.
     *
     * @return Number of navigation items.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    int getNavigationItemCount();

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link ActionBar#DISPLAY_SHOW_TITLE} is set.
     *
     * @param title Title to set
     * @see #setTitle(int)
     * @see #setDisplayOptions(int, int)
     */
    void setTitle(CharSequence title);

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link ActionBar#DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId Resource ID of title string to set
     * @see #setTitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    void setTitle(@StringRes int resId);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link ActionBar#DISPLAY_SHOW_TITLE} is set. Set to null to disable the
     * subtitle entirely.
     *
     * @param subtitle Subtitle to set
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int, int)
     */
    void setSubtitle(CharSequence subtitle);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link ActionBar#DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId Resource ID of subtitle string to set
     * @see #setSubtitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    void setSubtitle(int resId);

    /**
     * Set display options. This changes all display option bits at once. To change
     * a limited subset of display options, see {@link #setDisplayOptions(int, int)}.
     *
     * @param options A combination of the bits defined by the DISPLAY_ constants
     *                defined in ActionBar.
     */
    void setDisplayOptions(@DisplayOptions int options);

    /**
     * Set selected display options. Only the options specified by mask will be changed.
     * To change all display option bits at once, see {@link #setDisplayOptions(int)}.
     *
     * <p>Example: setDisplayOptions(0, DISPLAY_SHOW_HOME) will disable the
     * {@link ActionBar#DISPLAY_SHOW_HOME} option.
     * setDisplayOptions(DISPLAY_SHOW_HOME, DISPLAY_SHOW_HOME | DISPLAY_USE_LOGO)
     * will enable {@link ActionBar#DISPLAY_SHOW_HOME} and disable {@link ActionBar#DISPLAY_USE_LOGO}.
     *
     * @param options A combination of the bits defined by the DISPLAY_ constants
     *                defined in ActionBar.
     * @param mask    A bit mask declaring which display options should be changed.
     */
    void setDisplayOptions(@DisplayOptions int options, @DisplayOptions int mask);

    /**
     * Set whether to display the activity logo rather than the activity icon.
     * A logo is often a wider, more detailed image.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param useLogo true to use the activity logo, false to use the activity icon.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    void setDisplayUseLogoEnabled(boolean useLogo);

    /**
     * Set whether to include the application home affordance in the action bar.
     * Home is presented as either an activity icon or logo.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHome true to show home, false otherwise.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    void setDisplayShowHomeEnabled(boolean showHome);

    /**
     * Set whether home should be displayed as an "up" affordance.
     * Set this to true if selecting "home" returns up by a single level in your UI
     * rather than back to the top level or front page.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHomeAsUp true to show the user that selecting home will return one
     *                     level up rather than to the top level of the app.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

    /**
     * Set whether an activity title/subtitle should be displayed.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showTitle true to display a title/subtitle if present.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    void setDisplayShowTitleEnabled(boolean showTitle);

    /**
     * Set whether a custom view should be displayed, if set.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showCustom true if the currently set custom view should be displayed, false otherwise.
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    void setDisplayShowCustomEnabled(boolean showCustom);

    /**
     * Set the ActionBar's background. This will be used for the primary
     * action bar.
     *
     * @param d Background drawable
     * @see #setStackedBackgroundDrawable(Drawable)
     * @see #setSplitBackgroundDrawable(Drawable)
     */
    void setBackgroundDrawable(@Nullable Drawable d);

    /**
     * Set the ActionBar's stacked background. This will appear
     * in the second row/stacked bar on some devices and configurations.
     *
     * @param d Background drawable for the stacked row
     */
    void setStackedBackgroundDrawable(Drawable d);

    /**
     * Set the ActionBar's split background. This will appear in
     * the split action bar containing menu-provided action buttons
     * on some devices and configurations.
     * <p>You can enable split action bar with {@link android.R.attr#uiOptions}
     *
     * @param d Background drawable for the split bar
     */
    void setSplitBackgroundDrawable(Drawable d);

    /**
     * @return The current custom view.
     */
    View getCustomView();

    /**
     * Returns the current ActionBar title in standard mode.
     * Returns null if {@link #getNavigationMode()} would not return
     * {@link ActionBar#NAVIGATION_MODE_STANDARD}.
     *
     * @return The current ActionBar title or null.
     */
    @Nullable
    CharSequence getTitle();

    /**
     * Returns the current ActionBar subtitle in standard mode.
     * Returns null if {@link #getNavigationMode()} would not return
     * {@link ActionBar#NAVIGATION_MODE_STANDARD}.
     *
     * @return The current ActionBar subtitle or null.
     */
    @Nullable
    CharSequence getSubtitle();

    /**
     * Returns the current navigation mode. The result will be one of:
     * <ul>
     * <li>{@link ActionBar#NAVIGATION_MODE_STANDARD}</li>
     * <li>{@link ActionBar#NAVIGATION_MODE_LIST}</li>
     * <li>{@link ActionBar#NAVIGATION_MODE_TABS}</li>
     * </ul>
     *
     * @return The current navigation mode.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    @NavigationMode
    int getNavigationMode();

    /**
     * Set the current navigation mode.
     *
     * @param mode The new mode to set.
     * @see ActionBar#NAVIGATION_MODE_STANDARD
     * @see ActionBar#NAVIGATION_MODE_LIST
     * @see ActionBar#NAVIGATION_MODE_TABS
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void setNavigationMode(@NavigationMode int mode);

    /**
     * @return The current set of display options.
     */
    @DisplayOptions
    int getDisplayOptions();

    /**
     * Create and return a new {@link Tab}.
     * This tab will not be included in the action bar until it is added.
     *
     * @return A new Tab
     * @see #addTab(Tab)
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    Tab newTab();

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be added at the end of the list.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab Tab to add
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void addTab(Tab tab);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be added at the end of the list.
     *
     * @param tab         Tab to add
     * @param setSelected True if the added tab should become the selected tab.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void addTab(Tab tab, boolean setSelected);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be inserted at
     * <code>position</code>. If this is the first tab to be added it will become
     * the selected tab.
     *
     * @param tab      The tab to add
     * @param position The new position of the tab
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void addTab(Tab tab, int position);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be inserted at
     * <code>position</code>.
     *
     * @param tab         The tab to add
     * @param position    The new position of the tab
     * @param setSelected True if the added tab should become the selected tab.
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void addTab(Tab tab, int position, boolean setSelected);

    /**
     * Remove a tab from the action bar. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param tab The tab to remove
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void removeTab(Tab tab);

    /**
     * Remove a tab from the action bar. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param position Position of the tab to remove
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void removeTabAt(int position);

    /**
     * Remove all tabs from the action bar and deselect the current tab.
     *
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void removeAllTabs();

    /**
     * Select the specified tab. If it is not a child of this action bar it will be added.
     *
     * <p>Note: If you want to select by index, use {@link #setSelectedNavigationItem(int)}.</p>
     *
     * @param tab Tab to select
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    void selectTab(Tab tab);

    /**
     * Returns the currently selected tab if in tabbed navigation mode and there is at least
     * one tab present.
     *
     * @return The currently selected tab or null
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    @Nullable
    Tab getSelectedTab();

    /**
     * Returns the tab at the specified index.
     *
     * @param index Index value in the range 0-get
     * @return
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    Tab getTabAt(int index);

    /**
     * Returns the number of tabs currently registered with the action bar.
     *
     * @return Tab count
     * @deprecated Action bar navigation modes are deprecated and not supported by inline
     * toolbar action bars. Consider using other
     * <a href="http://developer.android.com/design/patterns/navigation.html">common
     * navigation patterns</a> instead.
     */
    @Deprecated
    int getTabCount();

    /**
     * Retrieve the current height of the ActionBar.
     *
     * @return The ActionBar's height
     */
    int getHeight();

    /**
     * Show the ActionBar if it is not currently showing.
     * If the window hosting the ActionBar does not have the feature
     * {@link Window#FEATURE_ACTION_BAR_OVERLAY} it will resize application
     * content to fit the new space available.
     *
     * <p>If you are hiding the ActionBar through
     * {@link View#SYSTEM_UI_FLAG_FULLSCREEN View.SYSTEM_UI_FLAG_FULLSCREEN},
     * you should not call this function directly.
     */
    void show();

    /**
     * Hide the ActionBar if it is currently showing.
     * If the window hosting the ActionBar does not have the feature
     * {@link Window#FEATURE_ACTION_BAR_OVERLAY} it will resize application
     * content to fit the new space available.
     *
     * <p>Instead of calling this function directly, you can also cause an
     * ActionBar using the overlay feature to hide through
     * {@link View#SYSTEM_UI_FLAG_FULLSCREEN View.SYSTEM_UI_FLAG_FULLSCREEN}.
     * Hiding the ActionBar through this system UI flag allows you to more
     * seamlessly hide it in conjunction with other screen decorations.
     */
    void hide();

    /**
     * @return <code>true</code> if the ActionBar is showing, <code>false</code> otherwise.
     */
    boolean isShowing();

    /**
     * Add a listener that will respond to menu visibility change events.
     *
     * @param listener The new listener to add
     */
    void addOnMenuVisibilityListener(OnMenuVisibilityListener listener);

    /**
     * Remove a menu visibility listener. This listener will no longer receive menu
     * visibility change events.
     *
     * @param listener A listener to remove that was previously added
     */
    void removeOnMenuVisibilityListener(OnMenuVisibilityListener listener);

    /**
     * Enable or disable the "home" button in the corner of the action bar. (Note that this
     * is the application home/up affordance on the action bar, not the system wide home
     * button.)
     *
     * <p>This defaults to true for packages targeting &lt; API 14. For packages targeting
     * API 14 or greater, the application should call this method to enable interaction
     * with the home/up affordance.
     *
     * <p>Setting the {@link ActionBar#DISPLAY_HOME_AS_UP} display option will automatically enable
     * the home button.
     *
     * @param enabled true to enable the home button, false to disable the home button.
     */
    void setHomeButtonEnabled(boolean enabled);

    /**
     * Returns a {@link Context} with an appropriate theme for creating views that
     * will appear in the action bar. If you are inflating or instantiating custom views
     * that will appear in an action bar, you should use the Context returned by this method.
     * (This includes adapters used for list navigation mode.)
     * This will ensure that views contrast properly against the action bar.
     *
     * @return A themed Context for creating views
     */
    Context getThemedContext();

    /**
     * Returns true if the Title field has been truncated during layout for lack
     * of available space.
     *
     * @return true if the Title field has been truncated
     * @hide pending API approval
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean isTitleTruncated();

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when {@link ActionBar#DISPLAY_HOME_AS_UP} is enabled. This can be useful if you are using
     * this mode to display an alternate selection for up navigation, such as a sliding drawer.
     *
     * <p>If you pass <code>null</code> to this method, the default drawable from the theme
     * will be used.</p>
     *
     * <p>If you implement alternate or intermediate behavior around Up, you should also
     * call {@link #setHomeActionContentDescription(int) setHomeActionContentDescription()}
     * to provide a correct description of the action for accessibility support.</p>
     *
     * @param indicator A drawable to use for the up indicator, or null to use the theme's default
     * @see #setDisplayOptions(int, int)
     * @see #setDisplayHomeAsUpEnabled(boolean)
     * @see #setHomeActionContentDescription(int)
     */
    void setHomeAsUpIndicator(@Nullable Drawable indicator);

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when {@link ActionBar#DISPLAY_HOME_AS_UP} is enabled. This can be useful if you are using
     * this mode to display an alternate selection for up navigation, such as a sliding drawer.
     *
     * <p>If you pass <code>0</code> to this method, the default drawable from the theme
     * will be used.</p>
     *
     * <p>If you implement alternate or intermediate behavior around Up, you should also
     * call {@link #setHomeActionContentDescription(int) setHomeActionContentDescription()}
     * to provide a correct description of the action for accessibility support.</p>
     *
     * @param resId Resource ID of a drawable to use for the up indicator, or null
     *              to use the theme's default
     * @see #setDisplayOptions(int, int)
     * @see #setDisplayHomeAsUpEnabled(boolean)
     * @see #setHomeActionContentDescription(int)
     */
    void setHomeAsUpIndicator(@DrawableRes int resId);

    /**
     * Set an alternate description for the Home/Up action, when enabled.
     *
     * <p>This description is commonly used for accessibility/screen readers when
     * the Home action is enabled. (See {@link #setDisplayHomeAsUpEnabled(boolean)}.)
     * Examples of this are, "Navigate Home" or "Navigate Up" depending on the
     * {@link ActionBar#DISPLAY_HOME_AS_UP} display option. If you have changed the home-as-up
     * indicator using {@link #setHomeAsUpIndicator(int)} to indicate more specific
     * functionality such as a sliding drawer, you should also set this to accurately
     * describe the action.</p>
     *
     * <p>Setting this to <code>null</code> will use the system default description.</p>
     *
     * @param description New description for the Home action when enabled
     * @see #setHomeAsUpIndicator(int)
     * @see #setHomeAsUpIndicator(android.graphics.drawable.Drawable)
     */
    void setHomeActionContentDescription(@Nullable CharSequence description);

    /**
     * Set an alternate description for the Home/Up action, when enabled.
     *
     * <p>This description is commonly used for accessibility/screen readers when
     * the Home action is enabled. (See {@link #setDisplayHomeAsUpEnabled(boolean)}.)
     * Examples of this are, "Navigate Home" or "Navigate Up" depending on the
     * {@linkActionBar #DISPLAY_HOME_AS_UP} display option. If you have changed the home-as-up
     * indicator using {@link #setHomeAsUpIndicator(int)} to indicate more specific
     * functionality such as a sliding drawer, you should also set this to accurately
     * describe the action.</p>
     *
     * <p>Setting this to <code>0</code> will use the system default description.</p>
     *
     * @param resId Resource ID of a string to use as the new description
     *              for the Home action when enabled
     * @see #setHomeAsUpIndicator(int)
     * @see #setHomeAsUpIndicator(android.graphics.drawable.Drawable)
     */
    void setHomeActionContentDescription(@StringRes int resId);

    /**
     * Enable hiding the action bar on content scroll.
     *
     * <p>If enabled, the action bar will scroll out of sight along with a
     * {@link View#setNestedScrollingEnabled(boolean) nested scrolling child} view's content.
     * The action bar must be in {@link Window#FEATURE_ACTION_BAR_OVERLAY overlay mode}
     * to enable hiding on content scroll.</p>
     *
     * <p>When partially scrolled off screen the action bar is considered
     * {@link #hide() hidden}. A call to {@link #show() show} will cause it to return to full view.
     * </p>
     *
     * @param hideOnContentScroll true to enable hiding on content scroll.
     */
    void setHideOnContentScrollEnabled(boolean hideOnContentScroll);

    /**
     * Return whether the action bar is configured to scroll out of sight along with
     * a {@link View#setNestedScrollingEnabled(boolean) nested scrolling child}.
     *
     * @return true if hide-on-content-scroll is enabled
     * @see #setHideOnContentScrollEnabled(boolean)
     */
    boolean isHideOnContentScrollEnabled();

    /**
     * Return the current vertical offset of the action bar.
     *
     * <p>The action bar's current hide offset is the distance that the action bar is currently
     * scrolled offscreen in pixels. The valid range is 0 (fully visible) to the action bar's
     * current measured {@link #getHeight() height} (fully invisible).</p>
     *
     * @return The action bar's offset toward its fully hidden state in pixels
     */
    int getHideOffset();

    /**
     * Set the current hide offset of the action bar.
     *
     * <p>The action bar's current hide offset is the distance that the action bar is currently
     * scrolled offscreen in pixels. The valid range is 0 (fully visible) to the action bar's
     * current measured {@link #getHeight() height} (fully invisible).</p>
     *
     * @param offset The action bar's offset toward its fully hidden state in pixels.
     */
    void setHideOffset(int offset);

    /**
     * Set the Z-axis elevation of the action bar in pixels.
     *
     * <p>The action bar's elevation is the distance it is placed from its parent surface. Higher
     * values are closer to the user.</p>
     *
     * @param elevation Elevation value in pixels
     */
    void setElevation(float elevation);

    /**
     * Get the Z-axis elevation of the action bar in pixels.
     *
     * <p>The action bar's elevation is the distance it is placed from its parent surface. Higher
     * values are closer to the user.</p>
     *
     * @return Elevation value in pixels
     */
    float getElevation();

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    void setDefaultDisplayHomeAsUpEnabled(boolean enabled);

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    void setShowHideAnimationEnabled(boolean enabled);

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    void onConfigurationChanged(Configuration config);

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    void dispatchMenuVisibilityChanged(boolean visible);

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    ActionMode startActionMode(ActionMode.Callback callback);

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean openOptionsMenu();

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean closeOptionsMenu();

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean invalidateOptionsMenu();

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean onMenuKeyEvent(KeyEvent event);

    /**
     * @hide
     **/
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean onKeyShortcut(int keyCode, KeyEvent ev);

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean collapseActionView();

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    void setWindowTitle(CharSequence title);

    /**
     * Attempts to move focus to the ActionBar if it does not already contain the focus.
     *
     * @return {@code true} if focus changes or {@code false} if focus doesn't change.
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP_PREFIX)
    boolean requestFocus();

    /**
     * Clean up any resources
     */
    void onDestroy();
}
