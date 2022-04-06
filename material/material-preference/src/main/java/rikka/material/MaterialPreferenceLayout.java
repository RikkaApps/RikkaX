package rikka.material;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import rikka.layoutinflater.view.LayoutInflaterFactory;

public class MaterialPreferenceLayout implements LayoutInflaterFactory.OnViewCreatedListener {

    public static LayoutInflaterFactory.OnViewCreatedListener LISTENER = new MaterialPreferenceLayout();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        if (view instanceof LinearLayout && view.getId() == androidx.preference.R.id.icon_frame) {
                view.setMinimumWidth((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    48,
                    view.getResources().getDisplayMetrics()
            ));
        }
    }
}
