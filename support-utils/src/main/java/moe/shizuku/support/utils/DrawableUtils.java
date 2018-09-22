/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package moe.shizuku.support.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import moe.shizuku.support.compat.Optional;

/**
 * Copied from Android KTX
 * https://github.com/android/android-ktx/blob/master/src/main/java/androidx/core/graphics/drawable/Drawable.kt
 */
public final class DrawableUtils {

    public static Bitmap toBitmap(Drawable drawable) {
        return toBitmap(drawable,
                drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), null);
    }

    public static Bitmap toBitmap(@NonNull Drawable drawable,
                                  int width, int height, @Nullable Bitmap.Config config) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (config == null || bitmapDrawable.getBitmap().getConfig() == config) {
                // Fast-path to return original. Bitmap.createScaledBitmap will do this check, but it
                // involves allocation and two jumps into native code so we perform the check ourselves.
                if (width == drawable.getIntrinsicWidth()
                        && height == drawable.getIntrinsicHeight()) {
                    return bitmapDrawable.getBitmap();
                }
                return Bitmap.createScaledBitmap(
                        ((BitmapDrawable) drawable).getBitmap(), width, height, true);
            }
        }

        Rect bounds = drawable.getBounds();
        int oldLeft = bounds.left;
        int oldTop = bounds.top;
        int oldRight = bounds.right;
        int oldBottom = bounds.bottom;

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Optional.of(config).orElse(Bitmap.Config.ARGB_8888));
        drawable.setBounds(0, 0, width, height);
        drawable.draw(new Canvas(bitmap));

        drawable.setBounds(oldLeft, oldTop, oldRight, oldBottom);
        return bitmap;
    }

}
