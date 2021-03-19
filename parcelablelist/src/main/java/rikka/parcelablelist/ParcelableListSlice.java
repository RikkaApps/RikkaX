package rikka.parcelablelist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class ParcelableListSlice<T extends Parcelable> extends BaseParcelableListSlice<T> {

    public static <T extends Parcelable> ParcelableListSlice<T> emptyList() {
        return new ParcelableListSlice<T>(Collections.<T>emptyList());
    }

    public ParcelableListSlice(@Nullable List<T> list) {
        super(list);
    }

    private ParcelableListSlice(Parcel in) {
        super(in);
    }

    @Override
    public T readElement(Parcel in) {
        return in.readParcelable(ParcelableListSlice.class.getClassLoader());
    }

    @Override
    protected void writeElement(T parcelable, Parcel dest, int writeFlags) {
        dest.writeParcelable(parcelable, writeFlags);
    }

    @Override
    public int describeContents() {
        int contents = 0;
        final List<T> list = getList();
        for (int i = 0; i < list.size(); i++) {
            contents |= list.get(i).describeContents();
        }
        return contents;
    }

    @SuppressWarnings("rawtypes")
    public static final Creator<ParcelableListSlice> CREATOR = new Creator<ParcelableListSlice>() {
        @Override
        public ParcelableListSlice createFromParcel(Parcel in) {
            return new ParcelableListSlice(in);
        }

        @Override
        public ParcelableListSlice[] newArray(int size) {
            return new ParcelableListSlice[size];
        }
    };
}
