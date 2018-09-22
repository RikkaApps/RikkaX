package moe.shizuku.billing.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import moe.shizuku.billing.R;
import moe.shizuku.support.recyclerview.BaseRecyclerViewAdapter;
import moe.shizuku.support.recyclerview.BaseViewHolder;


public class PaymentViewHolder extends BaseViewHolder<BaseRecyclerViewAdapter> {

    public static final Creator<BaseRecyclerViewAdapter> CREATOR = new Creator<BaseRecyclerViewAdapter>() {
        @Override
        public BaseViewHolder<BaseRecyclerViewAdapter> createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new PaymentViewHolder(inflater.inflate(R.layout.billingclient_item_card, parent, false));
        }
    };

    private RecyclerView mRecyclerView;

    public PaymentViewHolder(View itemView) {
        super(itemView);

        setIsRecyclable(false);

        mRecyclerView = itemView.findViewById(android.R.id.content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    }

    @Override
    public void onBind() {
        super.onBind();

        mRecyclerView.setAdapter(getData());
    }
}
