package rikka.material.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class FakeFontWeightMaterialButton extends MaterialButton {

    private static final FakeFontWeightImpl impl = FakeFontWeightImpl.getInstance();

    public FakeFontWeightMaterialButton(@NonNull Context context) {
        this(context, null);
    }

    public FakeFontWeightMaterialButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeFontWeightMaterialButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
