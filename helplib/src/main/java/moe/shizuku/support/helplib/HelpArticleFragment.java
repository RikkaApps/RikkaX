package moe.shizuku.support.helplib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
