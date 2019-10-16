package rikka.design.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FitsSystemWindowsRecyclerView extends RecyclerView {

    private FitsSystemWindowsChildDelegate mFitsSystemWindowsDelegate = new FitsSystemWindowsChildDelegate(this);

    public FitsSystemWindowsRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public FitsSystemWindowsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitsSystemWindowsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClipToPadding(false);
    }

    public FitsSystemWindowsChildDelegate getFitsSystemWindowsDelegate() {
        return mFitsSystemWindowsDelegate;
    }
}
