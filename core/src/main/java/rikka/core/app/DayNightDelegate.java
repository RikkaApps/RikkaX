package rikka.core.app;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

/**
 * Created by rikka on 2017/9/19.
 */

public class DayNightDelegate {

    private static final String TAG = "DayNightDelegate";

    private static final boolean DEBUG = false;

    private static final String KEY_LOCAL_NIGHT_MODE = "rikka:local_night_mode";

    /**
     * Mode which uses the system's night mode setting to determine if it is night or not.
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_FOLLOW_SYSTEM = -1;

    /**
     * Night mode which switches between dark and light mode depending on the time of day
     * (dark at night, light in the day).
     *
     * <p>The calculation used to determine whether it is night or not makes use of the location
     * APIs (if this app has the necessary permissions). This allows us to generate accurate
     * sunrise and sunset times. If this app does not have permission to access the location APIs
     * then we use hardcoded times which will be less accurate.</p>
     */
    public static final int MODE_NIGHT_AUTO_TIME = 0;

    /**
     * Night mode which uses always uses a light mode, enabling {@code notnight} qualified
     * resources regardless of the time.
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_NO = 1;

    /**
     * Night mode which uses always uses a dark mode, enabling {@code night} qualified
     * resources regardless of the time.
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_YES = 2;

    /**
     * Night mode which uses a dark mode when the system's 'Battery Saver' feature is enabled,
     * otherwise it uses a 'light mode'. This mode can help the device to decrease power usage,
     * depending on the display technology in the device.
     *
     * <em>Please note: this mode should only be used when running on devices which do not
     * provide a similar device-wide setting.</em>
     *
     * @see #setLocalNightMode(int)
     */
    public static final int MODE_NIGHT_AUTO_BATTERY = 3;

    /**
     * An unspecified mode for night mode. This is primarily used with
     * {@link #setLocalNightMode(int)}, to allow the default night mode to be used.
     * If both the default and local night modes are set to this value, then the default value of
     * {@link #MODE_NIGHT_FOLLOW_SYSTEM} is applied.
     */
    public static final int MODE_NIGHT_UNSPECIFIED = -100;

    private static Context sApplicationContext;

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static void setApplicationContext(Context applicationContext) {
        sApplicationContext = applicationContext;
    }

    @NightMode
    private static int sDefaultNightMode = MODE_NIGHT_UNSPECIFIED;

