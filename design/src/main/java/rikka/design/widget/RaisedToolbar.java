package rikka.design.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Toolbar;

import rikka.design.R;

public class RaisedToolbar extends Toolbar implements RaisedView {

    private static final int[] CHECKED_STATE_SET = {
            R.attr.state_raised
    };

    private boolean isRaised;

    public RaisedToolbar(Context context) {
        super(context);
    }

    public RaisedToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RaisedToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RaisedToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.isRaised = isRaised();
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final SavedState ss = (SavedState) state;
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

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
