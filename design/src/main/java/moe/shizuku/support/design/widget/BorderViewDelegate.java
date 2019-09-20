package moe.shizuku.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Objects;

import moe.shizuku.support.design.R;

public class BorderViewDelegate {

    private BorderView.OnBorderVisibilityChangedListener mBorderVisibilityChangedListener;

    private View mView;
    private BorderView mBorderView;

    private boolean isShowingTopBorder, isShowingBottomBorder;

    private BorderView.BorderStyle borderTopStyle, borderBottomStyle;
    private Drawable borderTopDrawable, borderBottomDrawable;

    public BorderViewDelegate(View view, Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mView = view;
        mBorderView = (BorderView) view;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderView, defStyleAttr, R.style.Widget_Design_BorderView);
        borderTopDrawable = a.getDrawable(R.styleable.BorderView_borderTopDrawable);
        borderBottomDrawable = a.getDrawable(R.styleable.BorderView_borderBottomDrawable);
        switch (a.getInt(R.styleable.BorderView_borderTopStyle, 0)) {
            case 0:
                borderTopStyle = BorderView.BorderStyle.NEVER;
                break;
            case 1:
                borderTopStyle = BorderView.BorderStyle.TOP_OR_BOTTOM;
                break;
            case 3:
                borderTopStyle = BorderView.BorderStyle.ALWAYS;
                break;
            case 2:
            default:
                borderTopStyle = BorderView.BorderStyle.SCROLLED;
                break;
        }
        switch (a.getInt(R.styleable.BorderView_borderBottomStyle, 0)) {
            case 0:
                borderBottomStyle = BorderView.BorderStyle.NEVER;
                break;
            case 1:
                borderBottomStyle = BorderView.BorderStyle.TOP_OR_BOTTOM;
                break;
            case 3:
                borderBottomStyle = BorderView.BorderStyle.ALWAYS;
                break;
            case 2:
            default:
                borderBottomStyle = BorderView.BorderStyle.SCROLLED;
                break;
        }
        a.recycle();

        isShowingTopBorder = borderTopStyle == BorderView.BorderStyle.TOP_OR_BOTTOM || borderTopStyle == BorderView.BorderStyle.ALWAYS;
        isShowingBottomBorder = borderBottomStyle == BorderView.BorderStyle.ALWAYS;
    }

    public BorderView.OnBorderVisibilityChangedListener getBorderVisibilityChangedListener() {
        return mBorderVisibilityChangedListener;
    }

    public void setBorderVisibilityChangedListener(BorderView.OnBorderVisibilityChangedListener borderVisibilityChangedListener) {
        mBorderVisibilityChangedListener = borderVisibilityChangedListener;
    }

    public BorderView.BorderStyle getBorderTopStyle() {
        return borderTopStyle;
    }

    public void setBorderTopStyle(BorderView.BorderStyle style) {
        if (style != this.borderTopStyle) {
            this.borderTopStyle = style;
            mBorderView.updateBorderStatus();
        }
    }

    public BorderView.BorderStyle getBorderBottomStyle() {
        return borderBottomStyle;
    }

    public void setBorderBottomStyle(BorderView.BorderStyle stle) {
        if (stle != this.borderBottomStyle) {
            this.borderBottomStyle = stle;
            mBorderView.updateBorderStatus();
        }
    }

    public Drawable getBorderTopDrawable() {
        return borderTopDrawable;
    }

    public void setBorderTopDrawable(Drawable borderTopDrawable) {
        if (borderTopDrawable != this.borderTopDrawable) {
            this.borderTopDrawable = borderTopDrawable;
            mView.postInvalidate();
        }
    }

    public Drawable getBorderBottomDrawable() {
        return borderBottomDrawable;
    }

    public void setBorderBottomDrawable(Drawable borderBottomDrawable) {
        if (borderBottomDrawable != this.borderBottomDrawable) {
            this.borderBottomDrawable = borderBottomDrawable;
            mView.postInvalidate();
        }
    }

    public boolean isShowingTopBorder() {
        return isShowingTopBorder;
    }

    public boolean isShowingBottomBorder() {
        return isShowingBottomBorder;
    }

    public void onBorderVisibilityChanged(boolean top, boolean oldTop, boolean bottom, boolean oldBottom) {
        if (getBorderVisibilityChangedListener() != null) {
            getBorderVisibilityChangedListener().onBorderVisibilityChanged(top, oldTop, bottom, oldBottom);
        }

        this.isShowingTopBorder = top;
        this.isShowingBottomBorder = bottom;

        mView.postInvalidate();
    }

    public void onDrawForeground(Canvas canvas) {
        if (borderTopDrawable == null && borderBottomDrawable == null) {
            return;
        }

        int c = canvas.save();

        if (borderTopDrawable != null) {
            int dy = mView.getScrollY() + mView.getPaddingTop();
            canvas.translate(0, dy);
            if (isShowingTopBorder()) {
                borderTopDrawable.setBounds(0, 0, canvas.getWidth(), borderTopDrawable.getIntrinsicHeight());
                mBorderView.onDrawBoardTop(borderTopDrawable, canvas);
            }
            canvas.translate(0, -dy);
        }

        if (borderBottomDrawable != null) {
            int dy = mView.getScrollY() + canvas.getHeight() - borderBottomDrawable.getIntrinsicHeight() - mView.getPaddingBottom();
            canvas.translate(0, dy);

            if (isShowingBottomBorder()) {
                borderBottomDrawable.setBounds(0, 0, canvas.getWidth(), borderBottomDrawable.getIntrinsicHeight());
                mBorderView.onDrawBoardBottom(borderBottomDrawable, canvas);
            }
        }

        canvas.restoreToCount(c);
    }
}
