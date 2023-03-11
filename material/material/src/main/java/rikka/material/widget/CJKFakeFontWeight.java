package rikka.material.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.Locale;

import rikka.material.app.LocaleDelegate;

public class CJKFakeFontWeight {

    private static final String TEXT_FOR_TEST = "好";

    private static Boolean noMediumFont = null;

    private static boolean isNoMediumFont() {
        Bitmap bitmap1 = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
        Bitmap bitmap2 = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap1);
        Canvas canvas2 = new Canvas(bitmap2);

        TextPaint paint = new TextPaint();
        paint.setTextSize(20);
        paint.setTextLocale(Locale.CHINESE);
        paint.setAntiAlias(false);
        paint.setSubpixelText(false);
        paint.setFakeBoldText(false);

        paint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        canvas1.drawText(TEXT_FOR_TEST, 20, 20, paint);
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        canvas2.drawText(TEXT_FOR_TEST, 20, 20, paint);

        return bitmap1.sameAs(bitmap2);
    }

    public static boolean isNoMediumWeightFont() {
        Locale locale = LocaleDelegate.getDefaultLocale();
        String language = locale.getLanguage();

        boolean isCJK = language.equals("zh") || language.equals("ja") || language.equals("ko");
        if (!isCJK) {
            return false;
        }

        if (noMediumFont == null) {
            noMediumFont = isNoMediumFont();
        }
        return noMediumFont;
    }

    public static void disable() {
        noMediumFont = false;
    }
}
