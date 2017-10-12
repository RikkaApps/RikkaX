package ${PACKAGE_NAME};

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.util.Log;

import moe.shizuku.support.app.DayNightDelegate;

#parse("File Header.java")
public abstract class ${NAME} extends Activity {

    private static final String TAG = "DayNightActivity";

    private DayNightDelegate mDelegate;
    private int mThemeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DayNightDelegate delegate = getDelegate();
        delegate.onCreate(savedInstanceState);
        if (delegate.applyDayNight() && mThemeId != 0) {
            // If DayNight has been applied, we need to re-apply the theme for
            // the changes to take effect. On API 23+, we should bypass
            // setTheme(), which will no-op if the theme ID is identical to the
            // current theme ID.
            if (Build.VERSION.SDK_INT >= 23) {
                onApplyThemeResource(getTheme(), mThemeId, false);
            } else {
                setTheme(mThemeId);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        Log.d(TAG, "onApplyThemeResource resId: " + resid + " first: " + first);

        // change night mode here to let supper class get correct TaskDescription color
        if (first && getResources() != null) {
            getDelegate().applyDayNight();
        }

        super.onApplyThemeResource(theme, resid, first);
    }

    @Override
    public void setTheme(@StyleRes final int resid) {
        super.setTheme(resid);
        // Keep hold of the theme id so that we can re-set it later if needed
        mThemeId = resid;

        Log.d(TAG, "setTheme: " + resid);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().cleanup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().cleanup();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getDelegate().onSaveInstanceState(outState);
    }

    public DayNightDelegate getDayNightDelegate() {
        if (mDelegate == null) {
            mDelegate = new DayNightDelegate(this, DayNightDelegate.getDefaultNightMode());
        }
        return mDelegate;
    }
}
