package org.example.factory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerSingleton {
    private Connection conn;
    private static volatile ConnectionManagerSingleton instance;

    private ConnectionManagerSingleton() {
        try {
            String url = "jdbc:mysql://localhost:3306/arquitp1";
            String user = "root";
            String password = "123";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionManagerSingleton getInstance() {
        if (instance == null) {
            synchronized (ConnectionManagerSingleton.class) {
                if (instance == null) {
                    instance = new ConnectionManagerSingleton();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                if (!this.conn.isClosed()) {
                    this.conn.close();
                    System.out.println("Conexión a la base de datos cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}


