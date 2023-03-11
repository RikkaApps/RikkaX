package rikka.material.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckedTextView;

public class FakeFontWeightAppCompatCheckedTextView extends AppCompatCheckedTextView {

    private static final FakeFontWeightImpl impl = FakeFontWeightImpl.getInstance();

    public FakeFontWeightAppCompatCheckedTextView(@NonNull Context context) {
        this(context, null);
    }

    public FakeFontWeightAppCompatCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeFontWeightAppCompatCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) {
        Typeface newTf = impl.onSetTypeface(tf);
        super.setTypeface(newTf);
        impl.onSetTypefacePost(this, tf);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        Typeface newTf = impl.onSetTypeface(tf);
        super.setTypeface(newTf, style);
        impl.onSetTypefacePost(this, tf);
    }
}
