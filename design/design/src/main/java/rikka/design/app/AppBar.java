package rikka.design.app;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AppBar {

    /**
     * Use logo instead of icon if available. This flag will cause appropriate
     * navigation modes to use a wider logo in place of the standard icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_USE_LOGO = 1;

    /**
     * Show 'home' elements in this action bar, leaving more space for other
     * navigation elements. This includes logo and icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_HOME = 1 << 1;

    /**
     * Display the 'home' element such that it appears as an 'up' affordance.
     * e.g. show an arrow to the left indicating the action that will be taken.
     *
     * Set this flag if selecting the 'home' button in the action bar to return
     * up by a single level in your UI rather than back to the top level or front page.
     *
     * <p>Setting this option will implicitly enable interaction with the home/up
     * button.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_HOME_AS_UP = 1 << 2;

    /**
     * Show the activity title and subtitle, if present.
     *
     * @see #setTitle(CharSequence)
     * @see #setTitle(int)
     * @see #setSubtitle(CharSequence)
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_TITLE = 1 << 3;

    /**
     * Show the custom view if one has been set.
     * @see #setCustomView(View)
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_CUSTOM = 1 << 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {
            DISPLAY_USE_LOGO,
            DISPLAY_SHOW_HOME,
            DISPLAY_HOME_AS_UP,
            DISPLAY_SHOW_TITLE,
            DISPLAY_SHOW_CUSTOM,
    })
    public @interface DisplayOptions {}

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.
     *
     * @param view Custom navigation view to place in the ActionBar.
     */
    public abstract void setCustomView(View view);

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
     * <p>The display option {@link #DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param view Custom navigation view to place in the ActionBar.
     * @param layoutParams How this custom view should layout in the bar.
     *
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setCustomView(View view, ActionBar.LayoutParams layoutParams);

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
     * <p>The display option {@link #DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param resId Resource ID of a layout to inflate into the ActionBar.
     *
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setCustomView(@LayoutRes int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param resId Resource ID of a drawable to show as an icon.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setIcon(@DrawableRes int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param icon Drawable to show as an icon.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setIcon(Drawable icon);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param resId Resource ID of a drawable to show as a logo.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setLogo(@DrawableRes int resId);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param logo Drawable to show as a logo.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setLogo(Drawable logo);

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param title Title to set
     *
     * @see #setTitle(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setTitle(CharSequence title);

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId Resource ID of title string to set
     *
     * @see #setTitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setTitle(@StringRes int resId);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set. Set to null to disable the
     * subtitle entirely.
     *
     * @param subtitle Subtitle to set
     *
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setSubtitle(CharSequence subtitle);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId Resource ID of subtitle string to set
     *
     * @see #setSubtitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setSubtitle(@StringRes int resId);

    /**
     * Set display options. This changes all display option bits at once. To change
     * a limited subset of display options, see {@link #setDisplayOptions(int, int)}.
     *
     * @param options A combination of the bits defined by the DISPLAY_ constants
     *                defined in ActionBar.
     */
    public abstract void setDisplayOptions(@DisplayOptions int options);

    /**
     * Set selected display options. Only the options specified by mask will be changed.
     * To change all display option bits at once, see {@link #setDisplayOptions(int)}.
     *
     * <p>Example: setDisplayOptions(0, DISPLAY_SHOW_HOME) will disable the
     * {@link #DISPLAY_SHOW_HOME} option.
     * setDisplayOptions(DISPLAY_SHOW_HOME, DISPLAY_SHOW_HOME | DISPLAY_USE_LOGO)
     * will enable {@link #DISPLAY_SHOW_HOME} and disable {@link #DISPLAY_USE_LOGO}.
     *
     * @param options A combination of the bits defined by the DISPLAY_ constants
     *                defined in ActionBar.
     * @param mask A bit mask declaring which display options should be changed.
     */
    public abstract void setDisplayOptions(@DisplayOptions int options, @DisplayOptions int mask);

    /**
     * Set whether to display the activity logo rather than the activity icon.
     * A logo is often a wider, more detailed image.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param useLogo true to use the activity logo, false to use the activity icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayUseLogoEnabled(boolean useLogo);

    /**
     * Set whether to include the application home affordance in the action bar.
     * Home is presented as either an activity icon or logo.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHome true to show home, false otherwise.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowHomeEnabled(boolean showHome);

    /**
     * Set whether home should be displayed as an "up" affordance.
     * Set this to true if selecting "home" returns up by a single level in your UI
     * rather than back to the top level or front page.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHomeAsUp true to show the user that selecting home will return one
     *                     level up rather than to the top level of the app.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

    /**
     * Set whether an activity title/subtitle should be displayed.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showTitle true to display a title/subtitle if present.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowTitleEnabled(boolean showTitle);

    /**
     * Set whether a custom view should be displayed, if set.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showCustom true if the currently set custom view should be displayed, false otherwise.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowCustomEnabled(boolean showCustom);

    /**
     * Set the ActionBar's background. This will be used for the primary
     * action bar.
     *
     * @param d Background drawable
     */
    public abstract void setBackgroundDrawable(@Nullable Drawable d);

    /**
     * @return The current custom view.
     */
    public abstract View getCustomView();

    /**
     * Returns the current ActionBar title in standard mode.
     *
     * @return The current ActionBar title or null.
     */
    public abstract CharSequence getTitle();

    /**
     * Returns the current ActionBar subtitle in standard mode.
     *
     * @return The current ActionBar subtitle or null.
     */
    public abstract CharSequence getSubtitle();

    /**
     * @return The current set of display options.
     */
    public abstract int getDisplayOptions();

    /**
     * Retrieve the current height of the ActionBar.
     *
     * @return The ActionBar's height
     */
    public abstract int getHeight();

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
    public abstract void show();

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
    public abstract void hide();

    /**
     * @return <code>true</code> if the ActionBar is showing, <code>false</code> otherwise.
     */
    public abstract boolean isShowing();

    /**
     * Add a listener that will respond to menu visibility change events.
     *
     * @param listener The new listener to add
     */
    public abstract void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener);

    /**
     * Remove a menu visibility listener. This listener will no longer receive menu
     * visibility change events.
     *
     * @param listener A listener to remove that was previously added
     */
    public abstract void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener listener);

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when {@link #DISPLAY_HOME_AS_UP} is enabled. This can be useful if you are using
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
     *
     * @see #setDisplayOptions(int, int)
     * @see #setDisplayHomeAsUpEnabled(boolean)
     * @see #setHomeActionContentDescription(int)
     */
    public void setHomeAsUpIndicator(Drawable indicator) { }

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when {@link #DISPLAY_HOME_AS_UP} is enabled. This can be useful if you are using
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
     *
     * @see #setDisplayOptions(int, int)
     * @see #setDisplayHomeAsUpEnabled(boolean)
     * @see #setHomeActionContentDescription(int)
     */
    public void setHomeAsUpIndicator(@DrawableRes int resId) { }

    /**
     * Set an alternate description for the Home/Up action, when enabled.
     *
     * <p>This description is commonly used for accessibility/screen readers when
     * the Home action is enabled. (See {@link #setDisplayHomeAsUpEnabled(boolean)}.)
     * Examples of this are, "Navigate Home" or "Navigate Up" depending on the
     * {@link #DISPLAY_HOME_AS_UP} display option. If you have changed the home-as-up
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
    public void setHomeActionContentDescription(CharSequence description) { }

    /**
     * Set an alternate description for the Home/Up action, when enabled.
     *
     * <p>This description is commonly used for accessibility/screen readers when
     * the Home action is enabled. (See {@link #setDisplayHomeAsUpEnabled(boolean)}.)
     * Examples of this are, "Navigate Home" or "Navigate Up" depending on the
     * {@link #DISPLAY_HOME_AS_UP} display option. If you have changed the home-as-up
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
    public void setHomeActionContentDescription(@StringRes int resId) { }

    /**
     * Set the Z-axis elevation of the action bar in pixels.
     *
     * <p>The action bar's elevation is the distance it is placed from its parent surface. Higher
     * values are closer to the user.</p>
     *
     * @param elevation Elevation value in pixels
     */
    public abstract void setElevation(float elevation);

    /**
     * Get the Z-axis elevation of the action bar in pixels.
     *
     * <p>The action bar's elevation is the distance it is placed from its parent surface. Higher
     * values are closer to the user.</p>
     *
     * @return Elevation value in pixels
     */
    public abstract float getElevation();

    public abstract boolean isRaised();

    public abstract void setRaised(boolean raised);
}
