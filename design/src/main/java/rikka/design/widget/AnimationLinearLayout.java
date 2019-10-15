package rikka.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnimationLinearLayout extends LinearLayout implements AnimationView {

    private AnimationViewDelegate mDelegate;

    public AnimationLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public AnimationLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AnimationLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mDelegate = new AnimationViewDelegate(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDelegate.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public float getPosition() {
        return mDelegate.getPosition();
    }

    @Override
    public void setPosition(float position) {
        mDelegate.setPosition(position);
    }
}
