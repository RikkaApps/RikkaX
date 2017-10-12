package moe.shizuku.support.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import moe.shizuku.support.BuildConfig;
import moe.shizuku.support.R;

import static moe.shizuku.support.app.DayNightDelegate.NightMode.*;

/**
 * Created by rikka on 2017/9/19.
 */

public class DayNightDelegate {

    private static final String TAG = "DayNightDelegate";

    private static final String KEY_LOCAL_NIGHT_MODE = "rikka:local_night_mode";

    @IntDef({MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_AUTO, MODE_NIGHT_FOLLOW_SYSTEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NightMode {
        int MODE_NIGHT_NO = 1;
        int MODE_NIGHT_YES = 2;
        int MODE_NIGHT_AUTO = 0;
        int MODE_NIGHT_FOLLOW_SYSTEM = -1;
    }

    public static final int MODE_NIGHT_UNSPECIFIED = -100;

    @NightMode
    private static int sDefaultNightMode = MODE_NIGHT_FOLLOW_SYSTEM;

    @NightMode
    private int mLocalNightMode = MODE_NIGHT_UNSPECIFIED;

    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoNightModeManager;

    private Context mContext;

    public static int getDefaultNightMode() {
        return sDefaultNightMode;
    }

    public static void setDefaultNightMode(@NightMode int mode) {
        switch (mode) {
            case MODE_NIGHT_AUTO:
            case MODE_NIGHT_NO:
            case MODE_NIGHT_YES:
            case MODE_NIGHT_FOLLOW_SYSTEM:
                sDefaultNightMode = mode;
                break;
            default:
                Log.d(TAG, "setDefaultNightMode() called with an unknown mode");
                break;
        }
    }

    public DayNightDelegate(Context context) {
        this(context, MODE_NIGHT_UNSPECIFIED);
    }

    public DayNightDelegate(Context context, int localNightMode) {
        mContext = context;
        mLocalNightMode = localNightMode;
    }

    public Context getContext() {
        return mContext;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && mLocalNightMode == MODE_NIGHT_UNSPECIFIED) {
            // If we have a icicle and we haven't had a local night mode set yet, try and read
            // it from the icicle
            mLocalNightMode = savedInstanceState.getInt(KEY_LOCAL_NIGHT_MODE,
                    MODE_NIGHT_UNSPECIFIED);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mLocalNightMode != MODE_NIGHT_UNSPECIFIED) {
            // If we have a local night mode set, save it
            outState.putInt(KEY_LOCAL_NIGHT_MODE, mLocalNightMode);
        }
    }

    public void cleanup() {
        // Make sure we clean up any receivers setup for AUTO mode
        if (mAutoNightModeManager != null) {
            mAutoNightModeManager.cleanup();
        }
    }

    public boolean applyDayNight() {
        boolean applied = false;

        final int nightMode = getNightMode();
        final int modeToApply = mapNightMode(nightMode);
        if (modeToApply != MODE_NIGHT_FOLLOW_SYSTEM) {
            applied = updateForNightMode(modeToApply);
        }

        if (nightMode == MODE_NIGHT_AUTO) {
            // If we're already been started, we may need to setup auto mode again
            ensureAutoNightModeManager();
            mAutoNightModeManager.setup();
        }

        mApplyDayNightCalled = true;
        return applied;
    }

    public int getNightMode() {
        return mLocalNightMode != MODE_NIGHT_UNSPECIFIED ? mLocalNightMode : getDefaultNightMode();
    }

    public void setLocalNightMode(final @NightMode int mode) {
        switch (mode) {
            case MODE_NIGHT_AUTO:
            case MODE_NIGHT_NO:
            case MODE_NIGHT_YES:
            case MODE_NIGHT_FOLLOW_SYSTEM:
                if (mLocalNightMode != mode) {
                    mLocalNightMode = mode;
                    if (mApplyDayNightCalled) {
                        // If we've already applied day night, re-apply since we won't be
                        // called again
                        applyDayNight();
                    }
                }
                Log.i(TAG, "setLocalNightMode() " + mode);
                break;
            default:
                Log.i(TAG, "setLocalNightMode() called with an unknown mode");
                break;
        }
    }

    private int mapNightMode(final int mode) {
        switch (mode) {
            case MODE_NIGHT_AUTO:
                ensureAutoNightModeManager();
                return mAutoNightModeManager.getApplyableNightMode();
            case MODE_NIGHT_UNSPECIFIED:
                // If we don't have a mode specified, just let the system handle it
                return MODE_NIGHT_FOLLOW_SYSTEM;
            default:
                return mode;
        }
    }

    /**
     * Updates the {@link Resources} configuration {@code uiMode} with the
     * chosen {@code UI_MODE_NIGHT} value.
     */
    private boolean updateForNightMode(final int mode) {
        final Resources res = mContext.getResources();
        final Configuration conf = res.getConfiguration();
        final int currentNightMode = conf.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        final int newNightMode = (mode == MODE_NIGHT_YES)
                ? Configuration.UI_MODE_NIGHT_YES
                : Configuration.UI_MODE_NIGHT_NO;

        if (currentNightMode != newNightMode) {
            if (shouldRecreateOnNightModeChange()) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "applyNightMode() | Night mode changed, recreating Activity");
                }
                // If we've already been created, we need to recreate the Activity for the
                // mode to be applied
                final Activity activity = (Activity) mContext;
                activity.getWindow().setWindowAnimations(R.style.Animation_WindowEnterExitFade);
                activity.recreate();
            } else {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "applyNightMode() | Night mode changed, updating configuration");
                }
                final Configuration newConf = new Configuration(conf);
                newConf.uiMode = newNightMode
                        | (newConf.uiMode & ~Configuration.UI_MODE_NIGHT_MASK);
                res.updateConfiguration(newConf, null);
            }
            return true;
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "applyNightMode() | Night mode has not changed. Skipping");
            }
        }
        return false;
    }

    private void ensureAutoNightModeManager() {
        if (mAutoNightModeManager == null) {
            mAutoNightModeManager = new AutoNightModeManager(TwilightManager.getInstance(mContext));
        }
    }

    @VisibleForTesting
    final AutoNightModeManager getAutoNightModeManager() {
        ensureAutoNightModeManager();
        return mAutoNightModeManager;
    }

    private boolean shouldRecreateOnNightModeChange() {
        if (mApplyDayNightCalled) {
            // If we've already appliedDayNight() (via setTheme), we need to check if the
            // Activity has configChanges set to handle uiMode changes
            final PackageManager pm = mContext.getPackageManager();
            try {
                final ActivityInfo info = pm.getActivityInfo(
                        new ComponentName(mContext, getClass()), 0);
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

    @VisibleForTesting
    final class AutoNightModeManager {
        private TwilightManager mTwilightManager;
        private boolean mIsNight;

        private BroadcastReceiver mAutoTimeChangeReceiver;
        private IntentFilter mAutoTimeChangeReceiverFilter;

        AutoNightModeManager(@NonNull TwilightManager twilightManager) {
            mTwilightManager = twilightManager;
            mIsNight = twilightManager.isNight();
        }

        final int getApplyableNightMode() {
            return mIsNight ? MODE_NIGHT_YES : MODE_NIGHT_NO;
        }

        final void dispatchTimeChanged() {
            final boolean isNight = mTwilightManager.isNight();
            if (isNight != mIsNight) {
                mIsNight = isNight;
                applyDayNight();
            }
        }

        final void setup() {
            cleanup();

            // If we're set to AUTO, we register a receiver to be notified on time changes. The
            // system only send the tick out every minute, but that's enough fidelity for our use
            // case
            if (mAutoTimeChangeReceiver == null) {
                mAutoTimeChangeReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (false/*DEBUG*/) {
                            Log.d("AutoTimeChangeReceiver", "onReceive | Intent: " + intent);
                        }
                        dispatchTimeChanged();
                    }
                };
            }
            if (mAutoTimeChangeReceiverFilter == null) {
                mAutoTimeChangeReceiverFilter = new IntentFilter();
                mAutoTimeChangeReceiverFilter.addAction(Intent.ACTION_TIME_CHANGED);
                mAutoTimeChangeReceiverFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
                mAutoTimeChangeReceiverFilter.addAction(Intent.ACTION_TIME_TICK);
            }
            mContext.registerReceiver(mAutoTimeChangeReceiver, mAutoTimeChangeReceiverFilter);
        }

        final void cleanup() {
            if (mAutoTimeChangeReceiver != null) {
                mContext.unregisterReceiver(mAutoTimeChangeReceiver);
                mAutoTimeChangeReceiver = null;
            }
        }
    }
}
