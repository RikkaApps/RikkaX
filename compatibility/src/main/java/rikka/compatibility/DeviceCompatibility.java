package rikka.compatibility;

import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

import rikka.lazy.Lazy;
import rikka.lazy.UnsafeLazy;

public class DeviceCompatibility {

    // android.os.SystemProperties is hidden but safe to use

    private static final Lazy<Boolean> IS_SAMSUNG = new UnsafeLazy<>(() ->
            Build.MANUFACTURER.equalsIgnoreCase("Samsung"));

    private static final Lazy<Boolean> IS_MEIZU = new UnsafeLazy<>(() ->
            Build.FINGERPRINT.contains("Flyme")
                    || Pattern.compile("Flyme", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find());

    private static final Lazy<Boolean> IS_EMUI = new UnsafeLazy<>(() ->
            !TextUtils.isEmpty(SystemProperties.get("ro.build.version.emui")));

    private static final Lazy<Boolean> IS_MIUI = new UnsafeLazy<>(() ->
            !TextUtils.isEmpty(SystemProperties.get("ro.miui.ui.version.name")));

    private static final Lazy<String> MIUI_REGION = new UnsafeLazy<>(() ->
            SystemProperties.get("ro.miui.region"));

    /**
     * Check if this is a Samsung device.
     *
     * @return If the device is a Samsung device
     */
    public static boolean isSamsung() {
        return IS_SAMSUNG.get();
    }

    /**
     * Check if the device is running Flyme. Meizu devices are running Flyme.
     *
     * @return If the device is running Flyme
     */
    public static boolean isFlyme() {
        return IS_MEIZU.get();
    }

    /**
     * Check if the device is running EMUI. Huawei devices are running EMUI.
     *
     * @return If the device is running EMUI
     */
    public static boolean isEmui() {
        return IS_EMUI.get();
    }

    /**
     * Check if the device is running MIUI. Xiaomi devices are running MIUI.
     *
     * @return If the device is running MIUI
     */
    public static boolean isMiui() {
        return IS_MIUI.get();
    }

    /**
     * Get the region of MIUI. The value could be "CN", "EU", etc.
     *
     * @return The region of MIUI or null if the device does not run MIUI
     */
    @Nullable
    public static String getRegionForMiui() {
        return MIUI_REGION.get();
    }
}
