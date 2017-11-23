package moe.shizuku.support.recyclerview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikka on 2017/11/19.
 */

public class IndexCreatorPool implements CreatorPool {

    private final List<BaseViewHolder.Creator> mCreators;

    public IndexCreatorPool() {
        mCreators = new ArrayList<>();
    }

    public List<BaseViewHolder.Creator> getCreators() {
        return mCreators;
    }

    @Override
    public int getCreatorIndex(BaseRecyclerViewAdapter adapter, int position) {
        return position;
    }

    @Override
    public BaseViewHolder.Creator getCreator(int index) {
        return mCreators.get(index);
    }
}
