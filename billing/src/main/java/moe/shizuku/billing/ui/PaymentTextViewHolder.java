package moe.shizuku.billing.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import moe.shizuku.billing.R;
import moe.shizuku.support.recyclerview.BaseViewHolder;

/**
 * Created by rikka on 2017/12/29.
 */
public class PaymentTextViewHolder extends BaseViewHolder<CharSequence> {

    public static final Creator<CharSequence> CREATOR = new Creator<CharSequence>() {
        @Override
        public BaseViewHolder<CharSequence> createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new PaymentTextViewHolder(inflater.inflate(R.layout.billingclient_item_text, parent, false));
        }
    };

    private TextView mTextView;

    public PaymentTextViewHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView;
    }

    @Override
    public void onBind() {
        mTextView.setText(getData());
    }
}