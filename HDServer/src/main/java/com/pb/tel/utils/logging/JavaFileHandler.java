package com.pb.tel.utils.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * Created by vladimir on 06.08.19.
 */
public class JavaFileHandler extends FileHandler {
    private Level bufferLevel;

    public JavaFileHandler() throws IOException, SecurityException {
        this.configureBufferLevel();
    }

    public JavaFileHandler(String pattern) throws IOException, SecurityException {
        super(pattern);
        this.configureBufferLevel();
    }

    public JavaFileHandler(String pattern, boolean append) throws IOException, SecurityException {
        super(pattern, append);
        this.configureBufferLevel();
    }

    public JavaFileHandler(String pattern, int limit, int count) throws IOException, SecurityException {
        super(pattern, limit, count);
        this.configureBufferLevel();
    }

    public JavaFileHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
        super(pattern, limit, count, append);
        this.configureBufferLevel();
    }

    private void configureBufferLevel() {
        this.bufferLevel = this.getLevel();
        String className = this.getClass().getName();
        this.setBufferLevel(Level.parse(this.getLogManagerProperty(className + ".bufferLevel", "" + this.bufferLevel)));
    }

    private String getLogManagerProperty(String name, String defaultValue) {
        String value = LogManager.getLogManager().getProperty(name);
        if(value == null) {
            value = defaultValue;
        } else {
            value = value.trim();
        }

        return value;
    }

    public synchronized Level getBufferLevel() {
        return this.bufferLevel;
    }

    public synchronized void setBufferLevel(Level bufferLevel) {
        this.bufferLevel = bufferLevel;
    }

    public void publish(LogRecord record) {
        if(MessageHandler.isUseMessageSeparator()) {
            MessageHandler access = MessageHandler.getInstance(this.getFormatter());
            String msg = this.getFormatter().format(record);
            if(access.isItStartCommand(msg)) {
                if(!access.isContainRecordId(record)) {
                    access.initLogWriter(record);
                    MessageHandler.getInstance(this.getFormatter()).setNewChainOfThisThred(true);
                    MessageHandler.getInstance(this.getFormatter()).addRecordId(record);
                }

                return;
            }

            if(access.isItFinishCommand(msg)) {
                if(access.isNewChainOfThisThred()) {
                    access.finishLogWriter(record);
                }

                return;
            }

            if(access.isNewChainOfThisThred() && this.isLoggableForBuffer(record) && !access.isContainRecordId(record) && !access.isFinish()) {
                access.addMessage(record);
                access.addRecordId(record);
            }
        }

        super.publish(record);
    }

    public boolean isLoggableForBuffer(LogRecord record) {
        return super.isLoggable(record) && record.getLevel().intValue() >= this.bufferLevel.intValue();
    }
}
