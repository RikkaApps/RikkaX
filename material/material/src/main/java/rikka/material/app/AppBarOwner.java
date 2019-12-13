package rikka.material.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import rikka.material.widget.AppBarLayout;

public interface AppBarOwner {

    @Nullable
    AppBar getAppBar();

    void setAppBar(@NonNull AppBarLayout appBarLayout, @NonNull Toolbar toolbar);
}
