package rikka.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import rikka.design.R;

public class BorderRecyclerView extends FitsSystemWindowsRecyclerView implements BorderView {

    private BorderViewDelegate mBorderViewDelegate;

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
        isShowingTopBorder = getBorderTopStyle() == BorderStyle.ALWAYS
                || (getBorderTopStyle() == BorderStyle.TOP_OR_BOTTOM && isTop)
                || (getBorderTopStyle() == BorderStyle.SCROLLED && !isTop);
        isShowingBottomBorder = getBorderBottomStyle() == BorderStyle.ALWAYS
                || (getBorderBottomStyle() == BorderStyle.TOP_OR_BOTTOM && isBottom)
                || (getBorderBottomStyle() == BorderStyle.SCROLLED && !isBottom);

        if (!Objects.equals(getBorderViewDelegate().isShowingTopBorder(), isShowingTopBorder) || !Objects.equals(getBorderViewDelegate().isShowingBottomBorder(), isShowingBottomBorder)) {
            onBorderVisibilityChanged(isShowingTopBorder,
                    getBorderViewDelegate().isShowingTopBorder(),
                    isShowingBottomBorder,
                    getBorderViewDelegate().isShowingBottomBorder());
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        updateBorderStatus();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

        getBorderViewDelegate().onDrawForeground(canvas);
    }
}
