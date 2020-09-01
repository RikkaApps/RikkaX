package rikka.html.text;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class LocalImageGetter implements Html.ImageGetter {

    private static final String TAG = "LocalImageGetter";

    private static final String RAW_PREFIX = "file:///android_res/raw/";

    private TextView mTextView;

    public LocalImageGetter(TextView textView) {
        mTextView = textView;
    }

    @Override
    public Drawable getDrawable(String url) {
        Drawable drawable = null;

        try {
            Resources resources = mTextView.getResources();
            int maxWidth = mTextView.getWidth() - mTextView.getPaddingLeft() - mTextView.getPaddingRight();

            if (url.startsWith(RAW_PREFIX)) {
                String name = url.substring(RAW_PREFIX.length(), url.lastIndexOf('.'));
                int res = resources.getIdentifier(name, "raw", mTextView.getContext().getPackageName());
                drawable = new BitmapDrawable(resources, BitmapFactory.decodeStream(resources.openRawResource(res)));

                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                if (width > maxWidth) {
                    height *= (float) maxWidth / width;
                    width = maxWidth;
                }

                drawable.setBounds(0, 0, width, height);
            } else {
                Log.w(TAG, "unsupported url: " + url);
            }
        } catch (Throwable e) {
            Log.w(TAG, "can't load image: " + url, e);
        }

        return drawable;
    }
}
