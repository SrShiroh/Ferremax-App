package com.ferremax.util;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {
    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());

    public static void showError(String message, String title) {
        logger.log(Level.SEVERE, message);
        showMessage(message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(String message, String title) {
        logger.log(Level.WARNING, message);
        showMessage(message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showInfo(String message, String title) {
        logger.log(Level.INFO, message);
        showMessage(message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showMessage(String message, String title, int messageType) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    title,
                    messageType
            );
        });
    }

    public static void logException(Exception ex, String message) {
        logger.log(Level.SEVERE, message, ex);
    }

    public static boolean confirmAction(String message, String title) {
        int option = JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return option == JOptionPane.YES_OPTION;
    }
}
