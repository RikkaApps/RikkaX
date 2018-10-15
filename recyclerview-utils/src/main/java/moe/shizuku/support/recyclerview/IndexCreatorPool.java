package moe.shizuku.support.recyclerview;

import java.util.ArrayList;
import java.util.List;

public class IndexCreatorPool implements CreatorPool {

    private final List<BaseViewHolder.Creator> mCreators;
    private final List<Integer> mPositionToIndex;

    public IndexCreatorPool() {
        mCreators = new ArrayList<>();
        mPositionToIndex = new ArrayList<>();
    }

    public void add(BaseViewHolder.Creator creator) {
        int indexOfCreator = mCreators.indexOf(creator);
        if (indexOfCreator != -1) {
            mPositionToIndex.add(indexOfCreator);
        } else {
            mCreators.add(creator);
            mPositionToIndex.add(mCreators.size() - 1);
        }
    }

    public void add(int index, BaseViewHolder.Creator creator) {
        int indexOfCreator = mCreators.indexOf(creator);
        if (indexOfCreator != -1) {
            mPositionToIndex.add(index, indexOfCreator);
        } else {
            mCreators.add(creator);
            mPositionToIndex.add(index, mCreators.size() - 1);
        }
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
