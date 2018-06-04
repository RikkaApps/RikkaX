package moe.shizuku.support.design;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import moe.shizuku.support.compat.CollectionsCompat;
import moe.shizuku.support.compat.Predicate;
import moe.shizuku.support.utils.ResolveInfoHelper;

public class BaseChooserFragment extends Fragment {

    @NonNull
    public static BaseChooserFragment newChooser(
            @NonNull Context context,
            @NonNull String title,
            @NonNull Intent sourceIntent,
            @Nullable ResolveListTransformer resolveListTransformer
    ) {
        BaseChooserFragment baseChooserFragment = new BaseChooserFragment();
        Bundle arguments = new Bundle();

        arguments.putString(ARG_EXTRA_TITLE, title);
        arguments.putParcelable(ARG_EXTRA_SOURCE_INTENT, sourceIntent);

        List<ResolveInfo> resolves = ResolveInfoHelper
                .queryIntentActivities(context, sourceIntent, 0);
        if (resolveListTransformer != null) {
            resolves = resolveListTransformer.apply(resolves);
        }
        arguments.putParcelableArrayList(ARG_EXTRA_RESOLVES, new ArrayList<>(resolves));

        baseChooserFragment.setArguments(arguments);

        return baseChooserFragment;
    }

    @NonNull
    public static BaseChooserFragment newSendEmailChooser(
            @NonNull Context context,
            @NonNull String chooserTitle,
            @NonNull Uri mailtoUri,
            @Nullable String subject,
            @Nullable String body,
            @Nullable Uri attachment
    ) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, mailtoUri);

        final List<ComponentName> exclude = new ArrayList<>();
        List<ResolveInfo> mailActivities = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        for (ResolveInfo info : mailActivities) {
            if (!info.activityInfo.exported) {
                exclude.add(new ComponentName(
                        info.activityInfo.packageName, info.activityInfo.name));
            }
        }

        intent = new Intent(Intent.ACTION_SEND, mailtoUri)
                .setType("vnd.android.cursor.dir/email")
                .putExtra(Intent.EXTRA_EMAIL, new String[]{mailtoUri.getSchemeSpecificPart()})
                .putExtra(Intent.EXTRA_SUBJECT, subject)
                .putExtra(Intent.EXTRA_TEXT, body)
                .putExtra(Intent.EXTRA_STREAM, attachment);

        List<ResolveInfo> sendActivities = context.
                getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : sendActivities) {
            boolean skip = false;
            for (ResolveInfo info2 : mailActivities) {
                if (Objects.equals(info.activityInfo.packageName, info2.activityInfo.packageName)
                        && Objects.equals(info.activityInfo.name, info2.activityInfo.name)) {
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            exclude.add(new ComponentName(
                    info.activityInfo.packageName, info.activityInfo.name));
        }

        return BaseChooserFragment.newChooser(context, chooserTitle, intent, new ResolveListTransformer() {
            @NonNull
            @Override
            public List<ResolveInfo> apply(@NonNull List<ResolveInfo> original) {
                return CollectionsCompat.filterToList(original, new Predicate<ResolveInfo>() {
                    @Override
                    public boolean apply(ResolveInfo info) {
                        final ComponentName currentComponentName = new ComponentName(
                                info.activityInfo.packageName, info.activityInfo.name);
                        return CollectionsCompat.anyMatch(exclude, new Predicate<ComponentName>() {
                            @Override
                            public boolean apply(ComponentName excludedItem) {
                                return excludedItem.equals(currentComponentName);
                            }
                        });
                    }
                });
            }
        });
    }

    private static final String ARG_EXTRA_TITLE = "extra.title";
    private static final String ARG_EXTRA_SOURCE_INTENT = "extra.source_intent";
    private static final String ARG_EXTRA_RESOLVES = "extra.resolves";

    private String mTitle;
    private Intent mSourceIntent;
    private List<ResolveInfo> mResolves;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            throw new IllegalArgumentException("Arguments cannot be null or empty.");
        }

        mTitle = getArguments().getString(ARG_EXTRA_TITLE);
        mSourceIntent = getArguments().getParcelable(ARG_EXTRA_SOURCE_INTENT);
        mResolves = getArguments().getParcelableArrayList(ARG_EXTRA_RESOLVES);
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

        view.<ResolverDrawerLayout>findViewById(R.id.contentPanel).setOnDismissedListener(
                new ResolverDrawerLayout.OnDismissedListener() {
                    @Override
                    public void onDismissed() {
                        requireActivity().finish();
                    }
                });

        RecyclerView recyclerView = view.findViewById(R.id.resolver_list);
        // TODO Adaptive grid layout
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        recyclerView.setAdapter(new BaseChooserItemAdapter(mResolves, this));
    }

    @NonNull
    public Intent getSourceIntent() {
        return mSourceIntent;
    }

    public interface ResolveListTransformer {

        @NonNull List<ResolveInfo> apply(@NonNull List<ResolveInfo> original);

    }

}
