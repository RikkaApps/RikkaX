package moe.shizuku.support.recyclerview.helper;

import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

import moe.shizuku.support.recyclerview.BaseRecyclerViewAdapter;

/**
 * 用来处理需要根据条件过滤数据及搜索的帮助类。
 */
public class FilterHelper<T> {

    private BaseRecyclerViewAdapter mAdapter;
    private Filter mFilter;

    private List<T> mOriginalItems;
    private List<T> mFilteredItems;

    private String mKeyword;
    private boolean mIsSearching;
    private SparseIntArray mIntKeys;
    private SparseBooleanArray mBooleanKeys;

    public FilterHelper(BaseRecyclerViewAdapter adapter, Filter filter) {
        mAdapter = adapter;
        mFilter = filter;
        mIntKeys = new SparseIntArray();
        mBooleanKeys = new SparseBooleanArray();

        mOriginalItems = new ArrayList<>();
        mFilteredItems = new ArrayList<>();

        updateOriginalData();
    }

    public List<T> getOriginalItems() {
        return mOriginalItems;
    }

    public List<T> getFilteredItems() {
        return mFilteredItems;
    }

    /**
     * 当把新的数据设置到 Adapter 之后使用，并将重新设置为过滤后的数据
     */
    public void updateOriginalData() {
        mOriginalItems.clear();
        mOriginalItems.addAll(mAdapter.<T>getItems());
        filter();
    }

    public void setKeyword(String keyword) {
        if (mKeyword != null && mKeyword.equals(keyword)) {
            return;
        }

        mKeyword = keyword;
        filter();
    }

    public void setSearching(boolean searching) {
        mIsSearching = searching;
        filter();
        mAdapter.notifyDataSetChanged();
    }

    public void putKey(int key, boolean value) {
        putKey(key, value, true);
    }

    public void putKey(int key, boolean value, boolean notify) {
        mBooleanKeys.put(key, value);
        if (notify) {
            filter();
        }
    }

    public void putKey(int key, int value) {
        putKey(key, value, true);
    }

    public void putKey(int key, int value, boolean notify) {
        mIntKeys.put(key, value);
        if (notify) {
            filter();
        }
    }

    @SuppressWarnings("unchecked")
    private void filter() {
        mFilteredItems.clear();

        for (int position = 0; position < mOriginalItems.size(); position++) {
            T obj = mOriginalItems.get(position);

            boolean add = true;
            for (int i = 0; i < mIntKeys.size(); i++) {
                int key = mIntKeys.keyAt(i);
                if (!mFilter.filter(key, mIntKeys.get(key), obj)) {
                    add = false;
                    break;
                }
            }
            for (int i = 0; i < mBooleanKeys.size(); i++) {
                int key = mBooleanKeys.keyAt(i);
                if (!mFilter.filter(key, mBooleanKeys.get(key), obj)) {
                    add = false;
                    break;
                }
            }
            if (!add) {
                continue;
            }

            if (mIsSearching && !mFilter.contains(mKeyword, obj)) {
                continue;
            }

            mFilteredItems.add(obj);
        }

        mAdapter.getItems().clear();
        mAdapter.getItems().addAll(mFilteredItems);
        mAdapter.notifyDataSetChanged();
    }

    public static class Filter<T> {

        public boolean contains(String keyword, T obj) {
            return true;
        }

        public boolean filter(int key, int value, T obj) {
            return true;
        }

        public boolean filter(int key, boolean value, T obj) {
            return true;
        }
    }
}
