package moe.shizuku.helplib;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;
import moe.shizuku.preference.PreferenceGroup;
import moe.shizuku.support.recyclerview.RecyclerViewHelper;

/**
 * Created by rikka on 2017/9/14.
 */

public class HelpFragment extends PreferenceFragment {

    public static final String KEY_HELP = "help";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_MAIL = "mail";
    public static final String KEY_TELEGRAM = "telegram";
    public static final String KEY_ISSUE = "issue";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.help);
    }

    @Override
    public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        RecyclerView recyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState);
        RecyclerViewHelper.fixOverScroll(recyclerView);
        return recyclerView;
    }

    @Nullable
    @Override
    public DividerDecoration onCreateItemDecoration() {
        return new CategoryDivideDividerDecoration();
    }

    /**
     * Add a preference to specified preference group.
     */
    public void addPreference(String categoryKey, @StringRes int title, @DrawableRes int icon, Intent intent) {
        addPreference(categoryKey, getString(title), getContext().getDrawable(icon), intent);
    }

    /**
     * Add a preference to specified preference group.
     */
    public void addPreference(String categoryKey, String title, Drawable icon, Intent intent) {
        Preference preference = new Preference(getContext());
        preference.setTitle(title);
        preference.setIcon(icon);
        preference.setIntent(intent);
        ((PreferenceGroup) findPreference(categoryKey)).addPreference(preference);
    }

    /**
     * Add a preference to specified preference group.
     */
    public void addPreference(String categoryKey, @StringRes int title, @DrawableRes int icon, Preference.OnPreferenceClickListener listener) {
        addPreference(categoryKey, getString(title), getContext().getDrawable(icon), listener);
    }

    /**
     * Add a preference to specified preference group.
     */
    public void addPreference(String categoryKey, String title, Drawable icon, Preference.OnPreferenceClickListener listener) {
        Preference preference = new Preference(getContext(), null, R.attr.preferenceStyle, R.style.HelpTheme_Preference);
        preference.setTitle(title);
        preference.setIcon(icon);
        preference.setOnPreferenceClickListener(listener);
        ((PreferenceGroup) findPreference(categoryKey)).addPreference(preference);
    }

    public void addArticle(@StringRes int title, @RawRes final int res) {
        addPreference(KEY_HELP, title, R.drawable.helplib_document_24dp, new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.dir_enter, R.animator.dir_leave, R.animator.dir_enter, R.animator.dir_leave)
                        .add(android.R.id.content, HelpArticleFragment.newInstance(res))
                        .addToBackStack(null)
                        .commit();

                return true;
            }
        });
    }
}
