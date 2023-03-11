package rikka.material.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import rikka.layoutinflater.view.LayoutInflaterFactory;

public class FakeFontWeightOnCreateViewListener implements LayoutInflaterFactory.OnCreateViewListener {

    public static FakeFontWeightOnCreateViewListener getInstance() {
        return new FakeFontWeightOnCreateViewListener();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        if (name.equals(MaterialButton.class.getName())) {
            return new FakeFontWeightMaterialButton(context, attrs);
        } else if (name.equals(MaterialTextView.class.getName())) {
            return new FakeFontWeightMaterialTextView(context, attrs);
        } else if (name.equals(AppCompatCheckedTextView.class.getName())) {
            return new FakeFontWeightAppCompatCheckedTextView(context, attrs);
        }
        return null;
    }
}
