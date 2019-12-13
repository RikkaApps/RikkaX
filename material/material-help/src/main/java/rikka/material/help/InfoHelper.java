package rikka.material.help;

import android.content.Context;
import android.os.Build;

public class InfoHelper {

    private static String ANDROID = Build.VERSION.RELEASE;
    private static String MODEL = Build.MODEL;
    private static String MANUFACTURER = Build.MANUFACTURER;

    private Context mContext;

    public InfoHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public StringBuilder getInfo(String versionName, int versionCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("Android Version: ").append(ANDROID).append(" (API ").append(Build.VERSION.SDK_INT).append(')').append("\n");
        sb.append("Device: ").append(MANUFACTURER).append(' ').append(MODEL).append("\n");
        sb.append("App Version: ").append(versionName).append(" (")
                .append(versionCode).append(")\n");

        String installerPackageName = mContext.getPackageManager().getInstallerPackageName(mContext.getPackageName());
        if (installerPackageName != null) {
            sb.append("Installer: ").append(installerPackageName).append("\n");
        }
        return sb;
    }
}
