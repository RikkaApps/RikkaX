@file:Suppress("unused")

package rikka.core.util

import android.os.Build

object BuildUtils {

    inline val atLeast21 get() = Build.VERSION.SDK_INT >= 21

    inline val atLeast22 get() = Build.VERSION.SDK_INT >= 22

    inline val atLeast23 get() = Build.VERSION.SDK_INT >= 23

    inline val atLeast24 get() = Build.VERSION.SDK_INT >= 24

    inline val atLeast25 get() = Build.VERSION.SDK_INT >= 25

    inline val atLeast26 get() = Build.VERSION.SDK_INT >= 26

    inline val atLeast27 get() = Build.VERSION.SDK_INT >= 27

    inline val atLeast28 get() = Build.VERSION.SDK_INT >= 28

    inline val atLeast29 get() = Build.VERSION.SDK_INT >= 29

    inline val atLeast30 get() = Build.VERSION.SDK_INT >= 30
}