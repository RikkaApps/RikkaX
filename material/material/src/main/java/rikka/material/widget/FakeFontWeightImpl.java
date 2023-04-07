package rikka.material.widget;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

public class FakeFontWeightImpl {

    private static final FakeFontWeightImpl INSTANCE = new FakeFontWeightImpl();

    public static FakeFontWeightImpl getInstance() {
        return INSTANCE;
    }

    private static final Typeface DEFAULT = Typeface.create("sans-serif", Typeface.NORMAL);
    private static final Typeface MEDIUM = Typeface.create("sans-serif-medium", Typeface.NORMAL);

    private void updatePaint(Paint paint, boolean isMedium) {
        if (isMedium) {
            paint.setStrokeWidth(0.65f);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else {
            paint.setStrokeWidth(0f);
            paint.setStyle(Paint.Style.FILL);
        }
    }

    public Typeface onSetTypeface(Typeface tf) {
        boolean isMedium = MEDIUM.equals(tf);
        if (!isMedium) {
            return tf;
        }
        return DEFAULT;
    }

    public void onSetTypefacePost(TextView textView, Typeface tf) {
        boolean isMedium = MEDIUM.equals(tf);
        updatePaint(textView.getPaint(), isMedium);
    }

}
