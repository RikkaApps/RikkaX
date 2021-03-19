package rikka.parcelablelist;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseParcelableListSlice<T> implements Parcelable {

    private static final String TAG = "ParcelableListSlice";
    private static final boolean DEBUG = true;

    private static final int MAX_IPC_SIZE = 64 * 1024;

    private final List<T> mList;

    public BaseParcelableListSlice(@Nullable List<T> list) {
        mList = list;
    }

    public final List<T> getList() {
        return mList;
    }

    protected BaseParcelableListSlice(Parcel in) {
        int size = in.readInt();
        if (size == -1) {
            mList = null;
            return;
        }
        mList = new ArrayList<>(size);

        readSliceFromParcel(mList, in);
        if (DEBUG) Log.d(TAG, "Retrieved " + mList.size() + " of " + size);

        if (mList.size() >= size) {
            return;
        }

        IBinder binder = in.readStrongBinder();
        do {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                binder.transact(IBinder.FIRST_CALL_TRANSACTION, data, reply, 0);
                readSliceFromParcel(mList, reply);

                if (DEBUG) Log.d(TAG, "Retrieved " + mList.size() + " of " + size + " from extra binder");
            } catch (RemoteException e) {
                Log.w(TAG, "Failure retrieving array; only received " + mList.size() + " of " + size, e);
                return;
            } finally {
                data.recycle();
                reply.recycle();
            }
        } while (mList.size() < size);
    }

    private void readSliceFromParcel(List<T> list, Parcel in) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            list.add(readElement(in));
        }
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        if (mList == null) {
            dest.writeInt(-1);
            return;
        }

        int size = mList.size();
        dest.writeInt(size);

        Iterator<T> iterator = mList.iterator();
        writeSliceToParcel(iterator, dest, flags);

        while (iterator.hasNext()) {
            int writeFlags = flags;
            IBinder binder = new Binder() {
                @Override
                protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) {
                    if (code != IBinder.FIRST_CALL_TRANSACTION || reply == null) {
                        return false;
                    }
                    writeSliceToParcel(iterator, reply, writeFlags);
                    return true;
                }
            };
            dest.writeStrongBinder(binder);
        }
    }

    private void writeSliceToParcel(Iterator<T> iterator, Parcel dest, int flags) {
        int startPosition = dest.dataPosition();
        dest.writeInt(0);
        int size = 0;

        // This will actually exceed MAX_IPC_SIZE.
        // However, consider that no one will use binder to send large objects directly
        // and binder has total 1MB buffer size across the process, unless all 16 binder
        // threads are doing this at the same time, exceed a little bit could never be
        // a problem.
        // Also, shrink is not allowed by Parcel and the system's ParceledListSlice class
        // has the same implementation, we really don't need to consider this.
        while (iterator.hasNext() && dest.dataSize() < MAX_IPC_SIZE) {
            writeElement(iterator.next(), dest, flags);
            size ++;
        }
        int position = dest.dataPosition();
        dest.setDataPosition(startPosition);
        dest.writeInt(size);
        dest.setDataPosition(position);
    }

    public abstract T readElement(Parcel in);

    protected abstract void writeElement(T parcelable, Parcel dest, int writeFlags);
}
