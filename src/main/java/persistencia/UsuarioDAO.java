package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public String[] validarInicioDeSesión(String usuario, String clave) {
        String consultaSQL = "SELECT rol, nombre_completo FROM usuarios WHERE usuario = ? AND clave = ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return null;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, usuario);
            sentencia.setString(2, clave);
            try (ResultSet resultados = sentencia.executeQuery()) {
                if (resultados.next()) {
                    String rol = resultados.getString("rol");
                    String rolFinal = (rol.equalsIgnoreCase("ADMIN") || rol.equalsIgnoreCase("Administrador")) ? "ADMIN" : "TRABAJADOR";
                    String nombre = resultados.getString("nombre_completo");
                    return new String[]{ rolFinal, nombre };
                }
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
        }
        return null;
    }

    public boolean existeUsuario(String usuario) {
        String consultaSQL = "SELECT id FROM usuarios WHERE usuario = ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return false;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, usuario);
            try (ResultSet resultados = sentencia.executeQuery()) {
                return resultados.next();
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean registrarUsuario(String usuario, String clave, String rol, String nombreCompleto) {
        String consultaSQL = "INSERT INTO usuarios (usuario, clave, rol, nombre_completo) VALUES (?, ?, ?, ?)";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return false;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, usuario);
            sentencia.setString(2, clave);
            sentencia.setString(3, rol);
            sentencia.setString(4, nombreCompleto);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public String[] obtenerDatosDeUsuario(String usuario) {
        String consultaSQL = "SELECT usuario, clave, rol, nombre_completo FROM usuarios WHERE usuario = ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return null;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, usuario);
            try (ResultSet resultados = sentencia.executeQuery()) {
                if (resultados.next()) {
                    return new String[]{
                        resultados.getString("usuario"),
                        resultados.getString("clave"),
                        resultados.getString("rol"),
                        resultados.getString("nombre_completo")
                    };
                }
            }
        } catch (Exception excepción) {
            excepción.printStackTrace();
        }
        return null;
    }

    public boolean actualizarUsuario(String usuarioOriginal, String nuevoUsuario, String nuevoNombre, String nuevaClave, String nuevoRol) {
        String consultaSQL = "UPDATE usuarios SET usuario = ?, nombre_completo = ?, clave = ?, rol = ? WHERE usuario = ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return false;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, nuevoUsuario);
            sentencia.setString(2, nuevoNombre);
            sentencia.setString(3, nuevaClave);
            sentencia.setString(4, nuevoRol);
            sentencia.setString(5, usuarioOriginal);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(String usuario) {
        String consultaSQL = "DELETE FROM usuarios WHERE usuario = ?";
        Connection conexión = ConexiónBD.obtenerConexión();
        if (conexión == null) return false;

        try (PreparedStatement sentencia = conexión.prepareStatement(consultaSQL)) {
            sentencia.setString(1, usuario);
            return sentencia.executeUpdate() > 0;
        } catch (Exception excepción) {
            excepción.printStackTrace();
            return false;
        }
    }
}