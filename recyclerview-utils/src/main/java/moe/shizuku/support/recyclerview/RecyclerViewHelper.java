package moe.shizuku.support.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rikka on 2017/8/16.
 */

public class RecyclerViewHelper {

    private static class FixOverScrollListener implements View.OnLayoutChangeListener {

        private boolean show = true;

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (shouldDrawOverScroll((RecyclerView) v) != show) {
                show = !show;

                if (show) {
                    v.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
                } else {
                    v.setOverScrollMode(View.OVER_SCROLL_NEVER);
                }
            }
        }

        public boolean shouldDrawOverScroll(RecyclerView recyclerView) {
            if (recyclerView.getLayoutManager() == null
                    || recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
                return false;
            }

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                int itemCount = recyclerView.getLayoutManager().getItemCount();
                int firstPosition, lastPosition;

                firstPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if (firstPosition == 0 && lastPosition == itemCount -1) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        }
    }

    public static void fixOverScroll(RecyclerView recyclerView) {
        recyclerView.addOnLayoutChangeListener(new FixOverScrollListener());
    }
}
