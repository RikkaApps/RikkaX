package moe.shizuku.support.design.widget;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public interface BorderView {

    enum BorderStyle {
        NEVER, TOP_OR_BOTTOM, SCROLLED, ALWAYS
    }

    interface OnBorderVisibilityChangedListener {
        void onBorderVisibilityChanged(boolean top, boolean oldTop, boolean bottom, boolean oldBottom);
    }

    BorderViewDelegate getBorderViewDelegate();

    void updateBorderStatus();

    default void onBorderVisibilityChanged(boolean top, boolean oldTop, boolean bottom, boolean oldBottom) {
        getBorderViewDelegate().onBorderVisibilityChanged(top, oldTop, bottom, oldBottom);
    }

    default OnBorderVisibilityChangedListener getBorderVisibilityChangedListener() {
        return getBorderViewDelegate().getBorderVisibilityChangedListener();
    }

    default void setBorderVisibilityChangedListener(OnBorderVisibilityChangedListener listener) {
        getBorderViewDelegate().setBorderVisibilityChangedListener(listener);
    }

    default BorderStyle getBorderTopStyle() {
        return getBorderViewDelegate().getBorderTopStyle();
    }

    default void setBorderTopStyle(BorderStyle style) {
        getBorderViewDelegate().setBorderTopStyle(style);
    }

    default BorderStyle getBorderBottomStyle() {
        return getBorderViewDelegate().getBorderBottomStyle();
    }

    default void setBorderBottomStyle(BorderStyle style) {
        getBorderViewDelegate().setBorderBottomStyle(style);
    }

    default Drawable getBorderTopDrawable() {
        return getBorderViewDelegate().getBorderTopDrawable();
    }

    default void setBorderTopDrawable(Drawable drawable) {
        getBorderViewDelegate().setBorderTopDrawable(drawable);
    }

    default Drawable getBorderBottomDrawable() {
        return getBorderViewDelegate().getBorderBottomDrawable();
    }

    default void setBorderBottomDrawable(Drawable drawable) {
        getBorderViewDelegate().setBorderBottomDrawable(drawable);
    }

    default boolean isShowingTopBorder() {
        return getBorderViewDelegate().isShowingTopBorder();
    }

    default boolean isShowingBottomBorder() {
        return getBorderViewDelegate().isShowingBottomBorder();
    }

    default void onDrawBoardTop(Drawable drawable, Canvas canvas) {
        drawable.draw(canvas);
    }

    default void onDrawBoardBottom(Drawable drawable, Canvas canvas) {
        drawable.draw(canvas);
    }
}
