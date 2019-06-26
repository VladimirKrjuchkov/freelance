package com.pb.tel.utils.logging;

import java.util.logging.Logger;

/**
 * Created by vladimir on 26.06.19.
 */
public class ThreadDiscreteLogPrinter {
    static Logger log = Logger.getLogger(ThreadDiscreteLogPrinter.class.getName());

    public ThreadDiscreteLogPrinter() {
    }

    public static void printMessage() {
        if(MessageHandler.isUseMessageSeparator()) {
            log.severe(MessageHandler.getMessage());
        }

    }
}
