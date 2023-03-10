package rikka.buildcompat;

import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

@SuppressWarnings("unused")
public class BuildCompat {

    /**
     * Checks if the device is running on a specific API version or newer.
     * <p>
     *
     * @param api API version
     * @return {@code true} if the specific version of APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(parameter = 0)
    public static boolean isAtLeast(int api) {
        return Build.VERSION.SDK_INT >= api;
    }

    /**
     * Checks if the device is running on a release version of Android 5.0 (L, API 21) or newer.
     * <p>
     *
     * @return {@code true} if Android 5.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isAtLeast5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Checks if the device is running on a release version of Android 5.1 (L MR1, API 22) or newer.
     * <p>
     *
     * @return {@code true} if Android 5.1 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean isAtLeast5_1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    /**
     * Checks if the device is running on a release version of Android 6.0 (M, API 23) or newer.
     * <p>
     *
     * @return {@code true} if Android 6.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    public static boolean isAtLeast6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Checks if the device is running on a release version of Android 7.0 (N, API 24) or newer.
     * <p>
     *
     * @return {@code true} if Android 7.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    public static boolean isAtLeast7() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * Checks if the device is running on a release version of Android 7.1 (N MR1, API 25) or newer.
     * <p>
     *
     * @return {@code true} if Android 7.1 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
    public static boolean isAtLeast7_1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    /**
     * Checks if the device is running on a release version of Android 8.0 (O, API 26) or newer.
     * <p>
     *
     * @return {@code true} if Android 8.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    public static boolean isAtLeast8() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * Checks if the device is running on a release version of Android 8.1 (O MR1, API 27) or newer.
     * <p>
     *
     * @return {@code true} if Android 8.1 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    public static boolean isAtLeast8_1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    /**
     * Checks if the device is running on a release version of Android 9.0 (P, API 28) or newer.
     * <p>
     *
     * @return {@code true} if Android 9.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    public static boolean isAtLeast9() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    /**
     * Checks if the device is running on a release version of Android 10 (Q, API 29) or newer.
     * <p>
     *
     * @return {@code true} if Android 10 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    public static boolean isAtLeast10() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * Checks if the device is running on a release version of Android 11 (R, API 30) or newer.
     * <p>
     *
     * @return {@code true} if Android 11 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    public static boolean isAtLeast11() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    /**
     * Checks if the device is running on a release version of Android 12 (S, API 31) or newer.
     * <p>
     *
     * @return {@code true} if Android 12 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    public static boolean isAtLeast12() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    /**
     * Checks if the device is running on a release version of Android 12L (S_V2, API 32) or newer.
     * <p>
     *
     * @return {@code true} if Android 12L APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    public static boolean isAtLeast12L() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2;
    }

    /**
     * Checks if the device is running on a release version of Android 13 (Tiramisu, API 33) or newer.
     * <p>
     *
     * @return {@code true} if Android 13 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    public static boolean isAtLeast13() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    /**
     * Checks if the device is running on a release version of Android 14 (UpsideDownCake, API 34) or newer.
     * <p>
     *
     * @return {@code true} if Android 14 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = 34)
    public static boolean isAtLeast14() {
        return (isAtLeast13() && Build.VERSION.PREVIEW_SDK_INT > 0) || Build.VERSION.SDK_INT >= 34;
    }

    /**
     * Checks if the device is running on a release version of Android 5.0 (L, API 21) or newer.
     * <p>
     *
     * @return {@code true} if Android 5.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isAtLeastL() {
        return isAtLeast5();
    }

    /**
     * Checks if the device is running on a release version of Android 5.1 (L MR1, API 22) or newer.
     * <p>
     *
     * @return {@code true} if Android 5.1 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean isAtLeastL_MR1() {
        return isAtLeast5_1();
    }

    /**
     * Checks if the device is running on a release version of Android 6.0 (M, API 23) or newer.
     * <p>
     *
     * @return {@code true} if Android 6.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    public static boolean isAtLeastM() {
        return isAtLeast6();
    }

    /**
     * Checks if the device is running on a release version of Android 7.0 (N, API 24) or newer.
     * <p>
     *
     * @return {@code true} if Android 7.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    public static boolean isAtLeastN() {
        return isAtLeast7();
    }

    /**
     * Checks if the device is running on a release version of Android 7.1 (N MR1, API 25) or newer.
     * <p>
     *
     * @return {@code true} if Android 7.1 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
    public static boolean isAtLeastN_MR1() {
        return isAtLeast7_1();
    }

    /**
     * Checks if the device is running on a release version of Android 8.0 (O, API 26) or newer.
     * <p>
     *
     * @return {@code true} if Android 8.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    public static boolean isAtLeastO() {
        return isAtLeast8();
    }

    /**
     * Checks if the device is running on a release version of Android 8.1 (O MR1, API 27) or newer.
     * <p>
     *
     * @return {@code true} if Android 8.1 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    public static boolean isAtLeastO_MR1() {
        return isAtLeast8_1();
    }

    /**
     * Checks if the device is running on a release version of Android 9.0 (P, API 28) or newer.
     * <p>
     *
     * @return {@code true} if Android 9.0 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    public static boolean isAtLeastP() {
        return isAtLeast9();
    }

    /**
     * Checks if the device is running on a release version of Android 10 (Q, API 29) or newer.
     * <p>
     *
     * @return {@code true} if Android 10 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    public static boolean isAtLeastQ() {
        return isAtLeast10();
    }

    /**
     * Checks if the device is running on a release version of Android 11 (R, API 30) or newer.
     * <p>
     *
     * @return {@code true} if Android 11 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    public static boolean isAtLeastR() {
        return isAtLeast11();
    }

    /**
     * Checks if the device is running on a release version of Android 12 (S, API 31) or newer.
     * <p>
     *
     * @return {@code true} if Android 12 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    public static boolean isAtLeastS() {
        return isAtLeast12();
    }

    /**
     * Checks if the device is running on a release version of Android 12L (S_V2, API 32) or newer.
     * <p>
     *
     * @return {@code true} if Android 12L APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    public static boolean isAtLeastS_V2() {
        return isAtLeast12L();
    }

    /**
     * Checks if the device is running on a release version of Android 13 (Tiramisu, API 33) or newer.
     * <p>
     *
     * @return {@code true} if Android 13 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    public static boolean isAtLeastT() {
        return isAtLeast13();
    }

    /**
     * Checks if the device is running on a release version of Android 14 (UpsideDownCake, API 34) or newer.
     * <p>
     *
     * @return {@code true} if Android 14 APIs are available for use, {@code false} otherwise
     */
    @ChecksSdkIntAtLeast(api = 34)
    public static boolean isAtLeastU() {
        return isAtLeast14();
    }

}
