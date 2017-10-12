package moe.shizuku.support.recyclerview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikka on 2017/10/12.
 */

public class CreatorPool {

    private final List<Class<?>> mClasses;
    private final List<BaseViewHolder.Creator> mCreators;

    public CreatorPool() {
        mClasses = new ArrayList<>();
        mCreators = new ArrayList<>();
    }

    public <T> CreatorPool putRule(Class<T> clazz, BaseViewHolder.Creator<T> creator) {
        int position = mClasses.indexOf(clazz);
        if (position != -1) {
            mCreators.set(position, creator);
        } else {
            mClasses.add(clazz);
            mCreators.add(creator);
        }
        return this;
    }

    public int getCreatorIndex(BaseRecyclerViewAdapter adapter, int position) {
        Object data = adapter.getItemAt(position);
        for (int i = 0; i < mClasses.size(); i++) {
            if (mClasses.get(i).isAssignableFrom(data.getClass())) {
                return i;
            }
        }
        return -1;
    }

    public BaseViewHolder.Creator getCreator(int index) {
        return mCreators.get(index);
    }
}
