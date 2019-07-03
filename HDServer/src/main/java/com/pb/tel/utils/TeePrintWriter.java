//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils;

import java.io.PrintWriter;

public class TeePrintWriter extends PrintWriter {
    PrintWriter branch;

    public TeePrintWriter(PrintWriter main, PrintWriter branch) {
        super(main, true);
        this.branch = branch;
    }

    public void write(char[] buf, int off, int len) {
        super.write(buf, off, len);
        super.flush();
        this.branch.write(buf, off, len);
        this.branch.flush();
    }

    public void write(String s, int off, int len) {
        super.write(s, off, len);
        super.flush();
        this.branch.write(s, off, len);
        this.branch.flush();
    }

    public void write(int c) {
        super.write(c);
        super.flush();
        this.branch.write(c);
        this.branch.flush();
    }

    public void flush() {
        super.flush();
        this.branch.flush();
    }
}
