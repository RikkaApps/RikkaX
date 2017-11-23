package moe.shizuku.support.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2017/2/16.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List mItems;
    private CreatorPool mCreatorPool;

    public BaseRecyclerViewAdapter() {
        this(new ArrayList<>());
    }

    public BaseRecyclerViewAdapter(CreatorPool creatorPool) {
        this(new ArrayList<>(), creatorPool);
    }

    public BaseRecyclerViewAdapter(List<?> items) {
        this(items, new ClassCreatorPool());
    }

    public BaseRecyclerViewAdapter(List<?> items, CreatorPool creatorPool) {
        mItems = items;
        mCreatorPool = creatorPool;
    }

    public CreatorPool getCreatorPool() {
        return mCreatorPool;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getItems() {
        return mItems;
    }

    public void setItems(List items) {
        mItems = items;
    }

    @SuppressWarnings("unchecked")
    public <T> T getItemAt(int position) {
        return (T) mItems.get(position);
    }

    @Override
    public final int getItemCount() {
        return mItems.size();
    }

    @Override
    public final int getItemViewType(int position) {
        Object data = getItemAt(position);
        int index = mCreatorPool.getCreatorIndex(this, position);
        if (index >= 0) {
            return index;
        }
        throw new IllegalStateException("Can't find Creator for " + data.getClass() + ", position: " + position);
    }

    public LayoutInflater onGetLayoutInflater(View parent) {
        return LayoutInflater.from(parent.getContext());
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int creatorIndex) {
        LayoutInflater inflater = onGetLayoutInflater(parent);
        BaseViewHolder.Creator creator = mCreatorPool.getCreator(creatorIndex);
        return creator.createViewHolder(inflater, parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(payloads, getItemAt(position), this);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        holder.bind(getItemAt(position), this);
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);

        holder.recycle();
    }
}
