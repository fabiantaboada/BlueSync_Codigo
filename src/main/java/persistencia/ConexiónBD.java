package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexiónBD {

    private static final String URL = "jdbc:mysql://localhost:3306/bluesync_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "194008"; 

    public static Connection obtenerConexión() {
        Connection conexión = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexión = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (ClassNotFoundException excepción) {
            System.err.println("Controlador JDBC no encontrado: " + excepción.getMessage());
        } catch (SQLException excepción) {
            System.err.println("Error al conectar a MySQL: " + excepción.getMessage());
        }
        return conexión;
    }
}