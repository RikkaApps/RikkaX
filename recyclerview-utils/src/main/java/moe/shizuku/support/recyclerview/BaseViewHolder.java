package moe.shizuku.support.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Rikka on 2017/2/16.
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public interface Creator<T> {
        BaseViewHolder<T> createViewHolder(LayoutInflater inflater, ViewGroup parent);
    }

    private T mData;
    private BaseRecyclerViewAdapter mAdapter;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public T getData() {
        return mData;
    }

    public BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    public final void bind(@NonNull List<Object> payloads, T data, BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;

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