    @RestrictTo(LIBRARY_GROUP_PREFIX)
    @IntDef({MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_AUTO_TIME, MODE_NIGHT_FOLLOW_SYSTEM,
            MODE_NIGHT_UNSPECIFIED, MODE_NIGHT_AUTO_BATTERY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NightMode {
    }

    @IntDef({MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_FOLLOW_SYSTEM})
    @Retention(RetentionPolicy.SOURCE)
    @interface ApplyableNightMode {
    }

    @NightMode
    private int mLocalNightMode;

    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoTimeNightModeManager;
    private AutoNightModeManager mAutoBatteryNightModeManager;

    private Context mContext;

    /**
     * Returns the default night mode.
     *
     * @see #setDefaultNightMode(int)
     */
    @NightMode
    public static int getDefaultNightMode() {
        return sDefaultNightMode;
    }

    /**
     * Sets the default night mode. This is the default value used for all components, but can
     * be overridden locally via {@link #setLocalNightMode(int)}.
     *
     * <p>This is the primary method to control the DayNight functionality, since it allows
     * the delegates to avoid unnecessary recreations when possible.</p>
     *
     * <p>If this method is called after any host components with attached
     * {@link DayNightDelegate}s have been 'started', a {@code uiMode} configuration change
     * will occur in each. This may result in those components being recreated, depending
     * on their manifest configuration.</p>
     *
     * <p>Defaults to {@link #MODE_NIGHT_FOLLOW_SYSTEM}.</p>
     *
     * @see #setLocalNightMode(int)
     * @see #getDefaultNightMode()
     */
    public static void setDefaultNightMode(@NightMode int mode) {
        switch (mode) {
            case MODE_NIGHT_NO:
            case MODE_NIGHT_YES:
            case MODE_NIGHT_FOLLOW_SYSTEM:
            case MODE_NIGHT_AUTO_TIME:
            case MODE_NIGHT_AUTO_BATTERY:
                if (sDefaultNightMode != mode) {
                    sDefaultNightMode = mode;
                    //applyDayNightToActiveDelegates();
                }
                break;
            default:
                Log.d(TAG, "setDefaultNightMode() called with an unknown mode");
                break;
        }
    }

    public DayNightDelegate(Context context) {
        this(context, DayNightDelegate.getDefaultNightMode());
    }

    public DayNightDelegate(Context context, int localNightMode) {
        mContext = context;
        mLocalNightMode = localNightMode;
    }

    public int getLocalNightMode() {
        return mLocalNightMode;
    }

    public void setLocalNightMode(final @NightMode int mode) {
        if (mLocalNightMode != mode) {
            mLocalNightMode = mode;
            ensureAutoManagers();
            applyDayNight();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && mLocalNightMode == MODE_NIGHT_UNSPECIFIED) {
            // If we have a icicle and we haven't had a local night mode set yet, try and read
            // it from the icicle
            final int saved = savedInstanceState.getInt(KEY_LOCAL_NIGHT_MODE,
                    MODE_NIGHT_UNSPECIFIED);
            if (saved != mLocalNightMode) {
                setLocalNightMode(saved);
            }
        }
        ensureAutoManagers();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mLocalNightMode != MODE_NIGHT_UNSPECIFIED) {
            // If we have a local night mode set, save it
            outState.putInt(KEY_LOCAL_NIGHT_MODE, mLocalNightMode);
        }
    }

    public void onDestroy() {
        cleanupAutoManagers();
    }

    private void cleanupAutoManagers() {
        // Make sure we clean up any receivers setup for AUTO mode
        if (mAutoTimeNightModeManager != null) {
            mAutoTimeNightModeManager.cleanup();
        }
        if (mAutoBatteryNightModeManager != null) {
            mAutoBatteryNightModeManager.cleanup();
        }
    }

    private void ensureAutoManagers() {
        final int nightMode = calculateNightMode();

        if (nightMode == MODE_NIGHT_AUTO_TIME) {
            if (!getAutoTimeNightModeManager().isListening()) {
                getAutoTimeNightModeManager().setup();
            }
        } else if (mAutoTimeNightModeManager != null) {
            // Make sure we clean up the existing manager
            mAutoTimeNightModeManager.cleanup();
        }
        if (nightMode == MODE_NIGHT_AUTO_BATTERY) {
            if (!getAutoBatteryNightModeManager().isListening()) {
                getAutoBatteryNightModeManager().setup();
            }
        } else if (mAutoBatteryNightModeManager != null) {
            // Make sure we clean up the existing manager
            mAutoBatteryNightModeManager.cleanup();
        }
    }

    /**
     * Update uiMode of given configuration.
     *
     * @param context Context
     */
    public void attachBaseContext(Context context, Configuration configuration) {
        final Context activity = mContext;
        mContext = context;

        final int nightMode = calculateNightMode();
        final int modeToApply = mapNightMode(nightMode);
        final int newNightMode = mapNightModeToFlag(modeToApply);

        configuration.uiMode = newNightMode
                | (configuration.uiMode & ~Configuration.UI_MODE_NIGHT_MASK);

        mContext = activity;

        mApplyDayNightCalled = true;
    }

    public boolean isDayNightChanged() {
        final int targetFlag = mapNightModeToFlag(mapNightMode(calculateNightMode()));
        final int currentFlag =  mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        return currentFlag != targetFlag;
    }

    @NightMode
    public int calculateNightMode() {
        return mLocalNightMode != MODE_NIGHT_UNSPECIFIED ? mLocalNightMode : getDefaultNightMode();
    }

    public void applyDayNight() {
        final int nightMode = calculateNightMode();
        final int modeToApply = mapNightMode(nightMode);
        updateForNightMode(mContext, modeToApply);

        mApplyDayNightCalled = true;
    }

    @ApplyableNightMode
    private int mapNightMode(@NightMode final int mode) {
        switch (mode) {
            case MODE_NIGHT_NO:
            case MODE_NIGHT_YES:
            case MODE_NIGHT_FOLLOW_SYSTEM:
                // $FALLTHROUGH since these are all valid modes to return
                return mode;
            case MODE_NIGHT_AUTO_TIME:
                if (Build.VERSION.SDK_INT >= 23) {
                    UiModeManager uiModeManager = mContext.getSystemService(UiModeManager.class);
                    if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_AUTO) {
                        // If we're set to AUTO and the system's auto night mode is already enabled,
                        // we'll just let the system handle it by returning FOLLOW_SYSTEM
                        return MODE_NIGHT_FOLLOW_SYSTEM;
                    }
                }
                return getAutoTimeNightModeManager().getApplyableNightMode();
            case MODE_NIGHT_AUTO_BATTERY:
                return getAutoBatteryNightModeManager().getApplyableNightMode();
            case MODE_NIGHT_UNSPECIFIED:
                // If we don't have a mode specified, let the system handle it
                return MODE_NIGHT_FOLLOW_SYSTEM;
            default:
                throw new IllegalStateException("Unknown value set for night mode. Please use one"
                        + " of the MODE_NIGHT values from AppCompatDelegate.");
        }
    }

