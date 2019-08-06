//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils.io;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class BufferedHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private PrintWriter writer;

    public BufferedHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.writer = new PrintWriter(this.bos);
    }

    public PrintWriter getWriter() throws IOException {
        return new TeePrintWriter(super.getWriter(), this.writer);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            private TeeOutputStream tee;

            {
                this.tee = new TeeOutputStream(BufferedHttpServletResponseWrapper.super.getOutputStream(), BufferedHttpServletResponseWrapper.this.bos);
            }

            public void write(int b) throws IOException {
                this.tee.write(b);
            }
        };
    }

    public String getContent() {
        return String.valueOf(this.bos);
    }

    public byte[] toByteArray() {
        return this.bos.toByteArray();
    }
}
