package rikka.widget.borderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BorderViewDelegate {

    private BorderView.OnBorderVisibilityChangedListener mBorderVisibilityChangedListener;

    private final View mView;
    private final BorderView mBorderView;

    private Boolean isShowingTopBorder, isShowingBottomBorder;

    private BorderView.BorderVisibility borderTopVisibility, borderBottomVisibility;
    private BorderView.BorderStyle borderTopStyle, borderBottomStyle;
    private Drawable borderTopDrawable, borderBottomDrawable;

    public BorderViewDelegate(View view, Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mView = view;
        mBorderView = (BorderView) view;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderView, defStyleAttr, R.style.Widget_BorderView);
        borderTopDrawable = a.getDrawable(R.styleable.BorderView_borderTopDrawable);
        borderBottomDrawable = a.getDrawable(R.styleable.BorderView_borderBottomDrawable);
        switch (a.getInt(R.styleable.BorderView_borderTopVisibility, 0)) {
            case 0:
                borderTopVisibility = BorderView.BorderVisibility.NEVER;
                break;
            case 1:
                borderTopVisibility = BorderView.BorderVisibility.TOP_OR_BOTTOM;
                break;
            case 3:
                borderTopVisibility = BorderView.BorderVisibility.ALWAYS;
                break;
            case 2:
            default:
                borderTopVisibility = BorderView.BorderVisibility.SCROLLED;
                break;
        }
        switch (a.getInt(R.styleable.BorderView_borderBottomVisibility, 0)) {
            case 0:
                borderBottomVisibility = BorderView.BorderVisibility.NEVER;
                break;
            case 1:
                borderBottomVisibility = BorderView.BorderVisibility.TOP_OR_BOTTOM;
                break;
            case 3:
                borderBottomVisibility = BorderView.BorderVisibility.ALWAYS;
                break;
            case 2:
            default:
                borderBottomVisibility = BorderView.BorderVisibility.SCROLLED;
                break;
        }
        switch (a.getInt(R.styleable.BorderView_borderTopStyle, 0)) {
            case 0:
                borderTopStyle = BorderView.BorderStyle.INSIDE;
                break;
            case 1:
            default:
                borderTopStyle = BorderView.BorderStyle.OUTSIDE;
                break;
        }
        switch (a.getInt(R.styleable.BorderView_borderBottomStyle, 0)) {
            case 0:
                borderBottomStyle = BorderView.BorderStyle.INSIDE;
                break;
            case 1:
            default:
                borderBottomStyle = BorderView.BorderStyle.OUTSIDE;
                break;
        }
        a.recycle();

        final View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (isShowingTopBorder == null || isShowingBottomBorder == null) {
                    isShowingTopBorder = isShowingTopBorder();
                    isShowingBottomBorder = isShowingBottomBorder();

                    if (getBorderVisibilityChangedListener() != null) {
                        getBorderVisibilityChangedListener().onBorderVisibilityChanged(
                                isShowingTopBorder,
                                borderTopVisibility == BorderView.BorderVisibility.TOP_OR_BOTTOM || borderTopVisibility == BorderView.BorderVisibility.ALWAYS,
                                isShowingBottomBorder,
                                borderBottomVisibility == BorderView.BorderVisibility.ALWAYS);
                    }
                }
                v.removeOnLayoutChangeListener(this);
            }
        };

        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                v.addOnLayoutChangeListener(onLayoutChangeListener);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                v.removeOnLayoutChangeListener(onLayoutChangeListener);
            }
        });
    }

    public BorderView.OnBorderVisibilityChangedListener getBorderVisibilityChangedListener() {
        return mBorderVisibilityChangedListener;
    }

    public void setBorderVisibilityChangedListener(BorderView.OnBorderVisibilityChangedListener borderVisibilityChangedListener) {
        mBorderVisibilityChangedListener = borderVisibilityChangedListener;
    }

    public BorderView.BorderVisibility getBorderTopVisibility() {
        return borderTopVisibility;
    }

    public void setBorderTopVisibility(BorderView.BorderVisibility style) {
        if (style != this.borderTopVisibility) {
            this.borderTopVisibility = style;
            mBorderView.updateBorderStatus();
        }
    }

    public BorderView.BorderVisibility getBorderBottomVisibility() {
        return borderBottomVisibility;
    }

    public void setBorderBottomVisibility(BorderView.BorderVisibility stle) {
        if (stle != this.borderBottomVisibility) {
            this.borderBottomVisibility = stle;
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

    public BorderView.BorderStyle getBorderTopStyle() {
        return borderTopStyle;
    }

    public void setBorderTopStyle(BorderView.BorderStyle borderTopStyle) {
        if (this.borderTopStyle != borderTopStyle) {
            this.borderTopStyle = borderTopStyle;
            mView.postInvalidate();
        }
    }

    public BorderView.BorderStyle getBorderBottomStyle() {
        return borderBottomStyle;
    }

    public void setBorderBottomStyle(BorderView.BorderStyle borderBottomStyle) {
        if (this.borderBottomStyle != borderBottomStyle) {
            this.borderBottomStyle = borderBottomStyle;
            mView.postInvalidate();
        }
    }

    public boolean isShowingTopBorder() {
        if (isShowingTopBorder != null) {
            return isShowingTopBorder;
        } else {
            return borderTopVisibility == BorderView.BorderVisibility.TOP_OR_BOTTOM || borderTopVisibility == BorderView.BorderVisibility.ALWAYS;
        }
    }

    public boolean isShowingBottomBorder() {
        if (isShowingBottomBorder != null) {
            return isShowingBottomBorder;
        } else {
            return borderBottomVisibility == BorderView.BorderVisibility.ALWAYS;
        }
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
            int dy = mView.getScrollY();
            if (borderTopStyle == BorderView.BorderStyle.INSIDE) {
                dy += mView.getPaddingTop();
            }
            canvas.translate(0, dy);
            if (isShowingTopBorder()) {
                borderTopDrawable.setBounds(0, 0, canvas.getWidth(), borderTopDrawable.getIntrinsicHeight());
                mBorderView.onDrawBoardTop(borderTopDrawable, canvas);
            }
            canvas.translate(0, -dy);
        }

        if (borderBottomDrawable != null) {
            int dy = mView.getScrollY() + canvas.getHeight() - borderBottomDrawable.getIntrinsicHeight();
            if (borderTopStyle == BorderView.BorderStyle.INSIDE) {
                dy -= mView.getPaddingBottom();
            }
            canvas.translate(0, dy);

            if (isShowingBottomBorder()) {
                borderBottomDrawable.setBounds(0, 0, canvas.getWidth(), borderBottomDrawable.getIntrinsicHeight());
                mBorderView.onDrawBoardBottom(borderBottomDrawable, canvas);
            }
        }

        canvas.restoreToCount(c);
    }
}
