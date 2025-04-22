package com.ferremax.util;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities; // Para asegurar que se ejecuta en el EDT

public class ExceptionHandler {

    public static void showError(String message, Exception exception) {
        System.err.println("ERROR: " + message + (exception != null ? " - Excepción: " + exception.getClass().getName() + ": " + exception.getMessage() : ""));
        if (exception != null) {
            exception.printStackTrace(System.err);
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            message,
                            "Error",
                            JOptionPane.ERROR_MESSAGE)
            );
        } else {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            message,
                            "Error",
                            JOptionPane.ERROR_MESSAGE)
            );
        }

    }

    public static void showInfo(String message) {
        System.out.println("INFO: " + message);

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null,
                        message,
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE)
        );
    }

    public static void showWarning(String message) {
        System.err.println("WARN: " + message);

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null,
                        message,
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE)
        );
    }

    public static boolean showConfirm(String message, String title) {
        if (!SwingUtilities.isEventDispatchThread()) {
            final boolean[] result = new boolean[1];
            try {
                SwingUtilities.invokeAndWait(() -> {
                    int choice = JOptionPane.showConfirmDialog(null,
                            message,
                            title,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    result[0] = (choice == JOptionPane.YES_OPTION);
                });
                return result[0];
            } catch (Exception e) {
                System.err.println("Error mostrando diálogo de confirmación: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            int choice = JOptionPane.showConfirmDialog(null,
                    message,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            return (choice == JOptionPane.YES_OPTION);
        }
    }
}
