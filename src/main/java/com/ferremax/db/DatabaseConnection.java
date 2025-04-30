package com.ferremax.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sistema_reparaciones_ac?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERROR: No se pudo conectar a la base de datos MySQL en " + DB_URL);
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Código de Error: " + e.getErrorCode());
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Verifica que el servidor MySQL esté corriendo, la URL, el usuario y la contraseña sean correctos, y la base de datos exista.");
            throw e;
        }
    }

    public static void closeResources(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
            }
        }
    }

    public static void closeResources(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Statement: " + e.getMessage());
            }
        }
    }

    public static void closeResources(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
    }

    public static void closeResources(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
    }

    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        closeResources(rs);
        closeResources(stmt);
        closeResources(conn);
    }

    public static void closeResources(Connection conn, PreparedStatement pstmt) {
        closeResources(pstmt);
        closeResources(conn);
    }

    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        closeResources(rs);
        closeResources(pstmt);
        closeResources(conn);
    }
}
