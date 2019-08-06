package com.pb.tel.utils.logging;

/**
 * Created by vladimir on 06.08.19.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerFactory {
    private static HashMap<String, FileHandler[]> paths = new HashMap();
    private static HashMap<String, ArrayList<String>> loggerFiles = new HashMap();
    private static HashMap<String, Logger> loggers = new HashMap();

    public LoggerFactory() {
    }

    public static Logger getLoger(boolean showClassMetodName, String logerName, String... pathToLog) {
        try {
//            Class path = LoggerFactory.class;
            Logger logger;
            synchronized(LoggerFactory.class) {
                logger = (Logger)loggers.get(logerName);
                if(logger == null) {
                    logger = Logger.getLogger(logerName);
                    loggers.put(logerName, logger);
                }
            }

//            path = null;
            if(pathToLog != null && pathToLog.length != 0 && pathToLog[0] != null) {
                for(int i = 0; i < pathToLog.length; ++i) {
                    File folder = new File(pathToLog[i].substring(0, pathToLog[i].lastIndexOf(File.separator)));
                    if(!folder.exists()) {
                        folder.mkdirs();
                    }

                    String path = pathToLog[i];
                    HashMap var7 = paths;
                    synchronized(paths) {
                        if(!paths.containsKey(path)) {
                            InitHandler(path, showClassMetodName, logger);
                        }
                    }

                    logger.setUseParentHandlers(false);
                    if(isItNewPathForLogger(path, logerName)) {
                        logger.addHandler(((FileHandler[])paths.get(path))[0]);
                        logger.addHandler(((FileHandler[])paths.get(path))[1]);
                    }
                }

                return logger;
            } else {
                System.out.println("=====================================> Error, pathToLog is null <==========================================");
                System.out.println("======> " + new String("Логи сыпятся в err-ый файл сервиса ипользующего логгер. Имя логгера ".getBytes("UTF8")) + logerName + " <=====");
                return logger;
            }
        } catch (Exception var11) {
            System.out.println("====> Error obtaining logger " + var11.toString() + " <===");
            System.err.println("====> Error obtaining logger <===");
            var11.printStackTrace();
            return null;
        }
    }

    private static synchronized boolean isItNewPathForLogger(String path, String logerName) {
        if(loggerFiles.get(logerName) == null) {
            ArrayList<String> loggerPaths = new ArrayList();
            loggerPaths.add(path);
            loggerFiles.put(logerName, loggerPaths);
            return true;
        } else if(((ArrayList)loggerFiles.get(logerName)).contains(path)) {
            return false;
        } else {
            ((ArrayList)loggerFiles.get(logerName)).add(path);
            return true;
        }
    }

    private static synchronized void InitHandler(String path, boolean showClassMetodName, Logger logger) {
        try {
            SimpleLogFormatter simpleLogFormatter = new SimpleLogFormatter();
            simpleLogFormatter.ShowClassMetodName(showClassMetodName);
            FHandler log = new FHandler(path + ".log", true, logger);
            log.setFormatter(simpleLogFormatter);
            log.setFilter(new Filter() {
                public boolean isLoggable(LogRecord record) {
                    return record.getLevel().intValue() <= Level.SEVERE.intValue();
                }
            });
            FHandler err = new FHandler(path + ".err", true, logger);
            err.setFormatter(simpleLogFormatter);
            err.setFilter(new Filter() {
                public boolean isLoggable(LogRecord record) {
                    return record.getLevel().intValue() == Level.SEVERE.intValue();
                }
            });
            paths.put(path, new FileHandler[]{log, err});
        } catch (Exception var6) {
            System.out.println("====> Error obtaining logger (InitHandler) " + var6.toString() + " <===");
            System.err.println("====> Error obtaining logger (InitHandler) <===");
            var6.printStackTrace();
        }

    }

    public static synchronized void RemoveHandler(Logger log, String path) {
        log.removeHandler(((FileHandler[])paths.get(path))[0]);
        log.removeHandler(((FileHandler[])paths.get(path))[1]);
        ((ArrayList)loggerFiles.get(log.getName())).remove(path);
    }
}

