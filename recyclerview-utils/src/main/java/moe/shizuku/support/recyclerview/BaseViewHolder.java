package moe.shizuku.support.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public interface Creator<T> {
        BaseViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent);
    }

    private T mData;
    private BaseRecyclerViewAdapter mAdapter;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    public final Context getContext() {
        return itemView.getContext();
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        setData(data, null);
    }

    public void setData(T data, Object payload) {
        mData = data;

        int position = getAdapterPosition();
        getAdapter().getItems().set(position, data);
        getAdapter().notifyItemChanged(position, payload);
    }

    public BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    public final void bind(@NonNull List<Object> payloads, T data, BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        mData = data;

        onBind(payloads);
    }

    public final void bind(T data, BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        mData = data;

        onBind();
    }

    /**
     * Called when bind.
     *
     **/
    public void onBind() {

    }

    /**
     * Called when partial bind.
     *
     * @param payloads A non-null list of merged payloads
     */
    public void onBind(@NonNull List<Object> payloads) {

    }

    public final void recycle() {
        mData = null;
        mAdapter = null;

        onRecycle();
    }

    public void onRecycle() {

    }
}
