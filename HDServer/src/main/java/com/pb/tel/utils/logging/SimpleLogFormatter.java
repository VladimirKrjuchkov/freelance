package com.pb.tel.utils.logging;

/**
 * Created by vladimir on 06.08.19.
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SimpleLogFormatter extends Formatter {
    Date dat = new Date();
    private boolean showClassMetodname = false;
    private String lineSeparator = "\n";

    public SimpleLogFormatter() {
    }

    public synchronized String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        this.dat.setTime(record.getMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");
        sb.append(formatter.format(this.dat));
        sb.append(" ");
        if(this.showClassMetodname) {
            if(record.getSourceClassName() != null) {
                sb.append(record.getSourceClassName());
            } else {
                sb.append(record.getLoggerName());
            }

            if(record.getSourceMethodName() != null) {
                sb.append(" ");
                sb.append(record.getSourceMethodName());
            }
        }

        String message = this.formatMessage(record);
        sb.append(":  ");
        sb.append(message);
        sb.append(this.lineSeparator);
        if(record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception var7) {
                ;
            }
        }

        return sb.toString();
    }

    public void ShowClassMetodName(boolean show) {
        this.showClassMetodname = show;
    }

    private static void testUWLogFormatter() {
        try {
            ConsoleHandler h = new ConsoleHandler();
            h.setFormatter(new SimpleLogFormatter());
            Logger log = Logger.getAnonymousLogger();
            log.setUseParentHandlers(false);
            log.addHandler(h);
            log.severe("test msg 1 to Console");
            log.severe("test msg 2 to Console");
            log.severe("test msg 3 to Console");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void main(String[] arg) {
        testUWLogFormatter();
    }
}

