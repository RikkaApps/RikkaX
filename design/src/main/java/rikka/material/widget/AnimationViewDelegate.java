package rikka.material.widget;

import android.view.View;

import rikka.design.R;

public class AnimationViewDelegate {

    private final View mView;
    private final float mElevation;

    private float mPosition = 0f;
    private int mSpan;

    public AnimationViewDelegate(View view) {
        mView = view;
        mElevation = view.getResources().getDimension(R.dimen.dir_elevation);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mSpan = h;
        setPosition(mPosition);
    }

    public float getPosition() {
        return mPosition;
    }

    public void setPosition(float position) {
        mPosition = position;
        mView.setY((mSpan > 0) ? (mPosition * mSpan) : 0);
        mView.setTranslationZ(mPosition * mElevation);
    }
}
