package moe.shizuku.billing.ui;


import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

public class PaymentFeatureInfo {

    public final Drawable drawable;
    public final @DrawableRes int drawableRes;
    public final CharSequence title;
    public final CharSequence summary;

    public PaymentFeatureInfo(Drawable drawable, CharSequence title, CharSequence summary) {
        this.drawable = drawable;
        this.title = title;
        this.summary = summary;
        this.drawableRes = 0;
    }

    public PaymentFeatureInfo(int drawableRes, CharSequence title, CharSequence summary) {
        this.drawableRes = drawableRes;
        this.title = title;
        this.summary = summary;
        this.drawable = null;
    }
}
