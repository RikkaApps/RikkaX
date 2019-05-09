package moe.shizuku.preference;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import moe.shizuku.multiprocesspreference.IMultiProcessPreferenceChangeListener;

import static moe.shizuku.preference.PreferenceProvider.EXTRA_DATA;
import static moe.shizuku.preference.PreferenceProvider.EXTRA_EDITOR_ACTIONS;
import static moe.shizuku.preference.PreferenceProvider.EXTRA_EDITOR_KEYS;
import static moe.shizuku.preference.PreferenceProvider.EXTRA_EDITOR_VALUES;
import static moe.shizuku.preference.PreferenceProvider.EXTRA_RESULT;
import static moe.shizuku.preference.PreferenceProvider.METHOD_CONTAINS;
import static moe.shizuku.preference.PreferenceProvider.METHOD_EDITOR_APPLY;
import static moe.shizuku.preference.PreferenceProvider.METHOD_EDITOR_COMMIT;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_ALL;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_BOOLEAN;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_FLOAT;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_INT;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_LONG;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_STRING;
import static moe.shizuku.preference.PreferenceProvider.METHOD_GET_STRING_SET;
import static moe.shizuku.preference.PreferenceProvider.METHOD_REGISTER_LISTENER;
import static moe.shizuku.preference.PreferenceProvider.METHOD_UNREGISTER_LISTENER;

public class MultiProcessPreference implements SharedPreferences, Handler.Callback {

    private static final Object CONTENT = new Object();
    private static final int MSG_PREFERENCE_CHANGED = 100;

    private final Object mLock = new Object();

    private final WeakHashMap<OnSharedPreferenceChangeListener, Object> mListeners =
            new WeakHashMap<>();

    private final IMultiProcessPreferenceChangeListener mListener = new IMultiProcessPreferenceChangeListener.Stub() {

        @Override
        public void onPreferenceChanged(String key) {
            Message msg = Message.obtain();
            msg.what = MSG_PREFERENCE_CHANGED;
            msg.obj = key;
            mHandler.sendMessage(msg);
        }
    };

    private final ContentResolver mContentResolver;
    private final Handler mHandler;
    private final Uri mUri;

    public MultiProcessPreference(Context context, String authority) {
        mContentResolver = context.getContentResolver();
        mHandler = new Handler(Looper.getMainLooper(), this);
        mUri = new Uri.Builder().scheme("content").authority(authority).build();
    }

    @Override
    public Map<String, ?> getAll() {
        Bundle reply = mContentResolver.call(mUri, METHOD_GET_ALL, null, null);
        if (reply == null)
            return null;

        //noinspection unchecked
        return (Map<String, ?>) reply.getSerializable(EXTRA_RESULT);
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        Objects.requireNonNull(key);

        Bundle reply = mContentResolver.call(mUri, METHOD_GET_STRING, key, null);
        if (reply == null)
            return defValue;

        return reply.getString(EXTRA_RESULT, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        Objects.requireNonNull(key);
        Bundle reply = mContentResolver.call(mUri, METHOD_GET_STRING_SET, key, null);
        if (reply == null)
            return defValues;

        //noinspection unchecked
        return (Set<String>) reply.getSerializable(EXTRA_RESULT);
    }

    @Override
    public int getInt(String key, int defValue) {
        Objects.requireNonNull(key);

        Bundle reply = mContentResolver.call(mUri, METHOD_GET_INT, key, null);
        if (reply == null)
            return defValue;

        return reply.getInt(EXTRA_RESULT);
    }

    @Override
    public long getLong(String key, long defValue) {
        Objects.requireNonNull(key);

        Bundle reply = mContentResolver.call(mUri, METHOD_GET_LONG, key, null);
        if (reply == null)
            return defValue;

        return reply.getLong(EXTRA_RESULT);
    }

    @Override
    public float getFloat(String key, float defValue) {
        Objects.requireNonNull(key);

        Bundle reply = mContentResolver.call(mUri, METHOD_GET_FLOAT, key, null);
        if (reply == null)
            return defValue;

        return reply.getFloat(EXTRA_RESULT);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        Objects.requireNonNull(key);

        Bundle reply = mContentResolver.call(mUri, METHOD_GET_BOOLEAN, key, null);
        if (reply == null)
            return defValue;

        return reply.getBoolean(EXTRA_RESULT);
    }

    @Override
    public boolean contains(String key) {
        Objects.requireNonNull(key);
        Bundle reply = mContentResolver.call(mUri, METHOD_CONTAINS, key, null);
        return reply != null;
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        synchronized (mLock) {
            if (mListeners.isEmpty()) {
                Bundle extras = new Bundle();
                extras.putBinder(EXTRA_DATA, mListener.asBinder());
                mContentResolver.call(mUri, METHOD_REGISTER_LISTENER, null, extras);
            }

            mListeners.put(listener, CONTENT);
        }
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        synchronized (mLock) {
            mListeners.remove(listener);

            if (mListeners.isEmpty()) {
                Bundle extras = new Bundle();
                extras.putBinder(EXTRA_DATA, mListener.asBinder());
                mContentResolver.call(mUri, METHOD_UNREGISTER_LISTENER, null, extras);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MSG_PREFERENCE_CHANGED && msg.obj instanceof String) {
            String key = (String) msg.obj;
            for (OnSharedPreferenceChangeListener listener : mListeners.keySet()) {
                if (listener != null)
                    listener.onSharedPreferenceChanged(MultiProcessPreference.this, key);
            }
            return true;
        }
        return false;
    }

    public class Editor implements SharedPreferences.Editor {

        private Bundle mData;
        private ArrayList<String> mActions;
        private ArrayList<String> mKeys;
        private ArrayList<Object> mValues;

        public Editor() {
            mData = new Bundle();
            mActions = new ArrayList<>();
            mKeys = new ArrayList<>();
            mValues = new ArrayList<>();
        }

        @Override
        public Editor putString(String key, @Nullable String value) {
            mActions.add("putString");
            mKeys.add(key);
            mValues.add(value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            mActions.add("putStringSet");
            mKeys.add(key);
            mValues.add(values == null ? null : new HashSet<>(values));
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            mActions.add("putInt");
            mKeys.add(key);
            mValues.add(value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            mActions.add("putLong");
            mKeys.add(key);
            mValues.add(value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            mActions.add("putFloat");
            mKeys.add(key);
            mValues.add(value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            mActions.add("putBoolean");
            mKeys.add(key);
            mValues.add(value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            mActions.add("remove");
            mKeys.add(key);
            mValues.add(null);
            return this;
        }

        @Override
        public Editor clear() {
            mActions.add("clear");
            mKeys.add(null);
            mValues.add(null);
            return this;
        }

        private boolean finish(boolean commit) {
            mData.putStringArrayList(EXTRA_EDITOR_ACTIONS, mActions);
            mData.putStringArrayList(EXTRA_EDITOR_KEYS, mKeys);
            mData.putSerializable(EXTRA_EDITOR_VALUES, mValues);

            Bundle reply = mContentResolver.call(mUri, commit ? METHOD_EDITOR_COMMIT : METHOD_EDITOR_APPLY, null, mData);
            if (reply == null)
                return false;

            return reply.getBoolean(EXTRA_RESULT, false);
        }

        @Override
        public boolean commit() {
            return finish(true);
        }

        @Override
        public void apply() {
            finish(false);
        }
    }
}
