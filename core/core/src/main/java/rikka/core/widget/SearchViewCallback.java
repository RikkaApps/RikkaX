package rikka.core.widget;

public interface SearchViewCallback {

    void onSearchExpand();

    void onSearchCollapse();

    void onSearchTextChange(String newText);
}
