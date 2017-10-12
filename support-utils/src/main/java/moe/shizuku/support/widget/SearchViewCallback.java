package moe.shizuku.support.widget;

/**
 * Created by Rikka on 2017/2/17.
 */

public interface SearchViewCallback {
    void onSearchExpand();
    void onSearchCollapse();
    void onSearchTextChange(String newText);
}
