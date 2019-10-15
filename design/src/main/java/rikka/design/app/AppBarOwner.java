package rikka.design.app;

import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import rikka.design.widget.AppBarLayout;

public interface AppBarOwner {

    @Nullable
    AppBar getAppBar();

    void setAppBar(@NonNull AppBarLayout appBarLayout, @NonNull Toolbar toolbar);

    //Menu getMenu();
}
