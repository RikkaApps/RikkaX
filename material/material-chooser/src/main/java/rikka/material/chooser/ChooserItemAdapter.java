package rikka.material.chooser;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rikka.recyclerview.BaseRecyclerViewAdapter;
import rikka.recyclerview.BaseViewHolder;
import rikka.recyclerview.CreatorPool;

public class ChooserItemAdapter extends BaseRecyclerViewAdapter<CreatorPool> {

    private final BaseViewHolder.Creator<ResolveInfo> CREATOR = (inflater, parent) -> new ChooserItemViewHolder(inflater.inflate(R.layout.chooser_grid_item, parent, false));

    private ChooserFragment mParentFragment;

    public ChooserItemAdapter(List<ResolveInfo> resolveInfo, ChooserFragment parentFragment) {
        super(resolveInfo);

        mParentFragment = parentFragment;
    }

    @Override
    public CreatorPool onCreateCreatorPool() {
        return new CreatorPool() {

            @Override
            public int getCreatorIndex(BaseRecyclerViewAdapter adapter, int position) {
                return 0;
            }

            @Override
            public BaseViewHolder.Creator getCreator(int index) {
                return CREATOR;
            }
        };
    }

    private class ChooserItemViewHolder extends BaseViewHolder<ResolveInfo> implements View.OnClickListener {

        private ImageView icon;
        private TextView title;

        ChooserItemViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(android.R.id.icon);
            title = itemView.findViewById(android.R.id.text1);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(mParentFragment.getTargetIntent(getData()));
            intent.setComponent(new ComponentName(
                    getData().activityInfo.packageName,
                    getData().activityInfo.name));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            try {
                context.startActivity(intent);
            } catch (Throwable ignored) {
            }

            mParentFragment.requireActivity().finish();
        }

        @Override
        public void onBind() {
            super.onBind();

            if (getData() == null) {
                icon.setImageDrawable(null);
                title.setText(null);
                itemView.setOnClickListener(null);
                itemView.setClickable(false);
                itemView.setFocusable(false);
                return;
            }

            itemView.setOnClickListener(this);
            itemView.setFocusable(true);

            PackageManager pm = itemView.getContext().getPackageManager();

            icon.setImageDrawable(getData().loadIcon(pm));
            title.setText(getData().loadLabel(pm));
        }

    }

}