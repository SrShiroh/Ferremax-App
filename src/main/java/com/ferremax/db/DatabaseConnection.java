package com.ferremax.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final Properties properties = new Properties();
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Error al cargar el driver MySQL", e);
        }

        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                logger.error("No se pudo encontrar el archivo db.properties");
            } else {
                properties.load(input);
                URL = properties.getProperty("db.url");
                USER = properties.getProperty("db.username");
                PASSWORD = properties.getProperty("db.password");

                if (URL == null || USER == null || PASSWORD == null) {
                    logger.error("Faltan una o más propiedades de base de datos en db.properties (db.url, db.username, db.password)");
                }
            }
        } catch (IOException ex) {
            logger.error("Error al leer el archivo db.properties", ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("La configuración de la base de datos no está cargada correctamente. Verifique los logs.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Error al cerrar conexión", e);
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Error al cerrar statement", e);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("Error al cerrar resultset", e);
            }
        }
    }
}
