/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package moe.shizuku.support.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

public class BulletSpan implements LeadingMarginSpan, ParcelableSpan {

    public static final int STANDARD_GAP_WIDTH = 2;
    public static final int STANDARD_BULLET_RADIUS = 5;

    private final int mGapWidth;
    private final boolean mWantColor;
    private final int mColor;
    private final int mBulletRadius;

    private Path mBulletPath = null;

    public BulletSpan() {
        mGapWidth = STANDARD_GAP_WIDTH;
        mBulletRadius = STANDARD_BULLET_RADIUS;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth) {
        mGapWidth = gapWidth;
        mBulletRadius = STANDARD_BULLET_RADIUS;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth, int bulletRadius) {
        mGapWidth = gapWidth;
        mBulletRadius = bulletRadius;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth, int bulletRadius, int color) {
        mGapWidth = gapWidth;
        mBulletRadius = bulletRadius;
        mWantColor = true;
        mColor = color;
    }

    public BulletSpan(Parcel src) {
        mGapWidth = src.readInt();
        mWantColor = src.readInt() != 0;
        mColor = src.readInt();
        mBulletRadius = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return 8/*TextUtils.BULLET_SPAN*/;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mGapWidth);
        dest.writeInt(mWantColor ? 1 : 0);
        dest.writeInt(mColor);
        dest.writeInt(mBulletRadius);
    }

    public int getLeadingMargin(boolean first) {
        return 2 * mBulletRadius + 2 * mGapWidth;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int oldcolor = 0;

            if (mWantColor) {
                oldcolor = p.getColor();
                p.setColor(mColor);
            }

            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                if (mBulletPath == null) {
                    mBulletPath = new Path();
                    // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                    mBulletPath.addCircle(0.0f, 0.0f, 1.2f * mBulletRadius, Direction.CW);
                }

                c.save();
                c.translate(x + dir * mBulletRadius + mGapWidth, (top + bottom) / 2.0f);
                c.drawPath(mBulletPath, p);
                c.restore();
            } else {
                c.drawCircle(x + dir * mBulletRadius + mGapWidth, (top + bottom) / 2.0f, mBulletRadius, p);
            }

            if (mWantColor) {
                p.setColor(oldcolor);
            }

            p.setStyle(style);
        }
    }
}
