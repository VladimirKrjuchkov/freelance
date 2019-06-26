package com.pb.tel.utils.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by vladimir on 26.06.19.
 */
public class MessageHandler {
    private boolean newChainOfThisThred;
    private boolean finish;
    private Set<Long> recordsId;
    private StringBuffer message;
    private static final String header = "\n========= Start =========\n";
    private static boolean useMessageSeparator = false;
    private static final int lenghtLimit = 2000000;
    public static final String startMarker = "<01 startMarker 10>";
    public static final String finishMarker = "<10 finishMarker 01>";
    private Formatter formatter;
    private static ThreadLocal<MessageHandler> handlersStorage = new ThreadLocal<MessageHandler>() {
        protected MessageHandler initialValue() {
            return new MessageHandler(false, false);
        }
    };

    static MessageHandler getInstance(Formatter formatter) {
        MessageHandler instance = (MessageHandler)handlersStorage.get();
        if(instance.formatter == null) {
            instance.formatter = formatter;
        }

        return instance;
    }

    public static boolean isUseMessageSeparator() {
        return useMessageSeparator;
    }

    public static void setUseMessageSeparator(boolean useMessageSeparator) {
        useMessageSeparator = useMessageSeparator;
    }

    boolean isItStartCommand(String msg) {
        return msg.contains("<01 startMarker 10>");
    }

    boolean isItFinishCommand(String msg) {
        return msg.contains("<10 finishMarker 01>");
    }

    public static void initLogWriter() {
        if(useMessageSeparator) {
            printOldMessageIfExist((MessageHandler)handlersStorage.get());
            ((MessageHandler)handlersStorage.get()).setNewChainOfThisThred(true);
            ((MessageHandler)handlersStorage.get()).appendMsg("\n========= Start =========\n");
        }

    }

    public static void initLogWriter(String msg) {
        if(useMessageSeparator) {
            printOldMessageIfExist((MessageHandler)handlersStorage.get());
            ((MessageHandler)handlersStorage.get()).setNewChainOfThisThred(true);
            ((MessageHandler)handlersStorage.get()).appendMsg(msg);
        }

    }

    public static void addMessage(String msg) {
        if(useMessageSeparator && ((MessageHandler)handlersStorage.get()).isNewChainOfThisThred()) {
            ((MessageHandler)handlersStorage.get()).appendMsg(msg);
        }

    }

    public static String getMessage() {
        return useMessageSeparator && ((MessageHandler)handlersStorage.get()).isNewChainOfThisThred()?((MessageHandler)handlersStorage.get()).getMsg():"";
    }

    public static void clearHandlersStorage() {
        if(useMessageSeparator) {
            handlersStorage.remove();
        }

    }

    void initLogWriter(LogRecord record) {
        printOldMessageIfExist(this);
        if(this.formatter.formatMessage(record).replaceAll("<01 startMarker 10>", "").length() > 1) {
            ((MessageHandler)handlersStorage.get()).appendMsg(this.formatter.format(record).replaceAll("<01 startMarker 10>", ""));
        } else {
            ((MessageHandler)handlersStorage.get()).appendMsg("\n========= Start =========\n");
        }

    }

    private static void printOldMessageIfExist(MessageHandler handler) {
        if(handler.isNewChainOfThisThred()) {
            handler.appendMsg("\n   Attention !!! You don't print your message at finish. We printed it instead you. \n========= Finish =========\n\n");
            flushBufer(handler);
            clearHandlersStorage();
        }

    }

    void finishLogWriter(LogRecord record) {
        this.appendMsg(this.formatter.format(record).replaceAll("<10 finishMarker 01>", ""));
        this.appendMsg("========= Finish =========\n\n");
        flushBufer(this);
        clearHandlersStorage();
    }

    void addMessage(LogRecord record) {
        String msg = this.formatter.format(record);
        if(this.getMsg().length() > 2000000) {
            this.cleanLogWriterByLimit(msg);
        } else {
            this.appendMsg(msg);
        }

    }

    private void cleanLogWriterByLimit(String msg) {
        String ref = UUID.randomUUID().toString();
        this.appendMsg(msg);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");
        this.appendMsg("\n\n " + sdf.format(new Date()) + " =========  Violation max length of message (" + ref + ") !!!  =========\n\n");
        flushBufer(this);
        this.clearMsg();
        this.recordsId.clear();
        this.appendMsg("\n\n\n========= Continue (" + ref + ") after violation max length of message !!! =========\n");
    }

    private static void flushBufer(MessageHandler handler) {
        handler.setFinish(true);
        ThreadDiscreteLogPrinter.printMessage();
        handler.setFinish(false);
    }

    private MessageHandler(boolean needPrintMessage, boolean finish) {
        this.recordsId = new HashSet();
        this.message = new StringBuffer();
        this.newChainOfThisThred = needPrintMessage;
        this.finish = finish;
    }

    public boolean isFinish() {
        return this.finish;
    }

    public boolean isNewChainOfThisThred() {
        return this.newChainOfThisThred;
    }

    public void setNewChainOfThisThred(boolean newChainOfThisThred) {
        this.newChainOfThisThred = newChainOfThisThred;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void addRecordId(LogRecord record) {
        this.recordsId.add(Long.valueOf(record.getSequenceNumber()));
    }

    public boolean isContainRecordId(LogRecord record) {
        return this.recordsId.contains(Long.valueOf(record.getSequenceNumber()));
    }

    public void appendMsg(String msg) {
        this.message.append(msg);
    }

    public String getMsg() {
        return this.message.toString();
    }

    public void clearMsg() {
        this.message.delete(0, this.message.length());
    }
}
