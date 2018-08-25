package moe.shizuku.support.app;

import android.app.IntentService;
import android.app.Notification;

public abstract class ForegroundIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ForegroundIntentService(String name) {
        super(name);
    }

    public abstract int getForegroundServiceNotificationId();

    public abstract Notification onStartForeground();

    public void onStopForeground() {
        stopForeground(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(getForegroundServiceNotificationId(), onStartForeground());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onStopForeground();
    }
}
