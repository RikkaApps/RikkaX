package moe.shizuku.support.recyclerview.helper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import moe.shizuku.support.recyclerview.BaseRecyclerViewAdapter;

/**
 * 用来处理选择状态的帮助类。
 */
public class ActionModeHelper<T> {

    private BaseRecyclerViewAdapter mAdapter;
    private List mSelected;

    private ActionModeCallback mActionModeCallback;

    public ActionModeHelper(BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        mSelected = new ArrayList<>();
    }

    public void setActionModeCallback(ActionModeCallback actionModeCallback) {
        mActionModeCallback = actionModeCallback;
    }

    public void toggle(T key, RecyclerView.ViewHolder holder) {
        boolean selected = mSelected.contains(key);
        selected = !selected;
        if (selected) {
            mSelected.add(key);
        } else {
            mSelected.remove(key);
        }

        holder.itemView.setSelected(selected);

        if (mActionModeCallback != null) {
            if (!isActionModeActive()) {
                mActionModeCallback.onStopActionMode();
            } else {
                mActionModeCallback.onSelectedChanged();
            }
        }
    }

    public void setSelected(T key, boolean selected, RecyclerView.ViewHolder holder) {
        if (selected) {
            if (!mSelected.contains(key)) {
                mSelected.add(key);
            }
        } else {
            mSelected.remove(key);
        }

        holder.itemView.setSelected(selected);
    }

    public boolean isSelected(T key) {
        return mSelected.contains(key);
    }

    public boolean isActionModeActive() {
        return mSelected.size() > 0;
    }

    public List<T> getSelected() {
        return mSelected;
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean notify) {
        mSelected.clear();
        if (notify) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
