package utils;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LogManager {
    public static void attachLogFile() {
        File logFile = new File("logs/application.log");
        if (logFile.exists()) {
            try (FileInputStream fis = new FileInputStream(logFile)) {
                Allure.addAttachment("Application Logs", fis);
            } catch (IOException e) {
                System.err.println("Failed to attach log file: " + e.getMessage());
            }
        } else {
            System.err.println("Log file not found.");
        }
    }
}
