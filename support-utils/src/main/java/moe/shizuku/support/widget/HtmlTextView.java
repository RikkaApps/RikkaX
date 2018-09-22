package moe.shizuku.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import moe.shizuku.support.R;
import moe.shizuku.support.utils.HtmlUtils;

@SuppressLint("AppCompatCustomView")
public class HtmlTextView extends TextView {

    public HtmlTextView(Context context) {
        this(context, null);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Attribute initialization.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView,
                defStyleAttr, 0);

        String html = a.getString(R.styleable.HtmlTextView_textHtml);
        setHtmlText(html);

        a.recycle();
    }

    public void setHtmlText(String html) {
        if (html != null) {
            setText(HtmlUtils.fromHtml(html));
        } else {
            setText(null);
        }
    }
}
