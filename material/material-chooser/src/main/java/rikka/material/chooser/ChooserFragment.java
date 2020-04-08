package rikka.material.chooser;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.appiconloader.AppIconLoader;
import rikka.material.internal.ThemedAppCompatDialogFragment;

public class ChooserFragment extends ThemedAppCompatDialogFragment {

    public static final String EXTRA_ARGUMENTS = ChooserFragment.class.getName() + ".extra.ARGUMENTS";

    public static final String ARG_TITLE = ChooserFragment.class.getName() + ".arg.TITLE";
    public static final String ARG_TARGET_INTENT = ChooserFragment.class.getName() + ".arg.TARGET_INTENT";
    public static final String ARG_RESOLVE_INTENT = ChooserFragment.class.getName() + ".arg.RESOLVE_INTENT";
    public static final String ARG_RESOLVE_INFO_LIST = ChooserFragment.class.getName() + ".arg.RESOLVE_INFO_LIST";
    public static final String ARG_EXCLUDE_COMPONENT_LIST = ChooserFragment.class.getName() + ".arg.EXCLUDE_COMPONENT_LIST";
    public static final String ARG_EXTRA_RESOLVE_INFO_LIST = ChooserFragment.class.getName() + ".arg.EXTRA_RESOLVE_INFO_LIST";
    public static final String ARG_EXTRA_INTENT_LIST = ChooserFragment.class.getName() + ".arg.EXTRA_INTENT_LIST";
    public static final String ARG_EXTRA_FROM_START = ChooserFragment.class.getName() + ".arg.EXTRA_FROM_START";
    public static final String ARG_EXTRA_FROM_START_NEW_LINE = ChooserFragment.class.getName() + ".arg.EXTRA_FROM_START_NEW_LINE";

    @NonNull
    public static Intent newIntent(@NonNull ComponentName componentName, @NonNull String title,
                                   @NonNull Intent targetIntent) {
        return newIntent(componentName, title, targetIntent, null, null, null);
    }

    @NonNull
    public static Intent newIntent(@NonNull ComponentName componentName, @NonNull String title,
                                   @NonNull Intent targetIntent, @Nullable Intent resolveIntent) {
        return newIntent(componentName, title, targetIntent, resolveIntent, null, null);
    }

    @NonNull
    public static Intent newIntent(@NonNull ComponentName componentName, @NonNull String title,
                                   @NonNull Intent targetIntent, @Nullable Intent resolveIntent,
                                   @Nullable List<ComponentName> excludeComponents) {
        return newIntent(componentName, title, targetIntent, resolveIntent, excludeComponents, null);
    }

    @NonNull
    public static Intent newIntent(@NonNull ComponentName componentName, @NonNull String title,
                                   @NonNull Intent targetIntent, @Nullable List<ResolveInfo> resolves) {
        return newIntent(componentName, title, targetIntent, null, null, resolves);
    }

    @NonNull
    private static Intent newIntent(@NonNull ComponentName componentName, @NonNull String title,
                                    @NonNull Intent targetIntent, @Nullable Intent resolveIntent,
                                    @Nullable List<ComponentName> excludeComponents,
                                    @Nullable List<ResolveInfo> resolves) {
        Bundle arguments = new Bundle();
        arguments.putString(ChooserFragment.ARG_TITLE, title);
        arguments.putParcelable(ChooserFragment.ARG_TARGET_INTENT, targetIntent);
        if (resolveIntent != null) {
            arguments.putParcelable(ChooserFragment.ARG_RESOLVE_INTENT, resolveIntent);
        }
        if (resolves != null) {
            arguments.putParcelableArrayList(ChooserFragment.ARG_RESOLVE_INFO_LIST, new ArrayList<>(resolves));
        }
        if (excludeComponents != null) {
            arguments.putParcelableArrayList(ChooserFragment.ARG_EXCLUDE_COMPONENT_LIST, new ArrayList<>(excludeComponents));
        }

        return new Intent()
                .setComponent(componentName)
                .putExtra(EXTRA_ARGUMENTS, arguments);
    }

    @NonNull
    public static ChooserFragment newInstance(
            @NonNull String title,
            @NonNull Intent targetIntent) {
        return newInstance(title, targetIntent, null, null, null);
    }

    @NonNull
    public static ChooserFragment newInstance(
            @NonNull String title,
            @NonNull Intent targetIntent,
            @Nullable Intent resolveIntent) {
        return newInstance(title, targetIntent, resolveIntent, null, null);
    }

    @NonNull
    public static ChooserFragment newInstance(
            @NonNull String title,
            @NonNull Intent targetIntent,
            @Nullable Intent resolveIntent,
            @Nullable List<ComponentName> excludeComponents) {
        return newInstance(title, targetIntent, resolveIntent, excludeComponents, null);
    }

