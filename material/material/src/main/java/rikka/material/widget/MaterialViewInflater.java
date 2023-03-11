package rikka.material.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;

public class MaterialViewInflater extends com.google.android.material.theme.MaterialComponentsViewInflater {

    @NonNull
    @Override
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        if (!CJKFakeFontWeight.isNoMediumWeightFont()) {
            return super.createTextView(context, attrs);
        }
        return new FakeFontWeightMaterialTextView(context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatButton createButton(@NonNull Context context, @NonNull AttributeSet attrs) {
        if (!CJKFakeFontWeight.isNoMediumWeightFont()) {
            return super.createButton(context, attrs);
        }
        return new FakeFontWeightMaterialButton(context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attrs) {
        if (!CJKFakeFontWeight.isNoMediumWeightFont()) {
            return super.createCheckedTextView(context, attrs);
        }
        return new FakeFontWeightAppCompatCheckedTextView(context, attrs);
    }
}
