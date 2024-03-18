package ru.jdk;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SaverLog {
    public static void saveLog(List<String> log, String logName){
        File logFile = new File("chat/");
        logFile.mkdirs();

        logFile = new File("chat/", logName + "Log.chat");
        try(FileOutputStream serverWriter = new FileOutputStream(logFile, false)) {
            for (String logLine : log) {
                serverWriter.write(logLine.getBytes());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failed save " + logName + " log", JOptionPane.ERROR_MESSAGE);
        }
    }
}
