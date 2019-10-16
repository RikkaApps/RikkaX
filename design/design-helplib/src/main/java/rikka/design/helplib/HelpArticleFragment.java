package rikka.design.helplib;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

public class HelpArticleFragment extends MarkdownFragment {

    public static MarkdownFragment newInstance(@RawRes int res) {
        Bundle args = new Bundle();
        args.putInt(Intent.EXTRA_TEXT, res);

        HelpArticleFragment fragment = new HelpArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.helplib_content_article, container, false);
    }
}
