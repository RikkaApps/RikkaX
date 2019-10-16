package rikka.design.helplib;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;
import moe.shizuku.preference.PreferenceGroup;
import rikka.recyclerview.RecyclerViewHelper;

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
        recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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
    public void addPreference(String categoryKey, @StringRes int title, @StringRes int summary, @DrawableRes int icon, Intent intent, Preference.OnPreferenceClickListener listener) {
        addPreference(categoryKey, getString(title), getString(summary), requireContext().getDrawable(icon), intent, listener);
    }

    /**
     * Add a preference to specified preference group.
     */
    public void addPreference(String categoryKey, CharSequence title, CharSequence summary, Drawable icon, Intent intent, Preference.OnPreferenceClickListener listener) {
        Preference preference = new Preference(requireContext(), null,R.attr.preferenceStyle, R.style.HelpTheme_Preference);
        preference.setTitle(title);
        preference.setSummary(summary);
        preference.setIcon(icon);
        preference.setIntent(intent);
        preference.setOnPreferenceClickListener(listener);
        ((PreferenceGroup) findPreference(categoryKey)).addPreference(preference);
    }

    public void addArticle(@StringRes int title, @StringRes int summary, @RawRes final int res) {
        addPreference(KEY_HELP, title, summary, R.drawable.helplib_document_24dp, null, new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.dir_enter, R.animator.dir_leave, R.animator.dir_enter, R.animator.dir_leave)
                        .add(android.R.id.content, HelpArticleFragment.newInstance(res))
                        .addToBackStack(null)
                        .commit();

                return true;
            }
        });
    }
}
