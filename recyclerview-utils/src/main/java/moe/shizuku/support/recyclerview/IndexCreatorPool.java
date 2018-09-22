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
        int index = mCreators.indexOf(creator);
        if (index != -1) {
            mPositionToIndex.add(index);
        } else {
            mCreators.add(creator);
            mPositionToIndex.add(mCreators.size() - 1);
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
