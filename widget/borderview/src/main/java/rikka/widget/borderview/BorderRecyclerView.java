package rikka.widget.borderview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BorderRecyclerView extends RecyclerView implements BorderView {

    private final BorderViewDelegate mBorderViewDelegate;

    public BorderRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public BorderRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.borderViewStyle);
    }

    public BorderRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBorderViewDelegate = new BorderViewDelegate(this, context, attrs, defStyle);
    }

    @Override
    public BorderViewDelegate getBorderViewDelegate() {
        return mBorderViewDelegate;
    }

    @Override
    public void updateBorderStatus() {
        int offset = computeVerticalScrollOffset();
        int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        final boolean isShowingTopBorder, isShowingBottomBorder;
        final boolean isTop, isBottom;
        if (range != 0) {
            isTop = offset == 0;
            isBottom = offset == range;
        } else {
            return;
        }
        isShowingTopBorder = getBorderTopVisibility() == BorderVisibility.ALWAYS
                || (getBorderTopVisibility() == BorderVisibility.TOP_OR_BOTTOM && isTop)
                || (getBorderTopVisibility() == BorderVisibility.SCROLLED && !isTop);
        isShowingBottomBorder = getBorderBottomVisibility() == BorderVisibility.ALWAYS
                || (getBorderBottomVisibility() == BorderVisibility.TOP_OR_BOTTOM && isBottom)
                || (getBorderBottomVisibility() == BorderVisibility.SCROLLED && !isBottom);

        if (!ObjectsCompat.equals(getBorderViewDelegate().isShowingTopBorder(), isShowingTopBorder) || !ObjectsCompat.equals(getBorderViewDelegate().isShowingBottomBorder(), isShowingBottomBorder)) {
            onBorderVisibilityChanged(isShowingTopBorder,
                    getBorderViewDelegate().isShowingTopBorder(),
                    isShowingBottomBorder,
                    getBorderViewDelegate().isShowingBottomBorder());
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        updateBorderStatus();

        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

        getBorderViewDelegate().onDrawForeground(canvas);
    }
}
