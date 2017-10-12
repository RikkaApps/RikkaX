package moe.shizuku.support.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import moe.shizuku.support.R;

/**
 * Created by rikka on 2017/10/12.
 */

public class IntentUtils {

    public static boolean isValid(Context context, Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    public static void startActivity(Context context, Intent intent) {
        startActivity(context, intent, context.getString(R.string.target_app_not_found));
    }

    public static void startActivity(Context context, Intent intent, String notFoundMessage) {
        if (isValid(context, intent)) {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            try {
                context.startActivity(intent);
            } catch (Exception ignored) {
            }
        } else if (notFoundMessage != null) {
            Toast.makeText(context, notFoundMessage, Toast.LENGTH_LONG).show();
        }
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        startActivityForResult(activity, intent, requestCode, activity.getString(R.string.target_app_not_found));
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, String notFoundMessage) {
        if (isValid(activity, intent)) {
            activity.startActivityForResult(intent, requestCode);
        } else if (notFoundMessage != null) {
            Toast.makeText(activity, notFoundMessage, Toast.LENGTH_LONG).show();
        }
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        startActivityForResult(fragment, intent, requestCode, fragment.getContext().getString(R.string.target_app_not_found));
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode, String notFoundMessage) {
        if (isValid(fragment.getContext(), intent)) {
            fragment.startActivityForResult(intent, requestCode);
        } else if (notFoundMessage != null) {
            Toast.makeText(fragment.getContext(), notFoundMessage, Toast.LENGTH_LONG).show();
        }
    }
}
