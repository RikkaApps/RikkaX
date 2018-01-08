package moe.shizuku.billing.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import moe.shizuku.billing.R;
import moe.shizuku.support.recyclerview.BaseViewHolder;


public class PaymentButtonViewHolder extends BaseViewHolder<PaymentButtonInfo> {

    public static final Creator<PaymentButtonInfo> CREATOR = new Creator<PaymentButtonInfo>() {
        @Override
        public BaseViewHolder<PaymentButtonInfo> createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new PaymentButtonViewHolder(inflater.inflate(R.layout.billingclient_item_button, parent, false));
        }
    };

    private ImageView mIcon;
    private TextView mTitle;
    private TextView mPrice;

    public PaymentButtonViewHolder(View itemView) {
        super(itemView);

        mIcon = itemView.findViewById(android.R.id.icon);
        mTitle = itemView.findViewById(android.R.id.title);
        mPrice = itemView.findViewById(android.R.id.summary);
    }

    @Override
    public void onBind() {
        Context context = itemView.getContext();
        if (getData().drawable != null) {
            mIcon.setImageDrawable(getData().drawable);
        }
        if (getData().drawableRes != 0) {
            mIcon.setImageDrawable(context.getDrawable(getData().drawableRes));
        }
        mTitle.setText(getData().label);
        mPrice.setText(getData().price);
        itemView.setOnClickListener(getData().listener);
    }
}
