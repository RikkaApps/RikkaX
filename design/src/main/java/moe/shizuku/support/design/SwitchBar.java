package moe.shizuku.support.design;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Fung Gwo on 2018/2/20.
 */

public class SwitchBar extends LinearLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private CharSequence mSwitchOnText, mSwitchOffText;

    private TextView mStatusText;
    private Switch mSwitch;

    private @Nullable OnCheckedChangeListener mOnCheckedChangeListener = null;

    private boolean isChecked = false;

    private boolean isBroadcasting = false;

    private static final String KEY_SUPER_STATES = BuildConfig.APPLICATION_ID + ".superStates";
    private static final String KEY_IS_CHECKED = BuildConfig.APPLICATION_ID + ".isChecked";

    public SwitchBar(@NonNull Context context) {
        this(context, null);
    }

    public SwitchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.switchBarStyle);
    }

    public SwitchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwitchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        ColorStateList textColor = null;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchBar, defStyleAttr, defStyleRes);
            textColor = a.getColorStateList(R.styleable.SwitchBar_android_textColor);
            mSwitchOnText = a.getString(R.styleable.SwitchBar_switchOnText);
            mSwitchOffText = a.getString(R.styleable.SwitchBar_switchOffText);
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.switchbar_widget_layout, this, true);
        mStatusText = findViewById(android.R.id.text1);
        mSwitch = findViewById(android.R.id.toggle);

        if (textColor != null)
            mStatusText.setTextColor(textColor);

        setOnClickListener(v -> toggle());

        updateViewStates();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mStatusText.setEnabled(enabled);
        mSwitch.setEnabled(enabled);
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean notify) {
        if (isChecked != checked) {
            if (notify) {
                if (isBroadcasting) {
                    return;
                }

                isBroadcasting = true;
                if (mOnCheckedChangeListener != null) {
                    if (!mOnCheckedChangeListener.onCheckedChanged(this, checked)) {
                        return;
                    }
                }
                isBroadcasting = false;
            }

            isChecked = checked;
            updateViewStates();
            refreshDrawableState();
        }
    }

    public void setSwitchOnText(@StringRes int switchOnTextRes) {
        setSwitchOnText(getContext().getString(switchOnTextRes));
    }

    public void setSwitchOnText(CharSequence switchOnText) {
        mSwitchOnText = switchOnText;
        if (isChecked())
            mStatusText.setText(mSwitchOnText);
    }

    public void setSwitchOffText(@StringRes int switchOffTextRes) {
        setSwitchOffText(getContext().getString(switchOffTextRes));
    }

    public void setSwitchOffText(CharSequence switchOffText) {
        mSwitchOffText = switchOffText;
        if (!isChecked())
            mStatusText.setText(mSwitchOffText);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    private void updateViewStates() {
        if (mStatusText != null && mSwitch != null) {
            mStatusText.setText(isChecked() ? mSwitchOnText : mSwitchOffText);
            mSwitch.setChecked(isChecked());
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATES, super.onSaveInstanceState());
        bundle.putBoolean(KEY_IS_CHECKED, isChecked());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATES));
            setChecked(bundle.getBoolean(KEY_IS_CHECKED));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public interface OnCheckedChangeListener {
        boolean onCheckedChanged(SwitchBar view, boolean isChecked);
    }

}
