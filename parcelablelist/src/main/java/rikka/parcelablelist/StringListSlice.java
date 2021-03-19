package rikka.parcelablelist;

import android.os.Parcel;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class StringListSlice extends BaseParcelableListSlice<String> {

    public static StringListSlice emptyList() {
        return new StringListSlice(Collections.emptyList());
    }

    public StringListSlice(@Nullable List<String> list) {
        super(list);
    }

    private StringListSlice(Parcel in) {
        super(in);
    }

    @Override
    public String readElement(Parcel in) {
        return in.readString();
    }

    @Override
    protected void writeElement(String string, Parcel dest, int writeFlags) {
        dest.writeString(string);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StringListSlice> CREATOR = new Creator<StringListSlice>() {
        @Override
        public StringListSlice createFromParcel(Parcel in) {
            return new StringListSlice(in);
        }

        @Override
        public StringListSlice[] newArray(int size) {
            return new StringListSlice[size];
        }
    };
}