    private int mapNightModeToFlag(int mode) {
        switch (mode) {
            case MODE_NIGHT_YES:
                return Configuration.UI_MODE_NIGHT_YES;
            case MODE_NIGHT_NO:
                return Configuration.UI_MODE_NIGHT_NO;
            default:
            case MODE_NIGHT_FOLLOW_SYSTEM:
                // If we're following the system, we just use the system default from the
                // application context
                if (getApplicationContext() != null) {
                    return getApplicationContext()
                            .getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                } else {
                    Log.d(TAG, "mapNightModeToFlag() | ApplicationContext not set");
                    return Configuration.UI_MODE_NIGHT_NO;
                }
        }
    }

    /**
     * Updates the {@link Resources} configuration {@code uiMode} with the
     * chosen {@code UI_MODE_NIGHT} value.
     */
    private boolean updateForNightMode(Context context, @ApplyableNightMode final int mode) {
        final Resources res = context.getResources();
        final Configuration conf = res.getConfiguration();
        final int currentNightMode = conf.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        int newNightMode = mapNightModeToFlag(mode);

        if (currentNightMode != newNightMode) {
            if (shouldRecreateOnNightModeChange()) {
                if (DEBUG) {
                    Log.d(TAG, "applyNightMode() | Night mode changed, recreating Activity");
                }
                // If we've already been created, we need to recreate the Activity for the
                // mode to be applied
                if (context instanceof Activity) {
                    final Activity activity = (Activity) context;
                    activity.recreate();
                }
            } else {
                if (DEBUG) {
                    Log.d(TAG, "applyNightMode() | Night mode changed, updating configuration");
                }
                final Configuration newConf = new Configuration(conf);
                newConf.uiMode = newNightMode
                        | (newConf.uiMode & ~Configuration.UI_MODE_NIGHT_MASK);
                res.updateConfiguration(newConf, null);
            }
            return true;
        } else {
            if (DEBUG) {
                Log.d(TAG, "applyNightMode() | Night mode has not changed. Skipping");
            }
        }
        return false;
    }

