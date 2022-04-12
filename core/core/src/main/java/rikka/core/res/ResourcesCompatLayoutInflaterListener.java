package rikka.core.res;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.core.util.ObjectsCompat;

import java.util.WeakHashMap;

import rikka.core.R;
import rikka.layoutinflater.view.LayoutInflaterFactory;

public class ResourcesCompatLayoutInflaterListener implements LayoutInflaterFactory.OnViewCreatedListener {

    private final static ResourcesCompatLayoutInflaterListener INSTANCE = new ResourcesCompatLayoutInflaterListener();

    private static final WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>>
            sColorStateCaches = new WeakHashMap<>(0);
    private static final Object sColorStateCacheLock = new Object();

    public static ResourcesCompatLayoutInflaterListener getInstance() {
        return INSTANCE;
    }

    private static final class ColorStateListCacheKey {
        final Resources mResources;
        @Nullable
        final Resources.Theme mTheme;

        ColorStateListCacheKey(@NonNull Resources resources, @Nullable Resources.Theme theme) {
            mResources = resources;
            mTheme = theme;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColorStateListCacheKey that = (ColorStateListCacheKey) o;
            return mResources.equals(that.mResources)
                    && ObjectsCompat.equals(mTheme, that.mTheme);
        }

        @Override
        public int hashCode() {
            return ObjectsCompat.hash(mResources, mTheme);
        }
    }

    private static class ColorStateListCacheEntry {
        final ColorStateList mValue;
        final Configuration mConfiguration;

        ColorStateListCacheEntry(@NonNull ColorStateList value,
                                 @NonNull Configuration configuration) {
            mValue = value;
            mConfiguration = configuration;
        }
    }

    @Nullable
    private static ColorStateList getCachedColorStateList(@NonNull ColorStateListCacheKey key,
                                                          @ColorRes int resId) {
        synchronized (sColorStateCacheLock) {
            final SparseArray<ColorStateListCacheEntry> entries = sColorStateCaches.get(key);
            if (entries != null && entries.size() > 0) {
                final ColorStateListCacheEntry entry = entries.get(resId);
                if (entry != null) {
                    if (entry.mConfiguration.equals(key.mResources.getConfiguration())) {
                        // If the current configuration matches the entry's, we can use it
                        return entry.mValue;
                    } else {
                        // Otherwise we'll remove the entry
                        entries.remove(resId);
                    }
                }
            }
        }
        return null;
    }

    private static void addColorStateListToCache(@NonNull ColorStateListCacheKey key,
                                                 @ColorRes int resId,
                                                 @NonNull ColorStateList value) {
        synchronized (sColorStateCacheLock) {
            SparseArray<ColorStateListCacheEntry> entries = sColorStateCaches.get(key);
            if (entries == null) {
                entries = new SparseArray<>();
                sColorStateCaches.put(key, entries);
            }
            entries.append(resId, new ColorStateListCacheEntry(value,
                    key.mResources.getConfiguration()));
        }
    }

    @SuppressLint("RestrictedApi")
    private ColorStateList resolveColorStateList(TypedArray a, int index, Context context) {
        if (!a.hasValue(index)) {
            return null;
        }

        int type = a.getType(index);
        if (type == TypedValue.TYPE_STRING) {
            int id = a.getResourceId(index, 0);
            Resources res = context.getResources();
            Resources.Theme theme = context.getTheme();

            ColorStateListCacheKey key = new ColorStateListCacheKey(res, theme);
            ColorStateList csl = getCachedColorStateList(key, id);
            if (csl != null) {
                return csl;
            }
            csl = ColorStateListInflaterCompat.inflate(a.getResources(),
                    id, context.getTheme());
            if (csl != null) {
                // If we inflated it, add it to the cache and return
                addColorStateListToCache(key, id, csl);
                return csl;
            }

            return csl;
        } else if (type >= TypedValue.TYPE_FIRST_COLOR_INT
                && type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return null;
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        if (!(view instanceof TextView)) {
            return;
        }

        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.ResourcesCompat, 0, 0);
        ColorStateList colorStateList = resolveColorStateList(a, R.styleable.ResourcesCompat_android_textColor, view.getContext());
        if (colorStateList != null) {
            ((TextView) view).setTextColor(colorStateList);
        }
        a.recycle();
    }
}
