package moe.shizuku.support.app;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;

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

    @TargetApi(Build.VERSION_CODES.O)
    public abstract void onCreateNotificationChannel(@NonNull NotificationManager notificationManager);

    public void onStopForeground() {
        stopForeground(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) {
                onCreateNotificationChannel(nm);
            }
        }

        startForeground(getForegroundServiceNotificationId(), onStartForeground());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onStopForeground();
    }
}