    private boolean shouldRecreateOnNightModeChange() {
        if (mApplyDayNightCalled) {
            // If we've already appliedDayNight() (via setTheme), we need to check if the
            // Activity has configChanges set to handle uiMode changes
            final PackageManager pm = mContext.getPackageManager();
            try {
                final ActivityInfo info = pm.getActivityInfo(
                        new ComponentName(mContext, mContext.getClass()), 0);
                // We should return true (to recreate) if configChanges does not want to
                // handle uiMode
                return (info.configChanges & ActivityInfo.CONFIG_UI_MODE) == 0;
            } catch (PackageManager.NameNotFoundException e) {
                // This shouldn't happen but let's not crash because of it, we'll just log and
                // return true (since most apps will do that anyway)
                Log.d(TAG, "Exception while getting ActivityInfo", e);
                return true;
            }
        }
        return false;
    }

    @NonNull
    @RestrictTo(LIBRARY)
    private AutoNightModeManager getAutoTimeNightModeManager() {
        if (mAutoTimeNightModeManager == null) {
            mAutoTimeNightModeManager = new AutoTimeNightModeManager(
                    TwilightManager.getInstance(mContext));
        }
        return mAutoTimeNightModeManager;
    }

    private AutoNightModeManager getAutoBatteryNightModeManager() {
        if (mAutoBatteryNightModeManager == null) {
            mAutoBatteryNightModeManager = new AutoBatteryNightModeManager(mContext);
        }
        return mAutoBatteryNightModeManager;
    }

    @VisibleForTesting
    @RestrictTo(LIBRARY)
    abstract class AutoNightModeManager {
        private BroadcastReceiver mReceiver;

        @ApplyableNightMode
        abstract int getApplyableNightMode();

        abstract void onChange();

        void setup() {
            cleanup();

            final IntentFilter filter = createIntentFilterForBroadcastReceiver();
            if (filter == null || filter.countActions() == 0) {
                // Null or empty IntentFilter, skip
                return;
            }

            if (mReceiver == null) {
                mReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        onChange();
                    }
                };
            }
            mContext.registerReceiver(mReceiver, filter);
        }

        @Nullable
        abstract IntentFilter createIntentFilterForBroadcastReceiver();

        void cleanup() {
            if (mReceiver != null) {
                try {
                    mContext.unregisterReceiver(mReceiver);
                } catch (IllegalArgumentException e) {
                    // If the receiver has already been unregistered, unregisterReceiver() will
                    // throw an exception. Just ignore and carry-on...
                }
                mReceiver = null;
            }
        }

        boolean isListening() {
            return mReceiver != null;
        }
    }

    private class AutoTimeNightModeManager extends AutoNightModeManager {
        private final TwilightManager mTwilightManager;

        AutoTimeNightModeManager(@NonNull TwilightManager twilightManager) {
            mTwilightManager = twilightManager;
        }

        @ApplyableNightMode
        @Override
        public int getApplyableNightMode() {
            return mTwilightManager.isNight() ? MODE_NIGHT_YES : MODE_NIGHT_NO;
        }

        @Override
        public void onChange() {
            applyDayNight();
        }

        @Override
        IntentFilter createIntentFilterForBroadcastReceiver() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_TIME_TICK);
            return filter;
        }
    }

    private class AutoBatteryNightModeManager extends AutoNightModeManager {
        private final PowerManager mPowerManager;

        AutoBatteryNightModeManager(@NonNull Context context) {
            mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        }

        @ApplyableNightMode
        @Override
        public int getApplyableNightMode() {
            if (Build.VERSION.SDK_INT >= 21) {
                return mPowerManager.isPowerSaveMode() ? MODE_NIGHT_YES : MODE_NIGHT_NO;
            }
            return MODE_NIGHT_NO;
        }

        @Override
        public void onChange() {
            applyDayNight();
        }

        @Override
        IntentFilter createIntentFilterForBroadcastReceiver() {
            if (Build.VERSION.SDK_INT >= 21) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);
                return filter;
            }
            return null;
        }
    }
}
