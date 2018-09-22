package moe.shizuku.support.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @NonNull
    public static FastScroller initFastScroller(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[] {
                android.R.attr.fastScrollThumbDrawable,
                android.R.attr.fastScrollTrackDrawable,
        });
        StateListDrawable thumbDrawable = (StateListDrawable) a.getDrawable(0);
        Drawable trackDrawable = a.getDrawable(1);
        a.recycle();

        return initFastScroller(recyclerView, thumbDrawable, trackDrawable, thumbDrawable, trackDrawable);
    }

    @SuppressLint("PrivateResource")
    @NonNull
    public static FastScroller initFastScroller(RecyclerView recyclerView, StateListDrawable verticalThumbDrawable,
                                        Drawable verticalTrackDrawable, StateListDrawable horizontalThumbDrawable,
                                        Drawable horizontalTrackDrawable) {
        Resources resources = recyclerView.getContext().getResources();
        return new FastScroller(recyclerView, verticalThumbDrawable, verticalTrackDrawable,
                horizontalThumbDrawable, horizontalTrackDrawable,
                resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness),
                resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range),
                resources.getDimensionPixelOffset(R.dimen.fastscroll_margin),
                resources.getDimensionPixelSize(R.dimen.fastscroll_min_height));
    }
}
