/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

/**
 * @hide
 */
@RestrictTo(LIBRARY_GROUP_PREFIX)
public final class AppCompatDrawableManager {
    private static final String TAG = "AppCompatDrawableManag";
    private static final boolean DEBUG = false;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;

    private static AppCompatDrawableManager INSTANCE;

    public static synchronized void preload() {
        if (INSTANCE == null) {
            INSTANCE = new AppCompatDrawableManager();
            INSTANCE.mResourceManager = ResourceManagerInternal.get();
        }
    }

    /**
     * Returns the singleton instance of this class.
     */
    public static synchronized AppCompatDrawableManager get() {
        if (INSTANCE == null) {
            preload();
        }
        return INSTANCE;
    }

    private ResourceManagerInternal mResourceManager;

    public synchronized Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        return mResourceManager.getDrawable(context, resId);
    }

    synchronized Drawable getDrawable(@NonNull Context context, @DrawableRes int resId,
                                      boolean failIfNotKnown) {
        return mResourceManager.getDrawable(context, resId, failIfNotKnown);
    }

    public synchronized void onConfigurationChanged(@NonNull Context context) {
        mResourceManager.onConfigurationChanged(context);
    }

    synchronized Drawable onDrawableLoadedFromResources(@NonNull Context context,
                                                        @NonNull VectorEnabledTintResources resources, @DrawableRes final int resId) {
        return mResourceManager.onDrawableLoadedFromResources(context, resources, resId);
    }

    boolean tintDrawableUsingColorFilter(@NonNull Context context,
                                         @DrawableRes final int resId, @NonNull Drawable drawable) {
        return mResourceManager.tintDrawableUsingColorFilter(context, resId, drawable);
    }

    synchronized ColorStateList getTintList(@NonNull Context context, @DrawableRes int resId) {
        return mResourceManager.getTintList(context, resId);
    }

    static void tintDrawable(Drawable drawable, TintInfo tint, int[] state) {
        ResourceManagerInternal.tintDrawable(drawable, tint, state);
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(
            int color, PorterDuff.Mode mode) {
        return ResourceManagerInternal.getPorterDuffColorFilter(color, mode);
    }
}
