package moe.shizuku.support.recyclerview;

/**
 * Created by rikka on 2017/10/12.
 */

public interface CreatorPool {

    int getCreatorIndex(BaseRecyclerViewAdapter adapter, int position);
    BaseViewHolder.Creator getCreator(int index);
}
