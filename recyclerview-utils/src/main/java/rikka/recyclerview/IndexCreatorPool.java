package rikka.recyclerview;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class IndexCreatorPool implements CreatorPool {

    private final List<BaseViewHolder.Creator> mCreators;
    private final List<Integer> mPositionToIndex;

    public IndexCreatorPool() {
        mCreators = new ArrayList<>();
        mPositionToIndex = new ArrayList<>();
    }

    public void add(BaseViewHolder.Creator creator) {
        int indexOfCreator = mCreators.indexOf(creator);
        if (indexOfCreator == -1) {
            mCreators.add(creator);
            indexOfCreator = mCreators.size() - 1;
        }
        mPositionToIndex.add(indexOfCreator);
    }

    public void add(int itemPosition, BaseViewHolder.Creator creator) {
        int indexOfCreator = mCreators.indexOf(creator);
        if (indexOfCreator == -1) {
            mCreators.add(creator);
            indexOfCreator = mCreators.size() - 1;
        }
        mPositionToIndex.add(itemPosition, indexOfCreator);
    }

    public void remove(int itemPosition) {
        mPositionToIndex.remove(itemPosition);
    }

    public void clear() {
        mPositionToIndex.clear();
    }

    @Override
    public int getCreatorIndex(BaseRecyclerViewAdapter adapter, int position) {
        return mPositionToIndex.get(position);
    }

    @Override
    public BaseViewHolder.Creator getCreator(int index) {
        return mCreators.get(index);
    }
}
