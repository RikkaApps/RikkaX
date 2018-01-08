package moe.shizuku.billing.ui;

import moe.shizuku.support.recyclerview.BaseRecyclerViewAdapter;
import moe.shizuku.support.recyclerview.ClassCreatorPool;


public class PaymentAdapter extends BaseRecyclerViewAdapter {

    public PaymentAdapter() {
        super();

        getCreatorPool()
                .putRule(BaseRecyclerViewAdapter.class, PaymentViewHolder.CREATOR)
                .putRule(PaymentButtonInfo.class, PaymentButtonViewHolder.CREATOR)
                .putRule(PaymentFeatureInfo.class, PaymentFeatureViewHolder.CREATOR)
                .putRule(CharSequence.class, PaymentTextViewHolder.CREATOR);
    }

    @Override
    public ClassCreatorPool getCreatorPool() {
        return (ClassCreatorPool) super.getCreatorPool();
    }
}
