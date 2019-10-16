package rikka.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import rikka.core.R;

public class IntentUtils {

    public static boolean isValid(Context context, Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    public static boolean startActivity(Context context, Intent intent) {
        return startActivity(context, intent, context.getString(R.string.target_app_not_found));
    }

    public static boolean startActivity(Context context, Intent intent, String notFoundMessage) {
        if (isValid(context, intent)) {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            try {
                context.startActivity(intent);
                return true;
            } catch (Exception ignored) {
            }
        } else if (notFoundMessage != null) {
            Toast.makeText(context, notFoundMessage, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public static boolean startActivityForResult(Activity activity, Intent intent, int requestCode) {
        return startActivityForResult(activity, intent, requestCode, activity.getString(R.string.target_app_not_found));
    }

    public static boolean startActivityForResult(Activity activity, Intent intent, int requestCode, String notFoundMessage) {
        if (isValid(activity, intent)) {
            try {
                activity.startActivityForResult(intent, requestCode);
                return true;
            } catch (Exception ignored) {
            }
            return true;
        } else if (notFoundMessage != null) {
            Toast.makeText(activity, notFoundMessage, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public static boolean startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        return startActivityForResult(fragment, intent, requestCode, fragment.requireContext().getString(R.string.target_app_not_found));
    }

    public static boolean startActivityForResult(Fragment fragment, Intent intent, int requestCode, String notFoundMessage) {
        if (fragment.getActivity() == null) {
            return false;
        }
        if (isValid(fragment.getActivity(), intent)) {
            try {
                fragment.startActivityForResult(intent, requestCode);
                return true;
            } catch (Exception ignored) {
            }
        } else if (notFoundMessage != null) {
            Toast.makeText(fragment.getActivity(), notFoundMessage, Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
