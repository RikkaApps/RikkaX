package rikka.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

public class LittleEndianDataOutputStream extends OutputStream implements DataOutput {

    protected final OutputStream out;
    private final byte[] buffer = new byte[8];

    public LittleEndianDataOutputStream(OutputStream out) {
        super();
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        out.write(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        out.write((byte) v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        buffer[0] = (byte) (0xFF & v);
        buffer[1] = (byte) (0xFF & (v >> 8));
        out.write(buffer, 0, 2);
    }

    @Override
    public void writeChar(int v) throws IOException {
        writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        buffer[0] = (byte) (0xFF & v);
        buffer[1] = (byte) (0xFF & (v >> 8));
        buffer[2] = (byte) (0xFF & (v >> 16));
        buffer[3] = (byte) (0xFF & (v >> 24));
        out.write(buffer, 0, 4);
    }

    @Override
    public void writeLong(long v) throws IOException {
        buffer[0] = (byte) (0xFF & v);
        buffer[1] = (byte) (0xFF & (v >> 8));
        buffer[2] = (byte) (0xFF & (v >> 16));
        buffer[3] = (byte) (0xFF & (v >> 24));
        buffer[4] = (byte) (0xFF & (v >> 32));
        buffer[5] = (byte) (0xFF & (v >> 40));
        buffer[6] = (byte) (0xFF & (v >> 48));
        buffer[7] = (byte) (0xFF & (v >> 56));
        write(buffer, 0, 8);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    @Override
    public void writeBytes(String s) throws IOException {
        out.write(s.getBytes());
    }

    @Override
    public void writeChars(String s) {
        throw new UnsupportedOperationException("writeChars is not supported");
    }

    @Override
    public void writeUTF(String s) {
        throw new UnsupportedOperationException("writeUTF is not supported");
    }
}
