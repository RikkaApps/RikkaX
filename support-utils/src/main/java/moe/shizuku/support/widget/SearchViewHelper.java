package moe.shizuku.support.widget;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Rikka on 2017/2/17.
 */

public class SearchViewHelper {

    private static final String KEYWORD_TAG = "searchViewHelper:keyword";
    private static final String IS_SEARCHING_TAG = "searchViewHelper:isSearching";

    private String mKeyword;
    private boolean mIsSearching;
    private int mId;

    public static SearchViewHelper create(Bundle savedInstanceState, int id) {
        SearchViewHelper searchViewHelper = new SearchViewHelper();
        searchViewHelper.onCreate(savedInstanceState, id);
        return searchViewHelper;
    }

    public void onCreate(Bundle savedInstanceState, int id) {
        mId = id;
        if (savedInstanceState != null) {
            mKeyword = savedInstanceState.getString(KEYWORD_TAG);
            mIsSearching = savedInstanceState.getBoolean(IS_SEARCHING_TAG);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEYWORD_TAG, mKeyword);
        outState.putBoolean(IS_SEARCHING_TAG, mIsSearching);
    }

    public void onCreateOptionsMenu(final SearchViewCallback o, final Menu menu) {
        MenuItem item = menu.findItem(mId);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                o.onSearchExpand();

                mIsSearching = true;
                setMenuItemsVisibility(menu, item, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                o.onSearchCollapse();

                mIsSearching = false;
                setMenuItemsVisibility(menu, null, true);
                return true;
            }
        });

        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                o.onSearchTextChange(newText);

                mKeyword = newText;
                return false;
            }
        });

        if (mIsSearching) {
            String keyword = mKeyword;
            item.expandActionView();
            searchView.setQuery(keyword, false);
        }
    }

    private void setMenuItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);

            if (item != exception) {
                item.setVisible(visible);
            }
        }
    }
}