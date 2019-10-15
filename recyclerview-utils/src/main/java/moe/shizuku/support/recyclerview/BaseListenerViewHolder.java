package moe.shizuku.support.recyclerview;

import android.view.View;

public class BaseListenerViewHolder<T, L> extends BaseViewHolder<T> {

    public BaseListenerViewHolder(View itemView) {
        super(itemView);
    }

    public L getListener() {
        //noinspection unchecked
        return (L) getAdapter().getListener();
    }
}
