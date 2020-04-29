package rikka.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

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
