package moe.shizuku.support.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;

import moe.shizuku.support.compat.CollectionsCompat;
import moe.shizuku.support.compat.Predicate;

public final class ResolveInfoHelper {

    @NonNull
    public static List<ResolveInfo> filter(@NonNull Context context,
                                           @NonNull Collection<ResolveInfo> source) {
        final String packageName = context.getPackageName();
        return CollectionsCompat.filterToList(source, new Predicate<ResolveInfo>() {
            @Override
            public boolean apply(ResolveInfo info) {
                return !info.activityInfo.packageName.equals(packageName);
            }
        });
    }

    @NonNull
    public static List<ResolveInfo> queryIntentActivities(
            @NonNull Context context, @NonNull Intent intent, int flags) {
        return ResolveInfoHelper.filter(context,
                context.getPackageManager().queryIntentActivities(intent, flags));
    }

}
