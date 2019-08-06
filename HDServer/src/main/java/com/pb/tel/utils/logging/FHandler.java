package com.pb.tel.utils.logging;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by vladimir on 06.08.19.
 */
public class FHandler extends FileHandler {
    File logFile;
    Logger logger;
    File folder;
    String filname;

    public FHandler(String path, boolean append, Logger logger) throws IOException, SecurityException {
        super(path, append);
        this.filname = path;
        this.logFile = new File(path);
        this.logger = logger;
        this.folder = new File(this.logFile.getParent());
    }

    public synchronized void publish(LogRecord record) {
        if(!this.logFile.exists()) {
            try {
                this.close();
                Method mOpenFiles = FileHandler.class.getDeclaredMethod("openFiles", new Class[0]);
                mOpenFiles.setAccessible(true);
                mOpenFiles.invoke(this, new Object[0]);
                this.deleteLCK();
                if(this.filname.substring(this.filname.length() - 3, this.filname.length()).equalsIgnoreCase("err")) {
                    this.logger.severe("=========================>>>  NEW Error LOG FILE, older was been deleted <<<===========================\n");
                } else {
                    this.logger.info("=========================>>>  NEW LOG FILE, older was been deleted <<<===========================\n");
                }
            } catch (IllegalArgumentException var3) {
                var3.printStackTrace();
            } catch (SecurityException var4) {
                var4.printStackTrace();
            } catch (IllegalAccessException var5) {
                var5.printStackTrace();
            } catch (InvocationTargetException var6) {
                var6.printStackTrace();
            } catch (NoSuchMethodException var7) {
                var7.printStackTrace();
            }
        }

        super.publish(record);
        if(this.folder.listFiles().length > 2) {
            this.deleteLCK();
        }

    }

    public void deleteLCK() {
        File[] var1 = this.folder.listFiles();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            File f = var1[var3];
            if(f.isFile() && f.getName().substring(f.getName().length() - 3, f.getName().length()).equalsIgnoreCase("lck")) {
                f.delete();
            }
        }

    }
}

