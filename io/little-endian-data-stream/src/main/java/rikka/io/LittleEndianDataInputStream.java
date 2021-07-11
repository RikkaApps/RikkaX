package rikka.io;

import androidx.annotation.NonNull;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianDataInputStream extends InputStream implements DataInput {

    protected final InputStream in;
    private final byte[] buffer = new byte[8];

    public LittleEndianDataInputStream(@NonNull InputStream in) {
        super();
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException("reached end after reading " + n + " bytes; excepted " + len + " bytes");
            n += count;
        }
    }

    @Override
    public int skipBytes(int n) throws IOException {
        int total = 0;
        int cur;

        while ((total < n) && ((cur = (int) in.skip(n - total)) > 0)) {
            total += cur;
        }

        return total;
    }

    @Override
    public int readUnsignedByte() throws IOException {
        int b = in.read();
        if (b < 0) {
            throw new EOFException();
        }
        return b;
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readUnsignedByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        readFully(buffer, 0, 2);
        return (buffer[1] & 0xFF) << 8 | (buffer[0] & 0xFF);
    }

    @Override
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        return (char) readUnsignedShort();
    }

    @Override
    public int readInt() throws IOException {
        readFully(buffer, 0, 4);
        return buffer[3] << 24 | (buffer[2] & 0xFF) << 16 | (buffer[1] & 0xFF) << 8 | (buffer[0] & 0xFF);
    }

    @Override
    public long readLong() throws IOException {
        readFully(buffer, 0, 8);
        return buffer[7] << 24 | (buffer[6] & 0xFF) << 16 | (buffer[5] & 0xFF) << 8 | (buffer[4] & 0xFF)
                | buffer[3] << 24 | (buffer[2] & 0xFF) << 16 | (buffer[1] & 0xFF) << 8 | (buffer[0] & 0xFF);
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readLine() {
        throw new UnsupportedOperationException("readLine is not supported");
    }

    @Override
    public String readUTF() throws IOException {
        throw new UnsupportedOperationException("readUTF is not supported");
    }
}
