package moe.shizuku.support.recyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IdBasedRecyclerViewAdapter extends BaseRecyclerViewAdapter<IndexCreatorPool> {

    private final List<Long> mIds = new ArrayList<>();

    public IdBasedRecyclerViewAdapter() {
        super();
    }

    public IdBasedRecyclerViewAdapter(@NonNull List<Object> data) {
        super(data);
    }

    @NonNull
    public List<Long> getIds() {
        return mIds;
    }

    @Override
    public long getItemId(int position) {
        return mIds.get(position);
    }

    public void clear() {
        getCreatorPool().clear();
        getItems().clear();
        getIds().clear();
    }

    public void addItem(@NonNull BaseViewHolder.Creator creator, @Nullable Object object, long id) {
        getCreatorPool().add(creator);
        getItems().add(object);
        getIds().add(id);
    }

    public void addItems(@NonNull BaseViewHolder.Creator creator, @NonNull List<Object> list, @NonNull List<Long> ids) {
        for (int index = 0; index < list.size(); index++) {
            addItem(creator, list.get(index), ids.get(index));
        }
    }

    public void notifyItemChangeById(long targetId) {
        for (int index = 0; index < getItemCount(); index++) {
            if (getIds().get(index) == targetId) {
                notifyItemChanged(index);
            }
        }
    }

    public void notifyItemChangeById(long targetId, @Nullable Object payload) {
        for (int index = 0; index < getItemCount(); index++) {
            if (getIds().get(index) == targetId) {
                notifyItemChanged(index, payload);
            }
        }
    }

    public void setFirstItemById(long targetId, @Nullable Object object) {
        for (int index = 0; index < getItemCount(); index++) {
            if (getIds().get(index) == targetId) {
                getItems().set(index, object);
                return;
            }
        }
        throw new NoSuchElementException("Cannot found any items belongs to id=" + targetId);
    }

    @Override
    public IndexCreatorPool onCreateCreatorPool() {
        return new IndexCreatorPool();
    }

}
