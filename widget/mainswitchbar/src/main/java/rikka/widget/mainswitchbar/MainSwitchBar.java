/*
 * Copyright (C) 2020 The Android Open Source Project
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

package rikka.widget.mainswitchbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * MainSwitchBar is a View with a customized Switch.
 * This component is used as the main switch of the page
 * to enable or disable the preferences on the page.
 */
public class MainSwitchBar extends LinearLayout implements Checkable {

    private final List<OnMainSwitchChangeListener> mSwitchChangeListeners = new ArrayList<>();

    protected TextView mTextView;
    protected Switch mSwitch;
    private final Drawable mBackgroundOn;
    private final Drawable mBackgroundOff;
    private final Drawable mBackgroundDisabled;
    private final View mFrameView;

    public MainSwitchBar(Context context) {
        this(context, null);
    }

    public MainSwitchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainSwitchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MainSwitchBar(Context context, AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        LayoutInflater.from(context).inflate(R.layout.m3_main_switch_bar, this);

        mFrameView = findViewById(R.id.frame);
        mTextView = findViewById(R.id.switch_text);
        mSwitch = findViewById(R.id.switch_widget);
        mBackgroundOn = getContext().getDrawable(R.drawable.m3_switch_bar_bg_on);
        mBackgroundOff = getContext().getDrawable(R.drawable.m3_switch_bar_bg_off);
        mBackgroundDisabled = getContext().getDrawable(
                R.drawable.m3_switch_bar_bg_disabled);

        setChecked(mSwitch.isChecked());

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MainSwitchBar, defStyleAttr, defStyleRes);
            CharSequence text = a.getText(R.styleable.MainSwitchBar_android_text);
            a.recycle();

            setTitle(text);
        }

        setBackground(true);
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    /**
     * Update the switch status
     */
    @Override
    public void setChecked(boolean checked) {
        if (mSwitch != null) {
            mSwitch.setChecked(checked);
        }
        setBackground(checked);
        propagateChecked(checked);
    }

    /**
     * Return the status of the Switch
     */
    @Override
    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    /**
     * Set the title text
     */
    public void setTitle(CharSequence text) {
        if (mTextView != null) {
            mTextView.setText(text);
        }
    }

    /**
     * Adds a listener for switch changes
     */
    public void addOnSwitchChangeListener(OnMainSwitchChangeListener listener) {
        if (!mSwitchChangeListeners.contains(listener)) {
            mSwitchChangeListeners.add(listener);
        }
    }

    /**
     * Remove a listener for switch changes
     */
    public void removeOnSwitchChangeListener(OnMainSwitchChangeListener listener) {
        mSwitchChangeListeners.remove(listener);
    }

    /**
     * Enable or disable the text and switch.
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTextView.setEnabled(enabled);
        mSwitch.setEnabled(enabled);

        if (enabled) {
            mFrameView.setBackground(isChecked() ? mBackgroundOn : mBackgroundOff);
        } else {
            mFrameView.setBackground(mBackgroundDisabled);
        }
    }

    private void propagateChecked(boolean isChecked) {
        final int count = mSwitchChangeListeners.size();
        for (int n = 0; n < count; n++) {
            mSwitchChangeListeners.get(n).onSwitchChanged(mSwitch, isChecked);
        }
    }

    private void setBackground(boolean isChecked) {
        mFrameView.setBackground(isChecked ? mBackgroundOn : mBackgroundOff);
    }

    static class SavedState extends BaseSavedState {
        boolean mChecked;
        int mVisibility;

        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mChecked = (Boolean) in.readValue(getClass().getClassLoader());
            mVisibility = (Integer) in.readValue(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(mChecked);
            out.writeValue(mVisibility);
        }

        @NonNull
        @Override
        public String toString() {
            return "MainSwitchBar.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + mChecked
                    + " visible=" + mVisibility + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.mChecked = mSwitch.isChecked();
        ss.mVisibility = getVisibility();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());

        mSwitch.setChecked(ss.mChecked);
        setChecked(ss.mChecked);
        setBackground(ss.mChecked);
        setVisibility(ss.mVisibility);

        requestLayout();
    }
}
