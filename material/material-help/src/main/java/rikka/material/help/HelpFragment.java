package rikka.material.help;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.RecyclerView;

import rikka.recyclerview.RecyclerViewKt;

public class HelpFragment extends PreferenceFragmentCompat {

    public static final String KEY_HELP = "help";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_MAIL = "mail";
    public static final String KEY_TELEGRAM = "telegram";
    public static final String KEY_ISSUE = "issue";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.helplib_preference);
    }

    @Override
    public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        RecyclerView recyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState);
        recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        RecyclerViewKt.fixEdgeEffect(recyclerView, true, true);
        return recyclerView;
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
        Preference preference = new Preference(requireContext(), null, androidx.preference.R.attr.preferenceStyle, R.style.Preference_Help);
        preference.setTitle(title);
        preference.setSummary(summary);
        preference.setIcon(icon);
        preference.setIntent(intent);
        preference.setOnPreferenceClickListener(listener);
        ((PreferenceGroup) findPreference(categoryKey)).addPreference(preference);
    }
}