    @NonNull
    public static ChooserFragment newInstance(
            @NonNull String title,
            @NonNull Intent targetIntent,
            @Nullable List<ResolveInfo> resolves) {
        return newInstance(title, targetIntent, null, null, resolves);
    }

    @NonNull
    private static ChooserFragment newInstance(
            @NonNull String title,
            @NonNull Intent targetIntent,
            @Nullable Intent resolveIntent,
            @Nullable List<ComponentName> excludeComponents,
            @Nullable List<ResolveInfo> resolves) {
        ChooserFragment chooserFragment = new ChooserFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putParcelable(ARG_TARGET_INTENT, targetIntent);
        if (resolveIntent != null) {
            arguments.putParcelable(ARG_RESOLVE_INTENT, resolveIntent);
        }
        if (resolves != null) {
            arguments.putParcelableArrayList(ARG_RESOLVE_INFO_LIST, new ArrayList<>(resolves));
        }
        if (excludeComponents != null) {
            arguments.putParcelableArrayList(ARG_EXCLUDE_COMPONENT_LIST, new ArrayList<>(excludeComponents));
        }
        chooserFragment.setArguments(arguments);
        return chooserFragment;
    }

    private AppIconLoader mAppIconLoader;

    private String mTitle;
    private Intent mTargetIntent;
    private List<ResolveInfo> mResolves;

    private List<Intent> mExtraTargetIntent;
    private List<ResolveInfo> mExtraResolves;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            throw new IllegalArgumentException("Arguments cannot be null or empty.");
        }

        mAppIconLoader = new AppIconLoader(requireContext().getResources().getDimensionPixelSize(R.dimen.rd_chooser_icon_size), true, requireContext());

        mTitle = getArguments().getString(ARG_TITLE);
        mTargetIntent = getArguments().getParcelable(ARG_TARGET_INTENT);
        mResolves = getArguments().getParcelableArrayList(ARG_RESOLVE_INFO_LIST);

        if (mResolves == null) {
            if (getArguments().containsKey(ARG_RESOLVE_INTENT)) {
                mResolves = requireContext().getPackageManager().queryIntentActivities(getArguments().<Intent>getParcelable(ARG_RESOLVE_INTENT), 0);
            } else {
                mResolves = requireContext().getPackageManager().queryIntentActivities(mTargetIntent, 0);
            }

            List<ComponentName> exclude = getArguments().getParcelableArrayList(ARG_EXCLUDE_COMPONENT_LIST);
            for (ResolveInfo info : new ArrayList<>(mResolves)) {
                if (!info.activityInfo.exported
                        || (exclude != null && exclude.contains(new ComponentName(info.activityInfo.packageName, info.activityInfo.name)))) {
                    mResolves.remove(info);
                }
            }

            if (getArguments().containsKey(ARG_EXTRA_RESOLVE_INFO_LIST)) {
                mExtraResolves = getArguments().getParcelableArrayList(ARG_EXTRA_RESOLVE_INFO_LIST);
                if (getArguments().getBoolean(ARG_EXTRA_FROM_START, true)) {
                    mResolves.addAll(0, mExtraResolves);

                    if (getArguments().getBoolean(ARG_EXTRA_FROM_START_NEW_LINE, true)) {
                        int count = 4 - (mExtraResolves.size() % 4);
                        for (int i = 0; i < count; i++) {
                            mResolves.add(mExtraResolves.size(), null);
                        }
                    }
                } else {
                    mResolves.addAll(mExtraResolves);
                }
            }

            getArguments().putParcelableArrayList(ARG_RESOLVE_INFO_LIST, new ArrayList<>(mResolves));
        }

        if (getArguments().containsKey(ARG_EXTRA_INTENT_LIST)) {
            mExtraTargetIntent = getArguments().getParcelableArrayList(ARG_EXTRA_INTENT_LIST);
            mExtraResolves = getArguments().getParcelableArrayList(ARG_EXTRA_RESOLVE_INFO_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chooser_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.<TextView>findViewById(android.R.id.title).setText(mTitle);

        View emptyView = view.findViewById(android.R.id.empty);
        emptyView.setVisibility(mResolves.isEmpty() ? View.VISIBLE : View.GONE);

        RecyclerView list = view.findViewById(R.id.resolver_list);
        // TODO Adaptive grid layout
        list.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        list.setAdapter(new ChooserItemAdapter(mResolves, this));

        ResolverDrawerLayout contentPanel = view.findViewById(R.id.contentPanel);
        contentPanel.setOnDismissedListener(() -> requireActivity().finish());
    }

    @NonNull
    public Intent getTargetIntent(ResolveInfo resolveInfo) {
        if (mExtraResolves != null && mExtraResolves.contains(resolveInfo)) {
            int index = mExtraResolves.indexOf(resolveInfo);
            return mExtraTargetIntent.get(index);
        }
        return mTargetIntent;
    }

    protected AppIconLoader getAppIconLoader() {
        return mAppIconLoader;
    }
}
