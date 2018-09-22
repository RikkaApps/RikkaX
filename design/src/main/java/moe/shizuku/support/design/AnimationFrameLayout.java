package moe.shizuku.support.design;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnimationFrameLayout extends FrameLayout implements AnimationView {

    private AnimationViewDelegate mDelegate;

    public AnimationFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public AnimationFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AnimationFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
