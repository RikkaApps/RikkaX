package moe.shizuku.preference;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import moe.shizuku.multiprocesspreference.IMultiProcessPreferenceChangeListener;

public abstract class PreferenceProvider extends ContentProvider implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_RESULT = "result";

    public static final String METHOD_GET_ALL = "getAll";
    public static final String METHOD_GET_STRING = "getString";
    public static final String METHOD_GET_STRING_SET = "getStringSet";
    public static final String METHOD_GET_INT = "getInt";
    public static final String METHOD_GET_LONG = "getLong";
    public static final String METHOD_GET_FLOAT = "getFloat";
    public static final String METHOD_GET_BOOLEAN = "getBoolean";
    public static final String METHOD_CONTAINS = "contains";
    public static final String METHOD_REGISTER_LISTENER = "registerListener";
    public static final String METHOD_UNREGISTER_LISTENER = "unregisterListener";

    public static final String METHOD_EDITOR_COMMIT = "editor_commit";
    public static final String METHOD_EDITOR_APPLY = "editor_apply";

    public static final String EXTRA_EDITOR_ACTIONS = "editor_actions";
    public static final String EXTRA_EDITOR_KEYS = "editor_keys";
    public static final String EXTRA_EDITOR_VALUES = "editor_values";

    private final RemoteCallbackList<IMultiProcessPreferenceChangeListener> mListeners = new RemoteCallbackList<>();

    private SharedPreferences mSharedPreferences;
    private Uri mUri;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);

        mSharedPreferences = onCreatePreference(context);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mUri = new Uri.Builder().scheme("content").authority(info.authority).build();
    }

    public abstract SharedPreferences onCreatePreference(Context context);

    @Override
    public boolean onCreate() {
        return true;
    }

    public Bundle getAll() {
        Bundle reply = new Bundle();
        reply.putSerializable(EXTRA_RESULT, new HashMap<>(mSharedPreferences.getAll()));
        return reply;
    }

    public Bundle getString(String key) {
        if (mSharedPreferences.contains(key)) {
            Bundle reply = new Bundle();
            reply.putString(EXTRA_RESULT, mSharedPreferences.getString(key, null));
            return reply;
        } else {
            return null;
        }
    }

    public Bundle getStringSet(String key) {
        if (mSharedPreferences.contains(key)) {
            Bundle reply = new Bundle();
            Set<String> set = mSharedPreferences.getStringSet(key, null);
            reply.putSerializable(EXTRA_RESULT, set == null ? null : new HashSet<>(set));
            return reply;
        } else {
            return null;
        }
    }

    public Bundle getInt(String key) {
        if (mSharedPreferences.contains(key)) {
            Bundle reply = new Bundle();
            reply.putInt(EXTRA_RESULT, mSharedPreferences.getInt(key, 0));
            return reply;
        } else {
            return null;
        }
    }

    public Bundle getLong(String key) {
        if (mSharedPreferences.contains(key)) {
            Bundle reply = new Bundle();
            reply.putLong(EXTRA_RESULT, mSharedPreferences.getLong(key, 0));
            return reply;
        } else {
            return null;
        }
    }

    public Bundle getFloat(String key) {
        if (mSharedPreferences.contains(key)) {
            Bundle reply = new Bundle();
            reply.putFloat(EXTRA_RESULT, mSharedPreferences.getFloat(key, 0));
            return reply;
        } else {
            return null;
        }
    }

    public Bundle getBoolean(String key) {
        if (mSharedPreferences.contains(key)) {
            Bundle reply = new Bundle();
            reply.putBoolean(EXTRA_RESULT, mSharedPreferences.getBoolean(key, false));
            return reply;
        } else {
            return null;
        }
    }

    public Bundle contains(String key) {
        if (mSharedPreferences.contains(key)) {
            return new Bundle();
        } else {
            return null;
        }
    }

    private Bundle registerOnSharedPreferenceChangeListener(IMultiProcessPreferenceChangeListener listener) {
        if (listener == null)
            return null;

        synchronized (this) {
            mListeners.register(listener);
        }
        return null;
    }

    private Bundle unregisterOnSharedPreferenceChangeListener(IMultiProcessPreferenceChangeListener listener) {
        if (listener == null)
            return null;

        synchronized (this) {
            mListeners.unregister(listener);
        }
        return null;
    }

    private void dispatchPreferenceChanged(IMultiProcessPreferenceChangeListener listener, String key) {
        if (listener == null)
            return;

        try {
            listener.onPreferenceChanged(key);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        int i = mListeners.beginBroadcast();
        while (i > 0) {
            i--;
            dispatchPreferenceChanged(mListeners.getBroadcastItem(i), key);
        }
        mListeners.finishBroadcast();
    }

    @SuppressLint("ApplySharedPref")
    public Bundle edit(boolean commit, Bundle extras) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        List<String> actions = extras.getStringArrayList(EXTRA_EDITOR_ACTIONS);
        List<String> keys = extras.getStringArrayList(EXTRA_EDITOR_KEYS);
        //noinspection unchecked
        List<Object> values = (List<Object>) extras.getSerializable(EXTRA_EDITOR_VALUES);

        Objects.requireNonNull(actions);
        Objects.requireNonNull(keys);
        Objects.requireNonNull(values);

        for (int i = 0; i < actions.size(); i++) {
            String action = actions.get(i);
            String key = keys.get(i);
            Object value = values.get(i);

            switch (action) {
                case "putString":
                    editor.putString(key, (String) value);
                    break;
                case "putStringSet":
                    //noinspection unchecked
                    editor.putStringSet(key, (Set<String>) value);
                    break;
                case "putInt":
                    editor.putInt(key, (int) value);
                    break;
                case "putLong":
                    editor.putLong(key, (long) value);
                    break;
                case "putFloat":
                    editor.putFloat(key, (float) value);
                    break;
                case "putBoolean":
                    editor.putBoolean(key, (boolean) value);
                    break;
                case "remove":
                    editor.remove(key);
                    break;
                case "clear":
                    editor.clear();
                    break;
            }
        }

        if (commit) {
            Bundle reply = new Bundle();
            reply.putBoolean(EXTRA_RESULT, editor.commit());
            return reply;
        } else {
            editor.apply();
            return null;
        }
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        switch (method) {
            case METHOD_GET_ALL:
                return getAll();
            case METHOD_GET_STRING:
                Objects.requireNonNull(arg);
                return getString(arg);
            case METHOD_GET_STRING_SET:
                Objects.requireNonNull(arg);
                return getStringSet(arg);
            case METHOD_GET_INT:
                Objects.requireNonNull(arg);
                return getInt(arg);
            case METHOD_GET_LONG:
                Objects.requireNonNull(arg);
                return getLong(arg);
            case METHOD_GET_FLOAT:
                Objects.requireNonNull(arg);
                return getFloat(arg);
            case METHOD_GET_BOOLEAN:
                Objects.requireNonNull(arg);
                return getBoolean(arg);

            case METHOD_CONTAINS:
                Objects.requireNonNull(arg);
                return contains(arg);

            case METHOD_REGISTER_LISTENER:
                Objects.requireNonNull(extras);
                return registerOnSharedPreferenceChangeListener(IMultiProcessPreferenceChangeListener.Stub.asInterface(extras.getBinder(EXTRA_DATA)));
            case METHOD_UNREGISTER_LISTENER:
                Objects.requireNonNull(extras);
                return unregisterOnSharedPreferenceChangeListener(IMultiProcessPreferenceChangeListener.Stub.asInterface(extras.getBinder(EXTRA_DATA)));

            case METHOD_EDITOR_COMMIT:
                Objects.requireNonNull(extras);
                return edit(true, extras);
            case METHOD_EDITOR_APPLY:
                Objects.requireNonNull(extras);
                return edit(false, extras);
        }

        throw new IllegalArgumentException("unsupported method " + method);
    }

    @Nullable
    @Override
    public final Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public final String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public final Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public final int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public final int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
