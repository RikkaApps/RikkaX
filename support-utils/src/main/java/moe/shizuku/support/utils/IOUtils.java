package moe.shizuku.support.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by rikka on 2017/10/12.
 */

public class IOUtils {

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] b = new byte[8192];
        for (int r; (r = is.read(b)) != -1;) {
            os.write(b, 0, r);
        }
    }

    public static String toString(InputStream is) {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] b = new byte[8192];
            int length;
            while ((length = is.read(b)) != -1) {
                result.write(b, 0, length);
            }
            return result.toString("UTF-8");
        } catch (IOException ignored) {
            return null;
        }
    }
}
