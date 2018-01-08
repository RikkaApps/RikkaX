package moe.shizuku.helplib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzhoujay.markdown.MarkDown;
import com.zzhoujay.markdown.method.LongPressLinkMovementMethod;

import moe.shizuku.support.text.LocalImageGetter;

/**
 * Created by rikka on 2017/11/7.
 */

public class MarkdownFragment extends Fragment {

    public static MarkdownFragment newInstance(@RawRes int res) {
        Bundle args = new Bundle();
        args.putInt(Intent.EXTRA_TEXT, res);

        MarkdownFragment fragment = new MarkdownFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.helplib_content_markdown, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final int res = getArguments().getInt(Intent.EXTRA_TEXT, 0);
        if (res == 0) {
            return;
        }

        final TextView textView = view.findViewById(android.R.id.text1);
        textView.setMovementMethod(LongPressLinkMovementMethod.getInstance());
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(MarkDown.fromMarkdown(getResources().openRawResource(res), new LocalImageGetter(textView), textView));
            }
        });
    }
}
