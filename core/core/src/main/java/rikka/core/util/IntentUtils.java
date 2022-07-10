package rikka.core.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class IntentUtils {

    public static boolean startActivity(Context context, Intent intent, String notFoundMessage) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException ignored) {
            if (notFoundMessage != null) {
                Toast.makeText(context, notFoundMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static boolean startActivityForResult(Activity activity, Intent intent, int requestCode, String notFoundMessage) {
        try {
            activity.startActivityForResult(intent, requestCode);
            return true;
        } catch (ActivityNotFoundException ignored) {
            if (notFoundMessage != null) {
                Toast.makeText(activity, notFoundMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

    public static boolean startActivityForResult(Fragment fragment, Intent intent, int requestCode, String notFoundMessage) {
        try {
            fragment.startActivityForResult(intent, requestCode);
            return true;
        } catch (ActivityNotFoundException ignored) {
            if (notFoundMessage != null) {
                Toast.makeText(fragment.requireContext(), notFoundMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Throwable ignored) {
        }
        return false;
    }
}
