package moe.shizuku.billing.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import moe.shizuku.billing.R;
import moe.shizuku.support.recyclerview.BaseViewHolder;


public class PaymentFeatureViewHolder extends BaseViewHolder<PaymentFeatureInfo> {

    public static final Creator<PaymentFeatureInfo> CREATOR = new Creator<PaymentFeatureInfo>() {
        @Override
        public BaseViewHolder<PaymentFeatureInfo> createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new PaymentFeatureViewHolder(inflater.inflate(R.layout.billingclient_item_feature, parent, false));
        }
    };

    private ImageView mIcon;
    private TextView mTitle;
    private TextView mSummary;

    public PaymentFeatureViewHolder(View itemView) {
        super(itemView);

        mIcon = itemView.findViewById(android.R.id.icon);
        mTitle = itemView.findViewById(android.R.id.title);
        mSummary = itemView.findViewById(android.R.id.summary);
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
        mTitle.setText(getData().title);
        mSummary.setText(getData().summary);
    }
}