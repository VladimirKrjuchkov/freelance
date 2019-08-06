package com.pb.tel.utils.io;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vladimir on 06.08.19.
 */
public class TeeServletOutputStream extends ServletOutputStream {
    private final TeeOutputStream targetStream;

    public TeeServletOutputStream(OutputStream one, OutputStream two) {
        this.targetStream = new TeeOutputStream(one, two);
    }

    public void write(int arg0) throws IOException {
        this.targetStream.write(arg0);
    }

    public void flush() throws IOException {
        super.flush();
        this.targetStream.flush();
    }

    public void close() throws IOException {
        super.close();
        this.targetStream.close();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}