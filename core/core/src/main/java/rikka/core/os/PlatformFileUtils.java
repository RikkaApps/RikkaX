package rikka.core.os;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.Executor;

@TargetApi(Build.VERSION_CODES.Q)
class PlatformFileUtils {

    public static long copy(@NonNull FileDescriptor in, @NonNull FileDescriptor out, @Nullable CancellationSignal signal, @Nullable Executor executor, @Nullable FileUtils.ProgressListener listener) throws IOException {
        return android.os.FileUtils.copy(in, out, signal, executor, listener != null ? listener::onProgress : null);
    }
}
