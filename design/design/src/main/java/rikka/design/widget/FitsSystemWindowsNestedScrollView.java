package rikka.design.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class FitsSystemWindowsNestedScrollView extends NestedScrollView implements FitsSystemWindowsChild {

    private final FitsSystemWindowsChildDelegate mFitsSystemWindowsDelegate = new FitsSystemWindowsChildDelegate(this);

    public FitsSystemWindowsNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public FitsSystemWindowsNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FitsSystemWindowsNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public FitsSystemWindowsChildDelegate getFitsSystemWindowsDelegate() {
        return mFitsSystemWindowsDelegate;
    }
}
