package rikka.material.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import rikka.design.R;

public class RaisedAppBarLayout extends AppBarLayout implements RaisedView {

    private static final int[] CHECKED_STATE_SET = {
            R.attr.state_raised
    };

    private boolean isRaised;

    public RaisedAppBarLayout(@NonNull Context context) {
        super(context);
    }

    public RaisedAppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RaisedAppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RaisedAppBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isRaised()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean isRaised() {
        return isRaised;
    }

    @Override
    public void setRaised(boolean raised) {
        if (isRaised != raised) {
            isRaised = raised;
            refreshDrawableState();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        RaisedAppBarLayout.SavedState state = new RaisedAppBarLayout.SavedState(super.onSaveInstanceState());
        state.isRaised = isRaised();
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final RaisedAppBarLayout.SavedState ss = (RaisedAppBarLayout.SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.isRaised) {
            setRaised(isRaised);
        }
    }

    static class SavedState extends BaseSavedState {

        public boolean isRaised;

        public SavedState(Parcel source) {
            super(source);
            isRaised = source.readByte() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(isRaised ? (byte) 1 : (byte) 0);
        }

        public static final Creator<RaisedAppBarLayout.SavedState> CREATOR = new Creator<RaisedAppBarLayout.SavedState>() {

            @Override
            public RaisedAppBarLayout.SavedState createFromParcel(Parcel source) {
                return new RaisedAppBarLayout.SavedState(source);
            }

            @Override
            public RaisedAppBarLayout.SavedState[] newArray(int size) {
                return new RaisedAppBarLayout.SavedState[size];
            }
        };
    }
}
