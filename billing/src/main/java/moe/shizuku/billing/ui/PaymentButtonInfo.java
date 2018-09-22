package moe.shizuku.billing.ui;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;

public class PaymentButtonInfo {

    public final Drawable drawable;
    public final @DrawableRes int drawableRes;
    public final CharSequence label;
    public final CharSequence price;
    public final View.OnClickListener listener;

    public PaymentButtonInfo(Drawable drawable, CharSequence label, CharSequence price, View.OnClickListener listener) {
        this.drawable = drawable;
        this.label = label;
        this.price = price;
        this.listener = listener;
        this.drawableRes = 0;
    }

    public PaymentButtonInfo(int drawableRes, CharSequence label, CharSequence price, View.OnClickListener listener) {
        this.drawableRes = drawableRes;
        this.label = label;
        this.price = price;
        this.listener = listener;
        this.drawable = null;
    }
}
