package moe.shizuku.support.helplib;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;
import moe.shizuku.support.utils.IOUtils;
import ru.noties.markwon.Markwon;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.helplib_content_markdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            return;
        }

        final int res = getArguments().getInt(Intent.EXTRA_TEXT, 0);
        if (res == 0) {
            return;
        }

        final TextView textView = view.findViewById(android.R.id.text1);
        Markwon markwon = Markwon.create(textView.getContext());
        Spanned spanned = markwon.toMarkdown(IOUtils.toString(getResources().openRawResource(res)));
        textView.setText(spanned);
    }
}
