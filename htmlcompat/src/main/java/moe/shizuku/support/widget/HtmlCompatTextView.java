package moe.shizuku.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import moe.shizuku.support.htmlcompat.R;
import moe.shizuku.support.text.HtmlCompat;

@SuppressLint("AppCompatCustomView")
public class HtmlCompatTextView extends TextView {

    private static final String TAG = "HtmlCompatTextView";

    private int mFlags;
    private Html.ImageGetter mImageGetter;
    private HtmlCompat.TagHandler mTagHandler;

    public HtmlCompatTextView(Context context) {
        this(context, null);
    }

    public HtmlCompatTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HtmlCompatTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HtmlCompatTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HtmlCompatTextView,
                defStyleAttr, 0);

        String html = a.getString(R.styleable.HtmlCompatTextView_htmlText);
        int flags = a.getInteger(R.styleable.HtmlCompatTextView_htmlFlags, HtmlCompat.FROM_HTML_MODE_LEGACY);
        String imageGetterClass = a.getString(R.styleable.HtmlCompatTextView_htmlImageGetter);
        String tagHandlerClass = a.getString(R.styleable.HtmlCompatTextView_htmlTagHandler);

        a.recycle();

        setFlags(flags);

        Html.ImageGetter imageGetter = null;
        if (imageGetterClass != null) {
            try {
                imageGetter = (Html.ImageGetter) Class.forName(imageGetterClass).getConstructor().newInstance();
            } catch (Throwable e) {
                Log.e(TAG, "unable create instance of " + imageGetterClass, e);
            }

            setImageGetter(imageGetter);
        }

        HtmlCompat.TagHandler tagHandler = null;
        if (tagHandlerClass != null) {
            try {
                tagHandler = (HtmlCompat.TagHandler) Class.forName(tagHandlerClass).getConstructor().newInstance();
            } catch (Throwable e) {
                Log.e(TAG, "unable create instance of " + tagHandlerClass, e);
            }

            setTagHandler(tagHandler);
        }

        setHtmlText(html, flags, imageGetter, tagHandler);
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public Html.ImageGetter getImageGetter() {
        return mImageGetter;
    }

    public void setImageGetter(Html.ImageGetter imageGetter) {
        mImageGetter = imageGetter;
    }

    public HtmlCompat.TagHandler getTagHandler() {
        return mTagHandler;
    }

    public void setTagHandler(HtmlCompat.TagHandler tagHandler) {
        mTagHandler = tagHandler;
    }

    public void setHtmlText(String html) {
        setHtmlText(html, getFlags(), getImageGetter(), getTagHandler());
    }

    public void setHtmlText(String html, int flags) {
        setHtmlText(html, flags, getImageGetter(), getTagHandler());
    }

    public void setHtmlText(String html, Html.ImageGetter imageGetter) {
        setHtmlText(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, getTagHandler());
    }

    public void setHtmlText(String html, HtmlCompat.TagHandler tagHandler) {
        setHtmlText(html, HtmlCompat.FROM_HTML_MODE_LEGACY, getImageGetter(), tagHandler);
    }

    public void setHtmlText(String html, int flags, Html.ImageGetter imageGetter) {
        setHtmlText(html, flags, imageGetter, getTagHandler());
    }

    public void setHtmlText(String html, int flags, HtmlCompat.TagHandler tagHandler) {
        setHtmlText(html, flags, getImageGetter(), tagHandler);
    }

    public void setHtmlText(String html, Html.ImageGetter imageGetter, HtmlCompat.TagHandler tagHandler) {
        setHtmlText(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler);
    }

    public void setHtmlText(String html, int flags, Html.ImageGetter imageGetter, HtmlCompat.TagHandler tagHandler) {
        if (html != null) {
            setText(HtmlCompat.fromHtml(html, flags, imageGetter, tagHandler));
            setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            setText(null);
        }
    }
}
